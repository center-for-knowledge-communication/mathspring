package edu.umass.ckc.wo.ttmain.ttcontroller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.umass.ckc.wo.login.LoginParams;
import edu.umass.ckc.wo.ttmain.ttconfiguration.errorCodes.TTCustomException;
import edu.umass.ckc.wo.ttmain.ttservice.loginservice.TTLoginService;
import edu.umass.ckc.wo.ttmain.ttservice.loginservice.impl.TTLoginServiceImpl;
import edu.umass.ckc.wo.ttmain.ttservice.util.TeacherLogger;


/**
 * Created by Neeraj on 3/24/2017.
 * 
 * Frank 	12-09-19	Issue #21 add logging of teacher login and logout
  * Frank 	12-21-19	Issue #21r2 add reworked database access setup
  * Frank 	01-14-20	Issue #45 & #21 add teacher logging by using the request object to get the TeacherLogger object
 * Frank 	02-24-20	Issue #21 convert to autowired implementation
 * Frank 	02-24-20	Issue #28
 */
@Controller
public class TeacherToolsMainLoginController {
    private static Logger logger = Logger.getLogger(TTLoginServiceImpl.class);

    @Autowired
    private TTLoginService loginService;
    
    @Autowired
    private TeacherLogger tLogger;

   
    @RequestMapping(value = "/tt/ttMain", method = RequestMethod.POST)
    public String printWelcome(@RequestParam("userName") String username, @RequestParam("password") String password, ModelMap model, HttpServletRequest request, HttpServletResponse response ) throws TTCustomException {

    	Locale loc = request.getLocale();
    	String lang = loc.getDisplayLanguage();

    	ResourceBundle rb = null;
    	try {
    		rb = ResourceBundle.getBundle("MathSpring",loc);
    	}
    	catch (Exception e) {
//    		logger.error(e.getMessage());	
    	}

    	String forgotPassword = request.getParameter("forgot");
    	
    	if (forgotPassword == null) {
    		forgotPassword = "off";
    	}
    	
    	int loginAllowed  = loginService.loginAssist(username,password,forgotPassword);
            if(loginAllowed == -1) {
            	if (forgotPassword.equals("off")) {
                    request.setAttribute(LoginParams.MESSAGE,rb.getString("invalid_creds_try_again"));
            	}
            	else {
            		request.setAttribute(LoginParams.MESSAGE,rb.getString("password_will_be_emailed"));
            	}
                return "login/loginK12_teacher";
            }else{
            	HttpSession session = request.getSession();
//        		session.invalidate();
//            	session = request.getSession();
//            	session.setMaxInactiveInterval(30*60);
        		session.setAttribute("teacherId", loginAllowed);
        		session.setAttribute("teacherUsername", username);         	
            	tLogger.logEntryWorker(loginAllowed, 0, "login", "");

                return loginService.populateClassInfoForTeacher(model,loginAllowed);
            }
    }


    @RequestMapping(value = "/tt/ttMain", method = RequestMethod.GET)
    public String homePage(@RequestParam("teacherId") String teacherId,ModelMap model) throws TTCustomException {
        return loginService.populateClassInfoForTeacher(model,Integer.valueOf(teacherId));
    }

    @RequestMapping(value = "/tt/logout", method = RequestMethod.GET)
    public String logoutSession(HttpSession session, HttpServletRequest request) {
        System.out.println("logging out");
		HttpSession MySession = request.getSession();
		int teacherId = (int) MySession.getAttribute("teacherId");
 		//TeacherLogger tlogger = (TeacherLogger) MySession.getAttribute("tLogger");
 		tLogger.logEntryWorker(teacherId, 0, "logout", "");

    	session.removeAttribute("tLogger");
    	session.removeAttribute("teacherUsername");
    	session.removeAttribute("teacherId");
		session.invalidate();

        return "login/loginK12_teacher";
        
    }
}
