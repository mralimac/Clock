/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clock;

import java.util.Calendar;
import java.util.Date;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mralimac
 */
public class AlarmControllerTest {
    
   
    /**
     * Test of getNextAlarm method, of class AlarmController.
     */
    @Test
    public void testGetNextAlarm() {
        AlarmController instance = new AlarmController();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());            
        calendar.add(Calendar.DAY_OF_YEAR, 7);
        Date date1 = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 14);
        Date date2 = calendar.getTime();
        
        try{
            instance.addAlarm(date1);
            instance.addAlarm(date2);
            AlarmModel headAlarm = instance.getNextAlarm(); 
            assertEquals(date1.getTime(), headAlarm.getDate().getTime());
        }catch(Exception e){
            fail("Error Detected");
        }
    }
    
    /**
     * Test of addAlarm method, of class AlarmController.
     */
    @Test
    public void testAddAlarm() {
        Date date = new Date();
        AlarmController instance = new AlarmController();
        try{
            instance.addAlarm(date);
            AlarmModel headAlarm = instance.getNextAlarm();
            assertEquals(headAlarm.getDate(), date);
        }catch(Exception e){
            fail("Error detected");
        }
    }
    


    /**
     * Test of checkAlarm method, of class AlarmController.
     */
    @Test
    public void testCheckAlarm() {
        AlarmController instance = new AlarmController();
        Date date = new Date();
        try{            
            instance.addAlarm(date);
            assertEquals(instance.checkAlarm(), true);
        }catch(QueueOverflowException e)
        {
            fail("Overflow Detected");
        }catch(Exception e){
            fail("Exception detected");
        }
        
        
        
    }
    
    /**
     * Test overload of alarm, of class AlarmController.
     */
    @Test(expected = QueueOverflowException.class)
    public void testOverload() throws Exception{
        AlarmController instance = new AlarmController();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());            
        calendar.add(Calendar.DAY_OF_YEAR, 7);
        Date date1 = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 14);
        Date date2 = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 27);
        Date date3 = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 36);
        Date date4 = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 45);
        Date date5 = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 54);
        Date date6 = calendar.getTime();
        
        instance.addAlarm(date1);
        instance.addAlarm(date2);
        instance.addAlarm(date3);
        instance.addAlarm(date4);
        instance.addAlarm(date5);
        instance.addAlarm(date6);
        
    }
    
    @Test
    public void testSaveAlarms(){
        AlarmController instance = new AlarmController();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());            
        calendar.add(Calendar.DAY_OF_YEAR, 7);
        Date date1 = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 14);
        Date date2 = calendar.getTime();
        
        try{
            instance.addAlarm(date1);
            instance.addAlarm(date2);
            instance.saveAlarms();
        }catch(Exception ex){
            fail("Save test failed");
        }
        
    }
    
    @Test
    public void testRemoveAlarms(){
        AlarmController instance = new AlarmController();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());            
        calendar.add(Calendar.DAY_OF_YEAR, 7);
        Date date1 = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 14);
        Date date2 = calendar.getTime();
        try{
            instance.addAlarm(date1);
            instance.addAlarm(date2);
            instance.removeAlarm();
            
            assertEquals(instance.getNextAlarm().getDate(), date2);
        }catch(Exception e){
            fail("Remove test failed");
        }
    }
    
    @Test
    public void testLoadAlarms(){
        AlarmController instance = new AlarmController();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());            
        calendar.add(Calendar.DAY_OF_YEAR, 7);
        Date date1 = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 14);
        Date date2 = calendar.getTime();
        
        try{
            instance.addAlarm(date1);
            instance.addAlarm(date2);
            instance.saveAlarms();
            instance.removeAlarm();
            instance.removeAlarm();
        }catch(Exception ex){
            fail("Failed to save to file");
        }
        try{
            instance.loadAlarms();
            assertEquals(date1, instance.getNextAlarm().getDate());   
        }catch(Exception ex){
            fail("Failed to load from file");
        }
    }
}
