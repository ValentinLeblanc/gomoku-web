package fr.leblanc.gomoku.service;

import fr.leblanc.gomoku.model.Move;
import org.springframework.beans.factory.annotation.Autowired;
import fr.leblanc.gomoku.repository.MoveRepository;
import org.springframework.stereotype.Service;

@Service
public class MoveService
{
    @Autowired
    private MoveRepository moveRepository;
    
    public void delete(final Move move) {
        this.moveRepository.delete(move);
    }
}