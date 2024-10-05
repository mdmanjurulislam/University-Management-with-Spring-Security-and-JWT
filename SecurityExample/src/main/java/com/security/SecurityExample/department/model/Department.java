package com.security.SecurityExample.department.model;

import com.security.SecurityExample.auth.model.Users;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Department",schema = "Man")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int departmentId;

    @Column(name = "department_name")
    private String departmentName;

    @Column(name = "department_code")
    private String departmentCode;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

//    @ManyToOne
//    @JoinColumn(name = "created_user_id")
//    private Users createdUser;
//
//    @ManyToOne
//    @JoinColumn(name = "modified_user_id")
//    private Users modifiedUser;
}
