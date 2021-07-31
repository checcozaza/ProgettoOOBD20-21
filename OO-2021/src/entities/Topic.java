package entities;

import java.util.ArrayList;

public class Topic {
	
	// Attributi
	private String name;
	private ArrayList<Project> topicProjects; // Codifica associazione
	
	// Costruttore
	public Topic(String name, ArrayList<Project> topicProjects) {
		super();
		this.name = name;
		this.topicProjects = topicProjects;
	}
		
	// Getters e setters
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	public ArrayList<Project> getTopicProjects() {
		return topicProjects;
	}
	public void setTopicProjects(ArrayList<Project> topicProjects) {
		this.topicProjects = topicProjects;
	}
}