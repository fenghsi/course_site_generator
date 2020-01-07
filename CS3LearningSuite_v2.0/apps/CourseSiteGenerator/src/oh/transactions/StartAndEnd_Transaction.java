/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oh.transactions;

import djf.AppTemplate;
import java.time.LocalDate;
import jtps.jTPS_Transaction;
import oh.data.CourseSiteGeneratorData;

/**
 *
 * @author fenghsiyu
 */
public class StartAndEnd_Transaction implements jTPS_Transaction {
    CourseSiteGeneratorData data;
    LocalDate old_start;
    LocalDate old_end;
    LocalDate start;
    LocalDate end;
    
    
    
    public StartAndEnd_Transaction (AppTemplate a) {
        data=(CourseSiteGeneratorData)a.getDataComponent();
        old_start=data.getDatepicker_start();
        old_end=data.getDatepicker_end();
        start=data.getStartCa().getValue();
        end=data.getEndCa().getValue();
    }

    @Override
    public void doTransaction() {
       
        data.setStartEndMF(start, end);
        
    }

    @Override
    public void undoTransaction() {
        
        data.setStartEndMF(old_start, old_end);
        
        
        
    }
}
