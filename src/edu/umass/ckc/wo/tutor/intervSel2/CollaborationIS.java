package edu.umass.ckc.wo.tutor.intervSel2;

import edu.umass.ckc.wo.collab.CollaborationManager;
import edu.umass.ckc.wo.collab.CollaborationState;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.event.tutorhut.InputResponseNextProblemInterventionEvent;
import edu.umass.ckc.wo.event.tutorhut.NextProblemEvent;
import edu.umass.ckc.wo.event.tutorhut.InterventionTimeoutEvent;
import edu.umass.ckc.wo.interventions.NextProblemIntervention;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.tutor.pedModel.PedagogicalModel;
import edu.umass.ckc.wo.tutor.response.Response;
import edu.umass.ckc.wo.tutormeta.Intervention;
import edu.umass.ckc.wo.util.State;
import edu.umass.ckc.wo.util.WoProps;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Melissa
 * Date: 2/2/15
 * Time: 7:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class CollaborationIS extends NextProblemInterventionSelector {
    //Stores state data, such as point in collaboration, time since collaboration
    private CollaborationState state;

    private static Logger logger = Logger.getLogger(CollaborationIS.class);

    public CollaborationIS(SessionManager smgr) throws SQLException {
        super(smgr);
        state = CollaborationManager.getCollaborationState(smgr);
    }

    public void init(SessionManager smgr, PedagogicalModel pedagogicalModel){
        this.pedagogicalModel = pedagogicalModel;
        configure();
    }

    private void configure() {
        state.setMaxPartnerWaitPeriod((long)stringToInt(getConfigParameter2("partnerWaitSeconds"), 15)*1000);
        state.setTimeInterval((long)stringToInt(getConfigParameter2("collaborationIntervalMinutes"), 5)*1000*60);
        state.setProbInterval(stringToInt(getConfigParameter2("collaborationIntervalProblems"), Integer.MAX_VALUE));

        /* //These config options were experimental. If they do get used, here's how to read them.
        Element config = getConfigXML();
        if(config != null) {
            for(Element e : (List<Element>) config.getChildren("intervalCriteria")) {
                String type = e.getAttributeValue("type");
                if(type.equals("affect")) {
                    String emotion = e.getAttributeValue("emotion");
                    int lowerBound = stringToInt(e.getAttributeValue("lowerBound"), 1);
                    int upperBound = stringToInt(e.getAttributeValue("upperBound"), 5);
                    //This needs to be hooked up to something when/if we decide to use it
                } else if(type.equals("random")) {
                    int frequency = stringToInt(e.getAttributeValue("frequency"), -1);
                    //This needs to be hooked up to something when/if we decide to use it
                    //Not sure what it was even supposed to be for?
                }
            }
        }
        */
    }

    private int stringToInt(String str, int default_value) {
        if(str == null) return default_value;
        else return Integer.parseInt(str);
    }

    public static Intervention tryGettingPartnerIS(SessionManager smgr, PedagogicalModel pedagogicalModel) throws Exception {
        // See if this student has been requested as a partner for some other student who needs help
        Integer partner = CollaborationManager.checkForRequestingPartner(smgr.getStudentId());
        if(partner != null){ //Another student is waiting for their help; set this one up as their partner
            CollaborationPartnerIS partnerIS = new CollaborationPartnerIS(smgr);
            partnerIS.init(smgr, pedagogicalModel);
            // tells the helper to work with the person next to them (on the other computer)
            // and locks their screen until they complete the problem together.
            return partnerIS.selectInterventionWithId(partner);
        }
        return null;
    }

    public NextProblemIntervention selectIntervention(NextProblemEvent e) throws Exception{
        // If they can't collaborate, don't bother checking anything else
        if(!CollaborationManager.canCollaborate(smgr)) return null;

        //They're going into either partner or originator for sure now, so trigger the cooldown
        state.triggerCooldown();
        state.setNumProblemsSinceLastIntervention(0);

        Intervention interv = tryGettingPartnerIS(smgr, pedagogicalModel);
        if(interv != null) //There was a student waiting on this one as a partner
            return (NextProblemIntervention)interv;
        else { //There were no students waiting on this one, so make this student originate a collaboration
            CollaborationOriginatorIS originatorIS = new CollaborationOriginatorIS(smgr);
            originatorIS.init(smgr, pedagogicalModel);

            return originatorIS.selectIntervention(e);
        }
    }

    public Response processInputResponseNextProblemInterventionEvent(InputResponseNextProblemInterventionEvent e) throws Exception{
        // The new intervention model does not allow one intervention selector to delegate to another intervention selector as done by this
        // implementation (because constructing the intervention selector using the name of the last intervention selector needs an intervention spec
        // object which is only created if an intervention selector is placed in pedagogies.xml.   Since these delegates don't have entries in pedagogies.xml
        // they cannot be constructed because no InterventionSpec exists).   So I make all interventions pass back the intervention selector to which
        // the input response should go.  e.g.  destination=edu.umass.ckc.wo.tutor.intervSel2.CollaborationOriginatorIS
        String dest = e.getServletParams().getString("destination");
        Class c = Class.forName(dest);
        NextProblemInterventionSelector is =  (NextProblemInterventionSelector) c.getConstructor(SessionManager.class).newInstance(smgr);
        is.init(smgr,pedagogicalModel);
        return is.processInputResponseNextProblemInterventionEvent(e);
    }

    @Override
    public Intervention processInterventionTimeoutEvent(InterventionTimeoutEvent e) throws Exception {
        String dest = e.getServletParams().getString("destination");
        Class c = Class.forName(dest);
        NextProblemInterventionSelector is =  (NextProblemInterventionSelector) c.getConstructor(SessionManager.class).newInstance(smgr);
        is.init(smgr,pedagogicalModel);
        return is.processInterventionTimeoutEvent(e);
//        return ((NextProblemInterventionSelector) getInterventionSelectorThatGeneratedIntervention()).processTimedInterventionEvent(e);
    }

    @Override
    public void problemGiven(Problem p) throws SQLException {
        state.setNumProblemsSinceLastIntervention(state.getNumProblemsSinceLastIntervention() + 1);
    }

    @Override
    public void newSession (int sessionId) throws SQLException {
        state.setTimeOfLastIntervention(System.currentTimeMillis());
    }
}