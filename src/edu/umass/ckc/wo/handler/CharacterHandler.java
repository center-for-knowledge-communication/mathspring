package edu.umass.ckc.wo.handler;

import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.event.tutorhut.ClickCharacterEvent;
import edu.umass.ckc.wo.event.tutorhut.EliminateCharacterEvent;
import edu.umass.ckc.wo.event.tutorhut.MuteCharacterEvent;
import ckc.servlet.servbase.View;
import edu.umass.ckc.wo.tutor.hintSel.StrategicHintSelector;
import edu.umass.ckc.wo.tutor.response.HintResponse;
import edu.umass.ckc.wo.tutor.response.Response;
import edu.umass.ckc.wo.tutor.TransactionLogger;
import edu.umass.ckc.wo.content.Hint;

import javax.servlet.ServletContext;
import java.sql.Connection;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: Feb 18, 2009
 * Time: 11:50:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class CharacterHandler {

    Connection conn;
    SessionManager smgr;
    public CharacterHandler(ServletContext servletContext, SessionManager smgr, Connection conn) {
        this.smgr = smgr;
        this.conn = conn;
    }

    /**
     * When a character is clicked, we want to give the strategic hint and log the event.
     * If the problem has no strategic hint, return a hint with label= noStrategicHint.  If
     * the strategic hint has already played, return a hint with label=strategicAlreadyPlayed
     * @param event
     * @return
     */
    public View handleRequest(ClickCharacterEvent event) throws Exception {
        // if the strategic hint has already been shown, return  strategicAlreadyPlayed
        if (smgr.getStudentState().isStrategicHintShown()) {
            Hint h = Hint.STRATEGIC_HINT_PLAYED;
            h.setProblemId(smgr.getStudentState().getCurProblem());
            HintResponse hr = new HintResponse(h);
            TransactionLogger.logClickCharacter(conn, event, hr, smgr.getStudentId(),smgr.getSessionNum());
            return hr.getView();

        }
        else {
            StrategicHintSelector hs= new StrategicHintSelector();
            hs.init(smgr);
            Hint h = hs.selectStrategicHint(smgr,smgr.getStudentState().getCurProblem());
            // return the hint (its either noStrategicHint or strategic_hint)
            HintResponse hr = new HintResponse(h);
            TransactionLogger.logClickCharacter(conn, event, hr, smgr.getStudentId(),smgr.getSessionNum());
            return hr.getView();
        }
    }


    /**
     * When the "go away" button is clicked, we just want to log the event
     * @param event
     * @return
     */
    public View handleRequest(EliminateCharacterEvent event) throws Exception {
        TransactionLogger.logEliminateCharacter(conn, event,smgr.getStudentId(),smgr.getSessionNum(), 
                smgr.getStudentState().getCurProblem());
        return new Response().getView();
    }

    public View handleRequest(MuteCharacterEvent event) throws Exception {
        TransactionLogger.logMuteCharacter(conn, event,smgr.getStudentId(),smgr.getSessionNum(), 
                smgr.getStudentState().getCurProblem());
        return new Response().getView();
    }


}
