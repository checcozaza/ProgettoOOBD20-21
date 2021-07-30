package guis;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import controllers.Controller;
import entities.Company;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.GridBagLayout;
import javax.swing.ImageIcon;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Toolkit;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CompanyFrame extends JFrame {

	private JPanel contentPane;
	private Controller c;

	/**
	 * Create the frame.
	 */
	public CompanyFrame(Controller co, Company signedInCompany) {
		setTitle("Homepage - Projesting");
		setIconImage(Toolkit.getDefaultToolkit().getImage(CompanyFrame.class.getResource("/bulb.png")));
		c = co;
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 580, 448);
		contentPane = new JPanel();
		contentPane.setBackground(Color.decode("#4C566A"));
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		
		JPanel welcomeCompanyPanel = new JPanel();
		welcomeCompanyPanel.setBackground(Color.decode("#434C5E"));
		
		JLabel welcomeCompanyLabel = new JLabel("<HTML> <center> Benvenuto, <br>"+ signedInCompany.getName() 
									+ " (P. IVA " + signedInCompany.getVatNumber() + ") </center> </HTML>");
		welcomeCompanyLabel.setForeground(Color.decode("#EBCB8B"));
		welcomeCompanyLabel.setFont(new Font("Roboto", Font.PLAIN, 28));
		welcomeCompanyPanel.add(welcomeCompanyLabel);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(CompanyFrame.class.getResource("/briefcase.png")));
		
		JButton logoutButton = new JButton("Logout");
		logoutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent clickLogout) {
				dispose();
				c.backToLogin();
			}
		});
		logoutButton.setForeground(new Color(46, 52, 64));
		logoutButton.setFont(new Font("Roboto", Font.PLAIN, 12));
		logoutButton.setBorderPainted(false);
		logoutButton.setBackground(new Color(235, 203, 139));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addComponent(welcomeCompanyPanel, GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(logoutButton, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 345, Short.MAX_VALUE)
					.addComponent(lblNewLabel)
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(welcomeCompanyPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 204, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblNewLabel)
						.addComponent(logoutButton, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);
		
		setLocationRelativeTo(null);
	}
}
