package com.security.SecurityExample.student.model;

import com.security.SecurityExample.auth.model.Users;
import com.security.SecurityExample.course_assign.model.SelectCourse;
import com.security.SecurityExample.department.model.Department;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Student",schema = "Man")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private int studentId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "student_code")
    private String studentCode;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department studentDepartment;

    @Column(name = "fathers_name")
    private String fathersName;

    @Column(name = "mothers_name")
    private String mothersName;

    @Column(name = "parents_contact")
    private String parentsContact;

    @Column(name = "relation_with_contact")
    private String relationWithContact;

    @Column(name = "blood_group")
    private String bloodGroup;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "present_address")
    private String presentAddress;

    @Column(name = "permanent_address")
    private String permanentAddress;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "is_active")
    private Boolean isActive;


    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;


    @ManyToOne
    @JoinColumn(name = "created_user_id")
    private Users createdUser;

    @ManyToOne
    @JoinColumn(name = "modified_user_id")
    private Users modifiedUser;

//    for file upload
    @Column(name = "actual_file_path")
    private String actualFilePath;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "file_directory_path")
    private String fileDirectoryPath;

}
