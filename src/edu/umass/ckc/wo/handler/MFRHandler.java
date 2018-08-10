package edu.umass.ckc.wo.handler;


import edu.umass.ckc.wo.event.SetMFRResultEvent;
import ckc.servlet.servbase.View;

import java.sql.*;

/**
 * User: marshall
 * Date: Mar 2, 2004
 * Time: 4:06:29 PM
 */
public class MFRHandler {
    public MFRHandler() {
    }

    public View handleEvent(Connection conn, SetMFRResultEvent e, int studentId, int sessionNum) throws SQLException {
        final int numrows = dbStoreValues(conn, sessionNum, studentId, e.getProbName(), Integer.parseInt(e.getSeqNum()),
                e.getStudentAnswer(), e.getStudentAnswer().equals(e.getCorrectAnswer()),
                Long.parseLong(e.getProbElapsed()));
        return new View() {
            public String getView() throws Exception {
                return "ack=" + (numrows == 1);
            }
        };
    }

    /**
     * Save the values into the MFRTestData Table.
     *
     * @param conn
     * @param sessionNum
     * @param studentId
     * @param probName
     * @param seqNum
     * @param studentAnswer
     * @param isCorrect
     * @param timeOnProb
     * @return the result of the insert
     */
    private int dbStoreValues(Connection conn, int sessionNum, int studentId, String probName, int seqNum, String studentAnswer, boolean isCorrect, long timeOnProb) throws SQLException {
        int studAnswerInt = 0;
        // convert studentAnswer to 0 and 1
        if (studentAnswer.equalsIgnoreCase("true")) studAnswerInt = 1;
        else studAnswerInt = 0;

        String q = "INSERT INTO MFRTestData (sessionId, isCorrect, studentAnswer, studId, " +
                "timeOnProblem, seqNum, probName) VALUES (?,?,?,?,?,?,?)";

        PreparedStatement ps = conn.prepareStatement(q);
        ps.setInt(1,sessionNum);
        ps.setInt(2,isCorrect?1:0 );
        ps.setInt(3,studAnswerInt);
        ps.setInt(4,studentId);
        ps.setLong(5,timeOnProb);
        ps.setInt(6,seqNum);
        ps.setString(7,probName);

        return ps.executeUpdate();
    }


}
