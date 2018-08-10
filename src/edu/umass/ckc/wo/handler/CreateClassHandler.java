package edu.umass.ckc.wo.handler;


import edu.umass.ckc.wo.beans.Classes;
import edu.umass.ckc.wo.beans.Teacher;
import edu.umass.ckc.wo.db.*;
import edu.umass.ckc.wo.beans.ClassInfo;
import edu.umass.ckc.wo.beans.PretestPool;
import edu.umass.ckc.wo.event.admin.*;
import edu.umass.ckc.servlet.servbase.ServletEvent;
import edu.umass.ckc.servlet.servbase.View;
//import edu.umass.ckc.wo.handler.ClassAdminHelper;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class CreateClassHandler  {
    private String teacherId;
    private HttpSession sess;

    public static final String JSP = "/teacherTools/createClass.jsp";
    public static final String NOCLASS_JSP="/teacherTools/noClassCreateClass.jsp";
    public static final String SELECT_PEDAGOGIES_JSP = "/teacherTools/selectPedagogies.jsp";
    public static final String SIMPLE_SELECT_PEDAGOGIES_JSP = "/teacherTools/simplePedagogySelect.jsp";
    public static final String SIMPLE_CLASS_CONFIG_JSP = "/teacherTools/simpleClassConfig.jsp";
    public static final String SELECT_PRETEST_POOL_JSP = "/teacherTools/selectPretest.jsp";
    public static final String ACTIVATE_HUTS_JSP = "/teacherTools/activatehuts.jsp";
    public static final String CLASS_INFO_JSP = "/teacherTools/classInfo.jsp";
    public static final String EDIT_SURVEYS_JSP = "/teacherTools/editSurveys.jsp";
    public static final String MAINPAGE_JSP ="/teacherTools/wayangMain.jsp";
    public static final String ADMIN_MAIN_JSP ="/teacherTools/adminMain.jsp";




    public CreateClassHandler() {
    }
    public View handleEvent(ServletContext sc, Connection conn, ServletEvent e, HttpServletRequest req, HttpServletResponse resp) throws Exception {

        // State 1: Process the event for a new class.
        // Generate createClass.jsp to get basic info about the class.
        if (e instanceof AdminCreateNewClassEvent) { // START state
            req.setAttribute("action","AdminCreateNewClass");
            req.setAttribute("message","");
            req.setAttribute("teacherId",((AdminCreateNewClassEvent) e).getTeacherId());
            setTeacherName(conn,req,((AdminCreateNewClassEvent) e).getTeacherId());
            Integer adminId = (Integer) req.getSession().getAttribute("adminId"); // determine if this is admin session

            req.setAttribute("sideMenu",adminId != null ? "adminNoClassSideMenu.jsp" : "teacherNoClassSideMenu.jsp"); // set side menu for admin or teacher

            req.getRequestDispatcher(JSP).forward(req,resp);
            return null;
        }
        // State 1A: Process the event for a new class without any previous class
        else if (e instanceof AdminNoClassCreateNewClassEvent){
            req.setAttribute("action","AdminCreateNewClass");
            req.setAttribute("message","");
            req.setAttribute("teacherId",((AdminNoClassCreateNewClassEvent) e).getTeacherId());
            setTeacherName(conn,req,((AdminNoClassCreateNewClassEvent) e).getTeacherId());
            Integer adminId = (Integer) req.getSession().getAttribute("adminId"); // determine if this is admin session

            req.setAttribute("sideMenu",adminId != null ? "adminNoClassSideMenu.jsp" : "teacherNoClassSideMenu.jsp"); // set side menu for admin or teacher

            req.getRequestDispatcher(NOCLASS_JSP).forward(req,resp);
            return null;

        }
        // State 2: Process submit-event generated from the createClass.jsp form that requests class name, town, year.
        // Creates the class in the db.
        //  Generates the next page as simpleClassConfig.jsp
        else if (e instanceof AdminSubmitClassFormEvent) // class created/  Now show select pedagogies page
            return processClass(conn, (AdminSubmitClassFormEvent) e, req, resp);

        // The result of processClass (above) is a simple pedagogy selection page (simplePedagogySelect.jsp).  That page has a link
        // that allows the user to ask for an "advanced selection" page which is a more detailed list of all pedagogies.
        else if (e instanceof AdminAdvancedPedagogySelectionEvent)
            return advancedPedagogySelectionPage(conn, (AdminAdvancedPedagogySelectionEvent) e, req,resp);


        // State 3: Process submitted form generated from the selectPedagogies.jsp page.   Check selections and regenerate
        // pedagogy selection page if there are errors.  If no errors errors, save selections and
        // Generate the next page as selectPretest.jsp (n.b. this JSP page requires that we set
        // its form submission event so we pass AdminSubmitSelectedPretest)
        else if (e instanceof AdminNoClassSubmitClassFormEvent) {
            req.setAttribute("teacherId", ((AdminNoClassSubmitClassFormEvent) e).getTeacherId());
            return processNoClass(conn, (AdminNoClassSubmitClassFormEvent) e, req, resp);
        }
        else if (e instanceof AdminSubmitSelectedPedagogiesEvent) {
            AdminSubmitSelectedPedagogiesEvent e2 = (AdminSubmitSelectedPedagogiesEvent) e;
            if (!ClassAdminHelper.errorCheckSelectedPedagogySubmission(e2.getClassId(),e2.getPedagogyIds(),req,resp,
                    "AdminSubmitSelectedPedagogies", e2.getTeacherId(), conn, true, false)) {
                ClassAdminHelper.saveSelectedPedagogies(conn,e2.getClassId(),e2.getPedagogyIds());

                ClassInfo info = DbClass.getClass(conn,((AdminSubmitSelectedPedagogiesEvent) e).getClassId());
                ClassInfo[] classes = DbClass.getClasses(conn, ((AdminSubmitSelectedPedagogiesEvent) e).getTeacherId());
                Classes bean = new Classes(classes);
                req.setAttribute("bean",bean);
                req.setAttribute("classInfo",info);
                req.setAttribute("classId", ((AdminSubmitSelectedPedagogiesEvent) e).getClassId());
                req.setAttribute("teacherId", ((AdminSubmitSelectedPedagogiesEvent) e).getTeacherId());
                req.setAttribute("action","AdminAlterClassPedagogies");


                generateValidPedagogySubmissionNextPage(conn,e2.getClassId(),req,resp);
            }
            return null;
        }

        // next state will be to select adventures stuff



        else return null;
    }

    public static void setTeacherName(Connection conn, HttpServletRequest req, int teacherId) throws SQLException {
        Teacher t = DbTeacher.getTeacher(conn,teacherId);
        if (t != null) {
            String n = t.getName();
            req.setAttribute("teacherName",n);
        }
    }


    /**
     * Given the fields describing the class.   This will add a row to the class table.
     * @param conn
     * @param e
     * @param req
     * @param resp
     * @return
     * @throws Exception
     */
    private View processClass(Connection conn, AdminSubmitClassFormEvent e, HttpServletRequest req,
                              HttpServletResponse resp) throws Exception {
        String className = e.getClassName();
        String school = e.getSchool();
        String schoolYear = e.getSchoolYear();
        String town = e.getTown();
        String section = e.getSection();
        String grade = e.getGrade();
        int newid;
        if (className.trim().equals("") || school.trim().equals("") || schoolYear.trim().equals("")
                || town.trim().equals("")) {
            req.setAttribute("message","You must correctly fill out all required fields in the form.");
            req.setAttribute("teacherId",e.getTeacherId());
            Integer adminId = (Integer) req.getSession().getAttribute("adminId"); // determine if this is admin session
            req.setAttribute("sideMenu",adminId != null ? "adminSideMenu.jsp" : "teacherSideMenu.jsp"); // set side menu for admin or teacher
            setTeacherName(conn,req,e.getTeacherId());
            req.getRequestDispatcher(JSP).forward(req,resp);
            return null;
        }
        else if (!validateYear(schoolYear)) {

            req.setAttribute("teacherId",e.getTeacherId());
            setTeacherName(conn,req,e.getTeacherId());
            Integer adminId = (Integer) req.getSession().getAttribute("adminId"); // determine if this is admin session
            req.setAttribute("sideMenu",adminId != null ? "adminSideMenu.jsp" : "teacherSideMenu.jsp"); // set side menu for admin or teacher

            req.setAttribute("message","That year is invalid. Please enter year as 2XXX");
            req.getRequestDispatcher(JSP).forward(req,resp);
            return null;
        }
        // After the basic info of a class is successfully given, we go to a simple class config
        // page where a teacher can quickly establish things they care about and then they are done.
        else {
            int defaultPropGroup = DbClass.getPropGroupWithName(conn,"default");
            String tid = Integer.toString(e.getTeacherId());
            // the prop group is a set of questions that we want to ask the user (gender, race, etc).
            // When a class is first created, we use the default prop group.
            // We'll allow it to be altered in the page that allows the user to edit the class fields.
            newid = DbClass.insertClass(conn,className, school, schoolYear, town, section,tid,
                    defaultPropGroup, 0, grade);
            if (newid != -1) {
                DbTopics.insertLessonPlanWithDefaultTopicSequence(conn,newid);
                ClassInfo info = DbClass.getClass(conn,newid);
                info.setSimpleConfigDefaults();
                ClassInfo[] classes = DbClass.getClasses(conn,e.getTeacherId());
                Classes bean = new Classes(classes);
                // The form submission event will go back to AlterClassHandler
                Integer adminId = (Integer) req.getSession().getAttribute("adminId"); // determine if this is admin session
                req.setAttribute("sideMenu",adminId != null ? "adminSideMenu.jsp" : "teacherSideMenu.jsp"); // set side menu for admin or teacher
                // tells the JSP to include a hidden input with createClassSeq=true which means the submit buttons will
                // take the user to the next page in a sequence of class creation which is create-class, simpleConfig, roster
                req.setAttribute("createClassSeq",true);
                req.setAttribute("formSubmissionEvent","AdminSubmitSimpleClassConfig");
                req.setAttribute("pedagogies", DbClassPedagogies.getClassSimpleConfigPedagogyBeans(conn, newid));
                req.setAttribute("bean",bean);
                req.setAttribute("classInfo",info);
                req.setAttribute("classId", newid);
                req.setAttribute("teacherId",e.getTeacherId());
                setTeacherName(conn,req,e.getTeacherId());
                req.setAttribute("action","AdminAdvancedPedagogySelection");
                req.getRequestDispatcher(SIMPLE_CLASS_CONFIG_JSP).forward(req,resp);
                return null;
            }
            else {

                req.setAttribute("teacherId",e.getTeacherId());
                Integer adminId = (Integer) req.getSession().getAttribute("adminId"); // determine if this is admin session
                req.setAttribute("sideMenu",adminId != null ? "adminSideMenu.jsp" : "teacherSideMenu.jsp"); // set side menu for admin or teacher
                setTeacherName(conn,req,e.getTeacherId());
                req.setAttribute("message","Failed to add class.  That class already exists");
                req.getRequestDispatcher(JSP).forward(req,resp);
                return null;
            }
        }
    }

    private View processNoClass(Connection conn, AdminNoClassSubmitClassFormEvent e, HttpServletRequest req,
                              HttpServletResponse resp) throws Exception {
        String className = e.getClassName();
        String school = e.getSchool();
        String schoolYear = e.getSchoolYear();
        String town = e.getTown();
        String section = e.getSection();
        String grade = e.getGrade();
        int newid;
        if (className.trim().equals("") || school.trim().equals("") || schoolYear.trim().equals("")
                || town.trim().equals("")) {
            req.setAttribute("message","You must correctly fill out all required fields in the form.");
            req.setAttribute("teacherId",e.getTeacherId());
            setTeacherName(conn,req,e.getTeacherId());
            req.getRequestDispatcher(NOCLASS_JSP).forward(req,resp);
            return null;
        }
        else if (!validateYear(schoolYear)) {
            req.setAttribute("teacherId",e.getTeacherId());
            setTeacherName(conn,req,e.getTeacherId());
            req.setAttribute("message","That year is invalid. Please enter year as 2XXX");
            req.getRequestDispatcher(NOCLASS_JSP).forward(req,resp);
            return null;
        }
        else {
            int defaultPropGroup = DbClass.getPropGroupWithName(conn,"default");
            String tid = Integer.toString(e.getTeacherId());
            // the prop group is a set of questions that we want to ask the user (gender, race, etc).
            // When a class is first created, we use the default prop group.
            // We'll allow it to be altered in the page that allows the user to edit the class fields.
            newid = DbClass.insertClass(conn,className, school, schoolYear, town, section,tid,
                    defaultPropGroup, 0, grade);
            if (newid != -1) {
                Integer adminId = (Integer) req.getSession().getAttribute("adminId"); // determine if this is admin session
                req.setAttribute("sideMenu",adminId != null ? "adminSideMenu.jsp" : "teacherSideMenu.jsp"); // set side menu for admin or teacher

                req.setAttribute("formSubmissionEvent","AdminSubmitSimpleClassConfig");
                req.setAttribute("pedagogies", DbClassPedagogies.getClassPedagogyBeans(conn,newid));
                req.setAttribute("classId",newid);
                req.setAttribute("teacherId",e.getTeacherId());
                req.setAttribute("createClassSeq",true);
                setTeacherName(conn,req,e.getTeacherId());
                ClassInfo info = DbClass.getClass(conn,newid);
                ClassInfo[] classes = DbClass.getClasses(conn,info.getTeachid());
                Classes bean = new Classes(classes);
                req.setAttribute("bean", bean);
                req.setAttribute("classInfo", info);
                req.setAttribute("action","AdminAdvancedPedagogySelection");
                req.getRequestDispatcher(SIMPLE_CLASS_CONFIG_JSP).forward(req,resp);
//                req.getRequestDispatcher(SELECT_PEDAGOGIES_JSP).forward(req,resp);
                return null;
            }
            else {
                req.setAttribute("teacherId",e.getTeacherId());
                setTeacherName(conn,req,e.getTeacherId());
                req.setAttribute("message","Failed to add class.  That class already exists");
                req.getRequestDispatcher(NOCLASS_JSP).forward(req,resp);
                return null;
            }
        }
    }

    private boolean validateYear (String yr) {
        try {
            int y = Integer.parseInt(yr);
            if (y < 2000 || y > 3000)      // optimistic software lifetime
                return false;
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    // generates the next page after the pedagogy selection page (selectPretest.jsp)
    public static void generateValidPedagogySubmissionNextPage (Connection conn, int classId,
                                                           HttpServletRequest req,
                                                           HttpServletResponse resp) throws SQLException, IOException, ServletException {
        ClassInfo info = DbClass.getClass(conn,classId);
        Integer adminId = (Integer) req.getSession().getAttribute("adminId"); // determine if this is admin session
        req.setAttribute("sideMenu",adminId != null ? "adminSideMenu.jsp" : "teacherSideMenu.jsp"); // set side menu for admin or teacher

        req.setAttribute("formSubmissionEvent","AdminSubmitSelectedPretest");
        req.setAttribute("classInfo",info);
        req.setAttribute("classId",classId);

        ClassInfo[] classes = DbClass.getClasses(conn,info.getTeachid());
        Classes bean = new Classes(classes);
        req.setAttribute("bean", bean);
        req.setAttribute("teacherId",info.getTeachid());
        setTeacherName(conn,req,info.getTeachid());
        req.setAttribute("selectedPool",-1); // on new class no pool is selected
        List<PretestPool> pools = DbPrePost.getAllPretestPools(conn);
        req.setAttribute("pools", pools);
//            req.getRequestDispatcher(SELECT_PRETEST_POOL_JSP).forward(req,resp);

        req.setAttribute("action", "AdminUpdateClassId");

        req.getRequestDispatcher(adminId != null ? ADMIN_MAIN_JSP : MAINPAGE_JSP).forward(req,resp);
    }

    private View advancedPedagogySelectionPage (Connection conn, AdminAdvancedPedagogySelectionEvent e, HttpServletRequest req,
                                                HttpServletResponse resp) throws Exception {

        int classId = e.getClassId();
        ClassInfo info = DbClass.getClass(conn,classId);
        ClassInfo[] classes = DbClass.getClasses(conn,e.getTeacherId());
        Classes bean = new Classes(classes);
        Integer adminId = (Integer) req.getSession().getAttribute("adminId"); // determine if this is admin session
        req.setAttribute("sideMenu",adminId != null ? "adminSideMenu.jsp" : "teacherSideMenu.jsp"); // set side menu for admin or teacher

        req.setAttribute("formSubmissionEvent","AdminSubmitSelectedPedagogies");
        req.setAttribute("pedagogies", DbClassPedagogies.getClassPedagogyBeans(conn,classId));
        req.setAttribute("bean",bean);
        req.setAttribute("classInfo",info);
        req.setAttribute("classId", classId);
        req.setAttribute("teacherId",e.getTeacherId());
        setTeacherName(conn,req,e.getTeacherId());
        req.getRequestDispatcher(SELECT_PEDAGOGIES_JSP).forward(req,resp);
        return null;

    }

 

}