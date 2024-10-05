package com.security.SecurityExample.student.service;

import com.security.SecurityExample.auth.common.CommonResponse;
import com.security.SecurityExample.auth.model.Role;
import com.security.SecurityExample.auth.model.UserPrinciple;
import com.security.SecurityExample.auth.model.Users;
import com.security.SecurityExample.auth.repository.UserRepo;
import com.security.SecurityExample.auth.service.JWTService;
import com.security.SecurityExample.auth.service.MyUserDetailsService;
import com.security.SecurityExample.auth.service.UserService;
import com.security.SecurityExample.department.model.Department;
import com.security.SecurityExample.department.repository.DepartmentRepo;
import com.security.SecurityExample.student.model.Student;
import com.security.SecurityExample.student.repository.StudentRepository;
import com.security.SecurityExample.subject.model.Subject;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.color.CMMException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService{

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private MyUserDetailsService.AuthService authService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private DepartmentRepo departmentRepo;

    @Autowired
    private UserService userService;

    @Value("${image.destinationDir}")
    private String destinationPath;

//    @Autowired
    private Role role;


    public CommonResponse createStudent(String token, Student student){

        boolean expiredToken = jwtService.isTokenExpired(token);
        if(expiredToken){
            return CommonResponse.builder().message("Token is expired").response(null).code(400).build();
        }else {
            Claims claims = jwtService.extractAllClaims(token);
            String sub = String.valueOf(claims.get("sub"));
            Optional<Users> user = Optional.ofNullable(userRepo.findByUserName(sub));
            UserDetails userDetails = new UserPrinciple(user.get());

            Boolean validToken = jwtService.isValidateToken(token,userDetails);
            if(!validToken){
                return CommonResponse.builder().message("Token is not Valid").response(null).code(400).build();
            }else {

                Optional<Department> department = departmentRepo.findById(student.getStudentDepartment().getDepartmentId());

                student.setFirstName(student.getFirstName());
                student.setLastName(student.getLastName());
                student.setFathersName(student.getFathersName());
                student.setMothersName(student.getMothersName());
                student.setStudentCode(student.getStudentCode());
                student.setContactNumber(student.getContactNumber());
                student.setEmail(student.getEmail());
                student.setBloodGroup(student.getBloodGroup());
                student.setBirthDate(student.getBirthDate());
                student.setStudentDepartment(department.get());

                student.setParentsContact(student.getParentsContact());
                student.setRelationWithContact(student.getRelationWithContact());
                student.setPresentAddress(student.getPresentAddress());
                student.setPermanentAddress(student.getPermanentAddress());

                student.setIsActive(Boolean.TRUE);
                student.setCreatedDate(LocalDateTime.now());

                student.setCreatedUser(user.get());

//                Creating Student User
                Users createStudentUser = new Users();
                createStudentUser.setFirstName(student.getFirstName());
                createStudentUser.setLastName(student.getLastName());
                createStudentUser.setUserName(student.getEmail());
                createStudentUser.setUserPassword("password");
                createStudentUser.setCreatedDateTime(LocalDateTime.now());
                createStudentUser.setActive(Boolean.TRUE);
                createStudentUser.setRole(role != null ? role : Role.STUDENT);
                userService.createUser(createStudentUser,createStudentUser.getRole());


                studentRepository.save(student);

                return CommonResponse.builder().message("Student Added Successfully").response(student).code(200).build();
            }
        }
    }


    public CommonResponse createStudentWithUploadDoc(String token, Student student, MultipartFile file) {

        List<Student> studentList = new ArrayList<>();

        try {
            String fileExtention = null;
            File destinationDirectory = new File(destinationPath);
            if (!destinationDirectory.exists()) {
                destinationDirectory.mkdir();
            }
            String originalFileName = file.getOriginalFilename();
            String uniqueFileName = System.currentTimeMillis() + "_" + originalFileName;
            String filePath = destinationPath + File.separator + uniqueFileName;

            file.transferTo(new File(filePath));


            boolean expiredToken = jwtService.isTokenExpired(token);
            if (expiredToken) {
                return CommonResponse.builder().message("Token is expired").response(null).code(401).build();
            } else {
                Claims claims = jwtService.extractAllClaims(token);
                String sub = String.valueOf(claims.get("sub"));
                Optional<Users> user = Optional.ofNullable(userRepo.findByUserName(sub));
                UserDetails userDetails = new UserPrinciple(user.get());

                Boolean validToken = jwtService.isValidateToken(token, userDetails);
                if (!validToken) {
                    return CommonResponse.builder().message("Token is not Valid").response(null).code(401).build();
                } else {
                    student.setFirstName(student.getFirstName());
                    student.setLastName(student.getLastName());
                    student.setFathersName(student.getFathersName());
                    student.setMothersName(student.getMothersName());
                    student.setStudentCode(student.getStudentCode());
                    student.setContactNumber(student.getContactNumber());
                    student.setEmail(student.getEmail());
                    student.setBloodGroup(student.getBloodGroup());
                    student.setBirthDate(student.getBirthDate());

                    student.setParentsContact(student.getParentsContact());
                    student.setRelationWithContact(student.getRelationWithContact());
                    student.setPresentAddress(student.getPresentAddress());
                    student.setPermanentAddress(student.getPermanentAddress());

                    student.setIsActive(Boolean.TRUE);
                    student.setCreatedDate(LocalDateTime.now());

                    student.setCreatedUser(user.get());
//                    saving file related information
                    student.setActualFilePath(filePath);
                    student.setFileName(uniqueFileName);
                    student.setFileDirectoryPath(destinationPath);

                    if(originalFileName != null){
                        int lastIndex = originalFileName.lastIndexOf('.');
                        if(lastIndex>0){
                            fileExtention = originalFileName.substring(lastIndex + 1);
                        }
                    }

                    if(fileExtention != null){
                        student.setFileType(fileExtention.toString());
                    }

                    studentList.add(student);


//                    Creating Student User
                    Users createStudentUser = new Users();
                    createStudentUser.setFirstName(student.getFirstName());
                    createStudentUser.setLastName(student.getLastName());
                    createStudentUser.setUserName(student.getEmail());
                    createStudentUser.setUserPassword("password");
                    createStudentUser.setCreatedDateTime(LocalDateTime.now());
                    createStudentUser.setActive(Boolean.TRUE);
                    createStudentUser.setRole(role != null ? role : Role.STUDENT);
                    userService.createUser(createStudentUser,createStudentUser.getRole());

//                Users createStudentUser = new Users();
//                createStudentUser.setFirstName(student.getFirstName());
//                createStudentUser.setLastName(student.getLastName());
//                createStudentUser.setUserName(student.getEmail());
//                createStudentUser.setUserPassword("password");
//                createStudentUser.setRole(Role.valueOf(Role.STUDENT.name()));
//                System.out.println("Student :"+createStudentUser.getRole());
//                System.out.println("Student :"+createStudentUser.getRole().name());
//                userService.createUser(createStudentUser,createStudentUser.getRole());


                    studentRepository.save(student);

                    return CommonResponse.builder().message("Student Added Successfully").response(studentList).code(200).build();
                }
            }
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public List<Student> getAllStudet(){
        return studentRepository.findAll();
    }

    public CommonResponse updateStudent(int studentId,String token,Student student){
        boolean expiredToken = jwtService.isTokenExpired(token);
        if(expiredToken){
            return CommonResponse.builder().message("Token is Expired").response(null).code(401).build();
        }else {
            Claims claims = jwtService.extractAllClaims(token);
            String sub = String.valueOf(claims.get("sub"));
            Optional<Users> user = Optional.ofNullable(userRepo.findByUserName(sub));
            UserDetails userDetails = new UserPrinciple(user.get());

            boolean validToken = jwtService.isValidateToken(token,userDetails);
            if (!validToken){
                return CommonResponse.builder().message("Token is not valid").response(null).code(401).build();
            }else {
                Optional<Student> existingStudentOptional = studentRepository.findById(studentId);
                if (existingStudentOptional.isPresent()){
                    Student updatedStudent = existingStudentOptional.get();
                    updatedStudent.setFirstName(student.getFirstName());
                    updatedStudent.setLastName(student.getLastName());
                    updatedStudent.setStudentCode(student.getStudentCode());
                    updatedStudent.setFathersName(student.getFathersName());
                    updatedStudent.setMothersName(student.getMothersName());
                    updatedStudent.setBirthDate(student.getBirthDate());
                    updatedStudent.setContactNumber(student.getContactNumber());
                    updatedStudent.setEmail(student.getEmail());
                    updatedStudent.setBloodGroup(student.getBloodGroup());

                    updatedStudent.setPresentAddress(student.getPresentAddress());
                    updatedStudent.setPermanentAddress(student.getPermanentAddress());
                    updatedStudent.setParentsContact(student.getParentsContact());
                    updatedStudent.setRelationWithContact(student.getRelationWithContact());

                    if (student.getIsActive() == null){
                        updatedStudent.setIsActive(Boolean.TRUE);
                    }else {
                        updatedStudent.setIsActive(student.getIsActive());
                    }
                    updatedStudent.setModifiedDate(LocalDateTime.now());
                    updatedStudent.setModifiedUser(user.get());

                    studentRepository.save(updatedStudent);

                    return CommonResponse.builder().message("Student Updated Successfully").response(updatedStudent).code(200).build();
                }else {
                    return CommonResponse.builder().message("Student Updated Failed").response(null).code(200).build();
                }
            }
        }
    }

    public CommonResponse deleteStudent(String token,int studentId){
        boolean expiredToken = jwtService.isTokenExpired(token);
        if(expiredToken){
            return CommonResponse.builder().message("Token is expired").response(null).code(401).build();
        }else {
            Claims claims = jwtService.extractAllClaims(token);
            String sub = String.valueOf(claims.get("sub"));
            Optional<Users> user = Optional.ofNullable(userRepo.findByUserName(sub));
            UserDetails userDetails = null;
            if (user.isPresent()){
                userDetails = new UserPrinciple(user.get());
            }

            boolean validToken = jwtService.isValidateToken(token,userDetails);
            if (!validToken){
                return CommonResponse.builder().message("Token is not valid").response(null).code(401).build();
            }else {
                try {
                    studentRepository.deleteById(studentId);
                    return CommonResponse.builder().message("Student Deleted Successfully").response(new HashMap<>()).code(200).build();
                }catch (Exception e){
                    return CommonResponse.builder().message("Student Deleted Failed").response(new HashMap<>()).code(401).build();
                }
            }
        }
    }


//    public List<Student> selectListOfSubject(String token,List<Subject> subjectList){
//
//    }

//    public CommonResponse uploadListOfFile(String token,Student student,List<MultipartFile> fileList){
//        List<Student> studentList = new ArrayList<>();
//
//        List<String> filePathList = new ArrayList<>();
//        List<String> fileNameList = new ArrayList<>();
//        List<String> fileExtensionList = new ArrayList<>();
//        try {
//            File destinationDirectory = new File(destinationPath);
//            if (!destinationDirectory.exists()){
//                destinationDirectory.mkdir();
//            }
//
//            for(MultipartFile file : fileList){
//                if(!file.isEmpty()){
//                    String originalFileName = file.getOriginalFilename();
//                    String uniqueFileName = System.currentTimeMillis()+"_"+originalFileName;
//                    String filPath = destinationPath + File.separator + uniqueFileName;
//
//                    file.transferTo(new File(filPath));
//
//                    FileModel studentFile = new FileModel();
//                    studentFile.setActualFilePath(filPath);
//                    studentFile.setFileName(uniqueFileName);
//                    studentFile.setFileDirectoryPath(destinationPath);
//
//                    if (originalFileName != null){
//                        int lastIndex = originalFileName.lastIndexOf('.');
//                        if(lastIndex>0){
//                            String fileExtension = originalFileName.substring(lastIndex+1);
//                            studentFile.setFileType(fileExtension);
//                        }
//                    }
//                    studentFile.setStudent(student);
//                    student.getStudentFiles().add(studentFile);
//                }
//            }
//
//            boolean expiredToken = jwtService.isTokenExpired(token);
//            if(expiredToken){
//                return CommonResponse.builder().message("Token is expired").response(null).code(400).build();
//            }else {
//                Claims claims = jwtService.extractAllClaims(token);
//                String sub = String.valueOf(claims.get("sub"));
//                Optional<Users> user = Optional.ofNullable(userRepo.findByUserName(sub));
//                UserDetails userDetails = new UserPrinciple(user.get());
//
//                boolean validToken = jwtService.isValidateToken(token,userDetails);
//                if(!validToken){
//                    return CommonResponse.builder().message("Token is not Valid").response(null).code(400).build();
//                }else {
//                    student.setFirstName(student.getFirstName());
//                    student.setLastName(student.getLastName());
//                    student.setFathersName(student.getFathersName());
//                    student.setMothersName(student.getMothersName());
//                    student.setStudentCode(student.getStudentCode());
//                    student.setContactNumber(student.getContactNumber());
//                    student.setEmail(student.getEmail());
//                    student.setBloodGroup(student.getBloodGroup());
//                    student.setBirthDate(student.getBirthDate());
//                    student.setParentsContact(student.getParentsContact());
//                    student.setRelationWithContact(student.getRelationWithContact());
//                    student.setPresentAddress(student.getPresentAddress());
//                    student.setPermanentAddress(student.getPermanentAddress());
//
//                    student.setIsActive(Boolean.TRUE);
//                    student.setCreatedDate(LocalDateTime.now());
//                    student.setCreatedUser(user.get());
//
//                    studentRepository.save(student);
//                    return CommonResponse.builder().message("Student and files added successfully").response(student).code(200).build();
////                  save file information to student
//
//                }
//            }
//        }catch (IOException e){
//            throw new RuntimeException(e);
//        }
//    }
}
