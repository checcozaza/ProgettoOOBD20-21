package guis;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controllers.Controller;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import com.github.lgooddatepicker.components.CalendarPanel;
import com.github.lgooddatepicker.components.DatePickerSettings.DateArea;
import com.github.lgooddatepicker.components.TimePicker;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;
import java.sql.Date;
import java.sql.Time;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.FlowLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
public class NewMeetingFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	// Dichiarazioni utili
	private Controller c;
	private JPanel contentPane;
	private JPanel formsPanel;
	private JPanel titlePanel;
	private JPanel bottomPanel;
	private CalendarPanel calendarPanel;
	private TimePicker startTimePicker;
	private TimePicker endTimePicker;
	private JTextField meetingPlaceTextField;
	private JTextField onlineMeetingTextField;
	private JRadioButton onlineMeetingRadioButton;
	private JRadioButton physicalMeetingRadioButton;
	private JButton logoutButton;
	private JButton confirmButton;
	private JButton goBackButton;

	// Creazione frame
	public NewMeetingFrame(Controller co, int projectNumber, String manager) {
		c = co;
		JFrame utility = this;
		setResizable(false);
		setTitle("Pianifica meeting - Projesting");
		setIconImage(Toolkit.getDefaultToolkit().getImage(NewMeetingFrame.class.getResource("/bulb.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 819, 555);
		contentPane = new JPanel();
		contentPane.setBackground(Color.decode("#4C566A"));
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		// Panel contenente componenti utili alla creazione di un nuovo meeting
		formsPanel = new JPanel();
		formsPanel.setBackground(Color.decode("#4C566A"));
		contentPane.add(formsPanel);
		
		// Label informativa per la scelta del giorno
		JLabel chooseDayLabel = new JLabel("Seleziona una data");
		chooseDayLabel.setForeground(Color.decode("#EBCB8B"));
		chooseDayLabel.setFont(new Font("Roboto", Font.PLAIN, 16));
		
		// Panel con calendario per la scelta del giorno
		calendarPanel = new CalendarPanel();
		calendarPanel.getNextYearButton().setBorderPainted(false);
		calendarPanel.getNextMonthButton().setBorderPainted(false);
		calendarPanel.getPreviousMonthButton().setBorderPainted(false);
		calendarPanel.getPreviousYearButton().setBorderPainted(false);
		
		// Estetica calendario
        calendarPanel.getSettings().setColor(DateArea.CalendarBackgroundNormalDates, Color.decode("#81A1C1"));
        calendarPanel.getSettings().setColor(DateArea.BackgroundOverallCalendarPanel, Color.decode("#4C566A"));
        calendarPanel.getSettings().setColor(DateArea.BackgroundMonthAndYearMenuLabels, Color.decode("#4C566A"));
        calendarPanel.getSettings().setColor(DateArea.BackgroundTodayLabel, Color.decode("#4C566A"));
        calendarPanel.getSettings().setColor(DateArea.BackgroundClearLabel, Color.decode("#4C566A"));
        calendarPanel.getSettings().setColor(DateArea.BackgroundMonthAndYearNavigationButtons, Color.decode("#4C566A"));
        calendarPanel.getSettings().setColor(DateArea.CalendarBackgroundSelectedDate, Color.decode("#EBCB8B"));
        calendarPanel.getSettings().setColor(DateArea.CalendarBorderSelectedDate, Color.WHITE);
        calendarPanel.getSettings().setColorBackgroundWeekdayLabels(Color.decode("#EBCB8B"), true);
        calendarPanel.getSettings().setColor(DateArea.TextMonthAndYearMenuLabels, Color.decode("#E5E9F0"));
        calendarPanel.getSettings().setColor(DateArea.TextMonthAndYearNavigationButtons, Color.decode("#E5E9F0"));
        calendarPanel.getSettings().setColor(DateArea.TextTodayLabel, Color.decode("#E5E9F0"));
        calendarPanel.getSettings().setColor(DateArea.TextClearLabel, Color.decode("#E5E9F0"));
        calendarPanel.getSettings().setColor(DateArea.CalendarTextNormalDates, Color.decode("#3B4252"));
        calendarPanel.getSettings().setColor(DateArea.CalendarTextWeekdays, Color.decode("#3B4252"));
        calendarPanel.getSettings().setColor(DateArea.BackgroundCalendarPanelLabelsOnHover, Color.decode("#81A1C1"));
        calendarPanel.getSettings().setColor(DateArea.TextCalendarPanelLabelsOnHover, Color.decode("#E5E9F0"));
        
        // Estetica font calendario
        Font robotoFont = new Font("Roboto", Font.PLAIN, 14);
        calendarPanel.getSettings().setFontMonthAndYearMenuLabels(robotoFont);
        calendarPanel.getSettings().setFontMonthAndYearNavigationButtons(robotoFont);
        calendarPanel.getSettings().setFontTodayLabel(robotoFont);
        calendarPanel.getSettings().setFontClearLabel(robotoFont);
        calendarPanel.getSettings().setFontCalendarDateLabels(robotoFont);
        calendarPanel.getSettings().setFontCalendarWeekdayLabels(robotoFont);
        calendarPanel.getSettings().setFontCalendarWeekNumberLabels(robotoFont);
        
		// Label informativa per la scelta dell'ora di inizio del meeting
		JLabel chooseStartTimeLabel = new JLabel("Seleziona un'ora di inizio");
		chooseStartTimeLabel.setForeground(Color.decode("#EBCB8B"));
		chooseStartTimeLabel.setFont(new Font("Roboto", Font.PLAIN, 16));
		
		// Label informativa per la scelta dell'ora di fine del meeting
		JLabel chooseEndTimeLabel = new JLabel("Seleziona un'ora di fine");
		chooseEndTimeLabel.setForeground(Color.decode("#EBCB8B"));
		chooseEndTimeLabel.setFont(new Font("Roboto", Font.PLAIN, 16));
		
		// TimePicker per selezionare l'orario di inizio del meeting
		startTimePicker = new TimePicker();
		startTimePicker.getComponentTimeTextField().setForeground(Color.decode("#5E81AC"));
		startTimePicker.getComponentTimeTextField().setFont(new Font("Roboto", Font.PLAIN, 12));
		
		// TimePicker per selezionare l'orario di fine del meeting
		endTimePicker = new TimePicker();
		endTimePicker.getComponentTimeTextField().setForeground(Color.decode("#5E81AC"));
		endTimePicker.getComponentTimeTextField().setFont(new Font("Roboto", Font.PLAIN, 12));
		
		// ButtonGroup per utilizzare i radiobutton
		ButtonGroup G = new ButtonGroup();
		
		// Radiobutton da selezionare nel caso di un meeting online
		onlineMeetingRadioButton = new JRadioButton("Su piattaforma telematica");
		onlineMeetingRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent clickOnlineMeeting) {
				onlineMeetingTextField.setEnabled(true);
				meetingPlaceTextField.setEnabled(false);
			}
		});
		onlineMeetingRadioButton.setForeground(Color.decode("#ECEFF4"));
		onlineMeetingRadioButton.setFont(new Font("Roboto", Font.PLAIN, 14));
		onlineMeetingRadioButton.setBackground(Color.decode("#4C566A"));
		G.add(onlineMeetingRadioButton);
		
		// Radiobutton da selezionare nel caso di un meeting fisico
		physicalMeetingRadioButton = new JRadioButton("In un luogo fisico");
		physicalMeetingRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent clickPhysicalMeeting) {
				meetingPlaceTextField.setEnabled(true);
				onlineMeetingTextField.setEnabled(false);
			}
		});
		physicalMeetingRadioButton.setForeground(Color.decode("#ECEFF4"));
		physicalMeetingRadioButton.setFont(new Font("Roboto", Font.PLAIN, 14));
		physicalMeetingRadioButton.setBackground(Color.decode("#4C566A"));
		G.add(physicalMeetingRadioButton);
		
		// Label informativa per la scelta della tipologia del meeting
		JLabel chooseWhereLabel = new JLabel("Seleziona una tipologia per il meeting");
		chooseWhereLabel.setForeground(Color.decode("#EBCB8B"));
		chooseWhereLabel.setFont(new Font("Roboto", Font.PLAIN, 16));
		chooseWhereLabel.setHorizontalAlignment(SwingConstants.LEFT);
		
		// Textfield dove scrivere la location del meeting fisico
		meetingPlaceTextField = new JTextField();
		meetingPlaceTextField.setForeground(Color.decode("#5E81AC"));
		meetingPlaceTextField.setFont(new Font("Roboto", Font.PLAIN, 12));
		meetingPlaceTextField.setColumns(10);
		
		// Label informativa
		JLabel meetingLocationLabel = new JLabel("Luogo meeting");
		meetingLocationLabel.setForeground(Color.decode("#EBCB8B"));
		meetingLocationLabel.setHorizontalAlignment(SwingConstants.LEFT);
		meetingLocationLabel.setFont(new Font("Roboto", Font.PLAIN, 16));
		
		// Panel contenente il titolo del frame
		titlePanel = new JPanel();
		titlePanel.setBackground(Color.decode("#434C5E"));
		titlePanel.setBounds(0, 0, 803, 63);
		contentPane.add(titlePanel, BorderLayout.NORTH);
		titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		// Label informativa
		JLabel titleLabel = new JLabel("Pianifica un meeting");
		titleLabel.setIconTextGap(18);
		titleLabel.setIcon(new ImageIcon(NewMeetingFrame.class.getResource("/online-meeting.png")));
		titlePanel.add(titleLabel);
		titleLabel.setForeground(Color.decode("#EBCB8B"));
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setFont(new Font("Roboto", Font.PLAIN, 28));
		
		// Label informativa
		JLabel onlineMeetingLabel = new JLabel("Piattaforma meeting");
		onlineMeetingLabel.setForeground(Color.decode("#EBCB8B"));
		onlineMeetingLabel.setFont(new Font("Roboto", Font.PLAIN, 16));
		
		// Textfield dove scrivere la piattaforma del meeting online
		onlineMeetingTextField = new JTextField();
		onlineMeetingTextField.setForeground(Color.decode("#5E81AC"));
		onlineMeetingTextField.setFont(new Font("Roboto", Font.PLAIN, 12));
		onlineMeetingTextField.setColumns(10);
		
		// Layout utilizzato
		GroupLayout gl_formsPanel = new GroupLayout(formsPanel);
		gl_formsPanel.setHorizontalGroup(
			gl_formsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_formsPanel.createSequentialGroup()
					.addGap(54)
					.addGroup(gl_formsPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_formsPanel.createSequentialGroup()
							.addComponent(chooseDayLabel, GroupLayout.PREFERRED_SIZE, 147, GroupLayout.PREFERRED_SIZE)
							.addGap(160)
							.addComponent(chooseStartTimeLabel, GroupLayout.PREFERRED_SIZE, 181, GroupLayout.PREFERRED_SIZE)
							.addGap(49)
							.addComponent(chooseEndTimeLabel, GroupLayout.PREFERRED_SIZE, 174, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_formsPanel.createSequentialGroup()
							.addComponent(calendarPanel, GroupLayout.PREFERRED_SIZE, 262, GroupLayout.PREFERRED_SIZE)
							.addGap(45)
							.addGroup(gl_formsPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_formsPanel.createSequentialGroup()
									.addComponent(startTimePicker, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addGap(154)
									.addComponent(endTimePicker, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addComponent(chooseWhereLabel, GroupLayout.PREFERRED_SIZE, 404, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_formsPanel.createSequentialGroup()
									.addComponent(onlineMeetingRadioButton, GroupLayout.PREFERRED_SIZE, 198, GroupLayout.PREFERRED_SIZE)
									.addGap(32)
									.addComponent(physicalMeetingRadioButton, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_formsPanel.createSequentialGroup()
									.addGroup(gl_formsPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(onlineMeetingTextField, GroupLayout.PREFERRED_SIZE, 152, GroupLayout.PREFERRED_SIZE)
										.addComponent(onlineMeetingLabel, GroupLayout.PREFERRED_SIZE, 152, GroupLayout.PREFERRED_SIZE))
									.addGap(78)
									.addGroup(gl_formsPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(meetingLocationLabel, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE)
										.addComponent(meetingPlaceTextField, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE))
									.addGap(42))))))
		);
		gl_formsPanel.setVerticalGroup(
			gl_formsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_formsPanel.createSequentialGroup()
					.addGap(22)
					.addGroup(gl_formsPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(chooseDayLabel, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_formsPanel.createSequentialGroup()
							.addGap(4)
							.addComponent(chooseStartTimeLabel, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_formsPanel.createSequentialGroup()
							.addGap(5)
							.addComponent(chooseEndTimeLabel, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)))
					.addGap(11)
					.addGroup(gl_formsPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(calendarPanel, GroupLayout.PREFERRED_SIZE, 282, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_formsPanel.createSequentialGroup()
							.addGroup(gl_formsPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(startTimePicker, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(endTimePicker, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(57)
							.addComponent(chooseWhereLabel, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
							.addGap(16)
							.addGroup(gl_formsPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(onlineMeetingRadioButton, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
								.addComponent(physicalMeetingRadioButton, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
							.addGroup(gl_formsPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_formsPanel.createSequentialGroup()
									.addGap(58)
									.addComponent(onlineMeetingLabel, GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_formsPanel.createSequentialGroup()
									.addGap(55)
									.addComponent(meetingLocationLabel, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)))
							.addGap(8)
							.addGroup(gl_formsPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(onlineMeetingTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(meetingPlaceTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))))
		);
		formsPanel.setLayout(gl_formsPanel);
		
		// Panel di fondo del frame con icone e bottoni
		bottomPanel = new JPanel();
		bottomPanel.setBackground(Color.decode("#4C566A"));
		bottomPanel.setBounds(0, 414, 803, 57);
		contentPane.add(bottomPanel, BorderLayout.SOUTH);
		GridBagLayout gbl_bottomPanel = new GridBagLayout();
		gbl_bottomPanel.columnWidths = new int[]{0, 204, 0, 0, 0, 201, 70, 0};
		gbl_bottomPanel.rowHeights = new int[]{70, 0};
		gbl_bottomPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_bottomPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		bottomPanel.setLayout(gbl_bottomPanel);
		
		// Bottone di logout
		logoutButton = new JButton("");
		GridBagConstraints gbc_logoutButton = new GridBagConstraints();
		gbc_logoutButton.insets = new Insets(0, 0, 0, 5);
		gbc_logoutButton.gridx = 0;
		gbc_logoutButton.gridy = 0;
		logoutButton.setIcon(new ImageIcon(NewMeetingFrame.class.getResource("/logout.png")));
		logoutButton.setToolTipText("Logout");
		logoutButton.setForeground(new Color(46, 52, 64));
		logoutButton.setFont(new Font("Roboto", Font.PLAIN, 12));
		logoutButton.setFocusPainted(false);
		logoutButton.setContentAreaFilled(false);
		logoutButton.setBorderPainted(false);
		logoutButton.setBackground(new Color(235, 203, 139));
		bottomPanel.add(logoutButton, gbc_logoutButton);
		
		logoutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent clickLogout) {
				c.backToLogin(utility);
			}
		});
		
		// Bottone per tornare indietro
		goBackButton = new JButton("Indietro");
		goBackButton.setFont(new Font("Roboto", Font.PLAIN, 16));
		goBackButton.setFocusPainted(false);
		goBackButton.setBorderPainted(false);
		goBackButton.setBackground(new Color(235, 203, 139));
		goBackButton.setForeground(Color.decode("#2E3440"));
		GridBagConstraints gbc_goBackButton = new GridBagConstraints();
		gbc_goBackButton.insets = new Insets(0, 0, 0, 5);
		gbc_goBackButton.gridx = 3;
		gbc_goBackButton.gridy = 0;
		bottomPanel.add(goBackButton, gbc_goBackButton);
		
		goBackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent goBack) {
					c.goBack(utility);
			}
		});
		
		// Bottone per inserire il nuovo meeting
		confirmButton = new JButton("Conferma meeting");
		confirmButton.setForeground(Color.decode("#2E3440"));
		confirmButton.setBackground(Color.decode("#EBCB8B"));
		confirmButton.setFocusPainted(false);
		confirmButton.setBorderPainted(false);
		confirmButton.setFont(new Font("Roboto", Font.PLAIN, 16));
		GridBagConstraints gbc_confirmButton = new GridBagConstraints();
		gbc_confirmButton.insets = new Insets(0, 0, 0, 5);
		gbc_confirmButton.gridx = 4;
		gbc_confirmButton.gridy = 0;
		bottomPanel.add(confirmButton, gbc_confirmButton);
		
		confirmButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent clickConfirmMeeting) {
				
			String place = "";
			if (onlineMeetingRadioButton.isSelected()) { // Prende il luogo del meeting che sia online o fisico
				place = onlineMeetingTextField.getText();
			}
			else if (physicalMeetingRadioButton.isSelected()) {
				place = meetingPlaceTextField.getText();
			}	
				
			if (calendarPanel.getSelectedDate() != null && 
				startTimePicker.getTimeStringOrEmptyString() != null &&
				endTimePicker.getTimeStringOrEmptyString()	!= null && 
				(onlineMeetingRadioButton.isSelected() || physicalMeetingRadioButton.isSelected()) &&
				place != null) { // Condizioni di corretta compilazione del form
				
					try {
						int newMeeting = c.confirmMeeting(projectNumber, 
										 Date.valueOf(calendarPanel.getSelectedDate()), 
										 Time.valueOf(startTimePicker.getTime()),
										 Time.valueOf(endTimePicker.getTime()),
										 onlineMeetingRadioButton.isSelected(),
										 place); // Crea un nuovo meeting
						c.addEmployeeToMeeting(manager, newMeeting); // Inserisce il project manager nel meeting appena creato
						c.openPopupDialog(utility, "Meeting programmato con successo!");
					} catch (Exception meetingCreationError) {
						meetingCreationError.printStackTrace();
						c.openPopupDialog(utility, "Si è verificato un errore: valori selezionati non validi. Riprova.");
					}
				}
			else
				c.openPopupDialog(utility, "Si prega di compilare tutti i campi.");
			}
		});
		
		// Label icona decorativa
		JLabel iconLabel = new JLabel("");
		GridBagConstraints gbc_iconLabel = new GridBagConstraints();
		gbc_iconLabel.gridx = 6;
		gbc_iconLabel.gridy = 0;
		bottomPanel.add(iconLabel, gbc_iconLabel);
		iconLabel.setIcon(new ImageIcon(NewMeetingFrame.class.getResource("/schedule.png")));
		
		pack();
		setLocationRelativeTo(null);
	}
}
