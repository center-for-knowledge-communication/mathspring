package edu.umass.ckc.wo.log;

import edu.umass.ckc.wo.util.CSVParser;

import java.sql.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: Nov 19, 2009
 * Time: 10:50:04 AM
 * To change this template use File | Settings | File Templates.
 */
public class EmotionInterventionInputLogger implements AuxilaryEventLogger {

    private String emotion;
    private int level;
    private String explanation; // user-entered text
    private String reasons;  // CSV of the checkboxes the user selected as reasons they feel a certain way

    public static final String TABLE = "EmotionInterventionResponse";
    /**
     *
     * @param emotion
     * @param level
     * @param explanation the user-entered text
     * @param reasons the selected reasons
     */
    public EmotionInterventionInputLogger(String emotion, int level, String explanation, List<String> reasons) {
        this.emotion = emotion;
        this.level = level;
        this.explanation = explanation;
        this.reasons = CSVParser.createCSV(reasons);
    }

    public String getAuxTable() {
        return TABLE;
    }

    public int logEntry(Connection conn) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "insert into "+TABLE+" (emotion, level, explanation, reasons) values (?,?,?,?)";
            stmt = conn.prepareStatement(q, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1,emotion);
            stmt.setInt(2,level);
            stmt.setString(3,explanation);
            stmt.setString(4,reasons);
            stmt.execute();
            rs = stmt.getGeneratedKeys();
            rs.next();
            return rs.getInt(1);
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        finally {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
        }
    }
}
