package edu.umass.ckc.wo.tutor;

import edu.umass.ckc.wo.content.PrePostProblem;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.event.EndActivityEvent;
import edu.umass.ckc.wo.event.StudentActionEvent;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.tutormeta.Activity;
import edu.umass.ckc.wo.tutormeta.OldPedagogicalModel;
import edu.umass.ckc.wo.tutormeta.PrePostProblemSelector;
import edu.umass.ckc.wo.tutormeta.StudentModel;

import java.util.List;


/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Aug 16, 2005
 * Time: 6:55:54 PM
 */
public class PrePostPedagogicalModel extends OldPedagogicalModel {
    private SessionManager smgr;
    private StudentModel studentModel;
    private PrePostProblemSelector problemSelector;

    // this uses the PrePostProblemSelector

    public PrePostPedagogicalModel(SessionManager smgr, PrePostProblemSelector preposttestProblemSelector) throws Exception {
        this.problemSelector = preposttestProblemSelector;
        this.problemSelector.init(smgr);
        this.studentModel = smgr.getStudentModel();
        this.smgr = smgr;
//        problemSelector.setServletInfo(smgr);
    }

    public String endActivity(EndActivityEvent e) throws Exception {
        return null;
    }

   /**
    *
    * @param e
    * @return  the type (pre or post) of problem that was just completed
    * @throws Exception
    */
    public String endActivityX(EndActivityEvent e) throws Exception {
        return null;

//        BaseStudentModelOld sm =(BaseStudentModelOld) smgr.getStudentModel(); // dm 6/24/09  was cast to AffectStudentModel
//        List preTestProbsGiven = smgr.getStudentState().getPretestProblemsGiven();
//        List postTestProbsGiven = smgr.getStudentState().getPosttestProblemsGiven();
//        boolean pretestComplete = smgr.getStudentState().getPretestCompleted();
//        boolean posttestComplete = smgr.getStudentState().getPosttestCompleted();
//        boolean correct = e.isCorrect();
//        boolean solved = e.isSolved(); // really means attempted
//        if (e.getUserInput() != null && !e.getUserInput().trim().equals("")) {
//            if (preTestProbsGiven.size() >= 0 && !pretestComplete) {
//                // We record the problem as "given" once we get a reply to it.
//                smgr.getStudentState().addPretestProblem(e.getProbId());
//                if (correct)
//                    sm.incrementPreTestNumCorrect();  // writes studmodel property to db
//                else if (!correct && solved)
//                    sm.incrementPreTestNumIncorrect();  // writes studmodel property to db
//            }
//            else if (postTestProbsGiven.size() >= 0 && !posttestComplete) {
//                // We record the problem as "given" once we get a reply to it.
//                smgr.getStudentState().addPosttestProblem(e.getProbId());
//                if (correct)
//                    sm.incrementPostTestNumCorrect();
//                else if (!correct && solved)
//                    sm.incrementPostTestNumIncorrect();
//            }
//        }
//        // when user is allowed to force exit from the pre/post hut, isQuit is true.  At that
//        // point we set the property for the test as completed.
//        if (e.isQuit()) {
//            if (!pretestComplete) {
//                ((PrePostProblemSelector) this.problemSelector).pretestComplete();
//            }
//            else if (!posttestComplete) {
//                ((PrePostProblemSelector) this.problemSelector).posttestComplete();
//            }
//        }
//       if (!pretestComplete)
//           return PrePostProblem.PRE;
//       else if (!posttestComplete)
//           return PrePostProblem.POST;
//       else return PrePostProblem.DEFAULT;
    }

    public Activity getActivity(StudentActionEvent e) throws Exception {
        smgr.getStudentState().setProbElapsedTime(e.getProbElapsed()); // set the probElapsedTime for the logger
        Problem p = this.problemSelector.selectProblem(e,smgr, this.studentModel);
//        return new ProblemResponse(p, null, e.getElapsedTime()) ;
        return null;

    }
}