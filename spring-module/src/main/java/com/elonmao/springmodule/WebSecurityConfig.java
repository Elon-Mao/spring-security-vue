package com.elonmao.springmodule;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter,
			UserDataFilter userDataFilter)
			throws Exception {
		return http
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests((requests) -> requests
						.requestMatchers("/persons").hasRole("admin")
						.requestMatchers("/persons/**").hasAnyRole("admin", "user")
						.anyRequest().authenticated())
				.exceptionHandling(exception -> exception.accessDeniedHandler((request,
						response, accessDeniedException) -> {
					response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				}))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.formLogin((form) -> form.permitAll().successHandler((request, response, authentication) -> {
					JwtAuthenticationFilter.setJwt(response, request.getParameter("username"));
				}).failureHandler((request, response, exception) -> {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				}))
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.addFilterAfter(userDataFilter, AuthorizationFilter.class)
				.build();
	}
}
