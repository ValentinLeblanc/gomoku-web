package fr.leblanc.gomoku.repository;

import org.springframework.stereotype.Repository;
import fr.leblanc.gomoku.model.OnlineBoard;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface OnlineBoardRepository extends CrudRepository<OnlineBoard, Long>
{
}