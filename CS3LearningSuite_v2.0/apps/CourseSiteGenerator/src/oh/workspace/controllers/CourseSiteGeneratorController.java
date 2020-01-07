package oh.workspace.controllers;

import djf.modules.AppGUIModule;
import djf.ui.dialogs.AppDialogsFacade;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import jtps.jTPS_Transaction;
import oh.CourseSiteGeneratorApp;
import static oh.CourseSiteGeneratorPropertyType.*;
import oh.data.CourseSiteGeneratorData;
import oh.data.Labs;
import oh.data.Lectures;
import oh.data.Recitation;
import oh.data.TAType;
import oh.data.TeachingAssistantPrototype;
import oh.data.TimeSlot;
import oh.data.TimeSlot.DayOfWeek;
import oh.transactions.AddTA_Transaction;
import oh.transactions.EditBanner_Transaction;
import oh.transactions.EditImages_Transaction;
import oh.transactions.*;

import oh.transactions.EditPages_Transaction;
import oh.transactions.EditTA_Transaction;
import oh.transactions.SelectTimeRange_Transaction;
import oh.transactions.ToggleOfficeHours_Transaction;
import oh.workspace.dialogs.TADialog;

/**
 *
 * @author McKillaGorilla
 */
public class CourseSiteGeneratorController {

    CourseSiteGeneratorApp app;
    String start_a;
    String start_b;
    
    
    public CourseSiteGeneratorController(CourseSiteGeneratorApp initApp) {
        app = initApp;
    }

    public void processAddTA() {
        AppGUIModule gui = app.getGUIModule();
        TextField nameTF = (TextField) gui.getGUINode(OH_NAME_TEXT_FIELD);
        String name = nameTF.getText();
        TextField emailTF = (TextField) gui.getGUINode(OH_EMAIL_TEXT_FIELD);
        String email = emailTF.getText();
        CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
        TAType type = data.getSelectedType();
        if (data.isLegalNewTA(name, email)) {
            TeachingAssistantPrototype ta = new TeachingAssistantPrototype(name.trim(), email.trim(), type);
            AddTA_Transaction addTATransaction = new AddTA_Transaction(data, ta);
            app.processTransaction(addTATransaction);

            // NOW CLEAR THE TEXT FIELDS
            nameTF.setText("");
            emailTF.setText("");
            nameTF.requestFocus();
        }
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processVerifyTA() {

    }

    public void processToggleOfficeHours() {
        AppGUIModule gui = app.getGUIModule();
        TableView<TimeSlot> officeHoursTableView = (TableView) gui.getGUINode(OH_OFFICE_HOURS_TABLE_VIEW);
        ObservableList<TablePosition> selectedCells = officeHoursTableView.getSelectionModel().getSelectedCells();
        if (selectedCells.size() > 0) {
            TablePosition cell = selectedCells.get(0);
            int cellColumnNumber = cell.getColumn();
            CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
            if (data.isDayOfWeekColumn(cellColumnNumber)) {
                DayOfWeek dow = data.getColumnDayOfWeek(cellColumnNumber);
                TableView<TeachingAssistantPrototype> taTableView = (TableView)gui.getGUINode(OH_TAS_TABLE_VIEW);
                TeachingAssistantPrototype ta = taTableView.getSelectionModel().getSelectedItem();
                if (ta != null) {
                    TimeSlot timeSlot = officeHoursTableView.getSelectionModel().getSelectedItem();
                    ToggleOfficeHours_Transaction transaction = new ToggleOfficeHours_Transaction(data, timeSlot, dow, ta);
                    app.processTransaction(transaction);
                }
                else {
                    Stage window = app.getGUIModule().getWindow();
                    AppDialogsFacade.showMessageDialog(window, OH_NO_TA_SELECTED_TITLE, OH_NO_TA_SELECTED_CONTENT);
                }
            }
            int row = cell.getRow();
            cell.getTableView().refresh();
        }
    }

    public void processTypeTA() {
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processEditTA() {
        CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
        if (data.isTASelected()) {
            TeachingAssistantPrototype taToEdit = data.getSelectedTA();
            TADialog taDialog = (TADialog)app.getGUIModule().getDialog(OH_TA_EDIT_DIALOG);
            taDialog.showEditDialog(taToEdit);
            TeachingAssistantPrototype editTA = taDialog.getEditTA();
            if (editTA != null) {
                EditTA_Transaction transaction = new EditTA_Transaction(taToEdit, editTA.getName(), editTA.getEmail(), editTA.getType());
                app.processTransaction(transaction);
            }
        }
    }

    public void processSelectAllTAs() {
        CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
        data.selectTAs(TAType.All);
    }

    public void processSelectGradTAs() {
        CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
        data.selectTAs(TAType.Graduate);
    }

    public void processSelectUndergradTAs() {
        CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
        data.selectTAs(TAType.Undergraduate);
    }

    public void processSelectTA() {
        AppGUIModule gui = app.getGUIModule();
        TableView<TimeSlot> officeHoursTableView = (TableView) gui.getGUINode(OH_OFFICE_HOURS_TABLE_VIEW);
        officeHoursTableView.refresh();
    }
    public void processSelectTimeRange(){
        AppGUIModule gui = app.getGUIModule();
        ComboBox start=(ComboBox) gui.getGUINode( OH_OFFICE_HOURS_START_COMBO);
        ComboBox end=(ComboBox) gui.getGUINode(OH_OFFICE_HOURS_END_COMBO);
        SelectTimeRange_Transaction transaction = new SelectTimeRange_Transaction(app,start.getValue().toString(),end.getValue().toString(),start_a,start_b, (CourseSiteGeneratorData) app.getDataComponent());
        app.processTransaction(transaction);
        
    }
    public void processClickonTimeRange(String olds,String olde){
        start_a=olds;
        start_b=olde;
    }
//Site
    public void processEditBanner(String banner_subject,String banner_title,String banner_smester,String banner_number,String banner_year){
        AppGUIModule gui = app.getGUIModule();
        CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
        if(!(banner_subject.equals(data.getBannerSubject())&&banner_smester.equals(data.getBannersmester())&&banner_number.equals(data.getBannerNumber())&&banner_year.equals(data.getBannerYear())&&banner_title.equals(data.getBannerTitle()))){
        EditBanner_Transaction transaction = new EditBanner_Transaction(app,banner_subject,banner_number,banner_smester,banner_year,banner_title);
        app.processTransaction(transaction);}
    }
    
    public void processEditPages(boolean home,boolean syllabus,boolean schedule, boolean hw,String type){
        
         AppGUIModule gui = app.getGUIModule();
         EditPages_Transaction transaction = new EditPages_Transaction(app,home,syllabus,schedule,hw,type);
         app.processTransaction(transaction);
    }
    public void processEditStyle(String type,String unknown ){
        AppGUIModule gui = app.getGUIModule();
       
        EditImages_Transaction transaction= new EditImages_Transaction(app,type,unknown);
        app.processTransaction(transaction);
       
    }
    public void processEditInstructor(String name,String email,String room,String homepage){
        AppGUIModule gui = app.getGUIModule();
        CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
        if(!(name.equals(data.getins_name())&&email.equals(data.getins_email())&&room.equals(data.getins_room())&&homepage.equals(data.getins_homepage()))){
        EditInstructor_Transaction transaction= new EditInstructor_Transaction(app,name,email,room,homepage);
        app.processTransaction(transaction);}
    }
    
    public void processEditInstructorOHButton(String text){
        AppGUIModule gui = app.getGUIModule();
        CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
        if(!text.equals(data.getins_textArea_string())){
        EditInsTextArea_Transaction  transaction= new EditInsTextArea_Transaction (app,text);
        app.processTransaction(transaction);}
        
    }
    
    public void processEditSyllabus(String text,String type){
        AppGUIModule gui = app.getGUIModule();
        CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
        
        if(type.equals("description")){
            if(!text.equals(data.getTxt("description"))){
                EditSyllabus_Transaction transaction= new EditSyllabus_Transaction(text,type,app);
                app.processTransaction(transaction);}
            }
            if(type.equals("topics")){
                 if(!text.equals(data.getTxt("topics"))){
                EditSyllabus_Transaction transaction= new EditSyllabus_Transaction(text,type,app);
            app.processTransaction(transaction);}
            }
            if(type.equals("prerequisites")){
                if(!text.equals(data.getTxt("prerequisites"))){
                EditSyllabus_Transaction transaction= new EditSyllabus_Transaction(text,type,app);
            app.processTransaction(transaction);}
            }
            if(type.equals("outcomes")){
                if(!text.equals(data.getTxt("outcomes"))){
               EditSyllabus_Transaction transaction= new EditSyllabus_Transaction(text,type,app);
            app.processTransaction(transaction);}
            }
            if(type.equals("textbooks")){
                if(!text.equals(data.getTxt("textbooks"))){
                EditSyllabus_Transaction transaction= new EditSyllabus_Transaction(text,type,app);
            app.processTransaction(transaction);}
            }
            if(type.equals("gradedComponents")){
                if(!text.equals(data.getTxt("gradedComponents"))){
               EditSyllabus_Transaction transaction= new EditSyllabus_Transaction(text,type,app);
            app.processTransaction(transaction);}
            }
            
            if(type.equals("gradingNote")){
                if(!text.equals(data.getTxt("gradingNote"))){
               EditSyllabus_Transaction transaction= new EditSyllabus_Transaction(text,type,app);
            app.processTransaction(transaction);}
            }
            if(type.equals("academicDishonesty")){
                if(!text.equals(data.getTxt("academicDishonesty"))){
               EditSyllabus_Transaction transaction= new EditSyllabus_Transaction(text,type,app);
            app.processTransaction(transaction);}
            }
            if(type.equals("specialAssistance")){
                if(!text.equals(data.getTxt("specialAssistance"))){
               EditSyllabus_Transaction transaction= new EditSyllabus_Transaction(text,type,app);
                app.processTransaction(transaction);}
            }
        
    }
    
    public void processAddLecture(){
        AppGUIModule gui = app.getGUIModule();
        CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
        if(true){
            AddLecture_Transaction transaction= new AddLecture_Transaction(data);
            app.processTransaction(transaction);
        }
        
    }
    public void processAddRecitation(){
        AppGUIModule gui = app.getGUIModule();
        CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
        if(true){
            AddRecitation_Transaction transaction= new  AddRecitation_Transaction (data);
            app.processTransaction(transaction);
        }
        
    }
    public void processAddLabs(){
        AppGUIModule gui = app.getGUIModule();
        CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
        if(true){
            AddLabs_Transaction transaction= new AddLabs_Transaction(data);
            app.processTransaction(transaction);
        }
        
    }
    
     
     
    public void processRemoveLecture(Lectures l){
        AppGUIModule gui = app.getGUIModule();
        TableView lectureTV=(TableView) gui.getGUINode(MT_LECTURE_TABLEVIEW);
        CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
        if(lectureTV.getSelectionModel().getSelectedItem()!=null){
            RemoveLecture_Transaction transaction= new RemoveLecture_Transaction(data,l);
            app.processTransaction(transaction);
        }
        
    }
    public void processRemoveRecitation(Recitation r){
        AppGUIModule gui = app.getGUIModule();
        CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
        TableView  recTV=(TableView) gui.getGUINode(MT_RECITATION_TABLEVIEW);
        if(recTV.getSelectionModel().getSelectedItem()!=null){
            RemoveRecitation_Transaction transaction= new  RemoveRecitation_Transaction (data,r);
            app.processTransaction(transaction);
        }
        
    }
    public void processRemoveLabs(Labs l){
        AppGUIModule gui = app.getGUIModule();
        TableView  labTV=(TableView) gui.getGUINode(MT_LAB_TABLEVIEW);
        CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
        if(labTV.getSelectionModel().getSelectedItem()!=null){
            RemoveLabs_Transaction transaction= new RemoveLabs_Transaction(data,l);
            app.processTransaction(transaction);
        }
        
    }
    
    public void processEditMTCells(Lectures l, String newvalue, String type){
        AppGUIModule gui = app.getGUIModule();
        CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
        EditMTLectureCells_Transaction tran=new EditMTLectureCells_Transaction(data,l,newvalue,type);
        app.processTransaction(tran);
    }
    public void processEditMTCells1(Recitation l, String newvalue, String type){
        AppGUIModule gui = app.getGUIModule();
        CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
        EditMTRecCell_Transaction tran=new EditMTRecCell_Transaction(data,l,newvalue,type);
        app.processTransaction(tran);
    }
    public void processEditMTCells2(Labs l, String newvalue, String type){
        AppGUIModule gui = app.getGUIModule();
        CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
        EditMTLabCell_Transaction tran=new EditMTLabCell_Transaction(data,l,newvalue,type);
        app.processTransaction(tran);
    }
    
    public void processClearScheudle(){
        AppGUIModule gui = app.getGUIModule();
        CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
        TextField title=(TextField)gui.getGUINode(SCHEDULE_AE_TITLE_TEXTFIELD);
        TextField topic=(TextField)gui.getGUINode(SCHEDULE_AE_TOPIC_TEXTFIELD);
        TextField link=(TextField)gui.getGUINode( SCHEDULE_AE_LINK_TEXTFIELD);
        DatePicker date=(DatePicker)gui.getGUINode(SCHEDULE_AE_DATE_COMBO);
        DatePicker start=(DatePicker)gui.getGUINode(SCHEDULE_CALENDAR_STARTMONDAY_COMBO);
        ComboBox type=(ComboBox)gui.getGUINode(SCHEDULE_AE_TYPE_COMBO);
        if(!title.getText().equals("")||!topic.getText().equals("")||!link.getText().equals("")||!type.getValue().toString().equals("holidays")){
        ClearSchedule_Transaction  tran=new ClearSchedule_Transaction (app);
        app.processTransaction(tran);}
    }
    
    
    public void processEditScheduleText(){
        AppGUIModule gui = app.getGUIModule();
        CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
        TextField title=(TextField)gui.getGUINode(SCHEDULE_AE_TITLE_TEXTFIELD);
        TextField topic=(TextField)gui.getGUINode(SCHEDULE_AE_TOPIC_TEXTFIELD);
        TextField link=(TextField)gui.getGUINode( SCHEDULE_AE_LINK_TEXTFIELD);
        DatePicker date=(DatePicker)gui.getGUINode(SCHEDULE_AE_DATE_COMBO);
        ComboBox type=(ComboBox)gui.getGUINode(SCHEDULE_AE_TYPE_COMBO);
        
        if(!data.getTitle().equals(title.getText().trim()) || !data.getTopic().equals(topic.getText().trim()) || !data.getLink().equals(link.getText()) || !data.getType().equals(type.getValue().toString())||!date.getValue().equals(data.getLocalDate())) 
        {
            EditScheduleItemText_Transaction  tran=new EditScheduleItemText_Transaction(app);
            app.processTransaction(tran);
        }
    }
    
    public void processAdd_Edit_ScheduleItem(String ae){
        AppGUIModule gui = app.getGUIModule();
        CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
        TextField title=(TextField)gui.getGUINode(SCHEDULE_AE_TITLE_TEXTFIELD);
        TextField topic=(TextField)gui.getGUINode(SCHEDULE_AE_TOPIC_TEXTFIELD);
        TextField link=(TextField)gui.getGUINode( SCHEDULE_AE_LINK_TEXTFIELD);
        DatePicker date=(DatePicker)gui.getGUINode(SCHEDULE_AE_DATE_COMBO);
        ComboBox type=(ComboBox)gui.getGUINode(SCHEDULE_AE_TYPE_COMBO);
        if(ae.equals("add")){
            AddSI_Transaction  tran=new AddSI_Transaction(app);
            app.processTransaction(tran);
            
        }
        else{
            EditSI_Transaction  tran=new EditSI_Transaction(app,title.getText(),topic.getText(),link.getText(),date.getValue().toString(),type.getValue().toString(),date.getValue());
            app.processTransaction(tran);
        }
        
    }
    public void processRemoveScheduleItems(){
        AppGUIModule gui = app.getGUIModule();
        CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
        RemoveSI_Transaction tran= new RemoveSI_Transaction(app);
        app.processTransaction(tran);
    }
    
    public void processEditStartEnd(){
        AppGUIModule gui = app.getGUIModule();
        CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
        StartAndEnd_Transaction tran= new StartAndEnd_Transaction(app);
        app.processTransaction(tran);
        
    }
    
    
    public void processCSSCombox(){
        AppGUIModule gui = app.getGUIModule();
        CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
        CSS_Transaction tran= new CSS_Transaction(data);
        app.processTransaction(tran);
        
    }
    
    
}