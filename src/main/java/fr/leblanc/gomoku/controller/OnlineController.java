package fr.leblanc.gomoku.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fr.leblanc.gomoku.model.MessageType;
import fr.leblanc.gomoku.model.WebSocketMessage;
import fr.leblanc.gomoku.service.OnlineService;
import fr.leblanc.gomoku.service.UserService;

@RestController
public class OnlineController {

	@Autowired
	private OnlineService onlineService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private WebSocketController webSocketController;

	@PostMapping("/challenge/{targetUsername}")
	public void challenge(@PathVariable String targetUsername) {
		if (onlineService.challenge(targetUsername)) {
			String challengeInfo = userService.getCurrentUser().getEmail() + "=>" + targetUsername;
			webSocketController.sendMessage(WebSocketMessage.builder().type(MessageType.NEW_CHALLENGER).content(challengeInfo).build());
		}
	}
	
	@PostMapping("/accept/{challengerUsername}")
	public void accept(@PathVariable String challengerUsername, RedirectAttributes redirectAttributes) {
		onlineService.acceptChallenge(challengerUsername);
		String currentUsername = userService.getCurrentUser().getEmail();
		webSocketController.sendMessage(WebSocketMessage.builder().type(MessageType.CHALLENGE_ACCEPTED).content(String.join(",", challengerUsername, currentUsername)).build());
	}
	
	@PostMapping("/decline/{challengerUsername}")
	public void decline(@PathVariable String challengerUsername) {
		onlineService.declineChallenge(challengerUsername);
		webSocketController.sendMessage(WebSocketMessage.builder().type(MessageType.REMOVE_CHALLENGER).content(challengerUsername).build());
	}
}
