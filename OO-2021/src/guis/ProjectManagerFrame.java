package guis;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controllers.Controller;
import entities.Employee;
import java.awt.Color;

public class ProjectManagerFrame extends JFrame {

	private JPanel contentPane;
	private Controller c;

	/**
	 * Create the frame.
	 */
	public ProjectManagerFrame(Controller co, Employee manager) {
		setBackground(Color.BLACK);
		c = co;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 580, 448);
		contentPane = new JPanel();
		contentPane.setBackground(Color.decode("#3B4252"));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		
		setLocationRelativeTo(null);
	}

}
