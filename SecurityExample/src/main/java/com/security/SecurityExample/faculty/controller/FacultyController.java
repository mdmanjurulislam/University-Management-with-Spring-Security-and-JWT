package com.security.SecurityExample.faculty.controller;

import com.security.SecurityExample.auth.common.CommonResponse;
import com.security.SecurityExample.department.model.Department;
import com.security.SecurityExample.faculty.model.Faculty;
import com.security.SecurityExample.faculty.service.FacultyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

    @Autowired
    private FacultyService facultyService;

    @PostMapping("/create-faculty")
    public CommonResponse createFaculty(@RequestHeader String token, @RequestBody Faculty faculty){
        return facultyService.createFaculty(token,faculty);
    }

    @GetMapping("/get-all-faculty")
    public CommonResponse getAllFaculty(){
        List<Faculty> allFaculty = facultyService.getAllFaculty();
        return CommonResponse.builder().message("All Faculty").response(allFaculty).code(200).build();
    }
}
