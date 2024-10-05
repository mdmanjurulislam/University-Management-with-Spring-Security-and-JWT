package com.security.SecurityExample.auth.service;

import com.security.SecurityExample.auth.common.CommonResponse;
import com.security.SecurityExample.auth.model.UserPrinciple;
import com.security.SecurityExample.auth.model.Users;
import com.security.SecurityExample.auth.repository.UserRepo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepo.findByUserName(username);

        if(user == null){
            System.out.println("No user found");
//            return (UserDetails) CommonResponse.builder().message("User Not found").response(new HashMap<>()).code(401).build();
            throw new UsernameNotFoundException("user not found");
        }
        return new UserPrinciple(user);
    }

    @Service
    public static class AuthService {
        @Autowired
        private AuthenticationManager authManager;

        @Autowired
        private JWTService jwtService;

        @Autowired
        private UserRepo userRepo;

        private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

        public String encodedPassword(String password){
            return encoder.encode(password);
        }

        public CsrfToken getCsrfToken(HttpServletRequest request){
            return (CsrfToken) request.getAttribute("_csrf");
        }

        public CommonResponse verify(Users user) throws Exception{
            Authentication authentication =
                    authManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    user.getUserName(),user.getUserPassword()));

    //        Optional<Users> existUserName = Optional.ofNullable(userRepo.findByUserName(user.getUserName()));
    //        Optional<Users> existUserPassword = Optional.ofNullable(userRepo.findByUserPassword(user.getUserPassword()));

            if(authentication.isAuthenticated()){
                String token = jwtService.generateToken(user);

                Map<String,String> response = new HashMap<>();
                response.put("token",token);
                response.put("userName",user.getUserName());

                return CommonResponse.builder().message("Login Success😊").response(response).code(200).build();
            }else {
                System.out.println("Failed Login");
                return CommonResponse.builder().message("Login Failed.Bad Credentials!").response(new HashMap<>()).code(400).build();
            }
        }

        //    public Boolean verify(Users user){
    //        Authentication authentication =
    //                authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(),user.getUserPassword()));
    //        if(authentication.isAuthenticated()){
    //            return true;
    //        }else {
    //            return false;
    //        }
    //    }


    }
}
