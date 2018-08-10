package edu.umass.ckc.wo.woreports;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.ArrayList;
import java.io.FileWriter;

/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Jun 20, 2005
 * Time: 3:35:50 PM
 * <p/>
 * produce a report for a full trajectory of a student at the grain of each transaction in epidata2.  Columns are:
 * studid
 * prob seq #
 * pretest score (as %)
 * total time spent in hints
 * skills involved in curr problem (30 bits)
 * # mistakes in curr prob
 * total # hints in soln path
 * number hints seen for curr prob
 * 30 counters (corresponding to (skill array above) of how many hints have been seen on those skills
 * curr prob difficulty
 */
public class Report8 extends DirectReport {

    private double gain;
    private double pre_pct;
    private int version;
    boolean firstCall=true;
    FileWriter fw ;

    public Report8() {

    }


    public void createReport(Connection conn, int classId) throws Exception {
        fw = new FileWriter("U:\\wo\\pipdata.csv");
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select studId from pencilpaperdata where studId>0  ";
            ps = conn.prepareStatement(q);
            rs = ps.executeQuery();
            int j=0;
            while (rs.next()) {
                int studId = rs.getInt(1);
                List rows = doSelect(conn, studId);
                for (int i = 0; i < rows.size(); i++) {
                    String[] r = (String[]) rows.get(i);
                    String dataRow = convertToCSV(r) + "\n";
                    fw.write(dataRow);
                }
            }
        } finally {
            rs.close();
            ps.close();
        }


    }

    private String convertToCSV(String[] r) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < r.length; i++) {
            String s = r[i];
            if (i == r.length - 1)
                i = i + 0;
            sb.append(s);
            if (i < r.length - 1)
                sb.append(",");

        }
        return sb.toString();
    }


    private List doSelect(Connection conn, int studId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        TallyState ts = new TallyState();
        ts.init(conn);
        List output = new ArrayList();
        if (firstCall) {
            output.add(ts.getColHeaders());
            firstCall=false;
        }
        try {
            String q = "select studId, a.problemId, action, userInput, isCorrect, elapsedTime, probElapsed," +
                    " hintStep, hintId, diff_level from episodicdata2 a, overallprobdifficulty b where " +
                    "a.problemId=b.problemId and studId=? order by sessNum, elapsedTime";
            ps = conn.prepareStatement(q);
            ps.setInt(1, studId);
            rs = ps.executeQuery();
            while (rs.next()) {
                int sid = rs.getInt("studId");
                int pid = rs.getInt("problemId");
                String action = rs.getString("action");
                String userInput = rs.getString("userInput");
                if (rs.wasNull())
                    userInput = null;
                int isCorrect = rs.getInt("isCorrect");
                int elapsedTime = rs.getInt("elapsedTime");
                int probElapsed = rs.getInt("probElapsed");
                String hintStep = rs.getString("hintStep");
                if (rs.wasNull())
                    hintStep = null;
                int hintId = rs.getInt("hintId");
                if (rs.wasNull())
                    hintId = -1;
                if (userInput != null && userInput.equals("help") && hintStep == null)
                    System.out.println("no hint");
                double diff = rs.getDouble("diff_level");
//                ts.tally(conn, sid, pid, action, userInput, isCorrect == 1, elapsedTime, probElapsed, hintStep, hintId, diff, this.pre_pct, this.gain, this.version, 0, x, x, x, x, x, x, x, x, y);
                if (ts.isOutputReady())
                    output.add(ts.outputRow());
            }
            return output;
        } finally {
//            rs.close();
//            ps.close();
        }
    }


    public static void main(String[] args) {

        Report8 r8 = new Report8();
        try {
            r8.createReport(r8.conn, -1);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


}
