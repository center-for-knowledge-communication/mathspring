package edu.umass.ckc.wo.interventions;

import edu.umass.ckc.wo.tutor.intervSel2.AskEmotionIS;
import org.jdom.Element;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 2/14/14
 * Time: 2:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class AskEmotionSliderIntervention extends InputResponseIntervention implements NextProblemIntervention {
    private AskEmotionIS.Emotion emotion;
    private int numVals;
    public static final String LEVEL = "level" ;
    public static final String EMOTION = "emotion" ;
    public static final String REASON = "reason" ;
    public static final String SKIP_REASON = "skipReason" ;
    private boolean askWhy;
    private boolean askAboutSkipping;
    private boolean skippedProblem;
    // the default question this thing asks has an emotion plugged into the string
    private String question = "Based on the last few problems tell us about your level of %s in solving math problems.";
    private String questionHeader = "Please tell us how you are feeling.";

    public AskEmotionSliderIntervention(AskEmotionIS.Emotion emotionToQuery, String numVals, boolean askWhy, boolean askAboutSkipping, String questionHeader, String question, boolean skippedProblem) {
        this.emotion = emotionToQuery;
        this.askWhy=askWhy;
        this.askAboutSkipping=askAboutSkipping;
        this.skippedProblem=skippedProblem;
        if (numVals != null)
            this.numVals = Integer.parseInt(numVals);

        // allow config to set the question that this thing asks.
        if (questionHeader != null)
            this.questionHeader = questionHeader;
        if (question != null)
            this.question = question;
    }


    public void configure (Element configXML) {

    }

    @Override
    public String logEventName() {
        return "AskEmotionIntervention-" + emotion.getName();
    }

    @Override
    public boolean isBuildProblem() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
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
    public String getResource() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getDialogHTML() {
        String str = "<div>  " +
                "<link href=\"css/simple-slider.css\" rel=\"stylesheet\" type=\"text/css\" />" +
                "<script type=\"text/javascript\" src=\"js/simple-slider.js\"></script>"
                + getFormOpen() + " <p>"+ questionHeader +" <br>" +
                String.format(question,this.emotion.getName());

        str += "<br><br>";
        str += "<input name=\"" + EMOTION + "\" type=\"hidden\" value=\"" + emotion.getName() + "\"/>";
        str += "<br>\n" +
"        <table><tr><td width=\"200\"><p>"+this.emotion.getLabels().get(0)+"</p></td><td><input type=\"text\" name=\""+ LEVEL +"\" data-slider=\"true\" data-slider-range=\"1,"+numVals+"\" data-slider-step=\"1\" data-slider-snap=\"true\"></td><td><p>"+this.emotion.getLabels().get(this.emotion.getLabels().size()-1)+"</p></td></tr></table>\n" +
"        </br>";
        if (askWhy) {
            str += "Why is that?<br>";
            str += "<textarea name=\"" + REASON + "\" rows=\"4\" cols=\"40\"/>";
        }
        if (askAboutSkipping && skippedProblem) {
            str += "<br>";
            str += "Have you skipped a problem recently (clicked on 'new problem' without answering)?";
            str += "<input type='radio' name='skipFrequency' value='never'> Never<br>";
            str += "<input type='radio' name='skipFrequency' value='fewTimes'> A few times<br>";
            str += "<input type='radio' name='skipFrequency' value='aLot'> A lot<br>";
            str += "If you skipped can you please say why?<br>";
            str += "<textarea name=\"" + SKIP_REASON + "\" rows=\"3\" cols=\"40\"/>";
        }

        str+="</p></form></div>";
        return str;
    }

    public String getType () {
        return "AskEmotionIntervention";
    }
}
