package fr.leblanc.gomoku.controller;

import java.util.Set;

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
import fr.leblanc.gomoku.web.dto.GameDto;
import fr.leblanc.gomoku.web.dto.MoveDto;

@RestController
public class GameController {

	@Autowired
	private GameService gameService;
	
	@GetMapping("/game/{gameType}")
	public ModelAndView displayGame(@PathVariable String gameType, Model model) {
		
		Game game = gameService.getCurrentGame(GameType.valueOf(gameType));
		
		model.addAttribute("game", game);
		
		model.addAttribute("evaluation", gameService.computeEvaluation(GameType.valueOf(gameType)));
		
		model.addAttribute("userSettings", game.getBlackPlayer().getSettings());
		
		return new ModelAndView("forward:/board");
	}
	
	@PostMapping("/reset-game/{gameType}")
	public RedirectView resetGame(@PathVariable String gameType) {

		gameService.resetGame(GameType.valueOf(gameType));

		return new RedirectView("/game/" + gameType);
	}
	
	@PostMapping("/add-move/{gameType}")
	public Set<Move> addMove(@PathVariable String gameType, @RequestBody MoveDto move) {
		return gameService.addMove(GameType.valueOf(gameType), move.getColumnIndex(), move.getRowIndex());
	}
	
	@PostMapping("/compute-move/{gameType}")
	public Set<Move> computeMove(@PathVariable String gameType, @RequestBody GameDto game) {
		return gameService.computeMove(GameType.valueOf(gameType), game);
	}

	@PostMapping("/undo-move/{gameType}")
	public Set<Move> undoMove(@PathVariable String gameType) {
		return gameService.undoMove(GameType.valueOf(gameType));
	}
	
	@PostMapping("/compute-evaluation/{gameType}")
	public Double computeEvaluation(@PathVariable String gameType) {
		return gameService.computeEvaluation(GameType.valueOf(gameType));
	}
	
	@PostMapping("/stop")
	public void stopComputation() {
		gameService.stopComputation();
	}

}
