package edu.umass.ckc.wo.content;

import edu.umass.ckc.wo.cache.ProblemMgr;
import net.sf.json.JSONObject;

import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 12/5/14
 * Time: 3:41 PM
 * To change this template use File | Settings | File Templates.
 * Frank 04-01-2020 Issue #91 remove all white space in short answer user input
 */
public class ProblemAnswer {

    private String val;
    private String letter;
    private String hint;
    private boolean distractor;
    private int bindingNumber;
    private int order;
    private int probId;

    public ProblemAnswer(String val, String letter, String hint, boolean distractor, int probId, int bindingNumber, int order) {
        this.val = val;
        this.letter = letter;
        this.hint = hint;
        this.distractor = distractor;
        this.probId = probId;
        this.bindingNumber = bindingNumber;
        this.order = order;
    }

    public ProblemAnswer (String val, int probId) {
        this(val,null,null,false,probId,-1,-1);
    }


    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public boolean isDistractor() {
        return distractor;
    }

    public void setDistractor(boolean distractor) {
        this.distractor = distractor;
    }

    public int getProbId() {
        return probId;
    }

    public void setProbId(int probId) {
        this.probId = probId;
    }

    public int getBindingNumber() {
        return bindingNumber;
    }

    public void setBindingNumber(int bindingNumber) {
        this.bindingNumber = bindingNumber;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public JSONObject getJSON(JSONObject jo) {
        jo.element(letter, val);
        return jo;
    }

    public boolean grade (String studentInput) {
        if (val != null) {
        	
        	String condensedVal = val.replaceAll("\\s+", "");
            return condensedVal.equalsIgnoreCase(studentInput.trim());
        }
        else return false;
    }
}
