package edu.umass.ckc.wo.content;

import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.db.DbCC;
import edu.umass.ckc.wo.db.DbProblem;
import edu.umass.ckc.wo.exc.DeveloperException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Note that before this class can have its content loaded the ProblemMgr needs to have loaded Problems.   This
 * is so that the Standards can point to their problems.
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 7/28/14
 * Time: 4:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class CCContentMgr {
    private Map<Integer,CCCluster> clusters;
    private Map<String,CCStandard> standards;

    private static CCContentMgr instance;

    private CCContentMgr() {}

    public static CCContentMgr getInstance () {
        if (instance == null)
            instance = new CCContentMgr();
        return instance;
    }

    public boolean loadContent (Connection conn) throws Exception {
        clusters = new Hashtable<Integer, CCCluster>(1028);
        standards = new Hashtable<String, CCStandard>(1028);
        List<CCCluster> clustList = DbCC.getClusters(conn);
        List<CCStandard> stdList = DbCC.getStandards(conn);
        for (CCCluster c: clustList)
            clusters.put(c.getId(),c);
        for (CCStandard s: stdList)
            standards.put(s.getId(),s);

        setStandardClusterPointers(clustList,stdList);
        addProblemsToStandards(conn);
        return true;
    }

    // adds lists of standards to the clusters and a cluster to each standard
    private void setStandardClusterPointers(List<CCCluster> clustList, List<CCStandard> stdList) throws DeveloperException {
        // assumption:  The standards all point to their parent clusters using a valid ID for a cluster.
        for (CCStandard s: stdList) {
            int clustId= s.getClustId();
            CCCluster parent = getCluster(clustId);
            if (parent == null)
//                throw new DeveloperException("Cluster not found " + clustId + " while setting parent of Standard " + s.getId());
                System.out.println("Cluster not found " + clustId + " while setting parent of Standard " + s.getId());
            else {
                s.setCluster(parent);
                parent.addStandard(s);
            }

        }

    }

    // sets the list of problems in the standard.  The ProblemMgr must have first loaded its problems
    // before this method can work.
    private void addProblemsToStandards(Connection conn) throws SQLException, DeveloperException {
        for (CCStandard s: standards.values()) {
            List<Integer> pids = DbCC.getStandardProblemIds(conn, s.getId());
            for (int pid: pids) {
                Problem p = ProblemMgr.getProblem(pid);
                if (p != null)
                    s.addProblem(p);
                else {

                    //                    throw new DeveloperException("Problem not found in ProblemMgr when attempting to add a Problem to Standard. Problem ID: " + pid + " Standard ID: " + s.getId());
                    System.out.print(pid + "," + s.getId() + ", Problem not found in ProblemMgr when attempting to add a Problem to Standard. Problem ID: " + pid + " Standard ID: " + s.getId());
                    if (DbProblem.isProblemDefined(conn,pid))
                        System.out.println("  Problem " + pid + " is in the db but is not considered active content.");
                    else System.out.println(" Problem " + pid + " is not in the db");
                }
 }
        }
    }

    public Collection<CCCluster> getClusters () {
        return this.clusters.values();
    }

    public Collection<CCStandard> getStandards() {
        return standards.values();
    }




    private boolean addStandard(CCStandard standard) {
        if (standards.get(standard) == null)  {
            standards.put(standard.getId(), standard);
            return true;
        }
        else return false;
    }

    // Clusters are being selected in order by ID and inserted in the list in order
    public boolean addCluster(CCCluster c) {
        if (clusters.get(c.getId()) == null)  {
            clusters.put(c.getId(),c);
            return true;
        }
        else return false;
    }

    public CCCluster getCluster (int id) {
        return this.clusters.get(id);
    }

    public CCStandard getStandard (String id) {
        return this.standards.get(id);
    }

    public void setStandardProblems (CCStandard standard, List<Problem> problems) {
        standard.setProblems(problems);
    }



}
