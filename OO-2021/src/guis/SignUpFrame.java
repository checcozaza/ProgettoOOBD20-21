package guis;

import java.awt.BorderLayout;
import java.awt.Color;
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
import java.awt.Toolkit;

public class SignUpFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	// Dichiarazioni utili
	private Controller c;
	private JPanel contentPane;
	private JPanel titlePanel;
	private JPanel buttonPanel;
	private JPanel formsPanel;
	private JTextField nameTextField;
	private JTextField surnameTextField;
	private JPasswordField passwordTextField;
	private JPasswordField confirmPasswordTextField;
	private JComboBox<Object> regionComboBox;
	private JComboBox<Object> provinceComboBox;
	private JComboBox<Object> cityComboBox;
	private JComboBox<Object> companyComboBox;
	private JComboBox<String> genderComboBox;
	private JSpinner wageSpinner;
	private DatePicker birthDatePicker;
	private JButton goBackButton;
	private JButton signUpButton;
	
	// Creazione frame
	public SignUpFrame(Controller co) throws Exception {
		setResizable(false);
		c = co;
		JFrame utility = this;
		setTitle("Registrazione - Projesting");
		setIconImage(Toolkit.getDefaultToolkit().getImage(SignUpFrame.class.getResource("/bulb.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 564, 401);
		contentPane = new JPanel();
		contentPane.setBackground(Color.decode("#4C566A"));
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		// Panel contenente il titolo del frame
		titlePanel = new JPanel();
		titlePanel.setBackground(Color.decode("#434C5E"));
		contentPane.add(titlePanel, BorderLayout.NORTH);
		titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		// Label informativa
		JLabel signUpLabel = new JLabel("Registrazione");
		signUpLabel.setIconTextGap(18);
		signUpLabel.setIcon(new ImageIcon(SignUpFrame.class.getResource("/contract.png")));
		signUpLabel.setForeground(Color.decode("#EBCB8B"));
		signUpLabel.setFont(new Font("Roboto", Font.PLAIN, 28));
		titlePanel.add(signUpLabel);
		
		// Panel di fondo del frame contenente i bottoni
		buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.decode("#4C566A"));
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		// Bottone per tornare al frame precedente
		goBackButton = new JButton("Indietro");
		goBackButton.setForeground(Color.decode("#2E3440"));
		goBackButton.setFocusPainted(false);
		goBackButton.setBorderPainted(false);
		goBackButton.setBackground(Color.decode("#EBCB8B"));
		goBackButton.setFont(new Font("Roboto", Font.PLAIN, 12));
		
		goBackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent goBack) {
				c.goBack(utility);
			}
		});
		buttonPanel.add(goBackButton);
		
		// Bottone registrazione
		signUpButton = new JButton("Registrati");
		signUpButton.setForeground(Color.decode("#2E3440"));
		signUpButton.setFocusPainted(false);
		signUpButton.setBorderPainted(false);
		signUpButton.setBackground(Color.decode("#EBCB8B"));
		signUpButton.setFont(new Font("Roboto", Font.PLAIN, 12));
		
		signUpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent clickSignUp) {
				boolean valid = c.checkCredentials(nameTextField.getText(), surnameTextField.getText(),
								   				   new String (passwordTextField.getPassword()), 
								   				   new String (confirmPasswordTextField.getPassword()),
								   				   birthDatePicker.getDate()); /*Al click del  tasto "Registrati", il metodo
								   				   								controlla la validità dei dati inseriti */
				
				if (valid) { // Se i dati inseriti sono validi
					try {
						// Genera il codice fiscale a partire dai dati inseriti
						String cf = c.cfGenerator(nameTextField.getText(), surnameTextField.getText(),
									  			  birthDatePicker.getDate(), 
									  			  genderComboBox.getSelectedItem().toString().toCharArray() [0], 
									  			  cityComboBox.getSelectedItem().toString()); 
						
						// Prende l'azienda inserita													  
						Company hiringCompany = new Company(companyComboBox.getSelectedItem().toString(), 
															null, null, null, null, null); 
						
						
						// Procede alla registrazione dell'utente inserendo nel DB i dati immessi e quelli calcolati
						c.insertEmployee(new Employee(cf, nameTextField.getText(), surnameTextField.getText(),
										 			  new String (passwordTextField.getPassword()), null, 
										 			  Float.parseFloat(wageSpinner.getValue().toString()),
										 			  hiringCompany, null, null, null));
						
						/* Apre una finestra di dialogo in cui sarà presente il codice fiscale appena calcolato (copiabile)
						per facilitarne l'inserimento in fase di login */
						c.openSuccessDialog(cf);
						
					} catch (Exception failedSignUp) {
						c.openPopupDialog(utility, "Registrazione non avvenuta correttamente.");
					}
				}
				else {
					c.openPopupDialog(utility, "Si prega di compilare correttamente tutti i campi"); // Popup di errore
				}
			}
		});
		buttonPanel.add(signUpButton);
		
		// Panel contenente le componenti utili alla registrazione
		formsPanel = new JPanel();
		formsPanel.setBackground(Color.decode("#4C566A"));
		contentPane.add(formsPanel, BorderLayout.CENTER);
		
		// Label informativa
		JLabel nameLabel = new JLabel("Nome");
		nameLabel.setForeground(Color.decode("#D8DEE9"));
		nameLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
		
		// Label informativa
		JLabel surnameLabel = new JLabel("Cognome");
		surnameLabel.setForeground(Color.decode("#D8DEE9"));
		surnameLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
		
		// Label informativa
		JLabel genderLabel = new JLabel("Sesso");
		genderLabel.setForeground(Color.decode("#D8DEE9"));
		genderLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
		
		// Textfield dove scrivere il nome dell'utente
		nameTextField = new JTextField();
		nameTextField.setBackground(Color.WHITE);
		nameTextField.setForeground(Color.decode("#5E81AC"));
		nameTextField.setFont(new Font("Roboto", Font.PLAIN, 14));
		nameTextField.setColumns(10);
		
		// Textfield dove scrivere il cognome dell'utente
		surnameTextField = new JTextField();
		surnameTextField.setBackground(Color.WHITE);
		surnameTextField.setForeground(Color.decode("#5E81AC"));
		surnameTextField.setFont(new Font("Roboto", Font.PLAIN, 14));
		surnameTextField.setColumns(10);
		
		// ComboBox dove selezionare il sesso dell'utente
		genderComboBox = new JComboBox<String>();
		genderComboBox.setBackground(Color.WHITE);
		genderComboBox.setForeground(Color.decode("#5E81AC"));
		genderComboBox.setFont(new Font("Roboto", Font.PLAIN, 14));
		genderComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"M", "F"}));
		
		// Label informativa
		JLabel birthDateLabel = new JLabel("Data di nascita");
		birthDateLabel.setForeground(Color.decode("#D8DEE9"));
		birthDateLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
		
		// Label informativa
		JLabel placeOfBirthLabel = new JLabel("Luogo di nascita");
		placeOfBirthLabel.setForeground(Color.decode("#D8DEE9"));
		placeOfBirthLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
		
		// BirthDatePicker per inserire la data di nascita dell'utente
		birthDatePicker = new DatePicker();
		birthDatePicker.getComponentToggleCalendarButton().setBackground(Color.WHITE);
		birthDatePicker.getComponentDateTextField().setBackground(Color.WHITE);
		birthDatePicker.getComponentToggleCalendarButton().setForeground(Color.decode("#5E81AC"));
		birthDatePicker.getComponentDateTextField().setForeground(Color.decode("#5E81AC"));
		birthDatePicker.getComponentDateTextField().setFont(new Font("Roboto", Font.PLAIN, 14));
		
		// ComboBox dove selezionare la regione di nascita dell'utente
		regionComboBox = new JComboBox<Object>();
		regionComboBox.setFont(new Font("Roboto", Font.PLAIN, 14));
		regionComboBox.setBackground(Color.WHITE);
		regionComboBox.setForeground(Color.decode("#5E81AC"));
		regionComboBox.setModel(new DefaultComboBoxModel<>(c.pickRegions()));
		
		regionComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent clickRegion) {
				try {
					provinceComboBox.setModel(new DefaultComboBoxModel<>(c.pickProvince(regionComboBox.getSelectedItem().toString())));
					cityComboBox.setModel(new DefaultComboBoxModel<>(c.pickCity(provinceComboBox.getSelectedItem().toString())));
				} catch (Exception regionNotFound) {
					c.openPopupDialog(utility, "Recupero dati fallito.");
				}
			}
		});
		
		// Label informativa
		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setForeground(Color.decode("#D8DEE9"));
		passwordLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
		
		// Label informativa
		JLabel confirmPasswordLabel = new JLabel("Conferma password");
		confirmPasswordLabel.setForeground(Color.decode("#D8DEE9"));
		confirmPasswordLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
		
		// Textfield dove scrivere la password dell'utente
		passwordTextField = new JPasswordField();
		passwordTextField.setToolTipText("La password deve contenere almeno una lettera, un numero e un carattere speciale.");
		passwordTextField.setBackground(Color.WHITE);
		passwordTextField.setForeground(Color.decode("#5E81AC"));
		passwordTextField.setFont(new Font("Roboto", Font.PLAIN, 14));
		
		// Textfield dove confermare la password dell'utente
		confirmPasswordTextField = new JPasswordField();
		confirmPasswordTextField.setBackground(Color.WHITE);
		confirmPasswordTextField.setForeground(Color.decode("#5E81AC"));
		confirmPasswordTextField.setFont(new Font("Roboto", Font.PLAIN, 14));
		
		// ComboBox dove selezionare la provincia di nascita dell'utente
		provinceComboBox = new JComboBox<Object>();
		provinceComboBox.setFont(new Font("Roboto", Font.PLAIN, 14));
		provinceComboBox.setBackground(Color.WHITE);
		provinceComboBox.setForeground(Color.decode("#5E81AC"));
		provinceComboBox.setModel(new DefaultComboBoxModel<>(c.pickProvince(regionComboBox.getSelectedItem().toString())));
		
		provinceComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent clickProvince) {
				try {
					cityComboBox.setModel(new DefaultComboBoxModel<>(c.pickCity(provinceComboBox.getSelectedItem().toString())));
				} catch (Exception provinceNotFound) {
					c.openPopupDialog(utility, "Recupero dati fallito.");
				}
			}
		});
		
		// ComboBox dove selezionare la città di nascita dell'utente
		cityComboBox =new JComboBox<Object>();
		cityComboBox.setFont(new Font("Roboto", Font.PLAIN, 14));
		cityComboBox.setBackground(Color.WHITE);
		cityComboBox.setForeground(Color.decode("#5E81AC"));
		cityComboBox.setModel(new DefaultComboBoxModel<>(c.pickCity(provinceComboBox.getSelectedItem().toString())));
		
		// Label informativa
		JLabel iconLabel = new JLabel("");
		iconLabel.setIcon(new ImageIcon(SignUpFrame.class.getResource("/folder.png")));
		
		// Spinner dove inserire il salario medio dell'utente
		wageSpinner = new JSpinner();
		wageSpinner.setFont(new Font("Roboto", Font.PLAIN, 14));
		wageSpinner.setModel(new SpinnerNumberModel(new Float(1500), new Float(1499), null, new Float(100)));
		wageSpinner.getEditor().getComponent(0).setForeground(Color.decode("#5E81AC"));
		
		// Label informativa
		JLabel wageLabel = new JLabel("Salario medio");
		wageLabel.setForeground(Color.decode("#D8DEE9"));
		wageLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
		
		// Label informativa
		JLabel companyLabel = new JLabel("Partita IVA Sede");
		companyLabel.setForeground(new Color(216, 222, 233));
		companyLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
		
		// ComboBox dove selezionare l'azienda che impiega l'utente
		companyComboBox = new JComboBox<Object>();
		companyComboBox.setForeground(new Color(94, 129, 172));
		companyComboBox.setFont(new Font("Roboto", Font.PLAIN, 14));
		companyComboBox.setBackground(Color.WHITE);
		companyComboBox.setModel(new DefaultComboBoxModel<>(c.pickCompanies()));
		
		// Layout utilizzato
		GroupLayout gl_formsPanel = new GroupLayout(formsPanel);
		gl_formsPanel.setHorizontalGroup(
			gl_formsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_formsPanel.createSequentialGroup()
					.addGap(30)
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
							.addComponent(surnameLabel, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE))
						.addComponent(placeOfBirthLabel, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_formsPanel.createSequentialGroup()
							.addComponent(regionComboBox, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)
							.addGap(23)
							.addComponent(provinceComboBox, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)
							.addGap(23)
							.addComponent(cityComboBox, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)))
					.addGap(37)
					.addGroup(gl_formsPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(companyLabel, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_formsPanel.createParallelGroup(Alignment.TRAILING)
							.addComponent(iconLabel)
							.addComponent(companyComboBox, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)))
					.addGap(189))
		);
		gl_formsPanel.setVerticalGroup(
			gl_formsPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_formsPanel.createSequentialGroup()
					.addContainerGap(14, Short.MAX_VALUE)
					.addGroup(gl_formsPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_formsPanel.createParallelGroup(Alignment.LEADING)
							.addComponent(nameLabel)
							.addComponent(surnameLabel))
						.addComponent(companyLabel, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
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
								.addComponent(cityComboBox, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))
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
									.addGap(3)
									.addGroup(gl_formsPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(iconLabel, GroupLayout.PREFERRED_SIZE, 118, GroupLayout.PREFERRED_SIZE)
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
											.addComponent(confirmPasswordTextField, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)))))
							.addPreferredGap(ComponentPlacement.RELATED))
						.addGroup(gl_formsPanel.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(companyComboBox, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)))
					.addGap(121))
		);
		formsPanel.setLayout(gl_formsPanel);

		setLocationRelativeTo(null);
	}
}
