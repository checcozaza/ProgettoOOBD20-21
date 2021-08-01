package guis;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controllers.Controller;
import entities.Employee;
import entities.EmployeeRating;
import entities.ProjectHistory;
import entities.Topic;

import java.awt.Font;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Toolkit;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.privatejgoodies.common.base.Objects;

public class UserFrame extends JFrame {

	private JPanel contentPane;
	private Controller c;
	private JTable table;
	JLabel lblNewLabel_2; 
	private JTable table_1;
	/**
	 * Create the frame.
	 */
	public UserFrame(Controller co, Employee user) {
		setTitle("Homepage - Projesting");
		setIconImage(Toolkit.getDefaultToolkit().getImage(UserFrame.class.getResource("/bulb.png")));
		c = co;
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 740, 497);
		contentPane = new JPanel();
		contentPane.setBackground(Color.decode("#4C566A"));
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		
		JPanel welcomeUserPanel = new JPanel();
		welcomeUserPanel.setBackground(Color.decode("#434C5E"));
		
		JLabel welcomeUserLabel = new JLabel("<HTML> <center> Benvenuto, <br>"+ user.getName() 
									+ "! </center> </HTML>");
		welcomeUserLabel.setForeground(Color.decode("#EBCB8B"));
		welcomeUserLabel.setFont(new Font("Roboto", Font.PLAIN, 28));
		welcomeUserPanel.add(welcomeUserLabel);
		
		JLabel userIconLabel = new JLabel("");
		userIconLabel.setIcon(new ImageIcon(UserFrame.class.getResource("/cv.png")));
		
		JButton logoutButton = new JButton("");
		logoutButton.setFocusPainted(false);
		logoutButton.setContentAreaFilled(false);
		logoutButton.setIcon(new ImageIcon(UserFrame.class.getResource("/logout.png")));
		logoutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent clickLogin) { // Al click del tasto logout, si tornerà alla schermata principale
				dispose();
				c.backToLogin();
			}
		});
		logoutButton.setFont(new Font("Roboto", Font.PLAIN, 12));
		logoutButton.setBorderPainted(false);
		logoutButton.setBackground(Color.decode("#EBCB8B"));
		logoutButton.setForeground(Color.decode("#2E3440"));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		
		JLabel lblNewLabel = new JLabel(user.getHiredBy().getName());
		
		JLabel lblNewLabel_1 = new JLabel(String.valueOf(user.getAvgWage()) + " €");
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(welcomeUserPanel, GroupLayout.DEFAULT_SIZE, 734, Short.MAX_VALUE)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(logoutButton, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
						.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 572, GroupLayout.PREFERRED_SIZE))
					.addGap(24)
					.addComponent(userIconLabel))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel)
					.addGap(48)
					.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(550, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(welcomeUserPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(lblNewLabel_1))
					.addGap(15)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(userIconLabel)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(logoutButton)))
					.addContainerGap())
		);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Progetto attuale", null, panel, null);
		panel.setLayout(null);
		
		
		lblNewLabel_2 = new JLabel("NESSUN PROGETTO ATTIVO");
		lblNewLabel_2.setBounds(10, 11, 547, 71);
		panel.add(lblNewLabel_2);
		if (user.getEmployeeProject() != null) {
			String topicToPrint = "";
			for (Topic t: user.getEmployeeProject().getProjectTopics()) {
				topicToPrint += t.getName() + ", ";
			}
			
			lblNewLabel_2.setText("<HTML> <p> <center> Codice Progetto: " + String.valueOf(user.getEmployeeProject().getProjectNumber()) 
								+ "<br> Tipologia: " + user.getEmployeeProject().getTypology()
								+ "<br> Ambiti: " + topicToPrint
								+ "</center> </p> </HTML>");
		}
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(0, 135, 567, 48);
		panel.add(scrollPane_1);
		
		table_1 = new JTable();
		scrollPane_1.setViewportView(table_1);
			
																
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Cronologia progetti", null, panel_1, null);
		panel_1.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 567, 262);
		panel_1.add(scrollPane);
		
		DefaultTableModel historyTM = new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Codice Progetto", "Tipologia", "Valutazione"
			}
		);
		
		for (EmployeeRating er: user.getEmployeeRatings()) {
			historyTM.addRow(new Object[] {er.getPastProject().getProjectNumber(),
										   er.getPastProject().getTypology().toString().replace('_', ' '),
										   er.getRating()});
		}
		
		table = new JTable(historyTM) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		scrollPane.setViewportView(table);
		contentPane.setLayout(gl_contentPane);
		
		setLocationRelativeTo(null);
	}
}
