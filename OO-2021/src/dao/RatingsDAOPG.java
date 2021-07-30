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

public class RatingsDAOPG {

	private Controller c;
	private Connection conn = null;
	private ResultSet result = null;
	private PreparedStatement query;

	public RatingsDAOPG(Controller co) {
		c = co;
	}

	public ArrayList<EmployeeRating> takeRatings(Employee signedIn) throws SQLException {
		conn = c.connect();
		if (conn == null) return null;
		
		query = conn.prepareStatement("SELECT Valutazione, CodProg FROM PartecipanteProg WHERE UserID = (SELECT UserID "
																							  			  + "FROM Partecipante "
																							  			  + "WHERE CF = ?)");
		
		query.setString(1, signedIn.getFiscalCode());
		result = query.executeQuery();
		
		ArrayList<EmployeeRating> ratings = new ArrayList<EmployeeRating>();
		while (result.next())
			ratings.add(new EmployeeRating(result.getInt("valutazione"),
										   signedIn,
										   new ProjectHistory(result.getInt("CodProg"), 
												   	   		  null, null)));
	
		result.close();
		conn.close();
		return ratings;
	}
}
