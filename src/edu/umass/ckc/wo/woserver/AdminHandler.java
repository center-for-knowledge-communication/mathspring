package edu.umass.ckc.wo.woserver;


import edu.umass.ckc.wo.beans.ClassInfo;
import edu.umass.ckc.wo.beans.Classes;
import edu.umass.ckc.wo.beans.Teacher;
import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.content.ProblemBinding;
import edu.umass.ckc.wo.content.QuickAuthProb;
import edu.umass.ckc.wo.db.DbAdmin;
import edu.umass.ckc.wo.db.DbClass;
import edu.umass.ckc.wo.db.DbTeacher;
import edu.umass.ckc.wo.event.admin.*;
import edu.umass.ckc.servlet.servbase.UserException;
import edu.umass.ckc.wo.handler.*;
import edu.umass.ckc.wo.html.admin.Variables;
import edu.umass.ckc.servlet.servbase.ServletEvent;
import edu.umass.ckc.servlet.servbase.View;
import edu.umass.ckc.wo.content.PrePostProblemDefn;
import edu.umass.ckc.wo.db.DbPrePost;
import edu.umass.ckc.wo.strat.StrategyCache;
import edu.umass.ckc.wo.tutor.Settings;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;


public class AdminHandler {
    public static final Logger logger = Logger.getLogger(AdminHandler.class);
    public static final String USERID = "userId";
    public static final String CLASSID = "classId";

    public AdminHandler() {
    }

    private HttpSession getSession (HttpServletRequest r) {
        HttpSession s = r.getSession();
        if (s.isNew())
            s.setMaxInactiveInterval(-1);  // make it persist indefinitely
        return s;
    }

    // todo Class creation needs to create a user/login for the class so that report access requires
    // the user/password
    /**
     *
     * @param servletRequest
     * @param servletResponse
     * @param sc
     * @param conn
     * @param e                                                      H
     * @param servletOutput
     * @return  true if the calling servlet is going to get some stuff to write into its PrintStream. false if
     * this forwards the request on to JSP
     * @throws Exception
     */
    public boolean handleEvent(HttpServletRequest servletRequest, HttpServletResponse servletResponse, ServletContext sc,
                               Connection conn, ServletEvent e, StringBuffer servletOutput) throws Exception {
        logger.info("AdminHandler.handleEvent " + e.getClass().getName());
        View v = null;
        Variables vv = new Variables(servletRequest.getServerName(),
                servletRequest.getServletPath(),
                servletRequest.getServerPort());
        if (e instanceof AdminLoginEvent) {
            if (Settings.useAdminServletSession) {
                HttpSession sess = servletRequest.getSession();
                sess.setMaxInactiveInterval(Settings.adminServletSessionTimeoutSeconds); // allow 60 minutes of inactive time before session expires
            }
            new AdminToolLoginHandler().handleEvent(conn, vv, (AdminLoginEvent) e, servletRequest, servletResponse);
            return false;  // request forwarded to JSP, so tell caller not to write servlet output
        }
        else if (e instanceof AdminTeacherLoginEvent) {
            new AdminToolLoginHandler().handleEvent(conn, vv, (AdminTeacherLoginEvent) e, servletRequest, servletResponse);
            return false;  // request forwarded to JSP, so tell caller not to write servlet output
        }
        // an admin can set a teacher id to narrow the classes they work with
        else if (e instanceof AdminSetTeacherEvent) {
            HttpSession sess = servletRequest.getSession();
            sess.setAttribute("teacherId",((AdminSetTeacherEvent) e).getTeacherId());
            Integer adminId = (Integer) sess.getAttribute("adminId");
            Teacher t = DbTeacher.getTeacher(conn,((AdminSetTeacherEvent) e).getTeacherId());
            Teacher a = DbAdmin.getAdmin(conn, adminId);
            AdminToolLoginHandler.showAdminMain(conn,servletRequest,servletResponse,a,t, -1);
            return false;
        }
        else if(e instanceof AdminEditProblemFormatEvent) {
            EditProblemFormatHandler.handleEvent((AdminEditProblemFormatEvent) e, sc, conn, servletRequest, servletResponse);
            return false;
        }
        else if (e instanceof AdminGetQuickAuthSkeletonEvent) {
            AdminGetQuickAuthSkeletonEvent ee = (AdminGetQuickAuthSkeletonEvent) e;
            RequestDispatcher disp=null;
            if(ee.getServletParams().getBoolean("reload", false)) {
                ProblemMgr.reloadProblem(conn, ee.getProbId());
            }
            Problem p = ProblemMgr.getProblem(ee.getProbId());
            String quickAuthJSP = "quickAuthProblem.jsp";
            disp = servletRequest.getRequestDispatcher(quickAuthJSP);
            servletRequest.setAttribute("problem",p);
            servletRequest.setAttribute("preview",true);
            servletRequest.setAttribute("sessionId",-1);
            servletRequest.setAttribute("eventCounter", -1);
            servletRequest.setAttribute("elapsedTime",-1);
            servletRequest.setAttribute("previewMode",true);
            servletRequest.setAttribute("teacherId",ee.getTeacherId());
            //Allows scaling the preview
            servletRequest.setAttribute("zoom", ee.getServletParams().getDouble("zoom", 1.0));
            String servContext= servletRequest.getContextPath();
            if (servContext != null && servContext.length()>1)
                servContext=servContext.substring(1);    // strip off the leading /

            servletRequest.setAttribute("servletContext", servContext);
//            servletRequest.setAttribute("servletName",servletInfo.getServletName());
            disp.forward(servletRequest,servletResponse);
            logger.info("<< JSP: " + quickAuthJSP);
            return false;
        }
        else if (e instanceof AdminFlushStrategyCacheEvent) {
            StrategyCache.getInstance().flush();
        }
        else if (e instanceof AdminFlushSingleStrategyFromCacheEvent) {
            StrategyCache.getInstance().flushStrategy(((AdminFlushSingleStrategyFromCacheEvent) e).getStrategyId());
        }
        if (Settings.useAdminServletSession && !(e instanceof UserRegistrationEvent)) {
            HttpSession sess = servletRequest.getSession(false);
            if (sess == null) {
                servletRequest.setAttribute("message","Your session has expired.   You must relogin");
                servletRequest.getRequestDispatcher(Settings.useNewGUI()
                            ? "/login/loginK12_new.jsp"
                            : "/teacherTools/teacherLogin.jsp").forward(servletRequest,servletResponse);
                return false;
            }
        }
        if (e instanceof AdminTeacherRegistrationEvent) {
            new TeacherRegistrationHandler().handleEvent(conn, (AdminTeacherRegistrationEvent) e, servletRequest, servletResponse);
            return false; // forward to JSP , tell caller not to generate output
        }
        if (e instanceof AdminTeacherEditEvent) {
            new TeacherEditHandler().handleEvent(conn, (AdminTeacherEditEvent) e, servletRequest, servletResponse);
            return false; // forward to JSP , tell caller not to generate output
        }
        else if (e instanceof AdminChooseActivityEvent) {
            AdminChooseActivityEvent ee = (AdminChooseActivityEvent) e;
            int teacherId = ee.getTeacherId();
            servletRequest.setAttribute("teacherId",teacherId);
            CreateClassHandler.setTeacherName(conn,servletRequest, ee.getTeacherId());
            servletRequest.getRequestDispatcher("/teacherTools/teacherActivities.jsp").forward(servletRequest,servletResponse);
            return false; // tell caller not to generate output
        }
        else if (e instanceof AdminCreateClassEvent) {
            v = new CreateClassHandler().handleEvent(sc,conn,e, servletRequest, servletResponse);
            if (v == null)    // v is null when JSP is generated
                return false; // tell caller not to generate output
        }
        else if (e instanceof AdminAlterStudentInClassEvent) {
            new AlterStudentInClassHandler().handleEvent(sc,conn, (AdminAlterStudentInClassEvent) e, servletRequest,servletResponse);
            return false; // Tells caller not to write servletOutput into servlet output stream
        }
        else if (e instanceof AdminEditTopicsEvent) {
            v = new TopicEditorHandler().handleEvent(sc,conn,(AdminEditTopicsEvent) e,servletRequest,servletResponse);
            if (v == null)
                return false;
        }
        else if (e instanceof AdminProblemSelectionEvent) {
            v = new ProblemSelectionHandler().handleEvent(sc,conn,(AdminProblemSelectionEvent) e,servletRequest,servletResponse);
            if (v == null)
                return false;
        }
        else if (e instanceof AdminGetQuickAuthProblemEvent) {
            Problem p = ProblemMgr.getProblem(((AdminGetQuickAuthProblemEvent) e).getProbId());
            ProblemBinding b = new ProblemBinding(p);
//            b.setBindings(smgr);
            QuickAuthProb qp = new QuickAuthProb(p,Settings.problemContentPath,b);
            JSONObject jo = new JSONObject();
            qp.buildJSON(jo);
            final String json = jo.toString();
            v = new View() {
                public String getView() throws Exception {
                    return json;

                }
            };
        }
        else if (e instanceof AdminSelectClassEvent) {
            AdminSelectClassEvent ee = (AdminSelectClassEvent) e;
//            servletRequest.getSession().setAttribute(AdminHandler.CLASSID,ee.getClassId());
            servletRequest.setAttribute("classId",ee.getClassId());
            servletRequest.setAttribute("teacherId",ee.getTeacherId());
            CreateClassHandler.setTeacherName(conn,servletRequest, ee.getTeacherId());
            servletRequest.getRequestDispatcher("/teacherTools/selectReport.jsp").forward(servletRequest,servletResponse);
            return false;
        }

        else if (e instanceof AdminViewReportEvent) {
            HttpSession sess = servletRequest.getSession();
            int teacherId = ((AdminViewReportEvent) e).getTeacherId();
            sess.setAttribute("teacherId",teacherId);
            Integer adminId = (Integer) sess.getAttribute("adminId");
            Teacher t = DbTeacher.getTeacher(conn,teacherId);
            Teacher a = null;
            if (adminId != null)
                a = DbAdmin.getAdmin(conn, adminId);

            v =  new ReportHandler(t,a).handleEvent(sc, e, conn, servletRequest,servletResponse);
            // If ReportHandler forward to a JSP we get back a null View, so return false, o/w fall out and return the HTML produced.
            if (v == null)
                return false;

        }
        else if (e instanceof AdminEditSurveysEvent && !((AdminEditSurveysEvent) e).isSaveMode()) {
            servletRequest.setAttribute("teacherId",((AdminEditSurveysEvent) e).getTeacherId());
            CreateClassHandler.setTeacherName(conn, servletRequest, ((AdminEditSurveysEvent) e).getTeacherId());
            servletRequest.setAttribute("preSurvey",Settings.preSurvey);
            servletRequest.setAttribute("postSurvey",Settings.preSurvey);
            servletRequest.getRequestDispatcher(CreateClassHandler.EDIT_SURVEYS_JSP).forward(servletRequest,servletResponse);
            return false;
        }
        else if (e instanceof AdminEditSurveysEvent && ((AdminEditSurveysEvent) e).isSaveMode()) {
            servletRequest.setAttribute("teacherId",((AdminEditSurveysEvent) e).getTeacherId());
            CreateClassHandler.setTeacherName(conn,servletRequest, ((AdminEditSurveysEvent) e).getTeacherId());
            Settings.setSurveys(conn, ((AdminEditSurveysEvent) e).getPreSurvey(),((AdminEditSurveysEvent) e).getPostSurvey());
            Settings.preSurvey= ((AdminEditSurveysEvent) e).getPreSurvey();
            Settings.postSurvey= ((AdminEditSurveysEvent) e).getPostSurvey();
            servletRequest.setAttribute("preSurvey",Settings.preSurvey);
            servletRequest.setAttribute("postSurvey",Settings.postSurvey);
            servletRequest.setAttribute("message","Survey settings saved.");
            servletRequest.getRequestDispatcher(CreateClassHandler.EDIT_SURVEYS_JSP).forward(servletRequest, servletResponse);
            return false;
        }
        // this else class must be after the previous ones because their events are subclasses of AdminClassEvent
        else if (e instanceof AdminClassEvent)  {
            new AlterClassHandler().handleEvent(sc,conn,(AdminClassEvent) e, servletRequest,servletResponse);
            return false; // Tells caller not to write servletOutput into servlet output stream
        }
//        else if (e instanceof AdminTeacherRegistrationEvent)  {
//            new TeacherRegistrationHandler().handleEvent(conn,(AdminTeacherRegistrationEvent) e, servletRequest, servletResponse);
//            return false; // forward to JSP , tell caller not to generate servlet output
//        }
        else if (e instanceof UserRegistrationEvent)   {
            v = new UserRegistrationHandler().handleEvent(sc, servletRequest, conn, e, servletResponse);
            if (v == null)
                return false;
        }


        else if (e instanceof AdminGetPrePostProblemPreviewEvent) {
            int probId =  ((AdminGetPrePostProblemPreviewEvent) e).getProbId();
            PrePostProblemDefn p = DbPrePost.getPrePostProblem(conn,probId);
            int ansType = showProblem(p,servletOutput);
            return true;
        }
        else if (e instanceof AdminMainPageEvent){
            AdminMainPageEvent e1 = (AdminMainPageEvent)((AdminEvent) e);
            ClassInfo[] classes1 = DbClass.getClasses(conn, e1.getTeacherId());
            Classes bean1 = new Classes(classes1);

            Integer adminId = (Integer) servletRequest.getSession().getAttribute("adminId"); // determine if this is admin session
            if (adminId != null) {
                Teacher t = DbTeacher.getTeacher(conn,((AdminEvent) e).getTeacherId());
                Teacher a = DbAdmin.getAdmin(conn,adminId);
                AdminToolLoginHandler.showAdminMain(conn,servletRequest,servletResponse,a,t, -1);
                return false;
            }
            if(classes1.length <= 0){
                servletRequest.getRequestDispatcher("/teacherTools/mainNoClasses.jsp").forward(servletRequest, servletResponse);
                return false;
            }
            ClassInfo classInfo = classes1[classes1.length-1];
            servletRequest.setAttribute("classInfo",classInfo);
            servletRequest.setAttribute("action","AdminUpdateClassId");
            servletRequest.setAttribute("bean", bean1);
            servletRequest.setAttribute("classId", Integer.toString(classInfo.getClassid()));
            servletRequest.setAttribute("teacherId",e1.getTeacherId());
            CreateClassHandler.setTeacherName(conn,servletRequest, e1.getTeacherId());
            if (classes1.length < 1)
                servletRequest.setAttribute("sideMenu", "teacherNoClassSideMenu.jsp");
            else servletRequest.setAttribute("sideMenu", "teacherSideMenu.jsp");
            servletRequest.setAttribute("classInfo", classInfo);
            servletRequest.getRequestDispatcher("/teacherTools/wayangMain.jsp").forward(servletRequest,servletResponse);
            return false;
        }

        else if (e instanceof AdminTestDbEvent) {
            v = new TestDbHandler().handleEvent(sc,conn,servletRequest,servletResponse,servletOutput);
        }
        else if (e instanceof AdminDeactivateLiveProblemEvent) {
            new TutorAdminHandler().processEvent(servletRequest, servletResponse, e, conn);
        }

        else if (e instanceof AdminReloadProblemsEvent || e instanceof AdminTutorEvent) {
            new TutorAdminHandler().processEvent(servletRequest, servletResponse, e, conn);
            return false;
        }
        else if (e instanceof AdminDeleteTeachersEvent || e instanceof AdminDeleteClassesEvent || e instanceof AdminDeleteStudentsEvent
                || e instanceof AdminDeleteClassesSubmitEvent || e instanceof AdminDeleteTeachersSubmitEvent ||
                e instanceof AdminEditTeacherEvent || e instanceof AdminEditTeacherSubmitEvent || e instanceof AdminEditTeacherSetTeacherEvent) {
            new TutorAdminHandler().processEvent(servletRequest, servletResponse, e, conn);
            return false;
        }


        else
            throw new UserException("Unknown event " + e);
        servletOutput.append(v.getView());
        return true; // Tells caller to write servletOutput into servlet output stream
    }

    private int showProblem(PrePostProblemDefn p, StringBuffer out) {
        // I think we can assume that there is a web server on the same machine as the web app?
        String host= Settings.prePostProblemURI;
        String url = host + p.getUrl();
        if (url != null)
            out.append("<img src=\"" + url +"\" /><p/>\n");
        String descr = p.getDescr();
        out.append(descr + "<p/>\n");
        int ansType = p.getAnsType();
        if (ansType == PrePostProblemDefn.MULTIPLE_CHOICE) {
            String ansA,ansB,ansC,ansD,ansE;
            String ansAURL,ansBURL,ansCURL,ansDURL,ansEURL;
            ansA= p.getaAns();
            if (ansA == null) {
                ansAURL =  p.getaURL();
                if (ansAURL != null)
                    out.append("a: <img src=\"" + host +ansAURL + "\"><br/>\n");
            }
            else out.append("a: " + ansA + "<br/>\n");
            ansB= p.getbAns();
            if (ansB == null) {
                ansBURL =  p.getbURL();
                if (ansBURL != null)
                    out.append("b: <img src=\"" + host +ansBURL + "\"><br/>\n");
            }
            else out.append("b: " + ansB + "<br/>\n");
            ansC= p.getcAns();
            if (ansC == null) {
                ansCURL =  p.getcURL();
                if (ansCURL != null)
                    out.append("c: <img src=\"" + host +ansCURL + "\"><br/>\n");
            }
            else out.append("c: " + ansC + "<br/>\n");
            ansD= p.getdAns();
            if (ansD == null) {
                ansDURL =  p.getdURL();
                if (ansDURL != null)
                    out.append("d: <img src=\"" + host + ansDURL + "\"><br/>\n");
            }
            else if (ansD != null) out.append("d: " + ansD + "<br/>\n");
            ansE= p.geteAns();
            if (ansE == null) {
                ansEURL =  p.geteURL();
                if (ansEURL != null)
                    out.append("e: <img src=\"" + host +ansEURL + "\"><br/>\n");
            }
            else if (ansE != null) out.append("e: " + ansE + "<br/>\n");

        }
        return ansType;
    }

}