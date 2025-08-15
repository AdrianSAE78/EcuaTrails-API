package com.ecuatrails.api.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecuatrails.api.dto.AuthRequest;
import com.ecuatrails.api.dto.AuthResponse;
import com.ecuatrails.api.dto.FirebaseAuthRequest;
import com.ecuatrails.api.dto.RegisterRequest;
import com.ecuatrails.api.model.Role;
import com.ecuatrails.api.model.User;
import com.ecuatrails.api.repository.RoleRepository;
import com.ecuatrails.api.service.FirebaseService;
import com.ecuatrails.api.service.JwtService;
import com.ecuatrails.api.service.UserService;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private FirebaseService firebaseService;
    
    @Autowired
    private RoleRepository roleRepository;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
            )
        );

        User user = userService.findByUsername(request.getUsername());
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String jwtToken = jwtService.generateToken(userDetails);

        List<String> roles = user.getRoles().stream()
                .map(role -> "ROLE_" + role.getRoleCode())
                .collect(Collectors.toList());

        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
                user.getName(),
                user.getLastName(),
                user.getUsername(),
                user.getEmail(),
                roles
        );

        return ResponseEntity.ok(AuthResponse.builder()
            .token(jwtToken)
            .user(userInfo)
            .build());
    }
    
    @PostMapping("/firebase")
    public ResponseEntity<?> authenticateWithFirebase(@RequestBody FirebaseAuthRequest request) {
        try {
            FirebaseToken firebaseToken = firebaseService.verifyIdToken(request.getIdToken());
            
            User user = firebaseService.findOrCreateUser(firebaseToken);
            
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
            String jwtToken = jwtService.generateToken(userDetails);

            List<String> roles = user.getRoles().stream()
                    .map(role -> "ROLE_" + role.getRoleCode())
                    .collect(Collectors.toList());

            AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
                    user.getName(),
                    user.getLastName(),
                    user.getUsername(),
                    user.getEmail(),
                    roles
            );

            return ResponseEntity.ok(AuthResponse.builder()
                .token(jwtToken)
                .user(userInfo)
                .authType("FIREBASE")
                .build());
                
        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Token de Firebase inválido: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error interno del servidor: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setBirthday(request.getBirthday());
        user.setAuthProvider("LOCAL");
        user.setCreated(LocalDateTime.now());
        user.setModified(LocalDateTime.now());
        
        Role defaultRole = roleRepository.findByRoleCode("USER")
                .orElseThrow(() -> new RuntimeException("Rol USER no encontrado"));
        user.setRoles(new ArrayList<>());
        user.getRoles().add(defaultRole);
        
        userService.save(user);
                
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String jwtToken = jwtService.generateToken(userDetails);
        
        List<String> roles = user.getRoles().stream()
                .map(role -> "ROLE_" + role.getRoleCode())
                .collect(Collectors.toList());
        
        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
                user.getName(),
                user.getLastName(),
                user.getUsername(),
                user.getEmail(),
                roles
        );
        
        return ResponseEntity.ok(AuthResponse.builder()
                .token(jwtToken)
                .user(userInfo)
                .authType("LOCAL")
                .build());
    }
    
    @GetMapping("/session")
    public ResponseEntity<AuthResponse> session(@AuthenticationPrincipal UserDetails ud) {
        User user = userService.findByUsername(ud.getUsername());
        List<String> roles = user.getRoles().stream()
            .map(r -> "ROLE_" + r.getRoleCode()).toList();

        var info = new AuthResponse.UserInfo(
            user.getName(), user.getLastName(), user.getUsername(), user.getEmail(), roles
        );
        return ResponseEntity.ok(AuthResponse.builder()
            .token(null)
            .user(info)
            .authType(user.getAuthProvider())
            .build());
    }
    
}