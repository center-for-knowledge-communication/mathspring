package edu.umass.ckc.wo.log;

import edu.umass.ckc.wo.woreports.util.EventLogEntryObjects;
import edu.umass.ckc.wo.woreports.util.StudentProblemHistoryObjects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Neeraj on 2/4/2017.
 */
public class CompleteEventDataLogger {
    private Connection connnection;

    /** ALL SQL QUERIES REALTED TO LOGGING */
    private static  String  SELECT_EVENT_LOG = "select * from eventlog where";
    private static  String  STUDENT_PROBLEM_HISTORY = "select * from studentproblemhistory where";


    public CompleteEventDataLogger(Connection conn) {
        this.connnection = conn;
    }

    public boolean publishDeveloperNotes(String[] formValuesToBePublished,int studId, int sessionNum){
        try {
            String insertNote = "insert into testernotes"+
                    "(id,studId,sessionNum,type,comments) values (?,?,?,?,?)";
            PreparedStatement preparedStmt = connnection.prepareStatement(insertNote);
            preparedStmt.setInt(1,Integer.valueOf(formValuesToBePublished[0]));
            preparedStmt.setInt(2,studId);
            preparedStmt.setInt(3,sessionNum);
            preparedStmt.setString(4,formValuesToBePublished[2]);
            preparedStmt.setString(5,formValuesToBePublished[1]);
            preparedStmt.execute();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public List<EventLogEntryObjects> getLatestSnapShotsForEventLog(int studId, int sessionNum) throws SQLException {

        String eventLogQuery = SELECT_EVENT_LOG+" studId=? and sessNum=? order by id desc";
        List<EventLogEntryObjects> completeUserLogEntries = new ArrayList<EventLogEntryObjects>();
        PreparedStatement preparedStmt = null;

        try {
            preparedStmt = connnection.prepareStatement(eventLogQuery);
            preparedStmt.setInt(1,studId);
            preparedStmt.setInt(2,sessionNum);
            ResultSet resultSet = preparedStmt.executeQuery();
            while(resultSet.next()){
                EventLogEntryObjects ulObjects =  new EventLogEntryObjects(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getInt(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getInt(6),
                        resultSet.getInt(7),
                        resultSet.getInt(8),
                        resultSet.getInt(9),
                        resultSet.getString(10),
                        resultSet.getInt(11),
                        resultSet.getString(12),
                        resultSet.getString(13),
                        resultSet.getInt(14),
                        resultSet.getString(15),
                        resultSet.getTimestamp(16),
                        resultSet.getInt(17),
                        resultSet.getString(18),
                        resultSet.getTimestamp(19)
                );
                completeUserLogEntries.add(ulObjects);
            }
            return completeUserLogEntries;
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (preparedStmt != null)
                preparedStmt.close();
            if (preparedStmt != null)
                preparedStmt.close();
        }
        return null;
    }

    public List<StudentProblemHistoryObjects> loadStudentProblemHistoryforCurrentSession(int studId,int sessionId) throws SQLException {
        ResultSet rs=null;
        PreparedStatement ps=null;
        List<StudentProblemHistoryObjects> history = new ArrayList<StudentProblemHistoryObjects>();
        try {
            String q = STUDENT_PROBLEM_HISTORY+" studId=? and sessionId=? order by ID desc";
            ps = connnection.prepareStatement(q);
            ps.setInt(1,studId );
            ps.setInt(2,sessionId );
            rs = ps.executeQuery();
            while (rs.next()) {
                StudentProblemHistoryObjects d = new StudentProblemHistoryObjects(
                        rs.getInt("ID"),
                        rs.getInt("studId"),
                rs.getInt("sessionId"),
                rs.getInt("problemId"),
                rs.getInt("topicId"),
                rs.getTimestamp("problemBeginTime"),
                rs.getTimestamp("problemEndTime"),
                rs.getInt("timeInSession"),
                        rs.getInt("timeInTutor"),
                rs.getInt("timeToFirstAttempt"),
                rs.getInt("timeToFirstHint"),
                        rs.getInt("timeToSolve"),
                rs.getInt("numMistakes"),
                rs.getInt("numHints"),
                rs.getInt("videoSeen"),
                rs.getInt("numAttemptsToSolve"),
                rs.getInt("solutionHintGiven"),
                rs.getString("mode"),
                rs.getFloat("mastery"),
                        rs.getString("emotionAfter"),
                 rs.getInt("emotionLevel"),
                 rs.getString("effort"),
                 rs.getInt("exampleSeen"),
                 rs.getInt("textReaderUsed"),
                 rs.getInt("numHintsBeforeSolve"),
                 rs.getInt("isSolved"),
                        rs.getString("adminFlag"),
                        rs.getString("authorFlag"),
                        rs.getInt("collaboratedWith"),

                rs.getInt("timeToSecondAttempt"),
                rs.getInt("timeToThirdAttempt"),
                        rs.getInt("timeToSecondHint"),
                rs.getInt("timeToThirdHint"),
              rs.getDouble("probDiff"));

                history.add(d);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
        }
        return  history;
    }
}
