package guis;

import java.awt.BorderLayout;
import java.awt.EventQueue;

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

public class ProjectManagerFrame extends JFrame {

	private JPanel contentPane;
	private Controller c;
	private JTable teamTable;
	private JTable meetingsTable;
	private JTabbedPane teamTabbedPane;
	private JPanel teamPanel;
	private JScrollPane teamScrollPane;
	private JScrollPane meetingsScrollPane;
	private DefaultTableModel freeEmployeesTM;
	private DefaultTableModel meetingsTM;
	private JButton logoutButton;

	// Creazione frame
	public ProjectManagerFrame(Controller co, Employee manager) throws Exception {
		setResizable(false);
		setTitle("Homepage - Projesting");
		setIconImage(Toolkit.getDefaultToolkit().getImage(ProjectManagerFrame.class.getResource("/bulb.png")));
		c = co;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 740, 544);
		contentPane = new JPanel();
		contentPane.setBackground(Color.decode("#4C566A"));
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		
		// Panel project manager
		JPanel welcomeManagerPanel = new JPanel();
		welcomeManagerPanel.setBackground(Color.decode("#434C5E"));
		
		// Label di benvenuto
		JLabel welcomeManagerLabel = new JLabel("<HTML> <center> Benvenuto, <br>"+ manager.getName()+ " " +manager.getSurname()
									+ "! </center> </HTML>");
		welcomeManagerLabel.setForeground(Color.decode("#EBCB8B"));
		welcomeManagerLabel.setFont(new Font("Roboto", Font.PLAIN, 28));
		welcomeManagerPanel.add(welcomeManagerLabel);
		
		// Label icona
		JLabel managerIconLabel = new JLabel("");
		managerIconLabel.setIcon(new ImageIcon(ProjectManagerFrame.class.getResource("/laptop.png")));
		
		UIManager.put("TabbedPane.contentAreaColor", Color.decode("#ECEFF4"));
		UIManager.put("TabbedPane.selected", Color.decode("#5E81AC"));
		
		logoutButton = new JButton("");
		logoutButton.setContentAreaFilled(false);
		logoutButton.setIcon(new ImageIcon(ProjectManagerFrame.class.getResource("/logout.png")));
		JFrame loggingOut = this;
		logoutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent clickLogout) { // Al click del tasto logout, si tornerà alla schermata principale
				c.backToLogin(loggingOut);
			}
		});
		logoutButton.setForeground(new Color(46, 52, 64));
		logoutButton.setFont(new Font("Roboto", Font.PLAIN, 12));
		logoutButton.setBorderPainted(false);
		logoutButton.setBackground(new Color(235, 203, 139));
		
		teamTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		teamTabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		teamTabbedPane.setFont(new Font("Roboto", Font.PLAIN, 12));
		teamTabbedPane.setForeground(Color.decode("#ECEFF4"));
		teamTabbedPane.setBackground(Color.decode("#B48EAD"));
		
		JFrame toEnable = this;
		
		JButton closeProjectButton = new JButton("Chiudi progetto");
		closeProjectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent clickEndProject) {
				try {
					c.closeProject(manager.getEmployeeProject().getProjectNumber());
					c.openPopupDialog(loggingOut, "Progetto terminato con successo!");
					
					//FA TORNARE ALLA PAGINA DI SOTTO, AGGIUSTA
					//c.backToLogin???
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		closeProjectButton.setForeground(Color.decode("#2E3440"));
		closeProjectButton.setBackground(Color.decode("#EBCB8B"));
		closeProjectButton.setBorderPainted(false);
		closeProjectButton.setFocusPainted(false);
		closeProjectButton.setFont(new Font("Roboto", Font.PLAIN, 14));
		
		JButton newMeetingButton = new JButton("Organizza un meeting");
		newMeetingButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent clickNewMeeting) {
				c.openNewMeetingFrame(manager.getEmployeeProject().getProjectNumber());
			}
		});
		newMeetingButton.setForeground(Color.decode("#2E3440"));
		newMeetingButton.setBackground(Color.decode("#EBCB8B"));
		newMeetingButton.setFont(new Font("Roboto", Font.PLAIN, 14));
		newMeetingButton.setFocusPainted(false);
		newMeetingButton.setBorderPainted(false);
		
		JLabel projectLabel = new JLabel("Codice progetto a carico: " +manager.getEmployeeProject().getProjectNumber());
		projectLabel.setHorizontalAlignment(SwingConstants.CENTER);
		projectLabel.setFont(new Font("Roboto", Font.PLAIN, 18));
		projectLabel.setForeground(Color.decode("#EBCB8B"));
		
		JLabel lblNewLabel = new JLabel("Per aggiungere uno o pi\u00F9 progettisti al team, selezionali dalla tabella e clicca su \"Aggiungi al team\".");
		lblNewLabel.setForeground(Color.decode("#EBCB8B"));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addComponent(welcomeManagerPanel, GroupLayout.DEFAULT_SIZE, 734, Short.MAX_VALUE)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(logoutButton, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 227, Short.MAX_VALUE)
					.addComponent(newMeetingButton)
					.addGap(50)
					.addComponent(closeProjectButton)
					.addGap(33)
					.addComponent(managerIconLabel)
					.addContainerGap())
				.addComponent(projectLabel, GroupLayout.DEFAULT_SIZE, 734, Short.MAX_VALUE)
				.addComponent(lblNewLabel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 734, Short.MAX_VALUE)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap(54, Short.MAX_VALUE)
					.addComponent(teamTabbedPane, GroupLayout.PREFERRED_SIZE, 636, GroupLayout.PREFERRED_SIZE)
					.addGap(44))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(welcomeManagerPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(projectLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(teamTabbedPane, GroupLayout.PREFERRED_SIZE, 295, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(29)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(logoutButton, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
									.addComponent(closeProjectButton)
									.addComponent(newMeetingButton, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(managerIconLabel)))
					.addContainerGap())
		);
		
		teamPanel = new JPanel();
		teamPanel.setBackground(Color.decode("#EBCB8B"));
		teamTabbedPane.addTab("Dipendenti liberi", null, teamPanel, null);
		teamPanel.setLayout(null);
		
		//Caratteristiche dello ScrollPane 
		teamScrollPane = new JScrollPane();
		teamScrollPane.setForeground(Color.decode("#434C5E"));
		teamScrollPane.setBackground(Color.decode("#434C5E"));
		teamScrollPane.setFont(new Font("Roboto", Font.PLAIN, 15));
		teamScrollPane.setBorder(new LineBorder(Color.decode("#434C5E"), 2, true));
		teamScrollPane.getViewport().setBackground(Color.decode("#D8DEE9"));
		teamScrollPane.setBounds(0, 0, 631, 229);
		teamPanel.add(teamScrollPane);
		
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
			if (pm.getEmployeeProject().getProjectNumber() == 0) {
				int avgRating = c.takeRatingForEmployee(pm.getFiscalCode());	
				freeEmployeesTM.addRow(new Object[] {pm.getFiscalCode(),
												 pm.getName(),
												 pm.getSurname(),
												 (int)pm.getAvgWage() +" €",
												 avgRating == 0 ? "Non presente" : avgRating +"/5",
												 pm.getRole()});
			}
		}
		
		
		// Rende la table non editabile, fatta eccezione per il ruolo
		teamTable = new JTable(freeEmployeesTM) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 5;
			}
		};
		
		teamTable.setBackground(Color.decode("#ECEFF4"));
		teamTable.setRowMargin(2);
		teamTable.setRowHeight(24);
		teamTable.setFont(new Font("Roboto", Font.PLAIN, 14));
		teamTable.setForeground(Color.decode("#434C5E"));
		teamTable.setGridColor(Color.decode("#B48EAD"));
		teamTable.getTableHeader().setBackground(Color.decode("#B48EAD"));
		teamTable.getTableHeader().setForeground(Color.decode("#ECEFF4"));
		teamTable.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 14));
		teamTable.getTableHeader().setReorderingAllowed(false);
		teamScrollPane.setViewportView(teamTable);
		JButton addToTeamButton = new JButton("Aggiungi al team");
		addToTeamButton.setBounds(240, 235, 136, 25);
		teamPanel.add(addToTeamButton);
		addToTeamButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent clickAddToTeam) {
				ArrayList<Employee> toAdd = new ArrayList<Employee>();
				try {
					for (int i = 0; i < teamTable.getRowCount(); i++) {
						if (teamTable.getValueAt(i, 5) != null) {
							toAdd.add(new Employee(teamTable.getValueAt(i, 0).toString(),
									  null, null, null, 
									  EnumRole.valueOf(teamTable.getValueAt(i, 5).toString().replace(' ', '_')), 
									  0, null, 
									  manager.getEmployeeProject(), 
									  null, null));
							freeEmployeesTM.removeRow(i);
						}
						else
							c.openPopupDialog(toEnable, "Nessun progettista selezionato");
						
					}
					c.addToTeam(toAdd);
				} catch (IllegalArgumentException invalidRole) {
					c.openPopupDialog(toEnable, "Ruolo non valido, riprova");
				} catch (Exception e) {
					e.printStackTrace();
				}
			
			}
		});
		addToTeamButton.setForeground(Color.decode("#2E3440"));
		addToTeamButton.setBackground(Color.decode("#8FBCBB"));
		addToTeamButton.setBorderPainted(false);
		addToTeamButton.setFocusPainted(false);
		addToTeamButton.setFont(new Font("Roboto", Font.PLAIN, 14));
		
		JPanel meetingPanel = new JPanel();
		meetingPanel.setBackground(Color.decode("#EBCB8B"));
		teamTabbedPane.addTab("Gestione meeting", null, meetingPanel, null);
		meetingPanel.setLayout(null);
		
		//Caratteristiche dello ScrollPane 
		meetingsScrollPane = new JScrollPane();
		meetingsScrollPane.setForeground(Color.decode("#434C5E"));
		meetingsScrollPane.setBackground(Color.decode("#434C5E"));
		meetingsScrollPane.setFont(new Font("Roboto", Font.PLAIN, 15));
		meetingsScrollPane.setBorder(new LineBorder(Color.decode("#434C5E"), 2, true));
		meetingsScrollPane.getViewport().setBackground(Color.decode("#D8DEE9"));
		meetingsScrollPane.setBounds(0, 0, 631, 229);
		meetingPanel.add(meetingsScrollPane);
				
		// Table model che contiene le informazioni sui meeting
		meetingsTM = new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"Data riunione", "Ora inizio", "Ora fine", "Luogo/Piattaforma", "Iniziato", "Finito"
				}
			);
				
		// Recupero informazioni sui meeting
		
		for (Meeting m: manager.getEmployeeMeetings()) {
			String meetingPlace = "";
			// Impedisce la visualizzazione di meeting in corso o già finiti
			if (m.getMeetingPlatform() == null) // Controlla se il meeting si tiene in un luogo fisico o su una piattaforma telematica
				meetingPlace = m.getMeetingRoom();
			else
				meetingPlace = m.getMeetingPlatform();
		
			// Riempimento tabella con le informazioni utili
			meetingsTM.addRow(new Object[] {m.getMeetingDate(),
											m.getStartTime(),
											m.getEndTime(),
											meetingPlace});
			
		}
				
				
		// Rende la table non editabile, fatta eccezione per il ruolo
		meetingsTable = new JTable(meetingsTM) {
//			@Override
//			public boolean isCellEditable(int row, int column) {
//				if (meetingsTable.getValueAt(row, column) != null)
//					return false;
//				return column == 4 && column == 5;
//			}
		};
		
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
		
		contentPane.setLayout(gl_contentPane);
		
		pack();
		setLocationRelativeTo(null);
	}
}
