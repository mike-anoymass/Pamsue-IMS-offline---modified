/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drivingschoolfmis;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import static drivingschoolfmis.AlertClass.makeAlert;

/**
 *
 * @author ANOYMASS
 */
public class StudentQueries {

    private static PreparedStatement pst;
    private static ResultSet rs;

    public static ResultSet getStudentsWithFees() {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement(
                    "SELECT studentID, fullname, postalAddress, phone, "
                            + "email, course, qualification, students.date AS dateAdded, students.duration AS courseType, fees  "
                            + "FROM students "
                            + "INNER JOIN courseDuration ON students.course=courseDuration.courseID" +
                            " and students.duration=courseDuration.duration");
            rs = pst.executeQuery();
            return rs;

        } catch (SQLException ex) {
            Logger.getLogger(StudentQueries.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }

    public static ResultSet getStudents() {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement("Select * from students order by date DESC");
            rs = pst.executeQuery();
            return rs;

        } catch (SQLException ex) {
            Logger.getLogger(StudentQueries.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }

    public static ResultSet getStudentsWithDiscount() {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement("Select * from students " +
                    "WHERE anyDiscount=?" +
                    " order by date DESC");
            pst.setBoolean(1, true);
            rs = pst.executeQuery();
            return rs;

        } catch (SQLException ex) {
            Logger.getLogger(StudentQueries.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }

    public static ResultSet getStudentsGraduated() {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement("Select * from students " +
                    "WHERE graduated=?" +
                    " order by date DESC");
            pst.setBoolean(1, true);
            rs = pst.executeQuery();
            return rs;

        } catch (SQLException ex) {
            Logger.getLogger(StudentQueries.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }

    public static ResultSet getStudentsNotGraduated() {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement("Select * from students " +
                    "WHERE graduated=?" +
                    " order by date DESC");
            pst.setBoolean(1, false);
            rs = pst.executeQuery();
            return rs;

        } catch (SQLException ex) {
            Logger.getLogger(StudentQueries.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }

    public static ResultSet getStudentsWithGvtFee() {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement("Select * from students " +
                    "WHERE anyGovernmentFee=?" +
                    " order by date DESC");
            pst.setBoolean(1, true);
            rs = pst.executeQuery();
            return rs;

        } catch (SQLException ex) {
            Logger.getLogger(StudentQueries.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }

    public static ResultSet getStudent(String id) {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement("SELECT * FROM students where studentID=?");
            pst.setString(1, id);
            rs = pst.executeQuery();
            return rs;


        } catch (SQLException ex) {
            Logger.getLogger(StudentQueries.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }

    static boolean addStudent(Student student) {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement(
                    "INSERT INTO students (fullname,gender, phone, guardianPhoneNumber," +
                            "postalAddress,course , duration, location, trn, dateRegistered,date," +
                            "anyDiscount,anyGovernmentFee, graduated) "
                            + "VALUES (?, ?, ?, ?, ?, ? ,? , ? ,?, ?, datetime('now'), ?, ? ,?)");
            pst.setString(1, student.getName());
            pst.setString(2, student.getGender());
            pst.setString(3, student.getPhone());
            pst.setString(4, student.getGuardianPhone());
            pst.setString(5, student.getAddress());
            pst.setString(6, student.getCourse());
            pst.setString(7, student.getCourseType());
            pst.setString(8, student.getLocation());
            pst.setString(9, student.getTrn());
            pst.setString(10, student.getDateRegistered());
            pst.setBoolean(11, student.isAnyDiscount());
            pst.setBoolean(12, student.isAnyGovtFee());
            pst.setBoolean(13, student.isGraduated());

            return pst.execute();
        } catch (SQLException ex) {
            Logger.getLogger(StudentQueries.class.getName()).log(Level.SEVERE, null, ex);
            makeAlert("error", ex.toString());
        }
        return true;
    }

    static int deleteStudents(ObservableList<Student> selectedStudents) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Please Confirm Deletion");
        //alert.setGraphic(GlyphsDude.createIcon(FontAwesomeIcon., "20px"));
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete ? \n" + selectedStudents.size() +
                " Student(s) will be deleted ");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {


            for (int i = 0; i < selectedStudents.size(); i++) {

                try {
                    pst = DrivingSchoolFMIS.conn.prepareStatement("DELETE FROM students WHERE studentID=?");
                    pst.setString(1, selectedStudents.get(i).getId());

                    pst.executeUpdate();

                } catch (SQLException ex) {
                    Logger.getLogger(StudentQueries.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            return 101;
        }
        return 404;
    }

    public static boolean editStudent(Student student, String id){
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement("UPDATE students " +
                    "SET fullname=?,gender=?, phone=?, guardianPhoneNumber=?," +
                    "postalAddress=?, location=?, trn=?, dateRegistered=?," +
                    "anyDiscount=?,anyGovernmentFee=?, graduated=? " +
                    " WHERE studentID=?");
            pst.setString(1, student.getName());
            pst.setString(2, student.getGender());
            pst.setString(3, student.getPhone());
            pst.setString(4, student.getGuardianPhone());
            pst.setString(5, student.getAddress());
            pst.setString(6, student.getLocation());
            pst.setString(7, student.getTrn());
            pst.setString(8, student.getDateRegistered());
            pst.setBoolean(9, student.isAnyDiscount());
            pst.setBoolean(10, student.isAnyGovtFee());
            pst.setBoolean(11, student.isGraduated());
            pst.setString(12, id);

            return pst.execute();
        } catch (SQLException ex) {
            Logger.getLogger(StudentQueries.class.getName()).log(Level.SEVERE, null, ex);
            makeAlert("error", "Failed to edit Student -> " + ex);
        }
        return true;
    }

    public static int countStudents() {
        int count = 0;
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement("Select count(*) FROM students");

            rs = pst.executeQuery();
            rs.next();
            count = rs.getInt(1);
            return count;

        } catch (SQLException ex) {
            Logger.getLogger(StudentQueries.class.getName()).log(Level.SEVERE, null, ex);
        }

        return count;
    }

    public static int countStudentsInThisCourse(String code) {
        int count = 0;
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement("Select count(*) FROM students " +
                    "where course=?");

            pst.setString(1, code);
            rs = pst.executeQuery();
            rs.next();
            count = rs.getInt(1);
            return count;

        } catch (SQLException ex) {
            Logger.getLogger(StudentQueries.class.getName()).log(Level.SEVERE, null, ex);
        }

        return count;
    }

    public static ResultSet getStudentByPhone(String phone) {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement("SELECT * FROM students where phone=?");
            pst.setString(1, phone);
            rs = pst.executeQuery();
            return rs;

        } catch (SQLException ex) {
            Logger.getLogger(StudentQueries.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }
}
