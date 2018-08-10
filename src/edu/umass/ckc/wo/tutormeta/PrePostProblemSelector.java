package edu.umass.ckc.wo.tutormeta;

import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.smgr.SessionManager;
import ckc.servlet.servbase.ActionEvent;

import java.sql.SQLException;


/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: May 2, 2006
 * Time: 2:22:34 PM
 */
public interface PrePostProblemSelector {
    public void pretestComplete() throws SQLException;
    public void posttestComplete() throws SQLException;


    public void init(SessionManager smgr) throws Exception ;
    public Problem selectProblem(ActionEvent e, SessionManager smgr, StudentModel studentModel) throws Exception ;

}
