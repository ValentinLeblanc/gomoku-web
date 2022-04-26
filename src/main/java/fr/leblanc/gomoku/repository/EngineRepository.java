package fr.leblanc.gomoku.repository;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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

@Repository
public class EngineRepository {
	
	@Autowired
	CustomProperties customProperties;

	public Set<Move> checkWin(Game game) {
		String baseApiUrl = customProperties.getEngineUrl();
		
		String checkWinUrl = baseApiUrl + "/checkWin";
		
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> request = new HttpEntity<String>(game.toJSON().toString());
		ResponseEntity<String> response = restTemplate.exchange(
				checkWinUrl, 
				HttpMethod.POST, 
				request, 
				String.class);
		
		JSONObject bodyResponse = new JSONObject(response.getBody());
		
		if (bodyResponse.has("result")) {
			try {
				
				Set<Move> result = new HashSet<Move>();
				
				JSONArray jsonResult = (JSONArray) bodyResponse.get("result");
				
				for (int i = 0; i < jsonResult.length(); i++) {
					
					JSONObject jsonMove = jsonResult.getJSONObject(i);
					
					result.add(Move.builder().color(GomokuColor.RED).columnIndex(jsonMove.getInt("column")).rowIndex(jsonMove.getInt("row")).build());
				}
				
				return result;
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return null;
		
	}

}
