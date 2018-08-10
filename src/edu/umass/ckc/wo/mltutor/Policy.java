package edu.umass.ckc.wo.mltutor;

import java.util.HashMap;

import java.sql.Connection;

/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Jul 13, 2005
 * Time: 11:33:30 AM
 */
public interface Policy {

    public String[] init (String policyFilename) ;

    public WayangAction selectProblemDifficulty (Connection conn, HashMap vals, int timeStep);
    public WayangAction[] getAlternativeActions(WayangAction action);
}
