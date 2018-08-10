package edu.umass.ckc.wo.tutor.pedModel;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: Sep 23, 2009
 * Time: 10:58:57 AM
 * To change this template use File | Settings | File Templates.
 */

public class UIElements {
    private int drawingTool;
    private int textTool;
    private int eraserTool=0;
    private int undoButton;
    private int showExampleButton;
    private int showVideoButton;
    private int solveProblemButton;
    private int hintButton;
    private int clearAllButton;
    private int replayButton;
    private int arrowButton;
    private int readProbButton;
    private int myProgressButton;
    private int villageButton;

    public static final int OFF = 0;
    public static final int DISPLAY = 2;
    public static final int ENABLED = 1;
    public static final int BOTH = DISPLAY + ENABLED;

    public UIElements(int drawingTool, int textTool, int eraserTool, int undoButton, int clearAllButton,
                      int arrowButton, int showExampleButton, int showVideoButton, int solveProblemButton,
                      int hintButton, int replayButton, int readProbButton, int myProgressButton, int villageButton) {
        this.drawingTool = drawingTool;
        this.textTool = textTool;
//        this.eraserTool = eraserTool;
        this.eraserTool = 0; // TEMPORARILY turned off until its working in client
        this.undoButton = undoButton;
        this.showExampleButton = showExampleButton;
        this.showVideoButton = showVideoButton;
        this.solveProblemButton = solveProblemButton;
        this.hintButton = hintButton;
        this.clearAllButton = clearAllButton;
        this.replayButton = replayButton;
        this.arrowButton = arrowButton;
        this.readProbButton = readProbButton;
        this.myProgressButton = myProgressButton ;
        this.villageButton = villageButton;
    }

    private String getButtonState (String buttonName, int flags) {
        return String.format("<uiElement name=\"%s\" display=\"%b\" enabled=\"%b\"/>",buttonName,
                (flags & DISPLAY) !=0, (flags & ENABLED) != 0);
    }

    public String getDrawingElt () {
        return getButtonState("DrawingTool",drawingTool);
    }
    public String getTextElt () {
        return getButtonState("TextTool",textTool);
    }
    public String getEraserToolElt () {
        return getButtonState("EraserTool",eraserTool);
    }
    public String getUndoButtonElt () {
        return getButtonState("UndoButton",undoButton);
    }
    public String getShowExampleButtonElt () {
        return getButtonState("ShowExampleButton",showExampleButton);
    }
    public String getShowVideoButtonElt () {
        return getButtonState("ShowVideoButton",showVideoButton);
    }
    public String getShowSolveProblemButtonElt () {
        return getButtonState("ShowSolveProblemButton",solveProblemButton);
    }
    public String getClearAllButtonElt () {
        return getButtonState("ClearAllButton",clearAllButton);
    }
    public String getReplayButtonElt () {
        return getButtonState("ReplayButton",replayButton);
    }
    public String getHintButtonElt () {
        return getButtonState("HintButton",hintButton);
    }
    public String getArrowButtonElt () {
        return getButtonState("ArrowButton",arrowButton);
    }

    public String getReadProblemButtonElt () {
        return getButtonState("ReadProblemButton",readProbButton);
    }

    public String getReadMyProgressButtonElt () {
        return getButtonState("MyProgressButton", myProgressButton);
    }

    public String getVillageButtonElt () {
        return getButtonState("VillageButton", villageButton);
    }

    public String getXML () {
        return "<uiElements>" +
                getDrawingElt() + getTextElt() + getEraserToolElt() +
                  getUndoButtonElt() + getShowExampleButtonElt() + getShowVideoButtonElt() +
                getShowSolveProblemButtonElt() +  getClearAllButtonElt() + getReplayButtonElt() +
                getHintButtonElt() + getArrowButtonElt() + getReadProblemButtonElt() + getReadMyProgressButtonElt() + getVillageButtonElt() +"</uiElements>";
    }



}
