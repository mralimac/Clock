/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clock;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
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
        dateLabel.setText("Enter Date as DD/MM/YY");
        
        
        MaskFormatter timeMask = null;
        MaskFormatter dateMask = null;
        
        try {
            timeMask = new MaskFormatter("##:##:##");//the # is for numeric values
            timeMask.setPlaceholderCharacter('#');
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        
        try {
            dateMask = new MaskFormatter("##/##/##");//the # is for numeric values
            dateMask.setPlaceholderCharacter('#');
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        
        final JFormattedTextField timeFormat = new JFormattedTextField(timeMask);
        final JFormattedTextField dateFormat = new JFormattedTextField(dateMask);
       
        timeFormat.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
              if(isDateTimeValid(timeFormat.getText(), dateFormat.getText())){
                  System.out.println("Hi");
              }
            }
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
        
        
        dateFormat.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
              if(isDateTimeValid(timeFormat.getText(), dateFormat.getText())){
                  System.out.println("Hi");
              }
            }
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
        JOptionPane.showMessageDialog(null, panel);
        
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
       
        
        int hour = Integer.parseInt(timeFormatArray[0]);
        int min = Integer.parseInt(timeFormatArray[1]);
        int sec = Integer.parseInt(timeFormatArray[2]);
        
        int day = Integer.parseInt(dateFormatArray[0]);
        int month = Integer.parseInt(dateFormatArray[1]);
        int year = Integer.parseInt(dateFormatArray[2]);
        
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
        
        if(year < 1980){
            return false;
        }
        
        
        return true;
    }
    
    public void addAlarm(int hour, int min, int sec, Date date){
        AlarmModel newAlarm = new AlarmModel(hour, min, sec, date);
        String parseAbleString = date.toString()+"T"+hour+":"+min+":"+sec;
        LocalDateTime dateTime = LocalDateTime.parse(parseAbleString);
        int priority = (int) dateTime.toEpochSecond(ZoneOffset.UTC);
       
        try {
            queue.add(newAlarm, priority);
        } catch (QueueOverflowException e) {
            System.out.println("Add operation failed: " + e);
        }
    }
    
    
    public void showAlarm(){
        //System.out.println(queue.toArray());
    }
    
    public int getNumberOfAlarms(){
        return 0;
    }
    
    public JPanel getPanel(int index)
    {
        return null;
    }
    
    //Triggers the alarm that would be at the front of the queue.
    public void triggerAlarm()
    {
        JOptionPane alarmPopup = new JOptionPane();
        JFrame f = new JFrame();
        alarmPopup.showMessageDialog(f, "Alarm Triggered");
    }
    
}
