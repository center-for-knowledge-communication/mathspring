package edu.umass.ckc.wo.tutor.response;

import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.html.tutor.TutorPage;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.state.StudentState;
import edu.umass.ckc.wo.tutormeta.Intervention;
import edu.umass.ckc.wo.tutormeta.TopicMastery;
import net.sf.json.JSONObject;

import java.sql.SQLException;
import java.util.List;
import java.util.Random;

/**
 * <p> Created by IntelliJ IDEA.
 * User: david
 * Date: Dec 29, 2008
 * Time: 10:59:25 AM
 */
public class ProblemResponse extends Response {
    public static final ProblemResponse NO_MORE_PROBLEMS = new ProblemResponse(true,false,false);
    public static final ProblemResponse NO_MORE_REVIEW_PROBLEMS = new ProblemResponse(false,true,false);
    public static final ProblemResponse NO_MORE_CHALLENGE_PROBLEMS = new ProblemResponse(false,false,true);
    protected Problem prob;
    private String endPage = null;
    private Intervention intervention = null;
    private String params = null; // Specific bindings for student, if this problem is parametrized

    private boolean noMoreProblems=false;
    private boolean noMoreReviewProblems=false;
    private boolean noMoreChallengeProblems=false;


    public ProblemResponse(Problem p, List<TopicMastery> topicMasteryLevels, int curTopic) {
        this.prob = p;
        this.topicMasteryLevels = topicMasteryLevels;
        this.curTopic=curTopic;
        buildJSON();
    }

    //new constructor to allow for finishing test
    public ProblemResponse(Problem p) {
        this.prob = p;
        buildJSON();
    }

    public ProblemResponse (boolean noMoreProblems, boolean noMoreReviewProblems, boolean noMoreChallengeProblems ) {
        this.endPage = TutorPage.NO_MORE_CONTENT; // set some default end page.
        this.noMoreProblems=noMoreProblems;
        this.noMoreReviewProblems=noMoreReviewProblems;
        this.noMoreChallengeProblems=noMoreChallengeProblems;
        buildJSON();
    }

    public ProblemResponse () {
        buildJSON();

    }

    public Intervention getIntervention() {
        return intervention;
    }

    public void setIntervention(Intervention intervention) {
        if (intervention != null)      {
            JSONObject interventionJSON = new JSONObject();
            intervention.buildJSON(interventionJSON);
            jsonObject.element("intervention", interventionJSON.toString());
        }
    }

    public String logEventName() {
        if (prob == null)
            return "NoProblem";
        else if (prob.isPractice())
           return "PracticeProblem";
        else return "ExampleProblem";
    }

    public JSONObject buildJSON() {
        jsonObject = new JSONObject();
        if (prob == null || noMoreProblems || noMoreChallengeProblems || noMoreReviewProblems) {
            String label =  "noMoreProblems";
            if (noMoreChallengeProblems)
                label = "noMoreChallengeProblems";
            else if (noMoreReviewProblems)
                label = "noMoreReviewProblems";
            jsonObject.element("activityType", label);
            jsonObject.element("endPage",endPage);
            // Some interventions are designed to be shown while a problem is being shown
            // These interventions append their JSon to the end of a Problem's JSon

            return jsonObject;
        }

        else {
            prob.buildJSON(jsonObject);  //  problem generates its own json object so we add in character stuff

            return jsonObject;
        }

    }

    public Problem getProblem() {
        return prob;
    }

    public boolean isNoMoreProblems () {
        return this.noMoreProblems;
    }

    public String getEndPage() {
        return endPage;
    }

    public void setEndPage(String endPage) {
        this.endPage = endPage;
        jsonObject.element("endPage",endPage);

    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    //This isn't used anywhere?
    public void setProblemBindings(SessionManager smgr) throws SQLException {
        Problem problem = this.getProblem();
        if (problem != null && problem.isParametrized()) {
            problem.getParams().addBindings( this, smgr.getStudentId(), smgr.getConnection(), smgr.getStudentState());
            // parameterized short answer problems need to save the possible answers in the student state

        }
    }
}