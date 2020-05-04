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
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.text.MaskFormatter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
/**
 *
 * @author mralimac
 */
public class AlarmController {
    
    PriorityQueue<AlarmModel> queue;
    
    public AlarmController(){
        queue = new SortedArrayPriorityQueue<>(5);
    }
    
    //This shows a GUI interface for creating a new alarm object
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
                addAlarm(date);
                messageDialog("Alarm added");
            }catch(ParseException e){
                messageDialog("Unable to convert date");
                openAlarmDialog();
            }
        }
    }
    
    //This function is for displaying a message dialog to the user
    public void messageDialog(String message){
        JOptionPane.showMessageDialog(null, message);
    }
    
    //This gets the alarm at the head of the queue
    public AlarmModel getNextAlarm() throws QueueUnderflowException
    {
        return queue.head();
    }
    
    //This takes the alarms in the priority queue and writes them to a json file
    public void saveAlarms()
    {
        JSONArray array = new JSONArray();
        int numberOfObjs = queue.getSize();
        
        for(int i = 0; i < numberOfObjs; i++)
        {
            try{
                AlarmModel alarm = queue.getItemAtIndex(i);
                JSONObject alarmJSON = new JSONObject();
                long date = alarm.getDate().getTime();
                alarmJSON.put("date", date);
                array.add(alarmJSON);
            }catch(NullPointerException e){
                //Breaking the loop as there will be no more instances in the list
                break;
            }
        }
       
        try (FileWriter file = new FileWriter("alarms.json")) {
            JOptionPane.showMessageDialog(null, "Number of Alarms successfully saved: "+ array.size()) ;
            file.write(array.toJSONString());
            file.flush();

        } catch (IOException e) {
        }
       
    }
    
    //This retrieves the file from a json file and attempts to add the alarms to the array
    public void loadAlarms()
    {
        
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("alarms.json"))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONArray listOfAlarms = (JSONArray) obj;
            if(listOfAlarms.size() <= queue.getSize())
            {
                for(int i = 0; i < listOfAlarms.size(); i++)
                {
                    JSONObject object = new JSONObject();
                    object = (JSONObject) listOfAlarms.get(i);
                    long date = (long) object.get("date");
                    Date convertedDate = new Date(date);
                    addAlarm(convertedDate);
                }
            }else{
                messageDialog("Too many alarms in json file. Maximum of " + queue.getSize() + " allowed");
            }
        } catch (FileNotFoundException e) {
            messageDialog("alarms.json not found");
        } catch (IOException e) {
            messageDialog("Error reading file");
        } catch (org.json.simple.parser.ParseException e) {
            messageDialog("JSON Malformed");
        }
    }
    
    //This function adds the alarm object to the priority queue
    public boolean addAlarm(Date date){
        AlarmModel newAlarm = new AlarmModel(date);
        System.out.println(date);
        int priority = (int) date.getTime();
       
        try {
            queue.add(newAlarm, priority);
        } catch (QueueOverflowException e) {
            System.out.println("Add operation failed: " + e);
            return false;
        }
        return true;
    }
    
    //This function checks to see if the next alarm is currently after the current time
    public void checkAlarm() 
    {
        try {
            AlarmModel nextAlarm = queue.head();
            long now = Instant.now().toEpochMilli();
            if(nextAlarm.getDate().getTime() < now)
            {
                JOptionPane.showMessageDialog(null, "Alarm Triggered");
                queue.remove();
            }
        } catch (QueueUnderflowException ex) {
           
        }
    }
}
