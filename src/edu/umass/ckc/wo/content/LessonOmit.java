package edu.umass.ckc.wo.content;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 8/4/14
 * Time: 3:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class LessonOmit {
    public enum type {standard, problem}
    private String stdId;
    private int probId;
    private int parentClust;
    private String parentStd;
    private type t;

    public LessonOmit(String stdId, int probId, int parentClust, String parentStd, type t) {
        this.stdId = stdId;
        this.probId = probId;
        this.parentClust = parentClust;
        this.parentStd = parentStd;
        this.t = t;
    }


}
