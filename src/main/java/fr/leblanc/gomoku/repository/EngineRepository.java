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
import fr.leblanc.gomoku.web.dto.CheckWinResultDto;
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
		
		ResponseEntity<CheckWinResultDto> response = restTemplate.exchange(
				checkWinUrl, 
				HttpMethod.POST, 
				request, 
				CheckWinResultDto.class);
		
		CheckWinResultDto checkWinResult = response.getBody();
		
		if (checkWinResult != null && checkWinResult.isWin()) {
			
			Set<Move> result = new HashSet<>();
			for (MoveDto move : checkWinResult.getWinMoves()) {
				result.add(Move.builder().color(GomokuColor.RED).columnIndex(move.getColumnIndex()).rowIndex(move.getRowIndex()).build());
			}
			
			return result;
		}
		
		return Collections.emptySet();
		
	}

}
