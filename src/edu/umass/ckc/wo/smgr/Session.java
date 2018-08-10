package edu.umass.ckc.wo.smgr;

import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: 9/12/11
 * Time: 10:16 AM
 * NOt really used except in a report that collects up a student's sessions.
 */
public class Session {
    private int id;
    int studId;
    Timestamp beginTime;
    Timestamp lastAccessTime;
    String clientType;

    public Session(int id, int studId, Timestamp beginTime, Timestamp lastAccessTime) {
        this.id = id;
        this.studId = studId;
        this.beginTime = beginTime;
        this.lastAccessTime = lastAccessTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudId() {
        return studId;
    }

    public void setStudId(int studId) {
        this.studId = studId;
    }

    public Timestamp getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Timestamp beginTime) {
        this.beginTime = beginTime;
    }

    public Timestamp getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(Timestamp lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }
}
