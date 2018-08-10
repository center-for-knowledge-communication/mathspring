package edu.umass.ckc.wo.handler;

import edu.umass.ckc.wo.admin.ClassContentSelector;
import edu.umass.ckc.wo.beans.*;
import edu.umass.ckc.wo.db.*;
import edu.umass.ckc.wo.event.admin.*;
import edu.umass.ckc.servlet.servbase.UserException;


import edu.umass.ckc.wo.strat.TutorStrategy;
import edu.umass.ckc.wo.tutor.Pedagogy;
import edu.umass.ckc.wo.exc.DeveloperException;
import edu.umass.ckc.wo.admin.ClassCloner;
import edu.umass.ckc.wo.smgr.User;
import edu.umass.ckc.wo.event.admin.AdminAlterClassCreateStudentsEvent;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Jan 23, 2008
 * Time: 2:18:57 PM
 */
public class AlterClassHandler {
    private int teacherId;
    private HttpSession sess;

    public final static String ALTER_CLASSES_JSP = "/teacherTools/alterClasses.jsp";
    public final static String EDIT_CLASS_JSP = "/teacherTools/editClass.jsp";
    public final static String CLONE_CLASS_JSP = "/teacherTools/cloneClass.jsp";
    public final static String OTHER_CONFIG_JSP = "/teacherTools/otherConfig.jsp";
    public final static String ROSTER_JSP = "/teacherTools/roster.jsp";
    public final static String MAIN_WAYANG_JSP = "/teacherTools/wayangMain.jsp";
    public final static String ADMIN_MAIN_JSP = "/teacherTools/adminMain.jsp";
    public final static String MAIN_NOCLASS_JSP = "/teacherTools/mainNoClasses.jsp";



    public AlterClassHandler() {
    }

    public void handleEvent (ServletContext sc, Connection conn, AdminClassEvent e, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        teacherId = e.getTeacherId();
        // a general request to alter classes produces a page listing all the classes with
        // some buttons next to them allowing delete and edit
        if (e instanceof AdminAlterClass1Event) { // START state
            ClassInfo[] classes = DbClass.getClasses(conn,teacherId);
            Classes bean = new Classes(classes);
            String action = "ALTER_CLASS_JSP";
            req.setAttribute("action", action);
            req.setAttribute("bean",bean);
            req.setAttribute("teacherId",teacherId);
            CreateClassHandler.setTeacherName(conn,req,teacherId);
            req.getRequestDispatcher(ALTER_CLASSES_JSP).forward(req,resp);
        }
        // When a request is to delete a class, delete it and its pedagogies
        // then just show the list of classes remaining
         else if (e instanceof AdminDeleteClassEvent) {
            // first get rid of any classpedagogies rows in the db
            DbClassPedagogies.removeClassPedagogies(conn, ((AdminAlterClassEditEvent) e).getClassId());
            // now get rid of the class row in the db
            DbClass.deleteClass(conn,((AdminAlterClassEditEvent) e).getClassId());
            ClassInfo[] classes = DbClass.getClasses(conn,teacherId);
            Classes bean = new Classes(classes);
            Integer adminId = (Integer) req.getSession().getAttribute("adminId"); // determine if this is admin session
            req.setAttribute("sideMenu",adminId != null ? "adminSideMenu.jsp" : "teacherSideMenu.jsp"); // set side menu for admin or teacher
            req.setAttribute("bean",bean);
            CreateClassHandler.setTeacherName(conn,req,teacherId);
            req.setAttribute("teacherId",teacherId);
            req.getRequestDispatcher(ALTER_CLASSES_JSP).forward(req,resp);
        }
         // when a request is to clone a class, then goto a JSP that requests info on the name and section.
        else if (e instanceof AdminAlterClassCloneClassEvent) {
            int classId =  ((AdminAlterClassCloneClassEvent) e).getClassId();
            ClassInfo[] classes = DbClass.getClasses(conn,teacherId);
            Classes bean = new Classes(classes);
            req.setAttribute("bean",bean);
            ClassInfo classInfo = DbClass.getClass(conn,classId);
            List<Pedagogy> pedsInUse = DbClassPedagogies.getClassPedagogies(conn,classId);
            PretestPool pool = DbPrePost.getPretestPool(conn,classId);
            Integer adminId = (Integer) req.getSession().getAttribute("adminId"); // determine if this is admin session
            req.setAttribute("sideMenu",adminId != null ? "adminSideMenu.jsp" : "teacherSideMenu.jsp"); // set side menu for admin or teacher
            CreateClassHandler.setTeacherName(conn,req,teacherId);
            req.setAttribute("action","AdminAlterClassCloneClass");
            req.setAttribute("pedagogies",pedsInUse);
            req.setAttribute("classInfo",classInfo);
            req.setAttribute("pool",pool);
            req.setAttribute("classId", classId);
            req.setAttribute("teacherId", teacherId);
            req.getRequestDispatcher(CLONE_CLASS_JSP).forward(req,resp);
        }
        // when a request is to edit a class, then goto a JSP that allows fields of the class
        // to be edited.
        else if (e instanceof AdminAlterClassEditEvent) {
            ClassInfo classInfo = DbClass.getClass(conn,((AdminAlterClassEditEvent) e).getClassId());
            List<Pedagogy> pedsInUse = DbClassPedagogies.getClassPedagogies(conn, ((AdminAlterClassEditEvent) e).getClassId());
            PretestPool pool = DbPrePost.getPretestPool(conn,((AdminAlterClassEditEvent) e).getClassId());
            ClassInfo[] classes = DbClass.getClasses(conn,teacherId);
            Classes bean = new Classes(classes);
            Integer adminId = (Integer) req.getSession().getAttribute("adminId"); // determine if this is admin session
            req.setAttribute("sideMenu",adminId != null ? "adminSideMenu.jsp" : "teacherSideMenu.jsp"); // set side menu for admin or teacher
            CreateClassHandler.setTeacherName(conn,req,teacherId);
            req.setAttribute("teacherId", teacherId);
            req.setAttribute("classId",((AdminAlterClassEditEvent) e).getClassId());
            req.setAttribute("bean",bean);
            req.setAttribute("action", "AdminAlterClassEdit");
            req.setAttribute("pedagogies",pedsInUse);
            req.setAttribute("classInfo",classInfo);
            req.setAttribute("pool",pool);
            req.getRequestDispatcher(EDIT_CLASS_JSP).forward(req,resp);
        }

        // creating a clone of an existing class
        else if (e instanceof AdminAlterClassCloneSubmitInfoEvent) {
            AdminAlterClassCloneSubmitInfoEvent e2 = (AdminAlterClassCloneSubmitInfoEvent) e;
            int classId = ClassCloner.cloneClass(conn,e2.getClassId(),e2.getClassName(),e2.getSection());
            if (classId < 0) {
                classId = e2.getClassId();
                ClassInfo classInfo = DbClass.getClass(conn,classId);
                List<Pedagogy> pedsInUse = DbClassPedagogies.getClassPedagogies(conn,classId);
                PretestPool pool = DbPrePost.getPretestPool(conn,classId);
                Integer adminId = (Integer) req.getSession().getAttribute("adminId"); // determine if this is admin session
                req.setAttribute("sideMenu",adminId != null ? "adminSideMenu.jsp" : "teacherSideMenu.jsp"); // set side menu for admin or teacher
                req.setAttribute("action", "AdminAlterClassCloneClass");
                CreateClassHandler.setTeacherName(conn,req,teacherId);
                req.setAttribute("classId",classId);
                req.setAttribute("teacherId", teacherId);
                req.setAttribute("pedagogies",pedsInUse);
                req.setAttribute("classInfo",classInfo);
                ClassInfo[] classes = DbClass.getClasses(conn,teacherId);
                Classes bean = new Classes(classes);
                req.setAttribute("bean",bean);
                req.setAttribute("pool",pool);
                req.setAttribute("message","Cloning the class failed.  For identification purposes you MUST give a new name and section to this class.");
                req.getRequestDispatcher(CLONE_CLASS_JSP).forward(req,resp);
            }
            else {
                ClassInfo classInfo = DbClass.getClass(conn,classId);
                List<Pedagogy> pedsInUse = DbClassPedagogies.getClassPedagogies(conn, classId);
                PretestPool pool = DbPrePost.getPretestPool(conn,classId);
                Integer adminId = (Integer) req.getSession().getAttribute("adminId"); // determine if this is admin session
                req.setAttribute("sideMenu",adminId != null ? "adminSideMenu.jsp" : "teacherSideMenu.jsp"); // set side menu for admin or teacher
                req.setAttribute("action", "AdminAlterClassCloneClass" );
                req.setAttribute("classId",e2.getClassId());
                CreateClassHandler.setTeacherName(conn,req,teacherId);
                req.setAttribute("teacherId", teacherId);
                req.setAttribute("classInfo",classInfo);
                ClassInfo[] classes = DbClass.getClasses(conn,teacherId);
                Classes bean = new Classes(classes);
                req.setAttribute("bean",bean);
                req.setAttribute("pedagogies",pedsInUse);
                req.setAttribute("pool",pool);
                req.setAttribute("message", "Successfuly created clone " + classInfo.getName() + " " + classInfo.getSection());
                req.getRequestDispatcher(CLONE_CLASS_JSP).forward(req,resp);
            }

        }

        // update in db and then forward to classInfo.jsp.
        // see editClass.jsp for note about invalid XML (in trying to get buttons to be on a single line)
        // that may cause problems
        else if (e instanceof AdminAlterClassSubmitInfoEvent) {
            AdminAlterClassSubmitInfoEvent e2= (AdminAlterClassSubmitInfoEvent) e;
            DbClass.updateClass(conn,e2.getClassId(),e2.getClassName(),e2.getSchool(),e2.getSchoolYear(),
                    e2.getTown(),e2.getSection(), e2.getGrade());
            ClassInfo classInfo = DbClass.getClass(conn,((AdminAlterClassSubmitInfoEvent) e).getClassId());
            List<Pedagogy> pedsInUse = DbClassPedagogies.getClassPedagogies(conn, ((AdminAlterClassSubmitInfoEvent) e).getClassId());
            PretestPool pool = DbPrePost.getPretestPool(conn,e2.getClassId());
            Integer adminId = (Integer) req.getSession().getAttribute("adminId"); // determine if this is admin session
            req.setAttribute("sideMenu",adminId != null ? "adminSideMenu.jsp" : "teacherSideMenu.jsp"); // set side menu for admin or teacher

            req.setAttribute("action","AdminAlterClassSubmitInfoEvent");
            req.setAttribute("pedagogies",pedsInUse);
            req.setAttribute("pool",pool);
            req.setAttribute("classInfo",classInfo);
            ClassInfo[] classes = DbClass.getClasses(conn,teacherId);
            Classes bean = new Classes(classes);
            CreateClassHandler.setTeacherName(conn,req,teacherId);
            req.setAttribute("teacherId", teacherId);
            req.setAttribute("classId",((AdminAlterClassSubmitInfoEvent) e).getClassId());
            req.setAttribute("bean",bean);
            req.setAttribute("action", "AdminAlterClassEdit");
            req.getRequestDispatcher(EDIT_CLASS_JSP).forward(req,resp);
        }

        // Events below have to do with altering an existing class

        // Change Class's Pedagogies Event coming in from either classInfo.jsp or editClass.jsp
        // Produce a new list of pedagogies to choose from (when the pedagogy selections on this
        // form are submitted, the old pedagogies for the class are cleared out)
        else if (e instanceof AdminAlterClassPedagogiesEvent) {
            // This JSP is used by both the creation of class pages and the alteration of class
            // pages.   This first parameter is the type of event that will be created when the
            // form is submitted.   For Class alteration, it should generate AdminAlterClassSubmitSelectedPedagogiesEvent
            // which is processed above.
            req.setAttribute("formSubmissionEvent","AdminAlterClassSubmitSelectedPedagogies");
            int classId = ((AdminAlterClassPedagogiesEvent) e).getClassId();
            ClassInfo[] classes1 = DbClass.getClasses(conn, teacherId);
            Classes bean1 = new Classes(classes1);
            ClassInfo classInfo = DbClass.getClass(conn,classId);
            Integer adminId = (Integer) req.getSession().getAttribute("adminId"); // determine if this is admin session
            req.setAttribute("sideMenu",adminId != null ? "adminSideMenu.jsp" : "teacherSideMenu.jsp"); // set side menu for admin or teacher
            // If the class is set up with tutoring strategies then we want to list no pedagogies as selected;
            // if there are no tutoring strategies set up for the class, then this will see if there are some
            // pedagogies selected for the class and will list them.  If no pedagogies and no tutoring strategies, this
            // selects all the default pedagogies and sets them as on for the class.
            List<TutorStrategy> strats = DbStrategy.getStrategies(conn,classId);
            if (strats.size() == 0) {
                List<PedagogyBean> peds = DbClassPedagogies.getClassSimpleConfigPedagogyBeans(conn,classId);
                req.setAttribute("pedagogies", peds);
            }
            else
                req.setAttribute("pedagogies", new ArrayList<PedagogyBean>());
            req.setAttribute("classId", classId);
            CreateClassHandler.setTeacherName(conn,req,teacherId);
            req.setAttribute("teacherId", teacherId);
            req.setAttribute("classInfo", classInfo);
            req.setAttribute("bean", bean1);
            req.setAttribute("action","AdminAlterClassPedagogies");
            req.setAttribute("usingTutoringStrategies",strats.size() > 0);
            req.getRequestDispatcher(CreateClassHandler.SIMPLE_SELECT_PEDAGOGIES_JSP).forward(req,resp);
        }
        else if (e instanceof AdminAlterClassAdvancedPedagogySelectionEvent )    {
            int classId = e.getClassId();
            List<TutorStrategy> strats = DbStrategy.getStrategies(conn,classId);
            Integer adminId = (Integer) req.getSession().getAttribute("adminId"); // determine if this is admin session
            req.setAttribute("sideMenu",adminId != null ? "adminSideMenu.jsp" : "teacherSideMenu.jsp"); // set side menu for admin or teacher
            req.setAttribute("action","AdminAlterClassAdvancedPedagogySelection");
            req.setAttribute("formSubmissionEvent","AdminAlterClassSubmitSelectedPedagogies");
            PedagogyBean[] peds= DbClassPedagogies.getClassPedagogyBeans(conn,classId);
            // If the class is set up with strategies don't put pedagogies on the page to choose from.
            if (strats.size() > 0)
                peds = new PedagogyBean[0];
            req.setAttribute("pedagogies", peds);
            req.setAttribute("classId",classId);
            CreateClassHandler.setTeacherName(conn,req,teacherId);
            req.setAttribute("teacherId", teacherId);
            ClassInfo info = DbClass.getClass(conn,classId);
            ClassInfo[] classes = DbClass.getClasses(conn,info.getTeachid());
            Classes bean = new Classes(classes);
            req.setAttribute("bean", bean);
            req.setAttribute("classInfo", info);
            req.setAttribute("useTutoringStrategies", (strats.size() > 0));
            req.getRequestDispatcher(CreateClassHandler.SELECT_PEDAGOGIES_JSP).forward(req,resp);

        }

        // Pedagogies submitted.  Error check the submission and regenerate page if errors.  Otherwise
        // overwrite the class's pedagogies with those submitted and then generate the page again to show the edits
        else if (e instanceof AdminAlterClassSubmitSelectedPedagogiesEvent) {
            AdminAlterClassSubmitSelectedPedagogiesEvent e2 = (AdminAlterClassSubmitSelectedPedagogiesEvent) e;
            if (!e2.isUseTutoringStrategies() && !ClassAdminHelper.errorCheckSelectedPedagogySubmission(e2.getClassId(), e2.getPedagogyIds(), req, resp,
                    "AdminAlterClassSubmitSelectedPedagogies", e2.getTeacherId(), conn, e2.isSimplePage(), e2.isUseTutoringStrategies())) {
                ClassAdminHelper.saveSelectedPedagogies(conn, e2.getClassId(), e2.getPedagogyIds());
                generateValidPedagogySubmissionNextPage(conn, e2.getClassId(), req, resp, e2.isSimplePage(), e2.isUseTutoringStrategies());
            } else if (e2.isUseTutoringStrategies()) {
                // We pass an empty list to the method below so it will remove all the pedagogies that are set for this class
                // which is how we indicate that tutoring strategies will be in use (and configured for the class using a separate tool)
                ClassAdminHelper.saveSelectedPedagogies(conn, e2.getClassId(), new ArrayList<String>());
                generateValidPedagogySubmissionNextPage(conn, e2.getClassId(), req, resp, e2.isSimplePage(), e2.isUseTutoringStrategies());

            }
        }
        else if (e instanceof AdminOtherClassConfigEvent) {
            AdminOtherClassConfigEvent e2 = (AdminOtherClassConfigEvent) e;
            ClassInfo[] classes1 = DbClass.getClasses(conn, teacherId);
            Classes bean1 = new Classes(classes1);
            int classId = e2.getClassId();
            ClassInfo classInfo = DbClass.getClass(conn,classId);
            Integer adminId = (Integer) req.getSession().getAttribute("adminId"); // determine if this is admin session
            req.setAttribute("sideMenu",adminId != null ? "adminSideMenu.jsp" : "teacherSideMenu.jsp"); // set side menu for admin or teacher

            req.setAttribute("action","AdminOtherClassConfig");
            req.setAttribute("classInfo",classInfo);
            req.setAttribute("bean", bean1);
            req.setAttribute("classId", classId);
            CreateClassHandler.setTeacherName(conn,req,teacherId);
            req.setAttribute("teacherId", teacherId);
            req.getRequestDispatcher(OTHER_CONFIG_JSP).forward(req, resp);
        }
        else if (e instanceof AdminUpdateClassIdEvent){
            Integer adminId = (Integer) req.getSession().getAttribute("adminId"); // determine if this is admin session
            if (adminId != null)  {
                Teacher t = DbTeacher.getTeacher(conn, e.getTeacherId());
                Teacher a = DbAdmin.getAdmin(conn, adminId);
                AdminToolLoginHandler.showAdminMain(conn,req,resp,a,t, e.getClassId());
            }
            else {
                //Opens ups new page with updated classID
                ClassInfo[] classes1 = DbClass.getClasses(conn, teacherId);
                Classes bean1 = new Classes(classes1);
                AdminUpdateClassIdEvent e2 = (AdminUpdateClassIdEvent) e;
                int newteachId = e2.getTeacherId();
                int classId1 = e2.getClassId();
                System.out.println("***********  new classId: " + classId1);
                System.out.println("***********  teacherId " + newteachId);
                ClassInfo classInfo = DbClass.getClass(conn, classId1);
                req.setAttribute("sideMenu", "teacherSideMenu.jsp"); // set side menu for admin or teacher

                req.setAttribute("action","AdminUpdateClassId");
                req.setAttribute("bean", bean1);
                req.setAttribute("classInfo", classInfo);
                req.setAttribute("classId", classId1);
                CreateClassHandler.setTeacherName(conn,req,teacherId);
                req.setAttribute("teacherId", teacherId);
                //req.setAttribute("teacherName", teacherName);
                req.getRequestDispatcher( MAIN_WAYANG_JSP).forward(req,resp);
            }
        }
        else if (e instanceof AdminAlterClassOtherConfigSubmitInfoEvent) {
            AdminAlterClassOtherConfigSubmitInfoEvent e2 = (AdminAlterClassOtherConfigSubmitInfoEvent) e;
            int classId = e2.getClassId();

            DbClass.updateClassEmailSettings(conn,classId,e2.studentEmailInterval,e2.studentReportPeriod,e2.teacherEmailInterval,e2.teacherReportPeriod);
            ClassInfo classInfo = DbClass.getClass(conn, classId);
            List<Pedagogy> pedsInUse = DbClassPedagogies.getClassPedagogies(conn, classId);
            PretestPool pool = DbPrePost.getPretestPool(conn, classId);
            Integer adminId = (Integer) req.getSession().getAttribute("adminId"); // determine if this is admin session
            req.setAttribute("sideMenu",adminId != null ? "adminSideMenu.jsp" : "teacherSideMenu.jsp"); // set side menu for admin or teacher

            req.setAttribute("action", "AdminAlterClassOtherConfigSubmitInfo");
            req.setAttribute("pedagogies", pedsInUse);
            req.setAttribute("pool",pool);
            req.setAttribute("classInfo",classInfo);
            ClassInfo[] classes1 = DbClass.getClasses(conn, teacherId);
            Classes bean1 = new Classes(classes1);

            req.setAttribute("action","AdminOtherClassConfig");
            req.setAttribute("classInfo",classInfo);
            req.setAttribute("bean", bean1);
            req.setAttribute("classId", classId);
            CreateClassHandler.setTeacherName(conn,req,teacherId);
            req.setAttribute("teacherId", teacherId);

            req.getRequestDispatcher(OTHER_CONFIG_JSP).forward(req,resp);
        }
        else if (e instanceof AdminEditClassListEvent) {
            editClassList(conn, e.getClassId(), teacherId, req, resp);
        }
        else if (e instanceof AdminAlterClassCreateStudentsEvent) {
            AdminAlterClassCreateStudentsEvent e2 = (AdminAlterClassCreateStudentsEvent) e;
            int classId = e2.getClassId();
            ClassInfo classInfo = DbClass.getClass(conn,classId);
            List<User> students = DbClass.getClassStudents(conn, classId);
            List<String> peds = DbClassPedagogies.getClassPedagogyIds(conn, classId);
            ClassInfo[] classes1 = DbClass.getClasses(conn, teacherId);
            Classes bean1 = new Classes(classes1);
            String errMessage="";
            if (peds.size() == 0)
                errMessage = "Class does not have pedagogies selected.   Please select some before generating students.";

            else if (e2.getEndNum() < e2.getBeginNum())
                errMessage = "Begin Number must be <= End Number";
            else {
                try {
                    DbClass.createClassStudents(conn,classInfo,e2.getPrefix(),e2.getPassword(),e2.getBeginNum(),e2.getEndNum(),e2.getTestUserPrefix(), e2.getPassword());
                    students = DbClass.getClassStudents(conn, classId);
                } catch (UserException ue) {
                    errMessage = "Failure while creating class. " + ue.getMessage();
                }
            }

            Integer adminId = (Integer) req.getSession().getAttribute("adminId"); // determine if this is admin session
            req.setAttribute("sideMenu",adminId != null ? "adminSideMenu.jsp" : "teacherSideMenu.jsp"); // set side menu for admin or teacher


            req.setAttribute("action","AdminEditClassList");
            req.setAttribute("message",errMessage);
            req.setAttribute("classInfo", classInfo);
            req.setAttribute("classId", classId);
            CreateClassHandler.setTeacherName(conn,req,teacherId);
            req.setAttribute("teacherId", teacherId);
            req.setAttribute("bean", bean1);
            req.setAttribute("students",students);
            req.setAttribute("numStudents", students.size());
            if (e2.isCreateClassSeq())  {
                req.setAttribute("message","Class successfully created!");
                req.getRequestDispatcher(MAIN_WAYANG_JSP).forward(req, resp);
            }
            else req.getRequestDispatcher(ROSTER_JSP).forward(req, resp);
        }
        else if (e instanceof AdminSimpleClassSetupEvent) {
            int classId =  e.getClassId();
            ClassInfo info = DbClass.getClass(conn, classId);
            Integer adminId = (Integer) req.getSession().getAttribute("adminId"); // determine if this is admin session
            ClassInfo[] classes = DbClass.getClasses(conn,e.getTeacherId());
            Classes bean = new Classes(classes);
            // The form submission event will go back to AlterClassHandler
            req.setAttribute("formSubmissionEvent","AdminSubmitSimpleClassConfig");
            req.setAttribute("pedagogies", DbClassPedagogies.getClassSimpleConfigPedagogyBeans(conn, classId));
            req.setAttribute("bean",bean);
            req.setAttribute("classInfo",info);
            req.setAttribute("classId", classId);
            CreateClassHandler.setTeacherName(conn,req,teacherId);
            req.setAttribute("teacherId", teacherId);
            req.setAttribute("action","AdminSimpleClassSetup");
            req.setAttribute("sideMenu",adminId != null ? "adminSideMenu.jsp" : "teacherSideMenu.jsp");
//                req.getRequestDispatcher(SIMPLE_SELECT_PEDAGOGIES_JSP).forward(req,resp);
            req.getRequestDispatcher(CreateClassHandler.SIMPLE_CLASS_CONFIG_JSP).forward(req,resp);
        }
        else if (e instanceof AdminSubmitSimpleClassConfigEvent) {
            AdminSubmitSimpleClassConfigEvent ee = (AdminSubmitSimpleClassConfigEvent) e;
            // if the submission is part of a create-class sequence go to the next page in the sequence: the edit roster
            if (((AdminSubmitSimpleClassConfigEvent) e).isCreateClassSeq())   {
                int classId =  e.getClassId();
                DbClass.setSimpleConfig(conn, classId, ee.getLc(), ee.getCollab(), ee.getDiffRate(), ee.getLowDiff(), ee.getHighDiff());
                ClassInfo info = DbClass.getClass(conn, classId);
                // Contains settings which allows us to select content for this class
                new ClassContentSelector(conn).selectContent(info);
                editClassList(conn,e.getClassId(),e.getTeacherId(),req,resp);
            }
            // not part of a create-class sequence so just show the simple config page again.
            else {
                int classId =  e.getClassId();
                ClassInfo info = DbClass.getClass(conn, classId);
                String oldLowDiff = info.getSimpleLowDiff();
                String oldHighDiff = info.getSimpleHighDiff();
                String message="";
                if (info.getGrade() != null) {
                    DbClass.setSimpleConfig(conn, classId, ee.getLc(), ee.getCollab(), ee.getDiffRate(), ee.getLowDiff(), ee.getHighDiff());
                    info = DbClass.getClass(conn, classId);
                    // If the window of difficulty (lo-hi) is being set for the first time or changed from it's previous settings, then
                    // we run the content selector (a slow process).
                    if (oldLowDiff == null || oldHighDiff == null ||
                            !oldHighDiff.equals(ee.getHighDiff()) || !oldLowDiff.equals(ee.getLowDiff()))
                        new ClassContentSelector(conn).selectContent(info);
                    message = "Your edits have been successully stored and content has been adjusted." ;
                }
                else message = "You need to set a grade for this class. Go to class information page to set this.";

                // After alterring the class we show the page again with a message saying edits were successful.
                Integer adminId = (Integer) req.getSession().getAttribute("adminId"); // determine if this is admin session

                ClassInfo[] classes = DbClass.getClasses(conn,e.getTeacherId());
                Classes bean = new Classes(classes);
                // The form submission event will go back to AlterClassHandler
                req.setAttribute("formSubmissionEvent","AdminSubmitSimpleClassConfig");
                req.setAttribute("pedagogies", DbClassPedagogies.getClassSimpleConfigPedagogyBeans(conn, classId));
                req.setAttribute("bean",bean);
                req.setAttribute("classInfo",info);
                req.setAttribute("classId", classId);
                CreateClassHandler.setTeacherName(conn,req,teacherId);
                req.setAttribute("teacherId", teacherId);
                req.setAttribute("action","AdminAdvancedPedagogySelection");
                req.setAttribute("sideMenu",adminId != null ? "adminSideMenu.jsp" : "teacherSideMenu.jsp");

                req.setAttribute("message",message);
//                req.getRequestDispatcher(SIMPLE_SELECT_PEDAGOGIES_JSP).forward(req,resp);
                req.getRequestDispatcher(CreateClassHandler.SIMPLE_CLASS_CONFIG_JSP).forward(req,resp);
            }
        }
        else if (e instanceof AdminAlterClassPrePostEvent) {
            int classId =  e.getClassId();
            ClassInfo info = DbClass.getClass(conn, classId);
            ClassInfo[] classes1 = DbClass.getClasses(conn, teacherId);
            Classes bean1 = new Classes(classes1);
            req.setAttribute("classInfo",info);
            req.setAttribute("classId", classId) ;
            req.setAttribute("teacherId", teacherId);
            req.setAttribute("bean", bean1) ;
            req.setAttribute("action","AdminAlterClassPrePost");
            Integer adminId = (Integer) req.getSession().getAttribute("adminId"); // determine if this is admin session
            req.setAttribute("sideMenu",adminId != null ? "adminSideMenu.jsp" : "teacherSideMenu.jsp");
            req.getRequestDispatcher(CreateClassHandler.SELECT_PRETEST_POOL_JSP).forward(req,resp);
        }
        else if (e instanceof AdminAlterClassSubmitPrePostEvent) {
            int classId =  e.getClassId();
            req.setAttribute("classId", classId) ;
            req.setAttribute("teacherId", teacherId);
            ClassInfo[] classes1 = DbClass.getClasses(conn, teacherId);
            Classes bean1 = new Classes(classes1);
            Integer adminId = (Integer) req.getSession().getAttribute("adminId"); // determine if this is admin session
            req.setAttribute("sideMenu",adminId != null ? "adminSideMenu.jsp" : "teacherSideMenu.jsp");
            DbClass.setClassConfigShowPostSurvey(conn, classId, ((AdminAlterClassSubmitPrePostEvent) e).showPostSurvey());
            ClassInfo info = DbClass.getClass(conn, classId);
            req.setAttribute("classInfo",info);
            req.setAttribute("action","AdminAlterClassPrePost");
            req.setAttribute("bean", bean1) ;
            String val = info.isShowPostSurvey() ? "ON" : "OFF";
            req.setAttribute("message","The post survey is now " + val);
            req.getRequestDispatcher(CreateClassHandler.SELECT_PRETEST_POOL_JSP).forward(req,resp);
        }


       
    }

    public static void editClassList(Connection conn, int classId, int teacherId, HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        ClassInfo classInfo = DbClass.getClass(conn, classId);
        ClassInfo[] classes1 = DbClass.getClasses(conn, teacherId);
        Classes bean1 = new Classes(classes1);
        Integer adminId = (Integer) req.getSession().getAttribute("adminId"); // determine if this is admin session
        req.setAttribute("sideMenu",adminId != null ? "adminSideMenu.jsp" : "teacherSideMenu.jsp"); // set side menu for admin or teacher

        req.setAttribute("action","AdminEditClassList");
        req.setAttribute("classInfo", classInfo);
        List<User> students = DbClass.getClassStudents(conn, classId);
        req.setAttribute("students",students );
        req.setAttribute("numStudents", students.size());
        req.setAttribute("bean", bean1);
        req.setAttribute("classId", classId);
        CreateClassHandler.setTeacherName(conn,req,teacherId);
        req.setAttribute("teacherId", teacherId);

        req.getRequestDispatcher(ROSTER_JSP).forward(req,resp);
    }


    /**
     * This is called after the pedagogy selections have been validated and the selected pedagogies have been
     * saved as part of this class.   This just generates the next JSP page which is the editClass.jsp
     */
 public void generateValidPedagogySubmissionNextPage(Connection conn, int classId,
                                                     HttpServletRequest req,
                                                     HttpServletResponse resp,
                                                     boolean isSimpleConfig, boolean useTutoringStrategies) throws SQLException, IOException, ServletException, DeveloperException {



     req.setAttribute("formSubmissionEvent","AdminAlterClassSubmitSelectedPedagogies");

     ClassInfo[] classes1 = DbClass.getClasses(conn, teacherId);
     Classes bean1 = new Classes(classes1);
     ClassInfo classInfo = DbClass.getClass(conn,classId);
     Integer adminId = (Integer) req.getSession().getAttribute("adminId"); // determine if this is admin session
     req.setAttribute("sideMenu",adminId != null ? "adminSideMenu.jsp" : "teacherSideMenu.jsp"); // set side menu for admin or teacher

     if (isSimpleConfig)
        req.setAttribute("pedagogies", DbClassPedagogies.getClassSimpleConfigPedagogyBeans(conn,classId));
     else {
         List<TutorStrategy> strats = DbStrategy.getStrategies(conn,classId);
         if (strats.size() == 0)
            req.setAttribute("pedagogies", DbClassPedagogies.getClassPedagogyBeans(conn, classId));
         else
             req.setAttribute("pedagogies", new PedagogyBean[0]);
     }
     req.setAttribute("action", "AdminAlterClassPedagogies");
     req.setAttribute("classId", classId);
     CreateClassHandler.setTeacherName(conn,req,teacherId);
     req.setAttribute("teacherId", teacherId);
     req.setAttribute("classInfo", classInfo);
     req.setAttribute("bean", bean1);
     req.setAttribute("useTutoringStrategies", useTutoringStrategies);
     req.setAttribute("message", "Your changes have been accepted.");
     req.setAttribute("action","AdminAlterClassPedagogies");
     if (isSimpleConfig)
        req.getRequestDispatcher(CreateClassHandler.SIMPLE_SELECT_PEDAGOGIES_JSP).forward(req,resp);
     else
         req.getRequestDispatcher(CreateClassHandler.SELECT_PEDAGOGIES_JSP).forward(req,resp);

    }


}