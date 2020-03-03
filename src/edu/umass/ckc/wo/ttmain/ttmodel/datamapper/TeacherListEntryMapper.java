package edu.umass.ckc.wo.ttmain.ttmodel.datamapper;

import edu.umass.ckc.wo.ttmain.ttmodel.TeacherListEntry;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Frank 	03-02-2020	Issue #45 teacher selection list
 */
public class TeacherListEntryMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
    	TeacherListEntry teacherLogEntry = new TeacherListEntry();
    	teacherLogEntry.setTeacherId(resultSet.getString("teacherId"));
    	teacherLogEntry.setUserName(resultSet.getString("userName"));
        return teacherLogEntry;
    }
}
