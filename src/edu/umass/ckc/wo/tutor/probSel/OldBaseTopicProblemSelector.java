package edu.umass.ckc.wo.tutor.probSel;

//import edu.umass.ckc.wo.content.ProblemImpl;


/**
 * This selector implements a strategy of problem selection where groups of problems have been put into "topics" (aka chunks).
 * The topics are then placed in a sequence (see below on how).  This selector then proceeds through the problem
 * groups in the specified order.  Selecting problems from within a topic uses an adaptive selection strategy
 * whereby a student's actual performance on the last problem within the group is compared with his expected performance (see below
 * on how expected performance is determined) to create a difficulty requirement of either 'easier' or 'harder'.
 * In general the selection algorithm looks at actual behavior and compares to expected behavior and does something like:
 * If the actual performance is >= expected, then a harder problem is selected from within the problem set.
 * If the actual performance is < expected, an easier problem is selected.
 * This problem selector will force a student solve every problem correctly in the topic before moving to another topic.   The only
 * ways around this are if the student answers the two hardest problems correctly with no hints and on the first attempt OR they
 * fail to solve the two easiest.
 *
 * The comparison of actual to expected is more complex than this, however.   The three components of behavior (# mistakes,
 * # hints, time) are compared and used for determining whether the next problem should be easier, harder, or of similar
 * difficulty.
 *
 * The problem selector moves to the next topic when the current topic is complete.  When entering
 * a new group the selector chooses the problem with median difficulty level from that group.
 *
 * The current topic is considered complete when all problems in the group have been attempted depending on the criteria defined
 * for the class, according to minimum and maximum thresholds across several criteria, isuch as mastery, problems seen, time, etc.
 *
 * There are a fixed number of topics in the system and they are defined by Wayang staff members (not teachers)
 * In a particular group no assumptions should be made regarding what a student might have seen previously in some other group
 *  (since an instructor sequences groups however he likes).  This means one should not include problems that are
 * off-topic in an attempt to review something that might have come before in another group.   It does not prevent one
 * from placing the same problem in multiple groups, however.  This means a problem could be shown twice in the context
 * of two different topics (topics).  The only thing preventing it from being shown a second time is if the student has
 * solved it correctly.
 *
 * As a note to the topic builder it is probably best to build groups that contain problems related to a single
 * topic (e.g. right angle problems).  That way we can label the group with the topic and an instructor knows what
 * is inside it and can eliminate the whole topic if it doesn't serve his class's needs.   A motivated instructor
 * could go in and turn off certain problems within a group but that is probably less likely to occur.
 *
 *  An instructor may choose the groups he wishes to work with and place them in a sequence of his choosing.
 *  He may turn-off certain problems within topics.  Instructors cannot create topics
 *

 *
 * Performance is based on 3 factors:
 *     - time spent
 *     - # mistakes made (incorrect answers submitted)
 *     - # hints seen
 * Expected Performance is the population's average values for the 3 factors.
 *
  * Each class that uses the system may want
 * to create this sequence of topics for their particular needs which means that we define topics on a per class basis
 * using a table ClassProblemGroup with columns:  classID, probGroupID, probId, seqPos
 * For a given class, each row defines what problem set a given problem is in.  The seqPos defines the position of the problem set in the
 * sequence of problem sets that will be presented.
 *
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Jun 25, 2007
 * Time: 8:56:59 AM
 */



//public class OldBaseTopicProblemSelector extends BaseProblemSelector {
//    private static Logger logger = Logger.getLogger(OldBaseTopicProblemSelector.class);
//    Random indexGenerator_ = new Random();
//
//    private int newIndex;
//    private List solvedProbIds;
//    private NextProblemEvent myForcedTopicEvent ;
////    private boolean canShowExternalActivity;
//
//    protected SessionManager smgr;
//    protected int divisor =2;  // this value will be overwritten by the value coming out of the classConfig table for this students class
//
//    private long timeInTopic=0;
//
//    public static final int QUICK_GUESS_TIME = 10 * 1000;  // a response in less than 10 secs is considered a random guess.
//    private static final String FIRST_PROB = "firstProb";
//    private static final String EASIER_PROB = "easierProb";
//    private static final String HARDER_PROB = "harderProb";
//    private static final String SAMEDIFF_PROB = "sameDifficultyProb";
//
//
//    private  int maxNumberProbs = 30;
//    private  int minNumberProbs = 3;
//    private  long maxTimeInTopic = 5 * 60 * 1000;
//    private  long minTimeInTopic = 30 * 1000; // 30 secs (if there are no problems available this may cause it to sit 30 secs)
//    private  long contentFailureThreshold = 1; // the number of times it will select a problem within this topic when it can't meet
//                                                // the easier/harder/same criteria.   Once exceeded, jump topics
//    private  double topicMastery = 0.85;
//    private boolean showTopicIntroFirst;
//    private boolean showExampleProblemFirst;
//
//
//
//    public BaseTopicProblemSelector(int maxNumberProbsPerTopic, long maxTimeInTopic,
//                                    int contentFailureThreshold, double topicMasteryLevel, boolean showTopicIntro, boolean showExampleProbFirst) {
//        this.maxNumberProbs = maxNumberProbsPerTopic;
//        this.maxTimeInTopic = maxTimeInTopic;
//        this.contentFailureThreshold = contentFailureThreshold;
//        this.topicMastery = topicMasteryLevel;
//        this.showTopicIntroFirst = showTopicIntro;
//        this.showExampleProblemFirst = showExampleProbFirst;
//    }
//
//    public BaseTopicProblemSelector(PedagogicalModelParameters params) {
//        this.maxNumberProbs = params.getMaxNumberProbs();
//        this.maxTimeInTopic = params.getMaxTimeMs();
//        this.contentFailureThreshold = params.getContentFailureThreshold();
//        this.topicMastery = params.getMastery();
//        this.minNumberProbs = params.getMinNumberProbs();
//        this.minTimeInTopic = params.getMinTimeInTopic();
//        this.divisor = params.getDifficultyRate();
//        this.showExampleProblemFirst = params.isShowExampleFirst();
//        this.showTopicIntroFirst= params.isShowTopicIntro();
//    }
//
//
//    public void setServletInfo (SessionManager smgr) throws Exception {
//        this.smgr = smgr;
//        conn = smgr.getConnection();
//        int studentID = smgr.getStudentId();
//        classID = smgr.getStudentClass(studentID);
//        topicLoader = new TopicLoader(conn, classID, smgr);
//    }
//
//
//
//
//    protected Problem getProblemBetweenIndices(int startIndex, int endIndex) throws Exception {
//
//
//        //getting here with no problems in list, just return null
//        if (probsInTopic.size() <= 0) {
//            return null;
//        }
//        assert(startIndex >= 0 && startIndex <= endIndex && endIndex < probsInTopic.size());
//
//        int offset = (endIndex - startIndex + 1) / divisor;
//        this.newIndex = startIndex + offset;
//        logger.debug("want problem between indices: " + startIndex + " and " + endIndex + ": selected index is: " + newIndex);
//        return probsInTopic.get(this.newIndex);
//    }
//
//
//
//    /**
//     * Will get a easier problem.  Failing that it will switch topics if the content failure threshold
//     * has been exceeded.
//     * @return
//     * @throws Exception
//     */
//    private Problem getEasierProblem () throws Exception {
//        // If lastProbIndex is 0, this is a content failure (goal of easier prob cannot be achieved).
//        // If the number of failures exceeds the contentFailureThreshold, switch topics.
//        StudentState state = smgr.getStudentState();
//        // if we attempt to get an easier problem than the easiest problem, instead get one that is slightly harder
//        if (lastProbIndex <= 0)  {
//            state.setContentFailureCounter(state.getContentFailureCounter()+1);
////            if (state.getContentFailureCounter() >= contentFailureThreshold)
//            // switch to next topic if the content failure threshold has been exceeded and the min time in topic
//            // and min number problems in topic have been satisfied
//            if (isContentFailureAndMinsMet(state))
//                return getNextTopicProblem(state);
//
//            else {
//                Problem p = getSimilarDifficultyProblem();
//                if ( p == null )
//                    //The last problem was the only one left in the topic.
//                    // If we return null, it will provoque noMoreProblems=true
//                    //This is not necessarily what we want if there are other topics remaining
//                    if ( state.isProblemSolved() )
//                        return getNextTopicProblem(state);
//                    else
//                        //If the last problem was skipped, and it is the only one left, show it again.
//                        p = probsInTopic.get(lastProbIndex) ;
//
//                return p ;
//            }
//        }
//        else {
//  //          if ( lastProb != null  && ! lastProb.getName().startsWith("problem") ) {  //lastProblem was an external URL Problem
//  //              probsInTopic = topicLoader.excludeExternalURLProblems(probsInTopic, lastProb.getId()) ;
//  //              int newLastProbIndex = topicLoader.indexOfProblem(probsInTopic, this.lastProb.getId()) ;
//
//  //              if ( newLastProbIndex-1 < 0 )            {
//  //                  System.out.println("newLastProbIndex-1 =" + (newLastProbIndex-1) + " <0" );
//  //                  return getSimilarDifficultyProblem();                 /// Failing to find an easier problem, with this extra restriction
//  //              }
//  //              else
//  //                  return getProblemBetweenIndices(0, newLastProbIndex-1) ;
//  //          }
//  //          else {
//                return getProblemBetweenIndices(0,lastProbIndex-1);
//   //         }
//        }
//    }
//
//    private boolean isContentFailureAndMinsMet (StudentState state) {
//        boolean b = state.getContentFailureCounter() >= contentFailureThreshold ||
//                  (state.getTopicNumProbsSeen() >= this.maxNumberProbs && timeInTopic >= this.minTimeInTopic) ||
//                  ( timeInTopic >= this.maxTimeInTopic && state.getTopicNumProbsSeen() >= this.minNumberProbs );
//        return b;
//    }
//
//    /**
//     * Will get a harder problem.  Failing that it will switch topics if the content failure threshold
//     * has been exceeded.
//     * @return
//     * @throws Exception
//     */
//    private Problem getHarderProblem () throws Exception {
//        StudentState state = smgr.getStudentState();
//        // If lastPRoblemIndex is greater than the size of the list, this is a content failure (cannot find harder prob)
//        // If number of failures exceeds the contentFailureThreshold, switch topics.
//        if (lastProbIndex >= probsInTopic.size()-1)  {
//            state.setContentFailureCounter(state.getContentFailureCounter()+1);
////            if (state.getContentFailureCounter() >= contentFailureThreshold)
//            // switch topics if the content failure threshold is exceeded and we've spent the min time in the topic
//            // and seen the min number of probs
//            if (isContentFailureAndMinsMet(state))
//                return getNextTopicProblem(state);
//
//            else {
//                Problem p = getSimilarDifficultyProblem();
//                if ( p == null )  //The last problem was the only one left
//                    //The last problem was the only one left in the topic.
//                    // If we return null, it will cause noMoreProblems=true
//                    //This is not necessarily what we want if there are other topics remaining
//                    if ( state.isProblemSolved() )
//                        return getNextTopicProblem(state);
//                    else
//                        //If the last problem was skipped, and it is the only one left, show it again.
//                        p = probsInTopic.get(lastProbIndex) ;
//
//                return p ;
//            }
//        }
////        else if ( lastProb != null  && ! lastProb.getName().startsWith("problem") ) {  //lastProblem was an external URL Problem
////                probsInTopic = topicLoader.excludeExternalURLProblems(probsInTopic, lastProb.getId()) ;
////                int newLastProbIndex = topicLoader.indexOfProblem(probsInTopic, this.lastProb.getId()) ;
//
////                if ( newLastProbIndex+1 > probsInTopic.size()-1 )            {
////                   System.out.println("newLastProbIndex+1 =" + (newLastProbIndex+1) + " > probsInTopic.size()-1=" + (probsInTopic.size()-1)) ;
////                    return getSimilarDifficultyProblem();
////                }
////                else
////                    return getProblemBetweenIndices(newLastProbIndex+1, probsInTopic.size()-1);
////        }
////        else
//             return getProblemBetweenIndices(lastProbIndex+1, probsInTopic.size()-1);
//   }
//
//    // get a problem with an index just below the last problem index (so its slightly easier).
//    // If the last problem was index 0, we must return the next harder problem
//    protected Problem getSimilarDifficultyProblem () throws Exception {
//
//        int newLastProbIndex = lastProbIndex ;
//
//        //IF lastProblem was an external URL Problem, do not show another external URL problem next
//        if ( lastProb != null  && ! lastProb.getName().startsWith("problem") ) {
//                probsInTopic = topicLoader.excludeExternalURLProblems(probsInTopic, lastProb.getId()) ;
//                newLastProbIndex = topicLoader.indexOfProblem(probsInTopic, this.lastProb.getId()) ;
//        }
//
//        //TODO if only 1 left in topic it is the last problem.   If the last problem was not correctly solved we need to give it
//        // again and this just returns null
//        if (probsInTopic.size() < 1) {
//            logger.debug("getSimilarDifficultyProblem:  no problems left to select from.  We should have switched topics!");
//            return null;
//        }
//        // problem at position 0 was shown but not solved (we know this because caller first called shouldSwitchTopics which
//        // would have said "yes" if there was only 1 prob in the list and it was solved.
//        else if (probsInTopic.size() == 1)   {
//            logger.debug("getSimilarDifficultyProblem:  Only one problem left in topic.  It has been shown but not solved.  Must show again until other exit criteria are met.");
//
//            return probsInTopic.get(0);
//        }
//
//        if (newLastProbIndex > 0) {
//            newIndex = newLastProbIndex-1;
//            return probsInTopic.get(newIndex);
//        }
//        else { //LastProbIndex is zero
//            newIndex = newLastProbIndex+1;
//            return probsInTopic.get(newIndex);
//        }
//    }
//
//
//    /**
//     * If just beginning to work in this topic , this will return a problem in the middle of the topic.
//     * If all problems in the topic are done or if the max number have been shown or if the time has been exceeded
//     * move to another topic.   If not on a boundary, it returns null
//     * @param smgr
//     * @param state
//     * @return
//     * @throws Exception
//     */
//    private Problem chooseProblemAtBeginningOfTopic(SessionManager smgr, StudentState state) throws Exception {
//        // if no problems have been given in this topic, choose one of median difficulty
//        Problem p=null;
//        if (lastProb == null && probsInTopic.size() > 0) {
//            p= getProblemBetweenIndices(0, probsInTopic.size()-1);
//            state.setTopicNumProbsSeen(state.getTopicNumProbsSeen()+1);
//           // state.setTimeInTopic();
//            state.addProblemSelectionCriterion(FIRST_PROB);
//            return p;
//        }
//        else return null;     //Not a problem at the beginning of topic
//    }
//
//
//    public Problem getSpecificTopicProblem (int myTopicId, StudentState state, NextProblemEvent e) throws Exception {
//
//        myForcedTopicEvent = e ;
//
//        if ( myTopicId != topicID ) {       //Chose a different topic now.
//            state.setSidelinedTopic(state.getCurTopic());
//            state.setCurTopic(myTopicId);
//            topicID = myTopicId;
//
//            state.setTopicIntroShown(false) ;
//            state.setTimeInTopic(0);
//            state.setTopicNumProbsSeen(0);
//            state.setContentFailureCounter(0);
//            logger.debug("Next Forced topic is " + topicID);
//            return selectProblemInForcedTopicMode(smgr,smgr.getStudentModel(), null);
//        }
//        return selectProblemInForcedTopicMode(smgr,smgr.getStudentModel(), e);
//    }
//
//
//
//
//    public Problem getNextTopicProblem (StudentState state) throws Exception {
//        logger.debug("getting next topic: getNextTopicProblem");
//        int nextTopicID = topicLoader.getNextTopicWithAvailableProblems(conn,classID,topicID,state);
//        if (nextTopicID == -1)
//            return null;
//        else
//            state.setCurTopic(nextTopicID);
////        int nextTopicID = topicLoader.setNextTopic(conn,classID,topicID,state);
////        if (nextTopicID == topicID) {
////            return null;
////        }
//        topicID = nextTopicID;
//        resetStateForNextTopic(state);
//        logger.debug("Next topic is " + topicID);
//        Problem p= selectProblem(smgr,smgr.getStudentModel(), (NextProblemEvent) null);
//        return p;
//    }
//
//
//
//    private void prepareTopic (SessionManager smgr) throws Exception {
//        topicLoader.prepareForSelection(smgr);  // gets rid of problems not in class lessonplan
//        classID = topicLoader.classID;
//        topicID = topicLoader.topicID;
//        probsInTopic = topicLoader.probsInTopic;
//        lastProb = topicLoader.lastProb;
//        lastProbMode = topicLoader.lastProbMode;
//        applyHeuristics();
//        lastProbIndex = probsInTopic.indexOf(lastProb);
//
//    }
//
//
//    // Roughly the old heuristic for removing problems that were examples or solved.
//    public List<Problem> removeExamplesAndSolved (int lastProbID, List<Problem> probsInGroup ) {
//         StudentState studentState = smgr.getStudentState();
//         // omit problems that have been given out as example problems as well as solved problems
//         List<String> correctlySolvedProbs = topicLoader.getSatProblemsSolved(studentState); // ids returned as Strings per change to StudentState
//         List<String> exampleProblemsGiven = studentState.getExampleProblemsGiven();
//         correctlySolvedProbs.addAll(exampleProblemsGiven);
//         logger.debug("Solved problems: " + correctlySolvedProbs);
//
//         // if the last prob was an example it should not be considered either.
//         if (lastProbID != -1 && !Problem.isExampleOrDemo(lastProbMode))
//             correctlySolvedProbs.remove(Integer.toString(lastProbID)); // don't include the last problem
//         // The reason we leave last problem in is because selector uses its position in the list returned to guide the
//         // selection.
//         // remove the problems that have been solved correctly
//         Iterator itr = probsInGroup.iterator();
//         while (itr.hasNext()) {
//             Problem problem = (Problem) itr.next();
//             if ( problem != null) {
//                 int id = problem.getId();
//                 if (inList(correctlySolvedProbs, id) && id != lastProbID)
//                     itr.remove();
//             }
//         }
//         return probsInGroup; // return the list of problems in the group that haven't been solved correctly
//
//    }
//
//
//    private void applyHeuristics() throws Exception {
//        probsInTopic = topicLoader.removeRecentExamplesAndCorrectlySolvedProblems(smgr, lastProb != null ? lastProb.getId() : -1, probsInTopic);
//        logger.debug("Unsolved problems: " + probsInTopic);
//    }
//
//
//
//    public Problem getSpecificProblem(NextProblemEvent e) throws UserException, SQLException {
//        if (e != null && e.getProblemNumber() != null) {
//            Problem p =  ProblemMgr.getProblem(Integer.parseInt(e.getProblemNumber()));
//            if (p == null)
//                throw new UserException("BaseTopicProblemSelector.selectProblem  NextProblemEvent requested problem that cannot be found (id=" + e.getProblemNumber() + ")");
//            else {
//                if (e.getTopicToForce() != -1)
//                    p.setInTopicId(e.getTopicToForce());
//                return p;
//            }
//        }
//        // gets a specific problem for debugging
//        else if (e != null && e.getProbName() != null) {
//            Problem p = ProblemMgr.getProblemByName(e.getProbName());
//             if (p == null)
//                    throw new UserException("You requested problem that cannot be found (name=" + e.getProbName() + ")");
//             else return p;
//        }
//        return null;
//    }
//
//
//    /** If an intervention selector lets user control difficulty of problem selection, this method will
//     * choose an easier problem.
//     * @param smgr
//     * @param e
//     * @return
//     * @throws Exception
//     */
//    public Problem selectProblemLessDifficulty(SessionManager smgr, NextProblemEvent e) throws Exception {
//        setTimeInTopic(smgr.getStudentState(),e.getProbElapsedTime()); // sets an instance variable
//        StudentState state = smgr.getStudentState();
//        StudentModel sm = smgr.getStudentModel();
//        // need to check topic switching criteria before selecting easier prob.
//        if (topicLoader.shouldSwitchTopics(state,e.getProbElapsedTime(),sm,maxNumberProbs,maxTimeInTopic,topicMastery, minNumberProbs, minTimeInTopic)) {
//            if ( e.getTopicToForce() > 0 )
//                return null ;
//            return getNextTopicProblem(state);
//        }
//
//
//        prepareTopic(smgr);
//        // if no problems have been given in this topic, choose one of middle difficulty
//        Problem p = chooseProblemAtBeginningOfTopic(smgr,smgr.getStudentState());
//        if (p == null)
//            p= getEasierProblem();
//        return p;
//
//    }
//    /** If an intervention selector lets user control difficulty of problem selection, this method will
//     * choose a problem of similar difficulty.
//     * @param smgr
//     * @param e
//     * @return
//     * @throws Exception
//     */
//    public Problem selectProblemSameDifficulty(SessionManager smgr, NextProblemEvent e) throws Exception {
//        setTimeInTopic(smgr.getStudentState(),e.getProbElapsedTime()); // sets an instance variable
//        StudentState state = smgr.getStudentState();
//        StudentModel sm = smgr.getStudentModel();
//        if (topicLoader.shouldSwitchTopics(state,e.getProbElapsedTime(),sm,maxNumberProbs,maxTimeInTopic,topicMastery,
//                        minNumberProbs, minTimeInTopic)) {
//            if ( e.getTopicToForce() > 0 )
//                return null ;
//            return getNextTopicProblem(state);
//        }
//        prepareTopic(smgr);
//        // if no problems have been given in this topic, choose one of middle difficulty
//        Problem p = chooseProblemAtBeginningOfTopic(smgr,smgr.getStudentState());
//        // need to check topic switching criteria before selecting same diff prob.
//        if (p == null)
//            p= getSimilarDifficultyProblem();
//        return p;
//    }
//
//    /** If an intervention selector lets user control difficulty of problem selection, this method will
//     * choose a harder problem.
//     * @param smgr
//     * @param e
//     * @return
//     * @throws Exception
//     */
//    public Problem selectProblemMoreDifficulty(SessionManager smgr, NextProblemEvent e) throws Exception {
//        setTimeInTopic(smgr.getStudentState(),e.getProbElapsedTime()); // sets an instance variable
//        StudentState state = smgr.getStudentState();
//        StudentModel sm = smgr.getStudentModel();
//        // need to check topic switching criteria before selecting harder prob.
//        if (topicLoader.shouldSwitchTopics(state,e.getProbElapsedTime(),sm,maxNumberProbs,maxTimeInTopic,topicMastery, minNumberProbs, minTimeInTopic)) {
//            if ( e.getTopicToForce() > 0 )
//                return null ;
//            return getNextTopicProblem(state);
//        }
//        prepareTopic(smgr);
//        Problem p = chooseProblemAtBeginningOfTopic(smgr,smgr.getStudentState());
//        // otherwise select harder problem
//        if (p == null)
//            p= getHarderProblem();
//        return p;
//    }
//
//    /**
//     *
//     * @param smgr
//     * @param studentModel
//     * @param e NextProblemEvent passed so that debugging options are available.
//     * mode=XXX will force a certain mode (example, practice) to be used.   probName or probId will force it
//     * to return a given that problem.   The event will be null if this is just following topic switching caused
//     * by a content failure.
//     * @return
//     * @throws Exception
//     */
//    public Problem selectProblem(SessionManager smgr, StudentModel studentModel, NextProblemEvent e) throws Exception {
//        logger.debug(">selectProblem 1");
//        //  e will be null if topic was just switched because of content failure (which means we don't need to run the test)
//        if (e != null)
//            setTimeInTopic(smgr.getStudentState(),e.getProbElapsedTime()); // sets an instance variable
//        else setTimeInTopic(smgr.getStudentState(),0);
//        Problem p;
//        // Gets a specific problem (used for debugging and MPP can request this but will also pass the topicId)
//        if ((p = getSpecificProblem(e)) != null)  {
//            logger.debug("<selectProblem 1a");
//            return p;  // If the topic was being forced as well as the prob, the inTopic instance var is set
//            // TODO need to make sure that logging in both logs shows the topic for this problem but subsequent problems need to be
//            // in the established topic the student was in.
//
//        }
//
//        StudentState state = smgr.getStudentState();
//        StudentModel sm = smgr.getStudentModel();
//        classID = topicLoader.determineClass();
//        topicID = topicLoader.getCurTopic(conn,state,classID);
////        canShowExternalActivity = areExternalActivityCriteriaMet(state);
//        p= selectProbHelper(smgr, e, state, sm);
//        if (p != null)
//            p.setInTopicId(topicLoader.getTopicId());
//        logger.debug("<selectProblem 1b");
//        return p;
//    }
//
//    // The current (dumb)heuristic is to see if the student has been in the topic for more than a certain amount of time.   If so,
//    // then external activities are made available.
//
//    // external activity selection has been put into an intervention selector and no longer happens in problem selection
////    private boolean areExternalActivityCriteriaMet(StudentState state) throws SQLException {
////        long timeInTopic = state.getTimeInTopic() ;
////        if (timeInTopic > 0) {
////            timeInTopic = (timeInTopic / (1000 * 60)); // convert milliseconds to minutes since thats how the threshold is stored.
////            return timeInTopic > DbClass.getClassExternalActivityThreshold(conn, classID);
////        }
////        else return false;
////    }
//
//    public Problem selectProblemInForcedTopicMode(SessionManager smgr, StudentModel studentModel, NextProblemEvent e) throws Exception {
//        logger.debug("selecting Problem");
//        //  e will be null if topic was just switched because of content failure (which means we don't need to run the test)
//        if (e != null)
//            setTimeInTopic(smgr.getStudentState(),e.getProbElapsedTime()); // sets an instance variable
//        else setTimeInTopic(smgr.getStudentState(),0);
//        Problem p;
//        // Gets a specific problem (used for debugging)
//        if ((p = getSpecificProblem(e)) != null)
//            return p;
//
//        StudentState state = smgr.getStudentState();
//        StudentModel sm = smgr.getStudentModel();
//        classID = topicLoader.determineClass();
//        topicID = topicLoader.getCurTopic(conn,state,classID);
//        return selectProbHelperInForcedTopic(smgr, e, state, sm);
//
//    }
//
//
//
//
//
//
//    private Problem selectProbHelper(SessionManager smgr, NextProblemEvent e, StudentState state, StudentModel sm) throws Exception {
//        Problem p;
//        // e will be null if topic was just switched because of content failure (which means we don't need to run the test)
//        // if curTopic is not in the lessonplan move to next topic (this can happen if a topic is de-activated in lessonplan while student
//        // is in the topic)
//
//        if ( e != null && e.getTopicToForce() > 0) {  //The student has chosen a topic to work on
//             getSpecificTopicProblem(e.getTopicToForce(), state, e);
//        }
//
//        if (!topicLoader.isTopicInClassLessonPlan(state.getCurTopic()))
//            return getNextTopicProblem(state);
//
//        if (e != null && topicLoader.shouldSwitchTopics(state,e.getProbElapsedTime(),sm,
//                maxNumberProbs,maxTimeInTopic,topicMastery, minNumberProbs, minTimeInTopic)) {
//            if ( e.getTopicToForce() > 0 || (e.getMode() != null && (e.getMode().equals("challenge") || e.getMode().equals("review"))))
//                return null ;
//            return getNextTopicProblem(state);
//        }
//        // If the intro has not been shown, show it.
//
//        if (e != null && e.isForceIntro() && (e==null || e.getMode() == null || !e.getMode().equals("review"))) {
//            Problem intro = DbTopics.getIntro(conn,this.topicID);
//            state.setTopicIntroShown(true);
//            return intro;
//        }
//
//        // If we should return a topic intro,  find it and we'll return it.
//        if ((p = getIntro(state, e)) != null)
//            return p;
//
//
//        prepareTopic(smgr);  // does stuff like eliminating problems that have been shown
//        // if we are at the beginning of a topic, this will choose the next problem
//        p = chooseProblemAtBeginningOfTopic(smgr,state);
//        if (p != null)  {
//            logger.debug("Index=" + this.newIndex + " Selecting problem " + p.getId() + " diff=" + ((Problem) p).getDifficulty());
//            return p;
//        }
//        // Its within a topic, so select next problem based on performance on last problem in this topic
//        else if (lastProb != null) {
//            p= selectProblemBasedOnPerformance(conn,lastProb,state);
//            if (p != null)   {
//                state.setTopicNumProbsSeen(state.getTopicNumProbsSeen() + 1);
//            }
//        }
////        NOT SURE WHY THIS IS HERE
//        else if (lastProb == null)
//            logger.debug("LAST PROB is NULL");
////            return getNextTopicProblem(state);
//        if (p != null)
//            logger.debug("Index=" + this.newIndex + " Selecting problem " + p.getId() + " diff=" + ((Problem) p).getDifficulty());
//        return p;
//    }
//
//    /**
//     * A TopicIntro (a type of Problem) may be returned if this problem selector is configured to use them.   If it is,
//     * the TopicIntro is selected as the first "problem" in the topic and will be returned.   Re-entry into the topic
//     * should not cause the intro to be returned again.
//     * @param state
//     * @param e
//     * @return
//     * @throws SQLException
//     */
//    private Problem getIntro(StudentState state, NextProblemEvent e) throws SQLException {
//        if ( !state.isTopicIntroShown() && (e==null || e.getMode() == null || !e.getMode().equals("review"))) {
//            //if we are in the CONTINUE button (forced topic) and the intro belongs to another topic (due to no more probs available in the forced topic)
//            if ( myForcedTopicEvent !=null && myForcedTopicEvent.getTopicToForce() > 0 && this.topicID != myForcedTopicEvent.getTopicToForce())
//                return null ;     //return noMoreProblems=true
//
//            Problem intro = DbTopics.getIntro(conn,this.topicID);
//            state.setTopicIntroShown(true);
//            logger.debug("Selecting intro " + intro.getId() );
//            return intro;
//        }
//        else return null;
//    }
//
//
//    private Problem selectProbHelperInForcedTopic(SessionManager smgr, NextProblemEvent e, StudentState state, StudentModel sm) throws Exception {
//        Problem p;
//        // e will be null if topic was just switched because of content failure (which means we don't need to run the test)
//        // if curTopic is not in the lessonplan move to next topic (this can happen if a topic is de-activated in lessonplan while student
//        // is in the topic)
//
////        if ( e != null ) {
////             return getSpecificTopicProblem(e.getTopic(), state, null);
////        }
//
//        if (!topicLoader.isTopicInClassLessonPlan(state.getCurTopic()))
//            return getNextTopicProblem(state);
//
//        if (e != null && topicLoader.shouldSwitchTopics(state,e.getProbElapsedTime(),sm,
//                maxNumberProbs,maxTimeInTopic,topicMastery, minNumberProbs, minTimeInTopic)) {
//            return null ;
//        }
//        // If the intro has not been shown, show it.
//        if (e != null && e.isForceIntro()) {
//            Problem intro = DbTopics.getIntro(conn,this.topicID);
//            state.setTopicIntroShown(true);
//            return intro;
//        }
//        if ( !state.isTopicIntroShown()) {
//            Problem intro = DbTopics.getIntro(conn,this.topicID);
//            state.setTopicIntroShown(true);
//            logger.debug("Selecting intro " + intro.getId() );
//            return intro;
//        }
//        prepareTopic(smgr);  // does stuff like eliminating problems that have been shown
//        // if we are at the beginning of a topic, this will choose the next problem
//        p = chooseProblemAtBeginningOfTopic(smgr,state);
//        if (p != null)  {
//            logger.debug("Index=" + this.newIndex + " Selecting problem " + p.getId() + " diff=" + ((Problem) p).getDifficulty());
//            return p;
//        }
//        // Its within a topic, so select next problem based on performance on last problem in this topic
//        else if (lastProb != null) {
//            p= selectProblemBasedOnPerformance(conn,lastProb,state);
//            if (p != null)   {
//                state.setTopicNumProbsSeen(state.getTopicNumProbsSeen() + 1);
//            }
//        }
//        return p;
//    }
//
//    /**
//     * When the user is prompted to select the topic, this will process the topic selected and will select a problem in that topic
//     * There are three special topics (0,-1,-2) which mean noPreference, topicWithBestPerformance, topicWithWorstPerformance
//     * TODO process the three special topics and consider one called sameTopic
//     * @param smgr
//     * @param topicID
//     * @param e
//     * @return
//     * @throws Exception
//     * @throws SQLException
//     */
//    private Problem selectProblemInTopic(SessionManager smgr, String topicID, NextProblemEvent e) throws Exception, SQLException {
//        logger.debug("selecting Problem");
//        Problem p;
//        // Gets a specific problem (used for debugging)
//        if ((p = getSpecificProblem(e)) != null)
//            return p;
//        prepareTopic(smgr);
//        StudentState state = smgr.getStudentState();
//        StudentModel sm = smgr.getStudentModel();
//        resetStateForNextTopic(state); // assumes that when this is called its switching topics and this the first problem in the topic
//        classID = topicLoader.determineClass();
//        this.topicID = Integer.parseInt(topicID);
//        state.setCurTopic( this.topicID);
//        return selectProbHelper(smgr,e,state,sm);
//    }
//
//    public Problem selectProblem(SessionManager smgr, SelectProblemSpecs selectionCriteria, NextProblemEvent e) throws Exception {
//        setTimeInTopic(smgr.getStudentState(),e.getProbElapsedTime()); // sets an instance variable
//        classID = topicLoader.determineClass();      // DM 2/9/11 fixed bug where this wasn't finding the default class and failed to move to next topic.
//        topicID = topicLoader.getCurTopic(conn,smgr.getStudentState(),classID);
//        Problem p;
//        if (selectionCriteria.getType() == SelectProblemSpecs.specType.DIFFICULTY_SELECTION) {
//            if (selectionCriteria.getName().equals(SelectProblemSpecs.EASIER_PROBLEM))
//                p= selectProblemLessDifficulty(smgr,e);
//            else if (selectionCriteria.getName().equals(SelectProblemSpecs.HARDER_PROBLEM))
//                p= selectProblemMoreDifficulty(smgr,e);
//            else  p= selectProblemSameDifficulty(smgr,e);
//            p.setInTopicId(topicLoader.getTopicId());
//            return p;
//        } else if (selectionCriteria.getType() == SelectProblemSpecs.specType.TOPIC_SELECTION) {
//            p= selectProblemInTopic(smgr,selectionCriteria.getName(),e);
//            p.setInTopicId(topicLoader.getTopicId());
//            return p;
//        }
//
//        else {
//            throw new UserException("SelectProblemSpecs has an unknown type " + selectionCriteria.getType());
//        }
//    }
//
//
//    private void resetStateForNextTopic(StudentState state) throws SQLException {
//        state.setTopicIntroShown(false); // at chunk switch time set flag to false so that an intro will be shown
//        // in the next chunk.
//        state.setTimeInTopic(0);
//        state.setTopicNumProbsSeen(0);
//        state.setContentFailureCounter(0);
//    }
//
//
//
//
//
//
//    /**
//     *
//     * @param conn
//     * @param probId
//     * @return an array of [avgHints, avgIncorrect, avgSolveTime(Secs)]
//     * @throws Exception
//     */
//    private float[] getExpectedBehavior(Connection conn, int probId) throws Exception {
//
//        String s = "select avghints,avgincorrect,avgsecsprob from OverallProbDifficulty where problemid=?" ;
//        PreparedStatement ps = conn.prepareStatement(s);
//        ps.setInt(1,probId);
//        ResultSet rs = ps.executeQuery();
//        if (rs.next()) {
//            float[] result = new float[3];
//            result[0] = rs.getFloat("avghints") ;
//            result[1] = rs.getFloat("avgincorrect") ;
//            result[2] = rs.getFloat("avgsecsprob") ;
//            return result ;
//          }
//        else return null;
//    }
//
//
//
//
//    private Problem selectProblemBasedOnPerformance(Connection conn, Problem lastProb, StudentState state) throws Exception {
//        double time = state.getTimeToSolve() / 1000.0;  // convert from ms to seconds
//        int mistakes = state.getNumMistakesOnCurProblem();
//        int hints = state.getNumHintsBeforeCorrect();
//        boolean isCorrect = state.isProblemSolved();
//
//        if (lastProb == null)
//            System.out.println("error");
//        // TODO Bug:  When there are no more unsolved probs in a topic (list is empty), the lastProb is null and then this dies.
//        // Note
//        float[] expected = getExpectedBehavior(conn,lastProb.getId()) ;
//        if (expected  != null) {
//            float expMistakes = expected[1] ;
//            float expHints = expected[0];
//            float expTime = expected[2];
////            logger.debug("Expected mistakes:"+expMistakes+" hints:"+expHints + " time:"+expTime);
////            logger.debug("Actual mistakes:"+mistakes+" hints:"+hints + " time:"+time + "isCorrect=" + isCorrect);
//
//            if ( !isCorrect ) {
////                logger.debug("Problem was skipped. Reduce the difficulty level") ;
//                state.addProblemSelectionCriterion(EASIER_PROB);
//                return getEasierProblem(); // get less difficult prob
//            }
//
//            if ( mistakes > expMistakes ) {
//                if ( hints > expHints ) {
//                    if ( time > expTime )
//                        ; //logger.debug("Problem seems hard even though student is putting effort and seeing help (m>E_m, h>E_h, t>E_t)" ) ;
//                    else
//                       ; // logger.debug("Student is seeing hints carelessly, and  making mistakes carelessly (m>E_m, h>E_h, t<E_t)");                   ;
//
//                    logger.debug ( "Decreasing difficulty level... ") ;
//                    state.addProblemSelectionCriterion(EASIER_PROB);
//                    return  getEasierProblem(); // get less difficult prob
//
//                }
//                else {
//                    if ( time > expTime )
//                        ; //logger.debug ( "Student is confused, making mistakes and not seeing help (m>E_m, h<E_h, t>E_t)") ;
//                    else
//                        ; //logger.debug("Rushing through problems without seeing help (m>E_m, h<E_h, t<E_t)") ;
//
//                    logger.debug ( "Decreasing difficulty level... ") ;
//                    state.addProblemSelectionCriterion(EASIER_PROB);
//                    return getEasierProblem(); // get less difficult prob
//
//                }
//            }
//            else { // mistakes are LOW
//                if ( hints > expHints ) {
//                    if ( time > expTime ) {
////                        logger.debug("Student is carefully seeing help and spending time, maintain level of difficulty (m<E_m, h>E_h, t>E_t)") ;
//                        logger.debug ( "Maintaining same difficulty level... ") ;
//                         state.addProblemSelectionCriterion(SAMEDIFF_PROB);
//                        return getSimilarDifficultyProblem();
//
//                    }
//                    else {
////                        logger.debug("Rushing through hints to get to the correct answer, decrease level of difficulty (m<E_m, h>E_h, t<E_t)") ;
//                        logger.debug ( "Decreasing difficulty level... ") ;
//                         state.addProblemSelectionCriterion(EASIER_PROB);
//                        return getEasierProblem(); // get less difficult prob
//
//                    }
//                }
//                else {
//                    if ( time > expTime ) {
////                        logger.debug("Correctly working on problem with effort but without help (m<E_m, h<E_h, t>E_t)") ;
////                        logger.debug("50% of the time increase level of difficulty. 50% of the time, maintain level of difficulty") ;
//                        if ( Math.random() > 0.5 ) {
//                            logger.debug ( "Increasing difficulty level... ") ;
//                             state.addProblemSelectionCriterion(HARDER_PROB);
//                            return getHarderProblem();
//                        }
//
//                        else {
//                            logger.debug ( "Maintaining same difficulty level... ") ;
//                            state.addProblemSelectionCriterion(SAMEDIFF_PROB);
//                            return getSimilarDifficultyProblem();
//                        }
//                    }
//                    else
//                       ; // logger.debug("Correctly working the problem with no effort and few hints (m<E_m, h<E_h, t<E_t)") ;
//                    logger.debug("Increasing the difficulty level...") ;
//                    state.addProblemSelectionCriterion(HARDER_PROB);
//                    return getHarderProblem();
//                }
//            }
//
//        }
//        return null;
//    }
//
//
//
//    private long setTimeInTopic (StudentState state, long probElapsedTime) {
//        this.timeInTopic = state.getTimeInTopic() + probElapsedTime;
//        return this.timeInTopic;
//    }
//
//    public String getReasonForNullProblem(StudentState state) {
////        if ( this.probsInTopic.size() == topicLoader.getProblemsSeenInTopic(conn, this.topicID, state).size() )
////            return "AllProblemsDone" ;
//
//        return topicLoader.reasonForNullProblem ;
//    }
//
//
//
//    /**
//     * For every problem but the first one in a topic,  it will be 'practice'.   The classic system also needs the mode
//     * to be 4mality or external if the problem is one of those.   When the problem is the first one in the topic, it
//     * may be returned in EXAMPLE (classic system) / DEMO (hybrid system) mode.
//     * @param p
//     * @return
//     * @throws Exception
//     */
//    public String selectProblemMode(Problem p, HintSelector hintSelector) throws Exception {
//        String mode = "practice";
//        StudentState state = smgr.getStudentState();
////        if (random.nextInt(7) == 0 && isProblemPlayableAsExample(p))
////            mode = "example";
//
//        if ( p!= null && p.getForm() != null && p.getForm().equals(Problem.FORMALITY_PROB_TYPE) )
//            mode = "4Mality" ;
//        else if ( p!= null && p.getForm() != null && p.getForm().equals(Problem.EXTERNAL_PROB_TYPE) )
//            mode = "external" ;
//        else if (!this.showTopicIntroFirst && this.showExampleProblemFirst && state.getTopicNumProbsSeen() < 1)
//            mode = setDemoMode(p, hintSelector);
//
//            // TODO This is dependent on TopicIntro being seen first.   Some pedagogies might like no TopicIntro but examples first.
//            // Need to make this condition know about that config.
//        else if (this.showExampleProblemFirst && state.isTopicIntroShown() && state.getTopicNumProbsSeen() == 1)
//            mode = setDemoMode(p, hintSelector);
//
//        return mode;
//    }
//
//    private String setDemoMode(Problem p, HintSelector hintSelector) throws Exception {
//        String mode;
//        mode = Settings.useHybridTutor ? Problem.DEMO : Problem.EXAMPLE;
//        hintSelector.setServletInfo(smgr);
//        List<Hint> soln = hintSelector.selectFullHintPath(smgr,p.getId());
//        p.setSolution(soln);
//        return mode;
//    }
//
//
//
//}
