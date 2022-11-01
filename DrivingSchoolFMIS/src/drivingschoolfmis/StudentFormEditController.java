package drivingschoolfmis;

import com.jfoenix.controls.JFXCheckBox;
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

import static drivingschoolfmis.NotificationClass.showNotification;

public class StudentFormEditController {
    Student student;
    ResultSet rs;

    @FXML
    private JFXTextField fullNameTxt;

    @FXML
    private JFXTextField phoneNumberTxt;

    @FXML
    private JFXTextArea addressTxt;

    @FXML
    private JFXTextField guardianNumberTxt;

    @FXML
    private ComboBox<String> locationComboBox;

    @FXML
    private JFXRadioButton maleRadioBtn;

    @FXML
    private JFXRadioButton femaleRadioBtn;

    @FXML
    private ComboBox<String> courseCombo;

    @FXML
    private ComboBox<String> categoryCombo;

    @FXML
    private JFXTextField trnTxt;

    @FXML
    private DatePicker dateTxt;

    @FXML
    private Label feesLbl;

    @FXML
    private JFXCheckBox graduatedCheck;



    @FXML
    private JFXCheckBox discountCheck;

    @FXML
    private JFXTextField discountTxt;

    @FXML
    private JFXCheckBox govtFeeCheck;

    @FXML
    void categoryComboOnAction(ActionEvent event) {
        new InitSetupForCombosAndToggles().categoryComboAction(courseCombo, categoryCombo, feesLbl);
    }

    @FXML
    void graduatedCheckOnAction(ActionEvent event) {

    }

    @FXML
    void courseComboOnAction(ActionEvent event) {
        String courseID = courseCombo.getSelectionModel().getSelectedItem();
        new InitSetupForCombosAndToggles().loadCategoriesForThisCourse(courseID, categoryCombo);
    }

    @FXML
    void discountCheckOnAction(ActionEvent event) {
        if(discountCheck.isSelected()){
            discountTxt.setVisible(true);
        }else{
            discountTxt.setVisible(false);
        }
    }

    @FXML
    void editBtnOnAction(ActionEvent event) {
        String gender;
        String dateRegistered;
        String trn = null;

        if(trnTxt.getText() != null && trnTxt.getText().length() == 0){
            trn = null;
        }

        if(maleRadioBtn.isSelected()){
            gender = "male";
        }else if (femaleRadioBtn.isSelected()){
            gender = "female";
        }else{
            gender = "";
        }


        try {
            dateRegistered = dateTxt.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }catch(NullPointerException ex){
            dateRegistered = "";
        }

        Student student = new Student(fullNameTxt.getText(), addressTxt.getText(), phoneNumberTxt.getText(),
                gender,courseCombo.getSelectionModel().getSelectedItem(),
                categoryCombo.getSelectionModel().getSelectedItem(),
                "", dateRegistered, guardianNumberTxt.getText(),
                locationComboBox.getSelectionModel().getSelectedItem(),
                trnTxt.getText(), discountCheck.isSelected(), govtFeeCheck.isSelected(), graduatedCheck.isSelected());

        if (new ValidateFieldsClass().validateStudent(student, discountTxt.getText())) {
            boolean edited = StudentQueries.editStudent(student, this.student.getId());

            if (!edited) {
                int count = 0;
                showNotification(student.getName() + " Has been Edited Succesfully");

                if(!student.isAnyDiscount()){
                    new DiscountQueries().deleteDiscount(this.student.getId());
                }else if(student.isAnyDiscount()){
                    try {
                        rs = new DiscountQueries().getDiscount(this.student.getId());
                        while(rs.next()){
                            new DiscountQueries().editDiscount(this.student.getId(), discountTxt.getText());
                            count++;
                        }
                    }catch(SQLException ex){

                    }

                    if(count == 0)
                    new DiscountQueries().addDiscount(new Discount(this.student.getId(),
                            discountTxt.getText()));
                }

                ((Node) event.getSource()).getScene().getWindow().hide();
                try {
                    FXMLLoader loader = new FXMLLoader();
                    loader.load(getClass().getResource("students.fxml").openStream());
                    StudentsController controller = loader.getController();
                    controller.initialize();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    void govtFeeCheckOnAction(ActionEvent event) {

    }


    public void initialize(Student student) {
        this.student = student;
        InitSetupForCombosAndToggles setup = new InitSetupForCombosAndToggles();
        setup.setGenderToggleGroup(maleRadioBtn, femaleRadioBtn);
        setup.loadIntoLocations(locationComboBox);
        setup.loadIntoCourses(courseCombo);

        autoFillForm();
    }

    private void autoFillForm() {
        fullNameTxt.setText(student.getName());
        phoneNumberTxt.setText(student.getPhone());
        addressTxt.setText(student.getAddress());
        guardianNumberTxt.setText(student.getGuardianPhone());
        locationComboBox.getSelectionModel().select(student.getLocation());

        if(student.getGender().equals("male")){
            maleRadioBtn.setSelected(true);
        }else if (student.getGender().equals("female")){
            femaleRadioBtn.setSelected(true);
        }else{
            maleRadioBtn.setSelected(false);
            femaleRadioBtn.setSelected(false);
        }

        courseCombo.getSelectionModel().select(student.getCourse());
        categoryCombo.getSelectionModel().select(student.getCourseType());
        trnTxt.setText(student.getTrn());
        dateTxt.setValue(LocalDate.parse(student.getDateRegistered()));

        if(student.isAnyDiscount()){
            discountCheck.setSelected(true);
            discountTxt.setVisible(true);

            try{
                rs = new DiscountQueries().getDiscount(student.getId());

                discountTxt.setText(rs.getString("amount"));
            }catch(SQLException ex){

            }

        }

        if(student.isAnyGovtFee()){
            govtFeeCheck.setSelected(true);
        }

        if(student.isGraduated()){
            graduatedCheck.setSelected(true);
        }

    }
}
