package fr.leblanc.gomoku.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import fr.leblanc.gomoku.model.Settings;
import fr.leblanc.gomoku.service.SettingsService;
import fr.leblanc.gomoku.web.dto.SettingsDto;

@RestController
public class SettingsController
{
    @Autowired
    private SettingsService settingsService;
    
    @PostMapping({ "/settings" })
    public RedirectView updateSettings(SettingsDto settingsDto, Model model) {
    	
    	Settings settings = settingsService.getCurrentSettings();
    	settings.setBoardSize(settingsDto.getBoardSize());
    	settings.setDisplayAnalysis(settingsDto.isDisplayAnalysis());
    	settings.setMinMaxDepth(settingsDto.getMinMaxDepth());
    	settings.setMinMaxExtent(settingsDto.getMinMaxExtent());
    	settings.setStrikeDepth(settingsDto.getStrikeDepth());
    	settings.setStrikeTimeout(settingsDto.getStrikeTimeout());
    	settings.setStrikeEnabled(settingsDto.isStrikeEnabled());
    	
        settingsService.save(settings);
        
        model.addAttribute("settings", settings);
		
		return new RedirectView("/settings");
    }
}