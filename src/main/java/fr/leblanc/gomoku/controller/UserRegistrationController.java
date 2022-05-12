package fr.leblanc.gomoku.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.leblanc.gomoku.exception.RegistrationException;
import fr.leblanc.gomoku.service.UserService;
import fr.leblanc.gomoku.web.dto.UserRegistrationDto;

@Controller
@RequestMapping({ "/registration" })
public class UserRegistrationController {
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
		} catch (RegistrationException e) {
			return "redirect:/registration?failure";
		}
		return "redirect:/login";
	}

	@DeleteMapping
	public String deleteUserAccount(@RequestBody String username) {
		userService.deleteUserAccount(username);
		
		return "redirect:/";
	}

}