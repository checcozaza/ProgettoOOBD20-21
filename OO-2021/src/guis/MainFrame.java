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

public class MainFrame extends JFrame {

	private JPanel contentPane;
	private JTextField cfTextField;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 411, 289);
		contentPane = new JPanel();
		contentPane.setBackground(Color.decode("#3B4252"));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel titlePanel = new JPanel();
		titlePanel.setBackground(Color.decode("#3B4252"));
		contentPane.add(titlePanel, BorderLayout.NORTH);
		
		JLabel titleLabel = new JLabel("Autenticazione");
		titleLabel.setForeground(Color.decode("#D8DEE9"));
		titleLabel.setFont(new Font("Roboto", Font.PLAIN, 25));
		titlePanel.add(titleLabel);
		
		JPanel formsPanel = new JPanel();
		formsPanel.setBackground(Color.decode("#3B4252"));
		contentPane.add(formsPanel, BorderLayout.EAST);
		GridBagLayout gbl_formsPanel = new GridBagLayout();
		gbl_formsPanel.columnWidths = new int[]{157, 25, 0};
		gbl_formsPanel.rowHeights = new int[]{31, 14, 24, 14, 20, 0};
		gbl_formsPanel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_formsPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		formsPanel.setLayout(gbl_formsPanel);
		
		JLabel cfLabel = new JLabel("Codice Fiscale");
		cfLabel.setFont(new Font("Roboto", Font.PLAIN, 12));
		cfLabel.setForeground(Color.decode("#D8DEE9"));
		GridBagConstraints gbc_cfLabel = new GridBagConstraints();
		gbc_cfLabel.anchor = GridBagConstraints.NORTH;
		gbc_cfLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_cfLabel.insets = new Insets(0, 0, 5, 5);
		gbc_cfLabel.gridx = 0;
		gbc_cfLabel.gridy = 1;
		formsPanel.add(cfLabel, gbc_cfLabel);
		
		cfTextField = new JTextField();
		cfTextField.setFont(new Font("Roboto", Font.PLAIN, 12));
		cfTextField.setForeground(Color.decode("#5E81AC"));
		GridBagConstraints gbc_cfTextField = new GridBagConstraints();
		gbc_cfTextField.anchor = GridBagConstraints.NORTH;
		gbc_cfTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_cfTextField.insets = new Insets(0, 0, 5, 5);
		gbc_cfTextField.gridx = 0;
		gbc_cfTextField.gridy = 2;
		formsPanel.add(cfTextField, gbc_cfTextField);
		cfTextField.setColumns(10);
		
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
		
		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Roboto", Font.PLAIN, 12));
		passwordField.setForeground(Color.decode("#5E81AC"));
		GridBagConstraints gbc_passwordField = new GridBagConstraints();
		gbc_passwordField.insets = new Insets(0, 0, 0, 5);
		gbc_passwordField.anchor = GridBagConstraints.NORTH;
		gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordField.gridx = 0;
		gbc_passwordField.gridy = 4;
		formsPanel.add(passwordField, gbc_passwordField);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.decode("#3B4252"));
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton signupButton = new JButton("Registrati");
		signupButton.setForeground(Color.decode("#2E3440"));
		signupButton.setFocusPainted(false);
		signupButton.setBorderPainted(false);
		signupButton.setBackground(Color.decode("#81A1C1"));
		signupButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		signupButton.setFont(new Font("Roboto", Font.PLAIN, 12));
		buttonPanel.add(signupButton);
		
		JButton loginButton = new JButton("Login");
		loginButton.setForeground(Color.decode("#2E3440"));
		loginButton.setFocusPainted(false);
		loginButton.setBorderPainted(false);
		loginButton.setBackground(Color.decode("#81A1C1"));
		loginButton.setFont(new Font("Roboto", Font.PLAIN, 12));
		buttonPanel.add(loginButton);
		
		JPanel iconPanel = new JPanel();
		iconPanel.setBackground(Color.decode("#3B4252"));
		contentPane.add(iconPanel, BorderLayout.CENTER);
		
		JLabel iconLabel = new JLabel("");
		iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
		iconLabel.setIcon(new ImageIcon(MainFrame.class.getResource("/user.png")));
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
	}
}
