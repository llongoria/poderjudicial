package mx.com.wcontact.poderjudicial.bl;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.bind.JsonbBuilder;
import jakarta.persistence.criteria.Root;
import mx.com.wcontact.poderjudicial.entity.Judge;
import mx.com.wcontact.poderjudicial.util.CustomHttpUrlConnection;
import mx.com.wcontact.poderjudicial.util.HibernateUtil;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public final class JudgeBL extends AbstractBL{

    private static final org.jboss.logging.Logger log = org.jboss.logging.Logger.getLogger(JudgeBL.class.getName());

    public JudgeBL(org.hibernate.Session session){
        super(session);
    }

    public JudgeBL(){
        super();
    }


    public String runQuery(String type) {
        CustomHttpUrlConnection http = new CustomHttpUrlConnection();
        try {
            // Ejemplo: penal_oral_judges?url=bulletin/penal_oral_judges
            String urlFormat = String.format("%s?url=bulletin/%s", type, type );
            String resp = http.sendGET(
                    urlFormat
            );
            String url = http.getUrl();
            //log.info(resp);
            if( resp == null || resp.isEmpty() ){
                return "response url is empty";
            }
            resp = resp.replaceAll("´´","");
            JsonObject mainObject = JsonbBuilder.create().fromJson(resp, JsonObject.class );
            int success = mainObject.getInt("success");
            JsonArray jsonArray = mainObject.getJsonArray("data");


            ArrayList<Judge> list = new ArrayList<>();

            for(int i = 0; i < jsonArray.size(); i++){
                JsonObject obj = jsonArray.getJsonObject(i);
                list.add( fromJson(obj, type, url) );
            }

            if(!list.isEmpty()) {
                updateTableJudge(list);
            }
            return "Success: "+ list.size();

        } catch (IOException | NoSuchAlgorithmException | KeyManagementException e) {
            log.error(e);
        } finally {
            log.info("***** BulletinBL|execute| runQuery end *****");
        }

        return "Error";


    }

    private void updateTableJudge(List<Judge> list){

        try {
            Transaction transaction = createTransaction();
            for(Judge judge : list){
                if(findCountJudge(judge.getValue()) == null) {
                    getSession().persist(judge);
                }
            }
            transaction.commit();

        } catch (Exception ex) {
            log.error( "BulletinTimer|updateTableBulletin|Falla al ejecutar updateTableBulletin", ex);
        }
    }

    public Judge findCountJudge(String value) {
        HibernateCriteriaBuilder cb = getSession().getCriteriaBuilder();
        jakarta.persistence.criteria.CriteriaQuery<Judge> cq = cb.createQuery(Judge.class);
        Root<Judge> root = cq.from(Judge.class);
        cq.select(root);
        cq.where(
                cb.equal(root.get("value"), value)
        );
        List<Judge> list = getSession().createQuery(cq).getResultList();
        return  list.isEmpty() ? null : list.getFirst();
    }

    public List<Judge> findAll(){
        HibernateCriteriaBuilder cb = getSession().getCriteriaBuilder();
        jakarta.persistence.criteria.CriteriaQuery<Judge> cq = cb.createQuery(Judge.class);
        cq.select(cq.from(Judge.class));
        return getSession().createQuery(cq).getResultList();
    }

    public List<Judge> findINvalue(Object[] columns){
        HibernateCriteriaBuilder cb = getSession().getCriteriaBuilder();
        jakarta.persistence.criteria.CriteriaQuery<Judge> cq = cb.createQuery(Judge.class);
        Root<Judge> root = cq.from(Judge.class);
        cq.select(root);
        cq.where(root.get("value").in(columns));
        return getSession().createQuery(cq).getResultList();
    }

    public List<Object[]> findCountJudges() {
        String sql = "SELECT type, COUNT(idJudge) as total\n" +
                "FROM [PoderJudicial].[dbo].[judged]\n" +
                "GROUP BY (type) ORDER BY type;";
        Query query = getSession().createNativeQuery(sql);
        return query.getResultList();

    }

    public final Judge fromJson(jakarta.json.JsonObject jsonObject, String type, String url) {
        final Judge judge = new Judge();
        judge.setType( type );
        judge.setUrl( url );
        final String defStr = "N/A";

        if(jsonObject.containsKey("value") && !jsonObject.isNull("value")) {
            judge.setValue(jsonObject.getString("value"));
        } else {
            judge.setValue(defStr);
        }

        if(jsonObject.containsKey("name") && !jsonObject.isNull("name")) {
            judge.setName(jsonObject.getString("name"));
        } else {
            judge.setName(defStr);
        }

        if(jsonObject.containsKey("secretary") && !jsonObject.isNull("secretary")) {
            judge.setSecretary(jsonObject.getString("secretary"));
        } else {
            judge.setSecretary(defStr);
        }

        if(jsonObject.containsKey("position") && !jsonObject.isNull("position")) {
            judge.setPosition(jsonObject.getString("position"));
        } else {
            judge.setPosition(defStr);
        }

        if(jsonObject.containsKey("address") && !jsonObject.isNull("address")) {
            judge.setAddress(jsonObject.getString("address"));
        } else {
            judge.setAddress(defStr);
        }
        return judge;
    }
}
