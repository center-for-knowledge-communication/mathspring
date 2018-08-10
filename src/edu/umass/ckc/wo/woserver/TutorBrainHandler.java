package edu.umass.ckc.wo.woserver;


import edu.umass.ckc.servlet.servbase.ServletEvent;
import edu.umass.ckc.servlet.servbase.UserException;
import edu.umass.ckc.servlet.servbase.View;
import edu.umass.ckc.wo.assistments.AssistmentsHandler;
import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.content.ProblemBinding;
import edu.umass.ckc.wo.content.QuickAuthProb;
import edu.umass.ckc.wo.db.DbProblem;
import edu.umass.ckc.wo.db.DbSession;
import edu.umass.ckc.wo.event.*;
import edu.umass.ckc.wo.event.tutorhut.*;
import edu.umass.ckc.wo.exc.NoSessionException;
import edu.umass.ckc.wo.handler.*;
import edu.umass.ckc.wo.html.tutor.TutorPage;
import edu.umass.ckc.wo.log.CompleteEventDataLogger;
import edu.umass.ckc.wo.log.TutorLogger;
import edu.umass.ckc.wo.login.LandingPage;
import edu.umass.ckc.wo.login.LoginAdult_2;
import edu.umass.ckc.wo.login.LoginK12_2;
import edu.umass.ckc.wo.login.LoginParams;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.state.StudentState;
import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.tutor.response.ErrorResponse;
import edu.umass.ckc.wo.tutor.response.Response;
import edu.umass.ckc.wo.tutormeta.LearningCompanion;
import edu.umass.ckc.wo.woreports.util.EventLogEntryObjects;
import edu.umass.ckc.wo.woreports.util.StudentProblemHistoryObjects;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.RequestDispatcher;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TutorBrainHandler {
    private TutorBrainEventFactory eventFactory;
    private static Logger logger =   Logger.getLogger(TutorBrainHandler.class);
    private ServletInfo servletInfo;




    public  TutorBrainHandler (ServletInfo info)  {
        this.servletInfo = info;
        eventFactory = new TutorBrainEventFactory();
    }




    private String quickQuery (Connection conn) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "select count(*) from administrator";
            stmt = conn.prepareStatement(q);
            rs = stmt.executeQuery();
            int c=0;
            while (rs.next()) {
                c= rs.getInt(1);
            }

            return "num admins: " + c + " db host: " + "dont know";

        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }

    private String slowQuery (Connection conn) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "select count(*) from woproperty p,  eventlog e where  e.studId = p.objid";
            stmt = conn.prepareStatement(q);
            rs = stmt.executeQuery();
            int c=0;
            while (rs.next()) {
                c= rs.getInt(1);
            }

            return "num woproperty: " + c + " db host: " + "dont know";

        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }



    // All servlet requests handled through this method
    public boolean handleRequest() throws Throwable {
        ServletEvent e = eventFactory.buildEvent(servletInfo.params, "tutorhut");
        if (e instanceof TeachTopicEvent)
            return new AssistmentsHandler(servletInfo).teachTopic((TeachTopicEvent) e);
        else if (e instanceof GetProblemDataEvent)
            return new AssistmentsHandler(servletInfo).getProblemData((GetProblemDataEvent) e);
        else if (e instanceof GetEventLogDataEvent){

            SessionManager sessionManager = new SessionManager(servletInfo.getConn(),((SessionEvent) e).getSessionId(), servletInfo.getHostPath(), servletInfo.getContextPath()).buildExistingSession(servletInfo.getParams());
            if(!servletInfo.getParams().getString("type").equals("publishNote")) {
                Map<String, Object> completeEventDataMap = new HashMap<String, Object>();

                List<EventLogEntryObjects> fullEventeventLog = new CompleteEventDataLogger(
                        servletInfo.getConn()).getLatestSnapShotsForEventLog(
                        sessionManager.getStudentId(), sessionManager.getSessionNum());

                List<StudentProblemHistoryObjects> fullstudentProblemHistryLog = new CompleteEventDataLogger(
                        servletInfo.getConn()).loadStudentProblemHistoryforCurrentSession(
                        sessionManager.getStudentId(), sessionManager.getSessionNum());
                completeEventDataMap.put("eventLog", fullEventeventLog);
                completeEventDataMap.put("studentProblemHistory", fullstudentProblemHistryLog);

                ObjectMapper objectMapp = new ObjectMapper();
                objectMapp.setVisibility(JsonMethod.FIELD, JsonAutoDetect.Visibility.ANY);
                String mapperValues = objectMapp.writeValueAsString(completeEventDataMap);

                servletInfo.getResponse().setContentType("application/json");
                servletInfo.getResponse().getOutputStream().print(mapperValues);
            }else{
                String[] formValuesToBePublished = servletInfo.getParams().getString("formData").split("~~");
                boolean publishNote  = new CompleteEventDataLogger(servletInfo.getConn()).publishDeveloperNotes(formValuesToBePublished,sessionManager.getStudentId(), sessionManager.getSessionNum());
                servletInfo.getResponse().setContentType("text/plain");
                servletInfo.getResponse().getOutputStream().print("contentSaved");
            }
            return false;

        }
        else if (e instanceof GetProblemListForTesterEvent) {
            List<Problem> probs = ProblemMgr.getAllProblems();
            RequestDispatcher disp=null;

            String jsp = "selectProblemDialog.jsp";
            disp = servletInfo.getRequest().getRequestDispatcher(jsp);
            servletInfo.getRequest().setAttribute("problems",probs);
            disp.forward(servletInfo.getRequest(),servletInfo.getResponse());
            return false;

        }
        else if (e instanceof GetClassesEvent) {
            servletInfo.getOutput().append(new GetClassHandler().handleRequest(servletInfo.getConn()));
        }
        else {
            SessionManager smgr = null;
            try {
                smgr = new SessionManager(servletInfo.getConn(), ((SessionEvent) e).getSessionId(), servletInfo.getHostPath(), servletInfo.getContextPath()).buildExistingSession(servletInfo.getParams());
            } catch (NoSessionException nse) {
                nse.printStackTrace();
                // This is a common exception and we want to pass back a fatal error to the client so it can let the user know their session is dead.
                View er = new ErrorResponse(nse, true);
                servletInfo.getOutput().append(er.getView());
                return true;

            }

            if (e instanceof TutorHomeEvent) {
                TutorPage tutorPage = new TutorPage(this.servletInfo,smgr);
                tutorPage.handleRequest((TutorHomeEvent) e);
                return false;
            }
            else if (e instanceof DbTestEvent) {
                StudentState state = smgr.getStudentState();
                long begin = System.currentTimeMillis();
                for (int j=0;j<5;j++) {
                    for (int i=0;i<50;i++)
                        state.setProp(smgr.getStudentId(),"Prop"+i,"Val"+i);
                }
                long end = System.currentTimeMillis();
                servletInfo.getOutput().append("<html><body> Executed 250 setProps Time: " + (end - begin) + "ms </body></html>");
            }
            else if (e instanceof AdventurePSolvedEvent)
                servletInfo.getOutput().append(smgr.adventureProblemSolved((AdventurePSolvedEvent) e));
            else if (e instanceof  EnterTutorEvent) {
                EnterTutorEvent ee = (EnterTutorEvent) e;
                smgr.getStudentState().newSession(smgr);
                boolean showMPP = smgr.getPedagogicalModel().isShowMPP();
                smgr.getPedagogicalModel().newSession(smgr.getSessionNum());
                smgr.getStudentModel().save();
                new TutorPage(servletInfo,smgr).createTutorPageFromState(ee.getElapsedTime(), 0, -1, -1,
                        Problem.PRACTICE, Problem.PRACTICE, null, true, null, null, true, showMPP);
                return false;
            }
            else if (e instanceof HomeEvent) {
                if (Settings.useNewGUI()) {
                    new DashboardHandler(
                            this.servletInfo.getServletContext(),
                            smgr,
                            smgr.getConnection(),
                            servletInfo.getRequest(),
                            servletInfo.getResponse()
                    ).showNewSplashPage(LandingPage.JSP_NEW,false);
                } else {
                    new DashboardHandler(
                            this.servletInfo.getServletContext(),
                            smgr,
                            smgr.getConnection(),
                            servletInfo.getRequest(),
                            servletInfo.getResponse()
                    ).showSplashPage(LandingPage.JSP, false);
                }
                new TutorLogger(smgr).logHomeEvent((HomeEvent) e);
                return false;
            }
            else if (e instanceof NavigationEvent) {
                View v = new NavigationHandler(servletInfo.getServletContext(), smgr, servletInfo.getConn(), servletInfo.getRequest(), servletInfo.getResponse()).handleRequest((NavigationEvent) e);
                if (v == null)
                    return false;  // if a jsp is used we don't want to have the caller write to output
                else servletInfo.getOutput().append(v.getView());
            }
            // DM 2/09 These are new events that are based on characters or various drawing tools

            // system 1 has the CharacterHandler process this event.   System 2 uses the learning companion within the pedagogy
            // to process it (it gets passed to the TutorHutHandler below)

            else if (e instanceof GetTutorHutQuestionAnswerEvent) {
                Problem p = new DbProblem().getProblem(servletInfo.getConn(),smgr.getStudentState().getCurProblem());
                servletInfo.getOutput().append("&correctAnswer="+ p.getAnswer());
            }
            else if (e instanceof SplashPageEvent)
                new MyProgressHandler(servletInfo.getServletContext(),smgr,smgr.getConnection(),servletInfo.getRequest(),servletInfo.getResponse()).handleRequest((SplashPageEvent) e);
            else if (e instanceof ShowProgressEvent)
                new MyProgressHandler(servletInfo.getServletContext(),smgr,smgr.getConnection(),servletInfo.getRequest(),servletInfo.getResponse()).handleRequest((ShowProgressEvent) e);
            // This event handling was in Pedagogical Model but I moved it here since nothing really happens except logging
            else if (e instanceof MPPTopicEvent) {
                // If its the hybrid tutor we want to generate a new TutorPage and plug it with the selected response (a problem?)
                if (Settings.useHybridTutor)  {
                    new MPPTutorHandler(servletInfo, smgr).handleRequest((MPPTopicEvent) e);  // may add stuff to event for logging

                    return false;  // indicates to caller we forwarded to JSP
                }
                else servletInfo.getOutput().append(new Response().getView()); // old system just wants ack=true
            }
            else if (e instanceof TopicDetailEvent || e instanceof SaveCommentEvent) {
                SessionEvent ee = (SessionEvent) e;
                new MyProgressHandler(servletInfo.getServletContext(),smgr,servletInfo.getConn(),servletInfo.getRequest(),servletInfo.getResponse()).handleRequest(ee);
                return false;
            }
            else if (e instanceof GetQuickAuthProblemSkeletonEvent) {
                RequestDispatcher disp=null;
                Problem p = ProblemMgr.getProblem(((GetQuickAuthProblemSkeletonEvent) e).getProbId());
                String quickAuthJSP = "quickAuthProblem.jsp";
                disp = servletInfo.getRequest().getRequestDispatcher(quickAuthJSP);
                servletInfo.getRequest().setAttribute("problem",p);
                servletInfo.getRequest().setAttribute("sessionId",smgr.getSessionNum());
                servletInfo.getRequest().setAttribute("eventCounter",smgr.getEventCounter());
                servletInfo.getRequest().setAttribute("elapsedTime",((GetQuickAuthProblemSkeletonEvent) e).getElapsedTime());
                String servContext= servletInfo.getRequest().getContextPath();
                if (servContext != null && servContext.length()>1)
                    servContext=servContext.substring(1);    // strip off the leading /

                servletInfo.getRequest().setAttribute("servletContext",servContext);
                servletInfo.getRequest().setAttribute("servletName",servletInfo.getServletName());
                servletInfo.getRequest().setAttribute("previewMode",false);
                servletInfo.getRequest().setAttribute("teacherId",-1);  // for preview mode it looks for a teacher ID
                disp.forward(servletInfo.getRequest(),servletInfo.getResponse());
                logger.info("<< JSP: " + quickAuthJSP);
                return false;
            }
            else if (e instanceof GetQuickAuthProblemEvent) {
                Problem p = ProblemMgr.getProblem(((GetQuickAuthProblemEvent) e).getProbId());
                ProblemBinding b = new ProblemBinding(p);
                b.setBindings(smgr);
                QuickAuthProb qp = new QuickAuthProb(p,Settings.problemContentPath,b);
                JSONObject jo = new JSONObject();
                qp.buildJSON(jo);
                final String json = jo.toString();
                View v = new View() {
                    public String getView() throws Exception {
                        return json;

                    }
                };
                servletInfo.getOutput().append(v.getView());
                return true;
            }

            // N.B.  The above new events have to come before this one because they all inherit from
            // TutorHutEvent and we don't want TutorHutEventHandler processing their events
            else if (e instanceof TutorHutEvent) {
                ((TutorHutEvent) e).setServletResponse(servletInfo.getResponse());  // This is because processing might result in forward to a JSP
                ((TutorHutEvent) e).setServletRequest(servletInfo.getRequest());  // This is because processing might result in forward to a JSP
                View v = new TutorHutEventHandler(smgr).handleRequest((TutorHutEvent) e);
                // if we get a null View, this means we forwarded to a JSP so we return false so the server doesn't flush output
                if (v == null)
                    return false;
                servletInfo.getOutput().append(v.getView());
            }

            // these are parts of the system (e.g. pretests that still rely on StudentActionEvents or EndActivityEvents)

            else if (e instanceof EndActivityEvent) {
                View v = new StudentActionHandler(smgr,servletInfo.getConn()).handleRequest((EndActivityEvent) e);
                servletInfo.getOutput().append(v.getView());
            }

            else if (e instanceof StudentActionEvent) {
                smgr.getStudentState().setProbElapsedTime(((StudentActionEvent) e).getProbElapsed()) ;
                View v = new StudentActionHandler(smgr,servletInfo.getConn()).handleRequest((StudentActionEvent) e);
                servletInfo.getOutput().append(v.getView());
            }
            else if (e instanceof KillSessionsEvent) {
                // if user wishes not to login and kill other sessions, killAll will be true.  We then
                // kill the temporary session that was set up to allow the this transaction
                if (((KillSessionsEvent) e).isKillAll()) {
                    smgr.inactivateTempUserSessions(((KillSessionsEvent) e).getSessionId());
                    servletInfo.getOutput().append(smgr.getLoginView(null, null, null,-1,-1, null));
                }
                // the user wishes to force login and kill other sessions beside this one.
                else {
                    smgr.inactivateUserSessions();
                    LearningCompanion lc = null;
                    lc = smgr.getPedagogicalModel().getLearningCompanion();
                    servletInfo.getOutput().append(smgr.getLoginView(SessionManager.LOGIN_USER_PASS,NavigationHandler.TRUE,null,
                            smgr.getSessionNum(),smgr.getStudentId(), lc));
                }
                // killsessions is similar to login except it first kills other sessions
                // It then returns acknowledgement + sessionNumber

            }

            else if (e instanceof LogoutEvent) {
                String ipAddr = servletInfo.getRequest().getRemoteAddr();
                servletInfo.getOutput().append(smgr.logoutStudent((LogoutEvent) e, ipAddr));
                smgr.getStudentState().clearTutorHutState();
                String clientType = DbSession.getClientType(servletInfo.getConn(), smgr.getSessionNum());
                RequestDispatcher disp=null;
                if (clientType == null)
                    clientType = LoginParams.K12;
                String loginJSP = clientType.equals(LoginParams.ADULT)
                        ? LoginAdult_2.LOGIN_JSP
                       // : Settings.useNewGUI()
                           // :LoginK12_2.LOGIN_JSP_NEW
                           : LoginK12_2.LOGIN_JSP;
                servletInfo.request.setAttribute("var", e.getServletParams().getString("var"));
                if (clientType.equals(LoginParams.ADULT) )
                    servletInfo.request.setAttribute("startPage","LoginAdult_1");
                else
                    servletInfo.request.setAttribute("startPage","LoginK12_1");

                disp = servletInfo.getRequest().getRequestDispatcher(loginJSP);
                disp.forward(servletInfo.getRequest(),servletInfo.getResponse());
                logger.info("<< JSP: " + loginJSP);
                return false;
            } else if (e instanceof SetMFRResultEvent) {
                // want to log a transaction for this event.  Need some new values for that
                // One problem is that the server doesn't have problem Ids for each problem.
                // want to update the MFRScore table
                View v = new MFRHandler().handleEvent(servletInfo.getConn(), (SetMFRResultEvent) e, smgr.getStudentId(), smgr.getSessionNum());
                servletInfo.getOutput().append(v.getView());
            } else if (e instanceof SetMRResultEvent) {
                View v = new MRHandler().handleEvent(servletInfo.getConn(), (SetMRResultEvent) e, smgr.getStudentId(), smgr.getSessionNum());
                servletInfo.getOutput().append(v.getView());
            } else if (e instanceof SetPrePostResultEvent) {
                View v = new PrePostHandler().handleEvent(servletInfo.getConn(), (SetPrePostResultEvent) e, smgr.getStudentId(), smgr.getSessionNum());
                servletInfo.getOutput().append(v.getView());
            }

            else if (e instanceof CleanOutSystemTestDataEvent) {
                smgr.removeTestSessionData();
                servletInfo.getOutput().append("All data removed for session data removed.");
            }

            else
                throw new UserException("Unknown Event");
        }
        return true;
    }



    public String getTopicStartAnchor(String client, int sessId,String lc, int topic ) {
        String url = Settings.flashClientPath + client;

        String args = "?sessnum="+sessId+"&learningHutChoice=true&elapsedTime=0&mode=teachStandard" + ((lc !=null) ? ("&learningCompanion="+lc) : "") +"&topicId="+topic; //"&problemIdString='+problemId;
        System.out.println("URL TO call flash is " + (url+args));
        return "<a href=\"" +(url+args)+ "\">here</a>";
    }
}
