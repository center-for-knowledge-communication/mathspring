package edu.umass.ckc.wo.tutor.probSel;

import ckc.servlet.servbase.UserException;
import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.tutormeta.ExampleSelector;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.db.DbProblem;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;

/**
 * <p> Created by IntelliJ IDEA.
 * User: david
 * Date: Aug 18, 2009
 * Time: 12:38:04 PM
 */
public class BaseExampleSelector implements ExampleSelector {

    public int selectProblem(Connection conn, int curProbId) throws Exception {

        int pid = doSelectExample(conn, curProbId);
        return pid;
    }

    private int doSelectExample (Connection conn, int curProbId) throws UserException, SQLException {

        DbProblem pmgr = new DbProblem() ;
        Problem p = ProblemMgr.getProblem(curProbId) ;
        //Problem p = pmgr.getProblemWithDifficulty(conn,curProbId) ;

        String thisProbSkills = pmgr.getProblemSkillsAsString(conn, p.getId()) ;
        int totalSkills = pmgr.getProblemSkills(conn, p.getId()).size() ;



        // DM 7/11/11  Formality problems do not have hints.   For now consider to not have any skills
        // and forget about having an example until skills are set up for these problems (if at all).
        // TODO figure out how to give 4mality probs skills so that examples can be found.
        if (thisProbSkills.equals("()"))
            return -1;

         /*  For testing the below query copy and paste this into a MYSQL query browser:
         select problemid, animationresource, answer, count(*) as numOverlap from hint h, problem p
where p.id != curProbId and skillid in
       (select distinct skillid from Hint where problemid=174 and skillid is not null)
   and problemid=p.id
   group by problemid
         having problemid in
                (select problemid from overallprobdifficulty where diff_level< .364)
                 order by numOverlap desc
          */
        // BUG FIX - Sometimes an example is returned that is the same problem as the curProbId.
        // Added test in query to prevent this.

        // 3/9/17 Mysql 5.7 on Ubuntu doesn't like this query with error:
        // jdbc4.MySQLSyntaxErrorException: Expression #5 of SELECT list is not in GROUP BY clause and contains nonaggregated column 'wayangoutpostdb.diff.diff_level' which is not functionally dependent on columns in GROUP BY clause; this is incompatible with sql_mode=only_full_group_by
        // so I added other columns to the group by until it stopped complaining
        //Give me a list of problems that are easier and cover those skills
        String OverlappingSkillProblems =
                "select p.id, animationresource, answer, count(*) as numOverlap, diff_level, abs(diff_level - " + p.getDifficulty() + ") as difference " +
                " from hint h, problem p, overallprobdifficulty diff " +
                " where p.id <> ? and skillid in " + thisProbSkills +
                " and p.id=h.problemid and p.status='ready' and p.form is null " +
                " and diff.problemid = p.id " +
                " group by p.id " +
//                " group by p.id, animationresource, answer,diff_level,difference " +
//                " having problemid in " +
//                " (select problemid from overallprobdifficulty where diff_level< " +
//                p.getDifficulty() +  ")" +
                " order by numOverlap desc, difference asc " ;

       PreparedStatement ps = conn.prepareStatement(OverlappingSkillProblems);
       ps.setInt(1,curProbId);
       ResultSet rs = ps.executeQuery();
       double minSkillOverlap = totalSkills * 0.9 ;

       while (rs.next()) {
           int problemid = rs.getInt(1) ;

           if ( rs.getInt("numOverlap") > minSkillOverlap )
                    return problemid ;

           return -1 ;           
                                          
  /*
           String exceedingSkillsInExample =
                   " select  count(*) as extraSkillsCount " +
                   " from hint " +
                   " where problemid=" + problemid +
                   " and skillid not in " + thisProbSkills ;

           PreparedStatement ps2 = conn.prepareStatement(exceedingSkillsInExample);
           ResultSet rs2 = ps2.executeQuery();

           if (!rs2.next())  //Got a perfect match
               return problemid ;

           else {  //some foreign skills in example
               int foreignSkills = rs2.getInt("extraSkillsCount") ;
               if (foreignSkills < 1)  // No foreign skill allowed in example
                    return problemid ;
           }
       } */
       }

       return -1 ;
    }
}
