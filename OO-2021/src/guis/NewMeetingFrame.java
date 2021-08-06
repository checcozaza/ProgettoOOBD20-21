package guis;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controllers.Controller;
import java.awt.GridLayout;
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
public class NewMeetingFrame extends JFrame {

	private JPanel contentPane;
	private Controller c;
	private JTextField meetingPlaceTextField;
	private JTextField onlineMeetingTextField;

	/**
	 * Create the frame.
	 */
	public NewMeetingFrame(Controller co, int projectNumber) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(NewMeetingFrame.class.getResource("/bulb.png")));
		setTitle("Pianifica meeting - Projesting");
		c = co;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 819, 531);
		contentPane = new JPanel();
		contentPane.setBackground(Color.decode("#4C566A"));
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(1, 0, 0, 0));
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.decode("#4C566A"));
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel chooseDayLabel = new JLabel("Seleziona una data");
		chooseDayLabel.setForeground(Color.decode("#EBCB8B"));
		chooseDayLabel.setFont(new Font("Roboto", Font.PLAIN, 16));
		chooseDayLabel.setBounds(54, 70, 147, 25);
		panel.add(chooseDayLabel);
		
		CalendarPanel calendarPanel = new CalendarPanel();
		calendarPanel.getNextYearButton().setBorderPainted(false);
		calendarPanel.getNextMonthButton().setBorderPainted(false);
		calendarPanel.getPreviousMonthButton().setBorderPainted(false);
		calendarPanel.getPreviousYearButton().setBorderPainted(false);
		calendarPanel.setBounds(54, 106, 262, 282);
		
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
        
        Font robotoFont = new Font("Roboto", Font.PLAIN, 14);
        calendarPanel.getSettings().setFontMonthAndYearMenuLabels(robotoFont);
        calendarPanel.getSettings().setFontMonthAndYearNavigationButtons(robotoFont);
        calendarPanel.getSettings().setFontTodayLabel(robotoFont);
        calendarPanel.getSettings().setFontClearLabel(robotoFont);
        calendarPanel.getSettings().setFontCalendarDateLabels(robotoFont);
        calendarPanel.getSettings().setFontCalendarWeekdayLabels(robotoFont);
        calendarPanel.getSettings().setFontCalendarWeekNumberLabels(robotoFont);
        
        calendarPanel.getSettings().setColor(DateArea.TextMonthAndYearMenuLabels, Color.decode("#E5E9F0"));
        calendarPanel.getSettings().setColor(DateArea.TextMonthAndYearNavigationButtons, Color.decode("#E5E9F0"));
        calendarPanel.getSettings().setColor(DateArea.TextTodayLabel, Color.decode("#E5E9F0"));
        calendarPanel.getSettings().setColor(DateArea.TextClearLabel, Color.decode("#E5E9F0"));
        calendarPanel.getSettings().setColor(DateArea.CalendarTextNormalDates, Color.decode("#3B4252"));
        calendarPanel.getSettings().setColor(DateArea.CalendarTextWeekdays, Color.decode("#3B4252"));
        calendarPanel.getSettings().setColor(DateArea.BackgroundCalendarPanelLabelsOnHover, Color.decode("#81A1C1"));
        calendarPanel.getSettings().setColor(DateArea.TextCalendarPanelLabelsOnHover, Color.decode("#E5E9F0"));
		
		panel.add(calendarPanel);
		
		JLabel chooseStartTimeLabel = new JLabel("Seleziona un'ora di inizio");
		chooseStartTimeLabel.setForeground(Color.decode("#EBCB8B"));
		chooseStartTimeLabel.setFont(new Font("Roboto", Font.PLAIN, 16));
		chooseStartTimeLabel.setBounds(361, 74, 181, 17);
		panel.add(chooseStartTimeLabel);
		
		JLabel chooseEndTimeLabel = new JLabel("Seleziona un'ora di fine");
		chooseEndTimeLabel.setForeground(Color.decode("#EBCB8B"));
		chooseEndTimeLabel.setFont(new Font("Roboto", Font.PLAIN, 16));
		chooseEndTimeLabel.setBounds(591, 75, 174, 14);
		panel.add(chooseEndTimeLabel);
		
		TimePicker startTimePicker = new TimePicker();
		startTimePicker.getComponentTimeTextField().setFont(new Font("Roboto", Font.PLAIN, 14));
		startTimePicker.setBounds(361, 106, 76, 23);
		panel.add(startTimePicker);
		
		TimePicker endTimePicker = new TimePicker();
		endTimePicker.getComponentTimeTextField().setFont(new Font("Roboto", Font.PLAIN, 14));
		endTimePicker.setBounds(591, 106, 76, 23);
		panel.add(endTimePicker);
		
		ButtonGroup G = new ButtonGroup();
		
		JRadioButton onlineMeetingRadioButton = new JRadioButton("Su piattaforma telematica");
		onlineMeetingRadioButton.setForeground(Color.decode("#ECEFF4"));
		onlineMeetingRadioButton.setFont(new Font("Roboto", Font.PLAIN, 14));
		onlineMeetingRadioButton.setBackground(Color.decode("#4C566A"));
		onlineMeetingRadioButton.setBounds(361, 219, 198, 23);
		panel.add(onlineMeetingRadioButton);
		G.add(onlineMeetingRadioButton);
		
		JRadioButton physicalMeetingRadioButton = new JRadioButton("In un luogo fisico");
		physicalMeetingRadioButton.setForeground(Color.decode("#ECEFF4"));
		physicalMeetingRadioButton.setFont(new Font("Roboto", Font.PLAIN, 14));
		physicalMeetingRadioButton.setBackground(Color.decode("#4C566A"));
		physicalMeetingRadioButton.setBounds(591, 219, 132, 23);
		panel.add(physicalMeetingRadioButton);
		G.add(physicalMeetingRadioButton);
		
		JLabel chooseWhereLabel = new JLabel("Seleziona una tipologia per il meeting");
		chooseWhereLabel.setForeground(Color.decode("#EBCB8B"));
		chooseWhereLabel.setFont(new Font("Roboto", Font.PLAIN, 16));
		chooseWhereLabel.setHorizontalAlignment(SwingConstants.LEFT);
		chooseWhereLabel.setBounds(361, 186, 404, 17);
		panel.add(chooseWhereLabel);
		
		meetingPlaceTextField = new JTextField();
		meetingPlaceTextField.setFont(new Font("Roboto", Font.PLAIN, 11));
		meetingPlaceTextField.setBounds(591, 330, 117, 20);
		panel.add(meetingPlaceTextField);
		meetingPlaceTextField.setColumns(10);
		
		JLabel meetingLocationLabel = new JLabel("Luogo meeting");
		meetingLocationLabel.setForeground(Color.decode("#EBCB8B"));
		meetingLocationLabel.setHorizontalAlignment(SwingConstants.LEFT);
		meetingLocationLabel.setFont(new Font("Roboto", Font.PLAIN, 16));
		meetingLocationLabel.setBounds(596, 297, 127, 25);
		panel.add(meetingLocationLabel);
		
		JLabel iconLabel = new JLabel("");
		iconLabel.setIcon(new ImageIcon(NewMeetingFrame.class.getResource("/schedule.png")));
		iconLabel.setBounds(723, 409, 70, 72);
		panel.add(iconLabel);
		
		JPanel titlePanel = new JPanel();
		titlePanel.setBackground(Color.decode("#434C5E"));
		titlePanel.setBounds(0, 0, 803, 63);
		panel.add(titlePanel);
		titlePanel.setLayout(null);
		
		JLabel titleLabel = new JLabel("Pianifica un meeting");
		titleLabel.setIconTextGap(18);
		titleLabel.setIcon(new ImageIcon(NewMeetingFrame.class.getResource("/online-meeting.png")));
		titleLabel.setBounds(0, 0, 803, 63);
		titlePanel.add(titleLabel);
		titleLabel.setForeground(Color.decode("#EBCB8B"));
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setFont(new Font("Roboto", Font.PLAIN, 28));
		
		JButton goBackButton = new JButton("Indietro");
		goBackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		goBackButton.setForeground(Color.decode("#2E3440"));
		goBackButton.setFont(new Font("Roboto", Font.PLAIN, 16));
		goBackButton.setFocusPainted(false);
		goBackButton.setBorderPainted(false);
		goBackButton.setBackground(new Color(235, 203, 139));
		goBackButton.setBounds(424, 444, 89, 24);
		panel.add(goBackButton);
		
		JButton logoutButton = new JButton("");
		logoutButton.setIcon(new ImageIcon(NewMeetingFrame.class.getResource("/logout.png")));
		logoutButton.setToolTipText("Logout");
		logoutButton.setForeground(new Color(46, 52, 64));
		logoutButton.setFont(new Font("Roboto", Font.PLAIN, 12));
		logoutButton.setFocusPainted(false);
		logoutButton.setContentAreaFilled(false);
		logoutButton.setBorderPainted(false);
		logoutButton.setBackground(new Color(235, 203, 139));
		logoutButton.setBounds(0, 417, 64, 75);
		panel.add(logoutButton);
		
		JButton confirmButton = new JButton("Conferma meeting");
		confirmButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent clickConfirmMeeting) {
				
			String place = "";
			if (onlineMeetingRadioButton.isSelected()) {
				place = onlineMeetingTextField.getText();
			}
			else if (physicalMeetingRadioButton.isSelected()) {
				place = meetingPlaceTextField.getText();
			}
			
				
				
			if (calendarPanel.getSelectedDate() != null && startTimePicker.getTimeStringOrEmptyString() != null &&
				endTimePicker.getTimeStringOrEmptyString()	!= null && 
				(onlineMeetingRadioButton.isSelected() || physicalMeetingRadioButton.isSelected()) &&
				place != null) {
				
				try {
					c.confirmMeeting(projectNumber, 
									 Date.valueOf(calendarPanel.getSelectedDate()), 
									 Time.valueOf(startTimePicker.getTime()),
									 Time.valueOf(endTimePicker.getTime()),
									 onlineMeetingRadioButton.isSelected(),
									 place);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			}
		});
		confirmButton.setForeground(Color.decode("#2E3440"));
		confirmButton.setBackground(Color.decode("#EBCB8B"));
		confirmButton.setFocusPainted(false);
		confirmButton.setBorderPainted(false);
		confirmButton.setFont(new Font("Roboto", Font.PLAIN, 16));
		confirmButton.setBounds(223, 444, 181, 24);
		panel.add(confirmButton);
		
		JLabel onlineMeetingLabel = new JLabel("Piattaforma meeting");
		onlineMeetingLabel.setForeground(Color.decode("#EBCB8B"));
		onlineMeetingLabel.setFont(new Font("Roboto", Font.PLAIN, 16));
		onlineMeetingLabel.setBounds(361, 300, 152, 18);
		panel.add(onlineMeetingLabel);
		
		onlineMeetingTextField = new JTextField();
		onlineMeetingTextField.setBounds(361, 330, 152, 20);
		panel.add(onlineMeetingTextField);
		onlineMeetingTextField.setColumns(10);
		
		setLocationRelativeTo(null);
	}
}
