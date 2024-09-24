package mx.com.wcontact.poderjudicial.entity;

import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(   catalog = "PoderJudicial"
        , schema = "dbo"
        , name = "BulletinME"
        , uniqueConstraints = { @UniqueConstraint(columnNames = {"idBulletin"}) }
)
public class BulletinME implements Serializable {

    private static final long serialVersionUID = 3L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long idBulletin;

    @Basic(optional = false)
    @Column(nullable = false)
    private Long httpQueryId;

    @Basic(optional = false)
    @Column(nullable = false, length = 50)
    private String expediente;

    @Column(name = "fechaPublicacion")
    @Basic(optional = true)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonbDateFormat(value = "yyyy-MM-dd HH:mm:ss" )
    private Date fechaPublicacion;

    @Column(name = "fechaAcuerdo")
    @Basic(optional = true)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonbDateFormat(value = "yyyy-MM-dd HH:mm:ss" )
    private Date fechaAcuerdo;

    @Basic(optional = false)
    @Column(nullable = false, length = 10000)
    private String boletin = "N/A";

    @Basic(optional = false)
    @Column(nullable = false, length = 10)
    private String tipo = "N/A";

    @Basic(optional = false)
    @Column(nullable = false, length = 10)
    private String notificacion = "N/A";

    @Basic(optional = false)
    @Column(nullable = false, length = 10)
    private String di = "N/A";

    @Column(name = "fechaResolucion")
    @Basic(optional = true)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonbDateFormat(value = "yyyy-MM-dd HH:mm:ss" )
    private Date fechaResolucion;

    @Basic(optional = false)
    @Column(nullable = false, length = 50)
    private String claveJuicio = "N/A";

    @Basic(optional = false)
    @Column(nullable = true, length = 20)
    private String claveJuzgado = "N/A";

    @Basic(optional = false)
    @Column(nullable = false, length = 50)
    private String descripcion = "N/A";

    @Basic(optional = false)
    @Column(nullable = false, length = 10000)
    private String actNames = "N/A";

    @Basic(optional = false)
    @Column(nullable = false, length = 10000)
    private String demNames = "N/A";

    @Column(name = "fechaQuery")
    @Basic(optional = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonbDateFormat(value = "yyyy-MM-dd HH:mm:ss" )
    private Date fechaQuery;

    @Column(name = "rowCreated", insertable = false, updatable = false)
    @Basic(optional = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonbDateFormat(value = "yyyy-MM-dd HH:mm:ss")
    private Date rowCreated;

    public BulletinME() { }

    public BulletinME(Long httpQueryId, String expediente, Date fechaPublicacion, Date fechaAcuerdo, String boletin,
                      String tipo, String notificacion, String di, Date fechaResolucion, String claveJuicio, String claveJuzgado,
                      String descripcion, String actNames, String demNames, Date fechaQuery) {
        this.httpQueryId = httpQueryId;
        this.expediente = expediente;
        this.fechaPublicacion = fechaPublicacion;
        this.fechaAcuerdo = fechaAcuerdo;
        this.boletin = boletin;
        this.tipo = tipo;
        this.notificacion = notificacion;
        this.di = di;
        this.fechaResolucion = fechaResolucion;
        this.claveJuicio = claveJuicio;
        this.claveJuzgado = claveJuzgado;
        this.descripcion = descripcion;
        this.actNames = actNames;
        this.demNames = demNames;
        this.fechaQuery = fechaQuery;
    }

    public Long getIdBulletin() {
        return idBulletin;
    }

    public void setIdBulletin(Long idBulletin) {
        this.idBulletin = idBulletin;
    }

    public Long getHttpQueryId() {
        return httpQueryId;
    }

    public void setHttpQueryId(Long httpQueryId) {
        this.httpQueryId = httpQueryId;
    }

    public String getExpediente() {
        return expediente;
    }

    public void setExpediente(String expediente) {
        this.expediente = expediente;
    }

    public Date getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(Date fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public Date getFechaAcuerdo() {
        return fechaAcuerdo;
    }

    public void setFechaAcuerdo(Date fechaAcuerdo) {
        this.fechaAcuerdo = fechaAcuerdo;
    }

    public String getBoletin() {
        return boletin;
    }

    public void setBoletin(String boletin) {
        this.boletin = boletin;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNotificacion() {
        return notificacion;
    }

    public void setNotificacion(String notificacion) {
        this.notificacion = notificacion;
    }

    public String getDi() {
        return di;
    }

    public void setDi(String di) {
        this.di = di;
    }

    public Date getFechaResolucion() {
        return fechaResolucion;
    }

    public void setFechaResolucion(Date fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
    }

    public String getClaveJuicio() {
        return claveJuicio;
    }

    public void setClaveJuicio(String claveJuicio) {
        this.claveJuicio = claveJuicio;
    }

    public String getClaveJuzgado() {
        return claveJuzgado;
    }

    public void setClaveJuzgado(String claveJuzgado) {
        this.claveJuzgado = claveJuzgado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getActNames() {
        return actNames;
    }

    public void setActNames(String actNames) {
        this.actNames = actNames;
    }

    public String getDemNames() {
        return demNames;
    }

    public void setDemNames(String demNames) {
        this.demNames = demNames;
    }

    public Date getFechaQuery() {
        return fechaQuery;
    }

    public void setFechaQuery(Date fechaQuery) {
        this.fechaQuery = fechaQuery;
    }

    public Date getRowCreated() {
        return rowCreated;
    }

    public void setRowCreated(Date rowCreated) {
        this.rowCreated = rowCreated;
    }

    @Override
    public String toString() {
        return STR."Bulletin{idBulletin=\{idBulletin}, httpQueryId=\{httpQueryId}, expediente='\{expediente}\{'\''}, fechaPublicacion=\{fechaPublicacion}, fechaAcuerdo=\{fechaAcuerdo}, boletin='\{boletin}\{'\''}, tipo='\{tipo}\{'\''}, notificacion='\{notificacion}\{'\''}, di='\{di}\{'\''}, fechaResolucion=\{fechaResolucion}, claveJuicio='\{claveJuicio}\{'\''}, claveJuzgado='\{claveJuzgado}\{'\''},descripcion='\{descripcion}\{'\''}, actNames='\{actNames}\{'\''}, demNames='\{demNames}\{'\''}, fechaQuery=\{fechaQuery}, rowCreated=\{rowCreated}\{'}'}";
    }
}
