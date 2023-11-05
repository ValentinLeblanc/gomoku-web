package fr.leblanc.gomoku.service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

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
	
	@Override
	public List<String> getConnectedUsers() {
		return getAllConnectedUsers().stream().filter(u -> u != getCurrentUser()).map(User::getUsername).toList();
	}
	
	@EventListener
    public void onSuccess(AuthenticationSuccessEvent event) {
		webSocketController.sendMessage(WebSocketMessage.builder().type(MessageType.USER_CONNECTED).content(event.getAuthentication().getName()).build());
    }
	
	@EventListener
	public void onLogout(LogoutSuccessEvent event) {
		webSocketController.sendMessage(WebSocketMessage.builder().type(MessageType.USER_DISCONNECTED).content(event.getAuthentication().getName()).build());
	}
	
	@Override
	public List<String> getChallengeTargets(User user) {
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(userRepository.findAll().iterator(), Spliterator.ORDERED), false)
				.filter(u -> u.getChallengers().contains(user))
				.map(User::getUsername)
				.toList();
	}
	
	private List<User> getAllConnectedUsers() {
		return sessionRegistry.getAllPrincipals()
				.stream()
				.filter(org.springframework.security.core.userdetails.User.class::isInstance)
				.map(org.springframework.security.core.userdetails.User.class::cast)
				.map(u -> findUserByUsername(u.getUsername()))
				.toList();
	}
	
	@Override
	public List<String> getChallengers(User user) {
		return user.getChallengers().stream().map(User::getUsername).toList();
	}
	
	@Override
	public void removeChallenger(String challengerUsername) {
		User currentUser = getCurrentUser();
		currentUser.getChallengers().remove(findUserByUsername(challengerUsername));
		save(currentUser);
	}
	
	@Override
	public boolean addChallengerTo(String targetUsername) {
		User targetUser = findUserByUsername(targetUsername);
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
				registrationDto.getUsername(), passwordEncoder.encode(registrationDto.getPassword()));
		return userRepository.save(user);
	}

	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		final User user = this.findUserByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				mapRolesToAuthorities(Collections.emptyList()));
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(final Collection<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList();
	}

	@Override
	public User findUserByUsername(final String username) {
		return this.userRepository.findByUsername(username);
	}

	@Override
	public User getCurrentUser() {
		final org.springframework.security.core.userdetails.User springUser = (org.springframework.security.core.userdetails.User) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		return this.findUserByUsername(springUser.getUsername());
	}

	@Override
	public void registerUserAccount(final UserRegistrationDTO registrationDto) throws RegistrationException {
		final User user = this.findUserByUsername(registrationDto.getUsername());
		if (user != null) {
			throw new RegistrationException("This username is already used in database.");
		}
		this.save(registrationDto);
	}

	@Override
	public User save(final User currentUser) {
		return userRepository.save(currentUser);
	}

	@Override
	public void deleteUserAccount(String username) {

		User user = findUserByUsername(username);

		if (user != null) {
			userRepository.delete(findUserByUsername(username));
		}
	}
}