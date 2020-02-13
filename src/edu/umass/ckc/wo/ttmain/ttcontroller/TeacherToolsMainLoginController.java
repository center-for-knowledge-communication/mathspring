package edu.umass.ckc.wo.ttmain.ttcontroller;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jndi.JndiTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.umass.ckc.wo.db.DbUtil;
import edu.umass.ckc.wo.ttmain.ttconfiguration.TTConfiguration;
import edu.umass.ckc.wo.ttmain.ttconfiguration.errorCodes.ErrorCodeMessageConstants;
import edu.umass.ckc.wo.ttmain.ttconfiguration.errorCodes.TTCustomException;
import edu.umass.ckc.wo.ttmain.ttservice.loginservice.TTLoginService;
import edu.umass.ckc.wo.ttmain.ttservice.loginservice.impl.TTLoginServiceImpl;
import edu.umass.ckc.wo.tutor.Settings;

import javax.servlet.ServletContext;
import javax.naming.NamingException;

import edu.umass.ckc.wo.log.TeacherLogger;
/**
 * Created by Neeraj on 3/24/2017.
 * 
 * Frank 	12-09-19	Issue #21 add logging of teacher login and logout
  * Frank 	12-21-19	Issue #21r2 add reworked database access setup
*/
@Controller
public class TeacherToolsMainLoginController {
    private static Logger logger = Logger.getLogger(TTLoginServiceImpl.class);

    @Autowired
    private TTLoginService loginService;
    

    @RequestMapping(value = "/tt/ttMain", method = RequestMethod.POST)
    public String printWelcome(@RequestParam("userName") String username, @RequestParam("password") String password, ModelMap model, HttpServletRequest request) throws TTCustomException {
        int loginAllowed  = loginService.loginAssist(username,password);
            if(loginAllowed == -1) {
                model.addAttribute("invalidLogin","Invalid Credentials.Please try again.");
                return "login/loginK12";
            }else{
            	HttpSession session = request.getSession();
            	session.setMaxInactiveInterval(30*60);

            	try {
            		ServletContext ctx  = session.getServletContext();    
            		TeacherLogger tlogger = new TeacherLogger(ctx);
            		session.setAttribute("tLogger", tlogger);
            		session.setAttribute("conn", tlogger.getConn());            		
            		session.setAttribute("teacherUsername", username);         	
            		session.setAttribute("teacherId", loginAllowed);
            		tlogger.insertLoginEntry(loginAllowed, "login", "");
            	}
            	catch(SQLException e) {
                    logger.error(e.getMessage());
            	}

                return loginService.populateClassInfoForTeacher(model,loginAllowed);
            }
    }


    @RequestMapping(value = "/tt/ttMain", method = RequestMethod.GET)
    public String homePage(@RequestParam("teacherId") String teacherId,ModelMap model) throws TTCustomException {
        return loginService.populateClassInfoForTeacher(model,Integer.valueOf(teacherId));
    }

    @RequestMapping(value = "/tt/logout", method = RequestMethod.GET)
    public String logoutSession(HttpSession logoutSession) {
        System.out.println("logging out");
    	try {
            int teacherId = (int) logoutSession.getAttribute("teacherId");
    		TeacherLogger tlog = (TeacherLogger) logoutSession.getAttribute("tlogger");
    		tlog.insertLoginEntry(teacherId, "logout", "");
    	}
    	catch (SQLException e ) {
            logger.error(e.getMessage());
    	}
    	finally {
    		logoutSession.invalidate();
    	}
        return "login/loginK12";
    }
}
