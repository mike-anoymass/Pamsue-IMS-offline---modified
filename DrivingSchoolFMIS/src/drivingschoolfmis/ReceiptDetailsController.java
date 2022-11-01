package drivingschoolfmis;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import static drivingschoolfmis.DrivingSchoolFMIS.schoolName;

public class ReceiptDetailsController {

    @FXML
    private Label receiptNoLbl;

    @FXML
    private Label balanceTxt;

    @FXML
    private ImageView logoImgView;

    @FXML
    private Label titleLbl;

    @FXML
    private JFXTextField dateTxt;

    @FXML
    private JFXTextField studentNameTxt;

    @FXML
    private JFXTextField modeOfPaymentTxt;

    @FXML
    private JFXTextField amountTxt;

    @FXML
    private JFXTextField paymentForTxt;

    @FXML
    private JFXTextField userTxt;

    public void initialize(Student student, Receipts r, String balance) {
        receiptNoLbl.setText(r.getId());
        balanceTxt.setText(balance);
        logoImgView.setImage(new Image("img/driving_32.png"));
        titleLbl.setText(schoolName + " Driving School");
        dateTxt.setText(r.getDateOfReceipt());
        studentNameTxt.setText(student.getName());
        amountTxt.setText(r.getAmount());
        modeOfPaymentTxt.setText(r.getMop());
        paymentForTxt.setText(r.getBpo());
        userTxt.setText(r.getUser());
    }
}
