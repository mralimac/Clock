/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clock;

import java.awt.GridLayout;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.MaskFormatter;
/**
 *
 * @author mralimac
 */
public class AlarmController {
    
    PriorityQueue<AlarmModel> queue;
    
    public AlarmController(){
        queue = new SortedArrayPriorityQueue<>(5);
    }
    
    public void openAlarmDialog()
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
            e.printStackTrace();
        }
        
        
        try {
            dateMask = new MaskFormatter("##/##/####");//the # is for numeric values
            dateMask.setPlaceholderCharacter('#');
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        
        final JFormattedTextField timeFormat = new JFormattedTextField(timeMask);
        final JFormattedTextField dateFormat = new JFormattedTextField(dateMask);
       
        timeFormat.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e){}
            public void removeUpdate(DocumentEvent e) {}
            
            
            public void insertUpdate(DocumentEvent e) {
              System.out.println("Button Pressed");
              if(isDateTimeValid(timeFormat.getText(), dateFormat.getText())){
                  System.out.println("Hi");
              }
            }
        });
        
        
        dateFormat.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {}
            public void removeUpdate(DocumentEvent e) {
              if(isDateTimeValid(timeFormat.getText(), dateFormat.getText())){
                System.out.println("Hi");
              }
            }
            
            
            public void insertUpdate(DocumentEvent e) {
              if(isDateTimeValid(timeFormat.getText(), dateFormat.getText())){
                System.out.println("Hi");
              }
            }
        });
        
        panel.add(timeLabel);
        panel.add(timeFormat);
        panel.add(dateLabel);
        panel.add(dateFormat);
        int dialogBox = JOptionPane.showConfirmDialog(null, panel, "Create Alarm", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (dialogBox == JOptionPane.OK_OPTION) {
            
            if(!isDateTimeValid(timeFormat.getText(), dateFormat.getText())){
                errorDialog("Incorrect time format");
            }else{
                try{
                    String combinedString = dateFormat.getText() + " " + timeFormat.getText();
                    Date date = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse(combinedString);  
                    addAlarm(date);
                }catch(ParseException e){
                    errorDialog("Unable to convert date");
                }
            }
        }
    }
    
    public void errorDialog(String message){
        JOptionPane.showMessageDialog(null, message);
    }
    
    public boolean isDateTimeValid(String timeFormat, String dateFormat){
        
        if(timeFormat.contains("#")){
            return false;
        }
        
        if(dateFormat.contains("#")){
            return false;
        }
        
        String[] timeFormatArray = timeFormat.split(":");
        String[] dateFormatArray = dateFormat.split("/");
        int hour;
        int min;
        int sec;
        
        int day;
        int month;
        int year;
        
        try{
            hour = Integer.parseInt(timeFormatArray[0]);
            min = Integer.parseInt(timeFormatArray[1]);
            sec = Integer.parseInt(timeFormatArray[2]);

            day = Integer.parseInt(dateFormatArray[0]);
            month = Integer.parseInt(dateFormatArray[1]);
            year = Integer.parseInt(dateFormatArray[2]);
        }catch(NumberFormatException e){
            return false;
        }
        
        if(hour > 23){
            return false;
        }
        
        if(min > 60){
            return false;
        }
        
        if(sec > 60){
            return false;
        }
        
        if(day > 31){
            return false;
        }
        
        if(month > 12){
            return false;
        }
        
        return true;
    }
    
    public void addAlarm(Date date){
        
        AlarmModel newAlarm = new AlarmModel(date);
        System.out.println(date);
        int priority = (int) date.getTime();
       
        try {
            queue.add(newAlarm, priority);
            errorDialog("Things");
        } catch (QueueOverflowException e) {
            System.out.println("Add operation failed: " + e);
        }
    }
    
    public void checkAlarm() 
    {
        try {
            AlarmModel nextAlarm = queue.head();
            System.out.println(nextAlarm.getDate().getTime());
            System.out.println(Instant.now().toEpochMilli());
            long now = Instant.now().toEpochMilli();
            if(nextAlarm.getDate().getTime() < now)
            {
                JOptionPane alarmPopup = new JOptionPane();
                JFrame f = new JFrame();
                alarmPopup.showMessageDialog(f, "Alarm Triggered");
                queue.remove();
            }
            
        } catch (QueueUnderflowException ex) {
           
        }
    }
}
