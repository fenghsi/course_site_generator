/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oh.data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author fenghsiyu
 */
public class Recitation {
    private final StringProperty section;
    private final StringProperty daytime;
    private final StringProperty room;
    private final StringProperty ta1;
    private final StringProperty ta2;
    public Recitation(String s, String d,String t, String r,String r1) {
        section = new SimpleStringProperty(s);
        daytime = new SimpleStringProperty(d);
        room = new SimpleStringProperty(t);
        ta1 = new SimpleStringProperty(r);
        ta2=new SimpleStringProperty(r1);
    }
    public String getSection() {
        return section.get();
    }

    public void setSection(String initName) {
        section.set(initName);
    }
    public String getDaytime() {
        return daytime.get();
    }

    public void setDaytime(String initName) {
        daytime.set(initName);
    }
    public String getRoom() {
        return room.get();
    }

    public void setRoom(String initName) {
        room.set(initName);
    }
    public String getTa1() {
        return ta1.get();
    }

    public void setTa1(String initName) {
        ta1.set(initName);
    }
    public String getTa2() {
        return ta2.get();
    }

    public void setTa2(String initName) {
        ta2.set(initName);
    }
    
}
