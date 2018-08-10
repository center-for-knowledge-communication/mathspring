package edu.umass.ckc.wo.strat;

import edu.umass.ckc.wo.lc.LCRuleset;
import edu.umass.ckc.wo.tutor.Pedagogy;
import edu.umass.ckc.wo.tutormeta.LearningCompanion;

import java.util.List;

/**
 * Created by marshall on 6/14/17.
 * Making it be a subclass of Pedagogy so that all places where Pedagogy is used can accept this class.
 */
public class TutorStrategy extends Pedagogy {


    private int genericStrategyId;
    private String name;
    private ClassStrategyComponent login_sc;
    private ClassStrategyComponent lesson_sc;
    private ClassStrategyComponent tutor_sc;
    private int lcid;
    private LC lc;




    public int getGenericStrategyId() {
        return genericStrategyId;
    }

    public void setGenericStrategyId(int genericStrategyId) {
        this.genericStrategyId = genericStrategyId;
    }

    public ClassStrategyComponent getLogin_sc() {
        return login_sc;
    }

    public void setLogin_sc(ClassStrategyComponent login_sc) {
        this.login_sc = login_sc;
    }

    public ClassStrategyComponent getLesson_sc() {
        return lesson_sc;
    }

    public void setLesson_sc(ClassStrategyComponent lesson_sc) {
        this.lesson_sc = lesson_sc;
    }

    public ClassStrategyComponent getTutor_sc() {
        return tutor_sc;
    }

    public void setTutor_sc(ClassStrategyComponent tutor_sc) {
        this.tutor_sc = tutor_sc;
    }





    public String getLearningCompanionCharacter () {
        return lc.getCharacter();
    }

    public int getLcid() {
        return lcid;
    }

    public void setLcid(int lcid) {
        this.lcid = lcid;
    }

    public LC getLC () {
        return this.lc;
    }

    public void setLc(LC lc) {
        this.lc = lc;
    }

    public String getLearningCompanionClass () {
        if (this.lc != null)
            return this.lc.getClassName();
        else return null;
    }


    public String getPedagogicalModelClass () {
        return tutor_sc.getParameterValue("pedagogicalModelClass", TutorSCParamDefaults.PEDAGOGICAL_MODEL);
    }

    public String getStudentModelClass () {
        return tutor_sc.getParameterValue("studentModelClass", TutorSCParamDefaults.STUDENT_MODEL);
    }

    public String getProblemSelectorClass () {

        return this.tutor_sc.getParameterValue("problemSelectorClass", TutorSCParamDefaults.PROBLEM_SELECTOR );
    }

    public String getReviewModeProblemSelectorClass() {
        return this.tutor_sc.getParameterValue("reviewModeProblemSelectorClass",TutorSCParamDefaults.REVIEW_PROBLEM_SELECTOR);
    }

    public String getChallengeModeProblemSelectorClass() {
        return this.tutor_sc.getParameterValue("challengeModeProblemSelectorClass", TutorSCParamDefaults.CHALLENGE_PROBLEM_SELECTOR);
    }

    public String getHintSelectorClass() {
        return this.tutor_sc.getParameterValue("hintSelectorClass", TutorSCParamDefaults.HINT_SELECTOR_SELECTOR);
    }

    public boolean hasRuleset () {
        if (this.lc != null)
            return this.lc.getRulesets() != null;
        return false;
    }

    public List<LCRuleset> getLearningCompanionRuleSets () {
        return lc.getRulesets();
    }

    public String toString(){
        return "TutorStrategy:" + id + ": " + name + "\n" +
                "\t" + (login_sc != null ? login_sc.toString() : "") + "\n" +
                "\t" + (lesson_sc != null ? lesson_sc.toString() : "")+ "\n" +
                "\t" + (tutor_sc != null ? tutor_sc.toString() : "");
    }
}
