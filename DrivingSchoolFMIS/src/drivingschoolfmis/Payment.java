package drivingschoolfmis;

public class Payment {
    private String id;
    private String expense;
    private String date;
    private String dateOfPayment;
    private String amount;
    private String mode;
    private String ref;
    private String mirage;
    private String user;

    public Payment(String id, String expense, String date,
                   String dateOfPayment, String amount,
                   String mode, String ref, String mirage, String user) {
        this.id = id;
        this.expense = expense;
        this.date = date;
        this.dateOfPayment = dateOfPayment;
        this.amount = amount;
        this.mode = mode;
        this.ref = ref;
        this.mirage = mirage;
        this.user = user;
    }

    public Payment(String expense, String date,
                   String dateOfPayment, String amount,
                   String mode, String ref, String mirage, String user) {
        this.expense = expense;
        this.date = date;
        this.dateOfPayment = dateOfPayment;
        this.amount = amount;
        this.mode = mode;
        this.ref = ref;
        this.mirage = mirage;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExpense() {
        return expense;
    }

    public void setExpense(String expense) {
        this.expense = expense;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getMirage() {
        return mirage;
    }

    public void setMirage(String mirage) {
        this.mirage = mirage;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
