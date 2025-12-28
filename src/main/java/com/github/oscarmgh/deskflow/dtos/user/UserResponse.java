package com.github.oscarmgh.deskflow.dtos.user;

import com.github.oscarmgh.deskflow.entities.User;
import com.github.oscarmgh.deskflow.entities.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {

	private Long id;
	private String fullName;
	private String email;
	private String company;
	private UserRole role;
	private Boolean active;

	public UserResponse(User user) {
		this.id = user.getId();
		this.fullName = user.getFullName();
		this.email = user.getEmail();
		this.company = user.getCompany();
		this.role = user.getRole();
		this.active = user.getActive();
	}
}
