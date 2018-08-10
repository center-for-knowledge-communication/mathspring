package edu.umass.ckc.wo.login.interv;

import ckc.servlet.servbase.ServletParams;
import edu.umass.ckc.wo.beans.Topic;
import edu.umass.ckc.wo.content.Hint;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.content.TopicIntro;
import edu.umass.ckc.wo.event.SessionEvent;
import edu.umass.ckc.wo.login.LoginSequence;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.tutor.intervSel2.InterventionSelector;
import edu.umass.ckc.wo.tutor.pedModel.PedagogicalModel;
import edu.umass.ckc.wo.tutormeta.Intervention;
import edu.umass.ckc.wo.tutormeta.PedagogicalMoveListener;
import edu.umass.ckc.wo.util.State;
import edu.umass.ckc.wo.util.WoProps;
import edu.umass.ckc.wo.woserver.ServletInfo;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 4/14/15
 * Time: 4:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class LoginInterventionSelector extends InterventionSelector implements PedagogicalMoveListener {
    public static final String INTERVENTION_CLASS = "interventionClass";
    protected HttpServletRequest request;
    protected ServletInfo servletInfo;
    protected MyState interventionState;
    protected boolean runOnce;

    public LoginInterventionSelector( SessionManager smgr) throws SQLException {
        super(smgr);
        interventionState = new MyState(smgr);
    }

    public void setServletInfo(ServletInfo servletInfo) {
        this.servletInfo=servletInfo;
    }

    public LoginIntervention processInput (ServletParams params) throws Exception {
        return null;
    }

    public Intervention selectIntervention (SessionEvent e) throws Exception {
        rememberInterventionSelector(this);
        interventionState.setTimeOfLastIntervention(System.currentTimeMillis());
        HttpServletRequest req = this.servletInfo.getRequest();
        req.setAttribute(LoginSequence.SKIN,servletInfo.getParams().getString("skin"));
        req.setAttribute(INTERVENTION_CLASS,getClass().getName());
        req.setAttribute(LoginSequence.SESSION_ID,e.getSessionId());
        return null;
    }

    public void clearState () throws SQLException {
        this.interventionState.clearState();
    }


    protected Class getInterventionClass () {
        return this.getClass();
    }

    @Override
    public void problemGiven(Problem p) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void exampleGiven(Problem ex) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void lessonIntroGiven(TopicIntro intro) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void attemptGraded(boolean isCorrect) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void hintGiven(Hint hint) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void interventionGiven(Intervention intervention) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void newTopic(Topic t) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void newSession(int sessId) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void init(SessionManager smgr, PedagogicalModel pedagogicalModel) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    protected class MyState extends State {
        private final String TIME_OF_LAST_INTERVENTION =  LoginInterventionSelector.this.getInterventionClass().getSimpleName() + ".TimeOfLastIntervention";
        long timeOfLastIntervention;

        MyState (SessionManager smgr) throws SQLException {

            this.conn=smgr.getConnection();
            this.objid = smgr.getStudentId();
            WoProps props = smgr.getStudentProperties();
            Map m = props.getMap();
            timeOfLastIntervention =  mapGetPropLong(m,TIME_OF_LAST_INTERVENTION,0);
//            if (timeOfLastIntervention ==0)
//                setTimeOfLastIntervention(System.currentTimeMillis());

        }


        protected long getTimeOfLastIntervention() {
            return timeOfLastIntervention;
        }

        protected void setTimeOfLastIntervention(long timeOfLastIntervention) throws SQLException {
            this.timeOfLastIntervention = timeOfLastIntervention;
            setProp(this.objid,TIME_OF_LAST_INTERVENTION,timeOfLastIntervention);
        }

        protected void clearState () throws SQLException {
            this.clearProp(this.objid,TIME_OF_LAST_INTERVENTION);
        }


    }
}
