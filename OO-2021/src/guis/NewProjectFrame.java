package guis;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controllers.Controller;
import entities.Company;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import enums.EnumTypology;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JRadioButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;


public class NewProjectFrame extends JFrame {

	private JPanel contentPane;
	private Controller c;
	private JComboBox commissionedByComboBox;


	/**
	 * Create the frame.
	 */
	public NewProjectFrame(Controller co, Company signedInCompany) {
		c = co;
		setTitle("Nuovo progetto - Projesting");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 781, 439);
		contentPane = new JPanel();
		contentPane.setBackground(Color.decode("#4C566A"));
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.SOUTH);
		
		JPanel panel_2 = new JPanel();
		contentPane.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(null);
		
		JComboBox typologyComboBox = new JComboBox();
		typologyComboBox.setBounds(141, 82, 131, 28);
		panel_2.add(typologyComboBox);
		typologyComboBox.setModel(new DefaultComboBoxModel(new String[] {"Ricerca Di Base", "Ricerca Industriale ", "Ricerca Sperimentale", "Sviluppo Sperimentale"}));
		
		JSpinner budgetSpinner = new JSpinner();
		budgetSpinner.setBounds(289, 82, 103, 28);
		panel_2.add(budgetSpinner);
		budgetSpinner.setModel(new SpinnerNumberModel(new Integer(5000), new Integer(5000), null, new Integer(1000)));
		
		ButtonGroup G = new ButtonGroup();
		
		JRadioButton customerRadioButton = new JRadioButton("New radio button");
		customerRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent chooseCustomer) {
				try {
					commissionedByComboBox.setModel(new DefaultComboBoxModel<> (c.pickCustomers()));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		customerRadioButton.setBounds(140, 158, 109, 23);
		panel_2.add(customerRadioButton);
		G.add(customerRadioButton);
		
		JRadioButton societyRadioButton = new JRadioButton("New radio button");
		societyRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent chooseCustomer) {
				try {
					commissionedByComboBox.setModel(new DefaultComboBoxModel<> (c.pickSocieties()));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		societyRadioButton.setBounds(289, 158, 109, 23);
		panel_2.add(societyRadioButton);
		G.add(societyRadioButton);
		
		commissionedByComboBox = new JComboBox();
		commissionedByComboBox.setBounds(180, 204, 156, 22);
		panel_2.add(commissionedByComboBox);
		
		JButton btnNewButton = new JButton("Crea nuovo progetto");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent confirmNewProject) {
				try {
					c.insertProject(signedInCompany.getVatNumber(), 
									typologyComboBox.getSelectedItem().toString(),
									Float.valueOf(budgetSpinner.getValue().toString()),
									societyRadioButton.isSelected() ? 
									commissionedByComboBox.getSelectedItem().toString().substring(0, 11) :
									commissionedByComboBox.getSelectedItem().toString().substring(0, 16));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		btnNewButton.setBounds(328, 335, 170, 23);
		panel_2.add(btnNewButton);
		//pack();
		setLocationRelativeTo(null);
	}
}

