package com.ecuatrails.api.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AuthResponse", description = "Respuesta de autenticación y sesión")
public class AuthResponse {
	@Schema(description = "JWT firmado (puede ser null en /session)", example = "eyJhbGciOi...")
    private String token;

    @Schema(description = "Datos básicos del usuario")
    private UserInfo user;

    @Schema(description = "Proveedor de autenticación", example = "LOCAL", allowableValues = {"LOCAL","FIREBASE"})
    private String authType;

    @Schema(name = "AuthResponse.UserInfo", description = "Perfil mínimo del usuario")
    public static class UserInfo {
    	@Schema(example = "1") private Integer id;
    	@Schema(example = "Ana") private String name;
        @Schema(example = "Pérez") private String lastName;
        @Schema(example = "ana") private String username;
        @Schema(example = "ana@acme.com") private String email;
        @Schema(description = "Roles con prefijo ROLE_", example = "[\"ROLE_USER\",\"ROLE_ADMIN\"]")
        private List<String> roles;

        public UserInfo(Integer id, String name, String lastName, String username, String email, List<String> roles) {
        	this.id = id;
            this.name = name;
            this.lastName = lastName;
            this.username = username;
            this.email = email;
            this.roles = roles;
        }

        // Getters y setters
        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public List<String> getRoles() { return roles; }
        public void setRoles(List<String> roles) { this.roles = roles; }
    }

    public static AuthResponseBuilder builder() {
        return new AuthResponseBuilder();
    }

    public static class AuthResponseBuilder {
        private String token;
        private UserInfo user;
        private String authType;

        public AuthResponseBuilder token(String token) {
            this.token = token;
            return this;
        }

        public AuthResponseBuilder user(UserInfo user) {
            this.user = user;
            return this;
        }

        public AuthResponseBuilder authType(String authType) {
            this.authType = authType;
            return this;
        }

        public AuthResponse build() {
            AuthResponse response = new AuthResponse();
            response.token = this.token;
            response.user = this.user;
            response.authType = this.authType;
            return response;
        }
    }

    // Getters and setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    
    public UserInfo getUser() { return user; }
    public void setUser(UserInfo user) { this.user = user; }
    
    public String getAuthType() { return authType; }
    public void setAuthType(String authType) { this.authType = authType; }
}
