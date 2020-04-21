package clock;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.util.Observer;
import java.util.Observable;

public class View implements Observer {
    
    ClockPanel panel;
    
    public View(Model model) {
        
        final AlarmController alarmController = new AlarmController();
        JFrame frame = new JFrame();
        panel = new ClockPanel(model);
        //frame.setContentPane(panel);
        frame.setTitle("Java Clock");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Start of border layout code
        
        // I've just put a single button in each of the border positions:
        // PAGE_START (i.e. top), PAGE_END (bottom), LINE_START (left) and
        // LINE_END (right). You can omit any of these, or replace the button
        // with something else like a label or a menu bar. Or maybe you can
        // figure out how to pack more than one thing into one of those
        // positions. This is the very simplest border layout possible, just
        // to help you get started.
        
        Container pane = frame.getContentPane();
       
        panel.setPreferredSize(new Dimension(200, 200));
        pane.add(panel, BorderLayout.CENTER);
         
        JButton button = new JButton("Set Alarm");
        pane.add(button, BorderLayout.PAGE_END);
        
        button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){  
                alarmController.showAlarm();
            }  
        });
       
        JPanel alarmPanel = new JPanel();
        alarmPanel.setLayout(new GridLayout(5,1));
        
        alarmPanel.add(new JButton("Alarm 1"));
        //alarmPanel.add(new JButton("Alarm 2"));
        //alarmPanel.add(new JButton("Alarm 3"));
        //alarmPanel.add(new JButton("Alarm 4"));
        //alarmPanel.add(new JButton("Alarm 5"));
        
        pane.add(alarmPanel, BorderLayout.LINE_END);
        
        // End of borderlayout code
        
        frame.pack();
        frame.setVisible(true);
    }
    
    public void update(Observable o, Object arg) {
        panel.repaint();
    }
}
