package mx.com.wcontact.poderjudicial.websocket;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import mx.com.wcontact.poderjudicial.dao.ServerConfig;
import mx.com.wcontact.poderjudicial.util.config.Config;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/pjsocket/{username}")
@ApplicationScoped
public class PJWebSocket {

    private static final org.jboss.logging.Logger log = org.jboss.logging.Logger.getLogger(PJWebSocket.class.getName());
    private static  final Map<String, Session> sessions = new ConcurrentHashMap<>();
    private static final Map<String, Date> sessionCreationTimes = new ConcurrentHashMap<>();
    private final ServerConfig serverConfig;
    private final SimpleDateFormat sdf = new SimpleDateFormat( "YYYY-MM-dd HH:mm:ss");

    public PJWebSocket(){
        serverConfig = Config.Instance().getServerConfig();
    }

    private static void recordSessionCreation(jakarta.websocket.Session session) {
        Date creationTime = new Date();
        sessionCreationTimes.put(session.getId(), creationTime);
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        log.infof("User %s Open Session", username);
        // broadcast("User " + username + " joined");
        sessions.put(username, session);
        recordSessionCreation(session);
        session.getAsyncRemote().sendObject(
                createJSONMessage("response","OK", "Welcome " + username + " to room.", sessions.get(username).getId() ).toString()
                , result -> {
                    if (result.getException() != null) {
                        log.error("Unable to send message: " + result.getException(), result.getException());
                    }
                });
    }

    @OnClose
    public void onClose(Session session, @PathParam("username") String username) {
        if(serverConfig.isDebugEnable())
            log.infof("User %s Close Session", username);

        if(username != null && !username.isEmpty()) {
            sessions.remove(username);
        }

        if (session != null && session.getId() != null) {
            sessionCreationTimes.remove(session.getId());
        }
    }

    @OnError
    public void onError(Session session, @PathParam("username") String username, Throwable throwable) {
        if(serverConfig.isDebugEnable()) {
            log.infof("User %s Error Session", username);
            log.error("Error on session: " + username + " id: " + session.getId(), throwable);
        }
        if(username != null && !username.isEmpty())
            sessions.remove(username);
    }

    @OnMessage
    public String onMessage(String message, @PathParam("username") String username, Session session) {
        if(serverConfig.isDebugEnable())
            log.infof("User %s onMessage %s", username, message);
        JsonObject jsonObject = null;
        try ( JsonReader jsonReader = Json.createReader(new StringReader(message)) ) {
            jsonObject = jsonReader.read().asJsonObject() ;
            String type = jsonObject.getString("type");
            if(serverConfig.isDebugEnable())
                log.infof("Tipo de Mensaje: %s", type );

        } catch(Exception e) {
            log.error("Error onMessage", e);
        }

        return createJSONMessage("response", "BAD_REQUEST", "TIPO NO ENCONTRADO", session.getId()).toString();
    }

    public static synchronized void broadcast(JsonObject jsonObject) {
        sessions.values().forEach(s -> {
            s.getAsyncRemote().sendObject(jsonObject.toString(), result ->  {
                if (result.getException() != null) {
                    log.error("Gateway| broadcast| result.getException()| Unable to send message: " + result.getException());
                }
            });
        });
    }


    public static Collection<Session> getSessions() {
        return sessions.values();
    }

    public static java.util.Date getSessionCreationTime(Session session) {
        return sessionCreationTimes.get(session.getId());
    }

    protected JsonObject createJSONMessage(String type, String status, String text, String id) {
        return Json.createObjectBuilder()
                .add("type", type)
                .add("status", status)
                .add("text", text)
                .add("id", id)
                .add("date", sdf.format( new Date() ) )
                .build();
    }



}
