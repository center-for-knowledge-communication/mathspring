package edu.umass.ckc.wo.assistments;

/**
 * A user who comes from another system like Assistments or MARI
 * User: marshall
 * Date: 9/13/13
 * Time: 11:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class CoopUser {
    private String uid;
    private String token;
    private int studId;


    public CoopUser(String uid, String token, int studId) {
        this.uid = uid;
        this.token = token;
        this.studId = studId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getStudId() {
        return studId;
    }

    public void setStudId(int studId) {
        this.studId = studId;
    }

}
