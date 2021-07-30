package entities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Meeting {
	
	private int meetingNumber;
	private LocalDate meetingDate;
	private LocalTime startTime;
	private LocalTime endTime;
	private String meetingPlatform;
	private String meetingRoom;
	private boolean started;
	private boolean ended;
	private Project meetingProject;
	private ArrayList<Employee> meetingEmployees;
	
	
	public Meeting(int meetingNumber, LocalDate meetingDate, LocalTime startTime, LocalTime endTime, String meetingPlatform,
				   String meetingRoom, boolean started, boolean ended, Project meetingProject,
				   ArrayList<Employee> meetingEmployees) {
		this.meetingNumber = meetingNumber;
		this.meetingDate = meetingDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.meetingPlatform = meetingPlatform;
		this.meetingRoom = meetingRoom;
		this.started = started;
		this.ended = ended;
		this.meetingProject = meetingProject;
		this.meetingEmployees = meetingEmployees;
	}
	
	public int getMeetingNumber() {
		return meetingNumber;
	}
	public void setMeetingNumber() {
		this.meetingNumber = meetingNumber;
	}
	
	public LocalDate getMeetingDate() {
		return meetingDate;
	}
	public void setMeetingDate(LocalDate meetingDate) {
		this.meetingDate = meetingDate;
	}
	
	
	public LocalTime getStartTime() {
		return startTime;
	}
	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}
	
	
	public LocalTime getEndTime() {
		return endTime;
	}
	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}
	
	
	public String getMeetingPlatform() {
		return meetingPlatform;
	}
	public void setMeetingPlatform(String meetingPlatform) {
		this.meetingPlatform = meetingPlatform;
	}
	
	
	public String getMeetingRoom() {
		return meetingRoom;
	}
	public void setMeetingRoom(String meetingRoom) {
		this.meetingRoom = meetingRoom;
	}
	
	
	public boolean isStarted() {
		return started;
	}
	public void setStarted(boolean started) {
		this.started = started;
	}
	
	
	public boolean isEnded() {
		return ended;
	}
	public void setEnded(boolean ended) {
		this.ended = ended;
	}
	
	
	public Project getMeetingProject() {
		return meetingProject;
	}
	public void setMeetingProject(Project meetingProject) {
		this.meetingProject = meetingProject;
	}
	
	
	public ArrayList<Employee> getMeetingEmployees() {
		return meetingEmployees;
	}
	public void setMeetingEmployees(ArrayList<Employee> meetingEmployees) {
		this.meetingEmployees = meetingEmployees;
	}
}