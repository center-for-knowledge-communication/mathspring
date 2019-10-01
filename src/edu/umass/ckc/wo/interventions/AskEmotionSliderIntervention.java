package edu.umass.ckc.wo.interventions;

import edu.umass.ckc.wo.tutor.intervSel2.AskEmotionIS;
import org.jdom.Element;

import java.util.Locale;
import java.util.ResourceBundle;


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
    private String question = "";
    private String questionHeader = "";

    private Locale locale;

    public AskEmotionSliderIntervention(AskEmotionIS.Emotion emotionToQuery, String numVals, boolean askWhy, boolean askAboutSkipping, String questionHeader, String question, boolean skippedProblem, Locale loc) {
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
        this.locale = loc;

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
        		
        		str = "<div>  " +
        				"<link href=\"css/simple-slider.css\" rel=\"stylesheet\" type=\"text/css\" />" +
        				"<script type=\"text/javascript\" src=\"js/simple-slider.js\"></script>"
        				+ getFormOpen() + " <p>" + rb.getString("ask_emotion_radio_header") + "<br>" + rb.getString("ask_emotion_radio_question") + strEmo + ".";
       			str += "<br><br>";
       			str += "<input name=\"" + EMOTION + "\" type=\"hidden\" value=\"" + emotion.getName() + "\"/>";
       			str += "<br>\n" +
"        			<table><tr><td width=\"200\"><p>"+this.emotion.getLabels().get(0)+"</p></td><td><input type=\"text\" name=\""+ LEVEL +"\" data-slider=\"true\" data-slider-range=\"1,"+numVals+"\" data-slider-step=\"1\" data-slider-snap=\"true\"></td><td><p>"+this.emotion.getLabels().get(this.emotion.getLabels().size()-1)+"</p></td></tr></table>\n" +
"        			</br>";
       			if (askWhy) {
       				str += rb.getString("why_is_that") + "<br>";
       				str += "<textarea name=\"" + REASON + "\" rows=\"4\" cols=\"40\"/>";
       			}
        		if (askAboutSkipping && skippedProblem) {
        			str += "<br>";
        			str += rb.getString("have_you_skipped") + "<br>";
        			str += "<input type='radio' name='skipFrequency' value='never'>" + rb.getString("never") + "<br>";
        			str += "<input type='radio' name='skipFrequency' value='fewTimes'>" + rb.getString("a_few_times") + "<br>";
        			str += "<input type='radio' name='skipFrequency' value='aLot'>" + rb.getString("a_lot") + "<br>";
        			str += "<br>" + rb.getString("if_you_skipped_why") +  "<br>";
        			str += "<textarea name=\"" + SKIP_REASON + "\" rows=\"2\" cols=\"50\"/>";
        		}

           		str+="</p></form></div>";
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
