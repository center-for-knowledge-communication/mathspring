package edu.umass.ckc.wo.db;

import edu.umass.ckc.wo.event.AdventurePSolvedEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: 6/12/12
 * Time: 4:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class DbAdventureLogger {


       public static void logAdventureProblemSolved(Connection conn, AdventurePSolvedEvent e, long elapsedTime, String userInput,
                                           int probId, int studId, int sessNum, String advName) throws Exception {
        String s = "insert into EpisodicAdventureData " +
                "(studId,sessnum,userInput,elapsedTime,advprobid,advname) values (?,?,?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(s);
        ps.setInt(1, studId);
        ps.setInt(2, sessNum);
        ps.setString(3, userInput);
        ps.setLong(4, elapsedTime);
        ps.setInt(5, probId);
        ps.setString(6,advName);
        ps.execute();
    }
}
