package com.hdekker.ui.sessions;

import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.hdekker.database.PushLayout;
import com.hdekker.media.slideshows.controller.ControllerService;
import com.hdekker.media.slideshows.data.SSSession;
import com.hdekker.media.slideshows.definition.DefinitionService;
import com.hdekker.media.slideshows.displays.DisplayService;
import com.hdekker.media.slideshows.sessions.SessionService;
import com.hdekker.mobileapp.components.OverlayComponent;
import com.hdekker.mobileapp.layout.HasApplicationViewHeading;
import com.hdekker.mobileapp.navigation.MenubarNavigable;
import com.hdekker.mobileapp.navigation.NavigableView;
import com.hdekker.mobileapp.navigation.components.AddButton;
import com.hdekker.mobileapp.style.UIStyle;
import com.hdekker.mobileapp.templates.SinglePageCard;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;

/**
 * allows a user to view all sessions 
 * and view all media display devices
 * and make an assignment of a slideshow to a device.
 * 
 * When a device is assigned to a session it should not appear in the list
 * of available devices.
 * 
 * A slide show can be used by multiple sessions.
 * 
 * @author HDekker
 *
 */
@SpringComponent
@Scope(value = "prototype")
@Route(value = "sesh-list", layout = PushLayout.class)
public class SessionList extends SinglePageCard implements AfterNavigationObserver, MenubarNavigable, HasApplicationViewHeading {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	SessionUIState state;
	
	Grid<SSSession> sessions = new Grid<SSSession>();
	
	@Autowired
	SessionService sessionService;
	
	@Autowired
	DisplayService displayService;
	
	@Autowired
	ControllerService controllerService;
	
	@Autowired
	DefinitionService definitionService;
	
	public SessionList(){
		
		add(sessions);
		
		OverlayComponent oc = new OverlayComponent();
		AddButton ab = new AddButton();
		ab.setClassName(UIStyle.BUTTON_BOTTOM_RIGHT);
		oc.add(ab);
		add(oc);
		
		sessions.addColumn(SSSession::getName).setHeader("Session Name");
		sessions.addColumn(new ComponentRenderer<Checkbox, SSSession>(Checkbox::new, (cb, sess)->{
			
			cb.setValue(sess.getIsActive());
			
			cb.addClickListener((e)->{
				sessionService.activateSession(sess, cb.getValue());
				sessions.setItems(sessionService.getSessions());
				
			});
			
		})).setHeader("isActive");
		
		ab.addClickListener((e)->{
			
			state.reset();
			state.setSession(Optional.of(sessionService.createSession("","", "", "")));
			UI.getCurrent().navigate(SessionDetails.class);
			
		});
		
		sessions.addItemClickListener((item)->{
			SSSession sesh = item.getItem();
			state.setSession(Optional.of(sesh));
			UI.getCurrent().navigate(SessionOverview.class);
		});
		
	}
	
	@Override
	public Supplier<NavigableView> getNavigableView() {
		
		return ()-> new NavigableView(SessionList.class, "Sessions");
	}

	@Override
	public Component getApplicationHeading() {
		
		return new H3("Sessions");
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		
		sessions.setItems(sessionService.getSessions());
		
	}
	
}
