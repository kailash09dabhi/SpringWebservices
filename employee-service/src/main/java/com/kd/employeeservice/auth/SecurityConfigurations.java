package com.kd.employeeservice.auth;

import com.kd.employeeservice.auth.security.UserDetailsServiceImpl;
import com.kd.employeeservice.auth.security.jwt.AuthEntryPointJwt;
import com.kd.employeeservice.auth.security.jwt.JwtAuthTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfigurations {
  @Bean
  public JwtAuthTokenFilter jwtAuthenticationFilter() {
    return new JwtAuthTokenFilter();
  }

  @Bean
  public AuthEntryPointJwt jwtAuthenticationEntryPoint() {
    return new AuthEntryPointJwt();
  }

  @Bean
  public JwtHelper jwtHelper() {
    return new JwtHelper();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    UserDetails userDetails =
        User.builder()
            .username("kailas")
            .password(passwordEncoder().encode("KailasLdabhi"))
            .build();
    // return new InMemoryUserDetailsManager(userDetails);
    return new UserDetailsServiceImpl();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration builder)
      throws Exception {
    return builder.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .csrf(csrf -> csrf.disable())
        .cors(cors -> cors.disable())
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/employee/**")
                    .authenticated()
                    .requestMatchers("/auth/login")
                    .permitAll()
                    .requestMatchers("/auth/signin")
                    .permitAll()
                    .requestMatchers("/auth/signup")
                    .permitAll()
                    .requestMatchers("/error")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .logout(
            logout ->
                logout
                    .logoutUrl("/auth/logout")
                    .logoutSuccessUrl("/login?logout")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID"))
        .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint()))
        .sessionManagement(
            sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    httpSecurity.addFilterBefore(
        jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    return httpSecurity.build();
  }
}
