package edu.umass.ckc.wo.ttmain.ttmodel.datamapper;

import edu.umass.ckc.wo.ttmain.ttmodel.ClassLandingReportStudents;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by nsmenon on 5/20/2017.
 */
public class ClassLandingReportStudentsMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        ClassLandingReportStudents classLandingReportStudents = new ClassLandingReportStudents();
        classLandingReportStudents.setStudentId(resultSet.getString("studentId"));
        classLandingReportStudents.setStudentName(resultSet.getString("studentName"));
        classLandingReportStudents.setUserName(resultSet.getString("userName"));
        classLandingReportStudents.setNoOfProblems(resultSet.getString("noOfProblems"));
        return classLandingReportStudents;
    }
}
