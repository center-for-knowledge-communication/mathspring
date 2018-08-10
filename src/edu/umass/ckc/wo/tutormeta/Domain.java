package edu.umass.ckc.wo.tutormeta;  

import java.util.ArrayList;
import java.util.List;

public class Domain {

  private ArrayList skills_;

  public Domain() {
  }

  public void addSkill (Skill sk) {
    skills_.add(sk);
  }

  public List getSkills () {
    return (ArrayList) skills_.clone();
  }
  public void setSkills (ArrayList skills) {
    this.skills_ = skills;
  }
}