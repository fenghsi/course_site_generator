/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oh.transactions;

import jtps.jTPS_Transaction;
import oh.data.CourseSiteGeneratorData;
import oh.data.Lectures;
import oh.data.TeachingAssistantPrototype;

/**
 *
 * @author fenghsiyu
 */
public class AddLecture_Transaction implements jTPS_Transaction {
    CourseSiteGeneratorData data;
    Lectures lecture;
    
    public AddLecture_Transaction (CourseSiteGeneratorData initData) {
        data = initData;
        lecture=new Lectures("?","?","?","?");
    }

    @Override
    public void doTransaction() {
        data.addLectures(lecture);
    }

    @Override
    public void undoTransaction() {
       data.removeLectures(lecture);
      
    }
}