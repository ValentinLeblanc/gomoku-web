package fr.leblanc.gomoku.web.dto;

import fr.leblanc.gomoku.model.Settings;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SettingsDto {

	public SettingsDto(Settings settings) {
		this.strikeEnabled = settings.getStrikeEnabled();
		this.minMaxDepth = settings.getMinMaxDepth();
		this.evaluationDepth = settings.getEvaluationDepth();
		this.strikeDepth = settings.getStrikeDepth();
		this.strikeTimeout = settings.getStrikeTimeout();
	}

	private Boolean strikeEnabled;
	
	private int minMaxDepth;
	
	private int strikeDepth;
	
	private int evaluationDepth;
	
	private int strikeTimeout;

}
