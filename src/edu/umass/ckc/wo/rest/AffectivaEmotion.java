package edu.umass.ckc.wo.rest;


import net.sf.json.JSONObject;

/**
 * Created by david on 10/5/2016.
 */
public class AffectivaEmotion {
    private String name;
    private double value;

    public AffectivaEmotion(String name, double value) {
        this.name = name;
        this.value = value;
    }

    public AffectivaEmotion() {
    }

    public static AffectivaEmotion createFromJSON (JSONObject o) {
        AffectivaEmotion e = new AffectivaEmotion();
        e.setName(o.getString("name"));
        e.setValue(o.getDouble("value"));
        return e;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String toString () {
        return "[" + this.name + ": " + this.value + "]";
    }
}
