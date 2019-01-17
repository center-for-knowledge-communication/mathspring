package edu.umass.ckc.wo.tutor.intervSel2;

import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.db.DbClass;
import edu.umass.ckc.wo.db.DbHint;
import edu.umass.ckc.wo.db.DbTopics;
import edu.umass.ckc.wo.event.tutorhut.InputResponseNextProblemInterventionEvent;
import edu.umass.ckc.wo.event.tutorhut.NextProblemEvent;
import edu.umass.ckc.wo.exc.DeveloperException;
import edu.umass.ckc.wo.interventions.DemoProblemIntervention;
import edu.umass.ckc.wo.interventions.NextProblemIntervention;
import edu.umass.ckc.wo.event.tutorhut.InterventionTimeoutEvent;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.state.StudentState;
import edu.umass.ckc.wo.tutor.model.TopicModel;
import edu.umass.ckc.wo.tutor.model.TutorModelUtils;
import edu.umass.ckc.wo.tutor.pedModel.PedagogicalModel;
import edu.umass.ckc.wo.tutor.probSel.PedagogicalModelParameters;
import edu.umass.ckc.wo.tutor.probSel.TopicModelParameters;
import edu.umass.ckc.wo.tutor.response.Response;
import edu.umass.ckc.wo.tutormeta.Intervention;
import edu.umass.ckc.wo.tutormeta.TopicSelector;
import org.jdom.Element;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 3/20/15
 * Time: 11:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class DemoProblemIS extends NextProblemInterventionSelector {
    TopicModel topicModel;
    TopicModelParameters.frequency freq;
    TopicSelector topicSelector;

    public DemoProblemIS(SessionManager smgr) {
        super(smgr);

    }

    @Override
    public void init(SessionManager smgr, PedagogicalModel pedagogicalModel) {
        this.pedagogicalModel=pedagogicalModel;
        topicModel = (TopicModel) pedagogicalModel.getLessonModel();
        topicSelector= topicModel.getTopicSelector();
        configure();
    }

    // The intervention must be defined with <demoFrequency> in the config, or a <param name="demoFrequency" value="always">
    // OR in the intervention param object if using tutor strategies.
    // Valid values for this are: never, oncePerSession, always.  If not provided, always will be used.
    private void configure () {

        String freqstr = getConfigParameter2("demoFrequency");
        if (freqstr == null)
            this.freq = TopicModelParameters.frequency.always;
        else
            this.freq = TopicModelParameters.convertExampleFrequency(freqstr);

    }

    @Override
    // Demo Problems are returned as Problem objects to the client.  This means that it won't send back any events when the
    // demo problem is done.
    public NextProblemIntervention selectIntervention(NextProblemEvent e) throws Exception {

        if (!smgr.getStudentState().isExampleShown()) {
            Problem demo = getTopicDemoProblem(studentState.getCurTopic());
            if (demo == null)
                return null;
            DemoProblemIntervention dpi = new DemoProblemIntervention(demo);
            smgr.getStudentState().setIsExampleShown(true);
            return dpi;
        }
        return null;
    }

    public Problem getTopicDemoProblem (int curTopic) throws Exception {
        Problem problem = null;
        StudentState state = smgr.getStudentState();
        // The classConfig table can have settings that control the topic-demo aspect of the pedagogy.
        // These settings should override the default settings that are in the lesson config part of the XML.

        int classId = smgr.getClassID();
        TopicModelParameters classParams = (TopicModelParameters) DbClass.getLessonModelParameters(conn, classId);
        // found classConfig parameters that control behavior of this lesson, use the frequency for the demo problem
        TopicModelParameters.frequency classFreq=null;
        if (classParams != null)
            classFreq = classParams.getTopicExampleFrequency();
        if (classFreq != null)
            this.freq = classFreq;
        // overload the defaults with stuff defined for the class.

//       if (this.pedagogicalModel instanceof ConfigurablePedagogy) {
        // these params are the ones that were passed in by Assistments and saved for the user


        if (!smgr.getStudentState().isExampleShown()) {
            if (freq == TopicModelParameters.frequency.always) {
                if (!smgr.getStudentState().isExampleSeen(curTopic))
                    smgr.getStudentState().addExampleSeen(curTopic);
                // see if there is a problem that is set as the demoProblem for the topic and use it if not -1
                int bestDemoProbId = DbTopics.getTopicDemoProblem(smgr.getConnection(),curTopic);
                problem = bestDemoProbId != -1 ? ProblemMgr.getProblem(bestDemoProbId): null;
                if (problem == null || !problem.isUsableAsExample())
                    // o/w grab a problem from within the topic and use it as a demo
                    problem = topicSelector.getDemoProblem(curTopic);

                if (problem == null)
                    return null;
                //  gets the solution to the problem from the hint selector and adds into the problem
                DbHint.updateHintsForDemoProblems(conn, problem);
                new TutorModelUtils().setupDemoProblem(problem,smgr,topicModel.getHintSelector());
                return problem;
            }
            else if (freq == TopicModelParameters.frequency.oncePerSession &&
                    !smgr.getStudentState().isExampleSeen(curTopic)) {
                smgr.getStudentState().addExampleSeen(curTopic);
                int bestDemoProbId = DbTopics.getTopicDemoProblem(smgr.getConnection(),curTopic);
                problem = bestDemoProbId != -1 ? ProblemMgr.getProblem(bestDemoProbId) : null;
                if (problem == null || !problem.isUsableAsExample())
                    problem = topicSelector.getDemoProblem(curTopic);
                else problem = ProblemMgr.getProblem(bestDemoProbId);

                if (problem == null) return null;
                //  gets the solution to the problem from the hint selector and adds into the problem
                DbHint.updateHintsForDemoProblems(conn, problem);
                new TutorModelUtils().setupDemoProblem(problem,smgr,topicModel.getHintSelector());
                return problem;
            }
        }
        return null;
    }

    @Override
    public Intervention processInterventionTimeoutEvent(InterventionTimeoutEvent e) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response processInputResponseNextProblemInterventionEvent(InputResponseNextProblemInterventionEvent e) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
