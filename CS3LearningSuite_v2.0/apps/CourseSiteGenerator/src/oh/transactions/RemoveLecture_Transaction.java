/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oh.transactions;

import jtps.jTPS_Transaction;
import oh.data.CourseSiteGeneratorData;
import oh.data.Lectures;
import oh.data.Recitation;

/**
 *
 * @author fenghsiyu
 */
public class RemoveLecture_Transaction implements jTPS_Transaction {
    CourseSiteGeneratorData data;
    Lectures lecs;
    
    public RemoveLecture_Transaction(CourseSiteGeneratorData initData,Lectures lec) {
        data = initData;
        lecs=lec;
    }

    @Override
    public void doTransaction() {
       data.removeLectures(lecs);
    }

    @Override
    public void undoTransaction() {
        data.addLectures(lecs);
    }
}