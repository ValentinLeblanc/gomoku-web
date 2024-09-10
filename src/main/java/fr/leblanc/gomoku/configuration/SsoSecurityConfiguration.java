package fr.leblanc.gomoku.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@EnableWebSecurity
@Profile("sso")
public class SsoSecurityConfiguration {
	
	@Bean
	public SecurityFilterChain configure(HttpSecurity http, SessionRegistry sessionRegistry, ClientRegistrationRepository clientRegistrationRepository) throws Exception {
		return http
				.oauth2Client(Customizer.withDefaults())
				.oauth2Login(Customizer.withDefaults())
				.sessionManagement(auth -> auth
						.maximumSessions(1)
						.sessionRegistry(sessionRegistry))
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/registration**", "/js/**", "/css/**", "/img/**", "/unauthenticated", "/oauth2/**", "/login/**").permitAll()
						.anyRequest().authenticated())
				.logout(auth -> auth
						.invalidateHttpSession(true)
						.clearAuthentication(true)
						.logoutSuccessHandler(oidcLogoutSuccessHandler(clientRegistrationRepository))
						)
				.build();

	}
	
	private LogoutSuccessHandler oidcLogoutSuccessHandler(ClientRegistrationRepository clientRegistrationRepository) {
		OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler =
				new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);

		// Sets the location that the End-User's User Agent will be redirected to
		// after the logout has been performed at the Provider
		oidcLogoutSuccessHandler.setPostLogoutRedirectUri("http://localhost:8080");

		return oidcLogoutSuccessHandler;
	}
}