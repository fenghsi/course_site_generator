/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oh.transactions;

import javafx.collections.ObservableList;
import jtps.jTPS_Transaction;
import oh.data.CourseSiteGeneratorData;
import oh.data.Labs;
import oh.data.Lectures;

/**
 *
 * @author fenghsiyu
 */
public class EditMTLectureCells_Transaction  implements jTPS_Transaction {
    CourseSiteGeneratorData data;
    String section;
    String days;
    String time;
    String room;
    String section_old;
    String days_old;
    String time_old;
    String room_old;
    Lectures lec;
    
    
    public EditMTLectureCells_Transaction(CourseSiteGeneratorData initData,Lectures lecture,String newvalue, String type) {
        data = initData;
        lec=lecture;
        section=lecture.getSection();
        days=lecture.getDays();
        time=lecture.getTime();
        room=lecture.getRoom();
        section_old=lecture.getSection();
        days_old=lecture.getDays();
        time_old=lecture.getTime();
        room_old=lecture.getRoom();
        if(type.equals("section")){
            section=newvalue;
        }
        if(type.equals("days")){
            days=newvalue;
        }
        if(type.equals("time")){
            time=newvalue;
        }
        if(type.equals("room")){
            room=newvalue;
        }
        
    }

    @Override
    public void doTransaction() {
        lec.setSection(section);
        lec.setDays(days);
        lec.setTime(time);
        lec.setRoom(room);
        data.changeLectureTable(lec);
          
    }

    @Override
    public void undoTransaction() {
        lec.setSection(section_old);
        lec.setDays(days_old);
        lec.setTime(time_old);
        lec.setRoom(room_old);
        data.changeLectureTable(lec);
        
      
    }
}