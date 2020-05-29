package edu.umass.ckc.wo.ttmain.ttservice.loginservice;

import edu.umass.ckc.wo.ttmain.ttconfiguration.errorCodes.TTCustomException;
import org.springframework.ui.ModelMap;

import java.util.List;

/**
 * Created by Neeraj on 3/24/2017.
 * Frank 04-24-2020 issue #28
 * Frank 05-29-2020 issue #28 re-work password reset
 */
public interface TTLoginService{

    public int loginAssist(String uname,String password) throws TTCustomException;

    public String populateClassInfoForTeacher(ModelMap model, int teacherId) throws TTCustomException;

    public int resetPassword(String uname,String email) throws TTCustomException;

}