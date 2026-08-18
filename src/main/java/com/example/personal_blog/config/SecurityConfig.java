package com.example.personal_blog.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${ADMIN_USERNAME}")
    private String adminUsername;

    @Value("${ADMIN_PASSWORD}")
    private String adminPassword;

    @Value("${USER_USERNAME}")
    private String userUsername;

    @Value("${USER_PASSWORD}")
    private String userPassword;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        System.out.println(">>> SECURITY CONFIG LOADED <<<");

        http
            .cors(Customizer.withDefaults())
            .csrf((csrf) -> csrf.disable()) // Disable CSRF for stateless REST APIs (no cookies/sessions), testing environments;
            .httpBasic(Customizer.withDefaults())
            .authorizeHttpRequests( (auth) -> auth
                .requestMatchers("/api/v1/public/**").permitAll()
                .requestMatchers(HttpMethod.POST,"/api/v1/admin/articles").hasAllRoles("ADMIN","USER")
                .requestMatchers(HttpMethod.PUT,"/api/v1/admin/articles/{id}").hasAllRoles("ADMIN","USER")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/admin/articles/{id}").hasAllRoles("ADMIN","USER")
                .anyRequest().authenticated()
            );
        return http.build();
        }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of(
            "http://localhost:3000"
        ));

        config.setAllowedMethods(List.of(
            "GET",
            "POST",
            "PUT",
            "DELETE"
        ));

        config.setAllowedHeaders(List.of("*"));

        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);
    
        return source;
    }

    @Bean
    public UserDetailsService userDetailsService(){

        UserDetails user = User.builder()
            .username(userUsername)
            .password(passwordEncoder().encode(userPassword))
            .roles("USER")
            .build();

        UserDetails admin = User.builder()
            .username(adminUsername)
            .password(passwordEncoder().encode(adminPassword))
            .roles("ADMIN","USER")
            .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
