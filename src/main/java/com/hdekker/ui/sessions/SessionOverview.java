package com.hdekker.ui.sessions;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayDeque;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.hdekker.media.slideshows.controller.ControllerService;
import com.hdekker.media.slideshows.data.Controller;
import com.hdekker.media.slideshows.data.Definition;
import com.hdekker.media.slideshows.data.MediaDisplayComponent;
import com.hdekker.media.slideshows.definition.DefinitionService;
import com.hdekker.media.slideshows.displays.DisplayService;
import com.hdekker.media.slideshows.sessions.SessionService;
import com.hdekker.mobileapp.components.OverlayComponent;
import com.hdekker.mobileapp.layout.ApplicationSideBarLayout;
import com.hdekker.mobileapp.layout.HasApplicationViewHeading;
import com.hdekker.mobileapp.navigation.components.AcceptButton;
import com.hdekker.mobileapp.style.UIStyle;
import com.hdekker.mobileapp.templates.SinglePageCard;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;

@Route(value = "sesh-overview", layout = ApplicationSideBarLayout.class)
public class SessionOverview extends SinglePageCard implements AfterNavigationObserver, HasApplicationViewHeading{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	static DateTimeFormatter eventDateTimeFormat() {
		DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
		builder.appendPattern("dd/MM/yyyy HH:mm");
		DateTimeFormatter form = builder.toFormatter();
		return form;
	}
	
	
	@Autowired
	SessionUIState state;
	
	@Autowired
	SessionService sessionService;
	
	Label deetLabel = new Label();
	Label deetLastUpdateLabel = new Label();
	Label sessionState = new Label();
	
	Label conLabel = new Label();
	Label dispLabel = new Label();
	Label defLabel = new Label();
	
	
	public SessionOverview() {
		
		HorizontalLayout deetLayout = new HorizontalLayout();
		VerticalLayout deetLabelLayout = new VerticalLayout();
		HorizontalLayout deetNameEdit = new HorizontalLayout();
		HorizontalLayout deetModifiedState = new HorizontalLayout();
		
		HorizontalLayout definitionLayout = new HorizontalLayout();
		HorizontalLayout controllerLayout = new HorizontalLayout();
		HorizontalLayout displayLayout = new HorizontalLayout();
		
		deetLayout.setAlignItems(Alignment.STRETCH);
		
		Button defEdit = new Button(new Icon(VaadinIcon.EDIT));
		defEdit.setId("edit-def");
		defEdit.addClickListener((e)->{
			UI.getCurrent().navigate(SelectDefinition.class);
		});
		Button conEdit = new Button(new Icon(VaadinIcon.EDIT));
		conEdit.setId("edit-con");
		conEdit.addClickListener((e)->{
			UI.getCurrent().navigate(SelectController.class);
		});
		
		Button dispEdit = new Button(new Icon(VaadinIcon.EDIT));
		dispEdit.setId("edit-disp");
		dispEdit.addClickListener((e)-> UI.getCurrent().navigate(SelectDisplay.class));
		
		Button deetEdit = new Button(new Icon(VaadinIcon.EDIT));
		deetEdit.setId("edit-deet");
		deetEdit.addClickListener((e)-> UI.getCurrent().navigate(SessionDetails.class));
		
		deetLayout.add(deetLabelLayout);
		deetLabelLayout.add(deetNameEdit, deetModifiedState);
		deetNameEdit.add(deetLabel, deetEdit);
		deetModifiedState.add(deetLastUpdateLabel, sessionState);
		
		controllerLayout.add(conLabel, conEdit);
		definitionLayout.add(defLabel, defEdit);
		displayLayout.add(dispLabel, dispEdit);
		
		add(deetLayout, definitionLayout,controllerLayout, displayLayout);
		
		OverlayComponent oc = new OverlayComponent();
		AcceptButton ab = new AcceptButton();
		Button cb = new Button(new Icon(VaadinIcon.TRASH));
		ab.setClassName(UIStyle.BUTTON_BOTTOM_RIGHT);
		cb.setClassName(UIStyle.BUTTON_BOTTOM_LEFT);
		oc.add(ab, cb);
		add(oc);
		
		ab.addClickListener((e)->{
			
			state.getSession().ifPresent((s)->{
				
				sessionService.updateSession(state.getSession().get());
				
			});
			
			UI.getCurrent().navigate(SessionList.class);
			
		});
		
		cb.addClickListener((e)-> {
			
			state.getSession().ifPresentOrElse((s)->{
				sessionService.delete(s);
				UI.getCurrent().navigate(SessionList.class);
			}, ()-> {
				UI.getCurrent().navigate(SessionList.class);
			});
			
			
		});
		
	}
	
	private Optional<ArrayDeque<Class<? extends Component>>> getNewQueue(Class<SessionOverview> class1) {
		
		ArrayDeque<Class<? extends Component>> queue = new ArrayDeque<>();
		queue.add(SessionOverview.class);
		return Optional.of(queue);
	}

	@Autowired
	DefinitionService defServ;
	
	@Autowired
	ControllerService conServ;
	
	@Autowired
	DisplayService dispService;
	
	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		
		if(state.getSession().isEmpty()) {
			UI.getCurrent().navigate(SessionList.class);
			return;
		}
		
		Optional<Definition> def = Optional.ofNullable(defServ.getDefinition(state.getSession().get().getSlideShowDefinitionSortKey()));
		Optional<Controller> con = Optional.ofNullable(conServ.getController(state.getSession().get().getControllerSortKey()));
		Optional<MediaDisplayComponent> ses = Optional.ofNullable(dispService.getDisplay(state.getSession().get().getMediaDisplayComponentSortKey()));
		
		deetLabel.setText(state.getSession().get().getName());
		
		deetLastUpdateLabel.setText(Optional.ofNullable(state.getSession().get().getLastModified())
										.map(s-> s.format(SessionOverview.eventDateTimeFormat()))
										.orElse(""));
		sessionState.setText(Optional.ofNullable(state.getSession().get().getSessionState()).map(e-> e.toString()).orElse(""));
		
		def.ifPresent(d->defLabel.setText(
				Optional.ofNullable(d.getDefinitionName()).orElse(""))
		);
		
		con.ifPresent(c->
			conLabel.setText(
					Optional.ofNullable(c.getControllerName()).orElse(""))
		);
		
		ses.ifPresent(s->{
			dispLabel.setText(Optional.ofNullable(s.getDisplayName()).orElse(""));
		});
		
	}

	@Override
	public Component getApplicationHeading() {
		
		return new H3("Session Overview");
	}

}
