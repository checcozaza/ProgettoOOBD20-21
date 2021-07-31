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
import entities.Meeting;

public class MeetingDAOPG {

	private Controller c;
	private Connection conn = null;
	private ResultSet result = null;
	private PreparedStatement query;

	public MeetingDAOPG(Controller co) {
		c = co;
	}

	// Metodo che permette il recupero dei meeting dell'utente loggato dal DB
	public ArrayList<Meeting> takeMeetings(Employee signedIn) throws SQLException {
		conn = c.connect();
		if (conn == null) return null;
		
		query = conn.prepareStatement("SELECT * FROM meeting WHERE codmeeting IN "
									+ "(SELECT CodMeeting FROM CompMeeting WHERE userid = "
									+ "(SELECT UserID FROM Partecipante WHERE cf = ?))");
		
		query.setString(1, signedIn.getFiscalCode());
		result = query.executeQuery();
		
		ArrayList<Meeting> meetings = new ArrayList<Meeting>();
		while (result.next())
			meetings.add(new Meeting(result.getInt("codmeeting"),
									 LocalDate.ofInstant(result.getDate("datariunione").toInstant(), ZoneId.systemDefault()),
									 LocalTime.ofInstant(result.getTime("orainizio").toInstant(), ZoneId.systemDefault()),
									 LocalTime.ofInstant(result.getTime("orafine").toInstant(), ZoneId.systemDefault()),
									 result.getString("piattaforma"),
									 result.getString("luogo"),
									 result.getBoolean("iniziato"),
									 result.getBoolean("finito"),
									 signedIn.getEmployeeProject(), 
									 null));
	
		result.close();
		conn.close();
		return meetings;
	}
}