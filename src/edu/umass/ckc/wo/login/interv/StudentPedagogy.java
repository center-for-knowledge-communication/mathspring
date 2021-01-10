package edu.umass.ckc.wo.login.interv;

import java.sql.SQLException;
import java.util.Map;

import edu.umass.ckc.servlet.servbase.ServletParams;
import edu.umass.ckc.wo.db.DbPedagogy;
import edu.umass.ckc.wo.db.DbUser;
import edu.umass.ckc.wo.event.SessionEvent;
import edu.umass.ckc.wo.login.LoginParams;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.tutormeta.Intervention;

public class StudentPedagogy extends LoginInterventionSelector {
	
    private static final String JSP_NEW = "studentPedagogy_name.jsp";

	public StudentPedagogy(SessionManager smgr) throws SQLException {
		super(smgr);
	}
	

    public Intervention selectIntervention (SessionEvent e) throws Exception {
        long shownTime = this.interventionState.getTimeOfLastIntervention();
        if ( shownTime > 0)
            return null;
        else {
            super.selectIntervention(e);
        	String studentPedagogyUrl = JSP_NEW;
        	Map<Integer, String> lcprofile = DbPedagogy.getLCprofiles(smgr.getConnection());
            LoginIntervention li = new LoginIntervention(studentPedagogyUrl);
            li.setLCprofile(lcprofile);
        	return li;                    
        }
    }

    public LoginIntervention processInput (ServletParams params) throws Exception {
    	String learningCharacter = params.getString(LoginParams.LEARNING_CHARACTER);
    	if (learningCharacter == null)
    		learningCharacter = "2";
        int lcIntValue = Integer.parseInt(learningCharacter);
        DbUser.setStudentPedagogy(conn, smgr.getStudentId(), lcIntValue);
        return null;
    }



    public String f (SessionManager smgr) {
        return JSP_NEW;
    }
}