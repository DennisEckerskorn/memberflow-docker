package com.denniseckerskorn.config;

import com.denniseckerskorn.security.CustomUserDetailsService;
import com.denniseckerskorn.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration class for the application.
 */
@EnableMethodSecurity
@Configuration
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
    private final CustomUserDetailsService customUserDetailsService;

    /**
     * Constructor for SecurityConfig.
     *
     * @param jwtAuthFilter            the JWT authentication filter
     * @param customUserDetailsService the custom user details service
     */
    public SecurityConfig(JwtAuthFilter jwtAuthFilter, CustomUserDetailsService customUserDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.customUserDetailsService = customUserDetailsService;
    }

    /**
     * Bean for PasswordEncoder.
     *
     * @return a BCryptPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean for CustomUserDetailsService.
     *
     * @return the custom user details service
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Bean for SecurityFilterChain.
     *
     * @param http the HttpSecurity object
     * @return a SecurityFilterChain instance
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configure(http))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/auth/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .requestMatchers("/api/v1/users/me").permitAll()
                        .requestMatchers("/api/v1/admins/**").hasAuthority("FULL_ACCESS")
                        .requestMatchers("/api/v1/teachers/**").hasAnyAuthority("FULL_ACCESS", "VIEW_OWN_DATA")
                        .requestMatchers("/api/v1/students/**").hasAnyAuthority("FULL_ACCESS", "MANAGE_STUDENTS", "VIEW_OWN_DATA")
                        .requestMatchers("/api/v1/users/**").hasAuthority("FULL_ACCESS")
                        .requestMatchers("/api/v1/notifications/**").hasAuthority("FULL_ACCESS")
                        .requestMatchers("/api/v1/studentHistories/**").hasAnyAuthority("FULL_ACCESS", "MANAGE_STUDENTS", "VIEW_OWN_DATA")
                        .requestMatchers("/api/v1/roles/**").hasAuthority("FULL_ACCESS")
                        .requestMatchers("/api/v1/permissions/**").hasAuthority("FULL_ACCESS")
                        .requestMatchers("/api/v1/memberships/**").hasAnyAuthority("FULL_ACCESS", "MANAGE_STUDENTS")
                        .requestMatchers("/api/v1/assistances/**").hasAnyAuthority("FULL_ACCESS", "MANAGE_STUDENTS")
                        .requestMatchers("/api/v1/training-sessions/**").hasAnyAuthority("FULL_ACCESS", "MANAGE_STUDENTS")
                        .requestMatchers("/api/v1/training-groups/**").hasAnyAuthority("FULL_ACCESS", "MANAGE_STUDENTS")
                        .requestMatchers("/api/v1/invoices/**").hasAnyAuthority("FULL_ACCESS", "MANAGE_STUDENTS")
                        .requestMatchers("/api/v1/invoice-lines/**").hasAnyAuthority("FULL_ACCESS", "MANAGE_STUDENTS")
                        .requestMatchers("/api/v1/payments/**").hasAnyAuthority("FULL_ACCESS", "MANAGE_STUDENTS")
                        .requestMatchers("/api/v1/products-services/**").hasAnyAuthority("FULL_ACCESS", "MANAGE_STUDENTS")
                        .requestMatchers("/api/v1/iva-types/**").hasAnyAuthority("FULL_ACCESS", "MANAGE_STUDENTS")
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


}
