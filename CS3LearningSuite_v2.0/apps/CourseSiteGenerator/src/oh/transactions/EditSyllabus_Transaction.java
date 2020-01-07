/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oh.transactions;

import jtps.jTPS_Transaction;
import oh.CourseSiteGeneratorApp;
import oh.data.CourseSiteGeneratorData;
import oh.data.TeachingAssistantPrototype;

/**
 *
 * @author fenghsiyu
 */
public class EditSyllabus_Transaction implements jTPS_Transaction {
    String text;
    String old_text;
    String type;
   CourseSiteGeneratorApp app;
    
    public EditSyllabus_Transaction(String text1,String type1, CourseSiteGeneratorApp a) {
        app=a;
        CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();

        text=text1;
        type=type1;
        old_text=data.getTxt(type1);
      
    }


    @Override
    public void doTransaction() {
           CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();

        
           data.setSyllabus(type, text);
       
        
       
    }

    @Override
    public void undoTransaction() {
           CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();

      
           data.setSyllabus(type, old_text);
        
        
    }
}