package com.training.easy_transfer.service;

import com.training.easy_transfer.model.Transactions;
import com.training.easy_transfer.model.User;
import com.training.easy_transfer.repository.TransactionsRepo;
import com.training.easy_transfer.repository.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionsService {

    private final TransactionsRepo transactionsRepository;

    private final UserRepository userRepository;

    @Autowired
    public TransactionsService(UserRepository userRepository, TransactionsRepo transactionsRepository) {
        this.transactionsRepository = transactionsRepository;
        this.userRepository = userRepository;
    }

    public List<Transactions> getTransactionsByMobileNumber(String mobileNumber) {
        return transactionsRepository.findByMobileNumber(mobileNumber);
    }

    public Optional<Transactions> findById(Long id) {
        return transactionsRepository.findById(id);
    }

    public String checkTransactionStatusByUID(String uid) {
        Transactions transaction = transactionsRepository.findByUid(uid);
        if (transaction != null) {
            return transaction.getStatus();
        }
        return "NOT_FOUND";
    }

    public List<Transactions> checkForPendingTransactions(String mobileNumber) {
        List<Transactions> transactions = transactionsRepository.findByMobileNumber(mobileNumber);

        List<Transactions> pendingTransactions = new ArrayList<>();
        for (Transactions transaction : transactions) {
            if (transaction.getStatus().equals("pending")) {
                pendingTransactions.add(transaction);
            }
        }
        return pendingTransactions;
    }

    public void saveTransaction(Transactions transaction) {
        transactionsRepository.save(transaction);
    }

    @Setter
    @Getter
    public static class TransactionsResponse {
        private List<Transactions> transactions;
        private long totalTransactions;

        public TransactionsResponse(List<Transactions> transactions, long totalTransactions) {
            this.transactions = transactions;
            this.totalTransactions = totalTransactions;
        }
    }

    public ResponseEntity<TransactionsResponse> getAllTransactions(Transactions transactions) {
        List<Transactions> userTransactions = transactionsRepository.findByUserMobileNumber(transactions.getMobileNumber());
        long totalTransactions = transactionsRepository.countByUserMobileNumber(transactions.getMobileNumber());
        TransactionsResponse transactionsResponse = new TransactionsResponse(userTransactions, totalTransactions);
        return ResponseEntity.ok(transactionsResponse);
    }

    public void processPaymentAlert(Transactions newReceiver, String mobileNumber) {
        User user = userRepository.findByMobileNumber(mobileNumber);
        if (user == null) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            return; // user not found
        }
        newReceiver.setUser(user);
        Transactions savedTransaction = transactionsRepository.save(newReceiver);
        ResponseEntity.ok(savedTransaction);
    }

}

