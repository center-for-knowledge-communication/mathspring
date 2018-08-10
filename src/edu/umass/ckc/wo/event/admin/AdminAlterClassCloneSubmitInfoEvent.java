package edu.umass.ckc.wo.event.admin;

import edu.umass.ckc.servlet.servbase.ServletParams;

/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Feb 11, 2008
 * Time: 11:15:05 AM
 */
public class AdminAlterClassCloneSubmitInfoEvent extends AdminClassEvent {

    private String className;
    private String section;
    private static final String CLASS_NAME = "className";
    private static final String SECTION = "section";



    public AdminAlterClassCloneSubmitInfoEvent(ServletParams p) throws Exception {
        super(p);
        this.className = (p.getString(CLASS_NAME));
        this.section = p.getString(SECTION);

    }


    public String getClassName() {
        return className;
    }

    public String getSection() {
        return section;
    }
}