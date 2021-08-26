package dao;

import java.sql.SQLException;

public interface TownDAO {

	// Metodo che permette il recupero delle regioni dal DB
	Object[] retrieveRegions() throws SQLException;

	// Metodo che permette il recupero delle province della regione selezionata dal DB
	Object[] retrieveProvinces(String selectedRegion) throws SQLException;

	// Metodo che permette il recupero delle città della provincia selezionata dal DB
	Object[] retrieveCity(String selectedProvince) throws SQLException;

	// Metodo che permette il recupero del codice catastale del comune selezionato dal DB
	String retrieveCodeCat(String comune) throws SQLException;

}