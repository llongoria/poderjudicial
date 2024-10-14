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
import mx.com.wcontact.poderjudicial.entity.HttpQuery;
import mx.com.wcontact.poderjudicial.entity.Judge;
import mx.com.wcontact.poderjudicial.listener.PJContextListener;
import mx.com.wcontact.poderjudicial.util.Result;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static mx.com.wcontact.poderjudicial.bl.JSONKeys.ZM_MERCANTIL_VALUES;

@Singleton
public class BulletinTimer {

    private static final org.jboss.logging.Logger log = org.jboss.logging.Logger.getLogger(BulletinTimer.class.getName());

    @Resource
    private SessionContext context;

    @Schedule( hour = "04", minute = "50", persistent = false)
    public void execute() {
        log.info("****************** execute| start running ******************");

        boolean isOpenSearchActive = true;
        final java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        JudgeBL judgeBL = null;
        List<Judge> allList = null;
        try {
            judgeBL = new JudgeBL();
            allList = judgeBL.findINvalue( ZM_MERCANTIL_VALUES );
        } catch (Exception ex){
            log.error(ex);
        }
        log.info("execute| Numero Total de Juzgados Encontrados: %d".formatted( allList.size() ) );

        BulletinBL bulletinBL = new BulletinBL( judgeBL.getSession() );

        int state0 = 0;
        int state1 = 0;
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DATE, -1);

            for(Judge judge : allList){
                Result<HttpQuery> result =  bulletinBL.runQuery(
                        judge.getValue(),
                        sdf.format( calendar.getTime() ),
                        judge.getParam(),
                        PJContextListener.getCfg().isIndexOpenSearch() );
                if(result.getState() == 1)
                    state1++;
                else state0 ++;
            }


        } catch (Exception ex){
            log.error("execute| Error al ejecutar bulletinBL", ex);


        } finally {
            log.info("****************** execute| end running, State0: %d, State1: %d ******************".formatted(state0, state1) );
            bulletinBL.close();
        }
    }


}
