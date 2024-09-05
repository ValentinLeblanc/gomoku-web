package fr.leblanc.gomoku.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Profile("sso")
public class SsoSecurityConfiguration {

	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
		return http
		.oauth2Client(auth -> {})
		.oauth2Login(auth -> auth
				.tokenEndpoint(t -> {}))
		.oauth2Login(auth -> auth
				.userInfoEndpoint(t -> {}))
		.sessionManagement(auth -> auth
				.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
		.authorizeHttpRequests(auth -> auth
				.requestMatchers("/unauthenticated", "/oauth2/**", "/login/**").permitAll()
				.anyRequest().fullyAuthenticated())
		.logout(auth -> auth
				.logoutSuccessUrl("http://gomoku-ids:8282/realms/external/protocol/openid-connect/logout"))
		.build();

	}
}