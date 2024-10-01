package mx.com.wcontact.poderjudicial.util.config;

import mx.com.wcontact.poderjudicial.dao.ServerConfig;
import mx.com.wcontact.poderjudicial.listener.PJContextListener;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.util.List;
import java.util.TimeZone;

public final class Config {

    private static final org.jboss.logging.Logger log = org.jboss.logging.Logger.getLogger(Config.class.getName());
    private static final Object lockCFG = new Object();
    private static Config config;
    public  static final String DATETIME_PATTERN_CONST = "yyyy-MM-dd HH:mm:ss";

    private final Object lockXML = new Object();
    private  final Configurations configs = new Configurations();
    private FileBasedConfigurationBuilder<XMLConfiguration> builder;

    private  XMLConfiguration xmlConfig;

    private Config() {
        TimeZone.setDefault( TimeZone.getTimeZone( "America/Mexico_City" ) );
        loadValuesProperties();
        log.info("Config| Instancia de mx.com.wcontact.contingencia.util.Config creada.");

    }

    public static synchronized Config Instance(){
        try {
            if (config == null) {
                synchronized (lockCFG) {
                    config = new Config();
                }
            }
        } catch (Exception ex) {
            log.error("Config Instance() Exception", ex);
        }
        return config;
    }


    private synchronized XMLConfiguration GetXML() {
        try {
            if (xmlConfig == null) {
                synchronized (lockXML) {

                    log.infof("Cargando archivo de XMLConfiguration: %s", PJContextListener.URL_CONFIG_FILE);
                    builder = configs.xmlBuilder(PJContextListener.URL_CONFIG_FILE);
                    xmlConfig = builder.getConfiguration();
                    log.infof("Archivo de XMLConfiguration cargado: %s", xmlConfig );

                    if (xmlConfig == null) {
                        throw new ConfigurationException("XMLConfiguration es null");
                    }
                }
            }
        } catch (ConfigurationException ex) {
            log.error("XMLConfiguration Instance() Exception", ex);
        }
        return xmlConfig;
    }

    public  String get(String key) {
        return GetXML().getString(key);
    }

    public List<String> getList(String key) {
        return GetXML().getList(String.class, key);
    }

    public  Boolean getBoolean(String key) {
        return Boolean.parseBoolean( get(key) );
    }

    public  int getInt(String key) {
        return Integer.parseInt( get(key) );
    }

    private void loadValuesProperties(){
        getServerConfig();
    }



    public  ServerConfig getServerConfig(){
        return new ServerConfig(
                get("version"),
                get("publicacion"),
                getBoolean("debug_enable"),
                get("timeZoneId"),
                getBoolean("opensearch.indexOpenSearch"),
                get("opensearch.trustStore"),
                get("opensearch.trustStore_password"),

                get("opensearch.scheme"),
                get("opensearch.hostname"),
                getInt("opensearch.port"),
                get("opensearch.user"),
                get("opensearch.password"),

                get("mail.from"),
                get("mail.smtp_host"),
                getInt("mail.smtp_port"),
                get("mail.smtp_user"),
                get("mail.smtp_pass"),
                getBoolean("mail.smtp_auth"),
                getBoolean("mail.use_ssl"),
                get("mail.mail_subject"),
                get("mail.mail_template_title"),
                getList("send_mail_pattern.pattern")
        );
    }

    public void setServerConfig(ServerConfig serverConfig) throws ConfigurationException {

        xmlConfig.setProperty("version",serverConfig.getVersion() );
        xmlConfig.setProperty("publicacion",serverConfig.getPublicacion() );
        xmlConfig.setProperty("debug_enable",serverConfig.isDebugEnable() );
        xmlConfig.setProperty("timeZoneId",serverConfig.getTimeZoneId() );
        xmlConfig.setProperty("opensearch.indexOpenSearch",serverConfig.isIndexOpenSearch() );
        xmlConfig.setProperty("opensearch.trustStore",serverConfig.getKeystore() );
        xmlConfig.setProperty("opensearch.trustStore_password",serverConfig.getKeystorePassword() );

        xmlConfig.setProperty("opensearch.scheme",serverConfig.getOpenSearchScheme() );
        xmlConfig.setProperty("opensearch.hostname",serverConfig.getOpenSearchHostname() );
        xmlConfig.setProperty("opensearch.port",serverConfig.getOpenSearchPort() );
        xmlConfig.setProperty("opensearch.user",serverConfig.getOpenSearchUser() );
        xmlConfig.setProperty("opensearch.password",serverConfig.getOpenSearchPassword() );

        xmlConfig.setProperty("mail.from",serverConfig.getMailFrom() );
        xmlConfig.setProperty("mail.smtp_host",serverConfig.getMailSmtpHost() );
        xmlConfig.setProperty("mail.smtp_port",serverConfig.getMailSmtpPort() );
        xmlConfig.setProperty("mail.smtp_user",serverConfig.getMailUser() );
        xmlConfig.setProperty("mail.smtp_pass",serverConfig.getMailPassword() );
        xmlConfig.setProperty("mail.smtp_auth",serverConfig.isMailSmtpAuth());
        xmlConfig.setProperty("mail.use_ssl",serverConfig.isMailUseSsl() );
        xmlConfig.setProperty("mail.mail_subject",serverConfig.isMailUseSsl() );
        xmlConfig.setProperty("mail.mail_template_title",serverConfig.isMailUseSsl() );
        xmlConfig.setProperty("send_mail_pattern.pattern",serverConfig.getSendMailPatterns() );

        builder.save();

        try {
            loadValuesProperties();
        } catch(Exception ex){
            log.error(ex);
        }
    }


}
