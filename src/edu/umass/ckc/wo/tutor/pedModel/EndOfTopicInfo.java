package edu.umass.ckc.wo.tutor.pedModel;

public class EndOfTopicInfo {
    boolean maxProbsReached=false, maxTimeReached=false, topicMasteryReached=false, failToFindEasier=false, failToFindHarder=false, failToFindSame=false;

    public EndOfTopicInfo(boolean maxProbsReached, boolean maxTimeReached, boolean failToFindEasier, boolean failToFindHarder, boolean failToFindSame, boolean topicMasteryReached) {
        this.maxProbsReached = maxProbsReached;
        this.maxTimeReached = maxTimeReached;
        this.failToFindEasier = failToFindEasier;
        this.failToFindHarder = failToFindHarder;
        this.failToFindSame = failToFindSame;

    }

    public boolean isTopicDone () {
        return maxProbsReached || maxTimeReached || topicMasteryReached || failToFindSame || failToFindEasier || failToFindHarder;
    }

    public boolean isContentFailure () {
        return failToFindEasier || failToFindHarder || failToFindSame;
    }

    public String getExplanation () {
        if (!isTopicDone())
            return null;
        StringBuilder sb = new StringBuilder();
        if (maxTimeReached)
            sb.append("You&#39;ve spent a lot of time in this topic. <br>");
        if (maxProbsReached)
            sb.append("You&#39;ve seen a lot of problems in this topic. <br>");
        if (topicMasteryReached)
            sb.append("You&#39;ve mastered this topic! <br>");
        if (failToFindEasier)
            sb.append("I cannot find a problem that is just right for you.<br>");
        if (failToFindHarder)
            sb.append("I can&#39;t find a problem more challenging.<br>");
        if (failToFindSame)
            sb.append("I&#39;m all out of problems in this topic.<br>");
        sb.append("Let&#39;s try something else!");
        return sb.toString();
    }

    boolean isMaxProbsReached() {
        return maxProbsReached;
    }

    void setMaxProbsReached(boolean maxProbsReached) {
        this.maxProbsReached = maxProbsReached;
    }

    boolean isMaxTimeReached() {
        return maxTimeReached;
    }

    void setMaxTimeReached(boolean maxTimeReached) {
        this.maxTimeReached = maxTimeReached;
    }

    public boolean isTopicMasteryReached() {
        return topicMasteryReached;
    }

    public void setTopicMasteryReached(boolean topicMasteryReached) {
        this.topicMasteryReached = topicMasteryReached;
    }

    boolean isFailToFindEasier() {
        return failToFindEasier;
    }

    void setFailToFindEasier(boolean failToFindEasier) {
        this.failToFindEasier = failToFindEasier;
    }

    boolean isFailToFindHarder() {
        return failToFindHarder;
    }

    void setFailToFindHarder(boolean failToFindHarder) {
        this.failToFindHarder = failToFindHarder;
    }

    boolean isFailToFindSame() {
        return failToFindSame;
    }

    void setFailToFindSame(boolean failToFindSame) {
        this.failToFindSame = failToFindSame;
    }
}

