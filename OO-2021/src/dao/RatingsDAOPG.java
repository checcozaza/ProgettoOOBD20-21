package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;

import controllers.Controller;
import entities.Employee;
import entities.EmployeeRating;
import entities.Meeting;
import entities.Project;
import entities.ProjectHistory;
import enums.EnumTypology;

public class RatingsDAOPG {

	private Controller c;
	private Connection conn = null;
	private ResultSet result = null;
	private PreparedStatement query;

	public RatingsDAOPG(Controller co) {
		c = co;
	}

	// Metodo che permette il recupero delle valutazioni del dipendente loggato dal DB
	public ArrayList<EmployeeRating> takeRatings(Employee signedIn) throws SQLException {
		conn = c.connect();
		if (conn == null) return null;
		
		query = conn.prepareStatement("SELECT PP.Valutazione, PP.CodProg, PR.Tipologia "
									+ "FROM PartecipanteProg AS PP JOIN ProgRealizzato AS PR ON PP.CodProg = PR.CodProg "
									+ "WHERE PP.UserID = (SELECT UserID "
													 + "FROM Partecipante "
													 + "WHERE CF = ?)");
		
		query.setString(1, signedIn.getFiscalCode());
		result = query.executeQuery();
		
		ArrayList<EmployeeRating> ratings = new ArrayList<EmployeeRating>();
		while (result.next())
			ratings.add(new EmployeeRating(result.getInt("valutazione"),
										   signedIn,
										   new ProjectHistory(result.getInt("CodProg"), 
										   EnumTypology.valueOf(result.getString("Tipologia").replace(' ', '_')) , null)));
	
		result.close();
		conn.close();
		return ratings;
	}

	public void insertRatingsForEmployees(String cf, int rating, int currentProject) throws SQLException {
		conn = c.connect();
		if (conn == null) return;
		
		query = conn.prepareStatement("UPDATE PartecipanteProg "
									+ "SET Valutazione = ? "
									+ "WHERE CodProg = ? AND UserID = (SELECT UserID "
																	 + "FROM PARTECIPANTE "
																	 + "WHERE CF = ?)");
		
		query.setInt(1, rating);
		query.setInt(2, currentProject);
		query.setString(3, cf);
		
		query.executeUpdate();
		
		conn.close();
		return;
		
	}
}
