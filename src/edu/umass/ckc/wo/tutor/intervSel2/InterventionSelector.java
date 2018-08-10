package edu.umass.ckc.wo.tutor.intervSel2;

import edu.umass.ckc.wo.beans.Topic;
import edu.umass.ckc.wo.content.Hint;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.content.TopicIntro;
import edu.umass.ckc.wo.event.SessionEvent;
import edu.umass.ckc.wo.event.tutorhut.AttemptEvent;
import edu.umass.ckc.wo.event.tutorhut.InputResponseEvent;
import edu.umass.ckc.wo.event.tutorhut.NextProblemEvent;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.state.StudentState;
import edu.umass.ckc.wo.strat.ClassSCInterventionSelector;
import edu.umass.ckc.wo.tutor.pedModel.PedagogicalModel;
import edu.umass.ckc.wo.tutormeta.Intervention;
import edu.umass.ckc.wo.tutormeta.PedagogicalMoveListener;
import edu.umass.ckc.wo.tutormeta.StudentModel;
import org.jdom.Element;

import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 11/14/13
 * Time: 12:11 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class InterventionSelector implements PedagogicalMoveListener {

    protected SessionManager smgr;
    protected Connection conn;
    protected StudentState studentState;
    protected StudentModel studentModel;
    protected PedagogicalModel pedagogicalModel;

    protected List<InterventionSelectorParam> params;
    protected Element configXML;

    protected String userInputXML;


    public InterventionSelector(SessionManager smgr) {
//        setServletInfo(smgr, pedagogicalModel);
        this.smgr = smgr;
        this.conn = smgr.getConnection();
        this.studentState = smgr.getStudentState();
        this.studentModel = smgr.getStudentModel();
    }

    public InterventionSelector (SessionManager smgr, ClassSCInterventionSelector isel) {
        this(smgr);
        this.params = (List<InterventionSelectorParam>) isel.getParams();
    }

    public Intervention selectIntervention(SessionEvent e) throws Exception {
        if (this instanceof NextProblemInterventionSelector)          {
            //  e could be a continueNextProblemInterventionEvent or InputResponseNextProb...
            if (! (e instanceof NextProblemEvent)) {
                e = new NextProblemEvent(e.getServletParams());
                e.setSessionId(e.getSessionId());
            }
            return ((NextProblemInterventionSelector) this).selectIntervention((NextProblemEvent) e);
        }
        else if (this instanceof AttemptInterventionSelector)
            return ((AttemptInterventionSelector) this).selectIntervention((AttemptEvent) e);
        else return null;
    }

    /**
     * The setServletInfo method of the InterventionSelector is called at a time later than the constructor.  This is necessary because
     * some of the objects necessary to the InterventionSelector are not available at the time of its construction (e.g. the PedagogicalModel).
     * So we wait until just before we really need the InterventionSelector and then call its setServletInfo method passing it the stuff it has to have at that point
     *
     * @param smgr
     * @param pedagogicalModel
     */
    public abstract void init(SessionManager smgr, PedagogicalModel pedagogicalModel) throws Exception;

    /**
     * Returns a JDOM XML Element that is the <config> ... </config> for the intervention selector
     * @return
     */
    public Element getConfigXML() {
        return configXML;
    }

    public void setConfigXML(Element configXML) {
        this.configXML = configXML;
    }


    public void setParams(List<InterventionSelectorParam> params) {
        this.params = params;
    }

    public List<InterventionSelectorParam> getParams() {
        return params;
    }

    /**
     * Each intervention selector must have a unique name (a string) that the tutoring engine can use
     * as a way of keeping state.  It must know which intervention selector produced the last intervention
     * so that when a response comes back from the student it can then forward this response to the
     * correct intervention selector.   The name of the class is sufficient to uniquely identify it.
     *
     * @return
     */
    public String getUniqueName() {
        return this.getClass().getName();
    }

    public void rememberInterventionSelector (InterventionSelector is) throws SQLException {
        smgr.getStudentState().setLastIntervention(is.getUniqueName());

    }

    public InterventionSelector getInterventionSelectorThatGeneratedIntervention () throws Exception {
        String classname = smgr.getStudentState().getLastIntervention();
        Class c = Class.forName(classname);
        Constructor constructor = c.getConstructor(SessionManager.class);
        InterventionSelector is =(InterventionSelector) constructor.newInstance(smgr);
        is.init(smgr,pedagogicalModel);
        return is;
    }

    protected String getParameter (String name, List<InterventionSelectorParam> params) {
        if (params == null)
            return null;
        for (InterventionSelectorParam param: params) {
            if (param.getName().equals(name))
                return param.getValue();
        }
        return null;

    }


    protected String getConfigParameter (String name) {
        Element x = configXML.getChild(name);
        if (x != null)
            return x.getTextTrim();
        else return null;
    }

    /**
     * Either get the param from within the config XML (if this intervention is being created from the XML living in
     * the database for defining Pedagogy, Lesson, or Login) or get it from the interventionSelectorParams that
     * get built by reading in the TUtorStrategy (or if the XML in the db uses <param name="x" value="y"> instead of the config XML)
     *
     * Because config still exists even in the TutorStrategy (because some things cannot be defined with simple key/value pairs) we
     * check it first.
     * @param name
     * @return
     */
    protected String getConfigParameter2 (String name) {
        // First check to see if there are is params and get the value of the param from there
        if (this.params != null) {
            for (InterventionSelectorParam p : this.params)
                if (p.getName().equalsIgnoreCase(name))
                    return p.getValue();
            return null;
        }
        // note an intervention selector operating under tutoring strategies may have config XML (e.g. AskEmotionIS) so
        // we only go into the configXML looking for paramters if there aren't any is-params (which is used to indicate that
        // we are using Pedagogy's rather than tutor strategies)
        else if (configXML != null) {
            Element x = configXML.getChild(name);
            if (x != null)
                return x.getTextTrim();
            return null;
        }

        else return null;
    }

    protected List<String> getParameters (String name, List<InterventionSelectorParam> params) {
        List<String> results = new ArrayList<String>();
        for (InterventionSelectorParam param: params) {
            if (param.getName().equals(name))
                results.add(param.getValue());
        }
        return results;

    }

    protected void setUserInput(InterventionSelector cl, String userInputXML, InputResponseEvent e) {
        String clname = cl.getClass().getSimpleName();
        String ui = String.format("<interventionInput class=\"%s\">%s</interventionInput>",clname,userInputXML);
        this.userInputXML = ui;
    }

    // PedagogicalMoveListener Methods

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
    public void hintGiven( Hint hint) throws SQLException {
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



}
