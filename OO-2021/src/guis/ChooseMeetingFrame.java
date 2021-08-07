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

public class ChooseMeetingFrame extends JFrame {

	private JPanel contentPane;
	private JTable openMeetingsTable;
	private JPanel panel;
	private JPanel panel_1;
	private JPanel panel_2;
	private Controller c;
	private DefaultTableModel openMeetingsTM;

	/**
	 * Create the frame.
	 */
	public ChooseMeetingFrame(Controller co, Employee user) {
		c = co;
		setTitle("Partecipa a un meeting - Projesting");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 819, 531);
		contentPane = new JPanel();
		contentPane.setBackground(Color.decode("#4C566A"));
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		
		panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.SOUTH);
		
		panel_2 = new JPanel();
		panel_2.setBackground(Color.decode("#EBCB8B"));
		contentPane.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(null);
		
		JScrollPane openMeetingsScrollPane = new JScrollPane();
		openMeetingsScrollPane.setBounds(158, 54, 454, 363);
		panel_2.add(openMeetingsScrollPane);
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
		
		JButton btnNewButton = new JButton("Partecipa");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent clickAttend) {
				try {
					c.addEmployeeToMeeting(user.getFiscalCode(), Integer.valueOf(openMeetingsTable.getValueAt(openMeetingsTable.getSelectedRow(), 4).toString()));
					openMeetingsTM.removeRow(openMeetingsTable.getSelectedRow());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		btnNewButton.setBounds(340, 438, 89, 23);
		panel_2.add(btnNewButton);
		
		//pack();
		setLocationRelativeTo(null);
	}
}
