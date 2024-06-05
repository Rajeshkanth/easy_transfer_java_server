package com.training.easy_transfer.service;

import com.training.easy_transfer.model.User;
import com.training.easy_transfer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String registerUser(String mobileNumber, String password) {
        User existingUser = userRepository.findByMobileNumber(mobileNumber);

        if (existingUser != null) {
            return "user already";
        } else {
            User newUser = new User();
            newUser.setMobileNumber(mobileNumber);
            newUser.setPassword(password);
            userRepository.save(newUser);
            return "user added";
        }
    }

    public String loginUser(String mobileNumber, String password) {
        User user = userRepository.findByMobileNumber(mobileNumber);
        if (user != null) {
            if (user.getPassword().equals(password)) {
                return "ok";
            } else {
                return "wrong password";
            }
        } else {
            return "new user";
        }
    }
}
