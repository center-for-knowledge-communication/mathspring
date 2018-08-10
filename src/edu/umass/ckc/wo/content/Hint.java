package edu.umass.ckc.wo.content;

import edu.umass.ckc.wo.cache.ProblemMgr;
import net.sf.json.JSONObject;

import java.sql.SQLException;

public class Hint  {
    // Column names in the Hint table
    public static final String ID = "id";
    public static final String PROBLEM_ID = "problemId";
    public static final String NAME = "name"; // also serves as the label within the Flash
    public static final String GIVES_ANSWER = "givesAnswer";
    public static final String ATTRIBUTE = "attribute" ;
    public static final String ATT_VALUE = "value" ;
    public static final String STRATEGIC_HINT_LABEL = "strategic_hint";
    // a constant hint object which is the strategic hint for any problem.   Note it must have an id that is not -1
    // so that the hint selector will think that a real hint was given .
    public static final Hint STRATEGIC_HINT  = new Hint(-113,STRATEGIC_HINT_LABEL,-1,false);
    public static final Hint NO_STRATEGIC_HINT  = new Hint(-114,"noStrategicHint",-1,false);
    public static final Hint STRATEGIC_HINT_PLAYED  = new Hint(-115,"strategicAlreadyPlayed",-1,false);
    private int id;
    private String label; // a label within the problem Flash or Edge  program
    private transient int problemId;     // the fields marked transient are not serialized into a JSON output
    private transient boolean is_visual ;
    private transient boolean givesAnswer;
    private String statementHTML;
    private int placement; // DM 1/23/18 1 is overlay problem figure, 2 is in hint side
    private String imageURL; // DM 1/23/18 will be something like {[foo.jpg]} or a full URL
    private String audioResource;
    private String hoverText;
    private int order;

    private boolean is_root=false;
    // This only exists to represent Hint information about a Hint event in 4mality.
    public Hint (int id, String label) {
        this.id = id;
        this.label = label;
    }

    public Hint(int id, String label, int problemId, boolean givesAnswer) {
        this.id=id;
        this.label=label;
        this.problemId=problemId;
        this.givesAnswer=givesAnswer;
    }

    public Hint(int id, String label, int problemId, boolean givesAnswer, String statementHTML, String audioResource, String hoverText, int order,
                int placement, String imageURL)  {
        this.id=id;
        this.label=label;
        this.problemId=problemId;
        this.givesAnswer=givesAnswer;
        this.is_visual = true ;
        this.statementHTML = statementHTML;
        this.audioResource = audioResource; // eg. {[mysound.mp3]}
        this.hoverText = hoverText;
        this.order = order;
        this.placement=placement; // DM 1/23/18 added  1 overlay, 2 side
        this.imageURL = imageURL; // DM added e.g. {[myimage.jpg]} OR full URL
    }

    public static boolean isBottomOut(String name) {
        return name.startsWith("choose");
    }

    public static void main(String[] args) {
        Hint h = new Hint(10,"myLab",345,true);
        String json = h.getJSON(new JSONObject()).toString();
        System.out.println("Hint json is " + json);
    }

    public JSONObject buildJSON(JSONObject jo) {
        return jo;
    }

    public JSONObject getJSON (JSONObject jo) {
        jo.element("id",this.id);
        jo.element("label",this.label);
        Problem p = null;
        try {
            p = ProblemMgr.getProblem(getProblemId());
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        if (p != null && p.isQuickAuth()) {
//            jo.element("statementHTML", JSONUtil.changeSpecialChars(statementHTML));
            jo.element("statementHTML", statementHTML);
            jo.element("audioResource", audioResource); // e.g. {[hint1.mp3]}
            jo.element("hoverText",hoverText);
            jo.element("placement",placement); // e.g. 1 or 2
            jo.element("imageURL",imageURL); // e.g. {[myimage.jpg]}
        }

        return jo;
    }

    public String getLabel() {
      return this.label;
    }

    public void setLabel (String label) {
        this.label = label;
    }

    public int getId() {
      return this.id;
    }

    public boolean isRoot() {
      return this.is_root;
    }

    public boolean isVisual() {
      return this.is_visual;
    }

    public int getProblemId() {
        return this.problemId;
    }

    public void setProblemId(int problemId) {
        this.problemId = problemId;
    }

    public boolean getGivesAnswer () {
        return this.givesAnswer;
    }

    public void setIs_root(boolean is_root) {
        this.is_root = is_root;
    }

    public int getOrder() {
        return order;
    }
}