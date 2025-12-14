package com.github.oscarmgh.deskflow.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.github.oscarmgh.deskflow.entities.Comment;
import com.github.oscarmgh.deskflow.entities.Ticket;
import com.github.oscarmgh.deskflow.entities.TicketCategory;
import com.github.oscarmgh.deskflow.entities.User;
import com.github.oscarmgh.deskflow.entities.enums.TicketPriority;
import com.github.oscarmgh.deskflow.entities.enums.TicketStatus;
import com.github.oscarmgh.deskflow.entities.enums.UserRole;
import com.github.oscarmgh.deskflow.repositories.CommentRepository;
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
        private final CommentRepository commentRepository;

        @Override
        public void run(String... args) {

                if (userRepository.count() > 0) {
                        return;
                }

                User normal = User.builder()
                                .fullName("Normal User")
                                .email("user@deskflow.com")
                                .password("$2a$10$3ryoqEbtjZxZdx.YfNpIPeGVn0YDn9lYdeuAQ48gErtLvOMkY4viC")
                                .role(UserRole.USER)
                                .active(true)
                                .build();

                User premium = User.builder()
                                .fullName("Premium User")
                                .email("premium@deskflow.com")
                                .password("$2a$10$09VqrCJOImEiWBxLX8e5YukoHmdEg1j7m96x.jicF04sX9hYPMxYi")
                                .role(UserRole.PREMIUM)
                                .active(true)
                                .build();

                User agent = User.builder()
                                .fullName("Agent User")
                                .email("agent@deskflow.com")
                                .password("$2a$10$EqytgvWVtWYICmg5sIBl4uKt3IuVbfmFs.NYR.d9BMj/VdwjTn4va")
                                .role(UserRole.AGENT)
                                .active(true)
                                .build();

                User admin = User.builder()
                                .fullName("Admin User")
                                .email("admin@deskflow.com")
                                .password("$2a$10$YKv.GUUAlTkRgk7BPRbtkOcoYGuWwshSloNiGMMC3gkuyIApPadAG")
                                .role(UserRole.ADMIN)
                                .active(true)
                                .build();

                userRepository.saveAll(List.of(normal, premium, admin, agent));

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

                List<Ticket> tickets = ticketRepository.saveAll(List.of(
                                Ticket.builder()
                                                .title("No puedo iniciar sesión")
                                                .description("El sistema rechaza mi contraseña")
                                                .status(TicketStatus.OPEN)
                                                .priority(TicketPriority.HIGH)
                                                .category(loginCategory)
                                                .user(normal)
                                                .build(),
                                Ticket.builder()
                                                .title("Error al recuperar contraseña")
                                                .description("El enlace de recuperación no funciona")
                                                .status(TicketStatus.IN_PROGRESS)
                                                .agent(agent)
                                                .priority(TicketPriority.MEDIUM)
                                                .user(premium)
                                                .category(loginCategory)
                                                .build(),

                                Ticket.builder()
                                                .title("Factura incorrecta")
                                                .description("El monto no coincide")
                                                .status(TicketStatus.IN_PROGRESS)
                                                .agent(agent)
                                                .priority(TicketPriority.MEDIUM)
                                                .user(premium)
                                                .category(generalCategory)
                                                .build(),
                                Ticket.builder()
                                                .title("Problema con notificaciones")
                                                .description("No recibo alertas en la aplicación")
                                                .status(TicketStatus.OPEN)
                                                .priority(TicketPriority.LOW)
                                                .category(generalCategory)
                                                .user(normal)
                                                .build(),

                                Ticket.builder()
                                                .title("App lenta")
                                                .description("La app tarda en cargar")
                                                .status(TicketStatus.RESOLVED)
                                                .priority(TicketPriority.LOW)
                                                .category(bugCategory)
                                                .user(normal)
                                                .build(),
                                Ticket.builder()
                                                .title("Error al guardar cambios")
                                                .description("Los cambios no se reflejan en la base de datos")
                                                .status(TicketStatus.OPEN)
                                                .priority(TicketPriority.HIGH)
                                                .category(bugCategory)
                                                .user(normal)
                                                .build(),

                                Ticket.builder()
                                                .title("Cobro duplicado")
                                                .description("Se me cobró dos veces la misma factura")
                                                .status(TicketStatus.OPEN)
                                                .priority(TicketPriority.HIGH)
                                                .category(billingCategory)
                                                .user(normal)
                                                .build(),
                                Ticket.builder()
                                                .title("Método de pago rechazado")
                                                .description("Mi tarjeta no es aceptada")
                                                .status(TicketStatus.IN_PROGRESS)
                                                .agent(agent)
                                                .priority(TicketPriority.MEDIUM)
                                                .user(normal)
                                                .category(billingCategory)
                                                .build(),

                                Ticket.builder()
                                                .title("Carga lenta de reportes")
                                                .description("Los reportes tardan demasiado en generarse")
                                                .status(TicketStatus.OPEN)
                                                .priority(TicketPriority.MEDIUM)
                                                .category(performanceCategory)
                                                .user(premium)
                                                .build(),
                                Ticket.builder()
                                                .title("Demora en inicio de sesión")
                                                .description("El login tarda más de 30 segundos")
                                                .status(TicketStatus.OPEN)
                                                .priority(TicketPriority.HIGH)
                                                .category(performanceCategory)
                                                .user(normal)
                                                .build(),

                                Ticket.builder()
                                                .title("Botón no visible")
                                                .description("El botón de guardar está oculto en móviles")
                                                .status(TicketStatus.OPEN)
                                                .priority(TicketPriority.LOW)
                                                .category(uiCategory)
                                                .user(premium)
                                                .build(),
                                Ticket.builder()
                                                .title("Texto cortado")
                                                .description("Los títulos no se muestran completos en la tabla")
                                                .status(TicketStatus.RESOLVED)
                                                .priority(TicketPriority.LOW)
                                                .category(uiCategory)
                                                .user(premium)
                                                .build(),

                                Ticket.builder()
                                                .title("Sesión expira demasiado rápido")
                                                .description("Se cierra la sesión en menos de 5 minutos")
                                                .status(TicketStatus.OPEN)
                                                .priority(TicketPriority.MEDIUM)
                                                .category(securityCategory)
                                                .user(premium)
                                                .build(),
                                Ticket.builder()
                                                .title("Acceso no autorizado")
                                                .description("Un usuario sin permisos pudo ver datos restringidos")
                                                .status(TicketStatus.IN_PROGRESS)
                                                .agent(agent)
                                                .priority(TicketPriority.HIGH)
                                                .user(premium)
                                                .category(securityCategory)
                                                .build(),

                                Ticket.builder()
                                                .title("No recibo respuesta del chat")
                                                .description("El chat de soporte no responde")
                                                .status(TicketStatus.OPEN)
                                                .priority(TicketPriority.MEDIUM)
                                                .category(supportCategory)
                                                .user(premium)
                                                .build(),
                                Ticket.builder()
                                                .title("Tiempo de espera largo")
                                                .description("El soporte tarda más de 24 horas en contestar")
                                                .status(TicketStatus.OPEN)
                                                .priority(TicketPriority.HIGH)
                                                .category(supportCategory)
                                                .user(premium)
                                                .build(),

                                Ticket.builder()
                                                .title("Función de exportar no disponible")
                                                .description("No puedo exportar reportes a Excel")
                                                .status(TicketStatus.OPEN)
                                                .priority(TicketPriority.MEDIUM)
                                                .category(featureCategory)
                                                .user(premium)
                                                .build(),
                                Ticket.builder()
                                                .title("Filtro avanzado no funciona")
                                                .description("El filtro por fecha no aplica correctamente")
                                                .status(TicketStatus.IN_PROGRESS)
                                                .agent(agent)
                                                .priority(TicketPriority.HIGH)
                                                .user(premium)
                                                .category(featureCategory)
                                                .build()));

                Ticket t14 = tickets.get(13);
                Ticket t18 = tickets.get(17);

                Comment c1 = Comment.builder()
                                .content("Estoy revisando tu caso.")
                                .ticket(t14)
                                .user(agent)
                                .build();

                Comment c2 = Comment.builder()
                                .content("Gracias, quedo atento.")
                                .ticket(t14)
                                .user(normal)
                                .build();

                Comment c3 = Comment.builder()
                                .content("Ya corregí el enlace de recuperación.")
                                .ticket(t18)
                                .user(agent)
                                .build();

                commentRepository.saveAll(List.of(c1, c2, c3));

                System.out.println("Tickets and comments created!");
        }

}
