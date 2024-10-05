package com.security.SecurityExample.subject.service;

import com.security.SecurityExample.auth.common.CommonResponse;
import com.security.SecurityExample.auth.dto.UserDto;
import com.security.SecurityExample.auth.model.UserPrinciple;
import com.security.SecurityExample.auth.model.Users;
import com.security.SecurityExample.auth.repository.UserRepo;
import com.security.SecurityExample.auth.service.JWTService;
import com.security.SecurityExample.department.model.Department;
import com.security.SecurityExample.department.repository.DepartmentRepo;
import com.security.SecurityExample.subject.model.Subject;
import com.security.SecurityExample.subject.repository.SubjectRepo;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepo subjectRepo;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private DepartmentRepo departmentRepo;


    public CommonResponse createSubject(String token, Subject subject){
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
                Optional<Department> department = departmentRepo.findById(subject.getSubjectDepartment().getDepartmentId());
                subject.setSubjectName(subject.getSubjectName());
                subject.setSubjectCode(subject.getSubjectCode());
                subject.setCreditAmount(subject.getCreditAmount());
                subject.setCreatedDate(LocalDateTime.now());
                subject.setIsActive(Boolean.TRUE);
                subject.setSubjectDepartment(department.get());
                subject.setCreatedUser(user.get());

                subjectRepo.save(subject);
                return CommonResponse.builder().message("Subject Created Successfully").response(subject).code(201).build();
            }
        }
    }

    public List<Subject> getAllSubject(){
        return subjectRepo.findAll();
    }

    public CommonResponse updateSubject(int subjectId,String token,Subject subject){
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
                Optional<Subject> existSubject = subjectRepo.findById(subjectId);
                if(existSubject.isPresent()){
                    Subject updatedSubject = existSubject.get();
                    updatedSubject.setSubjectName(subject.getSubjectName());
                    updatedSubject.setSubjectCode(subject.getSubjectCode());
                    updatedSubject.setSubjectType(subject.getSubjectType());
                    updatedSubject.setCreditAmount(subject.getCreditAmount());
                    if(subject.getIsActive() == null){
                        updatedSubject.setIsActive(Boolean.TRUE);
                    }else {
                        updatedSubject.setIsActive(subject.getIsActive());
                    }
                    updatedSubject.setModifiedDate(LocalDateTime.now());
                    updatedSubject.setModifiedUser(user.get());
//                    updatedSubject.setModifiedUser((Users) userResponse.);

                    subjectRepo.save(updatedSubject);
                    return CommonResponse.builder().message("Subject Updated Successfully").response(updatedSubject).code(201).build();
                }else {
                    return CommonResponse.builder().message("Subject Updated Failed").response(null).code(204).build();
                }
            }
        }
    }

    public CommonResponse deleteSubject(int subjectId,String token){
        boolean expiredToken = jwtService.isTokenExpired(token);
        if(expiredToken){
            return CommonResponse.builder().message("Token is expired").response(null).code(401).build();
        }else {
            Claims claims = jwtService.extractAllClaims(token);
            String sub = String.valueOf(claims.get("sub"));
            Optional<Users> user = Optional.ofNullable(userRepo.findByUserName(sub));
            UserDetails userDetails = null;

            if(user.isPresent()){
                userDetails = new UserPrinciple(user.get());
            }

            boolean validToken = jwtService.isValidateToken(token,userDetails);
            if(!validToken){
                return CommonResponse.builder().message("Token is not valid").response(null).code(401).build();
            }else {
                try {
                    subjectRepo.deleteById(subjectId);
                    return CommonResponse.builder().message("Subject deleted successfully").response(null).code(200).build();
                } catch (Exception e){
                    return CommonResponse.builder().message("Subject deleted failed").response(null).code(204).build();
                }
            }
        }
    }
}
