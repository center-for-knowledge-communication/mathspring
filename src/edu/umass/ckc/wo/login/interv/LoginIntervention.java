package edu.umass.ckc.wo.login.interv;

import java.util.Map;

import edu.umass.ckc.wo.tutormeta.Intervention;
import net.sf.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 4/27/15
 * Time: 10:00 AM
 * To change this template use File | Settings | File Templates.
 */
public class LoginIntervention implements Intervention {
    private String view;  // name of JSP
    private String url;   // URL which will be shown in a separate browser window   -- DM 8/16 not seeing evidence of this being used
    private Map<Integer, String> LCprofile;
    private boolean separateWindow =false; // tells whether to display the URL in a separate window      -- DM 8/16 not seeing evidence of this being used
    private boolean topLevel = false; // indicates if the JSP should be within an outer JSP or not

    public LoginIntervention(String view, String url, boolean separateWindow) {
        this.view = view;
        this.url = url;
        this.separateWindow =separateWindow;
        this.topLevel = false;
    }

    public LoginIntervention(String view) {
        this(view,null,false);

    }

    public LoginIntervention(String view, boolean isTopLevel) {
        this(view,null,false);
        this.topLevel = isTopLevel;
    }

    public String getView () {
        return this.view;
    }

    public boolean isTopLevel () {
        return this.topLevel;
    }

    public boolean openInSeparateWindow () {
        return this.separateWindow;
    }

    public String getURL () {
        return this.url;
    }
        
    public Map<Integer, String> getLCprofile() {
		return LCprofile;
	}

	public void setLCprofile(Map<Integer, String> lcprofile) {
		LCprofile = lcprofile;
	}

	@Override
    public String getName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
	
	@Override
    public int getId() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getResource() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public JSONObject buildJSON(JSONObject jo) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String logEventName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
