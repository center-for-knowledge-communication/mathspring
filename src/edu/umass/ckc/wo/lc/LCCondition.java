package edu.umass.ckc.wo.lc;


/**
 * A condition is a boolean expression composed of expressions joined by boolean operators AND, OR, and NOT.
 * For now This is being kept simple and is limited to just a single expression and NOT.
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 1/25/16
 * Time: 12:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class LCCondition  extends LCRuleComponent {
    private int condId;
    private LCExpr expr;
    boolean applyNot = false;
    private String paramType;
    private String param;


    public LCCondition (int id, String fnName, String relop, String param, String paramType, boolean applyNot) {
        this.condId = id;
        this.paramType = paramType;
        this.param = param;
        if (relop == null && param == null)
            expr = new LCExpr(fnName,null);
        else if (paramType.equalsIgnoreCase("String"))
            expr = new LCExpr(fnName,null,relop,param);
        else if (paramType.equalsIgnoreCase("Integer"))
            expr = new LCExpr(fnName,null,relop,Integer.parseInt(param));
        else if (paramType.equalsIgnoreCase("Double"))
            expr = new LCExpr(fnName,null,relop,Double.parseDouble(param));
        else if (paramType.equalsIgnoreCase("Mastery"))
            expr = new LCExpr(fnName,null,relop,param,true);
        this.applyNot = applyNot;
    }

    public int getNumParams () {
        return param == null ? 0 : 1;
    }

    public String getFnName () {
        return this.expr.getFn().getFnName();
    }

    public LCExpr getExpr () {
        return this.expr;
    }

    public String getParamType() {
        return paramType;
    }

    public String getParam() {
        return param;
    }

    public boolean isApplyNot() {
        return applyNot;
    }



    public boolean eval ( ) throws Exception {
        try {
            expr.setup(smgr, event, cache);
            boolean v = expr.eval();
            boolean res = applyNot ? !v : v;
            return res;
        } catch (Exception ee) {
            System.out.println("Failed to test condition " + expr.toString());
            throw ee;
        }
    }

    // Returns info about the data the condition was tested on.  Called AFTER the eval method.
    String getConditionInfo () {
        return this.expr.getValInfo();
    }

    public String getRelop() {
        return expr.getRelop();
    }

    public String toString () {
        return (applyNot ? "NOT " : "") + expr.toString();
    }
}
