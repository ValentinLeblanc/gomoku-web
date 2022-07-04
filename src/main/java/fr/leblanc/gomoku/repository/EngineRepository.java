package fr.leblanc.gomoku.repository;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import fr.leblanc.gomoku.model.CustomProperties;
import fr.leblanc.gomoku.model.Game;
import fr.leblanc.gomoku.model.GomokuColor;
import fr.leblanc.gomoku.model.Move;
import fr.leblanc.gomoku.web.dto.CheckWinResult;
import fr.leblanc.gomoku.web.dto.GameDto;
import fr.leblanc.gomoku.web.dto.MoveDto;

@Repository
public class EngineRepository {
	
	@Autowired
	private CustomProperties customProperties;

	public Set<Move> checkWin(Game game) {
		
		String checkWinUrl = customProperties.getEngineUrl() + "/checkWin";
		
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<GameDto> request = new HttpEntity<>(new GameDto(game));
		
		ResponseEntity<CheckWinResult> response = restTemplate.exchange(
				checkWinUrl, 
				HttpMethod.POST, 
				request, 
				CheckWinResult.class);
		
		CheckWinResult checkWinResult = response.getBody();
		
		if (checkWinResult != null && checkWinResult.isWin()) {
			
			Set<Move> result = new HashSet<>();
			for (MoveDto move : checkWinResult.getWinMoves()) {
				result.add(Move.builder().color(GomokuColor.GREEN).columnIndex(move.getColumnIndex()).rowIndex(move.getRowIndex()).build());
			}
			
			return result;
		}
		
		return Collections.emptySet();
		
	}

	public Move computeMove(GameDto game) {
		
		String computeMoveUrl = customProperties.getEngineUrl() + "/computeMove";
		
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<GameDto> request = new HttpEntity<>(game);
		
		ResponseEntity<MoveDto> response = restTemplate.exchange(
				computeMoveUrl, 
				HttpMethod.POST, 
				request, 
				MoveDto.class);
		
		MoveDto computedMove = response.getBody();
		
		return Move.builder().color(GomokuColor.toValue(computedMove.getColor())).columnIndex(computedMove.getColumnIndex()).rowIndex(computedMove.getRowIndex()).build();
			
	}

	public Double computeEvaluation(GameDto game) {
		
		String computeEvaluationUrl = customProperties.getEngineUrl() + "/computeEvaluation";

		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<GameDto> request = new HttpEntity<>(game);

		ResponseEntity<Double> response = restTemplate.exchange(
				computeEvaluationUrl, 
				HttpMethod.POST, 
				request, 
				Double.class);
		
		return response.getBody();
	}

}
