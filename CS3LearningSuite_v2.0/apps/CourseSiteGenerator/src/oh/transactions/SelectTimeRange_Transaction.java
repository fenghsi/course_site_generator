/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oh.transactions;

import djf.AppTemplate;
import djf.modules.AppGUIModule;
import javafx.scene.control.ComboBox;
import jtps.jTPS_Transaction;
import static oh.CourseSiteGeneratorPropertyType.OH_OFFICE_HOURS_END_COMBO;
import static oh.CourseSiteGeneratorPropertyType.OH_OFFICE_HOURS_START_COMBO;
import oh.data.CourseSiteGeneratorData;
import oh.data.TeachingAssistantPrototype;
import oh.data.TimeSlot;

/**
 *
 * @author fenghsiyu
 */
public class SelectTimeRange_Transaction implements jTPS_Transaction {
    CourseSiteGeneratorData data;
    String start;
    String end;
    String start_old;
    String end_old;
    AppTemplate app;
    
    public SelectTimeRange_Transaction(AppTemplate a,String s,String s1,String s2,String s3,CourseSiteGeneratorData d) {
        start=s;
        end=s1;
        start_old=s2;
        end_old=s3;
        data=d;
        app=a;
      
    }

    @Override
    public void doTransaction() {
       
        
        data.UpdateTimeTange(start,end);
        
    }

    @Override
    public void undoTransaction() {
        data.UpdateTimeTange(start_old,end_old);
        
        
        
        
    }
}