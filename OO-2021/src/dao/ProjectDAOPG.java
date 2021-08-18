package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import controllers.Controller;
import entities.Company;
import entities.Customer;
import entities.Employee;
import entities.Project;
import entities.Society;
import enums.EnumRole;
import enums.EnumTypology;

public class ProjectDAOPG {
	private Controller c;
	private Connection conn = null;
	private ResultSet result = null;
	private PreparedStatement query;

	public ProjectDAOPG(Controller co) {
		c = co;
	}

	// Metodo che permette il recupero del progetto attuale di un utente dal DB
	public Project takeProject(Employee signedIn) throws SQLException {

		conn = c.connect();
		if (conn == null) return null;

		query = conn.prepareStatement("SELECT * FROM Progetto WHERE codprogetto = ?");
		query.setInt(1, signedIn.getEmployeeProject().getProjectNumber());
		result = query.executeQuery();
		
		Project foundProject;
		if (result.next())
			foundProject = new Project(result.getInt("codprogetto"),
					 				   EnumTypology.valueOf(result.getString("tipologia").replace(' ', '_')),
					 				   result.getInt("numeropartecipanti"),
					 				   result.getFloat("budget"),
					 				   result.getBoolean("terminato"),
					 				   null, signedIn.getHiredBy(), 
					 				   null, null, null, signedIn.getEmployeeMeetings());
		else
			foundProject = null;
	
		result.close();
		conn.close();
		
		return foundProject;
	}

	// Metodo che permette il recupero dei progetti dell'azienda loggata dal DB
	public ArrayList<Project> takeProjectsForCompany(Company signedInCompany) throws SQLException {

		conn = c.connect();
		if (conn == null) return null;

		query = conn.prepareStatement("SELECT * FROM Progetto WHERE partiva = ?");
		query.setString(1, signedInCompany.getVatNumber());
		result = query.executeQuery();
		
		ArrayList<Project> projects = new ArrayList<Project>();
		while (result.next())
			
			projects.add(new Project(result.getInt("codprogetto"),
					 				 EnumTypology.valueOf(result.getString("tipologia").replace(' ', '_')),
					 				 result.getInt("numeropartecipanti"),
					 				 result.getFloat("budget"),
					 				 result.getBoolean("terminato"),
					 				 signedInCompany.getCompanyEmployees(), 
					 				 signedInCompany, new Customer(result.getString("cf"), null, null), 
					 				 new Society(result.getString("partitaiva"), null), 
					 				 null, null));
	
		result.close();
		conn.close();
		
		return projects;
	}

	public void newProject(String vatNumber, String typology, Float budget, String commissionedBy) throws SQLException {
		conn = c.connect();
		if (conn == null) return;
		
		query = conn.prepareStatement("INSERT INTO Progetto (partiva, tipologia, budget, cf, partitaiva) "
									+ "VALUES (?, ?, ?, ?, ?)");
		query.setString(1, vatNumber);
		query.setString(2, typology);
		query.setFloat(3, budget);
		if (commissionedBy.length() == 16) {
			query.setString(4, commissionedBy);
			query.setNull(5, Types.VARCHAR);
		}
		else {
			query.setString(5, commissionedBy);
			query.setNull(4, Types.VARCHAR);
		}

		query.executeUpdate();
		conn.close();

		return;
	}

	public int retrieveNewestProject(String vatNumber) throws SQLException {
		conn = c.connect();
		if (conn == null) return 0;

		query = conn.prepareStatement("SELECT MAX(codProgetto) AS ultimoProgetto FROM Progetto WHERE PartIva = ?");
		query.setString(1, vatNumber);
		result = query.executeQuery();
		
		Project lastProject = null;
		if (result.next())
			lastProject = new Project(result.getInt("ultimoProgetto"), null, 0, 0, false, null, null, null, null, null, null);
	
		result.close();
		conn.close();
		
		return lastProject.getProjectNumber();
	}

	public void endProject(int projectNumber) throws SQLException {
		conn = c.connect();
		if (conn == null) return;
		
		query = conn.prepareStatement("UPDATE Progetto "
									+ "SET Terminato = true "
									+ "WHERE CodProgetto = ?");
		
		query.setInt(1, projectNumber);
		query.executeUpdate();

		conn.close();
		return;
	}
}
