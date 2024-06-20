package com.sas.users.auth;

import com.sas.clients.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

	private final AuthenticationService service;
	@PostMapping("/register")
	public ResponseEntity<ResponseDTO<AuthenticationResponse>> register(
			@RequestBody RegisterRequest request
	) {
		AuthenticationResponse authResponse = service.register(request);
		ResponseDTO<AuthenticationResponse> response = ResponseDTO.<AuthenticationResponse>builder()
				.statusCode(HttpStatus.OK.value())
				.status(HttpStatus.OK.getReasonPhrase())
				.message("Auth Token.")
				.timestamp(System.currentTimeMillis())
				.body(authResponse)
				.build();
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/authenticate")
	public ResponseEntity<ResponseDTO<AuthenticationResponse>> authenticate(
			@RequestBody AuthenticationRequest request
	){
		ResponseDTO<AuthenticationResponse> response = ResponseDTO.<AuthenticationResponse>builder()
				.statusCode(HttpStatus.OK.value())
				.status(HttpStatus.OK.getReasonPhrase())
				.message("Auth Token.")
				.timestamp(System.currentTimeMillis())
				.body(service.authenticate(request))
				.build();
		return ResponseEntity.ok(response);
	}
}
