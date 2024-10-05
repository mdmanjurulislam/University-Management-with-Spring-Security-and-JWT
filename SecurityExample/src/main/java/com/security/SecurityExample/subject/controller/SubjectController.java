package com.security.SecurityExample.subject.controller;

import com.security.SecurityExample.auth.common.CommonResponse;
import com.security.SecurityExample.subject.model.Subject;
import com.security.SecurityExample.subject.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subject")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @PostMapping("/create-subject")
    public CommonResponse createSubject(@RequestHeader String token, @RequestBody Subject subject){
        return subjectService.createSubject(token,subject);
    }

    @GetMapping("/get-all-subjects")
    public CommonResponse getAllSubject(){
        List<Subject> allSubject = subjectService.getAllSubject();
        return CommonResponse.builder().message("All Subject").response(allSubject).code(200).build();
    }

    @PutMapping("/update-subject")
    public CommonResponse updateSubject(@RequestParam int subjectId,@RequestHeader String token,@RequestBody Subject subject){
        return subjectService.updateSubject(subjectId,token,subject);
    }

    @DeleteMapping("/delete-subject")
    public CommonResponse deleteSubject(@RequestParam int subjectId,@RequestHeader String token){
        return subjectService.deleteSubject(subjectId,token);
    }
}
