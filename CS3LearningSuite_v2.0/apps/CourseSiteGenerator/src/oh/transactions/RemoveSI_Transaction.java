/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oh.transactions;

import djf.modules.AppGUIModule;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import jtps.jTPS_Transaction;
import oh.CourseSiteGeneratorApp;
import static oh.CourseSiteGeneratorPropertyType.SCHEDULE_AE_DATE_COMBO;
import static oh.CourseSiteGeneratorPropertyType.SCHEDULE_AE_LINK_TEXTFIELD;
import static oh.CourseSiteGeneratorPropertyType.SCHEDULE_AE_TITLE_TEXTFIELD;
import static oh.CourseSiteGeneratorPropertyType.SCHEDULE_AE_TOPIC_TEXTFIELD;
import static oh.CourseSiteGeneratorPropertyType.SCHEDULE_AE_TYPE_COMBO;
import static oh.CourseSiteGeneratorPropertyType.SCHEDULE_CALENDAR_STARTMONDAY_COMBO;
import static oh.CourseSiteGeneratorPropertyType.SCHEDULE_ITEM_TABLEVIEW;
import oh.data.CourseSiteGeneratorData;
import oh.data.ScheduleItem;

/**
 *
 * @author fenghsiyu
 */
public class RemoveSI_Transaction implements jTPS_Transaction {
    
     CourseSiteGeneratorData data ;
     ScheduleItem item;
     
    
    
    public  RemoveSI_Transaction( CourseSiteGeneratorApp app ) {
        data=(CourseSiteGeneratorData) app.getDataComponent();
        AppGUIModule gui = app.getGUIModule();
        
        TableView tv=(TableView)gui.getGUINode(SCHEDULE_ITEM_TABLEVIEW);
         item=(ScheduleItem) tv.getSelectionModel().getSelectedItem();
        
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