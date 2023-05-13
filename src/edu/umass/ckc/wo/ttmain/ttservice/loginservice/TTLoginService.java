package edu.umass.ckc.wo.ttmain.ttservice.loginservice;

import edu.umass.ckc.wo.ttmain.ttconfiguration.errorCodes.TTCustomException;
import org.springframework.ui.ModelMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Neeraj on 3/24/2017.
 * Frank 04-24-2020 issue #28
 * Frank 05-29-2020 issue #28 re-work password reset
 * Frank 06-18-2020 issue #135 added method sendHelpMessage()
 */
public interface TTLoginService{

    public String loginAssist(String uname,String password);

    public String populateClassInfoForTeacher(ModelMap model, int teacherId, String teacherLoginType) throws TTCustomException;

    public int resetPassword(String uname,String email);

    public int sendHelpMessage(String subject, String email,String helpmsg) throws TTCustomException;

    public int logFeedback(String messageType, int teacherId, String objectId, String priority, String msg) throws TTCustomException;
    
    public String populateLCProfile() throws TTCustomException;
}