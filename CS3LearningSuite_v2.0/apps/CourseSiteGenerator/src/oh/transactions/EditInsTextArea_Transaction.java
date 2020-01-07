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
public class EditInsTextArea_Transaction  implements jTPS_Transaction {
    CourseSiteGeneratorApp app;
    String newText;
    String oldText;
    
    
    public EditInsTextArea_Transaction (CourseSiteGeneratorApp a,String new_text) {
        app=a;
        CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
        newText=new_text;
        oldText=data.getins_textArea_string();
        
    }


    @Override
    public void doTransaction() {
         CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
         data.setTextArea_ins(newText);
    }

    @Override
    public void undoTransaction() {
        CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
        data.setTextArea_ins(oldText);
        
    }
}