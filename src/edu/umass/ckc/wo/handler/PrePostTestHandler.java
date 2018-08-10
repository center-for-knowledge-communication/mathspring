package edu.umass.ckc.wo.handler;


import edu.umass.ckc.wo.event.NavigationEvent;
import edu.umass.ckc.wo.smgr.SessionManager;
import ckc.servlet.servbase.View;
import edu.umass.ckc.wo.db.DbClass;

import javax.servlet.ServletContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author unascribed
 * @version 1.0
 */

public class PrePostTestHandler implements View {
    private ServletContext sc;
    private Connection conn;
    private String testType;
    private String data;
    private SessionManager smgr;

    public PrePostTestHandler(ServletContext sc, SessionManager smgr, Connection conn) {
        this.sc = sc;
        this.conn = conn;
        this.smgr = smgr;
    }

    public String getView() throws Exception {
        StringBuffer res = new StringBuffer(64);
        // if they've done the pretest, they should do the post test
        String c = getPreTestCompleted();

        boolean preComp = smgr.getStudentState().getPretestCompleted();
        boolean postComp = smgr.getStudentState().getPosttestCompleted();
        // some classes are configured not to receive a pretest (they must be given a post test on entering test hut)
//        int gp = DbClass.getClassPrePostTest(smgr.getConnection(),smgr.getStudentClass(smgr.getStudentId()));
        int gp = -1;
        boolean givePretest=gp==1;
        if (preComp && postComp) {
            return "ack=true&allowEntry=false&reason=+" + edu.umass.ckc.wo.content.PrePostProblem.TC;
        } else if (preComp || !givePretest)
            return "ack=true&allowEntry=true&test_type=post";
            // haven't done pre test so do it
        else return "ack=true&allowEntry=true&test_type=pre";
    }


    // Will return the number of the pretest (from a problem given to the particular student)
    // that a student has done work on.
    private String getPreTestCompleted() throws Exception {
        String q = "select pre from PrePostTestProblems where studId=?";
        Connection conn = this.conn;
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setInt(1, smgr.getStudentId());
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String s = rs.getString(1);
            return s.trim();
        }
        return null;
    }

    private void savePrePost(int studId, int sessNum, String indicator, String f) throws Exception {

        String q = "insert into PrePostTestProblems (studId, sessionNumber," + indicator + ") values (?,?,?)";
        Connection conn = this.conn;
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setInt(1, studId);
        ps.setInt(2, sessNum);
        ps.setString(3, f);
        ps.execute();
    }

//  private String selectRandomTest () {
//    double r = Math.random();
//    String c;
//    if (r > 0.5)
//      c = "A";
//    else
//      c = "B";
//    return c;
//  }

    private String selectRandomTest() {
        return "C";
    }

    // build a comma delimited list String of problem names which are of the form <c><num>
    private String getPrePostSequence(String c, List probs) {
        StringBuffer sb = new StringBuffer();
        Iterator itr = probs.iterator();
        boolean first = true;
        while (itr.hasNext()) {
            String num = (String) itr.next(); // the problem number
            String prob = c + num;
            if (first) {
                sb.append(prob);
                first = false;
            } else sb.append("," + prob);
        }
        return sb.toString();
    }

    private void getPreTest(StringBuffer results) throws Exception {
        // f = select at random A or B
        String c = selectRandomTest();
        savePrePost(smgr.getStudentId(), smgr.getSessionNum(), "pre", c);
        // get a list of Strings that are the numbers of each Problem with form=f
        List probs = getProblemsWithForm(c);
        // return the testDefinition as a list of these
        results.append(NavigationHandler.ACK + "=" + NavigationHandler.TRUE + "\n");
        results.append("&" + NavigationEvent.TEST_TYPE + "=" + NavigationEvent.PRE + "\n");
        results.append("&" + NavigationHandler.TEST_TIME + "=" + NavigationHandler.PRE_TEST_TIME + "\n");
        results.append("&" + NavigationHandler.TEST_DEFINITION + "=" + getPrePostSequence(c, probs) + "\n");
    }

    private String nextPrePost(String pre) {
        //if (pre.equals("A")) return "B"; else return "A";
        return "C";
    }

    private void getPostTest(String pre, StringBuffer results) throws Exception {
        // if pre is A then f= B else f= A
        String f = nextPrePost(pre);
        savePrePost(smgr.getStudentId(), smgr.getSessionNum(), "post", f);
        // return the XML that is all the questions from Problem with form=f
        List probs = getProblemsWithForm(f);
        // return the testDefinition as a list of these things
        results.append(NavigationHandler.ACK + "=" + NavigationHandler.TRUE + "\n");
        results.append("&" + NavigationEvent.TEST_TYPE + "=" + NavigationEvent.POST + "\n");
        results.append("&" + NavigationHandler.TEST_TIME + "=" + NavigationHandler.POST_TEST_TIME + "\n");
        results.append("&" + NavigationHandler.TEST_DEFINITION + "=" + getPrePostSequence(f, probs) + "\n");
    }

    // return a list of Strings that are the problem names with form=f
    private List getProblemsWithForm(String f) throws Exception {
        String q = "select name from Problem where form=? or form like '%-%'";
        Connection conn = this.conn;
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setString(1, f);
        ResultSet rs = ps.executeQuery();
        List result = new ArrayList();
        while (rs.next()) {
            String name = rs.getString(1);
            result.add(name);
        }
        return result;
    }

}