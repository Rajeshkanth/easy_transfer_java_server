package com.training.easy_transfer.repository;

import com.training.easy_transfer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByMobileNumber(String mobileNumber);
}
