package fr.leblanc.gomoku.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.leblanc.gomoku.model.Game;
import fr.leblanc.gomoku.model.Move;
import fr.leblanc.gomoku.repository.EngineRepository;
import fr.leblanc.gomoku.web.dto.GameDto;

@Service
public class EngineService {

	 @Autowired
	 private EngineRepository engineRepository;
	 
	 public Set<Move> checkWin(Game game) {
		 return engineRepository.checkWin(game);
	 }

	public Move computeMove(GameDto game) {
		return engineRepository.computeMove(game);
	}

	public Double computeEvaluation(GameDto game) {
		return engineRepository.computeEvaluation(game);
	}
	 
}
