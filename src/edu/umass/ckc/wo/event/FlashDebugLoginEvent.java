package edu.umass.ckc.wo.event;
import edu.umass.ckc.servlet.servbase.ActionEvent;
import edu.umass.ckc.servlet.servbase.ServletParams;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: 9/14/12
 * Time: 1:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class FlashDebugLoginEvent extends ActionEvent {
    private String uname;
    private String password;
    private String loginTime;
    private String flashClient;

    public FlashDebugLoginEvent(ServletParams p) throws Exception {
        super(p);
        setUname(p.getString("uname"));
        setPassword(p.getString("password"));
        setLoginTime(p.getString("loginTime"));
        setFlashClient(p.getString("flashClient","debug_WoLogin_dm"));

    }

    public String getFlashClient() {
        return flashClient;
    }

    public void setFlashClient(String flashClient) {
        this.flashClient = flashClient;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
