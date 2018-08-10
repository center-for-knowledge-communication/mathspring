package edu.umass.ckc.wo.handler;

import edu.umass.ckc.wo.event.SetPrePostResultEvent;
import edu.umass.ckc.servlet.servbase.View;

import java.sql.*;


/**
 * User: marshall
 * Date: Mar 2, 2004
 * Time: 5:08:51 PM
 */
public class PrePostHandler {

    public View handleEvent(Connection conn, SetPrePostResultEvent e, int studentId, int sessionNum) throws SQLException {
        final int numrows = dbStoreValues(conn, sessionNum, studentId, e.getProbName(), e.getTestType(), Integer.parseInt(e.getSeqNum()),
                e.getStudAnswer(), Boolean.getBoolean(e.getSkipped()), Boolean.getBoolean(e.getCorrect()),
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
    private int dbStoreValues(Connection conn, int sessionNum, int studentId, String probName,
                                  String testType, int seqNum, String studentAnswer,
                                  boolean skipped, boolean isCorrect, long timeOnProb
                                  ) throws SQLException {

        // convert test types to int values; pre=1, post=2, unknown=0
        int tType = 0;
        if (testType.equalsIgnoreCase("pre")) {
            tType = 1;
        } else if (testType.equalsIgnoreCase("post")) {
            tType = 2;
        } else {
            System.out.println("Unknown pre/post test type");
            tType = 0;
        }

        String q = "INSERT INTO PrePostTestData (sessionId,  probName, isCorrect, studentAnswer, " +
                "studId, testType, timeOnProblem, seqNum, skipped) VALUES (?,?,?,?,?,?,?,?,?)";

        PreparedStatement ps = conn.prepareStatement(q);
        ps.setInt(1,sessionNum);
        ps.setString(2,probName);
        ps.setInt(3,isCorrect?1:0 );
        ps.setString(4,studentAnswer);
        ps.setInt(5,studentId);
        ps.setInt(6,tType);
        ps.setLong(7,timeOnProb);
        ps.setInt(8,seqNum);
        ps.setInt(9,skipped?1:0);

        return ps.executeUpdate();
    }

}
