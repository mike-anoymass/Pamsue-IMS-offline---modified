package drivingschoolfmis;

public class NextOfKin {
    private String id;
    private String employee;
    private String name ;
    private String phone;
    private String physicalAddress;

    public NextOfKin(String employee, String name, String phone, String physicalAddress) {
        this.employee = employee;
        this.phone = phone;
        this.physicalAddress = physicalAddress;
        this.name = name;
    }

    public NextOfKin(String id, String employee, String name, String phone, String physicalAddress) {
        this.id = id;
        this.employee = employee;
        this.phone = phone;
        this.physicalAddress = physicalAddress;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhysicalAddress() {
        return physicalAddress;
    }

    public void setPhysicalAddress(String physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
