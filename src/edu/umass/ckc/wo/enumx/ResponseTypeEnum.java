package edu.umass.ckc.wo.enumx;

/**
 * Two possible hint types: LABEL (a part of the Flash representation of the problem),
 *  RESOURCE: (the hint is a separate Flash file)
 *  */

public class ResponseTypeEnum extends StringEnum{
        /**
        The public instances.
        */
        public static final ResponseTypeEnum HINT = new ResponseTypeEnum("hint");
        public static final ResponseTypeEnum ALL_HINTS = new ResponseTypeEnum("allhints");
        public static final ResponseTypeEnum REINFORCEMENT = new ResponseTypeEnum("reinforcement");
        public static final ResponseTypeEnum PROBLEM = new ResponseTypeEnum("problem");
        public static final ResponseTypeEnum GRADE_PROBLEM = new ResponseTypeEnum("gradeProblem");
        public static final ResponseTypeEnum LOGOUT = new ResponseTypeEnum("logout");
        public static final ResponseTypeEnum INTERVENTION = new ResponseTypeEnum("intervention");
        public static final ResponseTypeEnum CONFIG = new ResponseTypeEnum("config");


    /**
        Private constructor.
        */
        private ResponseTypeEnum(String name) {
                super(name);
        }




}