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

public class RatingDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private Controller c;
	private JScrollPane employeesForRatingsScrollPane;
	private DefaultTableModel employeesForRatingsTM;
	private JTable employeesToRateTable;

	public RatingDialog(Controller co, int currentProject, ArrayList<Employee> employeesToRate, JFrame utility) {
		c = co;
		setBounds(100, 100, 792, 431);
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
						"CF", "Valutazione per questo progetto"
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
	
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPanel.createSequentialGroup()
					.addContainerGap(91, Short.MAX_VALUE)
					.addComponent(employeesForRatingsScrollPane, GroupLayout.PREFERRED_SIZE, 595, GroupLayout.PREFERRED_SIZE)
					.addGap(80))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPanel.createSequentialGroup()
					.addContainerGap(22, Short.MAX_VALUE)
					.addComponent(employeesForRatingsScrollPane, GroupLayout.PREFERRED_SIZE, 308, GroupLayout.PREFERRED_SIZE)
					.addGap(19))
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(Color.decode("#4C566A"));
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			
			JButton btnTerminaEdEsci = new JButton("Termina ed esci");
			btnTerminaEdEsci.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent logout) {
					c.backToLogin(utility);
					dispose();
				}
			});
			btnTerminaEdEsci.setForeground(new Color(46, 52, 64));
			btnTerminaEdEsci.setFont(new Font("Roboto", Font.PLAIN, 14));
			btnTerminaEdEsci.setFocusPainted(false);
			btnTerminaEdEsci.setBorderPainted(false);
			btnTerminaEdEsci.setBackground(new Color(235, 203, 139));
			buttonPane.add(btnTerminaEdEsci);
		}
		setLocationRelativeTo(null);
	}
}
