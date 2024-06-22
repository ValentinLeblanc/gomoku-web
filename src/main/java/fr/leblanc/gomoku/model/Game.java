package fr.leblanc.gomoku.model;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "game")
public class Game {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private GameType type;
	
	private Timestamp date;

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
	
	private String name;
	
	public Move getMove(final int columnIndex, final int rowIndex) {
		for (final Move move : this.moves) {
			if (move.getColumnIndex() == columnIndex && move.getRowIndex() == rowIndex) {
				return move;
			}
		}
		return null;
	}

	public Game() {
		this.moves = new HashSet<>();
		this.winCombination = new HashSet<>();
	}
	
	public Game(Game game) {
		this.blackPlayer = game.blackPlayer;
		this.whitePlayer = game.whitePlayer;
		this.date = game.date;
		this.boardSize = game.boardSize;
		this.moves = new HashSet<>();
		for (Move move : game.moves) {
			this.moves.add(new Move(move));
		}
		this.winCombination = new HashSet<>();
		for (Move move : game.winCombination) {
			this.winCombination.add(new Move(move));
		}
		this.winner = game.winner;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public GameType getType() {
		return type;
	}

	public void setType(GameType type) {
		this.type = type;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public User getBlackPlayer() {
		return blackPlayer;
	}

	public void setBlackPlayer(User blackPlayer) {
		this.blackPlayer = blackPlayer;
	}

	public User getWhitePlayer() {
		return whitePlayer;
	}

	public void setWhitePlayer(User whitePlayer) {
		this.whitePlayer = whitePlayer;
	}

	public int getBoardSize() {
		return boardSize;
	}

	public void setBoardSize(int boardSize) {
		this.boardSize = boardSize;
	}

	public Set<Move> getMoves() {
		return moves;
	}

	public void setMoves(Set<Move> moves) {
		this.moves = moves;
	}

	public User getWinner() {
		return winner;
	}

	public void setWinner(User winner) {
		this.winner = winner;
	}

	public Set<Move> getWinCombination() {
		return winCombination;
	}

	public void setWinCombination(Set<Move> winCombination) {
		this.winCombination = winCombination;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(blackPlayer, boardSize, date, id, moves, name, type, whitePlayer, winCombination, winner);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Game other = (Game) obj;
		return boardSize == other.boardSize
				&& Objects.equals(date, other.date) && Objects.equals(id, other.id)
				&& Objects.equals(moves, other.moves) && Objects.equals(name, other.name) && type == other.type
				&& Objects.equals(winCombination, other.winCombination) && Objects.equals(winner, other.winner);
	}

	@Override
	public String toString() {
		return "Game [id=" + id + ", type=" + type + ", date=" + date + ", blackPlayer=" + blackPlayer
				+ ", whitePlayer=" + whitePlayer + ", boardSize=" + boardSize + ", moves=" + moves + ", winner="
				+ winner + ", winCombination=" + winCombination + ", name=" + name + "]";
	}

	public static Game build() {
		return new Game();
	}

	public Game blackPlayer(User blackPlayer) {
		this.blackPlayer = blackPlayer;
		return this;
	}

	public Game whitePlayer(User whitePlayer) {
		this.whitePlayer = whitePlayer;
		return this;
	}

	public Game boardSize(Integer boardSize) {
		this.boardSize = boardSize;
		return this;
	}

	public Game type(GameType type) {
		this.type = type;
		return this;
	}

	public Game date(Timestamp date) {
		this.date = date;
		return this;
	}

}
