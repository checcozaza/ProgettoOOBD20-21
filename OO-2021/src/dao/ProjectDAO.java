package dao;

import java.sql.SQLException;
import java.util.ArrayList;

import entities.Company;
import entities.Employee;
import entities.Project;

public interface ProjectDAO {

	// Metodo che permette il recupero del progetto attuale di un utente dal DB
	Project takeProject(Employee signedIn) throws SQLException;

	// Metodo che permette il recupero dei progetti dell'azienda loggata dal DB
	ArrayList<Project> takeProjectsForCompany(Company signedInCompany) throws SQLException;

	// Metodo che permette la creazione di un nuovo progetto
	void newProject(String vatNumber, String typology, Float budget, String commissionedBy) throws SQLException;

	// Metodo per recuperare il codice dell'ultimo progetto creato
	int retrieveNewestProject(String vatNumber) throws SQLException;

	// Metodo per chiudere un progetto
	void endProject(int projectNumber) throws SQLException;

}