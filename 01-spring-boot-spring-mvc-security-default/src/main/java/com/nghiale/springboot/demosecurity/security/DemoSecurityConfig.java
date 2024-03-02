package com.nghiale.springboot.demosecurity.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

//B1: Add Configuration Annotation
@Configuration
public class DemoSecurityConfig {

    // add support for JDBC ... no more hardcoded users
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        // refactor -> introduce variable
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        // define query to retrieve a user by username
        jdbcUserDetailsManager.setUsersByUsernameQuery("select user_id, pw, active from members where user_id=?");

        // define query to retrieve the role by username
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("select user_id, role from roles where user_id=?");

        return jdbcUserDetailsManager;
    }

    //Custom Login Form
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(configurer ->
                        configurer
                                .requestMatchers("/").hasRole("EMPLOYEE")
                                .requestMatchers("/leaders/**").hasRole("MANAGER")
                                .requestMatchers("/systems/**").hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .formLogin(form ->
                        form
                                .loginPage("/loginPage")
                                .loginProcessingUrl("/authenticateTheUser")
                                .permitAll()
                )
                .logout(logout -> logout.permitAll()
                )
                .exceptionHandling(configurer ->
                        configurer.accessDeniedPage("/access-denied")
                );
        return httpSecurity.build();
    }

    /*
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
    */
}






























