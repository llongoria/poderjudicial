package mx.com.wcontact.poderjudicial.dao;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


/**
     <config>
     <version>1.0.4.20240930</version>
     <publicacion>WEBCONTACT</publicacion>
     <timeZoneId>America/Mexico_City</timeZoneId>
     <indexOpenSearch>true</indexOpenSearch>
     <mail>
     <from>comercial@wcontact.com.mx</from>
     <smtp_host>smtp.gmail.com</smtp_host>
     <smtp_port>587</smtp_port>
     <smtp_user>smtp.gmail.com</smtp_user>
     <smtp_pass>Lu1s1981</smtp_pass>
     <smtp_auth>true</smtp_auth>
     <use_ssl>true</use_ssl>
     <mail_subject>Poder Judicial, Expediente:</mail_subject>
     <mail_template_title>Boletin Poder Judicial del Estado</mail_template_title>
     </mail>
     <send_mail_pattern>
     <pattern>LONGORIA</pattern>
     <pattern>LARIOS VELAZQUEZ</pattern>
     </send_mail_pattern>
     </config>
 */
public class ServerConfig implements Serializable {

    private static final long serialVersionUID = 3L;

    private String version;
    private String publicacion;
    private boolean debugEnable;
    private String timeZoneId;
    private boolean indexOpenSearch;
    private String keystore;
    private String keystorePassword;
    private String openSearchScheme;
    private String openSearchHostname;
    private int openSearchPort;
    private String openSearchUser;
    private String openSearchPassword;
    private String mailFrom;
    private String mailSmtpHost;
    private int mailSmtpPort;
    private String mailUser;
    private String mailPassword;
    private boolean mailSmtpAuth;
    private boolean mailUseSsl;
    private String mailSubject;
    private String mailTemplateTitle;
    private List<String> sendMailPatterns;

    public ServerConfig() { }

    public ServerConfig(String version, String publicacion, boolean debugEnable, String timeZoneId, boolean indexOpenSearch,
                        String keystore, String keystorePassword, String openSearchScheme, String openSearchHostname,
                        int openSearchPort, String openSearchUser, String openSearchPassword, String mailFrom,
                        String mailSmtpHost, int mailSmtpPort, String mailUser, String mailPassword, boolean mailSmtpAuth,
                        boolean mailUseSsl,String mailSubject, String mailTemplateTitle, List<String> sendMailPatterns) {
        this.version = version;
        this.publicacion = publicacion;
        this.debugEnable = debugEnable;
        this.timeZoneId = timeZoneId;
        this.indexOpenSearch = indexOpenSearch;
        this.keystore = keystore;
        this.keystorePassword = keystorePassword;
        this.openSearchScheme = openSearchScheme;
        this.openSearchHostname = openSearchHostname;
        this.openSearchPort = openSearchPort;
        this.openSearchUser = openSearchUser;
        this.openSearchPassword = openSearchPassword;
        this.mailFrom = mailFrom;
        this.mailSmtpHost = mailSmtpHost;
        this.mailSmtpPort = mailSmtpPort;
        this.mailUser = mailUser;
        this.mailPassword = mailPassword;
        this.mailSmtpAuth = mailSmtpAuth;
        this.mailUseSsl = mailUseSsl;
        this.mailSubject = mailSubject;
        this.mailTemplateTitle = mailTemplateTitle;
        this.sendMailPatterns = sendMailPatterns;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(String publicacion) {
        this.publicacion = publicacion;
    }

    public boolean isDebugEnable() {
        return debugEnable;
    }

    public void setDebugEnable(boolean debugEnable) {
        this.debugEnable = debugEnable;
    }

    public String getTimeZoneId() {
        return timeZoneId;
    }

    public void setTimeZoneId(String timeZoneId) {
        this.timeZoneId = timeZoneId;
    }

    public boolean isIndexOpenSearch() {
        return indexOpenSearch;
    }

    public void setIndexOpenSearch(boolean indexOpenSearch) {
        this.indexOpenSearch = indexOpenSearch;
    }

    public String getKeystore() {
        return keystore;
    }

    public void setKeystore(String keystore) {
        this.keystore = keystore;
    }

    public String getKeystorePassword() {
        return keystorePassword;
    }

    public void setKeystorePassword(String keystorePassword) {
        this.keystorePassword = keystorePassword;
    }

    public String getOpenSearchScheme() {
        return openSearchScheme;
    }

    public void setOpenSearchScheme(String openSearchScheme) {
        this.openSearchScheme = openSearchScheme;
    }

    public String getOpenSearchHostname() {
        return openSearchHostname;
    }

    public void setOpenSearchHostname(String openSearchHostname) {
        this.openSearchHostname = openSearchHostname;
    }

    public int getOpenSearchPort() {
        return openSearchPort;
    }

    public void setOpenSearchPort(int openSearchPort) {
        this.openSearchPort = openSearchPort;
    }

    public String getOpenSearchUser() {
        return openSearchUser;
    }

    public void setOpenSearchUser(String openSearchUser) {
        this.openSearchUser = openSearchUser;
    }

    public String getOpenSearchPassword() {
        return openSearchPassword;
    }

    public void setOpenSearchPassword(String openSearchPassword) {
        this.openSearchPassword = openSearchPassword;
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    public String getMailSmtpHost() {
        return mailSmtpHost;
    }

    public void setMailSmtpHost(String mailSmtpHost) {
        this.mailSmtpHost = mailSmtpHost;
    }

    public int getMailSmtpPort() {
        return mailSmtpPort;
    }

    public void setMailSmtpPort(int mailSmtpPort) {
        this.mailSmtpPort = mailSmtpPort;
    }

    public String getMailUser() {
        return mailUser;
    }

    public void setMailUser(String mailUser) {
        this.mailUser = mailUser;
    }

    public String getMailPassword() {
        return mailPassword;
    }

    public void setMailPassword(String mailPassword) {
        this.mailPassword = mailPassword;
    }

    public boolean isMailSmtpAuth() {
        return mailSmtpAuth;
    }

    public void setMailSmtpAuth(boolean mailSmtpAuth) {
        this.mailSmtpAuth = mailSmtpAuth;
    }

    public boolean isMailUseSsl() {
        return mailUseSsl;
    }

    public void setMailUseSsl(boolean mailUseSsl) {
        this.mailUseSsl = mailUseSsl;
    }

    public String getMailSubject() {
        return mailSubject;
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    public String getMailTemplateTitle() {
        return mailTemplateTitle;
    }

    public void setMailTemplateTitle(String mailTemplateTitle) {
        this.mailTemplateTitle = mailTemplateTitle;
    }

    public List<String> getSendMailPatterns() {
        return sendMailPatterns;
    }

    public void setSendMailPatterns(List<String> sendMailPatterns) {
        this.sendMailPatterns = sendMailPatterns;
    }

    @Override
    public String toString() {
        return "ServerConfig{" +
                "version='" + version + '\'' +
                ", publicacion='" + publicacion + '\'' +
                ", debugEnable=" + debugEnable +
                ", timeZoneId='" + timeZoneId + '\'' +
                ", indexOpenSearch=" + indexOpenSearch +
                ", keystore='" + keystore + '\'' +
                ", keystorePassword='" + keystorePassword + '\'' +
                ", openSearchScheme='" + openSearchScheme + '\'' +
                ", openSearchHostname='" + openSearchHostname + '\'' +
                ", openSearchPort=" + openSearchPort +
                ", openSearchUser='" + openSearchUser + '\'' +
                ", openSearchPassword='" + openSearchPassword + '\'' +
                ", mailFrom='" + mailFrom + '\'' +
                ", mailSmtpHost='" + mailSmtpHost + '\'' +
                ", mailSmtpPort=" + mailSmtpPort +
                ", mailUser='" + mailUser + '\'' +
                ", mailPassword='" + mailPassword + '\'' +
                ", mailSmtpAuth=" + mailSmtpAuth +
                ", mailUseSsl=" + mailUseSsl +
                ", mailSubject='" + mailSubject + '\'' +
                ", mailTemplateTitle='" + mailTemplateTitle + '\'' +
                ", sendMailPatterns=[" + (sendMailPatterns != null ? sendMailPatterns.stream().collect(Collectors.joining(", ")) : sendMailPatterns)  + "]}";
    }

    public final JsonObject toJson(){
        final jakarta.json.JsonObjectBuilder objectBuilder = jakarta.json.Json.createObjectBuilder();
        objectBuilder.add("version",getVersion() );
        objectBuilder.add("publicacion", getPublicacion());
        objectBuilder.add("debugEnable", isDebugEnable());
        objectBuilder.add("timeZoneId", getTimeZoneId());
        objectBuilder.add("indexOpenSearch", isIndexOpenSearch());
        objectBuilder.add("keystore", getKeystore());
        objectBuilder.add("keystorePassword", getKeystorePassword());
        objectBuilder.add("openSearchScheme", getOpenSearchScheme());
        objectBuilder.add("openSearchHostname", getOpenSearchHostname());
        objectBuilder.add("openSearchPort", getOpenSearchPort());
        objectBuilder.add("openSearchUser", getOpenSearchUser());
        objectBuilder.add("openSearchPassword", getOpenSearchPassword());
        objectBuilder.add("mailFrom", getMailFrom());
        objectBuilder.add("mailSmtpHost", getMailSmtpHost());
        objectBuilder.add("mailSmtpPort", getMailSmtpPort());
        objectBuilder.add("mailUser", getMailUser() );
        objectBuilder.add("mailPassword", getMailPassword() );
        objectBuilder.add("mailSmtpAuth", isMailSmtpAuth() );
        objectBuilder.add("mailUseSsl", isMailUseSsl() );
        objectBuilder.add("mailSubject", getMailSubject() );
        objectBuilder.add("mailTemplateTitle", getMailTemplateTitle() );
        if(sendMailPatterns != null) {
            JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
            for (String patter : getSendMailPatterns()) {
                jsonArrayBuilder.add( patter );
            }
            objectBuilder.add("sendMailPatterns", jsonArrayBuilder);
        }
        return objectBuilder.build();
    }
}
