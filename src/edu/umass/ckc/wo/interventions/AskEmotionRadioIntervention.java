package edu.umass.ckc.wo.interventions;

import edu.umass.ckc.wo.tutor.intervSel2.AskEmotionIS;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 2/14/14
 * Time: 2:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class AskEmotionRadioIntervention extends InputResponseIntervention implements NextProblemIntervention {
    private AskEmotionIS.Emotion emotion;
    protected boolean buildProblem=false;
    private boolean askWhy=false;
    private boolean askAboutSkipping=false;
    private boolean skippedProblem=false;
    // the default question this thing asks has an emotion plugged into the string
    private String question = "Based on the last few problems tell us about your level of %s in solving math problems.";
    private String questionHeader = "Please tell us how you are feeling.";

    public static final String LEVEL = "level" ;
    public static final String EMOTION = "emotion" ;
    public static final String REASON = "reason" ;
    public static final String SKIP_REASON = "skipReason" ;

    public AskEmotionRadioIntervention(AskEmotionIS.Emotion emotionToQuery, boolean askWhy, boolean askAboutSkipping, boolean skippedProblem) {
        this.emotion = emotionToQuery;
        this.askWhy=askWhy;
        this.askAboutSkipping=askAboutSkipping;
        this.skippedProblem=skippedProblem;
    }

    @Override
    public String logEventName() {
        return "AskEmotionIntervention-" + emotion.getName();

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
                                 getFormOpen() + " <p>" + this.questionHeader + "<br>" +
                String.format(question,emotion.getName());

        str += "<input type=\"hidden\" name=\"" + EMOTION + "\" value=\"" + emotion.getName() + "\"><br>";
        for (int i =0;i<emotion.getLabels().size();i++)
            str += "<input name=\"" + LEVEL + "\" type=\"radio\" value=\"" + emotion.getVals().get(i) + "\">" + emotion.getLabels().get(i) + "</input><br>";
        str += "<br>";
        if (askWhy) {
            str += "Why is that?<br>";
            str += "<textarea name=\"" + REASON + "\" rows=\"2\" cols=\"50\"/>";
        }
        str+= "</p>";
        if (askAboutSkipping && skippedProblem) {
            str += "<br>";
            str += "Have you skipped a problem recently (clicked on 'new problem' without answering)?<br>";
            str += "<input type='radio' name='skipFrequency' value='never'> Never<br>";
            str += "<input type='radio' name='skipFrequency' value='fewTimes'> A few times<br>";
            str += "<input type='radio' name='skipFrequency' value='aLot'> A lot<br>";
            str += "<br>If you skipped can you please say why?<br>";
            str += "<textarea name=\"" + SKIP_REASON + "\" rows=\"2\" cols=\"50\"/>";
        }
        str+="</form></div>";

        return str;
    }

    public String getType () {
        return "AskEmotionIntervention";
    }
}
