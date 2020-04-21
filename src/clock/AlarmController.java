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
           int hour = model.getHour();
           java.util.Date date = null;
           int currentHour = (int)(date.getTime() % 86400000) / 3600000;
        }
        
        return 0;
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
