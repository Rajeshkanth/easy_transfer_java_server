package com.training.easy_transfer.service;

import com.training.easy_transfer.model.SavedAccount;
import com.training.easy_transfer.model.User;
import com.training.easy_transfer.repository.BeneficiaryRepo;
import com.training.easy_transfer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BeneficiaryService {

    private final BeneficiaryRepo beneficiaryRepo;
    private final UserRepository userRepository;

    @Autowired
    public BeneficiaryService(BeneficiaryRepo beneficiaryRepo, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.beneficiaryRepo = beneficiaryRepo;

    }

    public ResponseEntity<SavedAccount> addAndRetrieveBeneficiary(SavedAccount savedAccount) {

        User user = userRepository.findByMobileNumber(savedAccount.getMobileNumber());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        savedAccount.setUser(user);

        SavedAccount existingAccount = beneficiaryRepo.findByAccountNumber(savedAccount.getAccountNumber());

        if (existingAccount != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // existing user
        }

        SavedAccount saved = beneficiaryRepo.save(savedAccount);

        return ResponseEntity.ok(saved);
    }

    public ResponseEntity<List<SavedAccount>> getAllSavedAccounts(SavedAccount savedAccount) {

        User user = userRepository.findByMobileNumber(savedAccount.getMobileNumber());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // user not found
        }

        List<SavedAccount> savedAccounts = beneficiaryRepo.findByUser(user);
        return ResponseEntity.ok(savedAccounts);
    }

    public ResponseEntity<User> getUserName(String mobileNumber) {
        User user = userRepository.findByMobileNumber(mobileNumber);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<User> updateUserDetailsByMobileNumber(String mobileNumber, User userDetails) {
        User user = userRepository.findByMobileNumber(mobileNumber);
        if (user != null) {
            user.setUserName(userDetails.getUserName());
            user.setAge(userDetails.getAge());
            user.setAccNum(userDetails.getAccNum());
            user.setCard(userDetails.getCard());
            user.setCvv(userDetails.getCvv());
            user.setDob(userDetails.getDob());
            user.setExpireDate(userDetails.getExpireDate());

            userRepository.save(user);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build(); // user not found
        }
    }

    public ResponseEntity<String> deleteSavedBeneficiaryAccount(String mobileNumber, String accountNumber) {

        User user = userRepository.findByMobileNumber(mobileNumber);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // user not found
        }
        SavedAccount accountToDelete = beneficiaryRepo.findByAccountNumber(accountNumber);
        if (accountToDelete == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        beneficiaryRepo.delete(accountToDelete);
        return ResponseEntity.ok("account removed successfully");
    }

}


