/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oh.transactions;

import jtps.jTPS_Transaction;
import oh.data.CourseSiteGeneratorData;
import oh.data.TeachingAssistantPrototype;

/**
 *
 * @author fenghsiyu
 */
public class CSS_Transaction implements jTPS_Transaction {
    CourseSiteGeneratorData data;
    String css;
    String css_old;
    
    public CSS_Transaction(CourseSiteGeneratorData initData) {
        data = initData;
        css=data.getCss();
        css_old=data.getCSSString();
        
    }

    @Override
    public void doTransaction() {
        data.setCss(css);
    }

    @Override
    public void undoTransaction() {
       data.setCss(css_old);
    }
}
