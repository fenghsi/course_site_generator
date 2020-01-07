/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oh.transactions;

import jtps.jTPS_Transaction;
import oh.CourseSiteGeneratorApp;
import oh.data.CourseSiteGeneratorData;

/**
 *
 * @author fenghsiyu
 */
public class EditInstructor_Transaction implements jTPS_Transaction {
    CourseSiteGeneratorApp app;
    String name;
    String email;
    String room;
    String homepage;
    String name_old;
    String email_old;
    String room_old;
    String homepage_old;
    
    public EditInstructor_Transaction(CourseSiteGeneratorApp a,String name1,String email1,String room1,String homepage1) {
        app=a;
        CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
        name=name1;
        email=email1;
        room=room1;;
        homepage=homepage1;
        
        name_old=data.getins_name();
        email_old=data.getins_email();
        room_old=data.getins_room();
        homepage_old=data.getins_homepage();
    }


    @Override
    public void doTransaction() {
         CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
         data.setInstructor(name, email, room, homepage);
    }

    @Override
    public void undoTransaction() {
        CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
        data.setInstructor(name_old, email_old, room_old, homepage_old);
        
    }
}