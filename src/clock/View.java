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
        JButton button = new JButton("Set Alarm");
        pane.add(button, BorderLayout.PAGE_END);

        //Add a panel for displaying next alarm at top of window
        alarmPanel.setLayout(new GridLayout(5,1));
        pane.add(alarmPanel, BorderLayout.PAGE_START);
        
        //Add a control panel to the left side of the screen for Save/Load buttons
        JButton loadButton = new JButton("Load Alarms");
        JButton saveButton = new JButton("Save Alarms");
        JPanel loadSavePanel = new JPanel();
        loadSavePanel.setLayout(new GridLayout(2,1));
        pane.add(loadSavePanel, BorderLayout.LINE_START);
        loadSavePanel.add(saveButton);
        loadSavePanel.add(loadButton);
        
        //Add actions to the buttons in the GUI interface
        button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                try{
                   openAlarmDialog();
                }catch(Exception ex){
                    JOptionPane.showMessageDialog(null, "Error adding alarm");
                }
            }  
        });
        
        saveButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                try{
                    if(alarmController.saveAlarms()){
                        JOptionPane.showMessageDialog(null, "Alarms saved successfully");
                    }
                }catch(Exception ex){
                    JOptionPane.showMessageDialog(null, "Failed to save alarms");
                }
                
            }  
        });
        
        loadButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){ 
                try{
                    if(alarmController.loadAlarms()){
                        JOptionPane.showMessageDialog(null, "Alarms loaded successfully");
                    }
                }catch(Exception ex)
                {
                    JOptionPane.showMessageDialog(null, "Failed to load alarms");
                }
            }  
        });
        
        frame.pack();
        frame.setVisible(true);
    }
    
    //This function will add or change the panel to the top to the next alarm
    public void addAlarm() throws Exception
    {
        alarmPanel.removeAll();
        AlarmModel alarm = alarmController.getNextAlarm();
        if(!"".equals(alarm.getString()))
        { 
            alarmPanel.add(new JButton(alarm.getString()));
            alarmPanel.revalidate();
            alarmPanel.repaint();
        }
       
        alarmPanel.revalidate();
        alarmPanel.repaint();
    }
    
    @Override
    public void update(Observable o, Object arg) {
        panel.repaint();
        try{
            addAlarm();
            if(alarmController.checkAlarm())
            {
                JOptionPane.showMessageDialog(null, "Alarm Triggered");
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    //This shows a GUI interface for creating a new alarm object
    public void openAlarmDialog() throws Exception
    {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5,1));
        
        JLabel timeLabel = new JLabel();
        timeLabel.setText("Enter Time as hh:mm:ss");
        
        JLabel dateLabel = new JLabel();
        dateLabel.setText("Enter Date as DD/MM/YYYY");
        
        
        MaskFormatter timeMask = null;
        MaskFormatter dateMask = null;
        
        try {
            timeMask = new MaskFormatter("##:##:##");//the # is for numeric values
            timeMask.setPlaceholderCharacter('#');
        } catch (ParseException e) {
        }
        
        try {
            dateMask = new MaskFormatter("##/##/####");//the # is for numeric values
            dateMask.setPlaceholderCharacter('#');
        } catch (ParseException e) {
        }
        
        final JFormattedTextField timeFormat = new JFormattedTextField(timeMask);
        final JFormattedTextField dateFormat = new JFormattedTextField(dateMask);

        
        panel.add(timeLabel);
        panel.add(timeFormat);
        panel.add(dateLabel);
        panel.add(dateFormat);
        int dialogBox = JOptionPane.showConfirmDialog(null, panel, "Create Alarm", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (dialogBox == JOptionPane.OK_OPTION) {
            try{
                String combinedString = dateFormat.getText() + " " + timeFormat.getText();
                Date date = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse(combinedString);  
                alarmController.addAlarm(date);
                
            }catch(ParseException e){
                openAlarmDialog();
            }catch(QueueOverflowException e){
                
            }
        }
    }
}
