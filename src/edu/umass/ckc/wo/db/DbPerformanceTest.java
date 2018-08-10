package edu.umass.ckc.wo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *             SessionManager smgr = new SessionManager(this.conn,((DbTestEvent) e).getSessionId(), hostPath, contextPath).buildExistingSession();
 StudentState state = smgr.getStudentState();
 long begin = System.currentTimeMillis();
 for (int j=0;j<5;j++) {
 for (int i=0;i<50;i++)
 state.setProp(smgr.getStudentId(),"Prop"+i,"Val"+i);
 }
 long end = System.currentTimeMillis();
 this.output.append("<html><body> Executed 250 setProps Time: " +(end-begin) + "ms </body></html>");
 }
 *
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 3/21/13
 * Time: 12:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class DbPerformanceTest {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Provide a db host (e.g. rose.cs.umass.edu or cadmium.cs.umass.edu");
            System.exit(0);
        }
        String dbHost = args[0];
        DbUtil.loadDbDriver();
        try {
            Connection conn = DbUtil.getAConnection(dbHost);
            performQuery(conn);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private static void performQuery(Connection conn) throws SQLException {
        long begin = System.currentTimeMillis();
        for (int j=0;j<10;j++) {
            for (int i=0;i<50;i++)
                setProp(conn, 18816, "Prop" + i, "Val" + i, 0);
        }
        long end = System.currentTimeMillis();
        System.out.println("\n\n Executed 250 inserts in woproperty table\n Time: " + (end - begin) + "ms ");
}


    public static boolean setProp (Connection conn, int id, String property, String value, int position) throws SQLException {
//        synchronized (lock) {
            String q = "delete from woproperty where objid=? and property=? and position=?";
            PreparedStatement ps = conn.prepareStatement(q);
            ps.setInt(1,id);
            ps.setString(2,property);
            ps.setInt(3,position);
            ps.executeUpdate();
            try {
                ps.close();
                q = "insert into woproperty (objid,property,value,position) values (?,?,?,?)";
                ps = conn.prepareStatement(q);
                ps.setInt(1,id);
                ps.setString(2,property);
                if (value==null) {
                    //ps.setNull(3,Types.VARCHAR);
                    ps.setInt(3,0) ;
                }
                else ps.setString(3,value);
                ps.setInt(4,position);
                return ps.executeUpdate() > 0;
            } finally {
                ps.close();
            }
        }
//    }

}
