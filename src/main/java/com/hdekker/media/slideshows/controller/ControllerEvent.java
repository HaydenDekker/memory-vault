package com.hdekker.media.slideshows.controller;

import com.hdekker.media.slideshows.data.Controller;

public class ControllerEvent {

	Controller controller;
	ControllerCommand command;
	
	public ControllerEvent(Controller controller,
			ControllerCommand command) {
		this.controller = controller;
		this.command = command;
	}
	
	public Controller getController() {
		return controller;
	}
	public void setController(Controller controller) {
		this.controller = controller;
	}
	public ControllerCommand getCommand() {
		return command;
	}
	public void setCommand(ControllerCommand command) {
		this.command = command;
	}
	
	
	
}
