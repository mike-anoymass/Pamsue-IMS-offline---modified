/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drivingschoolfmis;

import javafx.scene.control.CheckBox;

/**
 *
 * @author ANOYMASS
 */
public class Student {
    private String id;
    private String name;
    private String address;
    private String phone;
    private String gender;
    private String course;
    private String courseType;
    private String date;
    private String dateRegistered;
    private String guardianPhone;
    private String location;
    private String trn;
    private boolean anyDiscount;
    private boolean anyGovtFee;
    private boolean graduated;
    private CheckBox checkBox = new CheckBox();


    public Student() {
    }

    public Student(String id, String name,String phone, String address, String gender,
                   String course, String courseType, String date, String dateRegistered,
                   String guardianPhone, String location, String trn,
                   boolean anyDiscount, boolean anyGovtFee, boolean graduated) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.gender = gender;
        this.course = course;
        this.courseType = courseType;
        this.date = date;
        this.dateRegistered = dateRegistered;
        this.guardianPhone = guardianPhone;
        this.location = location;
        this.trn = trn;
        this.anyDiscount = anyDiscount;
        this.anyGovtFee = anyGovtFee;
        this.graduated = graduated;
    }

    public Student(String name, String address, String phone, String gender,
                   String course, String courseType, String date, String dateRegistered,
                   String guardianPhone, String location, String trn,
                   boolean anyDiscount, boolean anyGovtFee, boolean graduated) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.gender = gender;
        this.course = course;
        this.courseType = courseType;
        this.date = date;
        this.dateRegistered = dateRegistered;
        this.guardianPhone = guardianPhone;
        this.location = location;
        this.trn = trn;
        this.anyDiscount = anyDiscount;
        this.anyGovtFee = anyGovtFee;
        this.graduated = graduated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(String dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public String getGuardianPhone() {
        return guardianPhone;
    }

    public void setGuardianPhone(String guardianPhone) {
        this.guardianPhone = guardianPhone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTrn() {
        return trn;
    }

    public void setTrn(String trn) {
        this.trn = trn;
    }

    public boolean isAnyDiscount() {
        return anyDiscount;
    }

    public void setAnyDiscount(boolean anyDiscount) {
        this.anyDiscount = anyDiscount;
    }

    public boolean isAnyGovtFee() {
        return anyGovtFee;
    }

    public void setAnyGovtFee(boolean anyGovtFee) {
        this.anyGovtFee = anyGovtFee;
    }

    public boolean isGraduated() {
        return graduated;
    }

    public void setGraduated(boolean graduated) {
        this.graduated = graduated;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }
}
