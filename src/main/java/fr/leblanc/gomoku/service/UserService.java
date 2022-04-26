package fr.leblanc.gomoku.service;

import fr.leblanc.gomoku.controller.RegistrationException;
import fr.leblanc.gomoku.model.User;
import fr.leblanc.gomoku.web.dto.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService
{
    User save(final UserRegistrationDto registrationDto);
    
    User findUserByEmail(final String email);
    
    User getCurrentUser();
    
    void registerUserAccount(final UserRegistrationDto registrationDto) throws RegistrationException;
    
    User save(final User currentUser);
}