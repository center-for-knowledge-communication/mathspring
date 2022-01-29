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
 * Frank	01-17-21	issue #358R3 disallow multiple concurrent logins
 * Frank	02-27-21	issue #383 logFeeaback
 * Frank  	10-09-2021	issue #528 Research Tool
 * Frank	01-29-22	issue #358R4 reduce instances of login retry if user has skipped logout 
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

        		Object activeTeacherIdObject = session.getAttribute("teacherId");
        		if (activeTeacherIdObject != null) {
        			if (teacherLoginType.equals("Master")) {
        				request.setAttribute(LoginParams.MESSAGE,rb.getString("teacher_already_logged_in")); 
        				return "login/loginK12_teacher";
        			}
        			if (teacherLoginType.equals("Normal")) {
        				if (loginId != (int) session.getAttribute("teacherId")) {
        					request.setAttribute(LoginParams.MESSAGE,rb.getString("multiple_teacher_logins_detected"));
        					session.invalidate();
            				return "login/loginK12_teacher";
        				}
        				else {
        					session.invalidate();
        					HttpSession  oldSession = request.getSession(false);
        					session = request.getSession();
        				}
        			}
        			if (teacherLoginType.equals("Researcher")) {
        				session.invalidate();
    					HttpSession  oldSession = request.getSession(false);
    					session = request.getSession();
        			}
        		}
    			session.setAttribute("teacherId", loginId);
        		session.setAttribute("teacherUsername", username);         	
        		session.setAttribute("teacherLoginType", teacherLoginType);
        		if (teacherLoginType.equals("Normal"))
        			tLogger.logEntryWorker(loginId, 0, "login", "");

                return loginService.populateClassInfoForTeacher(model,loginId,teacherLoginType);
            }
    }


    @RequestMapping(value = "/tt/ttMain", method = RequestMethod.GET)
    public String homePage(ModelMap model, HttpServletRequest request) throws TTCustomException {
    	HttpSession session = request.getSession();
		String teacherLoginType = (String) session.getAttribute("teacherLoginType");
    	int sTeacherId = (int) session.getAttribute("teacherId");
    	session.removeAttribute("classId");
        return loginService.populateClassInfoForTeacher(model,sTeacherId,teacherLoginType);
    }

    @RequestMapping(value = "/tt/logout", method = RequestMethod.GET)
    public String logoutSession(HttpSession session, HttpServletRequest request) {
        System.out.println("logging out");
		HttpSession MySession = request.getSession();
		int teacherId = (int) MySession.getAttribute("teacherId");
		if ("Normal".equals((String) session.getAttribute("teacherLoginType"))) {
			tLogger.logEntryWorker(teacherId, 0, "logout", "");
		}

		session.invalidate();
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

    
    @RequestMapping(value = "/tt/ttLogFeedback", method = RequestMethod.POST)
    public String logFeedback(@RequestParam("messageType") String messageType,@RequestParam("teacherId") int teacherId,@RequestParam("objectId") String objectId,@RequestParam("priority") String priority, @RequestParam("msg") String msg, ModelMap model, HttpServletRequest request, HttpServletResponse response ) throws TTCustomException {

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
  	
    	int result  = loginService.logFeedback(messageType,teacherId,objectId,priority, msg);
    	switch (result) {
    	case 0:
    		break;
    	case 1:
    		request.setAttribute(LoginParams.MESSAGE,"No feedback message entered.");
    		break;
    	default:
    		request.setAttribute(LoginParams.MESSAGE,"unexpected error");
    		break;
    	
    	}
    	String responseUrl = "teacherTools/feedbackRequest_status";
    	if (messageType.equals("problemFeedback")) {
    		request.setAttribute("objectId",objectId);
    		responseUrl = "teacherTools/problemFeedbackRequest_status";   		
    	}
    	return responseUrl;            			
    }    
    
}

