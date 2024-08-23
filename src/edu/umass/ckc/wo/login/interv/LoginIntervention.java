package edu.umass.ckc.wo.login.interv;

import java.util.List;
import java.util.Map;

import edu.umass.ckc.wo.tutormeta.Intervention;
import net.sf.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 4/27/15
 * Time: 10:00 AM
 * To change this template use File | Settings | File Templates.
 * 
 * Frank	05-13-23	Issue #763 make LCs selectable by class
 */
public class LoginIntervention implements Intervention {
    private String view;  // name of JSP
    private String url;   // URL which will be shown in a separate browser window   
    private String url2;   // URL which will be shown in a separate browser window   
    private String experiment;
    private Map<Integer, List<String>> LCprofile;
    private boolean separateWindow =false; // tells whether to display the URL in a separate window      -- DM 8/16 not seeing evidence of this being used
    private boolean topLevel = false; // indicates if the JSP should be within an outer JSP or not

    public LoginIntervention(String view, String url, boolean separateWindow) {
        this.view = view;
        this.url = url;
        this.url = url2;
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

    public String getURL2 () {
        return this.url2;
    }
    
    public String getExperiment () {
        return this.experiment;
    }

    public Map<Integer, List<String>> getLCprofile() {
		return LCprofile;
	}

	public void setLCprofile(Map<Integer, List<String>> lCprofile) {
		LCprofile = lCprofile;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setUrl2(String url) {
		this.url2 = url;
	}

	public void setExperiment(String experiment) {
		this.experiment = experiment;
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
