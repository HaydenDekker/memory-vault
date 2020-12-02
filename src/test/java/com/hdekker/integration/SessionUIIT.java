package com.hdekker.integration;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.github.mvysny.kaributesting.v10.spring.MockSpringServlet;
import com.hdekker.media.slideshows.controller.ControllerService;
import com.hdekker.media.slideshows.data.Controller;
import com.hdekker.media.slideshows.data.Definition;
import com.hdekker.media.slideshows.data.MediaDisplayComponent;
import com.hdekker.media.slideshows.data.SSSession;
import com.hdekker.media.slideshows.definition.DefinitionService;
import com.hdekker.media.slideshows.displays.DisplayService;
import com.hdekker.media.slideshows.sessions.SessionService;
import com.hdekker.mobileapp.navigation.components.AcceptButton;
import com.hdekker.mobileapp.navigation.components.AddButton;
import com.hdekker.mobileapp.navigation.components.CancelButton;
import com.hdekker.ui.sessions.SelectController;
import com.hdekker.ui.sessions.SelectDefinition;
import com.hdekker.ui.sessions.SelectDisplay;
import com.hdekker.ui.sessions.SessionDetails;
import com.hdekker.ui.sessions.SessionList;
import com.hdekker.ui.sessions.SessionOverview;
import com.hdekker.ui.sessions.SessionUIState;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.SpringServlet;

import kotlin.jvm.functions.Function0;

import static com.github.mvysny.kaributesting.v10.GridKt.*;
import static com.github.mvysny.kaributesting.v10.LocatorJ.*;

@SpringBootTest
public class SessionUIIT {

	private static Routes routes = null;
	
	 @Autowired
	 private ApplicationContext ctx;
	    
	
    @BeforeAll
    public static void createRoutes() {
        routes = new Routes().autoDiscoverViews("com.hdekker.ui");
    }

    @BeforeEach
    public void setupVaadin() {
        
    	final Function0<UI> uiFactory = UI::new;
        final SpringServlet servlet = new MockSpringServlet(routes, ctx, uiFactory);
        MockVaadin.setup(uiFactory, servlet);
        
    }
    
    @Autowired
    DefinitionService defService;
    

    @Autowired
    ControllerService controllerService;
    
    @Autowired
    DisplayService displayService;
    
    String testControllerKey;
    String testDisplayKey;
    String testDefinitionKey;
    
    Definition def;
	MediaDisplayComponent mdc;
	Controller con;
    
    SSSession session;
    
    @Autowired
    SessionService ss;
    
	@BeforeEach
	public void addATestEntity() {
		
		
		
		// definition
		List<Definition> definitions = defService.findAllDefinitions();
		definitions.stream().filter(f->f.getDefinitionName().contains("test-definition")).findFirst()
							.ifPresentOrElse((x)->{testDefinitionKey = x.getKey();}, ()->{ 
								def = defService.createNewDefinition("test-definition", "just a test description.");
								testDefinitionKey = def.getSortKey();
							});
	
		// display
		List<MediaDisplayComponent> displays = displayService.getDisplayComponents();
		displays.stream().filter(f->f.getDisplayName().contains("test-display")).findFirst()
							.ifPresentOrElse((x)->{testDisplayKey = x.getKey();}, ()->{
								mdc = displayService.createNewDisplay("test-display");
								testDisplayKey = mdc.getSortKey();
							});
	
		
		// controller
		List<Controller> controllers = controllerService.findAll();
		controllers.stream().filter(f->f.getControllerName().contains("test-controller")).findFirst()
							.ifPresentOrElse((x)->{testControllerKey = x.getKey();}, ()->{	
									con = controllerService.createNewController("test-controller");
									testControllerKey = con.getSortKey();
							});
		
		session = ss.createSession("Test Sesh", mdc.getSortKey(), def.getSortKey(), con.getSortKey());
		
		
	}
	
	@AfterEach
	public void deleteTestEntity() {
			
		// definition
		List<Definition> definitions = defService.findAllDefinitions();
		definitions.stream().filter(f->f.getDefinitionName().contains("test-definition")).findFirst()
							.ifPresentOrElse((x)->{defService.deleteDefinition(x);}, ()->{});			
		
		// display
		List<MediaDisplayComponent> displays = displayService.getDisplayComponents();
		displays.stream().filter(f->f.getDisplayName().contains("test-display")).findFirst()
							.ifPresentOrElse((x)->{displayService.delete(x);}, ()->{});
	
		
		// controller
		List<Controller> controllers = controllerService.findAll();
		controllers.stream().filter(f->f.getControllerName().contains("test-controller")).findFirst()
							.ifPresentOrElse((x)->{controllerService.delete(x);}, ()->{});
		
		ss.delete(session);
		
	}
	
	@Test
	public void checkEntitiesExist() {
		
		Optional.of(testControllerKey);
		Optional.of(testDefinitionKey);
		Optional.of(testDisplayKey);
		Optional.of(controllerService.getController(testControllerKey));
		Optional.of(defService.getDefinition(testDefinitionKey));
		Optional.of(displayService.getDisplay(testDisplayKey));
		
	}
	
	@Test
	public void canCreateANewSession() {
		
		UI.getCurrent().navigate(SessionList.class);
		_get(AddButton.class).click();
		_get(SessionDetails.class);
		
	}
	
	@Test
	public void setDetailsForSession() {
		
		SessionUIState state = ctx.getBean(SessionUIState.class);
		state.setSession(Optional.of(session));
		
		UI.getCurrent().navigate(SessionDetails.class);
		_get(TextField.class, src->src.withCaption("Session Name")).setValue("Just a test");
		
		_click(_get(AcceptButton.class));
		
		assertTrue(state.getSession().get().getName().equals("Just a test"));
		
	}
	
	@Test
	public void selectDefinitionForSession() {
		
		SessionUIState state = ctx.getBean(SessionUIState.class);
		state.setSession(Optional.of(session));
		
		UI.getCurrent().navigate(SelectDefinition.class);
		SelectDefinition selDef = _get(SelectDefinition.class);
		
		// select definition
		Grid<Definition> defgrid = _get(Grid.class);
		Definition defToSelect = selDef.currentDefinitions.stream()
							.filter(de->de.getSortKey().equals(testDefinitionKey))
							.findFirst().get();
		
		_clickItem(defgrid, selDef.currentDefinitions.indexOf(defToSelect));
		 
		// Expect next UI and correct state
		_get(SelectDisplay.class);
		// must have set def
		state.getSession().ifPresentOrElse((d)->{}, ()->fail());
		
	}
	
	@Test
	public void selectDisplayForASession() {
		
		SessionUIState state = ctx.getBean(SessionUIState.class);
		state.setSession(Optional.of(session));
		
		UI.getCurrent().navigate(SelectDisplay.class);
		SelectDisplay sd = _get(SelectDisplay.class);
		
		// Select a media display
		Grid<MediaDisplayComponent> mdcGrid = _get(Grid.class);
		MediaDisplayComponent item = sd.displays.stream().filter(d->d.getSortKey().equals(testDisplayKey))
						.findFirst().get();
		
		_clickItem(mdcGrid, sd.displays.indexOf(item));
		
		// Expect next UI and correct state
		_get(SelectController.class);
		state.getSession().ifPresentOrElse((d)->{}, ()->fail());
		
	}
	
	@Test
	public void selectControllerForASession() {
		
		SessionUIState state = ctx.getBean(SessionUIState.class);
		state.setSession(Optional.of(session));
		
		UI.getCurrent().navigate(SelectController.class);
		SelectController sc = _get(SelectController.class);
		
		// Select a controller
		Grid<Controller> controllerGrid = _get(Grid.class);
		Controller item = sc.controllers.stream().filter(c->c.getSortKey().equals(testControllerKey))
								.findFirst().get();
		_clickItem(controllerGrid, sc.controllers.indexOf(item));
		
		// Expect next UI and correct state
		_get(SessionOverview.class);
		state.getSession().ifPresentOrElse((d)->{}, ()->fail());
		
	}
	
	@Test
	public void overviewCanDisplayDetailsOfASession() {
		
		// Load test objects to state
		SessionUIState state = ctx.getBean(SessionUIState.class);
		state.setSession(Optional.of(session));
		
		// 
		UI.getCurrent().navigate(SessionOverview.class);
		
		// expect
		_get(Label.class, spec->spec.withText("test-definition"));
		DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
		builder.appendPattern("dd/MM/yyyy HH:mm");
		DateTimeFormatter form = builder.toFormatter();
		_get(Text.class, spec->spec.withText(session.getLastModified().format(form)));
		
	}
	
	@Test
	public void canEditASessionDefinition() {
		
		// Load test objects to state
		SessionUIState state = ctx.getBean(SessionUIState.class);
		state.setSession(Optional.of(session));
		
		// 
		UI.getCurrent().navigate(SessionOverview.class);
		
		// expect
		_get(Label.class, spec->spec.withText("test-definition"));
		
		_get(Button.class, spec->spec.withId("edit-def")).click();
		
		_get(SelectDefinition.class);
		
	}
	
	@Test
	public void canCancelAEditOnASessionDefinition() {
		
		// Load test objects to state
		SessionUIState state = ctx.getBean(SessionUIState.class);
		state.setSession(Optional.of(session));
		
		// 
		UI.getCurrent().navigate(SessionOverview.class);
		
		// expect
		_get(Label.class, spec->spec.withText("test-definition"));
		
		_get(Button.class, spec->spec.withId("edit-def")).click();
		
		_get(SelectDefinition.class);
		
		_get(CancelButton.class).click();
		
		// TODO check data consistancy
		_get(SessionOverview.class);
		
	}
	
	@Autowired
	SessionService sessionService;
	
	@Test
	public void canSetSessionActive() {
		
		Definition def = defService.getDefinition(testDefinitionKey);
		Controller con = controllerService.getController(testControllerKey);
		MediaDisplayComponent mdc = displayService.getDisplay(testDisplayKey);
		sessionService.createSession("Test Sesh", mdc.getSortKey(), def.getSortKey(), con.getSortKey());
		
		UI.getCurrent().navigate(SessionList.class);
		SessionList sessions = _get(SessionList.class);
		
		
	}
	
	
}
