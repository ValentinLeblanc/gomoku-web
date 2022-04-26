package fr.leblanc.gomoku.controller;

import java.util.Set;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import fr.leblanc.gomoku.model.Game;
import fr.leblanc.gomoku.model.GameType;
import fr.leblanc.gomoku.model.Move;
import fr.leblanc.gomoku.service.GameService;
import fr.leblanc.gomoku.service.OnlineBoardService;

@RestController
public class GameController {

	@Autowired
	private GameService gameService;
	
	@Autowired
	private OnlineBoardService onlineBoardService;

	@GetMapping("/online-game")
	public ModelAndView onlineGame(Model model) {
		
		model.addAttribute("onlineBoards", onlineBoardService.findAll());
		
		return new ModelAndView("forward:/online");
	}
	
	@GetMapping("/local-game")
	public ModelAndView localGame(Model model) {
		
		Game localGame = gameService.localGame();
		
		model.addAttribute("game", localGame);
		
		return new ModelAndView("forward:/board");
	}

	@PostMapping("/reset-game")
	public RedirectView resetGame(@RequestBody String body) {

		Game game = gameService.resetGame();

		if (game.getType() == GameType.LOCAL) {
			return new RedirectView("/local-game");
		}

		return null;
	}

	@PostMapping("/add-move")
	public Set<Move> addMove(@RequestBody String body) {
	
		JSONObject moveData = new JSONObject(body);
	
		int columnIndex = moveData.getInt("columnIndex");
		int rowIndex = moveData.getInt("rowIndex");
	
		return gameService.addMove(columnIndex, rowIndex);
	}

	@PostMapping("undo-move")
	public Set<Move> undoMove() {
		return gameService.undoMove();
	}

}
