/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clock;

import java.awt.GridLayout;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author mralimac
 */
public class AlarmView 
{
    AlarmController controller = new AlarmController();
    
    public void show(){
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
                controller.addAlarm(date);
                JOptionPane.showMessageDialog(null, "Alarm added");
            }catch(ParseException e){
                JOptionPane.showMessageDialog(null, "Unable to convert date");
                show();
            }
        }
    }
}
