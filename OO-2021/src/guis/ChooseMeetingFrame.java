package guis;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import java.awt.Color;
import javax.swing.border.LineBorder;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import controllers.Controller;
import entities.Employee;
import entities.Meeting;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class ChooseMeetingFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	// Dichiarazioni utili
	private Controller c;
	private JPanel contentPane;
	private JPanel titlePanel;
	private JPanel bottomPanel;
	private JPanel meetingsInformationPanel;
	private JScrollPane openMeetingsScrollPane;
	private DefaultTableModel openMeetingsTM;
	private JTable openMeetingsTable;
	private JButton goBackButton;
	private JButton logoutButton;
	private JButton signUpToMeetingButton;

	// Creazione frame
	public ChooseMeetingFrame(Controller co, Employee user, DefaultTableModel meetingsTM) {
		c = co;
		JFrame utility = this;
		setResizable(false);
		setTitle("Partecipa a un meeting - Projesting");
		setIconImage(Toolkit.getDefaultToolkit().getImage(ChooseMeetingFrame.class.getResource("/bulb.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 743, 481);
		contentPane = new JPanel();
		contentPane.setBackground(Color.decode("#4C566A"));
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		// Panel che contiene il titolo del frame
		titlePanel = new JPanel();
		titlePanel.setBackground(Color.decode("#434C5E"));
		titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		contentPane.add(titlePanel, BorderLayout.NORTH);
		
		// Label per il titolo del frame
		JLabel titleLabel = new JLabel("Scegli a quale meeting partecipare");
		titleLabel.setIconTextGap(18);
		titleLabel.setIcon(new ImageIcon(ChooseMeetingFrame.class.getResource("/list.png")));
		titleLabel.setForeground(Color.decode("#EBCB8B"));
		titleLabel.setFont(new Font("Roboto", Font.PLAIN, 28));
		titlePanel.add(titleLabel);
		
		// Panel di fondo del frame che contiene icone e bottoni
		bottomPanel = new JPanel();
		bottomPanel.setBackground(Color.decode("#4C566A"));
		contentPane.add(bottomPanel, BorderLayout.SOUTH);
		GridBagLayout gbl_bottomPanel = new GridBagLayout();
		gbl_bottomPanel.columnWidths = new int[]{108, 170, 89, 92, 188, 84};
		gbl_bottomPanel.rowHeights = new int[]{27, 0};
		gbl_bottomPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		gbl_bottomPanel.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		bottomPanel.setLayout(gbl_bottomPanel);
		
		// Bottone per tornare al frame precedente
		goBackButton = new JButton("Indietro");
		goBackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent goBack) {
					c.updateChosenMeetings(meetingsTM); // Aggiorna i meeting scelti prima di mostrarli
					c.goBack(utility);
			}
		});
		
		// Bottone di logout
		logoutButton = new JButton("");
		logoutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent clickLogout) {
				System.exit(0);
			}
		});
		
		logoutButton.setIcon(new ImageIcon(ChooseMeetingFrame.class.getResource("/logout.png")));
		GridBagConstraints gbc_logoutButton = new GridBagConstraints();
		gbc_logoutButton.anchor = GridBagConstraints.WEST;
		gbc_logoutButton.insets = new Insets(0, 0, 0, 5);
		gbc_logoutButton.gridx = 0;
		gbc_logoutButton.gridy = 0;
		bottomPanel.add(logoutButton, gbc_logoutButton);
		logoutButton.setToolTipText("Logout");
		logoutButton.setForeground(new Color(46, 52, 64));
		logoutButton.setFont(new Font("Roboto", Font.PLAIN, 12));
		logoutButton.setFocusPainted(false);
		logoutButton.setContentAreaFilled(false);
		logoutButton.setBorderPainted(false);
		logoutButton.setBackground(new Color(235, 203, 139));
		goBackButton.setForeground(new Color(46, 52, 64));
		goBackButton.setFont(new Font("Roboto", Font.PLAIN, 14));
		goBackButton.setFocusPainted(false);
		goBackButton.setBorderPainted(false);
		goBackButton.setBackground(new Color(235, 203, 139));
		GridBagConstraints gbc_goBackButton = new GridBagConstraints();
		gbc_goBackButton.insets = new Insets(0, 0, 0, 5);
		gbc_goBackButton.gridx = 2;
		gbc_goBackButton.gridy = 0;
		bottomPanel.add(goBackButton, gbc_goBackButton);
		
		// Bottone che iscrive l'utente al meeting selezionato
		signUpToMeetingButton = new JButton("Partecipa");
		GridBagConstraints gbc_signUpToMeetingButton = new GridBagConstraints();
		gbc_signUpToMeetingButton.insets = new Insets(0, 0, 0, 5);
		gbc_signUpToMeetingButton.gridx = 3;
		gbc_signUpToMeetingButton.gridy = 0;
		bottomPanel.add(signUpToMeetingButton, gbc_signUpToMeetingButton);
		signUpToMeetingButton.setForeground(Color.decode("#2E3440"));
		signUpToMeetingButton.setFont(new Font("Roboto", Font.PLAIN, 14));
		signUpToMeetingButton.setFocusPainted(false);
		signUpToMeetingButton.setBorderPainted(false);
		signUpToMeetingButton.setBackground(Color.decode("#EBCB8B"));
		
		signUpToMeetingButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent clickAttend) {
				try {
					c.addEmployeeToMeeting(user.getFiscalCode(), Integer.valueOf(openMeetingsTable.getValueAt(openMeetingsTable.getSelectedRow(), 4).toString()));
					Meeting m = user.getEmployeeProject().getProjectMeetings().get(openMeetingsTable.getSelectedRow());
					String meetingPlace = "";
					if (!m.isStarted() && !m.isEnded()) { // Impedisce la visualizzazione di meeting in corso o già finiti
						if (m.getMeetingPlatform() == null) // Controlla se il meeting si tiene in un luogo fisico o su una piattaforma telematica
							meetingPlace = m.getMeetingRoom(); // Ne conserva il luogo
						else
							meetingPlace = m.getMeetingPlatform(); // Ne conserva la piattaforma
						
						meetingsTM.addRow(new Object[] {m.getMeetingDate(),
														m.getStartTime(),
														m.getEndTime(),
														meetingPlace,
														m.getMeetingNumber()});
					openMeetingsTM.removeRow(openMeetingsTable.getSelectedRow()); // Aggiorna la table
					}
				} catch (Exception e) {
					c.openPopupDialog(utility, "Ops! Qualcosa è andato storto; riprova.");
				}
			}
		});
		
		// Label per icona decorativa
		JLabel iconLabel = new JLabel("");
		GridBagConstraints gbc_iconLabel = new GridBagConstraints();
		gbc_iconLabel.gridx = 5;
		gbc_iconLabel.gridy = 0;
		bottomPanel.add(iconLabel, gbc_iconLabel);
		iconLabel.setIcon(new ImageIcon(ChooseMeetingFrame.class.getResource("/meeting.png")));
		
		// Panel che racchiude le informazioni su ciascun meeting
		meetingsInformationPanel = new JPanel();
		meetingsInformationPanel.setBackground(Color.decode("#4C566A"));
		contentPane.add(meetingsInformationPanel, BorderLayout.CENTER);
		
		// ScrollPane che contiene la tabella sui meeting
		openMeetingsScrollPane = new JScrollPane();
		openMeetingsScrollPane.setForeground(Color.decode("#434C5E"));
		openMeetingsScrollPane.setBackground(Color.decode("#434C5E"));
		openMeetingsScrollPane.setFont(new Font("Roboto", Font.PLAIN, 15));
		openMeetingsScrollPane.setBorder(new LineBorder(Color.decode("#434C5E"), 2, true));
		openMeetingsScrollPane.getViewport().setBackground(Color.decode("#D8DEE9"));
		
		// Table model che contiene le informazioni sui meeting
		openMeetingsTM = new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
						"Data riunione", "Ora inizio", "Ora fine", "Luogo/Piattaforma", "Codice Meeting"
				}
			);
		
		
		// Recupero informazioni sui meeting
		for (Meeting m: user.getEmployeeProject().getProjectMeetings()) {
			String meetingPlace = "";
			if (m.getMeetingPlatform() == null) // Controlla se il meeting si tiene in un luogo fisico o su una piattaforma telematica
				meetingPlace = m.getMeetingRoom();
			else
				meetingPlace = m.getMeetingPlatform();
		
			// Riempimento tabella con le informazioni utili
			openMeetingsTM.addRow(new Object[] {m.getMeetingDate(),
											m.getStartTime(),
											m.getEndTime(),
											meetingPlace,
											m.getMeetingNumber()});
		}
		
		// Rende la table non editabile
		openMeetingsTable = new JTable(openMeetingsTM) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		// Caratteristiche estetiche della JTable
		openMeetingsTable.setBackground(Color.decode("#ECEFF4"));
		openMeetingsTable.setRowMargin(2);
		openMeetingsTable.setRowHeight(24);
		openMeetingsTable.setFont(new Font("Roboto", Font.PLAIN, 14));
		openMeetingsTable.setForeground(Color.decode("#434C5E"));
		openMeetingsTable.setGridColor(Color.decode("#B48EAD"));
		openMeetingsTable.getTableHeader().setBackground(Color.decode("#B48EAD"));
		openMeetingsTable.getTableHeader().setForeground(Color.decode("#ECEFF4"));
		openMeetingsTable.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 14));
		openMeetingsTable.getTableHeader().setReorderingAllowed(false);
		
		openMeetingsScrollPane.setViewportView(openMeetingsTable);
		
		// Layout utilizzato
		GroupLayout gl_meetingsInformationPanel = new GroupLayout(meetingsInformationPanel);
		gl_meetingsInformationPanel.setHorizontalGroup(
			gl_meetingsInformationPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_meetingsInformationPanel.createSequentialGroup()
					.addContainerGap(50, Short.MAX_VALUE)
					.addComponent(openMeetingsScrollPane, GroupLayout.PREFERRED_SIZE, 638, GroupLayout.PREFERRED_SIZE)
					.addGap(49))
		);
		gl_meetingsInformationPanel.setVerticalGroup(
			gl_meetingsInformationPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_meetingsInformationPanel.createSequentialGroup()
					.addGap(24)
					.addComponent(openMeetingsScrollPane, GroupLayout.PREFERRED_SIZE, 291, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(30, Short.MAX_VALUE))
		);
		meetingsInformationPanel.setLayout(gl_meetingsInformationPanel);
		
		pack();
		setLocationRelativeTo(null);
	}
}
