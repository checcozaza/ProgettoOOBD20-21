package dao;

import java.sql.SQLException;

import entities.Company;

public interface CompanyDAO {

	// Metodo che permette il recupero delle aziende dal DB
	Object[] retrieveCompanies() throws SQLException;

	// Metodo che permette il recupero dell'azienda dell'utente loggato dal DB
	Company takeCompany(String username, String pwd) throws SQLException;

}