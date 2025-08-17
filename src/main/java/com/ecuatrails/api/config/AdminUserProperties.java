package com.ecuatrails.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.admin")
public class AdminUserProperties {
  private String username = "admin";
  private String email = "admin@ecuatrails.com";
  private String password = "admin123"; 
  private String name = "Admin";
  private String lastName = "System";

  // getters/setters
  public String getUsername() { return username; }
  public void setUsername(String username) { this.username = username; }
  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }
  public String getPassword() { return password; }
  public void setPassword(String password) { this.password = password; }
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  public String getLastName() { return lastName; }
  public void setLastName(String lastName) { this.lastName = lastName; }
}