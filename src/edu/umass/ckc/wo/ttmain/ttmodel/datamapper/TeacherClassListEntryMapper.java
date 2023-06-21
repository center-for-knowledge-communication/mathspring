package edu.umass.ckc.wo.ttmain.ttmodel.datamapper;

import edu.umass.ckc.wo.ttmain.ttmodel.TeacherClassListEntry;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Frank 	03-02-2020	Issue #45 teacher selection list
 * Frank 	02-04-23    Issue #723 - handle class clusters 
 */
public class TeacherClassListEntryMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
    	TeacherClassListEntry teacherClassListEntry = new TeacherClassListEntry();
    	teacherClassListEntry.setTeacherId(resultSet.getString("teacherId"));
    	teacherClassListEntry.setTeacherName(resultSet.getString("teacherName"));
    	teacherClassListEntry.setClassId (resultSet.getString("classId"));
    	teacherClassListEntry.setClassName(resultSet.getString("className"));
    	teacherClassListEntry.setClassHasClusters(resultSet.getString("hasClusters"));
    	teacherClassListEntry.setClassIsCluster(resultSet.getString("isCluster"));
    	teacherClassListEntry.setColor(resultSet.getString("color"));
        return teacherClassListEntry;
    }
}
