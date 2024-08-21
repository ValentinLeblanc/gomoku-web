package fr.leblanc.gomoku.web.dto;

import java.util.Map;

public record EvaluationDTO(Double evaluation, Map<CellDTO, Double> cellMap) {

}
