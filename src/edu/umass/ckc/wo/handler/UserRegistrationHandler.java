package edu.umass.ckc.wo.handler;


import edu.umass.ckc.wo.admin.PedagogyAssigner;
import edu.umass.ckc.wo.admin.StrategyAssigner;
import edu.umass.ckc.wo.db.DbUser;
import edu.umass.ckc.wo.db.DbClass;
import edu.umass.ckc.wo.event.admin.*;
import edu.umass.ckc.wo.html.admin.*;
import edu.umass.ckc.servlet.servbase.ServletEvent;
import edu.umass.ckc.servlet.servbase.View;
import edu.umass.ckc.wo.smgr.User;
import edu.umass.ckc.wo.strat.TutorStrategy;
import edu.umass.ckc.wo.ttmain.ttservice.util.SendEM;
import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.email.Emailer;
import edu.umass.ckc.wo.util.ServletURI;
import edu.umass.ckc.wo.util.ThreeTuple;
import edu.umass.ckc.wo.beans.ClassInfo;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles all events for creating a user.
 * 
 * Frank	06-02-2020	Issue #122 Allow student to enter class code on sign-up page
 * Frank	07-17-20	Issue #122 modified UserRegistration events for classId parameter
 * Frank	09-01-20	Issue #221 change email param handling
 * Frank	09-13-20 	issue #242
 * Frank    09-15-20    issue #242 fix test for valid email address 
 * Frank	10-29-21	issue #526 validate user entry fields
 * Frank 	02-04-23    Issue #723 - Added class clusters
 */
public class UserRegistrationHandler {
    public static final String TEST_DEVELOPER_USER = "testDeveloper";
    public static final String TEST_STUDENT_USER = "testStudent";
    public static final String REGISTER1 = "login/userregistration.jsp";
    public static final String REGISTER2 = "login/userRegSelectClass.jsp";
    public static final String REGISTER3 = "login/userRegComplete.jsp";
    public static final Logger logger = Logger.getLogger(UserRegistrationHandler.class);

	protected Locale loc = null;    
	ResourceBundle rb = null;
	
    public UserRegistrationHandler() {
    	
    }

    /**
     * Creating a user provides the student with a page with entries for all the fields in the Student and
     * Class tables. Upon gathering this, a field for each property in the UserProp table is requested.
     * A row in the Class table holds the
     * id that maps the class to a PropGroup.  This PropGroup is then connected to the UserProp rows using
     * the PropGroupMap table.
     * <p/>
     * The states of user creation are:
     * START: The initial request: generate a page requesting fields in Student/Class tables
     * STUD:  the authentication fields in the Student table come in (if valid goto CLASS; else regenerate page with error msg)
     * CLASS: the fields of the Class table come in (if valid goto GETPROPS; else regenerate page with error msg)
     * GETPROPS:  additional user property fields in the UserProps table come in (goto to final)
     * FINAL: Generate a message saying the user has been created successfully.
     */
    public View handleEvent(ServletContext sc, HttpServletRequest servletRequest, Connection conn, ServletEvent e, HttpServletResponse servletResponse) throws Exception {

    	 Locale loc = servletRequest.getLocale();
       	 String lang = loc.getLanguage();
       	 String country = loc.getCountry();

       	 System.out.println("locale set to:" + lang + "-" + country );	

       	 if (!lang.equals("es")) {
       	 	loc = new Locale("en","US");	
       	 }			
       	       	
    	
    	try {
    		rb = ResourceBundle.getBundle("MathSpring",loc);
    	}
    	catch (Exception rbe) {
    		logger.error(rbe.getMessage());	
    	}

        if (e instanceof UserRegistrationStartEvent) // the first page in registering a new user prompts for user, pw, etc
            return generateStudPage(servletRequest, (UserRegistrationStartEvent) e, servletResponse);
        else if (e instanceof UserRegistrationValidateUsernameEvent)
            return validateRequest(conn, (UserRegistrationValidateUsernameEvent) e);  // make sure the user isn't already used
        else if (e instanceof UserRegistrationAuthenticationInfoEvent) // takes the inputs for the new user: username, pw, etc
            return processStudentInfo(servletRequest, servletResponse,conn, (UserRegistrationAuthenticationInfoEvent) e);   // produces a page to select a class
        else if (e instanceof UserRegistrationClassSelectionEvent) // the input from the class selection page.
            return processClassInfo(servletRequest, servletResponse, conn, (UserRegistrationClassSelectionEvent) e); // take the user back to the login page
        else if (e instanceof UserRegistrationMoreInfoEvent) // GETPROPS state
            return processPropertyInfo(servletRequest, conn, (UserRegistrationMoreInfoEvent) e);
        else
            return null; // should never reach
    }
    
    // Function to validate the username
    public boolean isNotValidField(String name, String regex)
    {
  
        // Regex to check valid username.
//        String regex = "^[A-Za-z]\\w{3,30}$";
        
         
        // Compile the ReGex
        Pattern p = Pattern.compile(regex);
  
        // If the username is empty
        // return false
        if (name == null) {
            return true;
        }
  
        // Pattern class contains matcher() method
        // to find matching between given username
        // and regular expression.
        Matcher m = p.matcher(name);
  
        // Return if the username
        // matched the ReGex
        return (!m.matches());
    }
     



    public View validateRequest (Connection conn, UserRegistrationValidateUsernameEvent e) throws Exception {

    	String infoResult = validateInfo(conn, (UserRegistrationValidateUsernameEvent) e);
    	String userResult = "";
    	String classResult = "";
    	if (infoResult.length() == 0) {
    		userResult = validateUser(conn, (UserRegistrationValidateUsernameEvent) e);
    		classResult = validateClass(conn, (UserRegistrationValidateUsernameEvent) e);
    	}
    		
    	String result = infoResult + userResult + classResult ;
    	
    	System.out.println(userResult);
    	System.out.println(classResult);
    	System.out.println(infoResult);
    	
    	int len = userResult.length() + classResult.length() + infoResult.length();
    	if (len == 0) {
 //       if  ((userResult.length() > 0) || (classResult.length() > 0)) {
            return null;
        }
        else return new View() {
            public String getView () {
                return result;
            }
        };
    }


    public String validateUser (Connection conn, UserRegistrationValidateUsernameEvent e) throws Exception {
        final String uName = e.getUserName();
        int id = DbUser.getStudent(conn, e.getUserName());
        if (id == -1)
            return "";
        else 
            return "Username: " + uName + " " + rb.getString("already_exists_try_another_username");


    }

    public String validateClass (Connection conn, UserRegistrationValidateUsernameEvent e) throws Exception {
        //String classId = e.getClassId();

    	String strClassId = e.getClassId();
    	
    	if (strClassId.length() == 0) {
    		return "";
    	}

    	try {
    		int intClassId = Integer.parseInt(strClassId);
    		ClassInfo ci = DbClass.getClass(conn, intClassId);	
    		if (ci == null)
    			return "Class id " + strClassId + " " + "was not found." + rb.getString("unsure_of_classcode_instructions");
    		else {
    			if (ci.getIsCluster() == 1) {
        			return rb.getString("class_id") + " " + strClassId + " " + rb.getString("is_not_an_available_class") + "  " + rb.getString("unsure_of_classcode_instructions");    				
    			}
    			else {
        			return "";
    				
    			}
    		}
    	}
    	catch (NumberFormatException ex) {
    		System.out.println(ex.getMessage());
    		return rb.getString("class_code_must_be_number");
    	}
    }

    public String validateInfo (Connection conn, UserRegistrationValidateUsernameEvent e) throws Exception {

    	String errorResult = "";
    	
    	if ((e.getFirstName().length() == 0) || (isNotValidField(e.getFirstName(),"^[A-Za-z]\\w{1,30}$"))) {
        	errorResult += " " + "[First Name]";
    		
    	}
    	
    	if ((e.getLastName().length() == 0) || (isNotValidField(e.getLastName(),"^[A-Za-z]\\w{2,30}$"))) {
        	errorResult += " " + "[Last Name]";
    		
    	}
    	
    	if ((e.getAge().length() == 0) || (isNotValidField(e.getAge(),"^[0-9]\\w{1,3}$"))) {
        	errorResult += " " + "[Age]";
    		
    	}

    	if ((e.getUserName().length() == 0) || (isNotValidField(e.getUserName(),"^[A-Za-z0-9]\\w{3,16}$"))) {
        	errorResult += " " + "[Username]";
    		
    	}
    	
    	if ((e.getPassword().length() == 0) || (isNotValidField(e.getPassword(),"^[a-zA-Z0-9]+$"))) {
        	errorResult += " " + "[Password]";
    		
    	}

    	if ((e.getClassId().length() > 0) && (isNotValidField(e.getClassId(),"^[0-9]\\w{1,4}$"))) {
        	errorResult += " " + "[Class ID]";
    		
    	}


    	if (errorResult.length() > 0) {
    		errorResult = "  The following fields have invalid data: " + errorResult;
    	}
    	return errorResult;
    }    	
    /**
     * Generate the inputs that collect the user name, email address, password, etc.
     *
     * @param req
     * @param resp
     * @return
     * @throws Exception
     */
    private View generateStudPage(HttpServletRequest req, UserRegistrationStartEvent e, HttpServletResponse resp) throws Exception {
        String url = ServletURI.getURI(req);


        
        Variables v = new Variables(req.getServerName(),
                req.getServletPath(),
                req.getServerPort());
        String startPage = e.getStartPage();
        req.setAttribute("startPage",startPage);
        req.setAttribute("classId", e.getClassId());
        RequestDispatcher disp = req.getRequestDispatcher(Settings.useNewGUI()
                ? "login/userregistration_new.jsp"
                : UserRegistrationHandler.REGISTER1);
        disp.forward(req,resp);
//        return new UserRegistrationAuthenticationInfoPage(url, null, e);
        return null;
    }

    /**
     * Validate the inputs from the first registration page and regenerate with error messages or generate the next page
     * which asks them to select their class/teacher from a list.
     * @param req
     * @param conn
     * @param e
     * @return
     * @throws Exception
     */
    private View  processStudentInfo(HttpServletRequest req, HttpServletResponse resp, Connection conn, UserRegistrationAuthenticationInfoEvent e) throws Exception {
        Variables v = new Variables(req.getServerName(),
                req.getServletPath(),
                req.getServerPort());
        //String url = ServletURI.getURI(req);
        int studId;

        // We either have a real student user who is registering in a class or a test user registering in a class.
        if (e.getUserType().equals(TEST_DEVELOPER_USER))
            studId = DbUser.createUser(conn,e.getFname(),e.getLname(),e.getUserName(),e.getPassword(),e.getEmail(), e.getAge(), e.getGender(), User.UserType.test);
        else if (e.getUserType().equals(TEST_STUDENT_USER))
            studId = DbUser.createUser(conn,e.getFname(),e.getLname(),e.getUserName(),e.getPassword(),e.getEmail(),e.getAge(), e.getGender(), User.UserType.testStudent);
        else
            studId = DbUser.createUser(conn,e.getFname(),e.getLname(),e.getUserName(),e.getPassword(),e.getEmail(), e.getAge(), e.getGender(), User.UserType.student);
        if (e.getEmail()!= null && e.getEmail().contains("@")) {  	
            SendEM sender = new SendEM();
            sender.sendPassword(conn,"DoNotReply@mathspring.org", Settings.mailServer,e.getUserName(),e.getPassword(),e.getEmail());
        }
        ClassInfo[] singleClass = {null};
        
        String classId = e.getClassId();
        if ((classId == null) || (classId.length() == 0)) {
            req.setAttribute("classId","0");
        	ClassInfo[] classes = DbClass.getRecentClasses(conn);
        	req.setAttribute("classes",classes);
        }
        else {
            int intClassId = Integer.parseInt(classId);
            req.setAttribute("classId",classId);
        	ClassInfo ci = DbClass.getClass(conn, intClassId);
        	singleClass[0] = ci;
            req.setAttribute("classes",singleClass);     	
        }

        String startPage = e.getStartPage();
        req.setAttribute("startPage",startPage);
        req.setAttribute("studId",studId);
        RequestDispatcher disp = req.getRequestDispatcher(Settings.useNewGUI()
                ? "login/userRegSelectClass_new.jsp"
                : UserRegistrationHandler.REGISTER2);
        disp.forward(req,resp);
//        return new UserRegistrationAuthenticationInfoPage(url, null, e);
        return null;
//        return new UserRegistrationClassSelectionPage(url, studId, conn, Actions.createUser3,  e);

    }

    public static String genName (Connection conn, String prefix) throws SQLException {
        int count = DbUser.getGuestUserCounter(conn);
        return prefix + count;
    }



    // currently usertypes are either coopStudent or coopStudentTest
    // The assumption is that we are willing to create a mathspring user for this external user so we need to generate a name.
    public static int registerExternalUser(Connection conn, String assistmentsClassName, String externalUserName, User.UserType ut, boolean keepExternalUserName) throws Exception {
        int count = DbUser.getGuestUserCounter(conn);
        String user;
        if (!keepExternalUserName)
            user= externalUserName + count;   // create a userName from the external name + counter
        else user=externalUserName;
        int studId = DbUser.createUser(conn,"","", user,"","", "0", "", ut);
        ClassInfo cl = DbClass.getClassByName(conn, assistmentsClassName);
        int classId = cl.getClassid();
        DbUser.updateStudentClass(conn, studId, classId);
        // Now that the student is in a class, he is assigned a Pedagogy from one of the pedagogies
        // that the class uses.
        int pedId = PedagogyAssigner.assignPedagogy(conn,studId, classId);
        // store the pedagogy id in the student table row for this user.
        DbUser.setStudentPedagogy(conn,studId,pedId);
        return studId;
    }


    /**
     * This is called when the p1-p5 params that indicate the call is from Assistments ARE NOT PRESENT.  This means
     * the TeachTopic action is being requested by what we call an external means (i.e. location box in a browser or a URL clicked on)
     * This means the entry into the system is not for a given user.  Essentially its an anonymous entry and we call this a
     * "temporary user".  This means that there isn't going to be data about the user kept.
     * @param conn
     * @param className
     * @param userType
     * @return
     * @throws Exception
     */
    public static int registerTemporaryUser(Connection conn, String className, User.UserType userType) throws Exception {
        int count = DbUser.getGuestUserCounter(conn);
        String prefix="temp";
        if (userType == User.UserType.guest)
            prefix = "guest";
        // Either no isTest was included OR &isTest=true was passed so show tester controls and include testable problems
        else if (userType == User.UserType.externalTempTest)
            prefix = "externalTempTester";
        // &istest=false was passed so do NOT show tester controls and do not include testable problems.  This
        // allows one to see the system as a real user would see it (without updating stats about problem difficulty)
        else if (userType == User.UserType.externalTempNonTest)
            prefix = "externalTempCaller";
        String user = genName(conn,prefix);
        int studId = DbUser.createUser(conn,"","", user,"","", "0", "", userType);
        ClassInfo cl = DbClass.getClassByName(conn, className);
        int classId = cl.getClassid();
        DbUser.updateStudentClass(conn, studId, classId);
        // Now that the student is in a class, he is assigned a Pedagogy from one of the pedagogies
        // that the class uses.
        int pedId = PedagogyAssigner.assignPedagogy(conn,studId, classId);
        // store the pedagogy id in the student table row for this user.
        DbUser.setStudentPedagogy(conn,studId,pedId);
        return studId;
    }

    public static int registerStudentUser(Connection conn, String userName, String pw, ClassInfo classInfo, User.UserType userType) throws Exception {

        int studId = DbUser.createUser(conn,"","", userName,pw,"", "0", "", userType);
        int classId = classInfo.getClassid();
        DbUser.updateStudentClass(conn, studId, classId);
        // Now that the student is in a class, he is assigned a Pedagogy from one of the pedagogies
        // that the class uses.
        int pedId = PedagogyAssigner.assignPedagogy(conn,studId, classId);
        // store the pedagogy id in the student table row for this user.
        DbUser.setStudentPedagogy(conn,studId,pedId);
        return studId;
    }







    /**
     * Given the classId, return a List of ThreeTuples <id, internalPropName, externalPropName> that are found in the
     * PropGroup associated with that class.  Note this uses the internalName column.
     * ; the name column is for display (in the html page) purposes only
     */
    public static List getUserProperties(Connection conn, int classId) throws Exception {
        int propGroupId = getPropGroup(conn, classId); // get the propGroup for this class
        if (propGroupId == -1)
            return new ArrayList();
        String q = "select b.internalName, b.name,b.id from PropGroupMap a, UserProp b where b.id = a.propId AND " +
                "a.groupId = ?";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setInt(1, propGroupId);
        ResultSet rs = ps.executeQuery();
        ArrayList l = new ArrayList();
        while (rs.next()) {
            String ipname = rs.getString(1); // internal name
            String epname = rs.getString(2); // external name
            int id = rs.getInt(3);
            ThreeTuple tt = new ThreeTuple(new Integer(id), ipname, epname);
            l.add(tt);
        }
        return l;
    }

    public static int getPropGroup(Connection conn, int classId) throws Exception {
        String q = "select propGroupId from Class where id=?";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setInt(1, classId);
        ResultSet rs = ps.executeQuery();
        if (rs.next())
            return rs.getInt(1);
        else
           return -1;
    }

    /**
     * The student has selected a class.  Now generate a page that tells the user his username and
     * gives a button to return to login
     * @param req
     * @param conn
     * @param e
     * @return
     * @throws Exception
     */
    private View processClassInfo(HttpServletRequest req, HttpServletResponse resp, Connection conn, UserRegistrationClassSelectionEvent e) throws Exception {

//        String uri = ServletURI.getURI(req);
        DbUser.updateStudentClass(conn, e.getStudId(), e.getIntClassId());
        User stud = DbUser.getStudent(conn,e.getStudId());
        ClassInfo classInfo = DbClass.getClass(conn,e.getIntClassId());
        // Now that the student is in a class, he is assigned a strategy or a pedagogy from one
        // that the class uses.
        TutorStrategy strat = StrategyAssigner.assignStrategy(conn,e.getStudId(),e.getIntClassId());
        if (strat == null) {
            int pedId = PedagogyAssigner.assignPedagogy(conn, e.getStudId(), e.getIntClassId());
            // store the pedagogy id in the student table row for this user.
            DbUser.setStudentPedagogy(conn, e.getStudId(), pedId);
        }
        req.setAttribute("userName", stud.getUname());
        req.setAttribute("fname", stud.getFname());
        req.setAttribute("lname", stud.getLname());
        req.setAttribute("teacher", classInfo.getTeacherName());
        req.setAttribute("className", classInfo.getName());
        req.setAttribute("startPage",e.getStartPage());
        RequestDispatcher disp = req.getRequestDispatcher(Settings.useNewGUI()
                ? "login/userRegComplete_new.jsp"
                : UserRegistrationHandler.REGISTER3);
        disp.forward(req,resp);
        return null;
    }


    /**
     * Given the column info for a UserPropVal create a new row.  This is the value for a given property
     * of a given student.
     */
    private void updateStudPropVal(Connection conn, int studId, Integer pid, String pname, String val) throws Exception {
        String q = "insert into UserPropVal (propId, studId,value) values (?,?,?)";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setInt(1, pid.intValue());
        ps.setInt(2, studId);
        ps.setString(3, val);
        ps.execute();
    }

    /**
     * At this point we are getting a bunch of properties but we don't know what
     * params they are.  We have to go to the database tables UserProps for the given
     * PropGroup that this students class is assigned to and get all the property names.
     * Using these names, we extract them from the param list and save the associated value
     * into the StudPropValues table
     */
    //todo: put these variables somewhere more appropriate
    private static final String GROUP = "problemSequenceGroup";
    private static final int DEFAULT_GROUP = 18;
    private static int currentGroup = DEFAULT_GROUP;

    private View processPropertyInfo(HttpServletRequest req, Connection conn, UserRegistrationMoreInfoEvent e) throws Exception {
        Variables v = new Variables(req.getServerName(),
                req.getServletPath(),
                req.getServerPort());
        String url = ServletURI.getURI(req);
        int studId = e.getStudId();
        int classId = e.getIntClassId();

        List propNames = getUserProperties(conn, classId);  // returns a list of tuples <id, internalPropName, externalPropName>
        Iterator iter = propNames.iterator();
        while (iter.hasNext()) {
            ThreeTuple tt = (ThreeTuple) iter.next();
            String ipname = (String) tt.getSecond();  // the property name
            Integer pid = (Integer) tt.getFirst();  // the property id
            String pval = e.getServletParams().getString(ipname); // extract the property value HTML form inputs (held in servlet params)

            //make sure group is valid
            pval = validateGroupParam(ipname, pval);
            updateStudPropVal(conn, studId, pid, ipname, pval);
        }
        return new UserRegistrationCompletePage(url, e);
    }

    private String validateGroupParam(String ipname, String pval) {
        if (ipname.equals(this.GROUP)){
            boolean validGroup = false;
            try{
                int group = Integer.parseInt(pval);
                if (groupIsValid(group)){
                    validGroup = true;
                }
            }
            catch(Exception ex){ }
            //else give a valid group
            if (!validGroup){
                pval = Integer.toString(UserRegistrationHandler.currentGroup);
                UserRegistrationHandler.currentGroup++;
                if (!groupIsValid(UserRegistrationHandler.currentGroup)){
                    UserRegistrationHandler.currentGroup = UserRegistrationHandler.DEFAULT_GROUP;
                }
            }
        }
        return pval;
    }


    private boolean groupIsValid(int val){
        //allows ld group
        //return (val >=0 && val <=4) || (val >=10 && val <=17);
        return (val >=15 && val <=20);
    }


    public static void setUserTestProperty(Connection conn, int studId, User.UserType ut) throws SQLException {
        boolean[] flags = User.getUserTypeFlags(ut);
        boolean updateStats = flags[2];
        boolean showTestControls = flags[3];
        boolean isTrialUser = User.isTrialUser(ut);
        PreparedStatement stmt = null;
        try {
            String q = "update Student set updateStats=?, showTestControls=?,trialUser=? where id=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, updateStats?1:0);
            stmt.setInt(2, showTestControls?1:0);
            stmt.setInt(3,isTrialUser?1:0);
            stmt.setInt(4,studId);
            stmt.executeUpdate();
        } finally {
            if (stmt != null)
                stmt.close();
        }


    }
}