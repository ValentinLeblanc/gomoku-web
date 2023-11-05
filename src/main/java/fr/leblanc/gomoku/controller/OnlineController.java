package fr.leblanc.gomoku.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fr.leblanc.gomoku.model.MessageType;
import fr.leblanc.gomoku.model.WebSocketMessage;
import fr.leblanc.gomoku.service.OnlineService;
import fr.leblanc.gomoku.service.UserService;
import fr.leblanc.gomoku.web.dto.OnlineUserDTO;

@RestController
@RequestMapping("/online")
public class OnlineController {

	@Autowired
	private OnlineService onlineService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private WebSocketController webSocketController;
	
	@GetMapping("/connected-users")
	public List<OnlineUserDTO> getConnectedUsers() {
		return userService.getConnectedUsers();
	}
	
	@GetMapping("/challengers")
	public List<OnlineUserDTO> getChallengers() {
		return userService.getChallengers();
	}
	
	@GetMapping("/challenge-targets")
	public List<String> getChallengeTargets() {
		return userService.getChallengeTargets();
	}

	@PostMapping("/challenge/{targetUsername}")
	public void challenge(@PathVariable String targetUsername) {
		if (onlineService.challenge(targetUsername)) {
			String newChallengerInfo = userService.getCurrentUser().getUsername() + ">" + targetUsername;
			webSocketController.sendMessage(WebSocketMessage.builder().type(MessageType.NEW_CHALLENGER).content(newChallengerInfo).build());
		}
	}
	
	@PostMapping("/accept/{challengerUsername}")
	public void accept(@PathVariable String challengerUsername, RedirectAttributes redirectAttributes) {
		onlineService.acceptChallenge(challengerUsername);
		String acceptChallengeInfo = String.join(",", challengerUsername, userService.getCurrentUser().getUsername());
		webSocketController.sendMessage(WebSocketMessage.builder().type(MessageType.CHALLENGE_ACCEPTED).content(acceptChallengeInfo).build());
	}
	
	@PostMapping("/decline/{challengerUsername}")
	public void decline(@PathVariable String challengerUsername) {
		onlineService.declineChallenge(challengerUsername);
		String declineChallengeInfo = challengerUsername + ">" + userService.getCurrentUser().getUsername();
		webSocketController.sendMessage(WebSocketMessage.builder().type(MessageType.CHALLENGE_DECLINED).content(declineChallengeInfo).build());
	}
	
	
}
