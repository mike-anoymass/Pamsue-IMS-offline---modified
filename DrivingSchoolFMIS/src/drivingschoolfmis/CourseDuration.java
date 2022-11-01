/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drivingschoolfmis;

/**
 *
 * @author ANOYMASS
 */

public class CourseDuration {
    private String courseID;
    private String durationID;
    private String code;
    private String duration;
    private String amount;
    private String date;

    public CourseDuration(String courseID, String durationID, String amount, String date) {
        this.courseID =courseID;
        this.durationID = durationID;
        this.amount = amount;
        this.date = date;
    }

    public CourseDuration(String courseID, String durationID, String code, String duration, String amount, String date) {
        this.courseID = courseID;
        this.durationID = durationID;
        this.code = code;
        this.duration = duration;
        this.amount = amount;
        this.date = date;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getDurationID() {
        return durationID;
    }

    public void setDurationID(String durationID) {
        this.durationID = durationID;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }
    

    public void setCode(String code) {
        this.code = code;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAmount() {
        return amount;
    }

    public String getCode() {
        return code;
    }

    public String getDuration() {
        return duration;
    }
   
}
