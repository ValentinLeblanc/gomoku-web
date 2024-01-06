package fr.leblanc.gomoku.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "onlineboard")
@Builder
@Data
@EqualsAndHashCode
public class OnlineBoard
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Game game;
    @ManyToOne
    private User owner;
    @ManyToOne
    private User guest;
    
    public OnlineBoard(final Long id, final Game game, final User owner, final User guest) {
        this.id = id;
        this.game = game;
        this.owner = owner;
        this.guest = guest;
    }
    
    public OnlineBoard() {
    }
}