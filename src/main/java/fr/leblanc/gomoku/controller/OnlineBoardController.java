package fr.leblanc.gomoku.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import fr.leblanc.gomoku.service.OnlineBoardService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OnlineBoardController
{
    @Autowired
    private OnlineBoardService onlineBoardService;
    
    @PostMapping({ "/add-board" })
    public void addBoard(@RequestBody final String body) {
        this.onlineBoardService.addBoard();
    }
}