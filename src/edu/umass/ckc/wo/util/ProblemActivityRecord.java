package edu.umass.ckc.wo.util;

/**
 * Created by IntelliJ IDEA.
 * User: ivon
 * Date: Mar 11, 2009
 * Time: 5:29:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProblemActivityRecord {

    int attemptsMade ;
    int hintsSeen ;
    boolean abusedHint ;
    boolean quickGuessed ;

    public ProblemActivityRecord() {
        this.attemptsMade = 0 ;
        this.hintsSeen = 0 ;
        this.quickGuessed = false ;
        this.abusedHint = false ;
    }

    public int getAttemptsMade() {
        return attemptsMade;
    }

    public void setAttemptsMade(int attemptsMade) {
        this.attemptsMade = attemptsMade;
    }

    public int getHintsSeen() {
        return hintsSeen;
    }

    public void setHintsSeen(int hintsSeen) {
        this.hintsSeen = hintsSeen;
    }

    public boolean isAbusedHint() {
        return abusedHint;
    }

    public void setAbusedHint(boolean abusedHint) {
        this.abusedHint = abusedHint;
    }

    public boolean isQuickGuessed() {
        return quickGuessed;
    }

    public void setQuickGuessed(boolean quickGuessed) {
        this.quickGuessed = quickGuessed;
    }
}
