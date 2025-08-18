package com.ecuatrails.api.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "\"user\"")
public class User {
	
	//Columns
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_preference_id")
   	private UserPreference userPreference; 
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
    		name = "user_role",
    		joinColumns = @JoinColumn(name = "user_id"),
    		inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles = new ArrayList<>();

    @Column(length = 32)
    private String name;

    @Column(length = 32)
    private String lastName;

    @Column(length = 64, unique = true)
    private String username;

    @Column(length = 128, unique = true)
    private String email;

    @Column(length = 128)
    private String password;

    private LocalDate birthday;

    @Column(length = 16)
    private String authProvider;

    private LocalDateTime created;

    private LocalDateTime modified;

    @Column(length = 128)
    private String uid;
    
    // Constructor
    public User(Integer userId, UserPreference userPreference, List<Role> roles, String name, String lastName,
			String username, String email, String password, LocalDate birthday, String authProvider,
			LocalDateTime created, LocalDateTime modified, String uid) {
		super();
		this.userId = userId;
		this.userPreference = userPreference;
		this.roles = roles;
		this.name = name;
		this.lastName = lastName;
		this.username = username;
		this.email = email;
		this.password = password;
		this.birthday = birthday;
		this.authProvider = authProvider;
		this.created = created;
		this.modified = modified;
		this.uid = uid;
	}
    
    // Empty Constructor
    public User() {
		super();
	}
    
    // Getters and Setters
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public UserPreference getUserPreference() {
		return userPreference;
	}

	public void setUserPreference(UserPreference userPreference) {
		this.userPreference = userPreference;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public LocalDate getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}

	public String getAuthProvider() {
		return authProvider;
	}

	public void setAuthProvider(String authProvider) {
		this.authProvider = authProvider;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public LocalDateTime getModified() {
		return modified;
	}

	public void setModified(LocalDateTime modified) {
		this.modified = modified;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
    
}
