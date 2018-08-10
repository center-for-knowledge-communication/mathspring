package edu.umass.ckc.wo.content;

import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.state.StudentState;
import net.sf.json.JSONObject;

import java.sql.SQLException;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 11/11/15
 * Time: 4:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProblemBinding {

    private Problem problem;
    private Binding binding;
    private String oldAnswer;
    private String newAnswer;

    public ProblemBinding(Problem problem) {
        this.problem = problem;
    }

    public void setBindings(SessionManager smgr) throws SQLException {

        if (problem != null && problem.isParametrized()) {
            binding = problem.getParams().addBindings2(problem, smgr.getStudentId(), smgr.getConnection(), smgr.getStudentState());
            // parameterized short answer problems need to save the possible answers in the student state
        }
    }


    public JSONObject getJSON() {
        if (binding != null) {
            JSONObject jo = new JSONObject();
            binding.getJSON(jo);
            return jo;
        }
        else return null;
    }
}
