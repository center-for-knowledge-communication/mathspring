package edu.umass.ckc.wo.tutor.response;

import edu.umass.ckc.wo.content.Hint;

/**
 * <p> Created by IntelliJ IDEA.
 * User: david
 * Date: Dec 29, 2008
 * Time: 10:59:25 AM
 */
public class ClickCharacterResponse extends Response {
    private String movieClip;
    private int clipId;

    public ClickCharacterResponse(String clip, int id) {
        this.movieClip = clip;
        this.clipId = id;
        buildJSON();
    }

    public String getMovieClip() {
        return movieClip;
    }

    public int getClipId() {
        return clipId;
    }

    public String logEventName() {
           return "ClickCharacter";
    }

}