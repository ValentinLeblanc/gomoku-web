package fr.leblanc.gomoku.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "user", uniqueConstraints = { @UniqueConstraint(columnNames = { "username" }) })
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "last_name")
    private String lastName;
    
    private String username;
    
    private String password;
    
    @OneToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "settings_id")
    private UserSettings settings = new UserSettings();
    
    @OneToOne
    @JoinColumn(name = "current_game_id")
    @JsonIgnore
    private Game currentLocalGame;
    
    @ManyToOne
    @JoinColumn(name = "current_online_game_id")
    @JsonIgnore
    private Game currentOnlineGame;
    
    @OneToOne
    @JoinColumn(name = "current_ai_game_id")
    @JsonIgnore
    private Game currentAIGame;
    
    @OneToOne
    @JoinColumn(name = "current_ai_vs_ai_game_id")
    @JsonIgnore
    private Game currentAIvsAIGame;
    
    @OneToOne
    @JoinColumn(name = "current_history_game_id")
    @JsonIgnore
    private Game currentHistoryGame;
    
    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(name = "users_games", joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "game_id", referencedColumnName = "id") })
    private List<Game> games = new ArrayList<>();
    
    @ElementCollection(targetClass=String.class)
    private List<String> challengers = new ArrayList<>();
    
    public User() {
    	
    }
    
    public User(final String firstName, final String lastName, final String username, final String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserSettings getSettings() {
		return settings;
	}

	public void setSettings(UserSettings settings) {
		this.settings = settings;
	}

	public Game getCurrentLocalGame() {
		return currentLocalGame;
	}

	public void setCurrentLocalGame(Game currentLocalGame) {
		this.currentLocalGame = currentLocalGame;
	}

	public Game getCurrentOnlineGame() {
		return currentOnlineGame;
	}

	public void setCurrentOnlineGame(Game currentOnlineGame) {
		this.currentOnlineGame = currentOnlineGame;
	}

	public Game getCurrentAIGame() {
		return currentAIGame;
	}

	public void setCurrentAIGame(Game currentAIGame) {
		this.currentAIGame = currentAIGame;
	}

	public Game getCurrentAIvsAIGame() {
		return currentAIvsAIGame;
	}

	public void setCurrentAIvsAIGame(Game currentAIvsAIGame) {
		this.currentAIvsAIGame = currentAIvsAIGame;
	}

	public Game getCurrentHistoryGame() {
		return currentHistoryGame;
	}

	public void setCurrentHistoryGame(Game currentHistoryGame) {
		this.currentHistoryGame = currentHistoryGame;
	}

	public List<Game> getGames() {
		return games;
	}

	public void setGames(List<Game> games) {
		this.games = games;
	}

	public List<String> getChallengers() {
		return challengers;
	}

	public void setChallengers(List<String> challengers) {
		this.challengers = challengers;
	}

	@Override
	public int hashCode() {
		return Objects.hash(challengers, firstName, id, lastName, password, settings, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(challengers, other.challengers)
				&& Objects.equals(firstName, other.firstName)
				&& Objects.equals(id, other.id) && Objects.equals(lastName, other.lastName)
				&& Objects.equals(password, other.password) && Objects.equals(settings, other.settings)
				&& Objects.equals(username, other.username);
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", username=" + username
				+ ", password=" + password + ", settings=" + settings + ", challengers=" + challengers + "]";
	}

}