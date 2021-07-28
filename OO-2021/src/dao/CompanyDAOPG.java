package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import controllers.Controller;
import entities.Company;

public class CompanyDAOPG {
	private Controller c;
	private Connection conn = null;
	private ResultSet result = null;
	private PreparedStatement query;

	public CompanyDAOPG(Controller co) {
		c = co;
	}

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
}
