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
public class EditStartEnd_Transaction implements jTPS_Transaction {
    
   CourseSiteGeneratorApp app;
    CourseSiteGeneratorData data;
    
    public EditStartEnd_Transaction( CourseSiteGeneratorApp a) {
        CourseSiteGeneratorApp app=a;
        data = (CourseSiteGeneratorData)app.getDataComponent();
        
      
    }


    @Override
    public void doTransaction() {
          
       
    }

    @Override
    public void undoTransaction() {
          
        
    }
}