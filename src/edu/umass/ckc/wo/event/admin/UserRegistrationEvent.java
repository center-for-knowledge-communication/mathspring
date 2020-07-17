package edu.umass.ckc.wo.event.admin;

import edu.umass.ckc.servlet.servbase.ActionEvent;
import edu.umass.ckc.servlet.servbase.ServletParams;


/**
 * User: marshall
 * Date: Feb 11, 2004
 * Time: 6:11:03 PM  
 * 
 * Frank	07-17-20	Issue #122 modified UserRegistration events for classId parameter
 */
public abstract class UserRegistrationEvent extends ActionEvent {

    public static final String START_PAGE = "startPage";
    public static final String CLASS_ID = "classId";
    private String startPage;
    private String classId;
    private int intClassId;

    public UserRegistrationEvent () {}

    public UserRegistrationEvent(ServletParams p)  throws Exception {
        super(p);
        setStartPage(p.getString(START_PAGE));
        setClassId(p.getString(CLASS_ID));
        if ((classId == null) || classId.length() == 0) {
        	setIntClassId(0);        	
        }
        else {
        	setIntClassId(Integer.valueOf(classId));
        }
    }

    public String getStartPage() {
        return startPage;
    }

    public void setStartPage(String startPage) {
        this.startPage = startPage;
    }
    
    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public void setIntClassId(int classId) {
        this.intClassId = classId;
    }
    
    public int getIntClassId() {
        return (this.intClassId);
    }

    
}

