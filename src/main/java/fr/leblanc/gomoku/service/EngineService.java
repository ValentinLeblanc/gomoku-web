package fr.leblanc.gomoku.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.leblanc.gomoku.model.Game;
import fr.leblanc.gomoku.model.Move;
import fr.leblanc.gomoku.repository.WebEngineRepository;
import fr.leblanc.gomoku.web.dto.GameDTO;

@Service
public class EngineService {

	 @Autowired
	 private WebEngineRepository engineRepository;
	 
	 public Set<Move> checkWin(Game game) {
		 return engineRepository.checkWin(game);
	 }

	public Move computeMove(GameDTO game) {
		return engineRepository.computeMove(game);
	}

	public Double computeEvaluation(GameDTO game) {
		return engineRepository.computeEvaluation(game);
	}

	public void stopComputation(Long gameId) {
		engineRepository.stopComputation(gameId);
	}

	public Boolean isComputing(Long id) {
		return engineRepository.isComputing(id);
	}
	
}
