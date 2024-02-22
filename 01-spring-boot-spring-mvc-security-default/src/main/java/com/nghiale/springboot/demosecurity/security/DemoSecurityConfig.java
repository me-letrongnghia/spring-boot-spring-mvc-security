package com.nghiale.springboot.demosecurity.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

//B1: Add Configuration Annotation
@Configuration
public class DemoSecurityConfig {

    @Bean
    //B2: Create InMemoryUserDetailsManager to save user's password and role
    public InMemoryUserDetailsManager userDetailsManager() {
        UserDetails nghiale = User.builder()
                .username("nghiale")
                .password("{noop}test123")
                .roles("ADMIN","MANAGER","EMPLOYEE")
                .build();
        UserDetails tole = User.builder()
                .username("tole")
                .password("{noop}test123")
                .roles("MANAGER","EMPLOYEE")
                .build();
        UserDetails thinhtran = User.builder()
                .username("thinhtran")
                .password("{noop}test123")
                .roles("EMPLOYEE")
                .build();
        return new InMemoryUserDetailsManager(nghiale, tole, thinhtran);
    }

    //Custom Login Form
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(configurer ->
                        configurer
                                .anyRequest().authenticated()
                )
                .formLogin(form ->
                        form
                                .loginPage("/loginPage")
                                .loginProcessingUrl("/authenticateTheUser")
                                .permitAll()
                );
        return httpSecurity.build();
    }
}
