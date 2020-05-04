package clock;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.util.Observer;
import java.util.Observable;

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
                alarmController.openAlarmDialog();
            }  
        });
        
        saveButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){  
                alarmController.saveAlarms();
            }  
        });
        
        loadButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){  
                alarmController.loadAlarms();
            }  
        });
        
        frame.pack();
        frame.setVisible(true);
    }
    
    //This function will add or change the panel to the top to the next alarm
    public void addAlarm()
    {
        alarmPanel.removeAll();
        try {
            AlarmModel alarm = alarmController.getNextAlarm();
            if(!"".equals(alarm.getString()))
            { 
                alarmPanel.add(new JButton(alarm.getString()));
                alarmPanel.revalidate();
                alarmPanel.repaint();
            }
        } catch (QueueUnderflowException ex) {
            
        }
        alarmPanel.revalidate();
        alarmPanel.repaint();
    }
    
    @Override
    public void update(Observable o, Object arg) {
        panel.repaint();
        addAlarm();
        alarmController.checkAlarm();
    }
}
