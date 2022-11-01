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

import static drivingschoolfmis.AlertClass.makeAlert;
import static drivingschoolfmis.NotificationClass.showNotification;

public class TestQueries {
    private PreparedStatement pst;
    private ResultSet rs;

    public ResultSet getTests() {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement("SELECT fullname, tests.id AS testID, " +
                    "testName, tests.date AS testDate, passOrFail, dateOfTest" +
                    " FROM tests " +
                    "INNER JOIN students ON students.studentID=tests.student " +
                    "ORDER BY fullname, dateOfTest, tests.date");
            rs = pst.executeQuery();

            return rs;
        } catch (SQLException ex) {
            Logger.getLogger(DiscountQueries.class.getName()).log(Level.SEVERE, null, ex);
        }

        return rs;
    }

    public ResultSet getTestsFor(String id) {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement("SELECT * FROM tests" +
                    " where student=? " +
                    "ORDER BY dateOfTest DESC, date, testName, passOrFail");
            pst.setString(1, id);
            rs = pst.executeQuery();

            return rs;
        } catch (SQLException ex) {
            Logger.getLogger(CourseQueries.class.getName()).log(Level.SEVERE, null, ex);
        }

        return rs;
    }

    boolean addTest(Test test) {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement("INSERT INTO " +
                    "tests (student, testName, date , dateOfTest, passOrFail)" +
                    " VALUES (?, ?, datetime('now'), ? ,?)");
            pst.setString(1, test.getStudent());
            pst.setString(2, test.getTestName());
            pst.setString(3, test.getDateOfTest());
            pst.setString(4, test.getPassOrFail());

            return pst.execute();
        } catch (SQLException ex) {
            Logger.getLogger(DiscountQueries.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }


    public void deleteTest(String id) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Please Confirm Deletion");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure; wanna delete this test record ?\n" +
                "This procedure is irreversible- we hope you know what you are doing");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {
            try {
                pst = DrivingSchoolFMIS.conn.prepareStatement("DELETE FROM tests WHERE id=?");
                pst.setString(1, id);

                pst.executeUpdate();

                showNotification("Test Deleted Successfully");

            } catch (SQLException ex) {
                Logger.getLogger(StudentQueries.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public int deleteMany(ObservableList<Test> selectedTests) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Please Confirm Deletion");
        //alert.setGraphic(GlyphsDude.createIcon(FontAwesomeIcon., "20px"));
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete ? \n" + selectedTests.size() +
                " Test(s) will be deleted \n" +
                "This Procedure is irreversible- we hope that you know what you are doing");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {


            for (int i = 0; i < selectedTests.size(); i++) {

                try {
                    pst = DrivingSchoolFMIS.conn.prepareStatement("DELETE FROM tests WHERE id=?");
                    pst.setString(1, selectedTests.get(i).getId());

                    pst.executeUpdate();

                } catch (SQLException ex) {
                    Logger.getLogger(StudentQueries.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            return 101;
        }
        return 404;
    }
}
