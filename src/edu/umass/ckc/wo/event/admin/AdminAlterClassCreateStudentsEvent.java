package edu.umass.ckc.wo.event.admin;

import ckc.servlet.servbase.ServletParams;


/**
 *
 */
public class AdminAlterClassCreateStudentsEvent extends AdminClassEvent {
    private String prefix;
    private String testUserPrefix;
    private String password;
    private int beginNum;
    private int endNum;
    private boolean createClassSeq;
    private int numStudentsToAdd;  // This is set when adding to class of existing students


    public AdminAlterClassCreateStudentsEvent(ServletParams p) throws Exception {
        super(p);
        this.prefix = p.getString("prefix");
        this.testUserPrefix = p.getString("testUserPrefix",null);
        this.password = p.getString("password");
        this.beginNum = p.getInt("beginNumber", -1);
        this.endNum = p.getInt("endNumber",-1);
        this.numStudentsToAdd = p.getInt("numStudentsToAdd",-1);
        this.createClassSeq = p.getBoolean("createClassSeq",false);

    }

    public String getPrefix() {
        return prefix;
    }

    public String getTestUserPrefix() {
        return testUserPrefix;
    }

    public String getPassword() {
        return password;
    }

    public int getBeginNum() {
        return beginNum;
    }

    public int getEndNum() {
        return endNum;
    }

    public boolean isCreateClassSeq() {
        return createClassSeq;
    }

    public int getNumStudentsToAdd() {
        return numStudentsToAdd;
    }
}