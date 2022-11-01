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
public class LoanSettlement {
    private int loan_id;
    private int settlement_id;
    private double amount;
    private String date;

    public LoanSettlement(int loan_id, double amount) {
        this.loan_id = loan_id;
        this.amount = amount;
    }

    public LoanSettlement(int loan_id, int settlement_id, double amount, String date) {
        this.loan_id = loan_id;
        this.settlement_id = settlement_id;
        this.amount = amount;
        this.date = date;
    }

    public int getLoan_id() {
        return loan_id;
    }

    public void setLoan_id(int loan_id) {
        this.loan_id = loan_id;
    }

    public int getSettlement_id() {
        return settlement_id;
    }

    public void setSettlement_id(int settlement_id) {
        this.settlement_id = settlement_id;
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
    
    
}
