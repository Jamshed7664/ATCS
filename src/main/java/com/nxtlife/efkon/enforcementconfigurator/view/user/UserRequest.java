package com.nxtlife.efkon.enforcementconfigurator.view.user;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.nxtlife.efkon.enforcementconfigurator.entity.user.User;
import com.nxtlife.efkon.enforcementconfigurator.view.Request;

import io.swagger.v3.oas.annotations.media.Schema;

public class UserRequest implements Request {

	@Schema(description = "Name of the user which cannot be null", example = "Admin", required = true)
	@NotNull(message = "User's name can't be null")
	private String name;

	@Schema(description = "Name of the user which cannot be null", example = "Admin", required = true)
	@NotNull(message = "User's name can't be null")
	@Pattern(regexp = "^[@A-Za-z0-9_]{3,20}$", message = "username should contains only alphabets/digit/@ and length should be in between 4 to 20")
	private String username;

	@Schema(description = "Email of the user which cannot be empty", example = "admin@gmail.com", required = true)
	@NotEmpty(message = "User's email can't be empty")
	@Email(message = "Email pattern isn't correct")
	private String email;

	@Schema(description = "Contact of the user", example = "1234567890")
	@Size(min = 10, max = 10)
	@Pattern(regexp = "^[0-9]*$", message = "Contact no should contain only digit")
	private String contactNo;

	@Schema(description = "List of the role ids which cannot be null", example = "[2,3]", required = true)
	@NotEmpty(message = "Role ids can't be null or empty")
	private Set<Long> roleIds;

	public String getName() {
		return name;
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public String getContactNo() {
		return contactNo;
	}

	public Set<Long> getRoleIds() {
		if(roleIds!=null){
			return roleIds.stream().map(id->unmask(id)).collect(Collectors.toSet());
		}
		return roleIds;
	}

	public User toEntity() {
		User user = new User();
		user.setName(name);
		user.setEmail(email);
		user.setContactNo(contactNo);
		user.setUsername(username);
		return user;
	}
}
