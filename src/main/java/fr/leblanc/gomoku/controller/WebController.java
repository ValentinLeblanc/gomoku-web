
package fr.leblanc.gomoku.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import fr.leblanc.gomoku.service.UserService;

@Controller
public class WebController
{
    private UserService userService;
    
    public WebController(UserService userService) {
		super();
		this.userService = userService;
	}

	@GetMapping({ "/login" })
    public String login() {
        return "login";
    }
    
    @GetMapping({ "/" })
    public String home() {
        return "home";
    }
    
    @GetMapping({ "/board" })
    public String board() {
        return "board";
    }
    
    @ModelAttribute("username")
    public String addUsernameToModel() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.getPrincipal() instanceof OidcUser oidcUser) {
			return oidcUser.getPreferredUsername();
		}
		return authentication.getName();
    }
    
    @GetMapping({ "/online" })
    public String online(Model model) {
    	if (userService.getCurrentUser().getCurrentOnlineGame() == null) {
    		return "online";
    	}
    	return "redirect:/game/online";
    }
    
    @GetMapping({ "/settings" })
    public String settings(final Model model) {
        model.addAttribute("settings", userService.getCurrentUser().getSettings());
        return "settings";
    }
    
    @GetMapping({ "/history" })
    public String history(final Model model) {
    	return "history";
    }
}