package mx.com.wcontact.poderjudicial.entity;

import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(   catalog = "PoderJudicial"
        , schema = "dbo"
        , name = "notification"
        , uniqueConstraints = {
        @UniqueConstraint(columnNames = {"idNotification"})
}
)
public class Notification implements Serializable {
    private static final long serialVersionUID = 3L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long idNotification;

    @Basic(optional = false)
    @Column(nullable = false)
    private Long idBulletin;

    @Basic(optional = false)
    @Column(nullable = false, length = 50)
    private String pattern;

    /**
     * SMS/Phone/EMAIL
     */
    @Basic(optional = false)
    @Column(nullable = true, length = 10)
    private String type;

    @Basic(optional = false)
    @Column(nullable = false, length = 200)
    private String destination;

    @Basic(optional = false)
    @Column(nullable = false)
    private Integer success;

    @Column(name = "rowCreated", insertable = false, updatable = false)
    @Basic(optional = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonbDateFormat(value = "yyyy-MM-dd HH:mm:ss")
    private Date rowCreated;

    public Notification() {}

    public Long getIdNotification() {
        return idNotification;
    }

    public void setIdNotification(Long idNotification) {
        this.idNotification = idNotification;
    }

    public Long getIdBulletin() {
        return idBulletin;
    }

    public void setIdBulletin(Long idBulletin) {
        this.idBulletin = idBulletin;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public Date getRowCreated() {
        return rowCreated;
    }

    public void setRowCreated(Date rowCreated) {
        this.rowCreated = rowCreated;
    }
}
