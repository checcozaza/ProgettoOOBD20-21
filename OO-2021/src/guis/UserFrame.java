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
import java.awt.Font;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Toolkit;
import javax.swing.JButton;

public class UserFrame extends JFrame {

	private JPanel contentPane;
	private Controller c;

	/**
	 * Create the frame.
	 */
	public UserFrame(Controller co, Employee user) {
		setTitle("Homepage - Projesting");
		setIconImage(Toolkit.getDefaultToolkit().getImage(UserFrame.class.getResource("/bulb.png")));
		c = co;
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 580, 448);
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
		
		JButton logoutButton = new JButton("Logout");
		logoutButton.setFont(new Font("Roboto", Font.PLAIN, 12));
		logoutButton.setBorderPainted(false);
		logoutButton.setBackground(Color.decode("#EBCB8B"));
		logoutButton.setForeground(Color.decode("#2E3440"));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addComponent(welcomeUserPanel, GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(256)
					.addComponent(logoutButton)
					.addGap(119)
					.addComponent(userIconLabel))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(welcomeUserPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 204, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(userIconLabel)
						.addComponent(logoutButton))
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);
		
		setLocationRelativeTo(null);
	}
}
