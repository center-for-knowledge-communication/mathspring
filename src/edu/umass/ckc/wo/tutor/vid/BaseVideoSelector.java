package edu.umass.ckc.wo.tutor.vid;


import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.db.DbUtil;
import edu.umass.ckc.wo.tutormeta.VideoSelector;
import edu.umass.ckc.wo.tutormeta.StudentModel;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.content.Video;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.db.DbProblem;
import edu.umass.ckc.wo.tutor.Settings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static edu.umass.ckc.wo.db.DbUtil.loadDbDriver;

/**
 * <p> Created by IntelliJ IDEA.
 * User: david
 * Date: Aug 18, 2009
 * Time: 2:40:36 PM
 */
public class BaseVideoSelector implements VideoSelector {

    
    public void init(SessionManager smgr) throws Exception {
    }

    //  TODO Ivon will write processing to select a video
    public String selectVideo(Connection conn, int curProbId) throws SQLException { 
//        return "http://chinacat.cs.umass.edu/wayang/video/CorrespondingAnglesHard.flv";
        DbProblem pmgr = new DbProblem() ;
//        Problem p = pmgr.getProblemWithDifficulty(conn,curProbId) ;
        Problem p = ProblemMgr.getProblem(curProbId) ;

        String thisProbSkills = pmgr.getProblemSkillsAsString(conn, p.getId()) ;
        // DM 7/11/11
        // Have to abandon if the problem is a 4mality problem with no hints and no skills
        // TODO find some skills for 4mality probs
        if (thisProbSkills.equals("()"))
            return null;

        //Give me a list of videos that cover those skills
        String overlappingSkillVideos =
                " select videoid, video.name as videoname, video.link as vidlink, count(*) as problemSkillsCovered " +
                " from videoskill, video " +
                " where videoskill.videoid=video.id" +
                " and skillid in " + thisProbSkills +
                " group by videoid " +
                " having problemSkillsCovered > 0 " +
                " order by problemSkillsCovered desc, videoid desc " ;

       PreparedStatement ps = conn.prepareStatement(overlappingSkillVideos);
       ResultSet rs = ps.executeQuery();

       while (rs.next()) {
           int videoid = rs.getInt(1) ;
           String videoname = rs.getString(2) ;
           String vidlink = rs.getString(3);
           boolean hasLink = !rs.wasNull() && !vidlink.equals("");
           int problemSkillsCovered = rs.getInt("problemSkillsCovered") ;

           String exceedingSkillsInVideo =
                   " select  count(*) as extraSkillsCount " +
                   " from videoskill " +
                   " where videoid=" + videoid +
                   " and skillid not in " + thisProbSkills ;

           PreparedStatement ps2 = conn.prepareStatement(exceedingSkillsInVideo);
           ResultSet rs2 = ps2.executeQuery();

           if (!rs2.next()) {  //Got a perfect match
               if (hasLink)
                    return vidlink;
               else return Settings.videoURI + videoname ;
           }
           else {
               int foreignSkills = rs2.getInt("extraSkillsCount") ;
               if (problemSkillsCovered > foreignSkills)  // 1 foreign skill in the video
                    if (hasLink)
                        return vidlink;
                    else return Settings.videoURI + videoname ;
           }
       }
       return null ;
    }

    public String getVideoSkillsAsString(Connection conn, int videoId) throws java.sql.SQLException {

        String q = " select distinct skillid " +
                " from videoskill, Skill " +
                " where videoid=" + videoId +
                " and skillid=skill.id " ;

        PreparedStatement ps = conn.prepareStatement(q);
        ResultSet rs = ps.executeQuery();
        String skills = "(" ;

        while (rs.next()) {
            if ( ! skills.equals("("))
                skills = new String(skills + ",") ;

            skills = new String( skills + rs.getInt("skillid"));
        }
        return skills + ")" ;
    }

    public static void main(String[] args) {
        DbUtil.loadDbDriver();
        try {
            Connection conn = DbUtil.getAConnection();
            ProblemMgr.loadProbs(conn);
            List<Problem> probs= ProblemMgr.getAllProblems();
            BaseVideoSelector s = new BaseVideoSelector();
            for (Problem p: probs)  {
                String vid = s.selectVideo(conn,p.getId());
                System.out.println("Problem id " + p.getId() + " name: " + p.getName() + " video: " + vid);
            }
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

}
