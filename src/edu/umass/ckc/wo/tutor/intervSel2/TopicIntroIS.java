package edu.umass.ckc.wo.tutor.intervSel2;

import edu.umass.ckc.wo.content.TopicIntro;
import edu.umass.ckc.wo.db.DbClass;
import edu.umass.ckc.wo.db.DbTopics;
import edu.umass.ckc.wo.event.SessionEvent;
import edu.umass.ckc.wo.event.tutorhut.ContinueNextProblemInterventionEvent;
import edu.umass.ckc.wo.event.tutorhut.InputResponseNextProblemInterventionEvent;
import edu.umass.ckc.wo.event.tutorhut.InterventionTimeoutEvent;
import edu.umass.ckc.wo.event.tutorhut.NextProblemEvent;
import edu.umass.ckc.wo.interventions.NextProblemIntervention;
import edu.umass.ckc.wo.interventions.TopicIntroIntervention;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.tutor.model.TopicModel;
import edu.umass.ckc.wo.tutor.pedModel.PedagogicalModel;
import edu.umass.ckc.wo.tutor.probSel.PedagogicalModelParameters;
import edu.umass.ckc.wo.tutor.probSel.TopicModelParameters;
import edu.umass.ckc.wo.tutor.response.Response;
import edu.umass.ckc.wo.tutormeta.Intervention;
import edu.umass.ckc.wo.tutormeta.PedagogicalMoveListener;
import org.jdom.Element;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 3/20/15
 * Time: 11:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class TopicIntroIS extends NextProblemInterventionSelector {
    TopicModel topicModel;
    TopicModelParameters tmParams;
    TopicModelParameters.frequency freq;
    PedagogicalMoveListener pedMoveListener;

    public TopicIntroIS(SessionManager smgr) {
        super(smgr);

    }

    @Override
    public void init(SessionManager smgr, PedagogicalModel pedagogicalModel) {
//        super.setServletInfo(smgr,pedagogicalModel);
        this.pedagogicalModel=pedagogicalModel;
        topicModel = (TopicModel) pedagogicalModel.getLessonModel();
        tmParams = topicModel.getTmParams();
        pedMoveListener = topicModel.getPedagogicalMoveListener();
        configure();
    }

    // The intervention must be defined with <topicIntroFrequecy> in the config.
    // Valid values for this are: never, oncePerSession, always.  If not provided a default is used
    // as defined in the  TopicModelParameters via PedagogicalModelParameters .
    private void configure () {

        String freqstr = getConfigParameter2("topicIntroFrequency");

        this.freq = PedagogicalModelParameters.convertTopicIntroFrequency(freqstr);

    }

    @Override
    // This makes sure the topic intro hasn't been seen   .
    // The TopicIntro is an InputResponseIntervention.   It doesn't play in a dialog that allows us to get the input normally.
    // Instead we have special-case code on the New-Problem button that checks to see if its a TopicIntro.  If it is, it sends an
    // input response event which then comes bach to this IS and is ignored so that we move on to the next thing.
    public NextProblemIntervention selectIntervention(NextProblemEvent e) throws Exception {
        TopicIntro intro=null;
        // Note additional conditions are checked and set in the TopicModel's TopicSelector class which knows
        // how to find a topic intro for a given topic.
        if  (!studentState.isTopicIntroShown())  {
            // additional conditions checked in method below
            intro = getTopicIntro(studentState.getCurTopic());
            // other conditions (e.g. topic intro already shown for this topic) may result in no intro
            if (intro == null)
                return null;
            // Because the TopicIntro is not played like other interventions (in a dialog with a form that can send inputs such as
            // the destinationIS)  we have to put it in the JSON.  Then we have a hack in the client when the new problem button click
            // is handled it sends back an InputResponse which has a destination obtained from the JSON.
            intro.setInterventionSelector(this.getClass().getName());
            studentState.setTopicIntroShown(true);
        }
        return intro;
    }

//
//    protected TopicIntro getTopicIntro (int curTopic) throws Exception {
//        // all checking of conditions for display of intro is done in getTopicIntro
//        return topicModel.getTopicIntro(curTopic);
//    }

    // TopicIntros being returned depends on the parameters
    public TopicIntro getTopicIntro(int curTopic) throws Exception {

        // The classConfig table can have settings that control the topic-demo aspect of the pedagogy.
        // These settings should override the default settings that are in the lesson config part of the XML.

        int classId = smgr.getClassID();
        TopicModelParameters classParams = (TopicModelParameters) DbClass.getLessonModelParameters(conn, classId);
        // found classConfig parameters that control behavior of this lesson, use the frequency for the demo problem
        TopicModelParameters.frequency classFreq=null;
        if (classParams != null)
            classFreq = classParams.getTopicIntroFrequency();
        if (classFreq != null)
            this.freq = classFreq;

        // if it should always be shown,  show it.
        if (this.freq == TopicModelParameters.frequency.always ) {
            // if it hasn't been seen in this session, store that it has.
            if (!smgr.getStudentState().isTopicIntroSeen(curTopic) )  {
                smgr.getStudentState().addTopicIntrosSeen(curTopic);
            }
            TopicIntro intro = DbTopics.getTopicIntro(smgr.getConnection(), curTopic, classId);
            this.pedMoveListener.lessonIntroGiven(intro); // inform pedagogical move listeners that an intervention is given
            return intro;

        }
        else if (this.freq == TopicModelParameters.frequency.oncePerSession &&
                !smgr.getStudentState().isTopicIntroSeen(curTopic)) {
            smgr.getStudentState().addTopicIntrosSeen(curTopic);
            TopicIntro intro = DbTopics.getTopicIntro(smgr.getConnection(), curTopic, classId);
            this.pedMoveListener.lessonIntroGiven(intro); // inform pedagogical move listeners that an intervention is given
            return intro;
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
