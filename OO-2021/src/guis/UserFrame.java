package guis;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import controllers.Controller;
import entities.Employee;
import entities.EmployeeRating;
import entities.Meeting;
import entities.Topic;

import java.awt.Font;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Toolkit;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import javax.swing.BoxLayout;

public class UserFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	// Dichiarazioni utili
	private Controller c;
	private JPanel contentPane;
	private JPanel welcomeUserPanel;
	private JPanel meetingsPanel;
	private JPanel historyPanel;
	private JPanel bottomPanel;
	private JScrollPane meetingsScrollPane;
	private JScrollPane historyScrollPanel;
	private JTabbedPane userTabbedPane;
	private DefaultTableModel meetingsTM;
	private DefaultTableModel historyTM;
	private JTable meetingsTable;
	private JTable historyTable;
	private JLabel projectInfoLabel;
	private JButton logoutButton;
	private JButton signUoToMeetingButton;

	// Creazione frame
	public UserFrame(Controller co, Employee user) {
		c = co;
		JFrame utility = this;
		setResizable(false);
		setTitle("Homepage - Projesting");
		setIconImage(Toolkit.getDefaultToolkit().getImage(UserFrame.class.getResource("/bulb.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 741, 523);
		contentPane = new JPanel();
		contentPane.setBackground(Color.decode("#4C566A"));
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		
		// Panel contenente il titolo del frame
		welcomeUserPanel = new JPanel();
		welcomeUserPanel.setBackground(Color.decode("#434C5E"));
		
		// Label informativa
		JLabel welcomeUserLabel = new JLabel("<HTML> <center> Benvenuto, <br>" +user.getName()+ " " +user.getSurname()
									+ "! </center> </HTML>");
		welcomeUserLabel.setForeground(Color.decode("#EBCB8B"));
		welcomeUserLabel.setFont(new Font("Roboto", Font.PLAIN, 28));
		welcomeUserPanel.add(welcomeUserLabel);

		// Caratteristiche estetiche TabbedPane
		UIManager.put("TabbedPane.contentAreaColor", Color.decode("#ECEFF4"));
		UIManager.put("TabbedPane.selected", Color.decode("#5E81AC"));
		
		userTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		userTabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		userTabbedPane.setFont(new Font("Roboto", Font.PLAIN, 12));
		userTabbedPane.setForeground(Color.decode("#ECEFF4"));
		userTabbedPane.setBackground(Color.decode("#B48EAD"));
		
		// Panel che racchiude le informazioni sui meeting
		meetingsPanel = new JPanel();
		meetingsPanel.setBackground(Color.decode("#EBCB8B"));
		userTabbedPane.addTab("Progetto attuale", null, meetingsPanel, null);
		
		// ScrollPane contenente la tabella dei meeting
		meetingsScrollPane = new JScrollPane();
		meetingsScrollPane.setBounds(0, 57, 611, 163);
		meetingsScrollPane.setForeground(Color.decode("#434C5E"));
		meetingsScrollPane.setBackground(Color.decode("#434C5E"));
		meetingsScrollPane.setFont(new Font("Roboto", Font.PLAIN, 15));
		meetingsScrollPane.setBorder(new LineBorder(Color.decode("#434C5E"), 2, true));
		meetingsScrollPane.getViewport().setBackground(Color.decode("#D8DEE9"));
		meetingsPanel.setLayout(null);
		meetingsPanel.add(meetingsScrollPane);
		
		// Table model che contiene le informazioni sui meeting
		meetingsTM = new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
						"Data riunione", "Ora inizio", "Ora fine", "Luogo/Piattaforma", "Codice Meeting"
				}
			);
		
		
		// Recupero informazioni sui meeting
		for (Meeting m: user.getEmployeeMeetings()) {
			String meetingPlace = "";
			if (!m.isStarted() && !m.isEnded()) { // Impedisce la visualizzazione di meeting in corso o già finiti
				if (m.getMeetingPlatform() == null) // Controlla se il meeting si tiene in un luogo fisico o su una piattaforma telematica
					meetingPlace = m.getMeetingRoom();
				else
					meetingPlace = m.getMeetingPlatform();
			
				// Riempimento tabella con le informazioni utili
				meetingsTM.addRow(new Object[] {m.getMeetingDate(),
												m.getStartTime(),
												m.getEndTime(),
												meetingPlace,
												m.getMeetingNumber()});
			}
		}
		
		// Rende la table non editabile
		meetingsTable = new JTable(meetingsTM) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
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
		
		// Label con informazioni del progetto dell'utente (se esiste) con codice, tipologia e ambiti del progetto.
		projectInfoLabel = new JLabel("NESSUN PROGETTO ATTIVO"); // Testo di default
		projectInfoLabel.setBounds(0, 0, 566, 59);
		projectInfoLabel.setForeground(Color.decode("#434C5E"));
		projectInfoLabel.setVerticalAlignment(SwingConstants.TOP);
		projectInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		projectInfoLabel.setFont(new Font("Roboto", Font.BOLD, 15));
		meetingsPanel.add(projectInfoLabel);
		
		// Bottone per prenotarsi ad un meeting
		signUoToMeetingButton = new JButton("Partecipa ad un meeting");
		signUoToMeetingButton.setForeground(Color.decode("#2E3440"));
		signUoToMeetingButton.setBackground(Color.decode("#8FBCBB"));
		signUoToMeetingButton.setFocusPainted(false);
		signUoToMeetingButton.setBorderPainted(false);
		signUoToMeetingButton.setBounds(203, 227, 214, 23);
		signUoToMeetingButton.setFont(new Font("Roboto", Font.PLAIN, 14));
		
		signUoToMeetingButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				c.openChooseMeetingFrame(user); // Apre frame con tutti i meeting ai quali è possibile partecipare
			}
		});
		meetingsPanel.add(signUoToMeetingButton);
		
		// Recupero ambiti di un progetto e formattazione in stringa
		if (user.getEmployeeProject() != null) { // Se l'utente ha progetti a carico
			String topicToPrint = "";
			for (Topic t: user.getEmployeeProject().getProjectTopics()) {
				topicToPrint += t.getName() + ", ";
			}
			topicToPrint = topicToPrint.substring(0, (topicToPrint.length() - 2));
			
			// Recupero informazioni da visualizzare
			projectInfoLabel.setText("<HTML> <center> Codice Progetto: " + String.valueOf(user.getEmployeeProject().getProjectNumber()) 
			+ "<br> Tipologia: " + user.getEmployeeProject().getTypology().toString().replace('_', ' ')
			+ "<br> Ambiti: " + topicToPrint
			+ "</center> </HTML>");
		}
		else
			meetingsScrollPane.setVisible(false);
													
		// Panel che racchiude le informazioni sui progetti precedenti
		historyPanel = new JPanel();
		userTabbedPane.addTab("Cronologia progetti", null, historyPanel, null);
		historyPanel.setLayout(null);
		
		// ScrollPane contenente la tabella sui progetti passati
		historyScrollPanel = new JScrollPane();
		historyScrollPanel.setForeground(Color.decode("#434C5E"));
		historyScrollPanel.setBackground(Color.decode("#434C5E"));
		historyScrollPanel.setFont(new Font("Roboto", Font.PLAIN, 15));
		historyScrollPanel.setBorder(new LineBorder(Color.decode("#434C5E"), 2, true));
		historyScrollPanel.getViewport().setBackground(Color.decode("#D8DEE9"));
		historyScrollPanel.setBounds(0, 0, 611, 258);
		historyPanel.add(historyScrollPanel);
		
		// Table model che contiene le informazioni sui progetti passati
		historyTM = new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Codice Progetto", "Tipologia", "Valutazione"
			}
		);
		
		// Riempimento della tabella con le informazioni utili
		for (EmployeeRating er: user.getEmployeeRatings()) {
			historyTM.addRow(new Object[] {er.getPastProject().getProjectNumber(),
										   er.getPastProject().getTypology().toString().replace('_', ' '),
										   er.getRating() == 0 ? "Non presente" : er.getRating()});
		}
		
		// Rende la table non editabile
		historyTable = new JTable(historyTM) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		// Caratteristiche estetiche della JTable
		historyTable.setBackground(Color.decode("#ECEFF4"));
		historyTable.setRowMargin(2);
		historyTable.setRowHeight(24);
		historyTable.setFont(new Font("Roboto", Font.PLAIN, 14));
		historyTable.setForeground(Color.decode("#434C5E"));
		historyTable.setGridColor(Color.decode("#B48EAD"));
		historyTable.getTableHeader().setBackground(Color.decode("#B48EAD"));
		historyTable.getTableHeader().setForeground(Color.decode("#ECEFF4"));
		historyTable.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 14));
		historyTable.getTableHeader().setReorderingAllowed(false);
		
		historyScrollPanel.setViewportView(historyTable);
		
		// Panel di fondo del frame contenente icone e bottoni
		bottomPanel = new JPanel();
		bottomPanel.setBackground(Color.decode("#4C566A"));
		
		// Layout utilizzato
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(bottomPanel, GroupLayout.DEFAULT_SIZE, 735, Short.MAX_VALUE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addComponent(welcomeUserPanel, GroupLayout.DEFAULT_SIZE, 745, Short.MAX_VALUE)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(64)
					.addComponent(userTabbedPane, GroupLayout.PREFERRED_SIZE, 616, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(65, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(welcomeUserPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(userTabbedPane, GroupLayout.PREFERRED_SIZE, 286, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
					.addComponent(bottomPanel, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE))
		);
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
		
		// Bottone logout
		logoutButton = new JButton("");
		bottomPanel.add(logoutButton);
		logoutButton.setToolTipText("Logout");
		logoutButton.setFocusPainted(false);
		logoutButton.setContentAreaFilled(false);
		logoutButton.setIcon(new ImageIcon(UserFrame.class.getResource("/logout.png")));
		
		logoutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent clickLogin) { // Al click del tasto logout, si tornerà alla schermata principale
				c.backToLogin(utility);
			}
		});
		logoutButton.setFont(new Font("Roboto", Font.PLAIN, 12));
		logoutButton.setBorderPainted(false);
		logoutButton.setBackground(Color.decode("#EBCB8B"));
		logoutButton.setForeground(Color.decode("#2E3440"));
		
		// Label con nome dell'azienda che impiega l'utente
		JLabel companyNameLabel = new JLabel("<HTML> <center> Azienda: <br>" +user.getHiredBy().getName()+  "<center> <HTML>");
		bottomPanel.add(companyNameLabel);
		companyNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		companyNameLabel.setVerticalAlignment(SwingConstants.TOP);
		companyNameLabel.setForeground(Color.decode("#EBCB8B"));
		companyNameLabel.setFont(new Font("Roboto", Font.PLAIN, 18));
		
		// Label con salario medio dell'utente
		JLabel wageLabel = new JLabel(String.valueOf("<HTML> <center> Salario medio: <br>" +(int)user.getAvgWage())+ " € <center> <HTML>");
		bottomPanel.add(wageLabel);
		wageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		wageLabel.setForeground(Color.decode("#EBCB8B"));
		wageLabel.setFont(new Font("Roboto", Font.PLAIN, 18));
		
		// Icona utente
		JLabel userIconLabel = new JLabel("");
		bottomPanel.add(userIconLabel);
		userIconLabel.setIcon(new ImageIcon(UserFrame.class.getResource("/cv.png")));
		contentPane.setLayout(gl_contentPane);
		
		pack();
		setLocationRelativeTo(null);
	}
}
