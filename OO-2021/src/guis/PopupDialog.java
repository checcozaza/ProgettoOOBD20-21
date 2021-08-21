package guis;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import controllers.Controller;

public class PopupDialog extends JDialog {

	// Dichiarazioni utili
	private Controller c;
	private final JPanel contentPanel = new JPanel();
	private JPanel buttonPane;
	private JLabel messageLabel;
	private JButton okButton;
	
	// Creazione Dialog
	public PopupDialog(Controller co, JFrame utility, String popupMessage) {
		c = co;
		setResizable(false);
		getContentPane().setBackground(Color.decode("#3B4252"));
		setUndecorated(true);
		setBounds(100, 100, 468, 144);
		getContentPane().setLayout(new BorderLayout());
		getRootPane().setBorder(BorderFactory.createLineBorder(Color.decode("#8FBCBB"), 2));
		contentPanel.setBackground(Color.decode("#4C566A"));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			messageLabel = new JLabel("<HTML> <p> <center> " +popupMessage+ "</center> </p> </HTML>");
			messageLabel.setVerticalAlignment(SwingConstants.TOP);
			messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
			messageLabel.setFont(new Font("Roboto", Font.PLAIN, 16));
			messageLabel.setForeground(Color.decode("#EBCB8B"));
			messageLabel.setBackground(Color.decode("#4C566A"));
		}
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(messageLabel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(messageLabel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			// Panel di fondo del frame contenente un bottone per chiudere la dialog e tornare al frame sottostante.
			buttonPane = new JPanel();
			buttonPane.setBackground(Color.decode("#4C566A"));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.setFont(new Font("Roboto", Font.PLAIN, 11));
				okButton.setFocusPainted(false);
				okButton.setBorderPainted(false);
				okButton.setBackground(Color.decode("#EBCB8B"));
				okButton.setForeground(Color.decode("#2E3440"));
				okButton.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent clickOK) {
						setVisible(false);
						c.backToBackgroundFrame(utility); /* Al click del tasto ok, viene richiamato un metodo che rende e 
														  enabled il frame sottostante */
					}
				});
				okButton.setActionCommand("OK");
				getRootPane().setDefaultButton(okButton);
			}
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			buttonPane.add(okButton);
		}
		
		//pack();
		setLocationRelativeTo(null);
	}
}
