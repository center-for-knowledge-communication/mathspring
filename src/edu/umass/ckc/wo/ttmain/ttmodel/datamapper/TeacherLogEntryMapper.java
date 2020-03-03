package edu.umass.ckc.wo.ttmain.ttmodel.datamapper;

import edu.umass.ckc.wo.ttmain.ttmodel.TeacherLogEntry;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Frank 	01-14-20	Issue #45 & #21
 * Frank    03-02-2020  fix - use sql timestamp data type 
 */
public class TeacherLogEntryMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
    	TeacherLogEntry teacherLogEntry = new TeacherLogEntry();
    	teacherLogEntry.setTeacherId(resultSet.getString("teacherId"));
    	teacherLogEntry.setTeacherName(resultSet.getString("teacherName"));
    	teacherLogEntry.setUserName(resultSet.getString("userName"));
    	teacherLogEntry.setAction(resultSet.getString("action"));
    	teacherLogEntry.setActivityName(resultSet.getString("activityName"));
    	teacherLogEntry.setTimestamp(resultSet.getTimestamp("timestamp"));
        return teacherLogEntry;
    }
}
