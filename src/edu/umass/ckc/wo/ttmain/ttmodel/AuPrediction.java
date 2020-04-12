package edu.umass.ckc.wo.ttmain.ttmodel;

import java.sql.Timestamp;

public class AuPrediction {

	private Long id;
	
	private Long studentId;

	private String au1;
	private String au2;
	private String au3;
	private Timestamp saveTime; 
	
	public AuPrediction() {
	}
	public AuPrediction(Long id, Long studentId, String au1, String au2, String au3,Timestamp saveTime) {
		
		this.id = id;
		this.studentId = studentId;
		this.au1 = au1;
		this.au2 = au2;
		this.au3 = au3;
		this.saveTime = saveTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAu1() {
		return au1;
	}

	public void setAu1(String au1) {
		this.au1 = au1;
	}

	public String getAu2() {
		return au2;
	}

	public void setAu2(String au2) {
		this.au2 = au2;
	}

	public String getAu3() {
		return au3;
	}

	public void setAu3(String au3) {
		this.au3 = au3;
	}
	public Timestamp getSaveTime() {
		return saveTime;
	}
	public void setSaveTime(Timestamp saveTime) {
		this.saveTime = saveTime;
	}
	public Long getStudentId() {
		return studentId;
	}
	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}
	
	
	
}
