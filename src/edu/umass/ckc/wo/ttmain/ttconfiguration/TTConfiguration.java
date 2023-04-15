package edu.umass.ckc.wo.ttmain.ttconfiguration;

import edu.umass.ckc.wo.ttmain.ttconfiguration.errorCodes.ErrorCodeMessageConstants;
import edu.umass.ckc.wo.ttmain.ttconfiguration.errorCodes.TTCustomException;
import edu.umass.ckc.wo.tutor.Settings;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jndi.JndiTemplate;
import org.springframework.web.context.ServletContextAware;

import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Neeraj on 3/24/2017.
 */
@Configuration
public class TTConfiguration implements ServletContextAware {

    private ServletContext servletContext;
    private DataSource dataSource;
    private static Logger logger =   Logger.getLogger(TTConfiguration.class);

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext =  servletContext;
    }

    @Bean
    public Connection getConnection() throws TTCustomException {
        try {
            JndiTemplate jndiTemplate = new JndiTemplate();
            String dataSourceLookup = this.servletContext.getInitParameter("wodb.datasource");
            Settings.webContentPath = this.servletContext.getInitParameter("webContentPath");
            Settings.webContentPath2 = this.servletContext.getInitParameter("webContentPath2");
            DataSource ds = (DataSource) jndiTemplate.lookup("java:comp/env/" + dataSourceLookup);
            return ds.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new TTCustomException(ErrorCodeMessageConstants.DATABASE_CONNECTION_FAILED);
        }
    }

    @Bean
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() throws TTCustomException {
        try {
        JndiTemplate jndiTemplate = new JndiTemplate();
        String dataSourceLookup = this.servletContext.getInitParameter("wodb.datasource");
        DataSource ds = (DataSource) jndiTemplate.lookup("java:comp/env/"+dataSourceLookup);
            return new NamedParameterJdbcTemplate(ds);
        } catch (NamingException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new TTCustomException(ErrorCodeMessageConstants.DATABASE_CONNECTION_FAILED);
        }

    }
}
