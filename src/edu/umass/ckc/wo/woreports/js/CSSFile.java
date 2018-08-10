package edu.umass.ckc.wo.woreports.js;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: Dec 9, 2011
 * Time: 2:41:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class CSSFile {
    private String filename;

    public CSSFile(String filename) {
        this.filename = filename;
    }

    public String getFile () {
        return this.filename;
    }
}