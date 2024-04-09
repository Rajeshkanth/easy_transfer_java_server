package com.training.easy_transfer.controller;

import com.training.easy_transfer.model.User;
import com.training.easy_transfer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/")
public class UserController {

    private final UserService userService;
    @Autowired
    public UserController(UserService userService){
        this.userService=userService;
    }

    @PostMapping("signUp")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        String response = userService.registerUser(user.getMobileNumber(), user.getPassword());
        return switch (response){
            case "user added"->ResponseEntity.ok("registration successful");
            case "user already"->ResponseEntity.status(HttpStatus.CREATED).build();
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error");
        };
    }

    @PostMapping("login")
    public ResponseEntity<String> loginUser(@RequestBody User user) {
        String response = userService.loginUser(user.getMobileNumber(), user.getPassword());

        return switch (response) {
            case "ok" -> ResponseEntity.ok("login successful");
            case "wrong password" -> ResponseEntity.status(HttpStatus.ACCEPTED).build();
            case "new user" -> ResponseEntity.status(HttpStatus.CREATED).build();
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error");
        };
    }

}
