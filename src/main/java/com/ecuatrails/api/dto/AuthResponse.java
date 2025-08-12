package com.ecuatrails.api.dto;

import java.util.List;

public class AuthResponse {
	private String token;
    private UserInfo user;
    private String authType;

    public static class UserInfo {
        private String name;
        private String lastName;
        private String username;
        private String email;
        private List<String> roles;

        public UserInfo(String name, String lastName, String username, String email, List<String> roles) {
            this.name = name;
            this.lastName = lastName;
            this.username = username;
            this.email = email;
            this.roles = roles;
        }

        // Getters y setters
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
