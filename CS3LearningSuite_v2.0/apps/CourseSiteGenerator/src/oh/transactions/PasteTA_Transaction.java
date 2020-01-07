package oh.transactions;

import jtps.jTPS_Transaction;
import oh.CourseSiteGeneratorApp;
import oh.data.CourseSiteGeneratorData;
import oh.data.TeachingAssistantPrototype;

public class PasteTA_Transaction implements jTPS_Transaction {
    CourseSiteGeneratorApp app;
    TeachingAssistantPrototype taToPaste;

    public PasteTA_Transaction(  CourseSiteGeneratorApp initApp, 
                                 TeachingAssistantPrototype initTAToPaste) {
        app = initApp;
        taToPaste = initTAToPaste;
    }

    @Override
    public void doTransaction() {
        CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
        data.addTA(taToPaste);
    }

    @Override
    public void undoTransaction() {
        CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
        data.removeTA(taToPaste);
    }   
}