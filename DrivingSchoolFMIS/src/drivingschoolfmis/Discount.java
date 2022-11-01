package drivingschoolfmis;

public class Discount {
    private String student;
    private String amount;

    public Discount(String student, String amount) {
        this.student = student;
        this.amount = amount;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
