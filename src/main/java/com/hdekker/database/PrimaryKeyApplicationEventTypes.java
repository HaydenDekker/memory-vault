package com.hdekker.database;

/**
 * The structure of the key is simply
 * 
 * user-name+key-event-type.
 * 
 * @author HDekker
 *
 */
public enum PrimaryKeyApplicationEventTypes{
		
		DefinitionEvent("definition-event"),
		ControllerEvent("controller-event"),
		SessionEvent("session-event"),
		MediaDisplayEvent("media-display-event"),
		FileSystemEvent("file-system-event");
		
		final String eventAddon;
		
		PrimaryKeyApplicationEventTypes(String eventAddon){
			this.eventAddon = eventAddon;
		}

		public String getEventAddon() {
			return eventAddon;
		}
}
	
