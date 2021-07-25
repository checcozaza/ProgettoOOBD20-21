package entities;

import java.sql.Connection;
import java.util.ArrayList;

public class Company {
	
	private String vatNumber;
	private String name;
	private String headquarters;
	private ArrayList<Project> companyProjects;
	private ArrayList<Employee> companyEmployees;
	
	public Company(String vatNumber, String name, String headquarters, ArrayList<Project> companyProjects,
			ArrayList<Employee> companyEmployees) {
		this.vatNumber = vatNumber;
		this.name = name;
		this.headquarters = headquarters;
		this.companyProjects = companyProjects;
		this.companyEmployees = companyEmployees;
	}
	
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
