package edu.umass.ckc.wo.ttmain.ttcontroller;

import edu.umass.ckc.wo.ttmain.ttconfiguration.errorCodes.TTCustomException;
import edu.umass.ckc.wo.ttmain.ttservice.loginservice.TTLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

/**
 * Created by Neeraj on 3/24/2017.
 */
@Controller
public class TeacherToolsMainLoginController {

    @Autowired
    private TTLoginService loginService;

    @RequestMapping(value = "/tt/ttMain", method = RequestMethod.POST)
    public String printWelcome(@RequestParam("userName") String username, @RequestParam("password") String password, ModelMap model) throws TTCustomException {
        int loginAllowed  = loginService.loginAssist(username,password);
            if(loginAllowed == -1) {
                model.addAttribute("invalidLogin","Invalid Credentials.Please try again.");
                return "login/loginK12";
            }else{
                return loginService.populateClassInfoForTeacher(model,loginAllowed);
            }
    }


    @RequestMapping(value = "/tt/ttMain", method = RequestMethod.GET)
    public String homePage(@RequestParam("teacherId") String teacherId,ModelMap model) throws TTCustomException {
        return loginService.populateClassInfoForTeacher(model,Integer.valueOf(teacherId));
    }

    @RequestMapping(value = "/tt/logout", method = RequestMethod.GET)
    public String logoutSession(HttpSession logoutSession) {
        logoutSession.invalidate();
        return "login/loginK12";
    }
}
