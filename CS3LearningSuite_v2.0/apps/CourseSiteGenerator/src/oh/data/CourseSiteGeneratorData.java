package oh.data;

import static djf.AppPropertyType.EXPORT_BUTTON;
import javafx.collections.ObservableList;
import djf.components.AppDataComponent;
import djf.modules.AppGUIModule;
import static djf.modules.AppGUIModule.ENABLED;
import djf.ui.AppNodesBuilder;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import oh.CourseSiteGeneratorApp;
import static oh.CourseSiteGeneratorPropertyType.MT_LAB_TABLEVIEW;
import static oh.CourseSiteGeneratorPropertyType.MT_LECTURE_TABLEVIEW;
import static oh.CourseSiteGeneratorPropertyType.MT_RECITATION_TABLEVIEW;
import static oh.CourseSiteGeneratorPropertyType.OH_ALL_RADIO_BUTTON;
import static oh.CourseSiteGeneratorPropertyType.OH_GRAD_RADIO_BUTTON;
import static oh.CourseSiteGeneratorPropertyType.OH_OFFICE_HOURS_END_COMBO;
import static oh.CourseSiteGeneratorPropertyType.OH_OFFICE_HOURS_START_COMBO;
import static oh.CourseSiteGeneratorPropertyType.OH_OFFICE_HOURS_TABLE_VIEW;
import static oh.CourseSiteGeneratorPropertyType.OH_TAS_TABLE_VIEW;
import static oh.CourseSiteGeneratorPropertyType.SCHEDULE_AE_ADD_UPDATE_BUTTON;
import static oh.CourseSiteGeneratorPropertyType.SCHEDULE_AE_DATE_COMBO;
import static oh.CourseSiteGeneratorPropertyType.SCHEDULE_AE_LINK_TEXTFIELD;
import static oh.CourseSiteGeneratorPropertyType.SCHEDULE_AE_TITLE_TEXTFIELD;
import static oh.CourseSiteGeneratorPropertyType.SCHEDULE_AE_TOPIC_TEXTFIELD;
import static oh.CourseSiteGeneratorPropertyType.SCHEDULE_AE_TYPE_COMBO;
import static oh.CourseSiteGeneratorPropertyType.SCHEDULE_CALENDAR_ENDFRIDAY_COMBO;
import static oh.CourseSiteGeneratorPropertyType.SCHEDULE_CALENDAR_STARTMONDAY_COMBO;
import static oh.CourseSiteGeneratorPropertyType.SCHEDULE_ITEM_TABLEVIEW;
import static oh.CourseSiteGeneratorPropertyType.SITE_BANNER_EXPORTDIR;
import static oh.CourseSiteGeneratorPropertyType.SITE_BANNER_NUMBER_COMBO;
import static oh.CourseSiteGeneratorPropertyType.SITE_BANNER_SMESTER_COMBO;
import static oh.CourseSiteGeneratorPropertyType.SITE_BANNER_SUBJECT_COMBO;
import static oh.CourseSiteGeneratorPropertyType.SITE_BANNER_TITLE_TEXTFIELD;
import static oh.CourseSiteGeneratorPropertyType.SITE_BANNER_YEAR_COMBO;
import static oh.CourseSiteGeneratorPropertyType.SITE_INSTRUCTOR;
import static oh.CourseSiteGeneratorPropertyType.SITE_INSTRUCTOR_EMAIL_TEXTFIELD;
import static oh.CourseSiteGeneratorPropertyType.SITE_INSTRUCTOR_HOMEPAGE_TEXTFIELD;
import static oh.CourseSiteGeneratorPropertyType.SITE_INSTRUCTOR_NAME_TEXTFIELD;
import static oh.CourseSiteGeneratorPropertyType.SITE_INSTRUCTOR_OH_BUTTON;
import static oh.CourseSiteGeneratorPropertyType.SITE_INSTRUCTOR_OH_TEXTAREA;
import static oh.CourseSiteGeneratorPropertyType.SITE_INSTRUCTOR_ROOM_TEXTFIELD;
import static oh.CourseSiteGeneratorPropertyType.SITE_PAGES_HOME_CHECKBOX;
import static oh.CourseSiteGeneratorPropertyType.SITE_PAGES_HW_CHECKBOX;
import static oh.CourseSiteGeneratorPropertyType.SITE_PAGES_SCHEDULE_CHECKBOX;
import static oh.CourseSiteGeneratorPropertyType.SITE_PAGES_SYLLABUS_CHECKBOX;
import static oh.CourseSiteGeneratorPropertyType.SITE_STYLE_FAVICON_IMAGE;
import static oh.CourseSiteGeneratorPropertyType.SITE_STYLE_LEFTFOOTER_IMAGE;
import static oh.CourseSiteGeneratorPropertyType.SITE_STYLE_NAVBAR_IMAGE;
import static oh.CourseSiteGeneratorPropertyType.SITE_STYLE_RIGHTFOOTER_IMAGE;
import static oh.CourseSiteGeneratorPropertyType.SITE_STYLE_SHEET_COMBO;
import static oh.CourseSiteGeneratorPropertyType.SYLLABUS_AD_TEXTFIELD;
import static oh.CourseSiteGeneratorPropertyType.SYLLABUS_DESCRIPTION_TEXTFIELD;
import static oh.CourseSiteGeneratorPropertyType.SYLLABUS_GC_TEXTFIELD;
import static oh.CourseSiteGeneratorPropertyType.SYLLABUS_GN_TEXTFIELD;
import static oh.CourseSiteGeneratorPropertyType.SYLLABUS_OUTCOMES_TEXTFIELD;
import static oh.CourseSiteGeneratorPropertyType.SYLLABUS_PREREQUISITES_TEXTFIELD;
import static oh.CourseSiteGeneratorPropertyType.SYLLABUS_SA_TEXTFIELD;
import static oh.CourseSiteGeneratorPropertyType.SYLLABUS_TEXTBOOKS_TEXTFIELD;
import static oh.CourseSiteGeneratorPropertyType.SYLLABUS_TOPICS_TEXTFIELD;
import static oh.CourseSiteGeneratorPropertyType.SYLLABUS_VBOXES1;
import static oh.CourseSiteGeneratorPropertyType.SYLLABUS_VBOXES2;
import static oh.CourseSiteGeneratorPropertyType.SYLLABUS_VBOXES3;
import static oh.CourseSiteGeneratorPropertyType.SYLLABUS_VBOXES4;
import static oh.CourseSiteGeneratorPropertyType.SYLLABUS_VBOXES5;
import static oh.CourseSiteGeneratorPropertyType.SYLLABUS_VBOXES6;
import static oh.CourseSiteGeneratorPropertyType.SYLLABUS_VBOXES7;
import static oh.CourseSiteGeneratorPropertyType.SYLLABUS_VBOXES8;
import static oh.CourseSiteGeneratorPropertyType.SYLLABUS_VBOXES9;
import oh.data.TimeSlot.DayOfWeek;
import static oh.workspace.style.OHStyle.CLASS_SITE_INSTRUCTOR;
import static oh.workspace.style.OHStyle.CLASS_SITE_TEXTAREA;

/**
 * This is the data component for TAManagerApp. It has all the data needed
 * to be set by the user via the User Interface and file I/O can set and get
 * all the data from this object
 * 
 * @author Richard McKenna
 */
public class CourseSiteGeneratorData implements AppDataComponent {
//officehours
    // WE'LL NEED ACCESS TO THE APP TO NOTIFY THE GUI WHEN DATA CHANGES
    CourseSiteGeneratorApp app;
    
    // THESE ARE ALL THE TEACHING ASSISTANTS
    HashMap<TAType, ArrayList<TeachingAssistantPrototype>> allTAs;

    // NOTE THAT THIS DATA STRUCTURE WILL DIRECTLY STORE THE
    // DATA IN THE ROWS OF THE TABLE VIEW
    ObservableList<TeachingAssistantPrototype> teachingAssistants;
    ObservableList<TimeSlot> officeHours;
    ArrayList<TimeSlot> oh;    

    // THESE ARE THE TIME BOUNDS FOR THE OFFICE HOURS GRID. NOTE
    // THAT THESE VALUES CAN BE DIFFERENT FOR DIFFERENT FILES, BUT
    // THAT OUR APPLICATION USES THE DEFAULT TIME VALUES AND PROVIDES
    // NO MEANS FOR CHANGING THESE VALUES
    int startHour;
    int endHour;
    
    // DEFAULT VALUES FOR START AND END HOURS IN MILITARY HOURS
    public static final int MIN_START_HOUR = 9;
    public static final int MAX_END_HOUR = 20;

    /**
     * This constructor will setup the required data structures for
     * use, but will have to wait on the office hours grid, since
     * it receives the StringProperty objects from the Workspace.
     * 
     * @param initApp The application this data manager belongs to. 
     */
    public CourseSiteGeneratorData(CourseSiteGeneratorApp initApp) {
        // KEEP THIS FOR LATER
        app = initApp;
        AppGUIModule gui = app.getGUIModule();
        
        

        // SETUP THE DATA STRUCTURES
        allTAs = new HashMap();
        allTAs.put(TAType.Graduate, new ArrayList());
        allTAs.put(TAType.Undergraduate, new ArrayList());
        
        // GET THE LIST OF TAs FOR THE LEFT TABLE
        TableView<TeachingAssistantPrototype> taTableView = (TableView)gui.getGUINode(OH_TAS_TABLE_VIEW);
        teachingAssistants = taTableView.getItems();

        // THESE ARE THE DEFAULT OFFICE HOURS
        startHour = MIN_START_HOUR;
        endHour = MAX_END_HOUR;
        
        resetOfficeHours();
        
        //init site
        
         banner_subject_com=(ComboBox)gui.getGUINode(SITE_BANNER_SUBJECT_COMBO);
         banner_number_com=(ComboBox)gui.getGUINode(SITE_BANNER_NUMBER_COMBO);
         banner_smester_com=(ComboBox)gui.getGUINode(SITE_BANNER_SMESTER_COMBO);
         banner_year_com=(ComboBox)gui.getGUINode(SITE_BANNER_YEAR_COMBO);
         exportdir_label=(Label)gui.getGUINode(SITE_BANNER_EXPORTDIR);
         banner_title_textfield=(TextField)gui.getGUINode(SITE_BANNER_TITLE_TEXTFIELD);
         //banner_subject_com.getValue().toString()
        
        setBanner("CSE",banner_number_com.getValue().toString(), banner_smester_com.getValue().toString(),banner_year_com.getValue().toString(), banner_title_textfield.getText());
        
        ins_name_textfield=(TextField)gui.getGUINode( SITE_INSTRUCTOR_NAME_TEXTFIELD);
        ins_email_textfield=(TextField)gui.getGUINode( SITE_INSTRUCTOR_EMAIL_TEXTFIELD);
        ins_room_textfield=(TextField)gui.getGUINode( SITE_INSTRUCTOR_ROOM_TEXTFIELD);
        ins_homepage_textfield=(TextField)gui.getGUINode( SITE_INSTRUCTOR_HOMEPAGE_TEXTFIELD);
        
        setInstructor(ins_name_textfield.getText(), ins_email_textfield.getText(), ins_room_textfield.getText(), ins_homepage_textfield.getText());
        
        imageview_f=(ImageView)gui.getGUINode(SITE_STYLE_FAVICON_IMAGE);
        imageview_n=(ImageView)gui.getGUINode(SITE_STYLE_NAVBAR_IMAGE);
        imageview_l=(ImageView)gui.getGUINode(SITE_STYLE_LEFTFOOTER_IMAGE);
        imageview_r=(ImageView)gui.getGUINode(SITE_STYLE_RIGHTFOOTER_IMAGE);
        
        setPages(true,true,true,true);
        this.setStyle("./images/images.jpg", "./images/SBUDarkRedShieldLogo.png","./images/SBUWhiteShieldLogo.jpg","./images/SBUCSLogo.png");
        
        ins_textArea=(TextArea)gui.getGUINode(SITE_INSTRUCTOR_OH_TEXTAREA);
        GridPane instructor=(GridPane)gui.getGUINode(SITE_INSTRUCTOR);
        instructor.getChildren().remove(ins_textArea);
        
        this.setTextArea_ins("[\n" +
"            { \"day\": \"Tuesday\",     \"time\": \"1:00pm-2:20pm\"    },\n" +
"            { \"day\": \"Wednesday\",   \"time\": \"2pm-3pm\"       },\n" +
"            { \"day\": \"Thursday\",      \"time\": \"1:00pm-2:20pm\"    }\n" +
"        ]");
        
        
        //init syllabus
        
        VBox VBox_Desciprtion=(VBox) gui.getGUINode(SYLLABUS_VBOXES1);
        VBox VBox_Topic=(VBox) gui.getGUINode(SYLLABUS_VBOXES2);
        VBox VBox_Prerequisite=(VBox) gui.getGUINode(SYLLABUS_VBOXES3);
        VBox VBox_OutComes=(VBox) gui.getGUINode(SYLLABUS_VBOXES4);
        VBox VBox_Textbook=(VBox) gui.getGUINode(SYLLABUS_VBOXES5);
        VBox VBox_GC=(VBox) gui.getGUINode(SYLLABUS_VBOXES6);
        VBox VBox_GN=(VBox) gui.getGUINode(SYLLABUS_VBOXES7);
        VBox VBox_AD=(VBox) gui.getGUINode(SYLLABUS_VBOXES8);
        VBox VBox_SA=(VBox) gui.getGUINode(SYLLABUS_VBOXES9);
        
        
          tx1=(TextArea)gui.getGUINode(SYLLABUS_DESCRIPTION_TEXTFIELD);
          tx2=(TextArea)gui.getGUINode(SYLLABUS_TOPICS_TEXTFIELD);
          tx3=(TextArea)gui.getGUINode(SYLLABUS_PREREQUISITES_TEXTFIELD);
          tx4=(TextArea)gui.getGUINode(SYLLABUS_OUTCOMES_TEXTFIELD);
          tx5=(TextArea)gui.getGUINode(SYLLABUS_TEXTBOOKS_TEXTFIELD);
          tx6=(TextArea)gui.getGUINode(SYLLABUS_GC_TEXTFIELD);
          tx7=(TextArea)gui.getGUINode(SYLLABUS_GN_TEXTFIELD);
          tx8=(TextArea)gui.getGUINode(SYLLABUS_AD_TEXTFIELD);
          tx9=(TextArea)gui.getGUINode(SYLLABUS_SA_TEXTFIELD);
          
        VBox_Desciprtion.getChildren().remove(tx1);
        VBox_Topic.getChildren().remove(tx2);
        VBox_Prerequisite.getChildren().remove(tx3);
        VBox_OutComes.getChildren().remove(tx4);
        VBox_Textbook.getChildren().remove(tx5);
        VBox_GC.getChildren().remove(tx6);
        VBox_GN.getChildren().remove(tx7);
        VBox_AD.getChildren().remove(tx8);
        VBox_SA.getChildren().remove(tx9);
        
          txt1="Development of the basic concepts and techniques from Computer Science I and II into practical programming skills that include a systematic approach to program design, coding, testing, and debugging. Application of these skills to the construction of robust programs of thousands of lines of source code. Use of programming environments and tools to aid in the software development process.";
          txt2="[\n" +
"        \"Programming style and its impact on readability, reliability, maintainability, and portability.\",\n" +
"        \"Decomposing problems into modular designs with simple, narrow interfaces.\",\n" +
"        \"Determining the proper objects in an object-oriented design.\",\n" +
"        \"Selecting appropriate algorithms and data structures.\",\n" +
"        \"Reusing code, including external libraries designed and built by others.\",\n" +
"        \"Learning systematic testing and debugging techniques.\",\n" +
"        \"Maintaining a repository of code during incremental development of a software project.\",\n" +
"        \"Learning how to use threads to synchronize several tasks.\",\n" +
"        \"Improving program performance.\",\n" +
"        \"Making effective use of a programming environment, including:<br /><ul><li>Syntax-directed editor</li><li>Build tools</li><li>Debugging tools</li><li>Testing tools</li><li>Source code management tools</li><li>Profiling tools</li></ul>\"\n" +
"    ]";
          txt3="You must have taken CSE 214 and received a grade of 'C' or better in order to take this course. In more detail, you are expected to have the following knowledge and skills at the beginning of the course:<br /><ul><li>Ability to write programs of a few hundred lines of code in the Java programming language.</li><li>Understanding of fundamental data structures, including lists, binary trees, hash tables, and graphs, and the ability to employ these data structures in the form provided by the standard Java API.</li><li>Ability to construct simple command-based user interfaces, and to use files for the input and output of data.</li><li>Mastery of basic mathematical and geometric reasoning using pre-calculus concepts.</li></ul>";
          txt4="[\n" +
"        \"Ability to systematically design, code, debug, and test programs of about two thousand lines of code.\",\n" +
"        \"Sensitivity to the issues of programming style and modularity and their relationship to the construction and evolution of robust software.\",\n" +
"        \"Knowledge of basic ideas and techniques of object-oriented programming.\",\n" +
"        \"Familiarity with the capabilities and use of programming tools such as syntax-directed editors, debuggers, execution profilers, documentation generators, and revison-control systems.\"\n" +
"    ]";
          txt5="[\n" +
"        {\n" +
"            \"title\": \"Head First Object Oriented Analysis and Design\",\n" +
"            \"link\": \"http://proquestcombo.safaribooksonline.com.proxy.library.stonybrook.edu/0596008678\",\n" +
"            \"photo\": \"./images/HeadFirstOOAAD.jpg\",\n" +
"            \"authors\": [\n" +
"                \"Brett McLaughlin\", \"Gary Pollice\", \"David West\"\n" +
"            ],\n" +
"            \"publisher\": \"O'Reilly Media, Inc.\",\n" +
"            \"year\": \"2006\"\n" +
"        },\n" +
"        {\n" +
"            \"title\": \"Head First Design Patterns\",\n" +
"            \"link\": \"http://proquestcombo.safaribooksonline.com.proxy.library.stonybrook.edu/0596007124\",\n" +
"            \"photo\": \"./images/HeadFirstDesignPatterns.gif\",\n" +
"            \"authors\": [\n" +
"                \"Eric T Freeman\", \"Elisabeth Robson\", \"Bert Bates\", \"Kathy Sierra\"\n" +
"            ],\n" +
"            \"publisher\": \"O'Reilly Media, Inc.\",\n" +
"            \"year\": \"2004\"\n" +
"        }\n" +
"    ]";
          txt6=" [\n" +
"        {\n" +
"            \"name\": \"Recitations\",\n" +
"            \"description\": \"Students will attend weekly recitations that will introduce use of essential development tools and will require completion of an exercise for submission.\",\n" +
"            \"weight\": \"10\"\n" +
"        },\n" +
"        {\n" +
"            \"name\": \"Homework Assignments\",\n" +
"            \"description\": \"The assignments will develop a students ability to design and implement object-oriented systems. Grading will be based on functionality and proper use specific tools. Submitted code that does not compile will receive no credit. Late submissions will NOT be accepted. Programming assignments will be handed in electronically, instructions for which will be provided early in the semester.\",\n" +
"            \"weight\": \"20\"\n" +
"        },\n" +
"        {\n" +
"            \"name\": \"Final Project\",\n" +
"            \"description\": \"The assignments will build to the final project, which will be a fully functioning application.\",\n" +
"            \"weight\": \"20\"\n" +
"        },\n" +
"        {\n" +
"            \"name\": \"Midterm Exam\",\n" +
"            \"description\": \"The midterm will cover all lecture, quizzes, and homework materials covered during the first 1/2 of the semester.\",\n" +
"            \"weight\": \"20\"\n" +
"        },\n" +
"        {\n" +
"            \"name\": \"Final Exam\",\n" +
"            \"description\": \"The final will be cumulative and will cover all lecture, reading, and homework material.\",\n" +
"            \"weight\": \"30\"\n" +
"        }\n" +
"    ]";
          txt7="<strong>Note CEAS Policy:</strong> The Pass/No Credit (P/NC) option is not available for this course.";
          txt8="You may <em>discuss</em> the homework in this course with anyone you like, however each student's submissionmust be ones own work.<br /><br />The College of Engineering and Applied Sciences regards academic dishonesty as a very serious matter, and provides for substantial penalties in such cases. For more information, obtain a copy of the CEAS guidelines on academic dishonesty from the CEAS office.<br /><br /><strong>Be advised that any evidence of academic dishonesty will be treated with utmost seriousness. If you have a situation that may tempt you into doing something academically dishonest, resist the urge and speak with your instructor during office hours for help.</strong><br /><br />";
          txt9="If you have a physical, psychological, medical or learning disability that may impact on your ability to carry out assigned course work, I would urge that you contact the staff at the <a href='https://www.stonybrook.edu/dss/'>Student Accessibility Support Center</a> (SASC) in the ECC building, 632-6748. SASC will review your concerns and determine with you what accommodations are necessary and appropriate. All information and documentation of disability are confidential.<br /><br /><br /><br />";
          
          tx1.setText(txt1);
          tx2.setText(txt2);
          tx3.setText(txt3);
          tx4.setText(txt4);
          tx5.setText(txt5);
          tx6.setText(txt6);
          tx7.setText(txt7);
          tx8.setText(txt8);
          tx9.setText(txt9);
          
          convertSyllabusTopic(txt2);
          convertOutComes(txt4);
          convertGradComp(txt6);
          convertTextbook(txt5);
      //meeting time
      
      lectureTV=(TableView) gui.getGUINode(MT_LECTURE_TABLEVIEW);
      recTV=(TableView) gui.getGUINode(MT_RECITATION_TABLEVIEW);
      labTV=(TableView) gui.getGUINode(MT_LAB_TABLEVIEW);
      
       
      lectures=lectureTV.getItems();
      recitation=recTV.getItems();
      labs=labTV.getItems();
      
      
      //schedule
      start=(DatePicker) gui.getGUINode(SCHEDULE_CALENDAR_STARTMONDAY_COMBO);
      end=(DatePicker) gui.getGUINode(SCHEDULE_CALENDAR_ENDFRIDAY_COMBO);
     
      
      
      title=(TextField)gui.getGUINode(SCHEDULE_AE_TITLE_TEXTFIELD);
      topic=(TextField)gui.getGUINode(SCHEDULE_AE_TOPIC_TEXTFIELD);
      link=(TextField)gui.getGUINode( SCHEDULE_AE_LINK_TEXTFIELD);
      date=(DatePicker)gui.getGUINode(SCHEDULE_AE_DATE_COMBO);
      type=(ComboBox)gui.getGUINode(SCHEDULE_AE_TYPE_COMBO);
    
      
      title_text=title.getText();
      topic_text=topic.getText();
      link_text=link.getText();
      type_text=type.getValue().toString();
      datepicker=date.getValue();
      datepicker_start=start.getValue();
      datepicker_end=end.getValue();
      
      
      SItable=((TableView)gui.getGUINode(SCHEDULE_ITEM_TABLEVIEW)).getItems();
      SItableview=(TableView)gui.getGUINode(SCHEDULE_ITEM_TABLEVIEW);
      
      xxxxx=(Button)gui.getGUINode(SCHEDULE_AE_ADD_UPDATE_BUTTON);
      css=(ComboBox)gui.getGUINode(SITE_STYLE_SHEET_COMBO);
      css_string=css.getValue().toString();
    }
    
    // ACCESSOR METHODS

    public int getStartHour() {
        return startHour;
    }

    public int getEndHour() {
        return endHour;
    }
    
    // PRIVATE HELPER METHODS
    
    private void sortTAs() {
        Collections.sort(teachingAssistants);
    }
    
    private void resetOfficeHours() {
        //THIS WILL STORE OUR OFFICE HOURS
        AppGUIModule gui = app.getGUIModule();
        
        TableView<TimeSlot> officeHoursTableView = (TableView)gui.getGUINode(OH_OFFICE_HOURS_TABLE_VIEW);
        officeHours = officeHoursTableView.getItems(); 
        officeHours.clear();
        oh=new ArrayList();
        for (int i = startHour; i <= endHour; i++) {
            TimeSlot timeSlot = new TimeSlot(   this.getTimeString(i, true),
                                                this.getTimeString(i, false));
            officeHours.add(timeSlot);
            oh.add(timeSlot);
            TimeSlot halfTimeSlot = new TimeSlot(   this.getTimeString(i, false),
                                                    this.getTimeString(i+1, true));
            officeHours.add(halfTimeSlot);
            oh.add(halfTimeSlot);
        }
    }
    
    private String getTimeString(int militaryHour, boolean onHour) {
        String minutesText = "00";
        if (!onHour) {
            minutesText = "30";
        }

        // FIRST THE START AND END CELLS
        int hour = militaryHour;
        if (hour > 12) {
            hour -= 12;
        }
        String cellText = "" + hour + ":" + minutesText;
        if (militaryHour < 12) {
            cellText += "am";
        } else {
            cellText += "pm";
        }
        return cellText;
    }
    
    // METHODS TO OVERRIDE
        
    /**
     * Called each time new work is created or loaded, it resets all data
     * and data structures such that they can be used for new values.
     */
    @Override
    public void reset() {
        
        startHour = MIN_START_HOUR;
        endHour = MAX_END_HOUR;
        teachingAssistants.clear();
        allTAs.get(TAType.Undergraduate).clear();
        allTAs.get(TAType.Graduate).clear();
        for (TimeSlot timeSlot : officeHours) {
            timeSlot.reset();
        }
        this.setBanner("CSE", "219", "Fall", "2018", "Computer Science III");
        this.setPages(true, true, true, true);
        this.setStyle("./images/images.jpg", "./images/SBUDarkRedShieldLogo.png","./images/SBUWhiteShieldLogo.jpg","./images/SBUCSLogo.png");
        this.setInstructor("Richard McKenna", "Richard.Mckenna@stonybrook.edu", "New CS 216", "http://www.cs.stonybrook.edu/~richard");
        this.setTextArea_ins("[\n" +
"            { \"day\": \"Tuesday\",     \"time\": \"1:00pm-2:20pm\"    },\n" +
"            { \"day\": \"Wednesday\",   \"time\": \"2pm-3pm\"       },\n" +
"            { \"day\": \"Thursday\",      \"time\": \"1:00pm-2:20pm\"    }\n" +
"        ]");
        
        txt1="Development of the basic concepts and techniques from Computer Science I and II into practical programming skills that include a systematic approach to program design, coding, testing, and debugging. Application of these skills to the construction of robust programs of thousands of lines of source code. Use of programming environments and tools to aid in the software development process.";
          txt2="[\n" +
"        \"Programming style and its impact on readability, reliability, maintainability, and portability.\",\n" +
"        \"Decomposing problems into modular designs with simple, narrow interfaces.\",\n" +
"        \"Determining the proper objects in an object-oriented design.\",\n" +
"        \"Selecting appropriate algorithms and data structures.\",\n" +
"        \"Reusing code, including external libraries designed and built by others.\",\n" +
"        \"Learning systematic testing and debugging techniques.\",\n" +
"        \"Maintaining a repository of code during incremental development of a software project.\",\n" +
"        \"Learning how to use threads to synchronize several tasks.\",\n" +
"        \"Improving program performance.\",\n" +
"        \"Making effective use of a programming environment, including:<br /><ul><li>Syntax-directed editor</li><li>Build tools</li><li>Debugging tools</li><li>Testing tools</li><li>Source code management tools</li><li>Profiling tools</li></ul>\"\n" +
"    ]";
          txt3="You must have taken CSE 214 and received a grade of 'C' or better in order to take this course. In more detail, you are expected to have the following knowledge and skills at the beginning of the course:<br /><ul><li>Ability to write programs of a few hundred lines of code in the Java programming language.</li><li>Understanding of fundamental data structures, including lists, binary trees, hash tables, and graphs, and the ability to employ these data structures in the form provided by the standard Java API.</li><li>Ability to construct simple command-based user interfaces, and to use files for the input and output of data.</li><li>Mastery of basic mathematical and geometric reasoning using pre-calculus concepts.</li></ul>";
          txt4="[\n" +
"        \"Ability to systematically design, code, debug, and test programs of about two thousand lines of code.\",\n" +
"        \"Sensitivity to the issues of programming style and modularity and their relationship to the construction and evolution of robust software.\",\n" +
"        \"Knowledge of basic ideas and techniques of object-oriented programming.\",\n" +
"        \"Familiarity with the capabilities and use of programming tools such as syntax-directed editors, debuggers, execution profilers, documentation generators, and revison-control systems.\"\n" +
"    ]";
          txt5="[\n" +
"        {\n" +
"            \"title\": \"Head First Object Oriented Analysis and Design\",\n" +
"            \"link\": \"http://proquestcombo.safaribooksonline.com.proxy.library.stonybrook.edu/0596008678\",\n" +
"            \"photo\": \"./images/HeadFirstOOAAD.jpg\",\n" +
"            \"authors\": [\n" +
"                \"Brett McLaughlin\", \"Gary Pollice\", \"David West\"\n" +
"            ],\n" +
"            \"publisher\": \"O'Reilly Media, Inc.\",\n" +
"            \"year\": \"2006\"\n" +
"        },\n" +
"        {\n" +
"            \"title\": \"Head First Design Patterns\",\n" +
"            \"link\": \"http://proquestcombo.safaribooksonline.com.proxy.library.stonybrook.edu/0596007124\",\n" +
"            \"photo\": \"./images/HeadFirstDesignPatterns.gif\",\n" +
"            \"authors\": [\n" +
"                \"Eric T Freeman\", \"Elisabeth Robson\", \"Bert Bates\", \"Kathy Sierra\"\n" +
"            ],\n" +
"            \"publisher\": \"O'Reilly Media, Inc.\",\n" +
"            \"year\": \"2004\"\n" +
"        }\n" +
"    ]";
          txt6=" [\n" +
"        {\n" +
"            \"name\": \"Recitations\",\n" +
"            \"description\": \"Students will attend weekly recitations that will introduce use of essential development tools and will require completion of an exercise for submission.\",\n" +
"            \"weight\": \"10\"\n" +
"        },\n" +
"        {\n" +
"            \"name\": \"Homework Assignments\",\n" +
"            \"description\": \"The assignments will develop a students ability to design and implement object-oriented systems. Grading will be based on functionality and proper use specific tools. Submitted code that does not compile will receive no credit. Late submissions will NOT be accepted. Programming assignments will be handed in electronically, instructions for which will be provided early in the semester.\",\n" +
"            \"weight\": \"20\"\n" +
"        },\n" +
"        {\n" +
"            \"name\": \"Final Project\",\n" +
"            \"description\": \"The assignments will build to the final project, which will be a fully functioning application.\",\n" +
"            \"weight\": \"20\"\n" +
"        },\n" +
"        {\n" +
"            \"name\": \"Midterm Exam\",\n" +
"            \"description\": \"The midterm will cover all lecture, quizzes, and homework materials covered during the first 1/2 of the semester.\",\n" +
"            \"weight\": \"20\"\n" +
"        },\n" +
"        {\n" +
"            \"name\": \"Final Exam\",\n" +
"            \"description\": \"The final will be cumulative and will cover all lecture, reading, and homework material.\",\n" +
"            \"weight\": \"30\"\n" +
"        }\n" +
"    ]";
          txt7="<strong>Note CEAS Policy:</strong> The Pass/No Credit (P/NC) option is not available for this course.";
          txt8="You may <em>discuss</em> the homework in this course with anyone you like, however each student's submissionmust be ones own work.<br /><br />The College of Engineering and Applied Sciences regards academic dishonesty as a very serious matter, and provides for substantial penalties in such cases. For more information, obtain a copy of the CEAS guidelines on academic dishonesty from the CEAS office.<br /><br /><strong>Be advised that any evidence of academic dishonesty will be treated with utmost seriousness. If you have a situation that may tempt you into doing something academically dishonest, resist the urge and speak with your instructor during office hours for help.</strong><br /><br />";
          txt9="If you have a physical, psychological, medical or learning disability that may impact on your ability to carry out assigned course work, I would urge that you contact the staff at the <a href='https://www.stonybrook.edu/dss/'>Student Accessibility Support Center</a> (SASC) in the ECC building, 632-6748. SASC will review your concerns and determine with you what accommodations are necessary and appropriate. All information and documentation of disability are confidential.<br /><br /><br /><br />";
          
          tx1.setText(txt1);
          tx2.setText(txt2);
          tx3.setText(txt3);
          tx4.setText(txt4);
          tx5.setText(txt5);
          tx6.setText(txt6);
          tx7.setText(txt7);
          tx8.setText(txt8);
          tx9.setText(txt9);
          
          convertSyllabusTopic(txt2);
          convertOutComes(txt4);
          convertGradComp(txt6);
          convertTextbook(txt5);
          
          
           lectures.clear();
           recitation.clear();
           labs.clear();
          
           
           SItable.clear();
          
           this.setScheduleItemsText_clear("", "", "", "holidays");
           xxxxx.setText("Add");
           
           css.setValue("mark_wolf.css");
           css_string=css.getValue().toString();
        
    }
    
    // SERVICE METHODS
    
    public void initHours(String startHourText, String endHourText) {
        int initStartHour = Integer.parseInt(startHourText);
        int initEndHour = Integer.parseInt(endHourText);
        if (initStartHour <= initEndHour) {
            // THESE ARE VALID HOURS SO KEEP THEM
            // NOTE THAT THESE VALUES MUST BE PRE-VERIFIED
            startHour = initStartHour;
            endHour = initEndHour;
        }
        resetOfficeHours();
    }
    
    public void addTA(TeachingAssistantPrototype ta) {
        if (!hasTA(ta)) {
            TAType taType = TAType.valueOf(ta.getType());
            ArrayList<TeachingAssistantPrototype> tas = allTAs.get(taType);
            tas.add(ta);
            this.updateTAs();
        }
    }

    public void addTA(TeachingAssistantPrototype ta, HashMap<TimeSlot, ArrayList<DayOfWeek>> officeHours) {
        addTA(ta);
        for (TimeSlot timeSlot : officeHours.keySet()) {
            ArrayList<DayOfWeek> days = officeHours.get(timeSlot);
            for (DayOfWeek dow : days) {
                timeSlot.addTA(dow, ta);
            }
        }
    }
    
    public void removeTA(TeachingAssistantPrototype ta) {
        // REMOVE THE TA FROM THE LIST OF TAs
        TAType taType = TAType.valueOf(ta.getType());
        allTAs.get(taType).remove(ta);
        
        // REMOVE THE TA FROM ALL OF THEIR OFFICE HOURS
        for (TimeSlot timeSlot : officeHours) {
            timeSlot.removeTA(ta);
        }
        
        // AND REFRESH THE TABLES
        this.updateTAs();
    }

    public void removeTA(TeachingAssistantPrototype ta, HashMap<TimeSlot, ArrayList<DayOfWeek>> officeHours) {
        removeTA(ta);
        for (TimeSlot timeSlot : officeHours.keySet()) {
            ArrayList<DayOfWeek> days = officeHours.get(timeSlot);
            for (DayOfWeek dow : days) {
                timeSlot.removeTA(dow, ta);
            }
        }    
    }
    
    public DayOfWeek getColumnDayOfWeek(int columnNumber) {
        return TimeSlot.DayOfWeek.values()[columnNumber-2];
    }

    public TeachingAssistantPrototype getTAWithName(String name) {
        Iterator<TeachingAssistantPrototype> taIterator = teachingAssistants.iterator();
        while (taIterator.hasNext()) {
            TeachingAssistantPrototype ta = taIterator.next();
            if (ta.getName().equals(name))
                return ta;
        }
        return null;
    }

    public TeachingAssistantPrototype getTAWithEmail(String email) {
        Iterator<TeachingAssistantPrototype> taIterator = teachingAssistants.iterator();
        while (taIterator.hasNext()) {
            TeachingAssistantPrototype ta = taIterator.next();
            if (ta.getEmail().equals(email))
                return ta;
        }
        return null;
    }

    public TimeSlot getTimeSlot(String startTime) {
        Iterator<TimeSlot> timeSlotsIterator = officeHours.iterator();
        while (timeSlotsIterator.hasNext()) {
            TimeSlot timeSlot = timeSlotsIterator.next();
            String timeSlotStartTime = timeSlot.getStartTime().replace(":", "_");
            if (timeSlotStartTime.equals(startTime))
                return timeSlot;
        }
        return null;
    }

    public TAType getSelectedType() {
        RadioButton allRadio = (RadioButton)app.getGUIModule().getGUINode(OH_ALL_RADIO_BUTTON);
        if (allRadio.isSelected())
            return TAType.All;
        RadioButton gradRadio = (RadioButton)app.getGUIModule().getGUINode(OH_GRAD_RADIO_BUTTON);
        if (gradRadio.isSelected())
            return TAType.Graduate;
        else
            return TAType.Undergraduate;
    }

    public TeachingAssistantPrototype getSelectedTA() {
        AppGUIModule gui = app.getGUIModule();
        TableView<TeachingAssistantPrototype> tasTable = (TableView)gui.getGUINode(OH_TAS_TABLE_VIEW);
        return tasTable.getSelectionModel().getSelectedItem();
    }
    
    public HashMap<TimeSlot, ArrayList<DayOfWeek>> getTATimeSlots(TeachingAssistantPrototype ta) {
        HashMap<TimeSlot, ArrayList<DayOfWeek>> timeSlots = new HashMap();
        for (TimeSlot timeSlot : officeHours) {
            if (timeSlot.hasTA(ta)) {
                ArrayList<DayOfWeek> daysForTA = timeSlot.getDaysForTA(ta);
                timeSlots.put(timeSlot, daysForTA);
            }
        }
        return timeSlots;
    }
    
    private boolean hasTA(TeachingAssistantPrototype testTA) {
        return allTAs.get(TAType.Graduate).contains(testTA)
                ||
                allTAs.get(TAType.Undergraduate).contains(testTA);
    }
    
    public boolean isTASelected() {
        AppGUIModule gui = app.getGUIModule();
        TableView tasTable = (TableView)gui.getGUINode(OH_TAS_TABLE_VIEW);
        return tasTable.getSelectionModel().getSelectedItem() != null;
    }

    public boolean isLegalNewTA(String name, String email) {
        if ((name.trim().length() > 0)
                && (email.trim().length() > 0)) {
            // MAKE SURE NO TA ALREADY HAS THE SAME NAME
            TAType type = this.getSelectedType();
            TeachingAssistantPrototype testTA = new TeachingAssistantPrototype(name, email, type);
            if (this.teachingAssistants.contains(testTA))
                return false;
            if (this.isLegalNewEmail(email)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isLegalNewName(String testName) {
        if (testName.trim().length() > 0) {
            for (TeachingAssistantPrototype testTA : this.teachingAssistants) {
                if (testTA.getName().equals(testName))
                    return false;
            }
            return true;
        }
        return false;
    }
    
    public boolean isLegalNewEmail(String email) {
        Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile(
                "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        if (matcher.find()) {
            for (TeachingAssistantPrototype ta : this.teachingAssistants) {
                if (ta.getEmail().equals(email.trim()))
                    return false;
            }
            return true;
        }
        else return false;
    }
    
    public boolean isDayOfWeekColumn(int columnNumber) {
        return columnNumber >= 2;
    }
    
    public boolean isTATypeSelected() {
        AppGUIModule gui = app.getGUIModule();
        RadioButton allRadioButton = (RadioButton)gui.getGUINode(OH_ALL_RADIO_BUTTON);
        return !allRadioButton.isSelected();
    }
    
    public boolean isValidTAEdit(TeachingAssistantPrototype taToEdit, String name, String email) {
        if (!taToEdit.getName().equals(name)) {
            if (!this.isLegalNewName(name))
                return false;
        }
        if (!taToEdit.getEmail().equals(email)) {
            if (!this.isLegalNewEmail(email))
                return false;
        }
        return true;
    }

    public boolean isValidNameEdit(TeachingAssistantPrototype taToEdit, String name) {
        if (!taToEdit.getName().equals(name)) {
            if (!this.isLegalNewName(name))
                return false;
        }
        return true;
    }

    public boolean isValidEmailEdit(TeachingAssistantPrototype taToEdit, String email) {
        if (!taToEdit.getEmail().equals(email)) {
            if (!this.isLegalNewEmail(email))
                return false;
        }
        return true;
    }    

    public void updateTAs() {
        TAType type = getSelectedType();
        selectTAs(type);
    }
    
    public void selectTAs(TAType type) {
        teachingAssistants.clear();
        Iterator<TeachingAssistantPrototype> tasIt = this.teachingAssistantsIterator();
        while (tasIt.hasNext()) {
            TeachingAssistantPrototype ta = tasIt.next();
            if (type.equals(TAType.All)) {
                teachingAssistants.add(ta);
            }
            else if (ta.getType().equals(type.toString())) {
                teachingAssistants.add(ta);
            }
        }
        
        // SORT THEM BY NAME
        sortTAs();

        // CLEAR ALL THE OFFICE HOURS
        Iterator<TimeSlot> officeHoursIt = officeHours.iterator();
        while (officeHoursIt.hasNext()) {
            TimeSlot timeSlot = officeHoursIt.next();
            timeSlot.filter(type);
        }
        
        app.getFoolproofModule().updateAll();
    }
    
    public Iterator<TimeSlot> officeHoursIterator() {
        return officeHours.iterator();
    }

    public Iterator<TeachingAssistantPrototype> teachingAssistantsIterator() {
        return new AllTAsIterator();
    }
    
    private class AllTAsIterator implements Iterator {
        Iterator gradIt = allTAs.get(TAType.Graduate).iterator();
        Iterator undergradIt = allTAs.get(TAType.Undergraduate).iterator();

        public AllTAsIterator() {}
        
        @Override
        public boolean hasNext() {
            if (gradIt.hasNext() || undergradIt.hasNext())
                return true;
            else
                return false;                
        }

        @Override
        public Object next() {
            if (gradIt.hasNext())
                return gradIt.next();
            else if (undergradIt.hasNext())
                return undergradIt.next();
            else
                return null;
        }
    }
    
    public void UpdateTimeTange(String start, String end){
        AppGUIModule gui = app.getGUIModule();
        ComboBox x=(ComboBox) gui.getGUINode(OH_OFFICE_HOURS_START_COMBO);
        ComboBox y=(ComboBox) gui.getGUINode(OH_OFFICE_HOURS_END_COMBO);
        x.getSelectionModel().select(start);
        y.getSelectionModel().select(end);
        
        TableView<TimeSlot> officeHoursTableView = (TableView)gui.getGUINode(OH_OFFICE_HOURS_TABLE_VIEW);
        officeHours = officeHoursTableView.getItems(); 
        officeHours.clear();
        boolean switch1=false;
        boolean switch2=false;
        a: for(int i=0;i<oh.size();i++){
            if(start.equals(oh.get(i).getStartTime())){
                switch1=true;
            }
            if(end.equals(oh.get(i).getEndTime())){
                switch2=true;
                switch1=false;
            }
            if(switch1){
                officeHours.add(oh.get(i));
            }
            if(switch2){
                officeHours.add(oh.get(i));
                break a;
            }
        }
        
    
    }
    
//site
    //banner
    String banner_subject;
    String banner_number;
    String banner_smester;
    String banner_year;
    String banner_title;
    ComboBox banner_subject_com;
    ComboBox banner_number_com;
    ComboBox banner_smester_com;
    ComboBox banner_year_com;
    TextField banner_title_textfield;
    Label exportdir_label;
    
    public String getTEXTF(){
        return banner_title_textfield.getText();
    }
    
    public String getBannerSubject(){
        return banner_subject;
    }
    public void setBannerSubject(String i){
        banner_subject=i;
    }
    public String getBannersmester(){
        return banner_smester;
    }
    public void setBannersmester(String i){
        banner_smester=i;
    }
    public String getBannerNumber(){
        return banner_number;
    }
    public void setBannerNumber(String i){
        banner_number=i;
    }
    public String getBannerYear(){
        return banner_year;
    }
    public void setBannerYear(String i){
        banner_year=i;
    }
    public String getBannerTitle(){
        return banner_title;
    }
    public void setBannerTitle(String i){
        banner_title=i;
    }
    public void setBanner(String subject,String number, String smester,String year, String title){
        AppGUIModule gui = app.getGUIModule();

            banner_subject=subject;
            banner_number=number;
            banner_smester=smester;
            banner_year=year;
            banner_title=title;
            
            Label exportdir=(Label)app.getGUIModule().getGUINode(SITE_BANNER_EXPORTDIR);
            ComboBox subject_com=(ComboBox)gui.getGUINode(SITE_BANNER_SUBJECT_COMBO);
            ComboBox smester_com=(ComboBox)gui.getGUINode(SITE_BANNER_SMESTER_COMBO);
            ComboBox num_com=(ComboBox)gui.getGUINode(SITE_BANNER_NUMBER_COMBO);
            ComboBox years_com=(ComboBox)gui.getGUINode(SITE_BANNER_YEAR_COMBO);
            TextField textfield_banner=(TextField)gui.getGUINode(SITE_BANNER_TITLE_TEXTFIELD);
            if(subject_com.getItems().contains(subject)){
                subject_com.getSelectionModel().select(subject);}
            else{
                subject_com.getItems().add(subject);
                subject_com.getSelectionModel().select(subject);
            }
            if(smester_com.getItems().contains(smester)){
                smester_com.getSelectionModel().select(smester);}
            else{
                smester_com.getItems().add(smester);
                smester_com.getSelectionModel().select(smester);;
            }
            if(num_com.getItems().contains(number)){
                num_com.getSelectionModel().select(number);}
            else{
                num_com.getItems().add(number);
                num_com.getSelectionModel().select(number);
            }
            if(years_com.getItems().contains(year)){
                years_com.getSelectionModel().select(year);}
            else{
                years_com.getItems().add(year);
                years_com.getSelectionModel().select(year);
            }
            
            textfield_banner.setText(title);
            
            Platform.runLater(new Runnable() {
        @Override
        public void run() {
            exportdir.setText(".\\export\\"+subject+"_"+number+"_"+smester+"_"+year+"\\public_html");
        }
        });
            
    }
    //pages
    boolean home;
    boolean syllabus;
    boolean schedule;
    boolean hw;
    
    public boolean getHome(){return home;}
    
    public boolean getSyllabus(){return syllabus;}
    
    public boolean getSchedule(){return schedule;}
    
    public boolean getHW(){return hw;}
    
    public void setPages(boolean home1,
    boolean syllabus1,
    boolean schedule1,
    boolean hw1){
        home=home1;
        syllabus=syllabus1;
        schedule=schedule1;
        hw=hw1;
        AppGUIModule gui = app.getGUIModule();
        CheckBox homecheck=(CheckBox)gui.getGUINode(SITE_PAGES_HOME_CHECKBOX);
        CheckBox syllabuscheck=(CheckBox)gui.getGUINode(SITE_PAGES_SYLLABUS_CHECKBOX);
        CheckBox schedulecheck=(CheckBox)gui.getGUINode(SITE_PAGES_SCHEDULE_CHECKBOX);
        CheckBox hwcheck=(CheckBox)gui.getGUINode(SITE_PAGES_HW_CHECKBOX);
                
        homecheck.setSelected(home);
        syllabuscheck.setSelected(syllabus);
        schedulecheck.setSelected(schedule);
        hwcheck.setSelected(hw);
        
    }
    //style
   
    
    String favicon_path;
    String navbar_path;
    String leftfooterpath;
    String rightfooter_path;
    
    ImageView imageview_f;
    ImageView imageview_n;
    ImageView imageview_l;
    ImageView imageview_r;
    
    public String getFavicon_path(){
        return favicon_path;
    }
    public String getnavbar_path(){
        return navbar_path;
    }
    public String getleftfooter_path(){
        return leftfooterpath;
    }
    public String getrightfooter_path(){
        return rightfooter_path;
    }
    
    public void setStyle(String a,String b,String c,String d){
    
     favicon_path=a;
     navbar_path=b;
     leftfooterpath=c;
     rightfooter_path=d;
     
            try{
                File ima1=new File(a);
                BufferedImage  bufferedImage1 = ImageIO.read(ima1);
                Image bannerImage1 = SwingFXUtils.toFXImage(bufferedImage1, null);
                File ima2=new File(b);
                BufferedImage  bufferedImage2 = ImageIO.read(ima2);
                Image bannerImage2 = SwingFXUtils.toFXImage(bufferedImage2, null);
                File ima3=new File(c);
                BufferedImage  bufferedImage3 = ImageIO.read(ima3);
                Image bannerImage3 = SwingFXUtils.toFXImage(bufferedImage3, null);
                File ima4=new File(d);
                BufferedImage  bufferedImage4 = ImageIO.read(ima4);
                Image bannerImage4 = SwingFXUtils.toFXImage(bufferedImage4, null);
                imageview_f.setImage(bannerImage1);
                imageview_n.setImage(bannerImage2);
                imageview_l.setImage(bannerImage3);
                imageview_r.setImage(bannerImage4);
            }
            catch(Exception e){
            
            }
     
    
    }
    public String convertPath(String x){
        String ok="";
        for(int i=x.length()-1;i>=0;i--){
            if(x.substring(i,i+1).equals("/")){
                break;
            }
            else{
                ok=x.substring(i,i+1)+ok;
            }
        }
        return "./images/"+ok;
    }
    
    
   
    
    //instructor
    String ins_name;
    String ins_email;
    String ins_room;
    String ins_homepage;
    TextField ins_name_textfield;
    TextField ins_email_textfield;
    TextField ins_room_textfield;
    TextField ins_homepage_textfield;
    
    public String getins_name(){
        return ins_name;
    }
    public String getins_email(){
        return ins_email;
    }
    public String getins_room(){
        return ins_room;
    }
    public String getins_homepage(){
        return ins_homepage;
    }
    
    public void setInstructor(String name,String email,String room,String homepage){
         ins_name=name;
         ins_email=email;
         ins_room=room;
         ins_homepage=homepage;
         AppGUIModule gui = app.getGUIModule();
         TextField ins_name1=(TextField)gui.getGUINode( SITE_INSTRUCTOR_NAME_TEXTFIELD);
         TextField ins_email1=(TextField)gui.getGUINode( SITE_INSTRUCTOR_EMAIL_TEXTFIELD);
         TextField ins_room1=(TextField)gui.getGUINode( SITE_INSTRUCTOR_ROOM_TEXTFIELD);
         TextField ins_homepage1=(TextField)gui.getGUINode( SITE_INSTRUCTOR_HOMEPAGE_TEXTFIELD);
         ins_name1.setText(ins_name);
         ins_email1.setText(ins_email);
         ins_room1.setText(ins_room);
         ins_homepage1.setText(ins_homepage);
    }
    TextArea ins_textArea;
    String  ins_textArea_string;
    ArrayList hours=new ArrayList();
    
    
    public TextArea geIns_textArea(){
        return ins_textArea;
    }
    
    public void setTextArea_ins(String x){
        ins_textArea.setText(x);
        ins_textArea_string=x;
        this.setInstructor_hours(x);
         
    }
    public String getins_textArea_string(){
        return  ins_textArea_string;
    }
    
    
    public ArrayList getHours_list(){
        return hours;
    }
    
    public void setInstructor_hours(String hour){
        hours.clear();
        String h="";
        boolean s=false;
        for(int i=0;i<hour.length();i++){
            if(hour.substring(i,i+1).equals("{")){
                
                s=true;
            }
            if(s){
                h+=hour.substring(i,i+1);
            }
            if(hour.substring(i,i+1).equals("}")){
                s=false;
                hours.add(h);
                h="";
            }
        }
  }
   
    public void saveToFile(Image image) {
        
        BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
        try {
             File outputFile = new File("./images/");
             ImageIO.write(bImage, "png", outputFile);
            } 
        catch (Exception e) {
             
            }
    }
    
    
    
    
    
    
//syllabus
        TextArea  tx1;
        TextArea  tx2;
        TextArea  tx3;
        TextArea  tx4;
        TextArea  tx5;
        TextArea  tx6;
        TextArea  tx7;
        TextArea  tx8;
        TextArea  tx9;
        String  txt1;
        String  txt2;
        String  txt3;
        String  txt4;
        String  txt5;
        String  txt6;
        String  txt7;
        String  txt8;
        String  txt9;
        
        ArrayList topics=new ArrayList();
        
        public void convertSyllabusTopic(String t){
        topics.clear();
        String h="";
        boolean s=false;
        for(int i=0;i<t.length();i++){
           
            if(s){
                if(!t.substring(i,i+1).equals("\""))
                h+=t.substring(i,i+1);
            }
            if(t.substring(i,i+1).equals("\"")){
                s=!s;
            }
            if(!s && !h.equals("")){
                topics.add(h);
                h="";
            }
            
        }
           
        
        }
        ArrayList outcomes=new ArrayList();
        public void convertOutComes(String t){
        outcomes.clear();
        String h="";
        boolean s=false;
        for(int i=0;i<t.length();i++){
           
            if(s){
                if(!t.substring(i,i+1).equals("\""))
                h+=t.substring(i,i+1);
            }
            if(t.substring(i,i+1).equals("\"")){
                s=!s;
            }
            if(!s && !h.equals("")){
                outcomes.add(h);
                h="";
            }
            
        }
           
        
        }
        ArrayList textbook=new ArrayList();
        
        public void convertTextbook(String t){
        textbook.clear();
        String h="";
        boolean s=false;
        for(int i=0;i<t.length();i++){
            if(t.substring(i,i+1).equals("{")){
                
                s=true;
            }
            if(s){
                h+=t.substring(i,i+1);
            }
            if(t.substring(i,i+1).equals("}")){
                s=false;
                textbook.add(h);
                h="";
            }
        }
  }
        
        
        
        ArrayList gradingcomp=new ArrayList();
        
        public void convertGradComp(String hour){
        gradingcomp.clear();
        String h="";
        boolean s=false;
        for(int i=0;i<hour.length();i++){
            if(hour.substring(i,i+1).equals("{")){
                
                s=true;
            }
            if(s){
                h+=hour.substring(i,i+1);
            }
            if(hour.substring(i,i+1).equals("}")){
                s=false;
                gradingcomp.add(h);
                h="";
            }
        }
  }
        
        
        public ArrayList getTopics(){
            return topics;
        }
        public ArrayList getOutComes(){
            return outcomes;
        }
        public ArrayList getTextBooks(){
            return textbook;
        }
        public ArrayList getGradingComp(){
            return gradingcomp;
        }
        
        public void setSyllabus(String type,String text){
            if(type.equals("description")){
                tx1.setText(text);
                txt1=text;
            }
            if(type.equals("topics")){
                tx2.setText(text);
                txt2=text;
                convertSyllabusTopic(text);
            }
            if(type.equals("prerequisites")){
                tx3.setText(text);
                txt3=text;
            }
            if(type.equals("outcomes")){
                tx4.setText(text);
                txt4=text;
                convertOutComes(text);
            }
            if(type.equals("textbooks")){
                tx5.setText(text);
                txt5=text;
                convertTextbook(text);
            }
            if(type.equals("gradedComponents")){
                tx6.setText(text);
                txt6=text;
                convertGradComp(text);
            }
            
            if(type.equals("gradingNote")){
                tx7.setText(text);
                txt7=text;
            }
            if(type.equals("academicDishonesty")){
                tx8.setText(text);
                txt8=text;
            }
            if(type.equals("specialAssistance")){
                tx9.setText(text);
                txt9=text;
            }
             
        
        }
        public String getTxt(String type)
        {
            if(type.equals("description")){
                return txt1;
            }
            if(type.equals("topics")){
                return txt2;
            }
            if(type.equals("prerequisites")){
                return txt3;
            }
            if(type.equals("outcomes")){
                return txt4;
            }
            if(type.equals("textbooks")){
                return txt5;
            }
            if(type.equals("gradedComponents")){
                return txt6;
            }
            
            if(type.equals("gradingNote")){
                return txt7;
            }
            if(type.equals("academicDishonesty")){
                return txt8;
            }
            if(type.equals("specialAssistance")){
                return txt9;
            }
            return "";
            
        }    
//meeting
      TableView lectureTV;
      TableView recTV;
      TableView labTV;
      
        ObservableList<Lectures> lectures;
        ObservableList<Recitation> recitation;
        ObservableList<Labs> labs;
        
        public void changeLectureTable(Lectures l){
            for(int i=0;i<lectures.size();i++){
                if(lectures.get(i).equals(l)){
                    lectures.get(i).setSection(l.getSection());
                    lectures.get(i).setDays(l.getDays());
                    lectures.get(i).setRoom(l.getRoom());
                    lectures.get(i).setTime(l.getTime());
                }
            }
            lectureTV.refresh();
        }
        public void changeLabsTable(Labs l){
            for(int i=0;i<labs.size();i++){
                if(labs.get(i).equals(l)){
                    labs.get(i).setSection(l.getSection());
                    labs.get(i).setDaytime(l.getDaytime());
                    labs.get(i).setRoom(l.getRoom());
                    labs.get(i).setTa1(l.getTa1());
                    labs.get(i).setTa2(l.getTa2());
                }
            }
            labTV.refresh();
        }
        public void changeRecTable(Recitation l){
            for(int i=0;i<recitation.size();i++){
                if(recitation.get(i).equals(l)){
                    recitation.get(i).setSection(l.getSection());
                    recitation.get(i).setDaytime(l.getDaytime());
                    recitation.get(i).setRoom(l.getRoom());
                    recitation.get(i).setTa1(l.getTa1());
                    recitation.get(i).setTa2(l.getTa2());
                }
            }
            recTV.refresh();
        }
        
        public void addLectures(Lectures l){
            lectures.add(l);
        }
        public void removeLectures(Lectures l){
            lectures.remove(l);
        }
        public void addRecitation(Recitation l){
            recitation.add(l);
        }
        public void removeRecitation(Recitation l){
            recitation.remove(l);
        }
        public void addLabs(Labs l){
            labs.add(l);
        }
        public void removeLabs(Labs l){
            labs.remove(l);
        }
        public  ObservableList getLectures(){
            return lectures;
        }
        public  ObservableList getRecitation(){
            return recitation;
        }
        public  ObservableList getLabs(){
            return labs;
        }
        
        
    
//schedule
        DatePicker end;
        DatePicker start;
        LocalDate datepicker_start;
        LocalDate datepicker_end;
        TextField title;
        TextField topic;
        TextField link;
        DatePicker date;
        ComboBox type;
        
        String title_text;
        String topic_text;
        String link_text;
        String type_text;
        LocalDate datepicker;
        
        ObservableList SItable;
        TableView SItableview;
        
         Button xxxxx;
        
        
        public LocalDate getDatepicker_start(){
            return datepicker_start;
        }
        
        public LocalDate getDatepicker_end(){
            return datepicker_end;
        }
        public void setStartEndMF(LocalDate s, LocalDate e){
            datepicker_start=s;
            datepicker_end=e;
            start.setValue(s);
            end.setValue(e);
        }
        
        public void EditSItable(ScheduleItem item,String ti,String to,String li,String da,String ty,LocalDate dl){
            for(int i=0;i<SItable.size();i++){
                if(SItable.get(i).equals(item)){
                    item.setDate(da);
                    item.setLink(li);
                    item.setTitle(ti);
                    item.setTopic(to);
                    item.setType(ty);
                    item.setDate_l(dl);
                }
               SItableview.refresh();
            }
            
            
        }
        public void AddSItable(ScheduleItem i){
            if(SItable.contains(i)){
                AppGUIModule gui = app.getGUIModule();
                
                SItable.remove(i);
                if(SItableview.getSelectionModel().getSelectedItem()==null){
                 Platform.runLater(new Runnable() {
        @Override
        public void run() {
            xxxxx.setText("Add");
        }
        });}
            }
            else{
                SItable.add(i);
            }
        
        }
        public ObservableList getSItable(){
            return SItable;
        }
        
        
        public LocalDate getLocalDate(){
            return datepicker;
        
        }
        
        public String getTitle(){
            return title_text;
        
        }
        public String getTopic(){
            
        return topic_text;
        }
        public String  getLink(){
            return link_text;
        
        }
        public String getType(){
            return type_text;
        
        }
        
        public DatePicker getStartCa(){
        
            return start;
        }
        public DatePicker getEndCa(){
        
            return end;
        }
        public void setStartCa(LocalDate a){
            start.setValue(a);
        }
        public void setEndCa(LocalDate a){
             end.setValue(a);
        }
        public void setScheduleItemsText(String title1,String topic1,String link1,String type1,LocalDate date1){
            title_text=title1;
            topic_text=topic1;
            link_text=link1;
            type_text=type1;
            datepicker=date1;
            
            title.setText(title1);
            topic.setText(topic1);
            link.setText(link1);
            type.getSelectionModel().select(type1);
            date.setValue(date1);
        }
       public void  setScheduleItemsText_clear(String title1,String topic1,String link1,String type1){
           title_text=title1;
            topic_text=topic1;
            link_text=link1;
            type_text=type1;
            
            title.setText(title1);
            topic.setText(topic1);
            link.setText(link1);
            type.getSelectionModel().select(type1);
           
       
       
       }
       public String getExportDir(){
           if(home){
           return "./export/"+banner_subject+"_"+banner_number+"_"+banner_smester+"_"+banner_year+"/index.html";}
           else if(syllabus){
           return "./export/"+banner_subject+"_"+banner_number+"_"+banner_smester+"_"+banner_year+"/syllabus.html";}
           else if(schedule){
           return "./export/"+banner_subject+"_"+banner_number+"_"+banner_smester+"_"+banner_year+"/schedule.html";}
           else{
            return "./export/"+banner_subject+"_"+banner_number+"_"+banner_smester+"_"+banner_year+"/hws.html";
           
           }
       }
       
       ComboBox css;
       String css_string;
       public String getCss(){
           return css.getValue().toString();
       }
       
       public void setCss(String ok){
           css.setValue(ok);
           css_string=ok;
       }
       
       public String getCSSString(){
           return css_string;
       }
}
