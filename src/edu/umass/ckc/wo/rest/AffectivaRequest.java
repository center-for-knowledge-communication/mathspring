package edu.umass.ckc.wo.rest;

import java.util.List;

/**
 * Created by david on 10/5/2016.
 */
public class AffectivaRequest {
    private List<AffectivaEmotion> emotions;
    private List<AffectivaFacePoint> faceDataPoints;

    public List<AffectivaEmotion> getEmotions() {
        return emotions;
    }

    public void setEmotionVals(List<AffectivaEmotion> emotions) {
        this.emotions = emotions;
    }

    public List<AffectivaFacePoint> getFaceDataPoints() {
        return faceDataPoints;
    }

    public void setFaceDataPoints(List<AffectivaFacePoint> faceDataPoints) {
        this.faceDataPoints = faceDataPoints;
    }



    @Override
    public String toString() {
        return "AffectivaRequest [nEmotions=" + this.emotions.size() + " nPoints=" + this.faceDataPoints.size() + "]";
    }
}
