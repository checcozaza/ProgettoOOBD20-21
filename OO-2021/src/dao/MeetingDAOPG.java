package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Types;
import java.util.ArrayList;

import controllers.Controller;
import entities.Employee;
import entities.Meeting;

public class MeetingDAOPG {

	// Dichiarazioni utili
	private Controller c;
	private Connection conn = null;
	private ResultSet result = null;
	private PreparedStatement query;

	// Costruttore
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

	// Metodo che permette la crezione di un nuovo meeting
	public int insertNewMeeting(int projectNumber, Date meetingDate, Time startTime, Time endTime, boolean online, String place) throws SQLException {
		conn = c.connect();
		if (conn == null) return 0;
		
		query = conn.prepareStatement("INSERT INTO Meeting (codprogetto, dataRiunione, oraInizio, oraFine, piattaforma, luogo) "
									+ "VALUES (?, ?, ?, ?, ?, ?) RETURNING CodMeeting");
		
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

		result = query.executeQuery();
		int meetingNum = 0;
		if (result.next())
			meetingNum = result.getInt("CodMeeting");
		result.close();
		conn.close();
		return meetingNum;
		
	}

	// Metodo per iscrivere un dipendente ad un meeting
	public void addEmployeeToMeeting(String cf, int newMeeting) throws SQLException {
		conn = c.connect();
		if (conn == null) return;
		
		query = conn.prepareStatement("CALL insertEmployeeInMeeting(?, ?);");
		query.setInt(1, newMeeting);
		query.setString(2, cf);
		
		query.executeUpdate();
		conn.close();
		return;
	}

	// Metodo per recuperare tutti i meeting programmati per un determinato progetto
	public ArrayList<Meeting> takeMeetingsForProject(Employee signedIn) throws SQLException {
		conn = c.connect();
		if (conn == null) return null;
		
		query = conn.prepareStatement("SELECT * FROM meeting WHERE CodProgetto = ? AND Iniziato = false AND finito = false "
									+ "AND CodMeeting NOT IN (SELECT CodMeeting FROM CompMeeting WHERE UserID = (SELECT UserID "
																												+"FROM Partecipante WHERE cf = ?))");
		
		query.setInt(1, signedIn.getEmployeeProject().getProjectNumber());
		query.setString(2, signedIn.getFiscalCode());
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

	// Metodo per aggiornare lo stato di un meeting
	public void updateMeetings(ArrayList<Meeting> meetings) throws SQLException {
		conn = c.connect();
		if (conn == null) return;
		
		query = conn.prepareStatement("UPDATE Meeting "
									+ "SET Iniziato = ?, Finito = ? "
									+ "WHERE CodMeeting = ?");
		
		for (Meeting mee: meetings) {
			query.setBoolean(1, mee.isStarted());
			query.setBoolean(2, mee.isEnded());
			query.setInt(3, mee.getMeetingNumber());
			query.executeUpdate();
		}
		
		conn.close();
		return;
	}

	// Metodo per assicurare la consistenza dei meeting
	public void cronMeeting() throws SQLException {
		conn = c.connect();
		if (conn == null) return;
		
		query = conn.prepareStatement("CALL cronMeetingUpdate();");
		query.executeUpdate();
		
		conn.close();
		return;
		
		
		
	}
}