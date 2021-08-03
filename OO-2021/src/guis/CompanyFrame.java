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
import javax.swing.table.TableModel;
import javax.swing.BoxLayout;
import java.awt.Component;
import java.awt.GridLayout;

public class CompanyFrame extends JFrame {

	private JPanel contentPane;
	private JPanel welcomeCompanyPanel;
	private JPanel employeesPanel;
	private Controller c;
	private JTable employeesTable;
	private JTabbedPane companyTabbedPane;
	private DefaultTableModel employeesTM;
	private JScrollPane employeesScrollPane;
	private JPanel projectPanel;
	private JScrollPane projectScrollPane;
	private JTable projectTable;
	private JPanel bottomPanel;
	private DefaultTableModel projectsTM;
	private JLabel currentProjectLabel;

	// Creazione frame
	public CompanyFrame(Controller co, Company signedInCompany) throws Exception {
		setResizable(false);
		c = co;
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
		
		UIManager.put("TabbedPane.contentAreaColor", Color.decode("#ECEFF4"));
		UIManager.put("TabbedPane.selected", Color.decode("#5E81AC"));
		
		companyTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		companyTabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		companyTabbedPane.setFont(new Font("Roboto", Font.PLAIN, 12));
		companyTabbedPane.setForeground(Color.decode("#ECEFF4"));
		companyTabbedPane.setBackground(Color.decode("#B48EAD"));
		
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
		employeesScrollPane.setBounds(0, 0, 678, 258);
		employeesPanel.add(employeesScrollPane);
		
		// Table model che contiene le informazioni sui dipendenti
		employeesTM = new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"Codice Fiscale", "Nome", "Cognome", "Salario medio", "Valutazione media"
				}
			);
		
		// Recupero informazioni sui dipendenti
		for (Employee em: signedInCompany.getCompanyEmployees()) {
			int avgRating = c.takeRatingForEmployee(em.getFiscalCode());
			employeesTM.addRow(new Object[] {em.getFiscalCode(),
											 em.getName(),
											 em.getSurname(),
											 (int)em.getAvgWage() +" €",
											 avgRating == 0 ? "Non presente" : avgRating +"/5"});
		}
		

		// Rende la table non editabile
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
		
		bottomPanel = new JPanel();
		bottomPanel.setBackground(Color.decode("#4C566A"));
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(welcomeCompanyPanel, GroupLayout.DEFAULT_SIZE, 820, Short.MAX_VALUE)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap(65, Short.MAX_VALUE)
					.addComponent(companyTabbedPane, GroupLayout.PREFERRED_SIZE, 683, GroupLayout.PREFERRED_SIZE)
					.addGap(72))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(bottomPanel, GroupLayout.PREFERRED_SIZE, 810, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(welcomeCompanyPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(companyTabbedPane, GroupLayout.PREFERRED_SIZE, 286, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
					.addComponent(bottomPanel, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE))
		);
		GridBagLayout gbl_bottomPanel = new GridBagLayout();
		gbl_bottomPanel.columnWidths = new int[]{315, 200, 405, 0};
		gbl_bottomPanel.rowHeights = new int[]{75, 0};
		gbl_bottomPanel.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_bottomPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		bottomPanel.setLayout(gbl_bottomPanel);
		
		JButton logoutButton = new JButton("");
		JFrame loggingOut = this;
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
				c.backToLogin(loggingOut);
			}
		});
		logoutButton.setForeground(new Color(46, 52, 64));
		logoutButton.setFont(new Font("Roboto", Font.PLAIN, 12));
		logoutButton.setBackground(new Color(235, 203, 139));
		
		JButton newProjectButton = new JButton("Crea nuovo progetto");
		newProjectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent createNewProject) {
				c.openNewProjectFrame(signedInCompany);
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
		
		projectPanel = new JPanel();
		projectPanel.setBackground(Color.decode("#EBCB8B"));
		companyTabbedPane.addTab("Progetti attivi", null, projectPanel, null);
		projectPanel.setLayout(null);
		
		projectScrollPane = new JScrollPane();
		projectScrollPane.setForeground(Color.decode("#434C5E"));
		projectScrollPane.setBackground(Color.decode("#434C5E"));
		projectScrollPane.setFont(new Font("Roboto", Font.PLAIN, 15));
		projectScrollPane.setBorder(new LineBorder(Color.decode("#434C5E"), 2, true));
		projectScrollPane.getViewport().setBackground(Color.decode("#D8DEE9"));
		projectScrollPane.setBounds(0, 57, 678, 201);
		projectPanel.add(projectScrollPane);
		
		
		// Table model che contiene le informazioni sui progetti attivi
		projectsTM = new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
						"Codice Progetto", "Numero Partecipanti", "Budget", "Commissionato da"
				}
			);
		
		currentProjectLabel = new JLabel("<HTML> <center> NESSUN PROGETTO ATTIVO <center> <HTML>");
		currentProjectLabel.setForeground(Color.decode("#434C5E"));
		currentProjectLabel.setHorizontalAlignment(SwingConstants.CENTER);
		currentProjectLabel.setFont(new Font("Roboto", Font.BOLD, 17));
		currentProjectLabel.setBounds(0, 0, 678, 59);
		projectPanel.add(currentProjectLabel);
		
		// Recupero informazioni sui progetti
		if (signedInCompany.getCompanyProjects().size() != 0) {
			String commissionedBy = "";
			for (Project pro: signedInCompany.getCompanyProjects()) {
				if (pro.getProjectCustomer().equals(null))
					commissionedBy = pro.getProjectCustomer().getFiscalCode();
				else
					commissionedBy = pro.getProjectSociety().getVatNumber();
				
				// Riempimento tabella con le informazioni utili
				projectsTM.addRow(new Object[] {pro.getProjectNumber(),
												pro.getEmployees(),
												(int)pro.getBudget() +" €",
												commissionedBy});
			}
			
			currentProjectLabel.setText("<HTML> <center> Progetti in corso: <center> <HTML>");
		}
		else
			projectScrollPane.setVisible(false);

		// Rende la table non editabile
		employeesTable = new JTable(employeesTM) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 3;
			}
		};
		
		
		projectTable = new JTable(projectsTM) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
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
}
