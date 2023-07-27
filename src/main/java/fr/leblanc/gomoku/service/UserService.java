package fr.leblanc.gomoku.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import fr.leblanc.gomoku.exception.RegistrationException;
import fr.leblanc.gomoku.model.User;
import fr.leblanc.gomoku.web.dto.UserRegistrationDTO;

public interface UserService extends UserDetailsService
{
    User save(final UserRegistrationDTO registrationDto);
    
    User findUserByEmail(final String email);
    
    User getCurrentUser();
    
    void registerUserAccount(final UserRegistrationDTO registrationDto) throws RegistrationException;
    
    User save(final User currentUser);

	void deleteUserAccount(String username);
}