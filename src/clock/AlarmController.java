/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clock;

import java.io.FileReader;
import java.io.FileWriter;
import java.time.Instant;
import java.util.Date;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
/**
 *
 * @author mralimac
 */
public class AlarmController {
    
    PriorityQueue<AlarmModel> queue;
    int sizeOfQueue;
    
    /**
     *
     */
    public AlarmController(){
        this.sizeOfQueue = 5;
        queue = new SortedArrayPriorityQueue<>(sizeOfQueue);
    }
    
    //This functions gets how large the queue size is

    /**
     *
     * @return
     */
    public int getSize(){
        return queue.getSize();
    }
    
   
    //This gets the alarm at the head of the queue

    /**
     *
     * @return
     * @throws QueueUnderflowException
     */
    public AlarmModel getNextAlarm() throws QueueUnderflowException
    {
        return queue.head();
    }
    
    //This function removes the alarm from the front of the queue

    /**
     *
     * @throws QueueUnderflowException
     */
    public void removeAlarm() throws QueueUnderflowException 
    {
        queue.remove();
    }
    
    /**
     *
     * @param index
     * @return
     * @throws NullPointerException
     */
    public AlarmModel getAlarmAtIndex(int index) throws NullPointerException
    {
       return queue.getItemAtIndex(index);
    }
    
    //This takes the alarms in the priority queue and writes them to a json file

    /**
     *
     * @return
     * @throws Exception
     */
    public boolean saveAlarms() throws Exception
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
        FileWriter file = new FileWriter("alarms.json");
        file.write(array.toJSONString());
        file.flush();
        return true;
    }
    
    //This retrieves the file from a json file and attempts to add the alarms to the array

    /**
     *
     * @return
     * @throws Exception
     */
    public boolean loadAlarms() throws Exception
    { 
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader("alarms.json");
        
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
            return true;
        }else{
            return false;
        }
    }
    
    //This function adds the alarm object to the priority queue

    /**
     *
     * @param date
     * @return
     * @throws QueueOverflowException
     */
    public boolean addAlarm(Date date) throws QueueOverflowException {
        AlarmModel newAlarm = new AlarmModel(date);
        int priority = (int) date.getTime();
        queue.add(newAlarm, priority);
        return true;
    }
    
    //This function checks to see if the next alarm is currently after the current time

    /**
     *
     * @return
     * @throws QueueUnderflowException
     */
    public boolean checkAlarm() throws QueueUnderflowException
    {
        AlarmModel nextAlarm = getNextAlarm();
        long now = Instant.now().toEpochMilli();
        if(nextAlarm.getDate().getTime() < now)
        {
            removeAlarm();
            return true;
        }
        return false;
    }
}
