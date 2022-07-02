package edu.umass.ckc.wo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Iterator;



/**
 * Copyright (c) University of Massachusetts
 * Written by: Frank Sylvia
 * Date: Jun 26, 2021
 * Frank	006-26-21	Support for gaze detection
 * Frank	07-03-21	Add update function for eventLogId
 */
public class DbGaze {


	public static String getStudentParams(Connection conn, int studId, int classId) throws SQLException {
		String result = "";
		String studentParams="";
		String query = "select gaze_params_json from student where id = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
    	    ps = conn.prepareStatement(query);
		    ps.setInt(1,studId);
		    rs = ps.executeQuery();
		    if (rs.next()) { 
		    	studentParams =  rs.getString(1);
		    }
        } finally {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
        }

        String classParams = getClassParams(conn, classId);

    	if ((studentParams == null) || (studentParams.length() == 0)) {
        	result = classParams ;
    	}
    	else {    
        	result = updateGazeParams(studentParams,classParams);
        }
        return result;

	}
		

	public static String getClassParams(Connection conn, int classId) throws SQLException {


		String result = "";
		String classParams = "";
		String query = "select gaze_params_json from classconfig where classId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
    	    ps = conn.prepareStatement(query);
		    ps.setInt(1,classId);
		    rs = ps.executeQuery();
		    if (rs.next()) { 
		    	classParams =  rs.getString(1);
		    }
        } finally {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
        }
    	
        String globalParams = getGlobalParams(conn);

    	if ((classParams == null) || (classParams.length() == 0)) {
        	result = globalParams ;
    	}
    	else {    
        	result = updateGazeParams(classParams,globalParams);
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

	public static String updateGazeParams(String target, String source) throws SQLException {

    	String result = "";
		System.out.println(target);
		System.out.println(source);

		JSONObject sourceJsonObject = (JSONObject)JSONSerializer.toJSON(source);    	
    	JSONObject targetJsonObject = (JSONObject)JSONSerializer.toJSON(target);    	

    	Iterator s = sourceJsonObject.keys();
    	
    	while (s.hasNext()) {
    		String skey = (String) s.next();
    		Object obj = targetJsonObject.get(skey);
			if (obj == null) {
				Object tobj = sourceJsonObject.get(skey);
				Object r = targetJsonObject.put(skey, tobj);
			}   		
    	}
    	result = targetJsonObject.toString();
		return result;
	}
	
	public static int getClassGazeDetectionOn(Connection conn, int classId) throws SQLException {

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
	
	
	public static String getLCFileName(Connection conn, int LCMessageID) throws SQLException {
		String result = "";

		String query = "select htmlFileName from lcmessageinstance where LCMessageID = ? and LCMessageStyleID = 2";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
    	    ps = conn.prepareStatement(query);
		    ps.setInt(1,LCMessageID);
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
		
		return result;
	}
	
    public static int insertGazeWanderingEvent (Connection conn, int studentId, int sessionId, int problemId,  JSONObject inGazeDataJSONObject) throws SQLException {

        ResultSet rs = null;
        int newId = 0;
        PreparedStatement ps = null;
        PreparedStatement metadata_ps = null;
        try {
        	        	
	    	JSONObject gazeEventJsonObject = inGazeDataJSONObject;    	
	    	gazeEventJsonObject.putIfAbsent("studentID", String.valueOf(studentId));
	    	gazeEventJsonObject.putIfAbsent("sessionID", String.valueOf(sessionId));
	    	gazeEventJsonObject.putIfAbsent("problemID", String.valueOf(problemId));

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
            ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            Iterator<?> valueIterator = gazeEventJsonObject.keys();
            while (valueIterator.hasNext()) {        	        
                String key = (String)valueIterator.next();
                Object valueObj = gazeEventJsonObject.get(key);
                String colType = columTypes.get(key);
                if (colType == null) {
                	colType = "";
                }
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
            rs = ps.getGeneratedKeys();
            rs.next();
            newId = rs.getInt(1);
            ps.close();
        } finally {
            if (ps != null)
                ps.close();
        }
        return newId;
    }

    public static void updateGazeWanderingEvent (Connection conn, int eventLogId, int gazeEventId) throws SQLException {

        ResultSet rs = null;
        int newId = 0;
        PreparedStatement ps = null;
        try {
        	        		        
            String query =  "update GazeWanderingEvents set eventLogId = ? where id = ?";

            ps = conn.prepareStatement(query);
            ps.setInt(1, eventLogId);
            ps.setInt(2, gazeEventId);
            ps.executeUpdate();
            ps.close();
        } finally {
            if (ps != null)
                ps.close();
        }
    }


    public static int insertGazePredictionEvent (Connection conn, int studentId, int sessionId,  JSONObject predictionDataJSONObject) throws SQLException {


        ResultSet rs = null;
        int newId = 0;
        PreparedStatement ps = null;
        PreparedStatement metadata_ps = null;
        try {
        	        	
	    	JSONObject gazeEventJsonObject = predictionDataJSONObject;    	
	    	gazeEventJsonObject.put("studentID", String.valueOf(studentId));
	    	gazeEventJsonObject.put("sessionID", String.valueOf(sessionId));

	    	int count = 1;
	        boolean insertComma = false;

	        String metadata_query = "show columns from gazepredictionevents";
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
            
	        String query_p1 = "insert into gazepredictionevents (";
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
            ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            Iterator<?> valueIterator = gazeEventJsonObject.keys();
            while (valueIterator.hasNext()) {        	        
                String key = (String)valueIterator.next();
                Object valueObj = gazeEventJsonObject.get(key);
                String colType = columTypes.get(key);
                if (colType == null) {
                	colType = "";
                }
                switch (colType) {
                	case "timestamp":
                		// Don't set - it defaults to CURRENT_TIMESTAMP
                		count++;
                		break;
                	case "int(11)":
                		int ti = Integer.parseInt((String)valueObj);
	                    ps.setInt(count++, ti);                	
                		break;
                	case "double":
                		double td = Double.parseDouble((String)valueObj);
	                    ps.setDouble(count++, td);                	
                		break;
                	default:
	                    ps.setString(count++, (String) valueObj);                	
                		break;
                }
            }
         
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            rs.next();
            newId = rs.getInt(1);
            ps.close();
        } finally {
            if (ps != null)
                ps.close();
        }
        return newId;
    	
    	
    	
/*    	
    	PreparedStatement ps = null;
        PreparedStatement metadata_ps = null;
        	
	    	JSONObject predictionEventJsonObject = predictionDataJSONObject;    	
	    	predictionEventJsonObject.putIfAbsent("studentID", String.valueOf(studentId));
	    	predictionEventJsonObject.putIfAbsent("sessionID", String.valueOf(sessionId));

	    	int count = 1;
	        boolean insertComma = false;

	        String metadata_query = "show columns from  gazepredictionevents";
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

	
	        Iterator<?> queryIterator = predictionEventJsonObject.keys();
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
            ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            Iterator<?> valueIterator = predictionEventJsonObject.keys();
            while (valueIterator.hasNext()) {        	        
                String key = (String)valueIterator.next();
                Object valueObj = predictionEventJsonObject.get(key);
                String colType = columTypes.get(key);
                if (colType == null) {
                	colType = "";
                }
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
            rs = ps.getGeneratedKeys();
            rs.next();
            newId = rs.getInt(1);
            ps.close();
        } finally {
            if (ps != null)
                ps.close();
        }
        return newId;
*/
    }

    public static void updateGazePredictionEvent (Connection conn, int sessionId, int gazeEventId) throws SQLException {

        ResultSet rs_evt = null;
        int newId = 0;

            int eventLogId = 0;
            PreparedStatement ps_evt = null;
            	        		        
            String query_evt =  "select id from eventlog where action = 'BeginProblem' and sessNum = ? order by id desc limit 1";

            ResultSet rs = null;
            try {
            	    ps_evt = conn.prepareStatement(query_evt);
                    ps_evt.setInt(1, eventLogId);
        		    rs_evt = ps_evt.executeQuery();
        		    if (rs_evt.next()) { 
        		    	eventLogId =  rs.getInt(1);
        		    }
        		rs_evt.close();
                ps_evt.close();
            } finally {
                if (ps_evt != null)
                    ps_evt.close();
            }


        
        PreparedStatement ps = null;
        try {
        	        		        
            String query =  "update gazepredictionevents set eventLogId = ? where id = ?";

            ps = conn.prepareStatement(query);
            ps.setInt(1, eventLogId);
            ps.setInt(2, gazeEventId);
            ps.executeUpdate();
            ps.close();
        } finally {
            if (ps != null)
                ps.close();
        }
    }

}

