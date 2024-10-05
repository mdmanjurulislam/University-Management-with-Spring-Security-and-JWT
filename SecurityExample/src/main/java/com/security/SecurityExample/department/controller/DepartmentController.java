package com.security.SecurityExample.department.controller;

import com.security.SecurityExample.auth.common.CommonResponse;
import com.security.SecurityExample.department.model.Department;
import com.security.SecurityExample.department.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;


    @PostMapping("/create-department")
    public CommonResponse createDepartment(@RequestHeader String token, @RequestBody Department department){
        return departmentService.createDepartment(token,department);
    }

    @GetMapping("/get-all-departments")
    public CommonResponse getAllDepartment(){
        List<Department> allDepartment=departmentService.getAllDepartment();
        return CommonResponse.builder().message("All Department").response(allDepartment).code(200).build();
    }

    @PutMapping("/update-department")
    public CommonResponse updateDepartment(@RequestParam int departmentId,@RequestHeader String token,@RequestBody Department department){
        return departmentService.updateDepartment(departmentId,token,department);
    }

    @DeleteMapping("/delete-department-by-id")
    public CommonResponse deleteDepartment(@RequestParam int departmentId,@RequestHeader String token){
        return departmentService.deleteDepartment(departmentId,token);
    }
}
