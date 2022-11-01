package drivingschoolfmis;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

import static drivingschoolfmis.NotificationClass.showNotification;

public class ReceiptFormController {
    Student student;
    ResultSet rs;
    CourseQueries courseQuery = new CourseQueries();
    Double balanceToValidation;

    @FXML
    private Label titleLbl;

    @FXML
    private Label courseLbl;

    @FXML
    private Label categoryLbl;

    @FXML
    private VBox receiptBox;

    @FXML
    private JFXTextField fullNameTxt;

    @FXML
    private JFXTextField amountTxt;

    @FXML
    private ComboBox<String> bpoCombo;

    @FXML
    private ComboBox<String> pmCombo;

    @FXML
    private DatePicker dateTxt;

    @FXML
    private JFXTextField referenceNoTxt;

    @FXML
    private Label discountLbl;

    @FXML
    private Label courseFeeLbl;

    @FXML
    private Label govtFeeLbl;

    @FXML
    private Label totalFeesLbl;

    @FXML
    private Label paidFeesLbl;

    @FXML
    private Label balanceLbl;

    @FXML
    private JFXButton historyBtn;

    @FXML
    void paymentHistoryAction(ActionEvent event) {
        ((Node)event.getSource()).getScene().getWindow().hide();

        try {
            FXMLLoader loader = new FXMLLoader();
            AnchorPane root = loader.load(getClass().getResource("FeesHistory.fxml").openStream());
            FeesHistoryController controller = (FeesHistoryController) loader.getController();
            controller.initialize(student);

            Scene scene = new Scene(root);
            StageManager.historyStage.setScene(scene);
            StageManager.historyStage.getIcons().add(new Image("img/history_16.png"));
            StageManager.historyStage.setTitle("Fees Payment History For " + student.getName());

            StageManager.historyStage.show();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @FXML
    void saveBtnOnAction(ActionEvent event) {
        String dateRegistered;
        try {
            dateRegistered = dateTxt.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }catch(NullPointerException ex){
            dateRegistered = "";
        }

        Receipts receipt = new Receipts(student.getId(), "date",dateRegistered,  amountTxt.getText(),
                pmCombo.getSelectionModel().getSelectedItem(), bpoCombo.getSelectionModel().getSelectedItem(),
                LoginDocumentController.userName, referenceNoTxt.getText());

        if (new ValidateFieldsClass().validateReceipt(receipt, balanceToValidation)) {
            boolean added = ReceiptQueries.addReceipt(receipt);

            if (added == false) {
                showNotification("Receipt Has been added Succesfully");
                clearFields();
                setLabels();
            }
        }
    }

    public void initialize(Student student){
        this.student = student;
        setLabels();
        setBpoCombo();
        setPaymentModeCombo();
    }

    private void setPaymentModeCombo() {
        new InitSetupForCombosAndToggles().loadPaymentsMode(pmCombo);
    }

    private void setBpoCombo() {
        ObservableList<String> options = FXCollections.observableArrayList();

        if (student.isAnyGovtFee()){
            options.addAll("Fees", "Blue Card", "Traffic Register Card",
                    "Provisional Driving Licence Fee", "Test Fee",
                    "License Fee for all Classes", "Medical Report"
            );
        }else{
            options.add("Fees");
        }

        bpoCombo.setItems(options);
    }

    private void setLabels() {
        titleLbl.setText("Name: " + student.getName());
        courseLbl.setText("Course: " + student.getCourse());
        categoryLbl.setText("Category: "+ student.getCourseType());
        fullNameTxt.setText(student.getName());

        FeesResults feesResults = new FeesCalculationsClass().FeesCalculations(student);

        courseFeeLbl.setText(feesResults.getCourseFee());
        govtFeeLbl.setText(feesResults.getGovtFee());
        discountLbl.setText(feesResults.getDiscountFee());
        totalFeesLbl.setText(feesResults.getTotalFee());
        paidFeesLbl.setText(feesResults.getTotalPaid());
        balanceLbl.setText(feesResults.getBalance());
        balanceToValidation = Double.parseDouble(feesResults.getBalance());

        hideReceipt(balanceToValidation);
    }

    private void hideReceipt(Double balance) {
        if(balance <= 0.0){
            receiptBox.setVisible(false);
        }else{
            receiptBox.setVisible(true);
        }
    }

    private void clearFields() {
        referenceNoTxt.setText("");
        amountTxt.setText("");
        bpoCombo.getSelectionModel().select(null);
        dateTxt.setValue(null);
        pmCombo.getSelectionModel().select(null);
    }
}
