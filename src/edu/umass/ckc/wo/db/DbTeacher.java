package edu.umass.ckc.wo.db;

import edu.umass.ckc.wo.beans.ClassInfo;
import edu.umass.ckc.wo.beans.Teacher;
import edu.umass.ckc.wo.event.admin.AdminTeacherRegistrationEvent;
import edu.umass.ckc.wo.login.PasswordAuthentication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 7/31/15
 * Time: 9:32 AM
 * To change this template use File | Settings | File Templates.
 * Frank 04-23-2020 Added function to support user profile self-maintenance
 * Frank 05-29-2020 issue #28 new functions for password reset
 * Frank 09-14-2020	issue #237 added pauseStudentUse coding
 * Frank 11-03-2020	issue #276 allow a Master teacher to 'login as' by using Master teacher's password with target teacher username.
 * Frank 11-12-2020	issue #276 change calling interface to pass back password used, Normal or Master 
 * Frank 10-09-2021	issue #528 Research Tool 
 */
public class DbTeacher {

    public static int getTeacherId (Connection conn, String username, String pw) throws SQLException {

    	boolean m = false;
    	int id = -1;
        PreparedStatement ps = conn.prepareStatement("select ID,password from Teacher where userName=?");
        ps.setString(1,username);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            id =  rs.getInt("ID");
            String token = rs.getString("password");
            m = PasswordAuthentication.getInstance().authenticate(pw.toCharArray(),token);
            if (m)
                return id;
            else {
            	String query = "select Administrator from globalsettings where id_shouldBe1 = ?";
                PreparedStatement ps2 = conn.prepareStatement(query);
                ps2.setString(1,"1");
                ResultSet rs2 = ps2.executeQuery();
                if (rs2.next()) {                	            	
                    String admin =  rs2.getString(1);
	                ps.setString(1,admin);
	                ResultSet rs3 = ps.executeQuery();
	                if (rs3.next()) {
	                    token = rs3.getString("password");
	                    m = PasswordAuthentication.getInstance().authenticate(pw.toCharArray(),token);
	                    if (m)
	                        return id;
	                    else 
	                    	return -1;
	                }
	                else
	                	return -1;
                }
                else 
                	return -1;
            }
        }
        else return -1;
    }

    public static String getTeacherIdAsString (Connection conn, String username, String pw) throws SQLException {

    	boolean m = false;
    	String result = "-1~failure";
    	int id = -1;
        PreparedStatement ps = conn.prepareStatement("select ID,password from Teacher where userName=?");
        ps.setString(1,username);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            id =  rs.getInt("ID");
            String token = rs.getString("password");
            m = PasswordAuthentication.getInstance().authenticate(pw.toCharArray(),token);
            if (m) {
            	result = String.valueOf(id) + "~Normal";
                return result;
            }
            else {
            	String query_admin = "select Administrator from globalsettings where id_shouldBe1 = ?";
                PreparedStatement ps_admin = conn.prepareStatement(query_admin);
                ps_admin.setString(1,"1");
                ResultSet rs_admin = ps_admin.executeQuery();
                if (rs_admin.next()) {                	            	
                    String admin =  rs_admin.getString(1);
	                ps.setString(1,admin);
	                ResultSet rs_admin2 = ps.executeQuery();
	                if (rs_admin2.next()) {
	                    token = rs_admin2.getString("password");
	                    m = PasswordAuthentication.getInstance().authenticate(pw.toCharArray(),token);
	                    if (m) {
	                    	result = String.valueOf(id) + "~Master";
	                    	return result;
	                    }
	                    else {
	                    	String query_researcher = "select Researcher from globalsettings where id_shouldBe1 = ?";
	                        PreparedStatement ps_researcher = conn.prepareStatement(query_researcher);
	                        ps_researcher.setString(1,"1");
	                        ResultSet rs_researcher = ps_researcher.executeQuery();
	                        if (rs_researcher.next()) {                	            	
	                            String researcher =  rs_researcher.getString(1);
	        	                ps.setString(1,researcher);
	        	                ResultSet rs_researcher2 = ps.executeQuery();
	        	                if (rs_researcher2.next()) {
	        	                    token = rs_researcher2.getString("password");
	        	                    m = PasswordAuthentication.getInstance().authenticate(pw.toCharArray(),token);
	        	                    if (m) {
	        	                    	result = String.valueOf(id) + "~Researcher";
	        	                    	return result;
	        	                    }
	        	                    else {
	        	                    	return result;
	        	                    }
	        	                }
	        	                else {
	        	                	return result;
	        	                }
	                        }	                    		                    
		                	return result;
	                    }
	                }
	                else
	                	return result;
                }
                else 
                	return result;
            }
        }
        else return result;
    }

    public static void insertTeacher (Connection conn, String userName, String fname, String lname, String pw, String email) throws SQLException {

        PreparedStatement ps = null;
        try {
            String s = "insert into Teacher (fname,lname,password,userName,email) values (?,?,?,?,?)";
            ps = conn.prepareStatement(s);
            ps.setString(1, fname);
            ps.setString(2, lname);
            String token = PasswordAuthentication.getInstance(0).hash(pw.toCharArray());
            ps.setString(3, token);
            ps.setString(4, userName);
            ps.setString(5, email);
            ps.executeUpdate();
        } finally {
            if (ps != null)
                ps.close();
        }
    }

    public static String getTeacherName (Connection conn, int teacherid) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("select fname, lname from Teacher where id=?");
        ps.setString(1,Integer.toString(teacherid));
        ResultSet rs = ps.executeQuery();
        String tname;
        if (rs.next()){
            tname = rs.getString(1);
            if (rs.next())
                tname = tname+" "+rs.getString(2);
            return tname;
        }else{
            tname = "Teacher";
            return tname;
        }

    }

    public static Teacher getTeacherFromUsername(Connection conn, String username) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "select fname,lname,id, email from teacher where username=?";
            stmt = conn.prepareStatement(q);
            stmt.setString(1,username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                String f = rs.getString(1);
                String l = rs.getString(2);
                int i    = rs.getInt(3);
                String e = rs.getString(4);
                return new Teacher(e,i,f,l,username,null,0);
            }
            return null;
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }

    public static Teacher getTeacherFromEmail(Connection conn, String email) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "select fname,lname,id, userName from teacher where email=?";
            stmt = conn.prepareStatement(q);
            stmt.setString(1,email);
            rs = stmt.executeQuery();
            if (rs.next()) {
                String f = rs.getString(1);
                String l = rs.getString(2);
                int i    = rs.getInt(3);
                String u = rs.getString(4);
                return new Teacher(email,i,f,l,u,null,0);
            }
            return null;
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }

    public static int countEmailAddrs(Connection conn, String email) throws SQLException {
    	
    	int count = 0;
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
        	String q = "select count(email) as EMAILCOUNT from teacher where email = ?";
            stmt = conn.prepareStatement(q);
            stmt.setString(1,email);
            rs = stmt.executeQuery();
            if (rs.next()) {
                count    = rs.getInt("EMAILCOUNT");
            }
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
        return count;
    }

    
    public static List<Teacher> getAllTeachers(Connection conn, boolean includeClasses) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "select id, fname,lname,username from teacher order by lname asc";
            stmt = conn.prepareStatement(q);
            List<Teacher> result = new ArrayList<Teacher>();
            rs = stmt.executeQuery();
            while (rs.next()) {
                int id= rs.getInt(1);
                String fname = rs.getString("fname");
                String lname = rs.getString("lname");
                String uname = rs.getString("username");
                Teacher t = new Teacher(null,id,fname,lname,uname,null,0);
                if (includeClasses) {
                    List<ClassInfo> classes = DbClass.getTeacherClasses(conn,t.getId());
                    t.setClasses(classes);
                }
                result.add(t);
            }
            return result;
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }

    public static Teacher getTeacher(Connection conn, int teacherId) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "select fname,lname,username, email, pauseStudentUse from teacher where id=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,teacherId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                String f = rs.getString(1);
                String l = rs.getString(2);
                String u = rs.getString(3);
                String e = rs.getString(4);
                int p = rs.getInt(5);
                return new Teacher(e,teacherId,f,l,u,null,p);
            }
            return null;
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }

    // Supports Teacher Profile self-maintenance
    public static boolean editTeacher(Connection conn, int teacherId, String fname, String lname, String email, String pw) throws SQLException {
        PreparedStatement ps = null;
        try {
            if (pw.length() > 0) {
                String q = "update teacher set fname=?, lname=?, email=?, password=? where id=?";
                ps = conn.prepareStatement(q);
                ps.setString(1, fname);
                ps.setString(2, lname);
                ps.setString(3, email);
                ps.setString(4, PasswordAuthentication.getInstance().hash(pw.toCharArray()));
                ps.setInt(5, teacherId);
                int n = ps.executeUpdate();
                return n == 1;
            } else {
                String q = "update teacher set fname=?, lname=?, email=? where id=?";
                ps = conn.prepareStatement(q);
                ps.setString(1, fname);
                ps.setString(2, lname);
                ps.setString(3, email);
                ps.setInt(4, teacherId);
                int n = ps.executeUpdate();
                return n == 1;
            }
        }
            finally{
                if (ps != null)
                    ps.close();
            }

    }


 
    public static boolean modifyTeacher(Connection conn, int teacherId, String fname, String lname, String uname, String pw) throws SQLException {
        PreparedStatement ps = null;
        try {
            if (pw.length() > 0) {
                String q = "update teacher set username=?, lname=?, fname=?,password=? where id=?";
                ps = conn.prepareStatement(q);
                ps.setString(1, uname);
                ps.setString(2, lname);
                ps.setString(3, fname);
                ps.setString(4, PasswordAuthentication.getInstance().hash(pw.toCharArray()));
                ps.setInt(5, teacherId);
                int n = ps.executeUpdate();
                return n == 1;
            } else {
                String q = "update teacher set username=?, lname=?, fname=? where id=?";
                ps = conn.prepareStatement(q);
                ps.setString(1, uname);
                ps.setString(2, lname);
                ps.setString(3, fname);
                ps.setInt(4, teacherId);
                int n = ps.executeUpdate();
                return n == 1;
            }
        }
            finally{
                if (ps != null)
                    ps.close();
            }

    }

    public static boolean modifyTeacherPassword(Connection conn, String uname, String pw) throws SQLException {
        PreparedStatement ps = null;
        try {
            if (pw.length() > 0) {
                String q = "update teacher set password=? where username=?";
                ps = conn.prepareStatement(q);
                ps.setString(1, PasswordAuthentication.getInstance().hash(pw.toCharArray()));
                ps.setString(2, uname);
                int n = ps.executeUpdate();
                return n == 1;
            }
            else {
            	return false;
            }
        }
        finally{
            if (ps != null)
                 ps.close();
        }

    }

    /**
     * Deleting teachers depends on first deleting their classes
     * @param conn
     * @param teacherIds
     * @throws SQLException
     */
    public static void deleteTeachers(Connection conn, int[] teacherIds) throws SQLException {
        for (int tid: teacherIds) {
            List<Integer> classIds = DbClass.getTeacherClassIds(conn,tid);
            for (int cid : classIds)
                DbClass.deleteClass(conn,cid);
            // once all the classes are gone there should be no other tables related to a teacher and now we delete the teacher row.
            deleteTeacher(conn, tid);
        }
    }


    private static int deleteTeacher(Connection conn, int tid) throws SQLException {
        PreparedStatement stmt = null;
        try {
            String q = "delete from teacher where id=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, tid);
            return stmt.executeUpdate();
        } finally {
            if (stmt != null)
                stmt.close();
        }
    }

    public static void main(String[] args) {
        try {
            Connection conn = DbUtil.getAConnection("rose.cs.umass.edu");
            PreparedStatement ps = null;
            ResultSet rs = null;
            PreparedStatement stmt = null;
            try {
                String q = "select id, password,oldpw  from teacher where id=70";
                stmt = conn.prepareStatement(q,ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_UPDATABLE);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    int c = rs.getInt(1);
                    String pw = rs.getString("password");
                    String opw = rs.getString("oldpw");
                    String token = PasswordAuthentication.getInstance(0).hash(opw.toCharArray());
                    rs.updateString("password",token);
                    rs.updateRow();
                    System.out.println(token);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (stmt != null)
                    stmt.close();
                if (rs != null)
                    rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}