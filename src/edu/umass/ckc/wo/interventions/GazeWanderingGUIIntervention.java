package edu.umass.ckc.wo.interventions;

import edu.umass.ckc.wo.tutormeta.Intervention;
import net.sf.json.JSONObject;

/**
 */
public class GazeWanderingGUIIntervention {

    protected String flashScreen;
    protected String flashBox;    
    protected String playSound;
    protected String LCompanion;
    

    public GazeWanderingGUIIntervention(String flashScreen, String flashBox, String playSound, String LCompanion) {
        super();
        this.flashScreen = flashScreen;
        this.flashBox = flashBox;
        this.playSound= playSound;
        this.LCompanion = LCompanion;
    }


    public String getFlashScreen() {
        return flashScreen;
    }

    public String getFlashBox() {
        return flashBox;
    }

    public String getPlaySound() {
        return playSound;
    }

    public String getLCompanion() {
        return LCompanion;
    }

}
