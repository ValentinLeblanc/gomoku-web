package fr.leblanc.gomoku.service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import fr.leblanc.gomoku.controller.WebSocketController;
import fr.leblanc.gomoku.exception.RegistrationException;
import fr.leblanc.gomoku.model.MessageType;
import fr.leblanc.gomoku.model.Role;
import fr.leblanc.gomoku.model.User;
import fr.leblanc.gomoku.model.WebSocketMessage;
import fr.leblanc.gomoku.repository.UserRepository;
import fr.leblanc.gomoku.web.dto.OnlineUserDTO;
import fr.leblanc.gomoku.web.dto.UserDTO;

@Service
public class UserService implements UserDetailsService {
	
	private UserRepository userRepository;
	
	private PasswordEncoder passwordEncoder;
	
	private SessionRegistry sessionRegistry;
	
	private WebSocketController webSocketController;
	
	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, SessionRegistry sessionRegistry,
			WebSocketController webSocketController) {
		super();
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.sessionRegistry = sessionRegistry;
		this.webSocketController = webSocketController;
	}
	
	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		final User user = this.findUserByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				mapRolesToAuthorities(Collections.emptyList()));
	}

	public List<OnlineUserDTO> getConnectedUsers() {
		return getAllConnectedUsers()
				.stream()
				.map(this::createOnlineUserDTO)
				.toList();
	}

	private OnlineUserDTO createOnlineUserDTO(User u) {
		return new OnlineUserDTO(u.getUsername(), u.getChallengers().contains(getCurrentUser().getUsername())
				, getCurrentUser().getChallengers().contains(u.getUsername()));
	}
	
	@EventListener
    public void onSuccess(AuthenticationSuccessEvent event) {
		webSocketController.sendMessage(WebSocketMessage.build().type(MessageType.USER_CONNECTED).content(event.getAuthentication().getName()));
    }
	
	@EventListener
	public void onLogout(LogoutSuccessEvent event) {
		webSocketController.sendMessage(WebSocketMessage.build().type(MessageType.USER_DISCONNECTED).content(event.getAuthentication().getName()));
	}
	
	public List<String> getChallengeTargets() {
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(userRepository.findAll().iterator(), Spliterator.ORDERED), false)
				.filter(u -> u.getChallengers().contains(getCurrentUser().getUsername()))
				.map(User::getUsername)
				.toList();
	}
	
	public List<User> getAllConnectedUsers() {
		return sessionRegistry.getAllPrincipals()
				.stream()
				.filter(org.springframework.security.core.userdetails.User.class::isInstance)
				.map(org.springframework.security.core.userdetails.User.class::cast)
				.map(u -> findUserByUsername(u.getUsername()))
				.toList();
	}
	
	public List<OnlineUserDTO> getChallengers() {
		return getCurrentUser().getChallengers()
				.stream()
				.filter(u -> !u.equals(getCurrentUser().getUsername()))
				.map(this::findUserByUsername)
				.map(this::createOnlineUserDTO)
				.toList();
	}
	
	public void removeChallenger(String challengerUsername) {
		User currentUser = getCurrentUser();
		currentUser.getChallengers().remove(challengerUsername);
		save(currentUser);
	}
	
	public boolean addChallengerTo(String targetUsername) {
		User targetUser = findUserByUsername(targetUsername);
		User currentUser = getCurrentUser();
		if (!targetUser.getChallengers().contains(currentUser.getUsername())) {
			targetUser.getChallengers().add(currentUser.getUsername());
			save(targetUser);
			return true;
		}
		return false;
	}

	public void removeChallengerFrom(String targetUsername) {
		User targetUser = findUserByUsername(targetUsername);
		String currentUsername = getCurrentUser().getUsername();
		targetUser.getChallengers().remove(currentUsername);
		save(targetUser);
	}

	public User save(final UserDTO registrationDto) {
		final User user = new User(registrationDto.getFirstName(), registrationDto.getLastName(),
				registrationDto.getUsername(), passwordEncoder.encode(registrationDto.getPassword()));
		return userRepository.save(user);
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(final Collection<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList();
	}

	public User findUserByUsername(final String username) {
		return this.userRepository.findByUsername(username);
	}

	public User getCurrentUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof OidcUser oidcUser) {
			return this.findUserByUsername(oidcUser.getPreferredUsername());
		}
		final org.springframework.security.core.userdetails.User springUser = (org.springframework.security.core.userdetails.User) principal;
		return this.findUserByUsername(springUser.getUsername());
	}

	public void registerUser(final UserDTO registrationDto) throws RegistrationException {
		final User user = this.findUserByUsername(registrationDto.getUsername());
		if (user != null) {
			throw new RegistrationException("This username is already used in database.");
		}
		this.save(registrationDto);
	}

	public User save(final User currentUser) {
		return userRepository.save(currentUser);
	}

	public void deleteUserAccount(String username) {

		User user = findUserByUsername(username);

		if (user != null) {
			userRepository.delete(findUserByUsername(username));
		}
	}

}