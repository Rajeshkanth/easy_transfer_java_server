package com.training.easy_transfer.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "saved_accounts")
public class SavedAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "beneficiary_name", nullable = false)
    private String beneficiaryName;

    @Column(name = "acc_num", nullable = false, unique = true)
    private String accountNumber;

    @Column(name = "ifsc", nullable = false)
    private String ifsc;

}
