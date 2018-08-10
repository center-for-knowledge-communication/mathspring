package edu.umass.ckc.wo.beans;

import edu.umass.ckc.wo.beans.ClassInfo;

/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Jan 23, 2008
 * Time: 3:37:25 PM
 */
public class Classes {
    private ClassInfo[] classes;

    public Classes(ClassInfo[] classes) {
        this.classes = classes;
    }

    public ClassInfo[] getClasses() {
        return classes;
    }

}
