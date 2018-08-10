package edu.umass.ckc.wo.rest;

import edu.umass.ckc.wo.assistments.CoopUser;
import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.content.CCContentMgr;
import edu.umass.ckc.wo.content.LessonMgr;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.db.DbAdmin;
import edu.umass.ckc.wo.db.DbCoopUsers;
import edu.umass.ckc.wo.db.DbPedagogy;
import edu.umass.ckc.wo.db.DbUser;
import edu.umass.ckc.wo.handler.UserRegistrationHandler;
import edu.umass.ckc.wo.login.PasswordAuthentication;
import edu.umass.ckc.wo.servletController.MariHandler;
import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.tutor.probSel.BaseExampleSelector;
import edu.umass.ckc.wo.tutor.vid.BaseVideoSelector;
import edu.umass.ckc.wo.util.Pair;
import edu.umass.ckc.wo.util.TwoTuple;
import edu.umass.ckc.wo.woserver.ServletUtil;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.sql.DataSource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


/**
 * RESTful web service to provide access to Mathspring resources.
 * Implemented using Jersey framework.
 * Created by david on 6/21/2016.
 *
 * Here are some test URLs:
 * http://localhost:8080/mt/rest/problems/ccss
 * http://localhost:8080/mt/rest/problems/ccss/6.RP.3.c
 * http://localhost:8080/mt/rest/problems/ccss/6.RP.3.c/userid/667824ad141fcc8f521f5d85f51cdbd28b353e662518dbd900963a75f61acd19
 */

@Path("/problems")
public class MathContentService {

    // TODO the below URL will be different for rose than localhost.   The value of the variable should come from web.xml
    protected static String tomcatDataSourceURL="jdbc/wodblocal"; // make sure META-INF/context.xml defines this datasource

    // TODO follow instructions from http://jersey.576304.n2.nabble.com/Servlet-Init-For-Jersey-REST-Service-td6507144.html
    // to establish a database connection once for this service.

    @javax.ws.rs.core.Context ServletContext servletContext;

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

    @GET
    @Produces("application/json")
    @Path("/ftocservice")
    public Response convertFtoC() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        Double fahrenheit = 98.24;
        Double celsius;
        celsius = (fahrenheit - 32)*5/9;
        jsonObject.put("F Value", fahrenheit);
        jsonObject.put("C Value", celsius);

        String result = "@Produces(\"application/json\") Output: \n\nF to C Converter Output: \n\n" + jsonObject;
        return Response.status(200).entity(result).build();
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
    @Path("/ccss/{std}/userid/{userid}")
    /**
     * returns JSON for the requested ccss like:  {"ccss":"8.G.2", "numProbs":"5"}
     * The request must also contain a userId which is assumed to be a MARI session token that serves as the users id in mathspring so that
     * we can use it to get to the numeric userId
     */
    public Response getCCSSStudentProblems(@PathParam("std")String ccstd, @PathParam("userid")String userid) throws Exception {
        JSONObject jsonObject = new JSONObject();
        Connection conn = getConnection(servletContext);
        serviceInit(conn);
        int n = ProblemMgr.getStandardNumProblems(conn,ccstd);
        jsonObject.put("ccss", ccstd);
        jsonObject.put("numProbs", n);
        CoopUser u = DbCoopUsers.getUser(conn,userid);
        if (u == null) {
            jsonObject.put("numProbs", 0);
        }
        else {
            int studId = u.getStudId();
            List<Integer> probIds = MariHandler.getStdProblemsForStudent(conn,studId,ccstd);
            jsonObject.put("numProbs", probIds.size());
        }

        String result = jsonObject.toString();
        return Response.status(200).entity(result).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/ccss/{std}")
    /**
     * returns JSON for the requested ccss like:  {"ccss":"8.G.2", "numProbs":"5"}
     */
    public Response getCCSSProblems(@PathParam("std")String ccstd) throws Exception {
        JSONObject jsonObject = new JSONObject();
        Connection conn = getConnection(servletContext);
        // If the service is called before any of the tutor servlets initialize the system, we do the servlet setServletInfo because we need static objects created for this
        serviceInit(conn);
        int n = ProblemMgr.getStandardNumProblems(conn,ccstd);
        jsonObject.put("ccss", ccstd);
        jsonObject.put("numProbs", n);

//        String result = "@Produces(\"application/json\") Output: \n\n" + jsonObject;
        String result = jsonObject.toString();
        return Response.status(200).entity(result).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/ccss")
    /**
     * returns JSON for all problems that are CCSS based.
     * {"ccssList":[
     {"ccss":"8.G.2", "numProbs":"5"},
     {"ccss":"6.NS.1", "numProbs":"8"},
     ...
     ]}
     *
     */
    public Response getAllCCSSProblems() throws Exception {
        JSONObject jsonObject = new JSONObject();
        Connection conn = getConnection(servletContext);
        // If the service is called before any of the tutor servlets initialize the system, we do the servlet setServletInfo because we need static objects created for this
        serviceInit(conn);
        // get back a list of <CCSS, numProbs>
        List<Pair<String,Integer>> list = ProblemMgr.getAllStandardNumProblems(conn);
        for (Pair<String,Integer> pair : list) {
            String ccss = pair.getP1();
            Integer numProbs = pair.getP2();
            JSONObject rec = new JSONObject();
            rec.put("ccss",ccss);
            rec.put("numProbs",numProbs);
            jsonObject.accumulate("problems",rec);
        }

//        String result = "@Produces(\"application/json\") Output: \n\n" + jsonObject;
        String result = jsonObject.toString();
        return Response.status(200).entity(result).build();
    }


}
