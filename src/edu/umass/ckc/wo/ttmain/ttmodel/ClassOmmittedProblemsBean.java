package edu.umass.ckc.wo.ttmain.ttmodel;

public class ClassOmmittedProblemsBean {
	private Integer probId;
	private Integer topicId;

	public ClassOmmittedProblemsBean(Integer probId, Integer topicId) {
		this.probId = probId;
		this.topicId = topicId;
	}

	public Integer getProbId() {
		return probId;
	}

	public Integer getTopicId() {
		return topicId;
	}

}
