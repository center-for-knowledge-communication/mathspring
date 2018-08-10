package edu.umass.ckc.wo.woreports.js;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: Dec 9, 2011
 * Time: 2:41:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class JSFile {
    private String filename;

    public JSFile(String filename) {
        this.filename = filename;
    }

    public String getFile () {
        return this.filename;
    }

    public static final JSFile JQUERY = new JSFile("js/jquery.js");

    
}
