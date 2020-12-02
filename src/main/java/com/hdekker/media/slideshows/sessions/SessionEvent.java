package com.hdekker.media.slideshows.sessions;

import com.hdekker.media.slideshows.data.SSSession;

public class SessionEvent {
	
		final SSSession session;
		final SlideShowSessionEventType eventType;
		
		public SessionEvent(SSSession session, SlideShowSessionEventType eventType) {
			super();
			this.session = session;
			this.eventType = eventType;
		}
		
		public SSSession getSession() {
			return session;
		}
		public SlideShowSessionEventType getEventType() {
			return eventType;
		}
		
}

