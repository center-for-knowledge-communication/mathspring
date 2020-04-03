package edu.umass.ckc.wo.ttmain.ttservice.loggerservice;

import edu.umass.ckc.wo.ttmain.ttconfiguration.errorCodes.TTCustomException;
import org.springframework.ui.ModelMap;

import java.util.List;

/**
 * Created by Neeraj on 3/24/2017.
 */
public interface TTLoggerService{

    public void tloggerAssist(int teacherId, int sessionId, String action, String activityName);

}