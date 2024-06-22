package fr.leblanc.gomoku.controller;

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
    private SettingsService settingsService;
    
    public SettingsController(SettingsService settingsService) {
		super();
		this.settingsService = settingsService;
	}

	@PostMapping({ "/settings" })
    public RedirectView updateSettings(UserSettingsDTO userSettingsDTO, Model model) {
    	
    	UserSettings settings = settingsService.getCurrentSettings();
    	
    	settings.setBoardSize(userSettingsDTO.boardSize());
    	settings.setDisplayAnalysis(userSettingsDTO.displayAnalysis());
    	settings.setMinMaxDepth(userSettingsDTO.minMaxDepth());
    	settings.setMinMaxExtent(userSettingsDTO.minMaxExtent());
    	settings.setStrikeDepth(userSettingsDTO.strikeDepth());
    	settings.setStrikeTimeout(userSettingsDTO.strikeTimeout());
    	settings.setStrikeEnabled(userSettingsDTO.strikeEnabled());
    	
        settingsService.save(settings);
        
        model.addAttribute("settings", settings);
		
		return new RedirectView("/");
    }
}