package edu.umass.ckc.wo.woreports;


import java.sql.Connection;

//import edu.umass.ckc.wo.event.admin.AdminActions;
//import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;
import edu.umass.ckc.wo.event.admin.AdminViewReportEvent;

import ckc.servlet.servbase.View;
import edu.umass.ckc.wo.util.ProbPlayer;
import edu.umass.ckc.wo.beans.ClassInfo;
import edu.umass.ckc.wo.woreports.js.CSSFile;
import edu.umass.ckc.wo.woreports.js.JSFile;
import edu.umass.ckc.wo.woreports.js.JSFunction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class Report implements View {

    protected StringBuffer src = new StringBuffer(512); // the resulting report is built up in this String
    protected static String gifURL = "images/reports/";
    protected static final String SORT_URL = "TableSort.js";
    protected static final String WORKING_SORT_URL = "table.js";
    protected static final String SLIDER_URL = "slider.js";
    protected static final String TABLE_CSS = "table.css";

    protected static final String foot = "</tbody>\n</table></body></html>";
    public static final int EPI_DATA2_LOG = 1;
    public static final int EVENT_LOG = 2;

    /**
     * All subclass must override this to return a View that is some HTML.  If not, then it is assumed that a null View means
     * the Report forwarded to a JSP.
     * @param conn
     * @param classId
     * @param e
     * @param req
     * @param response
     * @return
     * @throws Exception
     */
    public abstract View createReport(Connection conn, int classId, AdminViewReportEvent e, HttpServletRequest req, HttpServletResponse response) throws Exception ;


    protected String doubleToString(double d) {
        String str = Double.toString(d);
        int decimalPointAt = str.indexOf(".");
        if (decimalPointAt == -1)
            return str;
        else
            return str.substring(0, decimalPointAt + 2);
    }

    protected static String generateHeader(String title) {
    	String header = "<html>\n" +
		    			"<head>\n" +
		                "<title>" + title + "</title>\n" +
		                "<script src=\"" + SORT_URL + "\"></script>\n" +
		                "</head>\n" +
		                "\n" +
		                "<body onLoad='readTable(\"data\");'>";
        return header;
    }

    protected static String generateHeaderWithJS(String title, JSFile[] scripts, JSFunction[] fns, CSSFile[] cssfiles) {
    	String header = "<html>\n" +
		    			"<head>\n" +
		                "<title>" + title + "</title>\n";
        StringBuilder sb = new StringBuilder();
        sb.append("<style type=\"text/css\" title=\"currentStyle\">\n");
        // first put in CSS imports
        for (CSSFile css: cssfiles)
            sb.append(String.format("@import \"%s\";\n",css.getFile()));
        sb.append("</style>\n");
        // next put in Javacript includes
        for (JSFile script: scripts) {
            sb.append(String.format("<script type=\"text/javascript\" language=\"JavaScript\" src=\"%s\"></script>\n", script.getFile()));
        }
        // finally stick in the javascript functions
        sb.append("<script type=\"text/javascript\" charset=\"utf-8\">\n");
        for (JSFunction fn: fns) {
            sb.append(fn.getFunction() + "\n");
        }
        sb.append("</script>\n");
        sb.append("</head>\n");
        return header + sb.toString();
    }



    protected static String generateHeaderWithJS(String title, JSFile[] scripts, JSFunction[] fns) {
    	String header = "<html>\n" +
		    			"<head>\n" +
		                "<title>" + title + "</title>\n";
        StringBuilder sb = new StringBuilder();
        for (JSFile script: scripts) {
            sb.append(String.format("<script type=\"text/javascript\" language=\"JavaScript\" src=\"%s\"/>\n", script.getFile()));
        }
        sb.append("<script type=\"text/javascript\" charset=\"utf-8\">\n");
        for (JSFunction fn: fns) {
            sb.append(fn.getFunction() + "\n");
        }
        sb.append("</script>\n");
        String tail = "</head>\n";
        return header + sb.toString() + tail;
    }

    protected static String generateHeader2(String title) {
    	String header = "<html>\n" +
		    			"<head>\n" +
		                "<title>" + title + "</title>\n" +
		                "<script src=\"" + WORKING_SORT_URL + "\"></script>\n" +
                        "<script language=\"JavaScript\" src=\"slider.js\"></script>" +

                        "<link rel=\"stylesheet\" type=\"text/css\" href=\"table.css\" media=\"all\">\n" +
                        "</head>\n" +
		                "\n" +
		                "<body onLoad='readTable(\"data\");'>";
        return header;
    }

    protected static String generateHeader3(String title) {
    	String header = "<html>\n" +
		    			"<head>\n" +
		                "<title>" + title + "</title>\n" +
                "<style type=\"text/css\">\n" +
                "<!--\n" +
                "\n" +
                ".a1 {\n" +
                "\tcolor: #000000;\n" +
                "\ttext-decoration: none;\n" +
                "}\n" +
                "-->\n" +
                "</style>"  +
                        "<script src=\"" + WORKING_SORT_URL + "\"></script>\n" +
                        "<script language=\"JavaScript\" src=\"slider.js\"></script>" +

                        "<link rel=\"stylesheet\" type=\"text/css\" href=\"table.css\" media=\"all\">\n" +
                        "</head>\n" +
		                "\n" +
		                "<body onLoad='readTable(\"data\");'>";
        return header;
    }

    protected static String getLinkURL(String gifName, HttpServletRequest req) {

        String probnumber = gifName.substring(8) ;
        String link = ProbPlayer.getURLToProbPreviewer()+ "?questionNum=" + probnumber ;
    	return link;
    }

    public String getView () { return src.toString(); }

    protected String getClassName(ClassInfo cl) {
        String className = "";

        if (cl != null)
            className = cl.getSchool() + " - " + cl.getName();
        else
            className = "some class";
        return className;
    }

    public String getEventLogTable (ClassInfo cl) {
        return (cl.isNewLog()) ?  "EventLog" : "EpisodicData2";
       
    }

    public String getPrePostEventLogTable (ClassInfo cl) {
        return (cl.isNewLog()) ?  "PrePostEventLog" : "EpisodicData2";
    }

    public void addNavLinks (int classId, int teacherId) {
        this.src.append("<h5>" +
                "<a href='?action=" + "AdminViewReport" + "&teacherId=" + teacherId + "&classId=" + classId + "'>Choose another report</a>" +
                "</h5>\n");
    }

    // Return an href to a report
    protected String getLinkToReport (String action, int teacherId, int classId, int reportId, String state) {
        return String.format("WoAdmin?action=%s&teacherId=%d&classId=%d&reportId=%d&state=%s",action,teacherId,classId,reportId,state);
    }

    // Return an href to a report
    protected String getLinkToStudentReport(String action, int teacherId, int classId, int studId, int reportId, String state, String extra) {
        if (extra != null)
            return String.format("WoAdmin?action=%s&teacherId=%d&classId=%d&studId=%d&reportId=%d&state=%s&extraParam=%s",
                    action,teacherId,classId,studId,reportId,state,extra);
        else return String.format("WoAdmin?action=%s&teacherId=%d&classId=%d&studId=%d&reportId=%d&state=%s",
                    action,teacherId,classId,studId,reportId,state);
    }


}//class