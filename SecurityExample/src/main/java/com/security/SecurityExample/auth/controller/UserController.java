package com.security.SecurityExample.auth.controller;

import com.security.SecurityExample.auth.common.CommonResponse;
import com.security.SecurityExample.auth.model.Users;
import com.security.SecurityExample.auth.repository.UserRepo;
import com.security.SecurityExample.auth.service.UserService;
import com.security.SecurityExample.auth.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MyUserDetailsService.AuthService authService;

    @Autowired
    private UserRepo userRepo;



    @PostMapping("/create-user")
    public CommonResponse createUser(@RequestBody Users user){
        return userService.createUser(user,user.getRole());
    }

    @GetMapping("/get-all-users")
    public List<Users> getAllUser(){
        return userService.getAllUser();
    }

    @DeleteMapping("/delete-user")
    public String deleteUser(@RequestParam(value = "userID") int userId){
        userService.deleteUserById(userId);
        return "Deleted User Successfully";
    }

    @PutMapping("/update-user/{userId}")
    public String updateUser(@PathVariable int userId, @RequestBody Users user){
        userService.updateUserById(userId,user,user.getRole());
        return "User name "+user.getUserName() +" Updated Successfully";
    }

    @PostMapping("/login")
    public CommonResponse login(@RequestBody Users user) throws Exception {
        return authService.verify(user);

    }

    @GetMapping("/current-user")
    public CommonResponse getLoggedInUser(Principal principal){
        Users user = userRepo.findByUserName(principal.getName());
        Map<String,String> response = new HashMap<>();
        response.put("firstName",user.getFirstName());
        response.put("lastName",user.getLastName());
        response.put("userName",principal.getName());
        response.put("authorities", String.valueOf(user.getRole()));
        return CommonResponse.builder().message("Logged in User").response(response).code(200).build();
    }

    @GetMapping("/get-all-active-users")
    public CommonResponse getAllActiveUsers(){
        List<Users> response = userService.getAllActiveUsers();
        return CommonResponse.builder().message("All Active Users").response(response).code(200).build();
    }





//    For returning Boolean value from userService.verify()
//    @PostMapping("/login")
//    public String login(@RequestBody Users user){
//        if(userService.verify(user)){
//            return "Successfully Login";
//        }
//        return "Login Failed";
//    }



}
