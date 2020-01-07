package oh.files;

import static djf.AppPropertyType.EXPORT_BUTTON;
import static djf.AppPropertyType.LOAD_BUTTON;
import static djf.AppPropertyType.UNDO_BUTTON;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import djf.components.AppDataComponent;
import djf.components.AppFileComponent;
import djf.modules.AppGUIModule;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.image.Image;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javax.imageio.ImageIO;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import oh.CourseSiteGeneratorApp;
import oh.data.CourseSiteGeneratorData;
import oh.data.Labs;
import oh.data.Lectures;
import oh.data.Recitation;
import oh.data.ScheduleItem;
import oh.data.TAType;
import oh.data.TeachingAssistantPrototype;
import oh.data.TimeSlot;
import oh.data.TimeSlot.DayOfWeek;

/**
 * This class serves as the file component for the TA
 * manager app. It provides all saving and loading 
 * services for the application.
 * 
 * @author Richard McKenna
 */
public class CourseSiteGeneratorFiles implements AppFileComponent {
    // THIS IS THE APP ITSELF
    CourseSiteGeneratorApp app;
    
    // THESE ARE USED FOR IDENTIFYING JSON TYPES
    static final String JSON_GRAD_TAS = "grad_tas";
    static final String JSON_UNDERGRAD_TAS = "undergrad_tas";
    static final String JSON_NAME = "name";
    static final String JSON_EMAIL = "email";
    static final String JSON_TYPE = "type";
    static final String JSON_OFFICE_HOURS = "officeHours";
    static final String JSON_START_HOUR = "startHour";
    static final String JSON_END_HOUR = "endHour";
    static final String JSON_START_TIME = "time";
    static final String JSON_DAY_OF_WEEK = "day";
    static final String JSON_MONDAY = "monday";
    static final String JSON_TUESDAY = "tuesday";
    static final String JSON_WEDNESDAY = "wednesday";
    static final String JSON_THURSDAY = "thursday";
    static final String JSON_FRIDAY = "friday";
    static final String JSON_BANNER_SUBJECT = "subject";
    static final String JSON_BANNER_SMESTER = "smester";
    static final String JSON_BANNER_NUMBER= "number";
    static final String JSON_BANNER_YEAR = "year";
    static final String JSON_BANNER_TITLE = "title";
    static final String JSON_LOGOS = "logos";
    static final String JSON_LOGOS_FAVICON = "favicon";
    static final String JSON_LOGOS_NAVBAR = "navbar";
    static final String JSON_LOGOS_LEFTFOOTER = "bottom_left";
    static final String JSON_LOGOS_RIGHTFOOTER = "bottom_right";
    static final String JSON_LOGOS_SRC = "src";
    static final String JSON_LOGOS_HREF = "href";
    static final String JSON_INS = "instructor";
    static final String JSON_INS_LINK = "link";
    static final String JSON_INS_NAME = "name";
    static final String JSON_INS_EMAIL = "email";
    static final String JSON_INS_ROOM = "room";
    static final String JSON_INS_HOURS = "hours";
    static final String JSON_PAGES = "pages";
    static final String JSON_PAGES_NAME = "name";
    static final String JSON_SYLLABUS_DES = "description";
    static final String JSON_SYLLABUS_TOP = "topics";
    static final String JSON_SYLLABUS_PRE = "prerequisites";
    static final String JSON_SYLLABUS_OUT = "outcomes";
    static final String JSON_SYLLABUS_TEX = "textbooks";
    static final String JSON_SYLLABUS_GC = "gradedComponents";
    static final String JSON_SYLLABUS_GN = "gradingNote";
    static final String JSON_SYLLABUS_AD = "academicDishonesty";
    static final String JSON_SYLLABUS_SA = "specialAssistance";
    static final String JSON_MT_LECTURE = "lectures";
    static final String JSON_MT_RECITATION = "recitations";
    static final String JSON_MT_LABS = "labs";
    
    static final String JSON_MT_SECTION = "section";
    static final String JSON_MT_DAY = "days";
    static final String JSON_MT_TIME = "time";
    static final String JSON_MT_DAY_TIME = "day_time";
    static final String JSON_MT_ROOM = "location";
    static final String JSON_MT_ROOM_L = "room";
    static final String JSON_MT_TA1 = "ta_1";
    static final String JSON_MT_TA2 = "ta_2";
    
    static final String JSON_SI_STARTMONDAYMONTH = "startingMondayMonth";
    static final String JSON_SI_STARTMONDAYDAY = "startingMondayDay";
    static final String JSON_SI_ENDINGFRIDAYMONTH = "endingFridayMonth";
    static final String JSON_SI_ENDINGFRIDAYDAY ="endingFridayDay";
    
   static final String JSON_SI_HOLIDAYS ="holidays";
   static final String JSON_SI_LECTURES ="lecture";
   static final String JSON_SI_RECITATIONS ="recitation";
   static final String JSON_SI_REFERENCES ="references";
   static final String JSON_SI_HWS ="hws";
   
   static final String JSON_SI_MONTH ="month";
   static final String JSON_SI_DAY ="day";
   static final String JSON_SI_TITLE ="title";
   static final String JSON_SI_TOPIC ="topic";
   static final String JSON_SI_LINK ="link";
   
    

    public CourseSiteGeneratorFiles(CourseSiteGeneratorApp initApp) {
        app = initApp;
    }

    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException {
	// CLEAR THE OLD DATA OUT
        
        AppGUIModule gui = app.getGUIModule();
        Button exportbut=(Button)gui.getGUINode(EXPORT_BUTTON);
        exportbut.setDisable(false);
        
        
        
	CourseSiteGeneratorData dataManager = (CourseSiteGeneratorData)data;
        dataManager.reset();
        
	// LOAD THE JSON FILE WITH ALL THE DATA
	JsonObject json = loadJSONFile(filePath);
        
	// LOAD THE START AND END HOURS
	String startHour = json.getString(JSON_START_HOUR);
        String endHour = json.getString(JSON_END_HOUR);
        dataManager.initHours(startHour, endHour);
       
        
        // LOAD ALL THE GRAD TAs
        loadTAs(dataManager, json, JSON_GRAD_TAS);
        loadTAs(dataManager, json, JSON_UNDERGRAD_TAS);

        // AND THEN ALL THE OFFICE HOURS
        JsonArray jsonOfficeHoursArray = json.getJsonArray(JSON_OFFICE_HOURS);
       
        for (int i = 0; i < jsonOfficeHoursArray.size(); i++) {
            JsonObject jsonOfficeHours = jsonOfficeHoursArray.getJsonObject(i);
            String startTime = jsonOfficeHours.getString(JSON_START_TIME);
            DayOfWeek dow = DayOfWeek.valueOf(jsonOfficeHours.getString(JSON_DAY_OF_WEEK));
            String name = jsonOfficeHours.getString(JSON_NAME);
     
            TeachingAssistantPrototype ta = dataManager.getTAWithName(name);
            TimeSlot timeSlot = dataManager.getTimeSlot(startTime);

            timeSlot.toggleTA(dow, ta);
        }
        
        String subject=json.getString(JSON_BANNER_SUBJECT);
        String number=json.getString(JSON_BANNER_NUMBER);
        String semester=json.getString(JSON_BANNER_SMESTER);
        String year=json.getString(JSON_BANNER_YEAR);
        String title=json.getString(JSON_BANNER_TITLE );
        
        dataManager.setBanner(subject, number, semester, year, title);
        JsonObject logos=json.getJsonObject(JSON_LOGOS);
            String fav=logos.getJsonObject(JSON_LOGOS_FAVICON).getString(JSON_LOGOS_SRC);
            String nav=logos.getJsonObject(JSON_LOGOS_NAVBAR).getString(JSON_LOGOS_SRC);
            String lef=logos.getJsonObject(JSON_LOGOS_LEFTFOOTER).getString(JSON_LOGOS_SRC) ;      
            String rig=logos.getJsonObject(JSON_LOGOS_RIGHTFOOTER).getString(JSON_LOGOS_SRC);
              
        dataManager.setStyle(fav, nav, lef, rig);
        
        JsonObject instructor=json.getJsonObject(JSON_INS);
        String name=instructor.getString(JSON_INS_NAME);
        String room=instructor.getString(JSON_INS_ROOM);
        String email=instructor.getString(JSON_INS_EMAIL );
        String homepages=instructor.getString(JSON_INS_LINK);
        String hours=instructor.getJsonArray(JSON_INS_HOURS).toString();
        
        dataManager.setInstructor(name, email, room, homepages);
        dataManager.setTextArea_ins(hours);
        
        JsonArray pages=json.getJsonArray(JSON_PAGES );
        boolean home=false;
        boolean syllabus=false;
        boolean schedule=false;
        boolean hw=false;
        for(int i=0;i<pages.size();i++){
            JsonObject pages_ob=(JsonObject) pages.get(i);
            if(pages_ob.getString(JSON_PAGES_NAME).equals("Home")){
                home=true;
            }
            if(pages_ob.getString(JSON_PAGES_NAME).equals("Syllabus")){
                syllabus=true;
            }
            if(pages_ob.getString(JSON_PAGES_NAME).equals("Schedule")){
                schedule=true;
            }
            if(pages_ob.getString(JSON_PAGES_NAME).equals("HWs")){
                hw=true;
            }
        }
        dataManager.setPages(home, syllabus, schedule, hw);
        
        
        ///////syllabuys
        
        String description=json.getString(JSON_SYLLABUS_DES);
        String prerequisites=json.getString(JSON_SYLLABUS_PRE);
        String gn=json.getString(JSON_SYLLABUS_GN);
        String ad=json.getString(JSON_SYLLABUS_AD);
        String sa=json.getString(JSON_SYLLABUS_SA);
        String topics=json.getJsonArray(JSON_SYLLABUS_TOP).toString();
        String outcomes=json.getJsonArray(JSON_SYLLABUS_OUT).toString();
        String textbooks=json.getJsonArray(JSON_SYLLABUS_TEX).toString();
        String gc=json.getJsonArray(JSON_SYLLABUS_GC).toString();
        
        dataManager.setSyllabus( "description",description);
        dataManager.setSyllabus( "topics",topics);
        dataManager.setSyllabus( "prerequisites",prerequisites);
        dataManager.setSyllabus( "outcomes",outcomes);
        dataManager.setSyllabus( "textbooks",textbooks);
        dataManager.setSyllabus( "gradedComponents",gc);
        dataManager.setSyllabus( "gradingNote",gn);
        dataManager.setSyllabus( "academicDishonesty",ad);
        dataManager.setSyllabus( "specialAssistance",sa);
        
      //meeting
      JsonArray lectures=json.getJsonArray(JSON_MT_LECTURE );
      JsonArray labs=json.getJsonArray(JSON_MT_LABS );
      JsonArray recitations=json.getJsonArray(JSON_MT_RECITATION  );
        
      for(int i=0;i<lectures.size();i++){
          JsonObject item=lectures.getJsonObject(i);
          String sections= item.getString(JSON_MT_SECTION);
          String day= item.getString(JSON_MT_DAY);
          String time= item.getString(JSON_MT_TIME);
          String rooms= item.getString(JSON_MT_ROOM_L);
          Lectures newlec=new Lectures(sections,day,time,rooms);
          dataManager.addLectures(newlec);
      }
      for(int i=0;i<recitations.size();i++){
          JsonObject item=recitations.getJsonObject(i);
          String sections= item.getString(JSON_MT_SECTION);
          String daytime= item.getString(JSON_MT_DAY_TIME);
          String rooms= item.getString(JSON_MT_ROOM);
          String ta1= item.getString(JSON_MT_TA1);
          String ta2= item.getString(JSON_MT_TA2);
          Recitation newrec=new Recitation(sections,daytime,rooms,ta1,ta2);
          dataManager.addRecitation(newrec);
          
      }
      for(int i=0;i<labs.size();i++){
          JsonObject item=labs.getJsonObject(i);
          String sections= item.getString(JSON_MT_SECTION);
          String daytime= item.getString(JSON_MT_DAY_TIME);
          String rooms= item.getString(JSON_MT_ROOM);
          String ta1= item.getString(JSON_MT_TA1);
          String ta2= item.getString(JSON_MT_TA2);
          Labs newlabs=new Labs(sections,daytime,rooms,ta1,ta2);
          dataManager.addLabs(newlabs);
      }
      
      //schedule
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      
      int month_start=Integer.parseInt(json.getString(JSON_SI_STARTMONDAYMONTH ));
      int day_start=Integer.parseInt(json.getString(JSON_SI_STARTMONDAYDAY ));
      int month_END=Integer.parseInt(json.getString(JSON_SI_ENDINGFRIDAYMONTH ));
      int day_END=Integer.parseInt(json.getString(JSON_SI_ENDINGFRIDAYDAY ));
      String  m1;
      String  d1;
      String  m2;
      String  d2;
      
      if(month_start<10){
          m1="0"+month_start;
      }
      else{
          m1=""+month_start;
      }
      if(day_start<10){
          d1="0"+day_start;
      }
      else{
          d1=""+day_start;
      }
      if(month_END<10){
          m2="0"+month_END;
      }
      else{
          m2=""+month_END;
      }
      if(day_END<10){
          d2="0"+day_END;
      }
      else{
          d2=""+day_END;
      }
        DatePicker startca=dataManager.getStartCa();
        DatePicker endca=dataManager.getEndCa();
        LocalDate localDate_start = LocalDate.parse("2018-"+m1+"-"+d1, formatter);
        LocalDate localDate_end = LocalDate.parse("2018-"+m2+"-"+d2, formatter);
        
        startca.setValue(localDate_start);
        endca.setValue(localDate_end);
      
        
      JsonArray holiday=json.getJsonArray(JSON_SI_HOLIDAYS );
      JsonArray lecture=json.getJsonArray(JSON_SI_LECTURES  );
      JsonArray reference=json.getJsonArray(JSON_SI_RECITATIONS );
      JsonArray recitation=json.getJsonArray(JSON_SI_REFERENCES);
      JsonArray hws=json.getJsonArray(JSON_SI_HWS);
      
      for(int i=0;i<holiday.size();i++){
          JsonObject item=holiday.getJsonObject(i);
          String month=item.getString(JSON_SI_MONTH);
          String day=item.getString(JSON_SI_DAY);
          String title1=item.getString(JSON_SI_TITLE);
          String link=item.getString(JSON_SI_LINK);
          
          if(Integer.parseInt(month)<10){
              month="0"+month;
          }
          if(Integer.parseInt(day)<10){
              day="0"+day;
          }
          LocalDate tem = LocalDate.parse("2018-"+month+"-"+day, formatter);
          ScheduleItem si=new ScheduleItem("holidays","2018-"+month+"-"+day,title1,"",link,tem);
          dataManager.getSItable().add(si);
      }
      for(int i=0;i<lecture.size();i++){
          JsonObject item=lecture.getJsonObject(i);
          String month=item.getString(JSON_SI_MONTH);
          String day=item.getString(JSON_SI_DAY);
          String titles=item.getString(JSON_SI_TITLE);
          String topic=item.getString(JSON_SI_TOPIC);
          String link=item.getString(JSON_SI_LINK);
          
          if(Integer.parseInt(month)<10){
              month="0"+month;
          }
          if(Integer.parseInt(day)<10){
              day="0"+day;
          }
          LocalDate tem = LocalDate.parse("2018-"+month+"-"+day, formatter);
          ScheduleItem si=new ScheduleItem("lecture","2018-"+month+"-"+day,titles,topic,link,tem);
          dataManager.getSItable().add(si);
      }
      for(int i=0;i<recitation.size();i++){
          JsonObject item=recitation.getJsonObject(i);
          String month=item.getString(JSON_SI_MONTH);
          String day=item.getString(JSON_SI_DAY);
          String titles=item.getString(JSON_SI_TITLE);
          String topic=item.getString(JSON_SI_TOPIC);
          String link=item.getString(JSON_SI_LINK);
          
          if(Integer.parseInt(month)<10){
              month="0"+month;
          }
          if(Integer.parseInt(day)<10){
              day="0"+day;
          }
          LocalDate tem = LocalDate.parse("2018-"+month+"-"+day, formatter);
          ScheduleItem si=new ScheduleItem("recitations","2018-"+month+"-"+day,titles,topic,link,tem);
          dataManager.getSItable().add(si);
      }
      for(int i=0;i<reference.size();i++){
          JsonObject item=reference.getJsonObject(i);
          String month=item.getString(JSON_SI_MONTH);
          String day=item.getString(JSON_SI_DAY);
          String titles=item.getString(JSON_SI_TITLE);
          String topic=item.getString(JSON_SI_TOPIC);
          String link=item.getString(JSON_SI_LINK);
          
          if(Integer.parseInt(month)<10){
              month="0"+month;
          }
          if(Integer.parseInt(day)<10){
              day="0"+day;
          }
          LocalDate tem = LocalDate.parse("2018-"+month+"-"+day, formatter);
          ScheduleItem si=new ScheduleItem("references","2018-"+month+"-"+day,titles,topic,link,tem);
          dataManager.getSItable().add(si);
      }
      for(int i=0;i<hws.size();i++){
          JsonObject item=hws.getJsonObject(i);
          String month=item.getString(JSON_SI_MONTH);
          String day=item.getString(JSON_SI_DAY);
          String titles=item.getString(JSON_SI_TITLE);
          String topic=item.getString(JSON_SI_TOPIC);
          String link=item.getString(JSON_SI_LINK);
          
          if(Integer.parseInt(month)<10){
              month="0"+month;
          }
          if(Integer.parseInt(day)<10){
              day="0"+day;
          }
          LocalDate tem = LocalDate.parse("2018-"+month+"-"+day, formatter);
          ScheduleItem si=new ScheduleItem("hws","2018-"+month+"-"+day,titles,topic,link,tem);
          dataManager.getSItable().add(si);
      }
      dataManager.setCss(json.getString("css"));
      
      
      if(!((Button)(gui.getGUINode(LOAD_BUTTON))).isFocused()){
         
            app.getTPS().clearAllTransactions();
            ((Button)(gui.getGUINode(UNDO_BUTTON))).setDisable(true);
            dataManager.setBannerTitle(dataManager.getTEXTF());
        }
   

    }
    
    private void loadTAs(CourseSiteGeneratorData data, JsonObject json, String tas) {
        JsonArray jsonTAArray = json.getJsonArray(tas);
        for (int i = 0; i < jsonTAArray.size(); i++) {
            JsonObject jsonTA = jsonTAArray.getJsonObject(i);
            String name = jsonTA.getString(JSON_NAME);
            String email = jsonTA.getString(JSON_EMAIL);
            TAType type = TAType.valueOf(jsonTA.getString(JSON_TYPE));
            TeachingAssistantPrototype ta = new TeachingAssistantPrototype(name, email, type);
        
            data.addTA(ta);
        }     
    }
      
    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
	InputStream is = new FileInputStream(jsonFilePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
	return json;
    }

    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException {
	// GET THE DATA
        AppGUIModule gui = app.getGUIModule();
        Button exportbut=(Button)gui.getGUINode(EXPORT_BUTTON);
        exportbut.setDisable(false);
        
	CourseSiteGeneratorData dataManager = (CourseSiteGeneratorData)data;

	// NOW BUILD THE TA JSON OBJCTS TO SAVE
	JsonArrayBuilder gradTAsArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder undergradTAsArrayBuilder = Json.createArrayBuilder();
	Iterator<TeachingAssistantPrototype> tasIterator = dataManager.teachingAssistantsIterator();
        while (tasIterator.hasNext()) {
            TeachingAssistantPrototype ta = tasIterator.next();
	    JsonObject taJson = Json.createObjectBuilder()
		    .add(JSON_NAME, ta.getName())
		    .add(JSON_EMAIL, ta.getEmail())
                    .add(JSON_TYPE, ta.getType().toString()).build();
            if (ta.getType().equals(TAType.Graduate.toString()))
                gradTAsArrayBuilder.add(taJson);
            else
                undergradTAsArrayBuilder.add(taJson);
	}
        JsonArray gradTAsArray = gradTAsArrayBuilder.build();
	JsonArray undergradTAsArray = undergradTAsArrayBuilder.build();

	// NOW BUILD THE OFFICE HOURS JSON OBJCTS TO SAVE
	JsonArrayBuilder officeHoursArrayBuilder = Json.createArrayBuilder();
        Iterator<TimeSlot> timeSlotsIterator = dataManager.officeHoursIterator();
        while (timeSlotsIterator.hasNext()) {
            TimeSlot timeSlot = timeSlotsIterator.next();
            for (int i = 0; i < DayOfWeek.values().length; i++) {
                DayOfWeek dow = DayOfWeek.values()[i];
                tasIterator = timeSlot.getTAsIterator(dow);
                while (tasIterator.hasNext()) {
                    TeachingAssistantPrototype ta = tasIterator.next();
                    JsonObject tsJson = Json.createObjectBuilder()
                        .add(JSON_START_TIME, timeSlot.getStartTime().replace(":", "_"))
                        .add(JSON_DAY_OF_WEEK, dow.toString())
                        .add(JSON_NAME, ta.getName()).build();
                    officeHoursArrayBuilder.add(tsJson);
                }
            }
	}
	JsonArray officeHoursArray = officeHoursArrayBuilder.build();
   //site
        JsonArrayBuilder pages= Json.createArrayBuilder();
        if(dataManager.getHome()){
            pages.add(Json.createObjectBuilder().add(JSON_PAGES_NAME,"Home").add( "link", "index.html"));
        }
        if(dataManager.getSyllabus()){
            pages.add(Json.createObjectBuilder().add(JSON_PAGES_NAME,"Syllabus").add( "link", "syllabus.html"));
        }
        if(dataManager.getSchedule()){
            pages.add(Json.createObjectBuilder().add(JSON_PAGES_NAME,"Schedule").add( "link", "schedule.html"));
        }
        if(dataManager.getHW()){
            pages.add(Json.createObjectBuilder().add(JSON_PAGES_NAME,"HWs").add( "link", "hws.html"));
        }
        
        JsonArrayBuilder hourArray = Json.createArrayBuilder();
       
        ArrayList hours=dataManager.getHours_list();
        for(int i=0;i<hours.size();i++){
            JsonReader jsonReader = Json.createReader(new StringReader((String) hours.get(i)));
            JsonObject object = jsonReader.readObject();
            hourArray.add(object);
            jsonReader.close();
        }
       
        JsonArray hourArray_fi = hourArray.build();
       
        JsonArrayBuilder topic = Json.createArrayBuilder();
        ArrayList topic_array=dataManager.getTopics();
        
        for(int i=0;i<topic_array.size();i++){
            topic.add((String) topic_array.get(i));
        }
      //Syllabus
        JsonArray topics_fi = topic.build();
        
        JsonArrayBuilder outcomes = Json.createArrayBuilder();
        ArrayList outcomes_array=dataManager.getOutComes();
        
        for(int i=0;i<outcomes_array.size();i++){
            outcomes.add((String) outcomes_array.get(i));
        }
        
        JsonArray outcomes_fi = outcomes.build();
        
        JsonArrayBuilder textbook = Json.createArrayBuilder();
      
        
        ArrayList textbook_array=dataManager.getTextBooks();
        for(int i=0;i<textbook_array.size();i++){
            JsonReader jsonReader = Json.createReader(new StringReader((String) textbook_array.get(i)));
            JsonObject object = jsonReader.readObject();
            textbook.add(object);
            jsonReader.close();
        }
       
        JsonArray textbook_fi = textbook.build();
        
        
        JsonArrayBuilder gradcomp= Json.createArrayBuilder();
        
        ArrayList gradcomp_array=dataManager.getGradingComp();
        for(int i=0;i<gradcomp_array.size();i++){
            JsonReader jsonReader = Json.createReader(new StringReader((String) gradcomp_array.get(i)));
            JsonObject object = jsonReader.readObject();
            gradcomp.add(object);
            jsonReader.close();
        }
       
        JsonArray gradcomp_fi = gradcomp.build();
        
          //meeting
          JsonArrayBuilder lecture= Json.createArrayBuilder();
          JsonArrayBuilder recitation= Json.createArrayBuilder();
          JsonArrayBuilder lab= Json.createArrayBuilder();
          ObservableList<Lectures> lecture_a=dataManager.getLectures();
          ObservableList<Recitation> recitation_a=dataManager.getRecitation();
          ObservableList<Labs> labs_a=dataManager.getLabs();
          
          for(int i=0;i<lecture_a.size();i++){
            Lectures lec = lecture_a.get(i);
	    JsonObject taJson = Json.createObjectBuilder()
		    .add(JSON_MT_SECTION, lec.getSection())
		    .add(JSON_MT_DAY, lec.getDays())
                    .add(JSON_MT_TIME, lec.getTime())
                    .add(JSON_MT_ROOM_L, lec.getRoom())
                    .build();
           lecture.add(taJson);
          }
          for(int i=0;i<recitation_a.size();i++){
              Recitation lec = recitation_a.get(i);
	      JsonObject taJson = Json.createObjectBuilder()
		    .add(JSON_MT_SECTION, lec.getSection())
		    .add(JSON_MT_DAY_TIME, lec.getDaytime())
                    .add(JSON_MT_ROOM, lec.getRoom())
                    .add(JSON_MT_TA1, lec.getTa1())
                    .add(JSON_MT_TA2, lec.getTa2())
                    .build();
              recitation.add(taJson);
          }
          for(int i=0;i<labs_a.size();i++){
              Labs lec = labs_a.get(i);
	      JsonObject taJson = Json.createObjectBuilder()
		    .add(JSON_MT_SECTION, lec.getSection())
		    .add(JSON_MT_DAY_TIME, lec.getDaytime())
                    .add(JSON_MT_ROOM, lec.getRoom())
                    .add(JSON_MT_TA1, lec.getTa1())
                    .add(JSON_MT_TA2, lec.getTa2())
                    .build();
              lab.add(taJson);
          
          }
          JsonArray lecture_fi =lecture.build();
          JsonArray recitation_fi =recitation.build();
          JsonArray lab_fi =lab.build();
          
        //schedule
            ObservableList<ScheduleItem> scheduleItems=dataManager.getSItable();
            ArrayList<ScheduleItem> holiday=new ArrayList<ScheduleItem>();
            ArrayList<ScheduleItem> lectures=new ArrayList<ScheduleItem>();
            ArrayList<ScheduleItem> reference=new ArrayList<ScheduleItem>();
            ArrayList<ScheduleItem> recitations=new ArrayList<ScheduleItem>();
            ArrayList<ScheduleItem> hws=new ArrayList();
            for(int i=0;i<scheduleItems.size();i++){
                if(scheduleItems.get(i).getType().equals("holidays")){
                    holiday.add(scheduleItems.get(i));
                }
                if(scheduleItems.get(i).getType().equals("lectures")){
                    lectures.add(scheduleItems.get(i));
                }
                if(scheduleItems.get(i).getType().equals("recitations")){
                    recitations.add(scheduleItems.get(i));
                }
                if(scheduleItems.get(i).getType().equals("references")){
                    reference.add(scheduleItems.get(i));
                }
                if(scheduleItems.get(i).getType().equals("hws")){
                    hws.add(scheduleItems.get(i));
                }
            }
            JsonArrayBuilder holiday_array= Json.createArrayBuilder();
            JsonArrayBuilder lectures_array= Json.createArrayBuilder();
            JsonArrayBuilder recitations_array= Json.createArrayBuilder();
            JsonArrayBuilder references_array= Json.createArrayBuilder();
            JsonArrayBuilder hws_array= Json.createArrayBuilder();
            for(int i=0;i<holiday.size();i++){
                
                JsonObject taJson = Json.createObjectBuilder()
                        .add(JSON_SI_MONTH, ""+holiday.get(i).getDate_l().getMonthValue())
                        .add(JSON_SI_DAY, ""+holiday.get(i).getDate_l().getDayOfMonth())
                        .add(JSON_SI_TITLE ,holiday.get(i).getTitle())
                        .add(JSON_SI_LINK , holiday.get(i).getLink())
                        .build();
                 holiday_array.add(taJson);
            }
            for(int i=0;i<lectures.size();i++){
                
                JsonObject taJson = Json.createObjectBuilder()
                        .add(JSON_SI_MONTH, ""+lectures.get(i).getDate_l().getMonthValue())
                        .add(JSON_SI_DAY, ""+lectures.get(i).getDate_l().getDayOfMonth())
                        .add(JSON_SI_TITLE ,lectures.get(i).getTitle())
                        .add(JSON_SI_TOPIC, lectures.get(i).getTopic())
                        .add(JSON_SI_LINK , lectures.get(i).getLink())
                        .build();
                 lectures_array.add(taJson);
            }
            for(int i=0;i<recitations.size();i++){
                
                JsonObject taJson = Json.createObjectBuilder()
                        .add(JSON_SI_MONTH, ""+recitations.get(i).getDate_l().getMonthValue())
                        .add(JSON_SI_DAY, ""+recitations.get(i).getDate_l().getDayOfMonth())
                        .add(JSON_SI_TITLE ,recitations.get(i).getTitle())
                        .add(JSON_SI_TOPIC, recitations.get(i).getTopic())
                        .add(JSON_SI_LINK , recitations.get(i).getLink())
                        .build();
                 recitations_array.add(taJson);
            }
            for(int i=0;i<reference.size();i++){
                
                JsonObject taJson = Json.createObjectBuilder()
                        .add(JSON_SI_MONTH, ""+reference.get(i).getDate_l().getMonthValue())
                        .add(JSON_SI_DAY, ""+reference.get(i).getDate_l().getDayOfMonth())
                        .add(JSON_SI_TITLE ,reference.get(i).getTitle())
                        .add(JSON_SI_TOPIC, reference.get(i).getTopic())
                        .add(JSON_SI_LINK , reference.get(i).getLink())
                        .build();
                 references_array.add(taJson);
            }
            for(int i=0;i<hws.size();i++){
                
                JsonObject taJson = Json.createObjectBuilder()
                        .add(JSON_SI_MONTH, ""+hws.get(i).getDate_l().getMonthValue())
                        .add(JSON_SI_DAY, ""+hws.get(i).getDate_l().getDayOfMonth())
                        .add(JSON_SI_TITLE ,hws.get(i).getTitle())
                        .add(JSON_SI_TOPIC, hws.get(i).getTopic())
                        .add(JSON_SI_LINK , hws.get(i).getLink())
                        .build();
                 hws_array.add(taJson);
            }
            
             
            
  
   
 
	// THEN PUT IT ALL TOGETHER IN A JsonObject
	JsonObject dataManagerJSO = Json.createObjectBuilder()
		.add(JSON_START_HOUR, "" + dataManager.getStartHour())
		.add(JSON_END_HOUR, "" + dataManager.getEndHour())
                .add(JSON_GRAD_TAS, gradTAsArray)
                .add(JSON_UNDERGRAD_TAS, undergradTAsArray)
                .add(JSON_OFFICE_HOURS, officeHoursArray)
                .add(JSON_BANNER_SUBJECT, ""+dataManager.getBannerSubject())
                .add(JSON_BANNER_SMESTER, ""+dataManager.getBannersmester())
                .add(JSON_BANNER_NUMBER, ""+dataManager.getBannerNumber())
                .add(JSON_BANNER_YEAR, ""+dataManager.getBannerYear())
                .add(JSON_BANNER_TITLE, ""+dataManager.getBannerTitle())
                .add(JSON_LOGOS, Json.createObjectBuilder()
                    .add(JSON_LOGOS_FAVICON, Json.createObjectBuilder().add(JSON_LOGOS_SRC, dataManager.convertPath(dataManager.getFavicon_path())))
                    .add(JSON_LOGOS_NAVBAR, Json.createObjectBuilder().add(JSON_LOGOS_SRC, dataManager.convertPath(dataManager.getnavbar_path())))
                    .add(JSON_LOGOS_LEFTFOOTER, Json.createObjectBuilder().add(JSON_LOGOS_SRC, dataManager.convertPath(dataManager.getleftfooter_path())))
                    .add(JSON_LOGOS_RIGHTFOOTER, Json.createObjectBuilder().add(JSON_LOGOS_SRC, dataManager.convertPath(dataManager.getrightfooter_path()))))
                .add(JSON_INS, Json.createObjectBuilder()
                    .add(JSON_INS_NAME, dataManager.getins_name())
                    .add(JSON_INS_LINK, dataManager.getins_homepage())
                    .add(JSON_INS_EMAIL, dataManager.getins_email())
                    .add("photo", "./images/RichardMcKenna.jpg")
                    .add(JSON_INS_ROOM, dataManager.getins_room())
                    .add(JSON_INS_HOURS, hourArray_fi))
                .add(JSON_PAGES, pages)
                .add(JSON_SYLLABUS_DES, dataManager.getTxt("description"))
                .add(JSON_SYLLABUS_TOP, topics_fi)
                .add(JSON_SYLLABUS_PRE , dataManager.getTxt("prerequisites"))
                .add(JSON_SYLLABUS_OUT, outcomes_fi)
                .add(JSON_SYLLABUS_TEX,textbook_fi)
                .add(JSON_SYLLABUS_GC, gradcomp_fi )
                .add(JSON_SYLLABUS_GN, dataManager.getTxt("gradingNote"))
                .add(JSON_SYLLABUS_AD, dataManager.getTxt("academicDishonesty"))
                .add(JSON_SYLLABUS_SA, dataManager.getTxt("specialAssistance"))
                .add(JSON_MT_LECTURE,lecture_fi )
                .add(JSON_MT_RECITATION, recitation_fi)
                .add(JSON_MT_LABS, lab_fi)
                .add(JSON_SI_STARTMONDAYMONTH, ""+dataManager.getStartCa().getValue().getMonthValue())
                .add(JSON_SI_STARTMONDAYDAY, ""+dataManager.getStartCa().getValue().getDayOfMonth())
                .add(JSON_SI_ENDINGFRIDAYMONTH , ""+dataManager.getEndCa().getValue().getMonthValue())
                .add(JSON_SI_ENDINGFRIDAYDAY, ""+dataManager.getEndCa().getValue().getDayOfMonth())
                .add(JSON_SI_HOLIDAYS,  holiday_array.build())
                .add(JSON_SI_LECTURES, lectures_array.build())
                .add(JSON_SI_RECITATIONS, recitations_array.build())
                .add(JSON_SI_REFERENCES, references_array.build())
                .add(JSON_SI_HWS, hws_array.build())
                .add("css", dataManager.getCss())
		.build();
        
           
         
        //.add(JSON_INS_HOURS, Json.createArrayBuilder().add(Json.createObjectBuilder().add(JSON_DAY_OF_WEEK, BigDecimal.ONE)))
	
    
	// AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
        
        
    }
    
    // IMPORTING/EXPORTING DATA IS USED WHEN WE READ/WRITE DATA IN AN
    // ADDITIONAL FORMAT USEFUL FOR ANOTHER PURPOSE, LIKE ANOTHER APPLICATION

    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void exportData(AppDataComponent data, String filePath) throws IOException {
        CourseSiteGeneratorData newdata=(CourseSiteGeneratorData) data;
        File x=new File("./work/"+filePath);
        JsonObject json = loadJSONFile(x.getAbsolutePath());
        
        //create the directory
        File dir=new File("./export/"+json.getString(JSON_BANNER_SUBJECT)+"_"+json.getString(JSON_BANNER_NUMBER)+"_"+json.getString(JSON_BANNER_SMESTER)+"_"+json.getString(JSON_BANNER_YEAR));
        dir.mkdir();
        
        //create js folder
        File dir_js=new File("./export/"+json.getString(JSON_BANNER_SUBJECT)+"_"+json.getString(JSON_BANNER_NUMBER)+"_"+json.getString(JSON_BANNER_SMESTER)+"_"+json.getString(JSON_BANNER_YEAR)+"/js");
        dir_js.mkdir();
        //create css folder
        File dir_css=new File("./export/"+json.getString(JSON_BANNER_SUBJECT)+"_"+json.getString(JSON_BANNER_NUMBER)+"_"+json.getString(JSON_BANNER_SMESTER)+"_"+json.getString(JSON_BANNER_YEAR)+"/css");
        dir_css.mkdir();
        //create images folder
        File dir_img=new File("./export/"+json.getString(JSON_BANNER_SUBJECT)+"_"+json.getString(JSON_BANNER_NUMBER)+"_"+json.getString(JSON_BANNER_SMESTER)+"_"+json.getString(JSON_BANNER_YEAR)+"/images");
        dir_img.mkdir();
        
        
        
        boolean home=false;
        boolean syllabus=false;
        boolean schedule=false;
        boolean hw=false;
        
         JsonArray pages=json.getJsonArray(JSON_PAGES );
        
        for(int i=0;i<pages.size();i++){
            
            if(pages.getJsonObject(i).getString(JSON_NAME).equals("Home")){
                home=true;
            }
            else if(pages.getJsonObject(i).getString(JSON_NAME).equals("Syllabus")){
                syllabus=true;
            }
            else if(pages.getJsonObject(i).getString(JSON_NAME).equals("Schedule")){
                schedule=true;
            }
            else if(pages.getJsonObject(i).getString(JSON_NAME).equals("HWs")){
                hw=true;
            }
        
        }
  //adding json files
    
        File x1=new File("./export/"+json.getString(JSON_BANNER_SUBJECT)+"_"+json.getString(JSON_BANNER_NUMBER)+"_"+json.getString(JSON_BANNER_SMESTER)+"_"+json.getString(JSON_BANNER_YEAR)+"/js/PageData.json");
        x1.createNewFile();
        
        JsonObject logos=json.getJsonObject(JSON_LOGOS);
            String fav=logos.getJsonObject(JSON_LOGOS_FAVICON).getString(JSON_LOGOS_SRC);
            String nav=logos.getJsonObject(JSON_LOGOS_NAVBAR).getString(JSON_LOGOS_SRC);
            String lef=logos.getJsonObject(JSON_LOGOS_LEFTFOOTER).getString(JSON_LOGOS_SRC) ;      
            String rig=logos.getJsonObject(JSON_LOGOS_RIGHTFOOTER).getString(JSON_LOGOS_SRC);
            
        JsonObject instructor=json.getJsonObject(JSON_INS);
        String name=instructor.getString(JSON_INS_NAME);
        String room=instructor.getString(JSON_INS_ROOM);
        String email=instructor.getString(JSON_INS_EMAIL );
        String homepages=instructor.getString(JSON_INS_LINK);
        JsonArray hours=instructor.getJsonArray(JSON_INS_HOURS);
        
        
       
        
        JsonObject dataManagerJSO = Json.createObjectBuilder()
                .add(JSON_BANNER_SUBJECT, json.getString(JSON_BANNER_SUBJECT))
                .add("semester", json.getString(JSON_BANNER_SMESTER))
                .add(JSON_BANNER_NUMBER, json.getString(JSON_BANNER_NUMBER))
                .add(JSON_BANNER_YEAR, json.getString(JSON_BANNER_YEAR))
                .add(JSON_BANNER_TITLE, json.getString(JSON_BANNER_TITLE))
                .add(JSON_LOGOS, Json.createObjectBuilder()
                    .add(JSON_LOGOS_FAVICON, Json.createObjectBuilder().add(JSON_LOGOS_SRC, fav))
                    .add(JSON_LOGOS_NAVBAR, Json.createObjectBuilder().add(JSON_LOGOS_SRC, nav).add(JSON_LOGOS_HREF, "http://www.stonybrook.edu"))
                    .add(JSON_LOGOS_LEFTFOOTER, Json.createObjectBuilder().add(JSON_LOGOS_SRC, lef).add(JSON_LOGOS_HREF, "http://www.cs.stonybrook.edu"))
                    .add(JSON_LOGOS_RIGHTFOOTER, Json.createObjectBuilder().add(JSON_LOGOS_SRC, rig).add(JSON_LOGOS_HREF, "http://www.cs.stonybrook.edu")))
                .add(JSON_INS, Json.createObjectBuilder()
                    .add(JSON_INS_NAME, name)
                    .add(JSON_INS_LINK, homepages)
                    .add(JSON_INS_EMAIL, email)
                    .add(JSON_INS_ROOM, room)
                    .add("photo", json.getJsonObject("instructor").getString("photo"))
                    .add(JSON_INS_HOURS, hours))
                .add(JSON_PAGES, pages)
                .build();

        Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();
        
	// INIT THE WRITER
	OutputStream os = new FileOutputStream(x1.getAbsolutePath());
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(x1.getAbsolutePath());
	pw.write(prettyPrinted);
	pw.close();
        
   
        File x2=new File("./export/"+json.getString(JSON_BANNER_SUBJECT)+"_"+json.getString(JSON_BANNER_NUMBER)+"_"+json.getString(JSON_BANNER_SMESTER)+"_"+json.getString(JSON_BANNER_YEAR)+"/js/ScheduleData.json");
        x2.createNewFile();
        
        JsonArray holiday=json.getJsonArray(JSON_SI_HOLIDAYS );
        JsonArray lecture=json.getJsonArray(JSON_SI_LECTURES  );
        JsonArray reference=json.getJsonArray(JSON_SI_RECITATIONS );
        JsonArray recitation=json.getJsonArray(JSON_SI_REFERENCES);
        JsonArray hws=json.getJsonArray(JSON_SI_HWS);
        
        JsonObject dataManagerJSO1 = Json.createObjectBuilder()
                .add(JSON_SI_STARTMONDAYMONTH, json.getString(JSON_SI_STARTMONDAYMONTH))
                .add(JSON_SI_STARTMONDAYDAY, json.getString(JSON_SI_STARTMONDAYDAY))
                .add(JSON_SI_ENDINGFRIDAYMONTH ,json.getString(JSON_SI_ENDINGFRIDAYMONTH) )
                .add(JSON_SI_ENDINGFRIDAYDAY, json.getString(JSON_SI_ENDINGFRIDAYDAY))
                .add(JSON_SI_HOLIDAYS,  holiday)
                .add(JSON_SI_LECTURES+"s", lecture)
                .add(JSON_SI_RECITATIONS+"s", recitation)
                .add(JSON_SI_REFERENCES, reference)
                .add(JSON_SI_HWS, hws)
                .build();
        
        
        
        
        Map<String, Object> properties1 = new HashMap<>(1);
	properties1.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory1 = Json.createWriterFactory(properties1);
	StringWriter sw1 = new StringWriter();
	JsonWriter jsonWriter1 = writerFactory1.createWriter(sw1);
	jsonWriter1.writeObject(dataManagerJSO1);
	jsonWriter1.close();
        
	// INIT THE WRITER
	OutputStream os1 = new FileOutputStream(x2.getAbsolutePath());
	JsonWriter jsonFileWriter1 = Json.createWriter(os1);
	jsonFileWriter1.writeObject(dataManagerJSO1);
	String prettyPrinted1 = sw1.toString();
	PrintWriter pw1 = new PrintWriter(x2.getAbsolutePath());
	pw1.write(prettyPrinted1);
	pw1.close();
        
        
        
        
        File x3=new File("./export/"+json.getString(JSON_BANNER_SUBJECT)+"_"+json.getString(JSON_BANNER_NUMBER)+"_"+json.getString(JSON_BANNER_SMESTER)+"_"+json.getString(JSON_BANNER_YEAR)+"/js/SectionsData.json");
        
        JsonObject dataManagerJSO2 = Json.createObjectBuilder()
                .add(JSON_MT_LECTURE,json.getJsonArray(JSON_MT_LECTURE))
                .add(JSON_MT_RECITATION, json.getJsonArray(JSON_MT_RECITATION))
                .add(JSON_MT_LABS, json.getJsonArray(JSON_MT_LABS))
                .build();
        
        
        Map<String, Object> properties2 = new HashMap<>(2);
	properties2.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory2 = Json.createWriterFactory(properties2);
	StringWriter sw2 = new StringWriter();
	JsonWriter jsonWriter2 = writerFactory2.createWriter(sw2);
	jsonWriter2.writeObject(dataManagerJSO2);
	jsonWriter2.close();
        
	// INIT THE WRITER
	OutputStream os2 = new FileOutputStream(x3.getAbsolutePath());
	JsonWriter jsonFileWriter2 = Json.createWriter(os2);
	jsonFileWriter2.writeObject(dataManagerJSO2);
	String prettyPrinted2 = sw2.toString();
	PrintWriter pw2 = new PrintWriter(x3.getAbsolutePath());
	pw2.write(prettyPrinted2);
	pw2.close();
        
        File x4=new File("./export/"+json.getString(JSON_BANNER_SUBJECT)+"_"+json.getString(JSON_BANNER_NUMBER)+"_"+json.getString(JSON_BANNER_SMESTER)+"_"+json.getString(JSON_BANNER_YEAR)+"/js/SyllabusData.json");
        String description=json.getString(JSON_SYLLABUS_DES);
        String prerequisites=json.getString(JSON_SYLLABUS_PRE);
        String gn=json.getString(JSON_SYLLABUS_GN);
        String ad=json.getString(JSON_SYLLABUS_AD);
        String sa=json.getString(JSON_SYLLABUS_SA);
        JsonArray topics=json.getJsonArray(JSON_SYLLABUS_TOP);
        JsonArray outcomes=json.getJsonArray(JSON_SYLLABUS_OUT);
        JsonArray textbooks=json.getJsonArray(JSON_SYLLABUS_TEX);
        JsonArray gc=json.getJsonArray(JSON_SYLLABUS_GC);
        
        JsonObject dataManagerJSO3 = Json.createObjectBuilder()
                .add(JSON_SYLLABUS_DES, description)
                .add(JSON_SYLLABUS_TOP, topics)
                .add(JSON_SYLLABUS_PRE , prerequisites)
                .add(JSON_SYLLABUS_OUT, outcomes)
                .add(JSON_SYLLABUS_TEX,textbooks)
                .add(JSON_SYLLABUS_GC, gc )
                .add(JSON_SYLLABUS_GN, gn)
                .add(JSON_SYLLABUS_AD, ad)
                .add(JSON_SYLLABUS_SA, sa)
                .build();
        
        Map<String, Object> properties3 = new HashMap<>(3);
	properties3.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory3 = Json.createWriterFactory(properties3);
	StringWriter sw3 = new StringWriter();
	JsonWriter jsonWriter3 = writerFactory3.createWriter(sw3);
	jsonWriter3.writeObject(dataManagerJSO3);
	jsonWriter3.close();
        
	// INIT THE WRITER
	OutputStream os3 = new FileOutputStream(x4.getAbsolutePath());
	JsonWriter jsonFileWriter3 = Json.createWriter(os3);
	jsonFileWriter3.writeObject(dataManagerJSO3);
	String prettyPrinted3 = sw3.toString();
	PrintWriter pw3 = new PrintWriter(x4.getAbsolutePath());
	pw3.write(prettyPrinted3);
	pw3.close();
        
        
        
        File x5=new File("./export/"+json.getString(JSON_BANNER_SUBJECT)+"_"+json.getString(JSON_BANNER_NUMBER)+"_"+json.getString(JSON_BANNER_SMESTER)+"_"+json.getString(JSON_BANNER_YEAR)+"/js/OfficeHoursData.json");
        
        JsonObject dataManagerJSO4 = Json.createObjectBuilder()
                .add(JSON_START_HOUR, json.getString(JSON_START_HOUR))
		.add(JSON_END_HOUR, json.getString(JSON_END_HOUR))
                .add(JSON_INS, json.getJsonObject(JSON_INS))
                .add(JSON_GRAD_TAS, json.getJsonArray(JSON_GRAD_TAS))
                .add(JSON_UNDERGRAD_TAS,json.getJsonArray(JSON_UNDERGRAD_TAS))
                .add(JSON_OFFICE_HOURS, json.getJsonArray(JSON_OFFICE_HOURS))
                .build();
        
        
        Map<String, Object> properties4 = new HashMap<>(4);
	properties4.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory4 = Json.createWriterFactory(properties4);
	StringWriter sw4 = new StringWriter();
	JsonWriter jsonWriter4 = writerFactory4.createWriter(sw4);
	jsonWriter4.writeObject(dataManagerJSO4);
	jsonWriter4.close();
        
	// INIT THE WRITER
	OutputStream os4 = new FileOutputStream(x5.getAbsolutePath());
	JsonWriter jsonFileWriter4 = Json.createWriter(os4);
	jsonFileWriter4.writeObject(dataManagerJSO4);
	String prettyPrinted4 = sw4.toString();
	PrintWriter pw4 = new PrintWriter(x5.getAbsolutePath());
	pw4.write(prettyPrinted4);
	pw4.close();
        
    //adding css files
    File css=new File("./export/"+json.getString(JSON_BANNER_SUBJECT)+"_"+json.getString(JSON_BANNER_NUMBER)+"_"+json.getString(JSON_BANNER_SMESTER)+"_"+json.getString(JSON_BANNER_YEAR)+"/css/mark_wolf.css");
    css.createNewFile();
    
    File csshome=new File("./export/css/course_homepage_layout.css");
    
    Files.copy(csshome.toPath(), (new File("./export/"+json.getString(JSON_BANNER_SUBJECT)+"_"+json.getString(JSON_BANNER_NUMBER)+"_"+json.getString(JSON_BANNER_SMESTER)+"_"+json.getString(JSON_BANNER_YEAR)+"/css/course_homepage_layout.css")).toPath(),StandardCopyOption.REPLACE_EXISTING);
    
    Files.copy(new File("./export/css/"+json.getString("css")).toPath(), css.toPath(),StandardCopyOption.REPLACE_EXISTING);
    
    
    
    
    
        
    //add html
    if(home){
        File home_html= new File("./export/"+json.getString(JSON_BANNER_SUBJECT)+"_"+json.getString(JSON_BANNER_NUMBER)+"_"+json.getString(JSON_BANNER_SMESTER)+"_"+json.getString(JSON_BANNER_YEAR)+"/index.html");
        home_html.createNewFile();
        Files.copy(new File("./export/index.html").toPath(),home_html.toPath() , StandardCopyOption.REPLACE_EXISTING);
    }
    if(syllabus){
        File syllabus_html= new File("./export/"+json.getString(JSON_BANNER_SUBJECT)+"_"+json.getString(JSON_BANNER_NUMBER)+"_"+json.getString(JSON_BANNER_SMESTER)+"_"+json.getString(JSON_BANNER_YEAR)+"/syllabus.html");
        syllabus_html.createNewFile();
        Files.copy(new File("./export/syllabus.html").toPath(),syllabus_html.toPath() , StandardCopyOption.REPLACE_EXISTING);
    }
    if(schedule){
        File schedule_html= new File("./export/"+json.getString(JSON_BANNER_SUBJECT)+"_"+json.getString(JSON_BANNER_NUMBER)+"_"+json.getString(JSON_BANNER_SMESTER)+"_"+json.getString(JSON_BANNER_YEAR)+"/schedule.html");
        schedule_html.createNewFile();
        Files.copy(new File("./export/schedule.html").toPath(),schedule_html.toPath() , StandardCopyOption.REPLACE_EXISTING);
    }
    if(hw){
        File hws_html= new File("./export/"+json.getString(JSON_BANNER_SUBJECT)+"_"+json.getString(JSON_BANNER_NUMBER)+"_"+json.getString(JSON_BANNER_SMESTER)+"_"+json.getString(JSON_BANNER_YEAR)+"/hws.html");
        hws_html.createNewFile();
        Files.copy(new File("./export/hws.html").toPath(),hws_html.toPath() , StandardCopyOption.REPLACE_EXISTING);
    }
    
        
     // adding js files
     
     File HWsBuilder= new File("./export/js/HWsBuilder.js");//the file
     File HWsBuilder_new= new File("./export/"+json.getString(JSON_BANNER_SUBJECT)+"_"+json.getString(JSON_BANNER_NUMBER)+"_"+json.getString(JSON_BANNER_SMESTER)+"_"+json.getString(JSON_BANNER_YEAR)+"/js/HWsBuilder.js");//the file
     HWsBuilder_new.createNewFile();
     Files.copy(HWsBuilder.toPath(),HWsBuilder_new.toPath(),StandardCopyOption.REPLACE_EXISTING);
     
     File OfficeHoursBuilder= new File("./export/js/OfficeHoursBuilder.js");//the file
     File OfficeHoursBuilder_new= new File("./export/"+json.getString(JSON_BANNER_SUBJECT)+"_"+json.getString(JSON_BANNER_NUMBER)+"_"+json.getString(JSON_BANNER_SMESTER)+"_"+json.getString(JSON_BANNER_YEAR)+"/js/OfficeHoursBuilder.js");//the file
     OfficeHoursBuilder_new.createNewFile();
     Files.copy(OfficeHoursBuilder.toPath(),OfficeHoursBuilder_new.toPath(),StandardCopyOption.REPLACE_EXISTING);
     
     File PagesBuilder= new File("./export/js/PageBuilder.js");//the file
     File PagesBuilder_new= new File("./export/"+json.getString(JSON_BANNER_SUBJECT)+"_"+json.getString(JSON_BANNER_NUMBER)+"_"+json.getString(JSON_BANNER_SMESTER)+"_"+json.getString(JSON_BANNER_YEAR)+"/js/PageBuilder.js");//the file
     PagesBuilder_new.createNewFile();
     Files.copy(PagesBuilder.toPath(),PagesBuilder_new.toPath(),StandardCopyOption.REPLACE_EXISTING);   
     
     
     File ScheduleBuilder= new File("./export/js/ScheduleBuilder.js");//the file
     File ScheduleBuilder_new= new File("./export/"+json.getString(JSON_BANNER_SUBJECT)+"_"+json.getString(JSON_BANNER_NUMBER)+"_"+json.getString(JSON_BANNER_SMESTER)+"_"+json.getString(JSON_BANNER_YEAR)+"/js/ScheduleBuilder.js");//the file
     ScheduleBuilder_new.createNewFile();
     Files.copy(ScheduleBuilder.toPath(),ScheduleBuilder_new.toPath(),StandardCopyOption.REPLACE_EXISTING);
    
     
     File SectionsBuilder= new File("./export/js/SectionsBuilder.js");//the file
     File SectionsBuilder_new= new File("./export/"+json.getString(JSON_BANNER_SUBJECT)+"_"+json.getString(JSON_BANNER_NUMBER)+"_"+json.getString(JSON_BANNER_SMESTER)+"_"+json.getString(JSON_BANNER_YEAR)+"/js/SectionsBuilder.js");//the file
     SectionsBuilder_new.createNewFile();
     Files.copy(SectionsBuilder.toPath(),SectionsBuilder_new.toPath(),StandardCopyOption.REPLACE_EXISTING);
     
     File SyllabusBuilder= new File("./export/js/SyllabusBuilder.js");//the file
     File SyllabusBuilder_new= new File("./export/"+json.getString(JSON_BANNER_SUBJECT)+"_"+json.getString(JSON_BANNER_NUMBER)+"_"+json.getString(JSON_BANNER_SMESTER)+"_"+json.getString(JSON_BANNER_YEAR)+"/js/SyllabusBuilder.js");//the file
     SyllabusBuilder_new.createNewFile();
     Files.copy(SyllabusBuilder.toPath(),SyllabusBuilder_new.toPath(),StandardCopyOption.REPLACE_EXISTING);
   
     File jquerymin= new File("./export/js/jquery.min.js");//the file
     File jquerymin_new= new File("./export/"+json.getString(JSON_BANNER_SUBJECT)+"_"+json.getString(JSON_BANNER_NUMBER)+"_"+json.getString(JSON_BANNER_SMESTER)+"_"+json.getString(JSON_BANNER_YEAR)+"/js/jquery.min.js");//the file
     jquerymin_new.createNewFile();
     Files.copy(jquerymin.toPath(),jquerymin_new.toPath(),StandardCopyOption.REPLACE_EXISTING);
   //photos

     File ins_phtot= new File("./export"+json.getJsonObject(JSON_INS).getString("photo").substring(1));
     File ins_phtot_new= new File("./export/"+json.getString(JSON_BANNER_SUBJECT)+"_"+json.getString(JSON_BANNER_NUMBER)+"_"+json.getString(JSON_BANNER_SMESTER)+"_"+json.getString(JSON_BANNER_YEAR)+json.getJsonObject(JSON_INS).getString("photo").substring(1));
     ins_phtot_new.createNewFile();
     Files.copy(ins_phtot.toPath(),ins_phtot_new.toPath(),StandardCopyOption.REPLACE_EXISTING);
     
     JsonObject logo=json.getJsonObject(JSON_LOGOS);
     
     File FAVICON=new File("./export"+logo.getJsonObject(JSON_LOGOS_FAVICON ).getString(JSON_LOGOS_SRC).substring(1));
     File FAVICON_new=new File("./export/"+json.getString(JSON_BANNER_SUBJECT)+"_"+json.getString(JSON_BANNER_NUMBER)+"_"+json.getString(JSON_BANNER_SMESTER)+"_"+json.getString(JSON_BANNER_YEAR)+logo.getJsonObject(JSON_LOGOS_FAVICON ).getString(JSON_LOGOS_SRC).substring(1));
     FAVICON_new.createNewFile();
     Files.copy(FAVICON.toPath(), FAVICON_new.toPath(), StandardCopyOption.REPLACE_EXISTING);
     
     File NAVBAR=new File("./export"+logo.getJsonObject(JSON_LOGOS_NAVBAR ).getString(JSON_LOGOS_SRC).substring(1));
     File NAVBAR_new=new File("./export/"+json.getString(JSON_BANNER_SUBJECT)+"_"+json.getString(JSON_BANNER_NUMBER)+"_"+json.getString(JSON_BANNER_SMESTER)+"_"+json.getString(JSON_BANNER_YEAR)+logo.getJsonObject(JSON_LOGOS_NAVBAR ).getString(JSON_LOGOS_SRC).substring(1));
     NAVBAR_new.createNewFile();
     Files.copy(NAVBAR.toPath(), NAVBAR_new.toPath(), StandardCopyOption.REPLACE_EXISTING);
     
      File LEFTFOOTER=new File("./export"+logo.getJsonObject(JSON_LOGOS_LEFTFOOTER ).getString(JSON_LOGOS_SRC).substring(1));
     File LEFTFOOTER_new=new File("./export/"+json.getString(JSON_BANNER_SUBJECT)+"_"+json.getString(JSON_BANNER_NUMBER)+"_"+json.getString(JSON_BANNER_SMESTER)+"_"+json.getString(JSON_BANNER_YEAR)+logo.getJsonObject(JSON_LOGOS_LEFTFOOTER ).getString(JSON_LOGOS_SRC).substring(1));
     LEFTFOOTER_new.createNewFile();
     Files.copy(LEFTFOOTER.toPath(), LEFTFOOTER_new.toPath(), StandardCopyOption.REPLACE_EXISTING);
     
     File RIGHTFOOTER=new File("./export"+logo.getJsonObject(JSON_LOGOS_RIGHTFOOTER ).getString(JSON_LOGOS_SRC).substring(1));
     File RIGHTFOOTER_new=new File("./export/"+json.getString(JSON_BANNER_SUBJECT)+"_"+json.getString(JSON_BANNER_NUMBER)+"_"+json.getString(JSON_BANNER_SMESTER)+"_"+json.getString(JSON_BANNER_YEAR)+logo.getJsonObject(JSON_LOGOS_RIGHTFOOTER ).getString(JSON_LOGOS_SRC).substring(1));
     RIGHTFOOTER_new.createNewFile();
     Files.copy(RIGHTFOOTER.toPath(), RIGHTFOOTER_new.toPath(), StandardCopyOption.REPLACE_EXISTING);
     
     
     File txt_rs=new File("./export/images/HeadFirstDesignPatterns.gif");
     File txt_rs_new=new File("./export/"+json.getString(JSON_BANNER_SUBJECT)+"_"+json.getString(JSON_BANNER_NUMBER)+"_"+json.getString(JSON_BANNER_SMESTER)+"_"+json.getString(JSON_BANNER_YEAR)+"/images/HeadFirstDesignPatterns.gif");
     txt_rs_new.createNewFile();
     Files.copy(txt_rs.toPath(), txt_rs_new.toPath(),StandardCopyOption.REPLACE_EXISTING);
     
     File txt_rs1=new File("./export/images/HeadFirstOOAAD.jpg");
     File txt_rs1_new=new File("./export/"+json.getString(JSON_BANNER_SUBJECT)+"_"+json.getString(JSON_BANNER_NUMBER)+"_"+json.getString(JSON_BANNER_SMESTER)+"_"+json.getString(JSON_BANNER_YEAR)+"/images/HeadFirstOOAAD.jpg");
     txt_rs1_new.createNewFile();
     Files.copy(txt_rs1.toPath(), txt_rs1_new.toPath(),StandardCopyOption.REPLACE_EXISTING);
     
        
    }
    
    
    


}