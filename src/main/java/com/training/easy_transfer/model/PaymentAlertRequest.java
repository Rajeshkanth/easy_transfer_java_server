package com.training.easy_transfer.model;


public class PaymentAlertRequest {

    private Transactions newTransaction;
    private String mobileNumber;

    public Transactions getNewTransaction() {
        return newTransaction;
    }

    public void setNewTransaction(Transactions newTransaction) {
        this.newTransaction = newTransaction;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    @Override
    public String toString() {
        return "PaymentAlertRequest{" +
                ", newTransaction=" + newTransaction +
                ", mobileNumber='" + mobileNumber + '\'' +
                '}';
    }
}
