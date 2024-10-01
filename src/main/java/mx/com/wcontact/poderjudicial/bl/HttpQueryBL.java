package mx.com.wcontact.poderjudicial.bl;

import jakarta.persistence.criteria.Root;
import mx.com.wcontact.poderjudicial.entity.HttpQuery;
import mx.com.wcontact.poderjudicial.opensearch.bl.HttpQueryOS;
import mx.com.wcontact.poderjudicial.util.HibernateUtil;
import mx.com.wcontact.poderjudicial.util.Result;
import org.hibernate.Transaction;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;

import java.util.List;

public class HttpQueryBL {

    private static final String PATH_DIR_BULLETINERROR = "/home/llongoria/PoderJudicial/BulletinError";
    private static final org.jboss.logging.Logger log = org.jboss.logging.Logger.getLogger(HttpQueryBL.class.getName());
    private final transient java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private org.hibernate.Session hibernateSession;

    public HttpQueryBL(){

    }

    public org.hibernate.Transaction createTransaction() {
        return getSession().beginTransaction();
    }

    public org.hibernate.Session getSession(){
        if ( hibernateSession == null){
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


    public Result<HttpQuery> save(HttpQuery httpQuery, boolean isOpenSearchActive) {
        Transaction transaction = createTransaction();
        getSession().persist(httpQuery);
        transaction.commit();
        this.close();

        if(isOpenSearchActive){
            HttpQueryOS httpQueryOS = null;
            try {
                httpQueryOS = new HttpQueryOS();
                if(httpQuery.getRowCreated() == null){
                    httpQuery.setRowCreated(new java.util.Date());
                }
                //httpQueryOS.createDocument("pj-httpquery",httpQuery);
            } catch(Exception ex){
                log.error("***** BulletinTimer|execute| Falla al enviar los datos a Opensearch *****", ex);
            }
        }
        return new Result<>(1,httpQuery,"OK");
    }

    public HttpQuery existsHttpQuery(String judged, String date) {
        List<HttpQuery> list = null;
        try {
            HibernateCriteriaBuilder cb = getSession().getCriteriaBuilder();
            jakarta.persistence.criteria.CriteriaQuery<HttpQuery> cq = cb.createQuery(HttpQuery.class);
            Root<HttpQuery> root = cq.from(HttpQuery.class);

            cq.select(root);
            cq.where(
                    cb.and(
                            cb.equal(root.get("judged"), judged),
                            cb.equal(root.get("date"), date)
                    )
            );
            list = getSession().createQuery(cq).getResultList();
        }finally {
            close();
        }
        return list.isEmpty() ? null : list.getFirst();
    }

    public List<HttpQuery> findCountHttpQuery() {
        try {
            HibernateCriteriaBuilder cb = getSession().getCriteriaBuilder();
            jakarta.persistence.criteria.CriteriaQuery<HttpQuery> cq = cb.createQuery(HttpQuery.class);
            Root<HttpQuery> root = cq.from(HttpQuery.class);
            cq.select(root);
            cq.where(
                    cb.greaterThan(root.get("total"), 0)
            );
            return getSession().createQuery(cq).getResultList();
        } finally {
            close();
        }
    }

}
