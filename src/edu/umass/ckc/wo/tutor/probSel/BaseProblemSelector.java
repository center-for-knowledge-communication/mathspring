package edu.umass.ckc.wo.tutor.probSel;

import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.event.tutorhut.NextProblemEvent;
import edu.umass.ckc.wo.event.tutorhut.PreviewProblemEvent;
import edu.umass.ckc.wo.exc.DeveloperException;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.state.StudentState;
import edu.umass.ckc.wo.state.TopicState;
import edu.umass.ckc.wo.tutor.model.LessonModel;
import edu.umass.ckc.wo.tutor.model.LessonModel.difficulty;
import edu.umass.ckc.wo.tutor.model.TopicModel;
import edu.umass.ckc.wo.tutor.pedModel.ProblemScore;
import edu.umass.ckc.wo.tutormeta.ProblemSelector;
import edu.umass.ckc.wo.db.DbGaze;
import net.sf.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 1/30/14
 * Time: 4:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class BaseProblemSelector implements ProblemSelector {

    protected PedagogicalModelParameters parameters;
    protected TopicModel topicModel;
    protected LessonModelParameters lessonModelParameters;
    protected SessionManager smgr;

    public BaseProblemSelector(SessionManager smgr, LessonModel lessonModel, PedagogicalModelParameters params) {
        this.smgr = smgr;
        this.topicModel = (TopicModel) lessonModel;
        this.parameters=params;
        // When using this problem selector, there is the assumption that topics are used.
        // I don't think it's necessary to use TopicModelParameters though except that Pedagogies instantiate
        // by setting the topicModel.tmParams and its not so easy to have them set the topicModel.lmParams instead
        // (partially because of interleaved problem sets attributes in the topicModelParameters).
        // Tutoring strategies instantiate with the topicModelParameters.lmParameters set (and the tmParameters null)

        // Tutoring strategies have the lesson params on the lessonModel.lmParams.   Pedagogies have
        // the lesson params on lessonModel.tmParams, so we get the right one.
        LessonModelParameters lmParams = lessonModel.getLmParams();
        TopicModelParameters tmParams = ((TopicModel) lessonModel).getTmParams();
        if (lmParams != null)
            this.lessonModelParameters = lmParams; // tutoring strategies will have them here.
        else this.lessonModelParameters = tmParams; // pedagogies will have them here.
    }

    public static boolean hasInterleavedProblem (Connection conn, int studId) throws SQLException {
        ResultSet rs=null;
         PreparedStatement stmt=null;
        try {
            String q = "select count(*) from interleavedProblems where studid=? and shown=0";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,studId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                int c= rs.getInt(1);
                return c > 0;
            }
            return false;
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }

    public static Problem selectInterleavedProblem(Connection conn, int studId) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "select studid, probId,shown from interleavedProblems where studid=? and shown=0 order by position";
            stmt = conn.prepareStatement(q,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stmt.setInt(1,studId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                int probid= rs.getInt(2);
                rs.updateInt(3,1);
                rs.updateRow();
                return ProblemMgr.getProblem(probid);
            }
            else return null;
        }  finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }


    
    @Override
    /**
     * precondition:  This method is only called if we know the topic has no upcoming content failure and all other conditions for continuing in a topic
     * are met.    In theory,  there should be no fencepost errors based on this.
     */
    public Problem selectProblem(SessionManager smgr, NextProblemEvent e, ProblemScore lastProblemScore) throws Exception {
        long t = System.currentTimeMillis();


        if (topicModel.isInInterleavedTopic()) {
            return selectInterleavedProblem(smgr.getConnection(),smgr.getStudentId());
        }
        // DM 2/18 - note that this will take into account if curProb is broken and return SAME difficulty
/*
            if (e.getPreviewProblemData().length() > 0) {
            	if (!(e.getPreviewProblemData().equals("undefined"))) {
            		Problem problem = selectProblemUsingPreviewDifficulty(smgr,e);
            		return problem;
            	}
            }
*/
        
        TopicModel.difficulty nextDiff = topicModel.getNextProblemDifficulty(lastProblemScore);        
        StudentState state = smgr.getStudentState();
        // Gets problems with testable problems included if the user is marked to receive testable stuff.
        List<Integer> topicProbIds = topicModel.getUnsolvedProblems();
        int lastProbId = smgr.getStudentState().getCurProblem();
        List<String> brokenProblemsForThisStudent = smgr.getStudentState().getBrokenProblemIds(); // DM 2/5/18 added for Ivon request
        // if the last problem given wasn't solved it'll still be in the list.  Toss it out.  We don't want to show the same problem 2X in a row
        int loc = topicProbIds.indexOf(lastProbId);
        if (loc != -1)
            topicProbIds.remove(loc);
        // now remove broken problems from the list
        for (String pidstr: brokenProblemsForThisStudent) {
            loc = topicProbIds.indexOf(Integer.parseInt(pidstr));
            if (loc != -1)
                topicProbIds.remove(loc);
        }

        // Create a ArrayList of probIds and difficulty values
        
        Double minDiff = 9.99;
        Double maxDiff = 0.0;
        ArrayList<String> probDiffs = new ArrayList<String>();        
        for (int probId:topicProbIds) {
            Problem p = ProblemMgr.getProblem(probId);            
            probDiffs.add(String.valueOf(probId) + "~" + String.valueOf(p.getDifficulty()));
            if (p.getDifficulty() < minDiff) {
            	minDiff = p.getDifficulty();
            }
            if (p.getDifficulty() > maxDiff) {
            	maxDiff = p.getDifficulty();
            }
        }
        state.getTopicState().setMinPreviewDiff(String.valueOf(minDiff));
        state.getTopicState().setMaxPreviewDiff(String.valueOf(maxDiff));
        
        
        
//        List<Problem> topicProblems = xx;
        int lastIx = state.getCurProblemIndexInTopic();
        int nextIx=-1;

        // Error in setting of difficulty Rate is possible if configuration of the strategy was not done well.
        // Must correct it to a default because will lead to malfunction; set to the default difficulty rate
        if (parameters.getDifficultyRate() == 0.0)
            parameters.setDifficultyRate(PedagogicalModelParameters.DIFFICULTY_RATE);
        // lastIx is -1 when the topic is new.
        if (lastIx == -1)
            nextIx = (int) Math.round((topicProbIds.size()-1) / parameters.getDifficultyRate());

        if (nextIx == -1 && nextDiff == LessonModel.difficulty.EASIER) {
            if (lastIx <= 0) {
            	System.out.println("lastIx=" + String.valueOf(lastIx));
            	System.out.println("topicProbIds.size=" + String.valueOf(topicProbIds.size()));
                throw new DeveloperException("Last problem index=0 and want easier problem.   Content failure NOT PREDICTED by TopicSelector");
            }
            else {
                nextIx =(int) Math.round(lastIx / parameters.getDifficultyRate());
            }
        }
        else if (nextIx == -1 && nextDiff == LessonModel.difficulty.HARDER) {
            if (lastIx >= topicProbIds.size()) {
            	System.out.println("lastIx=" + String.valueOf(lastIx));
            	System.out.println("topicProbIds.size=" + String.valueOf(topicProbIds.size()));
            	System.out.println("Last problem >= number of problems in topic.   Content failure NOT PREDICTED by TopicSelector");
//                throw new DeveloperException("Last problem >= number of problems in topic.   Content failure NOT PREDICTED by TopicSelector");
                nextIx = lastIx;
                
            }
            nextIx = lastIx + ((int) Math.round((topicProbIds.size()-1 - lastIx) / parameters.getDifficultyRate()));

        }
        else if (nextIx == -1 && nextDiff == LessonModel.difficulty.SAME) {
            nextIx = Math.min(lastIx, topicProbIds.size()-1);
        }
        nextIx = Math.min(nextIx, topicProbIds.size()-1);
        state.setCurTopicHasEasierProblem(nextIx > 0);
        if (nextIx < topicProbIds.size() - 1)
            state.setCurTopicHasHarderProblem(true);
        else state.setCurTopicHasHarderProblem(false);
        // it takes 125 ms to get to here

        int nextProbId = topicProbIds.get( nextIx);
        state.setCurProblemIndexInTopic( nextIx);
        Problem p = ProblemMgr.getProblem(nextProbId);

        return p;
    }

    @Override
    
    public Problem selectProblemUsingPreviewDifficulty(SessionManager smgr, ProblemScore lastProblemScore, String problemPreviewData) throws Exception {
        

        String gazeParams = smgr.getGazeParamsJSON();
        String gazeParamsSp1[] = gazeParams.split("expCondition\":");
        String gazeParamsSp2[] = gazeParamsSp1[1].split(",");
        
        String studentTestGroup = gazeParamsSp2[0];
        
    	JSONObject probDiffEventJsonObject = new JSONObject();
    	
    	Problem selectedproblem = null;

    	if (topicModel.isInInterleavedTopic()) {
            return selectInterleavedProblem(smgr.getConnection(),smgr.getStudentId());
        }
    	// DM 2/18 - note that this will take into account if curProb is broken and return SAME difficulty
        TopicModel.difficulty nextDiff = topicModel.getNextProblemDifficulty(lastProblemScore);
        StudentState state = smgr.getStudentState();
        // Gets problems with testable problems included if the user is marked to receive testable stuff.
        List<Integer> topicProbIds = topicModel.getUnsolvedProblems();
        int lastProbId = smgr.getStudentState().getCurProblem();
        List<String> brokenProblemsForThisStudent = smgr.getStudentState().getBrokenProblemIds(); // DM 2/5/18 added for Ivon request
        // if the last problem given wasn't solved it'll still be in the list.  Toss it out.  We don't want to show the same problem 2X in a row
        int loc = topicProbIds.indexOf(lastProbId);
        if (loc != -1)
            topicProbIds.remove(loc);
        // now remove broken problems from the list
        for (String pidstr: brokenProblemsForThisStudent) {
            loc = topicProbIds.indexOf(Integer.parseInt(pidstr));
            if (loc != -1)
                topicProbIds.remove(loc);
        }

        // Create a ArrayList of probIds and difficulty values
        
        Double minDiff = 9.99;
        Double maxDiff = 0.0;
        ArrayList<String> probDiffs = new ArrayList<String>();        
        for (int probId:topicProbIds) {
            Problem p = ProblemMgr.getProblem(probId);            
            probDiffs.add(String.valueOf(probId) + "~" + String.valueOf(p.getDifficulty()));
            if (p.getDifficulty() < minDiff) {
            	minDiff = p.getDifficulty();
            }
            if (p.getDifficulty() > maxDiff) {
            	maxDiff = p.getDifficulty();
            }
        }
        state.getTopicState().setMinPreviewDiff(String.valueOf(minDiff));
        state.getTopicState().setMaxPreviewDiff(String.valueOf(maxDiff));
        
        int lastIx = state.getCurProblemIndexInTopic();
        int nextIx=-1;

        // lastIx is -1 when the topic is new.
        if (lastIx == -1)
            nextIx = (int) Math.round((topicProbIds.size()-1) / parameters.getDifficultyRate());


        
      // Start of binary section

      // Error in setting of difficulty Rate is possible if configuration of the strategy was not done well.
      // Must correct it to a default because will lead to malfunction; set to the default difficulty rate
      if (parameters.getDifficultyRate() == 0.0)
          parameters.setDifficultyRate(PedagogicalModelParameters.DIFFICULTY_RATE);
      // lastIx is -1 when the topic is new.
      if (lastIx == -1)
          nextIx = (int) Math.round((topicProbIds.size()-1) / parameters.getDifficultyRate());

      if (nextIx == -1 && nextDiff == LessonModel.difficulty.EASIER) {
          if (lastIx <= 0) {
          	System.out.println("lastIx=" + String.valueOf(lastIx));
          	System.out.println("topicProbIds.size=" + String.valueOf(topicProbIds.size()));
              throw new DeveloperException("Last problem index=0 and want easier problem.   Content failure NOT PREDICTED by TopicSelector");
          }
          else {
              nextIx =(int) Math.round(lastIx / parameters.getDifficultyRate());
          }
      }
      else if (nextIx == -1 && nextDiff == LessonModel.difficulty.HARDER) {
          if (lastIx >= topicProbIds.size()) {
          	System.out.println("lastIx=" + String.valueOf(lastIx));
          	System.out.println("topicProbIds.size=" + String.valueOf(topicProbIds.size()));
          	System.out.println("Last problem >= number of problems in topic.   Content failure NOT PREDICTED by TopicSelector");
//              throw new DeveloperException("Last problem >= number of problems in topic.   Content failure NOT PREDICTED by TopicSelector");
              nextIx = lastIx;
              
          }
          nextIx = lastIx + ((int) Math.round((topicProbIds.size()-1 - lastIx) / parameters.getDifficultyRate()));

      }
      else if (nextIx == -1 && nextDiff == LessonModel.difficulty.SAME) {
          nextIx = Math.min(lastIx, topicProbIds.size()-1);
      }
      nextIx = Math.min(nextIx, topicProbIds.size()-1);
       
      probDiffEventJsonObject.put("binaryDirection",nextDiff);
      //probDiffEventJsonObject.put("binarySelectedDiffcultyValue", previewData[0]);
      

      // Start of problempreview section
      
      int preview_nextIx = -1;
      
        boolean curTopicHasEasierProblem = state.curTopicHasEasierProblem();
        boolean curTopicHasHarderProblem = state.curTopicHasHarderProblem();
        String  topicInternalState="";

        String previewData[] = problemPreviewData.split("~");

        // data for logging

    	probDiffEventJsonObject.put("previewDiffcultyValue", previewData[0]);
    	probDiffEventJsonObject.put("previewDirection", previewData[1]);
    	probDiffEventJsonObject.put("previewPerformance", previewData[2]);
    	//probDiffEventJsonObject.put("previewPerformance", ".27");


        Double previewDiff = Double.valueOf(previewData[0]);

        TopicModel.difficulty previewNextDiff = LessonModel.difficulty.SAME;
        if (previewData[1].equals("EASIER")) {
        	previewNextDiff = LessonModel.difficulty.EASIER;
        }
        if (previewData[1].equals("HARDER")) {
        	previewNextDiff = LessonModel.difficulty.HARDER;
        }        
        
        if (preview_nextIx == -1 && (previewNextDiff == LessonModel.difficulty.EASIER)) {
        	int idx = -1;
        	double testMinDiff = minDiff;
	        for (int i=probDiffs.size()-1;i>=0; i--) {
	        	Double probDiff = minDiff;
	        	String tmp = (String) probDiffs.get(i);
	        	String t2[] = tmp.split("~");
	        	
	        	probDiff = Double.valueOf(t2[1]);
	        	if (probDiff < previewDiff) {
	        		idx = i;
	        		preview_nextIx = i;
	        		testMinDiff = probDiff;
	            	probDiffEventJsonObject.put("previewSelectedDiffcultyValue", t2[1]);
//		        	CurTopicHasEasierProblem = true;
	        		break;
	        	}
	        }
	        if ((preview_nextIx == -1) || (testMinDiff <= minDiff)) {
	        	curTopicHasEasierProblem = false;	        	
	        	topicInternalState = TopicState.END_OF_TOPIC;
	        }
            else {
            	curTopicHasEasierProblem = true;
            }
        }
        if (preview_nextIx == -1 && (previewNextDiff == LessonModel.difficulty.HARDER)) {       
        	int idx = -1;
        	double testMaxDiff = maxDiff;
        	for (int i=0;i<probDiffs.size(); i++) {
	        	String tmp = (String) probDiffs.get(i);
	        	String t2[] = tmp.split("~");
	        	
        		idx = i;
        		Double probDiff = Double.valueOf(t2[1]);
	        	if (probDiff > previewDiff ) {
	        		preview_nextIx = i;
	        		testMaxDiff = probDiff;
	            	probDiffEventJsonObject.put("previewSelectedDiffcultyValue", t2[1]);
//		            curTopicHasHarderProblem = true;	        	
	        		break;
	        	}

	        }
	        if ((preview_nextIx == -1) || (testMaxDiff >= maxDiff)) {

            	preview_nextIx = idx - 1;
                curTopicHasHarderProblem = false;
	        	topicInternalState = TopicState.END_OF_TOPIC;
            }
            else {
            	curTopicHasHarderProblem = true;
            }
        }

        if (preview_nextIx == -1 && (previewNextDiff == LessonModel.difficulty.SAME)) {
        	int idx = -1;
        	double testMaxDiff = maxDiff;
        	for (int i=0;i<probDiffs.size(); i++) {
	        	String tmp = (String) probDiffs.get(i);
	        	String t2[] = tmp.split("~");
	        	
        		idx = i;
        		Double probDiff = Double.valueOf(t2[1]);
	        	if (probDiff > previewDiff ) {
	        		preview_nextIx = i;
	        		testMaxDiff = probDiff;
	            	probDiffEventJsonObject.put("previewSelectedDiffcultyValue", t2[1]);
//		            curTopicHasHarderProblem = true;	        	
	        		break;
	        	}

	        }
	        if ((preview_nextIx == -1) || (testMaxDiff >= maxDiff)) {

            	preview_nextIx = idx - 1;
                curTopicHasHarderProblem = false;
	        	topicInternalState = TopicState.END_OF_TOPIC;
	        }
	        else {
	           	curTopicHasHarderProblem = true;
	        }
/*            
            else {
            	curTopicHasHarderProblem = true;
	        	if (curTopicHasEasierProblem) {
	        		idx = -1;
	    	        for (int i=probDiffs.size()-1;i>=0; i--) {
	    	        	String tmp = (String) probDiffs.get(i);
	    	        	String t2[] = tmp.split("~");
	    	        	
	    	        	Double probDiff = Double.valueOf(t2[1]);
	    	        	if (probDiff < previewDiff) {
	    	        		idx = i;
	    	        		preview_nextIx = i;
	    	            	probDiffEventJsonObject.put("previewSelectedDiffcultyValue", t2[1]);
	    	        		break;
	    	        	}
	    	        }
		            if (idx == topicProbIds.size() - 1) {
		            	preview_nextIx = idx - 1;
		                curTopicHasEasierProblem = false;
			        	topicInternalState = TopicState.END_OF_TOPIC;
		            }
		            else {
		            	curTopicHasEasierProblem = true;
		            }
	        	}
            }
*/        		        
            if ((curTopicHasEasierProblem == false) && (curTopicHasHarderProblem == false)) {
            	preview_nextIx = -1;
            }
        }
        
        int binaryNextProbId  = topicProbIds.get( nextIx);;
        int previewNextProbId = -1;

        
        if (studentTestGroup.equals("201")) {
  	      state.setCurTopicHasEasierProblem(nextIx > 0);
  	      if (nextIx < topicProbIds.size() - 1)
  	          state.setCurTopicHasHarderProblem(true);
  	      else state.setCurTopicHasHarderProblem(false);
  	      binaryNextProbId = topicProbIds.get( nextIx);
  	      state.setCurProblemIndexInTopic( nextIx);
  	      selectedproblem = ProblemMgr.getProblem(binaryNextProbId);
        }
        else {
            if (preview_nextIx >= 0) {
    	        previewNextProbId = topicProbIds.get( preview_nextIx);
    	        state.setCurProblemIndexInTopic( preview_nextIx);
    	        selectedproblem = ProblemMgr.getProblem(previewNextProbId);
    	        state.setCurTopicHasEasierProblem(curTopicHasEasierProblem);
    	        state.setCurTopicHasHarderProblem(curTopicHasHarderProblem);
    	        if (topicInternalState.length() > 0) {
    	        	state.setTopicInternalState(topicInternalState);
    	        }
            }
            else {
            	previewNextProbId = -1;
            	selectedproblem = null;
    	        if (topicInternalState.length() > 0) {
    	        	state.setTopicInternalState(topicInternalState);
    	        }
            }
        }
	    probDiffEventJsonObject.put("binarySelectedProblemId", String.valueOf(binaryNextProbId));
	  	probDiffEventJsonObject.put("previewSelectedProblemId", String.valueOf(previewNextProbId));
        probDiffEventJsonObject.put("expCondition", studentTestGroup);
        int gazeEventId = DbGaze.insertGazePredictionEvent(smgr.getConnection(),smgr.getStudentId(), smgr.getSessionId(),   probDiffEventJsonObject);   
        DbGaze.updateGazePredictionEvent(smgr.getConnection(), smgr.getSessionId(), gazeEventId);
        return selectedproblem;
    }


    /**
     * precondition:  This method is only called if we know the topic has no upcoming content failure and all other conditions for continuing in a topic
     * are met.    In theory,  there should be no fencepost errors based on this.
     */
    public TopicModel.difficulty getNextProblemDifficulty(SessionManager smgr, PreviewProblemEvent e, ProblemScore lastProblemScore) throws Exception {
        long t = System.currentTimeMillis();
        // DM 2/18 - note that this will take into account if curProb is broken and return SAME difficulty
        TopicModel.difficulty nextDiff = topicModel.getNextProblemDifficulty(lastProblemScore);
        return nextDiff;
    }


    
    @Override
    public void setParameters(PedagogicalModelParameters params) {
        this.parameters = params;
    }
}
