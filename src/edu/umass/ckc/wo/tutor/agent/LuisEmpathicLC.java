package edu.umass.ckc.wo.tutor.agent;

import edu.umass.ckc.wo.smgr.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: ivon
 * Date: Jul 24, 2008
 * Time: 11:08:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class LuisEmpathicLC extends FullEmpathicLC {


    public LuisEmpathicLC(SessionManager smgr) {}

    public String getGender() {
        return "male" ;
    }

    public String getEthnicity() {
        return "latino" ;
    }

    public String getCharactersName() {
        return "Luis" ;
    }



}
