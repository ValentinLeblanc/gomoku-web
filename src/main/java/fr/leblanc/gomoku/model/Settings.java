package fr.leblanc.gomoku.model;

import javax.persistence.Column;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

@Entity
@Table(name = "settings")
@Data
@EqualsAndHashCode
public class Settings
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "board_size")
    private Integer boardSize;
    @Column(name = "display_analysis")
    private Boolean displayAnalysis;
	private Boolean strikeEnabled;
	private int minMaxDepth;
	private int strikeDepth;
	private int evaluationDepth;

    public Settings() {
        this.boardSize = 15;
        this.displayAnalysis = Boolean.FALSE;
        this.strikeEnabled = Boolean.TRUE;
        this.minMaxDepth = 3;
        this.strikeDepth = 3;
        this.evaluationDepth = 3;
    }
}