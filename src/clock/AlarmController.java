/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clock;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

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
        JOptionPane setAlarmPopup = new JOptionPane();
        JFrame frame = new JFrame();
        setAlarmPopup.showMessageDialog(frame, "Set Alarm Dialog");
    }
    
    public void addAlarm(int hour, int min, int sec){
        AlarmModel newAlarm = new AlarmModel(hour, min, sec);
        int priority = this.figureOutPriority(newAlarm);
        try {
            queue.add(newAlarm, priority);
        } catch (QueueOverflowException e) {
            System.out.println("Add operation failed: " + e);
        }
    }
        
    public int figureOutPriority(AlarmModel newAlarm){
        if(queue.isEmpty())
        {
            return 0;
        }
        
        int size = queue.getSize();
        //PriorityItemist = queue.toArray();
        for(int i = 0; i < size; i++)
        {
           PriorityItem item = queue.getObject(i);
           AlarmModel model = (AlarmModel) item.getItem();
           
           if(isAfterCurrentTime(model) && isAfterCurrentTime(newAlarm))
           {
               
           }
           
           
        }
        return 0;
    }
    
    public boolean compareTwoAlarm(AlarmModel alarm1, AlarmModel alarm2)
    {
        return false;
    }
    
    
    public boolean isAfterCurrentTime(AlarmModel model){
        java.util.Date date = null;
        
        int hour = model.getHour();
        int min = model.getMin();
        int sec = model.getSec();
        
        int currentHour = (int)(date.getTime() % 86400000) / 3600000;
        int currentMin = (int)(date.getTime() % 86400000) / 60000;
        int currentSec = (int)(date.getTime() % 86400000) / 1000;
        
        if(hour > currentHour)
        {
            return true;
        }
        
        if(hour < currentHour)
        {
            return false;
        }
        
        if(hour == currentHour)
        {
            if(min > currentMin)
            {
                return true;
            }
            if(min < currentMin)
            {
                return false;
            }
            
            if(min == currentMin)
            {
                if(sec > currentSec)
                {
                    return true;
                }
                if(sec < currentSec)
                {
                    return false;
                }
                
                if(sec == currentSec)
                {
                    return true;
                }
            }
        }
        return true;
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
