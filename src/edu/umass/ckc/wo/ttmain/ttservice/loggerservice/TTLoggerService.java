package edu.umass.ckc.wo.ttmain.ttservice.loggerservice;

import edu.umass.ckc.wo.ttmain.ttconfiguration.errorCodes.TTCustomException;
import org.springframework.ui.ModelMap;

import java.util.List;

/**
 * Created by Neeraj on 3/24/2017.
 * Frank 	06-17-20	Issue #149
 */
public interface TTLoggerService{

    public void tloggerAssist(int teacherId, int sessionId, String action, String activityName);
    public void tloggerAssist(int teacherId, int sessionId, String classId, String action, String activityName);

}