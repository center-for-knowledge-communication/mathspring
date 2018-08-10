package edu.umass.ckc.wo.ttmain.ttmodel;

import edu.umass.ckc.wo.content.CCStandard;

import java.util.List;
import java.util.Set;

/**
 * Created by nsmenon on 4/14/2017.
 */
public class SatProbNew {
    private boolean activated;
    private int id;
    private String name;
    private String nickName;
    private String difficulty;
    private List<CCStandard> ccStand;
    private String type;
    private String htmlDirectory;
    private String problemNo;
    private String resource;

    public SatProbNew(boolean activated, int id, String name, String nickName, String difficulty, List<CCStandard> ccStand, String type,String htmlDirectory,String probNum,String resource) {
        this.activated = activated;
        this.id = id;
        this.name = name;
        this.nickName = nickName;
        this.difficulty = difficulty;
        this.ccStand = ccStand;
        this.type = type;
        this.htmlDirectory = htmlDirectory;
        this.problemNo = probNum;
        this.resource = resource;
    }
}
