package mx.com.wcontact.poderjudicial.timer;

import jakarta.annotation.Resource;
import jakarta.ejb.Singleton;
import jakarta.ejb.Schedule;
import jakarta.ejb.SessionContext;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.core.Response;
import mx.com.wcontact.poderjudicial.bl.BulletinBL;
import mx.com.wcontact.poderjudicial.bl.JudgeBL;
import mx.com.wcontact.poderjudicial.entity.Judge;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static mx.com.wcontact.poderjudicial.bl.JSONKeys.ZM_MERCANTIL_VALUES;

@Singleton
public class BulletinTimer {

    private static final org.jboss.logging.Logger log = org.jboss.logging.Logger.getLogger(BulletinTimer.class.getName());

    @Resource
    private SessionContext context;

    @Schedule( hour = "04", minute = "10", persistent = false)
    public void execute() {
        log.info("BulletinTimer|execute| start running");
        final java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        JudgeBL judgeBL = null;
        List<Judge> allList = null;
        try {
            judgeBL = new JudgeBL();
            allList = judgeBL.findINvalue( ZM_MERCANTIL_VALUES );
        } catch (Exception ex){
            log.error(ex);
        } finally {
            if(judgeBL != null){
                judgeBL.close();
            }
        }

        BulletinBL bulletinBL = new BulletinBL();
        String response = null;
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DATE, -1);

            for(Judge judge : allList){
                response = bulletinBL.runQuery(judge.getValue(), sdf.format( calendar.getTime() ), judge.getParam());
            }


        } catch (Exception ex){
            log.error("BulletinTimer|execute| Error al ejecutar bulletinBL", ex);
            response = ex.getMessage();

        } finally {
            bulletinBL.close();
            log.info(STR."BulletinTimer|execute|response| \{response}");
        }
    }


}
