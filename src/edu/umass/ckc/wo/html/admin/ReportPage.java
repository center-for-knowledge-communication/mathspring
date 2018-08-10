package edu.umass.ckc.wo.html.admin;


import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;
import edu.umass.ckc.wo.woreports.ReportProblem;
import edu.umass.ckc.wo.woreports.ReportSkill;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
public class ReportPage {


  public static String head;

  public static final String foot =" </table>\n"+
                                   "\n"+
                                   "</body>\n"+
                                   "\n"+
                                   "</html>";

  public String createReportOne(Connection conn, int studId) throws Exception {

    head =  head + "<html>\n"+
            "<head>\n"+
            "<title>Report 1</title>\n"+
            "</head>\n"+
            "\n"+
            "<body>";

    boolean solvedNoHelp = true;
    int numHints = 0;
    int timeToChoose =0;
    int timeToAnswer=0;
    int timeToHint=0;

    String neck ="<table border=1 cellspacing=1 cellpadding=1>\n"+
                 " <tr>\n"+
                 "  <td>  </td>\n"+
                 "  <td><b>Help?</b></td>\n"+
                 "  <td><b># Hints</td>\n"+
                 "  <td><b>Time to attempt</td>\n"+
                 "  <td><b>Time to solve</td>\n"+
                 "  <td><b>Time to hint</td>\n"+
                 " </tr>\n";


    String SQL = "SELECT EpisodicData2.problemId, Problem.nickname, EpisodicData2.action, " +
                 "EpisodicData2.userInput, EpisodicData2.isCorrect, EpisodicData2.probElapsed "+
                 "FROM EpisodicData2 INNER JOIN Student ON EpisodicData2.studId = Student.id " +
                 "INNER JOIN Problem ON EpisodicData2.problemId = Problem.id "+
                 "WHERE Student.id = " + Integer.toString(studId)+ " " +
                 "ORDER by problemId;";

    Statement classSt1 = conn.createStatement();
    ResultSet rs = classSt1.executeQuery(SQL);

    head = head + "<h3>Detail Problem Info for Student #"+Integer.toString(studId)+"</h3>\n" + neck;

    while (rs.next()) {
      int probId = Integer.parseInt(rs.getString("problemId"));
      String probName = rs.getString("nickName");
      String action = rs.getString("action");

      if (action.equalsIgnoreCase("beginProblem"))
      {
        timeToChoose =0;
        timeToAnswer=0;
        timeToHint=0;
        numHints = 0;
        solvedNoHelp = true;
      }

      if (action.equalsIgnoreCase("attempt"))
      {
        if (((Integer.parseInt(rs.getString("isCorrect"))) == 1) && (timeToAnswer ==0))
          timeToAnswer=Integer.parseInt(rs.getString("probElapsed"));
        if (timeToChoose==0)
          timeToChoose=Integer.parseInt(rs.getString("probElapsed"));
      }

      if (action.startsWith("hint"))
      {
        if (timeToHint==0)
          timeToHint=Integer.parseInt(rs.getString("probElapsed"));
        if (((Integer.parseInt(rs.getString("isCorrect"))) == 1)&&(timeToHint==0))
          solvedNoHelp = true;
        else
          solvedNoHelp=false;
        numHints++;
      }

      if (action.equalsIgnoreCase("endProblem"))
      {
        String str;
        if (solvedNoHelp)
          str = "No";
        else
          str = "Yes";
        head = head + " <tr>\n"+
               "  <td><b>"+ probName + "</b></td>\n" +
               "   <td>"+str+"</td>\n"+
               "   <td>"+Integer.toString(numHints)+"</td>\n"+
               "   <td>"+Integer.toString(timeToChoose)+"</td>\n"+
               "   <td>"+Integer.toString(timeToAnswer)+"</td>\n"+
               "   <td>"+Integer.toString(timeToHint)+"</td>\n"+
               " </tr>\n";
      }

    } //while

    return head.concat(foot);
  }

  public String createReportTwo(Connection conn, int studId) throws Exception {

      head =  head + "<html>\n"+
              "<head>\n"+
              "<title>Report 2</title>\n"+
              "</head>\n"+
              "\n"+
              "<body>";

      boolean solvedNoHelp = true;
      int numHints = 0;
      int timeToChoose =0;
      int timeToAnswer=0;
      int timeToHint=0;

      Hashtable skillHash = new Hashtable();

      String neck ="<table border=1 cellspacing=1 cellpadding=1>\n"+
                   " <tr>\n"+
                   "  <td>  </td>\n"+
                   "  <td><b># problems</b></td>\n"+
                   "  <td><b>% solved w/o help</td>\n"+
                   "  <td><b>Avg # of hints</td>\n"+
                   "  <td><b>% solved w/ help</td>\n"+
                   " </tr>\n";

      String SQL = "SELECT Skill.*, EpisodicData2.* "+
                   "FROM EpisodicData2 LEFT JOIN ((ProblemSkill RIGHT JOIN Problem ON ProblemSkill.problemId = Problem.id) LEFT JOIN Skill ON ProblemSkill.skillId = Skill.id) ON EpisodicData2.problemId = Problem.id " +
                   "WHERE EpisodicData2.studId=" + Integer.toString(studId)+ ";";

      Statement classSt1 = conn.createStatement();
      ResultSet rs = classSt1.executeQuery(SQL);

      head = "<h3>Detail Skill Info for Student #"+Integer.toString(studId)+"</h3>\n" + neck;

      while (rs.next()) {
        int probId = (rs.getInt("id"));
        String action = rs.getString("action");

        if (action.equalsIgnoreCase("beginProblem"))
        {
          timeToChoose=0;
          timeToAnswer=0;
          timeToHint=0;
          numHints = 0;
          solvedNoHelp = true;
        }

        if (action.equalsIgnoreCase("attempt"))
        {
          if (((Integer.parseInt(rs.getString("isCorrect"))) == 1) && (timeToAnswer ==0))
            timeToAnswer=Integer.parseInt(rs.getString("probElapsed"));
          if (timeToChoose==0)
            timeToChoose=Integer.parseInt(rs.getString("probElapsed"));
        }

        if (action.startsWith("hint"))
        {
          if (timeToHint==0)
            timeToHint=Integer.parseInt(rs.getString("probElapsed"));
          if (!(((Integer.parseInt(rs.getString("isCorrect"))) == 1)&&(timeToHint==0)))
            solvedNoHelp = false;
          else
            solvedNoHelp=true;
          numHints++;
        }

        if (action.equalsIgnoreCase("endProblem"))
        {
          int snh;
          if (solvedNoHelp)
            snh = 1;
          else
            snh = 0;

          int skipped;
          if (rs.getString("isCorrect").equals("0"))
            skipped = 1;
          else
            skipped = 0;

          if (skillHash.containsKey(new Integer(probId))) { //exisiting
            ((ReportSkill)skillHash.get(new Integer(probId))).seen(numHints,snh,skipped);
          }
          else { // new problem
            ReportSkill rsk = new ReportSkill(rs.getString("name"));
            rsk.seen(numHints,snh,skipped);
            skillHash.put(new Integer(probId), rsk);
          } //else

        }

      } //while

      Collection hashVal = skillHash.values();
      Iterator   iter    = hashVal.iterator();

      while (iter.hasNext())
      {
        ReportSkill rsk = (ReportSkill) iter.next();

        head = head +
               " <tr>\n"+
               "  <td><b>"+ rsk.getName() + "</b></td>\n" +
               "   <td>"+Integer.toString(hashVal.size())+"</td>\n"+
               "   <td>"+Double.toString(rsk.getSolvedFirstAtt())+"</td>\n"+
               "   <td>"+Double.toString(rsk.getNumHints())+"</td>\n"+
               "   <td>"+Double.toString(((rsk.getSolvedFirstAtt() + rsk.getSkipped()))) + "</td>\n</tr>\n";
      }//for

      return head.concat(foot);


  } //report 2

public String createReportThree(Connection conn, int classId) throws Exception {

    head =  "<html>\n"+
            "<head>\n"+
            "<title>Report 3</title>\n"+
            "</head>\n"+
            "\n"+
            "<body>";

    boolean solvedNoHelp = true;
    int numStud =0;
    int totProbs=0;
    int probsNoHelp=0;
    int probsWithHelp=0;
    int currStud=0;

    String neck ="<table border=1 cellspacing=1 cellpadding=1>\n"+
                 " <tr>\n"+
                 "  <td><b>Avg. Prob Seen</b></td>\n"+
                 "  <td><b>Avg. Solved w/o Help</td>\n"+
                 "  <td><b>% Solved w/o Help</td>\n"+
                 "  <td><b>Avg. Solved w/help</td>\n"+
                 "  <td><b>% Solved w/help</td>\n"+
                 " </tr>\n";

    String SQL = "SELECT EpisodicData2.*, EpisodicData2.id " +
                 "FROM (Student RIGHT JOIN EpisodicData2 ON Student.id = EpisodicData2.studId) "+
                 "LEFT JOIN Class ON Student.classId = Class.id " +
                 "WHERE Class.id = " + Integer.toString(classId) + " "+
                 "ORDER BY Student.id, EpisodicData2.id;";

    Statement classSt1 = conn.createStatement();
    ResultSet rs = classSt1.executeQuery(SQL);

    head = head + "<h3>Detail Class Info for Class#"+Integer.toString(classId)+"</h3>\n";

    while (rs.next()) {
      if (currStud != Integer.parseInt(rs.getString("studId"))) { //Create new student
        numStud++;
        currStud = Integer.parseInt(rs.getString("studId"));
      }
      else { // not new student

        String action = rs.getString("action");

        if (action.equalsIgnoreCase("beginProblem"))
          solvedNoHelp = true;

        if (action.startsWith("hint"))
        {
          if ((Integer.parseInt(rs.getString("isCorrect"))) == 1)
            solvedNoHelp = true;
          else
            solvedNoHelp=false;
        }

        if (action.equalsIgnoreCase("endProblem"))
        {
          String str;
          if ((Integer.parseInt(rs.getString("isCorrect"))) == 1){
            if (solvedNoHelp)
              probsNoHelp++;
            else
              probsWithHelp++;

          }//if
          totProbs++;
        }

      } //else
    }//while

    head = head + neck +
           " <tr>\n"+
           "  <td>"+ Double.toString(((double)totProbs)/((double)numStud)) + "</td>\n" +
      "   <td>"+Double.toString(((double)probsNoHelp)/((double)numStud))+"</td>\n"+
      "   <td>"+Double.toString((((double)probsNoHelp)/((double)totProbs))*100)+"%</td>\n"+
      "   <td>"+Double.toString(((double)probsWithHelp)/((double)numStud))+"</td>\n"+
      "   <td>"+Double.toString((((double)probsWithHelp)/((double)totProbs))*100)+"%</td>\n"+
      " </tr>\n";
    return head.concat(foot);

  }//Report Three

  public String createReportFour(Connection conn, int classId) throws Exception {

    head =  "<html>\n"+
            "<head>\n"+
            "<title>Report 4</title>\n"+
            "</head>\n"+
            "\n"+
            "<body>";

    Hashtable probHash = new Hashtable();

    boolean solvedNoHelp = true;
    int numHints = 0;
    int timeToChoose =0;
    int timeToAnswer=0;
    int timeToHint=0;

    String SQL = "SELECT EpisodicData2.*" +
                 "FROM (Student RIGHT JOIN EpisodicData2 ON Student.id = EpisodicData2.studId) "+
                 "LEFT JOIN Class ON Student.classId = Class.id " +
                 "WHERE Class.id = " + Integer.toString(classId) + " "+
                 "ORDER BY Student.id, EpisodicData2.id;";

    String neck ="<table border=1 cellspacing=1 cellpadding=1>\n"+
                 " <tr>\n"+
                 "  <td>  </td>\n"+
                 "  <td><b>Avg. solved without help</b></td>\n"+
                 "  <td><b>Avg. # of Hints</td>\n"+
                 "  <td><b>Avg. time to attempt</td>\n"+
                 "  <td><b>Avg. time to solve</td>\n"+
                 "  <td><b>Avg. time to hint</td>\n"+
                 " </tr>\n";

    Statement classSt1 = conn.createStatement();
    ResultSet rs = classSt1.executeQuery(SQL);

    head = head + "<h3>Detail Problem Info for Class #"+Integer.toString(classId)+"</h3>\n" + neck;

    while (rs.next()) {
      int probId = Integer.parseInt(rs.getString("problemId"));
//      String probName = rs.getString(rs.getString("nickName"));
      String action = rs.getString("action");

      if (action.equalsIgnoreCase("beginProblem"))
      {
        timeToChoose=0;
        timeToAnswer=0;
        timeToHint=0;
        numHints = 0;
        solvedNoHelp = true;
       }

      if (action.equalsIgnoreCase("attempt"))
      {
        if (((Integer.parseInt(rs.getString("isCorrect"))) == 1) && (timeToAnswer ==0))
          timeToAnswer=Integer.parseInt(rs.getString("probElapsed"));
        if (timeToChoose==0)
          timeToChoose=Integer.parseInt(rs.getString("probElapsed"));
      }

      if (action.startsWith("hint"))
      {
        if (timeToHint==0)
          timeToHint=Integer.parseInt(rs.getString("probElapsed"));
        if (!(((Integer.parseInt(rs.getString("isCorrect"))) == 1)&&(timeToHint==0)))
          solvedNoHelp = false;
        else
          solvedNoHelp=true;
        numHints++;
      }

      if (action.equalsIgnoreCase("endProblem"))
      {
        int snh;
        if (solvedNoHelp)
          snh = 1;
        else
          snh = 0;

        if (probHash.containsKey(new Integer(probId))) { //exisiting
          ((ReportProblem)probHash.get(new Integer(probId))).seen(timeToChoose,timeToAnswer,timeToHint,numHints,snh);
        }
        else { // new problem
          ReportProblem rp = new ReportProblem(probId);
          rp.seen(timeToChoose,timeToAnswer,timeToHint,numHints,snh);
          probHash.put(new Integer(probId), rp);
        } //else

      }

    } //while

    Collection hashVal = probHash.values();
    Iterator   iter    = hashVal.iterator();

    while (iter.hasNext())
    {
      ReportProblem rp = (ReportProblem) iter.next();

      head = head +
               " <tr>\n"+
               "  <td><b>Problem "+ Integer.toString(rp.getId()) + "</b></td>\n" +
               "   <td>"+Double.toString(rp.getAvgSolvedFirstAtt())+"</td>\n"+
               "   <td>"+Double.toString(rp.getAvgNumHints())+"</td>\n"+
               "   <td>"+Double.toString(rp.getAvgTimeToAttempt())+"</td>\n"+
               "   <td>"+Double.toString(rp.getAvgTimeToSolve())+"</td>\n"+
               "   <td>"+Double.toString(rp.getAvgTimeToHint())+"</td>\n"+
               " </tr>\n";

    }//for

    return head.concat(foot);
  }//report 4

  public String createReportFive(Connection conn, int classId) throws Exception {

    head =  head + "<html>\n"+
            "<head>\n"+
            "<title>Report 2</title>\n"+
            "</head>\n"+
            "\n"+
            "<body>";

    boolean solvedNoHelp = true;
    int numHints = 0;
    int timeToChoose =0;
    int timeToAnswer=0;
    int timeToHint=0;

    boolean ended = false;

    Hashtable skillHash = new Hashtable();

    String neck ="<table border=1 cellspacing=1 cellpadding=1>\n"+
                 " <tr>\n"+
                 "  <td>  </td>\n"+
                 "  <td><b># problems</b></td>\n"+
                 "  <td><b>% solved w/o help</td>\n"+
                 "  <td><b>Avg # of hints</td>\n"+
                 "  <td><b>% solved w/ help</td>\n"+
                 " </tr>\n";

    String SQL = "SELECT Skill.*, EpisodicData2.*, Student.classId, EpisodicData2.studId, EpisodicData2.id " +
                 "FROM ((Student INNER JOIN EpisodicData2 ON Student.id = EpisodicData2.studId) INNER JOIN ProblemSkill ON EpisodicData2.problemId = ProblemSkill.problemId) INNER JOIN Skill ON ProblemSkill.skillId = Skill.id " +
                 "WHERE Student.classId=" +Integer.toString(classId) + " " +
                 "ORDER BY EpisodicData2.studId, EpisodicData2.id;";


    Statement classSt1 = conn.createStatement();
    ResultSet rs = classSt1.executeQuery(SQL);

    head = head + "<h3>Detail Skill Info for Class#"+Integer.toString(classId)+"</h3>\n" + neck;

    while (rs.next()) {
      int probId = Integer.parseInt(rs.getString("id"));
      String action = rs.getString("action");

      if (action.equalsIgnoreCase("beginProblem"))
      {
        timeToChoose=0;
        timeToAnswer=0;
        timeToHint=0;
        numHints = 0;
        solvedNoHelp = true;
      }

      if (action.equalsIgnoreCase("attempt"))
      {

      }

      if (action.startsWith("hint"))
      {
        if (timeToHint==0)
          timeToHint=Integer.parseInt(rs.getString("probElapsed"));
        if (!(((Integer.parseInt(rs.getString("isCorrect"))) == 1)&&(timeToHint==0)))
          solvedNoHelp = false;
        else
          solvedNoHelp=true;
        numHints++;
      }

      if (action.equalsIgnoreCase("endProblem"))
      {
        int snh;
        if (solvedNoHelp)
          snh = 1;
        else
          snh = 0;

        int skipped;
        if (rs.getString("isCorrect").equals("0"))
          skipped = 1;
        else
          skipped = 0;

        if (skillHash.containsKey(new Integer(probId))) { //exisiting
          ((ReportSkill)skillHash.get(new Integer(probId))).seen(numHints,snh,skipped);
        }
        else { // new problem
          ReportSkill rsk = new ReportSkill(rs.getString("name"));
          rsk.seen(numHints,snh,skipped);
          skillHash.put(new Integer(probId), rsk);
        } //else

      }

    } //while

    Collection hashVal = skillHash.values();
    Iterator   iter    = hashVal.iterator();

    while (iter.hasNext())
    {
      ReportSkill rsk = (ReportSkill) iter.next();

      head = head +
             " <tr>\n"+
             "  <td><b>"+ rsk.getName() + "</b></td>\n" +
             "   <td>"+Integer.toString(hashVal.size())+"</td>\n"+
             "   <td>"+Double.toString(rsk.getSolvedFirstAtt())+"</td>\n"+
             "   <td>"+Double.toString(rsk.getNumHints())+"</td>\n"+
             "   <td>"+Double.toString((1.0 - (rsk.getSolvedFirstAtt() + rsk.getSkipped()))*100) + "</td>\n</tr>\n";
    }//for

    return head.concat(foot);


  } //report 4

  public String createReportSix(Connection conn, int classId) throws Exception {

    String neck ="<table border=1 cellspacing=1 cellpadding=1>\n"+
                 " <tr>\n"+
                 "  <td>  </td>\n"+
                 "  <td><b>Problems seen<b></td>\n"+
                 "  <td><b># Solved w/o Help</td>\n"+
                 "  <td><b>% Solved w/o Help</td>\n"+
                 "  <td><b># Skipped</td>\n"+
                 "  <td><b>% Skipped</td>\n"+
                 " </tr>\n";

    head =  "<html>\n"+
            "<head>\n"+
            "<title>Report 6</title>\n"+
            "</head>\n"+
            "\n"+
            "<body>";

    Hashtable probHash = new Hashtable();

    boolean solvedNoHelp = true;
    int numHints = 0;
    int timeToChoose =0;
    int timeToAnswer=0;
    int timeToHint=0;

    String SQL = "SELECT EpisodicData2.*, Student.fname, Student.lname" +
                 "FROM (Student RIGHT JOIN EpisodicData2 ON Student.id = EpisodicData2.studId) "+
                 "LEFT JOIN Class ON Student.classId = Class.id " +
                 "WHERE Class.id = " + Integer.toString(classId) + " "+
                 "ORDER BY Student.id, EpisodicData2.id;";


    Statement classSt1 = conn.createStatement();
    ResultSet rs = classSt1.executeQuery(SQL);

    head = head + "<h3>Detail Skill Info for Class #"+Integer.toString(classId)+"</h3>\n" + neck;

    while (rs.next()) {
      int probId = Integer.parseInt(rs.getString("problemId"));
//      String probName = rs.getString(rs.getString("nickName"));
      String action = rs.getString("action");

      if (action.equalsIgnoreCase("beginProblem"))
      {
        timeToChoose=0;
        timeToAnswer=0;
        timeToHint=0;
        numHints = 0;
        solvedNoHelp = true;
       }

      if (action.equalsIgnoreCase("attempt"))
      {
        if (((Integer.parseInt(rs.getString("isCorrect"))) == 1) && (timeToAnswer ==0))
          timeToAnswer=Integer.parseInt(rs.getString("probElapsed"));
        if (timeToChoose==0)
          timeToChoose=Integer.parseInt(rs.getString("probElapsed"));
      }

      if (action.startsWith("hint"))
      {
        if (timeToHint==0)
          timeToHint=Integer.parseInt(rs.getString("probElapsed"));
        if (!(((Integer.parseInt(rs.getString("isCorrect"))) == 1)&&(timeToHint==0)))
          solvedNoHelp = false;
        else
          solvedNoHelp=true;
        numHints++;
      }

      if (action.equalsIgnoreCase("endProblem"))
      {
        int snh;
        if (solvedNoHelp)
          snh = 1;
        else
          snh = 0;

        int skipped;
        if (rs.getString("isCorrect").equals("0"))
          skipped = 1;
        else
          skipped = 0;

        if (probHash.containsKey(new Integer(probId))) { //exisiting
          ((ReportProblem)probHash.get(new Integer(probId))).seen(timeToChoose,timeToAnswer,timeToHint,numHints,skipped);
        }
        else { // new problem
          ReportProblem rp = new ReportProblem(rs.getString("fname") + " " + rs.getString("lname"));
          rp.seen(timeToChoose,timeToAnswer,timeToHint,numHints,skipped);
          probHash.put(new Integer(probId), rp);
        } //else

      }

    } //while

    Collection hashVal = probHash.values();
    Iterator   iter    = hashVal.iterator();

    while (iter.hasNext())
    {
      ReportProblem rp = (ReportProblem) iter.next();

      head = head +
             " <tr>\n"+
             "  <td><b>"+ rp.getName() + "</b></td>\n" +
             "   <td>"+Integer.toString(hashVal.size())+"</td>\n"+
             "   <td>"+Double.toString(rp.getAvgSolvedFirstAtt())+"</td>\n"+
             "   <td>"+Double.toString(rp.getAvgSolvedFirstAtt() / (hashVal.size()*100)) + "</td>\n"+
             "   <td>"+Double.toString(rp.getAvgSkipped())+"</td>\n"+
             "   <td>"+Double.toString(rp.getAvgSkipped() / (hashVal.size() * 100)) + "</td>\n</tr>\n";
    }//for

    return head.concat(foot);
  }  //report 6

  public String getReportNum(AdminViewReportEvent e, int reportNum, Connection conn) throws Exception {
    switch (reportNum) {
      case 1:
        return this.createReportOne(conn,e.getStudId());

      case 2:
        return this.createReportTwo(conn,e.getStudId());

      case 3:
        return this.createReportThree(conn,e.getClassId());

      case 4:
        return this.createReportFour(conn,e.getClassId());

      case 5:
        return this.createReportFive(conn,e.getClassId());

      case 6:
        return this.createReportSix(conn,e.getClassId());

    }//switch
    return null;
  }


}//class

