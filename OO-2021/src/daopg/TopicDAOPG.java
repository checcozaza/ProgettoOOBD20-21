package daopg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import controllers.Controller;
import dao.TopicDAO;
import entities.Topic;

public class TopicDAOPG implements TopicDAO {

	// Dichiarazioni utili
	private Controller c;
	private Connection conn = null;
	private ResultSet result = null;
	private PreparedStatement query;

	// Costruttore
	public TopicDAOPG(Controller co) {
		c = co;
	}

	// Metodo che recupera gli ambiti di un progetto
	@Override
	public ArrayList<Topic> takeProjectTopics(int projectNumber) throws SQLException {
		conn = c.connect();
		if (conn == null) return null;
		
		query = conn.prepareStatement("SELECT Nome FROM ProgAmbito WHERE CodProgetto = ?");
		
		query.setInt(1, projectNumber);
		result = query.executeQuery();
		
		ArrayList<Topic> topics = new ArrayList<Topic>();
		while (result.next())
			topics.add(new Topic(result.getString("nome"), null));
	
		result.close();
		conn.close();
		
		return topics;
	}

	// Metodo che recupera tutti gli ambiti possibili per i progetti
	@Override
	public ArrayList<Topic> takeTopics() throws SQLException {
		conn = c.connect();
		if (conn == null) return null;
		
		query = conn.prepareStatement("SELECT Nome FROM Ambito");

		result = query.executeQuery();
		
		ArrayList<Topic> topics = new ArrayList<Topic>();
		while (result.next())
			topics.add(new Topic(result.getString("Nome"), null));
	
		result.close();
		conn.close();
		
		return topics;
	}

	// Metodo per assegnare uno o più ambiti a un progetto
	@Override
	public void insertTopics(int lastProject, ArrayList<String> chosenTopics) throws SQLException {
		conn = c.connect();
		if (conn == null) return;
		
		query = conn.prepareStatement("INSERT INTO ProgAmbito VALUES (?, ?)");
		
		query.setInt(1, lastProject);
		
		for (String s: chosenTopics) {
			query.setString(2, s);
			query.executeUpdate();
		}
		
		conn.close();
		return;
	}
}
