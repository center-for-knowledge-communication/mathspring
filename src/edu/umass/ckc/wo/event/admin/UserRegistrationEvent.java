package edu.umass.ckc.wo.event.admin;

import ckc.servlet.servbase.ActionEvent;
import ckc.servlet.servbase.ServletParams;


/**
 * User: marshall
 * Date: Feb 11, 2004
 * Time: 6:11:03 PM  
 * 
 */
public abstract class UserRegistrationEvent extends ActionEvent {

    public static final String START_PAGE = "startPage";
    protected String startPage;

    public UserRegistrationEvent () {}

    public UserRegistrationEvent(ServletParams p)  throws Exception {
        super(p);
        setStartPage(p.getString(START_PAGE));
    }




    public String getStartPage() {
        return startPage;
    }

    public void setStartPage(String startPage) {
        this.startPage = startPage;
    }
}

