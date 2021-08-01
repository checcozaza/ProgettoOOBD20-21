package guis;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JSplitPane;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.awt.CardLayout;
import javax.swing.BoxLayout;
import net.miginfocom.swing.MigLayout;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import controllers.Controller;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Toolkit;

public class MainFrame extends JFrame {

	private JPanel contentPane;
	private JTextField usernameTextField;
	private JPasswordField passwordTextField;
	private Controller c;

	/**
	 * Create the frame.
	 */
	public MainFrame(Controller co) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainFrame.class.getResource("/bulb.png")));
		setTitle("Autenticazione - Projesting");
		c = co;
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 411, 289);
		contentPane = new JPanel();
		contentPane.setBackground(Color.decode("#4C566A"));
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel titlePanel = new JPanel();
		titlePanel.setBackground(Color.decode("#4C566A"));
		contentPane.add(titlePanel, BorderLayout.NORTH);
		
		JLabel titleLabel = new JLabel("Autenticazione");
		titleLabel.setIconTextGap(18);
		titleLabel.setIcon(new ImageIcon(MainFrame.class.getResource("/enter.png")));
		titleLabel.setForeground(Color.decode("#EBCB8B"));
		titleLabel.setFont(new Font("Roboto", Font.PLAIN, 28));
		titlePanel.add(titleLabel);
		
		JPanel formsPanel = new JPanel();
		formsPanel.setBackground(Color.decode("#4C566A"));
		contentPane.add(formsPanel, BorderLayout.EAST);
		GridBagLayout gbl_formsPanel = new GridBagLayout();
		gbl_formsPanel.columnWidths = new int[]{157, 25, 0};
		gbl_formsPanel.rowHeights = new int[]{31, 14, 24, 14, 20, 0};
		gbl_formsPanel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_formsPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		formsPanel.setLayout(gbl_formsPanel);
		
		JLabel usernameLabel = new JLabel("Username");
		usernameLabel.setFont(new Font("Roboto", Font.PLAIN, 12));
		usernameLabel.setForeground(Color.decode("#D8DEE9"));
		GridBagConstraints gbc_usernameLabel = new GridBagConstraints();
		gbc_usernameLabel.anchor = GridBagConstraints.NORTH;
		gbc_usernameLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_usernameLabel.insets = new Insets(0, 0, 5, 5);
		gbc_usernameLabel.gridx = 0;
		gbc_usernameLabel.gridy = 1;
		formsPanel.add(usernameLabel, gbc_usernameLabel);
		
		usernameTextField = new JTextField();
		usernameTextField.setFont(new Font("Roboto", Font.PLAIN, 12));
		usernameTextField.setForeground(Color.decode("#5E81AC"));
		GridBagConstraints gbc_usernameTextField = new GridBagConstraints();
		gbc_usernameTextField.anchor = GridBagConstraints.NORTH;
		gbc_usernameTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_usernameTextField.insets = new Insets(0, 0, 5, 5);
		gbc_usernameTextField.gridx = 0;
		gbc_usernameTextField.gridy = 2;
		formsPanel.add(usernameTextField, gbc_usernameTextField);
		usernameTextField.setColumns(10);
		
		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setFont(new Font("Roboto", Font.PLAIN, 12));
		passwordLabel.setForeground(Color.decode("#D8DEE9"));
		GridBagConstraints gbc_passwordLabel = new GridBagConstraints();
		gbc_passwordLabel.anchor = GridBagConstraints.NORTH;
		gbc_passwordLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordLabel.insets = new Insets(0, 0, 5, 5);
		gbc_passwordLabel.gridx = 0;
		gbc_passwordLabel.gridy = 3;
		formsPanel.add(passwordLabel, gbc_passwordLabel);
		
		passwordTextField = new JPasswordField();
		passwordTextField.setFont(new Font("Roboto", Font.PLAIN, 12));
		passwordTextField.setForeground(Color.decode("#5E81AC"));
		GridBagConstraints gbc_passwordTextField = new GridBagConstraints();
		gbc_passwordTextField.insets = new Insets(0, 0, 0, 5);
		gbc_passwordTextField.anchor = GridBagConstraints.NORTH;
		gbc_passwordTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordTextField.gridx = 0;
		gbc_passwordTextField.gridy = 4;
		formsPanel.add(passwordTextField, gbc_passwordTextField);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.decode("#4C566A"));
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton signupButton = new JButton("Registrati");
		signupButton.setForeground(Color.decode("#2E3440"));
		signupButton.setFocusPainted(false);
		signupButton.setBorderPainted(false);
		signupButton.setBackground(Color.decode("#EBCB8B"));
		signupButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent clickSignUp) {
				try {
					c.openSignUpForm();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		signupButton.setFont(new Font("Roboto", Font.PLAIN, 12));
		buttonPanel.add(signupButton);
		
		JButton loginButton = new JButton("Login");
		JFrame toClose = this;
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent clickLogin) {
				String username = usernameTextField.getText();
				String pwd = new String (passwordTextField.getPassword());
				usernameTextField.setText("");
				passwordTextField.setText("");
				if (username.length() == 16 && !pwd.isBlank()) {
					try {
						c.checkLoginForEmployee(username, pwd);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else if (username.length() == 11) {
					try {
						c.checkLoginForCompany(username, pwd);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else {
					setEnabled(false);
					c.openPopupDialog(toClose, "Username o password non validi");
				}
			}
		});
		
		loginButton.setForeground(Color.decode("#2E3440"));
		loginButton.setFocusPainted(false);
		loginButton.setBorderPainted(false);
		loginButton.setBackground(Color.decode("#EBCB8B"));
		loginButton.setFont(new Font("Roboto", Font.PLAIN, 12));
		buttonPanel.add(loginButton);
		
		JPanel iconPanel = new JPanel();
		iconPanel.setBackground(Color.decode("#4C566A"));
		contentPane.add(iconPanel, BorderLayout.CENTER);
		
		JLabel iconLabel = new JLabel("");
		iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
		iconLabel.setIcon(new ImageIcon(MainFrame.class.getResource("/worldwide.png")));
		GroupLayout gl_iconPanel = new GroupLayout(iconPanel);
		gl_iconPanel.setHorizontalGroup(
			gl_iconPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(iconLabel, GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
		);
		gl_iconPanel.setVerticalGroup(
			gl_iconPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(iconLabel, GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)
		);
		iconPanel.setLayout(gl_iconPanel);
		
		pack();
		setLocationRelativeTo(null);
	}
}
