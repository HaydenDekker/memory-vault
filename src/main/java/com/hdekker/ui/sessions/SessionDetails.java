package com.hdekker.ui.sessions;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.hdekker.media.slideshows.sessions.SessionService;
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

@Route(value = "sesh-dets", layout = ApplicationSideBarLayout.class)
public class SessionDetails extends SinglePageCard implements AfterNavigationObserver, HasApplicationViewHeading{

	@Autowired
	SessionUIState state;
	
	TextField sessionName;
	
	@Autowired
	SessionService sessionService;
	
	public SessionDetails() {
		
		OverlayComponent oc = new OverlayComponent();
		AcceptButton ab = new AcceptButton();
		CancelButton cb = new CancelButton();
		oc.add(ab, cb);
		ab.setClassName(UIStyle.BUTTON_BOTTOM_RIGHT);
		cb.setClassName(UIStyle.BUTTON_BOTTOM_LEFT);
		
		cb.addClickListener(e->{
			
			sessionService.delete(state.getSession().get());
			UI.getCurrent().navigate(SessionList.class);
			
		});
		
		add(oc);
		
		sessionName = new TextField("Session Name");
		add(sessionName);
		
		sessionName.addValueChangeListener((v)->{
			state.getSession().get().setName(v.getValue());
			
		});
		
		ab.addClickListener((e)->{
			
			sessionService.updateSession(state.getSession().get());
			UI.getCurrent().navigate(SelectDefinition.class);
		});
		
	}
	
	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		
		if(state.getSession().isEmpty()) {
			UI.getCurrent().navigate(SessionList.class);
			return;
		}
		
		
		state.getSession().ifPresentOrElse((s)-> sessionName.setValue(Optional.ofNullable(s.getName()).orElse("")), 
						()->UI.getCurrent().navigate(SessionList.class));
		
		
	}

	@Override
	public Component getApplicationHeading() {
		
		return new H3("Details");
	}

	
	
}
