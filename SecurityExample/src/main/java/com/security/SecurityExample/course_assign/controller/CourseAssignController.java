package com.security.SecurityExample.course_assign.controller;

import com.security.SecurityExample.auth.common.CommonResponse;
import com.security.SecurityExample.course_assign.model.CourseAssign;
import com.security.SecurityExample.course_assign.model.SelectCourse;
import com.security.SecurityExample.course_assign.service.CourseAssignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseAssignController {


    @Autowired
    private CourseAssignService courseAssignService;


    @PostMapping("/assign-course")
    public CommonResponse createAssignedCourse(@RequestHeader String token, @RequestBody CourseAssign courseAssign){
        return courseAssignService.createAssignCourse(token,courseAssign);
    }

    @GetMapping("/get-all-assigned-course")
    public CommonResponse getAllAssignedCourse(){
        List<CourseAssign> allAssignedCourse = courseAssignService.getAllAssignedCourse();
        return CommonResponse.builder().message("All Assigned Course").response(allAssignedCourse).code(200).build();
    }

    @PutMapping("/update-assigned-course")
    public CommonResponse updateAssignedCourse(@RequestParam int courseAssignedId,@RequestHeader String token, @RequestBody CourseAssign courseAssign){
        return courseAssignService.updateAssignedCourse(courseAssignedId,token,courseAssign);
    }

    @DeleteMapping("/delete-assigned-course")
    public CommonResponse deleteAssignedCourse(@RequestParam int assignedCourseId,@RequestHeader String token){
        return courseAssignService.deleteAssignedCourse(assignedCourseId,token);
    }


    @PostMapping("/select-course")
    public CommonResponse saveSelectCourse(@RequestHeader String token,@RequestBody SelectCourse selectCourse){
        return courseAssignService.saveSelectCourse(token,selectCourse);
    }

    @GetMapping("/get-all-selected-course-by-user")
    public CommonResponse getAllCourseByUser(@RequestHeader String token){
        return courseAssignService.getAllCourseByUser(token);
    }


}
