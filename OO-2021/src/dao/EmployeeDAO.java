package dao;

import java.sql.SQLException;
import java.util.ArrayList;

import entities.Company;
import entities.Employee;

public interface EmployeeDAO {

	// Metodo che permette l'inserimento di un utente nel DB
	void insertEmployeeProfile(Employee employee) throws SQLException;

	// Metodo che permette il recupero di un utente dal DB
	Employee takeEmployee(String username, String pwd) throws SQLException;

	// Metodo che permette il recupero dei dipendenti dell'azienda loggata dal DB
	ArrayList<Employee> takeEmployeesForCompany(Company signedIn) throws SQLException;

	// Metodo che permette il recupero della valutazione media di ciascun dipendente
	int retrieveAvgRating(String fiscalCode) throws SQLException;

	// Metodo per aggiornare il ruolo di un project manager appena assegnato
	void pickProjectManager(int lastProject, String cf) throws SQLException;

	// Metodo che associa un progetto a un dipendente scelto
	void addToProject(ArrayList<Employee> toAdd) throws SQLException;

	// Metodo che aggiorna il salario medio di un dipendente in seguito ad un'eventuale modifica da parte dell'azienda
	void modifiedWage(String cf, Float newWage) throws SQLException;

	// Metodo che recupera i dipendenti per un progetto
	ArrayList<Employee> takeEmployeesForProject(Employee signedIn) throws SQLException;

}