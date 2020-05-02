/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clock;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 *
 * @author mralimac
 */
public class AlarmModel {
    
    protected Date date;
    
    public AlarmModel(Date date){
        
        this.date = date;
    }
    
    public Date getDate()
    {
        return this.date;
    }
    
    public void setDate(Date date)
    {
        this.date = date;
    }
    
    public String getString()
    {        
        DateFormat d = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        
        return d.format(this.date);
    }
}
