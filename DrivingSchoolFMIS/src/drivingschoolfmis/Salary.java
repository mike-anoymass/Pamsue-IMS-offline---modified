package drivingschoolfmis;

public class Salary {
    private String id;
    private String employee;
    private String date;
    private String dateOfPayment;
    private String description;
    private String amount;

    public Salary(String employee, String date, String dateOfPayment, String description, String amount) {
        this.employee = employee;
        this.date = date;
        this.dateOfPayment = dateOfPayment;
        this.description = description;
        this.amount = amount;
    }

    public Salary(String id, String employee, String date, String dateOfPayment, String description, String amount) {
        this.id = id;
        this.employee = employee;
        this.date = date;
        this.dateOfPayment = dateOfPayment;
        this.description = description;
        this.amount = amount;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDateOfPayment() {
        return dateOfPayment;
    }

    public void setDateOfPayment(String dateOfPayment) {
        this.dateOfPayment = dateOfPayment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
