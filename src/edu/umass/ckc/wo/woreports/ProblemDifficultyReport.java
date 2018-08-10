package edu.umass.ckc.wo.woreports;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

//import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;
import edu.umass.ckc.servlet.servbase.View;
import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;

import edu.umass.ckc.wo.beans.ClassInfo;
import edu.umass.ckc.wo.db.DbClass;
import edu.umass.ckc.wo.db.DbUtil;
import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.util.ProbPlayer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * useless comment
 * Class summary per problem.  Which problems are challenging for your students.
 * User: marshall. Date: Mar 2, 2004
 * Modifications: Ivon. August 17, 2006.
 * This report provides summaries PER PROBLEM, for a whole class. It highlights problems that are hard for students in a class.
 * This report can be called from top level or from another report that passes it a
 * a topic (semiabsskillid) which then limits it to just the problems that involve a particular topic
 * <p/>
 * Type 2 report means that this was passed in a topic and will limit results to only those
 * problems on the given topic.
 * <p/>
 * Problem Difficulty Report for class XXX
 * (if topic passed in) For Topic:  TTT
 * <p/>
 * For each problem: We produce information about how the class performed on it.
 */
public class ProblemDifficultyReport extends Report {

    private int semiabsskillId = -1;
    private int clusterId = -1;
    public static final int RED = 3;
    public static final int YELLOW = 2;
    public static final int OK = 0;
    public static final String REDCODE = "#FF0000";
    public static final String YELLOWCODE = "#FFFF00";
    public static final String GREYCODE = "#C0C0C0";
    public static final int MIN_STUDENTS_SEEN_FOR_REPORTING=3;
    public static final int COMP_TIME_1 = 100;
    public static final int COMP_TIME_2 = 75;
    public static final double PERCENT_REPEAT_UP_1 = .75;
    public static final double PERCENT_REPEAT_UP_2 = .50;
    public static final double PERCENT_GIVE_UP_1 = .75;
    public static final double PERCENT_GIVE_UP_2 = .50;
    public static final double PERCENT_SKIP_1 = .75;
    public static final double PERCENT_SKIP_2 = .50;
    public static final double PERCENT_EVENTUALLY_SOLVE_1 = .50;
    public static final double PERCENT_EVENTUALLY_SOLVE_2 = .75;
    public static final double PERCENT_TRY1_SOLVE_1 = .10;
    public static final double PERCENT_TRY1_SOLVE_2 = .20;
    public static final double PERCENT_TRY2_SOLVE_1 = .10;
    public static final double PERCENT_TRY2_SOLVE_2 = .20;
    public static final double NUM_HINTS_1 = 2;
    public static final double NUM_HINTS_2 = 1;




    private enum ProbStats {
        THINK_TIME, NUM_HINTS, NUM_INCORRECT, NUM_CORRECT_ATTEMPT1
    }

    ;
    String topicName = null; // null if all topics
    String clusterName = null; // null if all topics

//    class Stats {
//        int probId;
//        int nStudsSeen;
//        int nStudsRepeated;
//        int nStudsSkipped;
//        int nStudsGiveUp;
//        int nStudsSolved;
//        int nStudsSolvedOnEncounter1Attempt1;
//        int nStudsSolvedOnEncounter1Attempt2;
//    }

    class ProblemInfo {
        String name;
        int id;
        String topic;
        int nStudsSeen;
        int nStudsRepeated;
        int nStudsSkipped;
        int nStudsGiveUp;
        int nStudsSolved;
        int nStudsSolvedOnEncounter1Attempt1;
        int nStudsSolvedOnEncounter1Attempt2;
        double avgNumHintsOnLastEncounter;
        double avgComprehensionTime;
        String mostFreqIncorrectAns;
        int rating;
        String probType;
        String animationResource;

    }

    private Map<Integer, ProblemInfo> probData = new HashMap<Integer, ProblemInfo>();

    public ProblemDifficultyReport() {
    }

    public View createReport(Connection conn, int classid, int semiabsskillid, AdminViewReportEvent e, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        semiabsskillId = semiabsskillid;
        return createReport(conn, classid, e, req, resp);
    }

    public View createClusterReport(Connection conn, int classid, int clusterId, AdminViewReportEvent e, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        this.clusterId = clusterId;
        return createReport(conn, classid, e, req, resp);
    }

    public String getClusterName (Connection conn, int clusterId) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "select displayname from cluster where id=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,clusterId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                String n = rs.getString(1);
                return n;
            }
            return null;
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }

    public String getTopicName(Connection conn, int semiAbsSkillID) throws SQLException {
        ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
            String q = "select name from semiabstractskill where id=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, semiAbsSkillID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                String topicName = rs.getString(1);
                return topicName;
            }
            return null;
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }

    /*
    Columns in Report

    Problem Name , Topic (if not type2), #students seen (unique students),
  */
    public String getColumnHeaders() {
        String tabColHeader = "<table border=1 cellspacing=1 cellpadding=1 class=\"example altstripe sort02 table-autostripe table-autosort:2 table-stripeclass:alternate2\">\n" +
                "<thead>\n   <tr>\n" +
                "  <th class=\"table-sortable:numeric\"># Red</th>\n" +
                "  <th class=\"table-sortable:numeric\"># Colored</th>\n" +
                "  <th class=\"table-sortable:default\">Problem</th>\n" +
                "  <th class=\"table-sortable:numeric\">Number of Students Who have seen it</th>\n" +
                "  <th class=\"table-sortable:numeric\">Percentage of Students Who've repeated the problem</th>\n" +
                "  <th class=\"table-sortable:numeric\">Percentage of Students who skipped</th>\n" +
                "  <th class=\"table-sortable:numeric\">Percentage of Students who gave up</th>\n" +
                "  <th class=\"table-sortable:numeric\">Percentage of Students who eventually solved it</th>\n" +
                "  <th class=\"table-sortable:numeric\">Percentage of Students solved on first try</th>\n" +
                "  <th class=\"table-sortable:numeric\">Percentage of Students solved on second try</th>\n" +
                "  <th class=\"table-sortable:default\">Most frequent incorrect response</th>\n" +
                "  <th class=\"table-sortable:numeric\">Avg Number of hints on last encounter</th>\n" +
                "  <th class=\"table-sortable:numeric\">Avg comprehension time on solving encounter</th>\n" +
                "  <th class=\"table-sortable:numeric\">ID</th>\n" +
                " </tr></thead>\n" +
                "<tbody id='data'>\n";
        return tabColHeader;
    }

    public View createReport(Connection conn, int classId, AdminViewReportEvent e, HttpServletRequest req, HttpServletResponse response) throws Exception {


        String rept = getColumnHeaders();
        ClassInfo cl = DbClass.getClass(conn, classId);
        String className = getClassName(cl);
        String table = getEventLogTable(cl);
        String q;
        if (semiabsskillId == -1 && clusterId == -1)
            q = "select * from problem where status='ready'";
        else  if (clusterId != -1)
            q = "select p.* from problem p, probstdmap m, standard s where p.id=m.probId and s.id=m.stdId and s.clusterId=? and p.status='ready'";
        else q = "select * from problem where id in (select distinct problemid\n" +
                "from hint h, skill s\n" +
                "where problemid>0 and h.skillid=s.id\n" +
                "and s.semiabsskillid=?\n" +
                "order by problemid)";
        PreparedStatement ps = conn.prepareStatement(q);
        if (semiabsskillId != -1)
            ps.setInt(1, semiabsskillId);
        else if (clusterId != -1)
            ps.setInt(1,clusterId);
        if (semiabsskillId == -1 && clusterId == -1) {
            this.src.append(generateHeader3("4: Problem Difficulty Report - " + className));
            this.src.append("<h3>Problem Difficulty Report for " + className + "</h3>\n");
        }
        else if (clusterId != -1) {
            this.clusterName = getClusterName(conn, clusterId);
            this.src.append(generateHeader3("4: Problem Difficulty Report - " + className + " for problems in cluster: " + clusterName));
            this.src.append("<H3>Problem Difficulty Report - " + className + " for problems in cluster: " + clusterName + "</h3>\n");

        }

        else {
            topicName = getTopicName(conn, semiabsskillId);
            this.src.append(generateHeader3("4: Problem Difficulty Report - " + className + " for problems in topic: " + topicName));
            this.src.append("<H3>Problem Difficulty Report - " + className + " for problems in topic: " + topicName + "</h3>\n");

        }
        this.src.append("<table width=\"557\"> \n");
        this.src.append("<tr> \n <td bgcolor=\"" +GREYCODE+ "\" width=\"45\">&nbsp;</td>\n");
        this.src.append("     <td width=\"496\">Problems that less than 4 students saw</td> \n </tr> ");
//        this.src.append("<tr> \n <td bgcolor=#FFFF00 width=\"45\">&nbsp;</td>\n");
//        this.src.append("     <td width=\"496\">Problems that your students found challenging </td> \n </tr> ");
        this.src.append("</table>");

        this.src.append(rept);
        addNavLinks(classId,cl.getTeachid());


        generateTableBody(conn, classId, e.getTeacherId(), req, table, ps);

        this.src.append(foot);
        return this;
    }//report 4

    /*
       This is given a statement that will return all problem rows or problems within a given
       topic.

       For each problem: We produce information about how the class performed on it.
       Columns in Report

 Problem Name , Topic (if not type2), #students seen (unique students),
     */
    private void generateTableBody(Connection conn, int classId, int teacherId, HttpServletRequest req, String table, PreparedStatement ps) throws SQLException {
        ResultSet rs = ps.executeQuery();
//        List<ProblemInfo> probs = new ArrayList<ProblemInfo>();
        // Iterating through all problems or all problems w/in a topic
        while (rs.next()) {
            ProblemInfo probInf = new ProblemInfo();
//            probs.add(probInf);
            int pid = rs.getInt("id");
            String t = rs.getString("type");
            probInf.probType = t;
            String form = rs.getString("form");
            String ar = rs.getString("animationResource");
            probInf.animationResource = ar;
            probData.put(pid, probInf); // insert into hash map
            String probName = rs.getString("nickname");
            String flashProbName = rs.getString("name");
            probInf.name = flashProbName + ": " + probName;
            String probnumber = "null";
            if (flashProbName.length() > 8)
                probnumber = flashProbName.substring(8);
            else
                System.out.println("Warning: Problem name " + probName + " is badly formed");
            String probPlayerURL = ProbPlayer.getURLToProbPreviewer();
            // TODO a Hack to fulfill a request by Ivon for using a different player to show problems in this report.
            probPlayerURL = probPlayerURL.replace("probplayer","problem_checker");
            String link = probPlayerURL + "?questionNum=" + probnumber;
            String onclick = "";
            if(form != null && form.equalsIgnoreCase("quickAuth")) {
                link = "#";
                onclick = "onclick=\"window.open('%s/WoAdmin?action=AdminGetQuickAuthSkeleton&probId=%d&teacherId=%d',"
                        + "'ProblemPreview','width=750,height=550,status=yes,resizable=yes');event.preventDefault();\"";
                onclick = String.format(onclick, req.getContextPath(), pid, teacherId);
            } else if(probInf.probType.equalsIgnoreCase("html5")) {
                link = "#";
                onclick = "onclick=\"window.open('%s%s/%s',"
                        + "'ProblemPreview','width=750,height=550,status=yes,resizable=yes');event.preventDefault();\"";
                String folder = (ar.indexOf(".") == -1) ? ar : ar.substring(0, ar.indexOf("."));
                String filename = (ar.indexOf(".") == -1) ? ar + ".html" : ar;
                onclick = String.format(onclick, Settings.html5ProblemURI, folder, filename);
            }
            link = String.format("href=\"%s\"", link);
            probInf.id = pid;
            if (this.topicName != null)
                probInf.topic = topicName;
            else probInf.topic = "?"; // need to figure out the problem's topics
            boolean hasStudData = getStudentStatsForProblem(conn, classId, req, table, probInf);
//                probs.add(probInf);

            String bgcolor = new String("#FFFFFF");


//            if (probInf.nStudsSeen > 3 &&
//                    (probInf.nStudsSolvedOnEncounter1Attempt1 / probInf.nStudsSeen < .2 ||
//                            probInf.nStudsSolvedOnEncounter1Attempt2 / probInf.nStudsSeen < .3)
//                    && probInf.avgComprehensionTime / 1000 > 60) {
//                bgcolor = REDCODE;
//                probInf.rating = RED;
//            } else
//            if (probInf.nStudsSeen > 3 && (probInf.nStudsSolvedOnEncounter1Attempt1 / probInf.nStudsSeen < .25 ||
//                    probInf.nStudsSolvedOnEncounter1Attempt2 / probInf.nStudsSeen < .4)
//                    && probInf.avgComprehensionTime / 1000 > 45) {
//                bgcolor = YELLOWCODE;
//                probInf.rating = YELLOW;
//            }
            int colorCounters[] = new int[2]; // 0 will be count of reds, 1 will be count of others (yellow)

            if (hasStudData) {


                if (probInf.nStudsSeen <= 3)
                    bgcolor = GREYCODE;

                String cell3 = String.format("<td  bgcolor=\"%s\"><a %s %s>%s</a></td>", bgcolor, onclick, link, probInf.name);

                String cell4 = intCell(null, probInf.nStudsSeen, null);

                bgcolor = percentColor(probInf.nStudsRepeated/((double) probInf.nStudsSeen), probInf.nStudsSeen, PERCENT_REPEAT_UP_1,PERCENT_REPEAT_UP_2, colorCounters);
                String cell5 = percentCell(bgcolor, probInf.nStudsRepeated, probInf.nStudsSeen);

                bgcolor = percentColor(probInf.nStudsSkipped/((double) probInf.nStudsSeen), probInf.nStudsSeen, PERCENT_SKIP_1,PERCENT_SKIP_2, colorCounters);
                String cell6 = percentCell(bgcolor, probInf.nStudsSkipped, probInf.nStudsSeen);

                bgcolor = percentColor(probInf.nStudsGiveUp/ ((double) probInf.nStudsSeen), probInf.nStudsSeen,PERCENT_GIVE_UP_1, PERCENT_GIVE_UP_2, colorCounters);
                String cell7 = percentCell(bgcolor, probInf.nStudsGiveUp, probInf.nStudsSeen);

                bgcolor = percentColor2(probInf.nStudsSolved/ ((double) probInf.nStudsSeen), probInf.nStudsSeen,PERCENT_EVENTUALLY_SOLVE_1, PERCENT_EVENTUALLY_SOLVE_2, colorCounters);
                String cell8 = percentCell(bgcolor, probInf.nStudsSolved, probInf.nStudsSeen);

                bgcolor = percentColor2(probInf.nStudsSolvedOnEncounter1Attempt1/ ((double) probInf.nStudsSeen), probInf.nStudsSeen,PERCENT_TRY1_SOLVE_1, PERCENT_TRY1_SOLVE_2, colorCounters);
                String cell9 = percentCell(bgcolor, probInf.nStudsSolvedOnEncounter1Attempt1, probInf.nStudsSeen);

                bgcolor = null;
                String cell10 = percentCell(bgcolor, probInf.nStudsSolvedOnEncounter1Attempt2, probInf.nStudsSeen);

                String cell11 = strCell(null, probInf.mostFreqIncorrectAns, null);

                bgcolor = null; // chg to get value based on avgNumHintsOnLastEncounter
                String cell12 = strCell(null, String.format("%3.1f", probInf.avgNumHintsOnLastEncounter), null);

                bgcolor = compTimeColor(probInf.avgComprehensionTime / 1000,probInf.nStudsSeen, colorCounters);
                String cell13 = strCell(bgcolor, String.format("%5.1f", probInf.avgComprehensionTime / 1000), null);

                String cell1= intCell(null,colorCounters[0],null);

                String cell2= intCell(null,colorCounters[0]+colorCounters[1],null);


                String cell14 = intCell(null, probInf.id, null);

                StringBuilder sb = new StringBuilder();
                sb.append(cell1 + cell2 + cell3 + cell4 + cell5 + cell6 + cell7 + cell8 + cell9 + cell10 + cell11 + cell12 + cell13);
                sb.append("</tr>");
                this.src.append(sb.toString());

                /*
            s = String.format("<tr><td  bgcolor=\"%s\">%d</td><td  bgcolor=\"%s\"><a href=\"%s\">%s</a></td>" +
                "<td>%d</td><td><a href=\".\" class=\"a1\" title=\"%s\">%4.1f</a></td><td><a href=\".\" class=\"a1\" title=\"%s\">%4.1f</a></td><td><a href=\".\" class=\"a1\" title=\"%s\">%4.1f</a></td><td><a href=\".\" class=\"a1\" title=\"%s\">%4.1f</a></td><td><a href=\".\" class=\"a1\" title=\"%s\">%4.1f</a></td>" +
                "<td><a href=\".\" class=\"a1\" title=\"%s\">%4.1f</a></td><td>%s</td><td>%3.1f</td><td>%5.1f</td><td>%d</td></tr>",
                bgcolor,probInf.rating, bgcolor,
                link,probInf.name,probInf.nStudsSeen,
                (probInf.nStudsRepeated + "/" + probInf.nStudsSeen),probInf.nStudsRepeated*100.0/probInf.nStudsSeen,
                (probInf.nStudsSkipped + "/" + probInf.nStudsSeen),probInf.nStudsSkipped*100.0/probInf.nStudsSeen,
                (probInf.nStudsGiveUp + "/" + probInf.nStudsSeen),probInf.nStudsGiveUp*100.0/probInf.nStudsSeen,
                (probInf.nStudsSolved + "/" + probInf.nStudsSeen),probInf.nStudsSolved*100.0/probInf.nStudsSeen,
                (probInf.nStudsSolvedOnEncounter1Attempt1 + "/" + probInf.nStudsSeen),probInf.nStudsSolvedOnEncounter1Attempt1*100.0/probInf.nStudsSeen,
                (probInf.nStudsSolvedOnEncounter1Attempt2 + "/" + probInf.nStudsSeen),probInf.nStudsSolvedOnEncounter1Attempt2*100.0/probInf.nStudsSeen,
                probInf.mostFreqIncorrectAns,
                probInf.avgNumHintsOnLastEncounter,
                probInf.avgComprehensionTime/1000,
                probInf.id);
                */
            }


        }
        this.src.append(foot);
//        createTable(probs);
    }

    private String percentColor(double n, int numStuds, double threshold1, double threshold2, int[] counters) {
        if (numStuds > MIN_STUDENTS_SEEN_FOR_REPORTING) {
            if (n >= threshold1) {
                counters[0]++;
                return REDCODE;
            }
            else if (n >= threshold2) {
                counters[1]++;
                return YELLOWCODE;
            }
        }
        return null;
    }

    private String percentColor2(double n, int numStuds, double threshold1, double threshold2, int[] counters) {
        if (numStuds > MIN_STUDENTS_SEEN_FOR_REPORTING) {
            if (n <= threshold1) {
                counters[0]++;
                return REDCODE;
            }
            else if (n <= threshold2)  {
                counters[1]++;
                return YELLOWCODE;
            }
        }
        return null;
    }

    private String compTimeColor(double v, int nStudsSeen, int[] counters) {
        if (nStudsSeen > MIN_STUDENTS_SEEN_FOR_REPORTING) {
            if (v > 100) {
                counters[0]++;
                return REDCODE;
            }
            else if (v > 75) {
                counters[1]++;
                return YELLOWCODE;
            }
        }
        return null;
    }



    private String intCell(String bgcolor, int val, String hoverText) {
        if (hoverText == null) {
            if (bgcolor != null)
                return String.format("<td bgcolor=\"%s\">%d</td>", bgcolor, val);
            else return String.format("<td>%d</td>", val);
        } else {
            if (bgcolor != null)
                return String.format("<td bgcolor=\"%s\"><a href=\".\" class=\"a1\" title=\"%s\">%d</a></td>", bgcolor, hoverText, val);
            else return String.format("<td><a href=\"%s\">%d</a></td>", hoverText, val);
        }
    }

    private String percentCell(String bgcolor, int n, int d) {
        if (bgcolor != null)
            return String.format("<td bgcolor=\"%s\"><a href=\".\" class=\"a1\" title=\"%d/%d\">%4.1f</a></td>", bgcolor, n, d, n * 100.0 / d);
        else return String.format("<td><a href=\".\" class=\"a1\" title=\"%d/%d\">%4.1f</a></td>", n, d, n * 100.0 / d);
    }


    private String strCell(String bgcolor, String val, String hoverText) {
        if (hoverText == null) {
            if (bgcolor != null)
                return String.format("<td bgcolor=\"%s\">%s</td>", bgcolor, val);
            else return String.format("<td>%s</td>", val);
        } else {
            if (bgcolor != null)
                return String.format("<td bgcolor=\"%s\"><a href=\".\" class=\"a1\" title=\"%s\">%s</a></td>", bgcolor, hoverText, val);
            else return String.format("<td><a href=\"%s\">%s</a></td>", hoverText, val);
        }
    }

    private void createTable(List<ProblemInfo> probs) {
        System.out.println("ProbID, name,topic, #students Seen, #students skipped, #students give up, #students solve, #students solve attempt1, #students solve attempt2");
        for (ProblemInfo pinf : probs) {
            System.out.print(pinf.id + "," + pinf.name + "," + pinf.topic);
            System.out.println("," + pinf.nStudsSeen + "," + pinf.nStudsSkipped + "," + pinf.nStudsGiveUp + "," + pinf.nStudsSolved + "," + pinf.nStudsSolvedOnEncounter1Attempt1 + "," + pinf.nStudsSolvedOnEncounter1Attempt2);
        }
    }


    // Go through events for all students on a given problem
    private boolean getStudentStatsForProblem(Connection conn, int classId, HttpServletRequest req, String table, ProblemInfo problemInfo) throws SQLException {

        String q = "select e.* from eventlog e, student where student.trialUser=0 and problemId=?" +
                " and studId=student.id and student.classId=? and probElapsed<600000 " +
                " and e.action in ('Attempt', 'BeginProblem','EndProblem', 'Hint')" +
                " order by student.id, e.id";
        ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, problemInfo.id);
            stmt.setInt(2, classId);
            rs = stmt.executeQuery();
            int lastStud = -1, attemptIx = 0, correctAttemptIx = 0;
            boolean solved = false;
            int nStudsSeen = 0, studEncounters = 0, nSkips = 0, nHints = 0;
            long begTime = 0, probTime, firstActTime = 0;
            boolean found = false;
            int nA = 0, nB = 0, nC = 0, nD = 0;
            boolean isExample=false;
            while (rs.next()) {

                int studId = rs.getInt("e.studid");

                // encountered a new student
                if (studId != lastStud) {
                    // gather stats on last student
                    if (lastStud != -1) {
                        problemInfo.nStudsSeen++;
                        problemInfo.nStudsRepeated += (studEncounters > 1) ? 1 : 0;
                        problemInfo.nStudsSkipped += (studEncounters == nSkips) ? 1 : 0;
                        problemInfo.nStudsGiveUp += (studEncounters > nSkips && !solved) ? 1 : 0;
                        problemInfo.nStudsSolved += solved ? 1 : 0;

                        problemInfo.avgNumHintsOnLastEncounter = (problemInfo.avgNumHintsOnLastEncounter * (problemInfo.nStudsSeen - 1) + nHints) / problemInfo.nStudsSeen;
                        if (nA > nB && nA > nC && nA > nD)
                            problemInfo.mostFreqIncorrectAns = "A";
                        else if (nB > nA && nB > nC && nB > nD)
                            problemInfo.mostFreqIncorrectAns = "B";
                        else if (nC > nA && nC > nB && nC > nD)
                            problemInfo.mostFreqIncorrectAns = "C";
                        else if (nD > nA && nD > nB && nD > nC)
                            problemInfo.mostFreqIncorrectAns = "D";
                        else problemInfo.mostFreqIncorrectAns = "-";
                    }

                    // initialize for next student
                    begTime = 0;
                    firstActTime = 0;
                    solved = false;
                    lastStud = studId;
                    nStudsSeen++;
                    studEncounters = 0;
                    nSkips = 0;
                    nA = nB = nC = nD = 0;
                }
                String action = rs.getString("e.action");

                boolean isCorrect = rs.getBoolean("e.isCorrect");
                long elapsedTime = rs.getLong("e.elapsedTime");
                String userInput = rs.getString("e.userInput");
                String activityName = rs.getString("e.activityName"); // on beginProblem will be either practice or demo


                if (action.equals("BeginProblem") && activityName.equals("practice")) {
                    found = true;
                    isExample = false;
                    begTime = elapsedTime;
                    studEncounters++;
                    attemptIx = 0;
                    correctAttemptIx = 0;
                    nHints = 0;
                    firstActTime = 0;
                }
                else if (action.equals("BeginProblem"))
                    isExample = true;
                else if (action.equals("EndProblem") && !isExample) {
                    probTime = elapsedTime - begTime;
                    // if a problem ends six seconds after beginning with no hints or attempts, we count it as a skip
                    if (probTime < 6000 && attemptIx == 0 && nHints == 0)
                        nSkips++;
                } else if (action.equals("Hint") && !isExample) {
                    nHints++;
                    if (firstActTime == 0)
                        firstActTime = elapsedTime - begTime;
                } else if (action.equals("Attempt") && !isExample) {
                    attemptIx++;
                    if (firstActTime == 0)
                        firstActTime = elapsedTime - begTime;
                    if (isCorrect) {
                        solved = true;

                        // only set the index the first time it is correct (there may be subsequent ones)
                        if (correctAttemptIx == 0) {
                            correctAttemptIx = attemptIx;
                            // avg comprehension time is only updated when student solves the problem.  We use the first act time for the encounter where they solve.
                            problemInfo.avgComprehensionTime = (problemInfo.avgComprehensionTime * problemInfo.nStudsSolved + firstActTime) / (problemInfo.nStudsSolved + 1);
                        }
                        if (studEncounters == 1) {
                            if (correctAttemptIx == 1)
                                problemInfo.nStudsSolvedOnEncounter1Attempt1++;
                            else if (correctAttemptIx == 2)
                                problemInfo.nStudsSolvedOnEncounter1Attempt2++;
                        }
                    } else {
                        if (userInput.equalsIgnoreCase("a"))
                            nA++;
                        else if (userInput.equalsIgnoreCase("b"))
                            nB++;
                        else if (userInput.equalsIgnoreCase("c"))
                            nC++;
                        else if (userInput.equalsIgnoreCase("d"))
                            nD++;
                    }
                }
            }
            if (found) {
                problemInfo.nStudsSeen++;
                problemInfo.nStudsRepeated += (studEncounters > 1) ? 1 : 0;
                problemInfo.nStudsSkipped += (studEncounters == nSkips) ? 1 : 0;
                problemInfo.nStudsGiveUp += (studEncounters > nSkips && !solved) ? 1 : 0;
                problemInfo.nStudsSolved += solved ? 1 : 0;
                problemInfo.avgNumHintsOnLastEncounter = (problemInfo.avgNumHintsOnLastEncounter * (problemInfo.nStudsSeen - 1) + nHints) / problemInfo.nStudsSeen;
                if (nA > nB && nA > nC && nA > nD)
                    problemInfo.mostFreqIncorrectAns = "A";
                else if (nB > nA && nB > nC && nB > nD)
                    problemInfo.mostFreqIncorrectAns = "B";
                else if (nC > nA && nC > nB && nC > nD)
                    problemInfo.mostFreqIncorrectAns = "C";
                else if (nD > nA && nD > nB && nD > nC)
                    problemInfo.mostFreqIncorrectAns = "D";
                else problemInfo.mostFreqIncorrectAns = "-";
                return true;
            }
            return false;
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }


    /**
     * returns an array of #of times problem is solved on first attempt,  # of incorrect responses to problem
     * , the number of hints, the total time probElapsed time before either a hint or an attempt
     *
     * @param conn
     * @param pid
     * @param classId
     * @return
     * @throws java.sql.SQLException
     */
    private int[] gatherProblemData(Connection conn, int pid, int classId) throws SQLException {

        String q = "select * from eventlog where problemId=? and (action='Attempt' or action='Hint') and " +
                "studId in (select id from student where classId=?) order by sessNum, studId, elapsedTime";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setInt(1, pid);
        ps.setInt(2, classId);
        ResultSet rs = ps.executeQuery();
        int lastSess = 0, lastStudId = 0, attempt1Correct = 0, nIncorrect = 0, nHints = 0;
        int thinkTime = 0;
        while (rs.next()) {
            boolean isCorrect = rs.getBoolean("isCorrect");
            int sess = rs.getInt("sessNum");
            int stud = rs.getInt("studId");

            if (sess != lastSess && stud != lastStudId) {
                System.out.println("probElapsed: " + rs.getInt("probElapsed"));
                thinkTime += rs.getInt("probElapsed");
            }
            if (rs.getString("action").equalsIgnoreCase("Hint")) {
                nHints++;
                lastSess = sess;
                lastStudId = stud;
                continue;
            }
            if (isCorrect && sess != lastSess && stud != lastStudId)
                attempt1Correct++;
            else if (!isCorrect)
                nIncorrect++;
            lastSess = sess;
            lastStudId = stud;
        }
        System.out.println("totalprobEalspd:" + thinkTime);
        int[] result = new int[4];
        result[ProbStats.NUM_CORRECT_ATTEMPT1.ordinal()] = attempt1Correct;
        result[ProbStats.NUM_HINTS.ordinal()] = nHints;
        result[ProbStats.NUM_INCORRECT.ordinal()] = nIncorrect;
        result[ProbStats.THINK_TIME.ordinal()] = thinkTime;
        return result;
    }

    public static void main(String[] args) {

        ProblemDifficultyReport r = new ProblemDifficultyReport();
        DbUtil.loadDbDriver();
        try {
            Connection conn = DbUtil.getAConnection("cadmium.cs.umass.edu");
            int[] res = r.gatherProblemData(conn, 14, 214);
            System.out.println("Number hints: " + res[ProbStats.NUM_HINTS.ordinal()]);
            System.out.println("Num correct on attempt 1: " + res[ProbStats.NUM_CORRECT_ATTEMPT1.ordinal()]);
            System.out.println("Num incorrect: " + res[ProbStats.NUM_INCORRECT.ordinal()]);
            System.out.println("Think time: " + res[ProbStats.THINK_TIME.ordinal()]);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}