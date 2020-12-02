package com.hdekker.ui.display;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.hdekker.media.slideshows.data.MediaDisplayComponent;
import com.hdekker.media.slideshows.displays.DisplayService;
import com.hdekker.mobileapp.layout.ApplicationSideBarLayout;
import com.hdekker.mobileapp.layout.HasApplicationViewHeading;
import com.hdekker.mobileapp.navigation.MenubarNavigable;
import com.hdekker.mobileapp.navigation.NavigableView;
import com.hdekker.mobileapp.templates.SinglePageCard;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;

@SpringComponent
@Scope(value = "prototype")
@Route(value = "display-list", layout = ApplicationSideBarLayout.class)
public class DisplayList extends SinglePageCard implements AfterNavigationObserver,HasApplicationViewHeading, MenubarNavigable{

	Grid<MediaDisplayComponent> displays = new Grid<>();
	
	@Autowired
	DisplayService service;
	
	public DisplayList() {
	
		displays.addColumn(MediaDisplayComponent::getDisplayName).setHeader("Display Name");
		add(displays);
		
	}
	
	@Override
	public Component getApplicationHeading() {
		
		return new H3("Displays");
	}

	@Override
	public Supplier<NavigableView> getNavigableView() {
		
		return ()->new NavigableView(DisplayList.class, "Displays");
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		
		displays.setItems(service.getDisplayComponents());
		
	}

	
	
}
