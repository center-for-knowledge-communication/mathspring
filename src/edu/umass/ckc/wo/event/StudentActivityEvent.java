package edu.umass.ckc.wo.event;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: Oct 6, 2008
 * Time: 11:10:57 AM
 * To change this template use File | Settings | File Templates.
 */
public interface StudentActivityEvent {
    long getElapsedTime();

    long getProbElapsed();

    int getProbId();

    String getUserInput();
    void setUserInput(String input);

    boolean isCorrect();

    boolean isSolved();
}
