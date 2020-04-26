package edu.umass.ckc.wo.ttmain.ttservice.loginservice;

import edu.umass.ckc.wo.ttmain.ttconfiguration.errorCodes.TTCustomException;
import org.springframework.ui.ModelMap;

import java.util.List;

/**
 * Created by Neeraj on 3/24/2017.
 * Frank 04-24-2020 issue #28
 */
public interface TTLoginService{

    public int loginAssist(String uname,String password, String forgotPassword) throws TTCustomException;

    public String populateClassInfoForTeacher(ModelMap model, int teacherId) throws TTCustomException;

}