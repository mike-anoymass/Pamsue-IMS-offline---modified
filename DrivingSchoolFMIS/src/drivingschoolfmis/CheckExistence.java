package drivingschoolfmis;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CheckExistence {
    private ResultSet rs;

    public boolean checkStudentExist(String id) {
        try {
            rs = StudentQueries.getStudent(id);
            while (rs.next()) {
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CheckExistence.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
}
