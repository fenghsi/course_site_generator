/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oh.transactions;

import jtps.jTPS_Transaction;
import oh.data.CourseSiteGeneratorData;
import oh.data.Labs;
import oh.data.Recitation;
import oh.data.TeachingAssistantPrototype;

/**
 *
 * @author fenghsiyu
 */
public class AddRecitation_Transaction implements jTPS_Transaction {
    CourseSiteGeneratorData data;
    Recitation rec;
    public AddRecitation_Transaction(CourseSiteGeneratorData initData) {
        data = initData;
        rec=new Recitation("?","?","?","?","?");
    }

    @Override
    public void doTransaction() {
          data.addRecitation(rec);
    }

    @Override
    public void undoTransaction() {
         data.removeRecitation(rec);
    }
}