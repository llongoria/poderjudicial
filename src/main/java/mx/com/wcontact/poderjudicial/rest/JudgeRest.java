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
import mx.com.wcontact.poderjudicial.bl.JudgeBL;
import mx.com.wcontact.poderjudicial.entity.Judge;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Path("/judge")
public class JudgeRest {

    private static final org.jboss.logging.Logger log = org.jboss.logging.Logger.getLogger(JudgeRest.class.getName());
    protected final java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Ejemplo: runquery/penal_oral_judges
     * @param type
     * @return
     */
    @GET
    @Path("runquery/{type}")
    @Produces("text/plain")
    public String runQuery(@PathParam("type") String type) {

        JudgeBL judgeBL = new JudgeBL();

        String response = null;
        try {
            response = judgeBL.runQuery(type);
        } catch (Exception ex){
            log.error("JudgeRest|runQuery| Error al ejecutar judgeBL runQuery", ex);
            response = ex.getMessage();
        } finally {
            judgeBL.close();
        }
        return response;
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON})
    public jakarta.ws.rs.core.Response get() {

        JudgeBL judgeBL = null;
        try {
            judgeBL = new JudgeBL();
            List<Object[]> countDevices = judgeBL.findCountJudges();
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
            if(judgeBL != null){
                judgeBL.close();
            }

        }
    }

    JsonArrayBuilder convertListToJsonArray(List<Object[]> list) {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (Object[] cols : list) {
            JsonObject jsonObject = toJsonObj(cols);
            jsonArrayBuilder.add( jsonObject );
        }
        return jsonArrayBuilder;
    }

    public JsonObject toJsonObj(Object[] cols) {
        jakarta.json.JsonObjectBuilder objectBuilder = jakarta.json.Json.createObjectBuilder();
        objectBuilder.add("type",cols[0].toString());
        objectBuilder.add("total", Integer.parseInt(Objects.equals(cols[1].toString(), "") ?"0":cols[1].toString() ) );
        return objectBuilder.build();
    }

    JsonArrayBuilder convertListToCategories(List<Object[]> list) {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (Object[] cols : list) {
            jsonArrayBuilder.add( cols[0].toString() );
        }
        return jsonArrayBuilder;
    }

}
