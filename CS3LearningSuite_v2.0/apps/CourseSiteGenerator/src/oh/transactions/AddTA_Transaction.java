package oh.transactions;

import jtps.jTPS_Transaction;
import oh.data.CourseSiteGeneratorData;
import oh.data.TeachingAssistantPrototype;

/**
 *
 * @author McKillaGorilla
 */
public class AddTA_Transaction implements jTPS_Transaction {
    CourseSiteGeneratorData data;
    TeachingAssistantPrototype ta;
    
    public AddTA_Transaction(CourseSiteGeneratorData initData, TeachingAssistantPrototype initTA) {
        data = initData;
        ta = initTA;
    }

    @Override
    public void doTransaction() {
        data.addTA(ta);        
    }

    @Override
    public void undoTransaction() {
        data.removeTA(ta);
    }
}
