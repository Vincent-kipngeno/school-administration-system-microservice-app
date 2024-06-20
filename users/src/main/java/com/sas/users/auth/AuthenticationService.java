package com.sas.users.auth;

import com.mongodb.DuplicateKeyException;
import com.sas.exception.dto.EmailAlreadyExistsException;
import com.sas.users.config.JwtService;
import com.sas.clients.users.Role;
import com.sas.users.user.User;
import com.sas.users.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	
	private final UserRepository repository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

	public AuthenticationResponse register(RegisterRequest request) {
		User user = User.builder()
				.firstname(request.getFirstname())
				.lastname(request.getLastname())
				.email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword()))
				.role(Role.STUDENT)
				.build();

		try {
			repository.save(user);
			var jwtToken = jwtService.generateToken(user);
			return AuthenticationResponse.builder()
					.token(jwtToken)
					.build();
		} catch (Exception e) {
			throw new EmailAlreadyExistsException("Email already exists");
		}

	}

	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(
				request.getEmail(), 
				request.getPassword()
			)
		);
		User user = repository.findByEmail(request.getEmail())
				.orElseThrow();
		var jwtToken = jwtService.generateToken(user);
		return AuthenticationResponse.builder()
				.token(jwtToken)
				.build();
	}
}
