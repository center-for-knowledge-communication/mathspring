package edu.umass.ckc.wo.tutormeta;
import java.util.ArrayList;
import java.util.List;

public class Skill {
  public String name_;
  public int id_;
  public ArrayList subSkills_;

  public Skill(int id, String name) {
    name_ = name;
    id_ = id;
  }

  public void addSubSkill (Skill sk) {
    subSkills_.add(sk);
  }
  public int getId_() {
    return id_;
  }
  public String getName() {
    return name_;
  }
  public List getSubSkills() {
    return subSkills_;
  }
  public void setSubSkills_(ArrayList subSkills) {
    this.subSkills_ = subSkills;
  }



}