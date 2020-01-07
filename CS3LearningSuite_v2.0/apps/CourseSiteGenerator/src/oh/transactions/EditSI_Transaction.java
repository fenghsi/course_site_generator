/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oh.transactions;

import djf.modules.AppGUIModule;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import jtps.jTPS_Transaction;
import oh.CourseSiteGeneratorApp;
import static oh.CourseSiteGeneratorPropertyType.SCHEDULE_CALENDAR_STARTMONDAY_COMBO;
import static oh.CourseSiteGeneratorPropertyType.SCHEDULE_ITEM_TABLEVIEW;
import oh.data.CourseSiteGeneratorData;
import oh.data.ScheduleItem;

/**
 *
 * @author fenghsiyu
 */
public class EditSI_Transaction implements jTPS_Transaction {
    
     CourseSiteGeneratorData data ;
     String type;
     String title;
     String topic;
     String link;
     String date;
     LocalDate date_l;
     String type_old;
     String title_old;
     String topic_old;
     String link_old;
     String date_old;
     LocalDate date_l_old;
     ScheduleItem si;
     
    
    
    public  EditSI_Transaction( CourseSiteGeneratorApp app ,String ti,String to,String li,String da,String ty,LocalDate dl) {
        
        data=(CourseSiteGeneratorData) app.getDataComponent();
        AppGUIModule gui = app.getGUIModule();
        TableView  scheduleTable=(TableView)gui.getGUINode(SCHEDULE_ITEM_TABLEVIEW);
        DatePicker ok=(DatePicker)gui.getGUINode(SCHEDULE_CALENDAR_STARTMONDAY_COMBO);
        si=(ScheduleItem) scheduleTable.getSelectionModel().getSelectedItem();
        type_old=si.getType();
        title_old=si.getTitle();
        topic_old=si.getTopic();
        link_old=si.getLink();
        date_old=si.getDate();
        date_l_old=si.getDate_l();
         type=ty;
         title=ti;
         topic=to;
         link=li;
         date=da;
         date_l=dl;
    }


    @Override
    public void doTransaction() {
         data.EditSItable(si, title, topic, link, date, type,date_l);
    }

    @Override
    public void undoTransaction() {
       data.EditSItable(si, title_old, topic_old, link_old, date_old, type_old,date_l_old);
       
        
    }
}