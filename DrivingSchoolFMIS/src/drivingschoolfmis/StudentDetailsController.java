package drivingschoolfmis;

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

import static drivingschoolfmis.AlertClass.makeAlert;
import static drivingschoolfmis.DrivingSchoolFMIS.schoolName;

public class StudentDetailsController {
    private Student student;
    @FXML
    private Label studentIDLbl;

    @FXML
    private ImageView logoImgView;

    @FXML
    private Label titleLbl;

    @FXML
    private JFXTextField dateTxt;

    @FXML
    private JFXTextField studentNameTxt;

    @FXML
    private JFXTextField phoneTxt;

    @FXML
    private JFXTextField genderTxt;

    @FXML
    private JFXTextField addressTxt;

    @FXML
    private JFXTextField locationTxt;

    @FXML
    private JFXTextField guardianTxt;

    @FXML
    private JFXTextField courseTxt;

    @FXML
    private JFXTextField trnTxt;

    @FXML
    private JFXTextField categoryTxt;

    @FXML
    private JFXTextField dateCreatedTxt;

    @FXML
    private JFXTextField graduatedTxt;

    @FXML
    private Label govtFeeLbl;

    @FXML
    private Label discountLbl;

    @FXML
    void exploreTests(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            AnchorPane root = loader.load(getClass().getResource("StudentTest.fxml").openStream());
            StudentTestController controller = loader.getController();
            controller.initialize(student);

            Scene scene = new Scene(root);
            StageManager.studentTestStage.setScene(scene);
            StageManager.studentTestStage.getIcons().add(new Image("img/testicon_16.png"));
            StageManager.studentTestStage.setTitle("Exploring tests for " + student.getName());

            StageManager.studentTestStage.show();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }catch(NullPointerException ex){
            makeAlert("warning", "Student Not Found");
        }
    }

    @FXML
    void paymentHistory(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            AnchorPane root = loader.load(getClass().getResource("FeesHistory.fxml").openStream());
            FeesHistoryController controller = loader.getController();
            controller.initialize(student);

            Scene scene = new Scene(root);
            StageManager.historyStage.setScene(scene);
            StageManager.historyStage.getIcons().add(new Image("img/history_16.png"));
            StageManager.historyStage.setTitle("Fees Receipt History For " + student.getName());
            StageManager.historyStage.show();

        } catch(NullPointerException ex){
            makeAlert("warning", "Please Select a student from the table");
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @FXML
    void recordPayment(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            AnchorPane root = loader.load(getClass().getResource("ReceiptForm.fxml").openStream());
            ReceiptFormController controller = loader.getController();
            controller.initialize(student);

            Scene scene = new Scene(root);
            StageManager.receiptStage.setScene(scene);
            StageManager.receiptStage.getIcons().add(new Image("img/receipt_16.png"));
            StageManager.receiptStage.setTitle("Record Fees Receipt for " + student.getName());

            StageManager.receiptStage.show();
        } catch(NullPointerException ex){
            makeAlert("warning", "Please Select a student from the table");
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @FXML
    void update(ActionEvent event) {
        ((Node) event.getSource()).getScene().getWindow().hide();
        try {

            FXMLLoader loader = new FXMLLoader();
            AnchorPane root = loader.load(getClass().getResource("StudentFormEdit.fxml").openStream());
            StudentFormEditController controller = loader.getController();
            controller.initialize(student);

            Scene scene = new Scene(root);
            StageManager.studentFormEditStage.setScene(scene);
            StageManager.studentFormEditStage.getIcons().add(new Image("img/editicon_16.png"));
            StageManager.studentFormEditStage.setTitle("Update Student");

            StageManager.studentFormEditStage.show();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }catch(NullPointerException ex){
            makeAlert("warning", "Student Not Found");
        }
    }

    public void initialize(Student student) {
        studentIDLbl.setText(student.getId());
        logoImgView.setImage(new Image("img/driving_32.png"));
        titleLbl.setText(schoolName + " Driving School");
        studentNameTxt.setText(student.getName());
        genderTxt.setText(student.getGender());
        phoneTxt.setText(student.getPhone());
        addressTxt.setText(student.getAddress());
        locationTxt.setText(student.getLocation());
        guardianTxt.setText(student.getGuardianPhone());
        dateTxt.setText(student.getDateRegistered());
        courseTxt.setText(student.getCourse());
        categoryTxt.setText(student.getCourseType());
        trnTxt.setText(student.getTrn());
        dateCreatedTxt.setText(student.getDate());

        if (student.isGraduated()) {
            graduatedTxt.setText("Yes");
        } else {
            graduatedTxt.setText("No");
        }


        if(student.isAnyGovtFee()){
            govtFeeLbl.setText("Yes");
        }else{
            govtFeeLbl.setText("No");
        }

        if(student.isAnyDiscount()){
            discountLbl.setText("Yes");
        }else {
            discountLbl.setText("No");
        }

        this.student = student;
    }
}
