package fr.leblanc.gomoku.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import fr.leblanc.gomoku.model.Game;
import fr.leblanc.gomoku.model.Move;
import fr.leblanc.gomoku.service.GameService;
import fr.leblanc.gomoku.service.OnlineBoardService;
import fr.leblanc.gomoku.web.dto.GameDto;
import fr.leblanc.gomoku.web.dto.MoveDto;

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
		
		model.addAttribute("evaluation", gameService.computeEvaluation(new GameDto(localGame)));
		
		return new ModelAndView("forward:/board");
	}

	@PostMapping("/reset-game")
	public RedirectView resetLocalGame() {

		gameService.resetLocalGame();

		return new RedirectView("/local-game");
	}
	
	@PostMapping("/ai-game")
	public RedirectView AIGame() {
		
		gameService.AIGame();
		
		return new RedirectView("forward:/board");
	}

	@PostMapping("/add-move")
	public Set<Move> addMove(@RequestBody MoveDto move) {
		return gameService.addMove(move.getColumnIndex(), move.getRowIndex());
	}
	
	@PostMapping("/compute-move")
	public Set<Move> computeMove(@RequestBody GameDto game) {
		return gameService.computeMove(game);
	}

	@PostMapping("/undo-move")
	public Set<Move> undoMove() {
		return gameService.undoMove();
	}
	
	@PostMapping("/compute-evaluation")
	public Double computeEvaluation(@RequestBody GameDto game) {
		return gameService.computeEvaluation(game);
	}

}
