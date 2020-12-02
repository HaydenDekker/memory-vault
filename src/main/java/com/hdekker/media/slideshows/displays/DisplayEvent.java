package com.hdekker.media.slideshows.displays;

import com.hdekker.media.slideshows.data.MediaDisplayComponent;

public class DisplayEvent {

	final DisplayEventType eventType;
	final MediaDisplayComponent components;
	
	public DisplayEvent(DisplayEventType eventType, MediaDisplayComponent components) {
		super();
		this.eventType = eventType;
		this.components = components;
	}

	public DisplayEventType getEventType() {
		return eventType;
	}

	public MediaDisplayComponent getComponents() {
		return components;
	}
	
	
	
}
