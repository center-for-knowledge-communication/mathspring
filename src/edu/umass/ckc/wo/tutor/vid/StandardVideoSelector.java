package edu.umass.ckc.wo.tutor.vid;


import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.content.CCStandard;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.db.DbProblem;
import edu.umass.ckc.wo.db.DbUtil;
import edu.umass.ckc.wo.db.DbVideo;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.tutormeta.VideoSelector;
import edu.umass.ckc.wo.util.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * <p> Created by IntelliJ IDEA.
 * User: david
 * Date: Oct 16 2017
 * Time: 12:52 PM
 *
 * Requested by Ivon.  Selects a video for a problem based on CCSS and difficulty.
 * If the cur problem doesn't have a video, find a problem with the same standard and the similar difficulty level that does
 * have a video.
 * 
 * Kartik 02-12-2021 issue #384 video retrieval from video-standard-map table
 */
public class StandardVideoSelector implements VideoSelector {


    public void init(SessionManager smgr) throws Exception {
    }


    /**
     * Return the video with the most number of CCSS standards in common and the closest difficulty to the target problem.
     * @param conn
     * @param targetProbId
     * @return
     * @throws SQLException
     */
    public String selectVideo(Connection conn, int targetProbId) throws SQLException {

        Problem p = ProblemMgr.getProblem(targetProbId);
        String videoUrl = null;
        if (p.hasVideo())
            return p.getVideo();
        List<CCStandard> standards = p.getStandards();
        try {
        	String standardCode = standards.get(0).getCode();
        	videoUrl = DbVideo.getVideoByStandardCode(conn, standardCode, p.getProblemLanguage());
        }
        catch (Exception e) {
        	System.out.println("No standards found: prob " + p.getName());
        }
        return videoUrl;
        		
        // Re-implemented functionality to retrieve video for a problem, when video not found and commented previous code below (issue #384)
        
        /*List<Problem> relatedProbs = new ArrayList<Problem> ();
        // go through all the related problems, keeping only those that have videos
        double targetDiff = p.getDiff_level();
        for (CCStandard s : standards) {
            List<Problem> probs = ProblemMgr.getStandardProblems(conn, s.getCode());
            for (Problem p2: probs) {
                if (p2!=null && p2.hasVideo())
                    relatedProbs.add(p2);
            }
        }
        // If no problems had videos or if there were none with matching standards, exit
        if (relatedProbs.size() == 0) return null;
        Set s1 = p.getStandardsStringSet();

        // Try to find a problem that shares the most standards with the current problem.
        // This will be the problem with the largest intersection.
        // Create pairs [numStandardsInCommon, Problem] and put in a queue that is descending by number of in-common standards
        Comparator<Pair<Integer,Problem>> pairComparator = new Comparator<Pair<Integer, Problem>>() {
            @Override
            public int compare(Pair<Integer, Problem> o1, Pair<Integer, Problem> o2) {
                if (o1.getP1() < o2.getP1())
                    return 1;
                else if (o1.getP1() > o2.getP1())
                    return -1;
                else return 0;
            }
        };
        PriorityQueue<Pair<Integer,Problem>> q = new PriorityQueue<Pair<Integer,Problem>>(10,pairComparator);
        for (Problem p2 : relatedProbs) {
            Set s2 = p2.getStandardsStringSet();
            s2.retainAll(s1); // turns s2 into the intersection of s2 and s1.  All we care about is cardinality of this set
            q.add(new Pair(s2.size(), p2));
        }

        // The problems with the most number of in-common standards are at the front of the queue.

        int possible = q.size();
        Pair<Integer, Problem> best = q.peek();
        double bestdiff = Math.abs(targetDiff - best.getP2().getDiff_level());
        for (Pair<Integer,Problem> item: q) {
            if (best.getP1() > item.getP1())
                break;
            else {
                double diff2 = Math.abs(targetDiff - item.getP2().getDiff_level());
                if (diff2 < bestdiff) {
                    best = item;
                    bestdiff = diff2;
                }
            }
        }

        Problem winner = best.getP2();
        // print the candidates for debugging
//        System.out.print("Picked best for problem " + pm(p) + " from problem " + pm(winner) + " among " + possible + " candidates:");
//        for (Pair<Integer,Problem> item: q) {
//            System.out.print("problem-"+pm(item.getP2()) + " ");
//        }
//        System.out.println();
        return winner.getVideo(); */
    }

    // debugging util for above fn's commented out debug code
    private String pm (Problem p) {
        return p.getId() + "{" + p.getStandardsString() + "}";
    }



    public static void main(String[] args) {
        DbUtil.loadDbDriver();
        try {
            Connection conn = DbUtil.getAConnection("localhost");
            ProblemMgr.loadProbs(conn);
            List<Problem> probs= ProblemMgr.getAllProblems();
            StandardVideoSelector s = new StandardVideoSelector();
            int noVid = 0;
            int foundVid = 0;
            int hasVid =0;
            for (Problem p: probs)  {
                if (p.hasVideo())
                    hasVid++;
                String vid = s.selectVideo(conn,p.getId());
                if (vid != null)
                    foundVid++;
                else noVid++;
                System.out.println("Problem id " + p.getId() + " name: " + p.getName() + " video: " + vid);
            }
            System.out.println("There are " + hasVid + " problems that have their own video");
            System.out.println("There are " + foundVid + " problems that got video from another problem");
            System.out.println("There are " + noVid + " problems that cannot find any video");
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

}
