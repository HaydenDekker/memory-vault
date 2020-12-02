package com.hdekker.ui.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.hdekker.media.slideshows.controller.ControllerService;
import com.hdekker.mobileapp.components.OverlayComponent;
import com.hdekker.mobileapp.layout.ApplicationSideBarLayout;
import com.hdekker.mobileapp.layout.HasApplicationViewHeading;
import com.hdekker.mobileapp.navigation.components.AcceptButton;
import com.hdekker.mobileapp.navigation.components.CancelButton;
import com.hdekker.mobileapp.style.UIStyle;
import com.hdekker.mobileapp.templates.SinglePageCard;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;

@Route(value = "controller-details", layout = ApplicationSideBarLayout.class)
public class ControllerDetails extends SinglePageCard implements HasApplicationViewHeading,
																AfterNavigationObserver{

	@Autowired
	ControllerUIState state;
	
	@Autowired
	ControllerService cs;
	
	TextField controllerName = new TextField("Controller Name");
	
	public ControllerDetails() {
		OverlayComponent oc = new OverlayComponent();
		
		AcceptButton ab = new AcceptButton();
		ab.setClassName(UIStyle.BUTTON_BOTTOM_RIGHT);
		CancelButton cb = new CancelButton();
		cb.setClassName(UIStyle.BUTTON_BOTTOM_LEFT);
		oc.add(ab,cb);
		add(oc);
		
		ab.addClickListener((e)->{
			
			// update state
			state.getActiveController().ifPresentOrElse((c)->{
				
				c.setControllerName(controllerName.getValue());
				cs.updateController(c);

			}, 
			()->{
				state.setActiveController(
						Optional.of(
								cs.createNewController(controllerName.getValue())
				));
			});
			
			// navigate
			UI.getCurrent().navigate(ControllerOverview.class);
			
		});
		
		cb.addClickListener((c)->{
			
			Class<? extends Component> previousTarget = state.getPreviousNavigationTarget().get();
			UI.getCurrent().navigate(previousTarget);
			
		});
		
		setJustifyContentMode(JustifyContentMode.CENTER);
		
		add(controllerName);
		
	}
	
	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		
		state.previousNavigationTarget.ifPresentOrElse((t)->{
			
			if(t.isAssignableFrom(ControllerOverview.class)) {
			
				controllerName.setValue(
						state.getActiveController().get().getControllerName()
				);
			}
		}, () -> UI.getCurrent().navigate(ControllerList.class));
		
	}

	@Override
	public Component getApplicationHeading() {
		
		return new H3("Controller Details");
	}

	
	
}
