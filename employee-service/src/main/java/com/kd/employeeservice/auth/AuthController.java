package com.kd.employeeservice.auth;

import com.kd.employeeservice.auth.exception.TokenRefreshException;
import com.kd.employeeservice.auth.models.RefreshToken;
import com.kd.employeeservice.auth.models.Role;
import com.kd.employeeservice.auth.models.User;
import com.kd.employeeservice.auth.repository.RoleRepository;
import com.kd.employeeservice.auth.repository.UserRepository;
import com.kd.employeeservice.auth.request.LoginRequest;
import com.kd.employeeservice.auth.request.SignupRequest;
import com.kd.employeeservice.auth.request.TokenRefreshRequest;
import com.kd.employeeservice.auth.response.MessageResponse;
import com.kd.employeeservice.auth.response.TokenRefreshResponse;
import com.kd.employeeservice.auth.security.UserDetailsImpl;
import com.kd.employeeservice.auth.security.jwt.JwtUtils;
import com.kd.employeeservice.auth.services.RefreshTokenService;
import jakarta.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
  @Autowired AuthenticationManager manager;
  @Autowired UserDetailsService userDetailsService;
  @Autowired AuthenticationManager authenticationManager;
  @Autowired UserRepository userRepository;
  @Autowired RoleRepository roleRepository;
  @Autowired PasswordEncoder encoder;
  @Autowired JwtUtils jwtUtils;
  @Autowired RefreshTokenService refreshTokenService;
  private Logger logger = LoggerFactory.getLogger(AuthController.class);

  @PostMapping("/login")
  public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
    this.doAuthenticate(request.getEmail(), request.getPassword());
    UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
    String token = this.jwtUtils.generateTokenFromUsername(userDetails.getUsername());
    JwtResponse jwtResponse = new JwtResponse();
    jwtResponse.setJwtToken(token);
    jwtResponse.setUsername(userDetails.getUsername());
    return ResponseEntity.ok(jwtResponse);
  }

  private void doAuthenticate(String email, String password) {
    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(email, password);
    try {
      manager.authenticate(authenticationToken);
    } catch (AuthenticationException e) {
      throw new RuntimeException("Authentication failed for some reason!", e);
    }
  }

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    String jwt = this.jwtUtils.generateJwtToken(userDetails);

    List<String> roles =
        userDetails.getAuthorities().stream()
            .map(item -> item.getAuthority())
            .collect(Collectors.toList());

    RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

    return ResponseEntity.ok(
        new com.kd.employeeservice.auth.response.JwtResponse(
            jwt,
            refreshToken.getToken(),
            userDetails.getId(),
            userDetails.getUsername(),
            userDetails.getEmail(),
            roles));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity.badRequest()
          .body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity.badRequest()
          .body(new MessageResponse("Error: Email is already in use!"));
    }

    // Create new user's account
    User user =
        new User(
            signUpRequest.getUsername(),
            signUpRequest.getEmail(),
            encoder.encode(signUpRequest.getPassword()));

    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      Role userRole =
          roleRepository
              .findByName(RoleType.ROLE_USER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);
    } else {
      strRoles.forEach(
          role -> {
            switch (role) {
              case "admin" -> {
                Role adminRole =
                    roleRepository
                        .findByName(RoleType.ROLE_ADMIN)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(adminRole);
              }
              case "mod" -> {
                Role modRole =
                    roleRepository
                        .findByName(RoleType.ROLE_MODERATOR)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(modRole);
              }
              default -> {
                Role userRole =
                    roleRepository
                        .findByName(RoleType.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(userRole);
              }
            }
          });
    }

    user.setRoles(roles);
    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }

  @PostMapping("/refreshtoken")
  public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
    String requestRefreshToken = request.getRefreshToken();

    return refreshTokenService
        .findByToken(requestRefreshToken)
        .map(refreshTokenService::verifyExpiration)
        .map(RefreshToken::getUser)
        .map(
            user -> {
              String token = jwtUtils.generateTokenFromUsername(user.getUsername());
              return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
            })
        .orElseThrow(
            () ->
                new TokenRefreshException(
                    requestRefreshToken, "Refresh token is not in database!"));
  }
}
