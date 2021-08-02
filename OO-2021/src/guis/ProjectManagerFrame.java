package guis;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controllers.Controller;
import entities.Employee;
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
import java.awt.event.ActionEvent;

public class ProjectManagerFrame extends JFrame {

	private JPanel contentPane;
	private Controller c;

	// Creazione frame
	public ProjectManagerFrame(Controller co, Employee manager) {
		setTitle("Homepage - Projesting");
		setIconImage(Toolkit.getDefaultToolkit().getImage(ProjectManagerFrame.class.getResource("/bulb.png")));
		c = co;
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 740, 497);
		contentPane = new JPanel();
		contentPane.setBackground(Color.decode("#4C566A"));
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		
		JPanel welcomeManagerPanel = new JPanel();
		welcomeManagerPanel.setBackground(Color.decode("#434C5E"));
		
		JLabel welcomeManagerLabel = new JLabel("<HTML> <center> Benvenuto, <br>"+ manager.getName() 
									+ "! </center> </HTML>");
		welcomeManagerLabel.setForeground(Color.decode("#EBCB8B"));
		welcomeManagerLabel.setFont(new Font("Roboto", Font.PLAIN, 28));
		welcomeManagerPanel.add(welcomeManagerLabel);
		
		JLabel managerIconLabel = new JLabel("");
		managerIconLabel.setIcon(new ImageIcon(ProjectManagerFrame.class.getResource("/laptop.png")));
		
		
		JButton logoutButton = new JButton("");
		logoutButton.setContentAreaFilled(false);
		logoutButton.setIcon(new ImageIcon(ProjectManagerFrame.class.getResource("/logout.png")));
		JFrame loggingOut = this;
		logoutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent clickLogout) { // Al click del tasto logout, si torner� alla schermata principale
				c.backToLogin(loggingOut);
			}
		});
		logoutButton.setForeground(new Color(46, 52, 64));
		logoutButton.setFont(new Font("Roboto", Font.PLAIN, 12));
		logoutButton.setBorderPainted(false);
		logoutButton.setBackground(new Color(235, 203, 139));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addComponent(welcomeManagerPanel, GroupLayout.DEFAULT_SIZE, 734, Short.MAX_VALUE)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(logoutButton, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 417, Short.MAX_VALUE)
					.addComponent(managerIconLabel)
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(welcomeManagerPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 328, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(managerIconLabel)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(logoutButton, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())))
		);
		contentPane.setLayout(gl_contentPane);
		
		setLocationRelativeTo(null);
	}

}
