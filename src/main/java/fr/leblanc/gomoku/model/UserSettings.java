package fr.leblanc.gomoku.model;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "settings")
public class UserSettings
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "board_size")
    private int boardSize = 15;
    @Column(name = "display_analysis")
    private boolean displayAnalysis = false;
    private boolean displayEvaluation = false;
	private boolean strikeEnabled = false;
	private int minMaxDepth = 2;
	private int strikeDepth = 4;
	private int minMaxExtent = 0;
	private int strikeTimeout = 20;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public boolean isDisplayEvaluation() {
		return displayEvaluation;
	}
	
	public void setDisplayEvaluation(boolean displayEvaluation) {
		this.displayEvaluation = displayEvaluation;
	}

	public Integer getBoardSize() {
		return boardSize;
	}

	public void setBoardSize(Integer boardSize) {
		this.boardSize = boardSize;
	}

	public boolean isDisplayAnalysis() {
		return displayAnalysis;
	}

	public void setDisplayAnalysis(boolean displayAnalysis) {
		this.displayAnalysis = displayAnalysis;
	}

	public boolean isStrikeEnabled() {
		return strikeEnabled;
	}

	public void setStrikeEnabled(boolean strikeEnabled) {
		this.strikeEnabled = strikeEnabled;
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

	@Override
	public String toString() {
		return "UserSettings [id=" + id + ", boardSize=" + boardSize + ", displayAnalysis=" + displayAnalysis
				+ ", displayEvaluation=" + displayEvaluation + ", strikeEnabled=" + strikeEnabled + ", minMaxDepth="
				+ minMaxDepth + ", strikeDepth=" + strikeDepth + ", minMaxExtent=" + minMaxExtent + ", strikeTimeout="
				+ strikeTimeout + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(boardSize, displayAnalysis, displayEvaluation, id, minMaxDepth, minMaxExtent, strikeDepth,
				strikeEnabled, strikeTimeout);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserSettings other = (UserSettings) obj;
		return boardSize == other.boardSize && displayAnalysis == other.displayAnalysis
				&& displayEvaluation == other.displayEvaluation && id == other.id && minMaxDepth == other.minMaxDepth
				&& minMaxExtent == other.minMaxExtent && strikeDepth == other.strikeDepth
				&& strikeEnabled == other.strikeEnabled && strikeTimeout == other.strikeTimeout;
	}
}