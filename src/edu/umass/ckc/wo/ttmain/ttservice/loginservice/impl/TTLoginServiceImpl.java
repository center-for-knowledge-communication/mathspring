package edu.umass.ckc.wo.ttmain.ttservice.loginservice.impl;

import java.sql.Timestamp;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.sun.mail.smtp.SMTPTransport;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

import edu.umass.ckc.wo.beans.Teacher;
import edu.umass.ckc.email.Emailer;
import edu.umass.ckc.wo.beans.ClassInfo;
import edu.umass.ckc.wo.beans.Classes;
import edu.umass.ckc.wo.beans.Feedback;
import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.db.DbClass;
import edu.umass.ckc.wo.db.DbFeedback;
import edu.umass.ckc.wo.db.DbPedagogy;
import edu.umass.ckc.wo.db.DbTeacher;
import edu.umass.ckc.wo.login.LoginParams;
import edu.umass.ckc.wo.smgr.User;
import edu.umass.ckc.wo.ttmain.ttconfiguration.TTConfiguration;
import edu.umass.ckc.wo.ttmain.ttconfiguration.errorCodes.ErrorCodeMessageConstants;
import edu.umass.ckc.wo.ttmain.ttconfiguration.errorCodes.TTCustomException;
import edu.umass.ckc.wo.ttmain.ttmodel.CreateClassForm;
import edu.umass.ckc.wo.ttmain.ttservice.loginservice.TTLoginService;
import edu.umass.ckc.wo.tutor.Settings;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import edu.umass.ckc.wo.ttmain.ttservice.util.SendEM;

/**
 * Created by Neeraj on 3/25/2017.
 * Frank 	02-24-20	Issue #28
 * Frank	04-27-2020  Disable password update until email is working
 * Frank    05-12-2020  send password in email now working
 * Frank    05-24-2020  issue #130 Restore call to original emailer, now that it is working
 * Frank    05-29-2020  issue #28 re-work password reset
 * Frank 	06-18-2020	issue #135 added method sendHelpMessage()
 * Frank	07-08-20	issue #134 & #156 changed archive vs active class to include isClassActive test
 * Frank    07-13-20	issue #29 Change error handling of loginAssist and resetPassword
 * Frank    09-14-20	issue #237 added teacherPauseStudentUse to model
 * Frank	10-07-20	issue #267 removed School year as a determining factor for 'active' vs. 'archived' list entries
 * Frank 	11-12-20	issue #276 handle interface change for remembering type of login
 * Frank    11-28-20	issue #318 Sort Student - getClassStudentsByName(...)
 * Frank	02-14-21	issue #383R1 added logFeedback method
 * Frank	08-03-21	Issue 150 pass along the list of class name/id pairs
 */
@Service
public class TTLoginServiceImpl implements TTLoginService {
    private static Logger logger = Logger.getLogger(TTLoginServiceImpl.class);

    @Autowired
    private TTConfiguration connection;

    @Override
  public String loginAssist(String uname, String password) {
    	
    	String strTeacherId = "";
        try {
            strTeacherId = DbTeacher.getTeacherIdAsString(connection.getConnection(), uname, password);
            return strTeacherId;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return strTeacherId;
        }

    }

    @Override
    public String populateClassInfoForTeacher(ModelMap model, int teacherId) throws TTCustomException {
        try {

            if (!ProblemMgr.isLoaded()) {
                ProblemMgr.loadProbs(connection.getConnection());
                Settings.lessonMap = DbPedagogy.buildAllLessons(connection.getConnection());
                Settings.loginMap = DbPedagogy.buildAllLoginSequences(connection.getConnection());
                Settings.pedagogyGroups = DbPedagogy.buildAllPedagogies(connection.getConnection(), null);
            }
            ClassInfo[] classes = DbClass.getClasses(connection.getConnection(), teacherId);
            List<ClassInfo> classInfoList = new  ArrayList<>(Arrays.asList(classes));
//            Map<Boolean, List<ClassInfo>> groups = classInfoList.stream().collect(Collectors.partitioningBy(c -> ((c.getSchoolYear() == Year.now().getValue()) && (c.getIsActive() > 0))));                                           
            Map<Boolean, List<ClassInfo>> groups = classInfoList.stream().collect(Collectors.partitioningBy(c -> (c.getIsActive() > 0)));                                           
            List<ClassInfo> classInfoListLatest = groups.get(true);
            List<ClassInfo> classInfoListArchived = groups.get(false);
            Collections.reverse(classInfoListLatest);
            Classes classbean = new Classes(classInfoListLatest.toArray(new ClassInfo[classInfoListLatest.size()]));
            Classes classbeanArchived = new Classes(classInfoListArchived.toArray(new ClassInfo[classInfoListArchived.size()]));
            //String teacherName = DbTeacher.getTeacherName(connection.getConnection(), teacherId);
            Teacher teacher = DbTeacher.getTeacher(connection.getConnection(), teacherId);
            model.addAttribute("teacherName", teacher.getFname() + " " + teacher.getLname());
            model.addAttribute("teacherFname", teacher.getFname());
            model.addAttribute("teacherLname", teacher.getLname());
            model.addAttribute("teacherEmail", teacher.getEmail());
            model.addAttribute("teacherId", Integer.toString(teacherId));
            model.addAttribute("createClassForm", new CreateClassForm());
            model.addAttribute("teacherPauseStudentUse", Integer.toString(teacher.getPauseStudentUse()));
           
            JSONArray classNameIdArray = new JSONArray();
	        try {
	            if (classInfoListLatest.size() > 0) {
	            	classInfoListLatest.stream().forEach(element ->
	                {
	                    JSONObject nameIdJson = new JSONObject();
	                    nameIdJson.put("name", element.getName());
	                    nameIdJson.put("Id", element.getClassid());
	                    classNameIdArray.add(nameIdJson);                   
	                });
	            }
	            else {
                    JSONObject nameIdJson = new JSONObject();
                    nameIdJson.put("name", "dummyClass");
                    nameIdJson.put("Id", 0);
                    classNameIdArray.add(nameIdJson);                   	            	
	            }
	        } catch (JSONException e1) {
	                // TODO Auto-generated catch block
	              e1.printStackTrace();
	        }
            
           	String classNameIdArrayStr = classNameIdArray.toString();

            if (classes.length > 0) {
                int classId = classInfoList.get(0).getClassid();
                ClassInfo classInfo = DbClass.getClass(connection.getConnection(), classId);
                List<User> students = DbClass.getClassStudentsByName(connection.getConnection(), classId);
                model.addAttribute("students", students);
                model.addAttribute("classbean", classbean);
                model.addAttribute("classbeanArchived", classbeanArchived);
                model.addAttribute("classInfo", classInfo);
                model.addAttribute("noClass", false);
                model.addAttribute("classList", classes);
                model.addAttribute("classNameIdArrayStr", classNameIdArrayStr);
                return "teacherTools/teacherToolsMain";
            } else {
                model.addAttribute("classNameIdArrayStr", classNameIdArrayStr);
                model.addAttribute("noClass", true);
                return "teacherTools/teacherToolsMain";
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new TTCustomException(ErrorCodeMessageConstants.ERROR_OCCURED_ON_LOADING_TEACHER_DATA);
        }
    }

    @Override
    public int resetPassword(String uname, String email) {
        try {
        	int count = 0;
        	Teacher teacher = null;
        	if (uname.length() > 0) {
                teacher = DbTeacher.getTeacherFromUsername(connection.getConnection(), uname);
        	}
        	else {
        		count = DbTeacher.countEmailAddrs(connection.getConnection(),email);
        		if (count == 1 ) {
        			teacher = DbTeacher.getTeacherFromEmail(connection.getConnection(), email);
        			uname = teacher.getUserName();
        		}
        	}
        	
        	if (count > 1) {
        		return count;
        	}
            
            if (teacher == null) {
                return -1;
            }
            else {
                Random rand = new Random();
                int x = rand.nextInt(99999);
                String symbols = "!#$%";
                int mod = x % 3;
                String pw = uname.substring(0,2) + Integer.toString(x) + symbols.substring(mod,mod+1);
                logger.info(uname + pw);
                DbTeacher.modifyTeacherPassword(connection.getConnection(),uname,pw);       
                SendEM sender = new SendEM();
                sender.sendPassword(connection.getConnection(),"DoNotReply@mathspring.org", Settings.mailServer,uname,pw,teacher.getEmail());
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Override
    public int sendHelpMessage(String subject, String email, String helpmsg) {
        try {
        	String helpDeskEmail = Settings.getString(connection.getConnection(), "teacher_login_help_email");
            SendEM sender = new SendEM();
        	sender.sendHelpEmail(connection.getConnection(),helpDeskEmail, subject, email, helpmsg);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return 1;
        }
    }
    @Override
    public int logFeedback(String messageType, int teacherId, String objectId, String priority, String msg) {
        try {
//        	String helpDeskEmail = Settings.getString(connection.getConnection(), "teacher_login_help_email");
//          SendEM sender = new SendEM();
//        	sender.sendHelpEmail(connection.getConnection(),helpDeskEmail, severity, email, msg);
           	System.out.println(messageType);
           	System.out.println(String.valueOf(teacherId));
           	System.out.println(objectId);
           	System.out.println(priority);
           	System.out.println(msg);
           	
           	

           	if (msg.length() == 0) {
           		return(1);
           	}
           	
           	Timestamp timestamp = new Timestamp(System.currentTimeMillis());
           	Feedback f  = new Feedback(teacherId,messageType,priority,msg,objectId,timestamp);
           	DbFeedback.addFeedback(connection.getConnection(), f);
           	return 0;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return 2;
        }
    }
}
