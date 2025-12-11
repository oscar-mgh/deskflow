package com.github.oscarmgh.deskflow.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.github.oscarmgh.deskflow.entities.Ticket;
import com.github.oscarmgh.deskflow.entities.TicketCategory;
import com.github.oscarmgh.deskflow.entities.User;
import com.github.oscarmgh.deskflow.entities.enums.TicketPriority;
import com.github.oscarmgh.deskflow.entities.enums.TicketStatus;
import com.github.oscarmgh.deskflow.entities.enums.UserRole;
import com.github.oscarmgh.deskflow.repositories.TicketCategoryRepository;
import com.github.oscarmgh.deskflow.repositories.TicketRepository;
import com.github.oscarmgh.deskflow.repositories.UserRepository;
import com.github.oscarmgh.deskflow.services.TokenService;

import lombok.RequiredArgsConstructor;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final TicketRepository ticketRepository;
    private final TicketCategoryRepository categoryRepository;

    @Override
    public void run(String... args) {
        // Avoiding duplicate data loading
        if (userRepository.count() > 0) {
            return;
        }

        User guest = User.builder()
                .fullName("Guest User")
                .email("guest@deskflow.com")
                .password("$2a$12$F/vIGCzJf2dRrZjk86kuueuHyYqC1UL9NYHIMsAxzAJ0DNDzQ9sgS")
                .role(UserRole.GUEST)
                .active(true)
                .build();

        User normal = User.builder()
                .fullName("Normal User")
                .email("user@deskflow.com")
                .password("$2a$12$F/vIGCzJf2dRrZjk86kuueuHyYqC1UL9NYHIMsAxzAJ0DNDzQ9sgS")
                .role(UserRole.USER)
                .active(true)
                .build();

        User premium = User.builder()
                .fullName("Premium User")
                .email("premium@deskflow.com")
                .password("$2a$12$F/vIGCzJf2dRrZjk86kuueuHyYqC1UL9NYHIMsAxzAJ0DNDzQ9sgS")
                .role(UserRole.PREMIUM)
                .active(true)
                .build();

        User admin = User.builder()
                .fullName("Admin User")
                .email("admin@deskflow.com")
                .password("$2a$12$XVOxLQ9lyOaYKdgfYCgqnOiqSvfZj6ftpV8B.o71w.gipU4Uleusi")
                .role(UserRole.ADMIN)
                .active(true)
                .build();

        userRepository.saveAll(List.of(guest, normal, premium, admin));

        tokenService.generateToken(normal);
        tokenService.generateToken(premium);
        tokenService.generateToken(admin);

        System.out.println("Users created for DEV profile!");

        TicketCategory loginCategory = categoryRepository.save(
                TicketCategory.builder().name("Login").build()
        );
        TicketCategory generalCategory = categoryRepository.save(
                TicketCategory.builder().name("General").build()
        );
        TicketCategory bugCategory = categoryRepository.save(
                TicketCategory.builder().name("Bug").build()
        );

        if (ticketRepository.count() == 0) {
            ticketRepository.saveAll(List.of(
                Ticket.builder()
                    .title("No puedo iniciar sesión")
                    .description("El sistema rechaza mi contraseña")
                    .status(TicketStatus.OPEN)
                    .priority(TicketPriority.HIGH)
                    .demo(true)
                    .category(loginCategory)
                    .build(),

                Ticket.builder()
                    .title("Factura incorrecta")
                    .description("El monto no coincide")
                    .status(TicketStatus.IN_PROGRESS)
                    .priority(TicketPriority.MEDIUM)
                    .demo(true)
                    .category(generalCategory)
                    .build(),

                Ticket.builder()
                    .title("App lenta")
                    .description("La app tarda en cargar")
                    .status(TicketStatus.RESOLVED)
                    .priority(TicketPriority.LOW)
                    .demo(true)
                    .category(bugCategory)
                    .build()
            ));
        }
    }
}
