package edu.umass.ckc.wo.tutormeta;
import edu.umass.ckc.wo.event.StudentActionEvent;
import edu.umass.ckc.wo.event.EndActivityEvent;
import edu.umass.ckc.wo.tutor.response.Response;


public abstract class OldPedagogicalModel {
    protected StudentModel studentModel;
    protected ProblemSelector problemSelector ;// problem selection is a pluggable strategy
    protected HintSelector hintSelector;  // hint selection is a pluggable strategy
    protected InterventionSelector interventionSelector; //
    protected LearningCompanion learningCompanion ;

    public OldPedagogicalModel()
    {}

    public OldPedagogicalModel(StudentModel sm) {
      studentModel = sm;
    }
    public OldPedagogicalModel(StudentModel sm, ProblemSelector probSel, HintSelector hintSel) {
        this(sm,probSel,hintSel,null,null);
    }

    public OldPedagogicalModel(StudentModel sm, ProblemSelector probSel, HintSelector hintSel, InterventionSelector intervSel) {
      this(sm,probSel,hintSel,intervSel,null);

    }

    public OldPedagogicalModel(StudentModel sm, ProblemSelector probSel, HintSelector hintSel,
                             InterventionSelector intervSel, LearningCompanion learningComp) {
      this(sm);
      problemSelector = probSel;
      hintSelector = hintSel;
      interventionSelector = intervSel;
      learningCompanion = learningComp;
    }

    public abstract Activity getActivity(StudentActionEvent e) throws Exception;

    public abstract String endActivity (EndActivityEvent e) throws Exception;
}