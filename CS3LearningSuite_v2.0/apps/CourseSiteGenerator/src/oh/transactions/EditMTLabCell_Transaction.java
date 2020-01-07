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

/**
 *
 * @author fenghsiyu
 */
public class EditMTLabCell_Transaction implements jTPS_Transaction {
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
    Labs lab;
    
    
    public EditMTLabCell_Transaction(CourseSiteGeneratorData initData,Labs l,String newvalue, String type) {
        data = initData;
        lab=l;
        section=lab.getSection();
        daystime=lab.getDaytime();
        room=lab.getRoom();
        ta1=lab.getTa1();
        ta2=lab.getTa2();
        section_old=lab.getSection();
        daystime_old=lab.getDaytime();
        room_old=lab.getRoom();
        ta1_old=lab.getTa1();
        ta2_old=lab.getTa2();
        
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
        lab.setDaytime(daystime);
        lab.setRoom(room);
        lab.setSection(section);
        lab.setTa1(ta1);
        lab.setTa2(ta2);
        data.changeLabsTable(lab);
          
    }

    @Override
    public void undoTransaction() {
        lab.setDaytime(daystime_old);
        lab.setRoom(room_old);
        lab.setSection(section_old);
        lab.setTa1(ta1_old);
        lab.setTa2(ta2_old);
        data.changeLabsTable(lab);
        
      
    }
}