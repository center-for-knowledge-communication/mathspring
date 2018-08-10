package edu.umass.ckc.wo.lc;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 5/26/16
 * Time: 8:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class LCMessage {
    private int id;
    private String text;
    private String media;

    public LCMessage(int id, String text, String media) {
        this.id = id;
        this.text = text;
        this.media = media;
    }

    public String getText() {
        return text;
    }

    public String getMedia() {
        return media;
    }
}
