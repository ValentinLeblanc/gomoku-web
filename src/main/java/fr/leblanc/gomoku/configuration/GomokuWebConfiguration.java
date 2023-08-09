package fr.leblanc.gomoku.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GomokuWebConfiguration {

    @Value("${fr.leblanc.gomoku.engineUrl}")
    private String engineUrl;

    public String getEngineUrl() {
		return engineUrl;
	}
	
}
