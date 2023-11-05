package fr.leblanc.gomoku.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fr.leblanc.gomoku.service.UserService;
import fr.leblanc.gomoku.web.dto.HistoryGameDTO;

@RestController
@RequestMapping("/history")
public class GameHistoryController {
	
	@Autowired
	private GameHistoryService gameHistoryService;
	
	@Autowired
    private UserService userService;
    
	@GetMapping("/data")
	@ResponseBody
	public List<HistoryGameDTO> getGameHistoryData() {
		return gameHistoryService.getHistoryGames(userService.getCurrentUser());
	}

}
