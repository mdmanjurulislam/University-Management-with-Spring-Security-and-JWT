package com.security.SecurityExample.faculty.service;

import com.security.SecurityExample.auth.common.CommonResponse;
import com.security.SecurityExample.auth.model.Role;
import com.security.SecurityExample.auth.model.UserPrinciple;
import com.security.SecurityExample.auth.model.Users;
import com.security.SecurityExample.auth.repository.UserRepo;
import com.security.SecurityExample.auth.service.JWTService;
import com.security.SecurityExample.auth.service.UserService;
import com.security.SecurityExample.department.model.Department;
import com.security.SecurityExample.department.repository.DepartmentRepo;
import com.security.SecurityExample.faculty.model.Faculty;
import com.security.SecurityExample.faculty.repository.FacultyRepo;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FacultyService {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private DepartmentRepo departmentRepo;

    @Autowired
    private FacultyRepo facultyRepo;

    @Autowired
    private UserService userService;

    private Role role;


    public CommonResponse createFaculty(String token, Faculty faculty){
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
                Optional<Department> optionalDepartment = departmentRepo.findById(faculty.getMajor().getDepartmentId());
                faculty.setFirstName(faculty.getFirstName());
                faculty.setLastName(faculty.getLastName());
                faculty.setEducationalQualification(faculty.getEducationalQualification());
                faculty.setMajor(optionalDepartment.get());
                faculty.setContactNumber(faculty.getContactNumber());
                faculty.setEmail(faculty.getEmail());
                faculty.setPresentAddress(faculty.getPresentAddress());
                faculty.setPermanentAddress(faculty.getPermanentAddress());
                faculty.setIsActive(Boolean.TRUE);
                faculty.setCreatedDate(LocalDateTime.now());

                Users createFacultyUser = new Users();
                createFacultyUser.setFirstName(faculty.getFirstName());
                createFacultyUser.setLastName(faculty.getLastName());
                createFacultyUser.setUserName(faculty.getEmail());
                createFacultyUser.setUserPassword("password");
                createFacultyUser.setCreatedDateTime(LocalDateTime.now());
                createFacultyUser.setActive(Boolean.TRUE);
                createFacultyUser.setRole(role != null ? role : Role.TEACHER);
                userService.createUser(createFacultyUser,createFacultyUser.getRole());

                facultyRepo.save(faculty);
                return CommonResponse.builder().message("Faculty Created Successfully").response(faculty).code(201).build();
            }
        }
    }

    public List<Faculty> getAllFaculty(){
        return facultyRepo.findAll();
    }

    public CommonResponse updateFaculty(int facultyId,String token,Faculty faculty){
        boolean expiredToken = jwtService.isTokenExpired(token);
        if(expiredToken){
            return CommonResponse.builder().message("Token is expired").response(null).code(401).build();
        }else {
            Claims claims = jwtService.extractAllClaims(token);
            String sub = String.valueOf(claims.get("sub"));
            Optional<Users> user = Optional.ofNullable(userRepo.findByUserName(sub));
            UserDetails userDetails = new UserPrinciple(user.get());

            boolean validToken = jwtService.isValidateToken(token,userDetails);
            if(!validToken){
                return CommonResponse.builder().message("Token is not valid").response(null).code(401).build();
            }else {
                Optional<Faculty> existingFaculty = facultyRepo.findById(facultyId);
                if(existingFaculty.isPresent()){
                    Optional<Department> optionalDepartment = departmentRepo.findById(faculty.getMajor().getDepartmentId());
                    Faculty updatedFaculty = existingFaculty.get();
                    updatedFaculty.setFirstName(faculty.getFirstName());
                    updatedFaculty.setLastName(faculty.getLastName());
                    updatedFaculty.setEducationalQualification(faculty.getEducationalQualification());
                    updatedFaculty.setMajor(optionalDepartment.get());
                    updatedFaculty.setContactNumber(faculty.getContactNumber());
                    updatedFaculty.setEmail(faculty.getEmail());
                    updatedFaculty.setPresentAddress(faculty.getPresentAddress());
                    updatedFaculty.setPermanentAddress(faculty.getPermanentAddress());
                    if(faculty.getIsActive() == null){
                        updatedFaculty.setIsActive(Boolean.TRUE);
                    }else {
                        updatedFaculty.setIsActive(faculty.getIsActive());
                    }
                    updatedFaculty.setModifiedDate(LocalDateTime.now());

                    facultyRepo.save(updatedFaculty);
                    return CommonResponse.builder().message("Faculty Updated Successfully").response(updatedFaculty).code(200).build();
                }else {
                    return CommonResponse.builder().message("Faculty Updated Failed").response(null).code(401).build();
                }
            }
        }
    }

    public CommonResponse deleteFaculty(int facultyId,String token){
        boolean expiredToken = jwtService.isTokenExpired(token);
        if(expiredToken){
            return CommonResponse.builder().message("Token is expired").response(null).code(401).build();
        }else {
            Claims claims = jwtService.extractAllClaims(token);
            String sub = String.valueOf(claims.get("sub"));
            Optional<Users> user = Optional.ofNullable(userRepo.findByUserName(sub));
            UserDetails userDetails = new UserPrinciple(user.get());

            boolean validToken = jwtService.isValidateToken(token,userDetails);
            if(!validToken){
                return CommonResponse.builder().message("Token is not valid").response(null).code(401).build();
            }else {
                try {
                    facultyRepo.deleteById(facultyId);
                    return CommonResponse.builder().message("Faculty Successfully Deleted").response(null).code(200).build();
                }catch (Exception e){
                    return CommonResponse.builder().message("Faculty Deleted Failed").response(null).code(401).build();
                }
            }
        }
    }
}
