package edu.umass.ckc.wo.rest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;


import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Created by david on 10/5/2016.
 */

@Path("/affectiva")
public class AffectivaService {

    private final static Logger logger = Logger.getLogger(AffectivaService.class);

    @javax.ws.rs.core.Context ServletContext servletContext;

    /**
     * Receives post request from client running affectiva SDK.  Format of input is JSON like:
     * {emotions: [{name: "sad", value: 0.43}, {name: "joy", value: 9.3}],
     facepoints: [{locationId: "lnostril", x:34, y:10}, {locationId: "leye", x:45, y:959}]}
     * @param postdata
     * @param sessId
     * @return
     * @throws Exception
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/{sessionId}")
    public Response postFacialData (String postdata, @PathParam("sessionId") String sessId) throws Exception {
//        JSONTokener tokener = new JSONTokener(postdata); //uri.toURL().openStream());
        JSONObject root = JSONObject.fromObject(postdata);
        final JSONArray emotions = root.getJSONArray("emotions");
        final JSONArray facepoints = root.getJSONArray(("facepoints"));
        for (int i = 0; i < emotions.size(); i++) {
            JSONObject em = emotions.getJSONObject(i);
            AffectivaEmotion ae = AffectivaEmotion.createFromJSON(em);
            System.out.println(ae.toString());
        }
        for (int i = 0; i < facepoints.size(); i++) {
            JSONObject fp = facepoints.getJSONObject(i);
            AffectivaFacePoint afp = AffectivaFacePoint.createFromJSON(fp);
            System.out.println(afp.toString());
        }
        Connection conn = MathContentService.getConnection(servletContext);
        saveFaceData(conn,sessId,emotions,facepoints);

//        List<AffectivaFacePoint> points = aReq.getFaceDataPoints();
        root.put("sessionId",sessId);
        JSONObject o = new JSONObject();
        root.put("message","success");
        return Response.status(200).entity(root.toString()).build();
    }

    private void saveFaceData(Connection conn, String sessId, JSONArray emotions, JSONArray facepoints) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "insert into facedata (sessId, data) values (?,?)";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,Integer.parseInt(sessId));
            stmt.setString(2,emotions.toString() + facepoints.toString());
            stmt.execute();

        }
        finally {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
        }

    }

//    @POST
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Path("/test/{sessionId}")
//    public Response postFacialData (@PathParam("sessionId") String sessId) throws Exception {
//        List<AffectivaEmotion> emotions = new ArrayList<AffectivaEmotion>();
//        AffectivaEmotion ee = new AffectivaEmotion("joy",30.3);
//        emotions.add(ee);
//        ee = new AffectivaEmotion("disgust",4.56);
//        emotions.add(ee);
//        logger.debug("sessionId: " + sessId);
//
//        for (AffectivaEmotion e : emotions) {
//            logger.debug("Emotion: " + e);
//        }
//        JSONObject o = new JSONObject();
//        o.put("message","success");
//        return Response.status(200).entity(o.toString()).build();
//    }


    // http://localhost:8080/mt/affectiva/userEmotion/1234
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/userEmotion/{sessionId}")
    public Response getUserEmotion (@PathParam("sessionId") String sessId) throws Exception {
        logger.debug("request for " + sessId);
        AffectivaEmotion em = new AffectivaEmotion("disgust",0.56);
        JSONObject o = new JSONObject();
        o.put("hi","there");
        return Response.status(200).entity(o.toString()).build();
//        return em;
    }


    public static void main (String[] args) {
        String json = "{emotions: [{name: \"sad\", value: 0.43}, {name: \"joy\", value: 9.3}], " +
                "facepoints: [{locationId: \"lnostril\", x:34, y:10}, {locationId: \"leye\", x:45, y:959}]}";
        JSONObject o = JSONObject.fromObject(json);
        System.out.println(o.toString());
    }



}
