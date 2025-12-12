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
                                TicketCategory.builder().name("Login").build());
                TicketCategory generalCategory = categoryRepository.save(
                                TicketCategory.builder().name("General").build());
                TicketCategory bugCategory = categoryRepository.save(
                                TicketCategory.builder().name("Bug").build());
                TicketCategory billingCategory = categoryRepository.save(
                                TicketCategory.builder().name("Facturación").build());
                TicketCategory performanceCategory = categoryRepository.save(
                                TicketCategory.builder().name("Rendimiento").build());
                TicketCategory uiCategory = categoryRepository.save(
                                TicketCategory.builder().name("Interfaz").build());
                TicketCategory securityCategory = categoryRepository.save(
                                TicketCategory.builder().name("Seguridad").build());
                TicketCategory supportCategory = categoryRepository.save(
                                TicketCategory.builder().name("Soporte").build());
                TicketCategory featureCategory = categoryRepository.save(
                                TicketCategory.builder().name("Funcionalidad").build());

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
                                                        .title("Error al recuperar contraseña")
                                                        .description("El enlace de recuperación no funciona")
                                                        .status(TicketStatus.IN_PROGRESS)
                                                        .priority(TicketPriority.MEDIUM)
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
                                                        .title("Problema con notificaciones")
                                                        .description("No recibo alertas en la aplicación")
                                                        .status(TicketStatus.OPEN)
                                                        .priority(TicketPriority.LOW)
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
                                                        .build(),
                                        Ticket.builder()
                                                        .title("Error al guardar cambios")
                                                        .description("Los cambios no se reflejan en la base de datos")
                                                        .status(TicketStatus.OPEN)
                                                        .priority(TicketPriority.HIGH)
                                                        .demo(true)
                                                        .category(bugCategory)
                                                        .build(),

                                        Ticket.builder()
                                                        .title("Cobro duplicado")
                                                        .description("Se me cobró dos veces la misma factura")
                                                        .status(TicketStatus.OPEN)
                                                        .priority(TicketPriority.HIGH)
                                                        .demo(true)
                                                        .category(billingCategory)
                                                        .build(),
                                        Ticket.builder()
                                                        .title("Método de pago rechazado")
                                                        .description("Mi tarjeta no es aceptada")
                                                        .status(TicketStatus.IN_PROGRESS)
                                                        .priority(TicketPriority.MEDIUM)
                                                        .demo(true)
                                                        .category(billingCategory)
                                                        .build(),

                                        Ticket.builder()
                                                        .title("Carga lenta de reportes")
                                                        .description("Los reportes tardan demasiado en generarse")
                                                        .status(TicketStatus.OPEN)
                                                        .priority(TicketPriority.MEDIUM)
                                                        .demo(true)
                                                        .category(performanceCategory)
                                                        .build(),
                                        Ticket.builder()
                                                        .title("Demora en inicio de sesión")
                                                        .description("El login tarda más de 30 segundos")
                                                        .status(TicketStatus.OPEN)
                                                        .priority(TicketPriority.HIGH)
                                                        .demo(true)
                                                        .category(performanceCategory)
                                                        .build(),

                                        Ticket.builder()
                                                        .title("Botón no visible")
                                                        .description("El botón de guardar está oculto en móviles")
                                                        .status(TicketStatus.OPEN)
                                                        .priority(TicketPriority.LOW)
                                                        .demo(true)
                                                        .category(uiCategory)
                                                        .build(),
                                        Ticket.builder()
                                                        .title("Texto cortado")
                                                        .description("Los títulos no se muestran completos en la tabla")
                                                        .status(TicketStatus.RESOLVED)
                                                        .priority(TicketPriority.LOW)
                                                        .demo(true)
                                                        .category(uiCategory)
                                                        .build(),

                                        Ticket.builder()
                                                        .title("Sesión expira demasiado rápido")
                                                        .description("Se cierra la sesión en menos de 5 minutos")
                                                        .status(TicketStatus.OPEN)
                                                        .priority(TicketPriority.MEDIUM)
                                                        .demo(true)
                                                        .category(securityCategory)
                                                        .build(),
                                        Ticket.builder()
                                                        .title("Acceso no autorizado")
                                                        .description("Un usuario sin permisos pudo ver datos restringidos")
                                                        .status(TicketStatus.IN_PROGRESS)
                                                        .priority(TicketPriority.HIGH)
                                                        .demo(true)
                                                        .category(securityCategory)
                                                        .build(),

                                        Ticket.builder()
                                                        .title("No recibo respuesta del chat")
                                                        .description("El chat de soporte no responde")
                                                        .status(TicketStatus.OPEN)
                                                        .priority(TicketPriority.MEDIUM)
                                                        .demo(true)
                                                        .category(supportCategory)
                                                        .build(),
                                        Ticket.builder()
                                                        .title("Tiempo de espera largo")
                                                        .description("El soporte tarda más de 24 horas en contestar")
                                                        .status(TicketStatus.OPEN)
                                                        .priority(TicketPriority.HIGH)
                                                        .demo(true)
                                                        .category(supportCategory)
                                                        .build(),

                                        Ticket.builder()
                                                        .title("Función de exportar no disponible")
                                                        .description("No puedo exportar reportes a Excel")
                                                        .status(TicketStatus.OPEN)
                                                        .priority(TicketPriority.MEDIUM)
                                                        .demo(true)
                                                        .category(featureCategory)
                                                        .build(),
                                        Ticket.builder()
                                                        .title("Filtro avanzado no funciona")
                                                        .description("El filtro por fecha no aplica correctamente")
                                                        .status(TicketStatus.IN_PROGRESS)
                                                        .priority(TicketPriority.HIGH)
                                                        .demo(true)
                                                        .category(featureCategory)
                                                        .build()));
                }

        }
}
