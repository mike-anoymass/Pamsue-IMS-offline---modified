package drivingschoolfmis;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;

import static drivingschoolfmis.AlertClass.makeAlert;
import static drivingschoolfmis.NotificationClass.showNotification;

public class StaffPaymentFormController {

    Employee emp;
    EmployeeQueries empQuery = new EmployeeQueries();

    @FXML
    private Label titleLbl;

    @FXML
    private Label departmentLbl;

    @FXML
    private Label salaryLbl;

    @FXML
    private VBox receiptBox;

    @FXML
    private JFXTextField nameTxt;

    @FXML
    private JFXTextField amountTxt;

    @FXML
    private ComboBox<String> paymentTypeCombo;

    @FXML
    private ComboBox<String> pmCombo;

    @FXML
    private DatePicker dateTxt;

    @FXML
    private JFXTextField referenceNoTxt;

    @FXML
    void saveBtnOnAction(ActionEvent event) {
        Payment p = setPayment();

        boolean added = new AddPayment().add(p);
     
        if (added == false) {
            showNotification("Payment Has been added Succesfully");
            if (paymentTypeCombo.getSelectionModel().getSelectedItem() != null
                    && paymentTypeCombo.getSelectionModel().getSelectedItem().equals("Loan")) {
                LoanQueries.addLoan(new Loan(Integer.parseInt(emp.getId()), Double.parseDouble(p.getAmount()), PaymentQueries.getLastPayment()));
            }
            clearFields();
        }

    }

    public void initialize(Employee emp) {
        this.emp = emp;
        setFields();
    }

    private void setFields() {
        ObservableList<String> options = FXCollections.observableArrayList();
        options.addAll("Salary", "Advance", "Loan");
        paymentTypeCombo.setItems(options);

        new InitSetupForCombosAndToggles().loadPaymentsMode(pmCombo);

        titleLbl.setText(emp.getFullName());
        nameTxt.setText(emp.getFullName());
        departmentLbl.setText(emp.getDepartment());
        salaryLbl.setText("MK" + emp.getSalaryDesired());
    }

    private Payment setPayment() {
        String date;
        try {
            date = dateTxt.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (NullPointerException ex) {
            date = "";
        }

        Payment p = new Payment(
                nameTxt.getText(), "date", date, amountTxt.getText(),
                pmCombo.getSelectionModel().getSelectedItem(), referenceNoTxt.getText(),
                paymentTypeCombo.getSelectionModel().getSelectedItem(),
                LoginDocumentController.userName
        );

        return p;
    }

    private void clearFields() {
        pmCombo.getSelectionModel().select(null);
        paymentTypeCombo.getSelectionModel().select(null);
        dateTxt.setValue(null);
        referenceNoTxt.setText("");
        amountTxt.setText("");
    }
}
