package edu.umass.ckc.wo.ttmain.ttconfiguration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Neeraj on 3/24/2017.
 */
public class TTDataSource {
    private DataSource dataSource;

    public TTDataSource(DataSource ds) {
        this.dataSource = ds;
    }

    public Connection getDataSource() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
                e.printStackTrace();
                //Need to configure Logger and Log the connection Error
        }
        return null;
    }
}
