/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drivingschoolfmis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.util.Scanner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author ANOYMASS
 */
public class DrivingSchoolFMIS extends Application {

    public static String loginScreenID = "login";
    public static String loginScreenFile = "LoginDocument.fxml";
    public static String createAccountScreenID = "account";
    public static String createAccountScreenFile = "CreateAccountDocument.fxml";
    public static final Connection conn = SqlConnection.DbConnector();
    public static String schoolName = "";
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        try{
            schoolName = new SchoolName().getNameOfSchool();
        }catch(NullPointerException n){
          schoolName = new SchoolName().getNameOfSchool();
        }
        
        createDirectory();
        System.out.println(schoolName);
        StageManager.setStages();
        ScreensController mainController = new ScreensController();
        mainController.loadScreen(DrivingSchoolFMIS.loginScreenID, DrivingSchoolFMIS.loginScreenFile);
        mainController.loadScreen(DrivingSchoolFMIS.createAccountScreenID, DrivingSchoolFMIS.createAccountScreenFile);

        mainController.setScreen(DrivingSchoolFMIS.loginScreenID);

        Group root = new Group();
        root.getChildren().addAll(mainController);

        Scene scene = new Scene(root,Color.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        //primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("img/driving_16.png"));
        primaryStage.setTitle("Driving School FMIS");
        primaryStage.show();
    }



    private void createDirectory() {
        String documents = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
        File file = new File(documents+"\\" + schoolName + " FMIS");
        boolean bool = file.mkdir();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
