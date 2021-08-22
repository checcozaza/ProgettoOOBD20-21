package entities;

import java.util.ArrayList;

public class Company {
	
	// Attributi
	private String vatNumber;
	private String name;
	private String headquarters;
	private String passw;
	private ArrayList<Project> companyProjects; // Codifica associazione
	private ArrayList<Employee> companyEmployees; // Codifica associazione
	
	// Costruttore
	public Company(String vatNumber, String name, String headquarters, String passw, ArrayList<Project> companyProjects,
			ArrayList<Employee> companyEmployees) {
		this.vatNumber = vatNumber;
		this.name = name;
		this.headquarters = headquarters;
		this.passw = passw;
		this.companyProjects = companyProjects;
		this.companyEmployees = companyEmployees;
	}
	
	// Getters e setters
	public String getVatNumber() {
		return vatNumber;
	}
	public void setVatNumber(String vatNumber) {
		this.vatNumber = vatNumber;
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	public String getHeadquarters() {
		return headquarters;
	}
	public void setHeadquarters(String headquarters) {
		this.headquarters = headquarters;
	}
	
	public String getPassw() {
		return passw;
	}
	public void setPassw(String passw) {
		this.passw = passw;
	}
	
	public ArrayList<Project> getCompanyProjects() {
		return companyProjects;
	}
	public void setCompanyProjects(ArrayList<Project> companyProjects) {
		this.companyProjects = companyProjects;
	}
	
	
	public ArrayList<Employee> getCompanyEmployees() {
		return companyEmployees;
	}
	public void setCompanyEmployees(ArrayList<Employee> companyEmployees) {
		this.companyEmployees = companyEmployees;
	}
}
