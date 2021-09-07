package edu.umass.ckc.wo.interventions;

/**
 * Copyright (c) University of Massachusetts
 * Written by: Frank Sylvia
 * Date: Jun 26, 2021
 * Frank	06-26-21	Support for gaze detection
 */

public class GazeWanderingGUIIntervention {

    protected int flashScreen;
    protected int flashBox;    
    protected int textBox;    
    protected int playSound;
    protected int LCompanion;
    protected String LCFilename;
    protected String textBoxChoice;
    

    public GazeWanderingGUIIntervention(int flashScreen, int flashBox, int textBox, int playSound, int LCompanion, String LCFilename, String textBoxChoice) {
        super();
        this.flashScreen = flashScreen;
        this.flashBox = flashBox;
        this.textBox = textBox;
        this.playSound= playSound;
        this.LCompanion = LCompanion;
        this.LCFilename = LCFilename;
        this.textBoxChoice = textBoxChoice;
    }


    public int getFlashScreen() {
        return flashScreen;
    }

    public int getFlashBox() {
        return flashBox;
    }

    public int getTextBox() {
        return textBox;
    }

    public int getPlaySound() {
        return playSound;
    }

    public int getLCompanion() {
        return LCompanion;
    }

    public String getLCFilename() {
        return LCFilename;
    }

    public String getTextBoxChoice() {
        return textBoxChoice;
    }
}
