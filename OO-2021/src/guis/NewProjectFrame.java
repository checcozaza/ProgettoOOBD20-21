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
import entities.Topic;

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
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JRadioButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;


public class NewProjectFrame extends JFrame {

	private JPanel contentPane;
	private Controller c;
	private JComboBox commissionedByComboBox;
	private DefaultTableModel availableTopicsTM;
	private JTable availableTopicsTable;
	private JScrollPane availableTopicsScrollPane;
	private JTable chosenTopicsTable;
	private JScrollPane chosenTopicsScrollPane;
	private DefaultTableModel chosenTopicsTM;


	/**
	 * Create the frame.
	 * @param topics 
	 */
	public NewProjectFrame(Controller co, Company signedInCompany, String managerCf, ArrayList<Topic> topics) {
		setResizable(false);
		JFrame utility = this;
		setIconImage(Toolkit.getDefaultToolkit().getImage(NewProjectFrame.class.getResource("/bulb.png")));
		c = co;
		setTitle("Nuovo progetto - Projesting");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 679, 417);
		contentPane = new JPanel();
		contentPane.setBackground(Color.decode("#4C566A"));
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel formsPanel = new JPanel();
		formsPanel.setBackground(Color.decode("#4C566A"));
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
		customerRadioButton.setBackground(Color.decode("#4C566A"));
		customerRadioButton.setFont(new Font("Roboto", Font.PLAIN, 14));
		customerRadioButton.setForeground(Color.decode("#D8DEE9"));
		customerRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent chooseCustomer) {
				try {
					commissionedByComboBox.setModel(new DefaultComboBoxModel<> (c.pickCustomers()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		customerRadioButton.setBounds(391, 120, 109, 24);
		formsPanel.add(customerRadioButton);
		G.add(customerRadioButton);
		
		JRadioButton societyRadioButton = new JRadioButton("Societ\u00E0");
		societyRadioButton.setBackground(Color.decode("#4C566A"));
		societyRadioButton.setFont(new Font("Roboto", Font.PLAIN, 14));
		societyRadioButton.setForeground(Color.decode("#D8DEE9"));
		societyRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent chooseCustomer) {
				try {
					commissionedByComboBox.setModel(new DefaultComboBoxModel<> (c.pickSocieties()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		societyRadioButton.setBounds(530, 120, 109, 24);
		formsPanel.add(societyRadioButton);
		G.add(societyRadioButton);
		
		commissionedByComboBox = new JComboBox();
		commissionedByComboBox.setFont(new Font("Roboto", Font.PLAIN, 14));
		commissionedByComboBox.setForeground(Color.decode("#5E81AC"));
		commissionedByComboBox.setBounds(391, 198, 156, 24);
		formsPanel.add(commissionedByComboBox);
		
		JButton newProjectButton = new JButton("Crea nuovo progetto");
		newProjectButton.setBackground(Color.decode("#EBCB8B"));
		newProjectButton.setBorderPainted(false);
		newProjectButton.setFocusPainted(false);
		newProjectButton.setForeground(Color.decode("#2E3440"));
		newProjectButton.setFont(new Font("Roboto", Font.PLAIN, 16));
		JFrame toEnable = this;
		newProjectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent confirmNewProject) {
				int lastProject;
				if (chosenTopicsTable.getRowCount() > 0) {
					ArrayList<String> chosenTopics = new ArrayList<String>();
					for (int i = 0; i < chosenTopicsTable.getRowCount(); i++) {
						chosenTopics.add(chosenTopicsTable.getValueAt(i, 0).toString());
					}
					
						
					try {
						c.insertProject(signedInCompany.getVatNumber(), 
										typologyComboBox.getSelectedItem().toString(),
										Float.valueOf(budgetSpinner.getValue().toString()),
										societyRadioButton.isSelected() ? 
										commissionedByComboBox.getSelectedItem().toString().substring(0, 11) :
										commissionedByComboBox.getSelectedItem().toString().substring(0, 16));
						lastProject = c.pickNewestProject(signedInCompany.getVatNumber());
						c.insertProjectTopics(lastProject, chosenTopics);
						c.chooseProjectManager(lastProject, managerCf);
						c.goBack(utility);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		newProjectButton.setBounds(297, 342, 181, 24);
		formsPanel.add(newProjectButton);
		
		JPanel titlePanel = new JPanel();
		titlePanel.setBackground(Color.decode("#434C5E"));
		titlePanel.setBounds(0, 0, 670, 76);
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
		budgetLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		budgetLabel.setBounds(247, 89, 90, 28);
		formsPanel.add(budgetLabel);
		
		JLabel commissionedByLabel = new JLabel("Commissionato da");
		commissionedByLabel.setForeground(Color.decode("#EBCB8B"));
		commissionedByLabel.setFont(new Font("Roboto", Font.PLAIN, 16));
		commissionedByLabel.setHorizontalAlignment(SwingConstants.LEFT);
		commissionedByLabel.setBounds(391, 89, 139, 28);
		formsPanel.add(commissionedByLabel);
		
		JLabel chooseCustomerLabel = new JLabel("Seleziona il cliente");
		chooseCustomerLabel.setForeground(Color.decode("#EBCB8B"));
		chooseCustomerLabel.setHorizontalAlignment(SwingConstants.LEFT);
		chooseCustomerLabel.setFont(new Font("Roboto", Font.PLAIN, 16));
		chooseCustomerLabel.setBounds(391, 167, 156, 14);
		formsPanel.add(chooseCustomerLabel);
		
		JLabel iconLabel = new JLabel("");
		iconLabel.setIcon(new ImageIcon(NewProjectFrame.class.getResource("/pencil.png")));
		iconLabel.setBounds(584, 305, 69, 76);
		formsPanel.add(iconLabel);
		
		JButton logoutButton = new JButton("");
		logoutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent clickLogout) {
				c.backToLogin(utility);
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
		logoutButton.setBounds(0, 317, 64, 75);
		formsPanel.add(logoutButton);
		
		JButton goBackButton = new JButton("Indietro");
		goBackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent goBack) {
					c.goBack(utility);
			}
		});
		goBackButton.setForeground(Color.decode("#2E3440"));
		goBackButton.setFont(new Font("Roboto", Font.PLAIN, 16));
		goBackButton.setBackground(Color.decode("#EBCB8B"));
		goBackButton.setBorderPainted(false);
		goBackButton.setFocusPainted(false);
		goBackButton.setBounds(198, 342, 89, 24);
		formsPanel.add(goBackButton);
		
		JScrollPane availableTopicsScrollPane = new JScrollPane();
		availableTopicsScrollPane.setForeground(new Color(67, 76, 94));
		availableTopicsScrollPane.setFont(new Font("Roboto", Font.PLAIN, 12));
		availableTopicsScrollPane.setBorder(new LineBorder(Color.decode("#434C5E"), 2, true));
		availableTopicsScrollPane.setBackground(new Color(67, 76, 94));
		availableTopicsScrollPane.setBounds(56, 192, 109, 115);
		formsPanel.add(availableTopicsScrollPane);
		
		availableTopicsTM = new DefaultTableModel (
			new Object[][] {
			},
			new String[] {
					"Nome Ambito"
			}	
		);
		
		for (Topic top: topics) {
			availableTopicsTM.addRow(new Object[] {top.getName()
			});
		}
		
		// Rende la table non editabile
		availableTopicsTable = new JTable(availableTopicsTM) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		// Caratteristiche estetiche della JTable
		availableTopicsTable.setBackground(Color.decode("#ECEFF4"));
		availableTopicsTable.setRowMargin(2);
		availableTopicsTable.setRowHeight(24);
		availableTopicsTable.setFont(new Font("Roboto", Font.PLAIN, 12));
		availableTopicsTable.setForeground(Color.decode("#434C5E"));
		availableTopicsTable.setGridColor(Color.decode("#B48EAD"));
		availableTopicsTable.getTableHeader().setBackground(Color.decode("#B48EAD"));
		availableTopicsTable.getTableHeader().setForeground(Color.decode("#ECEFF4"));
		availableTopicsTable.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 12));
		availableTopicsTable.getTableHeader().setReorderingAllowed(false);
		
		availableTopicsScrollPane.setViewportView(availableTopicsTable);
		
		
		JScrollPane chosenTopicsScrollPane = new JScrollPane();
		chosenTopicsScrollPane.setForeground(new Color(67, 76, 94));
		chosenTopicsScrollPane.setFont(new Font("Roboto", Font.PLAIN, 15));
		chosenTopicsScrollPane.setBorder(new LineBorder(Color.decode("#434C5E"), 2, true));
		chosenTopicsScrollPane.setBackground(new Color(67, 76, 94));
		chosenTopicsScrollPane.setBounds(228, 192, 109, 115);
		formsPanel.add(chosenTopicsScrollPane);
		

		chosenTopicsTM = new DefaultTableModel (
			new Object[][] {
			},
			new String[] {
					"Nome Ambito"
			}	
		);
		
		// Rende la table non editabile
		chosenTopicsTable = new JTable(chosenTopicsTM) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		// Caratteristiche estetiche della JTable
		chosenTopicsTable.setBackground(Color.decode("#ECEFF4"));
		chosenTopicsTable.setRowMargin(2);
		chosenTopicsTable.setRowHeight(24);
		chosenTopicsTable.setFont(new Font("Roboto", Font.PLAIN, 12));
		chosenTopicsTable.setForeground(Color.decode("#434C5E"));
		chosenTopicsTable.setGridColor(Color.decode("#B48EAD"));
		chosenTopicsTable.getTableHeader().setBackground(Color.decode("#B48EAD"));
		chosenTopicsTable.getTableHeader().setForeground(Color.decode("#ECEFF4"));
		chosenTopicsTable.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 12));
		chosenTopicsTable.getTableHeader().setReorderingAllowed(false);
		
		chosenTopicsScrollPane.setViewportView(chosenTopicsTable);
		
		JButton btnNewButton = new JButton("");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent addTopic) {
				chosenTopicsTM.addRow(new Object[] {availableTopicsTable.getValueAt(availableTopicsTable.getSelectedRow(), 0)
				});
				availableTopicsTM.removeRow(availableTopicsTable.getSelectedRow());
			}
		});
		btnNewButton.setContentAreaFilled(false);
		btnNewButton.setIcon(new ImageIcon(NewProjectFrame.class.getResource("/plus.png")));
		btnNewButton.setBorderPainted(false);
		btnNewButton.setFocusPainted(false);
		btnNewButton.setBounds(162, 194, 69, 23);
		formsPanel.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent removeTopic) {
				availableTopicsTM.addRow(new Object[] {chosenTopicsTable.getValueAt(chosenTopicsTable.getSelectedRow(), 0)
				});
				chosenTopicsTM.removeRow(chosenTopicsTable.getSelectedRow());
			}
		});
		btnNewButton_1.setIcon(new ImageIcon(NewProjectFrame.class.getResource("/minus.png")));
		btnNewButton_1.setFocusPainted(false);
		btnNewButton_1.setBorderPainted(false);
		btnNewButton_1.setContentAreaFilled(false);
		btnNewButton_1.setBounds(162, 284, 69, 23);
		formsPanel.add(btnNewButton_1);
		
		JLabel availableTopicLabel = new JLabel("Disponibili");
		availableTopicLabel.setHorizontalAlignment(SwingConstants.LEFT);
		availableTopicLabel.setForeground(new Color(235, 203, 139));
		availableTopicLabel.setFont(new Font("Roboto", Font.PLAIN, 16));
		availableTopicLabel.setBounds(56, 163, 83, 28);
		formsPanel.add(availableTopicLabel);
		
		JLabel topicLabel = new JLabel("Ambito");
		topicLabel.setHorizontalAlignment(SwingConstants.CENTER);
		topicLabel.setForeground(new Color(235, 203, 139));
		topicLabel.setFont(new Font("Roboto", Font.PLAIN, 16));
		topicLabel.setBounds(162, 163, 69, 28);
		formsPanel.add(topicLabel);
		
		JLabel chosenTopicLabel = new JLabel("Scelti");
		chosenTopicLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		chosenTopicLabel.setForeground(new Color(235, 203, 139));
		chosenTopicLabel.setFont(new Font("Roboto", Font.PLAIN, 16));
		chosenTopicLabel.setBounds(291, 163, 46, 28);
		formsPanel.add(chosenTopicLabel);
		//pack();
		setLocationRelativeTo(null);
	}
}

