package com.security.SecurityExample.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(customizer -> customizer.disable())
//                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("login","create-user").permitAll()
                        .requestMatchers("/get-all-users","/get-all-active-users").hasAuthority("ADMIN")
                        .requestMatchers("/student/create-student","/student/get-all-students","/student/update-student","/student/delete-student-by-id").hasAuthority("ADMIN")
                        .requestMatchers("/subject/create-subject","/subject/get-all-subjects","/subject/update-subject","/subject/delete-subject-by-id").hasAuthority("ADMIN")
                        .requestMatchers("/department/create-department","/department/get-all-departments","/department/update-department","/department/delete-department-by-id").hasAuthority("ADMIN")
                        .requestMatchers("/faculty/create-faculty","/faculty/get-all-faculty","/faculty/update-faculty","/faculty/delete-faculty-by-id").hasAuthority("ADMIN")
                        .requestMatchers("/course/assign-course","/course/update-assigned-course","/course/delete-assigned-course").hasAuthority("ADMIN")
                        .requestMatchers("/course/get-all-assigned-course").hasAnyAuthority("STUDENT","ADMIN","TEACHER")
                        .requestMatchers("/course/select-course").hasAnyAuthority("STUDENT","ADMIN")
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())               //        But what if I want to get rest access from my postman then I have to implement this
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
        //        http.formLogin(Customizer.withDefaults());            //        if I hit from my postman then my postman will respond a html form page code
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
      return config.getAuthenticationManager();
    }





//    @Bean
//    public UserDetailsService userDetailsService(){
//
//        List<UserDetails> usersList = new ArrayList<>();
//
//        UserDetails user1 = User
//                .withDefaultPasswordEncoder()  //this is deprecated.so don't use this in production
//                .username("max")
//                .password("123")
//                .roles("USER")
//                .build();
//
//        UserDetails user2 = User
//                .withDefaultPasswordEncoder()  //this is deprecated.so don't use this in production
//                .username("rex")
//                .password("456")
//                .roles("ADMIN")
//                .build();
//
//        usersList.add(user1);
//        usersList.add(user2);
//
////        return new InMemoryUserDetailsManager(user1,user2);
//        return new InMemoryUserDetailsManager(usersList);
//    }


}
