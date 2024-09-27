package mx.com.wcontact.poderjudicial.entity;
import jakarta.json.JsonObject;
import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(   catalog = "PoderJudicial"
        , schema = "dbo"
        , name = "httpQuery"
        , uniqueConstraints = { @UniqueConstraint(columnNames = {"httpQueryId"}) }
      )
public class HttpQuery implements Serializable {

    private final transient java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final long serialVersionUID = 3L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long httpQueryId;

    /**
     * Ejemplo: Juzgado Sexto de lo Mercantil: M06
     */
    @Basic(optional = false)
    @Column(nullable = false, length = 50)
    private String judged;

    /**
     * Ejemplo: 2024-09-17
     */
    @Basic(optional = false)
    @Column(nullable = false, length = 12)
    private String date;

    /**
     * Ejemplo: bulletin/zmg_date
     */
    @Basic(optional = false)
    @Column(nullable = false, length = 50)
    private String url;

    @Basic(optional = false)
    @Column(nullable = false, length = 50)
    private int state = 0;

    @Basic(optional = false)
    @Column(nullable = false, length = 50)
    private int total = 0;

    @Column(name = "queryDate")
    @Basic(optional = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonbDateFormat(value = "yyyy-MM-dd HH:mm:ss" )
    private Date queryDate;

    @Column(name = "rowCreated", insertable = false, updatable = false)
    @Basic(optional = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonbDateFormat(value = "yyyy-MM-dd HH:mm:ss")
    private Date rowCreated;

    public HttpQuery() { }

    public HttpQuery(String judged, String date, String url, Date queryDate) {
        this.judged = judged;
        this.date = date;
        this.url = url;
        this.queryDate = queryDate;
    }

    public Long getHttpQueryId() {
        return httpQueryId;
    }

    public void setHttpQueryId(Long httpQueryId) {
        this.httpQueryId = httpQueryId;
    }

    public String getJudged() {
        return judged;
    }

    public void setJudged(String judged) {
        this.judged = judged;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getQueryDate() { return queryDate != null? sdf.format(queryDate):null;  }

    public void setQueryDate(Date queryDate) {
        this.queryDate = queryDate;
    }

    public String getRowCreated() { return rowCreated != null? sdf.format(rowCreated):null; }

    public void setRowCreated(Date rowCreated) {
        this.rowCreated = rowCreated;
    }

    public final JsonObject toJsonObj() {
        final jakarta.json.JsonObjectBuilder objectBuilder = jakarta.json.Json.createObjectBuilder();
        objectBuilder.add("httpQueryId",getHttpQueryId() );
        objectBuilder.add("judged", getJudged() );
        objectBuilder.add("date", getDate() );

        objectBuilder.add("url", getUrl() );
        objectBuilder.add("state", getState() );
        objectBuilder.add("total", getTotal() );
        objectBuilder.add("queryDate",  getQueryDate()   );
        objectBuilder.add("rowCreated", getRowCreated() );

        return objectBuilder.build();

    }
}
