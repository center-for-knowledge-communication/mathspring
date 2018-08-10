package edu.umass.ckc.wo.content;

/**  Reinforcement are spectial kinds of hints that are used to react to students' solving the problem correctly.
 *   It maybe some kind of reinforcing statement or reminder of what skill they used correctly, etc */

public class Reinforcement extends Hint {

    public Reinforcement(int id, String label, int problemId) throws Exception {
        super(id,label,problemId,false);
    }

    public String getXML () {
        return "<action type=\"reinforcement\">" + getLabel() + "</action>";
    }

    public String getFlashOut () {
      return "&action_type=reinforcement\n&reinforcement=" + getLabel() + "\n";
    }
}