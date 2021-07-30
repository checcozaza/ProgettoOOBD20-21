package connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PgConnection {

	//Dichiarazione variabili necessarie per la connessione al DB
    private static PgConnection instance;
    private final String USERNAME = "postgres";
    private final String PASSWORD = "010993";
    private final String IP = "188.153.228.238";
    private final String PORT = "5432";
    private Connection connection = null;
    private String url = "jdbc:postgresql://"+IP+":"+PORT+"/Progetto_BD";
    
    
    //Cerca e carica il driver
	private PgConnection() throws ClassNotFoundException, SQLException {
		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(url, USERNAME, PASSWORD);
		} catch (ClassNotFoundException driverNotFound) {
			throw new ClassNotFoundException("ERRORE CRITICO. CONTATTARE L'ASSISTENZA.");
		}
	}
	
	//Restituisce la connessione
    public Connection getConnection() {
        return connection;
    }
	
	//Singleton
    public static PgConnection getInstance() throws ClassNotFoundException, SQLException {
    	
        if (instance == null)
        	instance = new PgConnection();
        else if (instance.getConnection().isClosed())
        	instance = new PgConnection();
        
        return instance;
    }
}
