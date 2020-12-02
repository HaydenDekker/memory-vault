package com.hdekker.ui.sessions;

import java.util.ArrayDeque;
import java.util.Optional;

import com.hdekker.media.slideshows.data.Controller;
import com.hdekker.media.slideshows.data.Definition;
import com.hdekker.media.slideshows.data.MediaDisplayComponent;
import com.hdekker.media.slideshows.data.SSSession;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

/**
 * 
 * A session allows only a single definition assigned
 * to a single display
 * to a single controller
 * 
 * TODO update to multiple definitions
 * TODO update to multiple controllers
 * 
 * A session is always associated with zero or 1 displays
 * but a display may or may not be available to accept
 * commands from controllers.
 * 
 */
@UIScope
@SpringComponent
public class SessionUIState {

	Optional<SSSession> session = Optional.empty();
	
	public void reset() {
		session = Optional.empty();
	}

	public Optional<SSSession> getSession() {
		return session;
	}
	
	public void setSession(Optional<SSSession> session) {
		this.session = session;
	}

}
