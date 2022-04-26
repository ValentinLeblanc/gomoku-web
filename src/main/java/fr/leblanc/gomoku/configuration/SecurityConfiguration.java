package fr.leblanc.gomoku.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import fr.leblanc.gomoku.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserService userService;

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		final DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService((UserDetailsService) this.userService);
		auth.setPasswordEncoder((PasswordEncoder) new BCryptPasswordEncoder());
		return auth;
	}

	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider((AuthenticationProvider) this.authenticationProvider());
	}

	protected void configure(final HttpSecurity http) throws Exception {
		((HttpSecurity) ((HttpSecurity) ((FormLoginConfigurer) ((HttpSecurity) ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl) ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl) http
				.authorizeRequests().antMatchers(new String[] { "/registration**", "/js/**", "/css/**", "/img/**" }))
						.permitAll().anyRequest()).authenticated().and()).formLogin().loginPage("/login").permitAll())
								.and()).logout().invalidateHttpSession(true).clearAuthentication(true)
										.logoutRequestMatcher((RequestMatcher) new AntPathRequestMatcher("/logout"))
										.logoutSuccessUrl("/login?logout").permitAll().and()).csrf().disable();
	}
}