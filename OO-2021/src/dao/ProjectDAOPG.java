package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import controllers.Controller;
import entities.Company;
import entities.Employee;
import entities.Project;
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
					 				 null, null, null, null, null));
	
		result.close();
		conn.close();
		
		return projects;
	}
}
