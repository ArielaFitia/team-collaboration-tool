package com.ditto.teamcollaborationtool.controller;

import com.ditto.teamcollaborationtool.model.Role;
import com.ditto.teamcollaborationtool.model.User;
import com.ditto.teamcollaborationtool.payload.request.LoginRequest;
import com.ditto.teamcollaborationtool.payload.request.SignupRequest;
import com.ditto.teamcollaborationtool.payload.response.JwtResponse;
import com.ditto.teamcollaborationtool.payload.response.MessageResponse;
import com.ditto.teamcollaborationtool.repository.RoleRepository;
import com.ditto.teamcollaborationtool.repository.UserRepository;
import com.ditto.teamcollaborationtool.security.jwt.JwtUtils;
import com.ditto.teamcollaborationtool.security.service.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        // Regular user registration with ROLE_USER
        return registerWithRole(signUpRequest, Role.ERole.ROLE_USER);
    }

    @PostMapping("/register/admin")
    @PreAuthorize("hasRole('ADMIN')") // Only existing admins can create new admins
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody SignupRequest signUpRequest) {
        // Admin registration with ROLE_ADMIN
        return registerWithRole(signUpRequest, Role.ERole.ROLE_ADMIN);
    }

    private ResponseEntity<?> registerWithRole(SignupRequest signUpRequest, Role.ERole roleType) {
        // Common registration logic
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        User user = new User(
                signUpRequest.getUsername(),
                encoder.encode(signUpRequest.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName(roleType)
                .orElseThrow(() -> new RuntimeException("Error: Role not found."));
        roles.add(role);
        user.setRoles(roles);

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
