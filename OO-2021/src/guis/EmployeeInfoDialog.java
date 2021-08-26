package guis;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controllers.Controller;
import entities.EmployeeRating;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.Color;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;

public class EmployeeInfoDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	
	// Dichiarazioni utili
	private Controller c;
	private final JPanel contentPanel = new JPanel();
	private JPanel buttonPane;
	private JScrollPane employeesInfoScrollPane;
	private DefaultTableModel employeeTM;
	private JTable employeeTable;
	private JLabel titleLabel;
	private JButton okButton;

	// Creazione dialog
	public EmployeeInfoDialog(Controller co, String cf, JFrame utility) throws Exception {
		c = co;
		JDialog toDispose = this;
		setUndecorated(true);
		setBounds(100, 100, 686, 408);
		getContentPane().setLayout(new BorderLayout());
		getRootPane().setBorder(BorderFactory.createLineBorder(Color.decode("#8FBCBB"), 2));
		contentPanel.setBackground(Color.decode("#4C566A"));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		// ScrollPane che contiene la tabella con le informazioni sui progetti passati di un dipendente
		employeesInfoScrollPane = new JScrollPane();
		employeesInfoScrollPane.setForeground(Color.decode("#434C5E"));
		employeesInfoScrollPane.setBackground(Color.decode("#434C5E"));
		employeesInfoScrollPane.setFont(new Font("Roboto", Font.PLAIN, 15));
		employeesInfoScrollPane.setBorder(new LineBorder(Color.decode("#434C5E"), 2, true));
		employeesInfoScrollPane.setBackground(new Color(67, 76, 94));
		
		// Table model che contiene le informazioni sui progetti passati di un dipendente
		employeeTM = new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
						"Codice Progetto", "Tipologia", "Valutazione"
				}
			);
		

		// Riempimento della tabella con le informazioni utili
		for (EmployeeRating er: c.findUserHistory(cf)) {
			employeeTM.addRow(new Object[] {er.getPastProject().getProjectNumber(),
										   er.getPastProject().getTypology().toString().replace('_', ' '),
										   er.getRating() == 0 ? "Non presente" : er.getRating()});
		}
		
		// Rende la table non editabile
		employeeTable = new JTable(employeeTM) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		// Caratteristiche estetiche della JTable
		employeeTable.setBackground(Color.decode("#ECEFF4"));
		employeeTable.setRowMargin(2);
		employeeTable.setRowHeight(24);
		employeeTable.setFont(new Font("Roboto", Font.PLAIN, 14));
		employeeTable.setForeground(Color.decode("#434C5E"));
		employeeTable.setGridColor(Color.decode("#B48EAD"));
		employeeTable.getTableHeader().setBackground(Color.decode("#B48EAD"));
		employeeTable.getTableHeader().setForeground(Color.decode("#ECEFF4"));
		employeeTable.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 14));
		employeeTable.getTableHeader().setReorderingAllowed(false);
		
		employeesInfoScrollPane.setViewportView(employeeTable);
		
		// Label con il titolo del frame
		titleLabel = new JLabel("Progetti passati");
		titleLabel.setIconTextGap(18);
		titleLabel.setIcon(new ImageIcon(EmployeeInfoDialog.class.getResource("/paste.png")));
		titleLabel.setForeground(Color.decode("#EBCB8B"));
		titleLabel.setFont(new Font("Roboto", Font.PLAIN, 28));
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		// Layout utilizzato
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(titleLabel, GroupLayout.DEFAULT_SIZE, 676, Short.MAX_VALUE))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(48)
							.addComponent(employeesInfoScrollPane, GroupLayout.PREFERRED_SIZE, 595, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addComponent(titleLabel, GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(employeesInfoScrollPane, GroupLayout.PREFERRED_SIZE, 308, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			buttonPane = new JPanel();
			buttonPane.setBackground(Color.decode("#4C566A"));
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.setFont(new Font("Roboto", Font.PLAIN, 14));
				okButton.setForeground(Color.decode("#2E3440"));
				okButton.setBackground(Color.decode("#EBCB8B"));
				okButton.setBorderPainted(false);
				okButton.setFocusPainted(false);
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent clickOK) {
						c.backToBackgroundFrame(utility, toDispose);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
		
		pack();
		setLocationRelativeTo(null);
	}
}
