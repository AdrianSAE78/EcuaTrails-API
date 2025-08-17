package com.ecuatrails.api.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity
public class Role {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer roleId;
	
	@ManyToMany(mappedBy = "roles")
	private List<User> users = new ArrayList<>();
	
	@Column(length = 8, unique = true)
	private String roleCode;
	
	@Column(length = 16)
	private String roleName;
	
	private Boolean status = true;
	
	// Constructor	
	public Role(Integer roleId, List<User> users, String roleCode, String roleName, Boolean status) {
		super();
		this.roleId = roleId;
		this.users = users;
		this.roleCode = roleCode;
		this.roleName = roleName;
		this.status = status;
	}
	
	// Empty Constructor	
	public Role() {
		super();
	}

	// Getters and Setters
	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}
}
