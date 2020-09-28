package edu.umass.ckc.servlet.servbase;

import edu.umass.ckc.email.Emailer;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import javax.naming.Context;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: dave
 * Date: Jul 12, 2004
 * Time: 11:32:51 AM
 * To change this template use File | Settings | File Templates.
 * Frank	09-13-2020	issue #242 comment out email for now
  */

public class BaseServlet extends HttpServlet {
    protected String dbDriver = "com.mysql.jdbc.driver" ;
    protected String dbUser = "root";
    protected String dbPassword;
    protected String dbPrefix;
    protected String dbSource="wayangoutpostdb" ;
    protected String dbHost= "localhost";
    protected String contextPath;
    protected String hostPath;
    protected static String tomcatDataSourceURL="jdbc/wodb"; // make sure META-INF/context.xml defines this datasource
    public static String emailServer;
    public static String adminEmail;
    public static boolean sendErrorEmail;

    public static final boolean FORWARDED_TO_JSP = false;
    public static final boolean PRODUCED_OUTPUST = true;

//    protected static final String tomcatDataSourceURL_DavesDB="jdbc/mcasprep"; // make sure META-INF/context.xml defines this datasource
    private static Logger logger = Logger.getLogger(BaseServlet.class);

            //
    public BaseServlet() {
        super();    //To change body of overridden methods use File | Settings | File Templates.
    }



    protected void setHostAndContextPath (String servletName, ServletContext servletContext, HttpServletRequest request) {

        String scName = servletContext.getServletContextName();
        String servletNameFromURL = request.getServletPath(); // something like /TutorBrain
        servletNameFromURL = servletNameFromURL.substring(1,servletNameFromURL.length());
        StringBuffer uri = request.getRequestURL(); // like http://localhost:8082/wo4/TutorBrain
        String servPath = request.getServletPath();  // like /TutorBrain
        // a regexp to match the part I care about, the host and the port.
        Pattern pattern = Pattern.compile("(^(.)*?//(.)*?)[:|/]");
        Matcher m = pattern.matcher(uri.toString());
        m.find();
        hostPath = m.group(1) ;  // later we'll add this to the SessionManager
        pattern = Pattern.compile("(^(.)*?)/" + servletNameFromURL);
        m = pattern.matcher(uri.toString());
        m.find();
        contextPath = m.group(1);
    }

    protected void loadDbDriver () {
        try {
           Driver d = (Driver) Class.forName(dbDriver).newInstance(); // MySql
            System.out.println(d);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /**
     * Old-style connection creation based on settings in the web.xml file that name the database, user, password, etc.
     * @return
     * @throws SQLException
     */
    protected Connection getConnection2 () throws SQLException {
        String url;
        if (dbPrefix.equals("jdbc:mysql"))
            url = dbPrefix +"://"+ dbHost +"/"+ dbSource +"?user="+ dbUser ; //+"&password="+ dbPassword; // preferred by MySQL
        else // JDBCODBCBridge
            url = dbPrefix +":"+ dbSource;
//        url = "jdbc:mysql://localhost:3306/test";
//        url = "jdbc:mysql://localhost/rashidb"; // this works
        try {
            logger.info("connecting to db on url " + url);
            return DriverManager.getConnection(url,dbUser, dbPassword);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw e;
        }
    }

    //

    /**
     * New-style connection creation based on Tomcat data source that is defined in the META-INF/context.xml file.   The advantage
     * of using this technique is that it uses a connection pool and better deals with closing connections automatically if the
     * code neglects to.   With Tomcat 6,  the MySQL driver jar file must be placed in the TCHOME/libs folder.   With Tomcat 5.X the Mysql driver
     * must be placed in TCHOME/common/libs  (if that doesn't work, try TCHOME/shared/libs).  N.B. Make sure the mysql jar is not found
     * anywhere else on the classpath or it will cause failure to obtain a db connection.
     * @return
     * @throws SQLException
     */
    protected Connection getConnection () throws SQLException {
        // Obtain our environment naming context
        Context initCtx;
        try {
            initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");

            // Look up our data source by name
            DataSource ds = (DataSource) envCtx.lookup(tomcatDataSourceURL);
            Connection conn = ds.getConnection();
            // sometimes after a long time period a connection is returned that is no longer valid
            // and we get an exception.   This will keep requesting connections until a valid one
            // is returned

            // No longer necessary because of validationQuery and associated flags in context.xml
            while (!connectionIsValid(conn)) {
                conn = ds.getConnection();
            }
            return conn;
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return null;
    }

    /** do a cheap query to see if the connection is valid (because the Connection.isValid
     * method does not work).   If it isn't valid, free the resource.
     * @param dbConn
     * @return
     */
    private boolean connectionIsValid(Connection dbConn) {
        //log.debug("ENTER connectionIsValid(): "+dbConn);
        boolean result = true;

        PreparedStatement psr = null;
        try {
            //Prepared statement is used to cache the compiled SQL
            psr = dbConn.prepareStatement("SELECT NOW()");
            psr.executeQuery();
        } catch (SQLException e) {
            logger.debug("Excpetion occured, connection is not valid. "+e.getMessage());
            try {
                dbConn.close(); //dbConn is never null at this point
                dbConn=null;
            } catch (Exception ee) {
                //quite
            }
            result = false;
        } finally {
            try {
                //free up resource kept by the test statement
                if (psr!=null) {
                    psr.close();
                }
                psr=null;
            } catch (Exception e) {
                //quite
            }
        }
        return result;
    }


    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        StringBuffer output = new StringBuffer();

        //System.out.println("DEBUG:\t\t In BaseServlet.service: request:" + req.toString());
        Connection conn=null;
        try {
            conn = getConnection();
            res.setContentType("text/html");
            ServletParams params = new ServletParams(req);
            boolean producedOutput = this.handleRequest(conn,req,res,params,output);
            // Some servlets may not write output into the output buffer.   They may forward
            // the request to JSP for example.  Only write into the response's output stream
            // if the handleRequest method produces output.
            if (producedOutput) {
                res.getWriter().print(output.toString());
                res.getWriter().flush();
                res.getWriter().close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (adminEmail != null && sendErrorEmail) {
                ServletParams params = new ServletParams(req);
                StringBuffer msgBuffer = new StringBuffer();
                msgBuffer.append("error msg: ").append(e.toString());
                msgBuffer.append("\nparams: " + params + " <br>");
                StackTraceElement[] st = e.getStackTrace();
                for (int i = 0; i < st.length; i++) {
                    msgBuffer.append(st[i].toString()).append("<br>");
                }
                String errorMessage = msgBuffer.toString();
                Emailer em = new Emailer();
                String server = req.getServerName();
//                em.sendEmail(adminEmail, adminEmail,emailServer, "Wayang error message from server: " + server, msgBuffer.toString());
            }
            Element errElt = new Element("error");
            errElt.addContent(e.getMessage());
            Format f = Format.getPrettyFormat();
            XMLOutputter xout = new XMLOutputter(f);
            output.append(xout.outputString(errElt));
            res.getWriter().print(output.toString());
            res.getWriter().flush();
        } finally {
            if (conn != null)
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
        }
    }

    /**
     * This should be overriden by the subclasses so that they provide a datasource string
     * e.g. jdbc/wodbrose
     * @param servletConfig
     * @return
     */
    protected String getDataSource (ServletConfig servletConfig) {
        return "subclass must override getDataSource method ";
    }

    public void init(ServletConfig servletConfig) throws ServletException {
        logger.debug("initializing servlet");
        super.init(servletConfig);    //To change body of overridden methods use File | Settings | File Templates.
        try {
            String datasource = getDataSource(servletConfig); // should get from subclass
            adminEmail = servletConfig.getServletContext().getInitParameter("error.adminemail");
            emailServer = servletConfig.getServletContext().getInitParameter("error.smtpserver");
            String x = servletConfig.getServletContext().getInitParameter("error.sendEmail");
            if (x != null)
                sendErrorEmail = Boolean.parseBoolean(x);
            else sendErrorEmail = false;
            logger.info("Database is " + datasource);
            if (datasource != null)
                tomcatDataSourceURL  = datasource;
            Connection conn = getConnection();
            if (conn == null)
                throw new Exception("Failed to connect to database.");
            //setDbParams();
            //loadDbDriver();
            initialize(servletConfig,this.getServletContext(),conn);
            logger.debug("completed initializing servlet");
        }  catch (Exception e) {
            logger.error("initializing servlet failed");
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    protected void initialize(ServletConfig servletConfig, ServletContext servletContext, Connection connection) throws Exception {
    }


    public boolean handleRequest(Connection conn, HttpServletRequest request,
                                HttpServletResponse response, ServletParams params, StringBuffer output) throws Exception {
        return handleRequest(this.getServletContext(),conn,request,response,params,output);
   }

    protected boolean handleRequest(ServletContext servletContext, Connection conn, HttpServletRequest request,
                                    HttpServletResponse response, ServletParams params, StringBuffer servletOutput) throws Exception {
        // subclasses should override this method
        return false;
    }


    private void setDbParams() {
        dbDriver = this.getServletContext().getInitParameter("db.driver");
        dbPrefix = this.getServletContext().getInitParameter("db.prefix");
        dbSource = this.getServletContext().getInitParameter("db.source");
        dbUser = this.getServletContext().getInitParameter("db.user");
        dbPassword = this.getServletContext().getInitParameter("db.password");
        dbHost = this.getServletContext().getInitParameter("db.hostname");
    }

    public static void main(String[] args) {
        BaseServlet s = new BaseServlet();
        s.dbPrefix = "jdbc:mysql";
        s.dbHost = "localhost";
        s.dbSource = "wayangoutpostdb";
        s.dbDriver = "com.mysql.jdbc.Driver";
        s.dbUser = "WayangServer";
        s.dbPassword = "jupiter";
        s.loadDbDriver();
        try {
            Connection conn = s.getConnection2();
            s.connectionIsValid(conn);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        System.out.println("Connection established");
    }
}

