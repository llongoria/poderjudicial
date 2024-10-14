package mx.com.wcontact.poderjudicial.bl;

import jakarta.persistence.criteria.Root;
import mx.com.wcontact.poderjudicial.entity.Notification;
import mx.com.wcontact.poderjudicial.util.HibernateUtil;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;

import java.util.List;

public final class NotificacionBL extends AbstractBL{

    private static final org.jboss.logging.Logger log = org.jboss.logging.Logger.getLogger(NotificacionBL.class.getName());


    public NotificacionBL(org.hibernate.Session session){
        super(session);
    }

    public Notification findNotification(Long idBulletin){
        List<Notification> list = null;
        try {
            HibernateCriteriaBuilder cb = getSession().getCriteriaBuilder();
            jakarta.persistence.criteria.CriteriaQuery<Notification> cq = cb.createQuery(Notification.class);
            Root<Notification> root = cq.from(Notification.class);
            cq.select(root);
            cq.where(
                    cb.and(
                            cb.equal(root.get("idBulletin"), idBulletin),
                            cb.equal(root.get("success"),1)
                    )
            );
            list = getSession().createQuery(cq).getResultList();
        } finally {  }
        return list.isEmpty() ? null : list.getFirst();
    }
}
