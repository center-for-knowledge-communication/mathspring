package edu.umass.ckc.wo.assistments;

import edu.umass.ckc.wo.tutor.studmod.StudentProblemData;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 11/4/13
 * Time: 3:27 PM
 * Encapsulates the data for ONE problem that we send back to assistments as JSON
 * {
 "assignment": "<assignment ref>",
 "assistment": "<assistment ref>",
 "problem": "<problem ref>",
 "showCorrectness": "<is showing correctness enabled>",
 "showTutoring": "<is tutoring enabled>",
 "startDate": "<start date>",
 "startTime": "<start time>",
 "duration": "<milliseconds>",
 "firstActionTime": "<milliseconds>",
 "firstResponseTime": "<milliseconds>",
 "firstAnswer": "<answer text>",
 "attemptCount": "<number of tries>",
 "score": "<score given to time>",
 "feedback": "<descriptive feedback given>",
 "isScaffolding": "<response is for scaffolding>",
 "hintCount": "<hint count>",
 "answerInHint": "<hint contained answer>"
 }
 */
public class ProblemData {

    private String user;
    private String userClass;
    private String assignment;
    private String assistment;
    private String problem; // this is Assistments problem that they sent us
    private boolean showCorrectness;
    private boolean tutoringEnabled;
    private String startDate; // YYYY-MM-DD
    private String startTime; // HH:MM:SS[.sss]
    private long duration;
    private long firstActionTime;
    private long firstResponseTime;
    private String firstAnswer;
    private int attemptCount;
    private double score;
    private String feedback;
    private boolean scaffolding;
    private int hintCount;
    private boolean answerInHint;
    private String effort;

    private long timeToFirstHint;
    private long timeToFirstAttempt;
    private long endTime;
    private int numHintsBeforeCorrect;
    private int probId ;
    private int sessId ;

    public ProblemData() {
    }

    public ProblemData(String assignment, String assistment, String problem, boolean showCorrectness, boolean tutoringEnabled,
                       String startDate, String startTime, long duration, long firstActionTime, long firstResponseTime, String firstAnswer,
                       int attemptCount, double score, String feedback, boolean scaffolding, int hintCount, boolean answerInHint) {
        this.assignment = assignment;
        this.assistment = assistment;
        this.problem = problem;
        this.showCorrectness = showCorrectness;
        this.tutoringEnabled = tutoringEnabled;
        this.startDate = startDate;
        this.startTime = startTime;
        this.duration = duration;
        this.firstActionTime = firstActionTime;
        this.firstResponseTime = firstResponseTime;
        this.firstAnswer = firstAnswer;
        this.attemptCount = attemptCount;
        this.score = score;
        this.feedback = feedback;
        this.scaffolding = scaffolding;      //
        this.hintCount = hintCount;
        this.answerInHint = answerInHint;
    }


    private String[] buildDate (long dt) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(new Date(dt));
        int yyyy = cal.get(Calendar.YEAR);
        int mm = cal.get(Calendar.MONTH)+1;
        int dd = cal.get(Calendar.DAY_OF_MONTH);
        String[] result = new String[2];
        result[0]= String.format("%d-%d-%d",yyyy,mm,dd);
        int hh = cal.get(Calendar.HOUR_OF_DAY);
        int m = cal.get(Calendar.MINUTE);
        int ss = cal.get(Calendar.SECOND);
        int ms = cal.get(Calendar.MILLISECOND);
        result[1]= String.format("%d:%d:%d.%d",hh,m,ss,ms);
        return result;


    }



    public ProblemData(CoopUser u, AssistmentSessionData d, StudentProblemData lastProb) {
        // set the fields of this thing given the last problem from the problem history
        this.user = d.getUid();
        this.userClass = d.getaClass();
        this.assignment=d.getAssignment();
        this.assistment=d.getAssistment();
        this.problem=d.getProblem();
        this.showCorrectness=true;
        this.tutoringEnabled=true;
        // deal with time parsing etc, etc.
        String[] bgdt = buildDate(lastProb.getProblemBeginTime());
        String yyyymmdd =bgdt[0];
        String hhmmssmm = bgdt[1];
//        this.startTime = cal.get(Calendar.)
        this.startDate = yyyymmdd;
        this.startTime = hhmmssmm;
        this.duration = lastProb.getProblemEndTime() - lastProb.getProblemBeginTime();
        this.firstActionTime = Math.min(lastProb.getTimeToFirstAttempt(), lastProb.getTimeToFirstHint());
        this.firstResponseTime =   lastProb.getTimeToFirstAttempt();
//        this.firstAnswer = Math.randomInt()
        this.attemptCount = Math.max(lastProb.getNumAttemptsToSolve(), lastProb.getNumMistakes());
        this.score = lastProb.isSolved() ? 1.0 : 0.0;
        this.hintCount = lastProb.getNumHints();
        this.answerInHint = lastProb.getGivenAnswerHint();

         // We don't know what these are or do not have this data
        this.firstAnswer = "";
        this.feedback = "";
        this.scaffolding = lastProb.getNumHintsBeforeCorrect() > 0;

        this.timeToFirstHint = lastProb.getTimeToFirstHint();
        this.timeToFirstAttempt = lastProb.getTimeToFirstAttempt();
        this.endTime = lastProb.getProblemEndTime();
        this.numHintsBeforeCorrect = lastProb.getNumHintsBeforeCorrect();
        this.probId = lastProb.getProbId();
        this.sessId = lastProb.getSessId();
        this.effort = lastProb.getEffort();

    }
        /*
    "assignment": "<assignment ref>",
            "assistment": "<assistment ref>",
            "problem": "<problem ref>",
            "showCorrectness": "<is showing correctness enabled>",
            "showTutoring": "<is tutoring enabled>",
            "startDate": "<start date>",
            "startTime": "<start time>",
            "duration": "<milliseconds>",
            "firstActionTime": "<milliseconds>",
            "firstResponseTime": "<milliseconds>",
            "firstAnswer": "<answer text>",
            "attemptCount": "<number of tries>",
            "score": "<score given to time>",
            "feedback": "<descriptive feedback given>",
            "isScaffolding": "<response is for scaffolding>",
            "hintCount": "<hint count>",
            "answerInHint": "<hint contained answer>"
         */
    public String toJSONForAssistments () {
        JSONObject o = new JSONObject();
        // For some reason Assistments wants a strange form of JSON with everything quoted and subobjects all have quotes escaped.
        o.element("user",this.user);
        o.element("studentClass",this.userClass);
        o.element("assistment",this.assistment);
        o.element("assignment",this.assignment);
        o.element("problem",this.problem);
        o.element("showCorrectness",Boolean.toString(this.showCorrectness));
        o.element("showTutoring","true");
        o.element("startDate",this.startDate);
        o.element("startTime",this.startTime);
        o.element("duration",Long.toString(this.duration));
        o.element("firstActionTime",Long.toString(this.firstActionTime));
        o.element("firstResponseTime",Long.toString(this.firstResponseTime));
        o.element("firstAnswer",this.firstAnswer);
        o.element("attemptCount",Integer.toString(this.attemptCount));
        o.element("score",Double.toString(this.score));
        o.element("feedback",this.feedback);
        o.element("isScaffolding",Boolean.toString(this.scaffolding))  ;
        o.element("hintCount",Integer.toString(this.hintCount));
        o.element("answerInHint",Boolean.toString(this.answerInHint));
        JSONObject psd = new JSONObject();
        psd.element("timeToFirstHint",Long.toString(this.timeToFirstHint));
        psd.element("timeToFirstAttempt",Long.toString(this.timeToFirstAttempt));
        psd.element("problemEndTime",Long.toString(this.endTime));
        psd.element("numHintsBeforeCorrect",Integer.toString(this.numHintsBeforeCorrect));
        psd.element("mathspringProblemId",Integer.toString(this.probId));
        psd.element("mathspringSessionId",Integer.toString(this.sessId));
        psd.element("effort",this.effort);
        // have to fully escape all quote chars in the sub object
        String psdStr = String.format("\"{\"timeToFirstHint\": \"%s\", \"timeToFirstAttempt\": \"%s\", \"problemEndTime\": \"%s\", \"numHintsBeforeCorrect\": \"%s\"," +
                "\"mathspringProblemId\": \"%s\", \"mathspringSessionId\": \"%s\", \"effort\": \"%s\"   }\"",Long.toString(this.timeToFirstHint),
                Long.toString(this.timeToFirstAttempt), Long.toString(this.endTime),Integer.toString(this.numHintsBeforeCorrect), Integer.toString(this.probId),
                Integer.toString(this.sessId), this.effort);
        o.element("partnerSpecificData",psdStr);
        return o.toString();
    }




}
