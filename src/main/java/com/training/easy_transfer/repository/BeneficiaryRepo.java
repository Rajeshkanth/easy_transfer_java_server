package com.training.easy_transfer.repository;

import com.training.easy_transfer.model.SavedAccount;
import com.training.easy_transfer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BeneficiaryRepo extends JpaRepository<SavedAccount,Long> {
    SavedAccount findByAccountNumber(String accountNumber);
    List<SavedAccount> findByUser(User user);

}
