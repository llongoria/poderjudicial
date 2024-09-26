package mx.com.wcontact.poderjudicial.entity;

import java.io.Serializable;

import jakarta.json.JsonObject;
import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(   catalog = "PoderJudicial"
        , schema = "dbo"
        , name = "judged"
        , uniqueConstraints = {
            @UniqueConstraint(columnNames = {"idJudge"}) ,
            @UniqueConstraint(columnNames = {"value"})
        }
)
public class Judge implements Serializable {

    private static final long serialVersionUID = 3L;
    private final transient java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long idJudge;

    @Basic(optional = false)
    @Column(nullable = false, length = 10)
    private String value;

    @Basic(optional = false)
    @Column(nullable = false, length = 200)
    private String name;

    @Basic(optional = false)
    @Column(nullable = true, length = 200)
    private String secretary;

    @Basic(optional = false)
    @Column(nullable = true, length = 200)
    private String position;

    @Basic(optional = false)
    @Column(nullable = true, length = 300)
    private String address;

    @Basic(optional = false)
    @Column(nullable = false, length = 200)
    private String type;

    @Basic(optional = false)
    @Column(nullable = true, length = 20)
    private String param;

    @Basic(optional = false)
    @Column(nullable = false, length = 500)
    private String url;

    @Column(name = "rowCreated", insertable = false, updatable = false)
    @Basic(optional = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonbDateFormat(value = "yyyy-MM-dd HH:mm:ss")
    private Date rowCreated;

    public Judge() { }

    public Judge(String value, String name, String secretary, String position, String address, String type, String url) {
        this.value = value;
        this.name = name;
        this.secretary = secretary;
        this.position = position;
        this.address = address;
        this.type = type;
        this.url = url;
    }

    public Long getIdJudge() {
        return idJudge;
    }

    public void setIdJudge(Long idJudge) {
        this.idJudge = idJudge;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecretary() {
        return secretary;
    }

    public void setSecretary(String secretary) {
        this.secretary = secretary;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getRowCreated() {
        return rowCreated;
    }

    public void setRowCreated(Date rowCreated) {
        this.rowCreated = rowCreated;
    }

    public final JsonObject toJsonObj() {
        final jakarta.json.JsonObjectBuilder objectBuilder = jakarta.json.Json.createObjectBuilder();
        objectBuilder.add("idJudge",getIdJudge() );
        objectBuilder.add("value", getValue() );
        objectBuilder.add("name", getName() );
        objectBuilder.add("secretary", getSecretary() );
        objectBuilder.add("position", getPosition() );
        objectBuilder.add("address", getAddress() );
        objectBuilder.add("type", getType() );
        objectBuilder.add("param", getParam() );
        objectBuilder.add("url", getUrl() );
        objectBuilder.add("rowCreated", sdf.format( getRowCreated() ) );

        return objectBuilder.build();

    }
}
