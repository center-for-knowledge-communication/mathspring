package edu.umass.ckc.wo.content;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 1/8/14
 * Time: 2:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class CCCluster {
    private int id;
    private String displayName;
    private String clusterABCD;
    private String clusterCCName;
    private String categoryCode;
    private String c_grade;
    private String category;
    private List<CCStandard> standards;

    // TODO not being used currently.

    public CCCluster (int id) {
        setId(id);
    }

    public CCCluster(int id, String displayName, String clusterABCD, String clusterCCName,
                     String categoryCode, String c_grade, String category) {
        setId(id);
        setDisplayName(displayName);
        setClusterABCD(clusterABCD);
        setClusterCCName(clusterCCName);
        setCategoryCode(categoryCode);
        setCGrade(c_grade);
        setCategory(category);
        standards = new ArrayList<CCStandard>();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String name) {
        this.displayName = name;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCode(String code) {
        this.categoryCode = code;
    }

    public List<CCStandard> getStandards() {
        return standards;
    }

    public void setStandards(List<CCStandard> standards) {
        this.standards = standards;
    }

    public void addStandard (CCStandard standard) {
        this.standards.add(standard);
    }

    public String getClusterABCD() {
        return clusterABCD;
    }

    public void setClusterABCD(String clusterABCD) {
        this.clusterABCD = clusterABCD;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCGrade() {
        return c_grade;
    }

    public void setCGrade(String c_grade) {
        this.c_grade = c_grade;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getClusterCCName() {
        return clusterCCName;
    }

    public void setClusterCCName(String clusterCCName) {
        this.clusterCCName = clusterCCName;
    }

    public CCStandard getStandard(String stdId) {
        for (CCStandard s: this.standards)
            if (s.getId().equals(stdId))
                return s;
        return null;
    }

    public CCStandard getNextStandard(CCStandard curStd) {
        int i = this.standards.indexOf(curStd);
        if (i < this.standards.size()-1)
            return this.standards.get(i+1);
        return null;
    }

    public CCStandard getFirstStandard() {
        if (this.standards.size() > 0)
            return this.standards.get(0);
        else return null;
    }
}
