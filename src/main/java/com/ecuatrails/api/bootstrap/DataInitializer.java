package com.ecuatrails.api.bootstrap;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ecuatrails.api.config.AdminUserProperties;
import com.ecuatrails.api.model.Role;
import com.ecuatrails.api.model.User;
import com.ecuatrails.api.repository.RoleRepository;
import com.ecuatrails.api.repository.UserRepository;

@Component
public class DataInitializer implements ApplicationRunner {

  private final RoleRepository roleRepository;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AdminUserProperties adminProps;

  public DataInitializer(
      RoleRepository roleRepository,
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      AdminUserProperties adminProps
  ) {
    this.roleRepository = roleRepository;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.adminProps = adminProps;
  }

  @Override
  @Transactional
  public void run(ApplicationArguments args) {
    // 1) Roles base
    Role adminRole = ensureRole("ADMIN", "Administrador");
    Role userRole  = ensureRole("USER",  "Usuario");

    // 2) Usuario admin si no existe (por username o email)
    boolean exists = userRepository.existsByUsernameIgnoreCase(adminProps.getUsername())
        || userRepository.existsByEmailIgnoreCase(adminProps.getEmail());

    if (!exists) {
      User u = new User();
      u.setName(adminProps.getName());
      u.setLastName(adminProps.getLastName());
      u.setUsername(adminProps.getUsername());
      u.setEmail(adminProps.getEmail());
      u.setPassword(passwordEncoder.encode(adminProps.getPassword()));
      u.setAuthProvider("LOCAL");
      u.setCreated(LocalDateTime.now());
      u.setModified(LocalDateTime.now());

      List<Role> roles = new ArrayList<>();
      roles.add(adminRole);
      roles.add(userRole);
      u.setRoles(roles);

      userRepository.save(u);
      log: System.out.println("Admin user created: " + u.getUsername());
    }
  }

  private Role ensureRole(String code, String name) {
    return roleRepository.findByRoleCode(code)
        .orElseGet(() -> {
          Role r = new Role();
          r.setRoleCode(code);
          r.setRoleName(name);
          r.setStatus(true);
          return roleRepository.save(r);
        });
  }
}
