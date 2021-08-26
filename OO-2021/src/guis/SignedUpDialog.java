package guis;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controllers.Controller;

import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Color;

public class SignedUpDialog extends JDialog {
	
	private static final long serialVersionUID = 1L;
	
	// Dichiarazioni utili
	private Controller c;
	private final JPanel contentPanel = new JPanel();
	private JPanel buttonPane;
	private JTextField credentialsTextField;
	private JButton okButton;

	// Creazione dialog
	public SignedUpDialog(Controller co, String cf) {
		c = co;
		getContentPane().setBackground(Color.decode("#4C566A"));
		setUndecorated(true);
		setResizable(false);
		setBounds(100, 100, 409, 211);
		getContentPane().setLayout(new BorderLayout());
		getRootPane().setBorder(BorderFactory.createLineBorder(Color.decode("#8FBCBB"), 2));
		contentPanel.setBackground(Color.decode("#4C566A"));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{395, 0};
		gbl_contentPanel.rowHeights = new int[]{31, 69, 38, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel successLabel = new JLabel("<HTML> <p> <center> Form compilato con successo! "
											+ "<br> Credenziali per l'accesso: </center> </p> </HTML>");
			successLabel.setForeground(Color.decode("#EBCB8B"));
			successLabel.setHorizontalAlignment(SwingConstants.CENTER);
			successLabel.setVerticalAlignment(SwingConstants.TOP);
			successLabel.setFont(new Font("Roboto", Font.PLAIN, 16));
			GridBagConstraints gbc_successLabel = new GridBagConstraints();
			gbc_successLabel.fill = GridBagConstraints.BOTH;
			gbc_successLabel.insets = new Insets(0, 0, 5, 0);
			gbc_successLabel.gridx = 0;
			gbc_successLabel.gridy = 1;
			contentPanel.add(successLabel, gbc_successLabel);
		}
		
		// TextField che mostra il codice fiscale dell'utente
		credentialsTextField = new JTextField(cf);
		credentialsTextField.setForeground(Color.decode("#8FBCBB"));
		credentialsTextField.setHorizontalAlignment(SwingConstants.CENTER);
		credentialsTextField.setFont(new Font("Roboto", Font.BOLD, 17));
		credentialsTextField.setBorder(null);
		credentialsTextField.setOpaque(false);
		credentialsTextField.setEditable(false);
		GridBagConstraints gbc_credentialsTextField = new GridBagConstraints();
		gbc_credentialsTextField.fill = GridBagConstraints.BOTH;
		gbc_credentialsTextField.gridx = 0;
		gbc_credentialsTextField.gridy = 2;
		contentPanel.add(credentialsTextField, gbc_credentialsTextField);
		credentialsTextField.setColumns(10);
		{
			buttonPane = new JPanel();
			buttonPane.setBackground(Color.decode("#4C566A"));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				// Bottone per tornare al frame principale
				okButton = new JButton("OK");
				okButton.setForeground(Color.decode("#2E3440"));
				okButton.setBorderPainted(false);
				okButton.setFocusPainted(false);
				okButton.setBackground(Color.decode("#EBCB8B"));
				okButton.setFont(new Font("Roboto", Font.PLAIN, 14));
				
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent clickOk) {
						c.endSignUp();
					}
				});
				buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
		
		pack();
		setLocationRelativeTo(null);
	}
}
