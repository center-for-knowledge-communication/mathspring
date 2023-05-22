package edu.umass.ckc.wo.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.ServletContext;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;

import edu.umass.ckc.wo.admin.LessonMap;
import edu.umass.ckc.wo.admin.LoginMap;
import edu.umass.ckc.wo.admin.PedMap;
import edu.umass.ckc.wo.admin.PedagogyParser;
import edu.umass.ckc.wo.config.LessonXML;
import edu.umass.ckc.wo.config.LoginXML;
import edu.umass.ckc.wo.lc.DbLCRule;
import edu.umass.ckc.wo.lc.LCRuleset;
import edu.umass.ckc.wo.tutor.Pedagogy;
import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.tutor.probSel.LessonModelParameters;
import edu.umass.ckc.wo.xml.JDOMUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 11/18/15
 * Time: 5:44 PM
 * To change this template use File | Settings | File Templates.
 * 
 * Frank	08-03-21	Issue 150 and 487 - Remember LCProfile selection on login page
 * Frank	08-03-21	Issue 150 and 487 - Remove "No Companion" option from LCGetProfiles()
 * Frank	04-05023	Issue 725 add lang text to lcprofile
 * Frank	05-13-23	Issue #763 - make LCs selectable by class
 */
public class DbPedagogy {

    private static HashMap<String,Pedagogy> pedsByOldIdMap = new HashMap<String, Pedagogy>();

    private static boolean buildMapOfOldIds = true;

    public static LessonMap buildAllLessons (Connection conn) throws Exception {
        LessonMap m = new LessonMap();
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "select name,definition from lessonDefinition";
            stmt = conn.prepareStatement(q);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String name= rs.getString(1);
                String xml= rs.getString(2);
                buildLesson(name, xml, m);
            }
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        } 
        return m;
    }

    private static void buildLesson(String name, String xml, LessonMap m) throws Exception {
        Element e = JDOMUtils.getRoot(xml);
        Element interventions = e.getChild("interventions");
        Element control = e.getChild("controlParameters");
        String lmClassname = e.getAttributeValue("className");

        LessonXML lx = new LessonXML(interventions,control,name,lmClassname);
        m.put(name,lx);
    }

    public static LoginMap buildAllLoginSequences (Connection conn) throws Exception {
        LoginMap m = new LoginMap();
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "select name,definition from loginbehavior";
            stmt = conn.prepareStatement(q);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String name= rs.getString(1);
                String xml= rs.getString(2);
                buildLogin(name, xml, m);
            }
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
        return m;
    }

    private static void buildLogin(String name, String xml, LoginMap m) throws Exception {
        Element e = JDOMUtils.getRoot(xml);
        Element interventions = e.getChild("interventions");
        Element control = e.getChild("controlParameters");
        LoginXML lx = new LoginXML(interventions,control,name);
        m.put(name,lx);
    }


    public static PedMap buildAllPedagogies(Connection conn, ServletContext servletContext) throws Exception {
        
        PedMap pedmap = new PedMap();
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "select id,isBasic,definition,login,lesson,name,simpleConfigName from pedagogy where active=1";
            stmt = conn.prepareStatement(q);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int id= rs.getInt(1);
                boolean isBasic = rs.getBoolean(2);
                String xml = rs.getString(3);
                String login = rs.getString(4);
                String lesson = rs.getString(5);
                String name = rs.getString(6);
                String simpleConfigName = rs.getString(6);
                PedagogyParser pp = new PedagogyParser();
                Pedagogy ped = pp.parsePed(xml); // Fully instantiated pedagogy based on XML only
                // now replace the fields from the db into the pedagogy
                String xmlId = ped.getId();
                // This supports a one-time-only cleanup operation of setting new pedagogy ids in db tables
                // This creates a map of oldPedId -> Pedagogy objects for this purpose
                if (buildMapOfOldIds )
                    DbPedagogy.pedsByOldIdMap.put(xmlId,ped);
                ped.setId(Integer.toString(id));
                ped.setLogin(login);
                ped.setLesson(lesson);
                ped.setName(name);
                // only basic pedagogies should have a simpleConfig name (used in the teacher tools TEACHER view of pedagogies)
                if (isBasic)
                    ped.setSimpleConfigName(simpleConfigName);
                // pedagogies can mention ruleset names that controls the learning companion.   The rulesets come from
                // either the db or a file.  Each ruleset is added to the pedagogy.

                List<LCRuleset> rulesets = ped.getLearningCompanionRuleSets();
                if (rulesets != null)
                    for (LCRuleset rset : rulesets)
                        loadRuleset(conn,rset,ped,servletContext);

               pedmap.put(Integer.toString(id),ped);
            }
            return pedmap;
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }

    // A ruleset comes from either the db or from a file.   This loads the ruleset (and all its rules) into the pedagogy.
    private static void loadRuleset (Connection conn, LCRuleset rs , Pedagogy ped, ServletContext servletContext) throws Exception {
        if (rs.isFromDb())
            DbLCRule.loadRuleSetIntoPedagogy(conn,ped,rs);
//        else
//            XMLLCRule.loadRuleSet(conn, rs, servletContext.getResourceAsStream(rs.getSource()));
    }


    private static void readPedagogiesFromFile(Connection conn) throws IOException, SQLException, JDOMException {
        FileInputStream str = new FileInputStream("f:\\dev\\mathspring\\woServer\\resources\\pedagogies.xml");
        Document d = JDOMUtils.makeDocument(str);
        Element root = d.getRootElement();
        List<Element> peds =  root.getChildren("pedagogy");
        for (Element ped : peds) {
            readPedagogy(conn,ped);
        }
    }

    private static void readPedagogy(Connection conn, Element ped) throws SQLException {

        String name = ped.getChild("name").getTextTrim();
        Element e = ped.getChild("provideInSimpleConfig");
        String simpleConfigName=null;
        boolean isBasic = false;
        if (e != null) {
            String s = e.getAttributeValue("name");
            simpleConfigName = s;
            isBasic = true;
        }

        String lesson = ped.getChild("lesson").getTextTrim();
        String login = ped.getChild("login").getTextTrim();
        String xml = JDOMUtils.toXML(ped);
        e = ped.getChild("id") ;
        String idS = e.getTextTrim();
        int id = Integer.parseInt(idS);
        if (id == 44 || id == 45 || id == 46 || id == 48)
            writeToDb(conn,name,simpleConfigName,lesson,login,xml,isBasic );
    }

    private static int writeToDb(Connection conn, String name, String simpleConfigName,
                                  String lesson, String login, String xml, boolean isBasic) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "insert into pedagogy (isBasic,login,lesson,name,simpleConfigName,definition,active)" +
                    " values (?,?,?,?,?,?,?)";
            stmt = conn.prepareStatement(q,PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setBoolean(1,isBasic);
            stmt.setString(2, login);
            stmt.setString(3, lesson);
            stmt.setString(4, name);
            stmt.setString(5, simpleConfigName);
            stmt.setString(6, xml);
            stmt.setBoolean(7, true);
            stmt.execute();
            rs = stmt.getGeneratedKeys();
            rs.next();
            int newId = rs.getInt(1);
            return newId;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
        }
        return 21;
    }



    private static void adjustClassPedagogies(Connection conn) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "select classId, pedagogyId from classPedagogies";
            stmt = conn.prepareStatement(q,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int clid= rs.getInt(1);
                int pedId= rs.getInt(2);
                Pedagogy ped = pedsByOldIdMap.get(Integer.toString(pedId));
                // ped still exists so alter the ped in the table
                if (ped != null)   {
                    rs.updateInt("pedagogyId",Integer.parseInt(ped.getId()));
                    rs.updateRow();
                }
                // if the pedagogy cannot be found, then a class should not be listing it as a pedagogy.
                else {
                    rs.deleteRow();

                }
            }
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }


    /**
     * Go through the database tables that ref pedagogy Ids and fix them to use the table ids rather
     * than the one in the pedagogy XML.   If a pedagogy is refed that can't be found then it must have been
     * deleted.  In this situation we will set up userpedagogyparameters.overridePedagogy to refer to the ID for
     * Jane Full empathy (ID 1) so that these users can login
     * @param conn
     */
    private static void adjustStudents(Connection conn) throws SQLException {
        // for each student, change his pedagogyId (and his override pedagogy if necessary)
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "select id, pedagogyId,classId from student";
            stmt = conn.prepareStatement(q,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int studId= rs.getInt(1);
                int oldPedId = rs.getInt(2);     // use the old pedId to get the Pedagogy object from a map.
                boolean hasOldPed = !rs.wasNull() && !(oldPedId <= 0);
                int classid = rs.getInt(3);
                Pedagogy ped=null;
                if (hasOldPed)
                    ped = pedsByOldIdMap.get(Integer.toString(oldPedId));
                else ped = pedsByOldIdMap.get("1"); // a user with null pedagogyId is set to ped 1.
                    // found: so replace the student's pedId with the new id which is in the pedagogy object
                if (ped != null)  {
                        rs.updateInt("pedagogyId",Integer.parseInt(ped.getId()));
//                        rs.updateBoolean("pedIdAdjusted", true);
                        rs.updateRow();
                        // change teh pedagogygroup table <classId,studId,pedId> so that it has the right pedId
                        // not really necessary- this is what allows peds to be automatically assigned
                        setPedagogyGroup(conn,studId,oldPedId,ped.getId() );
                    }
                // student uses a pedagogy that was deleted (presumably), so we leave it alone for historical purposes.
                // But we do set the student's override pedagogy (in userpedagogyparameters) to a pedagogy that does exist
                // Issue:  what if the student already has an override pedagogy (from assistments)?   - I guess we blow it away
                else {
//                    rs.updateBoolean("pedIdAdjusted",true);
//                    rs.updateRow();
                    setStudentOverridePedagogy(conn,studId,1, classid); // set the students override pedagogy to 1.
                }
            }
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }

    private static void setPedagogyGroup(Connection conn, int studId, int oldPedId, String newPedId) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "select classId,pedagogyId,studId from pedagogyGroup where pedagogyId=? and studId=?";
            stmt = conn.prepareStatement(q,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stmt.setInt(1,studId);
            stmt.setInt(2,oldPedId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                int c= rs.getInt(1);
                int p= rs.getInt(2);
                int s= rs.getInt(3);
                rs.updateInt("pedagogyId",Integer.parseInt(newPedId));
                rs.updateRow();
            }
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }

    private static void setStudentOverridePedagogy(Connection conn, int studId, int pedId, int classId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select studId, overridePedagogy from userPedagogyParameters where studId=?";
            ps = conn.prepareStatement(q,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ps.setInt(1, studId);
            rs = ps.executeQuery();
            if (rs.next()) {
                rs.getInt(1);
                int opedId = rs.getInt(2);  // If there is one there, it's the one that assistment users were using.
                if (rs.wasNull()) {
                    rs.updateInt("overridePedagogy",pedId);
                    rs.updateRow();
                }
                else {     // convert the id of the overriding pedagogy that assistments users were set to.
                    Pedagogy ped = pedsByOldIdMap.get(Integer.toString(opedId));
                    rs.updateInt("overridePedagogy",Integer.parseInt(ped.getId()));
                    rs.updateRow();
                }
            }
            // We have a bug here.  This puts dumb values in important fields like maxProbs, maxTime.   When this user logs
            // in these values are used instead of those that are set-up for the pedagogy.  Effect is that students seem mastered
            // in all topics.  We need to put the correct values in this row  because they override values set
            // by default for the lesson or the class.  We get them in this order of precedence:
            // classconfig, lessonDefinition
            else {
                Pedagogy ped = Settings.pedagogyGroups.get(Integer.toString(pedId));
                // get the classConfig lesson settings
                LessonModelParameters classParams = DbClass.getLessonModelParameters(conn, classId);
                // get the lesson settings from the lessonDefinition
                LessonXML lx =  Settings.lessonMap.get(ped.getLessonName());
                // 5/16/16 DM Had to comment out the below line which was important for migrating pedagogies as defined in these methods.
                // But I've moved the lesson model parameters to an object that only lives inside the lesson model and can no longer be fetched
                // from the LessonXML object.  Will have to get the params some other way (thru the LessonModel) in order to overload them.
                // LessonModelParameters lessonModelParameters = lx.getLessonModelParams();
                LessonModelParameters lessonModelParameters=null; // set to null so it will cause an exception until the issue above is dealt with
                // we prefer class params to ones defined in lesson
                lessonModelParameters = classParams;
                // Now set the users lesson settings.
                DbUser.insertStudentOverridePedagogy(conn, studId, pedId, lessonModelParameters);
            }
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();

        }

    }

    public static Map<Integer, List<String>> getLCprofiles(Connection conn, int classId, int currStudentPedId) throws SQLException {
    	
		Map<Integer, List<String>> LCprofiles = new HashMap<Integer, List<String>>();

    	List<String> LCprofilesArr = new ArrayList<>();
		ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
        	String q = "select cp.pedagogyId, p.name, p.shortName from classpedagogies cp INNER JOIN pedagogy p ON cp.pedagogyId=p.id where cp.classid = ?"; 
//        			"UNION\r\n" + 
//        			"select pp.id, pp.name, pp.shortName from pedagogy pp where pp.id = 19";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, classId);
            rs = stmt.executeQuery();
            String checked = " ";
            while (rs.next()) {
            	int pedId = rs.getInt(1);
            	if (pedId == currStudentPedId) {
            		checked = " checked='checked' ";
            	}
            	else {
            		checked = " ";           		
            	}
            	String lang = "";
            	if ((rs.getString(3).equals("Lucas")) || (rs.getString(3).equals("Isabel"))){
            		lang = " (esp)";
            	}
            	String LCdef = pedId + "~" + rs.getString(2) + "~" + rs.getString(3) + "~" + lang + "~" + checked ;
            	LCprofilesArr.add(LCdef);
            }
        } finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
        Collections.shuffle(LCprofilesArr);
        
        ListIterator <String> lit = LCprofilesArr.listIterator();
        
        while (lit.hasNext()) {
        	String element = lit.next();
        	String sp[] = element.split("~");
        
        	int pedid = Integer.valueOf(sp[0]);
        	LCprofiles.put(pedid, new ArrayList<String>(Arrays.asList(sp[1], sp[2], sp[3], sp[4])));
        }

		return LCprofiles;
    }

    public static String getSelectableLCprofiles(Connection conn) throws SQLException {
        JSONArray resultArr = new JSONArray();
        String lcProfileStr = "";
        int rowCount = 0;
		ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
        	String q = "select p.id, p.name, p.shortName from pedagogy p where p.selectable = 1"; 
            stmt = conn.prepareStatement(q);
            rs = stmt.executeQuery();
            while (rs.next()) {           	
            	int id = rs.getInt(1);
            	String lang = "(eng)";
            	String url = Settings.webContentPath + "LearningCompanion/";
            	if ((rs.getString(3).equals("Isabel"))){
            		lang = " (esp)";
                	url = Settings.webContentPath2 + "LearningCompanion/";
            	}
            	if (rs.getString(3).equals("Lucas")){
            		lang = " (esp)";
            	}
            	JSONObject resultJson = new JSONObject();
            	resultJson.put("id", String.valueOf(id));                       		
            	resultJson.put("lcname", rs.getString(2));                       		
            	resultJson.put("lcshortname", rs.getString(3));                       		
            	resultJson.put("lang", lang);                 
            	resultJson.put("url",url);
                resultArr.add(resultJson);
            }
            Collections.shuffle(resultArr);
            lcProfileStr = resultArr.toString();
	               
	    } catch (JSONException e1) {
	                // TODO Auto-generated catch block
	              e1.printStackTrace();
        } finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
		return lcProfileStr;
    }

    public static void main(String[] args) {
        try {
            DbPedagogy p = new DbPedagogy();
            DbPedagogy.buildMapOfOldIds = true;
            Connection conn = DbUtil.getAConnection("rose.cs.umass.edu");
            Settings.lessonMap = DbPedagogy.buildAllLessons(conn);
            Settings.loginMap = DbPedagogy.buildAllLoginSequences(conn);
            Settings.pedagogyGroups = DbPedagogy.buildAllPedagogies(conn,null);
//            DbPedagogy.readPedagogiesFromFile();
//            adjustStudents(conn);
            adjustClassPedagogies(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
