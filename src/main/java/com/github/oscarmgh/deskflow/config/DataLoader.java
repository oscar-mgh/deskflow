package com.github.oscarmgh.deskflow.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.github.oscarmgh.deskflow.entities.User;
import com.github.oscarmgh.deskflow.entities.enums.UserRole;
import com.github.oscarmgh.deskflow.repositories.UserRepository;
import com.github.oscarmgh.deskflow.services.TokenService;

import lombok.RequiredArgsConstructor;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

	private final UserRepository userRepository;
	private final TokenService tokenService;

	@Override
	public void run(String... args) {
		// Avoiding duplicate data loading
		if (userRepository.count() > 0) {
			return;
		}

		User guest = User.builder()
				.fullName("Guest User")
				.email("guest@deskflow.com")
				.password("DeskflowUser")
				.role(UserRole.GUEST)
				.active(true)
				.build();

		User normal = User.builder()
				.fullName("Normal User")
				.email("user@deskflow.com")
				.password("DeskflowUser")
				.role(UserRole.USER)
				.active(true)
				.build();

		User premium = User.builder()
				.fullName("Premium User")
				.email("premium@deskflow.com")
				.password("DeskflowUser")
				.role(UserRole.PREMIUM)
				.active(true)
				.build();

		User admin = User.builder()
				.fullName("Admin User")
				.email("admin@deskflow.com")
				.password("DeskflowAdmin")
				.role(UserRole.ADMIN)
				.active(true)
				.build();

		userRepository.save(guest);
		userRepository.save(normal);
		userRepository.save(premium);
		userRepository.save(admin);
		
		tokenService.generateToken(normal);
		tokenService.generateToken(premium);
		tokenService.generateToken(admin);

		System.out.println("Users created for DEV profile!");
	}
}