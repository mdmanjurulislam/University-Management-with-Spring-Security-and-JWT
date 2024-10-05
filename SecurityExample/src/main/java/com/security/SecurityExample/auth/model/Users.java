package com.security.SecurityExample.auth.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "Users",schema = "Man")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_password")
    private String userPassword;

    @Column(name = "created_date")
    private LocalDateTime createdDateTime;

    @Column(name = "is_active")
    private Boolean active;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDateTime;


    @Enumerated(EnumType.STRING)
    private Role role;

}
