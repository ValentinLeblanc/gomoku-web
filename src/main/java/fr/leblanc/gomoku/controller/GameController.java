package fr.leblanc.gomoku.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
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
import fr.leblanc.gomoku.web.dto.GameDTO;
import fr.leblanc.gomoku.web.dto.MoveDTO;

@RestController
public class GameController {

	@Autowired
	private GameService gameService;
	
	@GetMapping("/game/{gameTypeString}")
	public ModelAndView displayGame(@PathVariable String gameTypeString, Model model) {
		
		GameType gameType = GameType.valueOf(gameTypeString.toUpperCase());
		
		Game game = gameService.getCurrentGame(gameType);
		
		model.addAttribute("game", game);
		model.addAttribute("evaluation", gameService.computeEvaluation(gameType));
		model.addAttribute("userSettings", game.getBlackPlayer().getSettings());
		model.addAttribute("isComputing", gameService.isComputing(game.getId()));
		model.addAttribute("winningMoves", gameService.getWinningMoves(gameType));
		
		return new ModelAndView("forward:/board");
	}
	
	@PostMapping("/reset-game/{gameType}")
	public RedirectView resetGame(@PathVariable String gameType) {

		gameService.resetGame(GameType.valueOf(gameType.toUpperCase()));

		return new RedirectView("/game/" + gameType);
	}
	
	@PostMapping("/add-move/{gameType}")
	public Move addMove(@PathVariable String gameType, @RequestBody MoveDTO move) {
		return gameService.addMove(GameType.valueOf(gameType.toUpperCase()), move.getColumnIndex(), move.getRowIndex());
	}
	
	@PostMapping("/compute-move/{gameType}")
	public Move computeMove(@PathVariable String gameType) {
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
	
	@PostMapping("/stop/{gameId}")
	public void stopComputation(@PathVariable Long gameId) {
		gameService.stopComputation(gameId);
	}
	
	@GetMapping("/lastMove/{gameType}")
	public Move lastMove(@PathVariable String gameType) {
		return gameService.getLastMove(GameType.valueOf(gameType.toUpperCase()));
	}

	@GetMapping("downloadGame/{gameType}")
	public ResponseEntity<Resource> downloadGame(@PathVariable String gameType) {
		return gameService.downloadGame(GameType.valueOf(gameType.toUpperCase()));
	}
	
	@PostMapping("uploadGame/{gameType}")
	public void uploadGame(@PathVariable String gameType, @RequestBody GameDTO uploadedGame) {
		gameService.uploadGame(GameType.valueOf(gameType.toUpperCase()), uploadedGame);
	}
	
	@GetMapping("isComputing/{gameId}")
	public void isComputing(@PathVariable Long gameId) {
		gameService.isComputing(gameId);
	}
}
