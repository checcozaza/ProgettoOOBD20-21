package guis;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controllers.Controller;
import entities.Employee;
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
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.Toolkit;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;

public class RatingDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	
	// Dichiarazioni utili
	private Controller c;
	private final JPanel contentPanel = new JPanel();
	private JPanel buttonPanel;
	private JScrollPane employeesForRatingsScrollPane;
	private DefaultTableModel employeesForRatingsTM;
	private JTable employeesToRateTable;
	private JButton closeButton;

	// Creazione dialog
	public RatingDialog(Controller co, int currentProject, ArrayList<Employee> employeesToRate, JFrame utility) {
		c = co;
		JDialog utilityDialog = this;
		setUndecorated(true);
		setIconImage(Toolkit.getDefaultToolkit().getImage(RatingDialog.class.getResource("/bulb.png")));
		setBounds(100, 100, 718, 439);
		getContentPane().setLayout(new BorderLayout());
		getRootPane().setBorder(BorderFactory.createLineBorder(Color.decode("#8FBCBB"), 2));
		contentPanel.setBackground(Color.decode("#4C566A"));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		// ScrollPane contenente informazioni sulle valutazioni di ciascun dipendente
		employeesForRatingsScrollPane = new JScrollPane();
		employeesForRatingsScrollPane.setForeground(Color.decode("#434C5E"));
		employeesForRatingsScrollPane.setBackground(Color.decode("#434C5E"));
		employeesForRatingsScrollPane.setFont(new Font("Roboto", Font.PLAIN, 15));
		employeesForRatingsScrollPane.setBorder(new LineBorder(Color.decode("#434C5E"), 2, true));
		employeesForRatingsScrollPane.setBackground(new Color(67, 76, 94));
		
		// Table model che contiene le informazioni sulle valutazioni
		employeesForRatingsTM = new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
						"CF", "Valutazione per questo progetto concordata con l'azienda"
				}
			);
		
		// Recupero informazioni sui dipendenti e le loro valutazioni
		for (Employee em: employeesToRate) {
			employeesForRatingsTM.addRow(new Object[] {em.getFiscalCode(),
													   "Non presente"
			});
		}

		// Rende la table non editabile fatta eccezione per la valutazione, che può inserire il project manager
		employeesToRateTable = new JTable(employeesForRatingsTM) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 1;
			}
		};
		
		// PropertyChangeListener che aggiorna la valutazione di ogni utente dopo l'inserimento
		employeesToRateTable.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if (employeesToRateTable.getSelectedRow() != -1) { // Se è stato selezionato un progettista
					try {
						if (!employeesToRateTable.getValueAt(employeesToRateTable.getSelectedRow(), 1).toString().equals("Non presente"))
							c.insertRating(employeesToRateTable.getValueAt(employeesToRateTable.getSelectedRow(), 0).toString(),
										   Integer.valueOf(employeesToRateTable.getValueAt(employeesToRateTable.getSelectedRow(), 1).toString()),
										   currentProject);
					} catch (NumberFormatException nAn) {
						nAn.printStackTrace();
						c.openPopupDialog(utilityDialog, "Il valore inserito non è valido");
					} catch (Exception ratingFailed) {
						ratingFailed.printStackTrace(); 
						c.openPopupDialog(utilityDialog, "Impossibile inserire la valutazione");
					}
				}
			}
		});
		
		// Caratteristiche estetiche della JTable
		employeesToRateTable.setBackground(Color.decode("#ECEFF4"));
		employeesToRateTable.setRowMargin(2);
		employeesToRateTable.setRowHeight(24);
		employeesToRateTable.setFont(new Font("Roboto", Font.PLAIN, 14));
		employeesToRateTable.setForeground(Color.decode("#434C5E"));
		employeesToRateTable.setGridColor(Color.decode("#B48EAD"));
		employeesToRateTable.getTableHeader().setBackground(Color.decode("#B48EAD"));
		employeesToRateTable.getTableHeader().setForeground(Color.decode("#ECEFF4"));
		employeesToRateTable.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 14));
		employeesToRateTable.getTableHeader().setReorderingAllowed(false);
		
		employeesForRatingsScrollPane.setViewportView(employeesToRateTable);
		
		// Label informativa
		JLabel titleLabel = new JLabel("Inserisci le valutazioni per questo progetto");
		titleLabel.setIconTextGap(18);
		titleLabel.setIcon(new ImageIcon(RatingDialog.class.getResource("/rating.png")));
		titleLabel.setForeground(Color.decode("#EBCB8B"));
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setFont(new Font("Roboto", Font.PLAIN, 28));
		
		// Label informativa
		JLabel infoLabel = new JLabel("Cliccando su \"Termina ed esci\" sarai reindirizzato alla schermata di login.");
		infoLabel.setForeground(Color.decode("#EBCB8B"));
		infoLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
		infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
	
		// Layout utilizzato
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(titleLabel, GroupLayout.DEFAULT_SIZE, 708, Short.MAX_VALUE)
				.addComponent(infoLabel, GroupLayout.DEFAULT_SIZE, 708, Short.MAX_VALUE)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(55)
					.addComponent(employeesForRatingsScrollPane, GroupLayout.PREFERRED_SIZE, 595, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(58, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addComponent(titleLabel, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(employeesForRatingsScrollPane, GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE)
					.addGap(18)
					.addComponent(infoLabel)
					.addGap(15))
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			// Panel di fondo della dialog
			buttonPanel = new JPanel();
			buttonPanel.setBackground(Color.decode("#4C566A"));
			buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPanel, BorderLayout.SOUTH);
			
			// Bottone per chiudere la dialog
			closeButton = new JButton("Termina ed esci");
			closeButton.setForeground(Color.decode("#2E3440"));
			closeButton.setFont(new Font("Roboto", Font.PLAIN, 14));
			closeButton.setFocusPainted(false);
			closeButton.setBorderPainted(false);
			closeButton.setBackground(Color.decode("#EBCB8B"));
			
			closeButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent logout) {
					c.backToLogin(utility);
					dispose();
				}
			});
			buttonPanel.add(closeButton);
		}
		
		pack();
		setLocationRelativeTo(null);
	}
}
