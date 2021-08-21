package guis;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controllers.Controller;
import entities.Company;
import entities.Topic;

import java.awt.GridBagLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.JRadioButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class NewProjectFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	// Dichiarazioni utili
	private Controller c;
	private JPanel contentPane;
	private JPanel formsPanel;
	private JPanel titlePanel;
	private JPanel bottomPanel;
	private JScrollPane availableTopicsScrollPane;
	private JScrollPane chosenTopicsScrollPane;
	private DefaultTableModel availableTopicsTM;
	private DefaultTableModel chosenTopicsTM;
	private JTable availableTopicsTable;
	private JTable chosenTopicsTable;
	private JComboBox<Object> commissionedByComboBox;
	private JComboBox<Object> typologyComboBox;
	private JSpinner budgetSpinner;
	private JRadioButton customerRadioButton;
	private JRadioButton societyRadioButton;
	private JButton addTopicButton;
	private JButton removeTopicButton;
	private JButton logoutButton;
	private JButton goBackButton;
	private JButton newProjectButton;

	// Creazione frame
	public NewProjectFrame(Controller co, Company signedInCompany, String managerCf, ArrayList<Topic> topics) {
		c = co;
		JFrame utility = this;
		setResizable(false);
		setTitle("Nuovo progetto - Projesting");
		setIconImage(Toolkit.getDefaultToolkit().getImage(NewProjectFrame.class.getResource("/bulb.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 679, 404);
		contentPane = new JPanel();
		contentPane.setBackground(Color.decode("#4C566A"));
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		// Panel contenente i form da compilare per la creazione del nuovo progetto
		formsPanel = new JPanel();
		formsPanel.setBackground(Color.decode("#4C566A"));
		contentPane.add(formsPanel, BorderLayout.CENTER);
		
		// Combobox per selezionare la tipologia di un progetto
		typologyComboBox = new JComboBox<Object>();
		typologyComboBox.setFocusable(false);
		typologyComboBox.setFont(new Font("Roboto", Font.PLAIN, 14));
		typologyComboBox.setForeground(Color.decode("#5E81AC"));
		typologyComboBox.setModel(new DefaultComboBoxModel<Object>(new String[] {"Ricerca Di Base", "Ricerca Industriale ", "Ricerca Sperimentale", "Sviluppo Sperimentale"}));
		
		// JSpinner per inserire un budget per il nuovo progetto
		budgetSpinner = new JSpinner();
		budgetSpinner.setFont(new Font("Roboto", Font.PLAIN, 14));
		budgetSpinner.setForeground(Color.decode("#5E81AC"));
		budgetSpinner.setModel(new SpinnerNumberModel(new Integer(5000), new Integer(5000), null, new Integer(1000)));
		
		// ButtonGroup per inserire i radiobutton
		ButtonGroup G = new ButtonGroup();
		
		// RadioButton da selezionare nel caso in cui il progetto sia stato commissionato da un privato
		customerRadioButton = new JRadioButton("Cliente");
		customerRadioButton.setBackground(Color.decode("#4C566A"));
		customerRadioButton.setFont(new Font("Roboto", Font.PLAIN, 14));
		customerRadioButton.setForeground(Color.decode("#D8DEE9"));
		
		customerRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent chooseCustomer) {
				try {
					commissionedByComboBox.setModel(new DefaultComboBoxModel<> (c.pickCustomers()));
				} catch (Exception customerNotFound) {
					c.openPopupDialog(utility, "Nessun cliente trovato tra i privati.");
				}
			}
		});
		G.add(customerRadioButton);
		
		// RadioButton da selezionare nel caso in cui il progetto sia stato commissionato da una società
		societyRadioButton = new JRadioButton("Societ\u00E0");
		societyRadioButton.setBackground(Color.decode("#4C566A"));
		societyRadioButton.setFont(new Font("Roboto", Font.PLAIN, 14));
		societyRadioButton.setForeground(Color.decode("#D8DEE9"));
		
		societyRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent chooseCustomer) {
				try {
					commissionedByComboBox.setModel(new DefaultComboBoxModel<> (c.pickSocieties()));
				} catch (Exception societyNotFound) {
					c.openPopupDialog(utility, "Nessun cliente trovato tra le società.");
				}
			}
		});
		G.add(societyRadioButton);
		
		// Combobox in cui selezionare chi ha commissionato il progetto
		commissionedByComboBox = new JComboBox<Object>();
		commissionedByComboBox.setFont(new Font("Roboto", Font.PLAIN, 14));
		commissionedByComboBox.setForeground(Color.decode("#5E81AC"));
		
		// Panel contenente il titolo del frame
		titlePanel = new JPanel();
		titlePanel.setBackground(Color.decode("#434C5E"));
		titlePanel.setBounds(0, 0, 670, 76);
		contentPane.add(titlePanel, BorderLayout.NORTH);
		titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		// Label informativa
		JLabel createNewProjectLabel = new JLabel("Crea un nuovo progetto");
		createNewProjectLabel.setIconTextGap(18);
		createNewProjectLabel.setIcon(new ImageIcon(NewProjectFrame.class.getResource("/statistics.png")));
		createNewProjectLabel.setForeground(Color.decode("#EBCB8B"));
		createNewProjectLabel.setFont(new Font("Roboto", Font.PLAIN, 28));
		createNewProjectLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titlePanel.add(createNewProjectLabel);
		
		// Label informativa
		JLabel typologyLabel = new JLabel("Tipologia");
		typologyLabel.setForeground(Color.decode("#EBCB8B"));
		typologyLabel.setFont(new Font("Roboto", Font.PLAIN, 16));
		
		// Label informativa
		JLabel budgetLabel = new JLabel("Budget");
		budgetLabel.setForeground(Color.decode("#EBCB8B"));
		budgetLabel.setFont(new Font("Roboto", Font.PLAIN, 16));
		budgetLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		
		// Label informativa
		JLabel commissionedByLabel = new JLabel("Commissionato da");
		commissionedByLabel.setForeground(Color.decode("#EBCB8B"));
		commissionedByLabel.setFont(new Font("Roboto", Font.PLAIN, 16));
		commissionedByLabel.setHorizontalAlignment(SwingConstants.LEFT);
		
		// Label informativa
		JLabel chooseCustomerLabel = new JLabel("Seleziona il cliente");
		chooseCustomerLabel.setForeground(Color.decode("#EBCB8B"));
		chooseCustomerLabel.setHorizontalAlignment(SwingConstants.LEFT);
		chooseCustomerLabel.setFont(new Font("Roboto", Font.PLAIN, 16));
		
		// ScrollPane che racchiude le informazioni sugli ambiti disponibili
		availableTopicsScrollPane = new JScrollPane();
		availableTopicsScrollPane.setForeground(new Color(67, 76, 94));
		availableTopicsScrollPane.setFont(new Font("Roboto", Font.PLAIN, 12));
		availableTopicsScrollPane.setBorder(new LineBorder(Color.decode("#434C5E"), 2, true));
		availableTopicsScrollPane.setBackground(new Color(67, 76, 94));
		
		// Table model che contiene le informazioni sugli ambiti disponibili
		availableTopicsTM = new DefaultTableModel (
			new Object[][] {
			},
			new String[] {
					"Nome Ambito"
			}	
		);
		
		// Recupero informazioni sugli ambiti
		for (Topic top: topics) {
			availableTopicsTM.addRow(new Object[] {top.getName()
			});
		}
		
		// Rende la table non editabile
		availableTopicsTable = new JTable(availableTopicsTM) {
			private static final long serialVersionUID = 1L;

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
		
		// ScrollPane che racchiude le informazioni sugli ambiti selezionati per il progetto
		chosenTopicsScrollPane = new JScrollPane();
		chosenTopicsScrollPane.setForeground(new Color(67, 76, 94));
		chosenTopicsScrollPane.setFont(new Font("Roboto", Font.PLAIN, 15));
		chosenTopicsScrollPane.setBorder(new LineBorder(Color.decode("#434C5E"), 2, true));
		chosenTopicsScrollPane.setBackground(new Color(67, 76, 94));
		
		// Table model che contiene le informazioni sugli ambiti selezionati
		chosenTopicsTM = new DefaultTableModel (
			new Object[][] {
			},
			new String[] {
					"Nome Ambito"
			}	
		);
		
		// Rende la table non editabile
		chosenTopicsTable = new JTable(chosenTopicsTM) {
			private static final long serialVersionUID = 1L;

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
		
		// Bottone per aggiungere un ambito al progetto
		addTopicButton = new JButton("");
		addTopicButton.setContentAreaFilled(false);
		addTopicButton.setIcon(new ImageIcon(NewProjectFrame.class.getResource("/plus.png")));
		addTopicButton.setBorderPainted(false);
		addTopicButton.setFocusPainted(false);
		
		addTopicButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent addTopic) {
				chosenTopicsTM.addRow(new Object[] {availableTopicsTable.getValueAt(availableTopicsTable.getSelectedRow(), 0) // Aggiunge l'ambito selezionato alla lista di quelli scelti
				});
				availableTopicsTM.removeRow(availableTopicsTable.getSelectedRow()); // Rimuove l'ambito selezionato dalla lista di quelli disponibili
			}
		});
		
		// Bottone per rimuovere un ambito scelto per il progetto
		removeTopicButton = new JButton("");
		removeTopicButton.setIcon(new ImageIcon(NewProjectFrame.class.getResource("/minus.png")));
		removeTopicButton.setFocusPainted(false);
		removeTopicButton.setBorderPainted(false);
		removeTopicButton.setContentAreaFilled(false);
		
		removeTopicButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent removeTopic) {
				availableTopicsTM.addRow(new Object[] {chosenTopicsTable.getValueAt(chosenTopicsTable.getSelectedRow(), 0) // Aggiunge l'ambito selezionato alla lista di quelli disponibili
				});
				chosenTopicsTM.removeRow(chosenTopicsTable.getSelectedRow()); // Rimuove l'ambito selezionato dalla lista di quelli scelti
			}
		});
		
		// Label informativa
		JLabel availableTopicLabel = new JLabel("Disponibili");
		availableTopicLabel.setHorizontalAlignment(SwingConstants.LEFT);
		availableTopicLabel.setForeground(new Color(235, 203, 139));
		availableTopicLabel.setFont(new Font("Roboto", Font.PLAIN, 16));
		
		// Label informativa
		JLabel topicLabel = new JLabel("Ambito");
		topicLabel.setHorizontalAlignment(SwingConstants.CENTER);
		topicLabel.setForeground(new Color(235, 203, 139));
		topicLabel.setFont(new Font("Roboto", Font.PLAIN, 16));
		
		// Label informativa
		JLabel chosenTopicLabel = new JLabel("Scelti");
		chosenTopicLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		chosenTopicLabel.setForeground(new Color(235, 203, 139));
		chosenTopicLabel.setFont(new Font("Roboto", Font.PLAIN, 16));
		
		// Layout utilizzato
		GroupLayout gl_formsPanel = new GroupLayout(formsPanel);
		gl_formsPanel.setHorizontalGroup(
			gl_formsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_formsPanel.createSequentialGroup()
					.addGap(54)
					.addGroup(gl_formsPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_formsPanel.createSequentialGroup()
							.addGroup(gl_formsPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(typologyComboBox, GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE)
								.addComponent(typologyLabel, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE))
							.addGap(31)
							.addGroup(gl_formsPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_formsPanel.createSequentialGroup()
									.addGap(4)
									.addComponent(budgetLabel, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE))
								.addComponent(budgetSpinner, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE))
							.addGap(54)
							.addGroup(gl_formsPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(commissionedByLabel, GroupLayout.PREFERRED_SIZE, 139, GroupLayout.PREFERRED_SIZE)
								.addComponent(customerRadioButton, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE))
							.addComponent(societyRadioButton, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_formsPanel.createSequentialGroup()
							.addComponent(availableTopicLabel, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE)
							.addGap(23)
							.addComponent(topicLabel, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE)
							.addGap(60)
							.addComponent(chosenTopicLabel, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
							.addGap(54)
							.addComponent(chooseCustomerLabel, GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_formsPanel.createSequentialGroup()
							.addGroup(gl_formsPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(availableTopicsScrollPane, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_formsPanel.createSequentialGroup()
									.addGap(106)
									.addComponent(removeTopicButton, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_formsPanel.createSequentialGroup()
									.addGap(106)
									.addComponent(addTopicButton, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_formsPanel.createSequentialGroup()
									.addGap(172)
									.addComponent(chosenTopicsScrollPane, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)))
							.addGap(54)
							.addComponent(commissionedByComboBox, GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE))))
		);
		gl_formsPanel.setVerticalGroup(
			gl_formsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_formsPanel.createSequentialGroup()
					.addGap(22)
					.addGroup(gl_formsPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_formsPanel.createSequentialGroup()
							.addGap(31)
							.addComponent(typologyComboBox, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))
						.addComponent(typologyLabel, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_formsPanel.createSequentialGroup()
							.addGap(2)
							.addComponent(budgetLabel, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
							.addGap(2)
							.addComponent(budgetSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_formsPanel.createSequentialGroup()
							.addGap(2)
							.addComponent(commissionedByLabel, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
							.addGap(3)
							.addComponent(customerRadioButton, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_formsPanel.createSequentialGroup()
							.addGap(33)
							.addComponent(societyRadioButton, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)))
					.addGap(19)
					.addGroup(gl_formsPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(availableTopicLabel, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
						.addComponent(topicLabel, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
						.addComponent(chosenTopicLabel, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_formsPanel.createSequentialGroup()
							.addGap(4)
							.addComponent(chooseCustomerLabel, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)))
					.addGap(1)
					.addGroup(gl_formsPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(availableTopicsScrollPane, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_formsPanel.createSequentialGroup()
							.addGap(92)
							.addComponent(removeTopicButton, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_formsPanel.createSequentialGroup()
							.addGap(2)
							.addComponent(addTopicButton, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
						.addComponent(chosenTopicsScrollPane, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_formsPanel.createSequentialGroup()
							.addGap(6)
							.addComponent(commissionedByComboBox, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))))
		);
		formsPanel.setLayout(gl_formsPanel);
		
		// Panel di fondo del frame contenente icone e bottoni
		bottomPanel = new JPanel();
		bottomPanel.setBackground(Color.decode("#4C566A"));
		bottomPanel.setBounds(0, 425, 670, 117);
		contentPane.add(bottomPanel, BorderLayout.SOUTH);
		GridBagLayout gbl_bottomPanel = new GridBagLayout();
		gbl_bottomPanel.columnWidths = new int[]{70, 152, 0, 0, 112, 64, 0};
		gbl_bottomPanel.rowHeights = new int[]{72, 0};
		gbl_bottomPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_bottomPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		bottomPanel.setLayout(gbl_bottomPanel);
		
		// Bottone logout
		logoutButton = new JButton("");
		logoutButton.setIcon(new ImageIcon(NewProjectFrame.class.getResource("/logout.png")));
		logoutButton.setToolTipText("Logout");
		logoutButton.setForeground(new Color(46, 52, 64));
		logoutButton.setFont(new Font("Roboto", Font.PLAIN, 12));
		logoutButton.setFocusPainted(false);
		logoutButton.setContentAreaFilled(false);
		logoutButton.setBorderPainted(false);
		logoutButton.setBackground(new Color(235, 203, 139));
		GridBagConstraints gbc_logoutButton = new GridBagConstraints();
		gbc_logoutButton.anchor = GridBagConstraints.WEST;
		gbc_logoutButton.insets = new Insets(0, 0, 0, 5);
		gbc_logoutButton.gridx = 0;
		gbc_logoutButton.gridy = 0;
		bottomPanel.add(logoutButton, gbc_logoutButton);
		
		logoutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent clickLogout) {
				c.backToLogin(utility);
			}
		});
		
		// Bottone per tornare al frame precedente
		goBackButton = new JButton("Indietro");
		goBackButton.setForeground(Color.decode("#2E3440"));
		goBackButton.setFont(new Font("Roboto", Font.PLAIN, 16));
		goBackButton.setBackground(Color.decode("#EBCB8B"));
		goBackButton.setBorderPainted(false);
		goBackButton.setFocusPainted(false);
		GridBagConstraints gbc_goBackButton = new GridBagConstraints();
		gbc_goBackButton.insets = new Insets(0, 0, 0, 5);
		gbc_goBackButton.gridx = 2;
		gbc_goBackButton.gridy = 0;
		bottomPanel.add(goBackButton, gbc_goBackButton);
		
		goBackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent goBack) {
					c.goBack(utility);
			}
		});
		
		// Bottone per confermare la creazione del progetto
		newProjectButton = new JButton("Crea nuovo progetto");
		newProjectButton.setBackground(Color.decode("#EBCB8B"));
		newProjectButton.setBorderPainted(false);
		newProjectButton.setFocusPainted(false);
		newProjectButton.setForeground(Color.decode("#2E3440"));
		newProjectButton.setFont(new Font("Roboto", Font.PLAIN, 16));
		GridBagConstraints gbc_newProjectButton = new GridBagConstraints();
		gbc_newProjectButton.insets = new Insets(0, 0, 0, 5);
		gbc_newProjectButton.gridx = 3;
		gbc_newProjectButton.gridy = 0;
		bottomPanel.add(newProjectButton, gbc_newProjectButton);
		
		newProjectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent confirmNewProject) {
				int lastProject; // Variabile che contiene il codice del nuovo progetto
				
				if (chosenTopicsTable.getRowCount() > 0) { // Se è stato inserito almeno un ambito per il progetto
					ArrayList<String> chosenTopics = new ArrayList<String>(); // Contiene tutti gli ambiti scelti
					
					for (int i = 0; i < chosenTopicsTable.getRowCount(); i++) 
						chosenTopics.add(chosenTopicsTable.getValueAt(i, 0).toString());
						
					try {
						c.insertProject(signedInCompany.getVatNumber(), 
										typologyComboBox.getSelectedItem().toString(),
										Float.valueOf(budgetSpinner.getValue().toString()),
										societyRadioButton.isSelected() ? 
										commissionedByComboBox.getSelectedItem().toString().substring(0, 11) :
										commissionedByComboBox.getSelectedItem().toString().substring(0, 16));
						lastProject = c.pickNewestProject(signedInCompany.getVatNumber()); // Prende il codice del nuovo progetto
						c.insertProjectTopics(lastProject, chosenTopics); // Inserisce gli ambiti per il progetto
						c.chooseProjectManager(lastProject, managerCf); // Inserisce il project manager del progetto
						c.goBack(utility);
					} catch (Exception projectNotCreated) {
						c.openPopupDialog(utility, "Recupero delle informazioni necessarie fallito: riprova.");
					}
				}
				else
					c.openPopupDialog(utility, "Si prega di inserire almeno un ambito per il progetto");
			}
		});
		
		// Label icona decorativa
		JLabel iconLabel = new JLabel("");
		GridBagConstraints gbc_iconLabel = new GridBagConstraints();
		gbc_iconLabel.anchor = GridBagConstraints.NORTHWEST;
		gbc_iconLabel.gridx = 5;
		gbc_iconLabel.gridy = 0;
		bottomPanel.add(iconLabel, gbc_iconLabel);
		iconLabel.setIcon(new ImageIcon(NewProjectFrame.class.getResource("/pencil.png")));
		
		pack();
		setLocationRelativeTo(null);
	}
}

