package edu.umass.ckc.wo.ttmain.ttmodel.datamapper;

import edu.umass.ckc.wo.ttmain.ttmodel.ClassLandingReportEvents;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by nsmenon on 5/20/2017.
 */
public class ClassLandingReportEventsMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        ClassLandingReportEvents classLandingReportEvents = new ClassLandingReportEvents();
        classLandingReportEvents.setStudentId(resultSet.getString("studentId"));
        classLandingReportEvents.setAction(resultSet.getString("action"));
        classLandingReportEvents.setProbElapsed(resultSet.getString("probElapsed"));
        classLandingReportEvents.setClickTime(resultSet.getTimestamp("clickTime"));
        return classLandingReportEvents;
    }
}
