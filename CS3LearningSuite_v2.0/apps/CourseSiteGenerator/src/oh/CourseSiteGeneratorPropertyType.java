package oh;

/**
 * This class provides the properties that are needed to be loaded for
 * setting up To Do List Maker workspace controls including language-dependent
 * text.
 * 
 * @author Richard McKenna
 * @version 1.0
 */
public enum CourseSiteGeneratorPropertyType {

    /* THESE ARE THE NODES IN OUR APP */
    // FOR SIMPLE OK/CANCEL DIALOG BOXES
    SPACE_LABEL,
    SPACE_LABEL1,
    OH_OK_PROMPT,
    OH_CANCEL_PROMPT,
    SPACE_LABEL2,
    
    CSG_TABPANE,

    // THESE ARE FOR TEXT PARTICULAR TO THE APP'S WORKSPACE CONTROLS
    OH_LEFT_PANE,
    OH_TAS_HEADER_PANE,
    OH_TAS_HEADER_LABEL,
    OH_GRAD_UNDERGRAD_TAS_PANE,
    OH_ALL_RADIO_BUTTON,
    OH_GRAD_RADIO_BUTTON,
    OH_UNDERGRAD_RADIO_BUTTON,
    OH_TAS_HEADER_TEXT_FIELD,
    OH_TAS_TABLE_VIEW,
    OH_NAME_TABLE_COLUMN,
    OH_EMAIL_TABLE_COLUMN,
    OH_SLOTS_TABLE_COLUMN,
    OH_TYPE_TABLE_COLUMN,
    OH_END_LABEL,
    OH_START_LABEL,

    OH_ADD_TA_PANE,
    OH_NAME_TEXT_FIELD,
    OH_EMAIL_TEXT_FIELD,
    OH_ADD_TA_BUTTON,

    OH_RIGHT_PANE,
    OH_OFFICE_HOURS_HEADER_PANE,
    OH_OFFICE_HOURS_HEADER_LABEL,
    OH_OFFICE_HOURS_TABLE_VIEW,
    OH_START_TIME_TABLE_COLUMN,
    OH_END_TIME_TABLE_COLUMN,
    OH_MONDAY_TABLE_COLUMN,
    OH_TUESDAY_TABLE_COLUMN,
    OH_WEDNESDAY_TABLE_COLUMN,
    OH_THURSDAY_TABLE_COLUMN,
    OH_FRIDAY_TABLE_COLUMN,
    OH_DAYS_OF_WEEK,
    OH_FOOLPROOF_SETTINGS,
    
    // FOR THE EDIT DIALOG
    OH_TA_EDIT_DIALOG,
    OH_TA_DIALOG_GRID_PANE,
    OH_TA_DIALOG_HEADER_LABEL, 
    OH_TA_DIALOG_NAME_LABEL,
    OH_TA_DIALOG_NAME_TEXT_FIELD,
    OH_TA_DIALOG_EMAIL_LABEL,
    OH_TA_DIALOG_EMAIL_TEXT_FIELD,
    OH_TA_DIALOG_TYPE_LABEL,
    OH_TA_DIALOG_TYPE_BOX,
    OH_TA_DIALOG_GRAD_RADIO_BUTTON,
    OH_TA_DIALOG_UNDERGRAD_RADIO_BUTTON,
    OH_TA_DIALOG_OK_BOX,
    OH_TA_DIALOG_OK_BUTTON, 
    OH_TA_DIALOG_CANCEL_BUTTON, 
    OH_OFFICE_HOURS_START_COMBO,
    OH_OFFICE_HOURS_END_COMBO,
    
    // THESE ARE FOR ERROR MESSAGES PARTICULAR TO THE APP
    OH_NO_TA_SELECTED_TITLE, OH_NO_TA_SELECTED_CONTENT,
    
    
    ////SITE
    SITE_PANE,
    SITE_BANNER,
    SITE_PAGES,
    SITE_STYLE,
    SITE_INSTRUCTOR,
    SITE_BANNER_HEADER_LABEL,
    SITE_BANNER_SUBJECT_LABEL,
    SITE_BANNER_SMESTER_LABEL,
    SITE_BANNER_NUMBER_LABEL,
    SITE_BANNER_TITLE_LABEL,
    SITE_BANNER_YEAR_LABEL,
    SITE_BANNER_EXPORTDIR_LABEL,
    SITE_BANNER_SUBJECT_COMBO,
    SITE_BANNER_SMESTER_COMBO,
    SITE_BANNER_NUMBER_COMBO,
    SITE_BANNER_YEAR_COMBO,
    SITE_BANNER_TITLE_TEXTFIELD,
    SITE_BANNER_EXPORTDIR,
    
    SITE_PAGES_HEADER_LABEL,
    SITE_PAGES_HOME_CHECKBOX,
    SITE_PAGES_SYLLABUS_CHECKBOX,
    SITE_PAGES_SCHEDULE_CHECKBOX,
    SITE_PAGES_HW_CHECKBOX,
    
    SITE_STYLE_HEADER_LABEL,
    SITE_STYLE_FAVICON_BUTTON,
    SITE_STYLE_NAVBAR_BUTTON,
    SITE_STYLE_LEFTFOOTER_BUTTON,
    SITE_STYLE_RIGHTFOOTER_BUTTON,
    SITE_STYLE_FAVICON_IMAGE,
    SITE_STYLE_NAVBAR_IMAGE,
    SITE_STYLE_LEFTFOOTER_IMAGE,
    SITE_STYLE_RIGHTFOOTER_IMAGE,
    SITE_STYLE_FONTCOLOR_LABEL,
    SITE_STYLE_NOTE_LABEL,
    SITE_STYLE_SHEET_COMBO,
    
    SITE_INSTRUCTOR_HEADER_LABEL,
    SITE_INSTRUCTOR_HOMEPAGE_LABEL,
    SITE_INSTRUCTOR_NAME_LABEL,
    SITE_INSTRUCTOR_EMAIL_LABEL,
    SITE_INSTRUCTOR_ROOM_LABEL,
    
    SITE_INSTRUCTOR_HOMEPAGE_TEXTFIELD,
    SITE_INSTRUCTOR_ROOM_TEXTFIELD,
    SITE_INSTRUCTOR_EMAIL_TEXTFIELD,
    SITE_INSTRUCTOR_NAME_TEXTFIELD,
    SITE_INSTRUCTOR_OH_BUTTON,
    SITE_INSTRUCTOR_OH_LABEL,
    SITE_INSTRUCTOR_OH_TEXTAREA,
    SITE_INSTRUCTOR_HBOX,
    
    APP_PATH_IMAGESs,
    
    
    SYLLABUS_PANE,
    //BUTTONS
    SYLLABUS_DESCRIPTION,
    SYLLABUS_TOPICS,
    SYLLABUS_PREREQUISITES,
    SYLLABUS_OUTCOMES,
    SYLLABUS_TEXTBOOKS,
    SYLLABUS_GN,
    SYLLABUS_GC,
    SYLLABUS_AD,
    SYLLABUS_SA,
    //TEXTFIELD
    SYLLABUS_DESCRIPTION_TEXTFIELD,
    SYLLABUS_TOPICS_TEXTFIELD,
    SYLLABUS_PREREQUISITES_TEXTFIELD,
    SYLLABUS_OUTCOMES_TEXTFIELD,
    SYLLABUS_TEXTBOOKS_TEXTFIELD,
    SYLLABUS_GN_TEXTFIELD,
    SYLLABUS_GC_TEXTFIELD,
    SYLLABUS_AD_TEXTFIELD,
    SYLLABUS_SA_TEXTFIELD,
    SYLLABUS_VBOXES1,
    SYLLABUS_VBOXES2,
    SYLLABUS_VBOXES3,
    SYLLABUS_VBOXES4,
    SYLLABUS_VBOXES5,
    SYLLABUS_VBOXES6,
    SYLLABUS_VBOXES7,
    SYLLABUS_VBOXES8,
    SYLLABUS_VBOXES9,
    
    
    
    MT_PANE,
    MT_LECTURE,
    MT_LAB,
    MT_RECITATION,
    MT_LECTURE_ADD_BUTTON,
    MT_LECTURE_MINUS_BUTTON,
    MT_RECITATION_ADD_BUTTON,
    MT_RECITATION_MINUS_BUTTON,
    MT_LAB_ADD_BUTTON,
    MT_LAB_MINUS_BUTTON,
    MT_LAB_HEADER_LABEL,
    MT_RECITATION_HEADER_LABEL,
    MT_LECTURE_HEADER_LABEL,
    MT_LECTURE_TABLEVIEW,
    MT_RECITATION_TABLEVIEW,
    MT_LAB_TABLEVIEW,
    MT_LECTURE_SECTION_TABLE_COLUMN,
    MT_LECTURE_DAYS_TABLE_COLUMN,
    MT_LECTURE_TIME_TABLE_COLUMN,
    MT_LECTURE_ROOM_TABLE_COLUMN,
    MT_RECITATION_SECTION_TABLE_COLUMN,
    MT_RECITATION_DAYS_TIME_TABLE_COLUMN,
    MT_RECITATION_ROOM_TABLE_COLUMN,
    MT_RECITATION_TA1_TABLE_COLUMN,
    MT_RECITATION_TA2_TABLE_COLUMN,
    MT_LAB_SECTION_TABLE_COLUMN,
    MT_LAB_DAYS_TIME_TABLE_COLUMN,
    MT_LAB_ROOM_TABLE_COLUMN,
    MT_LAB_TA1_TABLE_COLUMN,
    MT_LAB_TA2_TABLE_COLUMN,
    
    
    SCHEDULE_PANE,
    SCHEDULE_CALENDAR,
    SCHEDULE_ITEM,
    SCHEDULE_ADD_EDIT,
    SCHEDULE_CALENDAR_HEADER,
    SCHEDULE_ITEM_HEADER,
    SCHEDULE_ADD_EDIT_HEADER,
    SCHEDULE_CALENDAR_STARTMONDAY,
    SCHEDULE_CALENDAR_ENDFRIDAY,
    SCHEDULE_CALENDAR_STARTMONDAY_COMBO,
    SCHEDULE_CALENDAR_ENDFRIDAY_COMBO,
    SCHEDULE_CALENDAR_HBOX_START,
    SCHEDULE_CALENDAR_HBOX_END,
    SCHEDULE_MINUS_BUTTON,
    SCHEDULE_ITEM_TABLEVIEW,
    SCHEDULE_TYPE_COLUMN,
    SCHEDULE_DATE_COLUMN,
    SCHEDULE_TITLE_COLUMN,
    SCHEDULE_TOPIC_COLUMN,
    SCHEDULE_AE_TYPE,
    SCHEDULE_AE_DATE,
    SCHEDULE_AE_TITLE,
    SCHEDULE_AE_TOPIC,
    SCHEDULE_AE_LINK,
    SCHEDULE_AE_TYPE_COMBO,
    SCHEDULE_AE_DATE_COMBO,
    SCHEDULE_AE_TITLE_TEXTFIELD,
    SCHEDULE_AE_TOPIC_TEXTFIELD,
    SCHEDULE_AE_LINK_TEXTFIELD,
    SCHEDULE_AE_CLEAR_BUTTON,
    SCHEDULE_AE_ADD_UPDATE_BUTTON
    
    
 
    
    
    
    
    
    
    
    
    
}