package entities;

import java.util.ArrayList;

public class Topic {
	
	private String name;
	private ArrayList<Project> topicProjects;
	
	public Topic(String name, ArrayList<Project> topicProjects) {
		super();
		this.name = name;
		this.topicProjects = topicProjects;
	}
		
		
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