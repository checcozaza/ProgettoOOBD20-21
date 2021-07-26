package controllers;

import java.sql.Connection;
import java.sql.SQLException;
import connections.PgConnection;

public class Controller {
	
	private static Controller c;
	private Connection conn = null;
	private PgConnection pgc = null;
	
	//Metodo utilizzato da classi DAO che effettuano le connessioni, 
	//ritorna un oggetto di tipo Connessione, se questa avviene correttamente
	public Connection connect() {
		try {
			pgc = PgConnection.getInstance();
			conn = pgc.getConnection();
			return conn;
		} catch (SQLException connectionFailed) {
			connectionFailed.printStackTrace();
			return null;
		} catch (ClassNotFoundException classNotFound) {
			classNotFound.printStackTrace();
			return null;
		}
	}
	
	
	public static void main(String[] args) {
		c = new Controller();
	}
}
