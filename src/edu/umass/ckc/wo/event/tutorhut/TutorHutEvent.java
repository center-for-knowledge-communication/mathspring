package edu.umass.ckc.wo.event.tutorhut;

import edu.umass.ckc.wo.event.SessionEvent;
import edu.umass.ckc.servlet.servbase.ServletParams;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Only used to add elapsedTime to a SessionEvent.
 * User: marshall
 * Date: Nov 25, 2008
 * Time: 9:55:44 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class TutorHutEvent extends SessionEvent {
    protected long elapsedTime;
    protected long clickTime; //  timestamp given by Javascript (ms since Jan 1, 1970 midnight)
    protected static String ELAPSED_TIME = "elapsedTime";
    protected int eventCounter;
    protected int langIndex;

    protected HttpServletResponse servletResponse;  // THis is so the Tutor can forward to JSPs
    protected HttpServletRequest servletRequest;  // THis is so the Tutor can forward to JSPs


    TutorHutEvent() {}



    TutorHutEvent(ServletParams p) throws Exception {
        super(p);
        String et = p.getString(ELAPSED_TIME,"0");
        this.eventCounter = p.getInt("eventCounter",-1);
        this.clickTime = p.getLong("clickTime",0);
        this.langIndex = p.getInt("langIndex",0);
        long etl=0;
        try {
            etl = Long.parseLong(et);
        } catch (Exception e) {
            etl=0;
        }
        elapsedTime = etl;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public HttpServletResponse getServletResponse() {
        return servletResponse;
    }

    public void setServletResponse(HttpServletResponse servletResponse) {
        this.servletResponse = servletResponse;
    }

    public HttpServletRequest getServletRequest() {
        return servletRequest;
    }

    public void setServletRequest(HttpServletRequest servletRequest) {
        this.servletRequest = servletRequest;
    }

    public int getEventCounter() {
        return eventCounter;
    }

    public long getClickTime() {
        return clickTime;
    }

    public long getLangIndex() {
        return langIndex;
    }

}
