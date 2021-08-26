package dao;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;

import entities.Employee;
import entities.Meeting;

public interface MeetingDAO {

	// Metodo che permette il recupero dei meeting dell'utente loggato dal DB
	ArrayList<Meeting> takeMeetings(Employee signedIn) throws SQLException;

	// Metodo che permette la crezione di un nuovo meeting
	int insertNewMeeting(int projectNumber, Date meetingDate, Time startTime, Time endTime, boolean online,
			String place) throws SQLException;

	// Metodo per iscrivere un dipendente ad un meeting
	void addEmployeeToMeeting(String cf, int newMeeting) throws SQLException;

	// Metodo per recuperare tutti i meeting programmati per un determinato progetto
	ArrayList<Meeting> takeMeetingsForProject(Employee signedIn) throws SQLException;

	// Metodo per aggiornare lo stato di un meeting
	void updateMeetings(ArrayList<Meeting> meetings) throws SQLException;

	// Metodo per assicurare la consistenza dei meeting
	void cronMeeting() throws SQLException;

}