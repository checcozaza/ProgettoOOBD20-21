package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import controllers.Controller;
import entities.Employee;

public class EmployeeDAOPG {

	
	private Controller c;
	private Connection conn = null;
	private ResultSet result = null;
	private PreparedStatement query;

	public EmployeeDAOPG(Controller co) {
		c = co;
	}

	public void insertEmployeeProfile(Employee employee) throws SQLException {
		conn = c.connect();
		if (conn == null) return;
		
		query = conn.prepareStatement("INSERT INTO Partecipante (cf, nome, cognome, pw, salariomedio, partiva) "
									+ "VALUES (?, ?, ?, ?, ?, ?)");
		query.setString(1, employee.getFiscalCode());
		query.setString(2, employee.getName());
		query.setString(3, employee.getSurname());
		query.setString(4, employee.getPasswd());
		query.setDouble(5, employee.getAvgWage());
		query.setString(6, employee.getHiredBy().getVatNumber());

		query.executeUpdate();
		conn.close();
		return;
	}
	
	

}
