package fr.leblanc.gomoku.model;

import java.util.ArrayList;
import javax.persistence.FetchType;
import java.util.Collection;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.List;
import javax.persistence.JoinColumn;
import javax.persistence.CascadeType;
import javax.persistence.OneToOne;
import javax.persistence.Column;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.UniqueConstraint;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Table;
import javax.persistence.Entity;

@Entity
@Table(name = "user", uniqueConstraints = { @UniqueConstraint(columnNames = { "email" }) })
@Data
@EqualsAndHashCode
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
    private Settings settings;
    @OneToOne
    @JoinColumn(name = "current_game_id")
    private Game currentGame;
    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(name = "users_games", joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "game_id", referencedColumnName = "id") })
    private List<Game> games;
    @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
    @JoinTable(name = "users_roles", joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "role_id", referencedColumnName = "id") })
    private Collection<Role> roles;
    
    public User() {
        this.settings = new Settings();
        this.games = new ArrayList<>();
    }
    
    public User(final String firstName, final String lastName, final String email, final String password, final Collection<Role> roles) {
        this.settings = new Settings();
        this.games = new ArrayList<>();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }
}