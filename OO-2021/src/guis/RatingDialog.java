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
import java.awt.GridBagLayout;
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

	private final JPanel contentPanel = new JPanel();
	private Controller c;
	private JScrollPane employeesForRatingsScrollPane;
	private DefaultTableModel employeesForRatingsTM;
	private JTable employeesToRateTable;

	public RatingDialog(Controller co, int currentProject, ArrayList<Employee> employeesToRate, JFrame utility) {
		setUndecorated(true);
		setIconImage(Toolkit.getDefaultToolkit().getImage(RatingDialog.class.getResource("/bulb.png")));
		c = co;
		setBounds(100, 100, 718, 439);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(Color.decode("#4C566A"));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		
		employeesForRatingsScrollPane = new JScrollPane();
		employeesForRatingsScrollPane.setForeground(Color.decode("#434C5E"));
		employeesForRatingsScrollPane.setBackground(Color.decode("#434C5E"));
		employeesForRatingsScrollPane.setFont(new Font("Roboto", Font.PLAIN, 15));
		employeesForRatingsScrollPane.setBorder(new LineBorder(Color.decode("#434C5E"), 2, true));
		employeesForRatingsScrollPane.setBackground(new Color(67, 76, 94));
		
		// Table model che contiene le informazioni sui meeting
		employeesForRatingsTM = new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
						"CF", "Valutazione per questo progetto concordata con l'azienda"
				}
			);
		
		// Recupero informazioni sui dipendenti
		for (Employee em: employeesToRate) {
			employeesForRatingsTM.addRow(new Object[] {em.getFiscalCode(),
													   "Non presente"
			});
		}

		// Rende la table non editabile
		employeesToRateTable = new JTable(employeesForRatingsTM) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 1;
			}
		};
		
		employeesToRateTable.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if (employeesToRateTable.getSelectedRow() != -1) {
					try {
						c.insertRating(employeesToRateTable.getValueAt(employeesToRateTable.getSelectedRow(), 0).toString(),
									   Integer.valueOf(employeesToRateTable.getValueAt(employeesToRateTable.getSelectedRow(), 1).toString()),
									   currentProject);
					} catch (Exception e) {
						e.printStackTrace();
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
		
		JLabel titleLabel = new JLabel("Inserisci le valutazioni per questo progetto");
		titleLabel.setIconTextGap(18);
		titleLabel.setIcon(new ImageIcon(RatingDialog.class.getResource("/rating.png")));
		titleLabel.setForeground(Color.decode("#EBCB8B"));
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setFont(new Font("Roboto", Font.PLAIN, 28));
		
		JLabel infoLabel = new JLabel("Cliccando su \"Termina ed esci\" sarai reindirizzato alla schermata di login.");
		infoLabel.setForeground(Color.decode("#EBCB8B"));
		infoLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
		infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
	
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(titleLabel, GroupLayout.DEFAULT_SIZE, 708, Short.MAX_VALUE)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(55)
					.addComponent(employeesForRatingsScrollPane, GroupLayout.PREFERRED_SIZE, 595, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(58, Short.MAX_VALUE))
				.addComponent(infoLabel, GroupLayout.DEFAULT_SIZE, 708, Short.MAX_VALUE)
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addComponent(titleLabel, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(employeesForRatingsScrollPane, GroupLayout.PREFERRED_SIZE, 308, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(infoLabel)
					.addGap(15))
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPanel = new JPanel();
			buttonPanel.setBackground(Color.decode("#4C566A"));
			buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPanel, BorderLayout.SOUTH);
			
			JButton closeButton = new JButton("Termina ed esci");
			closeButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent logout) {
					c.backToLogin(utility);
					dispose();
				}
			});
			closeButton.setForeground(Color.decode("#2E3440"));
			closeButton.setFont(new Font("Roboto", Font.PLAIN, 14));
			closeButton.setFocusPainted(false);
			closeButton.setBorderPainted(false);
			closeButton.setBackground(new Color(235, 203, 139));
			buttonPanel.add(closeButton);
		}
		
		setLocationRelativeTo(null);
	}
}
