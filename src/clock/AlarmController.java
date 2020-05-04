/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clock;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import javax.swing.JOptionPane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
/**
 *
 * @author mralimac
 */
public class AlarmController {
    
    PriorityQueue<AlarmModel> queue;
    AlarmView alarmView = new AlarmView();
    
    public AlarmController(){
        queue = new SortedArrayPriorityQueue<>(5);
    }
    
    //This shows a GUI interface for creating a new alarm object
    public void openAlarmDialog()
    {
        alarmView.show();
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
                JOptionPane.showMessageDialog(null, "Too many alarms in json file. Maximum of " + queue.getSize() + " allowed");
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "alarms.json not found");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading file");
        } catch (org.json.simple.parser.ParseException e) {
            JOptionPane.showMessageDialog(null, "JSON Malformed");
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
