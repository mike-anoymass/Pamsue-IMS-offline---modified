package drivingschoolfmis;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InitSetupForCombosAndToggles {

    ResultSet rs;

    public void loadIntoCourses(ComboBox<String> courseCombo) {
        ObservableList<String> courseData = FXCollections.observableArrayList();
        courseData.clear();
        try {
            rs = new CourseQueries().getAllCourses();
            while (rs.next()) {
                courseData.add(rs.getString("id") + "~" + rs.getString("code"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(StudentFormAddController.class.getName()).log(Level.SEVERE, null, ex);
        }
        courseCombo.setItems(courseData);
    }

    public void loadIntoLocations(ComboBox<String> locationComboBox) {
        ObservableList<String> locations = FXCollections.observableArrayList();
        locations.add("Lilongwe");
        locations.add("Blantyre");
        locationComboBox.setItems(locations);
    }

    public void setGenderToggleGroup(RadioButton maleRadioBtn, RadioButton femaleRadioBtn) {
        ToggleGroup gender = new ToggleGroup();

        maleRadioBtn.setToggleGroup(gender);
        femaleRadioBtn.setToggleGroup(gender);
    }

    public void loadCategoriesForThisCourse(String courseID, ComboBox<String> categoryCombo) {
        if(courseID != null && courseID.length() > 0 ) {
            String[] courseArray;
            ObservableList<String> categoryData = FXCollections.observableArrayList();
            courseArray = courseID.split("~");

            courseID = courseArray[0];

            try {
                rs = new CourseQueries().getDurationsForThisCourse(courseID);
                while (rs.next()) {
                    categoryData.add(rs.getString("name"));
                }

            } catch (SQLException ex) {
                Logger.getLogger(StudentsController.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (categoryData.size() > 0){
                categoryCombo.setItems(categoryData);
            }else{
                categoryCombo.getItems().clear();
            }
        }
    }

    public void categoryComboAction(ComboBox<String> courseCombo, ComboBox<String> categoryCombo, Label feesLbl){
        String[] courseArray;
        String category, courseCode;
        Double fees;
        courseCode = "";


        if(courseCombo.getSelectionModel().getSelectedItem() != null){
            courseArray = courseCombo.getSelectionModel().getSelectedItem().split("~");
            courseCode = courseArray[1];
        }

        category = categoryCombo.getSelectionModel().getSelectedItem();

        if(courseCode.length() > 0 && category != null){
            fees = new CategoryFees().getCategoryFees(courseCode, category);
            feesLbl.setVisible(true);
            feesLbl.setText("MK" + fees);
        }else{
            feesLbl.setText("");
        }
    }

    public void loadIntoDepartments(ComboBox<String> departmentCombo) {
        ObservableList<String> options = FXCollections.observableArrayList();
        options.add("Driver");
        options.add("Instructor");
        options.add("Admin");
        options.add("Receptionist");
        options.add("Guard");
        options.add("Cleaner");
        departmentCombo.setItems(options);
    }

    public void loadIntoStatus(ComboBox<String> statusCombo) {
        ObservableList<String> options = FXCollections.observableArrayList();
        options.add("Full Time");
        options.add("Part Time");
        statusCombo.setItems(options);
    }

    public void loadPaymentsMode(ComboBox<String> pmCombo) {
        ObservableList<String> pm = FXCollections.observableArrayList();

        pm.add("Cash");
        pm.add("Cheque");
        pm.add("Airtel Money");
        pm.add("TNM Mpamba");
        pm.add("Mo626");

        pmCombo.setItems(pm);
    }
}
