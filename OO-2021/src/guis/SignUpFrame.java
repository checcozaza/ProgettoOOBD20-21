package guis;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.github.lgooddatepicker.components.DatePicker;

import controllers.Controller;
import entities.Company;
import entities.Employee;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSpinner;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SpinnerNumberModel;

public class SignUpFrame extends JFrame {

	private JPanel contentPane;
	private JTextField nameTextField;
	private JTextField surnameTextField;
	private JPasswordField passwordTextField;
	private JPasswordField confirmPasswordTextField;
	private Controller c;
	JComboBox<Object> regionComboBox;
	JComboBox<Object> provinceComboBox;
	JComboBox<Object> cityComboBox;
	DatePicker birthDatePicker;
	JComboBox<String> genderComboBox;
	JSpinner wageSpinner;
	JComboBox<Object> companyComboBox;
	

	/**
	 * Create the frame.
	 * @throws Exception 
	 */
	public SignUpFrame(Controller co) throws Exception {
		c = co;
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 580, 448);
		contentPane = new JPanel();
		contentPane.setBackground(Color.decode("#3B4252"));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel titlePanel = new JPanel();
		titlePanel.setBackground(Color.decode("#3B4252"));
		contentPane.add(titlePanel, BorderLayout.NORTH);
		titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel signUpLabel = new JLabel("Registrazione");
		signUpLabel.setIconTextGap(18);
		signUpLabel.setIcon(new ImageIcon(SignUpFrame.class.getResource("/contract.png")));
		signUpLabel.setForeground(Color.decode("#D8DEE9"));
		signUpLabel.setFont(new Font("Roboto", Font.PLAIN, 28));
		titlePanel.add(signUpLabel);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.decode("#3B4252"));
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton goBackButton = new JButton("Indietro");
		goBackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent goBack) {
				c.backToLogin();
			}
		});
		goBackButton.setForeground(Color.decode("#2E3440"));
		goBackButton.setFocusPainted(false);
		goBackButton.setBorderPainted(false);
		goBackButton.setBackground(Color.decode("#81A1C1"));
		goBackButton.setFont(new Font("Roboto", Font.PLAIN, 12));
		buttonPanel.add(goBackButton);
		
		JButton signUpButton = new JButton("Registrati");
		signUpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent clickSignUp) {
				boolean valid = c.checkCredentials(nameTextField.getText(), surnameTextField.getText(),
								   				   new String (passwordTextField.getPassword()), 
								   				   new String (confirmPasswordTextField.getPassword()),
								   				   birthDatePicker.getDate());
				if (valid) {
					try {
						String cf = c.cfGenerator(nameTextField.getText(), surnameTextField.getText(),
									  birthDatePicker.getDate(), genderComboBox.getSelectedItem().toString().toCharArray() [0], 
									  cityComboBox.getSelectedItem().toString());
						Company hiringCompany = new Company(companyComboBox.getSelectedItem().toString(), null, null, null, null, null);
						c.insertEmployee(new Employee(cf, nameTextField.getText(), surnameTextField.getText(),
										 			  new String (passwordTextField.getPassword()), null, 
										 			  Float.parseFloat(wageSpinner.getValue().toString()),
										 			  hiringCompany, null, null, null));
						c.openSuccessDialog(cf);
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		});
		signUpButton.setForeground(Color.decode("#2E3440"));
		signUpButton.setFocusPainted(false);
		signUpButton.setBorderPainted(false);
		signUpButton.setBackground(Color.decode("#81A1C1"));
		signUpButton.setFont(new Font("Roboto", Font.PLAIN, 12));
		buttonPanel.add(signUpButton);
		
		JPanel formsPanel = new JPanel();
		formsPanel.setBackground(Color.decode("#3B4252"));
		contentPane.add(formsPanel, BorderLayout.CENTER);
		
		JLabel nameLabel = new JLabel("Nome");
		nameLabel.setForeground(Color.decode("#D8DEE9"));
		nameLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
		
		JLabel surnameLabel = new JLabel("Cognome");
		surnameLabel.setForeground(Color.decode("#D8DEE9"));
		surnameLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
		
		JLabel genderLabel = new JLabel("Sesso");
		genderLabel.setForeground(Color.decode("#D8DEE9"));
		genderLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
		
		nameTextField = new JTextField();
		nameTextField.setBackground(Color.WHITE);
		nameTextField.setForeground(Color.decode("#5E81AC"));
		nameTextField.setFont(new Font("Roboto", Font.PLAIN, 14));
		nameTextField.setColumns(10);
		
		surnameTextField = new JTextField();
		surnameTextField.setBackground(Color.WHITE);
		surnameTextField.setForeground(Color.decode("#5E81AC"));
		surnameTextField.setFont(new Font("Roboto", Font.PLAIN, 14));
		surnameTextField.setColumns(10);
		
		genderComboBox = new JComboBox<String>();
		genderComboBox.setBackground(Color.WHITE);
		genderComboBox.setForeground(Color.decode("#5E81AC"));
		genderComboBox.setFont(new Font("Roboto", Font.PLAIN, 14));
		genderComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"M", "F"}));
		
		JLabel birthDateLabel = new JLabel("Data di nascita");
		birthDateLabel.setForeground(Color.decode("#D8DEE9"));
		birthDateLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
		
		JLabel placeOfBirthLabel = new JLabel("Luogo di nascita");
		placeOfBirthLabel.setForeground(Color.decode("#D8DEE9"));
		placeOfBirthLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
		
		birthDatePicker = new DatePicker();
		birthDatePicker.getComponentToggleCalendarButton().setBackground(Color.WHITE);
		birthDatePicker.getComponentDateTextField().setBackground(Color.WHITE);
		birthDatePicker.getComponentToggleCalendarButton().setForeground(Color.decode("#5E81AC"));
		birthDatePicker.getComponentDateTextField().setForeground(Color.decode("#5E81AC"));
		birthDatePicker.getComponentDateTextField().setFont(new Font("Roboto", Font.PLAIN, 14));
		
		regionComboBox = new JComboBox<Object>();
		regionComboBox.setFont(new Font("Roboto", Font.PLAIN, 14));
		regionComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent clickRegion) {
				try {
					provinceComboBox.setModel(new DefaultComboBoxModel<>(c.pickProvince(regionComboBox.getSelectedItem().toString())));
					cityComboBox.setModel(new DefaultComboBoxModel<>(c.pickCity(provinceComboBox.getSelectedItem().toString())));
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		regionComboBox.setBackground(Color.WHITE);
		regionComboBox.setForeground(Color.decode("#5E81AC"));
		regionComboBox.setModel(new DefaultComboBoxModel<>(c.pickRegions()));
		
		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setForeground(Color.decode("#D8DEE9"));
		passwordLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
		
		JLabel confirmPasswordLabel = new JLabel("Conferma password");
		confirmPasswordLabel.setForeground(Color.decode("#D8DEE9"));
		confirmPasswordLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
		
		passwordTextField = new JPasswordField();
		passwordTextField.setBackground(Color.WHITE);
		passwordTextField.setForeground(Color.decode("#5E81AC"));
		passwordTextField.setFont(new Font("Roboto", Font.PLAIN, 14));
		
		confirmPasswordTextField = new JPasswordField();
		confirmPasswordTextField.setBackground(Color.WHITE);
		confirmPasswordTextField.setForeground(Color.decode("#5E81AC"));
		confirmPasswordTextField.setFont(new Font("Roboto", Font.PLAIN, 14));
		
		provinceComboBox = new JComboBox<Object>();
		provinceComboBox.setFont(new Font("Roboto", Font.PLAIN, 14));
		provinceComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent clickProvince) {
				try {
					cityComboBox.setModel(new DefaultComboBoxModel<>(c.pickCity(provinceComboBox.getSelectedItem().toString())));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		provinceComboBox.setBackground(Color.WHITE);
		provinceComboBox.setForeground(Color.decode("#5E81AC"));
		provinceComboBox.setModel(new DefaultComboBoxModel<>(c.pickProvince(regionComboBox.getSelectedItem().toString())));
		
		cityComboBox =new JComboBox<Object>();
		cityComboBox.setFont(new Font("Roboto", Font.PLAIN, 14));
		cityComboBox.setBackground(Color.WHITE);
		cityComboBox.setForeground(Color.decode("#5E81AC"));
		cityComboBox.setModel(new DefaultComboBoxModel<>(c.pickCity(provinceComboBox.getSelectedItem().toString())));
		
		JLabel iconLabel = new JLabel("");
		iconLabel.setIcon(new ImageIcon(SignUpFrame.class.getResource("/folder.png")));
		
		wageSpinner = new JSpinner();
		wageSpinner.setFont(new Font("Roboto", Font.PLAIN, 14));
		wageSpinner.setModel(new SpinnerNumberModel(new Float(1500), new Float(1499), null, new Float(100)));
		wageSpinner.getEditor().getComponent(0).setForeground(Color.decode("#5E81AC"));
		
		JLabel wageLabel = new JLabel("Salario medio");
		wageLabel.setForeground(Color.decode("#D8DEE9"));
		wageLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
		
		JLabel companyLabel = new JLabel("Partita IVA Sede");
		companyLabel.setForeground(new Color(216, 222, 233));
		companyLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
		
		companyComboBox = new JComboBox<Object>();
		companyComboBox.setForeground(new Color(94, 129, 172));
		companyComboBox.setFont(new Font("Roboto", Font.PLAIN, 14));
		companyComboBox.setBackground(Color.WHITE);
		companyComboBox.setModel(new DefaultComboBoxModel<>(c.pickCompanies()));
		
		GroupLayout gl_formsPanel = new GroupLayout(formsPanel);
		gl_formsPanel.setHorizontalGroup(
			gl_formsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_formsPanel.createSequentialGroup()
					.addGap(30)
					.addGroup(gl_formsPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(placeOfBirthLabel, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_formsPanel.createSequentialGroup()
							.addComponent(regionComboBox, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)
							.addGap(23)
							.addComponent(provinceComboBox, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)
							.addGap(23)
							.addComponent(cityComboBox, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_formsPanel.createSequentialGroup()
							.addGroup(gl_formsPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_formsPanel.createSequentialGroup()
									.addGroup(gl_formsPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(birthDateLabel)
										.addComponent(birthDatePicker, GroupLayout.PREFERRED_SIZE, 157, GroupLayout.PREFERRED_SIZE)
										.addComponent(passwordLabel, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE)
										.addComponent(passwordTextField, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE))
									.addGap(23)
									.addGroup(gl_formsPanel.createParallelGroup(Alignment.LEADING, false)
										.addComponent(confirmPasswordLabel, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
										.addComponent(confirmPasswordTextField, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE)
										.addGroup(gl_formsPanel.createSequentialGroup()
											.addGap(1)
											.addGroup(gl_formsPanel.createParallelGroup(Alignment.LEADING)
												.addComponent(genderComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(genderLabel))
											.addGap(18)
											.addGroup(gl_formsPanel.createParallelGroup(Alignment.LEADING)
												.addComponent(wageLabel)
												.addComponent(wageSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))))
								.addGroup(gl_formsPanel.createSequentialGroup()
									.addComponent(nameTextField, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE)
									.addGap(35)
									.addComponent(surnameTextField, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_formsPanel.createSequentialGroup()
									.addComponent(nameLabel, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
									.addGap(70)
									.addComponent(surnameLabel, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)))
							.addGap(52)
							.addGroup(gl_formsPanel.createParallelGroup(Alignment.LEADING, false)
								.addComponent(companyLabel, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
								.addComponent(iconLabel, GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
								.addComponent(companyComboBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
		);
		gl_formsPanel.setVerticalGroup(
			gl_formsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_formsPanel.createSequentialGroup()
					.addGap(30)
					.addGroup(gl_formsPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(nameLabel)
						.addGroup(gl_formsPanel.createParallelGroup(Alignment.BASELINE)
							.addComponent(surnameLabel)
							.addComponent(companyLabel, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)))
					.addGroup(gl_formsPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_formsPanel.createSequentialGroup()
							.addGap(5)
							.addGroup(gl_formsPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(nameTextField, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
								.addComponent(surnameTextField, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))
							.addGap(22)
							.addComponent(placeOfBirthLabel)
							.addGap(5)
							.addGroup(gl_formsPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(regionComboBox, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
								.addComponent(provinceComboBox, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
								.addComponent(cityComboBox, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_formsPanel.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(companyComboBox, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)))
					.addGap(22)
					.addGroup(gl_formsPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_formsPanel.createSequentialGroup()
							.addComponent(birthDateLabel)
							.addGap(5)
							.addComponent(birthDatePicker, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
							.addGap(22)
							.addComponent(passwordLabel)
							.addGap(5)
							.addComponent(passwordTextField, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_formsPanel.createSequentialGroup()
							.addGroup(gl_formsPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(genderLabel)
								.addComponent(wageLabel))
							.addGap(4)
							.addGroup(gl_formsPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(genderComboBox, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
								.addComponent(wageSpinner, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))
							.addGap(23)
							.addComponent(confirmPasswordLabel)
							.addGap(5)
							.addComponent(confirmPasswordTextField, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))
						.addComponent(iconLabel, GroupLayout.PREFERRED_SIZE, 118, GroupLayout.PREFERRED_SIZE)))
		);
		formsPanel.setLayout(gl_formsPanel);
		
		//pack();
		setLocationRelativeTo(null);
	}
}