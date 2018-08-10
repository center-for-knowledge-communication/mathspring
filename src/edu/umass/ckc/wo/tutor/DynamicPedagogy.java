package edu.umass.ckc.wo.tutor;

import edu.umass.ckc.wo.tutormeta.Switcher;
import org.jdom.Element;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Melissa
 * Date: 9/15/15
 * Time: 1:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class DynamicPedagogy extends Pedagogy{

    private ArrayList<String> problemSelectors = new ArrayList<String>();
    private ArrayList<String> learningCompanions = new ArrayList<String>();
    private ArrayList<String> hintSelectors = new ArrayList<String>();
    private ArrayList<String> studentModels = new ArrayList<String>();
    private ArrayList<String> reviewModeSelectors = new ArrayList<String>();
    private ArrayList<String> challengeModeSelectors = new ArrayList<String>();
    private Element switchers;
    private Random switcher = new Random();

    public String changeProblemSelectorClass() {
        if(problemSelectors.size() <= 1)
            return null;
        else{
            boolean sameElement = true;
            int target = 0;
            while(sameElement){
                target = switcher.nextInt(problemSelectors.size());
                String candidate = getFullyQualifiedClassname(defaultClasspath+".probSel",problemSelectors.get(target));
                if(candidate.equals(this.getProblemSelectorClass())){
                    sameElement = true;
                }
                else{
                    this.setProblemSelectorClass(problemSelectors.get(target));
                    sameElement = false;
                }
            }
            return problemSelectors.get(target);
        }
    }

    public void addProblemSelectorClass(String problemSelectorClass) {
        problemSelectors.add(problemSelectorClass);
        if(this.getProblemSelectorClass() == null){
            this.setProblemSelectorClass(problemSelectorClass);
        }
    }

    public String changeLearningCompanionClass() {
        if(learningCompanions.size() <= 1)
            return null;
        else{
            boolean sameElement = true;
            int target = 0;
            while(sameElement){
                target = switcher.nextInt(learningCompanions.size());
                String candidate = getFullyQualifiedClassname(defaultClasspath + ".probSel", learningCompanions.get(target));
                if(candidate.equals(this.getLearningCompanionClass())){
                    sameElement = true;
                }
                else{
                    this.setLearningCompanionClass(learningCompanions.get(target));
                    sameElement = false;
                }
            }
            return learningCompanions.get(target);
        }
    }

    public void addLearningCompanionClass(String learningCompanionClass) {
        learningCompanions.add(learningCompanionClass);
        if(this.getLearningCompanionClass() == null){
            this.setLearningCompanionClass(learningCompanionClass);
        }
    }

    public String changeHintSelectorClass() {
        if(hintSelectors.size() <= 1)
            return null;
        else{
            boolean sameElement = true;
            int target = 0;
            while(sameElement){
                target = switcher.nextInt(hintSelectors.size());
                String candidate = getFullyQualifiedClassname(defaultClasspath+".probSel",hintSelectors.get(target));
                if(candidate.equals(this.getHintSelectorClass())){
                    sameElement = true;
                }
                else{
                    this.setHintSelectorClass(hintSelectors.get(target));
                    sameElement = false;
                }
            }
            return hintSelectors.get(target);
        }
    }

    public void addHintSelectorClass(String hintSelectorClass) {
        hintSelectors.add(hintSelectorClass);
        if(this.getHintSelectorClass() == null){
            this.setHintSelectorClass(hintSelectorClass);
        }
    }

    public String changeStudentModelClass() {
        if(studentModels.size() <= 1)
            return null;
        else{
            boolean sameElement = true;
            int target = 0;
            while(sameElement){
                target = switcher.nextInt(studentModels.size());
                String candidate = getFullyQualifiedClassname(defaultClasspath + ".probSel", studentModels.get(target));
                if(candidate.equals(this.getStudentModelClass())){
                    sameElement = true;
                }
                else{
                    this.setStudentModelClass(studentModels.get(target));
                    sameElement = false;
                }
            }
            return studentModels.get(target);
        }
    }

    public void addStudentModelClass(String studentModelClass) {
        studentModels.add(studentModelClass);
        if(this.getStudentModelClass() == null){
            this.setStudentModelClass(studentModelClass);
        }
    }

    public void addReviewModeProblemSelectorClass(String reviewModeProblemSelectorClass) {
        reviewModeSelectors.add(reviewModeProblemSelectorClass);
        if(this.getReviewModeProblemSelectorClass() == null){
            this.setReviewModeProblemSelectorClass(reviewModeProblemSelectorClass);
        }
    }

    public String changeReviewModeProblemSelectorClass() {
        if(reviewModeSelectors.size() <=1){
            return null;
        }
        else{
            boolean sameElement = true;
            int target = 0;
            while(sameElement){
                target = switcher.nextInt(reviewModeSelectors.size());
                String candidate = getFullyQualifiedClassname(defaultClasspath + ".probSel", reviewModeSelectors.get(target));
                if(candidate.equals(this.getReviewModeProblemSelectorClass())){
                    sameElement = true;
                }
                else{
                    this.setReviewModeProblemSelectorClass(reviewModeSelectors.get(target));
                    sameElement = false;
                }
            }
            return reviewModeSelectors.get(target);
        }
    }

    public void addChallengeModeProblemSelectorClass(String challengeModeProblemSelectorClass) {
        challengeModeSelectors.add(challengeModeProblemSelectorClass);
        if(this.getChallengeModeProblemSelectorClass() == null){
            this.setChallengeModeProblemSelectorClass(challengeModeProblemSelectorClass);
        }
    }

    public String changeChallengeModeProblemSelectorClass() {
        if(challengeModeSelectors.size() <= 1)
            return null;
        else{
            boolean sameElement = true;
            int target = 0;
            while(sameElement){
                target = switcher.nextInt(challengeModeSelectors.size());
                String candidate = getFullyQualifiedClassname(defaultClasspath + ".probSel", challengeModeSelectors.get(target));
                if(candidate.equals(this.getChallengeModeProblemSelectorClass())){
                    sameElement = true;
                }
                else{
                    this.setChallengeModeProblemSelectorClass(challengeModeSelectors.get(target));
                    sameElement = false;
                }
            }
            return challengeModeSelectors.get(target);
        }
    }

    public Element getSwitchers(){
        return switchers;
    }

    public void setSwitchersElement(Element e) {
        switchers = e;
    }
}
