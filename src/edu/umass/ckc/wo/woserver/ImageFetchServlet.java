package edu.umass.ckc.wo.woserver;

import edu.umass.ckc.servlet.servbase.BaseServlet;
import edu.umass.ckc.servlet.servbase.ServletParams;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by david on 8/25/2016.
 * A general-purpose fetcher of images that are stored as BLOBs in the db.   The request for the image gives
 * 1.  The table name
 * 2.  The field name
 * 3.  The ID field for the row.  (We are assuming that the primary key is a field called ID that is an int)
 *
 * The servlet just sends back the image binary as fetched from this db location
 */
public class ImageFetchServlet extends BaseServlet {

    protected void initialize(ServletConfig servletConfig, ServletContext servletContext, Connection connection) throws Exception {
    }

    public String getDataSource(ServletConfig servletConfig) {
        return servletConfig.getServletContext().getInitParameter("wodb.datasource");
    }

    protected boolean handleRequest(ServletContext servletContext, Connection conn, HttpServletRequest request,
                                    HttpServletResponse response, ServletParams params, StringBuffer servletOutput) throws Exception {
        String tableName = params.getMandatoryString("table");
        String columnName = params.getMandatoryString("column");
        ServletOutputStream out = response.getOutputStream();
        int id = params.getMandatoryInt("id");
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "select " + columnName + " from "  + tableName + "  where id=?";
            stmt = conn.prepareStatement(q);

            stmt.setInt(1,id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                Blob photo = rs.getBlob(1);
                response.setContentType("image/jpeg");
                InputStream in = photo.getBinaryStream();
                int length = (int) photo.length();

                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];

                while ((length = in.read(buffer)) != -1) {
                    out.write(buffer, 0, length);
                }

                in.close();
                out.flush();
            }
            else {
                response.setStatus(404);
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            e.printStackTrace(response.getWriter());
            response.setStatus(404);
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
        return false;  // tells the calling superclass that this doesn't need to be written into the servlet output stream.
    }
}
