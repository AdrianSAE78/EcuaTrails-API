package com.ecuatrails.api.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecuatrails.api.model.Role;
import com.ecuatrails.api.model.User;
import com.ecuatrails.api.repository.RoleRepository;
import com.ecuatrails.api.repository.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

@Service
public class FirebaseService {

	@Autowired
    private FirebaseAuth firebaseAuth;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;

    public FirebaseToken verifyIdToken(String idToken) throws FirebaseAuthException {
        return firebaseAuth.verifyIdToken(idToken);
    }

    public User findOrCreateUser(FirebaseToken firebaseToken) {
        String firebaseUid = firebaseToken.getUid();
        String email = firebaseToken.getEmail();
        String name = firebaseToken.getName();

        Optional<User> existingUser = userRepository.findByUid(firebaseUid);
        
        if (existingUser.isPresent()) {
            return existingUser.get();
        }

        Optional<User> userByEmail = userRepository.findByEmail(email);
        
        if (userByEmail.isPresent()) {
            User user = userByEmail.get();
            user.setUid(firebaseUid);
            user.setAuthProvider("FIREBASE");
            user.setModified(LocalDateTime.now());
            return userRepository.save(user);
        }

        User newUser = new User();
        newUser.setUid(firebaseUid);
        newUser.setEmail(email);
        newUser.setName(extractFirstName(name));
        newUser.setLastName(extractLastName(name));
        newUser.setUsername(generateUsername(email));
        newUser.setAuthProvider("FIREBASE");
        newUser.setCreated(LocalDateTime.now());
        newUser.setModified(LocalDateTime.now());
        
        Role defaultRole = roleRepository.findByRoleCode("USER")
                .orElseThrow(() -> new RuntimeException("Rol USER no encontrado"));
        newUser.setRoles(new ArrayList<>());
        newUser.getRoles().add(defaultRole);

        return userRepository.save(newUser);
    }

    private String extractFirstName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) return "Usuario";
        String[] parts = fullName.trim().split("\\s+");
        return parts[0];
    }

    private String extractLastName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) return "Firebase";
        String[] parts = fullName.trim().split("\\s+");
        return parts.length > 1 ? parts[parts.length - 1] : "Firebase";
    }

    private String generateUsername(String email) {
        String baseUsername = email.split("@")[0];
        String username = baseUsername;
        int counter = 1;
        
        while (userRepository.findByUsername(username).isPresent()) {
            username = baseUsername + counter;
            counter++;
        }
        
        return username;
    }
}
