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
public class Loan {
    private int loan_id;
    private int employee_id;
    private double amount;
    private String date;
    private double balance;
    private int payment_id;

    public Loan(int employee_id, double amount, int payment_id) {
        this.employee_id = employee_id;
        this.amount = amount;
        this.payment_id = payment_id;
    }

    public Loan(int loan_id, int employee_id, double amount, String date, double balance) {
        this.loan_id = loan_id;
        this.employee_id = employee_id;
        this.amount = amount;
        this.date = date;
        this.balance = balance;
    }

    public int getLoan_id() {
        return loan_id;
    }

    public void setLoan_id(int loan_id) {
        this.loan_id = loan_id;
    }

    public int getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(int payment_id) {
        this.payment_id = payment_id;
    }
}
