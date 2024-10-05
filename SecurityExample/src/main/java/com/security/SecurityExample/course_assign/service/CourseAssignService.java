package com.security.SecurityExample.course_assign.service;

import com.security.SecurityExample.auth.common.CommonResponse;
import com.security.SecurityExample.auth.model.Role;
import com.security.SecurityExample.auth.model.UserPrinciple;
import com.security.SecurityExample.auth.model.Users;
import com.security.SecurityExample.auth.repository.UserRepo;
import com.security.SecurityExample.auth.service.JWTService;
import com.security.SecurityExample.course_assign.model.CourseAssign;
import com.security.SecurityExample.course_assign.model.SelectCourse;
import com.security.SecurityExample.course_assign.repository.CourseAssignRepo;
import com.security.SecurityExample.course_assign.repository.SelectCourseRepo;
import com.security.SecurityExample.faculty.model.Faculty;
import com.security.SecurityExample.faculty.repository.FacultyRepo;
import com.security.SecurityExample.subject.model.Subject;
import com.security.SecurityExample.subject.repository.SubjectRepo;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CourseAssignService {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private SubjectRepo subjectRepo;

    @Autowired
    private FacultyRepo facultyRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CourseAssignRepo courseAssignRepo;

    @Autowired
    private SelectCourseRepo selectCourseRepo;



    public CommonResponse createAssignCourse(String token, CourseAssign courseAssign){
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
                Optional<Subject> subject = subjectRepo.findById(courseAssign.getSelectSubject().getSubjectId());
                Optional<Faculty> faculty = facultyRepo.findById(courseAssign.getSelectFaculty().getFacultyId());

                courseAssign.setSelectSubject(subject.get());
                courseAssign.setSelectFaculty(faculty.get());
                courseAssign.setCourseAssignedDate(LocalDateTime.now());
                courseAssign.setIsActive(Boolean.TRUE);

                courseAssignRepo.save(courseAssign);
                return CommonResponse.builder().message("Course assigned successfully").response(courseAssign).code(200).build();
            }
        }
    }


    public List<CourseAssign> getAllAssignedCourse(){
        return courseAssignRepo.findAll();
    }


    public CommonResponse updateAssignedCourse(int assignedCourseId,String token,CourseAssign courseAssign){
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
                Optional<CourseAssign> existingAssignedCourse = courseAssignRepo.findById(assignedCourseId);
                Optional<Faculty> faculty = facultyRepo.findById(courseAssign.getSelectFaculty().getFacultyId());
                Optional<Subject> subject = subjectRepo.findById(courseAssign.getSelectSubject().getSubjectId());

                if(existingAssignedCourse.isPresent()){
                    CourseAssign updateAssignCourse = existingAssignedCourse.get();

                    if(courseAssign.getIsActive() == null){
                        updateAssignCourse.setIsActive(Boolean.TRUE);
                    }else {
                        updateAssignCourse.setIsActive(courseAssign.getIsActive());
                    }
                    updateAssignCourse.setCourseModifiedDate(LocalDateTime.now());
                    updateAssignCourse.setSelectSubject(subject.get());
                    updateAssignCourse.setSelectFaculty(faculty.get());

                    courseAssignRepo.save(updateAssignCourse);
                    return CommonResponse.builder().message("Course Successfully Updated").response(updateAssignCourse).code(200).build();
                }else {
                    return CommonResponse.builder().message("Course Successfully Updated").response(null).code(401).build();
                }
            }
        }
    }

    public CommonResponse deleteAssignedCourse(int assignedCourseId,String token){
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
                    courseAssignRepo.deleteById(assignedCourseId);
                    return CommonResponse.builder().message("Assigned Course Deleted Successfully").response(null).code(200).build();
                } catch (Exception e){
                    return CommonResponse.builder().message("Assigned Course Deleted Failed").response(null).code(401).build();
                }
            }

        }
    }



    public CommonResponse saveSelectCourse(String token,SelectCourse selectCourse){
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
                if (user.get().getRole() == Role.STUDENT) {
                    Optional<CourseAssign> course = courseAssignRepo.findById(selectCourse.getSelectCourse().getCourseAssignId());
                    if (course.isPresent()) {
                        selectCourse.setCreatedDate(LocalDateTime.now());
                        selectCourse.setIsActive(Boolean.TRUE);
                        selectCourse.setSelectCourse(course.get());
                        selectCourse.setStudent(user.get());
                        selectCourseRepo.save(selectCourse);
                        return CommonResponse.builder().message("Course Selected Successfully").response(selectCourse).code(200).build();
                    }else {
                        return CommonResponse.builder().message("Course not found").response(course).code(401).build();
                    }
                }else {
                    return CommonResponse.builder().message("Course Selected Failed").response(selectCourse).code(401).build();
                }
            }

        }

    }

//    individual student will see there selected course
    public CommonResponse getAllCourseByUser(String token){
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
                if(user.isPresent() && user.get().getRole() == Role.STUDENT){
                    List<SelectCourse> allSelectedCoures = selectCourseRepo.findAllSelectedCourseByUserID(user.get().getUserId());
                    return CommonResponse.builder().message("Your Selected Courses").response(allSelectedCoures).code(200).build();
                }else if (user.isPresent() && user.get().getRole() == Role.TEACHER){
                    List<SelectCourse> allSelectedCouresByStudent = selectCourseRepo.findAllCourseSelectedByStudent(user.get().getUserId());
                    return CommonResponse.builder().message("Your Selected Courses").response(allSelectedCouresByStudent).code(200).build();
                }else {
                    return CommonResponse.builder().message("No course Found").response(null).code(401).build();
                }
            }
        }
    }










}
