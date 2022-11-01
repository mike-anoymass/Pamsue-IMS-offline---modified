/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drivingschoolfmis;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 *
 * @author ANOYMASS
 */
public class LoginDocumentController implements Initializable, ControlledScreen {

    @FXML
    private ImageView imageview;
    int count;
    @FXML
    private AnchorPane anchorpane;

    @FXML
    private JFXTextField usernameTxtField;
    @FXML
    private Label pcNameLbl;
    @FXML
    private Label title;
    @FXML
    private JFXButton closeBtn;
    @FXML
    private JFXPasswordField passwordTxtField;
    @FXML
    private JFXButton loginBtn;



    public PreparedStatement pst = null;
    public ResultSet rs = null;
    Connection conn = DrivingSchoolFMIS.conn;
    static String userName;
    static String userType;
    static String firstname;
    static String lastname;

    ScreensController controller;



    @Override
    public void initialize(URL url, ResourceBundle rb) {

        title.setText(DrivingSchoolFMIS.schoolName + " school pamseu  FMIS");
        UserCredentials user = new UserCredentials();
        pcNameLbl.textProperty().bind(user.userPcNameProperty());

        closeBtn.setTooltip(new Tooltip("Close the Application"));

        user.passwordProperty().bind(passwordTxtField.textProperty());
        user.userNameProperty().bind(usernameTxtField.textProperty());

        slideShow();

        anchorpane.getStylesheets().add(getClass().getResource("loginCss.css").toExternalForm());

        setTextFieldsProperties();

    }

    @Override
    public void setScreenParent1(ScreensController screen) {
        controller = screen;
    }

    private void goToMainpage(ActionEvent event) {
        controller.setScreen(DrivingSchoolFMIS.createAccountScreenID);
    }

    public void slideShow() {
        ArrayList<Image> images = new ArrayList<Image>();
        images.add(new Image("img/p1.jpg"));
        images.add(new Image("img/p2.jpg"));
        images.add(new Image("img/p3.jpg"));
        images.add(new Image("img/p4.jpg"));
        images.add(new Image("img/p5.jpeg"));
        images.add(new Image("img/p6.jpg"));
        images.add(new Image("img/p7.jpg"));
        images.add(new Image("img/p8.jpg"));
        images.add(new Image("img/p9.jpg"));
        images.add(new Image("img/p10.jpg"));
        images.add(new Image("img/p11.jpg"));
        images.add(new Image("img/p12.jpg"));
        images.add(new Image("img/p13.jpg"));
        images.add(new Image("img/p14.jpg"));
        images.add(new Image("img/p15.jpeg"));
        final DoubleProperty op = imageview.opacityProperty();

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5.0), event -> {
            imageview.setImage(images.get(count));
            count++;

            if (count == 15) {
                count = 0;
            }
        }, new KeyValue(op, 0.0)));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

    }

    @FXML
    private void quit(ActionEvent event) {
        System.exit(1);
    }

    @FXML
    private void loadMainPage(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        
        try {
            pst = conn.prepareStatement("SELECT * FROM users where username=? AND"
                    + " password=?");
            pst.setString(1, usernameTxtField.getText());
            pst.setString(2, passwordTxtField.getText());
            rs = pst.executeQuery();

            while (rs.next()) {
                userName = rs.getString("userName");
                userType = rs.getString("type");
                firstname = rs.getString("firstname");
                lastname = rs.getString("lastname");

                System.out.print("success");

                usernameTxtField.setText("");
                passwordTxtField.setText("");
                

                TrayNotification tray = new TrayNotification("Anoymass Programs",
                        "Welcome " + userName + " to " + DrivingSchoolFMIS.schoolName + " Pamseu FMIS",
                        NotificationType.SUCCESS);

                tray.setAnimationType(AnimationType.SLIDE);

                tray.showAndDismiss(Duration.seconds(3));

                try {
                    Window loginWindow = ((Node) event.getSource()).getScene().getWindow();
                    //Window alertWindow = ((Node) event.getSource()).getScene().getWindow();
                    loginWindow.hide();
                  
                    FXMLLoader loader = new FXMLLoader();

                    AnchorPane root = loader.load(getClass().getResource("MainDocument.fxml").openStream());
                    MainDocumentController controller = (MainDocumentController) loader.getController();
                    controller.setNameLbl(userName);
                    controller.setLoginWindow(loginWindow);
                    //controller.setAlertWindow(alertWindow);

                    Scene scene = new Scene(root);
                    StageManager.ParentStage.setScene(scene);
                    StageManager.ParentStage.setTitle(DrivingSchoolFMIS.schoolName + " Pamseu FMIS");
                    StageManager.ParentStage.showAndWait();

                } catch (IOException ex) {
                    System.err.println(ex.toString());
                    System.exit(1);
                }
           
            }
          

        } catch (SQLException ex) {
            Logger.getLogger(LoginDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        alert.setTitle("WARNING");
        alert.setHeaderText("LOGIN INFORMATION");
        alert.setContentText("->You have entered Invalid User Name or Password "
                + "\n_>Kindly check your input and try again");
        //setting an icon to alert Dialog- first thing, get he window of the alert dialog
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("img/warning_32.png"));
        alert.show();
       
    }

    private void setTextFieldsProperties() {
        usernameTxtField.textProperty().addListener((obs, ov, nv) -> {
            try {
                pst = conn.prepareStatement("SELECT * FROM users");
                rs = pst.executeQuery();

                while (rs.next()) {
                    boolean granted = usernameTxtField.getText().equals(rs.getString("username"));

                    if (granted) {
                        TrayNotification tray = new TrayNotification(DrivingSchoolFMIS.schoolName + " Driving School FMIS", "Correct User Name",
                                NotificationType.SUCCESS);

                        tray.setAnimationType(AnimationType.SLIDE);

                        tray.showAndDismiss(Duration.seconds(1));
                    }

                }
            } catch (SQLException ex) {
                Logger.getLogger(DrivingSchoolFMIS.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        passwordTxtField.textProperty().addListener((obs, ov, nv) -> {
            try {
                pst = conn.prepareStatement("SELECT * FROM users");
                rs = pst.executeQuery();

                while (rs.next()) {
                    boolean granted = passwordTxtField.getText().equals(rs.getString("password"));

                    if (granted) {
                        TrayNotification tray = new TrayNotification(DrivingSchoolFMIS.schoolName + " Driving School FMIS", "Correct Password",
                                NotificationType.SUCCESS);

                        tray.setAnimationType(AnimationType.SLIDE);

                        tray.showAndDismiss(Duration.seconds(1));
                    }

                }
            } catch (SQLException ex) {
                Logger.getLogger(DrivingSchoolFMIS.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

    }

}
