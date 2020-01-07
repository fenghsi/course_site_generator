/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oh.transactions;

import jtps.jTPS_Transaction;
import oh.data.CourseSiteGeneratorData;
import oh.data.Labs;
import oh.data.Lectures;
import oh.data.Recitation;

/**
 *
 * @author fenghsiyu
 */
public class EditMTRecCell_Transaction implements jTPS_Transaction {
    CourseSiteGeneratorData data;
    String section;
    String daystime;
    String room;
    String ta1;
    String ta2;
    String section_old;
    String daystime_old;
    String room_old;
    String ta1_old;
    String ta2_old;
    Recitation rec;
    
    
    public EditMTRecCell_Transaction(CourseSiteGeneratorData initData,Recitation l,String newvalue, String type) {
        data = initData;
        rec=l;
        section=rec.getSection();
        daystime=rec.getDaytime();
        room=rec.getRoom();
        ta1=rec.getTa1();
        ta2=rec.getTa2();
        section_old=rec.getSection();
        daystime_old=rec.getDaytime();
        room_old=rec.getRoom();
        ta1_old=rec.getTa1();
        ta2_old=rec.getTa2();
        
        if(type.equals("section")){
            section=newvalue;
        }
        if(type.equals("daystime")){
            daystime=newvalue;
        }
        if(type.equals("room")){
            room=newvalue;
        }
        if(type.equals("ta1")){
            ta1=newvalue;
        }
        if(type.equals("ta2")){
            ta2=newvalue;
        }
        
    }

    @Override
    public void doTransaction() {
        rec.setDaytime(daystime);
        rec.setRoom(room);
        rec.setSection(section);
        rec.setTa1(ta1);
        rec.setTa2(ta2);
        data.changeRecTable(rec);
          
    }

    @Override
    public void undoTransaction() {
        rec.setDaytime(daystime_old);
        rec.setRoom(room_old);
        rec.setSection(section_old);
        rec.setTa1(ta1_old);
        rec.setTa2(ta2_old);
        data.changeRecTable(rec);
        
      
    }
}