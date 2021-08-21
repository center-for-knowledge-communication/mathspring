package edu.umass.ckc.wo.ttmain.ttmodel.datamapper;

import edu.umass.ckc.wo.ttmain.ttmodel.ClassLiveDashboard;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Frank 	03-02-2020	Issue #45 teacher selection list
 */
public class ClassLiveDashboardMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
    	ClassLiveDashboard classLiveDashboard = new ClassLiveDashboard();
    	classLiveDashboard.setProblemsSolved(resultSet.getString("problemsSolved"));
        return classLiveDashboard;
    }
}
