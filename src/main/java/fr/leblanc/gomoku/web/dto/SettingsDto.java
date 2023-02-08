package fr.leblanc.gomoku.web.dto;

import fr.leblanc.gomoku.model.Settings;

public class SettingsDto {

	private int boardSize;

	private boolean strikeEnabled;
	
	private boolean displayAnalysis;
	
	public SettingsDto() {
		
	}
	
	public SettingsDto(Settings settings) {
    	setBoardSize(settings.getBoardSize());
    	setDisplayAnalysis(settings.isDisplayAnalysis());
    	setMinMaxDepth(settings.getMinMaxDepth());
    	setMinMaxExtent(settings.getMinMaxExtent());
    	setStrikeDepth(settings.getStrikeDepth());
    	setStrikeTimeout(settings.getStrikeTimeout());
    	setStrikeEnabled(settings.isStrikeEnabled());
	}

	public int getBoardSize() {
		return boardSize;
	}

	public void setBoardSize(int boardSize) {
		this.boardSize = boardSize;
	}

	public boolean isStrikeEnabled() {
		return strikeEnabled;
	}

	public void setStrikeEnabled(boolean strikeEnabled) {
		this.strikeEnabled = strikeEnabled;
	}

	public boolean isDisplayAnalysis() {
		return displayAnalysis;
	}

	public void setDisplayAnalysis(boolean displayAnalysis) {
		this.displayAnalysis = displayAnalysis;
	}

	public int getMinMaxDepth() {
		return minMaxDepth;
	}

	public void setMinMaxDepth(int minMaxDepth) {
		this.minMaxDepth = minMaxDepth;
	}

	public int getStrikeDepth() {
		return strikeDepth;
	}

	public void setStrikeDepth(int strikeDepth) {
		this.strikeDepth = strikeDepth;
	}

	public int getMinMaxExtent() {
		return minMaxExtent;
	}

	public void setMinMaxExtent(int minMaxExtent) {
		this.minMaxExtent = minMaxExtent;
	}

	public int getStrikeTimeout() {
		return strikeTimeout;
	}

	public void setStrikeTimeout(int strikeTimeout) {
		this.strikeTimeout = strikeTimeout;
	}

	private int minMaxDepth;
	
	private int strikeDepth;
	
	private int minMaxExtent;
	
	private int strikeTimeout;

}
