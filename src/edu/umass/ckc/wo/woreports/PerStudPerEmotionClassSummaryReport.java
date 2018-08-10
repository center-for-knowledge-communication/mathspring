package edu.umass.ckc.wo.woreports;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.ArrayList;

import edu.umass.ckc.servlet.servbase.View;
import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;
import edu.umass.ckc.wo.beans.ClassInfo;
import edu.umass.ckc.wo.db.DbClass;
import edu.umass.ckc.wo.xml.JDOMUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Element;

/**
 * Class emotion summary per student per emotion
 * User: marshall. Date: Mar 2, 2004
 * Modifications: Ivon. August 17, 2006.
 * This report provides summaries PER PROBLEM, for a whole class. It highlights problems that are hard for students in a class.
 * If it takes a semiabsskillid, then it shows the problems that involve a particular skill
 */
public class PerStudPerEmotionClassSummaryReport extends Report {


    public PerStudPerEmotionClassSummaryReport() {
    }


    private final static int INTERESTED = 0;
    private final static int CONFIDENT = 1;
    private final static int EXCITED = 2;
    private final static int FRUSTRATED = 3;

    private final static String emotions[] = {"Interested", "Confident", "Excited", "Frustrated"};

    /**
     * Parse the userInput string which looks like
     * nxtPrbInf,excited,3,prg
     * <p/>
     * The number following the emotion is called a valence and may be -1 if the user did not fill out a value
     * in which case we don't factor it into our calculation.
     *
     * @param userInput
     * @param numObs
     */
    private void setValence(String userInput, int[] numObs, List<Integer> interested,
                            List<Integer> confident, List<Integer> excited, List<Integer> frustrated) {
        String[] x = userInput.split(",");
        String emotion = x[1].toUpperCase();
        String val = x[2];

        int valence = Integer.parseInt(val);
        if (valence == -1)
            return;
        if (emotion.equals("INTERESTED")) {
            interested.add(valence);
            numObs[INTERESTED]++;
        } else if (emotion.equals("CONFIDENT")) {
            confident.add(valence);
            numObs[CONFIDENT]++;
        } else if (emotion.equals("EXCITED")) {
            excited.add(valence);
            numObs[EXCITED]++;
        } else if (emotion.equals("FRUSTRATED")) {
            frustrated.add(valence);
            numObs[FRUSTRATED]++;
        }
    }

    /**
     * A row in the eventlog will contain something like below in the userInput column
     * <userInput emotion="excited" level="2"><reasons><reason label="prb" selected="true" /><reason label="prg" selected="false" /></reasons><explanation><![CDATA[]]></explanation></userInput>
     * @param userInput
     * @param numObs
     * @param interested
     * @param confident
     * @param excited
     * @param frustrated
     */
    private void setValenceNew(String userInput, int[] numObs, List<Integer> interested,
                            List<Integer> confident, List<Integer> excited, List<Integer> frustrated) throws Exception {
        Element userInputElt = JDOMUtils.getRoot(userInput);
        String emotion= userInputElt.getAttributeValue("emotion").toUpperCase();
        String val = userInputElt.getAttributeValue("level");
        int valence = Integer.parseInt(val);
        if (valence == -1)
            return;
        if (emotion.equals("INTERESTED")) {
            interested.add(valence);
            numObs[INTERESTED]++;
        } else if (emotion.equals("CONFIDENT")) {
            confident.add(valence);
            numObs[CONFIDENT]++;
        } else if (emotion.equals("EXCITED")) {
            excited.add(valence);
            numObs[EXCITED]++;
        } else if (emotion.equals("FRUSTRATED")) {
            frustrated.add(valence);
            numObs[FRUSTRATED]++;
        }
    }

    public View createReport(Connection conn, int classId, AdminViewReportEvent e, HttpServletRequest req, HttpServletResponse response) throws Exception {

        ClassInfo cl = DbClass.getClass(conn, classId);
        String className = getClassName(cl);
        String logTable = getEventLogTable(cl);
        boolean isNewLog = cl.isNewLog();
       // the bug seems to be that the auxId is just about always null and the emotion data is stored in this table in the userInput field
        String q;
        if (isNewLog)
            q = "select s.pedagogyId, e.studId, e.userInput, s.username from "+logTable+" e, student s where s.trialUser=0 and e.studId=s.id and s.classId=? and auxTable='EmotionInterventionResponse' order by s.id";
        else
            q = "select s.pedagogyId, e.studId, e.userInput, s.username from "+logTable+" e, student s where s.trialUser=0 and e.studId=s.id and s.classId=? and activityName='emotionFeedbackInterventionResponse' order by e.studId";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setInt(1, classId);
        ResultSet rs = ps.executeQuery();
        int curStudId = -1;
        String curUserName="";
        int[] numObs = null;
        int pedId=-1;
        List<Integer> interested = new ArrayList<Integer>();
        List<Integer> confident = new ArrayList<Integer>();
        List<Integer> excited = new ArrayList<Integer>();
        List<Integer> frustrated = new ArrayList<Integer>();
        emitTable1();
        while (rs.next()) {
            pedId= rs.getInt(1);
            int studId = rs.getInt(2);
            String input = rs.getString(3);
            String user = rs.getString(4);

            // we just moved to a new student so report on last student.
            if (studId != curStudId) {
                // if there was a last student, report on him
                if (curStudId != -1)
                    reportStudentData(curStudId, pedId, numObs, interested, confident, excited, frustrated, curUserName);
                curStudId = studId;
                curUserName=user;
                // reset bookeeping
                numObs = new int[]{0, 0, 0, 0};
                interested = new ArrayList<Integer>();
                confident = new ArrayList<Integer>();
                excited = new ArrayList<Integer>();
                frustrated = new ArrayList<Integer>();
            }
            if (isNewLog)
                setValenceNew(input, numObs, interested, confident, excited, frustrated);
            else setValence(input, numObs, interested, confident, excited, frustrated);
        }
        if (curStudId != -1)
            reportStudentData(curStudId, pedId, numObs, interested, confident, excited, frustrated, curUserName);

        emitTable2();
        return this;
    }


    private double getMean(List<Integer> observations, int n) {
        int sum = 0;
        for (int o : observations)
            sum += o;
        return ((double) sum) / n;

    }

    private double getStdDev(List<Integer> observations, int n, double mean) {
        double sum = 0;
        for (int o : observations)
            sum += Math.pow(o - mean, 2);
        return Math.sqrt(sum / n);
    }

    private void reportStudentData(int studId, int pedId, int[] numObs, List<Integer> interested,
                                   List<Integer> confident, List<Integer> excited, List<Integer> frustrated, String userName) {

        emitRow(studId, userName, pedId, interested, numObs[INTERESTED], confident, numObs[CONFIDENT],
                excited, numObs[EXCITED], frustrated, numObs[FRUSTRATED]);

/*        emitRow(studId,pedId,"Confident",confident,numObs[CONFIDENT]);
        emitRow(studId,pedId,"Excited",excited,numObs[EXCITED]);
        emitRow(studId,pedId,"Frustrated",frustrated,numObs[FRUSTRATED]);
*/
    }

    private void emitRow(int studId, String userName, int pedId, List<Integer> data1, int n1,
                         List<Integer> data2, int n2,
                         List<Integer> data3, int n3,
                         List<Integer> data4, int n4) {
        double mean = getMean(data1, n1);
        double stdDev = getStdDev(data1, n1, mean);
        if (n1 < 1) {
            mean = 0;
            stdDev = 0;
        }
        // don't show data for no observations 
        this.src.append(
                String.format("<tr><td> %d</td><td>%s</td>" +
                        "<td>%d</td>" +
                        "<td>%3.2f</td>" +
                        "<td>%5.3f</td>" +
                        "<td>%d</td>\n", studId, userName,pedId, mean, stdDev, n1));

        mean = getMean(data2, n2);
        stdDev = getStdDev(data2, n2, mean);
        if (n2 < 1) {
            mean = 0;
            stdDev = 0;
        }

        this.src.append(
                String.format(
                        "<td>%3.2f</td>" +
                                "<td>%5.3f</td>" +
                                "<td>%d</td>\n", mean, stdDev, n2));

        mean = getMean(data3, n3);
        stdDev = getStdDev(data3, n3, mean);
        if (n3 < 1) {
            mean = 0;
            stdDev = 0;
        }

        this.src.append(
                String.format(
                        "<td>%3.2f</td>" +
                                "<td>%5.3f</td>" +
                                "<td>%d</td>\n", mean, stdDev, n3));

        mean = getMean(data4, n4);
        stdDev = getStdDev(data4, n4, mean);
        if (n4 < 1) {
            mean = 0;
            stdDev = 0;
        }

        this.src.append(
                String.format(
                        "<td>%3.2f</td>" +
                                "<td>%5.3f</td>" +
                                "<td>%d</td>\n", mean, stdDev, n4));

        this.src.append("</tr>");
    }

    private void emitTable2() {
        this.src.append("</table>");
    }

    private void emitTable1() {
        this.src.append("<table border=1 cellspacing=1 cellpadding=1>\n" +
                " <tr>\n" +
                "  <th>Stud ID</th>" +
                "  <th>User Name</th>" +
                "  <th>Pedagogy ID</th>" +
                "  <th>" + emotions[0] + "_Mean</th>" +
                "  <th>" + emotions[0] + "_Stdev</th>" +
                "  <th>" + emotions[0] + "_NCases</th>" +
                "  <th>" + emotions[1] + "_Mean</th>" +
                "  <th>" + emotions[1] + "_Stdev</th>" +
                "  <th>" + emotions[1] + "_NCases</th>" +
                "  <th>" + emotions[2] + "_Mean</th>" +
                "  <th>" + emotions[2] + "_Stdev</th>" +
                "  <th>" + emotions[2] + "_NCases</th>" +
                "  <th>" + emotions[3] + "_Mean</th>" +
                "  <th>" + emotions[3] + "_Stdev</th>" +
                "  <th>" + emotions[3] + "_NCases</th>" +
                " </tr>\n");
    }


}