package edu.umass.ckc.wo.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import edu.umass.ckc.wo.util.SqlQuery;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import edu.umass.ckc.wo.beans.Feedback;
import edu.umass.ckc.wo.login.PasswordAuthentication;;


/**

 */
public class DbGaze {


	public static String getStudentParams(Connection conn, int studId) throws SQLException {
		String result = "";
		String query = "select gaze_params_json from student where id = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
    	    ps = conn.prepareStatement(query);
		    ps.setInt(1,studId);
		    rs = ps.executeQuery();
		    if (rs.next()) { 
		    	result =  rs.getString(1);
		    }
        } finally {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
        }
        if ((result == null) || (result.length() == 0)) {
        	result = getGlobalParams(conn);
        }
        return result;

	}
		

	public static int getClassParams(Connection conn, int classId) throws SQLException {

		int result = 0;
		String query = "select gaze_detection_on from classconfig where classId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
    	    ps = conn.prepareStatement(query);
		    ps.setInt(1,classId);
		    rs = ps.executeQuery();
		    if (rs.next()) { 
		    	result =  rs.getInt(1);
		    }
        } finally {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
        }	    
        return result;
	}
	
	public static String getGlobalParams(Connection conn) throws SQLException {

		String result = null;
		String query = "select gaze_params_json from globalsettings where id_shouldBe1 = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
    	    ps = conn.prepareStatement(query);
		    ps.setString(1,"1");
		    rs = ps.executeQuery();
		    if (rs.next()) { 
		    	result =  rs.getString(1);
		    }
        } finally {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
        }	    return result;
	}

    public static void insertGazeWanderingEvent (Connection conn, int studentId, int sessionId, JSONObject inGazeDataJSONObject) throws SQLException {

        PreparedStatement ps = null;
        PreparedStatement metadata_ps = null;
        try {
        	        	
	    	JSONObject gazeEventJsonObject = inGazeDataJSONObject;    	
	    	gazeEventJsonObject.putIfAbsent("studentID", String.valueOf(studentId));
	    	gazeEventJsonObject.putIfAbsent("sessionID", String.valueOf(sessionId));

	    	int count = 1;
	        boolean insertComma = false;

	        String metadata_query = "show columns from gazeWanderingEvents";
            metadata_ps = conn.prepareStatement(metadata_query);
            Hashtable<String, String> columTypes = new Hashtable<String, String>();
            ResultSet metadata_rs = null;
            try {
            	metadata_ps = conn.prepareStatement(metadata_query);
            	metadata_rs = metadata_ps.executeQuery();
    		    while (metadata_rs.next()) {
    		    	columTypes.put(metadata_rs.getString(1), metadata_rs.getString(2));
    		    }
            } finally {
                if (metadata_ps != null)
                	metadata_ps.close();
                if (metadata_rs != null)
                	metadata_rs.close();
            }	    
            
	        String query_p1 = "insert into gazeWanderingEvents (";
	        String query_p2 = ") values (";
	        String query_p3 = ")";
	
	        Iterator<?> queryIterator = gazeEventJsonObject.keys();
	        while (queryIterator.hasNext()) {        	        
	            String key = (String)queryIterator.next();
	            if (insertComma) {
		            query_p1 = query_p1 + ",";
		            query_p2 = query_p2 + ",";	            	
	            }
	            else {
	            	insertComma = true;
	            }
            	query_p1 = query_p1 + key;
            	query_p2 = query_p2 + "?";
	        }
	
	        
            String query =  query_p1 + query_p2 + query_p3;
            ps = conn.prepareStatement(query);

            Iterator<?> valueIterator = gazeEventJsonObject.keys();
            while (valueIterator.hasNext()) {        	        
                String key = (String)valueIterator.next();
                Object valueObj = gazeEventJsonObject.get(key);
                String colType = columTypes.get(key);
                switch (colType) {
                	case "timestamp":
                		// Don't set - it defaults to CURRENT_TIMESTAMP
                		count++;
                		break;
                	case "int(11)":
                		int t = Integer.parseInt((String)valueObj);
	                    ps.setInt(count++, t);                	
                		break;
                	default:
	                    ps.setString(count++, (String) valueObj);                	
                		break;
                }
            }
         
            ps.executeUpdate();
            ps.close();
        } finally {
            if (ps != null)
                ps.close();
        }
    }


	
	//	var gazeData = '{"UpDegrees": "1", "DownDegrees": "2", "LeftDegrees": "3", "RightDegrees": "4", "tiltDegrees": "5", "modelConfidence": "6", "modelversionID": "7"}';
	
	


}



