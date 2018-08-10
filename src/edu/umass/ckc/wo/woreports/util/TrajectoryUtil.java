package edu.umass.ckc.wo.woreports.util;

import edu.umass.ckc.wo.content.Problem;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: 11/15/12
 * Time: 12:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class TrajectoryUtil {


    // This is given the id of the entry in the eventHistory.   We then go into the userEvents list (which is sorted in order of
    // event occurrence) and find the row just prior to this event which says what type of problem we are working with.
    // This is the activityName and is usually found on a BeginProblem or NextProblem event
    public static String getProblemType(List<EventLogEntry> userEvents, int eventId) {
        int index=-1; boolean found=false;
        int i=0;
        for (EventLogEntry entry : userEvents) {
            if (entry.id == eventId) {
                found=true;
                break;
            }
            else i++;
        }
        if (found) {
            index = i;   // this is the index into the userEvents list where the current event is found
            // now search backwards looking for an event that has activityName of ExampleProblem, PracticeProblem, TopicIntro
            while (index != 0) {
                index--;
                String activityName = userEvents.get(index).activityName;
                if (activityName.equalsIgnoreCase("PracticeProblem"))
                    return "PracticeProblem";
                else if (activityName.equalsIgnoreCase("ExampleProblem"))
                    return "ExampleProblem";
                else if (activityName.equalsIgnoreCase(Problem.TOPIC_INTRO_PROB_TYPE))
                    return Problem.TOPIC_INTRO_PROB_TYPE;
            }
            // if we get back to index 0 then we are probably at a topic intro so return that
            return Problem.TOPIC_INTRO_PROB_TYPE;
        }
        else return "PracticeProblem";  // failing to find problem, assume its practice
    }

}
