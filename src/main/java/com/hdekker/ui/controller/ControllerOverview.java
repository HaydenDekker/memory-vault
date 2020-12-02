package com.hdekker.ui.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hdekker.EventProvider;
import com.hdekker.database.PushLayout;
import com.hdekker.media.slideshows.controller.ControllerEvent;
import com.hdekker.media.slideshows.controller.ControllerService;
import com.hdekker.media.slideshows.sessions.SessionService;
import com.hdekker.mobileapp.layout.HasApplicationViewHeading;
import com.hdekker.mobileapp.templates.SinglePageCard;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;

@Route(value = "controller-overview", layout = PushLayout.class)
public class ControllerOverview extends SinglePageCard implements HasApplicationViewHeading,
															AfterNavigationObserver, 
															EventProvider<ControllerEvent>
															{
	
	Logger log = LoggerFactory.getLogger(ControllerOverview.class);
	
	@Autowired
	public ControllerUIState state;
	
	@Autowired
	ControllerService controllerService;
	
	@Autowired
	SessionService sessionService;
	
	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		
		log.info(String.valueOf(UI.getCurrent()));
		state.getActiveController().ifPresentOrElse((c)->{
			
			RemoteControlComponent rc = new RemoteControlComponent(c);
			add(rc);
			// pass on event
			rc.addListener((e)-> sessionService.accept(e));
			
		}, ()-> UI.getCurrent().navigate(ControllerList.class));
		
	}

	@Override
	public Component getApplicationHeading() {
		
		if(state.getActiveController().isEmpty()) {
			UI.getCurrent().navigate(ControllerList.class);
			return new H3("");
		}
		
		return state.getActiveController()
							.map(c->new H3(c.getControllerName()))
							.orElse(new H3("Controller Overview"));
	}
	
	List<Consumer<ControllerEvent>> eventListeners = new ArrayList<Consumer<ControllerEvent>>();

	@Override
	public List<Consumer<ControllerEvent>> getListeners() {
		
		return eventListeners;
	}


}
