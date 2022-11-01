package drivingschoolfmis;

import javafx.scene.control.CheckBox;

public class Receipts {
    String id;
    String date;
    String amount;
    String mop;
    String bpo;
    String user;
    String ref;
    String dateOfReceipt;
    private CheckBox checkBox = new CheckBox();
    private String fullName;
    private String student;

    public Receipts(String id, String date,String dateOfReceipt, String amount, String mop,
                    String bpo, String user, String ref, String fullName) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.mop = mop;
        this.bpo = bpo;
        this.user = user;
        this.ref = ref;
        this.fullName = fullName;
        this.dateOfReceipt = dateOfReceipt;
    }

    public Receipts(String student, String date, String dateOfReceipt, String amount,
                   String mop, String bpo, String user, String ref) {
        this.student = student;
        this.date = date;
        this.dateOfReceipt = dateOfReceipt;
        this.amount = amount;
        this.mop = mop;
        this.bpo = bpo;
        this.user = user;
        this.ref = ref;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setMop(String mop) {
        this.mop = mop;
    }

    public void setBpo(String bpo) {
        this.bpo = bpo;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getAmount() {
        return amount;
    }

    public String getMop() {
        return mop;
    }

    public String getBpo() {
        return bpo;
    }

    public String getUser() {
        return user;
    }

    public String getRef() {
        return ref;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }

    public String getFullname() {
        return fullName;
    }

    public void setFullname(String fullName) {
        this.fullName = fullName;
    }

    public String getDateOfReceipt() {
        return dateOfReceipt;
    }

    public void setDateOfReceipt(String dateOfReceipt) {
        this.dateOfReceipt = dateOfReceipt;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }
}
