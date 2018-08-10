package edu.umass.ckc.wo.ttmain.ttmodel;

/**
 * Created by nsmenon on 7/14/2017.
 */
public class PerClusterObjectBean {

    private String clusterId;
    private int noOfProblemsInCluster;
    private double totalHintsViewedPerCluster;
    private double noOfProblemsonFirstAttempt;

    private String categoryCodeAndDisplayCode;
    private String clusterCCName;

    public PerClusterObjectBean(String clusterId, int noOfProblemsInCluster, double totalHintsViewedPerCluster) {
        this.clusterId = clusterId;
        this.noOfProblemsInCluster = noOfProblemsInCluster;
        this.totalHintsViewedPerCluster = totalHintsViewedPerCluster;
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public int getNoOfProblemsInCluster() {
        return noOfProblemsInCluster;
    }

    public void setNoOfProblemsInCluster(int noOfProblemsInCluster) {
        this.noOfProblemsInCluster = noOfProblemsInCluster;
    }

    public double getTotalHintsViewedPerCluster() {
        return totalHintsViewedPerCluster;
    }

    public void setTotalHintsViewedPerCluster(double totalHintsViewedPerCluster) {
        this.totalHintsViewedPerCluster = totalHintsViewedPerCluster;
    }

    public double getNoOfProblemsonFirstAttempt() {
        return noOfProblemsonFirstAttempt;
    }

    public void setNoOfProblemsonFirstAttempt(double noOfProblemsonFirstAttempt) {
        this.noOfProblemsonFirstAttempt = noOfProblemsonFirstAttempt;
    }

    public String getCategoryCodeAndDisplayCode() {
        return categoryCodeAndDisplayCode;
    }

    public void setCategoryCodeAndDisplayCode(String categoryCodeAndDisplayCode) {
        this.categoryCodeAndDisplayCode = categoryCodeAndDisplayCode;
    }

    public String getClusterCCName() {
        return clusterCCName;
    }

    public void setClusterCCName(String clusterCCName) {
        this.clusterCCName = clusterCCName;
    }

}
