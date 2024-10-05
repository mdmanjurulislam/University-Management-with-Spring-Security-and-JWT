package com.security.SecurityExample.subject.model;

import com.security.SecurityExample.auth.model.Users;
import com.security.SecurityExample.department.model.Department;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Entity
@Getter
@Setter
@Table(name = "Subject",schema = "Man")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int subjectId;

    @Column(name = "subject_name")
    private String subjectName;

    @Column(name = "subject_code")
    private String subjectCode;

    @Column(name = "subject_type")
    private String subjectType;

    @Column(name = "credit_amount")
    private int creditAmount;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department subjectDepartment;

    @ManyToOne
    @JoinColumn(name = "created_user_id")
    private Users createdUser;

    @ManyToOne
    @JoinColumn(name = "modified_user_id")
    private Users modifiedUser;
}
