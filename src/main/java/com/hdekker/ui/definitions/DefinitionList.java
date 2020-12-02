package com.hdekker.ui.definitions;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.hdekker.media.slideshows.data.Definition;
import com.hdekker.media.slideshows.definition.DefinitionService;
import com.hdekker.mobileapp.components.OverlayComponent;
import com.hdekker.mobileapp.layout.ApplicationSideBarLayout;
import com.hdekker.mobileapp.navigation.MenubarNavigable;
import com.hdekker.mobileapp.navigation.NavigableView;
import com.hdekker.mobileapp.navigation.components.AddButton;
import com.hdekker.mobileapp.style.UIStyle;
import com.hdekker.mobileapp.templates.SinglePageCard;
import com.hdekker.mobileapp.layout.HasApplicationViewHeading;

import com.hdekker.users.UserConfig;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;

@Component
@Scope(value = "prototype")
@Route(value = "def-list", layout = ApplicationSideBarLayout.class)
public class DefinitionList extends SinglePageCard implements AfterNavigationObserver, MenubarNavigable, HasApplicationViewHeading{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Grid<Definition> definitionList = new Grid<>();
	
	@Autowired
	DefinitionService dbService;
	
	@Autowired
	UserConfig userConfig;
	
	VerticalLayout view;
	
	@Autowired
	DefinitionUIState state;
	
	public DefinitionList() {
		
		OverlayComponent buttons = new OverlayComponent();
		AddButton addButton = new AddButton();
		addButton.setClassName(UIStyle.BUTTON_BOTTOM_RIGHT);
		addButton.addClickListener((e)-> {
			// Always clear definition.
			state.setSelectedDefinition(Optional.empty());
			state.setCancelButtonNavTarget(DefinitionList.class);
			UI.getCurrent().navigate(DefinitionDetails.class);
		});
		buttons.add(addButton); 
		
		add(buttons);
		
		view = new VerticalLayout();
		
		add(view);
		
		definitionList.addSelectionListener((sel)-> {
			sel.getFirstSelectedItem().ifPresent(def->{
				state.setSelectedDefinition(Optional.of(def));
				UI.getCurrent().navigate(DefinitionOverview.class);
			});
		});
		definitionList.addColumn(Definition::getDefinitionName).setHeader("Slide Show");
		definitionList.addColumn(Definition::getDefinitionDescription).setHeader("Description");
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		List<Definition> defs = dbService.findAllDefinitions();
		if(defs.size()>0) {
			view.add(definitionList);
			definitionList.setItems(defs);
		}else {
			view.add(new Label("Add a new definition to begin"));	
		}
		
	}

	@Override
	public Supplier<NavigableView> getNavigableView() {
		
		return ()->new NavigableView(DefinitionList.class, "Slide Shows");
	}

	@Override
	public H3 getApplicationHeading() {
		
		return new H3("Slide Shows");
	}
}
