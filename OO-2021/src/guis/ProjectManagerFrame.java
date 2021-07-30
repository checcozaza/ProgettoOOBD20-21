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

public class ProjectManagerFrame extends JFrame {

	private JPanel contentPane;
	private Controller c;

	/**
	 * Create the frame.
	 */
	public ProjectManagerFrame(Controller co, Employee manager) {
		setTitle("Homepage - Projesting");
		setIconImage(Toolkit.getDefaultToolkit().getImage(ProjectManagerFrame.class.getResource("/bulb.png")));
		c = co;
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 580, 448);
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
		
		JButton logoutButton = new JButton("Logout");
		logoutButton.setForeground(new Color(46, 52, 64));
		logoutButton.setFont(new Font("Roboto", Font.PLAIN, 12));
		logoutButton.setBorderPainted(false);
		logoutButton.setBackground(new Color(235, 203, 139));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addComponent(welcomeManagerPanel, GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap(246, Short.MAX_VALUE)
					.addComponent(logoutButton, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
					.addGap(119)
					.addComponent(managerIconLabel)
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(welcomeManagerPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 215, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(managerIconLabel)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(logoutButton)
							.addContainerGap())))
		);
		contentPane.setLayout(gl_contentPane);
		
		setLocationRelativeTo(null);
	}

}
