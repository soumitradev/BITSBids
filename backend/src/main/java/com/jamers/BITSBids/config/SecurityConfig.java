package com.jamers.BITSBids.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http.authorizeHttpRequests((auth) -> auth.anyRequest().authenticated()).oauth2Login(login -> login.successHandler(
										(request, response, authentication) -> {
											response.sendRedirect("/details");
										})).oauth2Client(Customizer.withDefaults()).logout(logout -> logout.logoutSuccessUrl("/").permitAll()).csrf(
										AbstractHttpConfigurer::disable) // TODO: Remove this line in production
						.exceptionHandling(exceptionHandling -> exceptionHandling.defaultAuthenticationEntryPointFor(
										(request, response, accessDeniedException) -> {
											response.setStatus(401);
										},
										new RequestHeaderRequestMatcher("X-Requested-With", "BITSBids-Frontend")
						)).httpBasic(Customizer.withDefaults()).build();
	}
}
