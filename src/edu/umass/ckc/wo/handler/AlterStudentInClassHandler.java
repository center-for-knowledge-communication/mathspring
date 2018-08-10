package edu.umass.ckc.wo.handler;

import edu.umass.ckc.wo.beans.ClassInfo;
import edu.umass.ckc.wo.beans.Classes;
import edu.umass.ckc.wo.db.DbClassPedagogies;
import edu.umass.ckc.wo.db.DbClass;
import edu.umass.ckc.wo.db.DbUser;
import edu.umass.ckc.wo.event.admin.*;
import edu.umass.ckc.wo.tutor.Pedagogy;
import edu.umass.ckc.wo.smgr.User;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.util.List;

/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Jan 23, 2008
 * Time: 2:18:57 PM
 */
public class AlterStudentInClassHandler {
    private String teacherId;
    private HttpSession sess;

   public final static String EDIT_STUDENT_JSP = "/teacherTools/editStudent.jsp";



    public AlterStudentInClassHandler() {
    }

    public void handleEvent (ServletContext sc, Connection conn, AdminAlterStudentInClassEvent e, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String message="";
        int classId = e.getClassId();
        int teacherId = e.getTeacherId();
        int studId = e.getStudId();
        ClassInfo[] classes1 = DbClass.getClasses(conn, teacherId);
        Classes bean1 = new Classes(classes1);
        // a general request to alter classes produces a page listing all the classes with
        // some buttons next to them allowing delete and edit
        if (e instanceof AdminDeleteStudentFromRosterEvent) {
            DbUser.deleteStudent(conn,studId);
            message = "Student successfully deleted.";
        }
        else if (e instanceof AdminClearStudentAllDataEvent) {
            DbUser.deleteStudentData(conn, studId);
            message = "Student data successfully cleared.";
        }
        else if (e instanceof AdminClearStudentPracticeDataEvent) {
            DbUser.deleteStudentPracticeHutData(conn, studId);
            message = "Student practice hut data successfully cleared.";
        }
         else if (e instanceof AdminResetStudentPracticeDataEvent) {
            DbUser.resetStudentPracticeHut(conn, studId);
            message = "Student practice hut successfully reset.";
        }
        else if (e instanceof AdminClearStudentPretestDataEvent) {
            DbUser.deleteStudentPrePostEventData(conn,studId,true,false);
            message = "Student pretest hut data successfully cleared.";
        }
        else if (e instanceof AdminClearStudentPosttestDataEvent) {
            DbUser.deleteStudentPrePostEventData(conn,studId,false,true);
            message = "Student posttest hut data successfully cleared.";

        }
        else if (e instanceof AdminEditStudentEvent) {
            AdminEditStudentEvent e2 = (AdminEditStudentEvent) e;
            ClassInfo classInfo = DbClass.getClass(conn,classId);
            req.setAttribute("classInfo",classInfo);
            User u = DbUser.getStudent(conn,e2.getStudId());
            int pedId = DbUser.getStudentPedagogy(conn,e2.getStudId());
            u.setPedagogyId(pedId);
           
            List<Pedagogy> pedsInUse = DbClassPedagogies.getClassPedagogies(conn,classId);
            Integer adminId = (Integer) req.getSession().getAttribute("adminId"); // determine if this is admin session
            req.setAttribute("sideMenu",adminId != null ? "adminSideMenu.jsp" : "teacherSideMenu.jsp"); // set side menu for admin or teacher

            req.setAttribute("classId",e2.getClassId());
            req.setAttribute("teacherId",e2.getTeacherId());
            CreateClassHandler.setTeacherName(conn,req, e2.getTeacherId());

            req.setAttribute("action", "AdminEditStudent");
            req.setAttribute("pedagogies",pedsInUse);
            req.setAttribute("student",u);
            req.getRequestDispatcher(EDIT_STUDENT_JSP).forward(req,resp);
            return;
        }
        else if (e instanceof AdminAlterStudentEvent) {
            AdminAlterStudentEvent e2= (AdminAlterStudentEvent) e;
            DbUser.alterStudent(conn,e2.getStudId(),e2.getUname(),e2.getFname(),e2.getPassword(),e2.getPedagogyId());
            ClassInfo classInfo = DbClass.getClass(conn,classId);
            req.setAttribute("classInfo",classInfo);
            User u = DbUser.getStudent(conn,e2.getStudId());
            int pedId = DbUser.getStudentPedagogy(conn,e2.getStudId());
            u.setPedagogyId(pedId);

            List<Pedagogy> pedsInUse = DbClassPedagogies.getClassPedagogies(conn,classId);
            Integer adminId = (Integer) req.getSession().getAttribute("adminId"); // determine if this is admin session
            req.setAttribute("sideMenu",adminId != null ? "adminSideMenu.jsp" : "teacherSideMenu.jsp"); // set side menu for admin or teacher

            req.setAttribute("action","AdminAlterStudent");
            req.setAttribute("pedagogies",pedsInUse);
            req.setAttribute("classId",e2.getClassId());
            req.setAttribute("teacherId",e2.getTeacherId());
            CreateClassHandler.setTeacherName(conn,req, e2.getTeacherId());
            req.setAttribute("student",u);
            req.setAttribute("message","Edits have been saved successfully.");
            req.getRequestDispatcher(EDIT_STUDENT_JSP).forward(req,resp);
            return;
        }
        ClassInfo classInfo = DbClass.getClass(conn,classId);
        Integer adminId = (Integer) req.getSession().getAttribute("adminId"); // determine if this is admin session
        req.setAttribute("sideMenu",adminId != null ? "adminSideMenu.jsp" : "teacherSideMenu.jsp"); // set side menu for admin or teacher

        req.setAttribute("classInfo",classInfo);
        req.setAttribute("teacherId",teacherId);
        CreateClassHandler.setTeacherName(conn,req,teacherId);
        req.setAttribute("classId",classId);
        req.setAttribute("action","AdminEditClassList");
        req.setAttribute("students",DbClass.getClassStudents(conn,classId));
        req.setAttribute("message",message);
        req.setAttribute("bean",bean1);
        req.getRequestDispatcher(AlterClassHandler.ROSTER_JSP).forward(req,resp);

    }

}