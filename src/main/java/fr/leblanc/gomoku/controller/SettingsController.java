package fr.leblanc.gomoku.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import fr.leblanc.gomoku.model.UserSettings;
import fr.leblanc.gomoku.service.SettingsService;
import fr.leblanc.gomoku.web.dto.UserSettingsDTO;

@RestController
public class SettingsController
{
    @Autowired
    private SettingsService settingsService;
    
    @PostMapping({ "/settings" })
    public RedirectView updateSettings(UserSettingsDTO userSettingsDTO, Model model) {
    	
    	UserSettings settings = settingsService.getCurrentSettings();
    	
    	settings.setBoardSize(userSettingsDTO.getBoardSize());
    	settings.setDisplayAnalysis(userSettingsDTO.isDisplayAnalysis());
    	settings.setMinMaxDepth(userSettingsDTO.getMinMaxDepth());
    	settings.setMinMaxExtent(userSettingsDTO.getMinMaxExtent());
    	settings.setStrikeDepth(userSettingsDTO.getStrikeDepth());
    	settings.setStrikeTimeout(userSettingsDTO.getStrikeTimeout());
    	settings.setStrikeEnabled(userSettingsDTO.isStrikeEnabled());
    	
        settingsService.save(settings);
        
        model.addAttribute("settings", settings);
		
		return new RedirectView("/");
    }
}