package com.hdekker.ui.sessions;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.hdekker.media.slideshows.data.Definition;
import com.hdekker.media.slideshows.definition.DefinitionService;
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
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;

@Route(value = "sesh-sel-def", layout = ApplicationSideBarLayout.class)
public class SelectDefinition extends SinglePageCard implements AfterNavigationObserver, HasApplicationViewHeading{

	@Autowired
	DefinitionService definitions;
	
	Grid<Definition> allDefs = new Grid<Definition>();
	
	public List<Definition> currentDefinitions;
	
	@Autowired
	SessionUIState state;
	
	@Autowired
	SessionService ss;
	
	@Autowired
	DefinitionService ds;
	
	public SelectDefinition() {
		
		OverlayComponent oc = new OverlayComponent();
		CancelButton cb = new CancelButton();
		oc.add( cb);
		cb.setClassName(UIStyle.BUTTON_BOTTOM_LEFT);
		cb.addClickListener((e)->{
			
			// No state change here, just navigate to overview
			UI.getCurrent().navigate(SessionOverview.class);
			
		});
		
		add(oc);
		add(allDefs);
		
		allDefs.addColumn(Definition::getDefinitionName).setHeader("Definiton Name");
		allDefs.addItemClickListener((item)->{
			
			state.getSession().get().setSlideShowDefinitionSortKey(item.getItem().getSortKey());
			ss.updateSession(state.getSession().get());
			UI.getCurrent().navigate(SelectDisplay.class);
			
		});
	
	}
	
	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		
		if(state.getSession().isEmpty()) {
			UI.getCurrent().navigate(SessionList.class);
			return;
		}
		
		currentDefinitions = definitions.findAllDefinitions();
		allDefs.setItems(currentDefinitions);
		
	}

	@Override
	public Component getApplicationHeading() {
		
		return new H3("Select Definition");
	}

}
