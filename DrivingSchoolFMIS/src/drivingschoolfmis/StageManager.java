/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drivingschoolfmis;

import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author ANOYMASS
 */
public class StageManager {
    static Stage historyStage = new Stage();
    static Stage ParentStage = new Stage();
    static Stage announcementStage = new Stage();
    static Stage receiptStage = new Stage();
    static Stage browsingStage = new Stage();
    static Stage studentFormAddStage= new Stage();
    static Stage studentFormEditStage= new Stage();
    static Stage studentTestStage= new Stage();
    static Stage receiptDetails = new Stage();
    static Stage studentDetails = new Stage();
    static Stage staffAddStage = new Stage();
    static Stage staffEditStage = new Stage();
    static Stage staffDetailsStage = new Stage();
    static Stage staffPaymentStage = new Stage();
    static Stage staffHistoryStage = new Stage();
    static Stage loanStage = new Stage();
    static Stage settlementStage = new Stage();

    static void setStages(){
        browse(announcementStage, receiptStage, historyStage);

        browse(browsingStage, studentFormAddStage, studentFormEditStage);

        browse(studentTestStage, receiptDetails , studentDetails);

        browse(staffAddStage, staffEditStage , staffDetailsStage);

        browse(staffPaymentStage, staffHistoryStage , loanStage);
        
        browse(settlementStage, new Stage() , new Stage());
    }


    private static void browse(Stage browsingStage, Stage studentFormAddStage, Stage studentFormEditStage) {
        browsingStage.initModality(Modality.WINDOW_MODAL);
        browsingStage.setResizable(false);
        browsingStage.initOwner(ParentStage);

        studentFormAddStage.initModality(Modality.WINDOW_MODAL);
        studentFormAddStage.setResizable(false);
        studentFormAddStage.initOwner(ParentStage);

        studentFormEditStage.initModality(Modality.WINDOW_MODAL);
        studentFormEditStage.setResizable(false);
        studentFormEditStage.initOwner(ParentStage);
    }


}
