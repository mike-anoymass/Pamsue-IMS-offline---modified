/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drivingschoolfmis;

import static drivingschoolfmis.AlertClass.makeAlert;
import static drivingschoolfmis.PaymentQueries.pst;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Observable;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 *
 * @author ANOYMASS
 */
public class LoanQueries {
     public static boolean addLoan(Loan l) {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement(
                    "INSERT INTO loans (employee, amount, date, payment_id) "
                            + "VALUES (?, ? , datetime('now'), ?)");
            pst.setInt(1, l.getEmployee_id());
            pst.setDouble(2, l.getAmount());
            pst.setInt(3, l.getPayment_id());
           
            return pst.execute();
        } catch (SQLException ex) {
            Logger.getLogger(LoanQueries.class.getName()).log(Level.SEVERE, null, ex);
            makeAlert("error", "erroradding loan " + ex);
        }
        return true;
    }

    public ObservableList<Loan> getLoansFor(int employee_id) {
        ResultSet rs;
        ObservableList<Loan> loans = FXCollections.observableArrayList();
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement(
                    "Select * from loans where employee=?");
            pst.setInt(1, employee_id);
            rs = pst.executeQuery();
            
            while(rs.next()){
                loans.add(new Loan(
                        rs.getInt("id"),
                        rs.getInt("employee"),
                        rs.getDouble("amount"), 
                        rs.getString("date"), 
                        getBalance(rs.getInt("id"), rs.getDouble("amount")))
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoanQueries.class.getName()).log(Level.SEVERE, null, ex);
            makeAlert("error", "erroradding loan " + ex);
        }
        return loans;
    }
    
    public Loan getLoans(int loan_id) {
        ResultSet rs;
        ObservableList<Loan> loans = FXCollections.observableArrayList();
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement(
                    "Select * from loans where id=?");
            pst.setInt(1, loan_id);
            rs = pst.executeQuery();
            
            while(rs.next()){
                return new Loan(
                        rs.getInt("id"),
                        rs.getInt("employee"),
                        rs.getDouble("amount"), 
                        rs.getString("date"), 
                        getBalance(rs.getInt("id"), rs.getDouble("amount")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoanQueries.class.getName()).log(Level.SEVERE, null, ex);
            makeAlert("error", "erroradding loan " + ex);
        }
        return null;
    }
    
    public ObservableList<LoanSettlement> getSettlements(int loan_id) {
        ResultSet rs;
        ObservableList<LoanSettlement> loanSettlement = FXCollections.observableArrayList();
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement(
                    "Select * from loan_settlements where loan_id=?");
            pst.setInt(1, loan_id);
            rs = pst.executeQuery();
            
            while(rs.next()){
                loanSettlement.add(new LoanSettlement(
                        rs.getInt("loan_id"),
                        rs.getInt("id"),
                        rs.getDouble("loan_amount"),
                        rs.getString("date"))
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoanQueries.class.getName()).log(Level.SEVERE, null, ex);
            makeAlert("error", "erroradding loan " + ex);
        }
        return loanSettlement;
    }

    public double getBalance(int loanID, double loan_amount) {
        ObservableList<LoanSettlement> settlements = FXCollections.observableArrayList();
        settlements.addAll(getSettlements(loanID));
        double balance = 0.0;
        double totalPaid = 0.0;
        
        if(!settlements.isEmpty()){
            for(LoanSettlement ls : settlements){
               totalPaid += ls.getAmount();
            }
            
            balance = loan_amount - totalPaid;
            
            return balance;
        }
        
        return loan_amount;
    }
    
     public static boolean addSettlement(LoanSettlement l) {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement(
                    "INSERT INTO loan_settlements (loan_id, loan_amount, date) "
                            + " VALUES (?, ?, datetime('now'))");
            
            pst.setInt(1, l.getLoan_id());
            pst.setDouble(2, l.getAmount());
           
            return pst.execute();
        } catch (SQLException ex) {
            Logger.getLogger(LoanQueries.class.getName()).log(Level.SEVERE, null, ex);
            makeAlert("error", "error adding loan settlement " + ex);
        }
        return true;
    }
     
      public int delete(int id) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Please Confirm Deletion");
        // alert.setGraphic(GlyphsDude.createIcon(FontAwesomeIcon.RemoVe, "20px"));
        alert.setHeaderText(null);
        alert.setContentText("Are you sure; wanna delete, this loan settlement ?\n" +
                "This procedure is irreversible- we hope you know what you are doing");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {
            try {
                pst = DrivingSchoolFMIS.conn.prepareStatement("DELETE FROM loan_settlements WHERE id=?");
                pst.setInt(1, id);

                return pst.executeUpdate();

            } catch (SQLException ex) {
                Logger.getLogger(CourseQueries.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 404;
    }

}
