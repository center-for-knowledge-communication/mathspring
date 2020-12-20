package edu.umass.ckc.wo.db;

import edu.umass.ckc.wo.content.Video;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by marshall on 6/6/17.
 * 
 * Frank 12-20-2020 issue #333 - handle multi-lingual video selection
 */
public class DbVideo {

    public static Video getVideo (Connection conn, int id, String prob_language) throws SQLException {
         ResultSet rs = null;
          PreparedStatement ps = null;
          try {
              String q = "select link from video where id=? and language=?";
              ps = conn.prepareStatement(q);
              ps.setInt(1, id);
              ps.setString(2, prob_language);
              rs = ps.executeQuery();
              if (rs.next()) {
                  String url = rs.getString(1);
                  return new Video(url);
              }
              else return null;
          } finally {
                if (ps != null)
                     ps.close();
                if (rs != null)
                     rs.close();
          }
    }
}
