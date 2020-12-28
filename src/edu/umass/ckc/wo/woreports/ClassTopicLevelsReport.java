package edu.umass.ckc.wo.woreports;

import edu.umass.ckc.servlet.servbase.View;
import edu.umass.ckc.wo.db.DbTopics;
import edu.umass.ckc.wo.db.DbUser;
import edu.umass.ckc.wo.db.DbClass;
import edu.umass.ckc.wo.beans.Topic;
import edu.umass.ckc.wo.beans.ClassInfo;
import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;
import edu.umass.ckc.wo.tutor.studmod.StudentProblemData;
import edu.umass.ckc.wo.tutor.studmod.StudentProblemHistory;
import edu.umass.ckc.wo.tutormeta.TopicMastery;
import edu.umass.ckc.wo.util.WoProps;
import edu.umass.ckc.wo.handler.ReportHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: Aug 23, 2011
 * Time: 5:40:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClassTopicLevelsReport extends DirectReport {
    public static final String RED = "#ff0000";
    public static final String YELLOW = "#ffff00";
    public static final String GREEN = "C0FF97" ; // "#00ff00";
    public static final String BLUE = "A544FA" ;//"#2DBBFF";
    public static final double LOW = 0.25;
    public static final double LOWMED = 0.50;
    public static final double MED = 0.75;
    // for each topic (ID), keep a counter of how many times students have seen a problem it
    private Hashtable<Integer,Integer> topicFrequencies = new Hashtable<Integer,Integer>();
    private List<StudentData> studentData = new ArrayList<StudentData>();
    private int teacherId;
    private int classId;

    public ClassTopicLevelsReport() {
    }

    class TopicFrequency {
        int topicId;
        int numProbsGiven;

        TopicFrequency(int topicId, int numProbsGiven) {
            this.topicId = topicId;
            this.numProbsGiven = numProbsGiven;
        }
    }

    class StudentTopicData {
        int topicId;
        double mastery;
        int solved;
        int solvedOnFirstAttempt;
        int seen;

        StudentTopicData(int topicId, double mastery, int solved, int seen, int solvedOnFirst) {
            this.topicId = topicId;
            this.mastery = mastery;
            this.solved = solved;
            this.seen = seen;
            this.solvedOnFirstAttempt = solvedOnFirst;
        }
    }

    class StudentData {
        int studId;
        String fname;
        String uname;
        String lname;
        List<StudentTopicData> topicData;

        StudentData(int studId, String fname, String uname, String lname) {
            this.studId = studId;
            this.fname = fname;
            this.uname = uname;
            this.lname = lname;
        }


    }

    private void insertLegend () {
        this.src.append("<table><tr>\n");
        this.src.append("<td bgcolor=\"" + RED + "\">&nbsp;&nbsp;</td><td>Mastery level = LOW (grade=D) </td><tr>\n");
        this.src.append("<td bgcolor=\"" + YELLOW + "\">&nbsp;&nbsp;</td><td>Mastery level = MEDIUM LOW (grade=C) </td><tr>\n");
        this.src.append("<td bgcolor=\"" + GREEN + "\">&nbsp;&nbsp;</td><td>Mastery level = MEDIUM HIGH (grade=B) </td><tr>\n");
        this.src.append("<td bgcolor=\"" + BLUE + "\">&nbsp;&nbsp;</td><td>Mastery level = HIGH  (grade=A) </td><tr>\n");
        this.src.append("</table>");
        this.src.append("<br>Each cell contains:  mastery [number solved on first attempt / number problems solved] in the topic.<br>");
        this.src.append("<br>Only topics where a student has solved 10 problems are color coded<br>");
    }

    /**
     * Once the data has been gathered this will use the topicFrequencies hash table to create a list of
     * topic ids sorted in order of how frequently each topic has been used in the class.
     * @return
     */
    List<TopicFrequency> getTopicOrder () {
        List<TopicFrequency> topicOrder = new ArrayList<TopicFrequency>();
        Set<Map.Entry<Integer,Integer>> entries = this.topicFrequencies.entrySet();
        for (Map.Entry<Integer,Integer> e: entries) {
            TopicFrequency f = new TopicFrequency(e.getKey(),e.getValue());
            topicOrder.add(f);
        }
        Comparator<TopicFrequency> cmp = new Comparator<TopicFrequency>() {

            public int compare(TopicFrequency o1, TopicFrequency o2) {
                if (o1.numProbsGiven == o2.numProbsGiven) return 0;
                else if (o1.numProbsGiven > o2.numProbsGiven) return -1;
                else return 1;
            }
        };
        Collections.sort(topicOrder,cmp);  // sorts into ascending order by numProblemsGiven
        return topicOrder;
    }

    public View createReport(Connection conn, int classId, AdminViewReportEvent e, HttpServletRequest req, HttpServletResponse response) throws Exception {
        this.classId = classId;
        ClassInfo cl = DbClass.getClass(conn,classId);
        this.teacherId = cl.getTeachid();
        String neck = "<table border=1 cellspacing=1 cellpadding=1 class=\"example altstripe sort02 table-autostripe table-autosort:2 table-stripeclass:alternate2\">\n" +
                "<thead>\n"+
                " <tr>\n" +
                "  <th class=\"table-sortable:default\">Student Name</th>\n" +
                "  <th class=\"table-sortable:default\">User Name</th>\n";
        List<Topic> topics = DbTopics.getAllTopics(conn);
        // initialize tracking of most commonly used topics
        for (Topic t: topics) {
            topicFrequencies.put(t.getId(),0);
        }
        // get all the data for the students
        getTopicData(conn,classId);
        List<TopicFrequency> orderedTopics= getTopicOrder();  // this gives the topic ids in the order of most usage
        StringBuilder headers = new StringBuilder();
        // build header list
        for (TopicFrequency f: orderedTopics) {
            String tname = DbTopics.getTopicName(conn,f.topicId, classId);
            String url = getLinkToClassTopicMasteryTrajectoryReport(tname,f.topicId);
            headers.append("<th class=\"table-sortable:numeric\">" + url + "</th>\n");
        }
        neck += headers.toString();
        neck = neck.concat(" </tr></thead>\n");

        neck.concat("<tbody id='data'>\n");
        // write the data table into neck
        neck += writeTopicData(orderedTopics);



        String className = getClassName(cl);

        this.src.append(generateHeader2("Class Topic Mastery Level Report (23) - " + className));
        this.src.append("<h3>Topic Mastery for each student in class: " + className + "</h3>\n");
        insertLegend();
        this.src.append(neck);
        addNavLinks(classId,cl.getTeachid());

        getTopicData(conn,classId);
        this.src.append(foot);
        return this;
    }

    private String getLinkToClassTopicMasteryTrajectoryReport (String label, int topicId) {
        String link = getLinkToReport("AdminViewReport",teacherId,classId, ReportHandler.CLASS_TOPIC_MASTERY_TRAJECTORY_HTML,AdminViewReportEvent.SHOW_REPORT);
        link += "&extraParam="+ Integer.toString(topicId);
        return String.format("<a href=\"" +link+ "\">%s</a>",label);
    }

    private String getLinkToTopicMasteryTrajectoryReport (double mastery, String label, int studId,  int topicId) {
        String link = getLinkToStudentReport("AdminViewReport",teacherId,classId,studId, ReportHandler.STUDENT_TOPIC_MASTERY_TRAJECTORY_HTML,
                AdminViewReportEvent.SHOW_REPORT, Integer.toString(topicId));
        return String.format("<a href=\"" +link+ "\">%5.2f [%s]</a>",mastery,label);    
    }

    private String getLinkToAllTopicMasteryTrajectoryReport (String uname, int studId) {
        String link = getLinkToStudentReport("AdminViewReport",teacherId,classId, studId, ReportHandler.STUDENT_ALL_TOPICS_MASTERY_TRAJECTORY_HTML, AdminViewReportEvent.SHOW_REPORT, null);
        return String.format("<a href=\"" +link+ "\">%s</a>",uname);
    }

    // Takes a List of topicIds that have been sorted based on the frequency of problems seen
    public String writeTopicData (List<TopicFrequency> orderedTopics) {
        StringBuilder sb = new StringBuilder();
        for (StudentData sd: this.studentData) {
            sb.append("<tr>");
            String uNameLink = getLinkToAllTopicMasteryTrajectoryReport(sd.uname,sd.studId);
            sb.append("<td>"+sd.fname + " " + sd.lname+ "</td><td>" + uNameLink + "</td>");   // links to a topic mastery trajectory report for all topics for this user
            // Get the students topic stats in the order prescribed by the orderedTopics parameter
            for (TopicFrequency f: orderedTopics) {
                StudentTopicData td = getStudentTopicData(sd.topicData,f.topicId);
                if (td != null && td.seen > 0) {
                    String label = td.solvedOnFirstAttempt + "/" + td.solved;
                    // only color-code cells where 10 problems have been solved in the topic. (per Ivon request 1/5/16)
                    if (td.solved >= 10)
                        // the data is a link to a topic mastery trajectory report for this student on this topic
                        sb.append("<td bgcolor=\"" + getColor(td.mastery) + "\">"+ getLinkToTopicMasteryTrajectoryReport(td.mastery, label, sd.studId,f.topicId) + "</td>");
                    else sb.append("<td>"+ getLinkToTopicMasteryTrajectoryReport(td.mastery, label, sd.studId,f.topicId) + "</td>");
                }
                else
                    sb.append("<td></td>");
            }

            sb.append("</tr>");
        }
        sb.append("</table>");
        return sb.toString();
    }

    private StudentTopicData getStudentTopicData(List<StudentTopicData> topicData, int topicId) {
        for (StudentTopicData d: topicData) {
            if (d.topicId == topicId)
                return d;
        }
        return null;
    }

    public void getTopicData (Connection conn, int classId) throws SQLException {
        WoProps woProps;



        // we need to fetch the topicMasteryLevels for each student in the class.
        // This is a list of Strings- one for each topic in the problemgroup table
        List<Topic> topics = DbTopics.getAllTopics(conn);
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "SELECT Student.id, Student.fname, Student.lname, username  " +
                "FROM Student " +
                "WHERE Student.trialUser=0 and Student.classId = ? ORDER BY Student.id;";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,classId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int studId= rs.getInt(1);
                String fn = rs.getString(2);
                String ln = rs.getString(3);
                String un = rs.getString(4);
                StudentData sd = new StudentData(studId,fn,un,ln);
                this.studentData.add(sd);
                StudentProblemHistory hist = new StudentProblemHistory(conn,studId);
//                woProps = new WoProps(conn);
//                woProps.load(studId);   // get all properties for studId
//                StudentState state = new StudentState(conn, null);
//                state.extractProps(woProps);

//                System.out.println(studId + " Solved Problems: " + state.getSatHutProblemsSolved());
//                System.out.println(studId + " Given Problems: " + state.getSatHutProblemsGiven());

                List<TopicMastery> mastery = DbUser.getTopicMasteryLevels(conn,studId);
//                this.src.append("<tr>");
//                this.src.append("<td>"+fn + " " + ln+ "</td><td>" + un + "</td>");
                List<StudentTopicData> topicData = new ArrayList<StudentTopicData>();
                sd.topicData = topicData;
                for (TopicMastery m : mastery) {
                    if (m.isEntered()) {
                        List<StudentProblemData> data = hist.getTopicHistory(m.getTopic().getId());
                        List<String> solved = hist.getTopicProblemsSolved(m.getTopic().getId());
                        List<String> solved1 = hist.getTopicProblemsSolvedOnFirstAttempt(m.getTopic().getId());
                        List<String> seen  = hist.getTopicProblemsSeen(m.getTopic().getId());
                        int x= topicFrequencies.get(m.getTopic().getId())+ seen.size();
                        topicFrequencies.put(m.getTopic().getId(),x);
                        String label = solved.size() + "/" + seen.size();
                        topicData.add(new StudentTopicData(m.getTopic().getId(),m.getMastery(),solved.size(),seen.size(), solved1.size()));
//                        this.src.append("<td bgcolor=\"" + getColor(m.updateMastery()) + "\">"+ String.format("%5.2f [%s]",m.updateMastery(), label) + "</td>");
                    }
                    else {
                        topicData.add(new StudentTopicData(m.getTopic().getId(),-1,-1,-1, -1));
//                        this.src.append("<td></td>");
                    }
                }
//                this.src.append("</tr>\n");
            }
//            this.src.append("</table>\n");
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }

    private String getColor (double mastery) {
        if (mastery < LOW)
            return RED;
        else if (mastery < LOWMED)
            return YELLOW;
        else if (mastery < MED)
            return GREEN;
        else return BLUE;
    }

    public static void main(String[] args) {
        ClassTopicLevelsReport r = new ClassTopicLevelsReport();
        try {
            r.getTopicData(r.getConnection(),383);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
