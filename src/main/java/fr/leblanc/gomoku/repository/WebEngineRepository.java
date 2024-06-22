package fr.leblanc.gomoku.repository;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import fr.leblanc.gomoku.model.CustomProperties;
import fr.leblanc.gomoku.model.GomokuColor;
import fr.leblanc.gomoku.model.Move;
import fr.leblanc.gomoku.web.dto.CheckWinResultDTO;
import fr.leblanc.gomoku.web.dto.GameDTO;
import fr.leblanc.gomoku.web.dto.MoveDTO;

@Repository
public class WebEngineRepository {
	
	private static final Logger logger = LoggerFactory.getLogger(WebEngineRepository.class);
	
	private CustomProperties customProperties;

	public WebEngineRepository(CustomProperties customProperties) {
		super();
		this.customProperties = customProperties;
	}

	public Set<Move> checkWin(GameDTO game) {
		
		String checkWinUrl = customProperties.getEngineUrl() + "/engine/checkWin";
		
		RestTemplate restTemplate = new RestTemplate();
		
		HttpEntity<GameDTO> request = new HttpEntity<>(game);
		
		try {
			ResponseEntity<CheckWinResultDTO> response = restTemplate.exchange(
					checkWinUrl, 
					HttpMethod.POST, 
					request, 
					CheckWinResultDTO.class);
			
			CheckWinResultDTO checkWinResult = response.getBody();
			if (checkWinResult != null && checkWinResult.isWin()) {
				Set<Move> result = new HashSet<>();
				for (MoveDTO move : checkWinResult.winMoves()) {
					result.add(Move.build().color(GomokuColor.GREEN.toNumber()).columnIndex(move.columnIndex()).rowIndex(move.rowIndex()));
				}
				
				return result;
			}
		} catch (RestClientException e) {
			logger.error("Error while computing checkWin : {}", e.getMessage());
		}
		
		return Collections.emptySet();
		
	}

	public Move computeMove(GameDTO game) {
		
		String computeMoveUrl = customProperties.getEngineUrl() + "/engine/computeMove";
		
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<GameDTO> request = new HttpEntity<>(game);
		
		try {
			ResponseEntity<MoveDTO> response = restTemplate.exchange(
					computeMoveUrl, 
					HttpMethod.POST, 
					request, 
					MoveDTO.class);
			
			MoveDTO computedMove = response.getBody();
			if (computedMove != null) {
				return Move.build().color(computedMove.color()).columnIndex(computedMove.columnIndex()).rowIndex(computedMove.rowIndex());
			}
			
			return null;
		} catch (RestClientException e) {
			logger.error("Error while computing move : {}", e.getMessage());
			return null;
		}
		
	}

	public Double computeEvaluation(GameDTO game) {
		
		String computeEvaluationUrl = customProperties.getEngineUrl() + "/engine/computeEvaluation";

		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<GameDTO> request = new HttpEntity<>(game);

		try {
			ResponseEntity<Double> response = restTemplate.exchange(
					computeEvaluationUrl, 
					HttpMethod.POST, 
					request, 
					Double.class);
			
			return response.getBody();
		} catch (RestClientException e) {
			logger.error("Error while computing evaluation : {}", e.getMessage());
			return 0d;
		}
	}

	public void stopComputation(Long gameId) {
		String computeEvaluationUrl = customProperties.getEngineUrl() + "/engine/stop/" + gameId;

		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> request = new HttpEntity<>("");

		try {
			restTemplate.exchange(
					computeEvaluationUrl, 
					HttpMethod.POST, 
					request, 
					Void.class);
			
		} catch (RestClientException e) {
			logger.error("Error while stopping computation {}", e.getMessage());
		}
	}

	public Boolean isComputing(Long id) {
		String isComputingUrl = customProperties.getEngineUrl() + "/engine/isComputing/" + id;
		
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> request = new HttpEntity<>("");

		try {
			return restTemplate.exchange(
					isComputingUrl, 
					HttpMethod.GET, 
					request, 
					Boolean.class).getBody();
			
		} catch (RestClientException e) {
			logger.error("Error while stopping computation : {}", e.getMessage());
		}
		
		return Boolean.FALSE;
	}

	public void clearGame(Long id) {
		String clearGameUrl = customProperties.getEngineUrl() + "/engine/clearGame";
		
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<Long> request = new HttpEntity<>(id);

		try {
			restTemplate.exchange(
					clearGameUrl, 
					HttpMethod.DELETE, 
					request, 
					Void.class);
			
		} catch (RestClientException e) {
			logger.error("Error while deleting game : {}", e.getMessage());
		}
		
	}

}
