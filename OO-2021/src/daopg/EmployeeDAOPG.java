package daopg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import controllers.Controller;
import dao.EmployeeDAO;
import entities.Company;
import entities.Employee;
import entities.Project;
import enums.EnumRole;

public class EmployeeDAOPG implements EmployeeDAO {
	
	// Dichiarazioni utili
	private Controller c;
	private Connection conn = null;
	private ResultSet result = null;
	private PreparedStatement query;
	
	// Costruttore
	public EmployeeDAOPG(Controller co) {
		c = co;
	}

	// Metodo che permette l'inserimento di un utente nel DB
	@Override
	public void insertEmployeeProfile(Employee employee) throws SQLException {
		conn = c.connect();
		if (conn == null) return;
		
		query = conn.prepareStatement("INSERT INTO Partecipante (cf, nome, cognome, pw, salariomedio, partiva) "
									+ "VALUES (?, ?, ?, ?, ?, ?)");
		
		query.setString(1, employee.getFiscalCode());
		query.setString(2, employee.getName());
		query.setString(3, employee.getSurname());
		query.setString(4, employee.getPasswd());
		query.setDouble(5, employee.getAvgWage());
		query.setString(6, employee.getHiredBy().getVatNumber());

		query.executeUpdate();
		conn.close();
		
		return;
	}

	// Metodo che permette il recupero di un utente dal DB
	@Override
	public Employee takeEmployee(String username, String pwd) throws SQLException {
		conn = c.connect();
		if (conn == null) return null;
		
		query = conn.prepareStatement("SELECT * FROM Partecipante WHERE cf = ? AND pw = ?");
		
		query.setString(1, username);
		query.setString(2, pwd);
		result = query.executeQuery();
		
		Employee found;
		if (result.next())
			found = new Employee(result.getString("cf"),
								 result.getString("nome"),
								 result.getString("cognome"),
								 result.getString("pw"),
								 result.getString("ruolo") == null ? null : EnumRole.valueOf(result.getString("ruolo").replace(' ', '_')),
								 result.getFloat("salariomedio"),
								 new Company(result.getString("partiva"), null, null, null, null, null),
								 new Project(result.getInt("codprogetto"), null, 0, 0, false, null, null, null, null, null, null),
								 null, null);
		else
			found = null;
	
		result.close();
		conn.close();
		
		return found;
	}

	// Metodo che permette il recupero dei dipendenti dell'azienda loggata dal DB
	@Override
	public ArrayList<Employee> takeEmployeesForCompany(Company signedIn) throws SQLException {
		conn = c.connect();
		if (conn == null) return null;
		
		query = conn.prepareStatement("SELECT * FROM Partecipante WHERE partiva = ?");
		
		query.setString(1, signedIn.getVatNumber());
		result = query.executeQuery();
		
		ArrayList<Employee> employees = new ArrayList<Employee>();
		while (result.next())
			employees.add(new Employee(result.getString("cf"),
									   result.getString("nome"),
									   result.getString("cognome"),
									   result.getString("pw"),
									   result.getString("ruolo") == null ? null : EnumRole.valueOf(result.getString("ruolo").replace(' ', '_')),
									   result.getFloat("salariomedio"),
									   signedIn,
									   new Project(result.getInt("codprogetto"), null, 0, 0, false, null, null, null, null, null, null),
									   null, null));
		
		result.close();
		conn.close();
		
		return employees;
	}

	// Metodo che permette il recupero della valutazione media di ciascun dipendente
	@Override
	public int retrieveAvgRating(String fiscalCode) throws SQLException {
		conn = c.connect();
		if (conn == null) return 0;
		
		query = conn.prepareStatement("SELECT valutazionemedia FROM valutazionemediadipendente WHERE cf = ?");
		
		query.setString(1, fiscalCode);
		result = query.executeQuery();
		
		int avg = 0;
		if (result.next())
			avg = result.getInt("valutazionemedia");
		
		result.close();
		conn.close();
		
		return avg;
	}

	// Metodo per aggiornare il ruolo di un project manager appena assegnato
	@Override
	public void pickProjectManager(int lastProject, String cf) throws SQLException {
		conn = c.connect();
		if (conn == null) return;
		
		query = conn.prepareStatement("UPDATE Partecipante "
									+ "SET Ruolo = 'Project Manager', CodProgetto = ?"
									+ "WHERE cf = ? AND CodProgetto IS NULL");
		
		query.setInt(1, lastProject);
		query.setString(2, cf);
		query.executeUpdate();
		
		conn.close();
		
		return;
	}

	// Metodo che associa un progetto a un dipendente scelto
	@Override
	public void addToProject(ArrayList<Employee> toAdd) throws SQLException {
		conn = c.connect();
		if (conn == null) return;
		
		query = conn.prepareStatement("UPDATE Partecipante "
									+ "SET CodProgetto = ?, Ruolo = ? "
									+ "WHERE cf = ?");
		
		for (Employee em: toAdd) {
			query.setInt(1, em.getEmployeeProject().getProjectNumber());
			query.setString(2, em.getRole().toString().replace('_', ' '));
			query.setString(3, em.getFiscalCode());
			query.executeUpdate();
		}

		conn.close();
		
		return;
	}

	// Metodo che aggiorna il salario medio di un dipendente in seguito ad un'eventuale modifica da parte dell'azienda
	@Override
	public void modifiedWage(String cf, Float newWage) throws SQLException {
		conn = c.connect();
		if (conn == null) return;
		
		query = conn.prepareStatement("UPDATE Partecipante "
									+ "SET SalarioMedio = ? "
									+ "WHERE cf = ?");
		
		query.setFloat(1, newWage);
		query.setString(2, cf);
		
		query.executeUpdate();
		
		conn.close();
		
		return;
	}

	// Metodo che recupera i dipendenti per un progetto
	@Override
	public ArrayList<Employee> takeEmployeesForProject(Employee signedIn) throws SQLException {
		conn = c.connect();
		if (conn == null) return null;
		
		query = conn.prepareStatement("SELECT * FROM Partecipante WHERE CodProgetto = ?");
		
		query.setInt(1, signedIn.getEmployeeProject().getProjectNumber());
		result = query.executeQuery();
		
		ArrayList<Employee> employees = new ArrayList<Employee>();
		while (result.next())
			employees.add(new Employee(result.getString("cf"),
									   result.getString("nome"),
									   result.getString("cognome"),
									   result.getString("pw"),
									   result.getString("ruolo") == null ? null : EnumRole.valueOf(result.getString("ruolo").replace(' ', '_')),
									   result.getFloat("salariomedio"),
									   signedIn.getHiredBy(),
									   signedIn.getEmployeeProject(),
									   null, null));
		
		result.close();
		conn.close();
		
		return employees;
	}
}
