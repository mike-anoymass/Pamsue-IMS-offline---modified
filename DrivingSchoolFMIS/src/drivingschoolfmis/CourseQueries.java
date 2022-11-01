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

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import static drivingschoolfmis.AlertClass.makeAlert;

/**
 *
 * @author ANOYMASS
 */
public class CourseQueries {

    private PreparedStatement pst;
    private ResultSet rs;

    public ResultSet getAllCourses() {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement("SELECT * FROM courses ORDER BY id DESC");
            rs = pst.executeQuery();

            return rs;
        } catch (SQLException ex) {
            Logger.getLogger(CourseQueries.class.getName()).log(Level.SEVERE, null, ex);
        }

        return rs;
    }

    public ResultSet getCourse(String courseCode) {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement("SELECT * FROM courses where code=?");
            pst.setString(1, courseCode);
            rs = pst.executeQuery();

            return rs;
        } catch (SQLException ex) {
            Logger.getLogger(CourseQueries.class.getName()).log(Level.SEVERE, null, ex);
        }

        return rs;
    }

    boolean addCourse(Course course) {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement("INSERT INTO courses (code, name, governmentFee)" +
                    " VALUES (?, ?, ?)");
            pst.setString(1, course.getCode());
            pst.setString(2, course.getName());
            pst.setString(3, course.getGovernmentFee());

            return pst.execute();
        } catch (SQLException ex) {
            Logger.getLogger(CourseQueries.class.getName()).log(Level.SEVERE, null, ex);

        }
        return true;
    }

    public int deleteCourse(String text) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Please Confirm Deletion");
        // alert.setGraphic(GlyphsDude.createIcon(FontAwesomeIcon.RemoVe, "20px"));
        alert.setHeaderText(null);
        alert.setContentText("Are you sure; wanna delete, " + text +"\n" +
                "Note that deleting this course will delete all students belonging to this course\n" +
                "This procedure is irreversible- we hope you know what you are doing");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {
            try {
                pst = DrivingSchoolFMIS.conn.prepareStatement("DELETE FROM courses WHERE code=?");
                pst.setString(1, text);

                return pst.executeUpdate();

            } catch (SQLException ex) {
                Logger.getLogger(CourseQueries.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 404;
    }

    boolean editCourse(Course course) {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement("UPDATE courses SET name=?," +
                    " code=?, governmentFee=?" +
                    " WHERE id=?");
            pst.setString(1, course.getName());
            pst.setString(2, course.getCode());
            pst.setString(3, course.getGovernmentFee());
            pst.setString(4, course.getId());

            return pst.execute();
        } catch (SQLException ex) {
            Logger.getLogger(CourseQueries.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    ResultSet getAllCourseTypes() {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement("SELECT * FROM durations ORDER BY id DESC");
            rs = pst.executeQuery();

            return rs;
        } catch (SQLException ex) {
            Logger.getLogger(CourseQueries.class.getName()).log(Level.SEVERE, null, ex);
        }

        return rs;
    }

    ResultSet getCourseType(String name) {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement("SELECT * FROM durations where name=?");
            pst.setString(1, name);
            rs = pst.executeQuery();

            return rs;
        } catch (SQLException ex) {
            Logger.getLogger(CourseQueries.class.getName()).log(Level.SEVERE, null, ex);
        }

        return rs;
    }

    boolean addCourseType(CourseType cType) {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement("INSERT INTO durations (name, days) VALUES (?, ?)");
            pst.setString(1, cType.getName());
            pst.setString(2, cType.getDays());

            return pst.execute();
        } catch (SQLException ex) {
            Logger.getLogger(CourseQueries.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    boolean editCourseType(CourseType cType) {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement("UPDATE durations SET days=?, name=? WHERE id=?");
            pst.setString(1, cType.getDays());
            pst.setString(2, cType.getName());
            pst.setString(3, cType.getId());

            return pst.execute();
        } catch (SQLException ex) {
            Logger.getLogger(CourseQueries.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    int deleteCourseType(String text) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Please Confirm Deletion");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure; wanna delete this category ?\n" +
                "Note that deleting this category will delete all students and courses belonging to it\n" +
                "This procedure is irreversible- we hope you know what you are doing");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {
            try {
                pst = DrivingSchoolFMIS.conn.prepareStatement("DELETE FROM durations WHERE id=?");
                pst.setString(1, text);

                return pst.executeUpdate();

            } catch (SQLException ex) {
                makeAlert("error", "You can not" +
                        " delete this category\nReason: " +
                        "This category has students and courses associated with it");
                Logger.getLogger(CourseQueries.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 404;
    }

    ResultSet getAllCourseDurations() {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement("SELECT courseID , duration, "
                    + "code, durations.name AS category, fees, date "
                    + "FROM courseDuration"
                    +" INNER JOIN courses ON courseDuration.courseID=courses.id "
                    +" INNER JOIN durations ON courseDuration.duration=durations.id "
                    + "ORDER BY date DESC, code");
            rs = pst.executeQuery();

            return rs;
        } catch (SQLException ex) {
            Logger.getLogger(CourseQueries.class.getName()).log(Level.SEVERE, null, ex);
        }

        return rs;
    }

    ResultSet getCourseInfo(String courseID, String durationName) {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement("SELECT * FROM courseDuration where courseID=? and duration=?");
            pst.setString(1, courseID);
            pst.setString(2, durationName);
            rs = pst.executeQuery();

            return rs;
        } catch (SQLException ex) {
            Logger.getLogger(CourseQueries.class.getName()).log(Level.SEVERE, null, ex);
        }

        return rs;
    }

    boolean addCourseInfo(CourseDuration info) {

        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement("INSERT INTO courseDuration (courseID, duration, fees, date) " +
                    "VALUES (?, ?, ?, datetime('now'))");
            pst.setString(1, info.getCourseID());
            pst.setString(2, info.getDurationID());
            pst.setString(3, info.getAmount());

            return pst.execute();
        } catch (SQLException ex) {
            Logger.getLogger(CourseQueries.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    int deleteCourseInfo(String code, String duration) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Please Confirm Deletion");
        //alert.setGraphic(GlyphsDude.createIcon(FontAwesomeIcon.REMOVE, "20px"));
        alert.setHeaderText(null);
        alert.setContentText("Are you sure; wanna delete this record? \n" +
                "Deleting this record will delete all students associated with it!\n" +
                "This procedure is irreversible- we hope that you know what you are doing");

        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {
            try {
                pst = DrivingSchoolFMIS.conn.prepareStatement("DELETE FROM courseDuration" +
                        " WHERE courseID=? and duration=?");
                pst.setString(1, code);

                pst.setString(2, duration);

                return pst.executeUpdate();

            } catch (SQLException ex) {
                Logger.getLogger(CourseQueries.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 404;
    }

    public int countCourses() {
        int count = 0;
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement("Select count(*) FROM courses");

            rs = pst.executeQuery();
            rs.next();
            count = rs.getInt(1);
            return count;

        } catch (SQLException ex) {
            Logger.getLogger(CourseQueries.class.getName()).log(Level.SEVERE, null, ex);
        }

        return count;
    }

    public int countCourseTypes() {
        int count = 0;
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement("Select count(*) FROM durations");

            rs = pst.executeQuery();
            rs.next();
            count = rs.getInt(1);
            return count;

        } catch (SQLException ex) {
            Logger.getLogger(CourseQueries.class.getName()).log(Level.SEVERE, null, ex);
        }

        return count;
    }

    public int countCourseInfo() {
        int count = 0;
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement("Select count(*) FROM courseDuration");

            rs = pst.executeQuery();
            rs.next();
            count = rs.getInt(1);
            return count;

        } catch (SQLException ex) {
            Logger.getLogger(CourseQueries.class.getName()).log(Level.SEVERE, null, ex);
        }

        return count;
    }

    ResultSet getDurationsForThisCourse(String courseID) {

        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement("SELECT duration ,name , days " +
                    "FROM courseDuration INNER JOIN durations ON courseDuration.duration=durations.id " +
                    "WHERE courseDuration.courseID = ?");
            pst.setString(1, courseID);
            rs = pst.executeQuery();

            return rs;
        } catch (SQLException ex) {
            Logger.getLogger(CourseQueries.class.getName()).log(Level.SEVERE, null, ex);
        }

        return rs;

    }

    public boolean editCourseInfo(CourseDuration cd) {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement("UPDATE courseDuration SET fees=? " +
                    "WHERE courseID=? and duration=?");
            pst.setString(1, cd.getAmount());
            pst.setString(2, cd.getCourseID());
            pst.setString(3, cd.getDurationID());

            return pst.execute();
        } catch (SQLException ex) {
            Logger.getLogger(CourseQueries.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
}
