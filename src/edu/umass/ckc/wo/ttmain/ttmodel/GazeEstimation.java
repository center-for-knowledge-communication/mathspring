package edu.umass.ckc.wo.ttmain.ttmodel;

import java.sql.Timestamp;

public class GazeEstimation {

	private Long id;
	
	private Long studentId;

	private int gazeDirection;
	private Timestamp saveTime;
	
	public GazeEstimation() {
		
	}
	public GazeEstimation(Long id, Long studentId, int gazeDirection, Timestamp saveTime) {
		super();
		this.id = id;
		this.studentId = studentId;
		this.gazeDirection = gazeDirection;
		this.saveTime = saveTime;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getStudentId() {
		return studentId;
	}
	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}
	public int getGazeDirection() {
		return gazeDirection;
	}
	public void setGazeDirection(int gazeDirection) {
		this.gazeDirection = gazeDirection;
	}
	public Timestamp getSaveTime() {
		return saveTime;
	}
	public void setSaveTime(Timestamp saveTime) {
		this.saveTime = saveTime;
	} 
	
		
	
	
}
