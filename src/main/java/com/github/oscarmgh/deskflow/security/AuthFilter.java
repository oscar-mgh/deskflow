package com.github.oscarmgh.deskflow.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.github.oscarmgh.deskflow.entities.User;
import com.github.oscarmgh.deskflow.services.TokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

	private final TokenService tokenService;
	private final AntPathMatcher matcher = new AntPathMatcher();

	private static final List<String> PUBLIC_PATHS = Arrays.asList(
			"/auth/**",
			"/public/**",
			"/actuator/**",
			"/swagger-ui/**",
			"/v3/api-docs/**");

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getRequestURI();
		return PUBLIC_PATHS.stream().anyMatch(p -> matcher.match(p, path));
	}

	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {

		String header = request.getHeader("Authorization");

		if (header == null || !header.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		String tokenValue = header.substring(7);
		User user = tokenService.getUserFromToken(tokenValue);

		if (user != null) {
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
					user,
					null,
					user.getAuthorities());

			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		filterChain.doFilter(request, response);
	}
}
