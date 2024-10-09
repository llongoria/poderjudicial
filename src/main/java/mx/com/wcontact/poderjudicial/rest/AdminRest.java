package mx.com.wcontact.poderjudicial.rest;

import jakarta.json.Json;
import jakarta.json.JsonArray;
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
import mx.com.wcontact.poderjudicial.listener.PJContextListener;
import mx.com.wcontact.poderjudicial.util.Fecha;
import mx.com.wcontact.poderjudicial.util.Result;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static mx.com.wcontact.poderjudicial.bl.JSONKeys.ZM_MERCANTIL_VALUES;

@Path("/admin")
public class AdminRest {

    private static final org.jboss.logging.Logger log = org.jboss.logging.Logger.getLogger(AdminRest.class.getName());
    protected final java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final LocalDate initialDate = LocalDate.of(2024,5,25);

    @GET
    @Produces({ MediaType.APPLICATION_JSON})
    public jakarta.ws.rs.core.Response info(){

        return Response.status( Response.Status.OK.getStatusCode(),
                "OK"
        ).entity(PJContextListener.getCfg().toJson()).build();
    }

    @GET
    @Path("refactorME/{dateStr}")
    @Produces({ MediaType.APPLICATION_JSON})
    public jakarta.ws.rs.core.Response refactorME(@PathParam("dateStr") String dateStr) {
        java.text.SimpleDateFormat sdfDate = new java.text.SimpleDateFormat("yyyy-MM-dd");
        LocalDate localInitialDate = initialDate;
        try {
            localInitialDate = sdfDate.parse(dateStr).toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
        } catch (ParseException ex) {
            log.error(ex);
            JsonObject jsonObject = Json.createObjectBuilder()
                    .add("status", "ERROR")
                    .add("message", "La fecha [%s] tiene un formato incorrecto, por favor ingresa el formato: [yyyy-MM-dd]".formatted(dateStr) )
                    .build();
            return Response.status( Response.Status.BAD_REQUEST.getStatusCode(),
                    ex.getMessage()
            ).entity(jsonObject).build();
        }



        BulletinBL bulletinBL = null;
        List<LocalDate> listDates = Fecha.getDatesBetween(localInitialDate, LocalDate.now() );

        JudgeBL judgeBL = null;
        List<Judge> listJudge = null;
        try {
            judgeBL = new JudgeBL();
            listJudge = judgeBL.findINvalue( ZM_MERCANTIL_VALUES );
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
        log.info( String.format("***** AdminRest|refactorME| Total de Juzgados Mercantiles: %d.",listJudge.size() ) );
        try {
            bulletinBL = new BulletinBL();
            JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
            for(Judge judge: listJudge){
                for(LocalDate localDate: listDates){
                    Result<HttpQuery> result = bulletinBL.runQuery(judge.getValue(), localDate.toString(),judge.getParam(), PJContextListener.getCfg().isIndexOpenSearch());
                    try {
                        JsonObject objResp = createJsonObject(judge.getValue(), localDate.toString(), result.getResult());
                        jsonArrayBuilder.add(objResp);
                    }catch(Exception ex){
                        log.error("AdminRest|refactorME| Error al obtener el objResp y agregarlo al jsonArrayBuilder",ex);
                    }
                }
            }
            JsonObject jsonObject = Json.createObjectBuilder()
                    .add("type", "refactorME")
                    .add("status", "OK")
                    .add("date", sdf.format( new Date() ) )
                    .add("judges", listJudge.size() )
                    .add("dates", listDates.size() )
                    .add("data", jsonArrayBuilder)
                    .build();
            return Response.status(Response.Status.OK.getStatusCode(), "INFO OK").entity(jsonObject).build();

        } catch(Exception ex){
            log.error(ex);
            JsonObject jsonObject = Json.createObjectBuilder()
                    .add("status", "ERROR")
                    .add("message", ex.getMessage())
                    .build();
            return Response.status( Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                    ex.getMessage()
            ).entity(jsonObject).build();
        } finally {
        }
    }

    @GET
    @Path("judgeInfo")
    @Produces({ MediaType.APPLICATION_JSON})
    public jakarta.ws.rs.core.Response judgeInfo() {

        JudgeBL judgeBL = null;
        List<LocalDate> listDates = Fecha.getDatesBetween(initialDate, LocalDate.now() );

        try {
            judgeBL = new JudgeBL();
            List<Judge> allList = judgeBL.findAll();
            JsonObject jsonObject = Json.createObjectBuilder()
                    .add("type", "getconfig")
                    .add("status", "OK")
                    .add("date", sdf.format( new Date() ) )
                    .add("size", allList.size())
                    .add("data", convertListJudge(allList) )
                    .add("dates", convertListLocalDate(listDates) )
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

    JsonArrayBuilder convertListJudge(List<Judge> list) {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (Judge judge : list) {
            jsonArrayBuilder.add( judge.getValue());
        }
        return jsonArrayBuilder;
    }

    JsonArrayBuilder convertListLocalDate(List<LocalDate> list) {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (LocalDate localDate : list) {
            jsonArrayBuilder.add( localDate.toString() );
        }
        return jsonArrayBuilder;
    }

    public JsonObject createJsonObject(String judge, String date, String response) {
        jakarta.json.JsonObjectBuilder objectBuilder = jakarta.json.Json.createObjectBuilder();
        objectBuilder.add("judge", judge);
        objectBuilder.add("date", date );
        objectBuilder.add("response", response );
        return objectBuilder.build();
    }
}
