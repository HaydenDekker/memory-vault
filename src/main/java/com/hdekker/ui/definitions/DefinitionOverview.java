package com.hdekker.ui.definitions;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.hdekker.media.slideshows.data.Media;
import com.hdekker.media.slideshows.definition.DefinitionService;
import com.hdekker.mobileapp.components.OverlayComponent;
import com.hdekker.mobileapp.layout.ApplicationSideBarLayout;
import com.hdekker.mobileapp.layout.HasApplicationViewHeading;
import com.hdekker.mobileapp.navigation.components.AcceptButton;
import com.hdekker.mobileapp.style.UIStyle;
import com.hdekker.mobileapp.templates.SinglePageCard;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.dnd.GridDropLocation;
import com.vaadin.flow.component.grid.dnd.GridDropMode;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;

@Component
@Scope(value = "prototype")
@Route(value = "def-slide-show-overview", layout = ApplicationSideBarLayout.class)
public class DefinitionOverview extends SinglePageCard implements HasApplicationViewHeading, 
														 AfterNavigationObserver{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Grid<Media> slideShowMedia = new Grid<>();
	
	@Autowired
	DefinitionUIState state;

	private Media dragItem;
	
	@Autowired
	DefinitionService definitionService;
	
	public DefinitionOverview() {
		
		OverlayComponent oc = new OverlayComponent();
		Button addFile = new Button(new Icon(VaadinIcon.MOVIE));
		oc.add(addFile);
		addFile.setClassName(UIStyle.BUTTON_BOTTOM_LEFT);
		AcceptButton acceptButton = new AcceptButton();
		acceptButton.setClassName(UIStyle.BUTTON_BOTTOM_RIGHT);
		oc.add(acceptButton);
		 
		add(oc);
		
		addFile.addClickListener((e)-> UI.getCurrent().navigate(DefinitionAddMedia.class));
		acceptButton.addClickListener((e)-> UI.getCurrent().navigate(DefinitionList.class));
		
		add(slideShowMedia);
		
		slideShowMedia.addColumn(Media::getKey).setHeader("URL");
		
		slideShowMedia.addDragStartListener(event->{
			dragItem = event.getDraggedItems().get(0);
			slideShowMedia.setDropMode(GridDropMode.BETWEEN);
		});
		
		slideShowMedia.addDragEndListener(event -> {
		    dragItem = null;
		    slideShowMedia.setDropMode(null);
		});
		
		slideShowMedia.addDropListener(event->{
			
			Optional<Media> dropItem = event.getDropTargetItem();
			dropItem.ifPresent(item->{
				
				
				
				if (!item.equals(dragItem)) {
					List<Media> urls = state.getSelectedDefinition().get().getMedia();
					urls.remove(dragItem);
					Integer dropLoc = urls.indexOf(item) + (event.getDropLocation() == GridDropLocation.BELOW ? 1
	                        : 0);
					urls.add(dropLoc, dragItem);
					definitionService.updateDefinition(state.getSelectedDefinition().get());
					slideShowMedia.getDataProvider().refreshAll();
				}
				
			});
			
		});
		
		slideShowMedia.setSelectionMode(SelectionMode.NONE);
		slideShowMedia.setRowsDraggable(true);
		
	}

	@Override
	public HorizontalLayout getApplicationHeading() {
	
		HorizontalLayout hl = new HorizontalLayout();
		if(state.getSelectedDefinition().isPresent()) {
			hl.add(new H3(state.getSelectedDefinition().get().getDefinitionName()));
		}
		Button editButton = new Button(new Icon(VaadinIcon.EDIT));
		hl.add(editButton);
		
		hl.setAlignItems(Alignment.BASELINE);
		
		editButton.addClickListener((e)-> { 
			
			state.setCancelButtonNavTarget(DefinitionOverview.class);
			// Current definition remains
			UI.getCurrent().navigate(DefinitionDetails.class);
		});
		
		return hl;
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		
		if(state.getSelectedDefinition().isEmpty()) {
			UI.getCurrent().navigate(DefinitionList.class);
			return;
		}
		
		if(state.getSelectedDefinition().get().getMedia()!=null) {
			slideShowMedia.setItems(state.getSelectedDefinition().get().getMedia());
		}
	}
	
}
