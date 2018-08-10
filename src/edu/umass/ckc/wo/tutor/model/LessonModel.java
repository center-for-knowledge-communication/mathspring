package edu.umass.ckc.wo.tutor.model;



import edu.umass.ckc.wo.config.LessonXML;
import edu.umass.ckc.wo.db.DbClass;
import edu.umass.ckc.wo.db.DbUserPedagogyParams;
import edu.umass.ckc.wo.event.tutorhut.TutorHutEvent;
import edu.umass.ckc.wo.smgr.SessionManager;

import edu.umass.ckc.wo.state.StudentState;
import edu.umass.ckc.wo.strat.ClassStrategyComponent;
import edu.umass.ckc.wo.strat.SCParam;
import edu.umass.ckc.wo.strat.TutorStrategy;
import edu.umass.ckc.wo.tutor.Pedagogy;
import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.tutor.pedModel.PedagogicalModel;
import edu.umass.ckc.wo.tutor.pedModel.ProblemScore;
import edu.umass.ckc.wo.tutor.probSel.ClassTutorConfigParams;
import edu.umass.ckc.wo.tutor.probSel.LessonModelParameters;
import edu.umass.ckc.wo.tutor.probSel.TopicModelParameters;
import edu.umass.ckc.wo.event.internal.InternalEvent;
import edu.umass.ckc.wo.tutor.response.InterventionResponse;
import edu.umass.ckc.wo.tutor.response.Response;
import edu.umass.ckc.wo.tutormeta.Intervention;
import edu.umass.ckc.wo.tutormeta.PedagogicalMoveListener;
import edu.umass.ckc.wo.tutormeta.PedagogyParams;
import edu.umass.ckc.wo.tutormeta.StudentModel;
import org.apache.log4j.Logger;
import org.jdom.Element;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 2/23/15
 * Time: 3:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class LessonModel implements TutorEventProcessor {
    private static Logger logger = Logger.getLogger(LessonModel.class);

    protected SessionManager smgr;
    protected List<SCParam> scParams;
    protected LessonModelParameters  lmParams;
    protected Element lessonControlXML;
    protected Pedagogy pedagogy;
    protected PedagogicalMoveListener pedagogicalMoveListener;
    protected PedagogicalModel pedagogicalModel;
    protected StudentState studentState;
    protected StudentModel studentModel;
    protected InterventionGroup interventionGroup;


    public LessonModel(SessionManager smgr) {
        this.smgr = smgr;

    }

    public static LessonModel buildModel(SessionManager smgr, TutorStrategy strategy ) throws SQLException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        ClassStrategyComponent lessonSC = strategy.getLesson_sc();
        String lessonModelClassname = lessonSC.getClassName();
        Class cl = Class.forName(lessonModelClassname);
        Constructor constr = cl.getConstructor(new Class[] {SessionManager.class}) ;
        LessonModel lm= (LessonModel) constr.newInstance(smgr);
        lm.setLessonModelParameters(lessonSC);
        return lm;
    }

    /**
     * Called by the PedagogicalModel to build the lesson model (or topic model).  It uses the class name found in the lesson XML coming from the db.
     * It builds an instance of the lesson model and then sets its parameters from values found in the lesson's control element and then overloading
     * based on values that are set up for the class and finally the student.
     * @return
     * @throws SQLException
     */
    public static LessonModel buildModel(SessionManager smgr, Pedagogy ped , int classId, int studId) throws SQLException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        LessonXML lx  = Settings.lessonMap.get(ped.getLessonName().trim());
        String lessonModelClassname = lx.getLessonModelClassname();
        Class cl = Class.forName(lessonModelClassname);
        Constructor constr = cl.getConstructor(new Class[] {SessionManager.class}) ;
        LessonModel lm= (LessonModel) constr.newInstance(smgr);
        lm.setLessonModelParametersForUser(smgr.getConnection(),ped,classId,studId);
        return lm;
    }

    public LessonModelParameters getLmParams() {
        return lmParams;
    }

    /**
     * Given the definition in the LessonModel, extract what's relevant to building a Lesson Model so that this can run
     * based on the definition.  This includes rules that say what should happen on BeginLesson, EndLesson, and BeginTopic
     * and EndTopic (handled in the TopicModel subclass of this which relies on this for some inheritance of model behavior)
     * @param smgr
     *
     */
    public void init (SessionManager smgr, Pedagogy pedagogy,
                      PedagogicalModel pedagogicalModel, PedagogicalMoveListener pedagogicalMoveListener) throws Exception {
        this.smgr = smgr;
        this.pedagogy = pedagogy;
        this.pedagogicalModel = pedagogicalModel;
        this.pedagogicalMoveListener = pedagogicalMoveListener;
        this.studentState = smgr.getStudentState();
        this.studentModel= smgr.getStudentModel();

        if (pedagogy instanceof TutorStrategy) {
            interventionGroup = new InterventionGroup(((TutorStrategy) pedagogy).getLesson_sc().getInterventionSelectors());
        }
        else {
            LessonXML x = Settings.lessonMap.get(this.pedagogy.getLessonName());
            interventionGroup = new InterventionGroup(x.getInterventions());
        }
        interventionGroup.buildInterventions(smgr,pedagogicalModel);
    }

    @Override
    public Response processUserEvent(TutorHutEvent e) throws Exception {
        return null;
    }

    @Override
    /**
     * Gets the interventions that are defined for this model and finds ones that apply to the situation using
     * the onEventName tag of the intervention.   It selects the best candidate using a default algorithm that
     * takes uses the weight of each intervention to put it in order.
     */
    public Response processInternalEvent(InternalEvent e) throws Exception {
        Response r;
        Intervention interv = interventionGroup != null ? interventionGroup.selectIntervention(smgr,e.getSessionEvent(),e.getOnEventName()) : null;
        return buildInterventionResponse(interv);
    }

    public boolean hasReadyContent(int lessonId) throws Exception {
        return true;
    }

    // TODO this method needs renaming and a new signature to make sense for lessons but it is a start in
    // the  right direction for getting this test out of the BasePedMod
    // This method should only be in the TopicModel - removed it 5/3/16
//    public EndOfTopicInfo isEndOfTopic(long probElapsedTime, TopicModel.difficulty difficulty) throws Exception {
//        return null;
//    }



//    protected InterventionSelectorSpec getInterventionSelectorSpec(String lastInterventionClass) {
//        for (InterventionSelectorSpec s : this.interventionGroup.getInterventionsSpecs()) {
//            if (s.getFullyQualifiedClassname().equals(lastInterventionClass))
//                return s;
//        }
//        return null;
//    }

    protected Response buildInterventionResponse (Intervention interv) throws Exception {
        if (interv == null)
            return null;
        else return new InterventionResponse(interv);
    }

    public PedagogicalMoveListener getPedagogicalMoveListener() {
        return pedagogicalMoveListener;
    }

    public difficulty getNextProblemDifficulty (ProblemScore score) {
        // New sessions won't have a previous problem score so we just return same difficulty
        if (score == null || score.isProblemBroken())
            return difficulty.SAME;
        if (!score.isCorrect())
            return difficulty.EASIER;
        if (score.getMistakes() > score.getAvgMistakes()) {
            logger.debug("Decreasing difficulty level... ");
            return difficulty.EASIER;
        }
        else { // mistakes are LOW
            if (score.getHints() > score.getAvgHints()) {
                if (score.getSolveTimeSecs() > score.getAvgSolveTime()) {
//                        logger.debug("Student is carefully seeing help and spending time, maintain level of difficulty (m<E_m, h>E_h, t>E_t)") ;
                    logger.debug("Maintaining same difficulty level... ");
                    return difficulty.SAME;

                } else {
//                        logger.debug("Rushing through hints to get to the correct answer, decrease level of difficulty (m<E_m, h>E_h, t<E_t)") ;
                    logger.debug("Decreasing difficulty level... ");
                    return difficulty.EASIER;

                }
            } else {
                if (score.getSolveTimeSecs() > score.getAvgSolveTime()) {
//                        logger.debug("Correctly working on problem with effort but without help (m<E_m, h<E_h, t>E_t)") ;
//                        logger.debug("50% of the time increase level of difficulty. 50% of the time, maintain level of difficulty") ;
                    if (Math.random() > 0.5) {
                        logger.debug("Increasing difficulty level... ");
                        return difficulty.HARDER;
                    } else {
                        logger.debug("Maintaining same difficulty level... ");
                        return difficulty.SAME;
                    }
                } else
                    ; // logger.debug("Correctly working the problem with no effort and few hints (m<E_m, h<E_h, t<E_t)") ;
                logger.debug("Increasing the difficulty level...");
                return difficulty.HARDER;
            }
        }

    }

    public void setLessonModelParameters (ClassStrategyComponent lessonSC) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        this.scParams = lessonSC.getParams();
        Class cl = getLessonModelParametersClass();
        Constructor constr = cl.getConstructor(new Class[] {List.class});
        // build the parameters object from the control element in the lesson XML
        lmParams = (LessonModelParameters) constr.newInstance(lessonSC.getParams());
    }




    // The lesson parameters define how the lesson operates and are stored in a special object which only the lesson model knows about.
    public void setLessonModelParametersForUser(Connection connection, Pedagogy ped,
                                                                    int classId, int studId) throws SQLException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        String lessonName = ped.getLessonName();
        // first we get the parameters out of the Pedagogy's lesson as defined in the XML in the lessondefinition table
        LessonXML lx =  Settings.lessonMap.get(lessonName);
        // The lesson model tells us the Class to use for its parameters object.
        Class cl = getLessonModelParametersClass();
        Constructor constr = cl.getConstructor(new Class[] {Element.class});
        // build the parameters object from the control element in the lesson XML
        lmParams = (LessonModelParameters) constr.newInstance(lx.getControl());
//        lmParams = lx.getLessonModelParams();

        // If this is a configurable pedagogy (meaning that it can be given some parameters to guide its behavior),  then
        // see if this user has a set of parameters and if so use them to configure the pedagogy.
        // these params come from settings in the WoAdmin tool for the class.
//        LessonModelParameters classParams = DbClass.getClassConfigLessonModelParameters(connection, classId);
        ClassTutorConfigParams classParams = DbClass.getClassConfigTutorParameters(connection, classId);
        // overload the defaults with stuff defined for the class.  Note this will not overload values where
        // the classconfig specified -1 or null values in a parameter
        lmParams.overload(classParams);
//       if (this.pedagogicalModel instanceof ConfigurablePedagogy) {
        // these params are the ones that were passed in by Assistments and saved for the user

        PedagogyParams userParams = DbUserPedagogyParams.getPedagogyParams(connection, studId);
        lmParams.overload(userParams);
        // overload the params with anything provided for the user.
//        defaultParams.overload(userParams);

    }

    public Class getLessonModelParametersClass () {
        return LessonModelParameters.class;
    }

    public enum difficulty {
        EASIER,
        SAME,
        HARDER
    }
}
