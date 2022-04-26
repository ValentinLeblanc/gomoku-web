package fr.leblanc.gomoku.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import fr.leblanc.gomoku.web.dto.UserRegistrationDto;
import fr.leblanc.gomoku.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/registration" })
public class UserRegistrationController
{
    private UserService userService;
    
    public UserRegistrationController(final UserService userService) {
        this.userService = userService;
    }
    
    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }
    
    @GetMapping
    public String showRegistrationForm() {
        return "registration";
    }
    
    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") final UserRegistrationDto registrationDto) {
        try {
            this.userService.registerUserAccount(registrationDto);
        }
        catch (RegistrationException e) {
            return "redirect:/registration?failure";
        }
        return "redirect:/login";
    }
}