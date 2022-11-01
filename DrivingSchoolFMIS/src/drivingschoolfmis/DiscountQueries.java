package drivingschoolfmis;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static drivingschoolfmis.AlertClass.makeAlert;

public class DiscountQueries {
    private PreparedStatement pst;
    private ResultSet rs;

    public ResultSet getDiscounts() {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement("SELECT * FROM discounts");
            rs = pst.executeQuery();

            return rs;
        } catch (SQLException ex) {
            Logger.getLogger(DiscountQueries.class.getName()).log(Level.SEVERE, null, ex);
        }

        return rs;
    }

    public ResultSet getDiscount(String id) {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement("SELECT * FROM discounts where student=?");
            pst.setString(1, id);
            rs = pst.executeQuery();

            return rs;
        } catch (SQLException ex) {
            Logger.getLogger(CourseQueries.class.getName()).log(Level.SEVERE, null, ex);
        }

        return rs;
    }

    boolean addDiscount(Discount discount) {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement("INSERT INTO discounts (student, amount) VALUES (?, ?)");
            pst.setString(1, discount.getStudent());
            pst.setString(2, discount.getAmount());

            return pst.execute();
        } catch (SQLException ex) {
            Logger.getLogger(DiscountQueries.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    public boolean editDiscount(String id, String amount){
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement("UPDATE discounts " +
                    "SET amount=?" +
                    " WHERE student=?");
            pst.setString(1, amount);
            pst.setString(2, id);

            return pst.execute();
        } catch (SQLException ex) {
            Logger.getLogger(StudentQueries.class.getName()).log(Level.SEVERE, null, ex);
            makeAlert("error", "Failed to edit Discount -> " + ex);
        }
        return true;
    }


    public void deleteDiscount(String id) {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement("DELETE FROM discounts WHERE student=?");
            pst.setString(1, id);

            pst.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(StudentQueries.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
