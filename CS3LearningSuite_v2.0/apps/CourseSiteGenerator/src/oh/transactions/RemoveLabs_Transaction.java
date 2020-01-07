/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oh.transactions;

import jtps.jTPS_Transaction;
import oh.data.CourseSiteGeneratorData;
import oh.data.Labs;

/**
 *
 * @author fenghsiyu
 */
public class RemoveLabs_Transaction implements jTPS_Transaction {
    CourseSiteGeneratorData data;
    Labs labs;
    
    public RemoveLabs_Transaction(CourseSiteGeneratorData initData,Labs lab) {
        data = initData;
        labs=lab;
    }

    @Override
    public void doTransaction() {
       data.removeLabs(labs);
    }

    @Override
    public void undoTransaction() {
       data.addLabs(labs);
    }
}