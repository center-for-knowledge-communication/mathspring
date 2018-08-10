package edu.umass.ckc.wo.exc;

/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Oct 3, 2005
 * Time: 3:24:06 PM
 */
public class NoSessionException extends Exception {
    private int id=-1;

    public NoSessionException () {

    }

    public  NoSessionException (int id)  {
        this.id = id;
    }

    public String getMessage() {
        return "No session active " + ((id == -1) ? "" : Integer.toString(id));
    }
}
