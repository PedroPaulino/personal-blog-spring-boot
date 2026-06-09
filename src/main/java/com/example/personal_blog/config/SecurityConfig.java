package com.example.personal_blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
            .csrf((csrf) -> csrf.disable()) // Disable CSRF for stateless REST APIs (no cookies/sessions), testing environments;
            .httpBasic(Customizer.withDefaults())
            .authorizeHttpRequests( (auth) -> auth
                .requestMatchers(HttpMethod.GET,"/api/v1/public/articles").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/v1/public/articles/{id}").permitAll()
                .requestMatchers(HttpMethod.POST,"/api/v1/admin/articles").hasAllRoles("ADMIN","USER")
                .requestMatchers(HttpMethod.PUT,"/api/v1/admin/articles/{id}").hasAllRoles("ADMIN","USER")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/admin/articles/{id}").hasAllRoles("ADMIN","USER")
                .anyRequest().authenticated()
            );
        return http.build();
        }
    
    @Bean
    public UserDetailsService userDetailsService(){

        UserDetails user = User.builder()
            .username("user")
            .password(passwordEncoder().encode("password"))
            .roles("USER")
            .build();

        UserDetails admin = User.builder()
            .username("admin")
            .password(passwordEncoder().encode("admin123"))
            .roles("ADMIN","USER")
            .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
