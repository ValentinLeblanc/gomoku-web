package fr.leblanc.gomoku.repository;

import org.springframework.stereotype.Repository;
import fr.leblanc.gomoku.model.Move;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface MoveRepository extends CrudRepository<Move, Long>
{
}