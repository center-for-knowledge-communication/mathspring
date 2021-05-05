package edu.umass.ckc.wo.ttmain.ttmodel.datamapper;

import edu.umass.ckc.wo.ttmain.ttmodel.TeacherClassListEntry;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Frank 	03-02-2020	Issue #45 teacher selection list
 */
public class TeacherClassListEntryMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
    	TeacherClassListEntry teacherClassListEntry = new TeacherClassListEntry();
    	teacherClassListEntry.setTeacherId(resultSet.getString("teacherId"));
    	teacherClassListEntry.setTeacherName(resultSet.getString("teacherName"));
    	teacherClassListEntry.setClassId (resultSet.getString("classId"));
    	teacherClassListEntry.setClassName(resultSet.getString("className"));
        return teacherClassListEntry;
    }
}
