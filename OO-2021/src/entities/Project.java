package entities;

import java.util.ArrayList;

import enums.EnumTypology;

public class Project {
	
	// Attributi
	private int projectNumber;
	private EnumTypology typology;
	private int employees;
	private double budget;
	private boolean closed;
	private ArrayList<Employee> projectEmployees; // Codifica associazione
	private Company projectCompany; // Codifica associazione
	private Customer projectCustomer; // Codifica associazione
	private Society projectSociety; // Codifica associazione
	private ArrayList<Topic> projectTopics; // Codifica associazione
	private ArrayList<Meeting> projectMeetings; // Codifica associazione
	
	// Costruttore
	public Project(int projectNumber, EnumTypology typology, int employees, double budget, boolean closed,
				   ArrayList<Employee> projectEmployees, Company projectCompany, Customer projectCustomer,
			       Society projectSociety, ArrayList<Topic> projectTopics, ArrayList<Meeting> projectMeetings) {
		this.projectNumber = projectNumber;
		this.typology = typology;
		this.employees = employees;
		this.budget = budget;
		this.closed = closed;
		this.projectEmployees = projectEmployees;
		this.projectCompany = projectCompany;
		this.projectCustomer = projectCustomer;
		this.projectSociety = projectSociety;
		this.projectTopics = projectTopics;
		this.projectMeetings = projectMeetings;
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
	
	
	public int getEmployees() {
		return employees;
	}
	public void setEmployees(int employees) {
		this.employees = employees;
	}
	
	
	public double getBudget() {
		return budget;
	}
	public void setBudget(double budget) {
		this.budget = budget;
	}

	
	public boolean isClosed() {
		return closed;
	}
	public void setClosed(boolean closed) {
		this.closed = closed;
	}
	
	
	public ArrayList<Employee> getProjectEmployees() {
		return projectEmployees;
	}
	public void setProjectEmployees(ArrayList<Employee> projectEmployees) {
		this.projectEmployees = projectEmployees;
	}
	
	
	public Company getProjectCompany() {
		return projectCompany;
	}
	public void setProjectCompany(Company projectCompany) {
		this.projectCompany = projectCompany;
	}
	
	
	public Customer getProjectCustomer() {
		return projectCustomer;
	}
	public void setProjectCustomer(Customer projectCustomer) {
		this.projectCustomer = projectCustomer;
	}
	
	
	public Society getProjectSociety() {
		return projectSociety;
	}
	public void setProjectSociety(Society projectSociety) {
		this.projectSociety = projectSociety;
	}
	
	
	public ArrayList<Topic> getProjectTopics() {
		return projectTopics;
	}
	public void setProjectTopics(ArrayList<Topic> projectTopics) {
		this.projectTopics = projectTopics;
	}
	
	
	public ArrayList<Meeting> getProjectMeetings() {
		return projectMeetings;
	}
	public void setProjectMeetings(ArrayList<Meeting> projectMeetings) {
		this.projectMeetings = projectMeetings;
	}
}
