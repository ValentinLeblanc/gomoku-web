package fr.leblanc.gomoku.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "game")
@Builder
@Data
@AllArgsConstructor
public class Game {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private GameType type;

	@ManyToOne
	@JoinColumn(name = "black_player_id")
	private User blackPlayer;

	@ManyToOne
	@JoinColumn(name = "white_player_id")
	private User whitePlayer;
	private int boardSize;

	@OneToMany(cascade = { CascadeType.ALL })
	@JoinTable(name = "games_moves")
	private Set<Move> moves;

	@ManyToOne
	@JoinColumn(name = "winner_id")
	private User winner;

	@OneToMany(cascade = { CascadeType.ALL })
	@JoinTable(name = "games_wins")
	@Column(name = "win_combination")
	private Set<Move> winCombination;

	public Move getMove(final int columnIndex, final int rowIndex) {
		for (final Move move : this.moves) {
			if (move.getColumnIndex() == columnIndex && move.getRowIndex() == rowIndex) {
				return move;
			}
		}
		return null;
	}

	public Move getMove(final int moveNumber) {
		for (final Move move : this.moves) {
			if (move.getNumber() == moveNumber) {
				return move;
			}
		}
		return null;
	}

	public Game() {
		this.moves = new HashSet<>();
		this.winCombination = new HashSet<>();
	}

}
