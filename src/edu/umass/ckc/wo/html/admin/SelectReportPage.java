package edu.umass.ckc.wo.html.admin;

import edu.umass.ckc.servlet.servbase.View;
import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;
import edu.umass.ckc.wo.event.admin.AdminActions;



public class SelectReportPage implements View {
    public static final String serverCall = "WoAdmin?action=AdminViewReport";
    public static final int OVERALL_HTML = 3;
    public static final int PASSWORDS_HTML = 12;
    public static final int OVERALL_CSV = 30;
    public static final int PER_PROB_HTML = 4;
    public static final int PER_PROB_CSV = 40;
    public static final int PER_STUD_HTML= 5;
    public static final int PER_STUD_CSV = 50;
    public static final int PER_SKILL_HTML = 2 ;
    public static final int PER_EMOTION_HTML = 16 ;
    public static final int PER_INTERVENTION_HTML = 17 ;
    public static final int PER_STUDENT_LEARNING_HUT_ACTIVITY_HTML = 19 ;
    public static final int PER_EMOTION_CSV = 160 ;
    public static final int PER_SKILL_CSV = 2;
    public static final int EPISODICDATA_HTML = 13 ;
    public static final int PER_STUD_PER_SKILL_HTML = 7;
    public static final int PER_STUD_PER_SKILL_CSV = 70;
    public static final int PER_STUD_PREPOST_HTML = 10;
    public static final int PER_PRETEST_SUMMARY_HTML = 15;
    public static final int PER_PRETEST_PROB_HTML = 18;
    public static final int PER_STUD_PREPOST_CSV = 100;
    public static final int PER_CLIENT_TIME_CSV = 14;

    private String classId;

    public SelectReportPage (String classId) {
        this.classId=classId;
    }

    private String getServerCall (int reportID) {
        return serverCall + "&" + AdminViewReportEvent.CLASS_ID +
                "=" + this.classId + "&" + AdminViewReportEvent.REPORT_ID + "=" + reportID +
                "&" + AdminViewReportEvent.STATE + "=" + AdminViewReportEvent.SHOW_REPORT;
    }

    public String getView() throws Exception {
        return
"<html>\n"+
"<style type=\"text/css\">\n"+
"a:active {  font-family: Arial, Helvetica, sans-serif; font-size: 14px; color: #FFFFFF; text-decoration: underline}\n"+
"a:hover {  font-family: Arial, Helvetica, sans-serif; font-size: 14px; color: #FFFF00; text-decoration: underline}\n"+
"a:link {  font-family: Arial, Helvetica, sans-serif; font-size: 14px; color: #FFFFFF; text-decoration: underline}\n"+
"a:visited {  font-family: Arial, Helvetica, sans-serif; font-size: 14px; color: #FFFFFF; text-decoration: underline}\n"+
"</style>\n"+
"<head>\n"+
"<title>Wayang Outpost: Teacher Tools: Reports</title>\n"+
"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">\n"+
"</head>\n"+
"\n"+
"\n"+
"\n"+
"<body bgcolor=\"#904a4a\" text=\"#000000\">\n"+
"<p><b><font face=\"Arial, Helvetica, sans-serif\" size=\"7\" color=\"#FFFFFF\">Wayang \n"+
"  Outpost </font></b><i><font face=\"Arial, Helvetica, sans-serif\" size=\"6\" color=\"#FFFFFF\">Teacher \n"+
"  Tools</font></i></p>\n"+
"<p>&nbsp;</p>\n"+
"<p><font face=\"Arial, Helvetica, sans-serif\" size=\"4\" color=\"#FFFFFF\">Select a \n"+
"  report </font></p>\n"+
"<table width=\"781\" border=\"1\">\n"+
"  <tr> \n"+
"    <td ><font color=\"#FFFFFF\" face=\"Arial, Helvetica, sans-serif\"> Report</font></td>\n"+
"    <td ><font color=\"#FFFFFF\" face=\"Arial, Helvetica, sans-serif\"> Explanation</font></td>\n"+
"  </tr>\n"+
"  <tr> \n"+
"    <td bgcolor=#773430 ><font  face=\"Arial, Helvetica, sans-serif\"><a href=\"" + getServerCall(PASSWORDS_HTML) + "\">User names and passwords</td> <td><font color=\"#FFFFFF\" face=\"Arial, Helvetica, sans-serif\"> User names and passwords for this class</a></td>\n"+
//"    <td bgcolor=#000099><font  face=\"Arial, Helvetica, sans-serif\"><a href=\"" + getServerCall(OVERALL_CSV) + "\">Class Summary - Overall</a></td>\n"+
"  </tr>\n"+
"  <tr> \n"+
"    <td bgcolor=#773430 ><font  face=\"Arial, Helvetica, sans-serif\"><a href=\"" + getServerCall(OVERALL_HTML) + "\">Class Summary</td> <td><font color=\"#FFFFFF\" face=\"Arial, Helvetica, sans-serif\"> What your students have done overall</a></td>\n"+
//"    <td bgcolor=#991C16><font  face=\"Arial, Helvetica, sans-serif\"><a href=\"" + getServerCall(OVERALL_CSV) + "\">Class Summary - Overall</a></td>\n"+
"  </tr>\n"+
"  <tr> \n"+
"    <td bgcolor=#773430><font  face=\"Arial, Helvetica, sans-serif\"><a href=\"" + getServerCall(PER_STUD_PREPOST_HTML) + "\">Pre/Posttest scores per Student</td> <td><font color=\"#FFFFFF\" face=\"Arial, Helvetica, sans-serif\"> The scores of each student on a test before and after getting tutoring.</a></td>\n"+
//"    <td bgcolor=#991C16><font  face=\"Arial, Helvetica, sans-serif\"><a href=\"" + getServerCall(PER_STUD_PREPOST_CSV) + "\">Pre/Post scores per Student</a></td>\n"+
"  </tr>\n"+

"  <tr> \n"+
"    <td bgcolor=#773430><font  face=\"Arial, Helvetica, sans-serif\"><a href=\"" + getServerCall(PER_PRETEST_SUMMARY_HTML) + "\">Pre/Posttest problem summaries per class</td> <td><font color=\"#FFFFFF\" face=\"Arial, Helvetica, sans-serif\"> How your class is doing on individual problems in the pretest.</a></td>\n"+
//"    <td bgcolor=#991C16><font  face=\"Arial, Helvetica, sans-serif\"><a href=\"" + getServerCall(PER_STUD_PREPOST_CSV) + "\">Pre/Post scores per Student</a></td>\n"+
"  </tr>\n"+

"  <tr> \n"+
"    <td bgcolor=#773430><font  face=\"Arial, Helvetica, sans-serif\"><a href=\"" + getServerCall(PER_PRETEST_PROB_HTML) + "\">Pre/Posttest problem detail per student</td> <td><font color=\"#FFFFFF\" face=\"Arial, Helvetica, sans-serif\"> Shows individual pre/post test problem scoring data per student.</a></td>\n"+
//"    <td bgcolor=#991C16><font  face=\"Arial, Helvetica, sans-serif\"><a href=\"" + getServerCall(PER_STUD_PREPOST_CSV) + "\">Pre/Post scores per Student</a></td>\n"+
"  </tr>\n"+

"  <tr> \n"+
"    <td bgcolor=#773430><font  face=\"Arial, Helvetica, sans-serif\"><a href=\"" + getServerCall(PER_PROB_HTML)  +"\">Class Summary Per Problem</td> <td><font color=\"#FFFFFF\" face=\"Arial, Helvetica, sans-serif\"> Which problems are challenging for your students.</a></td>\n"+
//"    <td bgcolor=#991C16><font  face=\"Arial, Helvetica, sans-serif\"><a href=\"" + getServerCall(PER_PROB_CSV) +"\">Class Summary - Per Problem</a></td>\n"+
"  </tr>\n"+
"  <tr> \n"+
"    <td bgcolor=#773430><font  face=\"Arial, Helvetica, sans-serif\"><a href=\"" + getServerCall(PER_STUD_HTML) + "\">Class Summary Per Student</td> <td><font color=\"#FFFFFF\" face=\"Arial, Helvetica, sans-serif\"> How many problems and hints each student has seen</a></td>\n"+
//"    <td bgcolor=#991C16><font  face=\"Arial, Helvetica, sans-serif\"><a href=\"" + getServerCall(PER_STUD_CSV) + "\">Class Summary - Per Student</a></td>\n"+
"  </tr>\n"+
"  <tr> \n"+
"    <td bgcolor=#773430><font  face=\"Arial, Helvetica, sans-serif\"><a href=\"" + getServerCall(PER_SKILL_HTML) + "\">Class Summary Per Skill <br> (will take a couple of minutes to run) </td> <td><font color=\"#FFFFFF\" face=\"Arial, Helvetica, sans-serif\"> Which are the math skills that your students are having trouble with</a></td>\n"+
//"    <td bgcolor=#991C16><font  face=\"Arial, Helvetica, sans-serif\"><a href=\"" + getServerCall(PER_SKILL_CSV) + "\">Class Summary - Per Skill</a></td>\n"+
"  </tr>\n"+

"    <td bgcolor=#773430><font  face=\"Arial, Helvetica, sans-serif\"><a href=\"" + getServerCall(PER_EMOTION_HTML) + "\">Class Emotion Summary Per Student Per Emotion <br></td> <td><font color=\"#FFFFFF\" face=\"Arial, Helvetica, sans-serif\"> Emotion summary per student per emotion</a></td>\n"+
"  </tr>\n"+

"  <tr> \n"+
"    <td bgcolor=#773430><font  face=\"Arial, Helvetica, sans-serif\"><a href=\"" + getServerCall(EPISODICDATA_HTML) + "\">Detailed Activity of each student </td> <td><font color=\"#FFFFFF\" face=\"Arial, Helvetica, sans-serif\"> What students have worked on at each time step</a></td>\n"+
"  </tr>\n"+
"  <tr> \n"+
"    <td bgcolor=#773430><font  face=\"Arial, Helvetica, sans-serif\"><a href=\"" + getServerCall(PER_CLIENT_TIME_CSV) + "\">Start and Stop times of Students with associated sensors. </td> <td><font color=\"#FFFFFF\" face=\"Arial, Helvetica, sans-serif\"> What students have worked on at each time step</a></td>\n"+
"  </tr>\n"+
        "  <tr> \n"+
        "    <td bgcolor=#773430><font  face=\"Arial, Helvetica, sans-serif\"><a href=\"" + getServerCall(PER_INTERVENTION_HTML) + "\">Impact of Interventions </td> <td><font color=\"#FFFFFF\" face=\"Arial, Helvetica, sans-serif\"> What students have worked on at each time step</a></td>\n"+
"  </tr>\n"+

"  </tr>\n"+
        "  <tr> \n"+
        "    <td bgcolor=#773430><font  face=\"Arial, Helvetica, sans-serif\"><a href=\"" + getServerCall(PER_STUDENT_LEARNING_HUT_ACTIVITY_HTML) + "\">Student activity in learning hut.</td> <td><font color=\"#FFFFFF\" face=\"Arial, Helvetica, sans-serif\">Detailed activity report of learning hut activity- per student</a></td>\n"+
"  </tr>\n"+
//"  <tr> \n"+
//"    <td bgcolor=#991C16><font  face=\"Arial, Helvetica, sans-serif\"><a href=\"" + getServerCall(PER_STUD_PER_SKILL_HTML) + "\">Class Summary Per Student Per Skill </td> <td><font color=\"#FFFFFF\" face=\"Arial, Helvetica, sans-serif\"> How each student is performing in different math skills.</a></td>\n"+
//"    <td bgcolor=#991C16><font  face=\"Arial, Helvetica, sans-serif\"><a href=\"" + getServerCall(PER_STUD_PER_SKILL_CSV) + "\">Class Summary - Per Student Per Skill</a></td>\n"+
//"  </tr>\n"+
"</table>\n"+
"<p>&nbsp;</p>\n"+
"<p><a href=\"?action="+AdminActions.CHOOSE_ANOTHER_CLASS + "\">Return to Class Selection</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
"<a href=\"?action="+ AdminActions.CHOOSE_ACTIVITY +"\">&nbsp;Return to Activity Selection \n"+
"  to Activity Selection</a></p>\n"+
"<p>&nbsp; </p>\n"+
"</body>\n"+
"</html>\n"+
"";
    }


}
