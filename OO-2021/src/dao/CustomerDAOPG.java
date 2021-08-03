package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import controllers.Controller;

public class CustomerDAOPG {

	private Controller c;
	private Connection conn = null;
	private ResultSet result = null;
	private PreparedStatement query;

	public CustomerDAOPG(Controller co) {
		c = co;
	}

	public Object[] retrieveCustomers() throws SQLException {
		conn = c.connect();
		if (conn == null) return null;
		
		query = conn.prepareStatement("SELECT * FROM privato");
		result = query.executeQuery();
		ArrayList<String> customers = new ArrayList<String>();
		while (result.next())
			customers.add(result.getString("cf") + " (" +
						  result.getString("nome") + " " +
						  result.getString("cognome") + ")");
		
		result.close();
		conn.close();
		return customers.toArray();
	}
}
