package edu.umass.ckc.wo.myprogress;

import edu.umass.ckc.wo.beans.Topic;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.db.DbTopics;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.tutor.probSel.TopicLoader;
import edu.umass.ckc.wo.tutor.studmod.StudentProblemData;
import edu.umass.ckc.wo.tutor.studmod.StudentProblemHistory;
import edu.umass.ckc.wo.tutormeta.ProblemSelector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



/**
 * Copyright (c) University of Massachusetts
 */
public class StudentSummary {

    /*
    *
    *  the variables of a student in the topic
    *
    */


    int studId;
    String studentName;

    int classId;

    int sessionId;
    long elapsedTime;
    Connection conn;



    double masteryThreshold=0.8;

    int problemsSolved;
    int  topicsDone;
    int daysSinceLastSession;
    int problemsDoneInLastSession;
    int totalMasteredTopics;
    int topicsMasteredInLastSession;
    int numberOfDaysOfPractice;

    
    List<String> listOfDays=new ArrayList();
    List<dayDetails> dayDetailsList = new ArrayList<dayDetails>();


    List<String> topicsList= new ArrayList();
    List<Float> topicMasteryList= new ArrayList();

    List<String> TopicListMasteredInLastSession= new ArrayList();
    // output variables



    public StudentSummary(SessionManager smgr) throws Exception {

        studId = smgr.getStudentId();
        classId = smgr.getClassID();
        conn = smgr.getConnection();
        sessionId = smgr.getSessionNum();
        elapsedTime = smgr.getElapsedTime();



        StudentProblemHistory h = smgr.getStudentModel().getStudentProblemHistory();


        this.studentName = retrieveStudentName();
        this.problemsSolved = retrieveProblemsSolved();
        retrieveTopicsList();
        this.daysSinceLastSession=retrieveDaysSinceLastSession();
        this.problemsDoneInLastSession=retrieveProblemsDoneInLastSession();
        this.totalMasteredTopics=retrieveTotalMasteredTopics();
        this.topicsMasteredInLastSession=retrieveTopicsMasteredInLastSession();
        retrieveTopicListMasteredInLastSession();


        retrieveListOfDays();
        populateDayDetails();
        


    }





    private String retrieveStudentName() throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select userName from student where id=?";
            ps = conn.prepareStatement(q);
            ps.setInt(1, studId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
            return "";
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
        }
    }


    private int retrieveProblemsSolved() throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = " SELECT count(*) FROM `studentproblemhistory` where studId=? and effort IN ('SOF', 'SHINT', 'ATT', 'BOTTOMOUT', 'GUESS')";

            ps = conn.prepareStatement(q);
            ps.setInt(1, studId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
        }


    }


    private void retrieveTopicsList() throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;

        String q = " select count(*) from (SELECT distinct topicId FROM `studentproblemhistory` where  studId=? and effort IN ('SOF', 'SHINT', 'ATT', 'BOTTOMOUT', 'GUESS') ) as s";

        ps = conn.prepareStatement(q);
        ps.setInt(1, studId);
        rs = ps.executeQuery();
        if (rs.next()) {
            topicsDone=rs.getInt(1);
        }


        q = "SELECT problemgroup.description, s.mastery FROM `problemgroup`\n" +
            "INNER JOIN (SELECT s1.studId, s1.topicId, s1.mastery FROM `studentproblemhistory` s1\n" +
                "INNER JOIN (SELECT studId, topicId, max(problemBeginTime) AS lastProblemBeginTime FROM `studentproblemhistory`\n" +
                    "WHERE studId=? GROUP BY topicId) AS s2\n" +
                "ON s1.studId = s2.studId AND s1.problemBeginTime = lastProblemBeginTime) AS s\n" +
            "ON problemgroup.id = s.topicId";

        ps = conn.prepareStatement(q);
        ps.setInt(1, studId);
        rs = ps.executeQuery();
        while (rs.next()) {
            topicsList.add(rs.getString(1));
            topicMasteryList.add(rs.getFloat(2));
        }
    }

    //welcome message variables

     private int retrieveDaysSinceLastSession() throws Exception {
       PreparedStatement ps = null;
       ResultSet rs = null;
       try {
           String q = " SELECT DATEDIFF(NOW(),max(problemBeginTime)) from studentProblemHistory where studId= ?";

           ps = conn.prepareStatement(q);
           ps.setInt(1, studId);
           rs = ps.executeQuery();
           if (rs.next()) {
               return rs.getInt(1);
           }
           return 0;
       } finally {
           if (rs != null)
               rs.close();
           if (ps != null)
               ps.close();
       }


   }

    private int retrieveProblemsDoneInLastSession() throws Exception {
       PreparedStatement ps = null;
       ResultSet rs = null;
       try {
           String q = " SELECT COUNT( * )\n" +
                   "    FROM studentProblemhistory\n" +
                   "    WHERE sessionId = (\n" +
                   "            SELECT MAX( sessionId )\n" +
                   "    FROM  `studentproblemhistory`\n" +
                   "    WHERE studId =? )\n" +
                   "    AND effort IN ('SOF', 'SHINT', 'ATT', 'BOTTOMOUT', 'GUESS')";

           ps = conn.prepareStatement(q);
           ps.setInt(1, studId);
           rs = ps.executeQuery();
           if (rs.next()) {
               return rs.getInt(1);
           }
           return 0;
       } finally {
           if (rs != null)
               rs.close();
           if (ps != null)
               ps.close();
       }
   }

    private int retrieveTotalMasteredTopics() throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q =  "SELECT count(*)\n" +
                        "FROM `studentproblemhistory` s1\n" +
                        "  INNER JOIN\n" +
                        "    (SELECT studId, topicId, max(problemBeginTime) as lastProblemBeginTime\n" +
                        "    FROM `studentproblemhistory` \n" +
                        "    WHERE studId=?\n" +
                        "    GROUP BY topicId) AS s2\n" +
                        "  ON s1.studId = s2.studId AND s1.problemBeginTime = lastProblemBeginTime\n" +
                        "WHERE s1.mastery>=?";

            ps = conn.prepareStatement(q);
            ps.setInt(1, studId);
            ps.setDouble(2, masteryThreshold);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } finally {
           if (rs != null)
               rs.close();
           if (ps != null)
               ps.close();
        }
   }


   private int retrieveTopicsMasteredInLastSession() throws Exception {
       PreparedStatement ps = null;
       ResultSet rs = null;
       try {
           String q = " select count(*) from (SELECT distinct(topicId) FROM `studentproblemhistory` where mastery>=? and sessionId=(select max(sessionId) from studentProblemHistory where studId=?) and studId=?) as s";


           ps = conn.prepareStatement(q);
           ps.setDouble(1, masteryThreshold);
           ps.setInt(2, studId);
           ps.setInt(3, studId);
           rs = ps.executeQuery();
           if (rs.next()) {
               return rs.getInt(1);
           }
           return 0;
       } finally {
           if (rs != null)
               rs.close();
           if (ps != null)
               ps.close();
       }
   }

   private void retrieveTopicListMasteredInLastSession() throws Exception {
       PreparedStatement ps = null;
       ResultSet rs = null;
       try {
           String q = " select problemGroup.description from\n" +
                   "    problemgroup\n" +
                   "    INNER JOIN (SELECT distinct(topicId) as topicId FROM `studentproblemhistory` where mastery>=? and sessionId=(select max(sessionId) from studentProblemHistory where studId=?) and studId=?) as s\n" +
                   "    On problemgroup.id=s.topicId";

           ps = conn.prepareStatement(q);
           ps.setDouble(1, masteryThreshold);
           ps.setDouble(2, studId);
           ps.setDouble(3, studId);
           rs = ps.executeQuery();
           while (rs.next()) {
               TopicListMasteredInLastSession.add(rs.getString(1));
           }

       } finally {
           if (rs != null)
               rs.close();
           if (ps != null)
               ps.close();
       }
   }

    private void retrieveListOfDays() throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select distinct(date(problemBeginTime)) as day from studentProblemHistory where studId=? ORDER BY (date(problemBeginTime))  ASC";
            ps = conn.prepareStatement(q);
            ps.setDouble(1, studId);


            rs = ps.executeQuery();
            while (rs.next()) {
                listOfDays.add(rs.getString(1));
            }



        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
        }
    }


    private void populateDayDetails() throws Exception {

        PreparedStatement ps = null;
        ResultSet rs = null;

        String dayString;
        int problemsDone=0;
        int topicsDone=0;


        try {

            for (int i=0; i<listOfDays.size();i++){

                List<String> topics_List= new ArrayList();
                List<Float> topicMastery_List= new ArrayList();

                dayString= listOfDays.get(i);

                String q = "SELECT count(*) FROM `studentproblemhistory` where studId=? and effort IN ('SOF', 'SHINT', 'ATT', 'BOTTOMOUT', 'GUESS') and date(problemBeginTime)<=?";
                ps = conn.prepareStatement(q);
                ps.setDouble(1, studId);
                ps.setString(2, dayString);


                rs = ps.executeQuery();
                if (rs.next()) {

                    problemsDone=rs.getInt(1);

                }


                q = " select count(*) from (SELECT distinct topicId FROM `studentproblemhistory` where  studId=? and effort IN ('SOF', 'SHINT', 'ATT', 'BOTTOMOUT', 'GUESS') and date(problemBeginTime)<=?) as s";

                ps = conn.prepareStatement(q);
                ps.setInt(1, studId);
                ps.setString(2, dayString);

                rs = ps.executeQuery();
                if (rs.next()) {
                    topicsDone=rs.getInt(1);
                }


                q = "SELECT DISTINCT problemgroup.description,  ss.mastery FROM  problemgroup\n" +
                    "INNER JOIN\n" +
                    "  (SELECT s1.studId, s1.topicId, s1.mastery\n" +
                    "  FROM studentproblemhistory s1\n" +
                    "    INNER JOIN\n" +
                    "    (SELECT studId, topicId, MAX(problemBeginTime) AS lastProblemBeginTime\n" +
                    "    FROM studentproblemhistory\n" +
                    "    WHERE studId=? AND effort IN ('SOF', 'SHINT', 'ATT', 'BOTTOMOUT', 'GUESS') AND date(problemBeginTime)<=?\n" +
                    "    GROUP BY topicId) AS s2\n" +
                    "  ON s1.studId = s2.studId AND s1.problemBeginTime = lastProblemBeginTime) AS ss\n" +
                    "ON problemgroup.id = ss.topicId";

                ps = conn.prepareStatement(q);
                ps.setInt(1, studId);
                ps.setString(2, dayString);

                rs = ps.executeQuery();
                while (rs.next()) {
                    topics_List.add(rs.getString(1));
                    topicMastery_List.add(rs.getFloat(2));
                }


                dayDetails newDayDetails = new dayDetails(i, dayString,  problemsDone,  topicsDone,  topics_List,  topicMastery_List);
                dayDetailsList.add(newDayDetails);

            }

        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
        }
    }





    public String getStudentName() {
       return studentName;
     }

    public double getMasteryThreshold() {
        return masteryThreshold;
    }

   public int getProblemsSolved() {
       return problemsSolved;
   }

   public int getTopicsDone() {
       return topicsDone;
   }

   public int getDaysSinceLastSession() {
       return daysSinceLastSession;
   }

   public int getProblemsDoneInLastSession() {
       return problemsDoneInLastSession;
   }

   public int getTotalMasteredTopics() {
       return totalMasteredTopics;
   }

   public int getTopicsMasteredInLastSession() {
       return topicsMasteredInLastSession;
   }

   public List<String> getTopicsList() {
       return topicsList;
   }

    public List<Float> getTopicMasteryList() {
        return topicMasteryList;
    }

   public List<String> getTopicListMasteredInLastSession() {
       return TopicListMasteredInLastSession;
   }

    public List<dayDetails> getDayDetailsList() {
        return dayDetailsList;
    }

  }