package edu.umass.ckc.wo.interventions;

import edu.umass.ckc.wo.tutor.intervSel2.AskEmotionIS;
import java.util.Locale;
import java.util.ResourceBundle;

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

    public static final String LEVEL = "level" ;
    public static final String EMOTION = "emotion" ;
    public static final String REASON = "reason" ;
    public static final String SKIP_REASON = "skipReason" ;

    private Locale locale;

    public AskEmotionRadioIntervention(AskEmotionIS.Emotion emotionToQuery, boolean askWhy, boolean askAboutSkipping, boolean skippedProblem, Locale loc) {
        this.emotion = emotionToQuery;
        this.askWhy=askWhy;
        this.askAboutSkipping=askAboutSkipping;
        this.skippedProblem=skippedProblem;
        this.locale = loc;
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
    	
    	ResourceBundle rb = null;

    	String str = "";

        try {           	
        		// Multi=lingual enhancement
        		rb = ResourceBundle.getBundle("MathSpring",this.locale);
        		
        		String strEmotion = emotion.getName();       		
        		String strEmotionLower = strEmotion.toLowerCase(this.locale);
        		
        		String strEmo = ""; 
        		try {
        			strEmo = rb.getString(strEmotionLower);
        		}
        		catch(Exception e) {
        			strEmo = emotion.getName();
        		}
        		
        		str = "<div>  " + getFormOpen() + " <p>" + rb.getString("ask_emotion_radio_header") + "<br>" + rb.getString("ask_emotion_radio_question") + strEmo + ".";

        		str += "<input type=\"hidden\" name=\"" + EMOTION + "\" value=\"" + emotion.getName() + "\"><br>";
        		for (int i =0;i<emotion.getLabels().size();i++)
        			str += "<input name=\"" + LEVEL + "\" type=\"radio\" value=\"" + emotion.getVals().get(i) + "\">" + emotion.getLabels().get(i) + "</input><br>";
        		str += "<br>";
        		if (askWhy) {
       				str += rb.getString("why_is_that") + "<br>";
        			str += "<textarea name=\"" + REASON + "\" rows=\"2\" cols=\"50\"/>";
        		}
        		str+= "</p>";
        		if (askAboutSkipping && skippedProblem) {
        			str += "<br>";
        			str += rb.getString("have_you_skipped") + "<br>";
        			str += "<input type='radio' name='skipFrequency' value='never'>" + rb.getString("never") + "<br>";
        			str += "<input type='radio' name='skipFrequency' value='fewTimes'>" + rb.getString("a_few_times") + "<br>";
        			str += "<input type='radio' name='skipFrequency' value='aLot'>" + rb.getString("a_lot") + "<br>";
        			str += "<br>" + rb.getString("if_you_skipped_why") +  "<br>";
        			str += "<textarea name=\"" + SKIP_REASON + "\" rows=\"2\" cols=\"50\"/>";
        		}
        		str+="</form></div>";
        }
        catch (java.util.MissingResourceException e){
            System.out.println(e.getMessage());
            str = "System Error: " + e.getMessage();
        }

        return str;
    }

    public String getType () {
        return "AskEmotionIntervention";
    }
}
