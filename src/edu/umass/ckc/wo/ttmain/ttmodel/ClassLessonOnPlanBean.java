package edu.umass.ckc.wo.ttmain.ttmodel;

public class ClassLessonOnPlanBean {

	private Integer seqPos;
	private Integer probGroupId;
	private Integer isDefault;

	public ClassLessonOnPlanBean(Integer seqPos, Integer probGroupId, Integer isDefault) {
		this.seqPos = seqPos;
		this.probGroupId = probGroupId;
		this.isDefault = isDefault;
	}

	public Integer getSeqPos() {
		return seqPos;
	}

	public Integer getProbGroupId() {
		return probGroupId;
	}

	public Integer getIsDefault() {
		return isDefault;
	}

}
