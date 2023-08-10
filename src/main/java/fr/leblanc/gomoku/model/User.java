package fr.leblanc.gomoku.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user", uniqueConstraints = { @UniqueConstraint(columnNames = { "email" }) })
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
    private String email;
    private String password;
    @OneToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "settings_id")
    private UserSettings settings;
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
    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(name = "users_games", joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "game_id", referencedColumnName = "id") })
    private List<Game> games;
    @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
    @JoinTable(name = "users_roles", joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "role_id", referencedColumnName = "id") })
    private Collection<Role> roles;
    
    @ManyToMany
    private List<User> challengers;
    
    public User() {
        this.settings = new UserSettings();
        this.games = new ArrayList<>();
        this.challengers = new ArrayList<>();
    }
    
    public User(final String firstName, final String lastName, final String email, final String password, final Collection<Role> roles) {
        this.settings = new UserSettings();
        this.games = new ArrayList<>();
        this.challengers = new ArrayList<>();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

}