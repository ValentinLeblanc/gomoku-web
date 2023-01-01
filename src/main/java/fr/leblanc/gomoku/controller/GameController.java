package fr.leblanc.gomoku.controller;

import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import fr.leblanc.gomoku.model.Game;
import fr.leblanc.gomoku.model.GameType;
import fr.leblanc.gomoku.model.Move;
import fr.leblanc.gomoku.service.GameService;
import fr.leblanc.gomoku.web.dto.MoveDto;

@RestController
public class GameController {

	@Autowired
	private GameService gameService;
	
	@GetMapping("/game/{gameType}")
	public ModelAndView displayGame(@PathVariable String gameType, Model model) {
		
		Game game = gameService.getCurrentGame(GameType.valueOf(gameType.toUpperCase()));
		
		model.addAttribute("game", game);
		
		model.addAttribute("evaluation", gameService.computeEvaluation(GameType.valueOf(gameType.toUpperCase())));
		
		model.addAttribute("userSettings", game.getBlackPlayer().getSettings());
		
		return new ModelAndView("forward:/board");
	}
	
	@PostMapping("/reset-game/{gameType}")
	public RedirectView resetGame(@PathVariable String gameType) {

		gameService.resetGame(GameType.valueOf(gameType.toUpperCase()));

		return new RedirectView("/game/" + gameType);
	}
	
	@PostMapping("/add-move/{gameType}")
	public Set<Move> addMove(@PathVariable String gameType, @RequestBody MoveDto move) {
		return gameService.addMove(GameType.valueOf(gameType.toUpperCase()), move.getColumnIndex(), move.getRowIndex());
	}
	
	@PostMapping("/compute-move/{gameType}")
	public Set<Move> computeMove(@PathVariable String gameType) {
		return gameService.computeMove(GameType.valueOf(gameType.toUpperCase()));
	}

	@PostMapping("/undo-move/{gameType}")
	public Set<Move> undoMove(@PathVariable String gameType) {
		return gameService.undoMove(GameType.valueOf(gameType.toUpperCase()));
	}
	
	@PostMapping("/compute-evaluation/{gameType}")
	public Double computeEvaluation(@PathVariable String gameType) {
		return gameService.computeEvaluation(GameType.valueOf(gameType.toUpperCase()));
	}
	
	@PostMapping("/stop")
	public void stopComputation() {
		gameService.stopComputation();
	}
	
	@GetMapping("/lastMove/{gameType}")
	public Move lastMove(@PathVariable String gameType) {
		return gameService.getLastMove(GameType.valueOf(gameType.toUpperCase()));
	}

	@GetMapping("downloadGame/{gameType}")
	public void downloadGame(@PathVariable String gameType, HttpServletResponse response) {
		gameService.downloadGame(GameType.valueOf(gameType.toUpperCase()), null);
	}
}
