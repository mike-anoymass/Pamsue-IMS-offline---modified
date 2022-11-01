package drivingschoolfmis;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FeesCalculationsClass {
    double totalPaid , balance;
    String courseID, durationID;
    ResultSet rsForCourse = null;
    Double totalFees;
    ResultSet rs;
    CourseQueries courseQuery = new CourseQueries();
    FeesResults results = new FeesResults();

    public FeesResults FeesCalculations(Student student){
        totalPaid = balance = 0.0;

        try {
            rsForCourse = courseQuery.getCourse(student.getCourse());
            courseID = rsForCourse.getString("id");

            rs = courseQuery.getCourseType(student.getCourseType());
            durationID = rs.getString("id");

            rs = courseQuery.getCourseInfo(courseID, durationID);
            results.setCourseFee(rs.getString("fees"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        if(student.isAnyDiscount()){
            try {
                rs = new DiscountQueries().getDiscount(student.getId());
                results.setDiscountFee(rs.getString("amount"));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }else{
            results.setDiscountFee("0.0");
        }

        if(student.isAnyGovtFee()){
            try {
                results.setGovtFee(rsForCourse.getString("governmentFee"));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }else{
            results.setGovtFee("0.0");
        }

        totalFees = calculateTotalFees(results.getCourseFee(), results.getGovtFee(), results.getDiscountFee());
        results.setTotalFee("" + totalFees);


        try{
            rs = ReceiptQueries.getReceiptsFor(student.getId());

            while(rs.next()){
                totalPaid += Double.parseDouble(rs.getString("amount"));
            }

        }catch(SQLException ex){
            System.err.println(ex);
        }

        results.setTotalPaid("" + totalPaid);

        balance = totalFees - totalPaid;

        results.setBalance("" + balance);

        return results;
    }

    private Double calculateTotalFees(String courseFee, String govtFee, String discountFee) {
        return Double.parseDouble(courseFee) + Double.parseDouble(govtFee) - Double.parseDouble(discountFee);
    }
}
