package edu.umass.ckc.wo.event.admin;

import edu.umass.ckc.servlet.servbase.ServletParams;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: 9/14/11
 * Time: 1:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdminAlterClassOtherConfigSubmitInfoEvent extends AdminClassEvent {


    public static final String TEACHER_EMAIL_INTERVAL = "teacherEmailInterval";
    public static final String STUDENT_EMAIL_INTERVAL = "studentEmailInterval";
    public static final String TEACHER_REPORT_PERIOD = "teacherReportPeriod";
    public static final String STUDENT_REPORT_PERIOD = "studentReportPeriod";

    public int teacherEmailInterval;
    public int studentEmailInterval;
    public int teacherReportPeriod;
    public int studentReportPeriod;



    public AdminAlterClassOtherConfigSubmitInfoEvent(ServletParams p) throws Exception {
        super(p);

       teacherEmailInterval=p.getInt(TEACHER_EMAIL_INTERVAL);
       studentEmailInterval=p.getInt(STUDENT_EMAIL_INTERVAL);
       teacherReportPeriod=p.getInt(TEACHER_REPORT_PERIOD);
       studentReportPeriod=p.getInt(STUDENT_REPORT_PERIOD);
    }


}
