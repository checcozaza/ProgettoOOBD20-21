package controllers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

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
import entities.EmployeeRating;
import entities.Meeting;
import enums.EnumRole;
import guis.ChooseMeetingFrame;
import guis.CompanyFrame;
import guis.EmployeeInfoDialog;
import guis.PopupDialog;
import guis.MainFrame;
import guis.NewMeetingFrame;
import guis.NewProjectFrame;
import guis.ProjectManagerFrame;
import guis.RatingDialog;
import guis.SignUpFrame;
import guis.SignedUpDialog;
import guis.UserFrame;

public class Controller {
	
	// Dichiarazioni utili
	private static Controller c;
	private Connection conn = null;
	private PgConnection pgc = null;
	private ProjectDAOPG projectDAO;
	private MeetingDAOPG meetingDAO;
	private RatingsDAOPG ratingsDAO;
	private TownDAOPG townDAO;
	private EmployeeDAOPG employeeDAO;
	private CompanyDAOPG companyDAO;
	private TopicDAOPG topicDAO;
	private CustomerDAOPG customerDAO;
	private SocietyDAOPG societyDAO;
	private MainFrame mainFr;
	private SignUpFrame signUpFr;
	private NewProjectFrame newProjectFr;
	private ProjectManagerFrame projectManagerFr;
	private UserFrame userFr;
	private CompanyFrame companyFr;
	private NewMeetingFrame newMeetingFr;
	private ChooseMeetingFrame chooseMeetingFr;
	private SignedUpDialog signedUpDl;
	private PopupDialog infoDl;
	private RatingDialog ratingForEmployeesDl;
	private EmployeeInfoDialog employeeInfoDl;
	
	public static void main(String[] args) throws Exception {
		c = new Controller();
	}
	
	// Costruttore
	public Controller() throws Exception {
		meetingDAO = new MeetingDAOPG(this);
		meetingDAO.cronMeeting(); // Assicura la consistenza dei meeting ad ogni avvio
		mainFr = new MainFrame(this);
		mainFr.setVisible(true);
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
	
	// Metodo per effettuare una query nel DB con lo scopo di inserire un utente appena registrato
	public void insertEmployee(Employee employee) throws Exception {
		employeeDAO = new EmployeeDAOPG(this);
		employeeDAO.insertEmployeeProfile(employee);
		return;
	}
	
	// Metodo per effettuare una query nel DB con lo scopo di recuperare le aziende
	public Object[] pickCompanies() throws Exception {
		companyDAO = new CompanyDAOPG(this);
		return companyDAO.retrieveCompanies();
	}
	
	// Metodo per effettuare una query nel DB con lo scopo di recuperare le regioni, utile al calcolo del codice fiscale
	public Object[] pickRegions() throws Exception {
		townDAO = new TownDAOPG(this);
		return townDAO.retrieveRegions();
	}
	
	// Metodo per effettuare una query nel DB con lo scopo di recuperare le province, utile al calcolo del codice fiscale
	public Object[] pickProvince(String selectedRegion) throws Exception {
		townDAO = new TownDAOPG(this);
		return townDAO.retrieveProvinces(selectedRegion);
	}
	
	// Metodo per effettuare una query nel DB con lo scopo di recuperare le città, utile al calcolo del codice fiscale
	public Object[] pickCity(String selectedProvince) throws Exception {
		townDAO = new TownDAOPG(this);
		return townDAO.retrieveCity(selectedProvince);
	}
	
	// Metodo per effettuare una query nel DB con lo scopo di recuperare i codici catastali, utile al calcolo del codice fiscale
	private String pickCodeCat(String town) throws Exception {
		townDAO = new TownDAOPG(this);
		return townDAO.retrieveCodeCat(town);
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
		companyDAO = new CompanyDAOPG(this);
		signedIn.setHiredBy(companyDAO.takeCompany(signedIn.getHiredBy().getVatNumber(), null));
		meetingDAO = new MeetingDAOPG(this);
		signedIn.setEmployeeMeetings(meetingDAO.takeMeetings(signedIn));
		projectDAO = new ProjectDAOPG(this);
		signedIn.setEmployeeProject(projectDAO.takeProject(signedIn));
		if (signedIn.getEmployeeProject() != null) {
			topicDAO = new TopicDAOPG(this);
			signedIn.getEmployeeProject().setProjectTopics(topicDAO.takeProjectTopics(signedIn.getEmployeeProject().getProjectNumber()));
			meetingDAO = new MeetingDAOPG(this);
			signedIn.getEmployeeProject().setProjectMeetings(meetingDAO.takeMeetingsForProject(signedIn));
			employeeDAO = new EmployeeDAOPG(this);
			signedIn.getEmployeeProject().setProjectEmployees(employeeDAO.takeEmployeesForProject(signedIn));
		}
		employeeDAO = new EmployeeDAOPG(this);
		signedIn.getHiredBy().setCompanyEmployees(employeeDAO.takeEmployeesForCompany(signedIn.getHiredBy()));
		ratingsDAO = new RatingsDAOPG(this);
		signedIn.setEmployeeRatings(ratingsDAO.takeRatings(signedIn));
		return signedIn;
	}

	// Metodo che reindirizza ciascun utente a una homepage personalizzata, dopo aver recuperato le sue informazioni
	public void checkLoginForEmployee(String username, String pwd) throws Exception {
		employeeDAO = new EmployeeDAOPG(this);
		Employee signedIn = employeeDAO.takeEmployee(username, pwd);
		if (signedIn != null) { // Se nel DB è presente l'utente, ovvero se si è registrato
			signedIn = fillEmployeeForLogin(signedIn);
			if (signedIn.getRole() == EnumRole.Project_Manager)
				openPMFrame(signedIn); // Apre homepage per il project manager (che ha più funzionalità)
			else
				openUserFrame(signedIn); // Apre homepage per un progettista qualsiasi
		}
		else
			openPopupDialog(mainFr, "Username o password non validi"); // Messaggio di errore
		return;
	}
	
	// Metodo che recupera tutte le informazioni dell'azienda appena loggata
	public Company fillCompanyForLogin(Company signedInCompany) throws Exception {
		employeeDAO = new EmployeeDAOPG(this);
		signedInCompany.setCompanyEmployees(employeeDAO.takeEmployeesForCompany(signedInCompany));
		projectDAO = new ProjectDAOPG(this);
		signedInCompany.setCompanyProjects(projectDAO.takeProjectsForCompany(signedInCompany));
		return signedInCompany;
	}

	// Metodo che reindirizza ciascun azienda alla sua homepage personalizzata, dopo aver recuperato le sue informazioni
	public void checkLoginForCompany(String username, String pwd) throws Exception {
		companyDAO = new CompanyDAOPG(this);
		Company signedInCompany = companyDAO.takeCompany(username, pwd);
		if (signedInCompany != null) {
			signedInCompany = fillCompanyForLogin(signedInCompany);
			openCompanyFrame(signedInCompany);
		}
		else
			openPopupDialog(mainFr, "Username o password non validi");
	}
	
	// Metodi finalizzati a regolare la visibilità dei diversi frame ad ogni occorrenza
	
	// Metodo che apre il frame per la registrazione di un utente
	public void openSignUpForm() throws Exception {
		mainFr.setVisible(false);
		signUpFr = new SignUpFrame(this);
		signUpFr.setVisible(true);
	}
	
	// Metodo che reindirizza l'utente alla sua homepage personalizzata
	private void openUserFrame(Employee signedIn) {
		mainFr.setVisible(false);
		userFr = new UserFrame(this, signedIn);
		userFr.setVisible(true);
	}
	
	// Metodo che reindirizza il project manager adalla sua homepage personalizzata
	private void openPMFrame(Employee signedIn) throws Exception {
		mainFr.setVisible(false);
		projectManagerFr = new ProjectManagerFrame(this, signedIn);
		projectManagerFr.setVisible(true);
	}
	
	// Metodo che reindirizza l'azienda alla sua homepage personalizzata
	private void openCompanyFrame(Company signedInCompany) throws Exception {
		mainFr.setVisible(false);
		companyFr = new CompanyFrame(this, signedInCompany);
		companyFr.setVisible(true);
	}
	
	// Metodo che apre il frame per la programmazione di un nuovo meeting
	public void openNewMeetingFrame(int projectNumber, String cf) {
		projectManagerFr.setVisible(false);
		newMeetingFr = new NewMeetingFrame(this, projectNumber, cf);
		newMeetingFr.setVisible(true);
	}
	
	// Metodo che apre il frame per la creazione di un nuovo progetto
	public void openNewProjectFrame(Company signedInCompany, String managerCf, DefaultTableModel employeesTM, JTable employeesTable) throws Exception {
		companyFr.setVisible(false);
		topicDAO = new TopicDAOPG(this);
		newProjectFr = new NewProjectFrame(this, signedInCompany, managerCf, topicDAO.takeTopics(), employeesTM, employeesTable);
		newProjectFr.setVisible(true);
	}
	
	// Metodo per aprire il frame per scegliere a quale meeting partecipare per un utente
	public void openChooseMeetingFrame(Employee user, DefaultTableModel meetingsTM) {
		userFr.setVisible(false);
		chooseMeetingFr = new ChooseMeetingFrame(this, user, meetingsTM);
		chooseMeetingFr.setVisible(true);
	}
	
	// Metodo che fa ritornare alla schermata di login
	public void backToLogin(JFrame utility) {
		utility.dispose();
		mainFr.setVisible(true);
		mainFr.setEnabled(true);
	}
	
	// Metodo che fa ritornare alla schermata di login dopo la registrazione
	public void endSignUp() {
		signedUpDl.dispose();
		signUpFr.dispose();
		mainFr.setVisible(true);
	}
	
	// Metodo che rende visibile ed enabled il frame sottostante dopo il click del tasto ok di una dialog
	public void backToBackgroundFrame(JFrame utility, JDialog toDispose) {
		toDispose.dispose();
		utility.setVisible(true);
		utility.setEnabled(true);
	}
	
	// Metodo per aprire una dialog al quale viene passato il messaggio stesso che sarà visualizzato
	public void openPopupDialog(JFrame utility, String toPrintMessage) {
		infoDl = new PopupDialog(this, utility, toPrintMessage);
		infoDl.setVisible(true);
	}
	
	/* Metodo che apre una finestra di dialogo di conferma di avvenuta registrazione; sarà presente anche il codice fiscale
	dell'utente (copiabile) appena calcolato, così da poterlo incollare nella finestra di login alla quale si sarà reindirizzati */
	public void openSuccessDialog(String cf) {
		signedUpDl = new SignedUpDialog(this, cf);
		signedUpDl.setVisible(true);
		signUpFr.setEnabled(false);
	}
	
	// Metodo che apre la dialog per la valutazione dei progettisti
	public void openRatingDialog(int currentProject, ArrayList<Employee> employeesToRate, JFrame utility) {
		ratingForEmployeesDl = new RatingDialog(this, currentProject, employeesToRate, utility);
		ratingForEmployeesDl.setVisible(true);
	}
	
	// Metodo per aprire la dialog con le informazioni sui progetti passati di un dipendente
	public void openEmployeeInfoDialog(String cf, JFrame utility) throws Exception {
		employeeInfoDl = new EmployeeInfoDialog(this, cf, utility);
		employeeInfoDl.setVisible(true);
	}
	
	// Metodo per recuperare la valutazione per un utente
	public int takeRatingForEmployee(String fiscalCode) throws Exception {
		return employeeDAO.retrieveAvgRating(fiscalCode);
	}

	// Metodo che restituisce i privati che commissionano un progetto
	public Object[] pickCustomers() throws Exception {
		customerDAO = new CustomerDAOPG(this);
		return customerDAO.retrieveCustomers();
	}

	// Metodo che restituisce le società commissionanti per un progetto
	public Object[] pickSocieties() throws Exception {
		societyDAO = new SocietyDAOPG(this);
		return societyDAO.retrieveSocieties();
	}
	
	// Metodo che permette la creazione di un nuovo progetto
	public void insertProject(String vatNumber, String typology, Float budget, String commissionedBy) throws Exception {
		projectDAO = new ProjectDAOPG(this);
		projectDAO.newProject(vatNumber, typology, budget, commissionedBy);
		return; 
	}

	// Metodo che associa il project manager scelto al progetto appena creato
	public void chooseProjectManager(int lastProject, String cf) throws Exception {
		employeeDAO = new EmployeeDAOPG(this);
		employeeDAO.pickProjectManager(lastProject, cf);
	}

	// Metodo che recupera il codice del progetto appena creato
	public int pickNewestProject(String vatNumber) throws Exception {
		projectDAO = new ProjectDAOPG(this);
		int lastProject = projectDAO.retrieveNewestProject(vatNumber);
		return lastProject;
	}

	// Metodo per la chiusura di un progetto
	public void closeProject(int projectNumber) throws Exception {
		projectDAO = new ProjectDAOPG(this);
		projectDAO.endProject(projectNumber);
		return;
	}

	// Meeting che permette la creazione di un nuovo meeting
	public int confirmMeeting(int projectNumber, Date meetingDate, Time startTime, Time endTime, boolean online, String place) throws Exception {
		meetingDAO = new MeetingDAOPG(this);
		return meetingDAO.insertNewMeeting(projectNumber, meetingDate, startTime, endTime, online, place);
	}

	// Metodo per aggiungere un dipendente a un meeting
	public void addEmployeeToMeeting(String manager, int newMeeting) throws Exception {
		meetingDAO = new MeetingDAOPG(this);
		meetingDAO.addEmployeeToMeeting(manager, newMeeting);
		return;
	}
	
	// Metodo per aggiungere un dipendente al team
	public void addToTeam(ArrayList<Employee> toAdd) throws Exception {
		employeeDAO = new EmployeeDAOPG(this);
		employeeDAO.addToProject(toAdd);
		return;
	}

	// Metodo per inserire uno o più ambiti per il progetto creato
	public void insertProjectTopics(int lastProject, ArrayList<String> chosenTopics) throws Exception {
		topicDAO = new TopicDAOPG(this);
		topicDAO.insertTopics(lastProject, chosenTopics);
		return;
	}

	// Metodo che aggiorna il salario di un dipendente quando esso viene modificato dall'azienda
	public void updateWage(String cf, Float newWage) throws Exception {
		employeeDAO = new EmployeeDAOPG(this);
		employeeDAO.modifiedWage(cf, newWage);
		return;
	}

	// Metodo per aggiornare lo stato di un meeting
	public void insertMeetingUpdates(ArrayList<Meeting> meetings) throws Exception {
		meetingDAO = new MeetingDAOPG(this);
		meetingDAO.updateMeetings(meetings);
		return;
	}

	// Metodo che permette l'inserimento della valutazione per i dipendenti
	public void insertRating(String cf, Integer rating, int currentProject) throws Exception {
		ratingsDAO = new RatingsDAOPG(this);
		ratingsDAO.insertRatingsForEmployees(cf, rating, currentProject);
		return;
	}

	// Metodo che aggiorna la composizione di un team di lavoro
	public ArrayList<Employee> refillTeam(Employee manager) throws Exception {
		employeeDAO = new EmployeeDAOPG(this);
		return employeeDAO.takeEmployeesForProject(manager);
	}

	// Metodo che restituisce le valutazioni di un dipendente
	public ArrayList<EmployeeRating> findUserHistory(String cf) throws Exception {
		ratingsDAO = new RatingsDAOPG(this);
		return ratingsDAO.takeRatingsFromFiscalCode(cf);
	}
	
	// Metodo che gestisce i tasti per tornare ai frame precedenti
	public void goBack(JFrame utility) {
		if (newProjectFr != null) {
			utility.setVisible(false);
			companyFr.setVisible(true);
		}
		else if (chooseMeetingFr != null) {
			utility.setVisible(false);
			userFr.setVisible(true);
		}
		else if (newMeetingFr != null) {
			utility.setVisible(false);
			projectManagerFr.setVisible(true);
		}
	}

	public void setNewEmployeesTableForCompany(DefaultTableModel employeesTM) {
		companyFr.updateEmployeesTable(employeesTM);
		return;
	}

	public void updateChosenMeetings(DefaultTableModel meetingsTM) {
		userFr.updateMeetingsTable(meetingsTM);
		return;
	}
}
