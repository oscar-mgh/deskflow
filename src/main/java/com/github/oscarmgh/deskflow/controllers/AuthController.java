package com.github.oscarmgh.deskflow.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.github.oscarmgh.deskflow.dtos.auth.AuthResponse;
import com.github.oscarmgh.deskflow.dtos.auth.LoginRequest;
import com.github.oscarmgh.deskflow.dtos.auth.RegisterRequest;
import com.github.oscarmgh.deskflow.services.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;
	// private final TokenService tokenService;

	@PostMapping("/login")
	@ResponseStatus(HttpStatus.CREATED)
	public AuthResponse login(@RequestBody LoginRequest request) {
		return authService.login(request);
	}

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public AuthResponse register(
			@RequestBody @Valid RegisterRequest request) {
		return authService.register(request);
	}

	// @GetMapping("/validate")
	// @ResponseStatus(HttpStatus.OK)
	// public ResponseEntity<?> validate(@RequestHeader("Authorization") String
	// authHeader) {

	// String token = authHeader.replace("Bearer ", "");

	// boolean valid = tokenService.isValid(token);

	// if (!valid) {
	// return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	// }

	// User user = tokenService.getUserFromToken(token);
	// return ResponseEntity.ok(new UserResponse(user));
	// }
}
