package edu.umass.ckc.wo.tutormeta;

/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Aug 29, 2005
 * Time: 5:09:59 PM
 */
public interface Intervention extends Activity {

    public static final String INTERVENTION = "intervention"; // activity type passed to client


    /**
     * Each intervention has a unique name that identifies it.  This name is put in the transaction log (episodicData)
     * so that we can tell what intervention was given.  Note:  The string returned by the intervention can be as complex
     * as necessary to identify it + include any data parameters that are relevant in studying its effects later.
     * @return the name
     */
    public String getName () ;

    // for logging these activities are getting logged much like a problem.  Therefore they need to have an id that
    // uniquely identifies them
    public int getId();

     public String getResource();


}
