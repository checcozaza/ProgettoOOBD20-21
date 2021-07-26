package entities;

import java.util.ArrayList;

import enums.EnumTypology;

public class ProjectHistory {
	
	private EnumTypology typology;
	private ArrayList<EmployeeProjectRating> projectInfo;
	
	
	public ProjectHistory(EnumTypology typology, ArrayList<EmployeeProjectRating> projectInfo) {
		super();
		this.typology = typology;
		this.projectInfo = projectInfo;
	}
	
	
	public EnumTypology getTypology() {
		return typology;
	}
	public void setTypology(EnumTypology typology) {
		this.typology = typology;
	}
	
	
	public ArrayList<EmployeeProjectRating> getProjectInfo() {
		return projectInfo;
	}
	public void setProjectInfo(ArrayList<EmployeeProjectRating> projectInfo) {
		this.projectInfo = projectInfo;
	}
}
