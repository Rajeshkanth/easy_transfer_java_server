package com.training.easy_transfer.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "mobile_number", unique = true, nullable = false)
        private String mobileNumber;

        @Column(nullable = false)
        private String password;

        @Column(name = "user_name")
        private String userName;

        private Integer age;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate dob;

        @Column(name = "acc_num")
        private String accNum;

        private String card;

        private String cvv;

        @Column(name = "expire_date")
        private String expireDate;
}
