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
import oh.data.CourseSiteGeneratorData;

/**
 *
 * @author fenghsiyu
 */
public class EditScheduleItemText_Transaction implements jTPS_Transaction {
    
     CourseSiteGeneratorData data ;
     String type;
     String title;
     String topic;
     String link;
     LocalDate date;
     String type_old;
     String title_old;
     String topic_old;
     String link_old;
     LocalDate date_old;
    
    
    public EditScheduleItemText_Transaction( CourseSiteGeneratorApp app ) {
        data=(CourseSiteGeneratorData) app.getDataComponent();
        title_old=data.getTitle();
        topic_old=data.getTopic();
        link_old=data.getLink();
        type_old=data.getType();
        date_old=data.getLocalDate();
        AppGUIModule gui = app.getGUIModule();
        TextField title1=(TextField)gui.getGUINode(SCHEDULE_AE_TITLE_TEXTFIELD);
        TextField topic1=(TextField)gui.getGUINode(SCHEDULE_AE_TOPIC_TEXTFIELD);
        TextField link1=(TextField)gui.getGUINode( SCHEDULE_AE_LINK_TEXTFIELD);
        DatePicker date1=(DatePicker)gui.getGUINode(SCHEDULE_AE_DATE_COMBO);
        ComboBox type1=(ComboBox)gui.getGUINode(SCHEDULE_AE_TYPE_COMBO);
        type=type1.getValue().toString();
        title=title1.getText();
        topic=topic1.getText();
        link=link1.getText();
        date=date1.getValue();
        
    }


    @Override
    public void doTransaction() {
         data.setScheduleItemsText(title, topic, link, type,date);
        
    }

    @Override
    public void undoTransaction() {
       
        data.setScheduleItemsText(title_old, topic_old, link_old, type_old,date_old);
        
    }
}