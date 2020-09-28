package edu.umass.ckc.wo.woreports;


import edu.umass.ckc.email.Emailer;
import edu.umass.ckc.wo.state.StudentState;
import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.util.WoProps;
import edu.umass.ckc.wo.db.*;
import edu.umass.ckc.wo.beans.ClassInfo;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.tutormeta.TopicMastery;
import edu.umass.ckc.wo.ttmain.ttservice.util.SendEM;

import java.sql.*;
import java.util.*;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: 9/6/11
 * Time: 11:37 AM
 * On a regular basis active students are sent an email giving them some information about problems
 * that were hard for them to solve in the last period of time.  Some of the easier problems are also sent (why?)
 * <p/>
 * Issues:
 * How frequently to send the emails?
 * - Seems like this should be an admin setting adjustable by teacher (every N days)
 * How do we know what students are active or should receive emails?
 * - Seems that a class needs a flag on it that says it's active.   Students in active classes, get emails.
 * this is better than maintaining switches on users
 * How do we determine some problems that were harder?
 * option:  grovel over the eventlog looking for problems that weren't solved, took a long time, or required lots of
 * hints
 * <p/>
 * option: augment student model with a list of problem ids  (say the hardest 3)kept in sorted order of difficulty.
 * We'd track 5 features of the problems <timeOnProblem, numHints, solutionGiven, isSolved, difficultyRating>
 * Frank 09-13-20 issue #242
 */
public class StudentStatusEmail {

    private int classId;
    private ClassInfo classInfo;
    private long period;
    private StudentState state;

    public static final double ADVANCED = 0.85;
    public static final double GOOD = 0.75;

    class TopicStats {
        int topicId;
        List<ProblemStats> probStats;

        TopicStats(int topicId, List<ProblemStats> probStats) {
            this.topicId = topicId;
            this.probStats = probStats;
        }

    }

    class ProblemStats {
        int id;
        int nAttempts;
        int nEncounters;
        int nHints;
        boolean isCorrect;
        long timeOn;
        int nskips;
        long firstActTime;

        ProblemStats(int id, int nAttempts, int nEncounters, int nHints, boolean correct, long timeOn, int nskips, long firstActTime) {
            this.id = id;
            this.nAttempts = nAttempts;
            this.nEncounters = nEncounters;
            this.nHints = nHints;
            isCorrect = correct;
            this.timeOn = timeOn;
            this.nskips = nskips;
            this.firstActTime = firstActTime;
        }

        public String toString() {
            return "prob id:" + id + " DIFF=" + difficulty(this) + " attempts=" + nAttempts + " hints=" + nHints + " skips=" + nskips + " seen=" + nEncounters +
                    " timeOn=" + timeOn + " isCorrect=" + isCorrect;
        }
    }

    public StudentStatusEmail(int classId, long period) {
        this.classId = classId;
        this.period = period;

    }

    private void setClassInfo(Connection conn) throws SQLException {
        classInfo = DbClass.getClass(conn, this.classId);
    }

    /**
     * Go through all the students in the class and get info for each one and then send an email
     * @param conn
     * @param mailServer
     * @param debug
     * @throws SQLException
     * @throws IOException
     */
    private void sendEmailToStudents(Connection conn, String mailServer, boolean debug) throws SQLException, IOException {
        ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
            String q = "select id,fname,lname,userName,email from student where classId=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, this.classId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(1);
                String fname = rs.getString(2);
                String lname = rs.getString(3);
                String uname = rs.getString(4);
                String email = rs.getString(5);
                // don't go through the work of creating a report if we haven't got an email address that is any good
                if (Emailer.isValidEmailAddress(email)) {
                    String body = getStudentEmailContent(conn, id, fname, lname, uname);
                    if (!debug) {
                        SendEM em = new SendEM();
                        em.send(conn,email,"noreply@wayangoutpost.com",  "Wayang Outpost Performance Update for "+ uname, body, Settings.mailServer);
                    }
                    else {
                        System.out.println("User Id:" + id + "\n");
                        System.out.println(body);
                        System.out.println("-----------------------");
                    }
                }
            }
        } finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }

    public String getStudentEmailContent(Connection conn, int studId, String fname, String lname, String uname) throws SQLException {
        StringBuilder sb = new StringBuilder();
        String to = "Student";
        if (fname != null && !fname.equals(""))
            to = fname;
        else if (uname != null && !uname.equals(""))
            to = uname;
        sb.append("Dear " + to + ":<br><br>\n\n");
        sb.append("This email helps you understand your progress in Wayang Outpost.  It will help you create a study plan. ");
        sb.append("Below are some resources you can use.<br><br>");

        sb.append("Your assignments since the class started<br><br>\n ");
        List<TopicMastery> topics = DbUser.getTopicMasteryLevels(conn, studId);
        for (TopicMastery t : topics) {
            if (t.isEntered()) {
                String msg = "Use the problem links below for more pactice.";
                if (t.getMastery() > ADVANCED) msg = "Congratulations!";
                else if (t.getMastery() > GOOD) msg = "Nice Job!";
                String tm = String.format("<b>%s:</b> %s (%4.1f%%) (%s)", t.getTopic().getName(), t.getMasteryInterpretation(), t.getMastery() * 100,
                        msg);
                sb.append(tm + "<br>\n");
            }
        }
        List<TopicStats> topicStats = getTopicsStats(conn, studId, period);
        sb.append("<br><br>\nHere are some particular problems on topics you seem to still be learning.   Click to see these problems " +
                "and click the Help button to walk through a series of hints.<br>\n");
        // go through the lists of ProblemStats for each topic and sort them in order of difficulty
        for (TopicStats t : topicStats)
            orderStats(t.probStats);
        int firstN = 3;
        reportOnTopics(conn, sb, topicStats, firstN);
        sb.append("<br><br>\n\nHere are some topics and problems that you did well on.  You can click on them to review or get more practice.<br>\n");
        for (TopicStats t : topicStats)
            Collections.reverse(t.probStats);
        reportOnTopics(conn, sb, topicStats, firstN);

        return sb.toString();
    }

    private void reportOnTopics(Connection conn, StringBuilder sb, List<TopicStats> topicStats, int firstN) throws SQLException {

        for (TopicStats ts : topicStats) {
            if (ts.probStats.size() == 0)
                continue;
            String topicName = DbTopics.getTopicName(conn, ts.topicId);
            sb.append("<b>"+ topicName + ":</b><br>\n");
            int i = 0;
            for (ProblemStats s : ts.probStats) {
                if (i++ < firstN) {
                    Problem p = new DbProblem().getProblem(conn, s.id);
                    String pnameLink = getProbLink(p);
                    sb.append("&nbsp;&nbsp;&nbsp;&nbsp;" + pnameLink + "<br>\n");
                } else break;
            }
        }
    }

    private String getProbLink(Problem p) {
        String url = Settings.probplayerPath + "?questionNum=";
        String name = p.getName();
        if (name.startsWith("problem_")) {
            name = name.substring(name.lastIndexOf('_') + 1);
            return "<a href=\"" + url + name + "\">" + p.getName() + "</a>";
        } else return p.getName();
    }

    private int difficulty(ProblemStats stats) {
        return stats.nAttempts + stats.nEncounters + stats.nHints + stats.nskips + (int) stats.timeOn / (60 * 1000) +
                (stats.isCorrect ? 0 : 5);
    }

    private void orderStats(List<ProblemStats> stats) {
        Comparator<ProblemStats> cmp = new Comparator<ProblemStats>() {
            // want to sort into descending order of difficulty
            public int compare(ProblemStats o1, ProblemStats o2) {
                if (difficulty(o1) == difficulty(o2))
                    return 0;
                else if (difficulty(o1) > difficulty(o2))
                    return -1;
                else return 1;
            }
        };
        Collections.sort(stats, cmp);
    }


    private List<TopicStats> getTopicsStats(Connection conn, int studId, long periodDays) throws SQLException {
        // go through all the topics that the student has entered
        ResultSet rs = null;
        PreparedStatement stmt = null;
        List<TopicStats> tstats = new ArrayList<TopicStats>();
        try {
            String q = "select topicID, entered from studenttopicmastery where studId=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, studId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int topicId = rs.getInt(1);
                boolean isEntered = rs.getBoolean(2);
                if (isEntered) {
                    List<ProblemStats> pstats = getProblemStats(conn, studId, topicId, periodDays);
                    tstats.add(new TopicStats(topicId, pstats));
                }
            }
            return tstats;
        } finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }

    private List<ProblemStats> getProblemStats(Connection conn, int studId, int topicId, long periodDays) throws SQLException {
        ResultSet rs = null;
        PreparedStatement stmt = null;

//        Timestamp now = new Timestamp(System.currentTimeMillis());
        long now = System.currentTimeMillis();
        long beginTimeMS = now - (24 * 60 * 60 * 1000 * periodDays);
        Timestamp begin = new Timestamp(beginTimeMS);  // would like to lop off the hours, min, seconds so that it just goes back to the date.


        // go through all events related to SATHUT problems worked on in the last nDays.   Do it for a given student
        // ordering by problem id so all stats ona particular problem are gathered at once.   Order by event id so they
        // come in the order logged.   throw out problems more than 6 minutes of probElapsed
        try {

            // Go through eventlog gathering events that have to do with problems within the topic that is passed to this
            // method.
            //example problems need to be excluded but they are only marked as exampleProblem on the beginProblem event
            String q = "select l.* from eventlog l, probprobgroup m where l.studid=? and l.time>=? and l.probElapsed<600000 and l.action in " +
                    "('Attempt', 'BeginProblem','EndProblem', 'Hint') and l.problemId!=999 and m.probId=l.problemId and m.pgroupId=? order by problemId, id";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, studId);
            stmt.setTimestamp(2, begin);
            stmt.setInt(3, topicId);
            rs = stmt.executeQuery();
            int lastPID = -1;
            long begTime = 0, elapsedTime = 0;
            int nEncounters = 0;
            int nAttempts = 0;
            int nHints = 0;
            int nSkips = 0;
            long probTime = 0;
            long thisProbTime = 0;
            long firstActTime = 0;
            boolean solved = false;
            List<ProblemStats> stats = new ArrayList<ProblemStats>();

            // goes through the event log in the time frame we are interested in for the given student and orders by problemId
            // so all the data for a given problem comes consecutively.
            while (rs.next()) {
                int c = rs.getInt(1);
                String action = rs.getString("action");
                int pid = rs.getInt("problemId");
                elapsedTime = rs.getLong("elapsedTime");
                boolean isCorrect = rs.getBoolean("isCorrect");
                // starting events on a different problem, so tally
                if (lastPID == -1)
                    lastPID = pid;
                if (lastPID != pid) {
                    stats.add(new ProblemStats(pid, nAttempts, nEncounters, nHints, solved, probTime, nSkips, firstActTime));
                    nAttempts = 0;
                    nEncounters = 0;
                    nHints = 0;
                    nSkips = 0;
                    solved = false;
                    firstActTime = 0;
                    thisProbTime = probTime = 0;
                    lastPID = pid;
                }
                if (action.equals("BeginProblem")) {
                    begTime = elapsedTime;
                    nEncounters++;
                    thisProbTime = 0;
                    firstActTime = 0;
                } else if (action.equals("EndProblem")) {
                    thisProbTime = elapsedTime = begTime;
                    probTime += thisProbTime;
                    // if a problem ends six seconds after beginning with no hints or attempts, we count it as a skip
                    if (probTime < 6000 && nAttempts == 0 && nHints == 0)
                        nSkips++;
                    else if (!solved)
                        nSkips++;
                } else if (action.equals("Hint")) {
                    nHints++;
                    if (firstActTime == 0)
                        firstActTime = elapsedTime - begTime;
                } else if (action.equals("Attempt")) {
                    nAttempts++;
                    if (firstActTime == 0)
                        firstActTime = elapsedTime - begTime;
                    if (isCorrect) {
                        solved = true;
                    }
                }
            }
            return stats;
        } finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }

    private void getStudentState(Connection conn, int studId) throws SQLException {
        state = new StudentState(conn, null);
        WoProps p = new WoProps(conn);
        p.load(studId);
        state.extractProps(p);

    }





    // for every student in the class, we need to send an email
    public void sendEmail(Connection conn, String mailServer, boolean debug) throws SQLException, IOException {
        setClassInfo(conn);
        sendEmailToStudents(conn, mailServer, debug);
    }



    public static void main(String[] args) {
        DbUtil.loadDbDriver();
        Connection conn = null;
        int classId = Integer.parseInt(args[0]);
        long ndays = Long.parseLong(args[1]);
        ndays = 40;
        long now = System.currentTimeMillis();
        Timestamp nowts = new Timestamp(now);
        long window =   (24 * 60 * 60 * 1000 * ndays);
        System.out.println("Ms window: " + window);
        long beginTimeMS = now - window;
        Timestamp begin = new Timestamp(beginTimeMS);  // would like to lop off the hours, min, seconds so that it just goes back to the date.
        System.out.println("Today is " + nowts.toString() + " in ms: " + now);
        System.out.println("Getting data from before " + begin.toString() + " in ms: " + beginTimeMS);

        StringBuilder sb = new StringBuilder();
        try {
            conn = DbUtil.getAConnection("cadmium.cs.umass.edu");
            StudentStatusEmail r = new StudentStatusEmail(classId, ndays);
            r.sendEmail(conn, "", true);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }
}
