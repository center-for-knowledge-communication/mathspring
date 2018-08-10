package edu.umass.ckc.wo.tutor;


// This class has been commented out and its superclass eliminated.   This is so that we can eliminate classes
// and methods that this class depended on.

//public class WoPedagogicalModel extends OldPedagogicalModel {
public class WoPedagogicalModel {
//    private static Logger logger =   Logger.getLogger(WoPedagogicalModel.class);
//
//    private SessionManager smgr_;
////    private ProblemSelector advProblemSelector;
//
//    /**
//     * WoPedagogicalModel is given the SessionManager because that object has access to the
//     * student's session info including the student model and the current problem the
//     * student is working on.  Also is given a ProblemSelector because different selection strategies
//     * may be employeed we treat it as a plug-in.
//     *
//     * @param smgr                                         select
//     * @param probSel
//     * @param e
//     * @throws Exception
//     */
//    public WoPedagogicalModel(SessionManager smgr, ProblemSelector probSel, HintSelector hintSel,
//                              InterventionSelector intervSel, LearningCompanion lc, StudentActivityEvent e) throws Exception {
//        super(smgr.getStudentModel(), probSel, hintSel, intervSel, lc);
//        smgr_ = smgr;
//        problemSelector.setServletInfo(smgr);
//        hintSelector.setServletInfo(smgr);
//        if (intervSel != null)
//            intervSel.setServletInfo(smgr_);
//    }
//
////    public WoPedagogicalModel(SessionManager smgr, ProblemSelector probSel, ProblemSelector advProbSelector,
////                              HintSelector hintSel) throws Exception {
////      super(smgr.getStudentModel(),probSel,hintSel);
////      this.advProblemSelector = advProbSelector;
////      smgr_ = smgr;
////      probSel_.load(smgr_.getConnection());
////      this.advProblemSelector.load(smgr_.getConnection());
////      hintSel_.load(smgr_.getConnection());
////    }
//
//    public Problem getProblem(ActionEvent e) throws Exception {
//        Problem p = problemSelector.selectProblem(smgr_, null, (NextProblemEvent) null);    // DAM 2/09 eliminated event being passed
//        // no longer saving cur prob in smgr
//        return p;
//    }
//
//    private boolean isRealProblem (int id) {
//        // interventions have id < 0 and intros have id of 999
//        return id > 0 && id != 999;
//    }
//
//    public String endActivity(EndActivityEvent e) throws Exception {
//
//        // hack :  If the last activity was an intervention we don't want to call endProblem on student model
//        // because it will enter a bunch of data that will throw off the next problem selection.
//        // Some data probably should be pulled out of the intervention and saved but I don't know what
//        // TODO intervention data that is relevant must be stuffed into student model
//        if (isRealProblem(e.getProblemNumber()))
//            this.studentModel.endProblem(smgr_.getStudentState());
//        this.studentModel.save();
//        //todo: here is where to log interventions (access to both state and student model
//        // when user is allowed to force exit from the pre/post hut, isQuit is true.  At that
//        // point we set the property for the test as completed.
//        if (e.isQuit()) {
//            smgr_.getStudentState().setSatHutCompleted(true);
//        }
//        return Problem.SAT_PROBLEM;
//
//    }
//
//
//    /**
//     * Activities are selected for the student given knowledge of the student's current state.
//     * This returns an XML <response> document.
//     */
//    public Activity getActivity(StudentActionEvent e) throws Exception {
//        Response r = null;
//
//        // At the time of nextProblem the probElapsed is sent in as 0 (approx) but calculations need to get made at
//        // the time of nextProblem that depend on probElapsed being set to the time spent in the just-completed problem
//        // so we  update the probElapsedTime after all the calculations have been made.  In all other actions we want to
//        // know the current value of probElapsed so we set it before calculations are made
//
//        //todo: quick fox for student feedback
//        if (e.getStudentInput() != StudentInputEnum.NEXT_PROBLEM || e.getStudentInput() != StudentInputEnum.NEXT_PROB_WITH_INFO)
//            smgr_.getStudentState().setProbElapsedTime(e.getProbElapsed());// and the probElapsed time
//
//        if (e.getStudentInput() == StudentInputEnum.NEXT_PROB_WITH_INFO){
//            String[] emotiondata = e.getUserInput().split(",") ;
//            int emotionvalue = new Integer(emotiondata[2]).intValue() ;
//            ((AffectStudentModel) studentModel).setLastReportedEmotion(emotiondata[1],emotionvalue);
//        }
//
//        /** Student wants a hint or the next step in a currently playing hint */
//        if (!e.isAttempt() && (e.getStudentInput() == StudentInputEnum.HELP ||
//                e.getStudentInput() == StudentInputEnum.FORCE_HELP ||
//                e.getStudentInput().equals(StudentInputEnum.HELP_ACCEPTED) )) {
//            r = hintResponse(e);  // will alter smgr's current hint for transaction logging
//        }
//
//        else if (!e.isAttempt() && e.getStudentInput() == StudentInputEnum.HELP_REJECTED ) {
//            r = helpRejectedResponse(e) ;
//        }
//
//        /** Student wants a hint or the next step in a currently playing hint */
//        else if (!e.isAttempt() && e.getStudentInput() == StudentInputEnum.ALL_HINTS) {
//            r = remainingHintsResponse(e);
//        }
//
//        /** Students wants to get out of this problem and get another problem.
//         *  This can happen because: a) reinforcement is done, b) skipped the
//         *  problem (from within any of the tutoring states) */
//        else if (!e.isAttempt() && (
//                    e.getStudentInput() == StudentInputEnum.NEXT_PROBLEM ||
//                    e.getStudentInput() == StudentInputEnum.NEXT_PROB_WITH_INFO ||
//                    e.getStudentInput() == StudentInputEnum.EASIER_NEXT_PROBLEM ||
//                    e.getStudentInput() == StudentInputEnum.SAME_NEXT_PROBLEM ||
//                    e.getStudentInput() == StudentInputEnum.HARDER_NEXT_PROBLEM ))
//            {
//
//
//           r = newProblemOrInterventionResponse(e);  // will update the problemId inside the smgr for transaction logging
//        }
//
//        /** Incorrect response to problem.  In the future we may select a misconception
//         *  hint if one exists for this particular wrong answer. For now, we will
//         *  put the student in the INCORRECT_ANSWER state where they simply see their
//         *  graded response and are encouraged to ask for a hint.
//         */
//        // dm 12/08  grade the attempt here by comparing user input to problem's answer.  Then
//        // direct the client to mark the problem as wrong
//        else if (e.isAttempt() &&
//                !isCorrect(e.getUserInput(), smgr_.getStudentState().getCurProblem(),smgr_.getConnection())
//                ) {
//            // update the student model based on the student attempt
//            studentModel.studentAttempt(smgr_.getStudentState(), e.getUserInput(), e.isCorrect());
//            // r = hintResponse(e); // no misconception hints for now
//            r = incorrectAnswerResponse(e);
//        }
//
//        /** Student answers correctly.  Select a reinforcement.  If no reinforcement, mark the problem as correct */
//
//        // dm 12/08  grade the attempt here by comparing user input to problem's answer.  Then
//        // direct the client to mark the problem as correct
//        else if (e.isAttempt()&&
//                isCorrect(e.getUserInput(), smgr_.getStudentState().getCurProblem(),smgr_.getConnection())) {
//
//            // update the student model based on the student attempt
//            studentModel.studentAttempt(smgr_.getStudentState(), e.getUserInput(), e.isCorrect());
//            r = interventionResponse(e);
//            // provides some kind of random message when student gets it right.   DM 12/08 - made it
//            // direct the client to mark the attempt in addition to passing the reinforcement crap
//            if (r == null) {
//                r = reinforcementResponse(e);
//            }
//        }
//        // write the student model back to the database
//        this.studentModel.save();
//        return r;
//    }
//
//    private boolean isCorrect(String userInput, int curProblem, Connection connection) throws SQLException {
//        String ans = new DbProblem().getProblemAnswer(connection,curProblem);
//        return ans.equalsIgnoreCase(userInput);
//    }
//
//
//    /**
//     * This is called when the student answers incorrectly.  The PedagogicalModel can choose to return an Intervention
//     * (e.g. "UseTheHelpButtonYouDummy" motivator )  or can just return acknowledgement.
//     *
//     * @param e
//     * @return
//     * @throws Exception
//     */
//    private Response incorrectAnswerResponse(StudentActionEvent e) throws Exception {
//        // if the interventionSelector chooses to return an Intervention, return it + an emotion
//        if (this.interventionSelector != null) {
//            // todo Ivon needs to make the intervention selector deal with a StudentActionEvent that is "attempt"
//            Intervention interv = this.interventionSelector.selectIntervention(e);
//            if (interv != null) {
//                studentModel.interventionGiven(smgr_.getStudentState(), interv);
//                return new InterventionResponse(interv);
//            }
//        }
//        // gradeProblem=true isCorrect=false
//        return new AttemptResponse(true,false);
//    }
//
////Returns a response with all the hints to play one after the other. Sets the current hint to the last hint
////Logs each row for each hint, as if they were seen at the same time
//    private Response remainingHintsResponse(StudentActionEvent e) throws Exception {
//
//        Hint h = hintSelector.selectHint(smgr_, e);
//
//        List<Hint> hints = new ArrayList<Hint>();
//        Hint previous = null;
//        while (previous == null || h.getId() != previous.getId()) {
//            this.studentModel.hintGiven(smgr_.getStudentState(), h);
//            hints.add(h);
//            TransactionLogger.logEvent(e, smgr_, null);
//            previous = h;
//            h = hintSelector.selectHint(smgr_, e);
//        }
//
//        Response r = new AllHintsResponse( hints);
//
//        return r;
//    }
//
//    private Response hintResponse(StudentActionEvent e) throws Exception {
//
//        Hint h = hintSelector.selectHint(smgr_, e);
//        // update the student model based on the hint given
//        this.studentModel.hintGiven(smgr_.getStudentState(), h);
//        return new HintResponse( h);
//    }
//
//    private Response helpRejectedResponse(StudentActionEvent e) {
//      return new Response() ;
//    }
//
//
//    //todo: find better place for this variable
//    public static int SAT_MINIMUM_NUM_SOLVED = 30;
//    /**
//     * At this point the student is asking for nextProblem.  The PedagogicalModel may elect to choose
//     * a Problem and return it OR it may choose to return some Intervention (e.g. a BarGraphMotivator).
//     *
//     * Some odd logic here that should be restrutcured.  Basically, we are avoiding getting an
//     * intervention when the client sends a specialized message (e.g. EASIER_PROBLEM) by not calling
//     * interventionResponse when receiving those messages
//     *
//     * @param e
//     * @return
//     * @throws Exception
//     */
//    private Response newProblemOrInterventionResponse(StudentActionEvent e) throws Exception {
//        Problem p;
//        Response r;
//        String finish="" ;
//
//
//        //and we are getting specialized input, choose a special problem, and don't select interventions
//        if (e.getStudentInput() == StudentInputEnum.EASIER_NEXT_PROBLEM){
//            p = problemSelector.selectProblemLessDifficulty(smgr_);
//        }
//        else if (e.getStudentInput() == StudentInputEnum.SAME_NEXT_PROBLEM){
//            p = problemSelector.selectProblemSameDifficulty(smgr_);
//        }
//        else if (e.getStudentInput() == StudentInputEnum.HARDER_NEXT_PROBLEM){
//            p = problemSelector.selectProblemMoreDifficulty(smgr_);
//        }
//        else {
//            //else select intervention or a problem if no intervention
//            r = interventionResponse(e);
//            if (r != null){
//                return r;
//            }
//            p = problemSelector.selectProblem(smgr_, this.studentModel, (NextProblemEvent) null);
//        }
//
//
//         // update the student model based on the problem selected
//        this.studentModel.newProblem(smgr_.getStudentState(), p);
//
//        if (p == null) {
//            System.out.println("DEBUG: \t\tIn WoPedigogicalModel.newProblemOrInterventionResponse: No more problems to give");
//            smgr_.getStudentState().setSatHutCompleted(true);
//            return new LogoutResponse( );
//        }
//        if (this.studentModel != null){
//            if (this.studentModel instanceof AffectStudentModel){
////              System.out.println("DEBUG:\t\t In WoPedigogicalModel.NewProblemOrInt..: numSolved:" + ((AffectStudentModel)this.studentModel).numProbsSolved);
//                if (((AffectStudentModel)this.studentModel).numProbsSolved > SAT_MINIMUM_NUM_SOLVED){
//                    finish = "&finish=true" ;
//                    //return new Response( p, "finish=true");
//                }
//            }
//        }
//
//        String emotion = "null" ;
//        if ( p!= null && learningCompanion != null &&
//             learningCompanion instanceof EmotionalLC && p.isProblemToSolve() ) {
//            emotion = ((EmotionalLC) learningCompanion).getEmotionToDisplay((AffectStudentModel) studentModel) ;
//
//        }
//
//        if (p.hasStrategicHint()) {
//            if ( ! emotion.equals("null") )
//                emotion = new String(emotion + ", idea") ;
//            else
//                emotion = "idea" ;
//        }
//        return new ProblemResponse( p, "emotion=" + emotion + finish ) ;
//
//    }
//
//    private Response interventionResponse(StudentActionEvent e) throws Exception{
//        if (interventionSelector != null) {
//            Intervention interv = interventionSelector.selectIntervention(e);
//            //this call should be within the 'if' below if we only want to log when intervention is given
//            TransactionLogger.logInterventionEngagement(e, interv, smgr_, this.studentModel);
//            if (interv != null) {
//                // update the student model based on the intervention selected
//                this.studentModel.interventionGiven(smgr_.getStudentState(), interv);
//                return new InterventionResponse( interv);
//            }
//        }
//        return null;
//    }
//
//    private Response reinforcementResponse(StudentActionEvent e) throws Exception {
//
//        Reinforcement r = new ReinforcementSelector(smgr_.getConnection()).selectReinforcement(e, e.getProblemNumber());
//        return new ReinforcementResponse( (Reinforcement) r);
//    }

}
