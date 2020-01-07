/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oh.transactions;

import djf.modules.AppGUIModule;
import java.time.LocalDate;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import jtps.jTPS_Transaction;
import oh.CourseSiteGeneratorApp;
import static oh.CourseSiteGeneratorPropertyType.SCHEDULE_AE_DATE_COMBO;
import static oh.CourseSiteGeneratorPropertyType.SCHEDULE_AE_LINK_TEXTFIELD;
import static oh.CourseSiteGeneratorPropertyType.SCHEDULE_AE_TITLE_TEXTFIELD;
import static oh.CourseSiteGeneratorPropertyType.SCHEDULE_AE_TOPIC_TEXTFIELD;
import static oh.CourseSiteGeneratorPropertyType.SCHEDULE_AE_TYPE_COMBO;
import static oh.CourseSiteGeneratorPropertyType.SCHEDULE_CALENDAR_STARTMONDAY_COMBO;
import oh.data.CourseSiteGeneratorData;
import oh.data.ScheduleItem;

/**
 *
 * @author fenghsiyu
 */
public class AddSI_Transaction implements jTPS_Transaction {
    
     CourseSiteGeneratorData data ;
     ScheduleItem item;
     
    
    
    public  AddSI_Transaction( CourseSiteGeneratorApp app ) {
        data=(CourseSiteGeneratorData) app.getDataComponent();
        AppGUIModule gui = app.getGUIModule();
        TextField title=(TextField)gui.getGUINode(SCHEDULE_AE_TITLE_TEXTFIELD);
        TextField topic=(TextField)gui.getGUINode(SCHEDULE_AE_TOPIC_TEXTFIELD);
        TextField link=(TextField)gui.getGUINode( SCHEDULE_AE_LINK_TEXTFIELD);
        DatePicker date=(DatePicker)gui.getGUINode(SCHEDULE_AE_DATE_COMBO);
        ComboBox type=(ComboBox)gui.getGUINode(SCHEDULE_AE_TYPE_COMBO);
        DatePicker ok=(DatePicker)gui.getGUINode(SCHEDULE_CALENDAR_STARTMONDAY_COMBO);
        
        item=new ScheduleItem(type.getValue().toString(),date.getValue().toString(),title.getText(),topic.getText(),link.getText(),date.getValue());
        
    }


    @Override
    public void doTransaction() {
        data.AddSItable(item);
        
    }

    @Override
    public void undoTransaction() {
       data.AddSItable(item);
        
    }
}