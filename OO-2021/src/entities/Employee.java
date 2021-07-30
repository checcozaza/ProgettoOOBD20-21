package entities;

import java.util.ArrayList;

import enums.EnumRole;

public class Employee {

	
	private String fiscalCode;
	private String name;
	private String surname;
	private String passwd;
	private EnumRole role;
	private float avgWage;
	private Company hiredBy;
	private Project employeeProject;
	private ArrayList<Meeting> employeeMeetings;
	private ArrayList<EmployeeRating> employeeRatings;
	
	public Employee(String fiscalCode, String name, String surname, String passwd, EnumRole role, float avgWage, Company hiredBy,
					Project employeeProject, ArrayList<Meeting> employeeMeetings,
					ArrayList<EmployeeRating> employeeRatings) {
		this.fiscalCode = fiscalCode;
		this.name = name;
		this.surname = surname;
		this.passwd = passwd;
		this.role = role;
		this.avgWage = avgWage;
		this.hiredBy = hiredBy;
		this.employeeProject = employeeProject;
		this.employeeMeetings = employeeMeetings;
		this.employeeRatings = employeeRatings;
	}
	
	public String getFiscalCode() {
		return fiscalCode;
	}
	public void setFiscalCode(String fiscalCode) {
		this.fiscalCode = fiscalCode;
	}
	
	
	public Company getHiredBy() {
		return hiredBy;
	}
	public void setHiredBy(Company hiredBy) {
		this.hiredBy = hiredBy;
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	
	
	public EnumRole getRole() {
		return role;
	}
	public void setRole(EnumRole role) {
		this.role = role;
	}
	
	
	public double getAvgWage() {
		return avgWage;
	}
	public void setAvgWage(float avgWage) {
		this.avgWage = avgWage;
	}
	
	
	public Project getEmployeeProject() {
		return employeeProject;
	}
	public void setEmployeeProject(Project employeeProject) {
		this.employeeProject = employeeProject;
	}
	
	
	public ArrayList<Meeting> getEmployeeMeetings() {
		return employeeMeetings;
	}
	public void setEmployeeMeetings(ArrayList<Meeting> employeeMeetings) {
		this.employeeMeetings = employeeMeetings;
	}
	
	
	public ArrayList<EmployeeRating> getEmployeeRatings() {
		return employeeRatings;
	}
	public void setEmployeeRatings(ArrayList<EmployeeRating> employeeRatings) {
		this.employeeRatings = employeeRatings;
	}
}