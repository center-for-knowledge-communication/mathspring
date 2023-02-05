package edu.umass.ckc.wo.ttmain.ttmodel.datamapper;

import edu.umass.ckc.wo.ttmain.ttmodel.ClassClusterListEntry;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Frank 	01-15-2023	Issue #723 class cluster selection list
 */
public class ClassClusterListEntryMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
    	ClassClusterListEntry classClusterListEntry = new ClassClusterListEntry();
    	classClusterListEntry.setClassId (resultSet.getString("classId"));
    	classClusterListEntry.setClassName(resultSet.getString("className"));
    	classClusterListEntry.setClassHasClusters(resultSet.getString("hasClusters"));
    	classClusterListEntry.setClassIsCluster(resultSet.getString("isCluster"));
    	classClusterListEntry.setColor(resultSet.getString("color"));
    	return classClusterListEntry;
    }
}
