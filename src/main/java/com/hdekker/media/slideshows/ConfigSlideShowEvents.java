package com.hdekker.media.slideshows;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.hdekker.EventProvider;
import com.hdekker.media.slideshows.controller.ControllerService;
import com.hdekker.media.slideshows.definition.DefinitionService;
import com.hdekker.media.slideshows.definition.MediaEvent;
import com.hdekker.media.slideshows.sessions.SessionService;

@Configuration
public class ConfigSlideShowEvents implements InitializingBean{

	@Autowired
	EventProvider<MediaEvent> mediaEventProvider;
	
	@Autowired
	DefinitionService definitionService;
	
	@Autowired
	SessionService sessionManager;
	
	@Autowired
	ControllerService controllerManager;
	
	Logger log = LoggerFactory.getLogger(ConfigSlideShowEvents.class);

	public ConfigSlideShowEvents() {
		
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
		// Slide show updates
		log.info("Configuring S3 dynamic events.");
		
		mediaEventProvider.addListener(definitionService::updateMediaInSlideShowDefinition);
		definitionService.addListener(sessionManager::updateSlideShowDefinition);
		
	}
	
}
