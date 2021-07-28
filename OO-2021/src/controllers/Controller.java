package controllers;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

import connections.PgConnection;
import dao.CompanyDAOPG;
import dao.EmployeeDAOPG;
import dao.TownDAOPG;
import entities.Employee;
import guis.MainFrame;
import guis.SignUpFrame;
import guis.SignedUp;

public class Controller {
	
	private static Controller c;
	private Connection conn = null;
	private PgConnection pgc = null;
	MainFrame mf;
	SignUpFrame suf;
	TownDAOPG tdp;
	EmployeeDAOPG edp;
	CompanyDAOPG cdp;
	SignedUp sud;
	
	public static void main(String[] args) {
		c = new Controller();
	}
	
	public Controller() {
		mf = new MainFrame(this);
		mf.setVisible(true);
	}
	
	//Metodo utilizzato da classi DAO che effettuano le connessioni, 
	//ritorna un oggetto di tipo Connessione, se questa avviene correttamente
	public Connection connect() {
		try {
			pgc = PgConnection.getInstance();
			conn = pgc.getConnection();
			return conn;
		} catch (SQLException connectionFailed) {
			connectionFailed.printStackTrace();
			return null;
		} catch (ClassNotFoundException classNotFound) {
			classNotFound.printStackTrace();
			return null;
		}
	}
	
	public void openSignUpForm() throws Exception {
		mf.setVisible(false);
		suf = new SignUpFrame(this);
		suf.setVisible(true);
	}

	public void backToLogin() {
		suf.dispose();
		mf.setVisible(true);
	}

	public Object[] pickRegions() throws Exception {
		tdp = new TownDAOPG(this);
		return tdp.retrieveRegions();
	}

	public Object[] pickProvince(String selectedRegion) throws Exception {
		tdp = new TownDAOPG(this);
		return tdp.retrieveProvinces(selectedRegion);
	}

	public Object[] pickCity(String selectedProvince) throws Exception {
		tdp = new TownDAOPG(this);
		return tdp.retrieveCity(selectedProvince);
	}
	
	//Metodo per il controllo sulle vocali
	private boolean isVowel(char c) {
		return (c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U');
	}
	
	//Metodo per il calcolo del codice fiscale
	public String cfGenerator(String name, String surname, LocalDate birthDate, char sex, String comune) throws Exception {
		
		Integer year = birthDate.getYear();
		String month = birthDate.getMonth().toString();
		Integer day = birthDate.getDayOfMonth();
		String code = pickCodeCat(comune);
		
		//Dichiarazione variabili utili
		String cf = "";
		int count = 0, cons = 0;
		
		//Aggiunta cognome:
		for(char c: surname.toUpperCase().toCharArray()) {
			if(count == 3) break;
			if(!isVowel(c) && c != ' ') {
				cf += c;
				count++;
			}
		}
		
		//Se ci sono meno di 3 consonanti;
		if(count != 3) {
			for(char c: surname.toUpperCase().toCharArray()) {
				if(count == 3) break;
				if(isVowel(c)) {
					cf += c;
					count++;
				}
			}
		}
		
		//Se non bastano le vocali;
		while(cf.length() < 3) cf += 'X';
		
		//Aggiunta nome:
		count = 0;
		for(char c: name.toUpperCase().toCharArray())
			if(!isVowel(c) && c != ' ') cons++;
		
		//Se ci sono pià di 3 consonanti;
		if(cons > 3)
			for(char c: name.toUpperCase().toCharArray()) {
				if (count == 4) break;
				if(!isVowel(c) && c != ' ')
					if(count == 1) count++;
					else {
						cf += c;
						count++;
					}
			}
		else {
			for(char c: name.toUpperCase().toCharArray()) {
				if(count == 3) break;
				if(!isVowel(c) && c != ' ') {
					cf += c;
					count++;
				}
			}
			//Se ci sono meno di 3 consonanti;
			if(count != 3) {
				for(char c: name.toUpperCase().toCharArray()) {
					if(count == 3) break;
					if(isVowel(c)) {
						cf += c;
						count++;
					}
				}
			}
			//Se non bastano le vocali;
			while(cf.length() < 6) cf+='X';
		}
		
		//Aggiunta anno:
		cf += year.toString().substring(2);
		
		//Aggiunta mese:
		switch (month) {
		case "JANUARY":
				cf += 'A';
			break;
		
		case "FEBRUARY":
				cf += 'B';
			break;
			
		case "MARCH":
				cf += 'C';
			break;
		
		case "APRIL":
				cf += 'D';
			break;
			
		case "MAY":
				cf += 'E';
			break;
		
		case "JUNE":
				cf += 'H';
			break;
			
		case "JULY":
				cf += 'L';
			break;
		
		case "AUGUST":
				cf += 'M';
			break;
			
		case "SEPTEMBER":
				cf += 'P';
			break;
		
		case "OCTOBER":
				cf += 'R';
			break;
			
		case "NOVEMBER":
				cf += 'S';
			break;
		
		case "DECEMBER":
				cf += 'T';
			break;
		}
			
		//Aggiunta giorno:
		if(sex == 'F') 
			cf += day+40;
		else {
			if(day.toString().length() == 1)
				cf += 0;
			cf += day;
		}
		
		//Aggiunta codice catastale:
		cf += code;
		
		//Codice di controllo:
		int tempCTRL = 0;
		
		for(int i = 0; i < cf.length(); i++) {
			if((i+1)%2 == 1) {
				switch (cf.charAt(i)) {
				case 'A':
				case '0':
					tempCTRL += 1;
					break;
					
				case 'B':
				case '1':
					tempCTRL += 0;
					break;
					
				case 'C':
				case '2':
					tempCTRL += 5;
					break;
					
				case 'D':
				case '3':
					tempCTRL += 7;
					break;	
					
				case 'E':
				case '4':
					tempCTRL += 9;
					break;
					
				case 'F':
				case '5':
					tempCTRL += 13;
					break;		
					
				case 'G':
				case '6':
					tempCTRL += 15;
					break;		
					
				case 'H':
				case '7':
					tempCTRL += 17;
					break;		
					
				case 'I':
				case '8':
					tempCTRL += 19;
					break;		
					
				case 'J':
				case '9':
					tempCTRL += 21;
					break;		
					
				case 'K':
					tempCTRL += 2;
					break;	
					
				case 'L':
					tempCTRL += 4;
					break;		
					
				case 'M':
					tempCTRL += 18;
					break;		
					
				case 'N':
					tempCTRL += 20;
					break;
					
				case 'O':
					tempCTRL += 11;
					break;		
					
				case 'P':
					tempCTRL += 3;
					break;
					
				case 'Q':
					tempCTRL += 6;
					break;	
					
				case 'R':
					tempCTRL += 8;
					break;
					
				case 'S':
					tempCTRL += 12;
					break;	
					
				case 'T':
					tempCTRL += 14;
					break;	
					
				case 'U':
					tempCTRL += 16;
					break;		
					
				case 'V':
					tempCTRL += 10;
					break;		
					
				case 'W':
					tempCTRL += 22;
					break;	
					
				case 'X':
					tempCTRL += 25;
					break;	
					
				case 'Y':
					tempCTRL += 24;
					break;		
					
				case 'Z':
					tempCTRL += 23;
					break;		
				}	
			}
			else {
				switch (cf.charAt(i)) {
				case 'A':
				case '0':
					tempCTRL += 0;
					break;
					
				case 'B':
				case '1':
					tempCTRL += 1;
					break;
					
				case 'C':
				case '2':
					tempCTRL += 2;
					break;
					
				case 'D':
				case '3':
					tempCTRL += 3;
					break;	
					
				case 'E':
				case '4':
					tempCTRL += 4;
					break;
					
				case 'F':
				case '5':
					tempCTRL += 5;
					break;		
					
				case 'G':
				case '6':
					tempCTRL += 6;
					break;		
					
				case 'H':
				case '7':
					tempCTRL += 7;
					break;		
					
				case 'I':
				case '8':
					tempCTRL += 8;
					break;		
					
				case 'J':
				case '9':
					tempCTRL += 9;
					break;		
					
				case 'K':
					tempCTRL += 10;
					break;	
					
				case 'L':
					tempCTRL += 11;
					break;		
					
				case 'M':
					tempCTRL += 12;
					break;		
					
				case 'N':
					tempCTRL += 13;
					break;
					
				case 'O':
					tempCTRL += 14;
					break;		
					
				case 'P':
					tempCTRL += 15;
					break;
					
				case 'Q':
					tempCTRL += 16;
					break;	
					
				case 'R':
					tempCTRL += 17;
					break;
					
				case 'S':
					tempCTRL += 18;
					break;	
					
				case 'T':
					tempCTRL += 19;
					break;	
					
				case 'U':
					tempCTRL += 20;
					break;		
					
				case 'V':
					tempCTRL += 21;
					break;		
					
				case 'W':
					tempCTRL += 22;
					break;	
					
				case 'X':
					tempCTRL += 23;
					break;	
					
				case 'Y':
					tempCTRL += 24;
					break;		
					
				case 'Z':
					tempCTRL += 25;
					break;		
				}
			}
		}
		tempCTRL %= 26;
		
		switch (tempCTRL) {
		case 0:
			cf += 'A';
			break;
			
		case 1:
			cf += 'B';
			break;
		
		case 2:
			cf += 'C';
			break;
			
		case 3:
			cf += 'D';
			break;
			
		case 4:
			cf += 'E';
			break;
			
		case 5:
			cf += 'F';
			break;	
			
		case 6:
			cf += 'G';
			break;	
			
		case 7:
			cf += 'H';
			break;
			
		case 8:
			cf += 'I';
			break;	
			
		case 9:
			cf += 'J';
			break;
			
		case 10:
			cf += 'K';
			break;	
			
		case 11:
			cf += 'L';
			break;
			
		case 12:
			cf += 'M';
			break;	
			
		case 13:
			cf += 'N';
			break;
			
		case 14:
			cf += 'O';
			break;
			
		case 15:
			cf += 'P';
			break;	
			
		case 16:
			cf += 'Q';
			break;	
			
		case 17:
			cf += 'R';
			break;	
			
		case 18:
			cf += 'S';
			break;	
			
		case 19:
			cf += 'T';
			break;	
			
		case 20:
			cf += 'U';
			break;	
			
		case 21:
			cf += 'V';
			break;	
			
		case 22:
			cf += 'W';
			break;	
			
		case 23:
			cf += 'X';
			break;	
			
		case 24:
			cf += 'Y';
			break;
			
		case 25:
			cf += 'Z';
			break;
		}
		
		return cf;
	}

	private String pickCodeCat(String comune) throws Exception {
		tdp = new TownDAOPG(this);
		return tdp.retrieveCodeCat(comune);
	}

	public boolean checkCredentials(String name, String surname, String password, String confirmedPassword, LocalDate birthday) {
		if (name.length() <= 0 || surname.length() <= 0 || password.length() <= 0 || confirmedPassword.length() <= 0 || birthday == null)
			return false;
		else if (!password.equals(confirmedPassword))
			return false;
		return true;
	}

	public void insertEmployee(Employee employee) throws Exception {
		edp = new EmployeeDAOPG(this);
		edp.insertEmployeeProfile(employee);
		return;
	}

	public Object[] pickCompanies() throws Exception {
		cdp = new CompanyDAOPG(this);
		return cdp.retrieveCompanies();
	}

	public void openSuccessDialog(String cf) {
		sud = new SignedUp(this, cf);
		sud.setVisible(true);
		suf.setEnabled(false);
	}

	public void endSignUp() {
		sud.dispose();
		suf.dispose();
		mf.setVisible(true);
		
	}
	
}
