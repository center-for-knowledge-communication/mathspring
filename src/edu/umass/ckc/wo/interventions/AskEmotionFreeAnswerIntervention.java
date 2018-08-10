package edu.umass.ckc.wo.interventions;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 2/14/14
 * Time: 2:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class AskEmotionFreeAnswerIntervention extends InputResponseIntervention implements NextProblemIntervention {



    public static final String REASON = "reason" ;
    public static final String SKIP_REASON = "skipReason" ;
    public static final String SKIP_FREQ = "skipFrequency" ;
    public static final String FEELING = "feeling" ;
    public static final String CONTINUE = "continue" ;
    public static final String GOAL = "goal" ;
    public static final String RESULT = "result" ;

    private boolean askAboutSkipping;
    private boolean skippedProblem;

    public AskEmotionFreeAnswerIntervention(boolean askAboutSkipping, boolean skippedProblem) {
        this.askAboutSkipping = askAboutSkipping;
        this.skippedProblem = skippedProblem;
    }

    @Override
    public String logEventName() {
        return "AskEmotionFreeAnswerIntervention" ;

    }


    @Override
    public String getName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getId() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isBuildProblem() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getResource() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getDialogHTML() {
        String str = "<div>  " +
                                 getFormOpen() + " <p>We will ask these questions a <b>few</b> times, so its <b>OK</b> to change your mind.  " +
                "Please be as <b>honest</b> as possible in answering these questions. <br><br>";
        str += "1.  How would you describe your emotions right now" +
                " (as compared to the last time you were asked)?";
        str += "<textarea name=\"" + FEELING + "\" rows=\"3\" cols=\"40\"/>" ;
        str += "<br><br>2. Why do you feel that way?";
        str += "<textarea name=\"" + REASON + "\" rows=\"3\" cols=\"40\"/>" ;
        str += "<br><br>3. What do you wish you could do to improve this class right now?";
        str += "<textarea name=\"" + GOAL + "\" rows=\"3\" cols=\"40\"/>" ;
//        str += "<br><br>3. What do you want to happen?";
//        str += "<textarea name=\"" + RESULT + "\" rows=\"3\" cols=\"40\"/>" ;
        str+= "</p>";
        if (askAboutSkipping && skippedProblem) {
            str += "<br>";
            str += "Have you skipped a problem recently (clicked on 'new problem' without answering)?";
            str += "<input type='radio' name='skipFrequency' value='never'> Never<br>";
            str += "<input type='radio' name='skipFrequency' value='fewTimes'> A few times<br>";
            str += "<input type='radio' name='skipFrequency' value='aLot'> A lot<br>";
            str += "If you skipped can you please say why?<br>";
            str += "<textarea name=\"" + SKIP_REASON + "\" rows=\"3\" cols=\"40\"/>";
        }
        str+="</form></div>";

        return str;
    }

    public String getType () {
        return "AskEmotionIntervention";
    }
}
