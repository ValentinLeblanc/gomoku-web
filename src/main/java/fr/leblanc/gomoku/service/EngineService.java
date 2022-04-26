package fr.leblanc.gomoku.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.leblanc.gomoku.model.Game;
import fr.leblanc.gomoku.model.Move;
import fr.leblanc.gomoku.repository.EngineRepository;

@Service
public class EngineService {

	 @Autowired
	 private EngineRepository engineRepository;
	 
	 public Set<Move> checkWin(Game game) {
		 return engineRepository.checkWin(game);
	 }
	 
}
