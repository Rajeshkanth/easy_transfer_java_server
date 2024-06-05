package com.training.easy_transfer.controller;

import com.training.easy_transfer.model.PaymentAlertRequest;
import com.training.easy_transfer.model.Transactions;
import com.training.easy_transfer.service.TransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transaction/")
public class TransactionsController {

    private final TransactionsService transactionsService;


    @Autowired
    public  TransactionsController(TransactionsService transactionsService){
        this.transactionsService = transactionsService;
    }

    @PostMapping("transactionDetails")
    public ResponseEntity<TransactionsService.TransactionsResponse> getAllTransactions(@RequestBody Transactions transactions) {
        return transactionsService.getAllTransactions(transactions);
    }

    @PostMapping("fromPaymentAlert")
    public ResponseEntity<String> processPaymentAlert(@RequestBody PaymentAlertRequest request) {
        transactionsService.processPaymentAlert( request.getNewTransaction(), request.getMobileNumber());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("transactionStatus/{uid}")
    public ResponseEntity<String > sendTransactionStatus(@PathVariable String uid){
        String status = transactionsService.checkTransactionStatusByUID(uid);
        if (status.equals("confirmed")) {
            return ResponseEntity.ok("Transaction Confirmed");
        } else if (status.equals("canceled")) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
