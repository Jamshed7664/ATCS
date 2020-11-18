package com.nxtlife.efkon.enforcementconfigurator.view.user;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.nxtlife.efkon.enforcementconfigurator.entity.user.Authority;
import com.nxtlife.efkon.enforcementconfigurator.entity.user.Role;
import com.nxtlife.efkon.enforcementconfigurator.entity.user.User;
import com.nxtlife.efkon.enforcementconfigurator.view.Response;
import com.nxtlife.efkon.enforcementconfigurator.view.user.security.AuthorityResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.user.security.RoleResponse;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(value = Include.NON_ABSENT)
public class UserResponse implements Response {

	@Schema(description = "Id of user", example = "1")
	private Long id;
	@Schema(description = "Name of user", example = "John")
	private String name;
	@Schema(description = "Active", example = "TRUE")
	private Boolean active;
	@Schema(description = "User's username", example = "john123")
	private String username;
	@Schema(description = "User's email", example = "john@gmail.com")
	private String email;
	@Schema(description = "User's contact number", example = "1234567890")
	private String contactNo;
	@Schema(description = "User's pic url", example = "www.profile-pic.com/john.jpg", nullable = true)
	private String picUrl;
	@Schema(description = "User's organization id", example = "1", nullable = true)
	private Long organizationId;
	@Schema(description = "User's organization info")
	private OrganizationResponse organization;
	@Schema(description = "User's roles info")
	private Set<RoleResponse> roles;
	@Schema(description = "User's authorities info")
	private Set<AuthorityResponse> authorities;

	public UserResponse(Long id, String name, Boolean active, String username, String email, String contactNo,
			String picUrl, Long organizationId) {
		super();
		this.id = id;
		this.active = active;
		this.name = name;
		this.username = username;
		this.email = email;
		this.contactNo = contactNo;
		this.picUrl = picUrl;
		this.organizationId = organizationId;
	}

	public static UserResponse get(User user) {
		if (user != null) {
			UserResponse response = new UserResponse(user.getId() == null ? user.getUserId() : user.getId(),
					user.getName(), user.getActive(), user.getUsername(), user.getEmail(), user.getContactNo(),
					user.getPicUrl(), null);
			if (user.getAuthorities() != null) {
				response.authorities = new HashSet<>();
				for (Authority authority : user.getAuthorities()) {
					response.authorities.add(new AuthorityResponse(authority));
				}
			}
			if (user.getRoles() != null) {
				response.roles = new HashSet<>();
				for (Role role : user.getRoles()) {
					response.roles.add(RoleResponse.get(role));
				}
			}
			return response;
		}
		return null;
	}

	public Long getId() {
		return mask(id);
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public OrganizationResponse getOrganization() {
		return organization;
	}

	public void setOrganization(OrganizationResponse organization) {
		this.organization = organization;
	}

	public Set<RoleResponse> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleResponse> roles) {
		this.roles = roles;
	}

	public Set<AuthorityResponse> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<AuthorityResponse> authorities) {
		this.authorities = authorities;
	}
}
