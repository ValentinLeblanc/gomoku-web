package fr.leblanc.gomoku.model;

import java.util.ArrayList;
import java.util.List;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user", uniqueConstraints = { @UniqueConstraint(columnNames = { "username" }) })
@Getter
@Setter
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
    @OneToOne
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

}