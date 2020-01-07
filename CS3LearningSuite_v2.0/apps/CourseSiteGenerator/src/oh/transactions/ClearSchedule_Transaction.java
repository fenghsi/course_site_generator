/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oh.transactions;

import djf.AppTemplate;
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

/**
 *
 * @author fenghsiyu
 */
public class ClearSchedule_Transaction implements jTPS_Transaction {
    
     CourseSiteGeneratorData data ;
     String type;
     String title;
     String topic;
     String link;
   
     String type_old;
     String title_old;
     String topic_old;
     String link_old;
     
     
    
    
    public  ClearSchedule_Transaction( CourseSiteGeneratorApp app ) {
        data=(CourseSiteGeneratorData) app.getDataComponent();
        AppGUIModule gui = app.getGUIModule();
        DatePicker ok=(DatePicker)gui.getGUINode(SCHEDULE_CALENDAR_STARTMONDAY_COMBO);
        title_old=data.getTitle();
        topic_old=data.getTopic();
        link_old=data.getLink();
        type_old=data.getType();
        type="holidays";
        title="";
        topic="";
        link="";
        
    }


    @Override
    public void doTransaction() {
        
         data.setScheduleItemsText_clear(title, topic, link, type);
        
    }

    @Override
    public void undoTransaction() {
       
       
       data.setScheduleItemsText_clear(title_old, topic_old, link_old, type_old);
        
    }
}