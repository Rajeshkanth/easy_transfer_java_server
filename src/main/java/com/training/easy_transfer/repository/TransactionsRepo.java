package com.training.easy_transfer.repository;

import com.training.easy_transfer.model.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionsRepo extends JpaRepository<Transactions, Long> {
    List<Transactions> findByUserMobileNumber(String mobileNumber);
    List<Transactions> findByMobileNumber(String mobileNumber);
    long countByUserMobileNumber(String mobileNumber);
    Optional<Transactions> findById(Long id);
    Transactions findByUid(String uid);
}
