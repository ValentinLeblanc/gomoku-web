package fr.leblanc.gomoku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

import fr.leblanc.gomoku.model.User;
import fr.leblanc.gomoku.web.dto.UserDTO;

@Component
public class AuthenticationService {
	
	@Autowired
	private UserService userService;
	
	@EventListener
    public void onSuccess(AuthenticationSuccessEvent success) {
		Object principal = success.getAuthentication().getPrincipal();
		if (principal instanceof OidcUser oidcUser) {
			User externalUser = userService.findUserByUsername(oidcUser.getPreferredUsername());
			if (externalUser == null) {
				userService.save(new UserDTO(oidcUser.getGivenName(), oidcUser.getFamilyName(), oidcUser.getPreferredUsername(), "XXX"));
			}
		}
    }

}