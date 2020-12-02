package com.hdekker.ui.sessions;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.hdekker.media.slideshows.controller.ControllerService;
import com.hdekker.media.slideshows.data.Controller;
import com.hdekker.media.slideshows.sessions.SessionService;
import com.hdekker.mobileapp.components.OverlayComponent;
import com.hdekker.mobileapp.layout.ApplicationSideBarLayout;
import com.hdekker.mobileapp.layout.HasApplicationViewHeading;
import com.hdekker.mobileapp.navigation.components.CancelButton;
import com.hdekker.mobileapp.templates.SinglePageCard;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;

@Route(value = "sesh-sel-con", layout = ApplicationSideBarLayout.class)
public class SelectController extends SinglePageCard implements AfterNavigationObserver, HasApplicationViewHeading{

	VerticalLayout holder = new VerticalLayout();
	
	Grid<Controller> controllerGrid = new Grid<>();
	
	public List<Controller> controllers;
	
	@Autowired
	ControllerService controllerService;
	
	@Autowired
	SessionUIState state;
	
	@Autowired
	SessionService ss;
	
	public SelectController() {
		
		OverlayComponent oc = new OverlayComponent();
		CancelButton cb = new CancelButton();
		add(oc);
		oc.add(cb);
		add(holder);
		
		cb.addClickListener((e)->{
			
			// No state change here, just navigate to overview
			UI.getCurrent().navigate(SessionOverview.class);
			
		});
		
		controllerGrid.addColumn(Controller::getControllerName).setHeader("Controller Name");
		controllerGrid.addItemClickListener((item)->{
			
			state.getSession().get().setControllerSortKey(item.getItem().getSortKey());
			UI.getCurrent().navigate(SessionOverview.class);
			
		});
		
	}
	
	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		
		if(state.getSession().isEmpty()) {
			UI.getCurrent().navigate(SessionList.class);
			return;
		}
		
		holder.removeAll();
		controllers = controllerService.findAll();
		if(controllers.size()>0) {
			controllerGrid.setItems(controllers);
			holder.add(controllerGrid);
		}else {
			holder.add(new H3("Add a new controller before creating a seesion"));
		}
	}

	@Override
	public Component getApplicationHeading() {
		
		return new H3("Select Controller");
	}

}
