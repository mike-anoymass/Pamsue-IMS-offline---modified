package drivingschoolfmis;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryFees {
    public Double getCategoryFees(String courseCode, String category) {
        try {
            ResultSet fees, rsForCourseID, rsForCategory;
            rsForCourseID = new CourseQueries().getCourse(courseCode);
            rsForCategory = new CourseQueries().getCourseType(category);
            fees = new CourseQueries().getCourseInfo(rsForCourseID.getString("id"), rsForCategory.getString("id"));

            return Double.parseDouble(fees.getString("fees"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return 0.0;
    }
}
