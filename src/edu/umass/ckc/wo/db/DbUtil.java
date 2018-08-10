package edu.umass.ckc.wo.db;

import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.xml.JDOMUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;

import java.io.*;
import java.sql.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: Mar 25, 2009
 * Time: 3:05:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class DbUtil {


    public static void loadDbDriver () {
        String dbDriver = "com.mysql.jdbc.Driver";
        try {
           Driver d = (Driver) Class.forName(dbDriver).newInstance(); // MySql
            System.out.println(d);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static Connection connect (String un, String pw, String url) throws SQLException {
        try {
            System.out.println("connecting to db on url " + url);
            return DriverManager.getConnection(url,un,pw);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw e;
        }
    }

    public static Connection getAConnection (String host) throws SQLException {
        Element e=null;
        if (host.equals("rose") || host.equals("rose.cs.umass.edu"))
            e = getContextResource("jdbc/wodbrose");
        else if (host.equals("localhost"))
            e = getContextResource("jdbc/wodblocal");
        if (e != null) {
            String dbUser = e.getAttributeValue("username");
            String dbPassword = e.getAttributeValue("password");
            String url = e.getAttributeValue("url");
            return connect(dbUser, dbPassword, url);
        }
        else return null;
    }

    public static Connection getAConnection () throws SQLException {
        return getAConnection("rose.cs.umass.edu");
     }


    // Go through the problem table and copy external problems into externalactivity table.   Create
    // an entry mapping each xact to its topic based on what the mapping was for the problem the xact is based on
    // Then delete the problem and the mappings from probprobgroup
    public static void doDbWork (Connection conn) throws SQLException {
        try {

            FileReader r = new FileReader (new File("u:\\wodb\\firstProbSolved.csv"));
            BufferedReader rr = new BufferedReader(r);
            rr.readLine(); // elim first line
            String l;
            while ((l = rr.readLine()) != null) {
                System.out.println(l);
                int id = Integer.parseInt(l);
                delWoProp(conn,id);

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private static void delWoProp(Connection conn, int id) throws SQLException {
        PreparedStatement stmt=null;
        try {
            String q = "delete from woproperty where wopropkey=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,id);
            stmt.executeUpdate();
        } finally {
            if (stmt != null)
                stmt.close();
        }
    }



    public static String getHost(Connection conn) throws SQLException {
       String q ="select host from hostinfo";
        PreparedStatement ps = conn.prepareStatement(q);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            String host = rs.getString(1);
            return host;
        }
        return null;
    }

    /**
     * Goes to the woServer/web/META-INF/context.xml and returns the Element of theresource that has a name
     * equal to resourceName.
     *
     * All the run/debug configurations that make use of this must have an environment variable for
     * woServerDir.
     *
     * I've got this set up by going to Run | Edit Configurations | Default | Application | Environment Variables.
     * This puts woServerDir in every runtime config for an application.
     * woServerDir=$PROJECT_DIR$/woServer/
     */
    public static Element getContextResource (String resourceName) {
        // Get the environment variable woServerDir to get where the woServer module lives.
        String woServerDir = System.getenv("woServerDir");
        String path = woServerDir + "web/META-INF/";
        path += "context.xml";
        try {
            File f = new File(path);
            Document d = JDOMUtils.makeDocument(new FileInputStream(f));
            Element e = d.getRootElement();
            List<Element> resources = e.getChildren("Resource");
            for (Element r: resources) {
                if (r.getAttributeValue("name").equals(resourceName))
                    return r;
            }
            return null;
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main2(String[] args) {

        loadDbDriver ();
        try {

            Connection conn = getAConnection("cadmium.cs.umass.edu") ;
            doDbWork(conn);
            //setClassConfigs(conn);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static void main(String[] args) {
        try {
            Connection conn = getAConnection();
            ResultSet rs=null;
            PreparedStatement stmt=null;
            try {
                String q = "select name from pedagogy p";
                stmt = conn.prepareStatement(q);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    String n= rs.getString(1);
                    System.out.println(n);
                }
            }
            finally {
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