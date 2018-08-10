package edu.umass.ckc.wo.beans;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: Mar 9, 2009
 * Time: 11:08:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class PretestPool implements Serializable {
    private int id;
    private String description;

     public PretestPool() {}

    public PretestPool(int id, String descr) {
        this.id = id;
        this.description = descr;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
