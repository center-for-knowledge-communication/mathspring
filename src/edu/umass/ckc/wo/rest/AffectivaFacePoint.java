package edu.umass.ckc.wo.rest;


import net.sf.json.JSONObject;

/**
 * Created by david on 10/5/2016.
 */
public class AffectivaFacePoint {

    private String locationId;
    private int x;
    private int y;

    public AffectivaFacePoint() {
    }

    public AffectivaFacePoint(String locationId, int x, int y) {
        this.locationId = locationId;
        this.x = x;
        this.y = y;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String toString () {
        return "[" + this.getLocationId() + ": (" + this.getX() + ", " + this.getY() + ")]";
    }

    public static AffectivaFacePoint createFromJSON(JSONObject object) {
        AffectivaFacePoint fp = new AffectivaFacePoint();
        fp.setLocationId(object.getString("locationId"));
        fp.setX(object.getInt("x"));
        fp.setY(object.getInt("y"));
        return fp;
    }
}
