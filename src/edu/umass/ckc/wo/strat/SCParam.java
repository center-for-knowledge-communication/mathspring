package edu.umass.ckc.wo.strat;

/**
 * Created by marshall on 6/16/17.
 */
public class SCParam {
    private int id;
    private String name;
    private String value;

    public SCParam(int id, String name, String value) {
        this.id=id;
        this.name=name;
        this.value=value;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String toString () {
        return "SCParam: " + id + " " + name + "=" + value;
    }
}
