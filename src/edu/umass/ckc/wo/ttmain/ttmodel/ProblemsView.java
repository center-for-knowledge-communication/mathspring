package edu.umass.ckc.wo.ttmain.ttmodel;

import edu.umass.ckc.wo.beans.SATProb;
import edu.umass.ckc.wo.content.CCStandard;
import edu.umass.ckc.wo.content.Problem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by nsmenon on 4/14/2017.
 */
public class ProblemsView {
    private String topicName;
    private Set<CCStandard> topicStandars;
    private String topicSummary;
    private List<SatProbNew> problems;
    private String uri;
    private String html5ProblemURI;
    private String problemLevelId;

    public String getProblemLevelId() {
        return problemLevelId;
    }

    public void setProblemLevelId(String problemLevelId) {
        this.problemLevelId = "problemLevelId"+problemLevelId;
    }

    public String getTopicName() {
        return topicName;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getHtml5ProblemURI() {
        return html5ProblemURI;
    }

    public void setHtml5ProblemURI(String html5ProblemURI) {
        this.html5ProblemURI = html5ProblemURI;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public Set<CCStandard> getTopicStandars() {
        return topicStandars;
    }

    public void setTopicStandars(Set<CCStandard> topicStandars) {
        Set<CCStandard> topicStandar = new HashSet<>();
        for(CCStandard standard : topicStandars){
            CCStandard ccStandard = new CCStandard(
                    standard.getCode(), standard.getDescription(), standard.getCategory(), standard.getGrade(), standard.getIdABC());
            ccStandard.setId(standard.getId());
            topicStandar.add(ccStandard);
        }

        this.topicStandars = topicStandar;
    }

    public String getTopicSummary() {
        return topicSummary;
    }

    public void setTopicSummary(String topicSummary) {
        this.topicSummary = topicSummary;
    }

    public List<SatProbNew> getProblems() {
        return problems;
    }

    public void setProblems(List<SATProb> problems) {
        List<SatProbNew> SATProbListNew = new ArrayList<>();
        for(SATProb satprob : problems){
          SatProbNew sbNew = new SatProbNew(satprob.isActivated(),satprob.getId(),satprob.getName(),satprob.getNickname(),satprob.getDifficulty(),satprob.getProblem().getStandards(),
                  satprob.getType(),satprob.getProblem().getHTMLDir(),satprob.getQuestNum(),satprob.getProblem().getResource());
            SATProbListNew.add(sbNew);
        }
        this.problems = SATProbListNew;
    }
}
