
package fr.leblanc.gomoku.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import fr.leblanc.gomoku.service.UserService;
import org.springframework.stereotype.Controller;

@Controller
public class MainController
{
    @Autowired
    private UserService userService;
    
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
    
    @GetMapping({ "/online" })
    public String online(Model model) {
    	if (userService.getCurrentUser().getCurrentOnlineGame() == null) {
    		model.addAttribute("username", userService.getCurrentUser().getEmail());
    		model.addAttribute("connectedUsers", userService.getConnectedUsers());
    		model.addAttribute("challengers", userService.getCurrentChallengers());
    		model.addAttribute("challengeTargets", userService.getCurrentChallengeTargets());
    		return "online";
    	}
    	return "redirect:/game/online";
    }
    
    @GetMapping({ "/settings" })
    public String settings(final Model model) {
        model.addAttribute("settings", userService.getCurrentUser().getSettings());
        return "settings";
    }
}