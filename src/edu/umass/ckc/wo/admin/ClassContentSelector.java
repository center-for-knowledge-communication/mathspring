package edu.umass.ckc.wo.admin;

import edu.umass.ckc.wo.beans.ClassInfo;
import edu.umass.ckc.wo.beans.Topic;
import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.content.CCStandard;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.content.TopicMgr;
import edu.umass.ckc.wo.db.DbClass;
import edu.umass.ckc.wo.db.DbClassPedagogies;
import edu.umass.ckc.wo.db.DbProblem;
import edu.umass.ckc.wo.db.DbTopics;
import edu.umass.ckc.wo.tutor.Pedagogy;
import edu.umass.ckc.wo.tutor.Settings;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 7/27/15
 * Time: 3:47 PM
 * To change this template use File | Settings | File Templates.
 * 
 * Frank	08-08-20	issue #51 don't show active topics which have ZERO problems 
 * Kartik   09-20-20    issue #232 setting only one topic to be activated on class creation
 * Frank	08-10-21    issue #491 change default LCs to 1 & 2 (i.e. Jane Learning Companion and Jake Learning Companion) 
 * Frank	05-13-23 	issue #763 make lCs selectable by class
 */
public class ClassContentSelector {

    private Connection conn;

    public ClassContentSelector(Connection conn) {
        this.conn = conn;
    }


    // Run an algorithm which selects topics and problems for the class depending on settings in the event
    // Make sure to save the settings in the db so that re-visits to the simple class config page can show the
    // current settings.
    public void selectContent(ClassInfo ci, String lc, String selectedLCs) throws SQLException {
        String grade = ci.getGrade();
        String lowDiff = ci.getSimpleLowDiff();
        String highDiff = ci.getSimpleHighDiff();
        // remove topics and problems that don't fit the grade and difficulty levels
        removeTopicsAndProblems(ci.getClassid(),grade,lowDiff,highDiff);
        // set the class pedagogies based on collab and learning companions selections in the event
        setClassPedagogies(ci.getClassid(), ci.getSimpleLC(), ci.getSimpleCollab(), selectedLCs);
        // set the problem selectors difficulty rate based on the event
        setProblemDiffRate(ci.getClassid(), ci.getSimpleDiffRate(),ci.isDefaultClass());
    }

    private void setProblemDiffRate(int classid, String simpleDiffRate, boolean isDeafultClass) throws SQLException {
        // if simpleDiffRate is normal, set rate to 2 (which moves 1/2 the distance in the direction of right/wrong),
        //  mastery threshold to 0.85, and content failure to 4.
        // if simpleDiffRate is aggressive set rate to 1.5, mastery thresh to 0.85, and content failure to 3
        // if simpleDiffRate is gentle set rate to 3, mastery thresh to 0.9, content failure to 5
        double r = 2.0;
        double m = 0.95;
        int cf = 5;
        if (simpleDiffRate.equals("gentle"))  {
            r = 3.0;
            m = 0.9;
            cf = 5;
        }
        else if (simpleDiffRate.equals("aggressive")) {
            r = 1.5;
            cf = 3;
        }
        DbClass.setClassConfigDiffRate(conn, classid, r, m, cf, isDeafultClass);
    }

    private Pedagogy getPedagogyByName (String name) {
        for (Pedagogy p : Settings.pedagogyGroups.values())
            if (p.getName().equals(name))
                return p;
        return null;
    }

    /**
     * Set the class's pedagogies based on some simple indications of what learning companion is requested
     * and if collaboration is involved.
     *
     *
     * @param simpleLC  Values are: none, male, female, both
     * @param simpleCollab Values are: none, some, alot
     */
    private void setClassPedagogies(int classId, String simpleLC, String simpleCollab, String selectedLCs) throws SQLException {
        Pedagogy p=null;
        
        if (selectedLCs.length() > 0 ) {
	        DbClassPedagogies.removeClassPedagogies(conn, classId);
	
	        if (simpleCollab.equals("none")) {
	            if (simpleLC.equals("none")) {
	                p = getPedagogyByName("No Learning Companion");
	            	DbClassPedagogies.setClassPedagogy(conn, classId, p.getId());
	            }
	            else if (simpleLC.equals("male")) {
	                p = getPedagogyByName("Jake Learning Companion");
	            	DbClassPedagogies.setClassPedagogy(conn, classId, p.getId());
	            }
	            else if (simpleLC.equals("female")) {
	                p = getPedagogyByName("Jane Learning Companion");
	                DbClassPedagogies.setClassPedagogy(conn, classId, p.getId());
	            }
	            // if they want both, given them Jane semi and Jake full empathy
	            else if (simpleLC.equals("both")) {
	                p = getPedagogyByName("Jane Learning Companion");
	                DbClassPedagogies.setClassPedagogy(conn, classId, p.getId());
	                p = getPedagogyByName("Jake Learning Companion");
	            }
	            else if (simpleLC.equals("multi-lingual")) {
	                p = getPedagogyByName("Lucas Learning Companion");
	                DbClassPedagogies.setClassPedagogy(conn, classId, p.getId());
	                p = getPedagogyByName("Isabel Learning Companion");
	                DbClassPedagogies.setClassPedagogy(conn, classId, p.getId());
	                p = getPedagogyByName("Jane Learning Companion");
	                DbClassPedagogies.setClassPedagogy(conn, classId, p.getId());
	                p = getPedagogyByName("Jake Learning Companion");
	                DbClassPedagogies.setClassPedagogy(conn, classId, p.getId());
	            }
	            else if (simpleLC.equals("selectable")) {
	            	String[] id = selectedLCs.split("~"); 
	            	for(int i=0;i < id.length; i++) {
	            		DbClassPedagogies.setClassPedagogy(conn, classId, id[i]);
	            	}	
	            }
	            
	        }
	        else {
	            if (simpleLC.equals("none"))
	                p = getPedagogyByName("Collaboration with no learning companion");
	            else if (simpleLC.equals("male"))
	                p = getPedagogyByName("Collaboration with Jake Semi Empathy");
	            else if (simpleLC.equals("female"))
	                p = getPedagogyByName("Collaboration with Jane Semi Empathy");
	                // if they want both, given them Jane semi and Jake semi empathy with collab
	            else if (simpleLC.equals("both")) {
	                p = getPedagogyByName("Collaboration with Jake Semi Empathy");
	                DbClassPedagogies.setClassPedagogy(conn, classId, p.getId());
	                p = getPedagogyByName("Collaboration with Jane Semi Empathy");
	            }
	            DbClassPedagogies.setClassPedagogy(conn, classId, p.getId());
	        }
    	}
    }

    private int gradeToNum (String grade) {
        try {
            int i = Integer.parseInt(grade);
            return i;
        } catch (NumberFormatException e) {
            if (grade.toUpperCase().equals("K"))
                return 0;
            else if (grade.toUpperCase().equals("H"))
                return 9;
            else if (grade.toUpperCase().equals("ADULT")) // a value given by teacher tools simple class config.
                return 9;
            return 9;
        }

    }

    // Given something like above3,above2,above1, above0
    private int getLevelAbove (String highDiff) {
        if (highDiff.startsWith("above")) {
            return Integer.parseInt(highDiff.substring(5));
        }
        else return 0;
    }

    // Given something like below3,below2,below1, below0
    private int getLevelBelow (String lowDiff) {
        if (lowDiff.startsWith("below")) {
            return Integer.parseInt(lowDiff.substring(5));
        }
        else return 0;
    }

    private boolean withinDifficultyRange (CCStandard std, String grade, String lowDiff, String highDiff) {
        String ccstdGrade = std.getGrade();
        int stdGrade = gradeToNum(ccstdGrade);
        int myGrade = gradeToNum(grade);
        int lowDecr = getLevelBelow(lowDiff);
        int highIncr = getLevelAbove(highDiff);
        return (stdGrade >= (myGrade-lowDecr)) && (stdGrade <= (myGrade+highIncr));
    }

    /**
     * Using the grade and lowDiff (3below, 2below, 1below, nobelow) and highDiff (noabove, 1above,2above,3above)
     * to eliminate content.
     * Algorithm:  Go through all topics.  Use the CC standard on the problem to see if the problem is within
     * the grade level specified.  If not, add it to the classes omitted problems list.   If a topic becomes
     *  depleted of problems to the point where there are only 3 or less left, add the topic to the omitted
     *  topics for the class.
     * @param classId
     * @param grade
     * @param lowDiff
     * @param highDiff
     */
    private void removeTopicsAndProblems (int classId, String grade, String lowDiff, String highDiff) throws SQLException {
        DbTopics.clearClassLessonPlan(conn,classId); // gets rid of any previous class lesson plan
        List<Topic> topics =  ProblemMgr.getAllTopics();
        // remove all the omitted problems
        new DbProblem().clearClassOmittedProblems(conn,classId);
        TopicMgr topicMgr = new TopicMgr();
    	int i=1;
        for (Topic t: topics) {
        	if ((ProblemMgr.getTopicProblemCount(t.getId()) > 0) && t.getCcStandards() != null
        			&& (hasStandardWithinBounds(t.getCcStandards().iterator(),grade,lowDiff,highDiff))) {          
        		int numRemaining = removeProblemsFromTopic(classId,t,grade,lowDiff,highDiff);
        		if (numRemaining > 1 && i==1 
            			&& (hasStandardWithinBounds(t.getCcStandards().iterator(),grade,"below0","above0"))) {
        			DbTopics.insertTopic(conn, classId, i, t.getId());
        			i++;
        		}
        	}
        }
    }

    // returns the number of problems remaining the topic after we've deleted ones that are out of range.
    private int removeProblemsFromTopic(int classId, Topic t, String grade, String lowDiff, String highDiff) throws SQLException {
    List<Problem> probs = ProblemMgr.getTopicProblems(t.getId()); 
        List<Integer> deactivatedIds = new ArrayList<Integer>();
        DbProblem probMgr = new DbProblem();
        String class_language = probMgr.getClassLanguage(conn,probs,classId);
        for (Problem p: probs) {
            // If the problem has one or more standard within the bounds of the desired range, keep it.
            if (hasStandardWithinBounds(p.getStandards().iterator(),grade,lowDiff,highDiff) && p.getProblemLanguage().equalsIgnoreCase(class_language))
                continue;
            else
            	if(!deactivatedIds.contains(p.getId()))
            		deactivatedIds.add(p.getId());
        }
        probMgr.setClassTopicOmittedProblems(conn, classId, t.getId(), deactivatedIds);
        return Math.abs(probs.size() - deactivatedIds.size());
    }

  private boolean hasStandardWithinBounds(Iterator ccstds, String grade, String lowDiff, String highDiff) {

        while (ccstds.hasNext()) {
            CCStandard c = (CCStandard) ccstds.next();
            if (withinDifficultyRange(c,grade,lowDiff,highDiff))
                return true;
        }
        return false;
    }
}
