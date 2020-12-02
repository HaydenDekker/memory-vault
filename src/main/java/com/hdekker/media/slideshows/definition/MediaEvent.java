package com.hdekker.media.slideshows.definition;

public class MediaEvent {
	
		public enum SlideShowMediaEventType{
			
			ADDED,
			REMOVED,
			UPDATED
			
		}
		final SlideShowMediaEventType eventType;
		final String objectKey;
		
		public MediaEvent(SlideShowMediaEventType eventType, String objectKey) {
			super();
			this.eventType = eventType;
			this.objectKey = objectKey;
		}

		public SlideShowMediaEventType getEventType() {
			return eventType;
		}

		public String getObjectKey() {
			return objectKey;
		}
		
}


