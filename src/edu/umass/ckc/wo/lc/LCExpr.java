package edu.umass.ckc.wo.lc;

import edu.umass.ckc.wo.db.DbPedagogy;
import edu.umass.ckc.wo.db.DbUtil;
import edu.umass.ckc.wo.event.SessionEvent;
import edu.umass.ckc.wo.event.tutorhut.AttemptEvent;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.tutor.Settings;

import java.sql.Connection;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 1/25/16
 * Time: 12:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class LCExpr extends LCRuleComponent {
    private LCFn fn;
//    private String fn;
//    private List<Object> args;
    private String relop;   // possible are: =, !=, <, <= ,>,>=
    private int valType;
    private String strVal;
    private int iVal;
    private double dVal;
    private Object val;

    public static int STR = 0;
    public static int INT = 1;
    public static int DOUB = 2;
    public static int BOOL = 3;
    public static int MAST = 4;

    public static final double LOW_MAST = 0.35;
    public static final double HIGH_MAST = 0.75;

    public LCExpr (String fn, Class[] args) {
        this.fn=new LCFn(fn,args);
        this.valType = BOOL;

    }


    public LCExpr(String fn,  Class[] args, String relop, String strVal) {
        this.fn=new LCFn(fn,args);
        this.relop = relop;
        this.valType = STR;
        this.strVal = strVal;
    }

    public LCExpr(String fn,  Class[] args, String relop, String strVal, boolean isMastery) {
        this.fn=new LCFn(fn,args);
        this.relop = relop;
        this.valType = MAST;
        this.strVal = strVal;
    }

    public LCExpr(String fn,  Class[] args, String relop, int iVal) {
        this.fn=new LCFn(fn,args);
        this.relop = relop;
        this.valType = INT;
        this.iVal = iVal;
    }

    public LCExpr(String fn,  Class[] args, String relop, double dVal) {
        this.fn=new LCFn(fn,args);
        this.relop = relop;
        this.valType = DOUB;
        this.dVal = dVal;
    }

    public LCFn getFn() {
        return fn;
    }

    public String getRelop() {
        return relop;
    }

    public int getValType() {
        return valType;
    }

    public boolean eval () throws Exception {
        //  Use the relop to compare the result of the function to the value
        this.fn.setup(smgr,event, cache);

        try {
            val = this.fn.eval();
        } catch (Exception ee) {
            System.out.println("Failed to run LCRule function: "  + fn.getFnName());
            throw ee;
        }
        if (valType == BOOL)
            return ((Boolean) val) == true;
        else if (relop.equals("=") && valType == INT )
            return ((Integer) val) == this.iVal;
        else if (relop.equals("=") && valType == DOUB)
            return ((Double) val) == this.dVal;
        else if (relop.equals("=") && valType == STR)
            return ((String) val).equals(this.strVal);
        else if (relop.equals("!=") && valType == INT)
            return ((Integer) val) == this.iVal;
        else if (relop.equals("!=") && valType == DOUB)
            return ((Double) val) != this.dVal;
        else if (relop.equals("!=") && valType == STR)
            return !((String) val).equals(this.strVal);
        else if (relop.equals("<") && valType == INT)
            return ((Integer) val) < this.iVal;
        else if (relop.equals("<") && valType == DOUB)
            return ((Double) val) < this.dVal;
        else if (relop.equals("<=") && valType == INT)
            return ((Integer) val) <= this.iVal;
        else if (relop.equals("<=") && valType == DOUB)
            return ((Double) val) <= this.dVal;
        else if (relop.equals(">") && valType == INT)
            return ((Integer) val) > this.iVal;
        else if (relop.equals(">") && valType == DOUB)
            return ((Double) val) > this.dVal;
        else if (relop.equals(">=") && valType == INT)
            return ((Integer) val) >= this.iVal;
        else if (relop.equals(">=") && valType == DOUB)
            return ((Double) val) >= this.dVal;
        else if (valType == MAST)
            return evalMasteryExpr(relop,this.strVal,((Double) val));

        return false;
    }

    // Called after running eval.  Returns the name of the function and the value it fetched.
    String getValInfo () {
        return this.fn.getFnName() + ":" + this.val.toString();
    }

    // A kind of hack way of offering the ability to have a function like:
    // topicMastery = LOW.   strVal will be one of LOW, MIDDLE, HIGH.  val
    // will be a double returned by the topicMasteryValue function.
    //
    private boolean evalMasteryExpr(String relop, String strVal, double val) {
        double compareToVal = 0;
        if (strVal.equals("LOW"))
            compareToVal = LOW_MAST;
        else if (strVal.equals("HIGH"))
            compareToVal = HIGH_MAST;
        // it doesn't make sense to ask mastery < LOW.  So <= and < are both interpreted as
        // either mastery < HIGH or mastery < MIDDLE.  Similarly for > where it doesn't make
        // sense to have mastery > HIGH
        if (relop.equals("<"))
            return val < compareToVal;
        else if (relop.equals("<="))
            return val <= compareToVal;
        else if (relop.equals(">"))
            return val > compareToVal;
        else if (relop.equals(">="))
            return val >= compareToVal;
        else if (relop.equals("=")) {
            if (strVal.equals("LOW"))
                return val <= LOW_MAST;
            else if (strVal.equals("MIDDLE"))
                return val > LOW_MAST && val < HIGH_MAST;
            else if (strVal.equals("HIGH"))
                return val >= HIGH_MAST;
        }
        return false;

    }

    public boolean evalPrint () throws Exception {
        boolean b = eval();
        System.out.println(this.toString() +"is: " + b);
        return b ;
    }

    public String toString () {
        String val = "";
        if (this.valType == INT)
            val = Integer.toString(iVal);
        else if (this.valType == DOUB)
            val = Double.toString(dVal);
        else if (this.valType == BOOL)
            return this.getFn().getFnName();
        else
            val = strVal;
        return this.getFn().getFnName() + " " + this.getRelop() + " " + val;
    }

    public static void main(String[] args) {
        Connection conn = null;
        try {
            conn = DbUtil.getAConnection();
            Settings.lessonMap = DbPedagogy.buildAllLessons(conn);
            Settings.loginMap = DbPedagogy.buildAllLoginSequences(conn);
            Settings.pedagogyGroups = DbPedagogy.buildAllPedagogies(conn,null);
            SessionManager smgr = new SessionManager(conn);
            StudentDataCache cache = new StudentDataCache();
            smgr.attemptSessionCreation("dm","dm",System.currentTimeMillis(),true);

            System.out.println("Testing LCExpr with user dm/dm studId="+ smgr.getStudentId());

            SessionEvent atEv = new AttemptEvent("test",false,10,20,smgr.getSessionNum());
            LCExpr e1 = new LCExpr("curTopicMastery",null, "<", 0.5);
            e1.setup(smgr,null, cache);
            e1.evalPrint();
            Class[] args1 = new Class[] {SessionEvent.class};
            LCExpr e2 = new LCExpr("isAttemptEvent",args1);
            e2.setup(smgr,null, cache);
            e2.evalPrint();
            LCExpr e3 = new LCExpr("curProbNumIncorrectAttempts",null, ">=", 0);
            e3.setup(smgr,null, cache);
            e3.evalPrint();

            LCExpr e4 = new LCExpr("solvedOnAttempt",null, "=", 1);
            e4.setup(smgr,null, cache);
            e4.evalPrint();
            LCExpr e5 = new LCExpr("timeToSolve",null, ">=", 5000);
            e5.setup(smgr,null, cache);
            e5.evalPrint();
            LCExpr e6 = new LCExpr("curProbTimeToFirstAttempt",null, ">", 1000);
            e6.setup(smgr,null, cache);
            e6.evalPrint();
            LCExpr e7 = new LCExpr("effort3",null, "=", "SOF");
            e7.setup(smgr,null, cache);
            e7.evalPrint();
            LCExpr e8 = new LCExpr("effort2",null, "=", "SHINT");
            e8.setup(smgr,null, cache);
            e8.evalPrint();
            LCExpr e9 = new LCExpr("effort1",null, "=", "NOTR");
            e9.setup(smgr,null, cache);
            e9.evalPrint();

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
}
