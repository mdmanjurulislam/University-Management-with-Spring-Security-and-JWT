package com.security.SecurityExample.faculty.model;

import com.security.SecurityExample.department.model.Department;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Faculty",schema = "Man")
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int facultyId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "educational_qualification")
    private String educationalQualification;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department major;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "present_address")
    private String presentAddress;

    @Column(name = "permanent_address")
    private String permanentAddress;

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
