package dao;

import java.sql.SQLException;

public interface CustomerDAO {

	// Metodo che permette il recupero dei privati che commissionano progetti
	Object[] retrieveCustomers() throws SQLException;

}