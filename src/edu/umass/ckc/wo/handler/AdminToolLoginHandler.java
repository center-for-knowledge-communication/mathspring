package edu.umass.ckc.wo.handler;

import edu.umass.ckc.wo.beans.ClassInfo;
import edu.umass.ckc.wo.beans.Classes;
import edu.umass.ckc.wo.beans.Teacher;
import edu.umass.ckc.wo.db.DbAdmin;
import edu.umass.ckc.wo.db.DbClass;
import edu.umass.ckc.wo.db.DbTeacher;
import edu.umass.ckc.wo.event.admin.AdminLoginEvent;
import edu.umass.ckc.wo.event.admin.AdminTeacherLoginEvent;
import edu.umass.ckc.wo.html.admin.Variables;
import edu.umass.ckc.wo.tutor.Settings;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;
import java.util.List;

/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Jan 31, 2006
 * Time: 2:06:10 PM
 */
public class AdminToolLoginHandler {
    public void handleEvent(Connection conn, final Variables v,
                            AdminTeacherLoginEvent event, HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws Exception {
        int id,sessId;
       Teacher user;
        String teacherName;
        // Logging in as administrator because the URL was /WoAdmin?actionAdminLogin
        if (event instanceof AdminLoginEvent) {
            servletRequest.setAttribute("message","");
            servletRequest
                    .getRequestDispatcher("/teacherTools/teacherLogin.jsp")
                    .forward(servletRequest,servletResponse);
        }
        // no user action has taken place so its a AdminTeacherLoginEvent.  Show the new login page which allows both
        // students and teachers to login.  If this is the old GUI, show the old teacher tools login page.
        else if (!event.isLogin() && !event.isReg()) {
            servletRequest.setAttribute("message","");
            servletRequest
                    .getRequestDispatcher(Settings.useNewGUI()
                            ? "/login/loginK12_new.jsp"
                            : "/teacherTools/teacherLogin.jsp")
                    .forward(servletRequest,servletResponse);
        }
        // admin logins
        else if (event.isLogin() && (user = DbAdmin.getAdminSession(conn, event.getUname(), event.getPw())) != null) {
            showAdminMain(conn, servletRequest, servletResponse, user,null, -1);

        }
        // teacher logins
        else if (event.isLogin()) {
            id = DbTeacher.getTeacherId(conn, event.getUname(), event.getPw());
            if (id == -1) {
                servletRequest.setAttribute("message","Incorrect user/password");
                servletRequest
                        .getRequestDispatcher("b".equals(servletRequest.getParameter("var"))
                            ? "/login/loginK12_new.jsp"
                            : "/teacherTools/teacherLogin.jsp")
                        .forward(servletRequest,servletResponse);
            }
            else {
                // If its a teacher, invalidate the session associated with the browser so that all pages that the
                // teacher goes to will be shown for teacher and not an admin (which would be shown if a session
                // exists for the browser).
                servletRequest.getSession().invalidate();
                teacherName = DbTeacher.getTeacherName(conn, id);
                //check for classes with teacherId
                ClassInfo[] classes1 = DbClass.getClasses(conn, id);
                Classes bean1 = new Classes(classes1);

                if (classes1.length > 0){
                    int classId = classes1[classes1.length-1].getClassid();
                    ClassInfo classInfo = DbClass.getClass(conn,classId);
                    //if hasClasses, pass in teacherid, classid, teacherName
                    servletRequest.setAttribute("action","AdminUpdateClassId");
                    servletRequest.setAttribute("bean", bean1);
                    servletRequest.setAttribute("classInfo", classInfo);
                    servletRequest.setAttribute("classId", Integer.toString(classId));
                    servletRequest.setAttribute("teacherId",Integer.toString(id));
                    CreateClassHandler.setTeacherName(conn,servletRequest, id);
                    servletRequest.getRequestDispatcher(AlterClassHandler.MAIN_WAYANG_JSP).forward(servletRequest,servletResponse);
                    //servletRequest.getRequestDispatcher("/teacherTools/teacherActivities.jsp").forward(servletRequest,servletResponse);
                }else{
                    //pass in classId, and main page.
                    servletRequest.setAttribute("teacherId",Integer.toString(id));
                    servletRequest.getRequestDispatcher(AlterClassHandler.MAIN_NOCLASS_JSP).forward(servletRequest,servletResponse);
                }
                //ClassInfo classInfo = DbClass.getClass(conn,hasClasses);

                //servletRequest.setAttribute("teacherId",Integer.toString(id));
                //servletRequest.setAttribute("action","AdminUpdateClassId");
                //servletRequest.setAttribute("bean", bean1);
                //servletRequest.setAttribute("classInfo", classInfo);
                //servletRequest.setAttribute("classId", Integer.toString(hasClasses));
                //servletRequest.setAttribute("teacherName", teacherName);
                //servletRequest.getRequestDispatcher("/teacherTools/wayangMain.jsp").forward(servletRequest,servletResponse);
            }
        }
        else if (event.isReg()) {
            servletRequest.setAttribute("message","");
            servletRequest.getRequestDispatcher(Settings.useNewGUI()
                    ? "/teacherTools/teacherRegister_new.jsp"
                    : "/teacherTools/teacherRegister.jsp").forward(servletRequest,servletResponse);

        }
    }

    public static void showAdminMain(Connection conn, HttpServletRequest servletRequest,
                                     HttpServletResponse servletResponse, Teacher admin, Teacher teacher, int classId) throws SQLException, ServletException, IOException {
        int sessId;
        sessId = DbAdmin.getNewSession(conn, admin.getId());
        int adminId = admin.getId();
        HttpSession session = servletRequest.getSession();

        session.setAttribute("adminId",adminId);  // save the adminId in the session
        System.out.println("logging in as admin");
        servletRequest.setAttribute("sessId",Integer.toString(sessId));
        // TODO make a clone of the class selection JSP used by students.
        // If a teacher has been selected, get his classes; o/w all classes
        ClassInfo[] classes1 = teacher != null ? DbClass.getClasses(conn,teacher.getId()) : DbClass.getAllClasses(conn, false);
        Classes bean1 = new Classes(classes1);
        List<Teacher> teachers = DbTeacher.getAllTeachers(conn, false);
        ClassInfo classInfo = null;
        if (classId == -1) {
            if (classes1.length > 0) {
                classId = classes1[classes1.length-1].getClassid();
                classInfo= DbClass.getClass(conn,classId);
            }
        }
        else classInfo = DbClass.getClass(conn,classId);
        //if hasClasses, pass in teacherid, classid, teacherName
        servletRequest.setAttribute("action","AdminUpdateClassId");
        servletRequest.setAttribute("bean", bean1);
        servletRequest.setAttribute("teachers", teachers);
        if (classes1.length < 1)
            servletRequest.setAttribute("sideMenu", "adminNoClassSideMenu.jsp");
        else servletRequest.setAttribute("sideMenu", "adminSideMenu.jsp");


        servletRequest.setAttribute("classInfo", classInfo);
        servletRequest.setAttribute("classId", Integer.toString(classId));
        servletRequest.setAttribute("teacherId",teacher != null ? teacher.getId() : -1);  // use teacherId so that this will send to other pages
        if (teacher != null)
            CreateClassHandler.setTeacherName(conn,servletRequest, teacher.getId());
        servletRequest.setAttribute("adminName", admin.getName());
        servletRequest.getRequestDispatcher(AlterClassHandler.ADMIN_MAIN_JSP).forward(servletRequest,servletResponse);
    }

    public static String getName (Teacher user) {
        if (user.getFname() != null && user.getLname() != null)
            return user.getFname() + " " + user.getLname();
        else return user.getUserName();

    }






}