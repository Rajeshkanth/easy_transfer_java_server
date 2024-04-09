package com.training.easy_transfer.controller;

import com.training.easy_transfer.model.Action;
import com.training.easy_transfer.model.Transactions;
import com.training.easy_transfer.service.TransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;
import java.util.Optional;

@RestController
public class TemplateControllers {
    private final TransactionsService transactionsService;

    @Autowired
    public TemplateControllers(TransactionsService transactionsService){
        this.transactionsService=transactionsService;
    }

    @GetMapping("/")
    public ModelAndView loginPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @GetMapping("/home/{mobileNumber}")
    public ModelAndView myPage(@PathVariable String mobileNumber) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home");

        List<Transactions> transactions = transactionsService.getTransactionsByMobileNumber(mobileNumber);

        List<Transactions> pendingTransactions = transactions.stream()
                .filter(transaction -> "pending".equals(transaction.getStatus()))
                .toList();

        modelAndView.addObject("transactions", pendingTransactions);
        modelAndView.addObject("mobileNumber",mobileNumber);
        return modelAndView;
    }

    @PostMapping("/api/transaction/pending/{mobileNumber}")
    public ResponseEntity<String> newAlert(@PathVariable String mobileNumber) {
        List<Transactions> pendingTransactions = transactionsService.checkForPendingTransactions(mobileNumber);
        if (!pendingTransactions.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("Pending transactions found");
        } else {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("No pending transactions found");
        }
    }

    @GetMapping("/success/{mobileNumber}")
    public ModelAndView successPage(@PathVariable String mobileNumber){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("mobileNumber", mobileNumber);
        modelAndView.setViewName("success");
        return  modelAndView;
    }

    @GetMapping("/canceled/{mobileNumber}")
    public ModelAndView canceledPage(@PathVariable String mobileNumber){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("canceled");
        return modelAndView;
    }

    @PostMapping("/api/transaction/confirm")
    public ResponseEntity<String> confirmPayment(@RequestBody Action request ) {
        String action = request.getStatus();
        Long id =request.getId();

        Optional<Transactions> optionalTransaction = transactionsService.findById(id);

        if (optionalTransaction.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Transactions transaction = optionalTransaction.get();

        if(action.equalsIgnoreCase("confirm")){
            transaction.setStatus("confirmed");
            transactionsService.saveTransaction(transaction);
            return ResponseEntity.ok("Payment confirmed successfully");
        }else if(action.equalsIgnoreCase("cancel")){
            transaction.setStatus("canceled");
            transactionsService.saveTransaction(transaction);
            return  ResponseEntity.status(HttpStatus.CREATED).build();
        }

        return ResponseEntity.badRequest().body("Invalid request");
    }
}