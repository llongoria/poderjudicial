package mx.com.wcontact.poderjudicial.rest;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import mx.com.wcontact.poderjudicial.bl.BulletinBL;
import mx.com.wcontact.poderjudicial.bl.JudgeBL;
import mx.com.wcontact.poderjudicial.entity.HttpQuery;
import mx.com.wcontact.poderjudicial.entity.Judge;
import mx.com.wcontact.poderjudicial.timer.BulletinTimer;

import java.util.Date;
import java.util.List;

@Path("/bulletin")
public class BulletinRest {

    private static final org.jboss.logging.Logger log = org.jboss.logging.Logger.getLogger(BulletinRest.class.getName());
    protected final java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Ejemplo: runquery/M06/2024-09-17
     * @param judged
     * @param date
     * @return
     */
    @GET
    @Path("runquery/{judged}/{date}")
    @Produces("text/plain")
    public String runQuery(@PathParam("judged") String judged, @PathParam("date") String date) {
        Judge judge = null;
        JudgeBL judgeBL = new JudgeBL();
        try{
           judge = judgeBL.findCountJudge(judged);
        } catch(Exception ex) {
            log.error("BulletinRest|runQuery|findCountJudge| Error al ejecutar bulletinBL", ex);
            return ex.getMessage();
        }



        BulletinBL bulletinBL = new BulletinBL();

        String response = null;
        try {


            response = bulletinBL.runQuery(judged, date, judge.getParam() );
        } catch (Exception ex){
            log.error("BulletinRest|runQuery|runQuery| Error al ejecutar bulletinBL", ex);
            response = ex.getMessage();
        } finally {
            bulletinBL.close();
        }
        return response;
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON})
    public jakarta.ws.rs.core.Response httpquery() {

        BulletinBL bulletinBL = null;
        try {
            bulletinBL = new BulletinBL();
            List<HttpQuery> countDevices = bulletinBL.findCountHttpQuery();
            JsonObject jsonObject = Json.createObjectBuilder()
                    .add("type", "httpquery")
                    .add("status", "OK")
                    .add("date", sdf.format( new Date() ) )
                    .add("size", countDevices.size())
                    .add("data", convertListToJsonArray(countDevices) )
                    .add("categories", convertListToCategories(countDevices) )
                    .build();
            return Response.status(Response.Status.OK.getStatusCode(), "INFO OK").entity(jsonObject).build();

        } catch (Exception ex){
            log.error(ex);
            JsonObject jsonObject = Json.createObjectBuilder()
                    .add("status", "ERROR")
                    .add("message", ex.getMessage())
                    .build();
            return Response.status( Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                    ex.getMessage()
            ).entity(jsonObject).build();

        } finally {
            if(bulletinBL != null){
                bulletinBL.close();
            }

        }
    }

    JsonArrayBuilder convertListToJsonArray(List<HttpQuery> list) {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (HttpQuery httpQuery : list) {
            jsonArrayBuilder.add( httpQuery.toJsonObj() );
        }
        return jsonArrayBuilder;
    }

    JsonArrayBuilder convertListToCategories(List<HttpQuery> list) {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (HttpQuery httpQuery : list) {
            jsonArrayBuilder.add( httpQuery.getDate() );
        }
        return jsonArrayBuilder;
    }


}