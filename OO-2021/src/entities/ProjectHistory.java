package entities;

import java.util.ArrayList;

import enums.EnumTypology;

public class ProjectHistory {
	
	// Attributi
	private int projectNumber;
	private EnumTypology typology; // Codifica associazione
	private ArrayList<Ratings> projectInfo; // Codifica associazione
	
	// Costruttore
	public ProjectHistory(int projectNumber, EnumTypology typology, ArrayList<Ratings> projectInfo) {
		this.projectNumber = projectNumber;
		this.typology = typology;
		this.projectInfo = projectInfo;
	}
	
	// Getters e setters
	public int getProjectNumber() {
		return projectNumber;
	}	
	public void setProjectNumber(int projectNumber) {
		this.projectNumber = projectNumber;
	}
	
	
	public EnumTypology getTypology() {
		return typology;
	}
	public void setTypology(EnumTypology typology) {
		this.typology = typology;
	}
	
	
	public ArrayList<Ratings> getProjectInfo() {
		return projectInfo;
	}
	public void setProjectInfo(ArrayList<Ratings> projectInfo) {
		this.projectInfo = projectInfo;
	}
}
