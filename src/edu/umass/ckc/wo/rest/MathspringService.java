package edu.umass.ckc.wo.rest;

import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.content.CCContentMgr;
import edu.umass.ckc.wo.content.LessonMgr;
import edu.umass.ckc.wo.db.DbAdmin;
import edu.umass.ckc.wo.login.PasswordAuthentication;
import edu.umass.ckc.wo.tutor.probSel.BaseExampleSelector;
import edu.umass.ckc.wo.tutor.vid.BaseVideoSelector;
import edu.umass.ckc.wo.woserver.ServletUtil;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by david on 1/11/2017.
 *  * Here are some test URLs:
 * http://localhost:8080/mt/rest/admin/username/dm/pw/dm1!
 * http://localhost:8080/mt/rest/admin/username/dm/pw/dmwrong
 */
@Path("/admin")
public class MathspringService {
    private final static Logger logger = Logger.getLogger(MathspringService.class);

    @javax.ws.rs.core.Context
    ServletContext servletContext;

    public static Connection getConnection (ServletContext sc) throws SQLException {
        // Obtain our environment naming context
        Context initCtx;
        try {
            initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            // There is servlet context parameter defined in web.xml that says the name of the database resource to use from context.xml
            String datasource = sc.getInitParameter("wodb.datasource");
            // Look up our data source by name
            DataSource ds = (DataSource) envCtx.lookup(datasource);
            Connection conn = ds.getConnection();
            // sometimes after a long time period a connection is returned that is no longer valid
            // and we get an exception.   This will keep requesting connections until a valid one
            // is returned


            return conn;
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return null;
    }

    private void serviceInit (Connection conn) throws Exception {
        // If the service is called before any of the tutor servlets initialize the system, we do the servlet setServletInfo because we need static objects created for this
        ServletUtil.initialize(servletContext,conn);
        if (!ProblemMgr.isLoaded())  {
            ProblemMgr.loadProbs(conn);
            CCContentMgr.getInstance().loadContent(conn);
            LessonMgr.getAllLessons(conn);  // only to check integrity of content so we see errors early

        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/username/{username}/pw/{pw}")
    public Response authenticateUser(@PathParam("username")String username, @PathParam("pw")String pw) throws Exception {
        logger.debug("lookup user:" + username + " pw: " + pw);
        JSONObject jsonObject = new JSONObject();
        Connection conn = getConnection(servletContext);
        logger.debug("connection: " + conn);
        // If the service is called before any of the tutor servlets initialize the system, we do the servlet setServletInfo because we need static objects created for this
//        serviceInit(conn);
        PasswordAuthentication auth = PasswordAuthentication.getInstance();
        String hashedPw = DbAdmin.getPassword(conn, username);
        logger.debug("pw hash: " + hashedPw);
        boolean b= false;
        if (hashedPw != null)
            b = auth.authenticate(pw.toCharArray(),hashedPw);
        jsonObject.put("result", Boolean.toString(b));
        String result = jsonObject.toString();
        return Response.status(200).entity(result).build();
    }
}
