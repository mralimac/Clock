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
        
        //frame.setContentPane(panel);
        frame.setTitle("Java Clock");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               
        final Container pane = frame.getContentPane();
       
        panel.setPreferredSize(new Dimension(200, 200));
        pane.add(panel, BorderLayout.CENTER);
         
        JButton button = new JButton("Set Alarm");
        pane.add(button, BorderLayout.PAGE_END);
        
        alarmPanel.setLayout(new GridLayout(5,1));
        pane.add(alarmPanel, BorderLayout.LINE_END);
        
        
        button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){  
                alarmController.openAlarmDialog();
                
            }  
        });
        
        frame.pack();
        frame.setVisible(true);
    }
    
    public void addAlarm()
    {
        
        try {
            AlarmModel alarm = alarmController.getNextAlarm();
            
            if(alarm.getString() != "")
            { 
                //System.out.println(alarm.getString());
                alarmPanel.add(new JButton(alarm.getString()));
            }
            
        } catch (QueueUnderflowException ex) {
            
        }
        
        
    }
    
    public void update(Observable o, Object arg) {
        panel.repaint();
        addAlarm();
        alarmController.checkAlarm();
    }
}
