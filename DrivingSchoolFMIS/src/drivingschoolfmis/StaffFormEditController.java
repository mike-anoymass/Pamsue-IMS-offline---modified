package drivingschoolfmis;

import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static drivingschoolfmis.AlertClass.makeAlert;
import static drivingschoolfmis.NotificationClass.showNotification;

public class StaffFormEditController {
    private Employee emp;
    private EmployeeQueries empQuery = new EmployeeQueries();
    ResultSet rs;

    @FXML
    private JFXTextField fullNameTxt;

    @FXML
    private DatePicker dobTxt;

    @FXML
    private JFXRadioButton maleRadioBtn;

    @FXML
    private JFXRadioButton femaleRadioBtn;

    @FXML
    private JFXTextField phoneNumberTxt;

    @FXML
    private JFXTextArea addressTxt;

    @FXML
    private JFXTextField physicalAddrTxt;

    @FXML
    private ComboBox<String> departmentCombo;

    @FXML
    private ComboBox<String> statusCombo;

    @FXML
    private JFXTextField hrsTxt;

    @FXML
    private JFXTextField salaryTxt;

    @FXML
    private ComboBox<String> locationComboBox;

    @FXML
    private DatePicker dateTxt;

    @FXML
    private Label feesLbl;

    @FXML
    private JFXTextField guardianNameTxt;

    @FXML
    private JFXTextField guardianNumberTxt;

    @FXML
    private JFXTextArea guardianPhysicalAddressTxt;

    @FXML
    void updateStaff(ActionEvent event) {
        String dateRegistered, dob;
        String gender;

        if (!maleRadioBtn.isSelected()) {
            if (femaleRadioBtn.isSelected()){
                gender = "female";
            }else{
                gender = "";
            }
        } else {
            gender = "male";
        }

        try {
            dateRegistered = dateTxt.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            dob = dobTxt.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }catch(NullPointerException ex){
            dateRegistered = "";
            dob= "";
        }

        NextOfKin nextOfKin = new NextOfKin(
                this.emp.getId(),
                guardianNameTxt.getText(),
                guardianNumberTxt.getText(),
                guardianPhysicalAddressTxt.getText()
        );

        Employee emp = new Employee(
                fullNameTxt.getText(), phoneNumberTxt.getText(),
                addressTxt.getText(), physicalAddrTxt.getText(),
                departmentCombo.getSelectionModel().getSelectedItem(),
                locationComboBox.getSelectionModel().getSelectedItem(),
                statusCombo.getSelectionModel().getSelectedItem(),
                salaryTxt.getText(), hrsTxt.getText(), "", dateRegistered, gender, dob
        );

        if (new ValidateFieldsClass().validateStaff(emp, nextOfKin)) {
            boolean edited = empQuery.editStaff(emp, this.emp.getId());

            if (!edited) {
                showNotification(emp.getFullName() + " Has been Edited Succesfully");
                empQuery.editNextOfKin(nextOfKin);

                ((Node) event.getSource()).getScene().getWindow().hide();
                try {
                    FXMLLoader loader = new FXMLLoader();
                    loader.load(getClass().getResource("Staff.fxml").openStream());
                    StaffController controller = loader.getController();
                    controller.initialize();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void initialize(Employee emp){
        this.emp = emp;
        InitSetupForCombosAndToggles setup = new InitSetupForCombosAndToggles();
        setup.setGenderToggleGroup(maleRadioBtn, femaleRadioBtn);
        setup.loadIntoLocations(locationComboBox);
        setup.loadIntoDepartments(departmentCombo);
        setup.loadIntoStatus(statusCombo);

        autoFillForm();

    }

    private void autoFillForm() {
        fullNameTxt.setText(emp.getFullName());
        phoneNumberTxt.setText(emp.getPhone());
        addressTxt.setText(emp.getPostalAddress());
        locationComboBox.getSelectionModel().select(emp.getLocation());

        if(emp.getGender().equals("male")){
            maleRadioBtn.setSelected(true);
        }else if (emp.getGender().equals("female")){
            femaleRadioBtn.setSelected(true);
        }else{
            maleRadioBtn.setSelected(false);
            femaleRadioBtn.setSelected(false);
        }

        statusCombo.getSelectionModel().select(emp.getEmployeeStatus());
        departmentCombo.getSelectionModel().select(emp.getDepartment());
        salaryTxt.setText(emp.getSalaryDesired());
        dateTxt.setValue(LocalDate.parse(emp.getDateRegistered()));
        dobTxt.setValue(LocalDate.parse(emp.getDob()));
        hrsTxt.setText(emp.getWorkingHours());
        physicalAddrTxt.setText(emp.getPhysicalAddress());

        try{
            rs = empQuery.getNextOfKin(emp.getId());

            guardianNameTxt.setText(rs.getString("name"));
            guardianPhysicalAddressTxt.setText(rs.getString("physicalAddress"));
            guardianNumberTxt.setText(rs.getString("phone"));
        }catch(SQLException ex){
            makeAlert("warning", "Error Loading Guardian=> " + ex);
        }

    }

}
