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
import fr.leblanc.gomoku.web.dto.UserDTO;

@Controller
@RequestMapping({ "/registration" })
public class UserController {
	private UserService userService;

	public UserController(final UserService userService) {
		this.userService = userService;
	}
	
	@ModelAttribute("user")
	public UserDTO userRegistrationDto() {
		return new UserDTO(null, null, null, null);
	}

	@GetMapping
	public String showRegistrationForm() {
		return "registration";
	}

	@PostMapping
	public String registerUserAccount(@ModelAttribute("user") final UserDTO user) {
		try {
			this.userService.registerUser(user);
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