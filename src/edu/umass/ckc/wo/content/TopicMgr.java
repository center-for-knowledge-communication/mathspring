package edu.umass.ckc.wo.content;

import edu.umass.ckc.wo.beans.Topic;
import edu.umass.ckc.wo.db.DbTopics;
//import edu.umass.ckc.wo.event.admin.AdminReorderTopicsEvent;
import edu.umass.ckc.wo.event.admin.AdminReorderTopicsEvent;
import edu.umass.ckc.wo.ttmain.ttmodel.CreateClassForm;
import edu.umass.ckc.wo.ttmain.ttservice.util.TTUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

/**
 * <p> Created by IntelliJ IDEA.
 * User: david
 * Date: Jul 23, 2008
 * Time: 9:09:47 AM
 */
public class TopicMgr {


    public void removeTopicFromLessonPlan (Connection conn, int classId, int topicId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "delete from classlessonplan where classId=? and probgroupid=?";
            ps = conn.prepareStatement(q);
            ps.setInt(1,classId);
            ps.setInt(2,topicId);
            ps.executeUpdate();
        }  finally {
            if (ps != null)
                ps.close();
        }

    }
    
   

    public List<Topic> omitTopic (Connection conn,  int classId, int topicId) throws SQLException {
        List<Topic> topics = DbTopics.getClassActiveTopics(conn, classId);
        DbTopics.removeClassActiveTopics(conn, classId);

        Iterator<Topic> itr = topics.iterator();
        while(itr.hasNext()) {
            Topic t = itr.next();
            if(t.getId() == topicId) {
                itr.remove();
            }
        }

        DbTopics.insertTopics(conn, classId, topics);
        return topics;
    }

    public void reactivateTopic (Connection conn,  AdminReorderTopicsEvent e) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            //Find the last topic's seqPos for this class so we can insert at the end
            String q = "select max(seqPos)+1 from classLessonPlan where classId=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,e.getClassId());
            int seqPos = 1;
            rs = stmt.executeQuery();
            if(rs.next()) {
                seqPos = Math.max(1, rs.getInt(1));
            }
            rs.close();
            stmt.close();
            if(!isTopicInPlan(conn,e.getTopicId(),e.getClassId()))
                insertLessonPlanActiveTopic(conn,e.getTopicId(),e.getClassId(),seqPos);
                //If the lesson is in the plan, we shouldn't reposition it, because that would leave a gap
//            else updateLessonPlanMakeTopicActive(conn,e.getTopicId(),e.getClassId(),seqPos);
        } finally {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
        }


    }

    private void insertLessonPlanActiveTopic(Connection conn, int topicId, int classId, int seqPos) throws SQLException {
        ResultSet rs = null;
        PreparedStatement s = null;
        try {
            String q = "insert into classLessonPlan (classId, probGroupId, seqPos, isDefault) " +
                    "values (?,?,?,?)";
            s = conn.prepareStatement(q);
            s.setInt(1, classId);
            s.setInt(2, topicId);
            s.setInt(3, seqPos);
            s.setInt(4, 0);
            s.execute();
        } finally {
            if (rs != null)
                rs.close();
            if (s != null)
                s.close();
        }
    }

    private void updateLessonPlanMakeTopicActive(Connection conn, int topicId, int classId, int seqPos) throws SQLException {
        PreparedStatement stmt = null;
        try {
            String q = "update classlessonplan set seqpos=? where probgroupid=? and classId=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, seqPos);
            stmt.setInt(2, topicId);
            stmt.setInt(3, classId);
            stmt.executeUpdate();
        } finally {
            if (stmt != null)
                stmt.close();
        }
    }

    private boolean isTopicInPlan (Connection conn, int topicId, int classId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select seqPos from classlessonplan where probgroupId=? and classId=?";
            ps = conn.prepareStatement(q);
            ps.setInt(1,topicId);
            ps.setInt(2,classId);
            rs = ps.executeQuery();
            if (rs.next()) {
                int pos = rs.getInt(1);
                return pos > 0;
            }
            return false;
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
        }
    }



    public List<Topic> moveTopic (Connection conn, AdminReorderTopicsEvent e) throws SQLException {
        int classId= e.getClassId();
        int source = e.getSource();
        int destination = e.getDestination();
        List<Topic> topics = DbTopics.getClassActiveTopics(conn,classId);
        boolean swapped = moveTopicAux(topics, source, destination);
        // if the swap happens in the list, the list elements (topics) are swapped and the seqPos field of each object is mutated.
        //  Then we remove all active topics from the classlessonplan and stick them all back in again.
        if (swapped) {
            DbTopics.removeClassActiveTopics(conn,classId);
            DbTopics.insertTopics(conn,classId,topics);
        }
        return topics;
    }

    private boolean moveTopicAux(List<Topic> topics, int source, int destination) {
        if(Math.min(source, destination) < 0 || Math.max(source, destination) >= topics.size()) {
            System.out.println("Error: attempted to move topic at position " + source
                    + " outside of bounds of topic list to position " + destination
                    + ", but there are only " + topics.size() + " topics.");
            return false;
        }
        int inc = (destination - source) / Math.abs(destination - source);
        for(int i = source; i != destination; i += inc) {
            int pos = topics.get(i).getSeqPos();
            topics.get(i).setSeqPos(topics.get(i+inc).getSeqPos());
            topics.get(i+inc).setSeqPos(pos);
            Collections.swap(topics,i,i+inc);
        }
        return true;
    }
}
