package edu.umass.ckc.wo.myprogress;

import edu.umass.ckc.wo.beans.Topic;
import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.content.CCStandard;
import edu.umass.ckc.wo.db.DbClass;
import edu.umass.ckc.wo.db.DbUser;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.tutor.pedModel.TopicSelectorImpl;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.tutor.probSel.TopicModelParameters;
import edu.umass.ckc.wo.tutor.studmod.StudentProblemData;
import edu.umass.ckc.wo.tutor.studmod.StudentProblemHistory;
import edu.umass.ckc.wo.tutormeta.TopicSelector;
import edu.umass.ckc.wo.util.SqlQuery;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Copyright (c) University of Massachusetts
 */
public class TopicDetails {

    /*
    *
    *  the variables of a student in the topic
    *
    */
    Topic topic;

    int studId;

    int topicId;
    String topicName;
    String topicState = "";
    int classId;

    int sessionId;
    String client = null;

    String effortBuffer = "";

    Connection conn;

    TopicSelector curTopicLoader;
    Problem curProblem;

    List<problemDetails> problemDetailsList = new ArrayList<problemDetails>();


    int numOfVariables = 4;


    double masteryThreshold = 0.75;
    double problemDifficultyThreshold = 0.65;


    int classID;
    int currentProblemId;
    int problemId;
    String problemName;

    int problemsSolved=0;
    int totalProblems = 0;
    int masteredTimes;


    private int goBackTill = 5;
    int limitValue = 5;


    // output variables
    double mastery = 0;

    public TopicDetails(SessionManager smgr, int topic_id) throws Exception {


        topicId = topic_id;
        studId = smgr.getStudentId();
        classId = smgr.getClassID();

        //topicName=t.getName();

        conn = smgr.getConnection();

        curTopicLoader = new TopicSelectorImpl(smgr, (TopicModelParameters) DbClass.getClassLessonModelParameters(conn,classId));
        // gets the problems in this topic minus omitted ones.
        List<Integer> problemIdList = curTopicLoader.getClassTopicProblems(topicId, classId, DbUser.isShowTestControls(conn,smgr.getStudentId()));
        StudentProblemHistory h = smgr.getStudentModel().getStudentProblemHistory();

        List<Problem> problemList = new ArrayList<Problem>();
        for (int id : problemIdList)
            problemList.add(ProblemMgr.getProblem(id));
        totalProblems = problemList.size();
        // For each problem in the topic, get the students last encounter with it as a practice problem.
        // NOTE WELL:  A problem can be in more than one topic, so we may find an encounter with it as part of another
        // topic.   We want to create a problem-card in the topic-detail that reflects the interaction with the problem
        // regardless of what topic it was seen in.

        // If found,  put a detail record in the result list, else add an empty record for the problem.
        //  This will have the effect of not counting anything twice.
        for (Problem p : problemList) {
            List<CCStandard> stds = p.getStandards();
            StudentProblemData lastEncounter = h.getMostRecentPracticeProblemEncounter(p.getId());
            problemDetails pd;
            if (lastEncounter != null) {
                pd = new problemDetails(p.getId(), p.getName(), lastEncounter.getEffort(), lastEncounter.getNumAttemptsToSolve(),
                        lastEncounter.getNumHints(), getSnapshot(p.getId()), lastEncounter.isSolved());
                problemsSolved += lastEncounter.isSolved() ? 1 : 0;
            }
            else
                pd= new problemDetails(p.getId(),p.getName(),"empty",0,0,
                        getSnapshot(p.getId()), stds, false);
            problemDetailsList.add(pd);

        }
    }


    public Topic getTopic() {
        return topic;
    }

    public int getTopicId() {
        return topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public double getMastery() {
        return mastery;
    }


    public List<problemDetails> getProblemDetailsList() {

        return problemDetailsList;
    }

    public byte[] getSnapshot (int problemId) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            Blob img ;
            byte[] imgData = null ;
            String q = "select snapshot from Problem where id=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,problemId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                img = rs.getBlob(1);
                if (img != null)
                    imgData = img.getBytes(1,(int)img.length());
            }
            return imgData;
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }

    public int getProblemsSolved() {
        return problemsSolved;
    }

    public int getTotalProblems() {
        return totalProblems;
    }
}
