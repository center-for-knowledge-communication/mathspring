package edu.umass.ckc.wo.woreports;

import edu.umass.ckc.wo.state.PrePostState;
import edu.umass.ckc.wo.tutor.studmod.AffectStudentModel;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.List;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

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
public class PipQuery extends DirectReport {

    int numPreGiven;
    int numPreCorrect;
    int numPreIncorrect;
    int numPostGiven;
    int numPostCorrect;
    int numPostIncorrect;
    String preProblemSet;
    String postProblemSet;
    double pre_pct;
    double post_pct;
    double gain;

    private int version;
    boolean firstCall = true;
    FileWriter fw;
    public int studId;
    Connection conn;
    List studIds;
    private int noEpiData=0;
    private int noOutput=0;

    public PipQuery(Connection conn) {
        this.conn = conn;
    }


    public void createReportForOneId(Connection conn) throws Exception {
        fw = new FileWriter("U:\\wo\\test1.csv");
        List rows = doSelect(conn, studId);
        for (int i = 0; i < rows.size(); i++) {
            String[] r = (String[]) rows.get(i);
            String dataRow = convertToCSV(r) + "\n";
            fw.write(dataRow);
            if (i > rows.size() - 2)
                System.out.println(dataRow);
        }
        fw.write("\n");
        fw.close();
    }

    public void createReportForIds(Connection conn) throws Exception {
        fw = new FileWriter("j:\\web\\wo\\pipData20060411.csv");
        int nStudsOutput=0;
        for (int j = 0; j < studIds.size(); j++) {
            String id = (String) studIds.get(j);
            int sid = Integer.parseInt(id);
            List rows = doSelect(conn, sid);
            if (rows.size() > 0)
                nStudsOutput++;
            for (int i = 0; i < rows.size(); i++) {
                String[] r = (String[]) rows.get(i);
                String dataRow = convertToCSV(r) + "\n";
                fw.write(dataRow);
            }
        }
        System.out.println("No epi data for " + noEpiData + " students");
        System.out.println("EpiData found but no output for " + noOutput + " students");
        System.out.println("Wrote data to csv file for " + nStudsOutput + " students.");
        fw.write("\n");
        fw.close();
    }



    private void setPrePostFeatures (Connection conn, int studId) throws SQLException {
        // default value of pre/post features if not found in either place
        numPreCorrect=-1;
        numPreIncorrect=-1;
        numPostCorrect=-1;
        numPostIncorrect=-1;
        numPreGiven = -1;
        numPostGiven = -1;
        version=-1;
        preProblemSet=null;
        postProblemSet=null;


        List preTestProbsGiven = getWoPropVals(conn,studId, PrePostState.PRE_TEST_PROBLEMS_GIVEN);
        if (preTestProbsGiven.size() > 0) {
            numPreGiven =   preTestProbsGiven.size();
            preProblemSet = getWoPropVal(conn,studId,PrePostState.PRE_TEST_PROBLEM_SET);
            postProblemSet = getWoPropVal(conn,studId,PrePostState.POST_TEST_PROBLEM_SET);
            String numPreCorrectstr = getWoPropVal(conn,studId, AffectStudentModel.PRE_TEST_NUM_CORRECT);
            if (numPreCorrectstr != null) {
                numPreCorrect = Integer.parseInt(numPreCorrectstr);
            }
            String numPreIncorrectstr = getWoPropVal(conn,studId, AffectStudentModel.PRE_TEST_NUM_INCORRECT);
            if (numPreIncorrectstr != null)
                numPreIncorrect = Integer.parseInt(numPreIncorrectstr);
            List postTestProbsGiven = getWoPropVals(conn,studId,PrePostState.POST_TEST_PROBLEMS_GIVEN);
            numPostGiven =   postTestProbsGiven.size();
            String numPostCorrectstr = getWoPropVal(conn,studId, AffectStudentModel.POST_TEST_NUM_CORRECT);
            if (numPostCorrectstr != null)
                numPostCorrect = Integer.parseInt(numPostCorrectstr);
            String numPostIncorrectstr = getWoPropVal(conn,studId, AffectStudentModel.POST_TEST_NUM_INCORRECT);
            if (numPostIncorrectstr != null)
                numPostIncorrect = Integer.parseInt(numPostIncorrectstr);
            pre_pct = (numPreGiven != 0) ? ((double) numPreCorrect / (double) numPreGiven) : 0.0;
            post_pct = (numPostGiven != 0) ? ((double)numPostCorrect / (double)numPostGiven) : 0.0;
            gain = (pre_pct != 0) ? ((post_pct - pre_pct) /  pre_pct) : 0.0;

        }
        // try to find it in pencilandpaperdata
        else {
            String q = "select gain,pre_cor,pre_pct,pos_cor,pos_pct,version from pencilpaperdata where studId=?";
            PreparedStatement ps = conn.prepareStatement(q);
            ps.setInt(1,studId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                gain = rs.getDouble(1);
                numPreCorrect = rs.getInt(2);
                pre_pct = rs.getDouble(3);
                numPostCorrect = rs.getInt(4);
                post_pct = rs.getDouble(5);
                version=rs.getInt(6);
            }
        }
    }

    private String getWoPropVal (Connection conn, int objid, String prop) throws SQLException {
        String q = "select value from woproperty where objid=? and property=?";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setInt(1, objid);
        ps.setString(2,prop);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String v = rs.getString(1);
            return v;
        }
        return null;
    }

    private List getWoPropVals (Connection conn, int objid, String prop) throws SQLException {
        String q = "select value from woproperty where objid=? and property=? order by position";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setInt(1, objid);
        ps.setString(2,prop);
        List l = new ArrayList();
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String v = rs.getString(1);
            l.add(v);
        }
        return l;
    }

    //todo need to read from text file (extract from email that Ivon sent you) of ids and create
    // a list of ids and then call doSelect with
    // each Id.
    //
    public void createReport(Connection conn, int classId) throws Exception {
        fw = new FileWriter("U:\\wo\\pipData" + classId + ".csv");
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // todo replace this query with reading studIds from file
            String q = "select studId from pencilpaperdata where studId>0  ";
            ps = conn.prepareStatement(q);
            rs = ps.executeQuery();
            int j = 0;
            // this loop is what dumps data about a given studID
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
//        PreparedStatement ps = null;
//        ResultSet rs = null;
        TallyState ts = new TallyState();
        ts.init(conn);
        List output = new ArrayList();
        if (firstCall) {
            output.add(ts.getColHeaders());
            firstCall = false;
        }

        // omit pre/post problem log entries
        String q = "select activityName, problemId, action, userInput, isCorrect, elapsedTime, probElapsed, hintStep, hintId" +
                " from episodicdata2 where studId=? " +
                "order by sessNum, elapsedTime,id";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setInt(1, studId);
        ResultSet rs = ps.executeQuery();
        int excelRowNum = 2;
        boolean gotEpiData = false;
        boolean createdOutput=false;
        while (rs.next()) {
            String actName = rs.getString("activityName");
            // skip pretest or posttest data.
            if (actName != null && (actName.equals("pretestProblem") || actName.equals("posttestProblem")))
                continue;
            gotEpiData = true;
//                int sid = rs.getInt("studId");
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
            double diff = getDiffLevel(conn, pid);
            setPrePostFeatures(conn,studId);
            ts.tally(conn, studId, pid, action, userInput, isCorrect == 1, elapsedTime, probElapsed, hintStep, hintId, diff, this.pre_pct, this.gain, this.version, excelRowNum++,
                    post_pct, numPreGiven, numPostGiven, numPreCorrect, numPreIncorrect, numPostCorrect, numPostIncorrect, preProblemSet, postProblemSet);
            if (ts.isOutputReady()) {
                output.add(ts.outputRow());
                createdOutput=true;
            }
        }
        if (!gotEpiData) {
            noEpiData++;
            System.out.println("No episodic data for student " + studId);
        }
        if (gotEpiData && !createdOutput)  {
            noOutput++;
            System.out.println("Got Epidata but No CSV output for student " + studId);
        }
        return output;

    }

    private double getDiffLevel(Connection conn, int pid) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("select diff_level from  overallprobdifficulty where problemId=?");
        ps.setInt(1, pid);
        ResultSet rs = ps.executeQuery();
        try {
            if (rs.next()) {
                double d = rs.getDouble(1);
                return d;
            }
            return -1;
        } finally {
            rs.close();
            ps.close();
        }


    }

    private void query(Connection conn) throws SQLException {

        PreparedStatement ps = conn.prepareStatement("select a.problemId, action, userInput, isCorrect, elapsedTime, probElapsed, hintStep, hintId, diff_level from episodicdata2 a, overallprobdifficulty b where studId=? and a.problemId=b.problemId order by sessNum, elapsedTime");
        ps.setInt(1, studId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            System.out.println("got data");
            int pid = rs.getInt("problemId");
            System.out.println("PID " + pid);
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
        }
    }

    private void test(Connection conn) throws SQLException {

//        PreparedStatement ps = conn.prepareStatement("select a.problemId, action, userInput, isCorrect, elapsedTime, probElapsed, hintStep, hintId, diff_level from episodicdata2 a, overallprobdifficulty b where studId=? and a.problemId=b.problemId order by sessNum, elapsedTime");
        PreparedStatement ps = conn.prepareStatement("select * from episodicdata2 where studId=?");
        ps.setInt(1, studId);
        ResultSet rs = ps.executeQuery();
        int i = 0;
        while (rs.next()) {
            System.out.print(".");
        }
        System.out.println("done");
        rs.close();
    }

    private void test2(Connection conn) throws SQLException {

//        PreparedStatement ps = conn.prepareStatement("select a.problemId, action, userInput, isCorrect, elapsedTime, probElapsed, hintStep, hintId, diff_level from episodicdata2 a, overallprobdifficulty b where studId=? and a.problemId=b.problemId order by sessNum, elapsedTime");
        PreparedStatement ps = conn.prepareStatement("select value from woproperty where objid=1500 and property='db'");
        ResultSet rs = ps.executeQuery();
        int i = 0;
        while (rs.next()) {
            System.out.print("db location " + rs.getString(1));
        }
        System.out.println("done");
        rs.close();
    }

    private static void loadDbDriver() {
        String dbDriver = "com.mysql.jdbc.Driver";
        try {
            Driver d = (Driver) Class.forName(dbDriver).newInstance(); // MySql
            System.out.println(d);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private static Connection getAConnection() throws SQLException {
        String dbPrefix = "jdbc:mysql";
//        String dbHost = "localhost";
        String dbHost = "cadmium.cs.umass.edu";
//        String dbHost = "128.119.243.30";
        String dbSource = "wayangoutpostdb";
        String dbUser = "WayangServer";
        String dbPassword = "jupiter";

        String url;
        if (dbPrefix.equals("jdbc:mysql"))
            url = dbPrefix + "://" + dbHost + "/" + dbSource; // preferred by MySQL
        else // JDBCODBCBridge
            url = dbPrefix + ":" + dbSource;
//        url = "jdbc:mysql://localhost:3306/test";
//        url = "jdbc:mysql://localhost/rashidb"; // this works
        try {
            System.out.println("connecting to db on url " + url);
            return DriverManager.getConnection(url, dbUser, dbPassword);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw e;
        }
    }

    private static Connection getAConnection2() throws SQLException {
        String dbPrefix = "jdbc:mysql";
//        String dbHost = "localhost";
        String dbHost = "cadmium.cs.umass.edu";
//        String dbHost = "128.119.243.30";
        String dbName = "wayangoutpostdb";
        String dbUser = "WayangServer";
        String dbPassword = "jupiter";


        MysqlDataSource ds = new MysqlDataSource();
        ds.setServerName(dbHost);
        ds.setDatabaseName(dbName);

        try {
            return ds.getConnection(dbUser, dbPassword);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw e;
        }
    }

    public static void main(String[] args) {

        try {
            loadDbDriver();
            PipQuery r8 = new PipQuery(getAConnection2());
//            r8.studId = 3065;
            r8.studId = 1680;

            r8.readIdFile("U:\\wo\\pippin\\studIds.txt");
            r8.createReportForIds(r8.conn);

//            r8.createReportForOneId(r8.conn);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void readIdFile(String s) throws IOException {
        ArrayList result = new ArrayList();
        FileReader fr = new FileReader(s);
        BufferedReader br = new BufferedReader(fr);
        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            result.add(line);
        }
        studIds = result;
        System.out.println("Read in ID file.  " + result.size() + " student IDs read.");
    }


}
