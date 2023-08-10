package fr.leblanc.gomoku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OnlineService {

	@Autowired
	private UserService userService;
	
	@Autowired
	private GameService gameService;
	
	public boolean challenge(String targetUsername) {
		return userService.addChallengerTo(targetUsername);
	}

	public void acceptChallenge(String challengerUsername) {
		userService.removeChallenger(challengerUsername);
		String currentUsername = userService.getCurrentUser().getEmail();
		gameService.createOnlineGame(currentUsername, challengerUsername);
	}

	public void declineChallenge(String challengerUsername) {
		userService.removeChallenger(challengerUsername);
	}
	
}