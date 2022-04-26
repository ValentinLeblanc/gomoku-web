package fr.leblanc.gomoku.controller;

import org.springframework.web.bind.annotation.PostMapping;
import fr.leblanc.gomoku.model.Settings;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import fr.leblanc.gomoku.service.SettingsService;
import org.springframework.stereotype.Controller;

@Controller
public class SettingsController
{
    @Autowired
    private SettingsService settingsService;
    
    @PostMapping({ "/settings" })
    public void updateSettings(@RequestBody final String body) {
        final JSONObject jsonBody = new JSONObject(body);
        final Settings settings = this.settingsService.findById(Long.valueOf(jsonBody.getLong("id")));
        settings.setBoardSize(Integer.valueOf(jsonBody.getInt("boardSize")));
        this.settingsService.updateSettings(settings);
    }
}