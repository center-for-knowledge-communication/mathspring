

package edu.umass.ckc.wo.woreports;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

//import edu.umass.ckc.wo.event.admin.AdminActions;
//import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;
import edu.umass.ckc.servlet.servbase.View;
import edu.umass.ckc.wo.event.admin.AdminActions;
import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: mbaldwin
 *
 */
public class Report7 extends Report  {

  	/**
     * Build up the text of the report by appending to the src StringBuffer
     *
     * @param conn
     * @param classId
     * @throws Exception
     */
	public Report7() {
	}

    public View createReport(Connection conn, int classId, AdminViewReportEvent e, HttpServletRequest req, HttpServletResponse response) throws Exception {

    	String outputLine = "";

    	// html page header
        this.src.append("<html>\n" +
                "<head>\n" +
                "<title>Report 7 - Problem Data by Student and Skill" + "</title>\n" +
                "</head>\n" +
                "\n" +
                "<body>\n");

        // text header
        this.src.append("<h3>Problem Data by Student and Skill</h3>\n");

        // links to other reports or classes
        this.src.append("<h5>" +
    				"<a href='?action=" + AdminActions.SELECT_REPORT + classId + "'>Choose another report</a> | " +
    				"<a href='?action=" + AdminActions.CHOOSE_ANOTHER_CLASS + "'>Choose another class</a>" +
    				"</h5>");

        // begin the table
        this.src.append("<table border=1 cellspacing=1 cellpadding=1>\n");

    	// retrieve list of skill names
        String SQL = "SELECT AbstractLevelSkill.id, AbstractLevelSkill.name " +
        	"FROM AbstractLevelSkill ORDER BY AbstractLevelSkill.name";
        Statement skillNamesSt = conn.createStatement();
        ResultSet skillNamesRS = skillNamesSt.executeQuery(SQL);

        // write column headers containing skill names
        this.src.append("<tr>\n" +
                "  <td></td><td></td>\n");
        int numSkills = 0;
        String skillName = "";
        while (skillNamesRS.next()) {
        	skillName = skillNamesRS.getString("name");
        	if (!skillName.equalsIgnoreCase("Logic")) {
	            this.src.append("    <th colspan='4'>" + skillNamesRS.getString("name") + "</th>\n");
	            numSkills++;
            }
        }
        this.src.append("</tr>\n\n");

        // write sub-column headers with names of per-skill data
        this.src.append("<tr>\n" +
                // student id/name column headers
                "  <td><b>ID</b></td><td><b>Name</b></td>\n");
        for (int i=1; i<=numSkills; i++) {
            this.src.append("  <td><b># problems</b></td>\n" +
                "  <td nowrap='nowrap'><b>% solved w/o help</b></td>\n" +
                "  <td nowrap='nowrap'><b>avg. # of hints</b></td>\n" +
                "  <td nowrap='nowrap'><b>% solved w/ help</b></td>\n");
        }
        this.src.append("</tr>\n\n" +
         				"<tbody id='data'>\n");

        // retrieve list of students
        SQL = "SELECT Student.id AS studId, Student.fname, Student.lname, Student.userName " +
                "FROM Class INNER JOIN Student ON Class.id = Student.classId " +
                "WHERE Class.id = " + Integer.toString(classId) + " ORDER BY Student.lname";
        Statement studSt = conn.createStatement();
        ResultSet studRS = studSt.executeQuery(SQL);

        while (studRS.next()) { // for each student in the class

            int studId = studRS.getInt("studId");
            String studentName = (studRS.getString("fname")).trim() + " " + (studRS.getString("lname")).trim();

            // append the username if it is not null
            if (studRS.getString("userName") != null)
            	studentName += (" [" + studRS.getString("userName") + "]");

            // write row headers with student names
            this.src.append("<tr>\n " +
            	"<td>" + studId + "</td>\n" +
                "<td nowrap='nowrap'>" + studentName + "</td>\n");

            // retrieve problem data by skill type
            SQL = "SELECT AbstractLevelSkill.id, AbstractLevelSkill.name, Problem.id AS prob_id "+
                 "FROM ((EpisodicData2 INNER JOIN ((Hint INNER JOIN Skill ON Hint.skillId = Skill.id) INNER JOIN Problem ON Hint.problemId = Problem.id) ON EpisodicData2.problemId = Problem.id) INNER JOIN LinkToAbstractSkill ON Skill.id = LinkToAbstractSkill.skillid) INNER JOIN AbstractLevelSkill ON LinkToAbstractSkill.abstractskillid = AbstractLevelSkill.id " +
                 "WHERE EpisodicData2.studId="+ Integer.toString(studId) +" " +
                 "GROUP BY AbstractLevelSkill.id, AbstractLevelSkill.name, Problem.id, EpisodicData2.studId " +
                 "ORDER BY AbstractLevelSkill.name";

		    Statement classSt1 = conn.createStatement();
		    ResultSet rs = classSt1.executeQuery(SQL);

		    int currSkill = 0;
		    int lastSkill = -1;

		    int numProbs = 0;
		    int numHints = 0;
		    int numNoHints = 0;
		    int numWithHints = 0;

		    while (rs.next()) {

			      currSkill = rs.getInt("id");
			      int prob = rs.getInt("prob_id");

			      if (currSkill != lastSkill) { //start new skill row

			        if (lastSkill != -1) //no output the first pass
			          this.src.append(outputLine);

			        lastSkill = currSkill;

			        numProbs = 0;
			        numNoHints = 0;
			        numWithHints = 0;
			        numHints = 0;

			      }

				  // retrieve student data per problem grouped by skill type
			      String SQL2 = "SELECT EpisodicData2.problemId, Problem.name, Problem.nickname, EpisodicData2.action, " +
			                    "EpisodicData2.userInput, EpisodicData2.isCorrect, EpisodicData2.probElapsed "+
			                    "FROM EpisodicData2 INNER JOIN Student ON EpisodicData2.studId = Student.id " +
			                    "INNER JOIN Problem ON EpisodicData2.problemId = Problem.id "+
			                    "WHERE Student.id = " + Integer.toString(studId)+ " AND EpisodicData2.problemId = " +
			                    Integer.toString(prob) + " ORDER by EpisodicData2.problemId;";

			      Statement classSt2 = conn.createStatement();
			      ResultSet rs2 = classSt2.executeQuery(SQL2);

			      int local_numHints = 0;

			      boolean hintSeen = false;
			      boolean solvedNoHelp = false;

				  // loop through each problem marked by this skill
			      while (rs2.next()) {
					boolean isCorrect = (rs2.getInt("isCorrect") == 1);
					int probId = Integer.parseInt(rs2.getString("problemId"));

					String action = rs2.getString("action");

					if (action.equalsIgnoreCase("beginProblem"))
					{
					  local_numHints = 0;
					  hintSeen = false;
					}

					if (action.equalsIgnoreCase("attempt"))
					{
					  if ((isCorrect) && (hintSeen==false))
					    solvedNoHelp = true;
					}

					if (action.startsWith("hint"))
					{
					  hintSeen = true;
					  local_numHints++;
					}

					if (action.equalsIgnoreCase("endProblem"))
					{
					  numProbs++;

					  if ((!hintSeen) && isCorrect)
					    numNoHints++;

					  if ((hintSeen) && (isCorrect))// solved with help
					    numWithHints++;

					  numHints+=local_numHints;
					}

			      } // per problem while

			      double percentNoHelp = numNoHints / (double) numProbs;
			      double percentWithHelp = numWithHints / (double) numProbs;
			      double avgNumHints = numHints/(double)numProbs;

			      outputLine = "<td>" + numProbs + "</td>" +
			                   "<td>" + doubleToString(percentNoHelp * 100) + "%</td>" +
			                   "<td>" + doubleToString(avgNumHints) + "</td>" +
			                   "<td>" + doubleToString(percentWithHelp * 100) + "%</td>";
			} // per skill while
			this.src.append(outputLine);
			this.src.append("</tr>\n\n");
		} // per student while
		this.src.append(foot);
        return this;
    }
}
