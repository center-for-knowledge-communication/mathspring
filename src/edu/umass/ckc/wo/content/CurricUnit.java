package edu.umass.ckc.wo.content;

import ckc.servlet.servbase.UserException;
import edu.umass.ckc.wo.cache.ProblemMgr;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 8/1/14
 * Time: 12:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class CurricUnit {
    public static final Logger logger = Logger.getLogger(CurricUnit.class);
    public enum type {cluster, standard, problems}
    private int id;
    private type t;
    private String stdId;
    private int clustId;
    private int lessonId;
    private int position;  // 1-based its position in the lesson (which implies that a curric unit exists only for one lesson)
    private int probId;
//    private List<Integer> probIds;

    private CCCluster cluster;
    private CCStandard standard;
//    private List<Problem> problems;
    private Problem problem;



    public CurricUnit (int id,String stdId, int clustId, int probId, int lessonId, int position) throws UserException, SQLException {
        this.id=id;
        this.t=type.standard;
        this.clustId = clustId;
        this.stdId=stdId;
        setProbId(probId);
        // We must make sure the content referred to by the CU exists.
        if (clustId > 0) {
            CCCluster clust = CCContentMgr.getInstance().getCluster(clustId);
            if (clust == null)
                throw new UserException("Curriculum Unit " + id + " has a cluster (" + clustId + ") that can't be found " );
        }
        if (stdId != null) {
            CCStandard std = CCContentMgr.getInstance().getStandard(stdId);
            if (std == null)
                throw new UserException("Curriculum Unit " + id + " has a standard (" + stdId + ") that can't be found " );

        }
        // Because people go in and disable problems, it is possible to have an ID that is not found.
        // In this case we do nothing and at runtime, we generate an error
        if (probId > 0) {
            Problem p = ProblemMgr.getProblem(probId);
            if (p == null)
                logger.error("Curriculum Unit " + id + " has a problem (" + probId + ") that can't be found " );
//                throw new UserException("Curriculum Unit " + id + " has a problem (" + probId + ") that can't be found " );
            this.setProblem(p);
        }
        this.lessonId=lessonId;
        setPosition(position);
    }



    public void setCluster(CCCluster cluster) {
        this.cluster = cluster;
    }

    public void setStandard(CCStandard standard) {
        this.standard = standard;
    }

    public int getProbId() {
        return probId;
    }

    public void setProbId(int probId) {
        this.probId = probId;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }



    public int getId() {
        return id;
    }

    public int getLessonId() {
        return lessonId;
    }

    public CCCluster getCluster() {
        return cluster;
    }

    public CCStandard getStandard() {
        return standard;
    }

    public void setPosition (int p) throws UserException {
        this.position = p;
        if (p < 1)
            throw new UserException("Curriculum Unit "+ this.getId() +  " must be given a position >= 1");
    }


    public int getPosition() {
        return position;
    }

    public String getStdId() {
        return stdId;
    }

    public int getClustId() {
        return clustId;
    }
}
