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
        final Settings settings = this.settingsService.findById(jsonBody.getLong("id"));
        settings.setBoardSize(jsonBody.getInt("boardSize"));
        settings.setDisplayAnalysis(jsonBody.getBoolean("displayAnalysis"));
        settings.setStrikeEnabled(jsonBody.getBoolean("strikeEnabled"));
        settings.setStrikeDepth(jsonBody.getInt("strikeDepth"));
        settings.setMinMaxDepth(jsonBody.getInt("minMaxDepth"));
        settings.setEvaluationDepth(jsonBody.getInt("evaluationDepth"));
        this.settingsService.updateSettings(settings);
    }
}