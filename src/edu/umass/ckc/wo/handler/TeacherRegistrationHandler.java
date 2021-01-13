package edu.umass.ckc.wo.handler;

import edu.umass.ckc.wo.db.DbTeacher;
import edu.umass.ckc.wo.event.admin.AdminTeacherRegistrationEvent;
import edu.umass.ckc.wo.login.PasswordAuthentication;
import edu.umass.ckc.wo.ttmain.ttservice.util.SendEM;
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
 * Written by: David Marshall
 * Date: Jan 31, 2006
 * Time: 4:13:23 PM
 * Frank 09-13-20 issue #242
 */
public class TeacherRegistrationHandler {


    public void handleEvent(Connection conn, AdminTeacherRegistrationEvent event, HttpServletRequest req, HttpServletResponse resp) throws Exception {

    	Locale loc = req.getLocale();
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
    	    			
    			
    	// if passwords aren't same, gripe
        if (event.getFname() == null || event.getLname() == null || event.getPw1() == null || event.getPw2() == null || event.getEmail() == null ||
                event.getFname().equals("") || event.getLname().equals("") || event.getPw1().equals("") || event.getPw2().equals("") || event.getEmail().equals(""))
        {
            req.setAttribute("message",rb.getString("you_must_supply_values_for_required_fields"));
            Integer adminId = (Integer) req.getSession().getAttribute("adminId"); // determine if this is admin session
            req.setAttribute("isAdmin",adminId != null ? true : false);
            req.getRequestDispatcher(Settings.useNewGUI()
                    ? "/teacherTools/teacherRegister_new.jsp"
                    : "/teacherTools/teacherRegister_new.jsp").forward(req ,resp);
        }
        else if (!event.getPw1().equals(event.getPw2()))
        {
            req.setAttribute("message",rb.getString("passwords_must_match"));
            Integer adminId = (Integer) req.getSession().getAttribute("adminId"); // determine if this is admin session
            req.setAttribute("isAdmin",adminId != null ? true : false);
            req.getRequestDispatcher("b".equals(req.getParameter("var"))
                    ? "/teacherTools/teacherRegister_new.jsp"
                    : "/teacherTools/teacherRegister_new.jsp").forward(req ,resp);
        }
        // show userName created and give a button to proceed to class creation
        else {
            String userName = createUser(conn, event);
            Integer adminId = (Integer) req.getSession().getAttribute("adminId"); // determine if this is admin session
            new Thread(new Runnable() {

                @Override
                public void run() {
                    SendEM sender = new SendEM();
                    sender.sendPassword(conn,"mathspring@mathspring.org", Settings.mailServer,userName,event.getPw1(),event.getEmail());
                    }
                }).start();

            String msg =  "Created the user name: " + userName + ".  Please remember it (and your password). \n You will need it for" +
                    " creating classes and getting reports about how your students are doing." ;
            req.setAttribute("isAdmin",adminId != null ? true : false);
            req.setAttribute("message",msg);
            req.getRequestDispatcher(Settings.useNewGUI()
                            ? "/login/loginK12_new.jsp"
                            : "/teacherTools/teacherLogin.jsp").forward(req ,resp);
        }
    }

    /**
     * Create a user from the parameters the teacher filled in on the registration page.  This means that we try to
     * create a user with the username they requested first.  If they provided a name and it is in use, we will pound a
     * random 3 digit number onto that name and give them that user.  If they did not provide a username we will try using
     * the last name.  If that fails, we try using firstInitial + lastName.  If that fails we use last name
     * + 3 digit number.
     *
     * @param conn
     * @param event
     * @return
     */
    private String createUser(Connection conn, AdminTeacherRegistrationEvent event) throws SQLException {

        int id;
        // try using the preferred username
        if (event.getUn() != null && !event.getUn().equals("")) {
            id = getTeacherId(conn, event.getUn());
            if (id == -1) {
                addUser(conn, event, event.getUn());
                return event.getUn();
            }

            // in use, so tack a number onto it and use that
            Random rand = new Random();
            int x = 1;
            do {
                x = rand.nextInt(999);
                id = getTeacherId(conn, event.getUn() + Integer.toString(x));
            } while (id != -1);
            addUser(conn, event, event.getUn() + Integer.toString(x));
            return event.getUn() + Integer.toString(x);
        }

        // try using lname, fInit+lname, lname+number
        else {
            id = getTeacherId(conn, event.getLname());
            if (id == -1) {
                addUser(conn, event, event.getLname());
                return event.getLname();
            }
            id = getTeacherId(conn, event.getFname().substring(0, 1) + event.getLname());
            if (id == -1) {
                addUser(conn, event, event.getFname().substring(0, 1) + event.getLname());
                return event.getFname().substring(0, 1) + event.getLname();
            }
            Random rand = new Random();
            int x = 1;
            do {
                x = rand.nextInt(999);
                id = getTeacherId(conn, event.getLname() + Integer.toString(x));
            } while (id != -1);
            addUser(conn, event, event.getLname() + Integer.toString(x));
            return event.getLname() + Integer.toString(x);
        }
    }

    /**
     * Look up a username and return its id if found
     *
     * @param conn
     * @param userName
     * @return
     */
    private int getTeacherId(Connection conn, String userName) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("select ID from Teacher where userName=? ");
        ps.setString(1, userName);
        ResultSet rs = ps.executeQuery();
        if (rs.next())
            return rs.getInt(1);
        else
            return -1;
    }


    private void addUser (Connection conn, AdminTeacherRegistrationEvent e, String username ) throws SQLException {
        DbTeacher.insertTeacher(conn,username,e.getFname(),e.getLname(),e.getPw1(),e.getEmail());
    }


}