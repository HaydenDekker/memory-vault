package com.hdekker.ui.controller;

import java.util.Optional;

import com.hdekker.media.slideshows.data.Controller;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class ControllerUIState {

	/**
	 * Optional from the perspective of the user as they
	 * may navigate directly using the browser.
	 * 
	 */
	Optional<Controller> activeController = Optional.empty();

	public Optional<Controller> getActiveController() {
		return activeController;
	}

	public void setActiveController(Optional<Controller> activeController) {
		this.activeController = activeController;
	}

	/**
	 * Optional from the perspective of the user as they
	 * may navigate directly using the browser.
	 * 
	 */
	Optional<Class<? extends Component>> previousNavigationTarget = Optional.empty();

	public Optional<Class<? extends Component>> getPreviousNavigationTarget() {
		return previousNavigationTarget;
	}

	public void setPreviousNavigationTarget(Optional<Class<? extends Component>> previousNavigationTarget) {
		this.previousNavigationTarget = previousNavigationTarget;
	}

	
	
}
