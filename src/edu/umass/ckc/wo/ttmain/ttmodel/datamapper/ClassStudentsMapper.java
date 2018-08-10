package edu.umass.ckc.wo.ttmain.ttmodel.datamapper;

import edu.umass.ckc.wo.ttmain.ttmodel.ClassStudents;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by nsmenon on 5/20/2017.
 */
public class ClassStudentsMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        ClassStudents classStudents = new ClassStudents();
        classStudents.setStudentId(resultSet.getString("studentId"));
        classStudents.setStudentName(resultSet.getString("studentName"));
        classStudents.setUserName(resultSet.getString("userName"));
        classStudents.setNoOfProblems(resultSet.getString("noOfProblems"));
        return classStudents;
    }
}
