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
 * Frank	05-12-20    Test for empty username and password
 * Frank    05-29-2020  issue #28 re-work password reset
 * Frank	06-18-2020	issue #135 added method loginHelp()
 * Frank	07-28-20	issue #74 get teacherID from session attribute 
 * Frank	11-12-20    issue #276 suppress logging if logged in as Master
 * Frank	01-17-21	issue #358 keep track of number of active logins
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
    	String lang = loc.getLanguage();

    	if (lang.equals("es")) {
    		loc = new Locale("es","AR");	
    	}
    	else {
    		loc = new Locale("en","US");	
    	}	
    	
    	ResourceBundle rb = null;
    	try {
    		rb = ResourceBundle.getBundle("MathSpring",loc);
    	}
    	catch (Exception e) {
//    		logger.error(e.getMessage());	
    	}

   	
    	if ((username.length() == 0) || (password.length() == 0)) {
            request.setAttribute(LoginParams.MESSAGE,rb.getString("invalid_creds_try_again"));
            return "login/loginK12_teacher";    		
    	}
    	
    	String loginResult  = loginService.loginAssist(username,password);
            if(loginResult.contains("failure")) {
                request.setAttribute(LoginParams.MESSAGE,rb.getString("invalid_creds_try_again"));
                return "login/loginK12_teacher";
            }else{
        		String loginSplitter[] = loginResult.split("~");
        		int loginId = Integer.valueOf(loginSplitter[0]);
        		String teacherLoginType = loginSplitter[1];
            	HttpSession session = request.getSession();
//        		session.invalidate();
//            	session = request.getSession();
//            	session.setMaxInactiveInterval(30*60);
        		session.setAttribute("teacherId", loginId);
        		
        		// keep track of active logins
        		int activeLogins = 0;
        		Object activeLoginsObject = session.getAttribute("activeLogins");
        		if (activeLoginsObject == null) { 
        			session.setAttribute("activeLogins",1);
        		}
        		else {
        			activeLogins = (int) session.getAttribute("activeLogins");
        			activeLogins++;
        			session.setAttribute("activeLogins",activeLogins);
        		}
        		
        		session.setAttribute("teacherUsername", username);         	
        		session.setAttribute("teacherLoginType", teacherLoginType);
        		if (teacherLoginType.equals("Normal"))
        			tLogger.logEntryWorker(loginId, 0, "login", "");

                return loginService.populateClassInfoForTeacher(model,loginId);
            }
    }


    @RequestMapping(value = "/tt/ttMain", method = RequestMethod.GET)
    public String homePage(ModelMap model, HttpServletRequest request) throws TTCustomException {
    	HttpSession session = request.getSession();
    	int sTeacherId = (int) session.getAttribute("teacherId");
    	session.removeAttribute("classId");
        return loginService.populateClassInfoForTeacher(model,sTeacherId);
    }

    @RequestMapping(value = "/tt/logout", method = RequestMethod.GET)
    public String logoutSession(HttpSession session, HttpServletRequest request) {
        System.out.println("logging out");
		HttpSession MySession = request.getSession();
		int teacherId = (int) MySession.getAttribute("teacherId");
		if ("Normal".equals((String) session.getAttribute("teacherLoginType"))) {
			tLogger.logEntryWorker(teacherId, 0, "logout", "");
		}
		// keep track of active logins
		int activeLogins = 0;
		Object activeLoginsObject = session.getAttribute("activeLogins");
		if (activeLoginsObject == null) { 
			session.invalidate();
		}
		else {
			activeLogins = (int) session.getAttribute("activeLogins");
			if (activeLogins <= 1) {
				session.invalidate();				
			}
			else {
				activeLogins--;
				session.setAttribute("activeLogins",activeLogins);				
			}
		}

        return "login/loginK12_teacher";
        
    }
    
    @RequestMapping(value = "/tt/ttPassword", method = RequestMethod.POST)
    public String resetPassword(@RequestParam("userName") String username, @RequestParam("email") String email, ModelMap model, HttpServletRequest request, HttpServletResponse response ) throws TTCustomException {

    	Locale loc = request.getLocale(); 
    	String lang = loc.getLanguage();

    	if (lang.equals("es")) {
    		loc = new Locale("es","AR");	
    	}
    	else {
    		loc = new Locale("en","US");	
    	}	
    	
    	ResourceBundle rb = null;
    	try {
    		rb = ResourceBundle.getBundle("MathSpring",loc);
    	}
    	catch (Exception e) {
//    		logger.error(e.getMessage());	
    	}
  	
    	if (username.length() == 0) {
    		if (email.length() == 0) {
            	request.setAttribute(LoginParams.MESSAGE,rb.getString("enter_username_or_email"));
            	return "login/forgotPassword_error";
    		}
    	}
    	else {
    		if (email.length() > 0) {
            	request.setAttribute(LoginParams.MESSAGE,rb.getString("enter_username_or_email"));
            	return "login/forgotPassword_error";
    		}    		
    	}
    	
    	int result  = loginService.resetPassword(username, email);
    	if (result > 0) {
        	request.setAttribute(LoginParams.MESSAGE,rb.getString("email_not_unique"));
            return "login/forgotPassword_error";
        }
    		
        if(result == -1) {
        	request.setAttribute(LoginParams.MESSAGE,rb.getString("invalid_creds_try_again"));
            return "login/forgotPassword_error";
        }
        else {
        	request.setAttribute(LoginParams.MESSAGE,rb.getString("password_will_be_emailed"));
            return "login/loginK12_teacher";            	
        }
    }   
    
    @RequestMapping(value = "/tt/ttLoginHelp", method = RequestMethod.POST)
    public String loginHelp(@RequestParam("email") String email, @RequestParam("helpmsg") String helpmsg, ModelMap model, HttpServletRequest request, HttpServletResponse response ) throws TTCustomException {

    	Locale loc = request.getLocale(); 
    	String lang = loc.getLanguage();

    	if (lang.equals("es")) {
    		loc = new Locale("es","AR");	
    	}
    	else {
    		loc = new Locale("en","US");	
    	}	
    	
    	ResourceBundle rb = null;
    	try {
    		rb = ResourceBundle.getBundle("MathSpring",loc);
    	}
    	catch (Exception e) {
//    		logger.error(e.getMessage());	
    	}
  	
    	if (helpmsg.length() > 0) {
    		if (email.length() > 0) {
       	    	int result  = loginService.sendHelpMessage(rb.getString("login_help_request"),email, helpmsg);
       	    	if (result == 0) {
       	    		request.setAttribute(LoginParams.MESSAGE,rb.getString("help_request_sent"));
       	    	}
       	    	else {
       	    		request.setAttribute(LoginParams.MESSAGE,rb.getString("email_send_error"));       	    		
       	    	}
                return "login/loginK12_teacher";            			
    		}
    		else {
               	request.setAttribute(LoginParams.MESSAGE,rb.getString("enter_email"));
            	return "login/loginHelpRequest_error";
    		}
    	}
    	else {
        	request.setAttribute(LoginParams.MESSAGE,rb.getString("enter_email_and_message"));
        	return "login/loginHelpRequest_error";    		
    	}
    }    
    
}

