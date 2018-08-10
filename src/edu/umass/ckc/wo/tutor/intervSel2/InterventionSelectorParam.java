package edu.umass.ckc.wo.tutor.intervSel2;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 2/10/14
 * Time: 11:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class InterventionSelectorParam {
    protected int id;
    protected String name;
    protected String value;

    public InterventionSelectorParam() {
    }


    public InterventionSelectorParam(String name, String value) {
        this.name = name;
        this.value = value;
    }


    public InterventionSelectorParam(int paramId, String n, String v) {
        this(n,v);
        this.id=paramId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String toString () {
        return "\t\t\tParam: " + id + " " + name + "=" + value;
    }
}
