package edu.umass.ckc.wo.ttmain.ttservice.miscservice;

import edu.umass.ckc.wo.ttmain.ttconfiguration.errorCodes.TTCustomException;
import edu.umass.ckc.wo.ttmain.ttservice.miscservice.impl.TTMiscServiceImpl;

/**
 * Created by Neeraj on 3/24/2017.
 * Frank 	06-17-20	Issue #149
 */
public interface TTMiscService {
	
    public void test(String msg);
    public String getCohortTeachersClasses(String cohortId, String teacherId, String classId, String reportType, String lang, String filter) throws TTCustomException;
    public String getCohorts(String reportType, String lang, String filter) throws TTCustomException;

    public String getCohortReport(String cohortId, String reportType, String lang, String filter) throws TTCustomException;
    public String cohortAdmin(String cohortId, String reportType, String lang, String filter) throws TTCustomException;
    
}