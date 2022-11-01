package drivingschoolfmis;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import static drivingschoolfmis.AlertClass.makeAlert;
import static drivingschoolfmis.DrivingSchoolFMIS.schoolName;

public class StaffDetailsController {

    ResultSet rs;
    Employee emp;
    EmployeeQueries empQuery = new EmployeeQueries();

    @FXML
    private Label staffIDLbl;

    @FXML
    private ImageView logoImgView;

    @FXML
    private Label titleLbl;

    @FXML
    private JFXTextField dateTxt;

    @FXML
    private JFXTextField locationTxt;

    @FXML
    private JFXTextField studentNameTxt;

    @FXML
    private JFXTextField phoneTxt;

    @FXML
    private JFXTextField genderTxt;

    @FXML
    private JFXTextArea addressTxt;

    @FXML
    private JFXTextField physicalAddressTxt;

    @FXML
    private JFXTextField guardianTxt;

    @FXML
    private JFXTextField dobTxt;

    @FXML
    private JFXTextField departmentTxt;

    @FXML
    private JFXTextField hrsTxt;

    @FXML
    private JFXTextField statusTxt;

    @FXML
    private JFXTextField salaryTxt;

    @FXML
    private JFXTextField dateRTxt;

    @FXML
    private JFXTextField gurdianNameTxt;

    @FXML
    private JFXTextField guardianPhoneTxt;

    @FXML
    private JFXTextField guardianPAddrTxt;

    @FXML
    void paymentHistory(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            AnchorPane root = loader.load(getClass().getResource("StaffPayments.fxml").openStream());
            StaffPaymentsController controller = loader.getController();
            controller.initialize(emp);

            Scene scene = new Scene(root);
            StageManager.staffHistoryStage.setScene(scene); 
            StageManager.staffHistoryStage.getIcons().add(new Image("img/history_16.png"));
            StageManager.staffHistoryStage.setTitle("Payment History for " + emp.getFullName());

            StageManager.staffHistoryStage.show();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }catch(NullPointerException ex){
            makeAlert("warning", "Please Select a staff record\nUse your mouse buttons");
        }
    }

    @FXML
    void recordPayment(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            AnchorPane root = loader.load(getClass().getResource("StaffPaymentForm.fxml").openStream());
            StaffPaymentFormController controller = loader.getController();
            controller.initialize(emp);

            Scene scene = new Scene(root);
            StageManager.staffPaymentStage.setScene(scene);
            StageManager.staffPaymentStage.getIcons().add(new Image("img/receipt_16.png"));
            StageManager.staffPaymentStage.setTitle("Payment for - " + emp.getFullName());

            StageManager.staffPaymentStage.show();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }catch(NullPointerException ex){
            makeAlert("warning", "Please Select a staff record\nUse your mouse buttons");
        }
    }

    @FXML
    void update(ActionEvent event) {
        ((Node) event.getSource()).getScene().getWindow().hide();
        try {
            FXMLLoader loader = new FXMLLoader();
            AnchorPane root = loader.load(getClass().getResource("StaffFormEdit.fxml").openStream());
            StaffFormEditController controller = loader.getController();
            controller.initialize(emp);

            Scene scene = new Scene(root);
            StageManager.staffEditStage.setScene(scene);
            StageManager.staffEditStage.getIcons().add(new Image("img/editicon_16.png"));
            StageManager.staffEditStage.setTitle("Update Employee");

            StageManager.staffEditStage.show();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }catch(NullPointerException ex){
            makeAlert("warning", "Please Select a staff record\nUse your mouse buttons");
        }
    }

    public void initialize(Employee emp) {
        staffIDLbl.setText(emp.getId());
        logoImgView.setImage(new Image("img/driving_32.png"));
        titleLbl.setText(schoolName + " Driving School");
        studentNameTxt.setText(emp.getFullName());
        genderTxt.setText(emp.getGender());
        phoneTxt.setText(emp.getPhone());
        addressTxt.setText(emp.getPostalAddress());
        locationTxt.setText(emp.getLocation());
        physicalAddressTxt.setText(emp.getPhysicalAddress());
        dateTxt.setText(emp.getDateRegistered());
        dateRTxt.setText(emp.getDate());
        dobTxt.setText(emp.getDob());
        departmentTxt.setText(emp.getDepartment());
        statusTxt.setText(emp.getEmployeeStatus());
        hrsTxt.setText(emp.getWorkingHours());
        salaryTxt.setText(emp.getSalaryDesired());

        try{
            rs = empQuery.getNextOfKin(emp.getId());

            gurdianNameTxt.setText(rs.getString("name"));
            guardianPAddrTxt.setText(rs.getString("physicalAddress"));
            guardianPhoneTxt.setText(rs.getString("phone"));
        }catch(SQLException ex){
            makeAlert("warning", "Error Loading Guardian=> " + ex);
        }

        this.emp = emp;
    }
}
