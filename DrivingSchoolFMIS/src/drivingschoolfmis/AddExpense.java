package drivingschoolfmis;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static drivingschoolfmis.AlertClass.makeAlert;

public class AddExpense {
    ResultSet rs;

    public boolean add(Expense e) {
        if (validateExpenseFields(e)) {
            if (checkExpense(e.getName())) {
                boolean added = PaymentQueries.addExpense(e);
                return added;
            } else {
                makeAlert("warning", "The Expense you entered Exist");
            }
        }
        return true;
    }

    private boolean validateExpenseFields(Expense e) {
        if (!e.getName().isEmpty()) {
            return true;
        } else {
            makeAlert("warning", "Please complete the field");
        }

        return false;
    }

    private boolean checkExpense(String id){
        try {
            rs = PaymentQueries.getExpense(id);
            while (rs.next()) {
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CoursesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
}
