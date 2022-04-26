package fr.leblanc.gomoku.service;

import fr.leblanc.gomoku.model.Settings;
import fr.leblanc.gomoku.repository.SettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SettingsService
{
    @Autowired
    private UserService userService;
    @Autowired
    private SettingsRepository settingsRepository;
    
    public void updateSettings(final Settings settings) {
        this.userService.getCurrentUser().setSettings(settings);
        this.userService.save(this.userService.getCurrentUser());
    }
    
    public Settings findById(final Long id) {
        return this.settingsRepository.findById(id).get();
    }
}