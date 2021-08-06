package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Types;
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
									 result.getDate("datariunione").toLocalDate(),
									 result.getTime("orainizio").toLocalTime(),
									 result.getTime("orafine").toLocalTime(),
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

	public void insertNewMeeting(int projectNumber, Date meetingDate, Time startTime, Time endTime, boolean online, String place) throws SQLException {
		conn = c.connect();
		if (conn == null) return;
		
		query = conn.prepareStatement("INSERT INTO Meeting (codprogetto, dataRiunione, oraInizio, oraFine, piattaforma, luogo) "
									+ "VALUES (?, ?, ?, ?, ?, ?)");
		query.setInt(1, projectNumber);
		query.setDate(2, meetingDate);
		query.setTime(3, startTime);
		query.setTime(4, endTime);
		
		if (online == true) {
			query.setString(5, place);
			query.setNull(6, Types.VARCHAR);
		}
		else {
			query.setString(6,  place);
			query.setNull(5, Types.VARCHAR);
		}

		query.executeUpdate();
		conn.close();
		return;
		
	}
}