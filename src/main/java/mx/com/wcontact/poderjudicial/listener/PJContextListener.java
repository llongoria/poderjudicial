package mx.com.wcontact.poderjudicial.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import mx.com.wcontact.poderjudicial.dao.ServerConfig;
import mx.com.wcontact.poderjudicial.util.config.Config;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@WebListener
public class PJContextListener implements ServletContextListener, HttpSessionListener {

    private static final org.jboss.logging.Logger log = org.jboss.logging.Logger.getLogger(PJContextListener.class.getName());
    public static String URL_CONFIG_FILE = "/home/llongoria/PoderJudicial/Config/pj_config.xml";
    public static final int MAX_LENGTH_MESSAGE = 1024;
    private static final List<HttpSession> sessions = new CopyOnWriteArrayList<>();
    private static ServerConfig _SERVER_CONFIG;

    public PJContextListener(){ }

    public static final ServerConfig getCfg(){
        if(_SERVER_CONFIG == null){
            _SERVER_CONFIG = Config.Instance().getServerConfig();
        }
        return _SERVER_CONFIG;
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        log.info("############ Context PODER JUDICIAL initializing ###########");
        ServletContext ctx = sce.getServletContext();

        URL_CONFIG_FILE = ctx.getInitParameter("URL_CONFIG_FILE") != null
                ? ctx.getInitParameter("URL_CONFIG_FILE")
                : URL_CONFIG_FILE;

        _SERVER_CONFIG = Config.Instance().getServerConfig();
        printInfo();
        log.info("########### Context PODER JUDICIAL initialized ###########");

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("########### Context PODER JUDICIAL destroying ###########");

        log.info("########### Context PODER JUDICIAL destroyed ###########");
    }

    public void printInfo(){
        log.infof("URL_CONFIG_FILE: %s", URL_CONFIG_FILE);
        log.infof("CONFIG: %s", _SERVER_CONFIG.toString() );

    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        log.infof( "sessionCreated", "*** sessionCreated {0} ***",se.getSession().getId() );
        sessions.add(se.getSession());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        log.infof("sessionDestroyed", "*** sessionDestroyed {0} ***",se.getSession().getId() );
        sessions.remove(se.getSession());
    }

    public static List<HttpSession> getSessions() {
        return sessions;
    }


}
