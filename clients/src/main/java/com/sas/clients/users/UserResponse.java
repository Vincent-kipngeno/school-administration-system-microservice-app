package com.sas.clients.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
	private String id;
	private String firstname;
	private String lastname;
	private String email;
	private Role role;
}
