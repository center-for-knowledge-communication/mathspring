package edu.umass.ckc.wo.event.admin;

import ckc.servlet.servbase.ServletParams;


/**
 *
 */
public class AdminAlterClassSubmitActivatedHutsEvent extends AdminClassEvent {
    private boolean pretest;
    private boolean posttest;
    private boolean tutor;
    private boolean mfr;
    private boolean mr;
    private boolean adv;
    private boolean restoreDefaults;

  public AdminAlterClassSubmitActivatedHutsEvent(ServletParams p) throws Exception {
    super(p);
      String s = p.getString("restoreDefaults",null);
      if (s == null) {
          restoreDefaults = false;
          s = p.getString("useDefaultActivationRules",null);
          restoreDefaults = s != null;
          if (restoreDefaults)
              return;
          s = p.getString("pretest",null);
          pretest = s != null;
          s = p.getString("posttest",null);
          posttest = s != null;
          s = p.getString("tutor",null);
          tutor = s != null;
          s = p.getString("mfr",null);
          mfr = s != null;
          s = p.getString("mr",null);
          mr = s != null;
          s = p.getString("adventures",null);
          adv = s != null;
      }
      else {
          restoreDefaults = true;
      }

  }



    public boolean isPretest() {
        return pretest;
    }

    public boolean isPosttest() {
        return posttest;
    }

    public boolean isTutor() {
        return tutor;
    }

    public boolean isMfr() {
        return mfr;
    }

    public boolean isMr() {
        return mr;
    }

    public boolean isAdv() {
        return adv;
    }

    public boolean isRestoreDefaults() {
        return restoreDefaults;
    }
}