package edu.umass.ckc.wo.db;

import edu.umass.ckc.wo.content.Video;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by marshall on 6/6/17.
 */
public class DbVideo {

    public static Video getVideo (Connection conn, int id) throws SQLException {
         ResultSet rs = null;
          PreparedStatement ps = null;
          try {
              String q = "select link from video where id=?";
              ps = conn.prepareStatement(q);
              ps.setInt(1, id);
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
