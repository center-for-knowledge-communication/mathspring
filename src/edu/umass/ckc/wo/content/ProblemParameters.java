package edu.umass.ckc.wo.content;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import edu.umass.ckc.wo.db.DbStudentProblemHistory;
import edu.umass.ckc.wo.state.StudentState;
import edu.umass.ckc.wo.tutor.response.ProblemResponse;
import net.sf.json.*;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Jess
 * Date: 10/9/14
 * Time: 11:54 AM
 * To change this template use File | Settings | File Templates.
 */



public class ProblemParameters {
    List<Binding> bindings;

    public ProblemParameters() {
        this.bindings = null;
    }

    public ProblemParameters(HashMap<String, ArrayList<String>> params) {
        bindings = new ArrayList<Binding>();
        for (int i = 0; i < params.entrySet().iterator().next().getValue().size(); ++i) {
            Binding b =  new Binding();
            for (String key : params.keySet()) {
                b.addKVPair(key, params.get(key).get(i));
            }
            bindings.add(b);
        }
    }

    public List<Binding> getBindings() {
        return bindings;
    }

    public Map<String, String> getRandomAssignment() {
        if (bindings == null || bindings.size() == 0) {
            return null;
        }
        Random randomGenerator = new Random();
        int randomIndex = randomGenerator.nextInt(bindings.size());
        return bindings.get(randomIndex).getMap();
    }

    private List<Binding> generateBindings(List<String> jsonSeenBindings) {
        List<Binding> usedBindings = new ArrayList<Binding>();
        for (String bind : jsonSeenBindings) {
            Binding b = new Binding();
            JSONObject jParams = (JSONObject) JSONSerializer.toJSON(bind);
            Iterator<String> keys = jParams.keys();
            while (keys.hasNext()) {
                String key = (String)keys.next();
                String param = jParams.get(key).toString();
                b.addKVPair(key, param);
            }
            usedBindings.add(b);
        }
        return usedBindings;
    }

    private List<Binding> getUnusedBindings(List<Binding> seenBindings) {
        List<Binding> unusedBindings = new ArrayList<Binding>();
        for (Binding binding : bindings) {
            int i = seenBindings.indexOf(binding);
            if (i == -1) {
                unusedBindings.add(binding);
            }
        }
        return unusedBindings;
    }


    public Binding getUnusedAssignment(int probId, int studId, Connection conn) throws SQLException {
        if (bindings == null || bindings.size() == 0 || conn == null) {
            return null;
        }
        Random randomGenerator = new Random();
        int randomIndex = -1;

        List<Binding> seenBindings = generateBindings(DbStudentProblemHistory.getSeenBindings(probId, studId, conn));
        List<Binding> unusedBindings = getUnusedBindings(seenBindings);
        // All have been used, so go back to the full set and pick a random one.
        if (unusedBindings.size() == 0) {
            randomIndex = randomGenerator.nextInt(bindings.size());
            Binding b = bindings.get(randomIndex);
            b.setPosition(randomIndex);  // remember the position of the binding to help select answers bindings later
            return b.copy(); // return a copy so users of it don't destroy the map
        }
        // select an unused binding at random
        else {
            randomIndex = randomGenerator.nextInt(unusedBindings.size());
            Binding chosenBinding = unusedBindings.get(randomIndex);
            int position = bindings.indexOf(chosenBinding);
            chosenBinding.setPosition(position);    // remember the position of the binding to help select answers bindings later
            return chosenBinding.copy(); // return a copy so users of it don't destroy the map
        }
    }

    public void addBindings(ProblemResponse r, int studId, Connection conn, StudentState state) throws SQLException {

        JSONObject rJson = r.getJSON();
        // We have already bound this problem
        if (r.getParams() != null) {
            return;
        }
        Problem p = r.getProblem();
        Binding unusedBinding = getUnusedAssignment(r.getProblem().getId(), studId, conn);
        saveAssignment(unusedBinding, state);
        // short answer problems need to save the possible answers in the student state too.
        if (p.isShortAnswer()) {
            ProblemAnswer bestAnswer= saveAnswerPossibilities(conn,state,p.getAnswers(),unusedBinding.getPosition());
           // add in the answer to the map using this fixed variable.  N.B. This alters the map and thats why we use a copy
            // so that the next time the Best_answer isn't already there.

            unusedBinding.addKVPair("$Best_Answer",bestAnswer.getVal());
        }
        JSONObject pJson = unusedBinding.getJSON(new JSONObject());
        r.setParams(pJson.toString());
        rJson.element("parameters", pJson);

    }

    public Binding addBindings2(Problem p, int studId, Connection conn, StudentState state) throws SQLException {

//        JSONObject rJson = r.getJSON();
//        // We have already bound this problem
//        if (r.getParams() != null) {
//            return;
//        }

        Binding unusedBinding = getUnusedAssignment(p.getId(), studId, conn);
        saveAssignment(unusedBinding, state);
        // short answer problems need to save the possible answers in the student state too.
        if (p.isShortAnswer()) {
            ProblemAnswer bestAnswer= saveAnswerPossibilities(conn,state,p.getAnswers(),unusedBinding.getPosition());
            // add in the answer to the map using this fixed variable.  N.B. This alters the map and thats why we use a copy
            // so that the next time the Best_answer isn't already there.

            unusedBinding.addKVPair("$Best_Answer",bestAnswer.getVal());
        }
        return unusedBinding;
//        JSONObject pJson = unusedBinding.getJSON(new JSONObject());
//        r.setParams(pJson.toString());
//        rJson.element("parameters", pJson);

    }



    // short answer problems that are parameterized need to save the appropriate set of possible answers into the student state.
    // WHen the variable bindings are selected prior to calling this method, the index of the selected binding is kept and passed to this so that
    // we can then select the possible answers that have the same index (the bindingPosition in the ProblemAnswer table)
    // Note:  This returns the ProblemAnswer that is best (i.e. the one with the lowest order setting)
    private ProblemAnswer saveAnswerPossibilities(Connection conn, StudentState state, List<ProblemAnswer> answers, int position) throws SQLException {
        List<String> possibleAnswers = new ArrayList<String>();
        // the list of ALL answers as defined for the problem.   We now go through them and get the ones that are for the given
        // position (i.e. those that correspond with the position of the variable bindings that were just selected
        int min = 100;
        ProblemAnswer best=null;
        for (ProblemAnswer a: answers) {
            if (a.getBindingNumber() == position)  {
                possibleAnswers.add(a.getVal());
                if (a.getOrder() < min) {
                    best = a;
                    min= a.getOrder();
                }

            }
        }
        state.setPossibleShortAnswers(possibleAnswers);
        return best;
    }

    private void saveAssignment(Binding b, StudentState state) throws SQLException {
        state.setProblemBinding(b.toString());
    }

    public boolean hasUnusedParametrization(int timesEncountered) {
        if (bindings != null && bindings.size() > timesEncountered) {
            return true;
        }
        return false;
    }


    public JSONObject getJSON(JSONObject jo, Map<String, String> bindings) {
        for(Map.Entry<String, String> entry : bindings.entrySet()){
            jo.element(entry.getKey(), entry.getValue());
        }
        return jo;
    }

    public static void main(String[] args) {
        String jsonString = "{\n" +
                "  \"$a\": [\"40\", \"40\"],\n" +
                "  \"$b\": [\"30\", \"30\"],\n" +
                "  \"$c\": [\"x\", \"45\"],\n" +
                "  \"$d\": [\"25\", \"x\"],\n" +
                "  \"$ans_A\": [\"65\", \"65\"],\n" +
                "  \"$ans_B\": [\"45\", \"45\"],\n" +
                "  \"$ans_C\": [\"50\", \"50\"],\n" +
                "  \"$ans_D\": [\"35\", \"35\"],\n" +
                "  \"$ans_E\": [\"45\", \"25\"]\n" +
                "}";

        String usedBinding = "{\n" +
                "\"$a\": \"40\",\n" +
                "\"$b\":\"30\",\n" +
                "\"$c\": \"x\",\n" +
                "\"$d\": \"25\",\n" +
                "\"$ans_A\": \"65\",\n" +
                "\"$ans_B\": \"45\",\n" +
                "\"$ans_C\": \"50\",\n" +
                "\"$ans_D\": \"35\",\n" +
                "\"$ans_E\": \"45\"\n" +
                "}";
//        List<String> bindingStrings = new ArrayList<String>();
//        bindingStrings.add(usedBinding);
//        ProblemParameters parameters = new ProblemParameters(jsonString);
//        List<Binding> b = parameters.generateBindings(bindingStrings);
//        System.out.println(parameters.getUnusedBindings(b));
    }
}
