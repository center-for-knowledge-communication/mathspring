package edu.umass.ckc.wo.handler;

import edu.umass.ckc.wo.beans.Teacher;
import edu.umass.ckc.wo.db.DbTeacher;
import edu.umass.ckc.wo.event.admin.AdminTeacherEditEvent;
import edu.umass.ckc.wo.login.PasswordAuthentication;
import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.email.Emailer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Copyright (c) University of Massachusetts
 * Written by: Frank Sylvia
 * Date: Apr 24, 2020
 * 
 * Frank	05-12-2020 Remove email sender - not needed
 */
public class TeacherEditHandler {


    public void handleEvent(Connection conn, AdminTeacherEditEvent event, HttpServletRequest req, HttpServletResponse resp) throws Exception {

    	 Locale loc = req.getLocale(); 
    	 String lang = loc.getLanguage();
    	 String country = loc.getCountry();

    	 System.out.println("locale set to:" + lang + "-" + country );	

    	 if (!lang.equals("es")) {
    	 	loc = new Locale("en","US");	
    	 }			
    	
    	
    	ResourceBundle rb = null;
    	try {
    		rb = ResourceBundle.getBundle("MathSpring",loc);
    	}
    	catch (Exception e) {
//    		logger.error(e.getMessage());	
    	}
    	    			
    			
    	// if passwords aren't same, gripe
        if (event.getFname() == null || event.getLname() == null  || event.getEmail() == null ||
                event.getFname().equals("") || event.getLname().equals("") || event.getEmail().equals(""))
        {
            req.setAttribute("message",rb.getString("you_must_supply_values_for_required_fields"));
            req.getRequestDispatcher(Settings.useNewGUI()
                    ? "/teacherTools/teacherRegister_new.jsp"
                    : "/teacherTools/teacherRegister_new.jsp").forward(req ,resp);
        }
        else if (!event.getPw1().equals(event.getPw2()))
        {
            req.setAttribute("message",rb.getString("passwords_must_match"));
            req.getRequestDispatcher("b".equals(req.getParameter("var"))
                    ? "/teacherTools/teacherRegister_new.jsp"
                    : "/teacherTools/teacherRegister_new.jsp").forward(req ,resp);
        }
        // show userName created and give a button to proceed to class creation
        else {
        	Teacher dbValues = DbTeacher.getTeacher(conn, event.getIntId());
        	
            DbTeacher.editTeacher(conn,event.getIntId(),event.getFname(),event.getLname(),event.getEmail(),event.getPw1());
            String msg = rb.getString("profile_changes_processed");
            
        	if (event.getPw1().length() >= 4) {
        		msg = msg + rb.getString("please_remember_password");
        	}
        
            req.setAttribute("message",msg);

            req.getRequestDispatcher("/login/loginK12_new.jsp").forward(req ,resp);
        }
    }

}