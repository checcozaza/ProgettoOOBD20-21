package guis;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import controllers.Controller;
import entities.Company;
import entities.Employee;
import entities.Project;

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
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.Component;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class CompanyFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	// Dichiarazioni utili
	private Controller c;
	private JPanel contentPane;
	private JTabbedPane companyTabbedPane;
	private JPanel welcomeCompanyPanel;
	private JPanel employeesPanel;
	private JPanel projectPanel;
	private JPanel bottomPanel;
	private JScrollPane employeesScrollPane;
	private JScrollPane projectScrollPane;
	private DefaultTableModel employeesTM;
	private DefaultTableModel projectsTM;
	private JTable employeesTable;
	private JTable projectTable;
	private JLabel currentProjectLabel;
	private JButton newProjectButton;
	private JButton logoutButton;

	// Creazione frame
	public CompanyFrame(Controller co, Company signedInCompany) throws Exception {
		c = co;
		JFrame utility = this;
		setResizable(false);
		setTitle("Homepage - Projesting");
		setIconImage(Toolkit.getDefaultToolkit().getImage(CompanyFrame.class.getResource("/bulb.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 819, 531);
		contentPane = new JPanel();
		contentPane.setBackground(Color.decode("#4C566A"));
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		
		// Panel azienda
		welcomeCompanyPanel = new JPanel();
		welcomeCompanyPanel.setBackground(Color.decode("#434C5E"));
		
		// Label di benvenuto
		JLabel welcomeCompanyLabel = new JLabel("<HTML> <center> Benvenuto, <br>"+ signedInCompany.getName() 
											  + " (P. IVA " + signedInCompany.getVatNumber() + ") </center> </HTML>");
		welcomeCompanyLabel.setForeground(Color.decode("#EBCB8B"));
		welcomeCompanyLabel.setFont(new Font("Roboto", Font.PLAIN, 28));
		welcomeCompanyPanel.add(welcomeCompanyLabel);
		
		// Caratteristiche estetiche TabbedPane
		UIManager.put("TabbedPane.contentAreaColor", Color.decode("#ECEFF4"));
		UIManager.put("TabbedPane.selected", Color.decode("#5E81AC"));
		
		companyTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		companyTabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		companyTabbedPane.setFont(new Font("Roboto", Font.PLAIN, 12));
		companyTabbedPane.setForeground(Color.decode("#ECEFF4"));
		companyTabbedPane.setBackground(Color.decode("#B48EAD"));
		
		// Panel che racchiude le informazioni sui dipendenti
		employeesPanel = new JPanel();
		employeesPanel.setBackground(Color.decode("#EBCB8B"));
		companyTabbedPane.addTab("Gestione dipendenti", null, employeesPanel, null);
		employeesPanel.setLayout(null);
		
		//Caratteristiche dello ScrollPane 
		employeesScrollPane = new JScrollPane();
		employeesScrollPane.setForeground(Color.decode("#434C5E"));
		employeesScrollPane.setBackground(Color.decode("#434C5E"));
		employeesScrollPane.setFont(new Font("Roboto", Font.PLAIN, 15));
		employeesScrollPane.setBorder(new LineBorder(Color.decode("#434C5E"), 2, true));
		employeesScrollPane.getViewport().setBackground(Color.decode("#D8DEE9"));
		employeesScrollPane.setBounds(0, 0, 748, 258);
		employeesPanel.add(employeesScrollPane);
		
		// Table model che contiene le informazioni sui dipendenti
		employeesTM = new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"Codice Fiscale", "Nome", "Cognome", "Salario medio", "Valutazione media", "Occupato"
				}
			);
		
		// Recupero informazioni sui dipendenti
		for (Employee em: signedInCompany.getCompanyEmployees()) { // Iterazione su ciascun dipendente dell'azienda
			int avgRating = c.takeRatingForEmployee(em.getFiscalCode()); // Variabile per conservare l'eventuale valutazione di un dipendente
			String isBusy = "";
			if (em.getEmployeeProject().getProjectNumber() == 0) // Setta un dipendente a "occupato" o meno a seconda del codice progetto associato
				isBusy = "No";
			else
				isBusy = "Sì";
			
			// Riempimento tabella con le informazioni utili
			employeesTM.addRow(new Object[] {em.getFiscalCode(),
											 em.getName(),
											 em.getSurname(),
											 (int)em.getAvgWage(),
											 avgRating == 0 ? "Non presente" : avgRating +"/5",
											 isBusy});
		}

		// Rende la table editabile solo nella colonna relativa al salario di un dipendente, che l'azienda può modificare
		employeesTable = new JTable(employeesTM) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 3;
			}
		};
		
		// Caratteristiche estetiche della JTable
		employeesTable.setBackground(Color.decode("#ECEFF4"));
		employeesTable.setRowMargin(2);
		employeesTable.setRowHeight(24);
		employeesTable.setFont(new Font("Roboto", Font.PLAIN, 14));
		employeesTable.setForeground(Color.decode("#434C5E"));
		employeesTable.setGridColor(Color.decode("#B48EAD"));
		employeesTable.getTableHeader().setBackground(Color.decode("#B48EAD"));
		employeesTable.getTableHeader().setForeground(Color.decode("#ECEFF4"));
		employeesTable.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 14));
		employeesTable.getTableHeader().setReorderingAllowed(false);
		
		employeesScrollPane.setViewportView(employeesTable);
		
		// Property change listener che va ad aggiornare il valore del salario di un dipendente a quello appena inserito
		employeesTable.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent changeWage) {
				if (employeesTable.getSelectedRow() != -1) {
					try {
						c.updateWage(employeesTable.getValueAt(employeesTable.getSelectedRow(), 0).toString(),
								Float.valueOf(employeesTable.getValueAt(employeesTable.getSelectedRow(), 3).toString()));
					} catch (Exception e) {
						c.openPopupDialog(utility, "Ops! Qualcosa è andato storto; riprova.");
					}
				}
			}
		});
		
		// Panel di fondo del frame che contiene icone e bottoni
		bottomPanel = new JPanel();
		bottomPanel.setBackground(Color.decode("#4C566A"));
		
		// Label con informazioni per l'utilizzo ottimale dell'applicativo
		JLabel infoLabel = new JLabel("Per la creazione di un nuovo progetto, scegli prima il project manager selezionandolo tra i dipendenti attualmente liberi.");
		infoLabel.setForeground(Color.decode("#EBCB8B"));
		infoLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
		infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		// Layout utilizzato
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addComponent(welcomeCompanyPanel, GroupLayout.DEFAULT_SIZE, 820, Short.MAX_VALUE)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(bottomPanel, GroupLayout.PREFERRED_SIZE, 810, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap(33, Short.MAX_VALUE)
					.addComponent(companyTabbedPane, GroupLayout.PREFERRED_SIZE, 753, GroupLayout.PREFERRED_SIZE)
					.addGap(34))
				.addComponent(infoLabel, GroupLayout.DEFAULT_SIZE, 820, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(welcomeCompanyPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(companyTabbedPane, GroupLayout.PREFERRED_SIZE, 286, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(infoLabel, GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(bottomPanel, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE))
		);
		GridBagLayout gbl_bottomPanel = new GridBagLayout();
		gbl_bottomPanel.columnWidths = new int[]{315, 200, 405, 0};
		gbl_bottomPanel.rowHeights = new int[]{75, 0};
		gbl_bottomPanel.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_bottomPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		bottomPanel.setLayout(gbl_bottomPanel);
		
		// Bottone di logout
		logoutButton = new JButton("");
		GridBagConstraints gbc_logoutButton = new GridBagConstraints();
		gbc_logoutButton.anchor = GridBagConstraints.WEST;
		gbc_logoutButton.fill = GridBagConstraints.VERTICAL;
		gbc_logoutButton.insets = new Insets(0, 0, 0, 5);
		gbc_logoutButton.gridx = 0;
		gbc_logoutButton.gridy = 0;
		bottomPanel.add(logoutButton, gbc_logoutButton);
		logoutButton.setFocusPainted(false);
		logoutButton.setBorderPainted(false);
		logoutButton.setToolTipText("Logout");
		logoutButton.setContentAreaFilled(false);
		logoutButton.setIcon(new ImageIcon(CompanyFrame.class.getResource("/logout.png")));
		
		logoutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent clickLogout) {
				System.exit(0);
			}
		});
		logoutButton.setForeground(new Color(46, 52, 64));
		logoutButton.setFont(new Font("Roboto", Font.PLAIN, 12));
		logoutButton.setBackground(new Color(235, 203, 139));
		
		// Bottone per la creazione di un nuovo progetto
		newProjectButton = new JButton("Crea nuovo progetto");
		newProjectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent createNewProject) {
				if (employeesTable.getSelectedRow() != -1 && 
					employeesTable.getValueAt(employeesTable.getSelectedRow(), 5).equals("No")) { // Se è stato selezionato almeno un dipendente occupato
					try {
						c.openNewProjectFrame(signedInCompany, employeesTable.getValueAt(employeesTable.getSelectedRow(), 0).toString(), employeesTM, employeesTable); // Apertura frame per la creazione di un progetto
					} catch (Exception e) {
						c.openPopupDialog(utility, "Ops! Qualcosa è andato storto; riprova.");
					}
				}
				else
					c.openPopupDialog(utility, "<HTML> <center> Per creare un nuovo progetto, seleziona prima il Project Manager "
											 + "(non deve essere occupato) <center> <HTML>");
			}
		});
		newProjectButton.setBorderPainted(false);
		newProjectButton.setForeground(Color.decode("#EBCB8B"));
		newProjectButton.setContentAreaFilled(false);
		newProjectButton.setFocusPainted(false);
		newProjectButton.setFont(new Font("Roboto", Font.PLAIN, 16));
		newProjectButton.setIcon(new ImageIcon(CompanyFrame.class.getResource("/add.png")));
		GridBagConstraints gbc_newProjectButton = new GridBagConstraints();
		gbc_newProjectButton.anchor = GridBagConstraints.WEST;
		gbc_newProjectButton.insets = new Insets(0, 0, 0, 5);
		gbc_newProjectButton.gridx = 1;
		gbc_newProjectButton.gridy = 0;
		bottomPanel.add(newProjectButton, gbc_newProjectButton);
		
		
		// Label per icona decorativa
		JLabel companyIconLabel = new JLabel("");
		companyIconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		GridBagConstraints gbc_companyIconLabel = new GridBagConstraints();
		gbc_companyIconLabel.anchor = GridBagConstraints.EAST;
		gbc_companyIconLabel.fill = GridBagConstraints.VERTICAL;
		gbc_companyIconLabel.gridx = 2;
		gbc_companyIconLabel.gridy = 0;
		bottomPanel.add(companyIconLabel, gbc_companyIconLabel);
		companyIconLabel.setIcon(new ImageIcon(CompanyFrame.class.getResource("/briefcase.png")));

		employeesScrollPane.setViewportView(employeesTable);
		
		// Panel che racchiude le informazioni sui progetti attivi
		projectPanel = new JPanel();
		projectPanel.setBackground(Color.decode("#EBCB8B"));
		companyTabbedPane.addTab("Progetti attivi", null, projectPanel, null);
		projectPanel.setLayout(null);
		
		// ScrollPane che contiene la tabella sui progetti attivi
		projectScrollPane = new JScrollPane();
		projectScrollPane.setForeground(Color.decode("#434C5E"));
		projectScrollPane.setBackground(Color.decode("#434C5E"));
		projectScrollPane.setFont(new Font("Roboto", Font.PLAIN, 15));
		projectScrollPane.setBorder(new LineBorder(Color.decode("#434C5E"), 2, true));
		projectScrollPane.getViewport().setBackground(Color.decode("#D8DEE9"));
		projectScrollPane.setBounds(0, 57, 748, 201);
		projectPanel.add(projectScrollPane);
		
		// Table model che contiene le informazioni sui progetti attivi
		projectsTM = new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
						"Codice Progetto", "Numero Partecipanti", "Budget", "Commissionato da"
				}
			);
		
		// Label con informazioni sui progetti attivi; il suo testo viene modificato a seconda delle situazioni
		currentProjectLabel = new JLabel("<HTML> <center> NESSUN PROGETTO ATTIVO <br> Le informazioni sui progetti appena inseriti saranno disponibili a breve. <center> <HTML>");
		currentProjectLabel.setForeground(Color.decode("#434C5E"));
		currentProjectLabel.setHorizontalAlignment(SwingConstants.CENTER);
		currentProjectLabel.setFont(new Font("Roboto", Font.BOLD, 17));
		currentProjectLabel.setBounds(0, 0, 748, 59);
		projectPanel.add(currentProjectLabel);
		
		// Recupero informazioni sui progetti
		if (signedInCompany.getCompanyProjects().size() != 0) { // Se l'azienda ha almeno un progetto attivo
			String commissionedBy = "";
			for (Project pro: signedInCompany.getCompanyProjects()) {
				if (pro.getProjectCustomer().getFiscalCode() != null) // Controlla chi ha commissionato il progetto
					commissionedBy = pro.getProjectCustomer().getFiscalCode();
				else
					commissionedBy = pro.getProjectSociety().getVatNumber();
				
				// Riempimento tabella con le informazioni utili
				projectsTM.addRow(new Object[] {pro.getProjectNumber(),
												pro.getEmployees(),
												(int)pro.getBudget() +" €",
												commissionedBy});
			}
			currentProjectLabel.setText("<HTML> <center> Progetti in corso: <center> <HTML>"); // Cambia il testo della label informativa
		}
		else
			projectScrollPane.setVisible(false); // Se non ci sono progetti attivi, invece di mostrare la JTable vuota si rende lo ScrollPane invisibile
		
		// Rende la table non editabile
		projectTable = new JTable(projectsTM) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		// Caratteristiche estetiche della JTable
		projectTable.setBackground(Color.decode("#ECEFF4"));
		projectTable.setRowMargin(2);
		projectTable.setRowHeight(24);
		projectTable.setFont(new Font("Roboto", Font.PLAIN, 14));
		projectTable.setForeground(Color.decode("#434C5E"));
		projectTable.setGridColor(Color.decode("#B48EAD"));
		projectTable.getTableHeader().setBackground(Color.decode("#B48EAD"));
		projectTable.getTableHeader().setForeground(Color.decode("#ECEFF4"));
		projectTable.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 14));
		projectTable.getTableHeader().setReorderingAllowed(false);
		
		projectScrollPane.setViewportView(projectTable);
		contentPane.setLayout(gl_contentPane);
		
		pack();
		setLocationRelativeTo(null);
	}

	public void updateEmployeesTable(DefaultTableModel employeesTM2) {
		employeesTable.setModel(employeesTM2);
	}
}
