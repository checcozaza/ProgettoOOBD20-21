package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import controllers.Controller;
import entities.Company;
import entities.Employee;
import enums.EnumRole;

public class CompanyDAOPG {
	private Controller c;
	private Connection conn = null;
	private ResultSet result = null;
	private PreparedStatement query;

	public CompanyDAOPG(Controller co) {
		c = co;
	}

	// Metodo che permette il recupero delle aziende dal DB
	public Object[] retrieveCompanies() throws SQLException {
		conn = c.connect();
		if (conn == null) return null;
		
		query = conn.prepareStatement("SELECT partiva FROM Azienda");
		result = query.executeQuery();
		ArrayList<String> companies = new ArrayList<String>();
		while (result.next())
			companies.add(result.getString("partiva"));
	
		result.close();
		conn.close();
		return companies.toArray();
	}

	// Metodo che permette il recupero dell'azienda dell'utente loggato dal DB
	public Company takeCompany(String username, String pwd) throws SQLException {
		conn = c.connect();
		if (conn == null) return null;
		
		if (pwd == null) {
			query = conn.prepareStatement("SELECT * FROM Azienda WHERE partiva = ?");
		}
		else {
			query = conn.prepareStatement("SELECT * FROM Azienda WHERE partiva = ? AND passw = ?");
			query.setString(2, pwd);
		}
			
		query.setString(1, username);
		result = query.executeQuery();
		
		Company foundCompany;
		if (result.next())
			foundCompany = new Company(result.getString("partiva"),
								 result.getString("nome"),
								 result.getString("sedeprincipale"),
								 result.getString("passw"),
								 null, null);
		else
			foundCompany = null;
	
		result.close();
		conn.close();
		
		return foundCompany;
	}
}
