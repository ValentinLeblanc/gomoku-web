package fr.leblanc.gomoku.repository;

import org.springframework.stereotype.Repository;
import fr.leblanc.gomoku.model.Game;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface GameRepository extends CrudRepository<Game, Integer>
{
}