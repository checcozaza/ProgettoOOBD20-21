package daopg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import controllers.Controller;
import dao.SocietyDAO;

public class SocietyDAOPG implements SocietyDAO {
	
	// Dichiarazioni utili
	private Controller c;
	private Connection conn = null;
	private ResultSet result = null;
	private PreparedStatement query;

	// Costruttore
	public SocietyDAOPG(Controller co) {
		c = co;
	}

	// Metodo che recupera le informazioni sulle societ� che commissionano progetti
	@Override
	public Object[] retrieveSocieties() throws SQLException {
		conn = c.connect();
		if (conn == null) return null;
		
		query = conn.prepareStatement("SELECT * FROM societ�");
		result = query.executeQuery();
		ArrayList<String> societies = new ArrayList<String>();
		while (result.next())
			societies.add(result.getString("partitaiva") + " (" +
						  result.getString("nomesocieta") + ")");
		
		result.close();
		conn.close();
		return societies.toArray();
}
}
