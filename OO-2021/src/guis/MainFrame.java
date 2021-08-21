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

	private static final long serialVersionUID = 1L;
	
	// Dichiarazioni utili
	private Controller c;
	private JPanel contentPane;
	private JPanel titlePanel;
	private JPanel formsPanel;
	private JPanel buttonPanel;
	private JPanel iconPanel;
	private JTextField usernameTextField;
	private JPasswordField passwordTextField;
	private JButton signupButton;
	private JButton loginButton;

	// Creazione frame
	public MainFrame(Controller co) {
		c = co;
		JFrame utility = this;
		setResizable(false);
		setTitle("Autenticazione - Projesting");
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainFrame.class.getResource("/bulb.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 411, 289);
		contentPane = new JPanel();
		contentPane.setBackground(Color.decode("#4C566A"));
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		// Panel con il titolo del frame
		titlePanel = new JPanel();
		titlePanel.setBackground(Color.decode("#4C566A"));
		contentPane.add(titlePanel, BorderLayout.NORTH);
		
		// Label con il titolo del frame
		JLabel titleLabel = new JLabel("Autenticazione");
		titleLabel.setIconTextGap(18);
		titleLabel.setIcon(new ImageIcon(MainFrame.class.getResource("/enter.png")));
		titleLabel.setForeground(Color.decode("#EBCB8B"));
		titleLabel.setFont(new Font("Roboto", Font.PLAIN, 28));
		titlePanel.add(titleLabel);
		
		// Panel contenente componenti necessarie all'accesso
		formsPanel = new JPanel();
		formsPanel.setBackground(Color.decode("#4C566A"));
		contentPane.add(formsPanel, BorderLayout.EAST);
		GridBagLayout gbl_formsPanel = new GridBagLayout();
		gbl_formsPanel.columnWidths = new int[]{157, 25, 0};
		gbl_formsPanel.rowHeights = new int[]{31, 14, 24, 14, 20, 0};
		gbl_formsPanel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_formsPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		formsPanel.setLayout(gbl_formsPanel);
		
		// Label informativa username
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
		
		// Textfield nel quale inserire lo username
		usernameTextField = new JTextField();
		usernameTextField.setToolTipText("Se rappresenti un'azienda inserisci la partita iva, altrimenti inserisci il tuo codice fiscale.");
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
		
		// Label informativa password
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
		
		// Textfield nel quale inserire la password
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
		
		// Panel contenente i bottoni
		buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.decode("#4C566A"));
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		// Bottone per registrarsi
		signupButton = new JButton("Registrati");
		signupButton.setForeground(Color.decode("#2E3440"));
		signupButton.setFocusPainted(false);
		signupButton.setBorderPainted(false);
		signupButton.setBackground(Color.decode("#EBCB8B"));
		
		signupButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent clickSignUp) {
				try {
					c.openSignUpForm();
				} catch (Exception genericError) {
					c.openPopupDialog(utility, "Ops! Qualcosa è andato storto; riprova.");
				}
			}
		});
		signupButton.setFont(new Font("Roboto", Font.PLAIN, 12));
		buttonPanel.add(signupButton);
		
		// Bottone di login
		loginButton = new JButton("Login");
		loginButton.setForeground(Color.decode("#2E3440"));
		loginButton.setFocusPainted(false);
		loginButton.setBorderPainted(false);
		loginButton.setBackground(Color.decode("#EBCB8B"));
		loginButton.setFont(new Font("Roboto", Font.PLAIN, 12));
		buttonPanel.add(loginButton);
		
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent clickLogin) {
				String username = usernameTextField.getText();
				String pwd = new String (passwordTextField.getPassword());
				usernameTextField.setText("");
				passwordTextField.setText("");
				
				if (username.length() == 16 && !pwd.isBlank()) {
					try {
						c.checkLoginForEmployee(username, pwd);
					} catch (Exception loginError) {
						c.openPopupDialog(utility, "Verifica utente non andata a buon fine");
					}
				}
				else if (username.length() == 11 && !pwd.isBlank()){
					try {
						c.checkLoginForCompany(username, pwd);
					} catch (Exception loginError) {
						c.openPopupDialog(utility, "Verifica azienda non andata a buon fine");
					}
				}
				else {
					setEnabled(false);
					c.openPopupDialog(utility, "Username o password non validi");
				}
			}
		});
		
		// Panel con icona decorativa
		iconPanel = new JPanel();
		iconPanel.setBackground(Color.decode("#4C566A"));
		contentPane.add(iconPanel, BorderLayout.CENTER);
		
		// Label con icona decorativa
		JLabel iconLabel = new JLabel("");
		iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
		iconLabel.setIcon(new ImageIcon(MainFrame.class.getResource("/worldwide.png")));
		
		// Layout utilizzato
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
