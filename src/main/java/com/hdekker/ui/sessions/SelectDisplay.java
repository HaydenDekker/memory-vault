package com.hdekker.ui.sessions;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.hdekker.media.slideshows.data.MediaDisplayComponent;
import com.hdekker.media.slideshows.displays.DisplayService;
import com.hdekker.media.slideshows.sessions.SessionService;
import com.hdekker.mobileapp.components.OverlayComponent;
import com.hdekker.mobileapp.layout.ApplicationSideBarLayout;
import com.hdekker.mobileapp.layout.HasApplicationViewHeading;
import com.hdekker.mobileapp.navigation.components.CancelButton;
import com.hdekker.mobileapp.style.UIStyle;
import com.hdekker.mobileapp.templates.SinglePageCard;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;

@Route(value = "sesh-sel-disp", layout = ApplicationSideBarLayout.class)
public class SelectDisplay extends SinglePageCard implements AfterNavigationObserver, HasApplicationViewHeading{

	VerticalLayout gridOrNoneHolder = new VerticalLayout();
	
	Grid<MediaDisplayComponent> displayGrid = new Grid<MediaDisplayComponent>();
	
	public List<MediaDisplayComponent> displays;
	
	@Autowired
	SessionUIState state;
	
	public SelectDisplay() {
		
		OverlayComponent oc = new OverlayComponent();
		CancelButton cb = new CancelButton();
		oc.add(cb);
		add(oc);
		cb.setClassName(UIStyle.BUTTON_BOTTOM_LEFT);
		cb.addClickListener((e)->{
			
			// No state change here, just navigate to overview
			UI.getCurrent().navigate(SessionOverview.class);
			
		});
		
		add(gridOrNoneHolder);
		displayGrid.addColumn(MediaDisplayComponent::getDisplayName).setHeader("Name");
		gridOrNoneHolder.setSizeFull();
		
		displayGrid.addItemClickListener(item->{
			
			state.getSession().get().setMediaDisplayComponentSortKey(item.getItem().getSortKey());
			ss.updateSession(state.getSession().get());
			UI.getCurrent().navigate(SelectController.class);
			
		});
		
	}
	
	@Autowired
	DisplayService ds;
	
	@Autowired
	SessionService ss;
	
	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		
		if(state.getSession().isEmpty()) {
			UI.getCurrent().navigate(SessionList.class);
			return;
		}
		
		gridOrNoneHolder.removeAll();
		
		displays = ds.getDisplayComponents();
		if(displays.size()>0) {
			
			displayGrid.setItems(displays);
			gridOrNoneHolder.add(displayGrid);
			
		}else {
			
			gridOrNoneHolder.add(new Label("Connect a display before creating a session."));
			
		}
		
	}

	@Override
	public Component getApplicationHeading() {
		
		return new H3("Select Display");
	}

}
