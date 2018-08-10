package edu.umass.ckc.wo.enumx;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * These are the various buttons that a student can select
 */

public class StudentInputEnum extends StringEnum {

        public static final String a = "A";
        public static final String b = "B";
        public static final String c = "C";
        public static final String d = "D";
        public static final String e = "E";
        public static final String help = "help"; // want a hint
        public static final String forceHelp = "forceHelp"; //
        public static final String allhints = "allhints"; // pressed the why button to see all hints
        public static final String nextProblem = "nextProblem"; // go to the next problem
        public static final String replay = "replay";   // replay current hint
        public static final String helpAccepted = "helpAccepted";   // Accept offer of help
        public static final String helpRejected = "helpRejected";   // Reject offer of help

        public static final String easierNextProblem = "easierproblem";
        public static final String harderNextProblem = "harderproblem";
        public static final String sameNextProblem = "sameproblem";
        public static final String quitTestHut = "quitTestHut";

        public static final String nextProbWithInfo = "nxtPrbInf";



        /**
        The public instances.
        */
        public static final StudentInputEnum A = new StudentInputEnum(a);
        public static final StudentInputEnum B = new StudentInputEnum(b);
        public static final StudentInputEnum C = new StudentInputEnum(c);
        public static final StudentInputEnum D = new StudentInputEnum(d);
        public static final StudentInputEnum E = new StudentInputEnum(e);
        public static final StudentInputEnum HELP = new StudentInputEnum(help);
        public static final StudentInputEnum FORCE_HELP = new StudentInputEnum(help);
        public static final StudentInputEnum ALL_HINTS = new StudentInputEnum(allhints);
        public static final StudentInputEnum NEXT_PROBLEM = new StudentInputEnum(nextProblem);
        public static final StudentInputEnum REPLAY = new StudentInputEnum(replay);
        public static final StudentInputEnum HELP_REJECTED = new StudentInputEnum(helpRejected);
        public static final StudentInputEnum HELP_ACCEPTED = new StudentInputEnum(helpAccepted);
        public static final StudentInputEnum EASIER_NEXT_PROBLEM = new StudentInputEnum(easierNextProblem);
        public static final StudentInputEnum HARDER_NEXT_PROBLEM = new StudentInputEnum(harderNextProblem);
        public static final StudentInputEnum SAME_NEXT_PROBLEM = new StudentInputEnum(sameNextProblem);
        public static final StudentInputEnum QUIT_TEST_HUT = new StudentInputEnum(quitTestHut);

        public static final StudentInputEnum NEXT_PROB_WITH_INFO = new StudentInputEnum(nextProbWithInfo);


        public static final List<StudentInputEnum> legalAnswers_ = new ArrayList<StudentInputEnum>();

        static {
            legalAnswers_.add(A);
            legalAnswers_.add(B);
            legalAnswers_.add(C);
            legalAnswers_.add(D);
            legalAnswers_.add(E);
        }


        /**
        Private constructor.
        */
        private StudentInputEnum (String name) {
          super(name);
        }

        public static StudentInputEnum getInstance (String val) {
            if (val.equalsIgnoreCase(a))
                return A;
            else if (val.equalsIgnoreCase(b))
                return B;
            else if (val.equalsIgnoreCase(c))
                return C;
            else if (val.equalsIgnoreCase(d))
                return D;
            else if (val.equalsIgnoreCase(e))
                return E;
            else if (val.equalsIgnoreCase(help))
                return HELP;
            else if (val.equalsIgnoreCase(forceHelp))
                return FORCE_HELP;
            else if (val.equalsIgnoreCase(allhints))
                return ALL_HINTS;
            else if (val.equalsIgnoreCase(nextProblem))
                return NEXT_PROBLEM;
            else if (val.equalsIgnoreCase(replay))
                return REPLAY;
            else if (val.equalsIgnoreCase(helpAccepted))
                return HELP_ACCEPTED;
            else if (val.equalsIgnoreCase(helpRejected))
                return HELP_REJECTED;

            else if (val.equalsIgnoreCase(easierNextProblem))
                return EASIER_NEXT_PROBLEM;
            else if (val.equalsIgnoreCase(harderNextProblem))
                return HARDER_NEXT_PROBLEM;
            else if (val.equalsIgnoreCase(sameNextProblem))
                return SAME_NEXT_PROBLEM;
            else if (val.equalsIgnoreCase(quitTestHut))
                return QUIT_TEST_HUT;

            //todo: quick fox for student feedback
            else if (val.startsWith(nextProbWithInfo))
                return NEXT_PROB_WITH_INFO;

            else return null;
        }
}
