package edu.umass.ckc.wo.tutormeta;

import edu.umass.ckc.wo.beans.Topic;
import edu.umass.ckc.wo.content.Hint;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.content.TopicIntro;

import java.sql.SQLException;

/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Aug 24, 2005
 * Time: 6:20:48 PM
 */
public interface PedagogicalMoveListener {



    public void problemGiven (Problem p) throws SQLException;

    public void exampleGiven (Problem ex) throws SQLException;

    public void lessonIntroGiven (TopicIntro intro) throws SQLException;

    public void attemptGraded(boolean isCorrect);

    public void hintGiven(Hint hint) throws SQLException ;

    public void interventionGiven(Intervention intervention) throws SQLException ;

    public void newTopic (Topic t);

    public void newSession (int sessId) throws SQLException;
}
