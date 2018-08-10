package edu.umass.ckc.servlet.servbase;




/** An action event can be built from any URL string that contains an "action" parameter.  This
 *  parameter is a unique keyword that the system uses to identify the semantics of the event.
 *  Eg. action=getProblem means that the request is to get a problem. */

public class ActionEvent extends ServletEvent {
    protected String action_;

    public ActionEvent () {}



    public ActionEvent(ServletParams p) throws Exception {
        super(p);
        try {
            setAction(p.getString("action"));
        } catch (Exception e) {

            throw new Exception("ActionEvent expects action parameter.  Got these params:" + p.toString());
        }

    }

    public void setAction(String action_) {
        this.action_ = action_;
    }

    public String getAction () { return action_; }
}