package edu.umass.ckc.wo.db;

import edu.umass.ckc.wo.beans.Topic;
import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.content.CCStandard;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.content.TopicIntro;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * <p> Created by IntelliJ IDEA.
 * User: david
 * Date: Jul 17, 2008
 * Time: 10:31:46 AM
 * 
 ** Frank 	06-13-2020 	issue #106 replace use of probstdmap
 *  Frank   06-30-2020  issue #132 use idABC field when user-facing 
 *  Frank	12-26-20	issue #329 added multi-lingual versions of getAllTopics() and getTopicName()
 */
public class DbTopics {




    /**
     *  Get the Topics in the default class lesson plan.   The default class lesson plan is the set of rows in classlessonplan
     * that is marked isDefault=1.
     * @param conn
     * @return A List of Topic objects in the order they will be presented
     * @throws SQLException
     */
    public static List<Topic> getDefaultTopicsSequence(Connection conn, int classId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select probGroupId, json_unquote(json_extract(pgl.pg_language_name, (select concat('$.',language_code) from ms_language where language_name = (select class_language from class where id= (?))))) as description, seqPos from classlessonplan, problemgroup topic, problemgroup_description_multi_language pgl where isDefault=1 and probGroupId=topic.id  and pgl.pg_pg_grp_id=probGroupId order by seqPos";

            ps = conn.prepareStatement(q);
            rs = ps.executeQuery();
            List<Topic> topics = new ArrayList<Topic>();
            while (rs.next()) {
                Topic t = new Topic(rs.getInt(1),rs.getString(2));
                t.setSeqPos(rs.getInt(3));
                t.setOldSeqPos(t.getSeqPos());
                topics.add(t);
            }
            return topics;
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();

        }
    }

    public static List<Topic> getAllTopics (Connection conn) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        List<Topic> topics = new ArrayList<Topic>();
        try {

        	String q = "select id, json_unquote(json_extract(pgl.pg_language_name, '$.en')) as description from problemgroup, problemgroup_description_multi_language pgl where active=1 and pgl.pg_pg_grp_id=problemgroup.id order by id";
            
            stmt = conn.prepareStatement(q);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Topic t = new Topic(rs.getInt(1),rs.getString(2));
                topics.add(t);
            }
            return topics;
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }

    public static List<Topic> getAllTopics (Connection conn, int studId) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        List<Topic> topics = new ArrayList<Topic>();
        
    	int classId = DbUser.getStudentClass(conn, studId);

        try {
        	
            String q = "select problemgroup.id, pgl.pg_pg_grp_id, json_unquote(json_extract(pgl.pg_language_name, (select concat('$.',language_code) from ms_language where language_name = (select class_language from class where id= (?))))) as description from problemgroup, problemgroup_description_multi_language pgl where active=1 and pgl.pg_pg_grp_id=problemgroup.id order by id";
            
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,classId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Topic t = new Topic(rs.getInt(1),rs.getString(3));
                topics.add(t);
            }
            return topics;
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }

    public static Set<CCStandard> getRptTopicStandards(Connection conn, int id) throws SQLException {
        //List<CCStandard> result = new ArrayList<CCStandard>();
        Set<CCStandard> result = new TreeSet<CCStandard>();
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            // the type indicates we want problems that relate to the standard (P means prereq)
            String q = "select s.idABC,s.description,s.category,s.grade,s.idABC from topicstandardmap t, standard s where t.topicid=? and s.idABC=t.standardid order by s.idABC ;";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,id);
            rs = stmt.executeQuery();
            int count = 0;
            while (rs.next()) {
                String code= rs.getString(1);
                String descr= rs.getString(2);
                String category= rs.getString(3);
                String grade = rs.getString(4);
                String idABC = rs.getString(5);
                result.add(new CCStandard(code,descr,category,grade,idABC));
                count += 1;
            }
            if (count == 0) {
            	System.out.println("Missing topic:" + String.valueOf(id));
            }
            return result;
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        } 
    }

    
    
 public static List<Topic> getClassInactiveTopics(Connection conn, List<Topic> activeTopics) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            List<Topic> inactiveTopics = new ArrayList<Topic>();
            StringBuilder sb = new StringBuilder();
            Iterator itr = activeTopics.iterator();
            while (itr.hasNext()) {
                Topic t = (Topic) itr.next();
                // topics with no problems should not be considered active
                if (t.getNumProbs() > 0)
                    sb.append(t.getId() + ",");
                else {
                    inactiveTopics.add(t);  // remove empty topics from active list
                    itr.remove();
                }
            }
            String q;
            if (sb.length() == 0)
                q = "select id, description,summary from problemgroup where active=1";
            else {
                String ids = sb.substring(0,sb.toString().length()-1);
                q = "select id, description, summary from problemgroup where active=1 and " +
                     "id not in (" + ids + ")";
            }

            stmt = conn.prepareStatement(q);
            rs = stmt.executeQuery();


            while (rs.next()) {
                Topic t = new Topic(rs.getInt(1),rs.getString(2));
                String topicSummary = rs.getString("summary");
                if("".equals(topicSummary) || topicSummary == null)
                    topicSummary = "The problemset does not have a description";
                t.setSummary(topicSummary);
                inactiveTopics.add(t);
            }
            return inactiveTopics;
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
 }

    /** Gets topics for a class that are active and have problems that are ready.
     *  Takes an additional flag (includeTestableProblems ) which will also include a topic
     *  in the return list if it has testable problems
     * @return
     */
    public static List<Topic> getClassPlayableTopics (Connection conn, int classId, boolean includeTestableProblems) throws SQLException {
        List<Topic> possibleTopics  = getClassActiveTopics(conn,classId);
        Iterator<Topic> itr = possibleTopics.iterator();
        while (itr.hasNext()) {
            Topic t = itr.next();
            if (!ProblemMgr.isTopicPlayable(t.getId(), includeTestableProblems)) {
                itr.remove();
            }
        }
        return possibleTopics;

    }






    public static List<Topic> getClassActiveTopics (Connection conn, int classId) throws SQLException {
        List<Topic> topics = getClassActiveTopicsHelper(conn, classId, false);
        if (topics.size() == 0)
            return getClassActiveTopicsHelper(conn, classId, true);
        else return topics;
    }




    /**
     *
     * @param conn
     * @param classId  Will be used to get active topics for a class when isDefault is false
     * @param isDefault controls whether topics are found for the class or by using the default set of topics
     * @return
     * @throws SQLException
     */
    private static List<Topic> getClassActiveTopicsHelper (Connection conn, int classId, boolean isDefault) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "select probGroupId, \r\n" + 
            		"json_unquote(json_extract(pgl.pg_lanuage_description, (select concat('$.',language_code) from ms_language where language_name = (select class_language from class where id= (?))))) as description, seqPos,\r\n" + 
            		"json_unquote(json_extract(pgl.pg_language_name, (select concat('$.',language_code) from ms_language where language_name = (select class_language from class where id= (?))))) as summary \r\n" + 
            		"from classlessonplan, problemgroup topic, problemgroup_description_multi_language pgl where "
            		+(isDefault ? "isDefault=1" : "classid=? ")+ 
            		"and pgl.pg_pg_grp_id=probGroupId\r\n" + 
            		"and topic.id=probGroupId and seqPos > 0 and topic.active=1 and topic.id in (select distinct pgroupid from probprobgroup) \r\n" + 
            		"order by seqPos;";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,classId);
            stmt.setInt(2,classId);
            if (!isDefault)
                stmt.setInt(3,classId);
            rs = stmt.executeQuery();
            List<Topic> topics = new ArrayList<Topic>();
            while (rs.next()) {
                Topic t = new Topic(rs.getInt(1),rs.getString(2));
                t.setSeqPos(rs.getInt(3));
                t.setOldSeqPos(t.getSeqPos());
                String topicSummary = rs.getString("summary");
                    if("".equals(topicSummary) || topicSummary == null)
                        topicSummary = "The problemset does not have a description";
                t.setSummary(topicSummary);
                // We get the set of CCStandards for this topic from the ProblemMgr
                Set<CCStandard> stds = ProblemMgr.getTopicStandards(t.getId());
                t.setCcStandards(stds);
                topics.add(t);
            }
            return topics;
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }

    }

    /**
     * Given the id of a class, clone its lesson plan for another class.
     * @param conn
     * @param classId
     * @param newClassId
     * @throws SQLException
     */
    public static void cloneLessonPlan (Connection conn, int classId, int newClassId) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "select seqPos,probGroupId from classlessonplan where classId=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,classId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int pos= rs.getInt(1);
                int topicId = rs.getInt(2);
                insertTopic(conn, newClassId, pos, topicId);
            }
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }

    public static void insertTopic(Connection conn, int newClassId, int pos, int topicId) throws SQLException {
        PreparedStatement stmt=null;
        try {
            String q = "insert into classlessonplan (classId,seqPos,probGroupId) values (?,?,?)";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,newClassId);
            stmt.setInt(2, pos);
            stmt.setInt(3, topicId);
            stmt.execute();
        }

        finally {

            if (stmt != null)
                stmt.close();
        }
    }

    private static void addTopicCCStandards (Connection conn, List<Topic> topics)  {
        for (Topic t: topics) {


        }
    }

    /**
     * Get the topic sequence for a given class.
     * N.B.  Topics that have been deactivated have a seqPos of -1.  They are included at the end of the list after
     * the active topics.
     * @param conn
     * @param classId
     * @return  a List of Topic objects in the order they will be presented
     * @throws SQLException
     */
    public static List<Topic> getClassTopicsSequence2 (Connection conn, int classId) throws SQLException {
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                String q = "select probGroupId, topic.description, seqPos from classlessonplan, problemgroup topic where classid=? and topic.id=probGroupId and seqPos > 0 order by seqPos ";
                ps = conn.prepareStatement(q);
                ps.setInt(1,classId);
                rs = ps.executeQuery();
                List<Topic> topics = new ArrayList<Topic>();
                while (rs.next()) {
                    Topic t = new Topic(rs.getInt(1),rs.getString(2));
                    t.setSeqPos(rs.getInt(3));
                    t.setOldSeqPos(t.getSeqPos());
                    topics.add(t);
                }
                ps.close();
                rs.close();
                q = "select probGroupId, topic.description from classlessonplan, problemgroup topic where classid=? and topic.id=probGroupId and seqPos < 0 ";
                ps = conn.prepareStatement(q);
                ps.setInt(1,classId);
                rs = ps.executeQuery();
                while (rs.next()) {
                    Topic t = new Topic(rs.getInt(1),rs.getString(2));
                    t.setSeqPos(-1);
                    t.setOldSeqPos(-1);
                    topics.add(t);
                }
                return topics;
            } finally {
                if (rs != null)
                    rs.close();
                if (ps != null)
                    ps.close();

            }
        }

    /**
     * Return the topic sequence for a class.   Will return the default class lesson plan if a customized
     * plan does not exist for the class.   Topics are returned in the order they will be presented with deactivated
     * topics included at the tail of the list.
     * @param conn
     * @param classId
     * @return  List of Topic objects in presentation order.
     * @throws SQLException
     */
    public static List<Topic> getClassTopicsSequence (Connection conn, int classId) throws SQLException {
        List<Topic> topics = getClassTopicsSequence2(conn,classId);
        if (topics.size() == 0)
            return getDefaultTopicsSequence(conn,classId);
        else return topics;
    }





    public static String getTopicName (Connection conn, int topicId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select problemgroup.id, pgl.pg_pg_grp_id, json_unquote(json_extract(pgl.pg_language_name, concat('$.','en'))) as description from problemgroup, problemgroup_description_multi_language pgl where active=1 and pgl.pg_pg_grp_id=problemgroup.id order by id";
            ps = conn.prepareStatement(q);
            ps.setInt(1,topicId);
            rs = ps.executeQuery();
            

            if (rs.next()) {
                return rs.getString(1);
            }
            return null;
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();

        }
    }

    public static String getTopicName (Connection conn, int topicId, String lang) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select problemgroup.id, pgl.pg_pg_grp_id, json_unquote(json_extract(pgl.pg_language_name, concat('$.','?'))) as description from problemgroup, problemgroup_description_multi_language pgl where active=1 and pgl.pg_pg_grp_id=problemgroup.id order by id";
            ps = conn.prepareStatement(q);
            ps.setString(1,lang);
            rs = ps.executeQuery();
            

            if (rs.next()) {
                return rs.getString(1);
            }
            return null;
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();

        }
    }
    
    
    public static String getTopicName (Connection conn, int topicId, int classId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select problemgroup.id, pgl.pg_pg_grp_id, json_unquote(json_extract(pgl.pg_language_name, (select concat('$.',language_code) from ms_language where language_name = (select class_language from class where id= (?))))) as description from problemgroup, problemgroup_description_multi_language pgl where active=1 and pgl.pg_pg_grp_id=problemgroup.id order by id";
            ps = conn.prepareStatement(q);
            ps.setInt(1,topicId);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getString(3);
            }
            return null;
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();

        }
    }
    
    public static boolean hasLessonPlan (Connection conn, int classId) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "select * from ClassLessonPlan where classId=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,classId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
            else return false;
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }

    /**
     * Remove all the rows for a class lesson plan
     * @param conn
     * @param classId
     * @throws SQLException
     */
    public static void removeClassActiveTopics (Connection conn, int classId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "delete from classlessonplan where classId=? and seqPos>0";
            ps = conn.prepareStatement(q);
            ps.setInt(1,classId);
            ps.executeUpdate();
        }  finally {
            if (ps != null)
                ps.close();
        }
    }

    /**
     * Remove all the rows for a class lesson plan
     * @param conn
     * @param classId
     * @throws SQLException
     */
    public static void removeClassTopics (Connection conn, int classId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "delete from classlessonplan where classId=?";
            ps = conn.prepareStatement(q);
            ps.setInt(1,classId);
            ps.executeUpdate();
        }  finally {
            if (ps != null)
                ps.close();
        }
    }


    /**
     * Given a list of Topics that is sorted according to the order of presentation, insert each topic as a row
     * in the classlessonplan table.  N.B.   removeClassTopics (above) should be called first to make sure
     * the previous topic ordering is removed for the class.
     * @param conn
     * @param classId
     * @param topics
     * @throws SQLException
     */
    public static void insertTopics (Connection conn, int classId, List<Topic> topics) throws SQLException {
      PreparedStatement ps = null;
        try {
            String q = "insert into classlessonplan (classId, seqPos, probGroupId, isDefault) values (?,?,?,?)";
            int i=1;
            for (Topic topic: topics) {
                ps = conn.prepareStatement(q);
                ps.setInt(1,classId);
                ps.setInt(2,i++);   // dm used to be topic.seqPos which was creating gaps in the sequence
                ps.setInt(3,topic.getId());
                ps.setInt(4,0); // not a default
                ps.executeUpdate();
            }
        } finally {
            if (ps != null)
                ps.close();

        }
    }

    /**
     * Creates a lesson plan for a class that is the standard set of topics in the default order.
     * @param conn
     * @param classId
     * @throws SQLException
     */
    public static void insertLessonPlanWithDefaultTopicSequence(Connection conn, int classId) throws SQLException {
    	DbTopics.removeClassActiveTopics(conn, classId);
        List<Topic> topics = getDefaultTopicsSequence(conn, classId);
        insertTopics(conn,classId,topics);
    }


    /**
     * Returns A topic introduction if there is one.
     *
     * @return
     * @throws java.sql.SQLException
     */
    public static TopicIntro getTopicIntro(Connection conn, int topicID, int classID) throws SQLException {
    	
        String q = "select json_unquote(json_extract(pgl.pg_language_name, (select concat('$.',language_code) from ms_language where language_name = (select class_language from class where id= ?)))) as description,\r\n" + 
        		"json_unquote(json_extract(pgl.pg_intro_html, (select concat('$.',language_code) from ms_language where language_name = (select class_language from class where id= ?)))) as intro\r\n" + 
        		"from problemgroup_description_multi_language pgl where pgl.pg_pg_grp_id =?;";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setInt(1,classID);
        ps.setInt(2,classID);
        ps.setInt(3,topicID);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String intro = rs.getString("intro");
            String descr = rs.getString("description");
            return new TopicIntro(intro,"html", descr, topicID);
        }
        return null;
    }
    
    public static int getTopicDemoProblem(Connection conn, int topicID) throws SQLException {
        String q = "select demoProblem from problemGroup where id = ?";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setInt(1,topicID);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            int demoProbId = rs.getInt(1);
            if (rs.wasNull())
                return -1;
            else return demoProbId;
        }
        return -1;
    }



    public static void insertClassInactiveTopic(Connection conn, int classId, Topic t) throws SQLException {
        PreparedStatement stmt=null;
        try {
            String q = "insert into classlessonplan (classid, probgroupid, seqpos, isdefault) values (?,?,?,?)";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,classId);
            stmt.setInt(2,t.getId());
            stmt.setInt(3,-1);
            stmt.setInt(4,0);
            stmt.execute();
        }

        finally {
            if (stmt != null)
                stmt.close();
        }
    }

    public static void cloneOmittedProblems(Connection conn, int classId, int newClassId) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        PreparedStatement ps=null;
        try {
            String q = "select probId,topicId from classomittedproblems where classid=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,classId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int probId= rs.getInt(1);
                int topicId= rs.getInt(2);
                ps = conn.prepareStatement("insert into classomittedproblems (classid,probid,topicid) values (?,?,?)");
                ps.setInt(1,newClassId);
                ps.setInt(2,probId);
                ps.setInt(3,topicId);
                ps.executeUpdate();
                ps.close();
            }
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }


    /**
     * A dummy topic exists so that we can insert its id in places where foreign keys insist on legal topicIDs
     * @param conn
     * @return
     * @throws SQLException
     */
    public static int getDummyTopic(Connection conn) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select id from problemgroup where type='dummy'";
            ps = conn.prepareStatement(q);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
            return -1;
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();

        }
    }

    public static void clearClassLessonPlan(Connection conn, int classId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "delete from classlessonplan where classId=?";
            ps = conn.prepareStatement(q);
            ps.setInt(1,classId);
            ps.executeUpdate();
        }  finally {
            if (ps != null)
                ps.close();
        }
    }

    /**
     * Return the id of the topic whose description is Interleaved Problem Set ...
     * @param conn
     * @return
     * @throws SQLException
     */
    public static int getInterleavedTopicId(Connection conn) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "select id from problemgroup where description like 'Interleaved Problem Set%'";
            stmt = conn.prepareStatement(q);
            rs = stmt.executeQuery();
            if (rs.next()) {
                int c= rs.getInt(1);
                return c;
            }
            else return -1;
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }

    public static void deleteStudentInterleavedProblems (Connection conn, int studId) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;

        try {
            String q = "delete from interleavedProblems where studId=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,studId);
            stmt.executeUpdate();
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }

    /**
     * Returns a list of pairs [probId, topicIc] which are the problems in an interleaved prob set for a student.
     * @param conn
     * @param studId
     * @return
     * @throws SQLException
     */
    public static List<int[]> getInterleavedProblems (Connection conn, int studId) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "select probId, topicId from interleavedProblems where studId=? order by position";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,studId);
            rs = stmt.executeQuery();
            List<int[]> res = new ArrayList<int[]>();
            while (rs.next()) {
                int pid= rs.getInt(1);
                int tid= rs.getInt(2);
                res.add(new int[] {pid,tid});
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

    public static void addStudentInterleavedProblem (Connection conn, int studId, int probId, int topicId, int position) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "insert into interleavedProblems (studId, probId, topicId, position, shown) values (?,?,?,?,0)";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, studId);
            stmt.setInt(2, probId);
            stmt.setInt(3, topicId);
            stmt.setInt(4, position);
            stmt.execute();
        }

        finally {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
        }
    }

    public static List<Integer> getNonShownInterleavedProblemSetProbs(Connection conn, int studId) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            List<Integer> probs = new ArrayList<Integer>();
            String q = "select probId from interleavedProblems where studId=? and shown=0 order by position";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,studId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int c= rs.getInt(1);
                probs.add(c);
            }
            return probs;

        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }
}
