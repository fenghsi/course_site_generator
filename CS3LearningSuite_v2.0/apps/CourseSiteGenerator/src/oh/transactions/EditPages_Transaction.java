/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oh.transactions;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jtps.jTPS_Transaction;
import oh.CourseSiteGeneratorApp;
import oh.data.CourseSiteGeneratorData;
import oh.data.TeachingAssistantPrototype;

/**
 *
 * @author fenghsiyu
 */
public class EditPages_Transaction implements jTPS_Transaction {
    boolean home;
    boolean syllabus;
    boolean schedule;
    boolean hw;
    boolean home_old;
    boolean syllabus_old;
    boolean schedule_old;
    boolean hw_old;
    CourseSiteGeneratorApp app;
    
    
    
    public EditPages_Transaction(CourseSiteGeneratorApp a,boolean home1,
    boolean syllabus1,
    boolean schedule1,
    boolean hw1,
    String type) {
        app=a;
        home=home1;
        syllabus=syllabus1;
        schedule=schedule1;
        hw=hw1;
        if(type.equals("home")){
            home_old=(!home);}
        else{
            home_old=(home);
        }
        if(type.equals("syllabus")){
            syllabus_old=(!syllabus);
        }
        else{
            syllabus_old=(syllabus);
        }
        if(type.equals("schedule")){
            schedule_old=(!schedule);
        }
        else{
            schedule_old=(schedule);
        }
        if(type.equals("hw")){
            hw_old=(!hw);
        }
        else{
            hw_old=(hw);
        }
        
    }


    @Override
    public void doTransaction() {
         CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
         data.setPages(home, syllabus, schedule, hw);
        
    }

    @Override
    public void undoTransaction() {
        CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
        data.setPages(home_old, syllabus_old, schedule_old, hw_old);
        
    }
}