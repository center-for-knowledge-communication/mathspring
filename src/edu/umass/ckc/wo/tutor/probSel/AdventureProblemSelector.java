package edu.umass.ckc.wo.tutor.probSel;

import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.event.StudentActionEvent;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.tutormeta.StudentModel;
import edu.umass.ckc.servlet.servbase.ActionEvent;
import edu.umass.ckc.wo.util.SqlQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Vector;


/** As of 1/09 this class appears to be no longer in use.   It used to implement ProblemSelector but
 * that interface needed to change the selectProblem signature so I've removed the implements ProblemSelector
 * so this will compile
 */

public class AdventureProblemSelector { // implements ProblemSelector {

    public AdventureProblemSelector() {
    }

    public Problem selectProblem(ActionEvent e, SessionManager smgr, StudentModel studentModel) throws Exception {

        String advname = ((StudentActionEvent) e).getAdventureName();
        int advprobnum = ((StudentActionEvent) e).getAdventureProblemNumber();
        long elapsedtime = ((StudentActionEvent) e).getElapsedTime();
        String userinput = ((StudentActionEvent) e).getUserInput();

        Connection conn_ = smgr.getConnection();

        Problem p;

        SqlQuery q = new SqlQuery();
        List result = new Vector();

        String s = "select id, helpprobid, helpprobname, helptext, ordering " +
                " from AdventureHelp where advname='" + advname + "' and " +
                " advprobid=" + advprobnum +
                " and id NOT IN " +
                " (select advhelpid from EpisodicAdventureData " +
                " where sessNum= " + smgr.getSessionNum() +
                " and advname='" + advname + "')" +
                " order by ordering";

        ResultSet rs = q.read(conn_, s);
        while (rs.next()) {
            //Get the information about the help that will be shown
            int id = rs.getInt("helpprobid");
            int nthhint = rs.getInt("ordering");
            String resource = rs.getString("helpprobname");
            int helpid = rs.getInt("id");

            if (resource == null) {
                //Text message instead of SAT problem as help
                resource = rs.getString("helptext");
            }
            p = new Problem(id, resource, "");
            // no longer saving cur prob in smgr
            try {
                saveData(conn_, smgr, userinput, elapsedtime, advname, advprobnum, nthhint, helpid);
            } catch (Exception ex) {
            }

            //Return a help message or an SAT problem as help, whatever hasn't been seen yet.
            return p;
        }
        SqlQuery.closeRS(rs);
        //No more help available
        try {
            saveData(conn_, smgr, userinput, elapsedtime, advname, advprobnum, -1, -1);
        } catch (Exception ex) {
        }
        return new Problem(-1, "There is no more help available, make your best guess.", "");
    }


    public void saveData(Connection conn_, SessionManager smgr, String userinput, long elapsedtime, String advname, int advprobnum, int nthhint, int helpid)
            throws Exception {
        // Insert data into EpisodicAdventureData table
        SqlQuery q = new SqlQuery();

        String s = new String("insert into EpisodicAdventureData " +
                " (studId,sessNum,userInput,elapsedTime,advprobid,nthhint,advhelpid, advname) values " +
                " (" + smgr.getStudentId() + "," + smgr.getSessionNum() + ", '" + userinput + "'," +
                elapsedtime + "," + advprobnum + "," + nthhint + "," + helpid + ",'" + advname + "')");

        PreparedStatement ps = conn_.prepareStatement(s);

        try {
            ps.execute();
        } catch (Exception ee) {
            System.out.println(ee);
            throw ee;
        }
    }

    public void init(SessionManager smgr) throws Exception {

    }

   public Problem selectProblemLessDifficulty (SessionManager smgr) throws Exception {
        return null;
    }
    public Problem selectProblemSameDifficulty (SessionManager smgr) throws Exception {
        return null;
    }
    public Problem selectProblemMoreDifficulty (SessionManager smgr) throws Exception {
        return null;
    }    

}