package fr.leblanc.gomoku.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.leblanc.gomoku.model.Move;
import fr.leblanc.gomoku.repository.WebEngineRepository;
import fr.leblanc.gomoku.web.dto.EvaluationDTO;
import fr.leblanc.gomoku.web.dto.GameDTO;

@Service
public class EngineService {

	 @Autowired
	 private WebEngineRepository engineRepository;
	 
	 public Set<Move> checkWin(GameDTO game) {
		 return engineRepository.checkWin(game);
	 }

	public Move computeMove(GameDTO game) {
		return engineRepository.computeMove(game);
	}

	public EvaluationDTO computeEvaluation(GameDTO game) {
		return engineRepository.computeEvaluation(game);
	}

	public void stopComputation(Long gameId) {
		engineRepository.stopComputation(gameId);
	}

	public Boolean isComputing(Long id) {
		return engineRepository.isComputing(id);
	}

	public void clearGame(Long id) {
		engineRepository.clearGame(id);
	}
	
}
