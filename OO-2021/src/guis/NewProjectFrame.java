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
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.ImageIcon;


public class NewProjectFrame extends JFrame {

	private JPanel contentPane;
	private Controller c;
	private JComboBox commissionedByComboBox;


	/**
	 * Create the frame.
	 */
	public NewProjectFrame(Controller co, Company signedInCompany, String managerCf) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(NewProjectFrame.class.getResource("/bulb.png")));
		c = co;
		setTitle("Nuovo progetto - Projesting");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 547, 438);
		contentPane = new JPanel();
		contentPane.setBackground(Color.decode("#4C566A"));
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel formsPanel = new JPanel();
		formsPanel.setBackground(Color.decode("#434C5E"));
		contentPane.add(formsPanel, BorderLayout.CENTER);
		formsPanel.setLayout(null);
		
		JComboBox typologyComboBox = new JComboBox();
		typologyComboBox.setFont(new Font("Roboto", Font.PLAIN, 14));
		typologyComboBox.setForeground(Color.decode("#5E81AC"));
		typologyComboBox.setBounds(56, 118, 156, 24);
		formsPanel.add(typologyComboBox);
		typologyComboBox.setModel(new DefaultComboBoxModel(new String[] {"Ricerca Di Base", "Ricerca Industriale ", "Ricerca Sperimentale", "Sviluppo Sperimentale"}));
		
		JSpinner budgetSpinner = new JSpinner();
		budgetSpinner.setFont(new Font("Roboto", Font.PLAIN, 14));
		budgetSpinner.setForeground(Color.decode("#5E81AC"));
		budgetSpinner.setBounds(243, 119, 94, 24);
		formsPanel.add(budgetSpinner);
		budgetSpinner.setModel(new SpinnerNumberModel(new Integer(5000), new Integer(5000), null, new Integer(1000)));
		
		ButtonGroup G = new ButtonGroup();
		
		JRadioButton customerRadioButton = new JRadioButton("Cliente");
		customerRadioButton.setFont(new Font("Roboto", Font.PLAIN, 14));
		customerRadioButton.setForeground(Color.decode("#5E81AC"));
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
		customerRadioButton.setBounds(56, 196, 109, 24);
		formsPanel.add(customerRadioButton);
		G.add(customerRadioButton);
		
		JRadioButton societyRadioButton = new JRadioButton("Societ\u00E0");
		societyRadioButton.setFont(new Font("Roboto", Font.PLAIN, 14));
		societyRadioButton.setForeground(Color.decode("#5E81AC"));
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
		societyRadioButton.setBounds(195, 196, 109, 24);
		formsPanel.add(societyRadioButton);
		G.add(societyRadioButton);
		
		commissionedByComboBox = new JComboBox();
		commissionedByComboBox.setFont(new Font("Roboto", Font.PLAIN, 14));
		commissionedByComboBox.setForeground(Color.decode("#5E81AC"));
		commissionedByComboBox.setBounds(56, 274, 156, 24);
		formsPanel.add(commissionedByComboBox);
		
		JButton newProjectButton = new JButton("Crea nuovo progetto");
		newProjectButton.setBackground(Color.decode("#EBCB8B"));
		newProjectButton.setBorderPainted(false);
		newProjectButton.setFocusPainted(false);
		newProjectButton.setForeground(Color.decode("#2E3440"));
		newProjectButton.setFont(new Font("Roboto", Font.PLAIN, 16));
		newProjectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent confirmNewProject) {
				int lastProject;
				try {
					c.insertProject(signedInCompany.getVatNumber(), 
									typologyComboBox.getSelectedItem().toString(),
									Float.valueOf(budgetSpinner.getValue().toString()),
									societyRadioButton.isSelected() ? 
									commissionedByComboBox.getSelectedItem().toString().substring(0, 11) :
									commissionedByComboBox.getSelectedItem().toString().substring(0, 16));
					lastProject = c.pickNewestProject(signedInCompany.getVatNumber());
					c.chooseProjectManager(lastProject, managerCf);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		newProjectButton.setBounds(170, 351, 181, 24);
		formsPanel.add(newProjectButton);
		
		JPanel titlePanel = new JPanel();
		titlePanel.setBackground(Color.decode("#434C5E"));
		titlePanel.setBounds(0, 0, 531, 76);
		formsPanel.add(titlePanel);
		titlePanel.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel createNewProjectLabel = new JLabel("Crea un nuovo progetto");
		createNewProjectLabel.setIconTextGap(18);
		createNewProjectLabel.setIcon(new ImageIcon(NewProjectFrame.class.getResource("/statistics.png")));
		createNewProjectLabel.setForeground(Color.decode("#EBCB8B"));
		createNewProjectLabel.setFont(new Font("Roboto", Font.PLAIN, 28));
		createNewProjectLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titlePanel.add(createNewProjectLabel);
		
		JLabel typologyLabel = new JLabel("Tipologia");
		typologyLabel.setForeground(Color.decode("#EBCB8B"));
		typologyLabel.setFont(new Font("Roboto", Font.PLAIN, 16));
		typologyLabel.setBounds(56, 87, 75, 33);
		formsPanel.add(typologyLabel);
		
		JLabel budgetLabel = new JLabel("Budget");
		budgetLabel.setForeground(Color.decode("#EBCB8B"));
		budgetLabel.setFont(new Font("Roboto", Font.PLAIN, 16));
		budgetLabel.setHorizontalAlignment(SwingConstants.LEFT);
		budgetLabel.setBounds(247, 89, 57, 28);
		formsPanel.add(budgetLabel);
		
		JLabel commissionedByLabel = new JLabel("Commissionato da");
		commissionedByLabel.setForeground(Color.decode("#EBCB8B"));
		commissionedByLabel.setFont(new Font("Roboto", Font.PLAIN, 16));
		commissionedByLabel.setHorizontalAlignment(SwingConstants.LEFT);
		commissionedByLabel.setBounds(56, 165, 139, 28);
		formsPanel.add(commissionedByLabel);
		
		JLabel chooseCustomerLabel = new JLabel("Seleziona il cliente");
		chooseCustomerLabel.setForeground(Color.decode("#EBCB8B"));
		chooseCustomerLabel.setHorizontalAlignment(SwingConstants.LEFT);
		chooseCustomerLabel.setFont(new Font("Roboto", Font.PLAIN, 16));
		chooseCustomerLabel.setBounds(56, 243, 156, 14);
		formsPanel.add(chooseCustomerLabel);
		
		JLabel iconLabel = new JLabel("");
		iconLabel.setIcon(new ImageIcon(NewProjectFrame.class.getResource("/pencil.png")));
		iconLabel.setBounds(462, 323, 69, 76);
		formsPanel.add(iconLabel);
		
		JButton logoutButton = new JButton("");
		JFrame loggingOut = this;
		logoutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent clickLogout) {
				c.backToLogin(loggingOut);
			}
		});
		logoutButton.setIcon(new ImageIcon(NewProjectFrame.class.getResource("/logout.png")));
		logoutButton.setToolTipText("Logout");
		logoutButton.setForeground(new Color(46, 52, 64));
		logoutButton.setFont(new Font("Roboto", Font.PLAIN, 12));
		logoutButton.setFocusPainted(false);
		logoutButton.setContentAreaFilled(false);
		logoutButton.setBorderPainted(false);
		logoutButton.setBackground(new Color(235, 203, 139));
		logoutButton.setBounds(0, 324, 64, 75);
		formsPanel.add(logoutButton);
		//pack();
		setLocationRelativeTo(null);
	}
}

