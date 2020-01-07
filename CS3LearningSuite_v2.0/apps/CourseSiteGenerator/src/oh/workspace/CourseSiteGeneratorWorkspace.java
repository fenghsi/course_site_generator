package oh.workspace;

import static djf.AppPropertyType.APP_BANNER;
import static djf.AppPropertyType.APP_LOGO;
import static djf.AppPropertyType.APP_PATH_IMAGES;
import static djf.AppPropertyType.LOAD_BUTTON;
import static djf.AppPropertyType.REDO_BUTTON;
import static djf.AppPropertyType.UNDO_BUTTON;
import djf.components.AppWorkspaceComponent;
import djf.modules.AppFoolproofModule;
import djf.modules.AppGUIModule;
import static djf.modules.AppGUIModule.ENABLED;
import static djf.modules.AppLanguageModule.FILE_PROTOCOL;
import djf.ui.AppNodesBuilder;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import static java.time.DayOfWeek.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import oh.CourseSiteGeneratorApp;
import properties_manager.PropertiesManager;
import oh.CourseSiteGeneratorPropertyType;
import static oh.CourseSiteGeneratorPropertyType.*;
import oh.data.CourseSiteGeneratorData;
import oh.data.Labs;
import oh.data.Lectures;
import oh.data.Recitation;
import oh.data.ScheduleItem;
import oh.data.TeachingAssistantPrototype;
import oh.data.TimeSlot;
import oh.data.TimeSlot.DayOfWeek;
import oh.workspace.controllers.CourseSiteGeneratorController;
import oh.workspace.dialogs.TADialog;
import oh.workspace.foolproof.CourseSiteGeneratorFoolproofDesign;
import static oh.workspace.style.OHStyle.*;

/**
 *
 * @author McKillaGorilla
 */
public class CourseSiteGeneratorWorkspace extends AppWorkspaceComponent {
    
    
    
    public CourseSiteGeneratorWorkspace(CourseSiteGeneratorApp app) {
        super(app);

        // LAYOUT THE APP
        initLayout();

        // INIT THE EVENT HANDLERS
        initControllers();

        // 
        initFoolproofDesign();

        // INIT DIALOGS
        initDialogs();
    }

    private void initDialogs() {
        TADialog taDialog = new TADialog((CourseSiteGeneratorApp) app);
        app.getGUIModule().addDialog(OH_TA_EDIT_DIALOG, taDialog);
    }

    // THIS HELPER METHOD INITIALIZES ALL THE CONTROLS IN THE WORKSPACE
    private void initLayout()  {
        
        // FIRST LOAD THE FONT FAMILIES FOR THE COMBO BOX
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        CourseSiteGeneratorController controller = new CourseSiteGeneratorController((CourseSiteGeneratorApp) app);
        
        // THIS WILL BUILD ALL OF OUR JavaFX COMPONENTS FOR US
        AppNodesBuilder ohBuilder = app.getGUIModule().getNodesBuilder();
       
///Officehour
        // INIT THE HEADER ON THE LEFT
        VBox leftPane = ohBuilder.buildVBox(OH_LEFT_PANE, null, CLASS_OH_PANE, ENABLED);
        HBox tasHeaderBox = ohBuilder.buildHBox(OH_TAS_HEADER_PANE, leftPane, CLASS_OH_BOX, ENABLED);
        ohBuilder.buildLabel(CourseSiteGeneratorPropertyType.OH_TAS_HEADER_LABEL, tasHeaderBox, CLASS_OH_HEADER_LABEL, ENABLED);
        HBox typeHeaderBox = ohBuilder.buildHBox(OH_GRAD_UNDERGRAD_TAS_PANE, tasHeaderBox, CLASS_OH_RADIO_BOX, ENABLED);
        ToggleGroup tg = new ToggleGroup();
        ohBuilder.buildRadioButton(OH_ALL_RADIO_BUTTON, typeHeaderBox, CLASS_OH_RADIO_BUTTON, ENABLED, tg, true);
        ohBuilder.buildRadioButton(OH_GRAD_RADIO_BUTTON, typeHeaderBox, CLASS_OH_RADIO_BUTTON, ENABLED, tg, false);
        ohBuilder.buildRadioButton(OH_UNDERGRAD_RADIO_BUTTON, typeHeaderBox, CLASS_OH_RADIO_BUTTON, ENABLED, tg, false);
        
        // MAKE THE TABLE AND SETUP THE DATA MODEL
        TableView<TeachingAssistantPrototype> taTable = ohBuilder.buildTableView(OH_TAS_TABLE_VIEW, leftPane, CLASS_OH_TABLE_VIEW, ENABLED);
        taTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        TableColumn nameColumn = ohBuilder.buildTableColumn(OH_NAME_TABLE_COLUMN, taTable, CLASS_OH_COLUMN);
        TableColumn emailColumn = ohBuilder.buildTableColumn(OH_EMAIL_TABLE_COLUMN, taTable, CLASS_OH_COLUMN);
        TableColumn slotsColumn = ohBuilder.buildTableColumn(OH_SLOTS_TABLE_COLUMN, taTable, CLASS_OH_CENTERED_COLUMN);
        TableColumn typeColumn = ohBuilder.buildTableColumn(OH_TYPE_TABLE_COLUMN, taTable, CLASS_OH_COLUMN);
        nameColumn.setCellValueFactory(new PropertyValueFactory<String, String>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<String, String>("email"));
        slotsColumn.setCellValueFactory(new PropertyValueFactory<String, String>("slots"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<String, String>("type"));
        nameColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(1.0 / 5.0));
        emailColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(2.0 / 5.0));
        slotsColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(1.0 / 5.0));
        typeColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(1.0 / 5.0));
        
        // ADD BOX FOR ADDING A TA
        HBox taBox = ohBuilder.buildHBox(OH_ADD_TA_PANE, leftPane, CLASS_OH_PANE, ENABLED);
        ohBuilder.buildTextField(OH_NAME_TEXT_FIELD, taBox, CLASS_OH_TEXT_FIELD, ENABLED);
        ohBuilder.buildTextField(OH_EMAIL_TEXT_FIELD, taBox, CLASS_OH_TEXT_FIELD, ENABLED);
        ohBuilder.buildTextButton(OH_ADD_TA_BUTTON, taBox, CLASS_OH_BUTTON, !ENABLED);
        
        // MAKE SURE IT'S THE TABLE THAT ALWAYS GROWS IN THE LEFT PANE
        VBox.setVgrow(taTable, Priority.ALWAYS);
        
        // INIT THE HEADER ON THE RIGHT
        VBox rightPane = ohBuilder.buildVBox(OH_RIGHT_PANE, null, CLASS_OH_PANE, ENABLED);
        HBox officeHoursHeaderBox = ohBuilder.buildHBox(OH_OFFICE_HOURS_HEADER_PANE, rightPane, CLASS_OH_PANE, ENABLED);
        ohBuilder.buildLabel(OH_OFFICE_HOURS_HEADER_LABEL, officeHoursHeaderBox, CLASS_OH_HEADER_LABEL, ENABLED);
        ohBuilder.buildLabel(SPACE_LABEL1,officeHoursHeaderBox, CLASS_OH_HEADER_LABEL, ENABLED);
        ohBuilder.buildLabel(OH_START_LABEL,officeHoursHeaderBox, CLASS_OH_HEADER_LABEL, ENABLED);
        ComboBox cb_start=ohBuilder.buildComboBox(OH_OFFICE_HOURS_START_COMBO, "", "",officeHoursHeaderBox, CLASS_SITE_COMBO, ENABLED);
        ohBuilder.buildLabel(OH_END_LABEL,officeHoursHeaderBox, CLASS_OH_HEADER_LABEL, ENABLED);
        ComboBox cb_end=ohBuilder.buildComboBox(OH_OFFICE_HOURS_END_COMBO, "", "",officeHoursHeaderBox, CLASS_SITE_COMBO, ENABLED);
        
        for (int i = 9; i <= 20; i++) {
            cb_start.getItems().add(this.getTimeString(i, true));
            cb_end.getItems().add(this.getTimeString(i, true));
            
            cb_start.getItems().add(this.getTimeString(i, false));
            cb_end.getItems().add(this.getTimeString(i, false));
        }
        cb_start.getSelectionModel().select("9:00am");
        cb_end.getSelectionModel().select("9:00pm");
        // SETUP THE OFFICE HOURS TABLE
        TableView<TimeSlot> officeHoursTable = ohBuilder.buildTableView(OH_OFFICE_HOURS_TABLE_VIEW, rightPane, CLASS_OH_OFFICE_HOURS_TABLE_VIEW, ENABLED);
        setupOfficeHoursColumn(OH_START_TIME_TABLE_COLUMN, officeHoursTable, CLASS_OH_TIME_COLUMN, "startTime");
        setupOfficeHoursColumn(OH_END_TIME_TABLE_COLUMN, officeHoursTable, CLASS_OH_TIME_COLUMN, "endTime");
        setupOfficeHoursColumn(OH_MONDAY_TABLE_COLUMN, officeHoursTable, CLASS_OH_DAY_OF_WEEK_COLUMN, "monday");
        setupOfficeHoursColumn(OH_TUESDAY_TABLE_COLUMN, officeHoursTable, CLASS_OH_DAY_OF_WEEK_COLUMN, "tuesday");
        setupOfficeHoursColumn(OH_WEDNESDAY_TABLE_COLUMN, officeHoursTable, CLASS_OH_DAY_OF_WEEK_COLUMN, "wednesday");
        setupOfficeHoursColumn(OH_THURSDAY_TABLE_COLUMN, officeHoursTable, CLASS_OH_DAY_OF_WEEK_COLUMN, "thursday");
        setupOfficeHoursColumn(OH_FRIDAY_TABLE_COLUMN, officeHoursTable, CLASS_OH_DAY_OF_WEEK_COLUMN, "friday");
        
        // MAKE SURE IT'S THE TABLE THAT ALWAYS GROWS IN THE LEFT PANE
        VBox.setVgrow(officeHoursTable, Priority.ALWAYS);
        
        // BOTH PANES WILL NOW GO IN A SPLIT PANE
        VBox sPane = new VBox();
        sPane.getChildren().addAll(leftPane,rightPane);
        
        ScrollPane spooo=new ScrollPane(sPane);
           sPane.prefWidthProperty().bind(spooo.widthProperty());
//Site
         VBox pane= ohBuilder.buildVBox(SITE_PANE, null, CLASS_SITE_PANE, ENABLED);
        
        pane.setSpacing(10);
        
        //Build banner
        GridPane banner=ohBuilder.buildGridPane(SITE_BANNER, pane, CLASS_SITE_BANNER, ENABLED);
        banner.prefWidthProperty().bind(pane.widthProperty());
            //build headers
        Label banner_1=ohBuilder.buildLabel(SITE_BANNER_HEADER_LABEL,    banner, 0, 0, 1, 1, CLASS_SITE_BANNER_HEADER,     ENABLED);
        ohBuilder.buildLabel(                   SPACE_LABEL,                        banner, 0,1,1,1 ,   CLASS_SITE_BANNER_CATEGORIES, ENABLED);
        Label banner_2=ohBuilder.buildLabel(SITE_BANNER_SUBJECT_LABEL,   banner, 0, 2, 1, 1, CLASS_SITE_BANNER_CATEGORIES, ENABLED);
        ohBuilder.buildLabel(                   SPACE_LABEL,                        banner, 2,2,1,1 ,   CLASS_SITE_BANNER_CATEGORIES, ENABLED);
        Label banner_3=ohBuilder.buildLabel(SITE_BANNER_SMESTER_LABEL,   banner, 3, 2, 1, 1, CLASS_SITE_BANNER_CATEGORIES, ENABLED);
        ohBuilder.buildLabel(                   SPACE_LABEL,                        banner, 0,3,1,1 ,   CLASS_SITE_BANNER_CATEGORIES, ENABLED);
        Label banner_4=ohBuilder.buildLabel(SITE_BANNER_NUMBER_LABEL,    banner, 0, 4, 1, 1, CLASS_SITE_BANNER_CATEGORIES, ENABLED);
        ohBuilder.buildLabel(                   SPACE_LABEL,                        banner, 2,4,1,1 ,   CLASS_SITE_BANNER_CATEGORIES, ENABLED);
        Label banner_5=ohBuilder.buildLabel(SITE_BANNER_YEAR_LABEL,      banner, 3, 4, 1, 1, CLASS_SITE_BANNER_CATEGORIES, ENABLED);
        ohBuilder.buildLabel(                   SPACE_LABEL,                        banner, 0,5,1,1 ,   CLASS_SITE_BANNER_CATEGORIES, ENABLED);
        Label banner_6=ohBuilder.buildLabel(SITE_BANNER_TITLE_LABEL,     banner, 0, 6, 1, 1, CLASS_SITE_BANNER_CATEGORIES, ENABLED);
        ohBuilder.buildLabel(                   SPACE_LABEL,                        banner, 0,7,1,1 ,   CLASS_SITE_BANNER_CATEGORIES, ENABLED);
        Label banner_7=ohBuilder.buildLabel(SITE_BANNER_EXPORTDIR_LABEL, banner, 0, 8, 1, 1, CLASS_SITE_BANNER_CATEGORIES, ENABLED);
            //build combox boxes
     /*   banner_1.prefWidthProperty().bind(banner.widthProperty().multiply(1.0 / 4.0));
        banner_2.prefWidthProperty().bind(banner.widthProperty().multiply(1.0 / 4.0));
        banner_3.prefWidthProperty().bind(banner.widthProperty().multiply(1.0 / 4.0));
        banner_4.prefWidthProperty().bind(banner.widthProperty().multiply(1.0 / 4.0));
        banner_5.prefWidthProperty().bind(banner.widthProperty().multiply(1.0 / 4.0));
        banner_6.prefWidthProperty().bind(banner.widthProperty().multiply(1.0 / 4.0));
        banner_7.prefWidthProperty().bind(banner.widthProperty().multiply(1.0 / 4.0));*/
        
        ComboBox cb1=ohBuilder.buildComboBox(SITE_BANNER_SUBJECT_COMBO, banner, 1, 2, 4, 1, CLASS_SITE_COMBO, ENABLED, "", "CSE");
        ComboBox cb2= ohBuilder.buildComboBox(SITE_BANNER_SMESTER_COMBO, banner, 4, 2, 4, 1, CLASS_SITE_COMBO, ENABLED, "","FALL");
        ComboBox cb3= ohBuilder.buildComboBox(SITE_BANNER_NUMBER_COMBO,  banner, 1, 4, 4, 1, CLASS_SITE_COMBO, ENABLED, "", "219");
        ComboBox cb4= ohBuilder.buildComboBox(SITE_BANNER_YEAR_COMBO,    banner, 4, 4, 4, 1, CLASS_SITE_COMBO, ENABLED, "", "2018");
        cb1.prefWidthProperty().bind(banner.widthProperty().multiply(1.0 / 4.0));
        cb2.prefWidthProperty().bind(banner.widthProperty().multiply(1.0 / 4.0));
        cb3.prefWidthProperty().bind(banner.widthProperty().multiply(1.0 / 4.0));
        cb4.prefWidthProperty().bind(banner.widthProperty().multiply(1.0 / 4.0));
        
        cb1.setEditable(ENABLED);
        cb1.getItems().add("CSE");
        cb1.getSelectionModel().select("CSE");
        cb2.setEditable(ENABLED);
        cb2.getItems().add("Fall");
        cb2.getItems().add("Spring");
        cb3.setEditable(ENABLED);
        cb2.getSelectionModel().select("Fall");
        cb3.getItems().add("219");
        cb4.setEditable(ENABLED);
        cb3.getSelectionModel().select("219");
        cb4.getItems().add("2018");
        cb4.getItems().add("2019");
        cb4.getSelectionModel().select("2018");
        
        
            // build textfield
        TextField tff=ohBuilder.buildTextField(SITE_BANNER_TITLE_TEXTFIELD, banner, 1, 6, 2, 1, CLASS_SITE_BANNER_TITLE_TEXTFIELD, ENABLED);
        tff.prefWidthProperty().bind(banner.widthProperty().multiply(1.0 / 4.0));
            // build source
        Label exportdir=ohBuilder.buildLabel(SITE_BANNER_EXPORTDIR,    banner, 1, 8, 1, 1, CLASS_SITE_BANNER_EXPORTDIR_TEXT,     ENABLED);
        exportdir.prefWidthProperty().bind(banner.widthProperty().multiply(2.0 / 4.0));
        tff.setText("Computer Science III");
        Platform.runLater(new Runnable() {
        @Override
        public void run() {
            exportdir.setText(".\\export\\"+cb1.getValue().toString()+"_"+cb3.getValue().toString()+"_"+cb2.getValue().toString()+"_"+cb4.getValue().toString()+"\\public_html");
        }
        });
        
        
        //Build pages
        HBox pages=ohBuilder.buildHBox(SITE_PAGES, pane, CLASS_SITE_PAGES, ENABLED);
        pages.prefWidthProperty().bind(pane.widthProperty());
        Label pages1=ohBuilder.buildLabel(SITE_PAGES_HEADER_LABEL, pages, CLASS_SITE_PAGES_HEADER, ENABLED);
        CheckBox pages2=ohBuilder.buildCheckBox(SITE_PAGES_HOME_CHECKBOX,     pages, CLASS_SITE_PAGES_CHECKBOX, ENABLED);
        CheckBox pages3=ohBuilder.buildCheckBox(SITE_PAGES_SYLLABUS_CHECKBOX, pages, CLASS_SITE_PAGES_CHECKBOX, ENABLED);
        CheckBox pages4=ohBuilder.buildCheckBox(SITE_PAGES_SCHEDULE_CHECKBOX, pages, CLASS_SITE_PAGES_CHECKBOX, ENABLED);
        CheckBox pages5=ohBuilder.buildCheckBox(SITE_PAGES_HW_CHECKBOX,       pages, CLASS_SITE_PAGES_CHECKBOX, ENABLED);
        
        pages1.prefWidthProperty().bind(pages.widthProperty().multiply(1.0 / 5.0));
         pages2.prefWidthProperty().bind(pages.widthProperty().multiply(1.0 / 5.0));
          pages3.prefWidthProperty().bind(pages.widthProperty().multiply(1.0 / 5.0));
           pages4.prefWidthProperty().bind(pages.widthProperty().multiply(1.0 / 5.0));
            pages5.prefWidthProperty().bind(pages.widthProperty().multiply(1.0 / 5.0));
        
        
        //Build style
        GridPane style=ohBuilder.buildGridPane(SITE_STYLE, pane, CLASS_SITE_STYLE, ENABLED);
        style.prefWidthProperty().bind(pane.widthProperty());
        ohBuilder.buildLabel(SITE_STYLE_HEADER_LABEL,           style, 0, 0, 1, 1, CLASS_SITE_STYLE_HEADER,      ENABLED);
         ohBuilder.buildLabel(                   SPACE_LABEL,                       style, 0,1,1,1 ,   CLASS_SITE_BANNER_CATEGORIES, ENABLED);
        Button style1=ohBuilder.buildTextButton(SITE_STYLE_FAVICON_BUTTON,    style, 0, 2, 1, 1, CLASS_SITE_STYLE_BUTTON,      ENABLED);
         ohBuilder.buildLabel(                   SPACE_LABEL,                       style, 0,3,1,1 ,   CLASS_SITE_BANNER_CATEGORIES, ENABLED);
        Button style2=ohBuilder.buildTextButton(SITE_STYLE_NAVBAR_BUTTON,     style, 0, 4, 1, 1, CLASS_SITE_STYLE_BUTTON,      ENABLED);
         ohBuilder.buildLabel(                   SPACE_LABEL,                       style, 0,5,1,1 ,   CLASS_SITE_BANNER_CATEGORIES, ENABLED);
        Button style3=ohBuilder.buildTextButton(SITE_STYLE_LEFTFOOTER_BUTTON, style, 0, 6, 1, 1, CLASS_SITE_STYLE_BUTTON,      ENABLED);
         ohBuilder.buildLabel(                   SPACE_LABEL,                       style, 0,7,1,1 ,   CLASS_SITE_BANNER_CATEGORIES, ENABLED);
        Button style4= ohBuilder.buildTextButton(SITE_STYLE_RIGHTFOOTER_BUTTON,style, 0, 8, 1, 1, CLASS_SITE_STYLE_BUTTON,      ENABLED);
         ohBuilder.buildLabel(                   SPACE_LABEL,                       style, 0,9,1,1 ,   CLASS_SITE_BANNER_CATEGORIES, ENABLED);
        Label style5=ohBuilder.buildLabel(SITE_STYLE_FONTCOLOR_LABEL,        style, 0, 10, 1, 1, CLASS_SITE_STYLE_CATEGORIES,  ENABLED);
         ohBuilder.buildLabel(                   SPACE_LABEL,                       style, 0,11,1,1 ,   CLASS_SITE_BANNER_CATEGORIES, ENABLED);
        Label style6=ohBuilder.buildLabel(SITE_STYLE_NOTE_LABEL,             style, 0, 12, 7, 1, CLASS_SITE_STYLE_CATEGORIES,  ENABLED);
        ComboBox cb5=ohBuilder.buildComboBox(SITE_STYLE_SHEET_COMBO, style, 1, 10, 1, 1, CLASS_SITE_COMBO, ENABLED, "", "");
        cb5.getItems().add("sea_wolf.css");
        cb5.getItems().add("mark_wolf.css");
        cb5.getSelectionModel().select("mark_wolf.css");
        ImageView iv1=ohBuilder.buildImageView(SITE_STYLE_FAVICON_IMAGE,      style, 1, 2, 1, 1, CLASS_SITE_IMAGE,      ENABLED);
        ImageView iv2=ohBuilder.buildImageView(SITE_STYLE_NAVBAR_IMAGE,       style, 1, 4, 1, 1, CLASS_SITE_IMAGE,      ENABLED);
        ImageView iv3=ohBuilder.buildImageView(SITE_STYLE_LEFTFOOTER_IMAGE,   style, 1, 6, 1, 1, CLASS_SITE_IMAGE,      ENABLED);
        ImageView iv4=ohBuilder.buildImageView(SITE_STYLE_RIGHTFOOTER_IMAGE,  style, 1, 8, 1, 1, CLASS_SITE_IMAGE,      ENABLED);
        
    
        
       
        iv1.setFitHeight(17);
        iv1.setFitWidth(17);
        iv2.setFitHeight(25);
        iv2.setFitWidth(120);
        iv3.setFitHeight(25);
        iv3.setFitWidth(120);
        iv4.setFitHeight(25);
        iv4.setFitWidth(120);
            
        //Build instructor
        GridPane instructor=ohBuilder.buildGridPane(SITE_INSTRUCTOR, pane, CLASS_SITE_INSTRUCTOR, ENABLED);
        instructor.prefWidthProperty().bind(pane.widthProperty());
        ohBuilder.buildLabel(SITE_INSTRUCTOR_HEADER_LABEL,  instructor, 0, 0, 1, 1, CLASS_SITE_STYLE_HEADER, ENABLED);
        ohBuilder.buildLabel(                   SPACE_LABEL,                       instructor, 0,1,1,1 ,   CLASS_SITE_BANNER_CATEGORIES, ENABLED);
        ohBuilder.buildLabel(SITE_INSTRUCTOR_NAME_LABEL,   instructor, 0, 2, 1, 1, CLASS_SITE_INSTRUCTOR_CATEGORIES, ENABLED);
        
        ohBuilder.buildLabel(SITE_INSTRUCTOR_EMAIL_LABEL,   instructor, 3, 2, 1, 1, CLASS_SITE_INSTRUCTOR_CATEGORIES, ENABLED);
        ohBuilder.buildLabel(                   SPACE_LABEL,                       instructor, 0,3,1,1 ,   CLASS_SITE_BANNER_CATEGORIES, ENABLED);
        ohBuilder.buildLabel(SITE_INSTRUCTOR_ROOM_LABEL,    instructor, 0, 4, 1, 1, CLASS_SITE_INSTRUCTOR_CATEGORIES, ENABLED);
       
        ohBuilder.buildLabel(SITE_INSTRUCTOR_HOMEPAGE_LABEL,      instructor, 3, 4, 1, 1, CLASS_SITE_INSTRUCTOR_CATEGORIES, ENABLED);
        
        TextField tf_in=ohBuilder.buildTextField(SITE_INSTRUCTOR_NAME_TEXTFIELD,     instructor, 1, 2, 1, 1, CLASS_SITE_INSTRUCTOR_TEXTFIELD, ENABLED);
        ohBuilder.buildLabel(                   SPACE_LABEL2,                       instructor, 2,2,1,1 ,   CLASS_SITE_BANNER_CATEGORIES, ENABLED);
        TextField tf_in1=ohBuilder.buildTextField(SITE_INSTRUCTOR_EMAIL_TEXTFIELD,    instructor, 4, 2, 1, 1, CLASS_SITE_INSTRUCTOR_TEXTFIELD, ENABLED);
        TextField tf_in2=ohBuilder.buildTextField(SITE_INSTRUCTOR_ROOM_TEXTFIELD,     instructor, 1, 4, 1, 1, CLASS_SITE_INSTRUCTOR_TEXTFIELD, ENABLED);
         ohBuilder.buildLabel(                   SPACE_LABEL2,                       instructor, 2,4,1,1 ,   CLASS_SITE_BANNER_CATEGORIES, ENABLED);
        TextField tf_in3=ohBuilder.buildTextField(SITE_INSTRUCTOR_HOMEPAGE_TEXTFIELD, instructor, 4, 4, 1, 1, CLASS_SITE_INSTRUCTOR_TEXTFIELD, ENABLED);
        tf_in.setText("Richard Mckenna");
        tf_in1.setText("Richard.Mckenna@stonybrook.edu");
        tf_in2.setText("New CS 216");
        tf_in3.setText("http://www.cs.stonybrook.edu/~richard");
        ohBuilder.buildLabel(                   SPACE_LABEL,                       instructor, 0,5,1,1 ,   CLASS_SITE_BANNER_CATEGORIES, ENABLED);
        HBox ins=ohBuilder.buildHBox(SITE_INSTRUCTOR_HBOX, instructor,0,6,2,1, CLASS_SITE_INSTRUCTOR_HBOX, ENABLED);
        tf_in.prefWidthProperty().bind(instructor.widthProperty().multiply(1.0 / 4.0));
        tf_in1.prefWidthProperty().bind(instructor.widthProperty().multiply(1.0 / 4.0));
        tf_in2.prefWidthProperty().bind(instructor.widthProperty().multiply(1.0 / 4.0));
        tf_in3.prefWidthProperty().bind(instructor.widthProperty().multiply(1.0 / 4.0));
        ohBuilder.buildIconToggleButton(SITE_INSTRUCTOR_OH_BUTTON, ins, CLASS_SITE_INSTRUCTOR_OH_BUTTON, ENABLED);
        ohBuilder.buildLabel(                   SPACE_LABEL,       ins,CLASS_SITE_BANNER_CATEGORIES, ENABLED);
        ohBuilder.buildLabel(          SITE_INSTRUCTOR_OH_LABEL,   ins,  CLASS_SITE_STYLE_HEADER, ENABLED);
        
        TextArea dada=ohBuilder.buildTextArea(SITE_INSTRUCTOR_OH_TEXTAREA,   instructor, 0, 7, 10, 1 , CLASS_SITE_TEXTAREA, ENABLED);
        dada.prefWidthProperty().bind(instructor.widthProperty().multiply(4.0 / 4.0));
        VBox.setVgrow(pane, Priority.ALWAYS);
        ///
        ScrollPane sp=new ScrollPane(pane);
        pane.prefWidthProperty().bind(sp.widthProperty());
        sp.setPadding(new Insets(20,10,10,30));
        
//Syllabus
        VBox sbus= ohBuilder.buildVBox(SYLLABUS_PANE, null, CLASS_OH_PANE, ENABLED);
        sbus.setSpacing(10);
        
        
        createSyllabusBox(SYLLABUS_DESCRIPTION,SYLLABUS_DESCRIPTION_TEXTFIELD,sbus,SYLLABUS_VBOXES1,SYLLABUS_DESCRIPTION_TEXTFIELD);
        createSyllabusBox(SYLLABUS_TOPICS,SYLLABUS_TOPICS_TEXTFIELD,sbus,SYLLABUS_VBOXES2,SYLLABUS_TOPICS_TEXTFIELD);
        createSyllabusBox(SYLLABUS_PREREQUISITES,SYLLABUS_PREREQUISITES_TEXTFIELD,sbus,SYLLABUS_VBOXES3,SYLLABUS_PREREQUISITES_TEXTFIELD);
        createSyllabusBox(SYLLABUS_OUTCOMES,SYLLABUS_OUTCOMES_TEXTFIELD,sbus,SYLLABUS_VBOXES4,SYLLABUS_OUTCOMES_TEXTFIELD);
        createSyllabusBox(SYLLABUS_TEXTBOOKS,SYLLABUS_TEXTBOOKS_TEXTFIELD,sbus,SYLLABUS_VBOXES5,SYLLABUS_TEXTBOOKS_TEXTFIELD);
        createSyllabusBox(SYLLABUS_GC,SYLLABUS_GC_TEXTFIELD,sbus,SYLLABUS_VBOXES6,SYLLABUS_GC_TEXTFIELD);
        createSyllabusBox(SYLLABUS_GN,SYLLABUS_GN_TEXTFIELD,sbus,SYLLABUS_VBOXES7,SYLLABUS_GN_TEXTFIELD);
        createSyllabusBox(SYLLABUS_AD,SYLLABUS_AD_TEXTFIELD,sbus,SYLLABUS_VBOXES8,SYLLABUS_AD_TEXTFIELD);
        createSyllabusBox(SYLLABUS_SA,SYLLABUS_SA_TEXTFIELD,sbus,SYLLABUS_VBOXES9,SYLLABUS_SA_TEXTFIELD);
        
      
        
        VBox.setVgrow(sbus, Priority.ALWAYS);
        ScrollPane sp1=new ScrollPane(sbus);
        sbus.prefWidthProperty().bind(sp1.widthProperty());
//meeting
        VBox meet=ohBuilder.buildVBox(MT_PANE, null, CLASS_OH_PANE, ENABLED);
        
        meet.setSpacing(10);
        
        GridPane lecture=ohBuilder.buildGridPane(MT_LECTURE, meet, CLASS_MT_VBOXES, ENABLED);
        lecture.setPadding(new Insets(10, 10, 10, 10));
        ohBuilder.buildTextButton(MT_LECTURE_ADD_BUTTON, lecture,0,0,1,1 , CLASS_MT_BUTTONS, ENABLED);
        ohBuilder.buildTextButton(MT_LECTURE_MINUS_BUTTON, lecture,1,0,1,1 , CLASS_MT_BUTTONS, ENABLED);
        ohBuilder.buildLabel(MT_LECTURE_HEADER_LABEL, lecture,2,0,2,1 , CLASS_MT_HEADERS, ENABLED);
        TableView lectureTable = ohBuilder.buildTableView(MT_LECTURE_TABLEVIEW, lecture,0,1,20,10 , CLASS_MT_TABLEVIEW, ENABLED );
        lectureTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        lecture.prefWidthProperty().bind(meet.widthProperty());
        lectureTable.prefWidthProperty().bind(lecture.widthProperty());
        TableColumn section = ohBuilder.buildTableColumn(MT_LECTURE_SECTION_TABLE_COLUMN,lectureTable, CLASS_OH_COLUMN);
        TableColumn days = ohBuilder.buildTableColumn(MT_LECTURE_DAYS_TABLE_COLUMN, lectureTable, CLASS_OH_COLUMN);
        TableColumn time = ohBuilder.buildTableColumn(MT_LECTURE_TIME_TABLE_COLUMN, lectureTable, CLASS_OH_COLUMN);
        TableColumn room = ohBuilder.buildTableColumn(MT_LECTURE_ROOM_TABLE_COLUMN, lectureTable, CLASS_OH_COLUMN);
        section.setCellValueFactory(new PropertyValueFactory<String, String>("section"));
        days.setCellValueFactory(new PropertyValueFactory<String, String>("days"));
        time.setCellValueFactory(new PropertyValueFactory<String, String>("time"));
        room.setCellValueFactory(new PropertyValueFactory<String, String>("room"));
        section.prefWidthProperty().bind(lectureTable.widthProperty().multiply(1.0 / 4.0));
        days.prefWidthProperty().bind(lectureTable.widthProperty().multiply(1.0 / 4.0));
        time.prefWidthProperty().bind(lectureTable.widthProperty().multiply(1.0 / 4.0));
        room.prefWidthProperty().bind(lectureTable.widthProperty().multiply(1.0 / 4.0));
        section.setCellFactory(TextFieldTableCell.<Lectures>forTableColumn());
        section.setOnEditCommit(
        new EventHandler<CellEditEvent<Lectures, String>>() {
        @Override
        public void handle(CellEditEvent<Lectures, String> t) {
            controller.processEditMTCells(((Lectures) t.getTableView().getItems().get(t.getTablePosition().getRow())
                ), t.getNewValue(), "section");
            
        }
    }
);
        time.setCellFactory(TextFieldTableCell.<Lectures>forTableColumn());
        time.setOnEditCommit(
        new EventHandler<CellEditEvent<Lectures, String>>() {
        @Override
        public void handle(CellEditEvent<Lectures, String> t) {
            controller.processEditMTCells(((Lectures) t.getTableView().getItems().get(t.getTablePosition().getRow())
                ), t.getNewValue(), "time");
        }
    }
);
        days.setCellFactory(TextFieldTableCell.<Lectures>forTableColumn());
        days.setOnEditCommit(
        new EventHandler<CellEditEvent<Lectures, String>>() {
        @Override
        public void handle(CellEditEvent<Lectures, String> t) {
            controller.processEditMTCells(((Lectures) t.getTableView().getItems().get(t.getTablePosition().getRow())
                ), t.getNewValue(), "days");
        }
    }
);
        room.setCellFactory(TextFieldTableCell.<Lectures>forTableColumn());
        room.setOnEditCommit(
        new EventHandler<CellEditEvent<Lectures, String>>() {
        @Override
        public void handle(CellEditEvent<Lectures, String> t) {
            controller.processEditMTCells(((Lectures) t.getTableView().getItems().get(t.getTablePosition().getRow())
                ), t.getNewValue(), "room");
        }
    }
);
        
        
        GridPane recitation=ohBuilder.buildGridPane(MT_RECITATION, meet, CLASS_MT_VBOXES, ENABLED);
        recitation.setPadding(new Insets(10, 10, 10, 10));
        ohBuilder.buildTextButton(MT_RECITATION_ADD_BUTTON, recitation,0,0,1,1 , CLASS_MT_BUTTONS, ENABLED);
        ohBuilder.buildTextButton(MT_RECITATION_MINUS_BUTTON, recitation,1,0,1,1 , CLASS_MT_BUTTONS, ENABLED);
        ohBuilder.buildLabel(MT_RECITATION_HEADER_LABEL, recitation,2,0,2,1 , CLASS_MT_HEADERS, ENABLED);
        TableView recTable = ohBuilder.buildTableView(MT_RECITATION_TABLEVIEW, recitation,0,1,20,10 , CLASS_MT_TABLEVIEW, ENABLED );
        recTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        recitation.prefWidthProperty().bind(meet.widthProperty());
        recTable.prefWidthProperty().bind(lecture.widthProperty());
        TableColumn section1 = ohBuilder.buildTableColumn(MT_RECITATION_SECTION_TABLE_COLUMN,recTable, CLASS_OH_COLUMN);
        TableColumn days_time = ohBuilder.buildTableColumn(MT_RECITATION_DAYS_TIME_TABLE_COLUMN, recTable, CLASS_OH_COLUMN);
        TableColumn room1 = ohBuilder.buildTableColumn(MT_RECITATION_ROOM_TABLE_COLUMN, recTable, CLASS_OH_COLUMN);
        TableColumn ta1 = ohBuilder.buildTableColumn(MT_RECITATION_TA1_TABLE_COLUMN, recTable, CLASS_OH_COLUMN);
        TableColumn ta2 = ohBuilder.buildTableColumn(MT_RECITATION_TA2_TABLE_COLUMN, recTable, CLASS_OH_COLUMN);
        section1.setCellValueFactory(new PropertyValueFactory<String, String>("section"));
        days_time.setCellValueFactory(new PropertyValueFactory<String, String>("daytime"));
        room1.setCellValueFactory(new PropertyValueFactory<String, String>("room"));
        ta1.setCellValueFactory(new PropertyValueFactory<String, String>("ta1"));
        ta2.setCellValueFactory(new PropertyValueFactory<String, String>("ta2"));
        section1.prefWidthProperty().bind(recTable.widthProperty().multiply(1.0 / 5.0));
        days_time.prefWidthProperty().bind(recTable.widthProperty().multiply(1.0 / 5.0));
        room1.prefWidthProperty().bind(recTable.widthProperty().multiply(1.0 / 5.0));
        ta1.prefWidthProperty().bind(recTable.widthProperty().multiply(1.0 / 5.0));
        ta2.prefWidthProperty().bind(recTable.widthProperty().multiply(1.0 / 5.0));
        
        section1.setCellFactory(TextFieldTableCell.<Recitation>forTableColumn());
        section1.setOnEditCommit(
        new EventHandler<CellEditEvent<Recitation, String>>() {
        @Override
        public void handle(CellEditEvent<Recitation, String> t) {
            controller.processEditMTCells1(((Recitation) t.getTableView().getItems().get(t.getTablePosition().getRow())), t.getNewValue(), "section");
            
        }
    }
);
        days_time.setCellFactory(TextFieldTableCell.<Recitation>forTableColumn());
        days_time.setOnEditCommit(
        new EventHandler<CellEditEvent<Recitation, String>>() {
        @Override
        public void handle(CellEditEvent<Recitation, String> t) {
            controller.processEditMTCells1(((Recitation) t.getTableView().getItems().get(t.getTablePosition().getRow())), t.getNewValue(), "daystime");
        }
    }
);
        room1.setCellFactory(TextFieldTableCell.<Recitation>forTableColumn());
        room1.setOnEditCommit(
        new EventHandler<CellEditEvent<Recitation, String>>() {
        @Override
        public void handle(CellEditEvent<Recitation, String> t) {
            controller.processEditMTCells1(((Recitation) t.getTableView().getItems().get(t.getTablePosition().getRow())), t.getNewValue(), "room");
        }
    }
);
        ta1.setCellFactory(TextFieldTableCell.<Recitation>forTableColumn());
        ta1.setOnEditCommit(
        new EventHandler<CellEditEvent<Recitation, String>>() {
        @Override
        public void handle(CellEditEvent<Recitation, String> t) {
            controller.processEditMTCells1(((Recitation) t.getTableView().getItems().get(t.getTablePosition().getRow())), t.getNewValue(), "ta1");
        }
    }
);
        ta2.setCellFactory(TextFieldTableCell.<Recitation>forTableColumn());
        ta2.setOnEditCommit(
        new EventHandler<CellEditEvent<Recitation, String>>() {
        @Override
        public void handle(CellEditEvent<Recitation, String> t) {
            controller.processEditMTCells1(((Recitation) t.getTableView().getItems().get(t.getTablePosition().getRow())), t.getNewValue(), "ta2");
        }
    }
);
        
        GridPane lab=ohBuilder.buildGridPane(MT_LAB, meet,  CLASS_MT_VBOXES, ENABLED);
        lab.setPadding(new Insets(10, 10, 10, 10));
        ohBuilder.buildTextButton(MT_LAB_ADD_BUTTON, lab,0,0,1,1 , CLASS_MT_BUTTONS, ENABLED);
        ohBuilder.buildTextButton(MT_LAB_MINUS_BUTTON, lab,1,0,1,1 , CLASS_MT_BUTTONS, ENABLED);
        ohBuilder.buildLabel(MT_LAB_HEADER_LABEL, lab,2,0,2,1 , CLASS_MT_HEADERS, ENABLED);
        TableView labTable = ohBuilder.buildTableView(MT_LAB_TABLEVIEW, lab,0,1,20,10 , CLASS_MT_TABLEVIEW, ENABLED );
        labTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        lab.prefWidthProperty().bind(meet.widthProperty());
        labTable.prefWidthProperty().bind(lecture.widthProperty());
        TableColumn section2 = ohBuilder.buildTableColumn(MT_LAB_SECTION_TABLE_COLUMN,labTable, CLASS_OH_COLUMN);
        TableColumn days_time2 = ohBuilder.buildTableColumn(MT_LAB_DAYS_TIME_TABLE_COLUMN, labTable, CLASS_OH_COLUMN);
        TableColumn room2 = ohBuilder.buildTableColumn(MT_LAB_ROOM_TABLE_COLUMN, labTable, CLASS_OH_COLUMN);
        TableColumn ta1_2 = ohBuilder.buildTableColumn(MT_LAB_TA1_TABLE_COLUMN, labTable, CLASS_OH_COLUMN);
        TableColumn ta2_2 = ohBuilder.buildTableColumn(MT_LAB_TA2_TABLE_COLUMN, labTable, CLASS_OH_COLUMN);
        section2.setCellValueFactory(new PropertyValueFactory<String, String>("section"));
        days_time2.setCellValueFactory(new PropertyValueFactory<String, String>("daytime"));
        room2.setCellValueFactory(new PropertyValueFactory<String, String>("room"));
        ta1_2.setCellValueFactory(new PropertyValueFactory<String, String>("ta1"));
        ta2_2.setCellValueFactory(new PropertyValueFactory<String, String>("ta2"));
        section2.prefWidthProperty().bind(labTable.widthProperty().multiply(1.0 / 5.0));
        days_time2.prefWidthProperty().bind(labTable.widthProperty().multiply(1.0 / 5.0));
        room2.prefWidthProperty().bind(labTable.widthProperty().multiply(1.0 / 5.0));
        ta1_2.prefWidthProperty().bind(labTable.widthProperty().multiply(1.0 / 5.0));
        ta2_2.prefWidthProperty().bind(labTable.widthProperty().multiply(1.0 / 5.0));
        
        section2.setCellFactory(TextFieldTableCell.<Labs>forTableColumn());
        section2.setOnEditCommit(
        new EventHandler<CellEditEvent<Labs, String>>() {
        @Override
        public void handle(CellEditEvent<Labs, String> t) {
            controller.processEditMTCells2(((Labs) t.getTableView().getItems().get(t.getTablePosition().getRow())), t.getNewValue(), "section");
        }
    }
);
        days_time2.setCellFactory(TextFieldTableCell.<Labs>forTableColumn());
        days_time2.setOnEditCommit(
        new EventHandler<CellEditEvent<Labs, String>>() {
        @Override
        public void handle(CellEditEvent<Labs, String> t) {
            controller.processEditMTCells2(((Labs) t.getTableView().getItems().get(t.getTablePosition().getRow())), t.getNewValue(), "daystime");
        }
    }
);
        room2.setCellFactory(TextFieldTableCell.<Labs>forTableColumn());
        room2.setOnEditCommit(
        new EventHandler<CellEditEvent<Labs, String>>() {
        @Override
        public void handle(CellEditEvent<Labs, String> t) {
            controller.processEditMTCells2(((Labs) t.getTableView().getItems().get(t.getTablePosition().getRow())), t.getNewValue(), "room");
        }
    }
);
        ta1_2.setCellFactory(TextFieldTableCell.<Labs>forTableColumn());
        ta1_2.setOnEditCommit(
        new EventHandler<CellEditEvent<Labs, String>>() {
        @Override
        public void handle(CellEditEvent<Labs, String> t) {
            controller.processEditMTCells2(((Labs) t.getTableView().getItems().get(t.getTablePosition().getRow())), t.getNewValue(), "ta1");
        }
    }
);
        ta2_2.setCellFactory(TextFieldTableCell.<Labs>forTableColumn());
        ta2_2.setOnEditCommit(
        new EventHandler<CellEditEvent<Labs, String>>() {
        @Override
        public void handle(CellEditEvent<Labs, String> t) {
            controller.processEditMTCells2(((Labs) t.getTableView().getItems().get(t.getTablePosition().getRow())), t.getNewValue(), "ta2");
        }
    }
);
     
        
        VBox.setVgrow(meet, Priority.ALWAYS);
        ScrollPane sp2=new ScrollPane(meet);
        meet.prefWidthProperty().bind(sp2.widthProperty());
//schedule
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        VBox schedule=ohBuilder.buildVBox(SCHEDULE_PANE, null, CLASS_OH_PANE, ENABLED);
        schedule.setSpacing(10);
        //calander
        GridPane cb=ohBuilder.buildGridPane(SCHEDULE_CALENDAR, schedule,  CLASS_MT_VBOXES, ENABLED);
        cb.prefWidthProperty().bind(schedule.widthProperty());
        ohBuilder.buildLabel(                  SCHEDULE_CALENDAR_HEADER,            cb, 0,0,1,1 ,   CLASS_MT_HEADERS, ENABLED);
        ohBuilder.buildLabel(                   SPACE_LABEL,                        cb, 0,1,1,1 ,   CLASS_SITE_BANNER_CATEGORIES, ENABLED);
        HBox hb_cb=ohBuilder.buildHBox(        SCHEDULE_CALENDAR_HBOX_START,        cb, 0,2,1,1 ,   CLASS_SCHEDULE_CALENDAR_HBOX_START, ENABLED);
        HBox.setHgrow(ohBuilder.buildLabel(    SCHEDULE_CALENDAR_STARTMONDAY,       hb_cb ,   CLASS_MT_HEADERS, ENABLED), Priority.ALWAYS);
        
        DatePicker start=ohBuilder.buildDatePicker(SCHEDULE_CALENDAR_STARTMONDAY_COMBO, hb_cb,   CLASS_SITE_COMBO, ENABLED);
        LocalDate localDate2 = LocalDate.parse("2018-04-23", formatter);
        start.setValue(localDate2);
        HBox.setHgrow(start, Priority.ALWAYS);
        hb_cb.prefWidthProperty().bind(cb.widthProperty().divide(3));
        
        
        HBox hb_cb1=ohBuilder.buildHBox(       SCHEDULE_CALENDAR_HBOX_END,          cb, 1,2,1,1 ,   CLASS_SCHEDULE_CALENDAR_HBOX_START, ENABLED);
        HBox.setHgrow(ohBuilder.buildLabel(     SCHEDULE_CALENDAR_ENDFRIDAY,         hb_cb1,   CLASS_MT_HEADERS, ENABLED), Priority.ALWAYS);
        DatePicker end=ohBuilder.buildDatePicker(  SCHEDULE_CALENDAR_ENDFRIDAY_COMBO,   hb_cb1,   CLASS_SITE_COMBO, ENABLED);
        HBox.setHgrow(end, Priority.ALWAYS);
        hb_cb1.prefWidthProperty().bind(cb.widthProperty().divide(3));
        LocalDate localDate1 = LocalDate.parse("2018-04-27", formatter);
        end.setValue(localDate1);
        
        
        
        //item
        GridPane si=ohBuilder.buildGridPane(SCHEDULE_ITEM, schedule,  CLASS_MT_VBOXES, ENABLED);
        si.prefWidthProperty().bind(schedule.widthProperty());
        ohBuilder.buildTextButton(SCHEDULE_MINUS_BUTTON, si,0,0,1,1 , CLASS_MT_BUTTONS, ENABLED);
        ohBuilder.buildLabel(SCHEDULE_ITEM_HEADER, si, 1,0,1,1 , CLASS_MT_HEADERS, ENABLED);
        ohBuilder.buildLabel(                   SPACE_LABEL,                        si, 0,1,1,1 ,   CLASS_SITE_BANNER_CATEGORIES, ENABLED);
        TableView siTable = ohBuilder.buildTableView(SCHEDULE_ITEM_TABLEVIEW, si,0,2,20,8 , CLASS_MT_TABLEVIEW, ENABLED );
        siTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        siTable.prefWidthProperty().bind(si.widthProperty());
        TableColumn type = ohBuilder.buildTableColumn(SCHEDULE_TYPE_COLUMN,siTable, CLASS_OH_COLUMN);
        TableColumn date = ohBuilder.buildTableColumn(SCHEDULE_DATE_COLUMN, siTable, CLASS_OH_COLUMN);
        TableColumn title = ohBuilder.buildTableColumn(SCHEDULE_TITLE_COLUMN, siTable, CLASS_OH_COLUMN);
        TableColumn topic = ohBuilder.buildTableColumn(SCHEDULE_TOPIC_COLUMN, siTable, CLASS_OH_COLUMN);
        type.setCellValueFactory(new PropertyValueFactory<String, String>("type"));
        date.setCellValueFactory(new PropertyValueFactory<String, String>("date"));
        title.setCellValueFactory(new PropertyValueFactory<String, String>("title"));
        topic.setCellValueFactory(new PropertyValueFactory<String, String>("topic"));
        
        type.prefWidthProperty().bind(siTable.widthProperty().multiply(1.0 / 4.0));
        date.prefWidthProperty().bind(siTable.widthProperty().multiply(1.0 / 4.0));
        title.prefWidthProperty().bind(siTable.widthProperty().multiply(1.0 / 4.0));
        topic.prefWidthProperty().bind(siTable.widthProperty().multiply(1.0 / 4.0));
        //add and edit
        GridPane ae=ohBuilder.buildGridPane(SCHEDULE_ADD_EDIT, schedule,  CLASS_MT_VBOXES, ENABLED);
        ae.prefWidthProperty().bind(schedule.widthProperty());
        ohBuilder.buildLabel(SCHEDULE_ADD_EDIT_HEADER, ae, 0,0,1,1 , CLASS_MT_HEADERS, ENABLED);
        ohBuilder.buildLabel(                   SPACE_LABEL,                        ae, 0,1,1,1 ,   CLASS_SITE_BANNER_CATEGORIES, ENABLED);
        
        ohBuilder.buildLabel(SCHEDULE_AE_TYPE, ae, 0,2,1,1 , CLASS_MT_HEADERS, ENABLED);
        ohBuilder.buildLabel(                   SPACE_LABEL,                        ae, 0,3,1,1 ,   CLASS_SITE_BANNER_CATEGORIES, ENABLED);
        ohBuilder.buildLabel(SCHEDULE_AE_DATE, ae, 0,4,1,1 , CLASS_MT_HEADERS, ENABLED);
        ohBuilder.buildLabel(                   SPACE_LABEL,                        ae, 0,5,1,1 ,   CLASS_SITE_BANNER_CATEGORIES, ENABLED);
        ohBuilder.buildLabel(SCHEDULE_AE_TITLE, ae, 0,6,1,1 , CLASS_MT_HEADERS, ENABLED);
        ohBuilder.buildLabel(                   SPACE_LABEL,                        ae, 0,7,1,1 ,   CLASS_SITE_BANNER_CATEGORIES, ENABLED);
        ohBuilder.buildLabel(SCHEDULE_AE_TOPIC, ae, 0,8,1,1 , CLASS_MT_HEADERS, ENABLED);
        ohBuilder.buildLabel(                   SPACE_LABEL,                        ae, 0,9,1,1 ,   CLASS_SITE_BANNER_CATEGORIES, ENABLED);
        ohBuilder.buildLabel(SCHEDULE_AE_LINK, ae, 0,10,1,1 , CLASS_MT_HEADERS, ENABLED);
        ohBuilder.buildLabel(                   SPACE_LABEL,                        ae, 0,11,1,1 ,   CLASS_SITE_BANNER_CATEGORIES, ENABLED);
        
        ComboBox sicb=ohBuilder.buildComboBox(SCHEDULE_AE_TYPE_COMBO,ae, 1,2,1,1 , CLASS_SITE_COMBO, ENABLED,"","");
     
        sicb.getItems().add("holidays");
        sicb.getItems().add("lectures");
        sicb.getItems().add("references");
        sicb.getItems().add("recitations");
        sicb.getItems().add("hws");
        sicb.getSelectionModel().select("holidays");
        DatePicker datepick= ohBuilder.buildDatePicker(SCHEDULE_AE_DATE_COMBO,ae, 1,4,1,1 , CLASS_SITE_COMBO, ENABLED);
        
    
        
        
        start.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.getDayOfWeek() == SUNDAY|| date.getDayOfWeek() == TUESDAY|| date.getDayOfWeek() == WEDNESDAY|| date.getDayOfWeek() == THURSDAY|| date.getDayOfWeek() == FRIDAY|| date.getDayOfWeek() ==SATURDAY);
                if(date.isAfter(end.getValue())){
                   setDisable(true);
                }
               // datepick.setValue(start.getValue());
            }
        });
        
        
        end.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.getDayOfWeek() == SUNDAY|| date.getDayOfWeek() == TUESDAY|| date.getDayOfWeek() == WEDNESDAY|| date.getDayOfWeek() == THURSDAY|| date.getDayOfWeek() == MONDAY|| date.getDayOfWeek() ==SATURDAY);
                if(date.isBefore(start.getValue())){
                   setDisable(true);
                }
               
            }
        });
       
        
        
        
        LocalDate localDate = LocalDate.parse("2018-04-23", formatter);
        datepick.setValue(localDate);
        datepick.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if(date.isBefore(start.getValue()) || date.isAfter(end.getValue())){
                    setDisable(true);
                }
                
            }
        });
        
        
        
        datepick.setOnAction(e->{
         controller.processEditScheduleText();}
         
 );
        ohBuilder.buildTextField(SCHEDULE_AE_TITLE_TEXTFIELD, ae,1,6,1,1,CLASS_SITE_INSTRUCTOR_TEXTFIELD, ENABLED);
        ohBuilder.buildTextField(SCHEDULE_AE_TOPIC_TEXTFIELD, ae,1,8,1,1,CLASS_SITE_INSTRUCTOR_TEXTFIELD, ENABLED);
        ohBuilder.buildTextField(SCHEDULE_AE_LINK_TEXTFIELD, ae,1,10,1,1,CLASS_SITE_INSTRUCTOR_TEXTFIELD, ENABLED);
        
        ohBuilder.buildTextButton(SCHEDULE_AE_ADD_UPDATE_BUTTON, ae,0,12,1,1,CLASS_SITE_STYLE_BUTTON,  ENABLED);
        ohBuilder.buildLabel(                   SPACE_LABEL,                        ae, 1,12,1,1 ,   CLASS_SITE_BANNER_CATEGORIES, ENABLED);
        ohBuilder.buildTextButton(SCHEDULE_AE_CLEAR_BUTTON, ae,2,12,1,1,CLASS_SITE_STYLE_BUTTON, ENABLED);
        /////
        ScrollPane sp3=new ScrollPane(schedule);
        schedule.prefWidthProperty().bind(sp3.widthProperty());
/////////////////////////////////////////
        workspace = new BorderPane();
        TabPane x=ohBuilder.buildTabPane(CSG_TABPANE, null, CLASS_CSG_TABPANE, ENABLED);
        
        Tab site=new Tab("Site");
        site.setContent(sp);
        site.setClosable(!ENABLED);
        x.getTabs().add(site);
        
        
        Tab syllabus=new Tab("Syllabus");
        syllabus.setContent(sp1);
        syllabus.setClosable(!ENABLED);
        x.getTabs().add(syllabus);
        
        Tab oh=new Tab("Officehours");
        oh.setContent(spooo);
        oh.setClosable(!ENABLED);
        x.getTabs().add(oh);
        
        Tab mt=new Tab("Meeting Times");
        mt.setContent(sp2);
        mt.setClosable(!ENABLED);
        x.getTabs().add(mt);
        
        Tab sc=new Tab("Schedule");
        sc.setContent(sp3);
        sc.setClosable(!ENABLED);
        x.getTabs().add(sc);
        
        
        // AND PUT EVERYTHING IN THE WORKSPACE
        ((BorderPane) workspace).setCenter(x);
    }
    private void createSyllabusBox(Object b, Object t ,VBox v,Object hb,Object ta){
        AppNodesBuilder ohBuilder = app.getGUIModule().getNodesBuilder();
        VBox vb=ohBuilder.buildVBox(hb, v, CALSS_SYLLABUS_HBOXES, ENABLED);
        ohBuilder.buildIconToggleButton(b,vb,CLASS_SITE_INSTRUCTOR_CATEGORIES ,ENABLED);
        vb.prefWidthProperty().bind(v.widthProperty());
        ohBuilder.buildTextArea(ta, vb, CLASS_SITE_TEXTAREA, ENABLED);
                
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
    

    private void setupOfficeHoursColumn(Object columnId, TableView tableView, String styleClass, String columnDataProperty) {
        AppNodesBuilder builder = app.getGUIModule().getNodesBuilder();
        TableColumn<TeachingAssistantPrototype, String> column = builder.buildTableColumn(columnId, tableView, styleClass);
        column.setCellValueFactory(new PropertyValueFactory<TeachingAssistantPrototype, String>(columnDataProperty));
        column.prefWidthProperty().bind(tableView.widthProperty().multiply(1.0 / 7.0));
        column.setCellFactory(col -> {
            return new TableCell<TeachingAssistantPrototype, String>() {
                @Override
                protected void updateItem(String text, boolean empty) {
                    super.updateItem(text, empty);
                    if (text == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        // CHECK TO SEE IF text CONTAINS THE NAME OF
                        // THE CURRENTLY SELECTED TA
                        setText(text);
                        TableView<TeachingAssistantPrototype> tasTableView = (TableView) app.getGUIModule().getGUINode(OH_TAS_TABLE_VIEW);
                        TeachingAssistantPrototype selectedTA = tasTableView.getSelectionModel().getSelectedItem();
                        if (selectedTA == null) {
                            setStyle("");
                        } else if (text.contains(selectedTA.getName())) {
                            setStyle("-fx-background-color: yellow");
                        } else {
                            setStyle("");
                        }
                    }
                }
            };
        });
    }

    public void initControllers() {
        AppNodesBuilder ohBuilder = app.getGUIModule().getNodesBuilder();
//Office Hours
        CourseSiteGeneratorController controller = new CourseSiteGeneratorController((CourseSiteGeneratorApp) app);
        AppGUIModule gui = app.getGUIModule();

        // FOOLPROOF DESIGN STUFF
        TextField nameTextField = ((TextField) gui.getGUINode(OH_NAME_TEXT_FIELD));
        TextField emailTextField = ((TextField) gui.getGUINode(OH_EMAIL_TEXT_FIELD));

        nameTextField.textProperty().addListener(e -> {
            controller.processTypeTA();
        });
        emailTextField.textProperty().addListener(e -> {
            controller.processTypeTA();
        });
        

        // FIRE THE ADD EVENT ACTION
        nameTextField.setOnAction(e -> {
            controller.processAddTA();
        });
        emailTextField.setOnAction(e -> {
            controller.processAddTA();
        });
        ((Button) gui.getGUINode(OH_ADD_TA_BUTTON)).setOnAction(e -> {
            controller.processAddTA();
        });

        TableView officeHoursTableView = (TableView) gui.getGUINode(OH_OFFICE_HOURS_TABLE_VIEW);
        officeHoursTableView.getSelectionModel().setCellSelectionEnabled(true);
        officeHoursTableView.setOnMouseClicked(e -> {
            controller.processToggleOfficeHours();
        });

        // DON'T LET ANYONE SORT THE TABLES
        TableView tasTableView = (TableView) gui.getGUINode(OH_TAS_TABLE_VIEW);
        for (int i = 0; i < officeHoursTableView.getColumns().size(); i++) {
            ((TableColumn) officeHoursTableView.getColumns().get(i)).setSortable(false);
        }
        for (int i = 0; i < tasTableView.getColumns().size(); i++) {
            ((TableColumn) tasTableView.getColumns().get(i)).setSortable(false);
        }

        tasTableView.setOnMouseClicked(e -> {
            app.getFoolproofModule().updateAll();
            if (e.getClickCount() == 2) {
                controller.processEditTA();
            }
            controller.processSelectTA();
        });

        RadioButton allRadio = (RadioButton) gui.getGUINode(OH_ALL_RADIO_BUTTON);
        allRadio.setOnAction(e -> {
            controller.processSelectAllTAs();
        });
        RadioButton gradRadio = (RadioButton) gui.getGUINode(OH_GRAD_RADIO_BUTTON);
        gradRadio.setOnAction(e -> {
            controller.processSelectGradTAs();
        });
        RadioButton undergradRadio = (RadioButton) gui.getGUINode(OH_UNDERGRAD_RADIO_BUTTON);
        undergradRadio.setOnAction(e -> {
            controller.processSelectUndergradTAs();
        });
        
        ComboBox start=(ComboBox)gui.getGUINode(OH_OFFICE_HOURS_START_COMBO);
        ComboBox end=(ComboBox)gui.getGUINode(OH_OFFICE_HOURS_END_COMBO);
        
        
        start.setOnAction(e->{
            if(start.isFocused())
            controller.processSelectTimeRange();
        });
        end.setOnAction(e->{
            if(end.isFocused())
            controller.processSelectTimeRange();
        });
        
        end.setOnMouseClicked(e->{ 
        controller.processClickonTimeRange(start.getValue().toString(),end.getValue().toString());
        });
        start.setOnMouseClicked(e->{ 
        controller.processClickonTimeRange(start.getValue().toString(),end.getValue().toString());
        });
        
        
        
//Site
    //instructor
        GridPane instructor=(GridPane) gui.getGUINode(SITE_INSTRUCTOR);
        ToggleButton Instructor_OH=(ToggleButton) gui.getGUINode(SITE_INSTRUCTOR_OH_BUTTON);
        TextArea textarea1=(TextArea)gui.getGUINode(SITE_INSTRUCTOR_OH_TEXTAREA);
        
        Instructor_OH.setOnAction(e->{
           CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
           if(Instructor_OH.isSelected()){
            TextArea textarea=(TextArea)gui.getGUINode(SITE_INSTRUCTOR_OH_TEXTAREA);
            instructor.getChildren().add(textarea);
            Instructor_OH.setText("-");
            }
           else{
            TextArea textarea=(TextArea)gui.getGUINode(SITE_INSTRUCTOR_OH_TEXTAREA);
            instructor.getChildren().remove(textarea);
            Instructor_OH.setText("+");
           }
           
        });
        textarea1.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
           
         @Override
         public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {  
                if (newPropertyValue)
                {
                    
                }
                else
                 {
                     if((!((Button)gui.getGUINode(UNDO_BUTTON)).isFocused()&&!((Button)gui.getGUINode(REDO_BUTTON)).isFocused())||textarea1.isFocused()){
                         controller.processEditInstructorOHButton(textarea1.getText());
                        
                     }
                     
                }
            }
        });
      
        
        
        
        
        
    
   
        TextField ins_name=(TextField)gui.getGUINode( SITE_INSTRUCTOR_NAME_TEXTFIELD);
        TextField ins_email=(TextField)gui.getGUINode( SITE_INSTRUCTOR_EMAIL_TEXTFIELD);
        TextField ins_room=(TextField)gui.getGUINode( SITE_INSTRUCTOR_ROOM_TEXTFIELD);
        TextField ins_homepage=(TextField)gui.getGUINode( SITE_INSTRUCTOR_HOMEPAGE_TEXTFIELD);
        ins_name.setOnAction(e->{
            if(ins_name.isFocused()||(!((Button)gui.getGUINode(UNDO_BUTTON)).isFocused()&&!((Button)gui.getGUINode(REDO_BUTTON)).isFocused()))
            controller.processEditInstructor(ins_name.getText(), ins_email.getText(),ins_room.getText(), ins_homepage.getText());
        });
        
        
        ins_name.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
         @Override
         public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (newPropertyValue)
                {
                    
                }
                else
                 {
                     if(ins_name.isFocused()||(!((Button)gui.getGUINode(UNDO_BUTTON)).isFocused()&&!((Button)gui.getGUINode(REDO_BUTTON)).isFocused()))
                     controller.processEditInstructor(ins_name.getText(), ins_email.getText(),ins_room.getText(), ins_homepage.getText());
                     
                }
            }
        });
        ins_email.setOnAction(e->{
            if(ins_email.isFocused()||(!((Button)gui.getGUINode(UNDO_BUTTON)).isFocused()&&!((Button)gui.getGUINode(REDO_BUTTON)).isFocused()))
            controller.processEditInstructor(ins_name.getText(), ins_email.getText(),ins_room.getText(), ins_homepage.getText());
        });
        
        
        ins_email.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
         @Override
         public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (newPropertyValue)
                {
                    
                }
                else
                 {
                     if(ins_email.isFocused()||(!((Button)gui.getGUINode(UNDO_BUTTON)).isFocused()&&!((Button)gui.getGUINode(REDO_BUTTON)).isFocused()))
                     controller.processEditInstructor(ins_name.getText(), ins_email.getText(),ins_room.getText(), ins_homepage.getText());
                     
                }
            }
        });
        ins_room.setOnAction(e->{
            if(ins_room.isFocused()||(!((Button)gui.getGUINode(UNDO_BUTTON)).isFocused()&&!((Button)gui.getGUINode(REDO_BUTTON)).isFocused()))
            controller.processEditInstructor(ins_name.getText(), ins_email.getText(),ins_room.getText(), ins_homepage.getText());
        });
        
        ins_room.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
         @Override
         public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (newPropertyValue)
                {
                    
                }
                else
                 {
                     if(ins_room.isFocused()||(!((Button)gui.getGUINode(UNDO_BUTTON)).isFocused()&&!((Button)gui.getGUINode(REDO_BUTTON)).isFocused()))
                     controller.processEditInstructor(ins_name.getText(), ins_email.getText(),ins_room.getText(), ins_homepage.getText());
                     
                }
            }
        });
        ins_homepage.setOnAction(e->{
            if(ins_homepage.isFocused()||(!((Button)gui.getGUINode(UNDO_BUTTON)).isFocused()&&!((Button)gui.getGUINode(REDO_BUTTON)).isFocused()))
            controller.processEditInstructor(ins_name.getText(), ins_email.getText(),ins_room.getText(), ins_homepage.getText());
        });
       
        ins_homepage.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
         @Override
         public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (newPropertyValue)
                {
                    
                }
                else
                 {
                     if(ins_homepage.isFocused()||(!((Button)gui.getGUINode(UNDO_BUTTON)).isFocused()&&!((Button)gui.getGUINode(REDO_BUTTON)).isFocused()))
                     controller.processEditInstructor(ins_name.getText(), ins_email.getText(),ins_room.getText(), ins_homepage.getText());
                     
                }
            }
        });
        
      
      //style
        
        Button favicon=(Button) gui.getGUINode(SITE_STYLE_FAVICON_BUTTON);
        Button navbar=(Button) gui.getGUINode(SITE_STYLE_NAVBAR_BUTTON);
        Button leftfooter=(Button) gui.getGUINode(SITE_STYLE_LEFTFOOTER_BUTTON);
        Button rightfooter=(Button) gui.getGUINode(SITE_STYLE_RIGHTFOOTER_BUTTON);
        ImageView imageview_f=(ImageView)gui.getGUINode(SITE_STYLE_FAVICON_IMAGE);
        ImageView imageview_n=(ImageView)gui.getGUINode(SITE_STYLE_NAVBAR_IMAGE);
        ImageView imageview_l=(ImageView)gui.getGUINode(SITE_STYLE_LEFTFOOTER_IMAGE);
        ImageView imageview_r=(ImageView)gui.getGUINode(SITE_STYLE_RIGHTFOOTER_IMAGE);
        favicon.setOnAction(e->{  
            
            try {
             CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Resource File");
                File fc=fileChooser.showOpenDialog(new Stage());
                File xx= new File(data.convertPath(fc.getPath()));
                xx.createNewFile();
                File xy=new File("./export"+data.convertPath(fc.getPath()).substring(1));
                xy.createNewFile();
                BufferedImage bufferedImage = ImageIO.read(fc);
                Image bannerImage = SwingFXUtils.toFXImage(bufferedImage, null);
                ImageIO.write(bufferedImage, "png", xx);
                ImageIO.write(bufferedImage, "png", xy);
                
                if(!data.convertPath(data.getFavicon_path()).equals(data.convertPath(fc.getPath())))
                controller.processEditStyle( "favicon",fc.getPath());
                
            } catch (Exception ex) {
                
            }
            
            
            
        
        });
        navbar.setOnAction(e->{   
        
            try {
               CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Resource File");
                File fc=fileChooser.showOpenDialog(new Stage());
                File xx= new File(data.convertPath(fc.getPath()));
                xx.createNewFile();
                File xy=new File("./export"+data.convertPath(fc.getPath()).substring(1));
                xy.createNewFile();
                BufferedImage bufferedImage = ImageIO.read(fc);
                Image bannerImage = SwingFXUtils.toFXImage(bufferedImage, null);
                ImageIO.write(bufferedImage, "png", xx);
                ImageIO.write(bufferedImage, "png", xy);
                
                if(!data.convertPath(data.getnavbar_path()).equals(data.convertPath(fc.getPath())))
                controller.processEditStyle("navbar",fc.getPath());
            } catch (Exception ex) {
                
            }
        
        
        });
        leftfooter.setOnAction(e->{   
        
            try {
               CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Resource File");
                File fc=fileChooser.showOpenDialog(new Stage());
                File xx= new File(data.convertPath(fc.getPath()));
                xx.createNewFile();
                File xy=new File("./export"+data.convertPath(fc.getPath()).substring(1));
                xy.createNewFile();
                BufferedImage bufferedImage = ImageIO.read(fc);
                Image bannerImage = SwingFXUtils.toFXImage(bufferedImage, null);
                ImageIO.write(bufferedImage, "png", xx);
                ImageIO.write(bufferedImage, "png", xy);
                if(!data.convertPath(data.getleftfooter_path()).equals(data.convertPath(fc.getPath())))
                controller.processEditStyle( "leftfooter",fc.getPath());
            } catch (Exception ex) {
                
            }
        
        
        });
        rightfooter.setOnAction(e->{   
        
           try {
                CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Resource File");
                File fc=fileChooser.showOpenDialog(new Stage());
                File xx= new File(data.convertPath(fc.getPath()));
                xx.createNewFile();
                File xy=new File("./export"+data.convertPath(fc.getPath()).substring(1));
                xy.createNewFile();
                BufferedImage bufferedImage = ImageIO.read(fc);
                Image bannerImage = SwingFXUtils.toFXImage(bufferedImage, null);
                ImageIO.write(bufferedImage, "png", xx);
                ImageIO.write(bufferedImage, "png", xy);
                if(!data.convertPath(data.getrightfooter_path()).equals(data.convertPath(fc.getPath())))
                controller.processEditStyle( "rightfooter",fc.getPath());
            } catch (Exception ex) {
                
            }
        
        
        });
      //banner
        ComboBox subject=(ComboBox)gui.getGUINode(SITE_BANNER_SUBJECT_COMBO);
        ComboBox smester=(ComboBox)gui.getGUINode(SITE_BANNER_SMESTER_COMBO);
        ComboBox num=(ComboBox)gui.getGUINode(SITE_BANNER_NUMBER_COMBO);
        ComboBox years=(ComboBox)gui.getGUINode(SITE_BANNER_YEAR_COMBO);
        Label exportdir=(Label)gui.getGUINode(SITE_BANNER_EXPORTDIR);
        TextField textfield_banner=(TextField)gui.getGUINode(SITE_BANNER_TITLE_TEXTFIELD);
       
        subject.setOnAction(e->{
            if(subject.isFocused()||(!((Button)gui.getGUINode(UNDO_BUTTON)).isFocused()&&!((Button)gui.getGUINode(REDO_BUTTON)).isFocused()))
            {  if(!((Button)gui.getGUINode(LOAD_BUTTON)).isFocused())
                 controller.processEditBanner(subject.getValue().toString(),textfield_banner.getText().toString(),smester.getValue().toString(),num.getValue().toString(),years.getValue().toString());
            }
             if(!subject.getItems().contains(subject.getValue().toString())){
             subject.getItems().add(subject.getValue().toString());}
            
        });
        smester.setOnAction(e->{
            if(smester.isFocused()||(!((Button)gui.getGUINode(UNDO_BUTTON)).isFocused()&&!((Button)gui.getGUINode(REDO_BUTTON)).isFocused()))
            {if(!((Button)gui.getGUINode(LOAD_BUTTON)).isFocused())
            controller.processEditBanner(subject.getValue().toString(),textfield_banner.getText().toString(),smester.getValue().toString(),num.getValue().toString(),years.getValue().toString());
            }
             
            if(!smester.getItems().contains(smester.getValue().toString())){
             smester.getItems().add(smester.getValue().toString());}
        });
        num.setOnAction(e->{
            if(num.isFocused()||(!((Button)gui.getGUINode(UNDO_BUTTON)).isFocused()&&!((Button)gui.getGUINode(REDO_BUTTON)).isFocused()))
            {if(!((Button)gui.getGUINode(LOAD_BUTTON)).isFocused())
                controller.processEditBanner(subject.getValue().toString(),textfield_banner.getText().toString(),smester.getValue().toString(),num.getValue().toString(),years.getValue().toString());
            }
            if(!num.getItems().contains(num.getValue().toString())){
             num.getItems().add(num.getValue().toString());}
        });
        years.setOnAction(e->{
            if(years.isFocused()||(!((Button)gui.getGUINode(UNDO_BUTTON)).isFocused()&&!((Button)gui.getGUINode(REDO_BUTTON)).isFocused()))
             if(!((Button)gui.getGUINode(LOAD_BUTTON)).isFocused())
             {controller.processEditBanner(subject.getValue().toString(),textfield_banner.getText().toString(),smester.getValue().toString(),num.getValue().toString(),years.getValue().toString());
             }
            if(!years.getItems().contains(years.getValue().toString())){
             years.getItems().add(years.getValue().toString());}
        });
        
        textfield_banner.setOnAction(e->{
            if(textfield_banner.isFocused()||(!((Button)gui.getGUINode(UNDO_BUTTON)).isFocused()&&!((Button)gui.getGUINode(REDO_BUTTON)).isFocused()))
            controller.processEditBanner(subject.getValue().toString(),textfield_banner.getText().toString(),smester.getValue().toString(),num.getValue().toString(),years.getValue().toString());
            
        });
        
        textfield_banner.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
         @Override
         public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {   
                if (newPropertyValue)
                {
                   
                }
                else
                 {
                     if(textfield_banner.isFocused()||(!((Button)gui.getGUINode(UNDO_BUTTON)).isFocused()&&!((Button)gui.getGUINode(REDO_BUTTON)).isFocused()))
                     { if(!((Button)gui.getGUINode(LOAD_BUTTON)).isFocused())
                         controller.processEditBanner(subject.getValue().toString(),textfield_banner.getText().toString(),smester.getValue().toString(),num.getValue().toString(),years.getValue().toString());
                     }
                }
            }
        });
      //pages
        CheckBox homecheck=(CheckBox)gui.getGUINode(SITE_PAGES_HOME_CHECKBOX);
        CheckBox syllabuscheck=(CheckBox)gui.getGUINode(SITE_PAGES_SYLLABUS_CHECKBOX);
        CheckBox schedulecheck=(CheckBox)gui.getGUINode(SITE_PAGES_SCHEDULE_CHECKBOX);
        CheckBox hwcheck=(CheckBox)gui.getGUINode(SITE_PAGES_HW_CHECKBOX);
        homecheck.setSelected(ENABLED);
        syllabuscheck.setSelected(ENABLED);
        schedulecheck.setSelected(ENABLED);
        hwcheck.setSelected(ENABLED);
        
        homecheck.setOnAction(e->{
            if(homecheck.isFocused())
                controller.processEditPages(homecheck.isSelected(), syllabuscheck.isSelected(), schedulecheck.isSelected(), hwcheck.isSelected(), "home");
        });
        syllabuscheck.setOnAction(e->{
            if(syllabuscheck.isFocused())
                 controller.processEditPages(homecheck.isSelected(), syllabuscheck.isSelected(), schedulecheck.isSelected(), hwcheck.isSelected(), "syllabus");
        });
        schedulecheck.setOnAction(e->{
            if(schedulecheck.isFocused())
                 controller.processEditPages(homecheck.isSelected(), syllabuscheck.isSelected(), schedulecheck.isSelected(), hwcheck.isSelected(), "schedule");
        });
        hwcheck.setOnAction(e->{
            if(hwcheck.isFocused())
                 controller.processEditPages(homecheck.isSelected(), syllabuscheck.isSelected(), schedulecheck.isSelected(), hwcheck.isSelected(), "hw");
        });
        
        
//Syllabus
        VBox VBox_Desciprtion=(VBox) gui.getGUINode(SYLLABUS_VBOXES1);
        VBox VBox_Topic=(VBox) gui.getGUINode(SYLLABUS_VBOXES2);
        VBox VBox_Prerequisite=(VBox) gui.getGUINode(SYLLABUS_VBOXES3);
        VBox VBox_OutComes=(VBox) gui.getGUINode(SYLLABUS_VBOXES4);
        VBox VBox_Textbook=(VBox) gui.getGUINode(SYLLABUS_VBOXES5);
        VBox VBox_GC=(VBox) gui.getGUINode(SYLLABUS_VBOXES6);
        VBox VBox_GN=(VBox) gui.getGUINode(SYLLABUS_VBOXES7);
        VBox VBox_AD=(VBox) gui.getGUINode(SYLLABUS_VBOXES8);
        VBox VBox_SA=(VBox) gui.getGUINode(SYLLABUS_VBOXES9);
        
        ToggleButton t1=(ToggleButton)gui.getGUINode(SYLLABUS_DESCRIPTION);
        ToggleButton t2=(ToggleButton)gui.getGUINode(SYLLABUS_TOPICS);
        ToggleButton t3=(ToggleButton)gui.getGUINode(SYLLABUS_PREREQUISITES);
        ToggleButton t4=(ToggleButton)gui.getGUINode(SYLLABUS_OUTCOMES);
        ToggleButton t5=(ToggleButton)gui.getGUINode(SYLLABUS_TEXTBOOKS);
        ToggleButton t6=(ToggleButton)gui.getGUINode(SYLLABUS_GC);
        ToggleButton t7=(ToggleButton)gui.getGUINode(SYLLABUS_GN);
        ToggleButton t8=(ToggleButton)gui.getGUINode(SYLLABUS_AD);
        ToggleButton t9=(ToggleButton)gui.getGUINode(SYLLABUS_SA);
        
        TextArea  tx1=(TextArea)gui.getGUINode(SYLLABUS_DESCRIPTION_TEXTFIELD);
        TextArea  tx2=(TextArea)gui.getGUINode(SYLLABUS_TOPICS_TEXTFIELD);
        TextArea  tx3=(TextArea)gui.getGUINode(SYLLABUS_PREREQUISITES_TEXTFIELD);
        TextArea  tx4=(TextArea)gui.getGUINode(SYLLABUS_OUTCOMES_TEXTFIELD);
        TextArea  tx5=(TextArea)gui.getGUINode(SYLLABUS_TEXTBOOKS_TEXTFIELD);
        TextArea  tx6=(TextArea)gui.getGUINode(SYLLABUS_GC_TEXTFIELD);
        TextArea  tx7=(TextArea)gui.getGUINode(SYLLABUS_GN_TEXTFIELD);
        TextArea  tx8=(TextArea)gui.getGUINode(SYLLABUS_AD_TEXTFIELD);
        TextArea  tx9=(TextArea)gui.getGUINode(SYLLABUS_SA_TEXTFIELD);
        t1.setOnAction(e->{
            if(t1.isSelected()){
                VBox_Desciprtion.getChildren().add(tx1);
            }
            else{
                VBox_Desciprtion.getChildren().remove(tx1);
            }
        });
        t2.setOnAction(e->{
            if(t2.isSelected()){
                VBox_Topic.getChildren().add(tx2);
            
            }
            else{
                VBox_Topic.getChildren().remove(tx2);
            }
            
        });
        t3.setOnAction(e->{
            if(t3.isSelected()){
                VBox_Prerequisite.getChildren().add(tx3);
            }
            else{
                VBox_Prerequisite.getChildren().remove(tx3);
            }
            
        });
        t4.setOnAction(e->{
            if(t4.isSelected()){
                VBox_OutComes.getChildren().add(tx4);
            }
            else{
              VBox_OutComes.getChildren().remove(tx4);
            }
            
        });
        t5.setOnAction(e->{
            if(t5.isSelected()){
                VBox_Textbook.getChildren().add(tx5);
            }
            else{
                VBox_Textbook.getChildren().remove(tx5);
            }
           
        });
        t6.setOnAction(e->{
            if(t6.isSelected()){
                VBox_GC.getChildren().add(tx6);
            }
            else{
                 VBox_GC.getChildren().remove(tx6);
            }
            
        });
        t7.setOnAction(e->{
            if(t7.isSelected()){
                VBox_GN.getChildren().add(tx7);
            }
            else{
                VBox_GN.getChildren().remove(tx7);
            }
           
        });
        t8.setOnAction(e->{
            if(t8.isSelected()){
                VBox_AD.getChildren().add(tx8);
            }
            else{
                 VBox_AD.getChildren().remove(tx8);
            }
            
        });
        t9.setOnAction(e->{
            if(t9.isSelected()){
                VBox_SA.getChildren().add(tx9);
            }
            else{
                VBox_SA.getChildren().remove(tx9);
            }
           
        });
          
        tx1.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
           
         @Override
         public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {  
                if (newPropertyValue)
                {
                    
                }
                else
                 {
                     if((!((Button)gui.getGUINode(UNDO_BUTTON)).isFocused()&&!((Button)gui.getGUINode(REDO_BUTTON)).isFocused())&&!tx1.isFocused()){
                         controller.processEditSyllabus(tx1.getText(),"description");
                     }
                }
            }
        });
        tx2.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
           
         @Override
         public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {  
                if (newPropertyValue)
                {
                    
                }
                else
                 {
                     if((!((Button)gui.getGUINode(UNDO_BUTTON)).isFocused()&&!((Button)gui.getGUINode(REDO_BUTTON)).isFocused())&&!tx2.isFocused()){
                         controller.processEditSyllabus(tx2.getText(),"topics");
                     }
                }
            }
        });
        tx3.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
           
         @Override
         public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {  
                if (newPropertyValue)
                {
                    
                }
                else
                 {
                     if((!((Button)gui.getGUINode(UNDO_BUTTON)).isFocused()&&!((Button)gui.getGUINode(REDO_BUTTON)).isFocused())&&!tx3.isFocused()){
                         controller.processEditSyllabus(tx3.getText(),"prerequisites");
                     }
                }
            }
        });
        tx4.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
           
         @Override
         public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {  
                if (newPropertyValue)
                {
                    
                }
                else
                 {
                     if((!((Button)gui.getGUINode(UNDO_BUTTON)).isFocused()&&!((Button)gui.getGUINode(REDO_BUTTON)).isFocused())&&!tx4.isFocused()){
                         controller.processEditSyllabus(tx4.getText(),"outcomes");
                     }
                }
            }
        });
        tx5.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
           
         @Override
         public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {  
                if (newPropertyValue)
                {
                    
                }
                else
                 {
                     if((!((Button)gui.getGUINode(UNDO_BUTTON)).isFocused()&&!((Button)gui.getGUINode(REDO_BUTTON)).isFocused())&&!tx5.isFocused()){
                         controller.processEditSyllabus(tx5.getText(),"textbooks");
                     }
                }
            }
        });
        tx6.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
           
         @Override
         public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {  
                if (newPropertyValue)
                {
                    
                }
                else
                 {
                     if((!((Button)gui.getGUINode(UNDO_BUTTON)).isFocused()&&!((Button)gui.getGUINode(REDO_BUTTON)).isFocused())&&!tx6.isFocused()){
                         controller.processEditSyllabus(tx6.getText(),"gradedComponents");
                     }
                }
            }
        });
        tx7.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
           
         @Override
         public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {  
                if (newPropertyValue)
                {
                    
                }
                else
                 {
                     if((!((Button)gui.getGUINode(UNDO_BUTTON)).isFocused()&&!((Button)gui.getGUINode(REDO_BUTTON)).isFocused())&&!tx7.isFocused()){
                         controller.processEditSyllabus(tx7.getText(),"gradingNote");
                     }
                }
            }
        });
        tx8.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
           
         @Override
         public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {  
                if (newPropertyValue)
                {
                    
                }
                else
                 {   
                     if((!((Button)gui.getGUINode(UNDO_BUTTON)).isFocused()&&!((Button)gui.getGUINode(REDO_BUTTON)).isFocused())||tx8.isFocused()){
                         controller.processEditSyllabus(tx8.getText(),"academicDishonesty");
                     }
                }
            }
        });
        tx9.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
           
         @Override
         public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {  
                if (newPropertyValue)
                {
                    
                }
                else
                 {
                     if(!tx9.isFocused()){
                         controller.processEditSyllabus(tx9.getText(),"specialAssistance");
                     }
                }
            }
        });
        


//Meeting Time
    TableView  tablelecture=(TableView)gui.getGUINode(MT_LECTURE_TABLEVIEW);
    TableView  tablerec=(TableView)gui.getGUINode(MT_RECITATION_TABLEVIEW);
    TableView  tablelab=(TableView)gui.getGUINode( MT_LAB_TABLEVIEW);
    
    tablelecture.getSelectionModel().setCellSelectionEnabled(true);
    tablelecture.setEditable(true);
    tablerec.getSelectionModel().setCellSelectionEnabled(true);
    tablerec.setEditable(true);
    tablelab.getSelectionModel().setCellSelectionEnabled(true);
    tablelab.setEditable(true);
    
   
    Button  Addlecture=(Button)gui.getGUINode( MT_LECTURE_ADD_BUTTON);
    Button  Removelecture=(Button)gui.getGUINode(MT_LECTURE_MINUS_BUTTON);
    Button  Addrec=(Button)gui.getGUINode(MT_RECITATION_ADD_BUTTON);
    Button  Removerec=(Button)gui.getGUINode(MT_RECITATION_MINUS_BUTTON);
    Button  Addlab=(Button)gui.getGUINode(MT_LAB_ADD_BUTTON);
    Button  Removelab=(Button)gui.getGUINode(MT_LAB_MINUS_BUTTON);
    
    Addlecture.setOnAction(e->{
        controller.processAddLecture();
    });
    Removelecture.setOnAction(e->{
        controller.processRemoveLecture((Lectures) tablelecture.getSelectionModel().getSelectedItem());
    });
    Addrec.setOnAction(e->{
        controller.processAddRecitation();
    });
    Removerec.setOnAction(e->{
        controller.processRemoveRecitation((Recitation) tablerec.getSelectionModel().getSelectedItem());
    });
    Addlab.setOnAction(e->{
        controller.processAddLabs();
    });
    Removelab.setOnAction(e->{
        controller.processRemoveLabs((Labs) tablelab.getSelectionModel().getSelectedItem());
    });
    
    
    
    

//Schedule
    TableView  scheduleTable=(TableView)gui.getGUINode(SCHEDULE_ITEM_TABLEVIEW);
    Button  editadd=(Button)gui.getGUINode( SCHEDULE_AE_ADD_UPDATE_BUTTON);
    Button  clear=(Button)gui.getGUINode(SCHEDULE_AE_CLEAR_BUTTON);
    Button  SIbutton=(Button)gui.getGUINode(SCHEDULE_MINUS_BUTTON);
    TextField title=(TextField)gui.getGUINode(SCHEDULE_AE_TITLE_TEXTFIELD);
    TextField topic=(TextField)gui.getGUINode(SCHEDULE_AE_TOPIC_TEXTFIELD);
    TextField link=(TextField)gui.getGUINode( SCHEDULE_AE_LINK_TEXTFIELD);
    DatePicker date=(DatePicker)gui.getGUINode(SCHEDULE_AE_DATE_COMBO);
    ComboBox type=(ComboBox)gui.getGUINode(SCHEDULE_AE_TYPE_COMBO);
    DatePicker startm=(DatePicker)gui.getGUINode(SCHEDULE_CALENDAR_STARTMONDAY_COMBO);
    DatePicker endf=(DatePicker)gui.getGUINode(SCHEDULE_CALENDAR_ENDFRIDAY_COMBO);
    
    startm.setOnAction(e->{
        if(startm.isFocused()&&(!((Button)gui.getGUINode(UNDO_BUTTON)).isFocused()&&!((Button)gui.getGUINode(REDO_BUTTON)).isFocused())){
           controller.processEditStartEnd();
        }
        
    
    });
    
    endf.setOnAction(e->{
    
        if(endf.isFocused()&&(!((Button)gui.getGUINode(UNDO_BUTTON)).isFocused()&&!((Button)gui.getGUINode(REDO_BUTTON)).isFocused())){
           controller.processEditStartEnd();
        }
    
    });
   
    title.setOnAction(e->{
        controller.processEditScheduleText();
    });
    topic.setOnAction(e->{
        controller.processEditScheduleText();
    });
    link.setOnAction(e->{
        controller.processEditScheduleText();
    });
    
    title.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
           
         @Override
         public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {  
                if (newPropertyValue)
                {
                    
                }
                else
                 {
                     if((!((Button)gui.getGUINode(UNDO_BUTTON)).isFocused()&&!((Button)gui.getGUINode(REDO_BUTTON)).isFocused())&&!title.isFocused()){
                         controller.processEditScheduleText();
                     }
                }
            }
        });
    topic.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
           
         @Override
         public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {  
                if (newPropertyValue)
                {
                    
                }
                else
                 {
                     if((!((Button)gui.getGUINode(UNDO_BUTTON)).isFocused()&&!((Button)gui.getGUINode(REDO_BUTTON)).isFocused())&&!topic.isFocused()){
                         controller.processEditScheduleText();
                     }
                }
            }
        });
    link.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
           
         @Override
         public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {  
                if (newPropertyValue)
                {
                    
                }
                else
                 {
                     if((!((Button)gui.getGUINode(UNDO_BUTTON)).isFocused()&&!((Button)gui.getGUINode(REDO_BUTTON)).isFocused())&&!link.isFocused()){
                         controller.processEditScheduleText();
                     }
                }
            }
        });
    
    type.setOnAction(e->{
        if(type.isFocused()||(!((Button)gui.getGUINode(UNDO_BUTTON)).isFocused()&&!((Button)gui.getGUINode(REDO_BUTTON)).isFocused()))
        {  if(!((Button)gui.getGUINode(LOAD_BUTTON)).isFocused())
         controller.processEditScheduleText();}
    });
    Button updateadd=(Button)gui.getGUINode(SCHEDULE_AE_ADD_UPDATE_BUTTON);
  
    scheduleTable.setOnMouseClicked(e->{
        if(scheduleTable.getSelectionModel().getSelectedItem()!=null){
            ScheduleItem xx=(ScheduleItem) scheduleTable.getSelectionModel().getSelectedItem();
            title.setText(xx.getTitle());
            topic.setText(xx.getTopic());
            link.setText(xx.getLink());
            date.setValue(xx.getDate_l());
            type.setValue(xx.getType());
            
            controller.processEditScheduleText();
           
             Platform.runLater(new Runnable() {
        @Override
        public void run() {
            updateadd.setText("Edit");
        }
        });
        
        }
        else{
             Platform.runLater(new Runnable() {
        @Override
        public void run() {
            updateadd.setText("Add");
        }
        });
        
        
        }
    
    
    });
    
   
    
   
    
    
    editadd.setOnAction(e->{
      //add or edit
      if(scheduleTable.getSelectionModel().getSelectedItem()!=null){
          //edit
          controller.processAdd_Edit_ScheduleItem("edit");
          
          
      }
      else{
          controller.processAdd_Edit_ScheduleItem("add");
          
      }
    });
    clear.setOnAction(e->{
        scheduleTable.getSelectionModel().clearSelection();
        controller.processClearScheudle();
         Platform.runLater(new Runnable() {
        @Override
        public void run() {
            updateadd.setText("Add");
        }
        });
        
    });
    SIbutton.setOnAction(e->{
        if(scheduleTable.getSelectionModel().getSelectedItem()!=null){
         
        controller.processRemoveScheduleItems();}
    });
    
    
    ComboBox cssbox=(ComboBox)gui.getGUINode(SITE_STYLE_SHEET_COMBO);
    
    cssbox.setOnAction(e->{
        if(cssbox.isFocused()&&(!((Button)gui.getGUINode(UNDO_BUTTON)).isFocused()&&!((Button)gui.getGUINode(REDO_BUTTON)).isFocused())){
            controller.processCSSCombox();
        }
        
    });
    
    
    }

    public void initFoolproofDesign() {
        AppGUIModule gui = app.getGUIModule();
        AppFoolproofModule foolproofSettings = app.getFoolproofModule();
        foolproofSettings.registerModeSettings(OH_FOOLPROOF_SETTINGS,
                new CourseSiteGeneratorFoolproofDesign((CourseSiteGeneratorApp) app));
    }

    @Override
    public void processWorkspaceKeyEvent(KeyEvent ke) {
        // WE AREN'T USING THIS FOR THIS APPLICATION
    }

    @Override
    public void showNewDialog() {
        // WE AREN'T USING THIS FOR THIS APPLICATION
    }
    

   
}
