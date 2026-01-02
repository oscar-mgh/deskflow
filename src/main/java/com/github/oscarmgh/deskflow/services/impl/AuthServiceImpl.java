package com.github.oscarmgh.deskflow.services.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.oscarmgh.deskflow.dtos.auth.AuthResponse;
import com.github.oscarmgh.deskflow.dtos.auth.LoginRequest;
import com.github.oscarmgh.deskflow.dtos.auth.RegisterRequest;
import com.github.oscarmgh.deskflow.entities.User;
import com.github.oscarmgh.deskflow.entities.enums.UserRole;
import com.github.oscarmgh.deskflow.exceptions.auth.EmailExistsException;
import com.github.oscarmgh.deskflow.exceptions.auth.InactiveUserException;
import com.github.oscarmgh.deskflow.exceptions.auth.InvalidCredentialsException;
import com.github.oscarmgh.deskflow.exceptions.tickets.ResourceNotFoundException;
import com.github.oscarmgh.deskflow.repositories.UserRepository;
import com.github.oscarmgh.deskflow.services.AuthService;
import com.github.oscarmgh.deskflow.services.TokenService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;
	private final TokenService tokenService;
	private final PasswordEncoder passwordEncoder;

	public AuthResponse login(LoginRequest request) {

		User user = userRepository.findByEmail(request.getEmail()).orElse(null);

		if (user == null) {
			throw new ResourceNotFoundException("User", request.getEmail());
		}

		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new InvalidCredentialsException();
		}

		if (!user.getActive()) {
			throw new InactiveUserException();
		}

		return AuthResponse.builder().token(tokenService.generateToken(user)).build();
	}

	@Transactional
	public AuthResponse register(RegisterRequest request) {

		if (userRepository.findByEmail(request.getEmail()).isPresent()) {
			throw new EmailExistsException();
		}

		User user = User.builder()
				.fullName(request.getFullName())
				.email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword()))
				.role(UserRole.USER)
				.active(true)
				.company(request.getCompany())
				.build();

		user = userRepository.save(user);

		return AuthResponse.builder().token(tokenService.generateToken(user)).build();
	}
}
