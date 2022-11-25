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
		this.displayAnalysis = settings.getDisplayAnalysis();
		this.strikeEnabled = settings.getStrikeEnabled();
		this.minMaxDepth = settings.getStrikeDepth();
		this.evaluationDepth = settings.getEvaluationDepth();
		this.strikeDepth = settings.getStrikeDepth();
	}

	private Boolean displayAnalysis;
	
	private Boolean strikeEnabled;
	
	private int minMaxDepth;
	
	private int strikeDepth;
	
	private int evaluationDepth;

}
