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

public class EmployeeQueries {
    ResultSet rs ;
    PreparedStatement pst;

    ResultSet getEmployees(){
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement(
                "SELECT employees.id AS staffID, fullname, employees.phone as staffPhone, " +
                        "employees.postalAddress AS staffPostalAddress, employees.physicalAddress AS staffPAddr" +
                        ", gender, department, employeeStatus, salaryDesired, workingHours, dateOfEntry," +
                        " date, dob , location, nextOfKins.phone AS gPhone "
                        + "FROM employees "
                        + "INNER JOIN nextOfKins ON employees.id=nextOfKins.employee" +
                        " ORDER BY employees.date DESC "
            );
            rs = pst.executeQuery();
            return rs;

        } catch (SQLException ex) {
            Logger.getLogger(EmployeeQueries.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }

    public boolean addStaff(Employee emp) {

        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement(
                    "INSERT INTO employees (fullname, phone ,postalAddress," +
                            "physicalAddress, department, employeeStatus, salaryDesired " +
                            ",workingHours, dateOfEntry, date, location, gender, dob) "
                            + "VALUES (?, ?, ?, ?, ? ,? , ? ,?, ?, datetime('now'), ?, ? ,?)");
            setStudent(emp);

            return pst.execute();
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeQueries.class.getName()).log(Level.SEVERE, null, ex);
            makeAlert("error", "Ops! This employee exists!\nPlease pick another name");
        }
        return true;
    }

    public ResultSet getStaff(String fullName) {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement("SELECT * FROM employees where fullname=?");
            pst.setString(1, fullName);
            rs = pst.executeQuery();
            return rs;

        } catch (SQLException ex) {
            Logger.getLogger(EmployeeQueries.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }

    public void addNextOfKin(NextOfKin nextOfKin) {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement(
                    "INSERT INTO nextOfKins (employee, name, phone, physicalAddress) "
                            + "VALUES (?, ?, ?, ?)");
            pst.setString(1, nextOfKin.getEmployee());
            pst.setString(2, nextOfKin.getName());
            pst.setString(3, nextOfKin.getPhone());
            pst.setString(4, nextOfKin.getPhysicalAddress());

            pst.execute();
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeQueries.class.getName()).log(Level.SEVERE, null, ex);
            makeAlert("error", "error adding next of kin=> "+ ex);
        }
    }

    public int deleteEmployees(ObservableList<Employee> selectedStaff) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Please Confirm Deletion");
        //alert.setGraphic(GlyphsDude.createIcon(FontAwesomeIcon., "20px"));
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete ? \n" + selectedStaff.size() +
                " Employees(s) will be deleted ");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {


            for (int i = 0; i < selectedStaff.size(); i++) {

                try {
                    pst = DrivingSchoolFMIS.conn.prepareStatement("DELETE FROM employees WHERE id=?");
                    pst.setString(1, selectedStaff.get(i).getId());

                    pst.executeUpdate();

                } catch (SQLException ex) {
                    Logger.getLogger(StudentQueries.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            return 101;
        }
        return 404;
    }

    public ResultSet getDepartment(String department) {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement(
                    "SELECT employees.id AS staffID, fullname, employees.phone as staffPhone, " +
                            "employees.postalAddress AS staffPostalAddress, employees.physicalAddress AS staffPAddr" +
                            ", gender, department, employeeStatus, salaryDesired, workingHours, dateOfEntry," +
                            " date, dob , location, nextOfKins.phone AS gPhone "
                            + "FROM employees "
                            + "INNER JOIN nextOfKins ON employees.id=nextOfKins.employee " +
                            "WHERE department = ?" +
                            " ORDER BY employees.date DESC "
            );

            pst.setString(1, department);
            rs = pst.executeQuery();
            return rs;

        } catch (SQLException ex) {
            Logger.getLogger(EmployeeQueries.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }

    public ResultSet getNextOfKin(String id) {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement(
                    "SELECT *  FROM nextOfKins " +
                            "WHERE employee = ?"
            );

            pst.setString(1, id);
            rs = pst.executeQuery();
            return rs;

        } catch (SQLException ex) {
            Logger.getLogger(EmployeeQueries.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }

    public boolean editStaff(Employee emp, String id) {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement("UPDATE employees " +
                    "SET fullname = ?, phone=? ,postalAddress=?," +
                    "physicalAddress=?, department=?, employeeStatus=?, salaryDesired=?, " +
                    "workingHours=?, dateOfEntry=?, location=?, gender=?, dob=? " +
                    " WHERE id=?"
            );

            setStudent(emp);
            pst.setString(13, id);

            return pst.execute();
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeQueries.class.getName()).log(Level.SEVERE, null, ex);
            makeAlert("error", "Failed to edit Staff -> " + ex);
        }
        return true;
    }

    private void setStudent(Employee emp) throws SQLException {
        pst.setString(1, emp.getFullName());
        pst.setString(2, emp.getPhone());
        pst.setString(3, emp.getPostalAddress());
        pst.setString(4, emp.getPhysicalAddress());
        pst.setString(5, emp.getDepartment());
        pst.setString(6, emp.getEmployeeStatus());
        pst.setString(7, emp.getSalaryDesired());
        pst.setString(8, emp.getWorkingHours());
        pst.setString(9, emp.getDateRegistered());
        pst.setString(10, emp.getLocation());
        pst.setString(11, emp.getGender());
        pst.setString(12, emp.getDob());
    }

    public void editNextOfKin(NextOfKin nextOfKin) {
        try {
            pst = DrivingSchoolFMIS.conn.prepareStatement("UPDATE nextOfkins " +
                    "SET name = ?, phone=? ,physicalAddress=? " +
                    " WHERE employee=?"
            );

            pst.setString(1, nextOfKin.getName());
            pst.setString(2, nextOfKin.getPhone());
            pst.setString(3, nextOfKin.getPhysicalAddress());
            pst.setString(4, nextOfKin.getEmployee());

            pst.execute();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
