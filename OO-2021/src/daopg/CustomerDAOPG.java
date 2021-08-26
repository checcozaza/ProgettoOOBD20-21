package daopg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import controllers.Controller;
import dao.CustomerDAO;

public class CustomerDAOPG implements CustomerDAO {

	// Dichiarazioni utili
	private Controller c;
	private Connection conn = null;
	private ResultSet result = null;
	private PreparedStatement query;

	// Costruttore
	public CustomerDAOPG(Controller co) {
		c = co;
	}

	// Metodo che permette il recupero dei privati che commissionano progetti
	@Override
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
