package guis;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controllers.Controller;
import entities.Company;

public class CompanyFrame extends JFrame {

	private JPanel contentPane;
	private Controller c;

	/**
	 * Create the frame.
	 */
	public CompanyFrame(Controller co, Company signedInCompany) {
		c = co;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}

}
