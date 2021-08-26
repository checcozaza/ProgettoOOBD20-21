package entities;

public class Ratings {
	
	// Attributi
	private int rating;
	private Employee pastEmployee; // Codifica associazione
	private ProjectHistory pastProject; // Codifica associazione
	
	// Costruttore
	public Ratings(int rating, Employee pastEmployee, ProjectHistory pastProject) {
		this.rating = rating;
		this.pastEmployee = pastEmployee;
		this.pastProject = pastProject;
	}
	
	// Getters e setters
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	
	
	public Employee getPastEmployee() {
		return pastEmployee;
	}
	public void setPastEmployee(Employee pastEmployee) {
		this.pastEmployee = pastEmployee;
	}
	
	
	public ProjectHistory getPastProject() {
		return pastProject;
	}
	public void setPastProject(ProjectHistory pastProject) {
		this.pastProject = pastProject;
	}
}

