package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import controllers.Controller;

public class TownDAOPG {
	
	private Controller c;
	private Connection conn = null;
	private ResultSet result = null;
	private PreparedStatement query;

	public TownDAOPG(Controller co) {
		c = co;
	}

	// Metodo che permette il recupero delle regioni dal DB
	public Object[] retrieveRegions() throws SQLException {
		conn = c.connect();
		if (conn == null) return null;
		
		query = conn.prepareStatement("SELECT DISTINCT regione FROM comuneitaliano ORDER BY regione");
		result = query.executeQuery();
		ArrayList<String> regions = new ArrayList<String>();
		while (result.next())
			regions.add(result.getString("regione"));
		
		result.close();
		conn.close();
		return regions.toArray();
		
		
	}

	// Metodo che permette il recupero delle province della regione selezionata dal DB
	public Object[] retrieveProvinces(String selectedRegion) throws SQLException {
		conn = c.connect();
		if (conn == null) return null;
		
		query = conn.prepareStatement("SELECT DISTINCT provincia FROM comuneitaliano WHERE regione = ? ORDER BY provincia");
		query.setString(1, selectedRegion);
		result = query.executeQuery();
		ArrayList<String> provinces = new ArrayList<String>();
		while (result.next())
			provinces.add(result.getString("provincia"));
	
		result.close();
		conn.close();
		return provinces.toArray();
	}

	// Metodo che permette il recupero delle città della provincia selezionata dal DB
	public Object[] retrieveCity(String selectedProvince) throws SQLException {
		conn = c.connect();
		if (conn == null) return null;
		
		query = conn.prepareStatement("SELECT comune FROM comuneitaliano WHERE provincia = ? ORDER BY comune");
		query.setString(1, selectedProvince);
		result = query.executeQuery();
		ArrayList<String> cities = new ArrayList<String>();
		while (result.next())
			cities.add(result.getString("comune"));
	
		result.close();
		conn.close();
		return cities.toArray();

		
	}

	// Metodo che permette il recupero del codice catastale del comune selezionato dal DB
	public String retrieveCodeCat(String comune) throws SQLException {
		conn = c.connect();
		if (conn == null) return null;
		
		query = conn.prepareStatement("SELECT cod_comune FROM comuneitaliano WHERE comune = ?");
		query.setString(1, comune);
		result = query.executeQuery();
		result.next();
		String code = result.getString("cod_comune");

	
		result.close();
		conn.close();
		return code;
	}

	
}
