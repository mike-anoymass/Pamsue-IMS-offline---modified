package drivingschoolfmis;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static drivingschoolfmis.AlertClass.makeAlert;
import static drivingschoolfmis.NotificationClass.showNotification;
import static drivingschoolfmis.ProceedConfirmation.confirm;

public class StudentFormAddController implements Initializable {

    private ResultSet rs;

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
    private JFXCheckBox discountCheck;

    @FXML
    private JFXTextField discountTxt;

    @FXML
    private JFXCheckBox govtFeeCheck;

    @FXML
    private Label feesLbl;

    @FXML
    void categoryComboOnAction(ActionEvent event) {
       new InitSetupForCombosAndToggles().categoryComboAction(courseCombo, categoryCombo, feesLbl);
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
    void govtFeeCheckOnAction(ActionEvent event) {

    }

    @FXML
    void saveBtnOnAction(ActionEvent event) throws IOException {
        String gender, course;
        String dateRegistered;
        String[] courseArray;
        String trn = "";
        course = "";

        if(trnTxt.getText().length() == 0){
            trn = null;
        }

        if(maleRadioBtn.isSelected()){
            gender = "male";
        }else if (femaleRadioBtn.isSelected()){
            gender = "female";
        }else{
            gender = "";
        }

        if(courseCombo.getSelectionModel().getSelectedItem() != null){
            courseArray = courseCombo.getSelectionModel().getSelectedItem().split("~");
            course =  courseArray[1];
        }


        try {
            dateRegistered = dateTxt.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }catch(NullPointerException ex){
            dateRegistered = "";
        }

        Student student = new Student(fullNameTxt.getText(), addressTxt.getText(), phoneNumberTxt.getText(),
                gender,course, categoryCombo.getSelectionModel().getSelectedItem(),
                "", dateRegistered, guardianNumberTxt.getText(),
                locationComboBox.getSelectionModel().getSelectedItem(),
                trn, discountCheck.isSelected(), govtFeeCheck.isSelected(), false);

        if (new ValidateFieldsClass().validateStudent(student, discountTxt.getText())) {
            if (new CheckExistence().checkStudentExist(student.getId())) {
                boolean added = StudentQueries.addStudent(student);

                if (!added) {
                    showNotification(student.getName() + " Has been added Succesfully");

                    if(student.isAnyDiscount()){
                        try {
                            rs = StudentQueries.getStudentByPhone(student.getPhone());
                            new DiscountQueries().addDiscount(new Discount(rs.getString("studentID"), discountTxt.getText()));
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }

                    clearTextFields();
                    boolean confirmed = confirm("Student");

                    if(!confirmed){
                        ((Node) event.getSource()).getScene().getWindow().hide();
                    }
                    
                    FXMLLoader loader = new FXMLLoader();
                    loader.load(getClass().getResource("Students.fxml").openStream());
                    StudentsController controller = loader.getController();

                    controller.initialize();
                }
            } else {
                makeAlert("warning", "The Student you entered Exist");
            }
        }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        InitSetupForCombosAndToggles setup = new InitSetupForCombosAndToggles();
        setup.setGenderToggleGroup(maleRadioBtn, femaleRadioBtn);
        setup.loadIntoLocations(locationComboBox);
        setup.loadIntoCourses(courseCombo);
    }

    private void clearTextFields() {
        trnTxt.setText("");
        fullNameTxt.setText("");
        addressTxt.setText("");
        phoneNumberTxt.setText("");
        guardianNumberTxt.setText("");
        discountTxt.setText("");
        locationComboBox.getSelectionModel().select(null);
        courseCombo.getSelectionModel().select(null);
        categoryCombo.getSelectionModel().select(null);
        dateTxt.setValue(null);
        feesLbl.setText("");

        maleRadioBtn.setSelected(false);
        femaleRadioBtn.setSelected(false);
        discountCheck.setSelected(false);
        discountTxt.setVisible(false);
        govtFeeCheck.setSelected(false);
    }

}
