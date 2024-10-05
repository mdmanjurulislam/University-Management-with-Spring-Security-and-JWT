//package com.security.SecurityExample.student.model;
//
//import jakarta.persistence.*;
//import lombok.Data;
//
//@Data
//@Entity
//@Table(name = "T_File",schema = "Man")
//public class FileModel {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int fileId;
//
//    @Column(name = "actual_file_path")
//    private String actualFilePath;
//
//    @Column(name = "file_name")
//    private String fileName;
//
//    @Column(name = "file_type")
//    private String fileType;
//
//    @Column(name = "file_directory_path")
//    private String fileDirectoryPath;
//
//    @ManyToOne
//    @JoinColumn(name = "student_id")
//    private Student student;
//}
//
