package com.hdekker.ui.controller;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.hdekker.media.slideshows.controller.ControllerService;
import com.hdekker.media.slideshows.data.Controller;
import com.hdekker.mobileapp.components.OverlayComponent;
import com.hdekker.mobileapp.layout.ApplicationSideBarLayout;
import com.hdekker.mobileapp.layout.HasApplicationViewHeading;
import com.hdekker.mobileapp.navigation.MenubarNavigable;
import com.hdekker.mobileapp.navigation.NavigableView;
import com.hdekker.mobileapp.navigation.components.AddButton;
import com.hdekker.mobileapp.style.UIStyle;
import com.hdekker.mobileapp.templates.SinglePageCard;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;

@SpringComponent
@Scope(value = "prototype")
@Route(value = "controller-list", layout = ApplicationSideBarLayout.class)
public class ControllerList extends SinglePageCard implements HasApplicationViewHeading,
												AfterNavigationObserver, MenubarNavigable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Logger log = LoggerFactory.getLogger(ControllerList.class);
	
	@Autowired
	ControllerService controllerService;
	
	@Autowired
	ControllerUIState state;
	
	public List<Controller> controllersList;
	Grid<Controller> controllers = new Grid<>();
	
	AddButton ab;
	
	public ControllerList() {
		
		OverlayComponent oc = new OverlayComponent();
		ab = new AddButton();
		ab.setClassName(UIStyle.BUTTON_BOTTOM_RIGHT);
		oc.add(ab);
		add(oc);
		
		controllers.addItemClickListener(item->{
			
			state.setActiveController(Optional.of(item.getItem()));
			UI.getCurrent().navigate(ControllerOverview.class);
			
		});
		controllers.addColumn(Controller::getControllerName).setHeader("Controller Name");
		add(controllers);
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		
		ab.addClickListener((e)-> {
			
			state.setActiveController(Optional.empty());
			state.setPreviousNavigationTarget(Optional.of(ControllerList.class));
			UI.getCurrent().navigate(ControllerDetails.class);
			
		});

		controllersList = controllerService.findAll();
		controllers.setItems(controllersList);
		
	}

	@Override
	public Component getApplicationHeading() {
		
		return new H3("Controllers");
	}

	@Override
	public Supplier<NavigableView> getNavigableView() {
		// TODO Auto-generated method stub
		return ()->new NavigableView(ControllerList.class, "Controllers");
	}

}
