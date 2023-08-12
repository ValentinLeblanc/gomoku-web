
package fr.leblanc.gomoku.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import fr.leblanc.gomoku.service.UserService;

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
    
    @ModelAttribute("username")
    public String addUsernameToModel() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
    
    @GetMapping({ "/online" })
    public String online(Model model) {
    	if (userService.getCurrentUser().getCurrentOnlineGame() == null) {
    		model.addAttribute("connectedUsers", userService.getConnectedUsers());
    		model.addAttribute("challengers", userService.getChallengers(userService.getCurrentUser()));
    		model.addAttribute("challengeTargets", userService.getChallengeTargets(userService.getCurrentUser()));
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