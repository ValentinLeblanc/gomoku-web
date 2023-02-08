package fr.leblanc.gomoku.service;

import fr.leblanc.gomoku.model.Settings;
import fr.leblanc.gomoku.repository.SettingsRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SettingsService
{
    @Autowired
    private UserService userService;
    @Autowired
    private SettingsRepository settingsRepository;
    
    public void save(final Settings settings) {
        userService.getCurrentUser().setSettings(settings);
        userService.save(this.userService.getCurrentUser());
    }
    
    public Optional<Settings> findById(final Long id) {
    	return  settingsRepository.findById(id);
    }

	public Settings getCurrentSettings() {
		return userService.getCurrentUser().getSettings();
	}
}