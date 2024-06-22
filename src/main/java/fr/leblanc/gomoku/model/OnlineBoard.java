package fr.leblanc.gomoku.model;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "onlineboard")
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public User getGuest() {
		return guest;
	}

	public void setGuest(User guest) {
		this.guest = guest;
	}

	@Override
	public int hashCode() {
		return Objects.hash(game, guest, id, owner);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OnlineBoard other = (OnlineBoard) obj;
		return Objects.equals(game, other.game) && Objects.equals(guest, other.guest) && Objects.equals(id, other.id)
				&& Objects.equals(owner, other.owner);
	}

	@Override
	public String toString() {
		return "OnlineBoard [id=" + id + ", game=" + game + ", owner=" + owner + ", guest=" + guest + "]";
	}
    
}