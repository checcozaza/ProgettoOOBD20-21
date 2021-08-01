package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;

import controllers.Controller;
import entities.Meeting;
import entities.Topic;

public class TopicDAOPG {


	private Controller c;
	private Connection conn = null;
	private ResultSet result = null;
	private PreparedStatement query;

	public TopicDAOPG(Controller co) {
		c = co;
		
		
	}

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


}
