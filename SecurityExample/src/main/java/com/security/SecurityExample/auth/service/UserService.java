package com.security.SecurityExample.auth.service;

import com.security.SecurityExample.auth.common.CommonResponse;
import com.security.SecurityExample.auth.model.Role;
import com.security.SecurityExample.auth.model.Users;
import com.security.SecurityExample.auth.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MyUserDetailsService.AuthService authService;

    @Autowired
    private JWTService jwtService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);


    public CommonResponse createUser(Users user, Role role){
        Optional<Users> existUser = Optional.ofNullable(userRepo.findByUserName(user.getUserName()));
        if (existUser.isPresent()){
            return CommonResponse.builder().message("User Name Already Exist").response("").code(400).build();
        }else {
            user.setFirstName(user.getFirstName());
            user.setLastName(user.getLastName());
            user.setUserName(user.getUserName());
            user.setUserPassword(authService.encodedPassword(user.getUserPassword()));
            user.setActive(Boolean.TRUE);
            user.setCreatedDateTime(LocalDateTime.now());
            user.setRole(role != null ? role : Role.USER);
            userRepo.save(user);
        }
        return CommonResponse.builder().message("User Created Successfully").response(user).code(200).build();
    }

    public List<Users> getAllUser(){
        return userRepo.findAll();
    }

    public void deleteUserById(int userId){
        userRepo.deleteById(userId);
    }

    public Users updateUserById(int userId,Users user,Role role){
        Optional<Users> existingUserOptional = userRepo.findById(userId);

        if(existingUserOptional.isPresent()){
            Users existingUser = existingUserOptional.get();
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setUserName(user.getUserName());
            existingUser.setUserPassword(authService.encodedPassword(user.getUserPassword()));
            existingUser.setModifiedDateTime(LocalDateTime.now());
            if(user.getActive() == null){
                existingUser.setActive(Boolean.TRUE);
            }else {
                existingUser.setActive(user.getActive());
            }
            existingUser.setRole(role != null ? role : Role.USER);

            return userRepo.save(existingUser);
        }else {
            System.out.println("User Id "+userId+" not found");
            throw new UsernameNotFoundException("user "+userId+" not found");
        }
    }

    public List<Users> getAllActiveUsers(){
        List<Users> activeUserList = userRepo.getAllActiveUser();
        return activeUserList;
    }

//    public boolean isExistUserName(Users user){
//        return userRepo.existByUserName(user.getUserName());
//    }

}
