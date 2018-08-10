package edu.umass.ckc.wo.db;

import edu.umass.ckc.wo.beans.PretestPool;
import edu.umass.ckc.wo.content.PrePostProblemDefn;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: Mar 9, 2009
 * Time: 11:23:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class DbPrePost {

    public static final String PRETEST = "pretest";
    public static final String POSTTEST = "posttest";


        /**
       * Given a problemId return a PrePostProblem object or null if it doesn't exist
       * @param conn
       * @param probId
       * @return
       * @throws SQLException
       */
      public static PrePostProblemDefn getPrePostProblem (Connection conn, int probId) throws SQLException {
          PreparedStatement ps = null;
          ResultSet rs = null;
          try {
//              String q = "select problemSet,name,url,description,answer,ansType,aChoice,bChoice,cChoice,dChoice,eChoice," +
              String q = "select id, name,url,description,answer,ansType,aChoice,bChoice,cChoice,dChoice,eChoice," +
                      "aURL,bURL,cURL,dURL,eURL,waitTimeSecs,imageFilename from PrePostProblem where id=?";
              ps = conn.prepareStatement(q);
              ps.setInt(1, probId);
              rs = ps.executeQuery();
              if (rs.next()) {
//                  int problemSet = rs.getInt(1); // DM 3/09 don't want this anymore
                  int problemSet = -1; // give a bogus value.
                  int id = rs.getInt(1);       // added to take place of problemSet as item 1 which preserves #s below
                  String name = rs.getString(2);
                  String url = rs.getString(3);
                  if (rs.wasNull())
                      url = null;
                  String description = rs.getString(4);
                  if (rs.wasNull())
                      description = null;
                  String answer = rs.getString(5);
                  if (rs.wasNull())
                      answer = null;
                  int ansType = rs.getInt(6);
                  String aURL = null, bURL = null, cURL = null, dURL = null, eURL = null;
                  String aChoice = null, bChoice = null, cChoice = null, dChoice = null, eChoice = null;
                  int waitTimeSecs=0;
//                  Blob img=null; // DM 1/18 images now stored as files
                  waitTimeSecs= rs.getInt(17) ;
                  if (rs.wasNull())
                      waitTimeSecs=-1;
//                  img = rs.getBlob(18);
                  String imgFilename = rs.getString(18);
                  PrePostProblemDefn p;
                  if (ansType == PrePostProblemDefn.SHORT_ANSWER) {
                      waitTimeSecs= rs.getInt(17) ;
                      if (rs.wasNull())
                          waitTimeSecs=0;
                  }
                  else {
                      aChoice = rs.getString(7);
                      if (rs.wasNull())
                          aChoice = null;
                      bChoice = rs.getString(8);
                      if (rs.wasNull())
                          bChoice = null;
                      cChoice = rs.getString(9);
                      if (rs.wasNull())
                          cChoice = null;
                      dChoice = rs.getString(10);
                      if (rs.wasNull())
                          dChoice = null;
                      eChoice = rs.getString(11);
                      if (rs.wasNull())
                          eChoice = null;
                      aURL = rs.getString(12);
                      if (rs.wasNull())
                          aURL = null;
                      bURL = rs.getString(13);
                      if (rs.wasNull())
                          bURL = null;
                      cURL = rs.getString(14);
                      if (rs.wasNull())
                          cURL = null;
                      dURL = rs.getString(15);
                      if (rs.wasNull())
                          dURL = null;
                      eURL = rs.getString(16);
                      if (rs.wasNull())
                          eURL = null;



                  }
              return new PrePostProblemDefn(probId, name, description, url, ansType, answer, problemSet, aChoice, bChoice, cChoice,
                          dChoice, eChoice, aURL, bURL, cURL, dURL, eURL, waitTimeSecs, imgFilename);
              }
          } catch (SQLException e) {
              e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
          } finally {
              if (rs != null)
                  rs.close();
              if (ps != null)
              ps.close();
          }
          return null;
      }



    public static List<PretestPool> getPools(Connection conn) throws SQLException {
        List<PretestPool> pools = new ArrayList<PretestPool>();
        // check for id > 0 is way to avoid a sentinel value in the first row (id =0)
        String q = "select id,description from prepostpool where isActive=1 and id>0";
        PreparedStatement ps = conn.prepareStatement(q);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int id = rs.getInt(1);
            String descr = rs.getString(2);
            pools.add(new PretestPool(id,descr));
        }
        return pools;
    }


    public static Map<Integer,String> getActiveSurveyList(Connection conn) throws SQLException {
        Map<Integer,String> activeSurveys = new HashMap<Integer,String>();
        String q = "select * from preposttest where isActive = 1";
        PreparedStatement ps = conn.prepareStatement(q);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int surveyId = rs.getInt("id");
            String surveyName = rs.getString("name");
            activeSurveys.put(surveyId,surveyName);
        }
        return activeSurveys;
    }

    public static String[] getActivatedSurveyIdsForClass(Connection conn, int classId) throws SQLException {
        String[] activeSurveys = new String[2];
        String q = "select pretest,posttest from classconfig where classId = ?";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setInt(1,classId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String preSurverId = rs.getString("pretest");
            String postServeyId = rs.getString("posttest");
            if(preSurverId == null)
                preSurverId = "";
            if(postServeyId == null)
                postServeyId = "";
            activeSurveys[0] = preSurverId;
            activeSurveys[1] = postServeyId;
        }
        return activeSurveys;
    }

    public static PretestPool getPretestPool (Connection conn, int classId) throws SQLException {
        String q = "select pretestPoolId from class where Id=?";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setInt(1,classId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            int poolid = rs.getInt(1);
            q = "select description from prepostpool where id=?";
            PreparedStatement ps2  = conn.prepareStatement(q);
            ps2.setInt(1,poolid);
            ResultSet rs2 = ps2.executeQuery();
            if (rs2.next()) {
                String descr = rs2.getString(1);
                return new PretestPool(poolid,descr);
            }
            else return null;
        }
        else return null;

    }

    public static List<PretestPool> getAllPretestPools (Connection conn) throws SQLException {
        List<PretestPool> pools = DbPrePost.getPools(conn);
        return pools;
    }

    /**
     * Get all the prepost test ids that are in the given pool
     * @param conn
     * @param poolId
     * @return
     * @throws SQLException
     */
    public static List<Integer> getTestsInPool (Connection conn, int poolId) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            List<Integer> testIds = new ArrayList<Integer>();
            String q = "select id from preposttest where poolId=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,poolId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int testId= rs.getInt(1);
                testIds.add(testId);
            }
            return testIds;
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }

    public static void storeStudentAnswer(Connection conn, int sessId, int studentId, int probId, String userAnswer,
                                          String testType, int timeOnProb, boolean isCorrect, int seqNum) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "insert into preposttestdata (sessionId,probId,studentAnswer, studId, testType, isCorrect, timeOnProblem, seqNum) values (?,?,?,?,?,?,?,?)";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,sessId);
            stmt.setInt(2,probId);
            stmt.setString(3,userAnswer);
            stmt.setInt(4,studentId);
            stmt.setString(5,testType);
            stmt.setBoolean(6,isCorrect);
            stmt.setInt(7,timeOnProb);
            stmt.setInt(8,seqNum);
            stmt.execute();

        }

        finally {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
        }
    }

    // positions are zero based.
    public static PrePostProblemDefn getPrePosttestProblem(Connection conn, int pretestId, int position) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "select probId from prepostproblemtestmap where testid=? and position=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,pretestId);
            stmt.setInt(2,position);
            rs = stmt.executeQuery();
            if (rs.next()) {
                int probId= rs.getInt(1);
                return getPrePostProblem(conn,probId);
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

    /**
     * Counts the number of questions in a pre or post test.
     * @param conn
     * @param testId
     * @return
     * @throws SQLException
     */
    public static int getPrePostTestNumProblems(Connection conn, int testId) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "select count(*) from prepostproblemtestmap where testid=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,testId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                int c= rs.getInt(1);
                return c;
            }
            else return -1;
        } finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }

    public static int getPrePostTestNumSolvableProblems(Connection conn, int testId) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "select count(*) from prepostproblemtestmap m, prepostproblem p where m.testid=? and m.probId=p.id and p.answer is not null";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,testId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                int c= rs.getInt(1);
                return c;
            }
            else return -1;
        } finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }

    /**
     * Counts the number of questions a student has completed in a pre or post test
     * @param conn
     * @param testId
     * @param studentId
     * @param testType  either "pretest" or "posttest"
     * @return
     * @throws SQLException
     */
    public static int getStudentCompletedNumProblems(Connection conn, int testId, int studentId, String testType) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "select count(*) from preposttestdata d, prepostproblemtestmap m where d.studId=? and d.probId = m.probId and m.testid=? and d.testType=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,studentId);
            stmt.setInt(2,testId);
            stmt.setString(3,testType);
            rs = stmt.executeQuery();
            if (rs.next()) {
                int c= rs.getInt(1);
                return c;
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


    public static int getStudentCorrectNumProblems(Connection conn, int testId, int studentId, String testType) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "select sum(d.iscorrect) from preposttestdata d, prepostproblemtestmap m, prepostproblem p where d.studId=? and d.probId = m.probId and p.id = m.probId and p.answer is not null and m.testid=? and d.testType=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,studentId);
            stmt.setInt(2,testId);
            stmt.setString(3,testType);
            rs = stmt.executeQuery();
            if (rs.next()) {
                int numCorrect= rs.getInt(1);
                return numCorrect;
            }
            return 0;
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }

    public static boolean isTestActive(Connection conn, int testId) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "select isactive from preposttest where id=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, testId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                boolean b= rs.getBoolean(1);
                return b;
            }
            return false;
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }

    public static int getTest (Connection conn, String testName) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "select id from preposttest where name=?";
            stmt = conn.prepareStatement(q);
            stmt.setString(1, testName);
            rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt(1);
                return id;
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
}
