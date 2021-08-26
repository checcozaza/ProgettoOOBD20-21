package dao;

import java.sql.SQLException;
import java.util.ArrayList;

import entities.Topic;

public interface TopicDAO {

	// Metodo che recupera gli ambiti di un progetto
	ArrayList<Topic> takeProjectTopics(int projectNumber) throws SQLException;

	// Metodo che recupera tutti gli ambiti possibili per i progetti
	ArrayList<Topic> takeTopics() throws SQLException;

	// Metodo per assegnare uno o più ambiti a un progetto
	void insertTopics(int lastProject, ArrayList<String> chosenTopics) throws SQLException;

}