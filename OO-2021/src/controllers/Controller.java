package controllers;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.swing.JFrame;

import connections.PgConnection;
import dao.CompanyDAOPG;
import dao.CustomerDAOPG;
import dao.EmployeeDAOPG;
import dao.MeetingDAOPG;
import dao.ProjectDAOPG;
import dao.RatingsDAOPG;
import dao.SocietyDAOPG;
import dao.TopicDAOPG;
import dao.TownDAOPG;
import entities.Company;
import entities.Employee;
import enums.EnumRole;
import guis.CompanyFrame;
import guis.PopupDialog;
import guis.MainFrame;
import guis.NewProjectFrame;
import guis.ProjectManagerFrame;
import guis.SignUpFrame;
import guis.SignedUpDialog;
import guis.UserFrame;

public class Controller {
	
	// Attributi
	private static Controller c;
	private Connection conn = null;
	private PgConnection pgc = null;
	private ProjectDAOPG pdp;
	private MeetingDAOPG mdp;
	private RatingsDAOPG rdp;
	private TownDAOPG tdp;
	private EmployeeDAOPG edp;
	private CompanyDAOPG cdp;
	private TopicDAOPG todp;
	private CustomerDAOPG cudp;
	private SocietyDAOPG sdpg;
	private MainFrame mf;
	private SignUpFrame suf;
	private NewProjectFrame npf;
	private SignedUpDialog sud;
	private ProjectManagerFrame pmf;
	private UserFrame uf;
	private CompanyFrame cfr;
	private PopupDialog infoDialog;
	
	public static void main(String[] args) {
		c = new Controller();
	}
	
	// Costruttore
	public Controller() {
		mf = new MainFrame(this);
		mf.setVisible(true);
	}
	
	/* Metodo che permette di effettuare connessioni alle classi DAO. 
	   Se la connessione avviene con successo, ritorna un oggetto di tipo Connection. */
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

	// Metodo per effettuare una query nel DB con lo scopo di recuperare le regioni
	public Object[] pickRegions() throws Exception {
		tdp = new TownDAOPG(this);
		return tdp.retrieveRegions();
	}

	// Metodo per effettuare una query nel DB con lo scopo di recuperare le province
	public Object[] pickProvince(String selectedRegion) throws Exception {
		tdp = new TownDAOPG(this);
		return tdp.retrieveProvinces(selectedRegion);
	}

	// Metodo per effettuare una query nel DB con lo scopo di recuperare le città
	public Object[] pickCity(String selectedProvince) throws Exception {
		tdp = new TownDAOPG(this);
		return tdp.retrieveCity(selectedProvince);
	}
	
	// Metodo per effettuare una query nel DB con lo scopo di recuperare i codici catastali
	private String pickCodeCat(String town) throws Exception {
		tdp = new TownDAOPG(this);
		return tdp.retrieveCodeCat(town);
	}
	
	// Metodo per effettuare una query nel DB con lo scopo di inserire un utente appena registrato
	public void insertEmployee(Employee employee) throws Exception {
		edp = new EmployeeDAOPG(this);
		edp.insertEmployeeProfile(employee);
		return;
	}
	
	// Metodo per effettuare una query nel DB con lo scopo di recuperare le aziende
	public Object[] pickCompanies() throws Exception {
		cdp = new CompanyDAOPG(this);
		return cdp.retrieveCompanies();
	}
	
	// Metodo utilizzato per il controllo delle vocali, necessario nel calcolo del codice fiscale
	private boolean isVowel(char c) {
		return (c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U');
	}
	
	// Metodo per il calcolo del codice fiscale
	public String cfGenerator(String name, String surname, LocalDate birthDate, char sex, String comune) throws Exception {
		
		 // Il metodo riceve come parametro l'intera data di nascita; è necessario quindi effettuare un parsing del dato
		Integer year = birthDate.getYear();
		String month = birthDate.getMonth().toString();
		Integer day = birthDate.getDayOfMonth();
		String code = pickCodeCat(comune);
		
		// Dichiarazione variabili utilizzate
		String cf = "";
		int count = 0, cons = 0;
		
		// Generazione codice fiscale a partire dal cognome inserito
		for(char c: surname.toUpperCase().toCharArray()) {
			if(count == 3) break;
			if(!isVowel(c) && c != ' ') {
				cf += c;
				count++;
			}
		}
		
		// Blocco di istruzioni da eseguire se non ci sono abbastanza consonanti nel cognome
		if(count != 3) {
			for(char c: surname.toUpperCase().toCharArray()) {
				if(count == 3) break;
				if(isVowel(c)) {
					cf += c;
					count++;
				}
			}
		}
		
		// Blocco di istruzioni da eseguire se non ci sono abbastanza vocali nel cognome
		while(cf.length() < 3) cf += 'X';
		
		// Generazione codice fiscale a partire dal nome inserito
		count = 0;
		for(char c: name.toUpperCase().toCharArray())
			if(!isVowel(c) && c != ' ') cons++;
		
		// Blocco di istruzioni da eseguire se ci sono più di tre consonanti nel nome
		if (cons > 3)
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
			
			// Blocco di istruzioni da eseguire se ci sono meno di tre consonanti nel nome
			if(count != 3) {
				for(char c: name.toUpperCase().toCharArray()) {
					if(count == 3) break;
					if(isVowel(c)) {
						cf += c;
						count++;
					}
				}
			}
			
			// Blocco di istruzioni da eseguire se non ci sono abbastanza vocali nel nome
			while(cf.length() < 6) cf+='X';
		}
		
		// Aggiunta anno di nascita
		cf += year.toString().substring(2);
		
		// Aggiunta mese di nascita
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
			
		// Aggiunta giorno di nascita 
		if(sex == 'F') 
			cf += day+40;
		else {
			if(day.toString().length() == 1)
				cf += 0;
			cf += day;
		}
		
		// Aggiunta codice catastale
		cf += code;
		
		// Aggiunta codice di controllo
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
	
	/* Metodo che controlla se, alla registrazione, tutti i campi del form sono stati riempiti e se la password confermata
	è uguale a quella inserita in precedenza */
	public boolean checkCredentials(String name, String surname, String password, String confirmedPassword, LocalDate birthday) {
		if (name.length() <= 0 || surname.length() <= 0 || password.length() <= 0 || confirmedPassword.length() <= 0 || birthday == null)
			return false;
		else if (!password.equals(confirmedPassword))
			return false;
		return true;
	}
	
	// Metodo che recupera tutte le informazioni dell'utente appena loggato
	private Employee fillEmployeeForLogin(Employee signedIn) throws Exception {
		cdp = new CompanyDAOPG(this);
		signedIn.setHiredBy(cdp.takeCompany(signedIn.getHiredBy().getVatNumber(), null));
		mdp = new MeetingDAOPG(this);
		signedIn.setEmployeeMeetings(mdp.takeMeetings(signedIn));
		pdp = new ProjectDAOPG(this);
		signedIn.setEmployeeProject(pdp.takeProject(signedIn));
		if (signedIn.getEmployeeProject() != null) {
			todp = new TopicDAOPG(this);
			signedIn.getEmployeeProject().setProjectTopics(todp.takeProjectTopics(signedIn.getEmployeeProject().getProjectNumber()));
		}
		rdp = new RatingsDAOPG(this);
		signedIn.setEmployeeRatings(rdp.takeRatings(signedIn));
		return signedIn;
	}

	// Metodo che reindirizza ciascun utente a una homepage personalizzata, dopo aver recuperato le sue informazioni
	public void checkLoginForEmployee(String username, String pwd) throws Exception {
		edp = new EmployeeDAOPG(this);
		Employee signedIn = edp.takeEmployee(username, pwd);
		if (signedIn != null) { // Se nel DB è presente l'utente, ovvero se si è registrato
			signedIn = fillEmployeeForLogin(signedIn);
			if (signedIn.getRole() == EnumRole.Project_Manager)
				openPMFrame(signedIn); // Apre homepage per il project manager (che ha più funzionalità)
			else
				openUserFrame(signedIn); // Apre homepage per un progettista
		}
		else
			openPopupDialog(mf, "Username o password non validi");
		return;
	}
	
	// Metodo che recupera tutte le informazioni dell'azienda appena loggata
	private Company fillCompanyForLogin(Company signedInCompany) throws Exception {
		edp = new EmployeeDAOPG(this);
		signedInCompany.setCompanyEmployees(edp.takeEmployeesForCompany(signedInCompany));
		pdp = new ProjectDAOPG(this);
		signedInCompany.setCompanyProjects(pdp.takeProjectsForCompany(signedInCompany));
		return signedInCompany;
	}

	// Metodo che reindirizza ciascun azienda alla sua homepage personalizzata, dopo aver recuperato le sue informazioni
	public void checkLoginForCompany(String username, String pwd) throws Exception {
		cdp = new CompanyDAOPG(this);
		Company signedInCompany = cdp.takeCompany(username, pwd);
		if (signedInCompany != null) {
			signedInCompany = fillCompanyForLogin(signedInCompany);
			openCompanyFrame(signedInCompany);
		}
		else
			openPopupDialog(mf, "Username o password non validi");
	}
	
	// Metodi finalizzati a regolare la visibilità dei diversi frame ad ogni occorrenza
	
	public void openSignUpForm() throws Exception {
		mf.setVisible(false);
		suf = new SignUpFrame(this);
		suf.setVisible(true);
	}
	
	public void backToLogin(JFrame loggingOut) {
		loggingOut.dispose();
		mf.setVisible(true);
		mf.setEnabled(true);
	}
	
	public void endSignUp() {
		sud.dispose();
		suf.dispose();
		mf.setVisible(true);
	}
	
	// Metodo che reindirizza l'utente alla sua homepage personalizzata
	private void openUserFrame(Employee signedIn) {
		mf.setVisible(false);
		UserFrame uf = new UserFrame(this, signedIn);
		uf.setVisible(true);
		
	}
	
	// Metodo che reindirizza il project manager adalla sua homepage personalizzata
	private void openPMFrame(Employee signedIn) {
		mf.setVisible(false);
		ProjectManagerFrame pmf = new ProjectManagerFrame(this, signedIn);
		pmf.setVisible(true);
	}
	
	// Metodo che reindirizza l'azienda alla sua homepage personalizzata
	private void openCompanyFrame(Company signedInCompany) throws Exception {
		mf.setVisible(false);
		cfr = new CompanyFrame(this, signedInCompany);
		cfr.setVisible(true);
		
	}
	
	// Metodo per aprire una dialog al quale viene passato il messaggio stesso che sarà visualizzato
	public void openPopupDialog(JFrame toClose, String toPrintMessage) {
		infoDialog = new PopupDialog(this, toClose, toPrintMessage);
		infoDialog.setVisible(true);
	}
	
	// Metodo che rende enabled il frame sottostante dopo il click del tasto ok di una dialog
	public void backToBackgroundFrame(JFrame toClose) {
		infoDialog.dispose();
		toClose.setVisible(true);
		toClose.setEnabled(true);
	}
	
	/* Metodo che apre una finestra di dialogo di conferma di avvenuta registrazione; sarà presente anche il codice fiscale
	dell'utente (copiabile) appena calcolato, così da poterlo incollare nella finestra di login alla quale si sarà reindirizzati */
	public void openSuccessDialog(String cf) {
		sud = new SignedUpDialog(this, cf);
		sud.setVisible(true);
		suf.setEnabled(false);
	}

	public int takeRatingForEmployee(String fiscalCode) throws Exception {
		return edp.retrieveAvgRating(fiscalCode);
	}

	public void openNewProjectFrame(Company signedInCompany) {
		cfr.setVisible(false);
		npf = new NewProjectFrame(this, signedInCompany);
		npf.setVisible(true);
	}

	public Object[] pickCustomers() throws Exception {
		cudp = new CustomerDAOPG(this);
		return cudp.retrieveCustomers();
	}

	public Object[] pickSocieties() throws Exception {
		sdpg = new SocietyDAOPG(this);
		return sdpg.retrieveSocieties();
	}
	
	public void insertProject(String vatNumber, String typology, Float budget, String commissionedBy) throws Exception {
		System.out.println(commissionedBy);
		pdp = new ProjectDAOPG(this);
		pdp.newProject(vatNumber, typology, budget, commissionedBy);
		return; 
	}
}
