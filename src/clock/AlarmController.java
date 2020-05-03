/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clock;

import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.text.MaskFormatter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
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

        
        panel.add(timeLabel);
        panel.add(timeFormat);
        panel.add(dateLabel);
        panel.add(dateFormat);
        int dialogBox = JOptionPane.showConfirmDialog(null, panel, "Create Alarm", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (dialogBox == JOptionPane.OK_OPTION) {
            
            if(!isDateTimeValid(timeFormat.getText(), dateFormat.getText())){
                errorDialog("Unable to convert date");
                openAlarmDialog();
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
    
    public AlarmModel getNextAlarm() throws QueueUnderflowException
    {
        return queue.head();
    }
    
    public void saveAlarms()
    {
        int cap = queue.getSize();
        
        JSONObject object = new JSONObject();
        
        JSONArray jsonArray = new JSONArray();
        
        for(int i = 0; cap >= i; i++)
        {
            
            AlarmModel alarmModel = queue.getItemAtIndex(i);
            
            Date alarmDate = alarmModel.getDate();
            
            JSONObject alarmObject = new JSONObject();
            alarmObject.put("date", alarmDate.toString());
            
            jsonArray.add(alarmObject);
            
            object.put("alarms", alarmObject);
            
            try (FileWriter file = new FileWriter("alarms.json")) {
                file.write(object.toJSONString());
                file.flush();

            } catch (IOException e) {
            }
        }
    }
    
    public void loadAlarms()
    {
        JSONParser parse = new JSONParser();
        try(FileReader read = new FileReader("alarms.json"))
        {
            JSONObject jsonObject = (JSONObject) parse.parse(read);
            JSONArray msg = (JSONArray) jsonObject.get("alarms");
            Iterator<String> iterator = msg.iterator();
            while (iterator.hasNext()) {
                System.out.println(iterator.next());
            }

        }catch(FileNotFoundException e)
        {
            
        }catch(IOException e)
        {
            
        } catch (org.json.simple.parser.ParseException ex) {
            
        }
        
        
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
