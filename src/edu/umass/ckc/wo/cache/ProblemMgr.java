package edu.umass.ckc.wo.cache;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;
import org.apache.log4j.Logger;

import edu.umass.ckc.wo.beans.ProbProbGroupEntry;
import edu.umass.ckc.wo.beans.Topic;
import edu.umass.ckc.wo.content.CCStandard;
import edu.umass.ckc.wo.content.Hint;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.content.ProblemAnswer;
import edu.umass.ckc.wo.content.Video;
import edu.umass.ckc.wo.db.DbHint;
import edu.umass.ckc.wo.db.DbProblem;
import edu.umass.ckc.wo.db.DbUtil;
import edu.umass.ckc.wo.db.DbVideo;
import edu.umass.ckc.wo.tutor.probSel.StandardExampleSelector;
import edu.umass.ckc.wo.tutor.vid.StandardVideoSelector;
import edu.umass.ckc.wo.tutormeta.ExampleSelector;
import edu.umass.ckc.wo.tutormeta.VideoSelector;
import edu.umass.ckc.wo.util.Pair;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: Sep 24, 2009
 * Time: 2:40:23 PM
 * 
 * Frank 10-15-19 Issue #4 new status value in-progress
 * Frank 06-13-2020 issue #106 replace use of probstdmap
 * Frank 06-13-2020 issue #108 replace use of gradeFromStandard
 * Frank 06-13-2020 issue #106R2 missed one - replace use of probstdmap
 * Kartik 08-07-2020 issue #133 removed condition: where probStats.n =>10 and set default to 0.G5 (G- Grade)
 * 
 * To change this template use File | Settings | File Templates.
 */
public class ProblemMgr {

    private static final Logger logger = Logger.getLogger(ProblemMgr.class);

    private static ArrayList<Integer> problemIds = new ArrayList<Integer>();
    private static Map<Integer, Problem> allProblems = new HashMap<Integer, Problem>();
    private static Map<Integer, ArrayList<Integer>> probIdsByTopic;
    private static Map<Integer, Set<CCStandard>> stdsByTopic;
    private static List<Topic> allTopics = new ArrayList<Topic>();;
    private static ExampleSelector exSel = new StandardExampleSelector();
    private static VideoSelector vidSel = new StandardVideoSelector();
    private static int[] topicIds;
    private static boolean loaded=false;


    public static boolean isLoaded () {
        return loaded;
    }

    public static synchronized void loadProbs(Connection conn) throws Exception {
        if (!loaded) {
            loaded = true;
            problemIds = new ArrayList<Integer>();
            allProblems = new HashMap<Integer, Problem>();
            allTopics = new ArrayList<Topic>();
            probIdsByTopic = new HashMap<Integer,ArrayList<Integer>>();
            stdsByTopic = new HashMap<Integer,Set<CCStandard>>();
            loadTopics(conn);
            loadAllProblems(conn);
            updateProbProbGroup(conn);
            fillTopicProblemMap(conn);
            fillTopicStandardMap(conn);
            // once problems are built, we pass over them and set their examples using the StandardExampleSelector
            setProblemExamples(conn);
            updateProblemStats(conn,getProblemStatsProblemStats(conn));
        }
    }

    public static int getTopicProblemCount (int topicId) {
        ArrayList<Integer> probs = probIdsByTopic.get(topicId);
        if (probs != null)
            return probs.size();
        else return -1;
    }

    public static void dumpCache () {
        loaded = false;
    }

//    private List<TopicEntity> loadTopics2 () {
//        Session sess = HibernateUtil.getSessionFactory().openSession();
//        Transaction tx;
//        try {
//            tx = sess.beginTransaction();
//            Query q =  sess.createQuery("from TopicEntity where active=1");
//            List<TopicEntity> l = q.list();
//            return l;
//        }
//        finally {
//            sess.close();
//        }
//    }
    
    private static void updateProbProbGroup(Connection conn) throws SQLException {
    	
    	List<ProbProbGroupEntry> probProbGroupEntries = getProbProbGroupEntries(conn);
    	removeExistingEntries(conn, probProbGroupEntries);
    	PreparedStatement ps = null;
		ResultSet rs = null;
		String q = " insert into probprobgroup (pgroupId, probId)"
		        + " values (?, ?)";
		try {
			ps = conn.prepareStatement(q);
			for (ProbProbGroupEntry probProbGroupEntry : probProbGroupEntries) {

				double pgroupId = probProbGroupEntry.getPgroupId();
				double probId = probProbGroupEntry.getProbId();
										
				ps.setDouble(1, pgroupId);
				ps.setDouble(2, probId);
				
				ps.executeUpdate();
			}

		} finally {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();

		}

    }
    
    private static List<ProbProbGroupEntry> getProbProbGroupEntries(Connection conn) throws SQLException {
    	PreparedStatement ps = null;
        ResultSet rs = null;
        List<ProbProbGroupEntry> probProbGroupEntries = new ArrayList<>();
        try {
            String q = "select t.topicid, p.id, p.standardID, p.idABC " + 
            		"from " + 
            		"(select problem.id, problem.standardID, standard.idABC " + 
            		"from problem, standard " + 
            		"where problem.standardID = standard.id) as p ,(select standardid, topicid " + 
            		"from topicstandardmap " + 
            		"where topicid in (select id from problemgroup where active=1)) as t " + 
            		"where p.idABC = t.standardid " + 
            		"order by t.topicid, p.id"; 
            ps = conn.prepareStatement(q);
            rs = ps.executeQuery();
            while (rs.next()) {
            	ProbProbGroupEntry probProbGroupEntry = new ProbProbGroupEntry();
            	probProbGroupEntry.setPgroupId(rs.getInt("topicid"));
            	probProbGroupEntry.setProbId(rs.getInt("id"));
            	probProbGroupEntry.setStandardId(rs.getString("standardID"));
            	probProbGroupEntry.setStandardIdABC(rs.getString("idABC"));
                
            	probProbGroupEntries.add(probProbGroupEntry);
            }
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();

        }
        return probProbGroupEntries;
	}

	private static void removeExistingEntries(Connection conn, List<ProbProbGroupEntry> probProbGroupEntries) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String q = "delete from probprobgroup where pgroupId = ?";
		try {
			ps = conn.prepareStatement(q);
			for (ProbProbGroupEntry probProbGroupEntry : probProbGroupEntries) {

				int pgroupId = probProbGroupEntry.getPgroupId();
										
				ps.setInt(1, pgroupId);
	
				ps.executeUpdate();
			}

		} finally {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();

		}
	
}

	private static Map<Integer,List<Double>> getProblemStatsProblemStats(Connection conn) throws SQLException {
    	  PreparedStatement ps = null;
          ResultSet rs = null;
          Map<Integer,List<Double>> probStatsEntries = new HashMap<>();
          try {
              // issue #108 use problem.standardID instead of problem.gradeFromStandard
              String q = "select stats.probId,stats.meanHints, stats.meanAttempts,stats.meanTime,ov.avghints ,ov.avgincorrect ,ov.avgsecsprob, prob.standardID, stats.n from probstats stats join overallprobdifficulty ov on stats.probId = ov.problemId join problem prob on prob.id = ov.problemId order by probId"; 
              ps = conn.prepareStatement(q);
              rs = ps.executeQuery();
              while (rs.next()) {
                  List<Double> probStats = new ArrayList<>();
                  probStats.add(rs.getDouble("meanHints"));
                  probStats.add(rs.getDouble("meanAttempts"));
                  probStats.add(rs.getDouble("meanTime"));
                  
                  probStats.add(rs.getDouble("avghints"));
                  probStats.add(rs.getDouble("avgincorrect"));
                  probStats.add(rs.getDouble("avgsecsprob"));
                  
                  // issue #108 obsolete probStats.add(rs.getDouble("gradeFromStandard"));
                  
                  // issue #108 replacement
                  Double dblGrade = 1.0;
                  try {
                	  String standardID = rs.getString("standardID");
                	  String gradeFromStandard = standardID.substring(0, 1);
                	  if (gradeFromStandard.toLowerCase().equals("k")) {
                		  gradeFromStandard = "0";
                	  }
                	  else if (Character.isLetter(gradeFromStandard.charAt(0))) {
                		  gradeFromStandard = "9";
                	  }

                	  gradeFromStandard += ".0";
                	  dblGrade = Double.parseDouble(gradeFromStandard);
                  }
                  catch (Exception e) {
                	  logger.error(e.getMessage() + " on " + rs.getInt("probId"));
                  }
                  probStats.add(dblGrade);
                  probStats.add(rs.getDouble("n"));
              
                  probStatsEntries.put(rs.getInt("probId"), probStats);
              }
          } finally {
              if (rs != null)
                  rs.close();
              if (ps != null)
                  ps.close();

          }
          return probStatsEntries;
    }
    
   	private static Map<Integer, List<Double>> updateProblemStats(Connection conn,
			Map<Integer, List<Double>> probStatsEntries) throws SQLException {
		List<Double> totalAvgHints = new ArrayList<>();
		List<Double> totalAvgIncorrect = new ArrayList<>();
		List<Double> totalAvgSecsProblem = new ArrayList<>();

		probStatsEntries.forEach((probId, statEntries) -> {
			totalAvgHints.add(statEntries.get(3));
			totalAvgIncorrect.add(statEntries.get(4));
			totalAvgSecsProblem.add(statEntries.get(5));
		});

		double percentleHints = new Percentile().evaluate(ArrayUtils.toPrimitive(totalAvgHints.stream().toArray(Double[]::new)));
		double percentileAvgIncorrect = new Percentile().evaluate(ArrayUtils.toPrimitive(totalAvgIncorrect.stream().toArray(Double[]::new)));
		double percentileAvgSecsProblem = new Percentile().evaluate(ArrayUtils.toPrimitive(totalAvgSecsProblem.stream().toArray(Double[]::new)));
		PreparedStatement ps = null;
		ResultSet rs = null;
		String q = "UPDATE overallprobdifficulty SET diff_hints = ?, diff_time = ?,diff_incorr = ?,diff_level=?  WHERE problemId = ?";
		try {
			ps = conn.prepareStatement(q);
			for (Map.Entry<Integer, List<Double>> statEntries : probStatsEntries.entrySet()) {
				
				double diff_hints = statEntries.getValue().get(0) / percentleHints;
				double diff_incorr = statEntries.getValue().get(1) / percentileAvgIncorrect;
				double diff_time = statEntries.getValue().get(2) / percentileAvgSecsProblem;
				double diff_level_compute = (diff_hints + diff_incorr + diff_time) / 3;

				if (diff_level_compute == 0.0 || statEntries.getValue().get(7)< 10) {
					diff_level_compute = 0.5;
				}
				else {
					diff_level_compute = (diff_level_compute < 1 ? diff_level_compute : 0.99)/10;
				}
				double diff_level = statEntries.getValue().get(6) / 10 + diff_level_compute;
				
				diff_hints = diff_hints < 1 ? diff_hints : 0.99;
				diff_incorr = diff_incorr < 1 ? diff_incorr : 0.99;
				diff_time = diff_time < 1 ? diff_time : 0.99;
				
				ps.setDouble(1, diff_hints);
				ps.setDouble(2, diff_time);
				ps.setDouble(3, diff_incorr);
				ps.setDouble(4, diff_level);
				ps.setInt(5, statEntries.getKey());

				ps.executeUpdate();
			}

		} finally {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();

		}
		return probStatsEntries;
	}
  

    private static void loadTopics(Connection conn) throws SQLException {
//        List<TopicEntity> topicEntities = loadTopics2();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select id, description, summary from problemgroup where active=1";
            ps = conn.prepareStatement(q);
            rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String description = rs.getString("description");
                String summary = rs.getString("summary");
                allTopics.add(new Topic(id, description,summary));
            }
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();

        }
    }

    private static void setProblemExamples (Connection conn) throws Exception {

        for (int pid: problemIds) {
            int xid = exSel.selectProblem(conn,pid);
            Problem p = allProblems.get(pid);
            if (p != null && xid > 0)
                p.setExample(xid);

        }
    }

    public static Topic getTopic(int id)
    {
        for (Topic t : allTopics)
        {
            if (t.getId() == id)
            {
                return t;
            }
        }
        return null;
    }

    public static HashMap<String, ArrayList<String>> getVarDomain(int id, Connection conn) throws SQLException {
        String s = "select p.name, p.values from ProblemParamSet p where problemID="+Integer.toString(id);
        PreparedStatement ps = conn.prepareStatement(s);
        ResultSet rs = ps.executeQuery();
        HashMap<String, ArrayList<String>> vars = new HashMap<String, ArrayList<String>>();
        String name = null;
        ArrayList<String> vals = null;
        try {
            while (rs.next()) {
                // prepend the variable name with $#.   Later plugging values into the problem will use $# to indicate where
                // variables are located.
                name = "$#"+rs.getString("name");    // DM 6/2/15 changed per Toms request
                vals = new ArrayList<String>(Arrays.asList(rs.getString("values").split(",")));
                vars.put(name, vals);
            }
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
        }
        return vars;
    }


    // DM 1/23/18 Added in imageFileId and audioFileID
    private static PreparedStatement buildProblemQuery(Connection conn, Integer problemId) throws SQLException {
        String problemFilter = problemId != null ? " and p.id = " + problemId : "";
        String s = "select p.id, answer, animationResource, p.name, nickname," +
                " strategicHintExists, hasVars, screenShotURL, diff_level, form," +
                " isExternalActivity, type, video, example, p.status, p.questType," +
                " statementHTML, imageURL, audioResource, units, problemFormat, imageFileId, audioFileId, layoutID, usableAsExample,language" +
                " from Problem p, OverallProbDifficulty o" +
                " where p.id=o.problemid" + problemFilter +
                " and (status='Ready' or status='ready' or status='testable' or status='in-progress')" +
                " order by p.id;";
        PreparedStatement ps = conn.prepareStatement(s);
        return ps;
    }

    private static Problem buildProblem(Connection conn, ResultSet rs) throws Exception {
        int id = rs.getInt(Problem.ID);
        String answer = rs.getString(Problem.ANSWER);
        String resource = rs.getString(Problem.ANIMATION_RESOURCE);
        String name = rs.getString(Problem.NAME);
        boolean stratHint = rs.getBoolean(Problem.HAS_STRATEGIC_HINT);
        boolean hasVars = rs.getBoolean(Problem.HAS_VARS);
        String pname = name;
        String nname = rs.getString(Problem.NICKNAME);
        String form = rs.getString(Problem.FORM);
        String instructions = null ;
        String type = rs.getString(Problem.TYPE) ;
        boolean isExternal = rs.getBoolean(Problem.IS_EXTERNAL_ACTIVITY);
        double diff = rs.getDouble("diff_level") ;
        int video;
        try {
            video = rs.getInt("video");
            // test value video = 111;
            
        } catch (Exception e) {
            video = -1;
        }

        int exampleId = rs.getInt("example");
        if (rs.wasNull())
            exampleId = -1;
        String status = rs.getString("status");
        String t = rs.getString("questType");
        String statementHTML = rs.getString("statementHTML");
        String imgURL = rs.getString("imageURL");
        String audioRsc = rs.getString("audioResource");
        String units = rs.getString("units");
        String problemFormat = rs.getString("problemFormat");
        boolean isUsableAsExample = rs.getBoolean("usableAsExample");
        int imageFileId = rs.getInt(Problem.IMAGE_FILE_ID); // DM 1/23/18 added
        if (rs.wasNull())
            imageFileId = -1;
        int audioFileId = rs.getInt(Problem.AUDIO_FILE_ID); // DM 1/23/18 added
        if (rs.wasNull())
            audioFileId = -1;

        Problem.QuestType questType = Problem.parseType(t);
        HashMap<String, ArrayList<String>> vars = null;
        if (hasVars) {
            vars = getVarDomain(id, conn);
        }
        List<ProblemAnswer> answers =null;
        if (form != null && (questType == Problem.QuestType.shortAnswer || form.equals(Problem.QUICK_AUTH))) {
            answers = getAnswerValues(conn,id);
        }
        // perhaps its a short answer problem but not built with quickAuth
        else if (form == null && questType == Problem.QuestType.shortAnswer)
            answers = getAnswerValues(conn,id);

        String ssURL = rs.getString("screenShotURL");
        if (rs.wasNull())
            ssURL = null;

        int layoutID = rs.getInt("layoutID");
        // If no problemFormat has been set try to get from the layoutId which points into quickauthformattemplates table
        if (problemFormat == null || problemFormat.equals("") && !rs.wasNull()) {
            problemFormat = getLayoutFormat(conn, layoutID);
        }
        // No layoutID and no problemFormat means use the system default
        if (problemFormat == null) {
            problemFormat =Problem.defaultFormat ;
        }
        String prob_language = rs.getString("language");
        
        Problem p = new Problem(id, resource, answer, name, nname, stratHint,
                diff, null, form, instructions, type, status, vars, ssURL,
                questType, statementHTML, imgURL, audioRsc, units, problemFormat, imageFileId, audioFileId, isUsableAsExample,prob_language); // DM 1/23/18 added imageFileId and audioFileId

        p.setExternalActivity(isExternal);
        List<Hint> hints = DbHint.getHintsForProblem(conn,id);
        p.setHasStrategicHint(stratHint);
        p.setHints(hints);
        List<CCStandard> standards = DbProblem.getProblemStandards(conn,id);
        p.setStandards(standards);
        List<Topic> topics = DbProblem.getProblemTopics(conn,id);
        p.setTopics(topics);
        // short answer problems have a list of possible answers.
        if (answers != null)
            p.setAnswers(answers);
        allProblems.put(p.getId(), p);
//        if (exampleId == -1)
//            exampleId = (exSel != null) ? exSel.selectProblem(conn,id) : -1;
        p.setExample(exampleId);

        String vidURL = null;
        // if video is given, it is the id of a row in the video table.
        if (video == -1)
            vidURL = (vidSel != null) ? vidSel.selectVideo(conn,id) : "";
        else {
            //
            Video v= DbVideo.getVideo(conn, video);
            if (v == null) {
                System.out.println("Error: Problem " + id + " refers to video " + video + ".  Video not found");
                vidURL=null;
            }
            else vidURL = v.getUrl();
        }
        p.setVideo(vidURL);
        logger.debug("Loaded ready Problem id="+p.getId() + " name=" + p.getName() + " form=" + (p.isQuickAuth() ? "quickAuth" : type) );
        return p;
    }
    
    
    private static void updateProblemStats(Connection conn) throws Exception {
        PreparedStatement ps = buildProblemQuery(conn, null); //query all problems
        ResultSet rs = ps.executeQuery();
        try {
            while (rs.next()) {
                Problem p = buildProblem(conn, rs);
                problemIds.add(p.getId());
            }
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
        }
    }

    private static void loadAllProblems(Connection conn) throws Exception {
        loadDefaultProblemFormat(conn);
        PreparedStatement ps = buildProblemQuery(conn, null); //query all problems
        ResultSet rs = ps.executeQuery();
        try {
            while (rs.next()) {
                // Problem p = buildProblem1(conn,rs); // DM 1/23/18 changed to buildProblem1
                Problem p = buildProblem(conn, rs); // reverted to old way - Don't need to add in imageURL and audioResource from problemMediafile table
                problemIds.add(p.getId());
            }
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
        }
    }

    public static String getProblemMediaFilename (Connection conn, int pmfId) throws SQLException {
         ResultSet rs = null;
          PreparedStatement ps = null;
          try {
              String q = "select filename from ProblemMediafile where id=?";
              ps = conn.prepareStatement(q);
              ps.setInt(1, pmfId);
              rs = ps.executeQuery();
              if (rs.next()) {
                  String filename = rs.getString(1);
                  return filename;
              }
              else return null;
          } finally {
                if (ps != null)
                     ps.close();
                if (rs != null)
                     rs.close();
          }
    }


    // DM 1/23/18 Added this to patch in image and audio coming from the ProblemMediafile table.  If the Problem table has IDs
    // set for audio and img, go into this other table and get the filenames.   THen overwrite the imageURL and questionAudio fields of the Problem object
    public static Problem buildProblem1 (Connection conn,  ResultSet rs) throws Exception {
        Problem p = buildProblem(conn, rs);
        int imgFileId = p.getImageFileId();
        int audFileId = p.getAudioFileId();
        if (imgFileId != -1) {
            String imgFilename = getProblemMediaFilename(conn,imgFileId);
            // Now overwrite the Problem.imageURL with {[imgFilename]}   and quickAuth javascript will replace with a URL to the filename within problem dir
            p.setImageURL("{[" + imgFilename + "]}");
        }
        if (audFileId != -1) {

            String audFilename = getProblemMediaFilename(conn,audFileId);

            // Now overwrite the Problem.questionAudio with audFilename   and quickAuth javascript will replace with a URL to the filename within problem dir
            p.setQuestionAudio(audFilename);
        }
        return p;
    }

    public static void reloadProblem(Connection conn, int problemId) throws Exception {
        boolean notPreviouslyLoaded = false;
        if(!allProblems.containsKey(problemId)) {
            //Allowing for "re"loading an unloaded problem would require special handling
            //For now, just fail in this case rather than introduce subtle bugs
//            throw new Exception("Problem " + problemId + " was not loaded in the first place.");
            // Not sure why Rafael inserted the comment implying that a non-previously-loaded problem (i.e. a new one) cannot be
            // loaded (or reloaded) in a simple manner such as what I am now doing below - what subtle bugs?  It is possible
            // that if its marked 'ready' that users of mathspring might start seeing a new problem that is broken.  Not sure what else.
            notPreviouslyLoaded = true;
        }
        loadDefaultProblemFormat(conn);
        PreparedStatement ps = buildProblemQuery(conn, problemId);
        ResultSet rs = ps.executeQuery();
        try {
            while(rs.next()) {
//                Problem p = buildProblem1(conn, rs); // DM 1/23/18 changed to buildProblem1
                Problem p = buildProblem(conn, rs);  // no need for above - imageURL and audioResource are loaded with values by auth tool
                if (notPreviouslyLoaded)
                    allProblems.put(problemId, p);
            }
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
        }
    }

    // From the given the layoutID which is a field in the problem table, lookup the format from this id
    public static String getLayoutFormat (Connection conn, int formatID) throws SQLException {
         ResultSet rs = null;
          PreparedStatement ps = null;
          try {
              String q = "select problemFormat from quickauthformattemplates where id=?";
              ps = conn.prepareStatement(q);
              ps.setInt(1, formatID);
              rs = ps.executeQuery();
              if (rs.next()) {
                  String fmt = rs.getString(1);
                  return fmt;
              }
              else return null;
          } finally {
                if (ps != null)
                     ps.close();
                if (rs != null)
                     rs.close();
          }
    }

    private static void loadDefaultProblemFormat(Connection conn) throws SQLException {
        String query = "SELECT problemFormat FROM quickauthformattemplates WHERE id=1;";
        PreparedStatement ps = conn.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        try {
            while(rs.next()) {
                String template = rs.getString("problemFormat");
                Problem.defaultFormat = template;
            }
        } finally {
            if(ps != null) ps.close();
            if(rs != null) rs.close();
        }
    }

    private static List<ProblemAnswer> getAnswerValues(Connection conn, int id) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "select a.val,a.choiceletter,a.bindingPosition,a.order from problemanswers a where a.probid=? order by a.order";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,id);
            rs = stmt.executeQuery();
            List<ProblemAnswer> answers = new ArrayList<ProblemAnswer>();
            while (rs.next()) {
                String v= rs.getString("a.val");
                String l= rs.getString("a.choiceLetter");
                int bn= rs.getInt("a.bindingPosition");
                if (rs.wasNull())
                     bn = -1;
                int order = rs.getInt("a.order");
                answers.add(new ProblemAnswer(v,l,null,true,id, bn, order));
            }
            return answers;
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }

    /**
     * Creates a mapping from topic ids to a list of CCStandards in that topic.
     * Creates this by going through the map of topics->Problem and requesting the standards in each problem.
     * It then puts these standards into a Set (relying on duplicate removal).   The topic is then mapped to this Set.
     * @param conn
     * @throws SQLException
     */
    public static void fillTopicStandardMap(Connection conn) throws SQLException {
        Set<Integer> topicIDs = probIdsByTopic.keySet();
        for (int topicID: topicIDs) {
            Set<CCStandard> topicStandards = new TreeSet<CCStandard>();
            List<Problem> probs = getTopicProblems(topicID);
            for (Problem p: probs) {
                List<CCStandard> probStandards = p.getStandards();
                for (CCStandard probStd: probStandards) {
                    topicStandards.add(probStd);  // counting on Set to remove dupes.
                }
            }
            Topic topic = getTopicFromID(topicID);
            if (topic == null)  {
                System.out.println("Failed to find topic: " + topicID);
                continue;
            }
            topic.setCcStandards(topicStandards);
            stdsByTopic.put(topicID,topicStandards);

        }
    }

    private static Topic getTopicFromID(int topicID) {
        for (Iterator<Topic> iterator = allTopics.iterator(); iterator.hasNext(); ) {
            Topic next = iterator.next();
            if (next.getId() == topicID)
            {
                return next;
            }
        }
        return null;
    }

    public static Set<CCStandard> getTopicStandards (int topicId) {
        return stdsByTopic.get(topicId);
    }

    // For each topic get all its problems in order of difficulty and insert them into the probIdsByTopic
    // Note this correctly takes care of problems that live in more than one topic.
    public static void fillTopicProblemMap(Connection conn) throws Exception {
        String q = "select p.id,t.id from problem p, OverallProbDifficulty d, " +
                "ProbProbGroup m, problemgroup t where p.id = m.probID and t.id=m.pgroupid and t.active=1 and d.problemId = p.id and (p.status='ready' or p.status='testable' or p.status='in-progress')" +
                " order by t.id, d.diff_level";
        PreparedStatement ps = conn.prepareStatement(q);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int probId = rs.getInt(1);
            int topicId = rs.getInt(2);
            if (probIdsByTopic.get(topicId) == null) {
                probIdsByTopic.put(topicId,new ArrayList<Integer>());
            }
            probIdsByTopic.get(topicId).add(probId);
        }
        saveTopics();
    }

    /**
     * After all the problems are loaded we extract the topics (from the keyset of the map) and put them in topic
     * array sorted by id.
     */
    public static void saveTopics () {
        Set<Integer> topics = probIdsByTopic.keySet(); // no order guaranteed
        // need to guarantee an order, so we sort it
        topicIds = new int[topics.size()];
        int i=0;
        for (int topicId: topics) {
            topicIds[i++] = topicId;
        }
        Arrays.sort(topicIds);
    }

    public static int[] getTopicIds () {
        return topicIds.clone();
    }

    // returns a clone of the List because the caller may destroy the
    // contents of the list.
    public static List<Integer> getTopicProblemIds (int topicId) {
        List<Integer> l = probIdsByTopic.get(topicId);
        if (l == null)
            return null;
        else
            return (List<Integer>) probIdsByTopic.get(topicId).clone();
    }

    public static List<Problem> getTopicProblems (int topicId) {
        if (probIdsByTopic.get(topicId) == null) return new ArrayList<Problem>();
        List<Problem> l = new ArrayList<Problem>(probIdsByTopic.get(topicId).size());
        for(int i : probIdsByTopic.get(topicId)) l.add(allProblems.get(i));
        return l;
    }

    public static List<Problem> getStandardProblems (Connection conn, String ccss) throws SQLException{
        String[] standards = ccss.split(",");
        if (standards.length != 1) {
        	logger.error("Wrong number of standards " + String.valueOf(standards.length));
        }
        List<Problem> problems = new ArrayList<Problem>();
        String q = "select p.id from problem p, OverallProbDifficulty d " +
                "where d.problemId = p.id and p.status='ready' and p.standardID in ";
        String vars = "(";
        //Start at 1 to get the right number of commas
        for(int i = 1; i < standards.length; i++){
            vars = vars + "?,";
        }
        q = q + vars + "?) order by d.diff_level";
        PreparedStatement ps = conn.prepareStatement(q);
        for(int i = 1; i <= standards.length; i++){
            ps.setString(i, standards[i-1]);
        }
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int probId = rs.getInt(1);
            problems.add(getProblem(probId));                        //add to arraylist
        }

        return problems;
    }

    public static int getStandardNumProblems (Connection conn, String ccss) throws SQLException{
        String[] standards = ccss.split(",");
        List<Problem> problems = new ArrayList<Problem>();
        String q = "select count(p.id) from problem p, OverallProbDifficulty d " +
                "where d.problemId = p.id and p.status='ready' and p.standardID in ";        
        String vars = "(";
        //Start at 1 to get the right number of commas
        for(int i = 1; i < standards.length; i++){
            vars = vars + "?,";
        }
        q = q + vars + "?) order by d.diff_level";
        PreparedStatement ps = conn.prepareStatement(q);
        for(int i = 1; i <= standards.length; i++){
            ps.setString(i, standards[i-1]);
        }
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            int num = rs.getInt(1);
            return num;                        //add to arraylist
        }

        return 0;
    }

    public static List<Pair<String,Integer>> getAllStandardNumProblems(Connection conn) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            List<Pair<String,Integer>> res = new ArrayList<Pair<String,Integer>>();
            String q = "select s.idABC, count(p.id) from standard s, problem p where s.idABC=p.standardID and p.status='ready' group by m.stdid";
            stmt = conn.prepareStatement(q);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String std= rs.getString(1);
                int num = rs.getInt(2);
                if (std != null)
                    res.add(new Pair(std,num));
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

    public static Problem getProblem(int id) throws SQLException {
        if (allProblems.size() == 0)
            throw new SQLException("The ProblemMgr has no loaded problems.  Make sure the correct servlet is being run so that it gets loaded");
        return allProblems.get(id);
    }

    public static List<Problem> getAllProblems() {
        List<Problem> copy = new ArrayList<Problem>();
        for(int id : problemIds) {
            copy.add(allProblems.get(id));
        }
        return copy;
    }

    public static Problem getProblemByName(String pname) {
        for (Problem p : allProblems.values()) {
            if (p.getName().equals(pname))
                return p;
        }
        return null;
    }

    private static boolean removeProblemFromList(List<Integer> l, int probId, String probName) {
        Iterator itr = l.iterator();
        while (itr.hasNext()) {
            Problem problem = allProblems.get((Integer) itr.next());
            if (probId == problem.getId() || (probName != null && probName.equals(problem.getName()))) {
                itr.remove();
                return true;
            }
        }
        return false;
    }

    //#rezecib: supporting the dual id/name indexing is really an antipattern
    // that leads to all sorts of weird scenarios. I believe we should remove that.
    /** A request to deactivate a problem has come from an administrator.   Remove the problem from
     * the allProblems cache and from the topic map.   Also set the problem to status=deactivated in the db. */
    public static void deactivateProblem(int probId, String probName, Connection conn) throws SQLException {
        if(probName != null) {
            Problem p = getProblemByName(probName);
            //previously it iterated through problems in order and removed the first one
            //that had a matching name OR id. So if both are defined we need the min id
            //otherwise, we can just take the defined one
            if(p != null) probId = probId > -1 ? Math.min(probId, p.getId()) : p.getId();
        }
        allProblems.remove(probId);
        Collection<ArrayList<Integer>> vals= probIdsByTopic.values();
        // search all topics (a problem can be in more than one) and remove it.
        for (List<Integer> l : vals) {
            removeProblemFromList(l,probId,probName); // get it out of the topic's bucket.
        }
        DbProblem.deactivateProblem(conn,probId,probName);
    }

    /**
     * Returns all the non-testable problems in a topic
     */
    public static List<Problem> getWorkingProblems (int topicId) {
        List<Problem> all = getTopicProblems(topicId);
        if (all == null || all.size() == 0)
            return new ArrayList<Problem>();
        List<Problem> some = new ArrayList<Problem>(all.size());
        for (Problem p : all) {
            if (p.isTestProblem())
                continue;
            some.add(p);
        }
        return some;
    }

    /**
     * Return whether the topic contains ready problems.   If the  includeTestableProblems is true,
     * it will return true if there are no ready problems but some testables.
     * @param topicId
     * @param includeTestableProblems
     * @return
     * @throws SQLException
     */
    public static boolean isTopicPlayable(int topicId, boolean includeTestableProblems) throws SQLException {
        List<Problem> probs = ProblemMgr.getTopicProblems(topicId);
        if (probs == null)
            return false;
        for (Problem p : probs) {
            // if a problems isn't testable it must be ready.
            if (!p.isTestProblem())
                return true;
            else if (includeTestableProblems && p.isTestProblem())
                return true;
        }
        return false;
    }




    public static void main(String[] args) {
        DbUtil.loadDbDriver();
        try {
            Connection c = DbUtil.getAConnection("localhost");
            ProblemMgr.loadProbs(c);
            ProblemMgr.getTopicProblemIds(38);
//            System.out.println("Problem " + p);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public static List<Topic> getAllTopics () throws SQLException {
        return allTopics;
    }


    public static boolean isTestProb(int pid) throws SQLException {
        Problem p = getProblem(pid);
        if (p != null)
            return p.isTestProblem();
        else return false;
    }


}
