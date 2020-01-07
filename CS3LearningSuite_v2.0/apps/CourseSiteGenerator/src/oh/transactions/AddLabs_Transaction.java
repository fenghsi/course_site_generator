/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oh.transactions;

import jtps.jTPS_Transaction;
import oh.data.CourseSiteGeneratorData;
import oh.data.Labs;
import oh.data.TeachingAssistantPrototype;

/**
 *
 * @author fenghsiyu
 */
public class AddLabs_Transaction implements jTPS_Transaction {
    CourseSiteGeneratorData data;
    Labs lab;
    
    public AddLabs_Transaction(CourseSiteGeneratorData initData) {
        data = initData;
        lab=new Labs("?","?","?","?","?");
    }

    @Override
    public void doTransaction() {
       data.addLabs(lab);
    }

    @Override
    public void undoTransaction() {
       data.removeLabs(lab);
       System.out.print(lab.getSection());
    }
}
