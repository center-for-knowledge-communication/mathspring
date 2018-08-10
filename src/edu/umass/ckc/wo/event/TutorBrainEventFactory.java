package edu.umass.ckc.wo.event;


import edu.umass.ckc.wo.enumx.Actions;
import edu.umass.ckc.wo.event.tutorhut.LogoutEvent;
import edu.umass.ckc.wo.event.tutorhut.TopicDetailEvent;
import edu.umass.ckc.wo.exc.UnknownEventException;
import edu.umass.ckc.servlet.servbase.ActionEvent;
import edu.umass.ckc.servlet.servbase.ServletEvent;
import edu.umass.ckc.servlet.servbase.ServletParams;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


/**
 * Build events from the args to the servlet.
 */
public class TutorBrainEventFactory {


    public TutorBrainEventFactory() {
    }



    public ServletEvent buildEvent(ServletParams p, String tutorEventPackageName) throws Throwable {
        ActionEvent a = new ActionEvent(p);
        String action = a.getAction();


        if (action.equals(Actions.killOtherSessions))
            a = new KillSessionsEvent(p,false);
        else if (action.equals(Actions.killAllSessions))
            a = new KillSessionsEvent(p,true);

        else if (action.equals(Actions.logout))
            a = new LogoutEvent(p);
        else if (action.equals(Actions.endActivity))
            a = new EndActivityEvent(p);
        else if (action.equals(Actions.getActivity))
            a = new StudentActionEvent(p);
        else if (action.equals(Actions.adventureProblemSolved))
            a = new AdventurePSolvedEvent(p);
        else if (action.equals(Actions.replay))
            a = new ReplayHintEvent(p);



        else if (action.equals(Actions.getClasses))
            a = new GetClassesEvent(p);
        else if (action.equals(Actions.navigation))
            a = new NavigationEvent(p);
        else if (action.equals(Actions.setMFRResult))
            a = new SetMFRResultEvent(p);
        else if (action.equals(Actions.setMRResult))
            a = new SetMRResultEvent(p);
        else if (action.equals(Actions.setPreResult))
            a = new SetPrePostResultEvent(p);
        else if (action.equals(Actions.setPostResult))
            a = new SetPrePostResultEvent(p);
        else if (action.equals(Actions.removeSystemTestSessionData))
            a = new CleanOutSystemTestDataEvent(p);
        else if (action.equals(Actions.changeGroup))
            a = new ChangeGroupEvent(p);
        else if (action.equals(Actions.topicDetail))
            a = new TopicDetailEvent(p);
        else if (action.equals(Actions.saveComment))
            a = new SaveCommentEvent(p);
        else if (action.equals(Actions.flashDebugLogin))
            a = new FlashDebugLoginEvent(p);
        else if (action.equals(Actions.dbTest))
                   a = new DbTestEvent(p);
        else  {
            a = buildEventFromName(p, tutorEventPackageName);
            if (a != null)
                return a;
            else throw new UnknownEventException();
        }
        return a;    
    }

    public ActionEvent buildEventFromName(ServletParams p, String tutorEventPackageName) throws Throwable {
        String action = p.getString("action",null);
        // no action means we just go to the opening login page for a teacher
        if (action == null) {
            return null;
        }
        else {
            String name = "edu.umass.ckc.wo.event." +tutorEventPackageName+ "."+ action + "Event";
            Class c = Class.forName(name);
            if (c != null) {
                Constructor constr = c.getConstructor(ServletParams.class);
                try {
                    ActionEvent ev = (ActionEvent) constr.newInstance(p);
                    return ev;
                }
                catch (InvocationTargetException exc) {
                    throw exc.getCause();
                }
            }
            else return null;
        }
    }
}