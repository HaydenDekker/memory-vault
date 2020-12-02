package com.hdekker.ui.definitions;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hdekker.database.PushLayout;
import com.hdekker.media.slideshows.data.Media;
import com.hdekker.media.slideshows.definition.DefinitionService;
import com.hdekker.mobileapp.components.OverlayComponent;
import com.hdekker.mobileapp.layout.HasApplicationViewHeading;
import com.hdekker.mobileapp.navigation.components.AcceptButton;
import com.hdekker.mobileapp.style.UIStyle;
import com.hdekker.mobileapp.templates.SinglePageCard;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;

@Route(value = "def-add-media", layout = PushLayout.class)
public class DefinitionAddMedia extends SinglePageCard implements HasApplicationViewHeading,
																	AfterNavigationObserver{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Logger log = LoggerFactory.getLogger(DefinitionAddMedia.class);
	
	@Autowired
	DefinitionService definitionService;
	
	Grid<Media> availableMedia = new Grid<>();
	
	UI currentUI;
	
	@Autowired
	DefinitionUIState state;
	
	public DefinitionAddMedia() {
		
		
		add(availableMedia); 
		
		OverlayComponent oc = new OverlayComponent();
		AcceptButton ab = new AcceptButton();
		oc.add(ab);
		add(oc);
		ab.setClassName(UIStyle.BUTTON_BOTTOM_RIGHT);
		
		ab.addClickListener((e)-> UI.getCurrent().navigate(DefinitionOverview.class));
		
		availableMedia.addColumn(Media::getKey).setHeader("URL");
		
		availableMedia.addSelectionListener((sel)->{
			
			sel.getFirstSelectedItem().ifPresent((item)->{
				definitionService.addMediaToDefinition(state.getSelectedDefinition().get(), item);
			});
		 
		});
	
	}
	
	@Override
	public H3 getApplicationHeading() {
		
		return new H3("Add Media"); 
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		
		this.currentUI = this.getUI().get();
		if(state.getSelectedDefinition().isEmpty()) {
			UI.getCurrent().navigate(DefinitionList.class);
			return;
		}
		
		CompletableFuture<List<Media>> media = definitionService.getMedia();
		media.thenAcceptAsync((list)->{
			currentUI.access(()->{
				
				availableMedia.setItems(list);
				currentUI.push();
				
			});
		});
		
	}

}
