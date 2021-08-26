package dao;

import java.sql.SQLException;
import java.util.ArrayList;

import entities.Employee;
import entities.Ratings;

public interface RatingsDAO {

	// Metodo che permette il recupero delle valutazioni del dipendente loggato
	ArrayList<Ratings> takeRatings(Employee signedIn) throws SQLException;

	// Metodo che permette l'inserimento di una valutazione per i progettisti alla chiusura di un progetto
	void insertRatingsForEmployees(String cf, int rating, int currentProject) throws SQLException;

	// Metodo che recupera le valutazioni dei dipendenti
	ArrayList<Ratings> takeRatingsFromFiscalCode(String cf) throws SQLException;

}