package drivingschoolfmis;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ReceiptQueries {
    static PreparedStatement pst;
    static ResultSet rs;

    public static ResultSet getReceipt(String id) {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement("SELECT * FROM receipts where receiptNo=?");
            pst.setString(1, id);
            rs = pst.executeQuery();
            return rs;

        } catch (SQLException ex) {
            Logger.getLogger(StudentQueries.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }

    public static boolean addReceipt(Receipts receipt) {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement(
                    "INSERT INTO receipts (studentID, date, amount, modeOfPayment, paymentOf," +
                            " user , reference, dateOfPayment) "
                            + "VALUES (?, datetime('now') , ?, ?, ?, ? ,? , ? )");
            pst.setString(1, receipt.getStudent());
            pst.setString(2, receipt.getAmount());
            pst.setString(3, receipt.getMop());
            pst.setString(4, receipt.getBpo());
            pst.setString(5, receipt.getUser());
            pst.setString(6, receipt.getRef());
            pst.setString(7, receipt.getDateOfReceipt());

            return pst.execute();
        } catch (SQLException ex) {
            Logger.getLogger(ReceiptQueries.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    public static ResultSet getReceiptsFor(String studentID) {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement("SELECT * FROM receipts" +
                    " where studentID=? " +
                    "ORDER BY date DESC");
            pst.setString(1, studentID);
            rs = pst.executeQuery();
            return rs;

        } catch (SQLException ex) {
            Logger.getLogger(StudentQueries.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }

    public static ResultSet getReceipts(String studentID) {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement("Select * from receipts " +
                    "where studentID=? order by rowid DESC");
            pst.setString(1, studentID);
            rs = pst.executeQuery();
            return rs;

        } catch (SQLException ex) {
            Logger.getLogger(StudentQueries.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }

    public static int deleteReceipt(String id) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Please Confirm Deletion");
        //alert.setGraphic(GlyphsDude.createIcon(FontAwesomeIcon.REMOVE, "20px"));
        alert.setHeaderText(null);
        alert.setContentText("Are you sure; You wanna delete this fees payment ?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {
            try {
                pst = DrivingSchoolFMIS.conn.prepareStatement("DELETE FROM receipts WHERE receiptNo=?");
                pst.setString(1, id);
                return pst.executeUpdate();

            } catch (SQLException ex) {
                Logger.getLogger(CourseQueries.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 404;
    }

    public static ResultSet getNReceipts() {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement(
                    "Select receiptNo, fullname, rn.date AS aDate, amount, modeOfPayment," +
                            " paymentOf, user , reference, dateOfPayment " +
                            "from receipts AS rn INNER JOIN students ON rn.studentID=students.studentID " +
                            "order by fullname");
            rs = pst.executeQuery();
            return rs;

        } catch (SQLException ex) {
            Logger.getLogger(StudentQueries.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }

    public static ResultSet getFeesReceipts() {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement(
                    "Select receiptNo, fullname, rn.date AS aDate, amount, modeOfPayment," +
                            " paymentOf, user , reference, dateOfPayment " +
                            "from receipts AS rn INNER JOIN students ON rn.studentID=students.studentID " +
                            "WHERE paymentOf = ?  " +
                            "order by fullname");

            pst.setString(1, "Fees");
            rs = pst.executeQuery();
            return rs;

        } catch (SQLException ex) {
            Logger.getLogger(StudentQueries.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }

    public static int deleteReceipt(ObservableList<Receipts> selected) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Please Confirm Deletion");
        //alert.setGraphic(GlyphsDude.createIcon(FontAwesomeIcon., "20px"));
        alert.setHeaderText(null);
        alert.setContentText("Are you sure; wanna delete these Receipts ? ");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {
            for (int i = 0; i < selected.size(); i++) {

                try {
                    pst = DrivingSchoolFMIS.conn.prepareStatement("DELETE FROM receipts WHERE receiptNo=?");
                    pst.setString(1, selected.get(i).getId());

                    pst.executeUpdate();

                } catch (SQLException ex) {
                    Logger.getLogger(CourseQueries.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            return 101;
        }
        return 404;
    }

    public static ResultSet getReceiptsForThisYear(String selectedYear) {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement(
                    "Select receiptNo, fullname, rn.date AS aDate, amount, modeOfPayment, paymentOf, user , reference " +
                            "from receipts AS rn INNER JOIN students ON rn.studentID=students.studentID " +
                            "where aDate LIKE '2021' " +
                            "order by fullname");
            rs = pst.executeQuery();
            return rs;

        } catch (SQLException ex) {
            Logger.getLogger(StudentQueries.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }
}
