package dao;

import java.sql.SQLException;

public interface SocietyDAO {

	// Metodo che recupera le informazioni sulle societ� che commissionano progetti
	Object[] retrieveSocieties() throws SQLException;

}