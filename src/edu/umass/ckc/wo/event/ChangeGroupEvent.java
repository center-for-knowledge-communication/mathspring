package edu.umass.ckc.wo.event;

import edu.umass.ckc.servlet.servbase.ServletParams;
import edu.umass.ckc.servlet.servbase.ActionEvent;
import edu.umass.ckc.wo.mrcommon.Names;


public class ChangeGroupEvent extends ActionEvent {
    private String group_;

    public ChangeGroupEvent(ServletParams p) throws Exception {
      super(p);
      group_ = p.getString(Names.GROUP);
    }

    public String getGroup () { return group_; }
}
