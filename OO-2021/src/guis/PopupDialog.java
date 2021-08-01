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

	private final JPanel contentPanel = new JPanel();
	private Controller c;
	private JButton okButton;
	private JLabel lblNewLabel;
	
	// Creazione Dialog
	public PopupDialog(Controller co, JFrame toClose, String popupMessage) {
		getContentPane().setBackground(Color.decode("#3B4252"));
		setUndecorated(true);
		setResizable(true);
		c = co;
		setBounds(100, 100, 315, 122);
		getContentPane().setLayout(new BorderLayout());
		getRootPane().setBorder(BorderFactory.createLineBorder(Color.decode("#8FBCBB"), 2));
		contentPanel.setBackground(Color.decode("#4C566A"));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			lblNewLabel = new JLabel("<HTML> <p> <center> " +popupMessage+ "</center> </p> </HTML>");
			lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
			lblNewLabel.setFont(new Font("Roboto", Font.PLAIN, 18));
			lblNewLabel.setForeground(Color.decode("#EBCB8B"));
			lblNewLabel.setBackground(Color.decode("#4C566A"));
		}
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(lblNewLabel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(lblNewLabel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(Color.decode("#4C566A"));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.setFocusPainted(false);
				okButton.setBorderPainted(false);
				okButton.setBackground(Color.decode("#EBCB8B"));
				okButton.setForeground(Color.decode("#2E3440"));
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent clickOK) {
						setVisible(false);
						c.backToBackgroundFrame(toClose); /* Al click del tasto ok, viene richiamato un metodo che rende e 
													enabled il frame sottostante */
					}
				});
				okButton.setActionCommand("OK");
				getRootPane().setDefaultButton(okButton);
			}
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			buttonPane.add(okButton);
		}
		setLocationRelativeTo(null);
	}
}
