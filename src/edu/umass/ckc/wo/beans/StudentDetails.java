package edu.umass.ckc.wo.beans;

import java.util.Set;

public class StudentDetails {

	private Integer studentId;
	private String studentName;
	private String studentUserName;
	private Integer studentPedagogyId;
	private String studentGender;
	private Integer studentAge;
	private Set<SurveyQuestionDetails> questionset;
	
	public Integer getStudentId() {
		return studentId;
	}
	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getStudentUserName() {
		return studentUserName;
	}
	public void setStudentUserName(String studentUserName) {
		this.studentUserName = studentUserName;
	}
	public Integer getStudentPedagogyId() {
		return studentPedagogyId;
	}
	public void setStudentPedagogyId(Integer studentPedagogyId) {
		this.studentPedagogyId = studentPedagogyId;
	}
	public String getStudentGender() {
		return studentGender;
	}
	public void setStudentGender(String studentGender) {
		this.studentGender = studentGender;
	}
	public Integer getStudentAge() {
		return studentAge;
	}
	public void setStudentAge(Integer studentAge) {
		this.studentAge = studentAge;
	}
	public Set<SurveyQuestionDetails> getQuestionset() {
		return questionset;
	}
	public void setQuestionset(Set<SurveyQuestionDetails> questionset) {
		this.questionset = questionset;
	}
	
	
}
