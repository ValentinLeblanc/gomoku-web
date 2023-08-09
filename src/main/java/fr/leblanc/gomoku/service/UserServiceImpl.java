package fr.leblanc.gomoku.service;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import fr.leblanc.gomoku.exception.RegistrationException;
import fr.leblanc.gomoku.model.Role;
import fr.leblanc.gomoku.model.User;
import fr.leblanc.gomoku.repository.UserRepository;
import fr.leblanc.gomoku.web.dto.UserRegistrationDTO;

@Service
public class UserServiceImpl implements UserService {
	private UserRepository userRepository;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public UserServiceImpl(final UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public User save(final UserRegistrationDTO registrationDto) {
		final User user = new User(registrationDto.getFirstName(), registrationDto.getLastName(),
				registrationDto.getEmail(), this.passwordEncoder().encode(registrationDto.getPassword()),
				Arrays.asList(new Role("ROLE_USER")));
		return userRepository.save(user);
	}

	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		final User user = this.findUserByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				mapRolesToAuthorities(user.getRoles()));
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(final Collection<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList();
	}

	@Override
	public User findUserByEmail(final String email) {
		return this.userRepository.findByEmail(email);
	}

	@Override
	public User getCurrentUser() {
		final org.springframework.security.core.userdetails.User springUser = (org.springframework.security.core.userdetails.User) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		return this.findUserByEmail(springUser.getUsername());
	}

	@Override
	public void registerUserAccount(final UserRegistrationDTO registrationDto) throws RegistrationException {
		final User user = this.findUserByEmail(registrationDto.getEmail());
		if (user != null) {
			throw new RegistrationException("This email is already used in database.");
		}
		this.save(registrationDto);
	}

	@Override
	public User save(final User currentUser) {
		return userRepository.save(currentUser);
	}

	@Override
	public void deleteUserAccount(String username) {

		User user = findUserByEmail(username);

		if (user != null) {
			userRepository.delete(findUserByEmail(username));
		}
	}
}