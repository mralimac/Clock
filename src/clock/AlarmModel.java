/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clock;

import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author mralimac
 */
public class AlarmModel {
    
    protected int hour;
    protected int min;
    protected int sec;
    protected Date date;
    
    public AlarmModel(int hour, int min, int sec, Date date){
        this.hour = hour;
        this.min = min;
        this.sec = sec;
        this.date = date;
    }
    
    public void setHour(int hour)
    {
        this.hour = hour; 
    }
    
    public void setMin(int min)
    {
        this.min = min;
    }
    
    public void setSec(int sec)
    {
        this.sec = sec;
    }
    
    public int getHour()
    {
       return this.hour; 
    }
    
    public int getMin()
    {
        return this.min;
    }
    
    public int getSec()
    {
        return this.sec;
    }
}
