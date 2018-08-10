package edu.umass.ckc.wo.admin;

import edu.umass.ckc.wo.db.DbClass;
import edu.umass.ckc.wo.db.DbStrategy;
import edu.umass.ckc.wo.db.DbUser;
import edu.umass.ckc.wo.smgr.User;
import edu.umass.ckc.wo.strat.TutorStrategy;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Sep 19, 2017
 * Time: 12:00:01 PM      Will this save
 */
public class StrategyAssigner {

    /**
     * Set the student's strategy id in the student table.  Look at the other students in the class and try to keep the strategies balanced.
     * @param conn
     * @param studId
     * @param classId
     * @return the TutorStrategy that is assigned to the student
     */
    public static TutorStrategy assignStrategy (Connection conn, int studId, int classId) throws SQLException {
        List<TutorStrategy> strats = DbStrategy.getStrategies(conn,classId);
        if (strats.size() == 0)
            return null;
        List<User> students = DbClass.getClassStudents(conn,classId);
        // Create a map that has a count of how many times each strategy is used.  THe keys will be string ids of the strategies.
        Map<String,Integer> m = new Hashtable<String,Integer>();
        // load them all with counts of 0
        for (TutorStrategy s: strats)
            m.put(s.getId(),0);
        // Go through all the students and tally up the ones who've been assigned a strategy
        for (User s : students) {
            int strategyId = s.getStrategyId();
            if (strategyId >= 0) {
                // A student could be mapped to some old bogus strategy Id that is no longer connected to the class.
                Integer count = m.get(Integer.toString(strategyId)); // will be null if the students strategy isn't among the class ones.
                if (count != null)
                    m.put(Integer.toString(strategyId), count + 1);
            }
        }
        // Find the strategy with the lowest count and return it
        int min = 30000;
        String minStrat = strats.get(0).getId();

        for (Map.Entry<String,Integer> e : m.entrySet()) {
            if (e.getValue() < min) {
                min = e.getValue();
                minStrat = e.getKey();
            }
        }
        int stratId = Integer.parseInt(minStrat);
        DbUser.setStrategy(conn,studId,stratId); // write it to the student table
        // return the selected strategy object.
        for (TutorStrategy s: strats)
            if (s.getId().equals(minStrat))
                return s;
        return null;
    }


}
