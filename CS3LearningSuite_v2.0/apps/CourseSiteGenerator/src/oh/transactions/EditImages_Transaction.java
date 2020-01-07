/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oh.transactions;

import java.awt.image.BufferedImage;
import java.io.File;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import jtps.jTPS_Transaction;
import oh.CourseSiteGeneratorApp;
import oh.data.CourseSiteGeneratorData;

/**
 *
 * @author fenghsiyu
 */
public class EditImages_Transaction implements jTPS_Transaction {
    CourseSiteGeneratorApp app;
    String favicon;
    String navbar;
    String leftfooter;
    String rightfooter;
    String favicon_old;
    String navbar_old;
    String leftfooter_old;
    String rightfooter_old;
    
    
    
    
    public EditImages_Transaction(CourseSiteGeneratorApp initApp,String type,String unknown) {
        
         app = initApp;
         CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
         favicon=data.getFavicon_path();
         navbar=data.getnavbar_path();
         leftfooter=data.getleftfooter_path();
         rightfooter=data.getrightfooter_path();
         favicon_old=favicon;
         navbar_old=navbar;
         leftfooter_old=leftfooter;
         rightfooter_old=rightfooter;
         
         if(type.equals("favicon")){
             favicon=unknown;
            
         }
         if(type.equals("navbar")){
             navbar=unknown;
         }
         if(type.equals("leftfooter")){
             leftfooter=unknown;
         }
         if(type.equals("rightfooter")){
             rightfooter=unknown;
         }
         try{
             File fc=new File(unknown);
             BufferedImage bufferedImage = ImageIO.read(fc);
             Image bannerImage = SwingFXUtils.toFXImage(bufferedImage, null);
            // data.saveToFile(bannerImage);
             }
         catch(Exception ex){
             
             }
    }

    @Override
    public void doTransaction() {
        CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
        data.setStyle(favicon, navbar, leftfooter, rightfooter);
    }

    @Override
    public void undoTransaction() {
        CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
       data.setStyle(favicon_old, navbar_old, leftfooter_old, rightfooter_old);
    }
}