package clock;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.util.Observer;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class View implements Observer {
    
    ClockPanel panel;
    AlarmController alarmController = new AlarmController();
    
    JPanel alarmPanel = new JPanel();
    
    public View(Model model) {
        JFrame frame = new JFrame();
        panel = new ClockPanel(model);
        
        frame.setTitle("Java Clock");
        panel.setSize(1000,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               
        final Container pane = frame.getContentPane();
       
        panel.setPreferredSize(new Dimension(200, 200));
        pane.add(panel, BorderLayout.CENTER);
         
        JButton button = new JButton("Set Alarm");
        pane.add(button, BorderLayout.PAGE_END);
        
        alarmPanel.setLayout(new GridLayout(5,1));
        pane.add(alarmPanel, BorderLayout.LINE_END);
        
        JButton loadButton = new JButton("Load Alarms");
        JButton saveButton = new JButton("Save Alarms");
        JPanel loadSavePanel = new JPanel();
        loadSavePanel.setLayout(new GridLayout(2,1));
        pane.add(loadSavePanel, BorderLayout.LINE_START);
        loadSavePanel.add(saveButton);
        loadSavePanel.add(loadButton);
        
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
    
    public void addAlarm()
    {
        alarmPanel.removeAll();
        try {
            AlarmModel alarm = alarmController.getNextAlarm();
            
            if(alarm.getString() != "")
            { 
                //System.out.println(alarm.getString());
                
                alarmPanel.add(new JButton(alarm.getString()));
                alarmPanel.revalidate();
                alarmPanel.repaint();
            }
            
        } catch (QueueUnderflowException ex) {
            
        }
        alarmPanel.revalidate();
        alarmPanel.repaint();
        
    }
    
    public void update(Observable o, Object arg) {
        panel.repaint();
        addAlarm();
        alarmController.checkAlarm();
    }
}
