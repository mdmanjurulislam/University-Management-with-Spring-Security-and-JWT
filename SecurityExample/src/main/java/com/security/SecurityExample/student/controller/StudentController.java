package com.security.SecurityExample.student.controller;

import com.security.SecurityExample.auth.common.CommonResponse;
import com.security.SecurityExample.student.model.Student;
import com.security.SecurityExample.student.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/create-student")
    public CommonResponse createStudent(@RequestHeader String token, @RequestBody Student student){
        return studentService.createStudent(token,student);
    }

    @PostMapping("/create-student-with-doc")
    public ResponseEntity<CommonResponse> createStudentWithUploadDoc(@RequestHeader String token, @RequestParam("file")MultipartFile file, Student student){
        CommonResponse commonResponse = studentService.createStudentWithUploadDoc(token,student,file);
        return ResponseEntity.ok(commonResponse);
    }

    @GetMapping("/get-all-students")
    public CommonResponse getAllStudent(){
        List<Student> allStudent = studentService.getAllStudet();
        return CommonResponse.builder().message("All Student").response(allStudent).code(200).build();
    }

    @PutMapping("/update-student")
    public CommonResponse updateStudent(@RequestParam int studentId,@RequestHeader String token,@RequestBody Student student){
        return studentService.updateStudent(studentId,token,student);
    }

    @DeleteMapping("/delete-student-by-id")
    public CommonResponse deleteStudent(@RequestHeader String token,@RequestParam int studentID){
        return studentService.deleteStudent(token,studentID);
    }

//    @PostMapping("/upload-student-and-file")
//    public ResponseEntity<CommonResponse> uploadListOfFile(@RequestHeader String token,@ModelAttribute Student student,@RequestParam List<MultipartFile> fileList){
//        CommonResponse commonResponse= studentService.uploadListOfFile(token,student,fileList);
//        return ResponseEntity.ok(commonResponse);
//    }

}
