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
public class Lectures {
    private final StringProperty section;
    private final StringProperty days;
    private final StringProperty time;
    private final StringProperty room;
    public Lectures(String s, String d,String t, String r) {
        section = new SimpleStringProperty(s);
        days = new SimpleStringProperty(d);
        time = new SimpleStringProperty(t);
        room = new SimpleStringProperty(r);
    }
    public String getSection() {
        return section.get();
    }

    public void setSection(String initName) {
        section.set(initName);
    }
    public String getDays() {
        return days.get();
    }

    public void setDays(String initName) {
        days.set(initName);
    }
    public String getTime() {
        return time.get();
    }

    public void setTime(String initName) {
        time.set(initName);
    }
    public String getRoom() {
        return room.get();
    }

    public void setRoom(String initName) {
        room.set(initName);
    }
    
}
