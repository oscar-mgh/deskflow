package com.github.oscarmgh.deskflow.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.oscarmgh.deskflow.dtos.auth.LoginRequest;
import com.github.oscarmgh.deskflow.dtos.auth.RegisterRequest;
import com.github.oscarmgh.deskflow.dtos.user.UserResponse;
import com.github.oscarmgh.deskflow.entities.User;
import com.github.oscarmgh.deskflow.services.AuthService;
import com.github.oscarmgh.deskflow.services.TokenService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;
	private final TokenService tokenService;

	@PostMapping("/login")
	public String login(@RequestBody LoginRequest request) {
		return authService.login(request);
	}

	@PostMapping("/register")
	public ResponseEntity<String> register(
			@RequestBody @Valid RegisterRequest request) {

		String token = authService.register(request);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(token);
	}

	@GetMapping("/logout")
	public void logout(@RequestHeader("Authorization") String token) {
		authService.logout(token);
	}

	@GetMapping("/validate")
	public ResponseEntity<?> validate(@RequestHeader("Authorization") String authHeader) {

		String token = authHeader.replace("Bearer ", "");

		boolean valid = tokenService.isValid(token);

		if (!valid) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		User user = tokenService.getUserFromToken(token);
		return ResponseEntity.ok(new UserResponse(user));
	}
}
