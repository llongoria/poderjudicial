package mx.com.wcontact.poderjudicial.bl;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.bind.JsonbBuilder;
import jakarta.mail.MessagingException;
import jakarta.persistence.criteria.Root;
import mx.com.wcontact.poderjudicial.entity.BulletinME;
import mx.com.wcontact.poderjudicial.entity.HttpQuery;
import mx.com.wcontact.poderjudicial.entity.Notification;
import mx.com.wcontact.poderjudicial.listener.PJContextListener;
import mx.com.wcontact.poderjudicial.opensearch.bl.BulletinMeOS;
import mx.com.wcontact.poderjudicial.util.*;
import org.hibernate.Transaction;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BulletinBL {

    private static final String PATH_DIR_BULLETINERROR = "/home/llongoria/PoderJudicial/BulletinError";
    private static final org.jboss.logging.Logger log = org.jboss.logging.Logger.getLogger(BulletinBL.class.getName());
    private final transient java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private org.hibernate.Session hibernateSession;

    public BulletinBL(){}


    public org.hibernate.Transaction createTransaction() {
        return getSession().beginTransaction();
    }

    public org.hibernate.Session getSession(){
        if ( hibernateSession == null || !hibernateSession.isOpen()){
            hibernateSession = HibernateUtil.getSessionFactory().openSession();
        }
        return hibernateSession;
    }

    public void close(){
        if( hibernateSession != null){
            hibernateSession.close();
            hibernateSession = null;
        }
    }



    public Result<HttpQuery> runQuery(String judged, String date, String paramDate, boolean isOpenSearchActive) {
        String url =  "bulletin/"+paramDate;
        int resultStateHttpquery = 0;
        CustomHttpUrlConnection http = new CustomHttpUrlConnection();
        HttpQueryBL httpQueryBL = new HttpQueryBL();
        try {
            HttpQuery httpQuery = httpQueryBL.existsHttpQuery(judged,date);
            if(httpQuery != null){
                log.info( String.format("***** runQuery| El query Judge: %s, Date: %s, Total: %d, ya fue ejecutado.",judged,date,httpQuery.getTotal() ) );
                return new Result<HttpQuery>(resultStateHttpquery,httpQuery,"El query ya fue ejecutado");
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
                return new Result<HttpQuery>(resultStateHttpquery,httpQuery,"response url is empty");
            }
            resp = resp.replaceAll("´´","");
            log.info( String.format("***** runQuery| Obteniendo boletines del Juzgado: %s, Dia: %s", httpQuery.getJudged(), httpQuery.getDate()) );
            JsonArray jsonArray = null;
            try {
                JsonObject mainObject = JsonbBuilder.create().fromJson(resp, JsonObject.class );
                int success = mainObject.getInt("success");
                jsonArray = mainObject.getJsonArray("data");
                httpQuery.setState(success);
                httpQuery.setTotal( jsonArray.size() );

                log.info( String.format("***** runQuery| Lista de Boletines Obtenidos estatus=%d, Size=%d",httpQuery.getState(), jsonArray.size() ) );
                Result<HttpQuery> result = httpQueryBL.save(httpQuery,isOpenSearchActive);
                httpQuery = result.getObject();
                resultStateHttpquery = 1;
                if(success != 1) {
                    log.error( String.format("runQuery|El resultado del Query a la Pagina [%s], no fue exitoso: [%d] ", urlFormat, success ) );
                }
            } catch(Exception ex){
                log.error("***** runQuery| Falla al insertar httpQuery *****", ex);
                return new Result<HttpQuery>(resultStateHttpquery, httpQuery, "***** runQuery| Falla al insertar httpQuery *****");
            }

            if(jsonArray==null || jsonArray.size() < 1){
                log.error( String.format("***** runQuery| No existen boletines en la siguiente liga: [%s], estatus: [%d] ", urlFormat, httpQuery.getState() ) );
                return new Result<HttpQuery>(resultStateHttpquery, httpQuery, String.format("***** runQuery| No existen boletines en la siguiente liga: [%s], estatus: [%d] ", urlFormat, httpQuery.getState() ));
            }

            ArrayList<BulletinME> listBulletin = new ArrayList<>();

            for(int i = 0; i < jsonArray.size(); i++){
                JsonObject obj = jsonArray.getJsonObject(i);
                listBulletin.add( fromJson(obj, httpQuery) );
            }

            log.info( String.format("***** runQuery| Lista de Boletines Obtenidos Obtenidos de JSON Array=%d", jsonArray.size() ) );

            if(!listBulletin.isEmpty()) {
                updateTableBulletin(listBulletin,isOpenSearchActive);
            }

            return new Result<HttpQuery>(resultStateHttpquery, httpQuery, "***** runQuery| Success: "+ listBulletin.size() +" *****");

        } catch (IOException | NoSuchAlgorithmException | KeyManagementException | ParseException e) {
            log.error(e);
        } finally {
            log.info("***** runQuery|  runQuery end *****");
        }
        return new Result<HttpQuery>(resultStateHttpquery, null, "***** runQuery| Error *****");


    }

    private void updateTableBulletin(List<BulletinME> list,  boolean isOpenSearchActive){
        BulletinMeOS bulletinMeOS = isOpenSearchActive ? new BulletinMeOS() : null;
        try {
            for(BulletinME bulletin : list){
                save(bulletin,bulletinMeOS);
            }
        } catch (Exception ex) {
            log.error( "updateTableBulletin| Falla al ejecutar updateTableBulletin", ex);
        }
    }

    private void save(BulletinME bulletin, BulletinMeOS bulletinMeOS){
        Transaction transaction = createTransaction();
        try {
            getSession().persist(bulletin);
            if(bulletin.getIdBulletin() != null && bulletin.getIdBulletin() > 0){
                String pattern = checkPattern(
                        PJContextListener.getCfg().getSendMailPatterns(),
                        new String[]{ bulletin.getDemNames().toUpperCase(), bulletin.getActNames().toUpperCase() }
                );
                if( pattern != null ){
                    if( findNotification(bulletin.getIdBulletin() ) == null){
                        Notification notification = new Notification();
                        notification.setDestination("llongoria@wcontact.com.mx");
                        notification.setIdBulletin(bulletin.getIdBulletin());
                        notification.setPattern( pattern );
                        notification.setType("EMAIL");
                        try{
                            SSMail mail = new SSMail();
                            mail.buildMessage(
                                    PJContextListener.getCfg().getMailSubject() + bulletin.getExpediente(),
                                    notification.getDestination(),
                                    EmailTemplate.Coincidencia(PJContextListener.getCfg().getMailTemplateTitle(), bulletin.getDemNames(), bulletin.getFechaPublicacion().toString(), bulletin.getFechaAcuerdo().toString(), bulletin.getExpediente(),bulletin.getBoletin(),bulletin.getActNames(), bulletin.getDemNames())
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
                        getSession().persist(notification);
                    }
                }

                if(bulletinMeOS != null){
                    bulletinMeOS.createDocument("pj-bulletin-me",bulletin);
                }
                transaction.commit();
                return;
            }
            transaction.rollback();
        } catch(Exception ex){
            log.error( "save| Falla al ejecutar updateTableBulletin|" + bulletin.toString(), ex);
            transaction.rollback();
            WriteFileToJson(PATH_DIR_BULLETINERROR, bulletin, ex);
        } finally {
            getSession().close();
        }
    }

    public final BulletinME fromJson(jakarta.json.JsonObject jsonObject, HttpQuery httpQuery) throws ParseException {
        final BulletinME bulletin = new BulletinME();
        bulletin.setHttpQueryId( httpQuery.getHttpQueryId() );
        bulletin.setHttpQueryDate( httpQuery.getDate() );
        bulletin.setFechaQuery( sdf.parse( httpQuery.getQueryDate() ) );

        for(String key: JSONKeys.ZM_MERCANTIL){
            if(jsonObject.containsKey(key) && !jsonObject.isNull(key)) {
                switch (key){
                    case "EXP":
                        bulletin.setExpediente(jsonObject.getString("EXP"));
                        break;
                    case "BOLETIN":
                        bulletin.setBoletin(
                                ( jsonObject.getString("BOLETIN") != null ? replaceNonASCII(jsonObject.getString("BOLETIN")).toUpperCase()
                                        : jsonObject.getString("BOLETIN") )
                        );
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
                        try {
                            bulletin.setActNames( replaceNonASCII(jsonObject.getString("act_names")).toUpperCase() );
                        }catch(Exception ex){
                            log.warn("***** fromJson| act_names no pudo ser convertido a String, Objeto= %s".formatted( jsonObject ) );
                        }
                        break;
                    case "dem_names":
                        try {
                            bulletin.setDemNames(replaceNonASCII(jsonObject.getString("dem_names")).toUpperCase());
                        }catch(Exception ex){
                            log.warn("***** fromJson| dem_names no pudo ser convertido a String, Objeto= %s".formatted(jsonObject));
                        }
                        break;
                    case "FCH_PRO":
                        String fecha1 = jsonObject.getString("FCH_PRO");
                        if(verifyDateFormat(fecha1)) {
                            OffsetDateTime odt1 = OffsetDateTime.parse(jsonObject.getString("FCH_PRO")); // "2024-09-10T00:00:00.000Z"
                            bulletin.setFechaPublicacion(Date.from(odt1.toInstant()));
                        } else {
                            log.warn(String.format("***** fromJson| Juzgado [%s], Expediente[%s], Cadena [%s], con valor: [%s] no tiene el formato [2024-09-10T00:00:00.000Z]",bulletin.getClaveJuzgado(), bulletin.getExpediente(), key, fecha1 ));
                        }
                        break;
                    case "FCH_ACU":
                        String fecha2 = jsonObject.getString("FCH_ACU");
                        if(verifyDateFormat(fecha2)) {
                            OffsetDateTime odt2 = OffsetDateTime.parse(jsonObject.getString("FCH_ACU"));
                            bulletin.setFechaAcuerdo(Date.from(odt2.toInstant()));
                        } else {
                            log.warn(String.format("***** fromJson| Juzgado [%s], Expediente[%s], Cadena [%s], con valor: [%s] no tiene el formato [2024-09-10T00:00:00.000Z]",bulletin.getClaveJuzgado(), bulletin.getExpediente(), key, fecha2 ));
                        }
                        break;
                    case "FCH_RES":
                        String fecha3 = jsonObject.getString("FCH_RES");
                        if(verifyDateFormat(fecha3)) {
                            OffsetDateTime odt3 = OffsetDateTime.parse(jsonObject.getString("FCH_RES"));
                            bulletin.setFechaResolucion(Date.from(odt3.toInstant()));
                        } else {
                            log.warn(String.format("***** fromJson| Juzgado [%s], Expediente[%s], Cadena [%s], con valor: [%s] no tiene el formato [2024-09-10T00:00:00.000Z]",bulletin.getClaveJuzgado(), bulletin.getExpediente(), key, fecha3 ));
                        }
                        break;
                    default:
                        log.warn("fromJson| Cadena: %s, No mapeado a un objeto".formatted(key));
                }
            }
        }

        return bulletin;
    }

    boolean verifyDateFormat(String dateString) {
        if(dateString == null || dateString.isEmpty()) {
            return false;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        try {
            LocalDate dateParser = LocalDate.parse(dateString, formatter);
            return Fecha.isBetweenSqlServerDates(dateParser);

        } catch (DateTimeParseException e) {
            return false;
        }
    }



    public Notification findNotification(Long idBulletin){
        List<Notification> list = null;
        try {
            HibernateCriteriaBuilder cb = getSession().getCriteriaBuilder();
            jakarta.persistence.criteria.CriteriaQuery<Notification> cq = cb.createQuery(Notification.class);
            Root<Notification> root = cq.from(Notification.class);
            cq.select(root);
            cq.where(
                    cb.equal(root.get("idBulletin"), idBulletin)
            );
            list = getSession().createQuery(cq).getResultList();
        } finally {  }
        return list.isEmpty() ? null : list.getFirst();
    }

    public String checkPattern(List<String> patterns, String[] data){
        List<String> datas = Arrays.stream(data).toList();
        for(String ele: datas){
            for(String pattern: patterns){
                if(ele.contains(pattern)){
                    return pattern;
                }
            }
        }
        return null;
    }

    public String replaceNonASCII(String input) {
        String normalize = Normalizer.normalize(input, Normalizer.Form.NFD);
        return normalize.replaceAll("[^\\p{ASCII}]", "");
    }


    public void WriteFileToJson(String dir, BulletinME bulletinME,Exception e){
        String fileName = String.format("%d_%s_%s_%s.json", bulletinME.getHttpQueryId(),bulletinME.getClaveJuicio(),bulletinME.getClaveJuzgado(),bulletinME.getExpediente().replaceAll("/","-")  );
        File file = new File("%s/%s".formatted(dir,fileName));
        try {
            final jakarta.json.JsonObjectBuilder objectBuilder = jakarta.json.Json.createObjectBuilder();
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWritter = new FileWriter(file, false);
            BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
            objectBuilder.add("exceptionMessage", e.getMessage() );
            objectBuilder.add("fileName", file.getName() );
            objectBuilder.add("bulletin",bulletinME.toJSON() );
            bufferWritter.write( objectBuilder.build().toString() );
            bufferWritter.close();
        } catch (Exception ex) {
            log.error("WriteFileToJson| Error al guardar el archivo en la ruta: %s".formatted(file.getAbsoluteFile()));
        }
    }
}
