package edu.umass.ckc.wo.handler;


import edu.umass.ckc.wo.event.SetMRResultEvent;
import ckc.servlet.servbase.View;

import java.sql.*;


/**
 * User: marshall
 * Date: Mar 2, 2004
 * Time: 5:31:25 PM
 *
 */
public class MRHandler {
    public MRHandler() {
    }

    public View handleEvent(Connection conn, SetMRResultEvent e, int studentId, int sessionNum) throws SQLException {
        final int numrows = dbStoreValues(conn,sessionNum,studentId,e.getImageA(),e.getImageB(),Integer.parseInt(e.getSeqNum()),
                    e.getStudAnswer(),e.getStudAnswer().equals(e.getCorrectAnswer()),
                Long.parseLong(e.getProbElapsed()));
        return new View () {
            public String getView() throws Exception {
                return "ack=" + (numrows == 1);
            }
        };
    }

/**
 *
 * @param conn
 * @param sessionNum
 * @param studentId
 * @param imageA
 * @param imageB
 * @param seqNum
 * @param studentAnswer
 * @param isCorrect
 * @param timeOnProb
 * @return
 */
    private int dbStoreValues(Connection conn, int sessionNum, int studentId, String imageA, String imageB, int seqNum, String studentAnswer, boolean isCorrect, long timeOnProb) throws SQLException {

        int studAnswerInt = 0;
        // convert studentAnswer to 0 and 1
        if (studentAnswer.equalsIgnoreCase("true")) studAnswerInt = 1;
        else studAnswerInt = 0;

        String q = "INSERT INTO MRTestData (sessionId, isCorrect, studentAnswer, studId, " +
            "timeOnProblem, seqNum, imageA, imageB) VALUES (?,?,?,?,?,?,?,?)";

        PreparedStatement ps = conn.prepareStatement(q);
        ps.setInt(1,sessionNum);
        ps.setInt(2,isCorrect?1:0 );
        ps.setInt(3,studAnswerInt);
        ps.setInt(4,studentId);
        ps.setLong(5,timeOnProb);
        ps.setInt(6,seqNum);
        ps.setString(7,imageA);
        ps.setString(8,imageB);

        return ps.executeUpdate();
    }
}
