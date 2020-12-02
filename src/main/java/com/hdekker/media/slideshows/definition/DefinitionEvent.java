package com.hdekker.media.slideshows.definition;

import com.hdekker.media.slideshows.data.Definition;

public class DefinitionEvent{
	
	public enum SlideShowDefinitionEventType{
		
		ADDED,
		REMOVED,
		UPDATED
		
	}
	
	final SlideShowDefinitionEventType eventType;
	Definition slideShowDefinition;
	
	public DefinitionEvent(SlideShowDefinitionEventType eventType, Definition slideShowDefinition) {
		super();
		this.eventType = eventType;
		this.slideShowDefinition = slideShowDefinition;
	}

	public SlideShowDefinitionEventType getEventType() {
		return eventType;
	}

	public Definition getSlideShowDefinition() {
		return slideShowDefinition;
	}

	public void setSlideShowDefinition(Definition slideShowDefinition) {
		this.slideShowDefinition = slideShowDefinition;
	}
	
	

}
