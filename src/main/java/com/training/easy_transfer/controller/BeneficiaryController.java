package com.training.easy_transfer.controller;

import com.training.easy_transfer.model.SavedAccount;
import com.training.easy_transfer.model.User;
import com.training.easy_transfer.service.BeneficiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/user/")
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;
    @Autowired
    public BeneficiaryController(BeneficiaryService beneficiaryService){
        this.beneficiaryService=beneficiaryService;
    }

    @PostMapping("addNewBeneficiary")
    public ResponseEntity<SavedAccount> addBeneficiary(@RequestBody SavedAccount savedAccount) {
        return beneficiaryService.addAndRetrieveBeneficiary(savedAccount);
    }

    @PostMapping("getBeneficiaryDetails")
    public ResponseEntity<?> getBeneficiaryDetails(@RequestBody SavedAccount savedAccount) {
        return beneficiaryService.getAllSavedAccounts(savedAccount);
    }

    @PostMapping("checkUserName")
    public ResponseEntity<User> getUserName(@RequestBody User user){
        return beneficiaryService.getUserName(user.getMobileNumber());
    }

    @PostMapping("updateProfile")
    public ResponseEntity<User> updateProfileDetails(@RequestBody User user){
        return beneficiaryService.updateUserDetailsByMobileNumber(user.getMobileNumber(),user);
    }

    @PostMapping("removeBeneficiary")
    public  ResponseEntity<String> removeSavedBeneficiaryAccount(@RequestBody SavedAccount savedAccount){
        return beneficiaryService.deleteSavedBeneficiaryAccount(savedAccount.getMobileNumber(), savedAccount.getAccountNumber());
    }

}

