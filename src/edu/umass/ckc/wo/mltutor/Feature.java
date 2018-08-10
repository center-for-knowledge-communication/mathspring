package edu.umass.ckc.wo.mltutor;

/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Jul 13, 2005
 * Time: 11:22:49 AM
 */
public class Feature {
    private String name;
    private Object value;

    public Feature(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
