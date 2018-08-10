package edu.umass.ckc.wo.db;

import edu.umass.ckc.wo.content.ExternalActivity;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: 8/22/12
 * Time: 2:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class DbExternalActivity {
    
    
    public static List<Integer> getActivitiesForTopic (Connection conn, int topicId) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        List res = new ArrayList<Integer>();
        try {
            String q = "select t.xactid from externalactivitytopic t,externalactivity a where t.topicid=? and t.xactid=a.id and a.ready=1";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,topicId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int c= rs.getInt(1);
                res.add(c);
            }
            return res;
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        } 
    }

    public static List<Integer> getActivitiesForStudent (Connection conn, int studId) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        List res = new ArrayList<Integer>();
        try {
            String q = "select auxid from eventlog where studId=? and action='BeginExternalActivity'";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,studId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int c= rs.getInt(1);
                res.add(c);
            }
            return res;
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }


    // Get external activities within the topic that has not been shown to the student before (look in the event log to find
    // BeginExternalActivity events with that xactID
    public static List<ExternalActivity> getActivitiesForStudent (Connection conn, int topicId, int studId) throws SQLException {
       ResultSet rs=null;
       PreparedStatement stmt=null;
       try {
           List<ExternalActivity> l = new ArrayList<ExternalActivity>();
           String q = "select a.id,a.name,a.description,a.url,a.creator,a.lastModifier,a.ready,a.instructions,a.creationtime " +
                   "from externalactivity a, externalactivitytopic t where a.id=t.xactid and t.topicId=? and a.ready=1 and " +
                   "t.xactid not in (select problemid from eventlog where studid=? and " +
                   "(action='BeginExternalActivity'))";
           stmt = conn.prepareStatement(q);
           stmt.setInt(1,topicId);
           stmt.setInt(2,studId);
           rs = stmt.executeQuery();
           while (rs.next()) {
               int id= rs.getInt(1);
               String name= rs.getString(2);
               String descr = rs.getString(3);
               String url = rs.getString(4);
               String crea = rs.getString(5);
               String lastMod = rs.getString(6);
               boolean isReady = rs.getBoolean(7);
               String instr = rs.getString(8);
               Timestamp ts = rs.getTimestamp(9);
               double diff = 0.5; // We don't care about External activity having a difficulty anymore.
               ExternalActivity a = new ExternalActivity(id,name,descr,url, instr, diff);
               l.add(a);
           }
           return l;
       }
       finally {
           if (stmt != null)
               stmt.close();
           if (rs != null)
               rs.close();
       }      
    }

    public static ExternalActivity getExternalActivity (Connection conn, int xactId) throws SQLException {
        ResultSet rs=null;
       PreparedStatement stmt=null;
       try {
           List<ExternalActivity> l = new ArrayList<ExternalActivity>();
           String q = "select a.id,a.name,a.description,a.url,a.creator,a.lastModifier,a.ready,a.instructions,a.creationtime " +
                   "from externalactivity a where a.id=?";
           stmt = conn.prepareStatement(q);
           stmt.setInt(1,xactId);
           rs = stmt.executeQuery();
           while (rs.next()) {
               int id= rs.getInt(1);
               String name= rs.getString(2);
               String descr = rs.getString(3);
               String url = rs.getString(4);
               String crea = rs.getString(5);
               String lastMod = rs.getString(6);
               boolean isReady = rs.getBoolean(7);
               String instr = rs.getString(8);
               Timestamp ts = rs.getTimestamp(9);
               double diff = 0.5;
               ExternalActivity a = new ExternalActivity(id,name,descr,url, instr, diff);
               return a;
           }
           return null;
       }
       finally {
           if (stmt != null)
               stmt.close();
           if (rs != null)
               rs.close();
       }
    }

    public static void main(String[] args) {
        
    }
}
