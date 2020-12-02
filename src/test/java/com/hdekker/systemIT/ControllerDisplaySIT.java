package com.hdekker.systemIT;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.github.mvysny.kaributesting.v10.spring.MockSpringServlet;
import com.hdekker.media.slideshows.controller.ControllerCommand;
import com.hdekker.media.slideshows.controller.ControllerService;
import com.hdekker.media.slideshows.data.Controller;
import com.hdekker.media.slideshows.data.Definition;
import com.hdekker.media.slideshows.data.MediaDisplayComponent;
import com.hdekker.media.slideshows.data.SSSession;
import com.hdekker.media.slideshows.definition.DefinitionService;
import com.hdekker.media.slideshows.displays.DisplayService;
import com.hdekker.media.slideshows.sessions.SessionService;
import com.hdekker.ui.controller.ControllerOverview;
import com.hdekker.ui.controller.ControllerUIState;
import com.hdekker.ui.display.MemoryVault;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.spring.SpringServlet;

import kotlin.jvm.functions.Function0;

import static com.github.mvysny.kaributesting.v10.GridKt.*;
import static com.github.mvysny.kaributesting.v10.LocatorJ.*;


@SpringBootTest
public class ControllerDisplaySIT {
	
	Logger log = LoggerFactory.getLogger(ControllerDisplaySIT.class);
	
	private static Routes routes = null;
	
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
    private ApplicationContext ctx;
    
    @Autowired
    DisplayService displayService;
    
    @Autowired
    ControllerService cs;
    
    @Autowired
    DefinitionService defS;
    
    @Autowired
    SessionService ss;
	
	@Test
	public void controlIssuesCommandsToActiveDisplays() {
		
		// create display
		UI.getCurrent().navigate("memory-vault/sweet-test");
		
		log.info(UI.getCurrent().getUIId() + " is the current UIId");
		
		Optional<MediaDisplayComponent> mdcOpt = displayService.getDisplayComponents().stream().filter(d->d.getDisplayName().equals("sweet-test")).findFirst();
		assertTrue(mdcOpt.isPresent());
		// haven't yet made a controller to listen to
		assertTrue(ss.controllerConsumers.getListeners().size()==0);
		
		String controllerName = "st-cont";
		// create controller
		Controller cont = cs.createNewController(controllerName);
		
		// session
		Definition def = defS.createNewDefinition("ez-def", "just a simple test def");
		SSSession s = ss.createSession("test-sesh-cont", mdcOpt.get().getSortKey(), def.getSortKey(), cont.getSortKey());
		ss.activateSession(s, true);
		
		UI.getCurrent().navigate("memory-vault/sweet-test");
		assertTrue(ss.controllerConsumers.getListeners().size()==1);
		log.info(UI.getCurrent().getUIId() + " is the new UIId");
		
		// an additional navigation should remove the previous listener via a detach event
		UI.getCurrent().navigate("memory-vault/sweet-test");
		MemoryVault memVault = _get(MemoryVault.class);
		log.info(UI.getCurrent().getUIId() + " is the new UIId");
		
		
		assertTrue(ss.controllerConsumers.getListeners().size()==1);
		assertTrue(ss.controllerConsumers.getListeners().get(cont.getSortKey()).size()==1);
		
		// open controller
		
		UI ui = new UI();
		ui.getInternals().setSession(UI.getCurrent().getSession());
		ui.doInit(VaadinRequest.getCurrent(), 2);
		UI.getCurrent().getSession().addUI(ui);
		
		
		log.info("There are now " + UI.getCurrent().getSession().getUIs().size() + " uis.");
		// issue controller command
		UI.setCurrent(ui);
		ControllerUIState cState = ctx.getBean(ControllerUIState.class);
		cState.setActiveController(Optional.of(cont));
		ui.navigate(ControllerOverview.class);
		assertTrue(ss.controllerConsumers.getListeners().get(cont.getSortKey()).size()==1);
		
		ControllerOverview contUI = _get(ControllerOverview.class);
		Button pauseButton = _get(Button.class, spec->spec.withText("Pause"));
		
		_click(pauseButton);
		// expect display state update.
		
		// cleanup
		cs.delete(cont);
		defS.deleteDefinition(def);
		displayService.delete(mdcOpt.get());
		ss.delete(s);
		
		assertTrue(memVault.latestCommand.equals(ControllerCommand.PAUSE));
		
	}
	
	@Test
	public void controllerUpdatesAsDisplayStateChanges() {
		fail();
		
	}
	
	@Test
	public void controllerUpdatesAsSessionChanges() {
		fail();
		
	}
	
}
