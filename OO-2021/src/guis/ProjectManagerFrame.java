package guis;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import controllers.Controller;
import entities.Employee;
import entities.Meeting;
import enums.EnumRole;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.Font;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Toolkit;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.SwingConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class ProjectManagerFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	// Dichiarazioni utili
	private Controller c;
	private JPanel contentPane;
	private JPanel teamPanel;
	private JPanel titlePanel;
	private JPanel bottomPanel;
	private JPanel meetingPanel;
	private JTabbedPane teamTabbedPane;
	private JScrollPane freeEmployeesScrollPane;
	private JScrollPane meetingsScrollPane;
	private DefaultTableModel freeEmployeesTM;
	private DefaultTableModel meetingsTM;
	private JTable freeEmployeesTable;
	private JTable meetingsTable;
	private JButton logoutButton;
	private JButton newMeetingButton;
	private JButton closeProjectButton;
	private JButton addToTeamButton;
	private JButton updateMeetingStatusButton;

	// Creazione frame
	public ProjectManagerFrame(Controller co, Employee manager) throws Exception {
		c = co;
		JFrame utility = this;
		setResizable(false);
		setTitle("Homepage - Projesting");
		setIconImage(Toolkit.getDefaultToolkit().getImage(ProjectManagerFrame.class.getResource("/bulb.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 756, 593);
		contentPane = new JPanel();
		contentPane.setBackground(Color.decode("#4C566A"));
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		
		// Panel contenente il titolo del frame
		titlePanel = new JPanel();
		titlePanel.setBackground(Color.decode("#434C5E"));
		
		// Label contenente il titolo del frame
		JLabel welcomeManagerLabel = new JLabel("<HTML> <center> Benvenuto, <br>"+ manager.getName()+ " " +manager.getSurname()
											  + "! </center> </HTML>");
		welcomeManagerLabel.setForeground(Color.decode("#EBCB8B"));
		welcomeManagerLabel.setFont(new Font("Roboto", Font.PLAIN, 28));
		titlePanel.add(welcomeManagerLabel);
		
		// Caratteristiche estetiche TabbedPane
		UIManager.put("TabbedPane.contentAreaColor", Color.decode("#ECEFF4"));
		UIManager.put("TabbedPane.selected", Color.decode("#5E81AC"));
		
		// TabbedPane contenente informazioni utili al project manager
		teamTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		teamTabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		teamTabbedPane.setFont(new Font("Roboto", Font.PLAIN, 12));
		teamTabbedPane.setForeground(Color.decode("#ECEFF4"));
		teamTabbedPane.setBackground(Color.decode("#B48EAD"));
		
		// Label informativa
		JLabel projectLabel = new JLabel("Codice progetto a carico: " +manager.getEmployeeProject().getProjectNumber());
		projectLabel.setHorizontalAlignment(SwingConstants.CENTER);
		projectLabel.setFont(new Font("Roboto", Font.PLAIN, 18));
		projectLabel.setForeground(Color.decode("#EBCB8B"));
		
		// Label informativa
		JLabel infoLabel = new JLabel("<HTML> <p> <center> Per aggiungere uno o pi\u00F9 progettisti al team, "
										+ "selezionali dalla tabella e clicca su \"Aggiungi al team\". <br> "
										+ "Ricordati di inserire un ruolo per ognuno di essi. <br> "
										+ "Per avere informazioni sui progetti passati di un dipendente fai doppio click sulla riga "
										+ "corrispondente. "
										+ "</center> </p> </HTML>");
		infoLabel.setForeground(Color.decode("#EBCB8B"));
		infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		infoLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
		
		// Panel di fondo del frame contenente icone e bottoni
		bottomPanel = new JPanel();
		bottomPanel.setBackground(Color.decode("#4C566A"));
		
		// Layout utilizzato
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(titlePanel, GroupLayout.DEFAULT_SIZE, 750, Short.MAX_VALUE)
				.addComponent(projectLabel, GroupLayout.DEFAULT_SIZE, 750, Short.MAX_VALUE)
				.addComponent(bottomPanel, GroupLayout.PREFERRED_SIZE, 750, GroupLayout.PREFERRED_SIZE)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(infoLabel, GroupLayout.DEFAULT_SIZE, 730, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(37)
					.addComponent(teamTabbedPane, GroupLayout.PREFERRED_SIZE, 675, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(38, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(titlePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(projectLabel)
					.addPreferredGap(ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
					.addComponent(teamTabbedPane, GroupLayout.PREFERRED_SIZE, 312, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(infoLabel, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE)
					.addGap(31)
					.addComponent(bottomPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
		);
		GridBagLayout gbl_bottomPanel = new GridBagLayout();
		gbl_bottomPanel.columnWidths = new int[]{86, 152, 0, 0, 125, 83, 0};
		gbl_bottomPanel.rowHeights = new int[]{64, 0};
		gbl_bottomPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_bottomPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		bottomPanel.setLayout(gbl_bottomPanel);
		
		// Bottone logout
		logoutButton = new JButton("");
		logoutButton.setForeground(new Color(46, 52, 64));
		logoutButton.setFont(new Font("Roboto", Font.PLAIN, 12));
		logoutButton.setBorderPainted(false);
		logoutButton.setBackground(new Color(235, 203, 139));
		GridBagConstraints gbc_logoutButton = new GridBagConstraints();
		gbc_logoutButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_logoutButton.insets = new Insets(0, 0, 0, 5);
		gbc_logoutButton.gridx = 0;
		gbc_logoutButton.gridy = 0;
		bottomPanel.add(logoutButton, gbc_logoutButton);
		logoutButton.setContentAreaFilled(false);
		logoutButton.setIcon(new ImageIcon(ProjectManagerFrame.class.getResource("/logout.png")));
		
		logoutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent clickLogout) { // Al click del tasto logout, si tornerà alla schermata principale
				c.backToLogin(utility);
			}
		});
		
		// Bottone per programmare un nuovo meeting
		newMeetingButton = new JButton("Organizza un meeting");
		newMeetingButton.setForeground(Color.decode("#2E3440"));
		newMeetingButton.setBackground(Color.decode("#EBCB8B"));
		newMeetingButton.setFont(new Font("Roboto", Font.PLAIN, 14));
		newMeetingButton.setFocusPainted(false);
		newMeetingButton.setBorderPainted(false);
		GridBagConstraints gbc_newMeetingButton = new GridBagConstraints();
		gbc_newMeetingButton.insets = new Insets(0, 0, 0, 5);
		gbc_newMeetingButton.gridx = 2;
		gbc_newMeetingButton.gridy = 0;
		bottomPanel.add(newMeetingButton, gbc_newMeetingButton);
		
		newMeetingButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent clickNewMeeting) {
				c.openNewMeetingFrame(manager.getEmployeeProject().getProjectNumber(), manager.getFiscalCode());
			}
		});
		
		// Bottone per concludere il progetto a carico del project manager
		closeProjectButton = new JButton("Chiudi progetto");
		closeProjectButton.setForeground(Color.decode("#2E3440"));
		closeProjectButton.setBackground(Color.decode("#EBCB8B"));
		closeProjectButton.setBorderPainted(false);
		closeProjectButton.setFocusPainted(false);
		closeProjectButton.setFont(new Font("Roboto", Font.PLAIN, 14));
		GridBagConstraints gbc_closeProjectButton = new GridBagConstraints();
		gbc_closeProjectButton.insets = new Insets(0, 0, 0, 5);
		gbc_closeProjectButton.gridx = 3;
		gbc_closeProjectButton.gridy = 0;
		bottomPanel.add(closeProjectButton, gbc_closeProjectButton);
		
		closeProjectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent clickEndProject) {
				try {
					manager.getEmployeeProject().setProjectEmployees(c.refillTeam(manager));
					c.closeProject(manager.getEmployeeProject().getProjectNumber());
					c.openRatingDialog(manager.getEmployeeProject().getProjectNumber(), manager.getEmployeeProject().getProjectEmployees(), utility);
				} catch (Exception genericError) {
					c.openPopupDialog(utility, "Errore nella chiusura del progetto.");
				}
			}
		});
		
		// Label icona
		JLabel managerIconLabel = new JLabel("");
		GridBagConstraints gbc_managerIconLabel = new GridBagConstraints();
		gbc_managerIconLabel.anchor = GridBagConstraints.NORTH;
		gbc_managerIconLabel.gridx = 5;
		gbc_managerIconLabel.gridy = 0;
		bottomPanel.add(managerIconLabel, gbc_managerIconLabel);
		managerIconLabel.setIcon(new ImageIcon(ProjectManagerFrame.class.getResource("/laptop.png")));
		
		// Panel che racchiude le informazioni sui dipendenti
		teamPanel = new JPanel();
		teamPanel.setBackground(Color.decode("#EBCB8B"));
		teamTabbedPane.addTab("Dipendenti liberi", null, teamPanel, null);
		teamPanel.setLayout(null);
		
		// ScrollPane contenente la tabella con i dipendenti liberi
		freeEmployeesScrollPane = new JScrollPane();
		freeEmployeesScrollPane.setForeground(Color.decode("#434C5E"));
		freeEmployeesScrollPane.setBackground(Color.decode("#434C5E"));
		freeEmployeesScrollPane.setFont(new Font("Roboto", Font.PLAIN, 15));
		freeEmployeesScrollPane.setBorder(new LineBorder(Color.decode("#434C5E"), 2, true));
		freeEmployeesScrollPane.getViewport().setBackground(Color.decode("#D8DEE9"));
		freeEmployeesScrollPane.setBounds(0, 0, 670, 229);
		teamPanel.add(freeEmployeesScrollPane);
		
		// Table model che contiene le informazioni sui dipendenti liberi
		freeEmployeesTM = new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"Codice Fiscale", "Nome", "Cognome", "Salario medio", "Valutazione media", "Ruolo"
				}
			);
		
		// Recupero informazioni sui dipendenti liberi
		for (Employee pm: manager.getHiredBy().getCompanyEmployees()) {
			if (pm.getEmployeeProject().getProjectNumber() == 0) { // Se un dipendente è libero
				int avgRating = c.takeRatingForEmployee(pm.getFiscalCode()); // Calcola valutazione media per ciascun dipendente
				freeEmployeesTM.addRow(new Object[] {pm.getFiscalCode(),
												 pm.getName(),
												 pm.getSurname(),
												 (int)pm.getAvgWage() +" €",
												 avgRating == 0 ? "Non presente" : avgRating +"/5",
												 pm.getRole()});
			}
		}
		
		
		// Rende la table non editabile, fatta eccezione per il ruolo, che il project manager deve specificare prima di aggiungere al team
		freeEmployeesTable = new JTable(freeEmployeesTM) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 5;
			}
		};
		
		// MouseListener che permette al project manager di visualizzare informazioni sui progetti passati di un dipendente facendo doppio 
		// click con il mouse sulla riga corrispondente
		freeEmployeesTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent clickOnEmployee) {
				if (freeEmployeesTable.getSelectedColumn() != 5) {
			        if(clickOnEmployee.getClickCount() >= 2){
			            try {
							c.openEmployeeInfoDialog(freeEmployeesTable.getValueAt(freeEmployeesTable.getSelectedRow(), 0).toString(), utility);
						} catch (Exception historyNotFound) {
							c.openPopupDialog(utility, "Recupero informazioni fallito; riprova.");
						}
			        }
				}
			}
		});
		
		// Caratteristiche estetiche della JTable
		freeEmployeesTable.setBackground(Color.decode("#ECEFF4"));
		freeEmployeesTable.setRowMargin(2);
		freeEmployeesTable.setRowHeight(24);
		freeEmployeesTable.setFont(new Font("Roboto", Font.PLAIN, 14));
		freeEmployeesTable.setForeground(Color.decode("#434C5E"));
		freeEmployeesTable.setGridColor(Color.decode("#B48EAD"));
		freeEmployeesTable.getTableHeader().setBackground(Color.decode("#B48EAD"));
		freeEmployeesTable.getTableHeader().setForeground(Color.decode("#ECEFF4"));
		freeEmployeesTable.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 14));
		freeEmployeesTable.getTableHeader().setReorderingAllowed(false);
		freeEmployeesScrollPane.setViewportView(freeEmployeesTable);
		
		// Bottone per aggiungere un dipendente al team
		addToTeamButton = new JButton("Aggiungi al team");
		addToTeamButton.setBounds(241, 240, 195, 32);
		addToTeamButton.setForeground(Color.decode("#2E3440"));
		addToTeamButton.setBackground(Color.decode("#8FBCBB"));
		addToTeamButton.setBorderPainted(false);
		addToTeamButton.setFocusPainted(false);
		addToTeamButton.setFont(new Font("Roboto", Font.PLAIN, 16));
		teamPanel.add(addToTeamButton);
		
		addToTeamButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent clickAddToTeam) {
				try {
					ArrayList<Employee> toAdd = new ArrayList<Employee>(); // Dipendenti aggiunti al team
					for (int i = 0; i < freeEmployeesTable.getRowCount(); i++) {
						if (freeEmployeesTable.getValueAt(i, 5) != null) { // Se è stato specificato un ruolo per il dipendente
							if (!freeEmployeesTable.getValueAt(i, 5).toString().isBlank()) {
								toAdd.add(new Employee(freeEmployeesTable.getValueAt(i, 0).toString(),
										  null, null, null, 
										  EnumRole.valueOf(freeEmployeesTable.getValueAt(i, 5).toString().replace(' ', '_')), 
										  0, null, 
										  manager.getEmployeeProject(), 
										  null, null));
							}
						}
					}
					c.addToTeam(toAdd); // Aggiunge dipendente al team
				} catch (IllegalArgumentException invalidRole) {
					invalidRole.printStackTrace();
					c.openPopupDialog(utility, "Ruolo non valido, riprova");
				} catch (Exception addFailed) {
					addFailed.printStackTrace();
					c.openPopupDialog(utility, "Aggiunta al team fallita; riprova.");
				}
			}
		});
		
		// Panel che racchiude le informazioni sui meeting
		meetingPanel = new JPanel();
		meetingPanel.setBackground(Color.decode("#EBCB8B"));
		teamTabbedPane.addTab("Gestione meeting", null, meetingPanel, null);
		meetingPanel.setLayout(null);
		
		// ScrollPane contenente le informazioni sui meeting
		meetingsScrollPane = new JScrollPane();
		meetingsScrollPane.setForeground(Color.decode("#434C5E"));
		meetingsScrollPane.setBackground(Color.decode("#434C5E"));
		meetingsScrollPane.setFont(new Font("Roboto", Font.PLAIN, 15));
		meetingsScrollPane.setBorder(new LineBorder(Color.decode("#434C5E"), 2, true));
		meetingsScrollPane.getViewport().setBackground(Color.decode("#D8DEE9"));
		meetingsScrollPane.setBounds(0, 0, 670, 229);
		meetingPanel.add(meetingsScrollPane);
				
		// Table model che contiene le informazioni sui meeting
		meetingsTM = new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"Data riunione", "Ora inizio", "Ora fine", "Luogo/Piattaforma", "Iniziato", "Finito", "Codice Meeting"
				}
			);
				
		// Recupero informazioni sui meeting
		for (Meeting m: manager.getEmployeeMeetings()) {
			String meetingPlace = "";
			
			if (m.getMeetingPlatform() == null) // Controlla se il meeting si tiene in un luogo fisico o su una piattaforma telematica
				meetingPlace = m.getMeetingRoom();
			else
				meetingPlace = m.getMeetingPlatform();
		
			// Riempimento tabella con le informazioni utili
			meetingsTM.addRow(new Object[] {m.getMeetingDate(),
											m.getStartTime(),
											m.getEndTime(),
											meetingPlace,
											m.isStarted() ? "Sì" : "No",
											m.isEnded() ? "Sì" : "No",
											m.getMeetingNumber()});
			
		}
				
				
		// Rende la table non editabile, fatta eccezione per lo stato del meeting (iniziato e finito), che il project manager può aggiornare
		meetingsTable = new JTable(meetingsTM) {
			@Override
			public boolean isCellEditable(int row, int column) {
					return column == 4 || column == 5;
			}
		};
		
		// Caratteristiche estetiche della JTable
		meetingsTable.setBackground(Color.decode("#ECEFF4"));
		meetingsTable.setRowMargin(2);
		meetingsTable.setRowHeight(24);
		meetingsTable.setFont(new Font("Roboto", Font.PLAIN, 14));
		meetingsTable.setForeground(Color.decode("#434C5E"));
		meetingsTable.setGridColor(Color.decode("#B48EAD"));
		meetingsTable.getTableHeader().setBackground(Color.decode("#B48EAD"));
		meetingsTable.getTableHeader().setForeground(Color.decode("#ECEFF4"));
		meetingsTable.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 14));
		meetingsTable.getTableHeader().setReorderingAllowed(false);
		meetingsScrollPane.setViewportView(meetingsTable);
		
		// Bottone per aggiornare stato di un meeting
		updateMeetingStatusButton = new JButton("Aggiorna stato meeting");
		updateMeetingStatusButton.setForeground(new Color(46, 52, 64));
		updateMeetingStatusButton.setFont(new Font("Roboto", Font.PLAIN, 14));
		updateMeetingStatusButton.setFocusPainted(false);
		updateMeetingStatusButton.setBorderPainted(false);
		updateMeetingStatusButton.setBackground(new Color(143, 188, 187));
		updateMeetingStatusButton.setBounds(226, 240, 214, 32);
		
		updateMeetingStatusButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent updateMeetingStatus) {
				if (meetingsTable.getRowCount() != -1) { // Se sono presenti dei meeting
					ArrayList<Meeting> meetings = new ArrayList<Meeting>(); // Contiene tutti i meeting
					
					for (int i = 0; i < meetingsTable.getRowCount(); i++) {
						Meeting m = new Meeting(Integer.valueOf(meetingsTable.getValueAt(i, 6).toString()), 
												null, null, null, null, null,
												meetingsTable.getValueAt(i, 4).toString().equals("Sì") ? true : false,
												meetingsTable.getValueAt(i, 5).toString().equals("Sì") ? true : false, 
												null, null);
						
						if (m.isStarted() && m.isEnded()) // Se un meeting è iniziato e finito, si rimuove dalla tabella
							meetingsTM.removeRow(i);
						
						meetings.add(m); // Aggiunge ciascun meeting all'ArrayList
					}
					
					try {
						c.insertMeetingUpdates(meetings);
					} catch (Exception updateFailed) {
						c.openPopupDialog(utility, "Aggiornamento fallito; un meeting non può essere finito se non inizia.");
					}
				}
			}
		});
		
		meetingPanel.add(updateMeetingStatusButton);
		contentPane.setLayout(gl_contentPane);
		
		pack();
		setLocationRelativeTo(null);
	}
}
