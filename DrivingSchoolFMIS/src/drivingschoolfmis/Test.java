package drivingschoolfmis;

import javafx.scene.control.CheckBox;

public class Test {
    private String id;
    private String student;
    private String testName;
    private String date;
    private String dateOfTest;
    private String passOrFail;
    private CheckBox checkBox = new CheckBox();

    public Test(String student, String testName, String date, String dateOfTest, String passOrFail) {
        this.student = student;
        this.testName = testName;
        this.date = date;
        this.dateOfTest = dateOfTest;
        this.passOrFail = passOrFail;
    }

    public Test(String id, String student, String testName, String date, String dateOfTest, String passOrFail) {
        this.id = id;
        this.student = student;
        this.testName = testName;
        this.date = date;
        this.dateOfTest = dateOfTest;
        this.passOrFail = passOrFail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDateOfTest() {
        return dateOfTest;
    }

    public void setDateOfTest(String dateOfTest) {
        this.dateOfTest = dateOfTest;
    }

    public String getPassOrFail() {
        return passOrFail;
    }

    public void setPassOrFail(String passOrFail) {
        this.passOrFail = passOrFail;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }
}
