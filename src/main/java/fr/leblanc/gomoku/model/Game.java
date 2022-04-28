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

import org.json.JSONArray;
import org.json.JSONObject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "game")
@Builder
@Data
@EqualsAndHashCode
@AllArgsConstructor
public class Game
{
    private static final String BOARD_SIZE = "boardSize";
	public static final String MOVES_STRING = "moves";
    public static final String TYPE_STRING = "type";
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
    
    public JSONObject toJSON() {
        final JSONObject json = new JSONObject();
        json.put(TYPE_STRING, this.type);
        json.put(BOARD_SIZE, this.boardSize);
        json.put(MOVES_STRING, this.getMovesToJSON());
        return json;
    }
    
    public JSONArray getMovesToJSON() {
        final JSONArray jsonMoves = new JSONArray();
        for (final Move move : this.moves) {
            jsonMoves.put(move.toJSON());
        }
        return jsonMoves;
    }
    
    public Game() {
        this.moves = new HashSet<>();
        this.winCombination = new HashSet<>();
    }

}
    
