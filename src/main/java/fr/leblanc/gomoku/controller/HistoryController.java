package fr.leblanc.gomoku.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.leblanc.gomoku.web.dto.HistoryGameDTO;

@RestController
@RequestMapping("/history")
public class HistoryController {
	
	@Autowired
	private HistoryService historyService;
	
	@GetMapping("/get/{username}")
	public List<HistoryGameDTO> getGameHistoryData(@PathVariable String username) {
		return historyService.getUserHistory(username);
	}
	
	@PostMapping("/save/{gameId}")
	public void save(@PathVariable Long gameId) {
		historyService.saveGameInHistory(gameId);
	}
	
	@PostMapping("/view/{gameId}")
	public void viewGameFromHistory(@PathVariable Long gameId) {
		historyService.viewGameFromHistory(gameId);
	}
	
	@DeleteMapping("/delete/{gameId}")
	public void delete(@PathVariable Long gameId) {
		historyService.deleteGameFromHistory(gameId);
	}

}
