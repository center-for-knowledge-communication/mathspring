package edu.umass.ckc.wo.interventions;

import java.util.Locale;
import java.util.ResourceBundle;

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

    private Locale locale;

    public AskEmotionFreeAnswerIntervention(boolean askAboutSkipping, boolean skippedProblem, Locale loc) {
        this.askAboutSkipping = askAboutSkipping;
        this.skippedProblem = skippedProblem;
        this.locale = loc;
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
    	ResourceBundle rb = null;

    	String str = "";

        try {           	
        		// Multi=lingual enhancement
        		rb = ResourceBundle.getBundle("MathSpring",this.locale);
        		str = "<div>  " + getFormOpen() + " <p>" + rb.getString("ask_emotion_radio_header") + "<br><br>";
        		str += rb.getString("ask_emotion_free_question1");
        		str += " <textarea name=\"" + FEELING + "\" rows=\"3\" cols=\"40\"/>" ;
        		str += "<br><br>" + rb.getString("ask_emotion_free_question2");
        		str += " <textarea name=\"" + REASON + "\" rows=\"3\" cols=\"40\"/>" ;
        		str += "<br><br>" + rb.getString("ask_emotion_free_question3");
        		str += " <textarea name=\"" + GOAL + "\" rows=\"3\" cols=\"40\"/>" ;
//        str += "<br><br>3. What do you want to happen?";
//        str += "<textarea name=\"" + RESULT + "\" rows=\"3\" cols=\"40\"/>" ;
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
