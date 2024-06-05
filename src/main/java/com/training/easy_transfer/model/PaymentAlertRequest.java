package com.training.easy_transfer.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentAlertRequest {

    private Transactions newTransaction;
    private String mobileNumber;

    @Override
    public String toString() {
        return "PaymentAlertRequest{" +
                ", newTransaction=" + newTransaction +
                ", mobileNumber='" + mobileNumber + '\'' +
                '}';
    }
}
