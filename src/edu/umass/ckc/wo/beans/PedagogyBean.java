package edu.umass.ckc.wo.beans;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: Mar 17, 2009
 * Time: 10:06:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class PedagogyBean {
    private int id;
    private String description;
    private boolean selected;

    public PedagogyBean(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public PedagogyBean() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
