package edu.umass.ckc.wo.tutormeta;

import net.sf.json.JSONObject;

/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Aug 17, 2005
 * Time: 2:20:08 PM
 */
public interface Activity {

//
//    String getXML ();

    JSONObject buildJSON(JSONObject jo);

//    JSONObject getJSONObject();

//    String getFlashOut();

    String logEventName ();
}
