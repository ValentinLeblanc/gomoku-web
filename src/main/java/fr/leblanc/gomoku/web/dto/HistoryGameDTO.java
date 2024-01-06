package fr.leblanc.gomoku.web.dto;

import java.sql.Timestamp;

import fr.leblanc.gomoku.model.GameType;

public record HistoryGameDTO(Long id, String name, GameType gameType, PlayerDTO blackPlayer, PlayerDTO whitePlayer, PlayerDTO winner, Timestamp date) {

}
