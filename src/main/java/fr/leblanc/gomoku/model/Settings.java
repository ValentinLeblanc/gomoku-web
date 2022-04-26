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
    
    public Settings() {
        this.boardSize = 15;
    }
}