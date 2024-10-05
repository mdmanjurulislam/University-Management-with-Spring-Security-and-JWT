package com.security.SecurityExample.department.service;

import com.security.SecurityExample.auth.common.CommonResponse;
import com.security.SecurityExample.auth.model.UserPrinciple;
import com.security.SecurityExample.auth.model.Users;
import com.security.SecurityExample.auth.repository.UserRepo;
import com.security.SecurityExample.auth.service.JWTService;
import com.security.SecurityExample.department.model.Department;
import com.security.SecurityExample.department.repository.DepartmentRepo;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private DepartmentRepo departmentRepo;

    public CommonResponse createDepartment(String token, Department department) {
        boolean expiredToken = jwtService.isTokenExpired(token);
        if (expiredToken) {
            return CommonResponse.builder().message("Token is expired").response(null).code(401).build();
        } else {
            Claims claims = jwtService.extractAllClaims(token);
            String sub = String.valueOf(claims.get("sub"));
            Optional<Users> user = Optional.ofNullable(userRepo.findByUserName(sub));
            UserDetails userDetails = new UserPrinciple(user.get());

            boolean validToken = jwtService.isValidateToken(token, userDetails);
            if (!validToken) {
                return CommonResponse.builder().message("Token is not valid").response(null).code(401).build();
            }else {
                department.setDepartmentName(department.getDepartmentName());
                department.setDepartmentCode(department.getDepartmentCode());
                department.setCreatedDate(LocalDateTime.now());
                department.setIsActive(Boolean.TRUE);
                departmentRepo.save(department);

                return CommonResponse.builder().message("Department Created Successfully").response(department).code(201).build();
            }
        }
    }

    public List<Department> getAllDepartment(){
        return departmentRepo.findAll();
    }


    public CommonResponse updateDepartment(int departmentId,String token,Department department){
        boolean expiredToken = jwtService.isTokenExpired(token);
        if (expiredToken) {
            return CommonResponse.builder().message("Token is expired").response(null).code(401).build();
        } else {
            Claims claims = jwtService.extractAllClaims(token);
            String sub = String.valueOf(claims.get("sub"));
            Optional<Users> user = Optional.ofNullable(userRepo.findByUserName(sub));
            UserDetails userDetails = new UserPrinciple(user.get());

            boolean validToken = jwtService.isValidateToken(token, userDetails);
            if (!validToken) {
                return CommonResponse.builder().message("Token is not valid").response(null).code(401).build();
            }else {
                Optional<Department> existingDepartment = departmentRepo.findById(departmentId);
                if(existingDepartment.isPresent()){
                    Department updatedDepartment = existingDepartment.get();
                    updatedDepartment.setDepartmentName(department.getDepartmentName());
                    updatedDepartment.setDepartmentCode(department.getDepartmentCode());
                    updatedDepartment.setModifiedDate(LocalDateTime.now());
                    if(department.getIsActive() == null){
                        updatedDepartment.setIsActive(Boolean.TRUE);
                    }else {
                        updatedDepartment.setIsActive(department.getIsActive());
                    }
                    departmentRepo.save(updatedDepartment);

                    return CommonResponse.builder().message("Department Updated Successfully").response(updatedDepartment).code(201).build();
                }else {
                    return CommonResponse.builder().message("Department Updated Failed").response(null).code(401).build();
                }
            }
        }
    }


    public CommonResponse deleteDepartment(int departmentId,String token){
        boolean expiredToken = jwtService.isTokenExpired(token);
        if (expiredToken) {
            return CommonResponse.builder().message("Token is expired").response(null).code(401).build();
        } else {
            Claims claims = jwtService.extractAllClaims(token);
            String sub = String.valueOf(claims.get("sub"));
            Optional<Users> user = Optional.ofNullable(userRepo.findByUserName(sub));
            UserDetails userDetails = null;

            if(user.isPresent()){
                userDetails =new UserPrinciple(user.get());
            }

            boolean validToken = jwtService.isValidateToken(token, userDetails);
            if (!validToken) {
                return CommonResponse.builder().message("Token is not valid").response(null).code(401).build();
            }else {
                try {
                    departmentRepo.deleteById(departmentId);
                    return CommonResponse.builder().message("Department deleted successfully").response(null).code(200).build();
                }catch (Exception e){
                    return CommonResponse.builder().message("Department deleted Failed").response(null).code(401).build();
                }
            }
        }
    }
}
