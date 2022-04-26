package fr.leblanc.gomoku.service;

import java.util.ArrayList;
import fr.leblanc.gomoku.model.OnlineBoard;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import fr.leblanc.gomoku.repository.OnlineBoardRepository;
import org.springframework.stereotype.Service;

@Service
public class OnlineBoardService {
	
	@Autowired
	private OnlineBoardRepository onlineBoardRepository;
	@Autowired
	private UserService userService;

	public List<OnlineBoard> findAll() {
		final List<OnlineBoard> onlineBoards = new ArrayList<OnlineBoard>();
		final Iterable<OnlineBoard> onlineBoardsIterator = (Iterable<OnlineBoard>) this.onlineBoardRepository.findAll();
		onlineBoardsIterator.forEach(e -> onlineBoards.add(e));
		return onlineBoards;
	}

	public OnlineBoard addBoard() {
		final OnlineBoard newBoard = OnlineBoard.builder().owner(this.userService.getCurrentUser()).build();
		return onlineBoardRepository.save(newBoard);
	}
}