package entities;

public class Society {

	// Attributi
	private String vatNumber;
	private String name;
	
	//Costruttore
	public Society(String vatNumber, String name) {
		this.vatNumber = vatNumber;
		this.name = name;
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
}
