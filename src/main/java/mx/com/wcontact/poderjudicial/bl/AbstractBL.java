package mx.com.wcontact.poderjudicial.bl;

import mx.com.wcontact.poderjudicial.util.HibernateUtil;

public abstract class AbstractBL {

    protected final transient java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    protected org.hibernate.Session hibernateSession;

    public AbstractBL(org.hibernate.Session session){
        this.hibernateSession = session;
    }

    public AbstractBL(){
        getSession();
    }

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
}
