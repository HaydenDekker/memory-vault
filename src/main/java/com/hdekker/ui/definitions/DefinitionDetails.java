package com.hdekker.ui.definitions;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.hdekker.media.slideshows.data.Definition;
import com.hdekker.media.slideshows.definition.DefinitionService;
import com.hdekker.mobileapp.components.OverlayComponent;
import com.hdekker.mobileapp.layout.ApplicationSideBarLayout;
import com.hdekker.mobileapp.layout.HasApplicationViewHeading;
import com.hdekker.mobileapp.navigation.components.CancelButton;
import com.hdekker.mobileapp.style.UIStyle;
import com.hdekker.mobileapp.templates.SinglePageCard;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;

@Route(value = "definition-details", layout = ApplicationSideBarLayout.class)
public class DefinitionDetails extends SinglePageCard implements HasApplicationViewHeading,
														AfterNavigationObserver{

	// TreeGrid<TestObj> slideShowMedia = new TreeGrid<>();
	
//	class TestObj{
//		String testItem;
//		public TestObj(String test) {
//			this.testItem = test;
//		}
//		public String getTestItem() {
//			return testItem;
//		}
//	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	DefinitionService definitionService;
	
	@Autowired
	DefinitionUIState state;
	
	TextField slideShowName = new TextField("Name");
	TextArea slideShowDescription = new TextArea("Description");
	
	public DefinitionDetails() {
		
		FormLayout hl = new FormLayout();
		hl.add(slideShowName, slideShowDescription);
		
		slideShowName.addValueChangeListener(v-> saveOrUpdate());
		slideShowDescription.addValueChangeListener(v-> saveOrUpdate());
		
		add(hl); 
		
		Button done = new Button(new Icon(VaadinIcon.CHECK));
		done.setClassName(UIStyle.BUTTON_BOTTOM_RIGHT);
		CancelButton cancelButton = new CancelButton();
		cancelButton.setClassName(UIStyle.BUTTON_BOTTOM_LEFT);
		OverlayComponent oc = new OverlayComponent();
		oc.add(done, cancelButton);
		add(oc);
		
		cancelButton.addClickListener((e)->{
			
			if(state.getCancelButtonNavTarget().isAssignableFrom(DefinitionList.class)) {
				UI.getCurrent().navigate(DefinitionList.class);
			}
			if(state.getCancelButtonNavTarget().isAssignableFrom(DefinitionOverview.class)) {
				UI.getCurrent().navigate(DefinitionOverview.class);
			}
			
		});
		
		done.addClickListener((e) -> {
			
			if(state.getSelectedDefinition().isEmpty()) {
				Definition def = definitionService.createNewDefinition(slideShowName.getValue(), slideShowDescription.getValue());
				state.setSelectedDefinition(Optional.of(def));
			}else {
				state.getSelectedDefinition().get().setDefinitionName(slideShowName.getValue());
				state.getSelectedDefinition().get().setDefinitionDescription(slideShowDescription.getValue());
				definitionService.updateDefinition(state.getSelectedDefinition().get());
			}
			
			UI.getCurrent().navigate(DefinitionOverview.class);
		
		});
		
		
		// Main view scrolls through file system list with preview of media.
		// Selecting media adds to the slide show.
		
		
		// Bottom Slide Up Panel
		// Allows user to see selection and make modifications to the slide show order
		
//		TestObj p1 = new TestObj("p1");
//		TestObj pp1 = new TestObj("pp1");
//		TestObj c1 = new TestObj("c1");
//		TestObj c3 = new TestObj("c3");
//		TestObj p2 = new TestObj("p2");
//		TestObj c2 = new TestObj("c2");
//		
//		List<TestObj> parents = Arrays.asList(p1, pp1, p2);
//		List<TestObj> ch1 = Arrays.asList(c1, c3);
//		List<TestObj> ch2 = Arrays.asList(c2);
//		HashMap<TestObj, List<TestObj>> items = new HashMap<DefinitionUI.TestObj, List<TestObj>>();
//		items.put(p1, Arrays.asList(pp1));
//		items.put(pp1, ch1);
//		items.put(p2, ch2);
//		
//		TreeData<TestObj> td = new TreeData<DefinitionUI.TestObj>();
//		td.addItems(null, p1);
//		td.addItem(null, p2);
//		td.addItems(p1, Arrays.asList(pp1));
//		td.addItems(pp1, ch1);
//		td.addItems(p2, ch2);
//		
//		
//		slideShowMedia.setTreeData(td);
//		add(slideShowMedia);
//		slideShowMedia.addHierarchyColumn(TestObj::getTestItem).setHeader("test");
		
	}

	// User can either be creating a new object or updating an existing
	private void saveOrUpdate() {
		
	}

	@Override
	public Component getApplicationHeading() {
		
		if(state.getCancelButtonNavTarget()==null) UI.getCurrent().navigate(DefinitionList.class);
		
		HorizontalLayout hl = new HorizontalLayout();
		Button delButton = new Button(
				new Icon(VaadinIcon.TRASH)); 
		hl.add(new H3("Details"));
		
		delButton.addClickListener((e)-> {
		
			definitionService.deleteDefinition(state.getSelectedDefinition().get());
			UI.getCurrent().navigate(DefinitionList.class);
		});
		
		if(state.getCancelButtonNavTarget().isAssignableFrom(DefinitionOverview.class)) {
			hl.add(delButton);
		}
		
		return hl;
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		
		if(state.getSelectedDefinition().isPresent()) {
			this.slideShowDescription.setValue(state.getSelectedDefinition().get().getDefinitionDescription());
			this.slideShowName.setValue(state.getSelectedDefinition().get().getDefinitionName());
		} else {
			this.slideShowName.setValue("");
			this.slideShowDescription.setValue("");
		}
	}
	
}
