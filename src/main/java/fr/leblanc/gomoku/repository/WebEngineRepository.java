package fr.leblanc.gomoku.repository;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import fr.leblanc.gomoku.model.CustomProperties;
import fr.leblanc.gomoku.model.Game;
import fr.leblanc.gomoku.model.GomokuColor;
import fr.leblanc.gomoku.model.Move;
import fr.leblanc.gomoku.web.dto.CheckWinResult;
import fr.leblanc.gomoku.web.dto.GameDto;
import fr.leblanc.gomoku.web.dto.MoveDto;
import lombok.extern.log4j.Log4j2;

@Repository
@Log4j2
public class WebEngineRepository {
	
	@Autowired
	private CustomProperties customProperties;

	public Set<Move> checkWin(Game game) {
		
		String checkWinUrl = customProperties.getEngineUrl() + "/checkWin";
		
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<GameDto> request = new HttpEntity<>(new GameDto(game));
		
		try {
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
		} catch (RestClientException e) {
			log.error("Error while computing checkWin : " + e.getMessage());
		}
		
		return Collections.emptySet();
		
	}

	public Move computeMove(GameDto game) {
		
		String computeMoveUrl = customProperties.getEngineUrl() + "/computeMove";
		
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<GameDto> request = new HttpEntity<>(game);
		
		try {
			ResponseEntity<MoveDto> response = restTemplate.exchange(
					computeMoveUrl, 
					HttpMethod.POST, 
					request, 
					MoveDto.class);
			
			MoveDto computedMove = response.getBody();
			if (computedMove != null) {
				return Move.builder().color(GomokuColor.toValue(computedMove.getColor())).columnIndex(computedMove.getColumnIndex()).rowIndex(computedMove.getRowIndex()).build();
			}
			
			return null;
		} catch (RestClientException e) {
			log.error("Error while computing move : " + e.getMessage());
			return null;
		}
		
	}

	public Double computeEvaluation(GameDto game) {
		
		String computeEvaluationUrl = customProperties.getEngineUrl() + "/computeEvaluation";

		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<GameDto> request = new HttpEntity<>(game);

		try {
			ResponseEntity<Double> response = restTemplate.exchange(
					computeEvaluationUrl, 
					HttpMethod.POST, 
					request, 
					Double.class);
			
			return response.getBody();
		} catch (RestClientException e) {
			log.error("Error while computing evaluation : " + e.getMessage());
			return 0d;
		}
	}

	public void stopComputation() {
		String computeEvaluationUrl = customProperties.getEngineUrl() + "/stop";

		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> request = new HttpEntity<>("");

		try {
			restTemplate.exchange(
					computeEvaluationUrl, 
					HttpMethod.POST, 
					request, 
					Void.class);
			
		} catch (RestClientException e) {
			log.error("Error while stopping computation : " + e.getMessage());
		}
	}

}
