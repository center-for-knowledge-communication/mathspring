package edu.umass.ckc.wo.tutor.pedModel;

import edu.umass.ckc.wo.event.tutorhut.NextProblemEvent;
import edu.umass.ckc.wo.html.tutor.TutorPage;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.tutor.Pedagogy;
import edu.umass.ckc.wo.tutor.pedModel.BasePedagogicalModel;
import edu.umass.ckc.wo.tutor.probSel.*;
import edu.umass.ckc.wo.tutor.response.ProblemResponse;
import edu.umass.ckc.wo.tutor.response.Response;
import edu.umass.ckc.wo.tutormeta.*;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: 6/21/12
 * Time: 4:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class SingleTopicPM extends BasePedagogicalModel {


    public void configure(PedagogyParams pedParams) {

        setParams(
                new PedagogicalModelParameters(pedParams.getMode(), pedParams.isShowIntro(), pedParams.getMaxTime(),
                        pedParams.getMaxProbs(), pedParams.isSingleTopicMode(), pedParams.getCcss()));
        ProblemSelector psel = getProblemSelector();   // searches down into the wrapped PMs to find the prob selector
        psel.setParameters(params);
    }


    public SingleTopicPM() {
    }

    public SingleTopicPM(SessionManager smgr, Pedagogy p) throws Exception {
        super(smgr,p);
//        this.smgr = smgr;
//        this.studentModel = new BaseStudentModel(smgr.getConnection());
//        this.smgr.setStudentModel(this.studentModel);
//        PedagogicalModelParameters params = smgr.getClassPedagogicalModelParameters();
//        reviewModeSelector = new ReviewModeProblemSelector();
//        challengeModeSelector = new ChallengeModeProblemSelector();
//        hintSelector = new PercentageHintSelector();
//        problemSelector = new SingleTopicProblemSelector(smgr,params);
//        problemSelector.setServletInfo(smgr);



    }


    public Response processNextProblemRequest(NextProblemEvent e) throws Exception {
        Response resp = super.processNextProblemRequest(e);
        // if we get back a response which is noMoreProblems, we want to insert a farewell page in the return
        if (resp instanceof ProblemResponse && ((ProblemResponse) resp).getProblem() == null) {
            ((ProblemResponse) resp).setEndPage(TutorPage.END_TUTOR_FRAME_CONTENT);

        }
        return resp;

    }

    protected int switchTopics (int curTopic) throws Exception {
        return -1;
    }


}
