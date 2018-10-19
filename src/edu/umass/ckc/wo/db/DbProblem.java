package edu.umass.ckc.wo.db;

import edu.umass.ckc.wo.beans.Topic;
import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.content.CCStandard;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.content.ProblemStats;
import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.util.Lists;
import edu.umass.ckc.wo.beans.SATProb;
import edu.umass.ckc.wo.tutormeta.Skill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * An object which is in charge of database mgmt of Problem objects.
 *
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Jun 25, 2007
 * Time: 12:02:49 PM
 */
public class DbProblem extends BaseMgr {
    private static final Logger logger = Logger.getLogger(DbProblem.class);

    public static final int DUMMY_PROBLEM_ID = 899;

    /**
     * Given a problem return a list of Topic objects that this problem is associated with.
     * @param conn
     * @param probId
     * @return
     * @throws SQLException
     */
    public static List<Topic> getProblemTopics (Connection conn, int probId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select  t.* from probprobgroup m, problemgroup t where m.probid=? and t.id=m.pgroupid and t.active=1";
            ps = conn.prepareStatement(q);
            ps.setInt(1,probId);
            rs = ps.executeQuery();
            List<Topic> topics = new ArrayList<Topic>();
            while (rs.next()) {
                int topicId = rs.getInt(Topic.ID);
                Topic t = ProblemMgr.getTopic(topicId);
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

    public static boolean isProblemDefined (Connection conn, int id) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select * from problem where id=?";
            ps = conn.prepareStatement(q);
            ps.setInt(1,id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
            return false;
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
        }
    }

    /**
     * Given a problem ID return a ProblemImpl object containing that problem's data.
     * @param conn
     * @param id
     * @return
     * @throws SQLException
     */

      /*
    public Problem getProblem(Connection conn, int id) throws SQLException {
        String s = "select " + Problem.ID + "," + Problem.ANSWER +
                "," + Problem.ANIMATION_RESOURCE + "," + Problem.NAME +
                "," + Problem.NICKNAME +  "," + Problem.HAS_STRATEGIC_HINT  +
                "," + Problem.SOURCE +  "," + Problem.INSTRUCTIONS + "," + Problem.TYPE +
                " from Problem where id=? ";   // used to say status='Ready'  but should not put that in since we are going by ID
        String pname= "No problem found";
        PreparedStatement ps = conn.prepareStatement(s);
        ps.setInt(1,id);
        ResultSet rs = ps.executeQuery();
        try {
            if (rs.next()) {
                String resource = rs.getString(Problem.ANIMATION_RESOURCE);
                String source = rs.getString(Problem.SOURCE) ;
                String instructions = rs.getString(Problem.INSTRUCTIONS) ;
                String type = rs.getString(Problem.TYPE) ;
                String answer = rs.getString(Problem.ANSWER);
                String name = rs.getString(Problem.NAME);
                pname = name;
                String nname = rs.getString(Problem.NICKNAME);
                boolean hasStratHint = rs.getBoolean(Problem.HAS_STRATEGIC_HINT);
                return new Problem(id, resource, answer, name, nname, hasStratHint, source, instructions,type);
            }
            return null;
        } finally {
            logger.debug("getProblem for ID: " + id + " is " + pname);
            closeQuery(rs,ps);
        }
    }
      */
    public Problem getProblem(Connection conn, int id) throws SQLException {
        return ProblemMgr.getProblem(id);
    }
                       /*
    public Problem getProblemWithDifficulty(Connection conn, int id) throws SQLException {
        String s = "select problemid," + Problem.ANSWER +
                "," + Problem.ANIMATION_RESOURCE + ", p.name as name" +
                "," + Problem.NICKNAME +
                "," + Problem.SOURCE +
                "," + Problem.INSTRUCTIONS + "," + Problem.TYPE +
                ", diff_level "  +
                " from Problem p, OverallProbDifficulty o" +
                " where p.id=problemid and problemid=? ";   // used to say status='Ready'  but should not put that in since we are going by ID
        String pname= "No problem found";
        PreparedStatement ps = conn.prepareStatement(s);
        ps.setInt(1,id);
        ResultSet rs = ps.executeQuery();
        try {
            if (rs.next()) {
                String resource = rs.getString(Problem.ANIMATION_RESOURCE);
                String source = rs.getString(Problem.SOURCE);
                String instructions = rs.getString(Problem.INSTRUCTIONS);
                String type = rs.getString(Problem.TYPE);
                String answer = rs.getString(Problem.ANSWER);
                String name = rs.getString(Problem.NAME);
                pname = name;
                String nname = rs.getString(Problem.NICKNAME);
                double diff = rs.getDouble("diff_level") ;
                return new Problem(id, resource, answer, diff, name, nname, source, instructions,type);
            }
            return null;
        } finally {
            logger.debug("getProblem for ID: " + id + " is " + pname);
            closeQuery(rs,ps);
        }
    }       */
      /*
    public Problem getProblem(Connection conn, String name) throws SQLException {
        String s = "select " + Problem.ID + "," + Problem.ANSWER +
                "," + Problem.ANIMATION_RESOURCE + "," + Problem.SOURCE + "," + Problem.NAME +
                "," + Problem.NICKNAME +  "," + Problem.HAS_STRATEGIC_HINT  + "," + Problem.INSTRUCTIONS + "," + Problem.TYPE +
                " from Problem where name=? ";
        String pname= "No problem found";
        PreparedStatement ps = conn.prepareStatement(s);
        ps.setString(1,name);
        ResultSet rs = ps.executeQuery();
        try {
            if (rs.next()) {
                int id = rs.getInt(Problem.ID);
                String resource = rs.getString(Problem.ANIMATION_RESOURCE);
                String source = rs.getString(Problem.SOURCE);
                String instructions = rs.getString(Problem.INSTRUCTIONS) ;
                String type = rs.getString(Problem.TYPE) ;
                String answer = rs.getString(Problem.ANSWER);
                pname = name;
                String nname = rs.getString(Problem.NICKNAME);
                boolean hasStratHint = rs.getBoolean(Problem.HAS_STRATEGIC_HINT);
                return new Problem(id, resource, answer, name, nname, hasStratHint, source, instructions,type);
            }
            return null;
        } finally {
            logger.debug("getProblem for NAME: " + name + " is " + pname);
            closeQuery(rs,ps);
        }
    }
     */
    public Problem getProblem(Connection conn, String name) throws SQLException {
        return ProblemMgr.getProblemByName(name);
    }


    public String getProblemAnswer(Connection conn, int id) throws SQLException {
        String s = "select " + Problem.ANSWER +
                " from Problem where id=? and status='Ready' ";
        PreparedStatement ps = conn.prepareStatement(s);
        ps.setInt(1,id);
        ResultSet rs = ps.executeQuery();
        try {
            if (rs.next()) {
                String answer = rs.getString(Problem.ANSWER);
                return answer;
            }
            return null;
        } finally {
            closeQuery(rs,ps);
        }
    }

    /**
     * Return a List of all the active SAT problems in the system ordered by ID.
     * @param conn
     * @return   List of Problems
     * @throws Exception

    public List<Problem> getAllProblems (Connection conn) throws Exception {
        List result = new ArrayList<Problem>();
        String s = "select p." + Problem.ID + "," + Problem.ANSWER +
            "," + Problem.ANIMATION_RESOURCE + ", count(h.problemId)" +
            " from Problem p, Hint h where status='Ready' and p.id=h.problemId group by p.id order by p." + Problem.ID ;
        PreparedStatement ps = conn.prepareStatement(s);
        ResultSet rs = ps.executeQuery();
        try {
            while (rs.next()) {
                int id = rs.getInt(Problem.ID);
                String resource = rs.getString(Problem.ANIMATION_RESOURCE);
                String answer = rs.getString(Problem.ANSWER);
                int numHints = rs.getInt(4);
                result.add(new Problem(id,resource,answer,numHints));
            }
            return result;
        } finally {
            closeQuery(rs,ps);
        }
    }
 */


    public List<String> getClassOmittedTopicProblemIds(Connection conn, int classID, int topicId) throws SQLException {

        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            String q = "select probId from ClassOmittedProblems where classId=? and topicId=?";
            ps = conn.prepareStatement(q);
            ps.setInt(1,classID);
            ps.setInt(2,topicId);
            List<String> results = new ArrayList<String>();
            rs = ps.executeQuery();
            while (rs.next()) {
                int pid = rs.getInt(1);
                results.add(Integer.toString(pid));
            }
            return results;
        } finally {
            closeQuery(rs,ps);
        }
    }

    public List<String> getClassOmittedProblemIds(Connection conn, int classID) throws SQLException {

        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            String q = "select probId from ClassOmittedProblems where classId=?";
            ps = conn.prepareStatement(q);
            ps.setInt(1,classID);
            List<String> results = new ArrayList<String>();
            rs = ps.executeQuery();
            while (rs.next()) {
                int pid = rs.getInt(1);
                results.add(Integer.toString(pid));
            }
            return results;
        } finally {
            closeQuery(rs,ps);
        }
    }

    /**
     * Given all the problems in a topic, return a list of SATProblem beans.  This means using the classOmittedProblems to find problems
     * that have been omitted and then setting the bean's activation field. The bean simply adds an activation status field to ProblemImpl
     * so that it can be passed into a JSP and iterated over.
     *
     * @param conn
     * @param classID
     * @param allProbsInTopic
     * @param topicId
     * @return
     * @throws SQLException
     */
    public List<SATProb> getTopicOmittedProblems(Connection conn, int classID, List<Problem> allProbsInTopic, int topicId) throws SQLException {
        List<String> ids = getClassOmittedTopicProblemIds(conn,classID,topicId);
        List<SATProb> beans = new ArrayList<SATProb>();
        for (Problem prob: allProbsInTopic) {
            if (Lists.inList(prob.getId(),ids))
               beans.add(new SATProb(prob,false));
            else beans.add(new SATProb(prob,true));
        }
        return beans;
    }

    /**
     * Remove from the probs list any problems that are to be excluded for this class.
     * @param conn
     * @param classID
     * @param probs
     * @return
     * @throws SQLException
     */
    public List<Problem> excludeProblemsOmittedForClass(Connection conn, int classID, List<Problem> probs) throws SQLException {
        List omittedProbIds = getClassOmittedProblemIds(conn, classID); // ids returned as Strings

        Iterator itr = probs.iterator();
        while (itr.hasNext()) {
            Problem problem = (Problem) itr.next();
            int id = problem.getId();
            if (Lists.inList(id,omittedProbIds))
                itr.remove();
        }
        return probs;

    }


    /**
     *
     *
     * @param topicID
     * @return  a List of ProblemImpl objects that are members of the given problem group ordered by difficulty
     * @throws SQLException
     */
    /*
    public List<Problem> getProblemsInTopic (Connection conn, int probGroupID) throws SQLException {
        String q = "select p.id,animationResource,answer,d.diff_level,p.name,p.nickname,p.form,p.statementHTML,p.type " +
                   " from problem p, OverallProbDifficulty d, " +
                   " ProbProbGroup ppg where p.id = ppg.probID and d.problemId = p.id and p.status='ready' and ppg.pgroupID=? order by d.diff_level";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setInt(1,probGroupID);
        ResultSet rs = ps.executeQuery();
        List<Problem> probs = new ArrayList<Problem>();
        while (rs.next()) {
            int id = rs.getInt(1);
            String resource = rs.getString(2);
            String answer = rs.getString(3);
            double diff = rs.getDouble(4);
            String name = rs.getString(5);
            String nname = rs.getString(6);
            String source = rs.getString(7);
            String instructions = rs.getString(8);
            String type = rs.getString(9);
            Problem p = new Problem(id,resource,answer,diff,name,nname, source, instructions,type);
            probs.add(p);
        }
        return probs;
    }
     */

    public List<Problem> getProblemsInTopic(int topicID) throws SQLException {
        return ProblemMgr.getTopicProblems(topicID);
    }




    public List<Problem> getActivatedTopicProblems(Connection conn, int classId, int topicId) throws SQLException {
        List<Problem> topicProbs = getProblemsInTopic(topicId);
        topicProbs = excludeProblemsOmittedForClass(conn,classId,topicProbs);
        return topicProbs;
    }

    // If the given problem is among those that are in the active list, return true; false o/w
    private boolean isActivated (Problem prob, int[] activeProbs) {
        int id = prob.getId();
        for (int i:activeProbs) {
            if (i == id)
                return true;
        }
        return false;
    }

    /**
     *  Create a list of SATProb objects from a list of Problem objects.   Use the list of activeProblemIds
     * to mark each SATProb as active or not.
     * @param conn
     *@param allTopicProbs
     * @param activatedIds @return  a List of SATProb objects for a topic.
     */
    public List<SATProb> activateTopicProblems(Connection conn, List<Problem> allTopicProbs, int[] activatedIds) throws SQLException {
        List<SATProb> result = new ArrayList<SATProb>();

        // make a list of SATProb objects and mark as active if its id is in the list of activated problems
        for (Problem p : allTopicProbs) {
            SATProb sp = new SATProb(p,isActivated(p,activatedIds));
            result.add(sp);

        }
        return result;
    }


    /**
     * Insert a row for each <classId,topicId,problemId> that is an active problem in this class and topic
     * @param conn
     * @param classId
     * @param topicId
     * @param deactivatedIds
     * @throws SQLException
     */
    public void setClassTopicOmittedProblems(Connection conn, int classId, int topicId, List<Integer> deactivatedIds) throws SQLException {
        PreparedStatement ps=null;
        try {
            String q = "insert into ClassOmittedProblems (classId,topicId,probId) values (?,?,?)";
            for (int probId : deactivatedIds) {
                ps = conn.prepareStatement(q);
                ps.setInt(1,classId);
                ps.setInt(2,topicId);
                ps.setInt(3,probId);
                int n = ps.executeUpdate();
            }
        } finally {
            closeQuery(ps);
        }
    }

    public  void clearClassOmittedProblems (Connection conn, int classId) throws SQLException {
        PreparedStatement ps=null;
        try {
            String q = "delete from ClassOmittedProblems where classId=?";
            ps = conn.prepareStatement(q);
            ps.setInt(1,classId);
            int n = ps.executeUpdate();


        } finally {
            closeQuery(ps);
        }
    }

    /**
     * Remove all the <classId,topicId,problemId> rows for a given class, topic
     * @param conn
     * @param classId
     * @param topicId
     * @throws SQLException
     */
    private void removeActiveTopicProblemsForClass (Connection conn, int classId, int topicId) throws SQLException {
        PreparedStatement ps=null;
        try {
            String q = "delete from ClassOmittedProblems where classId=? and topicId=?";
            ps = conn.prepareStatement(q);
            ps.setInt(1,classId);
            ps.setInt(2,topicId);
            int n = ps.executeUpdate();


        } finally {
            closeQuery(ps);
        }
    }
    
	/**
	 * Remove all the <classId,topicId,problemId> rows for a given class, topic
	 * 
	 * @param conn
	 * @param classId
	 * @param probs
	 * @throws SQLException
	 */
	public List<Integer> filterproblemsBasedOnLanguagePreference(Connection conn, List<Problem> probs, int classId)
			throws SQLException {
		PreparedStatement ps = null;
		List<Integer> elementsToBeRemoved = null;
		try {
			String class_Language_Query = "select class_language from class c where c.id=?";
			ps = conn.prepareStatement(class_Language_Query);
			ps.setInt(1, classId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				final String class_language = rs.getString("class_language");
				elementsToBeRemoved = probs.stream()
                        .filter(listrmv -> !(listrmv.getProblemLanguage().equals(class_language))).map(Problem::getId)
                        .collect(Collectors.toList());
				probs.removeIf(listrmv -> !(listrmv.getProblemLanguage().equals(class_language)));
				return elementsToBeRemoved;
			}

		} finally {
			closeQuery(ps);
		}
		return elementsToBeRemoved;
	}

    /**
     * Refresh the the activated problems list for a class and topic.   Remove the old and then reinsert.
     * @param conn
     * @param classId
     * @param topicId
     * @param deactivatedIds
     * @throws SQLException
     */
    public void updateOmittedProbsList(Connection conn, int classId, int topicId, List<Integer> deactivatedIds) throws SQLException {
        removeActiveTopicProblemsForClass(conn,classId,topicId);
        setClassTopicOmittedProblems(conn, classId, topicId, deactivatedIds);
    }

    public List<Skill> getProblemSkills(Connection conn, int probId) throws java.sql.SQLException {
        List skills = new ArrayList<Skill>();

        String q = "select distinct skillid, Skill.name as name" +
                " from Hint, Skill where skill.id=skillid " +
                " and problemid=" + probId +
                " and skillid is not null";

        PreparedStatement ps = conn.prepareStatement(q);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Skill thisskill = new Skill(rs.getInt("skillid"), rs.getString("name"));
            skills.add(thisskill);
        }
        return skills;
    }

    public String getProblemSkillsAsString(Connection conn, int probId) throws java.sql.SQLException {

        String q = "select distinct skillid " +
                " from Hint " +
                " where problemid=" + probId +
                " and skillid is not null";

        PreparedStatement ps = conn.prepareStatement(q);
        ResultSet rs = ps.executeQuery();
        String skills = "(" ;

        while (rs.next()) {
            if ( ! skills.equals("("))
                skills = new String(skills + ",") ;

            skills = new String( skills + rs.getInt("skillid"));
        }
        return skills + ")" ;
    }

    public String getProblemSemiabstractSkillsAsString(Connection conn, int probId) throws java.sql.SQLException {

        String q = " select distinct semiabsskillid " +
                " from Hint, Skill " +
                " where problemid=" + probId +
                " and skillid=skill.id " ;

        PreparedStatement ps = conn.prepareStatement(q);
        ResultSet rs = ps.executeQuery();
        String skills = "(" ;

        while (rs.next()) {
            if ( ! skills.equals("("))
                skills = new String(skills + ",") ;

            skills = new String( skills + rs.getInt("skillid"));
        }
        return skills + ")" ;
    }

    public static void deactivateProblem(Connection conn, int probId, String probName) throws SQLException {

        String q;
        if (probId == -1)
            q = "select status,id from problem where name='"+probName+"'";
        else q = "select status from problem where id="+probId;
        Statement s = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                                ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = s.executeQuery(q);
        while (rs.next()) {
            rs.updateString(1,"disabled");
            rs.updateRow();
        }


    }

    /**
     * GIven a list of problemIds (that a student has solved or seen) this returns the ids that are in the given topic
     * @param conn
     * @param problemIds
     * @param topicId
     */
    public static List<String> getProblemsInTopic (Connection conn, List<String> problemIds, int topicId) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (String id: problemIds) sb.append(id + ",");
        if (problemIds.size() > 0)
            sb.replace(sb.length()-1,sb.length(),")");
        else
           return new ArrayList<String>();
        String idlist = sb.toString();
        try {
            String q = "select probid from probprobgroup where pgroupid=? and probid in " + idlist;
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,topicId);
            rs = stmt.executeQuery();
            List<String> res = new ArrayList<String>();
            while (rs.next()) {
                int pid= rs.getInt(1);
                res.add(Integer.toString(pid));
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

    /**
     *
     * @param conn
     * @param probId
     * @return an array of [avgHints, avgIncorrect, avgSolveTime(Secs)]
     * @throws Exception
     */
    public static float[] getExpectedBehavior(Connection conn, int probId) throws Exception {

        String s = "select avghints,avgincorrect,avgsecsprob from OverallProbDifficulty where problemid=?" ;
        PreparedStatement ps = conn.prepareStatement(s);
        ps.setInt(1,probId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            float[] result = new float[3];
            result[0] = rs.getFloat("avghints") ;
            result[1] = rs.getFloat("avgincorrect") ;
            result[2] = rs.getFloat("avgsecsprob") ;
            return result ;
        }
        else return null;
    }

    public static double getDiffLevel(Connection conn, int probID) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "select diff_level from overallprobdifficulty where problemId=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,probID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
        return 0.5;  //If there is no difficulty level, assume it is 0.5
    }

    /**
     *
     * @param conn
     * @param probID
     * @return an array [ probablily of SLIP,  probabilty that a guess could be correct ]
     * @throws SQLException
     */
    public static double[] getSlipGuess(Connection conn, int probID) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "select slip, guess from overallprobdifficulty where problemId=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,probID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                double s= rs.getDouble(1);
                double g= rs.getDouble(2);
                return new double[] {s,g};
            }
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
        return null;
    }

    /**
     * Given the formality prob id return the wayang prob id that is associated with it.   (in the animation resource col)
     * @param conn
     * @param formalityPID
     */
    public static int getFormalityProbId(Connection conn, int formalityPID) throws SQLException {
        // TODO lookup the problem in the animationResource column of the problem table
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "select id from problem where animationResource=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,formalityPID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                int woPID= rs.getInt(1);
                return woPID;
            }
            return -1;
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }

    /**
     * Given a problem ID, return a List of Standards that this problem is in.
     * @param conn
     * @param id
     * @return
     * @throws SQLException
     */
    public static List<CCStandard> getProblemStandards(Connection conn, int id) throws SQLException {
        List<CCStandard> result = new ArrayList<CCStandard>();
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            // the type indicates we want problems that relate to the standard (P means prereq)
            String q = "select s.id,s.description,s.category,s.grade,s.idABC from probstdmap m, standard s where m.probid=? and s.id=m.stdid";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String code= rs.getString(1);
                String descr= rs.getString(2);
                String category= rs.getString(3);
                String grade = rs.getString(4);
                String idABC = rs.getString(5);
                result.add(new CCStandard(code,descr,category,grade,idABC));
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


    public static ProblemStats getStats (Connection conn, int probId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select n, meanHints,meanAttempts,meanTime,varianceNumHints,varianceNumAttempts,varianceNumTime from probstats where probid=?";
            ps = conn.prepareStatement(q);
            ps.setInt(1,probId);
            rs = ps.executeQuery();
            if (rs.next()) {
                int n = rs.getInt("n");
                double mHints = rs.getDouble("meanHints");
                double mAttempts = rs.getDouble("meanAttempts");
                double mTime = rs.getDouble("meanTime");
                double sHints = rs.getDouble("varianceNumHints");
                double sAttempts = rs.getDouble("varianceNumAttempts");
                double sTime = rs.getDouble("varianceNumTime");
                return new ProblemStats(probId,n,mHints,mAttempts,mTime,sHints,sAttempts,sTime);
            }
            return null;
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
        }
    }

    /**
     * If a stats record does not exist create it,  else update the one that is there.
     *
     * @param conn
     * @param stats
     * @return
     */
    public static void updateStats(Connection conn, ProblemStats stats) throws SQLException {
        ProblemStats s = getStats(conn,stats.getPid());
        if (s != null)  {
            PreparedStatement stmt = null;
            try {
                String q = "update probstats set n=?, meanHints=?,meanAttempts=?,meanTime=?,varianceNumHints=?," +
                        "varianceNumAttempts=?,varianceNumTime=?, sdHints=?,sdAttempts=?,sdTime=? where probid=?";
                stmt = conn.prepareStatement(q);
                stmt.setInt(1, stats.getN());
                stmt.setDouble(2, stats.getMeanHints());
                stmt.setDouble(3, stats.getMeanAttempts());
                stmt.setDouble(4, stats.getMeanTimeSecs());
                stmt.setDouble(5, stats.getVarianceNumeratorHints());
                stmt.setDouble(6, stats.getVarianceNumeratorAttempts());
                stmt.setDouble(7, stats.getVarianceNumeratorTime());
                if (stats.getN() > 1) {
                    stmt.setDouble(8,Math.sqrt(stats.getVarianceNumeratorHints()/(stats.getN()-1)));
                    stmt.setDouble(9,Math.sqrt(stats.getVarianceNumeratorAttempts()/(stats.getN()-1)));
                    stmt.setDouble(10,Math.sqrt(stats.getVarianceNumeratorTime()/(stats.getN()-1)));
                }
                stmt.setInt(11,stats.getPid());
                stmt.executeUpdate();
            } finally {
                if (stmt != null)
                    stmt.close();
            }
        }
        else {
            insertStats(conn,stats);

        }

    }

    public static void insertStats(Connection conn, ProblemStats stats) throws SQLException {
        ResultSet rs = null;
        PreparedStatement s = null;
        try {
            String q = "insert into probstats (probid,n,meanHints,meanAttempts,meanTime,varianceNumHints,varianceNumAttempts,varianceNumTime) " +
                    "values (?,?,?,?,?,?,?,?)";
            s = conn.prepareStatement(q);
            s.setInt(1, stats.getPid());
            s.setInt(2, stats.getN());
            s.setDouble(3, stats.getMeanHints());
            s.setDouble(4, stats.getMeanAttempts());
            s.setDouble(5,stats.getMeanTimeSecs());
            s.setDouble(6,stats.getVarianceNumeratorHints());
            s.setDouble(7, stats.getVarianceNumeratorAttempts());
            s.setDouble(8, stats.getVarianceNumeratorTime());
            s.execute();
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
            if (e.getErrorCode() == Settings.duplicateRowError || e.getErrorCode() == Settings.keyConstraintViolation)
                ;
            else throw e;
        } finally {
            if (rs != null)
                rs.close();
            if (s != null)
                s.close();
        }
    }

    /**
     * Given the Topics for a class, modify each Topic object so that it contains the number of problems that are activated for the topic
     * and the class, and the number of problems broken down by grade level.
     * @param conn
     * @param classId
     * @param activetopics
     * @throws SQLException
     */
    public static void setTopicNumProbsForClass(Connection conn, int classId, List<Topic> activetopics) throws SQLException {
        for (Topic t: activetopics) {
            DbProblem probMgr = new DbProblem();
            List<Problem> problems = ProblemMgr.getWorkingProblems(t.getId());
            // get the problems omitted for this topic
            List<String> ids = probMgr.getClassOmittedTopicProblemIds(conn,classId,t.getId());
            int problemCount = 0;
            if (problems != null) {
                t.setNumProbs(problems.size() - (ids != null ? ids.size() : 0));
                int[] problemsByGrade = new int[10];
                for (Problem p : problems) {
                    if(!ids.contains("" + p.getId())) {
                        List<CCStandard> ccStandards = p.getStandards();
                        if (ccStandards.size() > 0) {
                            //Note: we're only taking the first standard, it's possible that it may be labeled with multiple grades
                            // ... but I didn't find any that were and I'm not sure what we'd do in that case anyway
                            int gradeNum = getGradeNum(ccStandards.get(0).getGrade());
                            if (gradeNum >= 0) {
                                problemsByGrade[gradeNum]++;
                                problemCount++;
                            }
                        }
                    }
                }
                t.setNumProbsByGrade(problemsByGrade);
            }
            t.setNumProbs(problemCount);
        }
    }

    /**
     * Gets a boolean mask for grade columns for displaying problems by grade.
     * Assumption: There are 10 grade categories, K, 1-8, and H.
     * @param topics The list of topics for which problems by grade will be displayed
     * @return a boolean[] indicating which grade columns contain active problems
     */
    public static boolean[] getGradeColumnMask(List<Topic> topics) {
        boolean[] gradeColumnMask = new boolean[10];
        for(Topic t : topics) {
            int[] problemsByGrade = t.getProblemsByGrade();
            for(int i = 0; i < gradeColumnMask.length; i++)
                if(problemsByGrade[i] > 0)
                    gradeColumnMask[i] = true;
        }
        return gradeColumnMask;
    }

    /**
     * Converts a grade string into a column number for displaying grades.
     * @param grade The grade attribute from a CCStandard
     * @return The column number for displaying this grade
     */
    public static int getGradeNum(String grade) {
        if(grade == null) return -1;
        int gradeNum = -1;
        if (grade.equals("K"))
            gradeNum = 0;
        else if (grade.equals("H"))
            gradeNum = 9;
        else {
            try {
                gradeNum = Integer.parseInt(grade);
            } catch (NumberFormatException e) {
                //We don't really care if we can't parse it, but let's print an error message
                System.out.println("Couldn't find grade for grade string: " + grade);
            }
        }
        return gradeNum;
    }
}
