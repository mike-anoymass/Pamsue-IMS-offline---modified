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
public class CourseType {
    private String id;
    private String name;
    private String days;
    
    public CourseType(String name, String days){
        this.days = days;
        this.name = name;
    }

    public CourseType(String id, String name, String days) {
        this.id = id;
        this.name = name;
        this.days = days;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDays(String days){
        this.days = days;
    }
    
    public void setName(String name){
        this.name = name;
    }

    public String getDays(){
        return days;
    }
    
    public String getName(){
        return name;
    }
}
