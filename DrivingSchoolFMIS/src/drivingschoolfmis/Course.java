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
public class Course {
    private String id;
    private String code;
    private String name;
    private String governmentFee;

    public Course(String id, String code, String name, String governmentFee) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.governmentFee = governmentFee;
    }

    public Course(String code, String name, String governmentFee) {
        this.code = code;
        this.name = name;
        this.governmentFee = governmentFee;
    }

    public Course(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCode(String code){
        this.code = code;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getCode(){
        return code;
    }

    public String getName(){
        return name;
    }

    public String getGovernmentFee() {
        return governmentFee;
    }

    public void setGovernmentFee(String governmentFee) {
        this.governmentFee = governmentFee;
    }
}
