package drivingschoolfmis;

import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

import static drivingschoolfmis.AlertClass.makeAlert;
import static drivingschoolfmis.NotificationClass.showNotification;

public class StudentTestController {
    private Student student;
    private ObservableList<Test> testData = FXCollections.observableArrayList();
    private ResultSet rs;
    private TestQueries query = new TestQueries();

    @FXML
    private AnchorPane anchor;

    @FXML
    private Label titleLbl;

    @FXML
    private Label courseLbl;

    @FXML
    private Label categoryLbl;

    @FXML
    private JFXTextField fullNameTxt;

    @FXML
    private ComboBox<String> testComboBox;

    @FXML
    private JFXRadioButton passRadioBtn;

    @FXML
    private JFXRadioButton failRadioBtn;

    @FXML
    private DatePicker dateTxt;

    @FXML
    private TableView<Test> testTable;

    @FXML
    void saveBtnOnAction(ActionEvent event) {
        String result, dateOfTest;

        if(passRadioBtn.isSelected()){
            result = "pass";
        }else if (failRadioBtn.isSelected()){
            result = "fail";
        }else{
            result = "";
        }

        try {
            dateOfTest = dateTxt.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }catch(NullPointerException ex){
            dateOfTest = "";
        }

        Test test = new Test (
                    student.getId(),
                    testComboBox.getSelectionModel().getSelectedItem(),
                    "date",
                    dateOfTest,
                    result
                );

        if (new ValidateFieldsClass().validateTest(test)) {
                boolean added = query.addTest(test);;

                if (!added) {
                    showNotification(test.getTestName() + " for " + student.getName() +
                            " Has been added Succesfully");

                    clearTextFields();
                    loadTestData();
                }
        }
    }

    @FXML
    void deleteTest(ActionEvent event) {
        try{
            Test test = testTable.getSelectionModel().getSelectedItem();

            query.deleteTest(test.getId());

            loadTestData();

        }catch(NullPointerException ex){
            makeAlert("warning", "Nothing to delete\nSelect test from the table");
        }


    }

    public void initialize(Student student){
        this.student = student;
        anchor.getStylesheets().add(getClass().getResource("students.css").toExternalForm());

        setLabels();
        loadTestInComboBox();
        setRemarksToggleGroup();

        loadTestData();
        setTestTable();
    }

    private void loadTestData(){
        testData.clear();

        try {
            rs = query.getTestsFor(student.getId());

            while(rs.next()){
                testData.add(
                        new Test(
                                rs.getString("id"),
                                rs.getString("student"),
                                rs.getString("testName"),
                                rs.getString("date"),
                                rs.getString("dateOfTest"),
                                rs.getString("passOrFail")
                        )
                );
            }
        } catch(SQLException ex){
            makeAlert("error", "Error getting Test: \n" + ex);
        }

    }

    private void setTestTable() {
        testTable.setItems(testData);

        TableColumn col1 = new TableColumn("Test Taken");
        TableColumn col2 = new TableColumn("Result");
        TableColumn col3 = new TableColumn("Test Taken on");
        TableColumn col4 = new TableColumn("Date Added");

        col1.setMinWidth(100);
        col2.setMinWidth(50);
        col3.setMinWidth(90);
        col4.setMinWidth(100);

        testTable.getColumns().addAll(col1, col2, col3, col4);
        testTable.setEditable(true);

        col1.setCellValueFactory(new PropertyValueFactory<>("testName"));
        col2.setCellValueFactory(new PropertyValueFactory<>("passOrFail"));
        col3.setCellValueFactory(new PropertyValueFactory<>("dateOfTest"));
        col4.setCellValueFactory(new PropertyValueFactory<>("date"));

    }

    private void loadTestInComboBox() {
        ObservableList<String> test = FXCollections.observableArrayList();
        test.add("Aptitude test 1");
        test.add("Aptitude test 2");
        test.add("Road test");

        testComboBox.setItems(test);
    }

    private void setLabels() {
        titleLbl.setText("Tests for: " + student.getName());
        courseLbl.setText("Course: " + student.getCourse());
        categoryLbl.setText("Category: " + student.getCourseType());
        fullNameTxt.setText(student.getName());
    }

    public void setRemarksToggleGroup() {
        new InitSetupForCombosAndToggles().setGenderToggleGroup(passRadioBtn, failRadioBtn);
    }


    private void clearTextFields() {
        testComboBox.getSelectionModel().select(null);
        passRadioBtn.setSelected(false);
        passRadioBtn.setSelected(false);
        dateTxt.setValue(null);
    }
}
