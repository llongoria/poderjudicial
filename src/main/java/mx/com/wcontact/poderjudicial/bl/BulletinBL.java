package mx.com.wcontact.poderjudicial.bl;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.bind.JsonbBuilder;
import jakarta.mail.MessagingException;
import jakarta.persistence.criteria.Root;
import mx.com.wcontact.poderjudicial.entity.BulletinME;
import mx.com.wcontact.poderjudicial.entity.HttpQuery;
import mx.com.wcontact.poderjudicial.entity.Notification;
import mx.com.wcontact.poderjudicial.util.CustomHttpUrlConnection;
import mx.com.wcontact.poderjudicial.util.EmailTemplate;
import mx.com.wcontact.poderjudicial.util.HibernateUtil;
import mx.com.wcontact.poderjudicial.util.SSMail;
import org.hibernate.Transaction;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BulletinBL {

    private static final org.jboss.logging.Logger log = org.jboss.logging.Logger.getLogger(BulletinBL.class.getName());
    private org.hibernate.Session session;

    public BulletinBL(){
        session = HibernateUtil.getSessionFactory().openSession();
    }

    public void close(){
        if(session != null){
            session.close();
        }
    }

    public String runQuery(String judged, String date, String paramDate) {
        String url =  "bulletin/"+paramDate;
        CustomHttpUrlConnection http = new CustomHttpUrlConnection();
        try {

            HttpQuery httpQuery = this.existsHttpQuery(judged,date);
            if(httpQuery != null){
                log.info("El query ya fue ejecutado");
                return "El query ya fue ejecutado";
            }
            httpQuery = new HttpQuery(
                    judged
                    ,date
                    ,url
                    , new Date()
            );
            httpQuery.setState(0);
            String urlFormat = String.format("%s?judged=%s&date=%s&url=%s",paramDate, httpQuery.getJudged(), httpQuery.getDate(), httpQuery.getUrl() );
            String resp = http.sendGET(
                    urlFormat
            );
            //log.info(resp);
            if( resp == null || resp.isEmpty() ){
                return "response url is empty";
            }
            resp = resp.replaceAll("´´","");
            log.info( String.format("Obteniendo boletines del Juzgado: %s, Dia: %s", httpQuery.getJudged(), httpQuery.getDate()) );
            JsonArray jsonArray = null;
            try {
                JsonObject mainObject = JsonbBuilder.create().fromJson(resp, JsonObject.class );
                int success = mainObject.getInt("success");
                jsonArray = mainObject.getJsonArray("data");
                httpQuery.setState(success);
                httpQuery.setTotal( jsonArray.size() );
                log.info( String.format("Lista de Boletines Obtenidos estatus=%d, Size=%d",httpQuery.getState(), jsonArray.size() ) );

                Transaction transaction = session.beginTransaction();
                session.persist(httpQuery);
                session.flush();
                transaction.commit();
                if(success != 1) {
                    log.error( String.format("BulletinTimer|execute|El resultado del Query a la Pagina [%s], no fue exitoso: [%d] ", urlFormat, success ) );
                }
            } catch(Exception ex){
                log.error("***** BulletinTimer|execute| Falla al insertar httpQuery *****", ex);
                return "***** BulletinTimer|execute| Falla al insertar httpQuery *****";
            }

            if(jsonArray==null || jsonArray.size() < 1){
                log.error( String.format("BulletinTimer|execute|JsonArray is null a la Pagina [%s], no fue exitoso: [%d] ", urlFormat, httpQuery.getState() ) );
                return "JSON Array is NULL";
            }

            ArrayList<BulletinME> listBulletin = new ArrayList<>();

            for(int i = 0; i < jsonArray.size(); i++){
                JsonObject obj = jsonArray.getJsonObject(i);
                listBulletin.add( fromJson(obj, httpQuery) );
            }

            log.info( String.format("Lista de Boletines Obtenidos Obtenidos de JSON Array=%d", jsonArray.size() ) );

            if(!listBulletin.isEmpty()) {
                updateTableBulletin(listBulletin);
            }

            return "Success: "+ listBulletin.size();

        } catch (IOException | NoSuchAlgorithmException | KeyManagementException e) {
            log.error(e);
        } finally {
            log.info("***** BulletinBL|execute| runQuery end *****");
        }

        return "Error";


    }

    private void updateTableBulletin(List<BulletinME> list){

        try {
            Transaction transaction = session.beginTransaction();
            for(BulletinME bulletin : list){
                try {
                    session.persist(bulletin);

                    if(bulletin.getIdBulletin() != null && bulletin.getIdBulletin() > 0){
                        if(bulletin.getDemNames().toUpperCase().contains("LONGORIA")
                        || bulletin.getActNames().toUpperCase().contains("LONGORIA")){
                            if( findNotification(bulletin.getIdBulletin() ) == null){
                                Notification notification = new Notification();
                                notification.setDestination("llongoria@wcontact.com.mx");
                                notification.setIdBulletin(bulletin.getIdBulletin());
                                notification.setPattern("LONGORIA");
                                notification.setType("EMAIL");
                                try{
                                    SSMail mail = new SSMail();
                                    mail.buildMessage(
                                            "Poder Judicial, Expediente: " + bulletin.getExpediente(),
                                            notification.getDestination(),
                                            EmailTemplate.Coincidencia("Boletin Poder Judicial del Estado", bulletin.getDemNames(), bulletin.getFechaPublicacion().toString(), bulletin.getFechaAcuerdo().toString(), bulletin.getExpediente(),bulletin.getBoletin(),bulletin.getActNames(), bulletin.getDemNames())
                                    );
                                    try {
                                        mail.sendMessage();
                                        notification.setSuccess(1);
                                    } catch (MessagingException e) {
                                        notification.setSuccess(0);
                                    }

                                } catch( Exception ex){
                                    notification.setSuccess(0);
                                }
                                session.persist(notification);
                            }
                        }
                    }
                }catch(Exception ex){
                    log.error( "BulletinTimer|updateTableBulletin|Falla al ejecutar updateTableBulletin|" + bulletin.toString(), ex);
                }
            }
            transaction.commit();

        } catch (Exception ex) {
            log.error( "BulletinTimer|updateTableBulletin|Falla al ejecutar updateTableBulletin", ex);
        }
    }

    public final BulletinME fromJson(jakarta.json.JsonObject jsonObject, HttpQuery httpQuery){
        final BulletinME bulletin = new BulletinME();
        final String defStr = "N/A";
        bulletin.setHttpQueryId( httpQuery.getHttpQueryId() );
        bulletin.setFechaQuery( httpQuery.getQueryDate() );

        for(String key: JSONKeys.ZM_MERCANTIL){
            if(jsonObject.containsKey(key) && !jsonObject.isNull(key)) {
                switch (key){
                    case "EXP":
                        bulletin.setExpediente(jsonObject.getString("EXP"));
                        break;
                    case "BOLETIN":
                        bulletin.setBoletin(jsonObject.getString("BOLETIN"));
                        break;
                    case "TIPO":
                        bulletin.setTipo(jsonObject.getString("TIPO"));
                        break;
                    case "NOTIFICACI":
                        bulletin.setNotificacion(jsonObject.getString("NOTIFICACI"));
                        break;
                    case "DI":
                        bulletin.setDi(jsonObject.getString("DI"));
                        break;
                    case "CVE_JUI":
                        bulletin.setClaveJuicio(jsonObject.getString("CVE_JUI"));
                        break;
                    case "CVE_JUZ":
                        bulletin.setClaveJuzgado(jsonObject.getString("CVE_JUZ"));
                        break;
                    case "DESCRIP":
                        bulletin.setDescripcion(jsonObject.getString("DESCRIP"));
                        break;
                    case "act_names":
                        bulletin.setActNames(jsonObject.getString("act_names"));
                        break;
                    case "dem_names":
                        try {
                            bulletin.setDemNames(jsonObject.getString("dem_names"));
                        }catch(Exception ex){
                            log.error("BulletinBL|fromJson| dem_names no pudo ser convertido a String, Objeto= " + jsonObject.toString() );
                        }
                        break;
                    case "FCH_PRO":
                        OffsetDateTime odt1 = OffsetDateTime.parse(jsonObject.getString("FCH_PRO")); // "2024-09-10T00:00:00.000Z"
                        bulletin.setFechaPublicacion(Date.from(odt1.toInstant()));
                        break;
                    case "FCH_ACU":
                        OffsetDateTime odt2 = OffsetDateTime.parse(jsonObject.getString("FCH_ACU"));
                        bulletin.setFechaAcuerdo(Date.from(odt2.toInstant()));
                        break;
                    case "FCH_RES":
                        OffsetDateTime odt3 = OffsetDateTime.parse(jsonObject.getString("FCH_RES"));
                        bulletin.setFechaResolucion( Date.from( odt3.toInstant() )  );
                        break;
                    default:
                        log.warn("BulletinBL|fromJson| Cadena :" + key + ", No mapeado a un objeto");
                }
            }
        }

        return bulletin;
    }

    public HttpQuery existsHttpQuery(String judged, String date) {

        HibernateCriteriaBuilder cb = session.getCriteriaBuilder();
        jakarta.persistence.criteria.CriteriaQuery<HttpQuery> cq = cb.createQuery(HttpQuery.class);
        Root<HttpQuery> root = cq.from(HttpQuery.class);

        cq.select(root);
        cq.where(
                cb.and(
                        cb.equal( root.get("judged"), judged),
                        cb.equal( root.get("date"), date )
                )
        );
        List<HttpQuery> list = session.createQuery(cq).getResultList();
        return list.isEmpty() ? null : list.getFirst();
    }

    public Notification findNotification(Long idBulletin){
        HibernateCriteriaBuilder cb = session.getCriteriaBuilder();
        jakarta.persistence.criteria.CriteriaQuery<Notification> cq = cb.createQuery(Notification.class);
        Root<Notification> root = cq.from(Notification.class);

        cq.select(root);
        cq.where(
                cb.equal( root.get("idBulletin"), idBulletin)
        );
        List<Notification> list = session.createQuery(cq).getResultList();
        return list.isEmpty() ? null : list.getFirst();
    }


    public List<HttpQuery> findCountHttpQuery() {
        HibernateCriteriaBuilder cb = session.getCriteriaBuilder();
        jakarta.persistence.criteria.CriteriaQuery<HttpQuery> cq = cb.createQuery(HttpQuery.class);
        Root<HttpQuery> root = cq.from(HttpQuery.class);
        cq.select(root);
        cq.where(
                cb.greaterThan(root.get("total"), 0)
        );
        return session.createQuery(cq).getResultList();
    }
}
