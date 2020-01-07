/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oh.data;

import java.time.LocalDate;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author fenghsiyu
 */
public class ScheduleItem {
    private final StringProperty type;
    private final StringProperty date;
    private final StringProperty title;
    private final StringProperty topic;
    private final StringProperty link;
    private LocalDate date_l;
    public ScheduleItem (String s, String d,String t, String r,String r1,LocalDate dd) {
        type=new SimpleStringProperty(s);
        date=new SimpleStringProperty(d);
        title=new SimpleStringProperty(t);
        topic=new SimpleStringProperty(r);
        link=new SimpleStringProperty(r1);
        date_l=dd;
     
    }
    
    public LocalDate getDate_l(){
        return date_l;
    }
    public void setDate_l(LocalDate d){
        date_l=d;
    }
    public String getType() {
        return type.get();
    }

    public void setType(String initName) {
        type.set(initName);
    }
    public String getDate() {
        return date.get();
    }

    public void setDate(String initName) {
        date.set(initName);
    }
    public String getTitle() {
        return title.get();
    }

    public void setTitle(String initName) {
        title.set(initName);
    }
    public String getTopic() {
        return topic.get();
    }

    public void setTopic(String initName) {
        topic.set(initName);
    }
    public String getLink() {
        return link.get();
    }

    public void setLink(String initName) {
        link.set(initName);
    }
    
}
