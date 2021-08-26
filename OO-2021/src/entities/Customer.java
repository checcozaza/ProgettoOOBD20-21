package entities;

public class Customer {
	
	// Attributi
	private String fiscalCode;
	private String name;
	private String surname;
	
	// Costruttore
	public Customer(String fiscalCode, String name, String surname) {
		this.fiscalCode = fiscalCode;
		this.name = name;
		this.surname = surname;
	}
	
	// Getters e setters
	public String getFiscalCode() {
		return fiscalCode;
	}
	public void setFiscalCode(String fiscalCode) {
		this.fiscalCode = fiscalCode;
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
}

