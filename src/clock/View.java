package clock;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import java.util.Observer;
import java.util.Observable;
import javax.swing.text.MaskFormatter;

public class View implements Observer {
    
    ClockPanel panel;
    AlarmController alarmController = new AlarmController();
    
    JPanel alarmPanel = new JPanel();
    
    
    //Creates the main interface

    /**
     *
     * @param model
     */
    public View(Model model) {
        
        //First of all, we create a frame that'll be the container for the GUI elements
        JFrame frame = new JFrame();
        panel = new ClockPanel(model);
        frame.setTitle("Java Clock");
        panel.setSize(1000,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final Container pane = frame.getContentPane();
       
        panel.setPreferredSize(new Dimension(200, 200));
        pane.add(panel, BorderLayout.CENTER);
         
        //Adding a button to the bottom of the window that'll let the user set an alarm
        JButton setAlarmButton = new JButton("Set Alarm");
        JButton exitButton = new JButton("Exit Program");
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1,2));
        bottomPanel.add(setAlarmButton);
        bottomPanel.add(exitButton);
        
        
        pane.add(bottomPanel, BorderLayout.PAGE_END);

        //Add a panel for displaying next alarm at top of window
        alarmPanel.setLayout(new GridLayout(5,1));
        pane.add(alarmPanel, BorderLayout.LINE_END);
        
        //Add a control panel to the left side of the screen for Save/Load buttons
        JButton loadButton = new JButton("Load Alarms");
        JButton saveButton = new JButton("Save Alarms");
        JPanel loadSavePanel = new JPanel();
        loadSavePanel.setLayout(new GridLayout(2,1));
        pane.add(loadSavePanel, BorderLayout.LINE_START);
        loadSavePanel.add(saveButton);
        loadSavePanel.add(loadButton);
        
        //Add actions to the buttons in the GUI interface
        setAlarmButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                try{
                   openAlarmDialog();
                }catch(Exception ex){
                    JOptionPane.showMessageDialog(null, "Error adding alarm");
                }
            }  
        });
        
        //This adds an event to the save button
        saveButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                try{
                    //Attempt to save alarms, display a message if successful
                    if(alarmController.saveAlarms()){
                        JOptionPane.showMessageDialog(null, "Alarms saved successfully");
                    }
                }catch(Exception ex){
                    JOptionPane.showMessageDialog(null, "Failed to save alarms");
                }
                
            }  
        });
        
        //This adds an event to the load button
        loadButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){ 
                try{
                    if(alarmController.loadAlarms()){
                        JOptionPane.showMessageDialog(null, "Alarms loaded successfully");
                    }
                }catch(Exception ex){
                    JOptionPane.showMessageDialog(null, "Failed to load alarms");
                }
            }  
        });
        
        //This adds an event to the exit button
        exitButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){ 
                //Asks the user if they want to save the current alarms that are in the PriorityQueue. Yes for save, no will exit without saving
                int exitPane = JOptionPane.showConfirmDialog(null, "Do you want to save your alarms?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
                if(exitPane == JOptionPane.OK_OPTION)
                {
                    try{
                        if(alarmController.saveAlarms()){
                            JOptionPane.showMessageDialog(null, "Alarms saved successfully");
                        }
                    }catch(Exception ex){
                        JOptionPane.showMessageDialog(null, "Failed to saved alarms");
                    }
                    System.exit(0);
                }else{
                    System.exit(0);
                }
            }  
        });
        frame.pack();
        frame.setVisible(true);
    }
    
    //This function will get the list of alarms in the queue and display them

    /**
     *
     */
    public void addAlarm()
    {
        //Clear the panel of all GUI elements
        alarmPanel.removeAll();
        alarmPanel.revalidate();
        alarmPanel.repaint();
        for(int i = 0; i < alarmController.getSize(); i++)
        {
            //Add alarm GUI item to the panel
            try{
                AlarmModel alarm = alarmController.getAlarmAtIndex(i);
                alarmPanel.add(new JLabel("<html>Next Alarm: <br><strong>" + alarm.getString()+"</strong></html>" ));
                alarmPanel.revalidate();
                alarmPanel.repaint();
            }catch(NullPointerException e){
            }
        }
    }
    
    //Updates the clock face and also checks if the alarm conditions are met

    /**
     *
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {
        panel.repaint();
        addAlarm();
        
        try{
            if(alarmController.checkAlarm())
            {
                //This pops up if the alarm conditions are met
                JOptionPane.showMessageDialog(null, "Alarm Triggered");
            }
        }catch(QueueUnderflowException e){
            
        }
    }
    
    //This shows a GUI interface for creating a new alarm object

    /**
     *
     * @throws Exception
     */
    public void openAlarmDialog() throws Exception
    {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5,1));
        
        JLabel timeLabel = new JLabel();
        timeLabel.setText("Enter Time as hh:mm:ss");
        
        JLabel dateLabel = new JLabel();
        dateLabel.setText("Enter Date as DD/MM/YYYY");
        
        //Create a formatted text input with masked inputs to allow only numbers
        MaskFormatter timeMask = new MaskFormatter("##:##:##");
        timeMask.setPlaceholderCharacter('#');

        MaskFormatter dateMask = new MaskFormatter("##/##/####");
        dateMask.setPlaceholderCharacter('#');
        
        
        final JFormattedTextField timeFormat = new JFormattedTextField(timeMask);
        final JFormattedTextField dateFormat = new JFormattedTextField(dateMask);

        
        panel.add(timeLabel);
        panel.add(timeFormat);
        panel.add(dateLabel);
        panel.add(dateFormat);
        int dialogBox = JOptionPane.showConfirmDialog(null, panel, "Create Alarm", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (dialogBox == JOptionPane.OK_OPTION) {
            try{
                //Attempt to create a date from the supplied string, if successful then go onto adding an alarm
                String combinedString = dateFormat.getText() + " " + timeFormat.getText();
                Date date = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse(combinedString);  
                alarmController.addAlarm(date);
                
            }catch(ParseException e){
                //This triggers if the inputted data cannot be an date. This throws the user back to the input screen
                JOptionPane.showMessageDialog(null, "Incorrect date format entered. Please retry");
                openAlarmDialog();
            }catch(QueueOverflowException e){
                //This triggers if too many alarms have been added
                JOptionPane.showMessageDialog(null, "Cannot add anymore alarms. Queue is full");
            }
        }
    }
}
