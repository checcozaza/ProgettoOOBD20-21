package guis;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import java.awt.Color;
import javax.swing.border.LineBorder;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import controllers.Controller;
import entities.Employee;
import entities.Meeting;

import javax.swing.LayoutStyle.ComponentPlacement;
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

	private JPanel contentPane;
	private JTable openMeetingsTable;
	private JPanel titlePanel;
	private JPanel bottomPanel;
	private JPanel meetingsInformationPanel;
	private Controller c;
	private DefaultTableModel openMeetingsTM;
	private JButton goBackButton;

	/**
	 * Create the frame.
	 */
	public ChooseMeetingFrame(Controller co, Employee user) {
		setResizable(false);
		JFrame utility = this;
		setIconImage(Toolkit.getDefaultToolkit().getImage(ChooseMeetingFrame.class.getResource("/bulb.png")));
		c = co;
		setTitle("Partecipa a un meeting - Projesting");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 845, 614);
		contentPane = new JPanel();
		contentPane.setBackground(Color.decode("#4C566A"));
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		titlePanel = new JPanel();
		titlePanel.setBackground(Color.decode("#434C5E"));
		contentPane.add(titlePanel, BorderLayout.NORTH);
		titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblNewLabel = new JLabel("Scegli a quale meeting partecipare");
		lblNewLabel.setForeground(Color.decode("#EBCB8B"));
		lblNewLabel.setFont(new Font("Roboto", Font.PLAIN, 28));
		titlePanel.add(lblNewLabel);
		
		bottomPanel = new JPanel();
		bottomPanel.setBackground(Color.decode("#4C566A"));
		contentPane.add(bottomPanel, BorderLayout.SOUTH);
		GridBagLayout gbl_bottomPanel = new GridBagLayout();
		gbl_bottomPanel.columnWidths = new int[]{108, 236, 98, 92, 225, 84};
		gbl_bottomPanel.rowHeights = new int[]{27, 0};
		gbl_bottomPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		gbl_bottomPanel.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		bottomPanel.setLayout(gbl_bottomPanel);
		
		goBackButton = new JButton("Indietro");
		goBackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent goBack) {
					c.goBack(utility);
			}
		});
		
		JButton logoutButton = new JButton("");
		logoutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent clickLogout) {
				c.backToLogin(utility);
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
		gbc_goBackButton.anchor = GridBagConstraints.WEST;
		gbc_goBackButton.insets = new Insets(0, 0, 0, 5);
		gbc_goBackButton.gridx = 2;
		gbc_goBackButton.gridy = 0;
		bottomPanel.add(goBackButton, gbc_goBackButton);
		
		JButton signUpToMeetingButton = new JButton("Partecipa");
		GridBagConstraints gbc_signUpToMeetingButton = new GridBagConstraints();
		gbc_signUpToMeetingButton.insets = new Insets(0, 0, 0, 5);
		gbc_signUpToMeetingButton.anchor = GridBagConstraints.WEST;
		gbc_signUpToMeetingButton.gridx = 3;
		gbc_signUpToMeetingButton.gridy = 0;
		bottomPanel.add(signUpToMeetingButton, gbc_signUpToMeetingButton);
		signUpToMeetingButton.setForeground(Color.decode("#2E3440"));
		signUpToMeetingButton.setFont(new Font("Roboto", Font.PLAIN, 14));
		signUpToMeetingButton.setFocusPainted(false);
		signUpToMeetingButton.setBorderPainted(false);
		signUpToMeetingButton.setBackground(Color.decode("#EBCB8B"));
		
		JLabel iconLabel = new JLabel("");
		GridBagConstraints gbc_iconLabel = new GridBagConstraints();
		gbc_iconLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_iconLabel.gridx = 5;
		gbc_iconLabel.gridy = 0;
		bottomPanel.add(iconLabel, gbc_iconLabel);
		iconLabel.setIcon(new ImageIcon(ChooseMeetingFrame.class.getResource("/meeting.png")));
		signUpToMeetingButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent clickAttend) {
				try {
					c.addEmployeeToMeeting(user.getFiscalCode(), Integer.valueOf(openMeetingsTable.getValueAt(openMeetingsTable.getSelectedRow(), 4).toString()));
					openMeetingsTM.removeRow(openMeetingsTable.getSelectedRow());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		meetingsInformationPanel = new JPanel();
		meetingsInformationPanel.setBackground(Color.decode("#4C566A"));
		contentPane.add(meetingsInformationPanel, BorderLayout.CENTER);
		
		JScrollPane openMeetingsScrollPane = new JScrollPane();
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
		GroupLayout gl_meetingsInformationPanel = new GroupLayout(meetingsInformationPanel);
		gl_meetingsInformationPanel.setHorizontalGroup(
			gl_meetingsInformationPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_meetingsInformationPanel.createSequentialGroup()
					.addGap(82)
					.addComponent(openMeetingsScrollPane, GroupLayout.PREFERRED_SIZE, 638, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(93, Short.MAX_VALUE))
		);
		gl_meetingsInformationPanel.setVerticalGroup(
			gl_meetingsInformationPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_meetingsInformationPanel.createSequentialGroup()
					.addGap(58)
					.addComponent(openMeetingsScrollPane, GroupLayout.PREFERRED_SIZE, 363, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(76, Short.MAX_VALUE))
		);
		meetingsInformationPanel.setLayout(gl_meetingsInformationPanel);
		
		setLocationRelativeTo(null);
	}
}
