package drivingschoolfmis;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static drivingschoolfmis.AlertClass.makeAlert;

public class PaymentQueries {
    static ResultSet rs;
    static PreparedStatement pst;

    public static ResultSet getAllExpenses() {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement("SELECT * FROM expenses ORDER BY rowid DESC");
            rs = pst.executeQuery();

            return rs;
        } catch (SQLException ex) {
            Logger.getLogger(PaymentQueries.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }

    public static ResultSet getAllPayments() {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement(
                    "SELECT * FROM payments " +
                            "ORDER BY date DESC, expense");
            rs = pst.executeQuery();

            return rs;
        } catch (SQLException ex) {
            Logger.getLogger(PaymentQueries.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }

    public static ResultSet getPayment(String id) {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement("SELECT * FROM payments where id=?");
            pst.setString(1, id);
            rs = pst.executeQuery();

            return rs;
        } catch (SQLException ex) {
            Logger.getLogger(PaymentQueries.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }

    public static boolean addPayment(Payment p) {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement(
                    "INSERT INTO payments (expense, date, amount, mode, dateOfPayment, mirage," +
                            " user , ref) "
                            + "VALUES (?, datetime('now'), ?, ?, ?, ? ,?, ?)");
            pst.setString(1, p.getExpense());
            pst.setString(2, p.getAmount());
            pst.setString(3, p.getMode());
            pst.setString(4, p.getDateOfPayment());
            pst.setString(5, p.getMirage());
            pst.setString(6, p.getUser());
            pst.setString(7, p.getRef());

            return pst.execute();
        } catch (SQLException ex) {
            Logger.getLogger(PaymentQueries.class.getName()).log(Level.SEVERE, null, ex);
            makeAlert("error", "Error Adding payment\nThis employee name does not exist in expense table\n" +
                    "Please Add this employee name on the list of expense inorder to record payments");
        }
        return true;
    }

    public static ResultSet getExpense(String id) {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement("SELECT * FROM Expenses where name=?");
            pst.setString(1, id);
            rs = pst.executeQuery();

            return rs;
        } catch (SQLException ex) {
            Logger.getLogger(PaymentQueries.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }

    public static boolean addExpense(Expense e) {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement(
                    "INSERT INTO Expenses (name, date) "
                            + "VALUES (?,  datetime('now'))");
            pst.setString(1, e.getName());

            return pst.execute();
        } catch (SQLException ex) {
            Logger.getLogger(PaymentQueries.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    public static int deleteExpense(String name) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Please Confirm Deletion");
        //alert.setGraphic(GlyphsDude.createIcon(FontAwesomeIcon.REMOVE, "20px"));
        alert.setHeaderText(null);
        alert.setContentText("Are you sure; You wanna delete this Expense ? \n" +
                "Note that deleting this expense will delete all payments associated with it!\n" +
                "This procedure is irreversible- we hope you know what you are doing");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {
            try {
                pst = DrivingSchoolFMIS.conn.prepareStatement("DELETE FROM Expenses WHERE name=?");
                pst.setString(1, name);
                return pst.executeUpdate();

            } catch (SQLException ex) {
                Logger.getLogger(PaymentQueries.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 404;
    }

    public static int deletePayment(String id) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Please Confirm Deletion");
        //alert.setGraphic(GlyphsDude.createIcon(FontAwesomeIcon.REMOVE, "20px"));
        alert.setHeaderText(null);
        alert.setContentText("Are you sure; You wanna delete this Payment ?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {
            try {
                pst = DrivingSchoolFMIS.conn.prepareStatement("DELETE FROM payments WHERE id=?");
                pst.setString(1, id);
                return pst.executeUpdate();

            } catch (SQLException ex) {
                Logger.getLogger(PaymentQueries.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 404;
    }

    public static ResultSet getPaymentsFor(String expense) {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement(
                    "SELECT * FROM payments " +
                            "WHERE expense LIKE ?" +
                            "ORDER BY date DESC");

            pst.setString(1, "%" + expense + "%");
            rs = pst.executeQuery();

            return rs;
        } catch (SQLException ex) {
            Logger.getLogger(PaymentQueries.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }

    public boolean editPayment(Payment p, String id) {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement(
                    "UPDATE payments " +
                            "SET expense=?, amount=?, mode=?, ref=? , dateOfPayment=?, mirage=?" +
                            "WHERE id=?");
            pst.setString(1, p.getExpense());
            pst.setString(2, p.getAmount());
            pst.setString(3, p.getMode());
            pst.setString(4, p.getRef());
            pst.setString(5, p.getDateOfPayment());
            pst.setString(6, p.getMirage());
            pst.setString(7, id);

            return pst.execute();
        } catch (SQLException ex) {
            Logger.getLogger(PaymentQueries.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    } 
    
    public static int getLastPayment() {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement("SELECT max(id) from payments");
            rs = pst.executeQuery();

            return rs.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(PaymentQueries.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
}
