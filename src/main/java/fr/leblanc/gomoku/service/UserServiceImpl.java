package fr.leblanc.gomoku.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import fr.leblanc.gomoku.controller.WebSocketController;
import fr.leblanc.gomoku.exception.RegistrationException;
import fr.leblanc.gomoku.model.MessageType;
import fr.leblanc.gomoku.model.Role;
import fr.leblanc.gomoku.model.User;
import fr.leblanc.gomoku.model.WebSocketMessage;
import fr.leblanc.gomoku.repository.UserRepository;
import fr.leblanc.gomoku.web.dto.UserRegistrationDTO;

@Service
public class UserServiceImpl implements UserService {
	
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private SessionRegistry sessionRegistry;
	
	@Autowired
	private WebSocketController webSocketController;
	
	public UserServiceImpl(final UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@EventListener
    public void onSuccess(AuthenticationSuccessEvent event) {
		webSocketController.sendMessage(WebSocketMessage.builder().type(MessageType.CONNECTED_USER).content(event.getAuthentication().getName()).build());
    }
	
	@EventListener
	public void onLogout(LogoutSuccessEvent event) {
		webSocketController.sendMessage(WebSocketMessage.builder().type(MessageType.DISCONNECTED_USER).content(event.getAuthentication().getName()).build());
	}
	
	@Override
	public List<String> getCurrentChallengeTargets() {
		return getConnectedUsers().stream().filter(u -> findUserByEmail(u).getChallengers().contains(getCurrentUser())).toList();
	}
	
	@Override
	public List<String> getConnectedUsers() {
		List<String> connectedUsers = new ArrayList<>();
		String currentUsername = getCurrentUser().getEmail();
		connectedUsers.add(currentUsername);
		connectedUsers.addAll(sessionRegistry.getAllPrincipals()
				.stream()
				.filter(p -> p instanceof org.springframework.security.core.userdetails.User)
				.map(p -> ((org.springframework.security.core.userdetails.User) p).getUsername())
				.filter(u -> !u.equals(currentUsername))
				.toList());
		return connectedUsers;
	}
	
	@Override
	public List<String> getCurrentChallengers() {
		return getCurrentUser().getChallengers().stream().map(User::getEmail).toList();
	}
	
	@Override
	public void removeChallenger(String challengerUsername) {
		User currentUser = getCurrentUser();
		currentUser.getChallengers().remove(findUserByEmail(challengerUsername));
		save(currentUser);
	}
	
	@Override
	public boolean addChallengerTo(String targetUsername) {
		User targetUser = findUserByEmail(targetUsername);
		User currentUser = getCurrentUser();
		if (!targetUser.getChallengers().contains(currentUser)) {
			targetUser.getChallengers().add(currentUser);
			save(targetUser);
			return true;
		}
		return false;
	}

	@Override
	public User save(final UserRegistrationDTO registrationDto) {
		final User user = new User(registrationDto.getFirstName(), registrationDto.getLastName(),
				registrationDto.getEmail(), passwordEncoder.encode(registrationDto.getPassword()),
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