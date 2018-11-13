package edu.umass.ckc.wo.beans;

public class SurveyQuestionDetails {

	private String questionName;
	private String description;
	private Integer problemSet;
	private Integer ansType;
	private Integer skipped;
	private String studentAnswer;
	
	private String aChoice;
	private String bChoice;
	private String cChoice;
	private String dChoice;
	private String eChoice;
	
	public String getQuestionName() {
		return questionName;
	}
	public void setQuestionName(String questionName) {
		this.questionName = questionName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getProblemSet() {
		return problemSet;
	}
	public void setProblemSet(Integer problemSet) {
		this.problemSet = problemSet;
	}
	public Integer getAnsType() {
		return ansType;
	}
	public void setAnsType(Integer ansType) {
		this.ansType = ansType;
	}
	public Integer getSkipped() {
		return skipped;
	}
	public void setSkipped(Integer skipped) {
		this.skipped = skipped;
	}
	public String getStudentAnswer() {
		return studentAnswer;
	}
	public void setStudentAnswer(String studentAnswer) {
		this.studentAnswer = studentAnswer;
	}
	public String getaChoice() {
		return aChoice;
	}
	public void setaChoice(String aChoice) {
		this.aChoice = aChoice;
	}
	public String getbChoice() {
		return bChoice;
	}
	public void setbChoice(String bChoice) {
		this.bChoice = bChoice;
	}
	public String getcChoice() {
		return cChoice;
	}
	public void setcChoice(String cChoice) {
		this.cChoice = cChoice;
	}
	public String getdChoice() {
		return dChoice;
	}
	public void setdChoice(String dChoice) {
		this.dChoice = dChoice;
	}
	public String geteChoice() {
		return eChoice;
	}
	public void seteChoice(String eChoice) {
		this.eChoice = eChoice;
	}
	
	
}
