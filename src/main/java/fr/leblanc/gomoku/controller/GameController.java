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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import fr.leblanc.gomoku.configuration.GomokuWebConfiguration;
import fr.leblanc.gomoku.model.Game;
import fr.leblanc.gomoku.model.GameType;
import fr.leblanc.gomoku.model.Move;
import fr.leblanc.gomoku.service.GameService;
import fr.leblanc.gomoku.service.UserService;
import fr.leblanc.gomoku.web.dto.GameDTO;
import fr.leblanc.gomoku.web.dto.MoveDTO;

@RestController
@RequestMapping("/game")
public class GameController {

	@Autowired
	private GameService gameService;
	
	@Autowired
	private UserService userService;
	
    @Autowired
    private GomokuWebConfiguration webConfiguration;
	
	@GetMapping("/{gameTypeString}")
	public ModelAndView displayGame(@PathVariable String gameTypeString, Model model) {
		
		GameType gameType = GameType.valueOf(gameTypeString.toUpperCase());
		
		if (gameType == GameType.ONLINE && gameService.getCurrentGame(GameType.ONLINE) == null) {
			return new ModelAndView("redirect:/online");
		}
		
		Game game = gameService.getOrCreateGame(gameType);
		
		model.addAttribute("game", game);
		model.addAttribute("evaluation", gameService.computeEvaluation(gameType));
		model.addAttribute("userSettings", game.getBlackPlayer().getSettings());
		model.addAttribute("isComputing", gameService.isComputing(game.getId()));
		model.addAttribute("winningMoves", gameService.getWinningMoves(gameType));
		model.addAttribute("webSocketEngineUrl", webConfiguration.getEngineUrl() + "/engineMessages");
		model.addAttribute("username", userService.getCurrentUser().getUsername());
		if (gameType == GameType.ONLINE) {
			model.addAttribute("blackPlayer", game.getBlackPlayer().getUsername());
			model.addAttribute("whitePlayer", game.getWhitePlayer().getUsername());
		}
		
		return new ModelAndView("forward:/board");
	}
	
	@PostMapping("/reset/{gameType}")
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
	
	@PostMapping("/redo-move/{gameType}")
	public Set<Move> redoMove(@PathVariable String gameType) {
		return gameService.redoMove(GameType.valueOf(gameType.toUpperCase()));
	}
	
	@PostMapping("/compute-evaluation/{gameType}")
	public Double computeEvaluation(@PathVariable String gameType) {
		return gameService.computeEvaluation(GameType.valueOf(gameType.toUpperCase()));
	}
	
	@PostMapping("/stop/{gameId}")
	public void stopComputation(@PathVariable Long gameId) {
		gameService.stopComputation(gameId);
	}
	
	@GetMapping("/lastMove/{gameType}/{propagate}")
	public Move lastMove(@PathVariable String gameType, @PathVariable boolean propagate) {
		return gameService.getLastMove(GameType.valueOf(gameType.toUpperCase()), propagate);
	}

	@GetMapping("/download/{gameType}")
	public ResponseEntity<Resource> downloadGame(@PathVariable String gameType) {
		return gameService.downloadGame(GameType.valueOf(gameType.toUpperCase()));
	}
	
	@PostMapping("/upload/{gameType}")
	public void uploadGame(@PathVariable String gameType, @RequestBody GameDTO uploadedGame) {
		gameService.uploadGame(GameType.valueOf(gameType.toUpperCase()), uploadedGame);
	}
	
	@GetMapping("/isComputing/{gameId}")
	public void isComputing(@PathVariable Long gameId) {
		gameService.isComputing(gameId);
	}
	
	@PostMapping("/save-history/{gameType}")
	public void saveGame(@PathVariable String gameType) {
		gameService.saveHistoryGame(GameType.valueOf(gameType.toUpperCase()));
	}
	
}
