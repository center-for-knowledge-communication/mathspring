package edu.umass.ckc.wo.woreports.js;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: Dec 15, 2011
 * Time: 9:57:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class DataTable {
    public static JSFile DATATABLE_JS = new JSFile("js/dataTable/jquery.dataTables.js");

    public static CSSFile DATATABLE_PAGE_CSS = new CSSFile("js/dataTable/css/demo_page.css");
    public static CSSFile DATATABLE_TABLE_JUI_CSS = new CSSFile("js/dataTable/css/demo_table_jui.css");
    public static CSSFile DATATABLE_THEME_CSS = new CSSFile("js/dataTable/css/themes/jquery-ui-1.8.4.custom.css");

    public static JSFunction DATATABLE_JQUERY_DOCREADY_FUNCTION = new JSFunction("$(document).ready(function() {\n" +
            "    oTable = $('#example').dataTable({\n" +
            "        \"bJQueryUI\": true,\n" +
            "        \"sPaginationType\": \"full_numbers\"\n" +
            "    });\n" +
            "} );");

    public static String BODY_TAG = "<body id=\"dt_example\">\n ";
    public static String DIV1 = "<div id=\"wojcontainer\"> \n";
    public static String DIV2 = "<div id=\"wojdemo\">\n ";


}
