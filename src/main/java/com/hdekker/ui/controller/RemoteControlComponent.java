package com.hdekker.ui.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.hdekker.EventProvider;
import com.hdekker.media.slideshows.controller.ControllerCommand;
import com.hdekker.media.slideshows.controller.ControllerEvent;
import com.hdekker.media.slideshows.data.Controller;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class RemoteControlComponent extends VerticalLayout
										implements EventProvider<ControllerEvent>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Button pause = new Button("Pause");
	Button stop = new Button("Stop");
	Button start = new Button("Start");
	Button next = new Button("Next");
	Button previous = new Button("Back");
	
	Controller controller;
	
	public RemoteControlComponent(Controller controller) {
		
		this.controller = controller;
		add(pause, stop, start, next, previous);
		setControlListeners();
		
	}
	
	public void setControlListeners() {
		
		pause.addClickListener((e)-> 
					{
						fireListeners(new ControllerEvent(this.controller, ControllerCommand.PAUSE));
						});
		stop.addClickListener((e)-> fireListeners(new ControllerEvent(this.controller, ControllerCommand.STOP)));
		start.addClickListener((e)-> fireListeners(new ControllerEvent(this.controller, ControllerCommand.PLAY)));
		next.addClickListener((e)-> fireListeners(new ControllerEvent(this.controller, ControllerCommand.FORWARD)));
		previous.addClickListener((e)-> fireListeners(new ControllerEvent(this.controller, ControllerCommand.BACK)));
		
	}
	
	List<Consumer<ControllerEvent>> controllerListeners = new ArrayList<>();

	@Override
	public List<Consumer<ControllerEvent>> getListeners() {
		
		return controllerListeners;
	}
}
