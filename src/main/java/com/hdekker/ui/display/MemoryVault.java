package com.hdekker.ui.display;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hdekker.component.MediaSSDisplay;
import com.hdekker.component.MediaDefinitionDTO;
import com.hdekker.component.SlideShowCommandType;
import com.hdekker.media.slideshows.controller.ControllerCommand;
import com.hdekker.media.slideshows.controller.ControllerEvent;
import com.hdekker.media.slideshows.data.Definition;
import com.hdekker.media.slideshows.data.MediaDisplayComponent;
import com.hdekker.media.slideshows.definition.DefinitionService;
import com.hdekker.media.slideshows.displays.DisplayService;
import com.hdekker.media.slideshows.sessions.SessionEvent;
import com.hdekker.media.slideshows.sessions.SessionService;
import com.hdekker.media.slideshows.sessions.SlideShowSessionEventType;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;

@Route("memory-vault")
@Push
public class MemoryVault extends VerticalLayout implements
						AfterNavigationObserver, BeforeLeaveObserver, HasUrlParameter<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Logger log = LoggerFactory.getLogger(MemoryVault.class);

	UI ui = UI.getCurrent();
	
	public MediaSSDisplay display;
	MediaDisplayComponent mdc;
	
	@Autowired
	SessionStreamResourceManager ssrm;
	
	@Autowired
	SessionService service;
	
	@Autowired
	DisplayService displayService;
	
	@Autowired
	DefinitionService definitionService;
	
	public MemoryVault() {
		
		setSizeFull();
		
		getStyle().set("justify-content", "center");
		getStyle().set("align-items", "center");
		getStyle().set("background-color", "black");
		
		display = new MediaSSDisplay();
		add(display);
		
	}
	
	// TODO conceal local state
	Optional<Registration> detachListenerReg = Optional.empty();
	Optional<Registration> controllerListenerReg = Optional.empty();
	Optional<Registration> sessionSubscriberReg = Optional.empty();

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		
		// Find an active session and start if possible
		service.getActiveSessionForDisplay(mdc).ifPresent((s)->{
			
			controllerListenerReg = Optional.of(service.registerControllerListener(Arrays.asList(s.getControllerSortKey()), this::accept));
			sessionSubscriberReg = Optional.of(service.subscribeSessionEvents(this::accept));
			
			// If user exits UI and it times out.
			detachListenerReg = Optional.of(addDetachListener((l)-> {
				controllerListenerReg.get().remove();
				sessionSubscriberReg.get().remove();
			}));
			
			SessionEvent start = new SessionEvent(s, SlideShowSessionEventType.Activated);
			onSlideShowSessionEvent(start);
			
			log.info("Display " + mdc + " has started ");
		});
		
		// TODO else display something nice.
		
	}
	
	public ControllerCommand latestCommand;
	
	private SlideShowCommandType getCommand(ControllerCommand e) {
		
		switch (e) {
		
			case PLAY:
				return SlideShowCommandType.PLAY;
			case STOP:
				return SlideShowCommandType.STOP;
			case BACK:
				return SlideShowCommandType.BACK;
			case FORWARD:
				return SlideShowCommandType.FORWARD;
			case PAUSE:
				return SlideShowCommandType.PAUSE;
			default:
				return SlideShowCommandType.PAUSE;
		}
		
	}
	
	public void receiveCommand(ControllerEvent e) {
		
		latestCommand = e.getCommand();
		
		log.info("Display recieved " + e.getCommand().toString());
		
		ui.access(()-> {
			display.sendCommand(getCommand(e.getCommand()));
			ui.push();
		});
	}
	
	public void onSlideShowSessionEvent(SessionEvent event) {
		
		log.info("Slide show event recieved.");
		
		Definition def = definitionService.getDefinition(event.getSession().getSlideShowDefinitionSortKey());
		
		ui.access(()->{
			
			List<MediaDefinitionDTO> dtos = ssrm.getAndInitMediaDefinitions()
					.apply(def.getMedia());
				
			display.setMediaDefinitions(dtos);
			
		});

	}
	
	@Override
	public void setParameter(BeforeEvent event, String parameter) {
		
		mdc = displayService.checkInDisplay(parameter);
		log.info("Display that checked in is " + parameter);
		
	}

	public void accept(ControllerEvent event) {
		
		this.receiveCommand(event);
		log.info("Reveived event for controller " + event.getController().getControllerName());
		
	}
	
	public void accept(SessionEvent event) {
		
		// TODO implement finer grained session management
		onSlideShowSessionEvent(event);
		
	}

	@Override
	public void beforeLeave(BeforeLeaveEvent event) {
		
		detachListenerReg.ifPresent(l->l.remove());
		controllerListenerReg.ifPresent(l->l.remove());
		sessionSubscriberReg.ifPresent(l->l.remove());
		
	}
	
}
