/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oh.transactions;

import jtps.jTPS_Transaction;
import oh.CourseSiteGeneratorApp;
import oh.data.CourseSiteGeneratorData;


/**
 *
 * @author McKillaGorilla
 */
public class EditBanner_Transaction implements jTPS_Transaction {
    CourseSiteGeneratorApp app;
    String banner_subject;
    String banner_number;
    String banner_smester;
    String banner_year;
    String banner_title;
    String banner_subject_old;
    String banner_number_old;
    String banner_smester_old;
    String banner_year_old;
    String banner_title_old;
    
    
    public EditBanner_Transaction(CourseSiteGeneratorApp initApp, String sub,String num,String smes,String year,String title) {
         
         app = initApp;
         CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
         banner_subject=sub;
         banner_number=num;
         banner_smester=smes;
         banner_year=year;
         banner_title=title;
         banner_subject_old=data.getBannerSubject();
         banner_number_old=data.getBannerNumber();
         banner_smester_old=data.getBannersmester();
         banner_year_old=data.getBannerYear();
         banner_title_old=data.getBannerTitle();
    }

    @Override
    public void doTransaction() {
        CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
        data.setBanner(banner_subject,banner_number, banner_smester, banner_year, banner_title);
    }

    @Override
    public void undoTransaction() {
        CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
        data.setBanner(banner_subject_old,banner_number_old, banner_smester_old, banner_year_old, banner_title_old);
        
    }
}