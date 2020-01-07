/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oh.transactions;

import jtps.jTPS_Transaction;
import oh.data.CourseSiteGeneratorData;
import oh.data.Recitation;

/**
 *
 * @author fenghsiyu
 */
public class RemoveRecitation_Transaction implements jTPS_Transaction {
    CourseSiteGeneratorData data;
    Recitation recs;
    
    public RemoveRecitation_Transaction(CourseSiteGeneratorData initData,Recitation rec) {
        data = initData;
        recs=rec;
    }

    @Override
    public void doTransaction() {
       data.removeRecitation(recs);
    }

    @Override
    public void undoTransaction() {
       data.addRecitation(recs);
    }
}