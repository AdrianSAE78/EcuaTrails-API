package com.ecuatrails.api.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecuatrails.api.dto.ApiError;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@Validated
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Autenticación con credenciales locales, Firebase y sesión actual")
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

	@Operation(summary = "Iniciar sesión (credenciales locales)", description = "Autentica con username/password y devuelve un **JWT** y la información del usuario.", operationId = "loginLocal")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Autenticado", content = @Content(schema = @Schema(implementation = AuthResponse.class), examples = @ExampleObject(name = "ok", value = """
					{
					  "token": "eyJhbGciOi...",
					  "authType": "LOCAL",
					  "user": {
					    "name": "Ana",
					    "lastName": "Pérez",
					    "username": "ana",
					    "email": "ana@acme.com",
					    "roles": ["ROLE_USER"]
					  }
					}
					"""))),
			@ApiResponse(responseCode = "401", description = "Credenciales inválidas", content = @Content(schema = @Schema(implementation = ApiError.class))) })

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> authenticate(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Credenciales de acceso", content = @Content(schema = @Schema(implementation = AuthRequest.class), examples = @ExampleObject(value = """
					{ "username":"ana", "password":"Secr3t0!" }
					"""))) @RequestBody AuthRequest request) {

		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

		UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
		String jwtToken = jwtService.generateToken(userDetails);

		var user = userService.findByUsername(request.getUsername());
		
		List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

		AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(user.getUserId(), user.getName(), user.getLastName(),
				user.getUsername(), user.getEmail(), roles);

		return ResponseEntity.ok(AuthResponse.builder().token(jwtToken).user(userInfo).authType("LOCAL").build());
	}

	@Operation(summary = "Iniciar sesión con Firebase", description = "Valida el **Firebase ID Token**, registra o actualiza el usuario y emite un **JWT** propio.", operationId = "loginFirebase")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Autenticado", content = @Content(schema = @Schema(implementation = AuthResponse.class), examples = @ExampleObject(value = """
					{
					  "token": "eyJhbGciOi...",
					  "authType": "FIREBASE",
					  "user": {
					    "name": "Ana",
					    "lastName": "Pérez",
					    "username": "ana.firebase",
					    "email": "ana@acme.com",
					    "roles": ["ROLE_USER"]
					  }
					}
					"""))),
			@ApiResponse(responseCode = "401", description = "Token de Firebase inválido o expirado", content = @Content(schema = @Schema(implementation = ApiError.class), examples = @ExampleObject(value = """
					{
					  "status": 401,
					  "error": "Unauthorized",
					  "message": "Token de Firebase inválido",
					  "path": "/api/auth/firebase"
					}
					"""))),
			@ApiResponse(responseCode = "500", description = "Error interno", content = @Content(schema = @Schema(implementation = ApiError.class))) })

	@PostMapping("/firebase")
	public ResponseEntity<?> authenticateWithFirebase(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "ID Token emitido por Firebase (cliente)", content = @Content(schema = @Schema(implementation = FirebaseAuthRequest.class), examples = @ExampleObject(value = """
					{ "idToken":"eyJhbGciOiJSUzI1NiIsImtpZCI6..." }
					"""))) @RequestBody FirebaseAuthRequest request) {
		try {
			FirebaseToken firebaseToken = firebaseService.verifyIdToken(request.getIdToken());

			User user = firebaseService.findOrCreateUser(firebaseToken);

			UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
			String jwtToken = jwtService.generateToken(userDetails);

			List<String> roles = user.getRoles().stream().map(role -> "ROLE_" + role.getRoleCode())
					.collect(Collectors.toList());

			AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(user.getUserId(), user.getName(), user.getLastName(),
					user.getUsername(), user.getEmail(), roles);

			return ResponseEntity
					.ok(AuthResponse.builder().token(jwtToken).user(userInfo).authType("FIREBASE").build());

		} catch (FirebaseAuthException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token de Firebase inválido: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error interno del servidor: " + e.getMessage());
		}
	}

	@Operation(summary = "Registro de usuario (credenciales locales)", description = "Crea un usuario **LOCAL** y devuelve un **JWT** listo para usar.", operationId = "registerLocal")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Registrado", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
			@ApiResponse(responseCode = "409", description = "Conflicto (username/email ya existen)", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "400", description = "Validación fallida", content = @Content(schema = @Schema(implementation = ApiError.class))) })

	@PostMapping("/register")
	public ResponseEntity<AuthResponse> register(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Datos de registro", content = @Content(schema = @Schema(implementation = RegisterRequest.class), examples = @ExampleObject(value = """
					{
					  "name":"Ana",
					  "lastName":"Pérez",
					  "username":"ana",
					  "email":"ana@acme.com",
					  "password":"Secr3t0!",
					  "birthday":"1995-03-15"
					}
					"""))) @RequestBody RegisterRequest request) {
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

		List<String> roles = user.getRoles().stream().map(role -> "ROLE_" + role.getRoleCode())
				.collect(Collectors.toList());

		AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(user.getUserId(), user.getName(), user.getLastName(),
				user.getUsername(), user.getEmail(), roles);

		return ResponseEntity.ok(AuthResponse.builder().token(jwtToken).user(userInfo).authType("LOCAL").build());
	}

	@Operation(summary = "Obtener sesión actual", description = "Devuelve la información del usuario autenticado. **No** emite un nuevo token.", operationId = "me", security = @SecurityRequirement(name = "bearer-jwt"))
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = ApiError.class))) })

	@GetMapping("/session")
	@Transactional(readOnly = true)
	public ResponseEntity<AuthResponse> session(@AuthenticationPrincipal UserDetails ud) {
		var roles = ud.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

		var user = userService.findByUsername(ud.getUsername());
		var info = new AuthResponse.UserInfo(user.getUserId(), user.getName(), user.getLastName(), user.getUsername(), user.getEmail(),
				roles);

		return ResponseEntity
				.ok(AuthResponse.builder().token(null).user(info).authType(user.getAuthProvider()).build());
	}

}